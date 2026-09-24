package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals

class ParyantaSamasaTest {
    @Test
    fun `numeral and paryanta derive as a neuter compound`() {
        val result = SamasaEngine().derive(
            padas = listOf(
                SamasaPada("दशन्", Vibhakti.PRATHAMA),
                SamasaPada("पर्यन्त", Vibhakti.PRATHAMA, linga = Linga.NAPUMSAKA),
            ),
            type = SamasaType.KARMADHARAYA,
            outputLinga = Linga.NAPUMSAKA,
        )

        assertEquals(
            "दशपर्यन्तम्",
            result.final.surface,
            result.applications.joinToString { "${it.sutra}: ${it.after.surface}" },
        )
    }
}
