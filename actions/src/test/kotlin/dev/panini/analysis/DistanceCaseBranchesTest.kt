package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class DistanceCaseBranchesTest {
    @Test fun `distance word options preserve the canonical source of each case`() {
        val slots = mapOf(Vibhakti.DVITIYA to "अम्", Vibhakti.TRTIYA to "टा",
            Vibhakti.PANCHAMI to "ङसिँ", Vibhakti.SAPTAMI to "ङि")
        fun source(case: Vibhakti) = if (case == Vibhakti.SAPTAMI) "2.3.36" else "2.3.35"
        for (stem in listOf("दूर", "अन्तिक", "विप्रकृष्ट", "अभ्याश")) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) for ((case, sup) in slots) {
                val pada = SubantaPada(stem, MulaPratipadika(stem, stem), SupPratyaya(sup, sup))
                val p = ParticipantFacts("p", pada, setOf(case), semanticRelations = setOf(
                    SemanticRelation.DISTANCE_OR_PROXIMITY, SemanticRelation.NON_SUBSTANCE_MEANING))
                val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("गम्"), p, listOf(p), voice))
                assertEquals(Karaka.ANIRDHARITA, result.resolved)
                assertEquals(case, result.resolvedVibhakti)
                assertEquals(listOf(source(case)), result.evidence.map { it.sutra })
                assertEquals(slots.keys - case, result.alternatives.map { it.vibhakti }.toSet())
                result.alternatives.forEach {
                    assertEquals(listOf(source(requireNotNull(it.vibhakti))), it.evidence.map { proof -> proof.sutra })
                    assertEquals(KarakaAlternativeKind.OPTIONAL_VIBHAKTI, it.kind)
                    assertFalse(it.matchesObservedMorphology)
                }
            }
        }
    }

    @Test fun `ordinary locus does not acquire distance word case alternatives`() {
        val p = ParticipantFacts("locus", AvyayaPada("locus", "locus"), setOf(Vibhakti.SAPTAMI),
            semanticRelations = setOf(SemanticRelation.LOCATION))
        val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("कृ"), p, listOf(p), Prayoga.KARTARI))
        assertEquals(listOf("1.4.45", "2.3.36"), result.evidence.map { it.sutra })
        assertTrue(result.alternatives.isEmpty())
    }
}
