package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.DhatuPrakriti
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.TingPratyaya
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DivTransactionCasesTest {
    private val premises = setOf(SemanticRelation.TRANSACTION_GAMBLING_OBJECT,
        SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)
    private val gen = Vibhakti.SASTHI
    private val acc = Vibhakti.DVITIYA
    private fun resolve(cases: Set<Vibhakti>, prefixes: List<String> = emptyList(),
        relations: Set<SemanticRelation> = premises, voice: Prayoga = Prayoga.KARTARI,
        root: String = "दिव्"): KarakaResolution {
        val p = ParticipantFacts("stake", AvyayaPada("शत", "शत"), cases, semanticRelations = relations)
        val verb = TingantaPada("test", prefixes, DhatuPrakriti(root, root), Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p), voice, verbNode = verb))
    }

    @Test fun `unprefixed div transaction has mandatory specific genitive rather than instrument labeling`() {
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            val result = resolve(setOf(gen, acc), voice = voice)
            assertEquals(Karaka.SAMBANDHA, result.resolved)
            assertEquals(gen, result.resolvedVibhakti)
            assertEquals(listOf("2.3.58"), result.evidence.map { it.sutra })
            assertEquals(emptyList(), result.alternatives)
            assertEquals(null, resolve(setOf(acc), voice = voice).resolvedVibhakti)
        }
        assertTrue(resolve(setOf(gen), relations = setOf(SemanticRelation.GAMBLING_INSTRUMENT)).evidence.none {
            it.sutra in setOf("2.3.58", "2.3.59") })
        for (missing in premises) {
            assertTrue(resolve(setOf(gen), relations = premises - missing).evidence.none { it.sutra == "2.3.58" }, missing.name)
        }
    }

    @Test fun `prefixed div preserves relational and independently proven object interpretations`() {
        val genitive = resolve(setOf(gen), listOf("प्रति"))
        assertEquals(Karaka.SAMBANDHA, genitive.resolved)
        assertEquals(listOf("2.3.59"), genitive.evidence.map { it.sutra })
        val accOption = genitive.alternatives.single()
        assertEquals(Karaka.KARMAN, accOption.karaka)
        assertEquals(acc, accOption.vibhakti)
        assertEquals(listOf("1.4.49", "2.3.59"), accOption.evidence.map { it.sutra })
        assertEquals(false, accOption.matchesObservedMorphology)

        val accusative = resolve(setOf(acc), listOf("प्रति"))
        assertEquals(Karaka.KARMAN, accusative.resolved)
        assertEquals(acc, accusative.resolvedVibhakti)
        assertEquals(listOf("1.4.49", "2.3.59"), accusative.evidence.map { it.sutra })
        assertEquals(Karaka.SAMBANDHA, accusative.alternatives.single().karaka)
        assertEquals(listOf("2.3.59"), accusative.alternatives.single().evidence.map { it.sutra })

        val ambiguous = resolve(setOf(gen, acc), listOf("प्रति"))
        assertEquals(null, ambiguous.resolved)
        assertEquals(null, ambiguous.resolvedVibhakti)
        assertEquals(setOf(Karaka.SAMBANDHA, Karaka.KARMAN), ambiguous.candidates)
        assertEquals(true, ambiguous.alternatives.single().matchesObservedMorphology)
    }

    @Test fun `passive expressed object does not receive an accusative option`() {
        val genitive = resolve(setOf(gen), listOf("प्रति"), voice = Prayoga.KARMANI)
        assertEquals(Karaka.SAMBANDHA, genitive.resolved)
        assertEquals(emptyList(), genitive.alternatives)
        assertEquals(null, resolve(setOf(acc), listOf("प्रति"), voice = Prayoga.KARMANI).resolvedVibhakti)
    }

    @Test fun `brahmana exception suppresses generic and prefixed genitive fallbacks`() {
        val brahmana = premises + SemanticRelation.BRAHMANA_DIV_OBJECT
        for (prefixes in listOf(emptyList(), listOf("प्रति"))) {
            val result = resolve(setOf(gen, acc), prefixes, brahmana)
            assertEquals(Karaka.KARMAN, result.resolved)
            assertEquals(acc, result.resolvedVibhakti)
            assertEquals(listOf("1.4.49", "2.3.60"), result.evidence.map { it.sutra })
            assertEquals(emptyList(), result.alternatives)
            assertEquals(null, resolve(setOf(gen), prefixes, brahmana).resolvedVibhakti)
            assertEquals(null, resolve(setOf(acc), prefixes, brahmana, Prayoga.KARMANI).resolvedVibhakti)
        }
        assertTrue(resolve(setOf(acc), relations = setOf(SemanticRelation.BRAHMANA_DIV_OBJECT)).evidence.none {
            it.sutra == "2.3.60" })
        assertTrue(resolve(setOf(gen, acc), listOf("अज्ञात")).evidence.none { it.sutra in setOf("2.3.58", "2.3.59") })
        assertEquals(listOf("2.3.50"), resolve(setOf(gen), relations = premises, root = "कृ").evidence.map { it.sutra })
    }
}
