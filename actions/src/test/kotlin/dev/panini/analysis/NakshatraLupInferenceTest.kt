package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.LopaType
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NakshatraLupInferenceTest {
    private val premises = setOf(SemanticRelation.LOCATION, SemanticRelation.ASTROLOGICAL_TIME,
        SemanticRelation.NAKSATRA_EXPRESSION)
    private val allowed = setOf(Vibhakti.TRTIYA, Vibhakti.SAPTAMI)
    private fun resolve(cases: Set<Vibhakti>, relations: Set<SemanticRelation> = premises,
        lopa: LopaType? = LopaType.LUP, voice: Prayoga = Prayoga.KARTARI): KarakaResolution {
        val expression = SubantaPada("पुष्य", MulaPratipadika("पुष्य", "पुष्य"), SupPratyaya("ङि", "ङि"))
        val p = ParticipantFacts("nakshatra-time", expression, cases, semanticRelations = relations,
            taddhitaLopa = lopa?.let { TaddhitaLopaEvidence(it, "4.2.4") })
        // The caller independently supplies the locus hypothesis; case does not establish it.
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("अश्"), p, listOf(p), voice,
            candidates = setOf(Karaka.ADHIKARANA)))
    }

    @Test fun `nakshatra lup locus has exact chain and two case realizations in either voice`() {
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            for (case in allowed) {
                val result = resolve(setOf(case), voice = voice)
                assertEquals(Karaka.ADHIKARANA, result.resolved)
                assertEquals(case, result.resolvedVibhakti)
                assertEquals(listOf("1.4.45", "2.3.45"), result.evidence.map { it.sutra })
                assertTrue(result.evidence.last().reason.contains("4.2.4"))
                val alternative = result.alternatives.single()
                assertEquals((allowed - case).single(), alternative.vibhakti)
                assertEquals(listOf("1.4.45", "2.3.45"), alternative.evidence.map { it.sutra })
                assertEquals(false, alternative.matchesObservedMorphology)
            }
        }
        val ambiguous = resolve(allowed)
        assertEquals(Karaka.ADHIKARANA, ambiguous.resolved)
        assertEquals(null, ambiguous.resolvedVibhakti)
        assertEquals(true, ambiguous.alternatives.single().matchesObservedMorphology)
    }

    @Test fun `time marker alone absent lup other lopa and nonlocus use do not license instrumental`() {
        for (missing in premises) {
            val result = resolve(setOf(Vibhakti.TRTIYA), premises - missing)
            assertEquals(null, result.resolvedVibhakti, missing.name)
            assertTrue(result.evidence.none { it.sutra == "2.3.45" }, missing.name)
        }
        for (lopa in listOf(null, LopaType.LOPA, LopaType.LUK, LopaType.SHLU)) {
            assertEquals(null, resolve(setOf(Vibhakti.TRTIYA), lopa = lopa).resolvedVibhakti)
            val ordinary = resolve(setOf(Vibhakti.SAPTAMI), lopa = lopa)
            assertEquals(listOf("1.4.45", "2.3.36"), ordinary.evidence.map { it.sutra })
        }
        // Kāśikā: adya puṣyaḥ has lup but does not construe puṣya as the action locus.
        val naming = resolve(setOf(Vibhakti.PRATHAMA), premises - SemanticRelation.LOCATION)
        assertTrue(naming.evidence.none { it.sutra == "2.3.45" })
        assertEquals(null, resolve(setOf(Vibhakti.DVITIYA)).resolvedVibhakti)
    }
}
