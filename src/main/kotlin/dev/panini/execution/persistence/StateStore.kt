package dev.panini.execution.persistence

import dev.panini.execution.SambhashanaContext

/**
 * Abstraction for persisting conversational and execution context state to storage.
 */
interface StateStore {
    fun save(key: String, context: SambhashanaContext)
    fun load(key: String): SambhashanaContext?
    fun delete(key: String): Boolean
    fun listKeys(): List<String>
}
