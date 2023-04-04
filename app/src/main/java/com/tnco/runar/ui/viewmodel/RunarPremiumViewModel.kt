package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.model.SkuModel
import com.tnco.runar.repository.SharedDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RunarPremiumViewModel @Inject constructor(
    sharedDataRepository: SharedDataRepository
) : ViewModel() {

    val fontSize: LiveData<Float> = sharedDataRepository.fontSize

    // TODO replace with list of Sku, that take from server
    val listOfSkus = listOf(
        SkuModel(
            id = 0,
            title = "Month",
            description = "\$23.68/year",
            cost = "11.99",
            currencySign = "$"
        ),
        SkuModel(
            id = 1,
            title = "Year",
            description = "\$23.68/year",
            cost = "11.99",
            currencySign = "$",
            discount = "-50%"
        ),
        SkuModel(
            id = 2,
            title = "Eternal",
            description = "pay once",
            cost = "29.99",
            currencySign = "$"
        )
    )
}
