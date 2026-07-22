package dev.panini.execution.external

import dev.panini.execution.ExecutionEffect

/**
 * Registry and dispatcher for executing external system handlers.
 */
class ExternalCapabilityDispatcher {

    fun interface CapabilityHandler {
        fun handle(payload: String, effect: ExecutionEffect): String
    }

    private val handlers = java.util.concurrent.ConcurrentHashMap<ExecutionEffect, CapabilityHandler>()

    fun register(effect: ExecutionEffect, handler: CapabilityHandler) {
        handlers[effect] = handler
    }

    fun unregister(effect: ExecutionEffect) {
        handlers.remove(effect)
    }

    fun dispatch(effect: ExecutionEffect, payload: String): String {
        val handler = handlers[effect]
            ?: return "Simulated dispatch for effect $effect with payload '$payload'"
        return handler.handle(payload, effect)
    }

    fun clear() {
        handlers.clear()
    }
}
