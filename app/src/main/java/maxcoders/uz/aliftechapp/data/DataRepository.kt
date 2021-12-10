package maxcoders.uz.aliftechapp.data

import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import maxcoders.uz.aliftechapp.db.DataBase
import maxcoders.uz.aliftechapp.db.SampleDao
import maxcoders.uz.aliftechapp.model.Data
import maxcoders.uz.aliftechapp.network.RetrofitApi
import maxcoders.uz.aliftechapp.paging.DataPagingSource
import maxcoders.uz.aliftechapp.paging.DataRemoteMediator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val dao: DataBase, private val retrofitApi: RetrofitApi) {

    suspend fun insert(data: List<Data>) = dao.getSampleDao().insert(data)

    fun getAllData() = dao.getSampleDao().getAllData()

    suspend fun getDataApi() = retrofitApi.getData()

    fun getAllPagingData() :PagingSource<Int, Data>{
        return dao.getSampleDao().getAllPagingData()
    }

    //get data from network
    @ExperimentalPagingApi
    fun getDataPaging(): Flow<PagingData<Data>>{
        return Pager(
            config = PagingConfig(
                pageSize = 3,
                maxSize = 24,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {DataPagingSource(retrofitApi)}
        ).flow
    }

    // get data from db
    @ExperimentalPagingApi
    fun getDataFromDb(): Flow<PagingData<Data>>{
        val pagingSourceFactory = { dao.getSampleDao().getAllPagingData() }
        return Pager(
            config = PagingConfig(
                pageSize = 3,
                maxSize = 24,
                enablePlaceholders = false
            ),
            remoteMediator = DataRemoteMediator(dao, retrofitApi),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun fillWithData(data: List<Data>){
        dao.getSampleDao().deleteAll()
        dao.getSampleDao().insert(data)
    }

    suspend fun deleteAllData(){
        dao.getSampleDao().deleteAll()
    }
}