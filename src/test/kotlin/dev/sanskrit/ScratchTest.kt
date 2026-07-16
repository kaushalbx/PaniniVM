package dev.sanskrit

import dev.sanskrit.derivation.*
import kotlin.test.Test

class ScratchTest {

    @Test
    fun testDerivationTrace() {
        val requests = listOf(
            "BHŪ + अट्" to TingantaDerivationRequest("भू", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LET, letAugment = LetAugment.AT),
            "LABH ĀTMANEPADA DUAL" to TingantaDerivationRequest("लभ्", Vacana.DVIVACANA, Purusha.PRATHAMA, Lakara.LET, letAugment = LetAugment.AT),
            "LABH ĀTMANEPADA OPTIONAL ऐ" to TingantaDerivationRequest("लभ्", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LET, letEOption = LetEOption.AI),
            "TṜ̄ + सिप् + अट्" to TingantaDerivationRequest("तॄ", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LET, LetAugment.AT, LetFormation.SIP_AORIST),
        )
        requests.forEach { (label, request) ->
            val dhatu = dev.sanskrit.dhatupatha.DhatuPatha.all.first {
                it.upadesha == request.dhatu || it.derivationalSurface == request.dhatu
            }
            val result = DerivationEngine().derive(request.initialState(dhatu))
            println("=== $label ===")
            result.applications.forEachIndexed { index, application ->
                println("${index + 1}. ${application.sutra}: ${application.before.surface} -> ${application.after.surface}")
            }
            println("FINAL: ${result.final.surface}")
        }
    }
}
