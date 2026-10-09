package dev.panini.execution

import dev.panini.core.KrtAffix
import dev.panini.core.Vibhakti
import dev.panini.execution.binding.toSamasaPada
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class CompoundAffixMetadataTest {
    @Test fun `compound adapter carries explicit krt identity without stem guessing`() {
        for ((affix, stem) in listOf(KrtAffix.KTA to "कृत", KrtAffix.KTAVATU to "कृतवत्", KrtAffix.YAT to "देय", KrtAffix.NAMUL to "कारम्")) {
            val ast = KridantaPratipadika("कृ + ${affix.upadesha}", emptyList(), DhatuPrakriti("कृ", "कृ"), affix.upadesha)
            assertEquals(affix, ast.krtAffix)
            val member=ast.toSamasaPada(stem, Vibhakti.SAPTAMI)
            assertEquals(affix, member.krtAffix)
            assertEquals(stem, member.upadesha)
            assertEquals(Vibhakti.SAPTAMI, member.vibhakti)
            assertEquals(affix, member.copy(vibhakti=Vibhakti.PRATHAMA).krtAffix)
        }
        assertNull(MulaPratipadika("कृत", "कृत").toSamasaPada("कृत", Vibhakti.PRATHAMA).krtAffix)
    }
}
