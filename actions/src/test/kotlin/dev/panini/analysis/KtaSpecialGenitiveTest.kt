package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KtaSpecialGenitiveTest {
    private val kta = KridantaPratipadika("भुक्तम्", emptyList(), DhatuPrakriti("भुज्", "भुज्"), "क्त")
    private fun resolve(role: SemanticRelation = SemanticRelation.INDEPENDENT_AGENT,
        present: Boolean = false, expressed: Set<Karaka>? = setOf(Karaka.ADHIKARANA),
        governor: KridantaPratipadika? = if (present) kta.copy(sourceText = "मतः", dhatu = DhatuPrakriti("मन्", "मन्")) else kta,
        voice: Prayoga = Prayoga.KARTARI,
        cases: Set<Vibhakti> = setOf(Vibhakti.SASTHI), dual: Boolean = false,
        extra: Set<SemanticRelation> = emptySet()): KarakaResolution {
        val p = ParticipantFacts("dependent", AvyayaPada("dependent", "dependent"), cases,
            semanticRelations = setOf(role) + extra + if (present) setOf(SemanticRelation.PRESENT_PARTICIPLE_AGENT) else emptySet())
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("भुज्"), p, listOf(p), voice,
            governingKrt = governor, governingKrtExpressedRoles = expressed,
            governingKrtOccurrenceId = "kta-1", governingKrtGenitiveEligibility = if (dual)
                KrtGenitiveEligibility("kta-1", setOf(Karaka.KARTR, Karaka.KARMAN)) else null))
    }

    @Test fun `present kta agent has special genitive independently of outer voice`() {
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            val result = resolve(present = true, expressed = setOf(Karaka.KARMAN), voice = voice)
            assertEquals(Karaka.KARTR, result.resolved)
            assertEquals(Vibhakti.SASTHI, result.resolvedVibhakti)
            assertEquals(listOf("1.4.54", "2.3.67"), result.evidence.map { it.sutra })
        }
        assertEquals(null, resolve(present = false, expressed = setOf(Karaka.KARMAN)).resolvedVibhakti)
        assertEquals(null, resolve(present = true, expressed = setOf(Karaka.KARTR)).resolvedVibhakti)
        assertTrue(resolve(role = SemanticRelation.DESIRED_OBJECT, present = true,
            expressed = emptySet()).evidence.none { it.sutra == "2.3.67" })
    }

    @Test fun `location denoting kta permits both agent and object genitives despite dual eligibility`() {
        for ((premise, role, sutra) in listOf(
            Triple(SemanticRelation.INDEPENDENT_AGENT, Karaka.KARTR, "1.4.54"),
            Triple(SemanticRelation.DESIRED_OBJECT, Karaka.KARMAN, "1.4.49"))) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                val result = resolve(premise, dual = true, voice = voice)
                assertEquals(role, result.resolved)
                assertEquals(Vibhakti.SASTHI, result.resolvedVibhakti)
                assertEquals(listOf(sutra, "2.3.68"), result.evidence.map { it.sutra })
            }
        }
    }

    @Test fun `special genitive takes precedence over ordinary instrumental`() {
        for (present in listOf(false, true)) {
            val expressed = if (present) setOf(Karaka.KARMAN) else setOf(Karaka.ADHIKARANA)
            val result = resolve(present = present, expressed = expressed,
                cases = setOf(Vibhakti.SASTHI, Vibhakti.TRTIYA))
            assertEquals(Vibhakti.SASTHI, result.resolvedVibhakti)
            assertEquals(null, resolve(present = present, expressed = expressed, cases = setOf(Vibhakti.TRTIYA)).resolvedVibhakti)
        }
    }

    @Test fun `action reading unlinked governor unknown reading and wrappers cannot license special genitive`() {
        assertEquals(null, resolve(expressed = emptySet(),
            extra = setOf(SemanticRelation.LOCATION_PARTICIPLE_RELATION)).resolvedVibhakti)
        assertEquals(null, resolve(expressed = null).resolvedVibhakti)
        for (governor in listOf(null, kta.copy(krtPratyaya = "क्तवतुँ"),
            kta.copy(vikaras = listOf(TaddhitaVikara("इनि", "इनि"))))) {
            for (present in listOf(false, true)) {
                assertTrue(resolve(present = present, governor = governor).evidence.none {
                    it.sutra == "2.3.67" || it.sutra == "2.3.68"
                })
            }
        }
    }
}
