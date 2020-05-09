package com.anibalbastias.androidranduser.domain.model

data class DomainUserResult(
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
    var isFavorite: Boolean
) {

    override fun hashCode(): Int = userId.toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DomainUserResult

        return (userId == other.userId)
    }
}