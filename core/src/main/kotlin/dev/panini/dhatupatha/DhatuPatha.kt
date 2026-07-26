package dev.panini.dhatupatha

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
}
