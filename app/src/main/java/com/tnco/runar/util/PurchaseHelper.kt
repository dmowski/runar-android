package com.tnco.runar.util

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.*
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PurchaseHelper(val activity: Activity) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private lateinit var billingClient: BillingClient
    private lateinit var purchase: Purchase

    private val productMonthlyId = "runar_premium.forever"
    private val productYearlyId = "runar_yearly"

    private val _products = MutableStateFlow(listOf<ProductDetails>())
    val products = _products.asStateFlow()

    private val _buyEnabled = MutableStateFlow(false)
    val buyEnabled = _buyEnabled.asStateFlow()

    private val _consumeEnabled = MutableStateFlow(false)
    val consumeEnabled = _consumeEnabled.asStateFlow()

    private val _statusText = MutableStateFlow("Initializing...")
    val statusText = _statusText.asStateFlow()

    private val purchasesUpdatedListener: PurchasesUpdatedListener

    init {
        purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    completePurchase(purchase)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                _statusText.value = "Purchase Canceled"
            } else {
                _statusText.value = "Purchase Error"
            }
        }

        billingClient = BillingClient.newBuilder(activity).setListener(purchasesUpdatedListener)
            .enablePendingPurchases().build()
    }

    fun billingSetup() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(
                billingResult: BillingResult
            ) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    _statusText.value = "Billing Client Connected"
                    queryProduct(productMonthlyId, productYearlyId)
                    Log.d("TAG_PURCHASE", "onBillingSetupFinished: Ok!")
                } else {
                    Log.d("TAG_PURCHASE", "onBillingSetupFinished: Connection Failure!")
                    _statusText.value = "Billing Client Connection Failure"
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d("TAG_PURCHASE", "onBillingServiceDisconnected: Connection Lost!")
                _statusText.value = "Billing Client Connection Lost"
            }
        })
    }

    fun queryProduct(productWeekLyId: String, productYearLyId: String) {
        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder().setProductList(
            ImmutableList.of(
                QueryProductDetailsParams.Product.newBuilder().setProductId(productWeekLyId)
                    .setProductType(
                        BillingClient.ProductType.SUBS
                    ).build(),
                QueryProductDetailsParams.Product.newBuilder().setProductId(productYearLyId)
                    .setProductType(
                        BillingClient.ProductType.SUBS
                    ).build(),
            )
        ).build()

        billingClient.queryProductDetailsAsync(
            queryProductDetailsParams
        ) { billingResult, productDetailsList ->
            Log.d("TAG_PURCHASE", "queryProduct: Result - $billingResult")

            // TODO: Handle - FEATURE_NOT_SUPPORTED

            if (productDetailsList.isNotEmpty()) {
                Log.d("TAG_PURCHASE", "queryProduct: ok! - Products = $productDetailsList")
                _products.value = productDetailsList
                _buyEnabled.value = true
            } else {
                Log.d("TAG_PURCHASE", "queryProduct: No Matching Products Found!")
                _statusText.value = "No Matching Products Found"
                _buyEnabled.value = false
            }
        }
    }

    private fun completePurchase(item: Purchase) {
        purchase = item
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            _buyEnabled.value = false
            _consumeEnabled.value = true
            _statusText.value = "Purchase Completed"
        }
    }

    fun makePurchase(productDetails: ProductDetails) {
        Log.d("TAG_PURCHASE", "makePurchase: ProductDetails  = $productDetails")
        val billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(
                        productDetails.subscriptionOfferDetails?.get(0)?.offerToken ?: ""
                    )
                    .build()
            )
        ).build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    fun consumePurchase() {
        val consumeParams =
            ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()

        coroutineScope.launch {
            val result = billingClient.consumePurchase(consumeParams)

            if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                _statusText.value = "Purchase Consumed"
                _buyEnabled.value = true
                _consumeEnabled.value = false
            }
        }
    }

    fun reloadPurchase() {
        val queryPurchasesParams =
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build()

        billingClient.queryPurchasesAsync(
            queryPurchasesParams, purchasesListener
        )
    }

    private val purchasesListener = PurchasesResponseListener { billingResult, purchases ->
        for (prch in purchases) {
            if (prch.purchaseState == Purchase.PurchaseState.PURCHASED) {
                purchase = prch
                _buyEnabled.value = false
                _consumeEnabled.value = true
                _statusText.value = "Previous Purchase Found"
                return@PurchasesResponseListener
            }
        }

        _buyEnabled.value = true
        _consumeEnabled.value = false

        if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            _statusText.value = "Purchase Canceled"
        } else {
            _statusText.value = "Purchase Error"
            Log.i("InAppPurchase", billingResult.getDebugMessage())
        }
    }
}
