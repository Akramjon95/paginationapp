package maxcoders.uz.aliftechapp.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class Data(
    @PrimaryKey
    val url:String,
    val startDate:String? = null,
    val endDate:String? = null,
    val name:String? = null,
    val icon:String? = null,
//    @Ignore
//    val venue: Venue? = null
)
