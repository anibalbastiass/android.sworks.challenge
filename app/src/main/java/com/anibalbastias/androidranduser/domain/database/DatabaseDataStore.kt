package com.anibalbastias.androidranduser.domain.database

import com.anibalbastias.androidranduser.domain.mapper.BdRandomUsersMapper
import com.anibalbastias.androidranduser.domain.model.DomainUserResult
import com.anibalbastias.androidranduser.domain.repository.DatabaseRepository


open class DatabaseDataStore(
    private val usersDatabase: UsersDatabase,
    private val mapper: BdRandomUsersMapper
) : DatabaseRepository {

    override suspend fun getFavoriteUserById(userId: String): DomainUserResult {
        with(mapper) {
            return usersDatabase.users.getUserById(userId = userId).fromDatabaseToDomain()
        }
    }

    override suspend fun getFavoriteUsers(): List<DomainUserResult> {
        with(mapper) {
            return usersDatabase.users.getUsers().map { it.fromDatabaseToDomain() }
        }
    }

    override suspend fun saveFavoriteUser(user: DomainUserResult) {
        with(mapper) {
            usersDatabase.users.saveUser(user.fromDomainToDatabase())
        }
    }

    override suspend fun deleteFavoriteUser(user: DomainUserResult) {
        with(mapper) {
            usersDatabase.users.deleteUser(user.fromDomainToDatabase())
        }
    }

}
