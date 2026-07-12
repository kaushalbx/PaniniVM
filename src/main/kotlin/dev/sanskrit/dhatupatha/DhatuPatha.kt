package dev.sanskrit.dhatupatha

/** Read-only lookup over the Dhātupāṭha sections currently represented. */
object DhatuPatha {
    val all: List<Dhatu> = listOf(
        BhvadiDhatus.all,
        AdadiDhatus.all,
        JuhotyadiDhatus.all,
        DivadiDhatus.all,
        SvadiDhatus.all,
        TudadiDhatus.all,
        RudhadiDhatus.all,
        TanadiDhatus.all,
        KryadiDhatus.all,
        CuradiDhatus.all,
    ).flatten()

    fun find(id: String): Dhatu? = all.singleOrNull { it.id == id }

    fun findByUpadesha(upadesha: String): List<Dhatu> =
        all.filter { it.upadesha == upadesha }
}
