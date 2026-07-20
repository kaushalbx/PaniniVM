package dev.panini.dhatupatha

import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.dhatupatha.rudhadi.RudhadiDhatus
import dev.panini.dhatupatha.svadi.SvadiDhatus
import dev.panini.dhatupatha.tanadi.TanadiDhatus
import dev.panini.dhatupatha.tudadi.TudadiDhatus

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

    fun findByUpadesha(upadesha: String): List<Dhatu> = all.filter { it.upadesha == upadesha }


    fun findOneByUpadesha(upadesha: String):Dhatu? = all.single { it.upadesha == upadesha }
}
