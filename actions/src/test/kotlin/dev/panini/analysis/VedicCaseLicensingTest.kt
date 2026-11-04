package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaPada
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class VedicCaseLicensingTest {
    private val yajMeaning = setOf(SemanticRelation.VEDIC_YAJ_INSTRUMENT, SemanticRelation.INSTRUMENT)
    private val yajLicense = VedicCaseLicense("2.3.63", "Kāśikā ad 2.3.63: घृतस्य यजते")
    private val dativeLicense = VedicCaseLicense("2.3.62", "Kāśikā ad 2.3.62: पुरुषमृगश्चन्द्रमसः")
    private fun resolve(cases: Set<Vibhakti>, relations: Set<SemanticRelation> = yajMeaning,
        license: VedicCaseLicense? = yajLicense, register: GrammarRegister = GrammarRegister.VEDIC,
        root: String = "यज्", voice: Prayoga = Prayoga.KARTARI,
        candidates: Set<Karaka> = emptySet(), intendedObject: IntendedObject? = null): KarakaResolution {
        val p = ParticipantFacts("licensed-occurrence", AvyayaPada("test", "test"), cases,
            semanticRelations = relations, vedicCaseLicense = license, intendedObject = intendedObject)
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p), voice,
            candidates = candidates, register = register))
    }

    @Test fun `yaj licensed genitive retains instrument designation and source provenance`() {
        for (register in listOf(GrammarRegister.VEDIC, GrammarRegister.BRAHMANA)) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                val result = resolve(setOf(Vibhakti.SASTHI), register = register, voice = voice)
                assertEquals(Karaka.KARANA, result.resolved)
                assertEquals(listOf("1.4.42", "2.3.63"), result.evidence.map { it.sutra })
                assertTrue(result.evidence.last().reason.contains(yajLicense.reference))
            }
        }
        val explicitRole = resolve(setOf(Vibhakti.SASTHI), candidates = setOf(Karaka.KARANA))
        assertEquals(listOf("1.4.42", "2.3.63"), explicitRole.evidence.map { it.sutra })
    }

    @Test fun `bahulam requires local licence register root and instrument meaning`() {
        for (register in listOf(GrammarRegister.UNSPECIFIED, GrammarRegister.CLASSICAL)) {
            assertTrue(resolve(setOf(Vibhakti.SASTHI), register = register).evidence.none { it.sutra == "2.3.63" })
        }
        assertTrue(resolve(setOf(Vibhakti.SASTHI), license = null).evidence.none { it.sutra == "2.3.63" })
        assertTrue(resolve(setOf(Vibhakti.SASTHI), license = dativeLicense).evidence.none { it.sutra == "2.3.63" })
        assertTrue(resolve(setOf(Vibhakti.SASTHI), root = "कृ").evidence.none { it.sutra == "2.3.63" })
        for (missing in yajMeaning) {
            assertTrue(resolve(setOf(Vibhakti.SASTHI), relations = yajMeaning - missing).evidence.none { it.sutra == "2.3.63" })
        }
        assertFailsWith<IllegalArgumentException> { VedicCaseLicense("2.3.63", " ") }
        val ordinary = resolve(setOf(Vibhakti.TRTIYA))
        assertEquals(listOf("1.4.42", "2.3.18"), ordinary.evidence.map { it.sutra })
        assertEquals(emptyList(), ordinary.alternatives) // no freely generated genitive
    }

    @Test fun `licensed competing observed instrument cases remain ambiguous`() {
        val result = resolve(setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI))
        assertEquals(Karaka.KARANA, result.resolved)
        assertEquals(null, result.resolvedVibhakti)
        assertEquals(listOf("1.4.42", "2.3.18"), result.evidence.map { it.sutra })
        assertEquals(listOf("1.4.42", "2.3.63"), result.alternatives.single().evidence.map { it.sutra })
        assertEquals(true, result.alternatives.single().matchesObservedMorphology)
        val classical = resolve(setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI), register = GrammarRegister.CLASSICAL)
        assertEquals(Vibhakti.TRTIYA, classical.resolvedVibhakti)
        assertEquals(emptyList(), classical.alternatives)
    }

    @Test fun `dative meaning genitive needs its own licensed Vedic construction`() {
        val meaning = setOf(SemanticRelation.VEDIC_DATIVE_MEANING)
        val accepted = resolve(setOf(Vibhakti.SASTHI), meaning, dativeLicense, root = "अस्")
        assertEquals(Karaka.ANIRDHARITA, accepted.resolved)
        assertEquals(listOf("2.3.62"), accepted.evidence.map { it.sutra })
        for (register in listOf(GrammarRegister.CLASSICAL, GrammarRegister.UNSPECIFIED)) {
            assertEquals(null, resolve(setOf(Vibhakti.SASTHI), meaning, dativeLicense, register).resolvedVibhakti)
        }
        assertEquals(null, resolve(setOf(Vibhakti.SASTHI), meaning, null).resolvedVibhakti)
        assertEquals(null, resolve(setOf(Vibhakti.SASTHI), meaning, yajLicense).resolvedVibhakti)
        val recipient = resolve(setOf(Vibhakti.CHATURTHI, Vibhakti.SASTHI),
            meaning + SemanticRelation.RECIPIENT, dativeLicense, root = "दा", intendedObject = IntendedObject.Implied)
        assertEquals(Karaka.SAMPRADANA, recipient.resolved)
        assertEquals(null, recipient.resolvedVibhakti)
        assertEquals(listOf("1.4.32", "2.3.13"), recipient.evidence.map { it.sutra })
        assertEquals(listOf("1.4.32", "2.3.62"), recipient.alternatives.single().evidence.map { it.sutra })
    }
}
