package com.ktorlearning.repositories

import com.ktorlearning.models.Account

object AccountRepository : Repository<Account>() {
    override fun equal(entityA: Account, entity: Account): Boolean = entityA.id == entity.id

    fun findById(id: String): Account? = this.entities.find { it.id == id }

    fun findAll(): Collection<Account> = this.entities
}




