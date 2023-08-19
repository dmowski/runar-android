package com.tnco.runar.util

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.google.common.collect.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PurchaseHelper(val activity: Activity) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val billingClient: BillingClient
    private lateinit var purchase: Purchase

    private val productMonthlyId = "runar_premium.forever"
    private val productYearlyId = "runar_yearly"

    private val _products = MutableStateFlow(listOf<ProductDetails>())
    val products = _products.asStateFlow()

    private val _buyEnabled = MutableStateFlow(false)
    val buyEnabled = _buyEnabled.asStateFlow()

    private val _consumeEnabled = MutableStateFlow(false)
    val consumeEnabled = _consumeEnabled.asStateFlow()

    private val _statusText = MutableStateFlow("")
    val statusText = _statusText.asStateFlow()

    private val purchasesUpdatedListener: PurchasesUpdatedListener

    init {
        purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    // if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    // acknowledgeIfNecessary(purchase)
                    // }
                    completePurchase(purchase)
                }
            } else if (billingResult.responseCode == BillingResponseCode.USER_CANCELED) {
                _statusText.value = "Purchase Canceled"
            } else {
                _statusText.value = "Purchase Error"
            }
        }

        billingClient = BillingClient.newBuilder(activity).setListener(purchasesUpdatedListener)
            .enablePendingPurchases().build()
    }

    /*
    private fun acknowledgeIfNecessary(purchase: Purchase) {
        if (!purchase.isAcknowledged) {
            val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken).build()

            billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                val billingResponseCode = billingResult.responseCode
                val billingDebugMessage = billingResult.debugMessage

                Log.v("TAG_INAPP", "response code: $billingResponseCode")
                Log.v("TAG_INAPP", "debugMessage : $billingDebugMessage")
            }
        }
    }*/

    fun billingSetup() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(
                billingResult: BillingResult
            ) {
                when (billingResult.responseCode) {
                    BillingResponseCode.OK -> {
                        reloadPurchase()
                        queryProduct(productMonthlyId, productYearlyId)
                        Log.d("TAG_PURCHASE", "onBillingSetupFinished: Ok!")
                    }
                    in setOf(
                        BillingResponseCode.BILLING_UNAVAILABLE,
                        BillingResponseCode.DEVELOPER_ERROR,
                        BillingResponseCode.FEATURE_NOT_SUPPORTED,
                    ) -> {
                        _statusText.value =
                            "Billing Server Unavailable. Please check Play Store updates and try again!"
                        _buyEnabled.value = false
                    }
                    /*
                    BillingResponseCode.ITEM_NOT_OWNED
                    BillingResponseCode.ERROR,
                    BillingResponseCode.SERVICE_DISCONNECTED,
                    BillingResponseCode.SERVICE_UNAVAILABLE*/
                    else -> {
                        _statusText.value = "Something went wrong. Please Try again!"
                        _buyEnabled.value = false
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d("TAG_PURCHASE", "onBillingServiceDisconnected: Connection Lost!")
                _statusText.value = "Billing Client Connection Lost"
            }
        })
    }

    private fun queryProduct(productWeekLyId: String, productYearLyId: String) {
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

            when (billingResult.responseCode) {
                BillingResponseCode.OK -> {
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
                in setOf(
                    BillingResponseCode.BILLING_UNAVAILABLE,
                    BillingResponseCode.DEVELOPER_ERROR,
                    BillingResponseCode.FEATURE_NOT_SUPPORTED,
                ) -> {
                    _statusText.value =
                        "Billing Unavailable. Please check Play Store updates and try again!"
                    _buyEnabled.value = false
                }
                /*
                BillingResponseCode.ITEM_NOT_OWNED
                BillingResponseCode.ERROR,
                BillingResponseCode.SERVICE_DISCONNECTED,
                BillingResponseCode.SERVICE_UNAVAILABLE*/
                else -> {
                    _statusText.value = "Something going wrong. Please Try again!"
                    _buyEnabled.value = false
                }
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

            if (result.billingResult.responseCode == BillingResponseCode.OK) {
                _statusText.value = "Purchase Consumed"
                _buyEnabled.value = true
                _consumeEnabled.value = false
            }
        }
    }

    fun reloadPurchase() {
        val queryPurchasesParams =
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build()

        if (billingClient.isReady) {
            billingClient.queryPurchasesAsync(
                queryPurchasesParams, purchasesListener
            )
        } else
            Log.d("TAG_BILLING_TEST", "reloadPurchase: is Not Ready!")
    }

    private val purchasesListener = PurchasesResponseListener { billingResult, purchases ->
        Log.i("TAG_BILLING_TEST", "Purchaes found: ${purchases.size}")
        _buyEnabled.value = true
        _consumeEnabled.value = false

        when (billingResult.responseCode) {
            BillingResponseCode.OK ->
                for (purchase in purchases)
                    completePurchase(purchase)
            BillingResponseCode.USER_CANCELED ->
                _statusText.value = "Purchase Canceled"
            else ->
                _statusText.value = "Purchase Error"
        }
    }
}
