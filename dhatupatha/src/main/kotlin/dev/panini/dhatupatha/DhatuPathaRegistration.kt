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

object DhatuPathaRegistration {
    val allDhatus: List<Dhatu> by lazy {
        listOf(
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
    }

    fun resolve(upadesha: String): List<Dhatu> =
        allDhatus.filter { it.upadesha == upadesha }

    fun resolveOperations(upadesha: String): List<dev.panini.execution.DhatuOperation> =
        resolve(upadesha).flatMap(Dhatu::operations).distinct()

    val operationCatalog: dev.panini.execution.OperationCatalog =
        dev.panini.execution.OperationCatalog(
            operationsForUpadesha = ::resolveOperations,
            dhatus = { allDhatus },
        )

    init {
        ensureRegistered()
    }

    fun ensureRegistered() {
        if (DhatuPatha.provider() !== allDhatus) {
            DhatuPatha.provider = { allDhatus }
        }
    }
}

val autoRegisterDhatuPatha: Unit = DhatuPathaRegistration.ensureRegistered()
