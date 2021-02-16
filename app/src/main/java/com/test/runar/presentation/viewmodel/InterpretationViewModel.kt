package com.test.runar.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.runar.extensions.SingleLiveEvent
import com.test.runar.model.AffimDescriptionModel
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.model.RuneDescriptionModel
import com.test.runar.model.UserLayoutModel
import com.test.runar.repository.DatabaseRepository
import com.test.runar.repository.SharedDataRepository
import com.test.runar.repository.SharedPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class InterpretationViewModel(application: Application) : AndroidViewModel(application) {
    var preferencesRepository = SharedPreferencesRepository.get()
    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    var runesData: List<RuneDescriptionModel> = emptyList()
    var affirmData: List<AffimDescriptionModel> = emptyList()


    private var _selectedRune = SingleLiveEvent<RuneDescriptionModel>()
    private var _currentAffirm = SingleLiveEvent<String>()
    private var _currentInterpretation = SingleLiveEvent<String>()
    private var  _currentAusp = SingleLiveEvent<Int>()
    private var _selectedLayout = SingleLiveEvent<LayoutDescriptionModel>()
    private var userLayout = arrayListOf<Int>()

    var currentAusp : LiveData<Int> = _currentAusp
    val selectedLayout: LiveData<LayoutDescriptionModel> = _selectedLayout
    var currentAffirm : LiveData<String> = _currentAffirm
    var currentInterpretation : LiveData<String> = _currentInterpretation
    var selectedRune : LiveData<RuneDescriptionModel> = _selectedRune


    fun getSelectedRuneData(id: Int) {
        val layoutId = selectedLayout.value?.layoutId
        val userLayout = this.userLayout
        var runeId = 0
        when (layoutId) {
            2 -> {
                when (id) {
                    0 -> {
                        runeId = userLayout[2]
                    }
                    1 -> {
                        runeId = userLayout[6]
                    }
                }
            }
            3 -> {
                when (id) {
                    2 -> {
                        runeId = userLayout[5]
                    }
                    1 -> {
                        runeId = userLayout[2]
                    }
                    0 -> {
                        runeId = userLayout[6]
                    }
                }
            }
            4 -> {
                when (id) {
                    3 -> {
                        runeId = userLayout[1]
                    }
                    1 -> {
                        runeId = userLayout[2]
                    }
                    2 -> {
                        runeId = userLayout[3]
                    }
                    0 -> {
                        runeId = userLayout[6]
                    }
                }
            }
            5 -> {
                when (id) {
                    3 -> {
                        runeId = userLayout[1]
                    }
                    1 -> {
                        runeId = userLayout[5]
                    }
                    2 -> {
                        runeId = userLayout[6]
                    }
                    0 -> {
                        runeId = userLayout[2]
                    }
                }
            }
            6 -> {
                when (id) {
                    3 -> {
                        runeId = userLayout[1]
                    }
                    1 -> {
                        runeId = userLayout[2]
                    }
                    4 -> {
                        runeId = userLayout[3]
                    }
                    0 -> {
                        runeId = userLayout[5]
                    }
                    2 -> {
                        runeId = userLayout[6]
                    }
                }
            }
            7 -> {
                when (id) {
                    5 -> {
                        runeId = userLayout[0]
                    }
                    4 -> {
                        runeId = userLayout[1]
                    }
                    1 -> {
                        runeId = userLayout[2]
                    }
                    3 -> {
                        runeId = userLayout[3]
                    }
                    2 -> {
                        runeId = userLayout[5]
                    }
                    0 -> {
                        runeId = userLayout[6]
                    }
                }
            }
            8 -> {
                when (id) {
                    6 -> {
                        runeId = userLayout[0]
                    }
                    5 -> {
                        runeId = userLayout[1]
                    }
                    0 -> {
                        runeId = userLayout[2]
                    }
                    4 -> {
                        runeId = userLayout[3]
                    }
                    3 -> {
                        runeId = userLayout[4]
                    }
                    2 -> {
                        runeId = userLayout[5]
                    }
                    1 -> {
                        runeId = userLayout[6]
                    }
                }
            }
        }
        for (rune in runesData) {
            if (rune.runeId == runeId) {
                _selectedRune.postValue(rune)
            }
        }
    }
    fun getInterpretation() {
        var layoutId = selectedLayout.value?.layoutId
        var userLayout = this.userLayout
        var result: String = ""
        when (layoutId) {
            1 -> result = getFullDescriptionForRune(userLayout[2]) + "."
            2 -> {
                CoroutineScope(IO).launch {
                    val index = userLayout[2] * 100 + userLayout[6]
                    val inter = DatabaseRepository.getTwoRunesInterpretation(index)
                    _currentInterpretation.postValue("Ваше настоящее положение дел можно охарактеризовать как <bf>$inter</bf>.")
                }
                return
            }
            3 -> result =
                    "Общее положение Ваших дел может быть описано как <bf>${getMeaningForRune(userLayout[6])}</bf>.<br><br>" +
                            "Обратите самое пристальное внимание на важную проблему, а именно - <bf>${
                                getMeaningForRune(
                                        userLayout[2]
                                )
                            }</bf>.<br><br>" +
                            "Возможное решение этой проблемы - это <bf>${
                                getMeaningForRune(
                                        userLayout[5]
                                )
                            }</bf>."
            4 -> result =
                    "Ваше текущее состояние можно описать как <bf>${getMeaningForRune(userLayout[6])}</bf>.<br><br>" +
                            "Ваша проблема - это <bf>${getMeaningForRune(userLayout[2])}</bf>, " +
                            "но для её решения вы должны опираться на <bf>${getMeaningForRune(userLayout[3])}</bf>.<br><br>" +
                            "Если вы будете настойчивы в своих действиях то вас ждет <bf>${
                                getMeaningForRune(
                                        userLayout[1]
                                )
                            }</bf>."
            5 -> result =
                    "В прошлом вы ощущали <bf>${getMeaningForRune(userLayout[2])}</bf>.<br><br>" +
                            "В настоящее время Вы чувствуете <bf>${getMeaningForRune(userLayout[5])}</bf>.<br><br>" +
                            "В будущем вас ожидает <bf>${getMeaningForRune(userLayout[1])}</bf>."
            6 -> result =
                    "В прошлом вы ощущали <bf>${getMeaningForRune(userLayout[5])}</bf>.<br><br>" +
                            "Настоящее (в том числе проблема) может быть описано как <bf>${
                                getMeaningForRune(
                                        userLayout[2]
                                )
                            }</bf>.<br><br>" +
                            "При естественном развитии ситуации, Вас ожидает <bf>${
                                getMeaningForRune(
                                        userLayout[6]
                                )
                            }</bf>, " +
                            "причем есть сила, а именно <bf>${getMeaningForRune(userLayout[3])}</bf>, на которую Вы не можете влиять.<br><br>" +
                            "Но если Судьба принесет вам помощь - <bf>${getMeaningForRune(userLayout[1])}</bf>, то Вас ждет лучшее будущее."
            7 -> result =
                    "Вам, как человеку, присуща важная черта - <bf>${getMeaningForRune(userLayout[2])}</bf>, " +
                            "и в настоящее время с Вами происходит <bf>${getMeaningForRune(userLayout[6])}</bf>.<br><br>" +
                            "Источником Ваших проблем может быть <bf>${getMeaningForRune(userLayout[3])}</bf>.<br><br>" +
                            "Наиболее вероятное будущее может быть описано как <bf>${
                                getMeaningForRune(
                                        userLayout[5]
                                )
                            }</bf>.<br><br>" +
                            "Для достижения этого результата, Ваша главная цель - <bf>${
                                getMeaningForRune(
                                        userLayout[1]
                                )
                            }</bf>.<br><br>" +
                            "Если же Вы не достигнете поставленной цели - вас ждет <bf>${
                                getMeaningForRune(
                                        userLayout[0]
                                )
                            }</bf>."
            8 -> result =
                    "В настоящее время с Вами происходит <bf>${getMeaningForRune(userLayout[2])}</bf>, " +
                            "что является следствием вашего прошлого - <bf>${
                                getMeaningForRune(
                                        userLayout[6]
                                )
                            }</bf>.<br><br>" +
                            "Если в будущем ваша цель <bf>${getMeaningForRune(userLayout[5])}</bf>, " +
                            "вам необходимо обратить внимание на <bf>${getMeaningForRune(userLayout[4])}</bf>.<br><br>" +
                            "Возможно, причиной ваших трудностей является <bf>${
                                getMeaningForRune(
                                        userLayout[3]
                                )
                            }</bf>.<br><br>" +
                            "Лучшее, чего Вы можете ожидать - это <bf>${getMeaningForRune(userLayout[1])}</bf>.<br><br>" +
                            "Как наиболее вероятный результат - вас ждет <bf>${getMeaningForRune(userLayout[0])}</bf>."
            else -> result = "lol dude"
        }
        _currentInterpretation.postValue(result)
    }

    fun saveUserLayout() {
        val userId = preferencesRepository.userId
        val layoutId = selectedLayout.value?.layoutId
        val userLayoutRunes = this.userLayout
        var currentDate = System.currentTimeMillis() / 1000L
        CoroutineScope(IO).launch {
            var userLayout = UserLayoutModel(
                    userId,
                    currentDate,
                    layoutId,
                    userLayoutRunes[0],
                    userLayoutRunes[1],
                    userLayoutRunes[2],
                    userLayoutRunes[3],
                    userLayoutRunes[4],
                    userLayoutRunes[5],
                    userLayoutRunes[6]
            )
            DatabaseRepository.addUserLayout(userLayout)
        }
    }

    fun getAuspForCurrentLayout() {
        val layoutId = selectedLayout.value?.layoutId
        var ausp: Int = 0
        val userLayout = this.userLayout
        when (layoutId) {
            1 -> ausp = getAuspForRune(userLayout[2])
            2 -> ausp = (getAuspForRune(userLayout[2]) + getAuspForRune(userLayout[6])) / 2
            3 -> ausp = getAuspForRune(userLayout[5])
            4 -> ausp = (getAuspForRune(userLayout[1]) + getAuspForRune(userLayout[3])) / 2
            5 -> ausp =
                    (getAuspForRune(userLayout[5]) + getAuspForRune(userLayout[1]) + getAuspForRune(
                            userLayout[6]
                    )) / 3
            6 -> ausp =
                    (getAuspForRune(userLayout[3]) + getAuspForRune(userLayout[1]) + getAuspForRune(
                            userLayout[6]
                    )) / 3
            7 -> ausp =
                    (getAuspForRune(userLayout[0]) + getAuspForRune(userLayout[1]) + getAuspForRune(
                            userLayout[5]
                    )) / 3
            8 -> ausp =
                    (getAuspForRune(userLayout[0]) + getAuspForRune(userLayout[1]) + getAuspForRune(
                            userLayout[5]
                    ) + getAuspForRune(userLayout[4])) / 4
        }
        _currentAusp.postValue(ausp)
    }

    fun getAffimForCurrentLayout(ausp: Int) {
        while (true) {
            val affirmElement = affirmData.random()
            when (ausp) {
                in 0..19 -> {
                    _currentAffirm.postValue(affirmElement.lvl1)
                    return
                }
                in 20..29 -> {
                    _currentAffirm.postValue(affirmElement.lvl2)
                    return
                }
                in 30..39 -> {
                    _currentAffirm.postValue(affirmElement.lvl3)
                    return
                }
                in 40..50 -> {
                    if (affirmElement.lvl4 != null || affirmElement.lvl4 != "") {
                        _currentAffirm.postValue(affirmElement.lvl4)
                        return
                    }
                }
            }
        }
    }

    fun getAuspForRune(id: Int): Int {
        for (rune in runesData) {
            if (rune.runeId == id) {
                return rune.ausp!!
            }
        }
        return 0
    }

    private fun getMeaningForRune(id: Int): String {
        for (rune in runesData) {
            if (rune.runeId == id) {
                return rune.meaning!!
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
            runesData = DatabaseRepository.getRunesList()
        }
    }

    fun getAffirmationsDataFromDB() {
        CoroutineScope(IO).launch {
            affirmData = DatabaseRepository.getAffirmList()
        }
    }

    fun getLayoutDescription(id: Int) {
        CoroutineScope(IO).launch {
            _selectedLayout.postValue(DatabaseRepository.getLayoutDetails(id))
        }
    }

    fun setCurrentUserLayout(currentLayout: ArrayList<Int>) {
        userLayout = currentLayout
    }
}