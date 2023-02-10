package com.tnco.runar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.analytics.AnalyticsHelper
import com.tnco.runar.model.AffimDescriptionModel
import com.tnco.runar.model.LayoutDescriptionModel
import com.tnco.runar.model.RuneDescriptionModel
import com.tnco.runar.model.UserLayoutModel
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterpretationViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    val analyticsHelper: AnalyticsHelper,
    val sharedPreferencesRepository: SharedPreferencesRepository,
    sharedDataRepository: SharedDataRepository
) : ViewModel() {
    val fontSize: LiveData<Float> = sharedDataRepository.fontSize
    var runesData: List<RuneDescriptionModel> = emptyList()
    var affirmData: List<AffimDescriptionModel> = emptyList()

    private var _selectedRune = SingleLiveEvent<RuneDescriptionModel>()
    private var _singleRune = SingleLiveEvent<String>()
    private var _currentAffirm = SingleLiveEvent<String>()
    private var _currentInterpretation = SingleLiveEvent<String>()
    private var _currentAuspiciousness = SingleLiveEvent<Int>()
    private var _selectedLayout = SingleLiveEvent<LayoutDescriptionModel>()
    private var userLayout = arrayListOf<Int>()

    var currentAuspiciousness: LiveData<Int> = _currentAuspiciousness
    val selectedLayout: LiveData<LayoutDescriptionModel> = _selectedLayout
    var currentAffirm: LiveData<String> = _currentAffirm
    var currentInterpretation: LiveData<String> = _currentInterpretation
    var selectedRune: LiveData<RuneDescriptionModel> = _selectedRune
    var singleRune: LiveData<String> = _singleRune

    var affirmId = 1

    fun getSingleRuneData(id: Int) {
        for (rune in runesData) {
            if (rune.runeId == id) {
                _singleRune.postValue(rune.runeName!!)
            }
        }
    }
    fun getSelectedRuneData(id: Int) {
        val runeId = userLayout[id]
        for (rune in runesData) {
            if (rune.runeId == runeId) {
                _selectedRune.postValue(rune)
            }
        }
    }

    fun getInterpretation() {
        val layoutId = selectedLayout.value?.layoutId
        var result = ""
        when (layoutId) {
            1 -> result = getFullDescriptionForRune(userLayout[1])
            2 -> {
                CoroutineScope(IO).launch {
                    val index = userLayout[1] * 100 + userLayout[2]
                    val inter = databaseRepository.getTwoRunesInterpretation(index)
                    val res = String.format(selectedLayout.value?.interpretation!!, inter)
                    _currentInterpretation.postValue(res)
                }
                return
            }
            3 -> result = String.format(
                selectedLayout.value?.interpretation!!,
                getMeaningForRune(userLayout[1]),
                getMeaningForRune(userLayout[2]),
                getMeaningForRune(userLayout[3])
            )

            4 -> result = String.format(
                selectedLayout.value?.interpretation!!,
                getMeaningForRune(userLayout[1]),
                getMeaningForRune(userLayout[2]),
                getMeaningForRune(userLayout[3]),
                getMeaningForRune(userLayout[4])
            )
            5 -> result = String.format(
                selectedLayout.value?.interpretation!!,
                getMeaningForRune(userLayout[1]),
                getMeaningForRune(userLayout[2]),
                getMeaningForRune(userLayout[4])
            )
            6 -> result = String.format(
                selectedLayout.value?.interpretation!!,
                getMeaningForRune(userLayout[1]),
                getMeaningForRune(userLayout[2]),
                getMeaningForRune(userLayout[3]),
                getMeaningForRune(userLayout[5]),
                getMeaningForRune(userLayout[4])
            )
            7 -> result = String.format(
                selectedLayout.value?.interpretation!!,
                getMeaningForRune(userLayout[2]),
                getMeaningForRune(userLayout[1]),
                getMeaningForRune(userLayout[4]),
                getMeaningForRune(userLayout[3]),
                getMeaningForRune(userLayout[5]),
                getMeaningForRune(userLayout[6])
            )
            8 -> result = String.format(
                selectedLayout.value?.interpretation!!,
                getMeaningForRune(userLayout[1]),
                getMeaningForRune(userLayout[2]),
                getMeaningForRune(userLayout[3]),
                getMeaningForRune(userLayout[4]),
                getMeaningForRune(userLayout[5]),
                getMeaningForRune(userLayout[6]),
                getMeaningForRune(userLayout[7])
            )
        }
        _currentInterpretation.postValue(result)
    }

    fun saveUserLayout() {
        val userId = sharedPreferencesRepository.userId
        val layoutId = selectedLayout.value?.layoutId
        val currentDate = System.currentTimeMillis() / 1000L
        CoroutineScope(IO).launch {
            val userLayout = UserLayoutModel(
                userId,
                currentDate,
                layoutId,
                userLayout[1],
                userLayout[2],
                userLayout[3],
                userLayout[4],
                userLayout[5],
                userLayout[6],
                userLayout[7],
                affirmId
            )
            databaseRepository.addUserLayout(userLayout)
        }
    }

    fun getLuckPercentForCurrentLayout() {
        val layoutId = selectedLayout.value?.layoutId
        var luckPercent = 0
        when (layoutId) {
            1 -> luckPercent = getSumOfAusp(arrayListOf(1))
            2 -> luckPercent = getSumOfAusp(arrayListOf(1, 2)) / 2
            3 -> luckPercent = getSumOfAusp(arrayListOf(3))
            4 -> luckPercent = getSumOfAusp(arrayListOf(3, 4)) / 2
            5 -> luckPercent = getSumOfAusp(arrayListOf(2, 3, 4)) / 3
            6 -> luckPercent = getSumOfAusp(arrayListOf(3, 4, 5)) / 3
            7 -> luckPercent = getSumOfAusp(arrayListOf(3, 5, 6)) / 3
            8 -> luckPercent = getSumOfAusp(arrayListOf(3, 4, 6, 7)) / 4
        }
        _currentAuspiciousness.postValue(luckPercent)
    }

    fun getAffimForCurrentLayout(ausp: Int) {
        while (true) {
            val affirmElement = affirmData.random()
            affirmId = affirmElement.id!! * 100 + ausp
            when (ausp) {
                in 0..20 -> {
                    _currentAffirm.postValue(affirmElement.lvl1!!)
                    return
                }
                in 21..40 -> {
                    _currentAffirm.postValue(affirmElement.lvl2!!)
                    return
                }
                in 41..69 -> {
                    _currentAffirm.postValue(affirmElement.lvl3!!)
                    return
                }
                in 70..100 -> {
                    _currentAffirm.postValue(affirmElement.lvl4!!)
                    return
                }
            }
        }
    }

    private fun getSumOfAusp(ids: ArrayList<Int>): Int {
        var sum = 0
        for (runePos in ids) {
            for (rune in runesData) {
                if (rune.runeId == userLayout[runePos]) {
                    sum += rune.ausp!!
                }
            }
        }
        return sum
    }

    private fun getMeaningForRune(id: Int): String {
        for (rune in runesData) {
            if (rune.runeId == id) {
                return rune.meaning!!.lowercase()
            }
        }
        return ""
    }

    private fun getFullDescriptionForRune(id: Int): String {
        for (rune in runesData) {
            if (rune.runeId == id) {
                return rune.fullDescription!!
            }
        }
        return ""
    }

    fun getRuneDataFromDB() {
        CoroutineScope(IO).launch {
            runesData = databaseRepository.getRunesList()
        }
    }

    fun getAffirmationsDataFromDB() {
        CoroutineScope(IO).launch {
            affirmData = databaseRepository.getAffirmList()
        }
    }

    fun getLayoutDescription(id: Int) {
        CoroutineScope(IO).launch {
            _selectedLayout.postValue(databaseRepository.getLayoutDetails(id))
        }
    }

    fun setCurrentUserLayout(currentLayout: ArrayList<Int>) {
        userLayout = currentLayout
    }
}
