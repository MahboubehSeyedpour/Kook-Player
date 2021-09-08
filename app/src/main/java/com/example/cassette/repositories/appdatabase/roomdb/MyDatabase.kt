package com.example.cassette.repositories.appdatabase.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cassette.repositories.appdatabase.entities.PlaylistModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(
    entities = [PlaylistModel::class],
    version = 1
)
//@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao


    private class MyDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.playlistDao())
                }
            }
        }

        suspend fun populateDatabase(playlistDao: PlaylistDao) {
            // Delete all content here.
            playlistDao.deleteAll()

        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MyDatabase {
            return INSTANCE ?: synchronized(MyDatabase::class.java)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java, "playlist-_database"
                )
                    .addCallback(MyDatabaseCallback(scope))
                    .build()
                INSTANCE = instance

//                return instance
                instance
            }
        }


    }


}