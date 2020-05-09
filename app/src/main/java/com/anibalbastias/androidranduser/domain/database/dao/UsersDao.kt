package com.anibalbastias.androidranduser.domain.database.dao

import androidx.room.*
import com.anibalbastias.androidranduser.domain.model.database.EntityUser

@Dao
interface UsersDao {

    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<EntityUser>

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: String): EntityUser

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(vararg user: EntityUser)

    @Delete
    suspend fun deleteUser(vararg user: EntityUser)

}