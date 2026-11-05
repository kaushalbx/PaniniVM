package dev.panini.analysis

import dev.panini.core.*
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.vyakaranam.ast.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RitualOblationGenitiveTest {
    private val premises = setOf(SemanticRelation.DEITY_OBLATION_OBJECT,
        SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)
    private val deity = ParticipantFacts("deity", AvyayaPada("अग्नये", "अग्नये"), setOf(Vibhakti.CHATURTHI),
        semanticRelations = setOf(SemanticRelation.DEITY_REFERENT, SemanticRelation.RECIPIENT))
    private fun verb(root: String = "ब्रू", prefixes: List<String> = listOf("अनु")) =
        TingantaPada("test", prefixes, DhatuPrakriti(root, root), Lakara.LOT, TingPratyaya("सिप्", "सिप्"))
    private fun resolve(node: TingantaPada = verb(), relations: Set<SemanticRelation> = premises,
        recipients: List<ParticipantFacts> = listOf(deity), cases: Set<Vibhakti> = setOf(Vibhakti.SASTHI),
        lexical: Dhatu? = null): KarakaResolution {
        val p = ParticipantFacts("oblation", AvyayaPada("हविषः", "हविषः"), cases, semanticRelations = relations)
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(node.dhatu.mulaDhatu), p,
            listOf(p) + recipients, Prayoga.KARTARI, verbNode = node, baseDhatu = lexical))
    }

    @Test fun `bruhi and divadi presya oblations require exact ritual construction`() {
        for (node in listOf(verb(), verb(prefixes = emptyList()))) {
            val result = resolve(node)
            assertEquals(Karaka.SAMBANDHA, result.resolved)
            assertEquals(listOf("2.3.61"), result.evidence.map { it.sutra })
        }
        val lexical = DhatuPatha.all.single { it.id == "04.0022" }
        val presya = verb("इष्", listOf("प्र"))
        assertEquals(listOf("2.3.61"), resolve(presya, lexical = lexical).evidence.map { it.sutra })
        assertTrue(resolve(presya).evidence.none { it.sutra == "2.3.61" })
        assertTrue(resolve(presya, lexical = DhatuPatha.all.single { it.id == "09.0061" }).evidence.none { it.sutra == "2.3.61" })
        assertTrue(resolve(presya, lexical = DhatuPatha.all.single { it.id == "04.0108" }).evidence.none { it.sutra == "2.3.61" })
        for (prefixes in listOf(emptyList(), listOf("वि"), listOf("अज्ञात"))) {
            assertTrue(resolve(verb("इष्", prefixes), lexical = lexical).evidence.none { it.sutra == "2.3.61" })
        }
    }

    @Test fun `missing oblation deity recipient or imperative features cannot establish special genitive`() {
        for (missing in premises) {
            assertTrue(resolve(relations = premises - missing).evidence.none { it.sutra == "2.3.61" }, missing.name)
        }
        for (recipients in listOf(emptyList(), listOf(deity.copy(id = "oblation")),
            listOf(deity.copy(semanticRelations = setOf(SemanticRelation.RECIPIENT))),
            listOf(deity.copy(semanticRelations = setOf(SemanticRelation.DEITY_REFERENT))))) {
            assertTrue(resolve(recipients = recipients).evidence.none { it.sutra == "2.3.61" })
        }
        for (ending in listOf("तिप्", "थस्", "थ", "मिप्", "थास्")) {
            assertTrue(resolve(verb().copy(ting = TingPratyaya(ending, ending))).evidence.none { it.sutra == "2.3.61" }, ending)
        }
        assertTrue(resolve(verb().copy(lakara = Lakara.LAT)).evidence.none { it.sutra == "2.3.61" })
        assertTrue(resolve(verb().copy(vikarana = Vikarana.YAK)).evidence.none { it.sutra == "2.3.61" })
        assertTrue(resolve(verb().copy(dhatu = DhatuPrakriti("ब्रू", "ब्रू", listOf("णिच्")))).evidence.none { it.sutra == "2.3.61" })
        assertTrue(resolve(verb("हु", emptyList())).evidence.none { it.sutra == "2.3.61" })
    }

    @Test fun `vartika excludes an oblation already set forth from 2 3 61`() {
        val marked = premises + SemanticRelation.PRASTHITA_OBLATION
        assertTrue(resolve(relations = marked).evidence.none { it.sutra == "2.3.61" })
        assertTrue(resolve(relations = premises).evidence.any { it.sutra == "2.3.61" })
    }
}
