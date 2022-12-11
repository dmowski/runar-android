package com.tnco.runar.repository

// import com.tnco.runar.data.remote.GeneratorApi
// import com.tnco.runar.model.RunesItemsModel
// import com.tnco.runar.repository.backend.DataClassConverters
// import javax.inject.Inject
// //TODO
// class GeneratorRepository @Inject constructor(
//    private val databaseRepository: DatabaseRepository,
//    private val generatorApi: GeneratorApi
// ) { //TODO нужен ли этот метод и этот класс???
//    suspend fun getRunes(): List<RunesItemsModel> {
//        val response = generatorApi.getRunes()
//        if (response.isSuccessful) {
//            val convertedResult =
//                DataClassConverters.runesRespToItems(response.body()!!)
//            databaseRepository.updateRunesGeneratorDB(convertedResult)
//            return convertedResult
//        }
//        return listOf(RunesItemsModel())
//    }
// }
