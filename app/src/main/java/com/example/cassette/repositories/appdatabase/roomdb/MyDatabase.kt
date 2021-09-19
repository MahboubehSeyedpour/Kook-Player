package com.example.cassette.repositories.appdatabase.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cassette.repositories.appdatabase.entities.Favorites
import com.example.cassette.repositories.appdatabase.entities.PlaylistModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(
    entities = [PlaylistModel::class, Favorites::class],
    version = 1
)
//@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun favoriteDao(): FavoriteDao
//    abstract fun globalValueDao(): GlobalValueDao

    private class MyDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {

                    populatePlaylistDatabase(database.playlistDao())
                    populateFavoriteDatabase(database.favoriteDao())
                }
            }
        }

        suspend fun populatePlaylistDatabase(playlistDao: PlaylistDao) {
            // Delete all content here.
            playlistDao.deleteAll()

        }

        suspend fun populateFavoriteDatabase(favoriteDao: FavoriteDao) {
            // Delete all content here.
            favoriteDao.deleteAll()

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
                    MyDatabase::class.java, "_database"
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