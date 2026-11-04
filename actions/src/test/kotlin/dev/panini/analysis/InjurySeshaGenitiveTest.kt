package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.DhatuPrakriti
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.TingPratyaya
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InjurySeshaGenitiveTest {
    private val premises = setOf(SemanticRelation.INJURY_VIOLENCE_OBJECT,
        SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)
    private fun verb(root: String, prefixes: List<String> = emptyList()) =
        TingantaPada("test", prefixes, DhatuPrakriti(root, root), Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
    private fun resolve(root: String, relations: Set<SemanticRelation> = premises,
        node: TingantaPada? = null, lexical: Dhatu? = null, voice: Prayoga = Prayoga.KARTARI,
        case: Vibhakti = Vibhakti.SASTHI): KarakaResolution {
        val p = ParticipantFacts("affected", AvyayaPada("चौरस्य", "चौरस्य"), setOf(case), semanticRelations = relations)
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p), voice,
            candidates = setOf(Karaka.KARMAN), verbNode = node, baseDhatu = lexical))
    }

    @Test fun `specified injury roots require violence object and sesha with precise provenance`() {
        for (root in listOf("नट्", "क्रथ्", "पिष्")) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                val result = resolve(root, voice = voice)
                assertEquals(Karaka.SAMBANDHA, result.resolved)
                assertEquals(Vibhakti.SASTHI, result.resolvedVibhakti)
                assertEquals(listOf("2.3.56"), result.evidence.map { it.sutra })
            }
            for (missing in premises) {
                assertTrue(resolve(root, premises - missing).evidence.none { it.sutra == "2.3.56" }, missing.name)
            }
            val ordinary = resolve(root, premises - SemanticRelation.SESHA_RELATION, case = Vibhakti.DVITIYA)
            assertEquals(listOf("1.4.49", "2.3.2"), ordinary.evidence.map { it.sutra })
        }
        assertEquals(listOf("2.3.50"), resolve("हिंस्").evidence.map { it.sutra })
        assertTrue(resolve("पिष्", premises - SemanticRelation.INJURY_VIOLENCE_OBJECT).evidence.none { it.sutra == "2.3.56" })
    }

    @Test fun `han permits ni pra combinations but excludes bare vi and unknown prefixes`() {
        for (prefixes in listOf(listOf("नि"), listOf("प्र"), listOf("नि", "प्र"), listOf("प्र", "नि"))) {
            assertEquals(listOf("2.3.56"), resolve("हन्", node = verb("हन्", prefixes)).evidence.map { it.sutra })
        }
        for (prefixes in listOf(emptyList(), listOf("वि"), listOf("उद्"), listOf("अज्ञात"), listOf("नि", "नि"))) {
            assertTrue(resolve("हन्", node = verb("हन्", prefixes)).evidence.none { it.sutra == "2.3.56" }, prefixes.toString())
        }
        // A misleading combined alias cannot override the structured vi-han AST.
        assertTrue(resolve("निप्रहन्", node = verb("हन्", listOf("वि"))).evidence.none { it.sutra == "2.3.56" })
    }

    @Test fun `jas requires curadi identity and excludes the divadi homonym and unknown metadata`() {
        for (id in listOf("10.0184", "10.0243")) {
            val lexical = DhatuPatha.all.single { it.id == id }
            assertEquals(listOf("2.3.56"), resolve("जस्", lexical = lexical).evidence.map { it.sutra })
        }
        val divadi = DhatuPatha.all.single { it.id == "04.0108" }
        assertTrue(resolve("जस्", lexical = divadi).evidence.none { it.sutra == "2.3.56" })
        assertTrue(resolve("जस्").evidence.none { it.sutra == "2.3.56" })
    }
}
