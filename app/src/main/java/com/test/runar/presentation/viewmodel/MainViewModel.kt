package com.test.runar.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.test.runar.model.LayoutDescriptionModel
import com.test.runar.model.RuneDescriptionModel
import com.test.runar.repository.DatabaseRepository
import com.test.runar.repository.SharedPreferencesRepository
import com.test.runar.retrofit.RetrofitClient
import com.test.runar.retrofit.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var selectedLayout = MutableLiveData<LayoutDescriptionModel>(null)
    var currentUserLayout = MutableLiveData<Array<Int>>(null)
    var showStatus = MutableLiveData(3)
    var preferencesRepository = SharedPreferencesRepository(application)
    var runeHeight = MutableLiveData(0)
    var currentAusp = MutableLiveData(0)
    var runesData : List<RuneDescriptionModel> = emptyList()

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

    fun getAuspForCurrentLayout(){
        var layoutId = selectedLayout.value?.layoutId
        var ausp = currentUserLayout.value?.get(2)?.let { getAuspForRune(it) }
        currentAusp.postValue(ausp)
    }
    fun clearAusp(){
        currentAusp.postValue(null)
    }

    fun getAuspForRune(id: Int): Int? {
        for(rune in runesData){
            if(rune.runeId==id){
                return rune.ausp
            }
        }
        return null
    }

    fun getRuneDataFromDB(context: Context){
        CoroutineScope(IO).launch {
            runesData = DatabaseRepository.getRunesList(context)
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

    fun setRuneHeight(height: Int){
        if(height!=runeHeight.value){
            runeHeight.postValue(height)
        }
    }

    fun clearShowStatus() {
        showStatus.postValue(3)
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