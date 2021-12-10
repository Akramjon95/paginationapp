package maxcoders.uz.aliftechapp.paging

import androidx.lifecycle.Transformations.map
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import maxcoders.uz.aliftechapp.db.DataBase
import maxcoders.uz.aliftechapp.db.DataRemoteKey
import maxcoders.uz.aliftechapp.db.SampleDao
import maxcoders.uz.aliftechapp.model.Data
import maxcoders.uz.aliftechapp.network.RetrofitApi
import java.io.InvalidObjectException
import java.lang.Exception

@ExperimentalPagingApi
class DataRemoteMediator(
    private val db:DataBase,
    private val retrofitApi: RetrofitApi
    ): RemoteMediator<Int,Data>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Data>
    ): MediatorResult {

   return     try {
            val page = when(loadType){
                LoadType.APPEND->{
                    val remoteKey = getLastkey(state)?:throw InvalidObjectException("InvalidObject")
                    remoteKey?.next?:return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.PREPEND->{
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.REFRESH->{
                    val remoteKey = getClosestKey(state)
                    remoteKey?.next?.minus(1)?: 1
                }
            }

            val response = retrofitApi.getPagingData(page, state.config.pageSize)
            val endOfPagination = response.body()?.data?.size!! < state.config.pageSize

            if (response.isSuccessful){
                response.body()?.let {
                    if (loadType == LoadType.REFRESH){
                        db.getSampleDao().deleteAll()
                        db.getSampleDao().deleteAllRemoteKey()
                    }

                    val prevKey = if (page == 1)null else page - 1
                    val nextKey = if (endOfPagination)null else page + 1

                    val list = response.body()?.data?.map {
                       DataRemoteKey(it.url, prevKey, nextKey)
                    }

                    if (list != null){
                        db.getSampleDao().insertAllRemoteKeys(list)
                    }
                    db.getSampleDao().insert(it.data!!)
                    //it.data?.let { it1 -> db.getSampleDao().insert(it1) }
                }
                MediatorResult.Success(endOfPagination)
            } else{
                MediatorResult.Success(endOfPaginationReached = true)
            }

        }catch (e: Exception){
            MediatorResult.Error(e)
        }
           // return MediatorResult.Success(endOfPaginationReached = true)
    }

    suspend fun getClosestKey(state: PagingState<Int, Data>): DataRemoteKey?{
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let {
                db.getSampleDao().getAllRemoteKey(it.url)
            }
        }
    }

    suspend fun getLastkey(state: PagingState<Int, Data>): DataRemoteKey?{
        return state.lastItemOrNull()?.let {
            db.getSampleDao().getAllRemoteKey(it.url)
        }
    }

}