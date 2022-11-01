package com.tnco.runar.ui.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.tnco.runar.data.remote.NetworkResult
import com.tnco.runar.data.remote.RunesResponse
import com.tnco.runar.data.remote.UserInfo
import com.tnco.runar.model.RunesItemsModel
import com.tnco.runar.repository.DatabaseRepository
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.repository.SharedPreferencesRepository
import com.tnco.runar.repository.backend.BackendRepository
import com.tnco.runar.repository.backend.DataClassConverters
import com.tnco.runar.retrofit.BackgroundInfo
import com.tnco.runar.util.NetworkMonitor
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val networkMonitor = NetworkMonitor.get()
    val isNetworkAvailable = networkMonitor.isConnected.asLiveData()

    var preferencesRepository = SharedPreferencesRepository.get()

    val fontSize: LiveData<Float> = MutableLiveData(SharedDataRepository.fontSize)
    var backgroundInfo = mutableListOf<BackgroundInfo>()
    val backgroundInfoResponse = MutableLiveData<NetworkResult<List<BackgroundInfo>>>()
//    val selectedIndices = mutableListOf<Int>()

    var readRunes: LiveData<List<RunesItemsModel>> = DatabaseRepository.getRunesGenerator().asLiveData()
    val runesResponse = MutableLiveData<NetworkResult<List<RunesItemsModel>>>()

    val runePattern = mutableListOf<String>()
    val runesImages = mutableListOf<Bitmap>()
    val runesImagesResponse = MutableLiveData<NetworkResult<List<Bitmap>>>()
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

    fun identify() {
        val userId = preferencesRepository.userId
        val timeStamp = System.currentTimeMillis() / 1000L
        val androidVersion = "Android " + Build.VERSION.RELEASE
        BackendRepository.identify(UserInfo(userId,timeStamp,androidVersion))
    }

    fun getBackgroundInfo() = viewModelScope.launch(Dispatchers.IO) {
        backgroundInfo.clear()

        try {
            val response = BackendRepository.getBackgroundInfo()
            handleBackgroundInfoResponse(response)
        } catch (e: Exception) {
            getBackgroundImages()
        }
    }

    fun getBackgroundImages() = viewModelScope.launch(Dispatchers.IO) {
        if (backgroundInfo.isEmpty()) {
            backgroundInfoResponse.postValue(NetworkResult.Error(""))
        }

        for (index in backgroundInfo.indices) {
            try {
                val response = BackendRepository.getBackgroundImage(
                    runesSelected,
                    runePattern[selectedRuneIndex],
                    backgroundInfo[index].name,
                    720,
                    1280
                )
                backgroundInfoResponse.postValue(handleBackgroundImageResponse(index, response))
            } catch (e: Exception) {
                backgroundInfoResponse.postValue(NetworkResult.Error(e.toString()))
            }
        }
    }

    fun getRunePattern() = viewModelScope.launch(Dispatchers.IO) {
        runePattern.clear()

        try {
            val response = BackendRepository.getRunePattern(runesSelected)
            handleRunePatternResponse(response)
        } catch (e: Exception) {
            getRuneImages()
        }
    }

    fun getRuneImages() = viewModelScope.launch(Dispatchers.IO) {
        runesImages.clear()
        runesImagesResponse.postValue(NetworkResult.Loading())

        if (runePattern.isEmpty()) {
            runesImagesResponse.postValue(NetworkResult.Error(""))
        }

        for (imgPath in runePattern) {
            try {
                val response = BackendRepository.getRuneImage(runesSelected, imgPath)
                runesImagesResponse.postValue(handleRuneImagesResponse(response))
            } catch (e: Exception) {
                runesImagesResponse.postValue(NetworkResult.Error(e.toString()))
            }
        }
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

    private fun handleBackgroundInfoResponse(
        response: Response<List<BackgroundInfo>>
    ) {
        if (response.isSuccessful) {
            backgroundInfo = response.body()!!.toMutableList()
            Log.d("MainViewModel", "BackgroundInfo: $backgroundInfo")
        }
        getBackgroundImages()
    }

    private fun handleBackgroundImageResponse(
        index: Int,
        response: Response<ResponseBody>
    ): NetworkResult<List<BackgroundInfo>> {
        return when {
            response.isSuccessful -> {
                val conf = Bitmap.Config.ARGB_8888
                val opt = BitmapFactory.Options()
                opt.inPreferredConfig = conf
                val image = BitmapFactory.decodeStream(response.body()!!.byteStream(),null,opt)
                backgroundInfo[index].img = image
                Log.d("MainViewModel", "BackgroundImage: $image")

                NetworkResult.Success(backgroundInfo)
            }
            else -> NetworkResult.Error(response.errorBody().toString())
        }
    }

    private fun handleRunePatternResponse(
        response: Response<List<String>>
    ) {
        if (response.isSuccessful) {
            val newPatterns = response.body()!!
            for (p in newPatterns) {
                val runeNumber = p.substringAfterLast('/')
                runePattern.add(runeNumber)
                Log.d("MainViewModel", "runePattern: $p")
            }
        }
        getRuneImages()
    }

    private fun handleRuneImagesResponse(
        response: Response<ResponseBody>
    ): NetworkResult<List<Bitmap>> {
        return when {
            response.isSuccessful -> {
                val conf = Bitmap.Config.ARGB_8888
                val opt = BitmapFactory.Options()
                opt.inPreferredConfig = conf
                val image = BitmapFactory.decodeStream(response.body()!!.byteStream(),null,opt)

                if (image != null) {
                    runesImages.add(image)
                    Log.d("MainViewModel", "runesImages: $image")
                    NetworkResult.Success(runesImages)
                } else {
                    NetworkResult.Success(emptyList())
                }
            }
            else -> NetworkResult.Error(response.errorBody().toString())
        }
    }
}