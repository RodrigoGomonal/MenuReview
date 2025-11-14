package com.example.menureview.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.menureview.data.daos.CommentDao
import com.example.menureview.data.daos.RestaurantDao
import com.example.menureview.data.daos.TagDao
import com.example.menureview.data.daos.UserDao
import com.example.menureview.data.models.CommentEntity
import com.example.menureview.data.models.RestaurantEntity
import com.example.menureview.data.models.TagEntity
import com.example.menureview.data.models.UserEntity

@Database(
    entities = [
        UserEntity::class,
        RestaurantEntity::class,
        CommentEntity::class,
        TagEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun restaurantDao(): RestaurantDao
    abstract fun commentDao(): CommentDao
    abstract fun tagDao(): TagDao

//    companion object {
//        @Volatile private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "menu_review_db"
//                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
//            }
//        }
//    }
}