package com.anibalbastias.androidranduser.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anibalbastias.androidranduser.domain.Constants.DATABASE_VERSION
import com.anibalbastias.androidranduser.domain.database.dao.UsersDao
import com.anibalbastias.androidranduser.domain.model.database.EntityUser

@Database(
    entities = [
        EntityUser::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class UsersDatabase : RoomDatabase() {
    abstract val users: UsersDao
}