package maxcoders.uz.aliftechapp.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import maxcoders.uz.aliftechapp.model.Data

@Dao
interface SampleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: List<Data>)

    @Query("SELECT * FROM data")
    fun getAllData(): LiveData<List<Data>>

    @Query("SELECT * FROM data")
    fun getAllPagingData(): PagingSource<Int, Data>

    @Query("DELETE FROM  data")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRemoteKeys(list: List<DataRemoteKey>)

    @Query("SELECT * FROM remote_key where id = :id")
    suspend fun getAllRemoteKey(id: String): DataRemoteKey?

    @Query("DELETE FROM remote_key")
    suspend fun deleteAllRemoteKey()

}