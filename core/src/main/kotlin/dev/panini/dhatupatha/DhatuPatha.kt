package dev.panini.dhatupatha

import dev.panini.execution.DhatuOperation

/** Read-only lookup over the Dhātupāṭha sections currently represented. */
object DhatuPatha {
    var provider: () -> List<Dhatu> = { emptyList() }

    val all: List<Dhatu> get() {
        val list = provider()
        if (list.isNotEmpty()) return list
        runCatching {
            Class.forName("dev.panini.dhatupatha.DhatuPathaRegistration")
        }
        return provider()
    }

    fun find(id: String): Dhatu? = all.singleOrNull { it.id == id }

    fun findByUpadesha(upadesha: String): List<Dhatu> = all.filter { it.upadesha == upadesha }

    fun findOneByUpadesha(upadesha: String): Dhatu? = all.single { it.upadesha == upadesha }

    fun resolveOperation(dhatuUpadesha: String, operationName: String): DhatuOperation {
        val dhatu = all.firstOrNull { 
            (it.upadesha == dhatuUpadesha || it.id == dhatuUpadesha) &&
            it.operations.any { op -> op.name == operationName }
        } ?: all.firstOrNull { 
            it.upadesha == dhatuUpadesha || it.id == dhatuUpadesha
        } ?: error("Dhātu not found in registry: $dhatuUpadesha")
        return dhatu.operations.firstOrNull { it.name == operationName }
            ?: error("Operation '$operationName' not found for dhātu $dhatuUpadesha")
    }
}
