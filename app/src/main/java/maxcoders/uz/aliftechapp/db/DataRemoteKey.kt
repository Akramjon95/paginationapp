package maxcoders.uz.aliftechapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key")
data class DataRemoteKey(
    @PrimaryKey
    val id: String,
    val prev: Int?,
    val next: Int?
) {
}