package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaPada
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SpecialSeshaGenitiveTest {
    @Test fun `nath wish genitive requires root object and sesha rather than blessing alone`() {
        val premises = setOf(SemanticRelation.BLESSING_HOPE_OBJECT,
            SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)
        for (root in listOf("नाथ्", "नाध्")) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                val result = resolve(root, premises, role = Karaka.KARMAN, voice = voice)
                assertEquals(Karaka.SAMBANDHA, result.resolved)
                assertEquals(Vibhakti.SASTHI, result.resolvedVibhakti)
                assertEquals(listOf("2.3.55"), result.evidence.map { it.sutra })
            }
            for (missing in premises) {
                assertTrue(resolve(root, premises - missing).evidence.none { it.sutra == "2.3.55" }, missing.name)
            }
            val ordinary = resolve(root, premises - SemanticRelation.SESHA_RELATION,
                Vibhakti.DVITIYA, Karaka.KARMAN)
            assertEquals(listOf("1.4.49", "2.3.2"), ordinary.evidence.map { it.sutra })
            val request = resolve(root, setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION))
            assertEquals(listOf("2.3.50"), request.evidence.map { it.sutra })
        }
        assertEquals(listOf("2.3.50"), resolve("कृ", premises).evidence.map { it.sutra })
        assertTrue(resolve("नाथ्", setOf(SemanticRelation.BENEDICTION_WELLBEING,
            SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)).evidence.none { it.sutra == "2.3.55" })
    }

    @Test fun `santapi exclusion follows structured sam tap nic construction`() {
        val verb = dev.panini.vyakaranam.ast.TingantaPada("संतापयति", listOf("सम्"),
            dev.panini.vyakaranam.ast.DhatuPrakriti("तप्", "तप्", listOf("णिच्")), dev.panini.core.Lakara.LAT,
            dev.panini.vyakaranam.ast.TingPratyaya("तिप्", "तिप्"))
        val context = VibhaktiRuleContext(Karaka.ANIRDHARITA, setOf(Vibhakti.SASTHI),
            dhatu = DhatuIdentity("तप्"), verbNode = verb)
        assertTrue(KarakaDhatuDomains.isExcludedDiseaseVerb(context))
        assertEquals(false, KarakaDhatuDomains.isExcludedDiseaseVerb(context.copy(verbNode = verb.copy(upasargas = emptyList()))))
    }

    private fun resolve(root: String, relations: Set<SemanticRelation>,
        case: Vibhakti = Vibhakti.SASTHI, role: Karaka = Karaka.ANIRDHARITA,
        voice: Prayoga = Prayoga.KARTARI,
        verb: dev.panini.vyakaranam.ast.TingantaPada? = null,
        others: List<ParticipantFacts> = emptyList()): KarakaResolution {
        val p = ParticipantFacts("dependent", AvyayaPada("dependent", "dependent"), setOf(case), semanticRelations = relations)
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p) + others, voice,
            candidates = setOf(role), verbNode = verb))
    }

    @Test fun `pratiyatna requires kr an existing substrate underlying object and relational construal`() {
        val premises = setOf(SemanticRelation.TRANSFORMATION_ENDOWMENT, SemanticRelation.EXISTING_SUBSTRATE,
            SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            val result = resolve("कृ", premises, role = Karaka.KARMAN, voice = voice)
            assertEquals(Karaka.SAMBANDHA, result.resolved)
            assertEquals(listOf("2.3.53"), result.evidence.map { it.sutra })
        }
        for (missing in premises) {
            assertTrue(resolve("कृ", premises - missing).evidence.none { it.sutra == "2.3.53" }, missing.name)
        }
        assertEquals(listOf("2.3.50"), resolve("पच्", premises).evidence.map { it.sutra })
        assertEquals(listOf("2.3.50"), resolve("कृ", premises - SemanticRelation.DESIRED_OBJECT +
            SemanticRelation.INSTRUMENT).evidence.map { it.sutra })
        val ordinary = resolve("कृ", premises - SemanticRelation.SESHA_RELATION, Vibhakti.DVITIYA, Karaka.KARMAN)
        assertEquals(listOf("1.4.49", "2.3.2"), ordinary.evidence.map { it.sutra })
    }

    @Test fun `rujartha genitive requires a distinct bhava agent and excludes jvar and santapi`() {
        val premises = setOf(SemanticRelation.DISEASE_PAIN_OBJECT,
            SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)
        val agent = ParticipantFacts("disease", AvyayaPada("रोगः", "रोगः"), setOf(Vibhakti.PRATHAMA),
            semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT, SemanticRelation.BHAVA_EVENT))
        for (root in listOf("रुज्", "अम्")) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                val expressedAgent = if (voice == Prayoga.KARMANI)
                    agent.copy(possibleVibhaktis = setOf(Vibhakti.TRTIYA)) else agent
                val result = resolve(root, premises, role = Karaka.KARMAN, voice = voice, others = listOf(expressedAgent))
                assertEquals(Karaka.SAMBANDHA, result.resolved)
                assertEquals(listOf("2.3.54"), result.evidence.map { it.sutra })
            }
            for (missing in premises) {
                assertTrue(resolve(root, premises - missing, others = listOf(agent)).evidence.none {
                    it.sutra == "2.3.54" }, missing.name)
            }
            for (others in listOf(emptyList(), listOf(agent.copy(id = "dependent")),
                listOf(agent.copy(semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT))),
                listOf(agent.copy(semanticRelations = setOf(SemanticRelation.BHAVA_EVENT))))) {
                assertTrue(resolve(root, premises, others = others).evidence.none { it.sutra == "2.3.54" })
            }
            val ordinary = resolve(root, premises - SemanticRelation.SESHA_RELATION,
                Vibhakti.DVITIYA, Karaka.KARMAN, others = listOf(agent))
            assertEquals(listOf("1.4.49", "2.3.2"), ordinary.evidence.map { it.sutra })
        }
        for (root in listOf("ज्वर्", "सन्तप्", "संताप्")) {
            assertTrue(KarakaDhatuDomains.isExcludedDiseaseVerb(VibhaktiRuleContext(Karaka.ANIRDHARITA,
                setOf(Vibhakti.SASTHI), dhatu = DhatuIdentity(root))))
            // The exclusion is from the specific rule, not a blanket ban on relational genitive.
            assertEquals(listOf("2.3.50"), resolve(root, premises, others = listOf(agent)).evidence.map { it.sutra })
        }
        assertEquals(listOf("2.3.50"), resolve("गम्", premises, others = listOf(agent)).evidence.map { it.sutra })
    }

    @Test fun `structured verb controls governing root and adhi prefix instead of a misleading raw alias`() {
        fun verb(root: String, prefixes: List<String>) = dev.panini.vyakaranam.ast.TingantaPada(
            "test", prefixes, dev.panini.vyakaranam.ast.DhatuPrakriti(root, root), dev.panini.core.Lakara.LAT,
            dev.panini.vyakaranam.ast.TingPratyaya("त", "त"))
        val remembering = setOf(SemanticRelation.REMEMBERING_SENSE,
            SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)
        assertEquals(listOf("2.3.52"), resolve("अधी", remembering,
            verb = verb("इ", listOf("अधि"))).evidence.map { it.sutra })
        for (prefixes in listOf(emptyList(), listOf("प्र"), listOf("अज्ञात"))) {
            assertTrue(resolve("अधी", remembering, verb = verb("इ", prefixes)).evidence.none { it.sutra == "2.3.52" })
        }
        val nonKnowledge = setOf(SemanticRelation.JNA_NON_KNOWLEDGE_SENSE,
            SemanticRelation.INSTRUMENT, SemanticRelation.SESHA_RELATION)
        assertTrue(resolve("ज्ञा", nonKnowledge, verb = verb("कृ", emptyList())).evidence.none { it.sutra == "2.3.51" })
    }

    @Test fun `non knowledge jna instrumental construed as relation gets specific genitive provenance`() {
        val premises = setOf(SemanticRelation.JNA_NON_KNOWLEDGE_SENSE, SemanticRelation.INSTRUMENT,
            SemanticRelation.SESHA_RELATION)
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            val result = resolve("ज्ञा", premises, role = Karaka.KARANA, voice = voice)
            assertEquals(Karaka.SAMBANDHA, result.resolved)
            assertEquals(Vibhakti.SASTHI, result.resolvedVibhakti)
            assertEquals(listOf("2.3.51"), result.evidence.map { it.sutra })
        }
        for (missing in premises) {
            assertTrue(resolve("ज्ञा", premises - missing).evidence.none { it.sutra == "2.3.51" }, missing.name)
        }
        assertEquals(listOf("2.3.50"), resolve("कृ", premises).evidence.map { it.sutra })
        val ordinary = resolve("ज्ञा", setOf(SemanticRelation.INSTRUMENT), Vibhakti.TRTIYA, Karaka.KARANA)
        assertEquals(listOf("1.4.42", "2.3.18"), ordinary.evidence.map { it.sutra })
        assertTrue(resolve("ज्ञा", setOf(SemanticRelation.MEMORY_OR_RULING_OBJECT)).evidence.none {
            it.sutra in setOf("2.3.51", "2.3.52") })
    }

    @Test fun `memory day ish objects require matching root sense and sesha construal`() {
        val domains = listOf("स्मृ" to SemanticRelation.REMEMBERING_SENSE,
            "अधी" to SemanticRelation.REMEMBERING_SENSE,
            "दय्" to SemanticRelation.DAY_DANA_GATI_RAKSHANA_SENSE,
            "ईश्" to SemanticRelation.ISH_AISHVARYA_SENSE)
        for ((root, sense) in domains) {
            val premises = setOf(sense, SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION)
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                val result = resolve(root, premises, role = Karaka.KARMAN, voice = voice)
                assertEquals(Karaka.SAMBANDHA, result.resolved)
                assertEquals(listOf("2.3.52"), result.evidence.map { it.sutra })
            }
            for (missing in premises) {
                assertTrue(resolve(root, premises - missing).evidence.none { it.sutra == "2.3.52" }, missing.name)
            }
            assertEquals(listOf("2.3.50"), resolve("कृ", premises).evidence.map { it.sutra })
            val instrument = premises - SemanticRelation.DESIRED_OBJECT + SemanticRelation.INSTRUMENT
            assertEquals(listOf("2.3.50"), resolve(root, instrument).evidence.map { it.sutra })
            val ordinary = resolve(root, setOf(sense, SemanticRelation.DESIRED_OBJECT),
                Vibhakti.DVITIYA, Karaka.KARMAN)
            assertEquals(listOf("1.4.49", "2.3.2"), ordinary.evidence.map { it.sutra })
        }
    }
}
