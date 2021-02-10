package com.test.runar.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.test.runar.model.AffimDescriptionModel
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.model.RuneDescriptionModel
import com.test.runar.model.UserLayoutModel
import com.test.runar.repository.DatabaseRepository
import com.test.runar.repository.SharedPreferencesRepository
import com.test.runar.retrofit.RetrofitClient
import com.test.runar.retrofit.UserInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.HttpException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var selectedLayout = MutableLiveData<LayoutDescriptionModel>(null)
    var currentUserLayout = MutableLiveData<Array<Int>>(null)
    var showStatus = MutableLiveData(3)
    var preferencesRepository = SharedPreferencesRepository(application)
    var currentAusp = MutableLiveData(0)
    var currentAffirm = MutableLiveData("")
    var runesData : List<RuneDescriptionModel> = emptyList()
    var affirmData : List<AffimDescriptionModel> = emptyList()
    var currentInterpretation = MutableLiveData("")
    var lastUserLayoutId = MutableLiveData<Int>(null)
    var selectedRune = MutableLiveData<RuneDescriptionModel>(null)
    var fontSize = MutableLiveData<Float>(null)
    var backButtonPressed = MutableLiveData<Boolean>(false)
    var readyToDialog = MutableLiveData(true)


    var layoutInterpretationData: LiveData<Pair<LayoutDescriptionModel,Array<Int>>> = object : MediatorLiveData<Pair<LayoutDescriptionModel,Array<Int>>>(){
        var currentLayout: LayoutDescriptionModel? = null
        var userLayout: Array<Int>? = null
        init{
            addSource(selectedLayout){
                currentLayout ->
                this.currentLayout = currentLayout
                userLayout?.let { value = currentLayout to it }
            }
            addSource(currentUserLayout){
                userLayout ->
                this.userLayout = userLayout
                currentLayout?.let { value = it to userLayout }
            }
        }
    }

    fun setDialogReady(state: Boolean){
        readyToDialog.postValue(state)
    }
    fun pressBackButton(state: Boolean){
        backButtonPressed.postValue(state)
    }

    fun setFontSize(size: Float){
        fontSize.postValue(size)
    }

    fun getSelectedRuneData(id: Int){
        var layoutId = selectedLayout.value?.layoutId
        var userLayout = currentUserLayout.value!!
        var runeId =0
        when(layoutId){
            2->{
                when(id){
                    0->{runeId = userLayout[2]}
                    1->{runeId = userLayout[6]}
                }
            }
            3->{
                when(id){
                    2->{runeId = userLayout[5]}
                    1->{runeId = userLayout[2]}
                    0->{runeId = userLayout[6]}
                }
            }
            4->{
                when(id){
                    3->{runeId = userLayout[1]}
                    1->{runeId = userLayout[2]}
                    2->{runeId = userLayout[3]}
                    0->{runeId = userLayout[6]}
                }
            }
            5->{
                when(id){
                    3->{runeId = userLayout[1]}
                    1->{runeId = userLayout[5]}
                    2->{runeId = userLayout[6]}
                    0->{runeId = userLayout[2]}
                }
            }
            6->{
                when(id){
                    3->{runeId = userLayout[1]}
                    1->{runeId = userLayout[2]}
                    4->{runeId = userLayout[3]}
                    0->{runeId = userLayout[5]}
                    2->{runeId = userLayout[6]}
                }
            }
            7->{
                when(id){
                    5->{runeId = userLayout[0]}
                    4->{runeId = userLayout[1]}
                    1->{runeId = userLayout[2]}
                    3->{runeId = userLayout[3]}
                    2->{runeId = userLayout[5]}
                    0->{runeId = userLayout[6]}
                }
            }
            8->{
                when(id){
                    6->{runeId = userLayout[0]}
                    5->{runeId = userLayout[1]}
                    0->{runeId = userLayout[2]}
                    4->{runeId = userLayout[3]}
                    3->{runeId = userLayout[4]}
                    2->{runeId = userLayout[5]}
                    1->{runeId = userLayout[6]}
                }
            }
        }
        for(rune in runesData){
            if(rune.runeId==runeId){
                selectedRune.postValue(rune)
            }
        }
    }

    fun setLastUserLayout(id: Int){
        lastUserLayoutId.value = id
    }

    fun getInterpretation(context: Context){
        var layoutId = selectedLayout.value?.layoutId
        var userLayout = currentUserLayout.value!!
        var result : String =""
        when(layoutId){
            1-> result = getFullDescriptionForRune(userLayout[2])+"."
            2-> {
                CoroutineScope(IO).launch {
                    val index =userLayout[2]*100+userLayout[6]
                    val inter = DatabaseRepository.getTwoRunesInterpretation(context,index)
                    currentInterpretation.postValue("Ваше настоящее положение дел можно охарактеризовать как <bf>$inter</bf>.")
                }
                return
            }
            3->result = "Общее положение Ваших дел может быть описано как <bf>${getMeaningForRune(userLayout[6])}</bf>.<br><br>" +
                    "Самое пристальное внимание обратите на проблему с вашим <bf>${getMeaningForRune(userLayout[2])}</bf>.<br><br>" +
                    "Для решения данной проблемы, определитесь с <bf>${getMeaningForRune(userLayout[5])}</bf>."
            4->result = "Ваше текущее состояние можно описать как <bf>${getMeaningForRune(userLayout[6])}</bf>.<br><br>" +
                    "Ваша проблема - это <bf>${getMeaningForRune(userLayout[2])}</bf>, " +
                    "но для её решения вы должны опираться на <bf>${getMeaningForRune(userLayout[3])}</bf>.<br><br>" +
                    "Если вы будете настойчивы в своих действиях то вас ждет <bf>${getMeaningForRune(userLayout[1])}</bf>."
            5->result = "В прошлом вы ощущали <bf>${getMeaningForRune(userLayout[2])}</bf>.<br><br>" +
                    "В настоящее время Вы чувствуете <bf>${getMeaningForRune(userLayout[5])}</bf>.<br><br>" +
                    "В будущем вас ожидает <bf>${getMeaningForRune(userLayout[1])}</bf>."
            6->result = "В прошлом вы ощущали <bf>${getMeaningForRune(userLayout[5])}</bf>.<br><br>" +
                    "Настоящее (в том числе проблема) может быть описано как <bf>${getMeaningForRune(userLayout[2])}</bf>.<br><br>" +
                    "При естественном развитии ситуации, Вас ожидает <bf>${getMeaningForRune(userLayout[6])}</bf>, " +
                    "причем есть сила, а именно <bf>${getMeaningForRune(userLayout[3])}</bf>, на которую Вы не можете влиять.<br><br>" +
                    "Но если Судьба принесет вам помощь - <bf>${getMeaningForRune(userLayout[1])}</bf>, то Вас ждет лучшее будущее."
            7->result = "Вам, как человеку, присуща важная черта - <bf>${getMeaningForRune(userLayout[2])}</bf>, " +
                    "и в настоящее время с Вами происходит <bf>${getMeaningForRune(userLayout[6])}</bf>.<br><br>" +
                    "Источником Ваших проблем может быть <bf>${getMeaningForRune(userLayout[3])}</bf>.<br><br>" +
                    "Наиболее вероятное будущее может быть описано как <bf>${getMeaningForRune(userLayout[5])}</bf>.<br><br>" +
                    "Для достижения этого результата, Ваша главная цель - <bf>${getMeaningForRune(userLayout[1])}</bf>.<br><br>" +
                    "Если же Вы не достигнете поставленной цели - вас ждет <bf>${getMeaningForRune(userLayout[0])}</bf>."
            8->result ="В настоящее время с Вами происходит <bf>${getMeaningForRune(userLayout[2])}</bf>, " +
                    "что является следствием вашего прошлого - <bf>${getMeaningForRune(userLayout[6])}</bf>.<br><br>" +
                    "Чтобы достичь <bf>${getMeaningForRune(userLayout[5])}</bf> в будущем, " +
                    "вам необходимо обратить внимание на <bf>${getMeaningForRune(userLayout[4])}</bf>.<br><br>" +
                    "Возможно, причиной ваших трудностей является <bf>${getMeaningForRune(userLayout[3])}</bf>.<br><br>" +
                    "Лучшее, чего Вы можете ожидать - это <bf>${getMeaningForRune(userLayout[1])}</bf>.<br><br>" +
                    "В результате, вас ждет <bf>${getMeaningForRune(userLayout[0])}</bf>."
            else ->result = "lol dude"
        }
        currentInterpretation.postValue(result)
    }

    fun saveUserLayout(context: Context){
        var userId = preferencesRepository.getUserId()
        var layoutId = selectedLayout.value?.layoutId
        var userLayoutRunes = currentUserLayout.value!!
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
            DatabaseRepository.addUserLayout(context,userLayout)
        }
    }

    fun getAuspForCurrentLayout(){
        var layoutId = selectedLayout.value?.layoutId
        var ausp : Int = 0
        var userLayout = currentUserLayout.value!!
        when(layoutId){
            1-> ausp = getAuspForRune(userLayout[2])
            2-> ausp = (getAuspForRune(userLayout[2])+getAuspForRune(userLayout[6]))/2
            3-> ausp = getAuspForRune(userLayout[5])
            4-> ausp = (getAuspForRune(userLayout[1])+getAuspForRune(userLayout[3]))/2
            5-> ausp = (getAuspForRune(userLayout[5])+getAuspForRune(userLayout[1])+getAuspForRune(userLayout[6]))/3
            6-> ausp = (getAuspForRune(userLayout[3])+getAuspForRune(userLayout[1])+getAuspForRune(userLayout[6]))/3
            7-> ausp = (getAuspForRune(userLayout[0])+getAuspForRune(userLayout[1])+getAuspForRune(userLayout[5]))/3
            8-> ausp = (getAuspForRune(userLayout[0])+getAuspForRune(userLayout[1])+getAuspForRune(userLayout[5])+getAuspForRune(userLayout[4]))/4
        }
        currentAusp.postValue(ausp)
    }
    fun getAffimForCurrentLayout(ausp: Int){
        while(true){
            var affirmElement = affirmData.random()
            when(ausp){
                in 0..19->{
                    currentAffirm.postValue(affirmElement.lvl1)
                    return
                }
                in 20..29->{
                    currentAffirm.postValue(affirmElement.lvl2)
                    return
                }
                in 30..39->{
                    currentAffirm.postValue(affirmElement.lvl3)
                    return
                }
                in 40..50->{
                    if(affirmElement.lvl4!=null||affirmElement.lvl4!=""){
                        currentAffirm.postValue(affirmElement.lvl4)
                        return
                    }
                }
            }
        }
    }
    fun clearAffirm(){
        currentAffirm.postValue("")
    }
    fun clearAusp(){
        currentAusp.postValue(null)
    }

    fun clearInterpretation(){
        currentInterpretation.postValue(null)
    }

    fun getAuspForRune(id: Int): Int {
        for(rune in runesData){
            if(rune.runeId==id){
                return rune.ausp!!
            }
        }
        return 0
    }

    fun getMeaningForRune(id: Int): String {
        for(rune in runesData){
            if(rune.runeId==id){
                return rune.meaning!!
            }
        }
        return ""
    }

    fun getFullDescriptionForRune(id: Int): String {
        for(rune in runesData){
            if(rune.runeId==id){
                return rune.fullDescription!!
            }
        }
        return ""
    }

    fun getRuneDataFromDB(context: Context){
        CoroutineScope(IO).launch {
            runesData = DatabaseRepository.getRunesList(context)
        }
    }

    fun getAffirmationsDataFromDB(context: Context){
        CoroutineScope(IO).launch {
            affirmData = DatabaseRepository.getAffirmList(context)
        }
    }


    fun notShowSelectedLayout(context: Context, id: Int) {
        CoroutineScope(IO).launch {
            DatabaseRepository.notShow(context, id)
        }
    }

    fun getLayoutDescription(context: Context, id: Int) {
        CoroutineScope(IO).launch {
            selectedLayout.postValue(DatabaseRepository.getLayoutDetails(context, id))
        }
    }

    fun clearLayoutData() {
        selectedLayout.postValue(null)
    }

    fun setCurrentUserLayout(currentLayout: Array<Int>){
        currentUserLayout.postValue(currentLayout)
    }

    fun clearUserLayoutData(){
        currentUserLayout.postValue(null)
    }

    fun descriptionCheck(context: Context, id: Int) {
        CoroutineScope(IO).launch {
            showStatus.postValue(DatabaseRepository.getShowStatus(context, id))
        }
    }

    fun clearShowStatus() {
        showStatus.postValue(3)
    }

    fun closeDB(){
        CoroutineScope(IO).launch {
            DatabaseRepository.closeDB()
        }
    }

    fun identify() {
        var userId = preferencesRepository.getUserId()
        var timeStamp = System.currentTimeMillis() / 1000L
        var androidVersion = "Android " + Build.VERSION.RELEASE
        Log.d("Log", androidVersion)
        CoroutineScope(IO).launch {
            try {
                val response = RetrofitClient.apiInterface.createUser(UserInfo(userId, timeStamp, androidVersion))
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Log.d("Log", response.message().toString())
                    } else {
                        Log.d("Log", response.code().toString())
                    }
                }
            } catch (e: HttpException) {
                Log.d("Log", "Http error ")
            } catch (e: Throwable) {
                Log.d("Log", "Some Error ")
            }
        }
    }
}