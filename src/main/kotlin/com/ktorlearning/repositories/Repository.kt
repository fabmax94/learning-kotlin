package com.ktorlearning.repositories

abstract class Repository<A> {
    protected var entities: Collection<A> = mutableListOf<A>()

    abstract fun equal(entityA: A, entity: A): Boolean

    fun save(entity: A) {
        this.entities += entity
    }

    fun remove(entity: A) {
        this.entities = this.entities.filter { !this.equal(entity, it) }
    }

    fun update(entity: A) {
        this.remove(entity)
        this.save(entity)
    }
}