package maxcoders.uz.aliftechapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import maxcoders.uz.aliftechapp.model.Data


@Database(entities = [Data::class, DataRemoteKey::class], version = 4)
abstract class DataBase : RoomDatabase() {

    abstract fun getSampleDao(): SampleDao

    companion object {
        @Volatile
        private var instance: DataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(LOCK) {
            instance?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DataBase::class.java,
                "room_db.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}