package com.tnco.runar.ui.viewmodel

import android.graphics.Bitmap
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tnco.runar.data.remote.UserInfo
import com.tnco.runar.repository.backend.BackendRepository
import android.util.Log
import androidx.lifecycle.*
import com.tnco.runar.data.remote.NetworkResult
import com.tnco.runar.data.remote.RunesResponse
import com.tnco.runar.model.RunesItemsModel
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.repository.backend.DataClassConverters
import com.tnco.runar.retrofit.BackgroundInfo
import com.tnco.runar.util.NetworkMonitor
import kotlinx.coroutines.*
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val networkMonitor = NetworkMonitor.get()
    val isNetworkAvailable = networkMonitor.isConnected.asLiveData()

    var preferencesRepository = SharedPreferencesRepository.get()

    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    val backgroundInfo = MutableLiveData(mutableListOf<BackgroundInfo>())
//    val selectedIndices = mutableListOf<Int>()

    var readRunes: LiveData<List<RunesItemsModel>> = DatabaseRepository.getRunesGenerator().asLiveData()
    val runesResponse = MutableLiveData<NetworkResult<List<RunesItemsModel>>>()

    val runePattern = mutableListOf<String>()
    val runesImages = mutableListOf<Bitmap>()
    var selectedRuneIndex = 0

    var shareURL = ""

    fun getRunes() = viewModelScope.launch(Dispatchers.IO) {
        runesResponse.postValue(NetworkResult.Loading())
        try {
            val response = BackendRepository.getRunes()
            runesResponse.postValue(handleRunesResponse(response))
        } catch (e: Exception) {
            runesResponse.postValue(NetworkResult.Error(e.toString()))
        }
    }

    var runesSelected: String = ""
    val runeImagesReady = MutableLiveData<Boolean>(false)

    fun identify() {
        val userId = preferencesRepository.userId
        val timeStamp = System.currentTimeMillis() / 1000L
        val androidVersion = "Android " + Build.VERSION.RELEASE
        BackendRepository.identify(UserInfo(userId,timeStamp,androidVersion))
    }

    fun getBackgroundInfo() = viewModelScope.launch(Dispatchers.IO) {
            backgroundInfo.value?.clear()
            val info = BackendRepository.getBackgroundInfo()
            for (i in info.indices){
                val bmp = BackendRepository.getBackgroundImage(
                    runesSelected,
                    runePattern[selectedRuneIndex],
                    info[i].name,
                    720,
                    1280
                )
                info[i].img = bmp
                backgroundInfo.postValue(info.toMutableList())
            }
        }

    fun getRunePattern() = viewModelScope.launch(Dispatchers.IO) {
            runePattern.clear()
            runesImages.clear()
            backgroundInfo.value?.clear()

            val newPatterns = BackendRepository.getRunePattern(runesSelected)
            for (p in newPatterns){
                val runeNumber = p.substringAfterLast('/')
                runePattern.add(runeNumber)
            }
            Log.d("!!! runePattern",runePattern.toString())
            for (imgPath in runePattern){
                val image = BackendRepository.getRuneImage(runesSelected,imgPath)
                if (image != null) {
                    runesImages.add(image)
                }
            }
            runeImagesReady.postValue(true)
            Log.d("!!! runesImages",runesImages.toString())
        }

    fun cancelChildrenCoroutines() = viewModelScope.coroutineContext.cancelChildren()

    private fun handleRunesResponse(
        response: Response<List<RunesResponse>>
    ): NetworkResult<List<RunesItemsModel>> {
        return when {
            response.isSuccessful -> {
                val convertedResult =
                    DataClassConverters.runesRespToItems(response.body()!!)
                DatabaseRepository.updateRunesGeneratorDB(convertedResult)
                NetworkResult.Success(convertedResult)
            }
            else -> NetworkResult.Error(response.errorBody().toString())
        }
    }
}