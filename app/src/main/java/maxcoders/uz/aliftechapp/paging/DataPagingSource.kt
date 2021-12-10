package maxcoders.uz.aliftechapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import maxcoders.uz.aliftechapp.model.Data
import maxcoders.uz.aliftechapp.network.RetrofitApi
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1
class DataPagingSource constructor(private val retrofitApi: RetrofitApi): PagingSource<Int, Data>() {


    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = retrofitApi.getPagingData(page, params.loadSize)
            LoadResult.Page(
                data = response.body()?.data!!,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (response.body()?.data!!.isEmpty()) null else page + 1
            )
        }catch (exception: IOException){
            return LoadResult.Error(exception)
        }catch (exception: HttpException){
            return LoadResult.Error(exception)
        }
    }
}