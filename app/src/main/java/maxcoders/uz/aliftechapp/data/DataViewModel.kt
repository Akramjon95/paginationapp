package maxcoders.uz.aliftechapp.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import maxcoders.uz.aliftechapp.model.Data
import maxcoders.uz.aliftechapp.model.DataResponse
import maxcoders.uz.aliftechapp.util.Resource
import retrofit2.Response
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class DataViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {
    private val TAG = "DataViewModel"
    var myData : DataResponse? = null
    val dataList: MutableLiveData<Resource<DataResponse>> = MutableLiveData()

    fun insert(data: List<Data>) = viewModelScope.launch {
        repository.insert(data)
    }

    fun getAllData() = repository.getAllData()

    fun getAllPagingData(): Flow<PagingData<Data>>{
        return Pager(config = PagingConfig( pageSize = 3, maxSize = 24),
        pagingSourceFactory = {repository.getAllPagingData()}).flow.cachedIn(viewModelScope)
    }


    var items: Flow<PagingData<Data>> = repository.getDataFromDb()

    fun getPagingDataFromDb(): Flow<PagingData<Data>>{
        return items
    }


    fun handleDataResponse(response: Response<DataResponse>): Resource<DataResponse>{
        Log.d(TAG, "handleUsersResponse: +" + response)
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
//                if (myData == null){
//                    myData = resultResponse
//
//                    myData?.data?.let { insert(it) }
//                }
                resultResponse.data?.let { insert(it) }
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




    fun getDataApi(){
        viewModelScope.launch {
            dataList.postValue(Resource.Loading())
            val response = repository.getDataApi()
            dataList.postValue(handleDataResponse(response))
        }
    }

}