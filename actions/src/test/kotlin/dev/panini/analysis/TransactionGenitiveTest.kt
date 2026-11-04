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

class TransactionGenitiveTest {
    private val premises = setOf(SemanticRelation.TRANSACTION_GAMBLING_OBJECT,
        SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)
    private fun verb(root: String, prefixes: List<String>) = TingantaPada("test", prefixes,
        DhatuPrakriti(root, root), Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
    private fun resolve(root: String, relations: Set<SemanticRelation> = premises,
        case: Vibhakti = Vibhakti.SASTHI, node: TingantaPada? = null,
        voice: Prayoga = Prayoga.KARTARI): KarakaResolution {
        val p = ParticipantFacts("stake", AvyayaPada("शतस्य", "शतस्य"), setOf(case), semanticRelations = relations)
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p), voice,
            candidates = setOf(Karaka.KARMAN), verbNode = node))
    }

    @Test fun `transaction object in sesha has specific genitive for vyavahr and pan`() {
        for (root in listOf("व्यवहृ", "पण्")) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                val result = resolve(root, voice = voice)
                assertEquals(Karaka.SAMBANDHA, result.resolved)
                assertEquals(Vibhakti.SASTHI, result.resolvedVibhakti)
                assertEquals(listOf("2.3.57"), result.evidence.map { it.sutra })
            }
            for (missing in premises) {
                assertTrue(resolve(root, premises - missing).evidence.none { it.sutra == "2.3.57" }, missing.name)
            }
            val ordinary = resolve(root, premises - SemanticRelation.SESHA_RELATION, Vibhakti.DVITIYA)
            assertEquals(listOf("1.4.49", "2.3.2"), ordinary.evidence.map { it.sutra })
            // 2.3.59 belongs to prefixed DIV, not these transaction verbs.
            assertEquals(null, resolve(root, case = Vibhakti.DVITIYA).resolvedVibhakti)
        }
        assertEquals(listOf("2.3.50"), resolve("कृ").evidence.map { it.sutra })
    }

    @Test fun `structured vi ava hr controls lexical construction and excludes other prefix orders`() {
        assertEquals(listOf("2.3.57"), resolve("हृ", node = verb("हृ", listOf("वि", "अव"))).evidence.map { it.sutra })
        for (prefixes in listOf(emptyList(), listOf("वि"), listOf("अव"), listOf("अव", "वि"), listOf("अज्ञात"))) {
            assertTrue(resolve("व्यवहृ", node = verb("हृ", prefixes)).evidence.none { it.sutra == "2.3.57" })
        }
    }

    @Test fun `counting and praise without transaction sense do not license the specific genitive`() {
        for (root in listOf("व्यवहृ", "पण्")) {
            val nonTransaction = setOf(SemanticRelation.DESIRED_OBJECT)
            assertEquals(listOf("1.4.49", "2.3.2"), resolve(root, nonTransaction, Vibhakti.DVITIYA).evidence.map { it.sutra })
            assertTrue(resolve(root, nonTransaction + SemanticRelation.SESHA_RELATION).evidence.none { it.sutra == "2.3.57" })
        }
    }
}
