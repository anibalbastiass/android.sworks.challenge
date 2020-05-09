package com.anibalbastias.androidranduser.domain.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anibalbastias.androidranduser.domain.Constants.TABLE_USERS

@Entity(tableName = TABLE_USERS)
data class EntityUser(
    @PrimaryKey
    val userId: String,
    val fullName: String,
    val gender: String,
    val address: String,
    val city: String,
    val state: String,
    val country: String,
    val email: String,
    val age: String,
    val phone: String,
    val cell: String,
    val thumbImageUrl: String,
    val largeImageUrl: String,
    val nationality: String,
    val pageSize: Int,
    val isFavorite: Boolean = true
)