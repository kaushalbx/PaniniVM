package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.Prayoga
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.DhatuPrakriti
import dev.panini.vyakaranam.ast.TingPratyaya
import dev.panini.vyakaranam.ast.TingantaPada
import kotlin.test.Test
import kotlin.test.assertEquals

class KarakaInferenceTest {
    @Test
    fun `a mediating object hypothesis needs canonical object eligibility`() {
        val recipient = ParticipantFacts("recipient", AvyayaPada("recipient", "recipient"),
            setOf(Vibhakti.SASTHI), semanticRelations = setOf(SemanticRelation.RECIPIENT),
            intendedObject = IntendedObject.Written("object"))
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            fun resolve(case: Vibhakti, source: SemanticEvidenceSource): KarakaResolution {
                val obj = ParticipantFacts("object", AvyayaPada("object", "object"), setOf(case),
                    semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT),
                    semanticEvidence = setOf(SemanticRelationEvidence(SemanticRelation.DESIRED_OBJECT,
                        source, "Mediating object reading.")))
                return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("दा"), recipient,
                    listOf(recipient, obj), voice))
            }
            val unsupported = resolve(Vibhakti.SASTHI, SemanticEvidenceSource.UNAMBIGUOUS_MORPHOLOGY)
            assertEquals(null, unsupported.resolved, voice.name)
            assertEquals(emptyList(), unsupported.evidence, voice.name)
            // Known object meaning can survive its own invalid ending.
            val known = resolve(Vibhakti.SASTHI, SemanticEvidenceSource.CLAUSE_CONTEXT)
            assertEquals(Karaka.SAMPRADANA, known.resolved, voice.name)
            assertEquals(null, known.resolvedVibhakti, voice.name)
            assertEquals(listOf("1.4.32"), known.evidence.map { it.sutra }, voice.name)
            // Case and voice propose an object without establishing its meaning.
            val ordinary = resolve(if (voice == Prayoga.KARTARI) Vibhakti.DVITIYA else Vibhakti.PRATHAMA,
                SemanticEvidenceSource.VOICE_CONSTRUCTION)
            assertEquals(null, ordinary.resolved, voice.name)
            assertEquals(emptyList(), ordinary.evidence, voice.name)
        }
    }

    @Test
    fun `late recipient rules require established meaning independently of candidates`() {
        for ((number, root, prefixes) in listOf(
                Triple("1.4.39", "राध्", emptyList()),
                Triple("1.4.40", "श्रु", listOf("प्रति")),
                Triple("1.4.41", "गॄ", listOf("अनु")))) {
            val rule = KarakaRuleEngine.karakaRules.single { it.number == number }
            val premises = if (number == "1.4.39") setOf(SemanticRelation.INQUIRY_DESTINY_TARGET)
                else setOf(SemanticRelation.PRIOR_ACTION_AGENT)
            val p = ParticipantFacts(number, AvyayaPada(number, number), setOf(Vibhakti.TRTIYA),
                semanticRelations = premises)
            val verb = TingantaPada("verb", prefixes, DhatuPrakriti(root, root),
                Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
            for (candidates in listOf(setOf(Karaka.SAMPRADANA), setOf(Karaka.KARANA), setOf(Karaka.SAMBANDHA))) {
                val context = KarakaRuleContext(DhatuIdentity(root), p, listOf(p), Prayoga.KARTARI,
                    candidates = candidates, verbNode = verb, baseDhatu = findMockDhatu(root))
                if (number == "1.4.41") {
                    assertEquals(false, rule.matches(context.copy(baseDhatu = null)),
                        "The spelling alone cannot choose the praise homonym.")
                }
                val result = KarakaRuleEngine.resolve(context)
                assertEquals(Karaka.SAMPRADANA, result.resolved, number)
                assertEquals(null, result.resolvedVibhakti, number)
                assertEquals(listOf(number), result.evidence.map { it.sutra }, number)
                for (weakened in premises.map { setOf(it) } + listOf(premises)) {
                    val weak = p.copy(semanticEvidence = premises.mapTo(mutableSetOf()) {
                        SemanticRelationEvidence(it, if (it in weakened) SemanticEvidenceSource.UNAMBIGUOUS_MORPHOLOGY
                            else SemanticEvidenceSource.CLAUSE_CONTEXT, "A hypothesis is not the required reading.")
                    })
                    assertEquals(false, rule.matches(context.copy(participant = weak,
                        allParticipants = listOf(weak))), "$number: $weakened")
                }
            }
        }
    }

    @Test
    fun `late recipient construction belongs to its nominal action rather than the outer verb`() {
        for ((number, root, prefixes) in listOf(
                Triple("1.4.39", "राध्", emptyList()),
                Triple("1.4.40", "श्रु", listOf("आङ्")),
                Triple("1.4.41", "गॄ", listOf("प्रति")))) {
            val rule = KarakaRuleEngine.karakaRules.single { it.number == number }
            val premises = if (number == "1.4.39") setOf(SemanticRelation.INQUIRY_DESTINY_TARGET)
                else setOf(SemanticRelation.PRIOR_ACTION_AGENT)
            val p = ParticipantFacts(number, AvyayaPada(number, number), setOf(Vibhakti.CHATURTHI),
                semanticRelations = premises)
            val outer = TingantaPada("outer", emptyList(), DhatuPrakriti("पठ्", "पठ्"),
                Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
            val nominal = dev.panini.vyakaranam.ast.KridantaPratipadika("nominal action", prefixes,
                DhatuPrakriti(root, root), "ल्युट्")
            val context = KarakaRuleContext(DhatuIdentity(root), p, listOf(p), Prayoga.KARTARI,
                verbNode = outer, baseDhatu = findMockDhatu(root), governingKrt = nominal,
                governingKrtExpressedRoles = emptySet())
            assertEquals(listOf(number, "2.3.13"), KarakaRuleEngine.resolve(context).evidence.map { it.sutra }, number)
            val matchingOuter = outer.copy(upasargas = prefixes, dhatu = DhatuPrakriti(root, root))
            val wrongNominal = nominal.copy(dhatu = DhatuPrakriti("पठ्", "पठ्"))
            assertEquals(false, rule.matches(context.copy(verbNode = matchingOuter,
                governingKrt = wrongNominal, baseDhatu = findMockDhatu("पठ्"))), number)
        }
    }

    @Test
    fun `special recipient premises must be established independently of case`() {
        val constructions = listOf(
            Triple("1.4.33", "रुच्", SemanticRelation.PLEASED_EXPERIENCER),
            Triple("1.4.34", "श्लाघ", SemanticRelation.INTENDED_TO_BE_INFORMED),
            Triple("1.4.35", "धृ", SemanticRelation.CREDITOR),
            Triple("1.4.36", "स्पृह", SemanticRelation.LONGED_FOR_OBJECT),
            Triple("1.4.37", "क्रुध्", SemanticRelation.ANGER_TARGET),
            Triple("1.4.38", "क्रुध्", SemanticRelation.ANGER_TARGET),
        )
        for ((number, root, specialMeaning) in constructions) {
            val rule = KarakaRuleEngine.karakaRules.single { it.number == number }
            val premises = setOf(specialMeaning)
            val p = ParticipantFacts(number, AvyayaPada(number, number), setOf(Vibhakti.TRTIYA),
                semanticRelations = premises)
            val verb = TingantaPada("verb", if (number == "1.4.38") listOf("अभि") else emptyList(),
                DhatuPrakriti(root, root, if (number == "1.4.35") listOf("णिच्") else emptyList()),
                Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
            for (candidates in listOf(setOf(Karaka.SAMPRADANA), setOf(Karaka.KARMAN), setOf(Karaka.KARANA))) {
                val context = KarakaRuleContext(DhatuIdentity(root), p, listOf(p), Prayoga.KARTARI,
                    candidates = candidates, verbNode = verb)
                assertEquals(true, rule.matches(context), number)
                for (weakened in premises.map { setOf(it) } + listOf(premises)) {
                    val weak = p.copy(semanticEvidence = premises.mapTo(mutableSetOf()) {
                        SemanticRelationEvidence(it, if (it in weakened) SemanticEvidenceSource.UNAMBIGUOUS_MORPHOLOGY
                            else SemanticEvidenceSource.CLAUSE_CONTEXT, "Case alone cannot establish this meaning.")
                    })
                    assertEquals(false, rule.matches(context.copy(participant = weak,
                        allParticipants = listOf(weak))), "$number: $weakened")
                }
            }
        }
    }

    @Test
    fun `a case hypothesis cannot establish an incompatible ordinary meaning`() {
        for (meaning in listOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.INDEPENDENT_AGENT,
                SemanticRelation.INSTRUMENT, SemanticRelation.LOCATION)) {
            val weak = ParticipantFacts("participant", AvyayaPada("participant", "participant"),
                setOf(Vibhakti.TRTIYA), semanticRelations = setOf(meaning),
                semanticEvidence = setOf(SemanticRelationEvidence(meaning,
                    SemanticEvidenceSource.UNAMBIGUOUS_MORPHOLOGY, "Case hypothesis only.")))
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("पच्"), weak,
                    listOf(weak), voice))
                assertEquals(null, result.resolved)
                assertEquals(emptyList(), result.evidence)
            }
        }
    }

    private fun assertUnvalidatedAgent(result: KarakaResolution) {
        assertEquals(Karaka.KARTR, result.resolved)
        assertEquals(null, result.resolvedVibhakti)
        assertEquals(listOf("1.4.54"), result.evidence.map { it.sutra })
        assertEquals(emptyList(), result.alternatives)
    }

    @Test
    fun `prthag vina nana retains three cases without inventing an apadana`() {
        val premises = setOf(SemanticRelation.EXCLUSION_VINA, SemanticRelation.PRTHAG_VINA_NANA_LINK)
        fun resolve(cases: Set<Vibhakti>, relations: Set<SemanticRelation> = premises,
            voice: Prayoga = Prayoga.KARTARI): KarakaResolution {
            val p = ParticipantFacts("excluded", AvyayaPada("excluded", "excluded"), cases,
                semanticRelations = relations)
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("गम्"), p, listOf(p), voice))
        }
        val allowed = setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA, Vibhakti.PANCHAMI)
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            for (case in allowed) {
                val result = resolve(setOf(case), voice = voice)
                assertEquals(Karaka.ANIRDHARITA, result.resolved)
                assertEquals(case, result.resolvedVibhakti)
                assertEquals(listOf("2.3.32"), result.evidence.map { it.sutra })
                assertEquals(allowed - case, result.alternatives.map { it.vibhakti }.toSet())
                assertEquals(true, result.alternatives.all { !it.matchesObservedMorphology })
            }
        }
        val syncretic = resolve(allowed)
        assertEquals(null, syncretic.resolvedVibhakti)
        assertEquals(2, syncretic.alternatives.count { it.matchesObservedMorphology })
        for (missing in premises) {
            assertEquals(null, resolve(allowed, premises - missing).resolvedVibhakti, missing.name)
        }
        assertEquals(null, resolve(setOf(Vibhakti.SAPTAMI)).resolvedVibhakti)
    }

    @Test
    fun `superiority and sovereignty locatives require matching karmapravacaniya links`() {
        fun resolve(cases: Set<Vibhakti>, relations: Set<SemanticRelation>): KarakaResolution {
            val p = ParticipantFacts("reference", AvyayaPada("reference", "reference"), cases, semanticRelations = relations)
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("भू"), p, listOf(p), Prayoga.KARTARI))
        }
        val governance = SemanticRelation.KARMAPRAVACANIYA_GOVERNANCE
        for ((meaning, link) in listOf(
            SemanticRelation.SUPERIORITY_REFERENCE to SemanticRelation.UPA_SUPERIORITY_KARMAPRAVACANIYA_LINK,
            SemanticRelation.SOVEREIGNTY_REFERENCE to SemanticRelation.ADHI_SOVEREIGNTY_KARMAPRAVACANIYA_LINK)) {
            val premises = setOf(governance, meaning, link)
            val result = resolve(setOf(Vibhakti.DVITIYA, Vibhakti.SAPTAMI), premises)
            assertEquals(listOf("2.3.9"), result.evidence.map { it.sutra })
            assertEquals(Vibhakti.SAPTAMI, result.resolvedVibhakti)
            assertEquals(null, resolve(setOf(Vibhakti.DVITIYA), premises).resolvedVibhakti)
            for (missing in premises) {
                assertEquals(null, resolve(setOf(Vibhakti.SAPTAMI), premises - missing).resolvedVibhakti, missing.name)
            }
        }
        assertEquals(null, resolve(setOf(Vibhakti.SAPTAMI), setOf(governance,
            SemanticRelation.SOVEREIGNTY_REFERENCE, SemanticRelation.UPA_SUPERIORITY_KARMAPRAVACANIYA_LINK)).resolvedVibhakti)
    }

    @Test
    fun `ap ang pari ablative requires exclusion limit construction and does not swallow other pari senses`() {
        val premises = setOf(SemanticRelation.EXCLUSION_LIMIT, SemanticRelation.KARMAPRAVACANIYA_GOVERNANCE,
            SemanticRelation.AP_ANG_PARI_KARMAPRAVACANIYA_LINK)
        fun resolve(cases: Set<Vibhakti>, relations: Set<SemanticRelation> = premises): KarakaResolution {
            val p = ParticipantFacts("reference", AvyayaPada("reference", "reference"), cases, semanticRelations = relations)
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("वृष्"), p, listOf(p), Prayoga.KARTARI))
        }
        val result = resolve(setOf(Vibhakti.DVITIYA, Vibhakti.PANCHAMI))
        assertEquals(listOf("2.3.10"), result.evidence.map { it.sutra })
        assertEquals(Vibhakti.PANCHAMI, result.resolvedVibhakti)
        assertEquals(Karaka.ANIRDHARITA, result.resolved)
        assertEquals(null, resolve(setOf(Vibhakti.DVITIYA)).resolvedVibhakti)
        for (missing in premises) {
            assertEquals(null, resolve(setOf(Vibhakti.PANCHAMI), premises - missing).resolvedVibhakti, missing.name)
        }
        val nonExclusionPari = resolve(setOf(Vibhakti.DVITIYA), premises - SemanticRelation.EXCLUSION_LIMIT)
        assertEquals(listOf("2.3.8"), nonExclusionPari.evidence.map { it.sutra })
    }

    @Test
    fun `prati exchange ablative belongs to original reference and supersedes general accusative`() {
        val premises = setOf(SemanticRelation.REPRESENTATIVE_EXCHANGE,
            SemanticRelation.REPRESENTATIVE_EXCHANGE_REFERENCE, SemanticRelation.KARMAPRAVACANIYA_GOVERNANCE,
            SemanticRelation.PRATI_KARMAPRAVACANIYA_LINK)
        fun resolve(cases: Set<Vibhakti>, relations: Set<SemanticRelation> = premises): KarakaResolution {
            val p = ParticipantFacts("original", AvyayaPada("तिलेभ्यः", "तिलेभ्यः"), cases, semanticRelations = relations)
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("दा"), p, listOf(p), Prayoga.KARTARI))
        }
        val result = resolve(setOf(Vibhakti.DVITIYA, Vibhakti.PANCHAMI))
        assertEquals(Karaka.ANIRDHARITA, result.resolved)
        assertEquals(Vibhakti.PANCHAMI, result.resolvedVibhakti)
        assertEquals(listOf("2.3.11"), result.evidence.map { it.sutra })
        assertEquals(null, resolve(setOf(Vibhakti.DVITIYA)).resolvedVibhakti)
        for (missing in premises) {
            assertEquals(null, resolve(setOf(Vibhakti.PANCHAMI), premises - missing).resolvedVibhakti, missing.name)
        }
        assertEquals(listOf("2.3.8"), resolve(setOf(Vibhakti.DVITIYA),
            setOf(SemanticRelation.KARMAPRAVACANIYA_GOVERNANCE)).evidence.map { it.sutra })
        assertEquals(null, resolve(setOf(Vibhakti.DVITIYA), emptySet()).resolvedVibhakti)
    }

    @Test
    fun `karaka power interval supports both cases without requiring two distinct entities`() {
        val premises = setOf(SemanticRelation.INTERVENING_DURATION_DISTANCE, SemanticRelation.KARAKA_POWER_INTERVAL)
        fun resolve(cases: Set<Vibhakti>, relations: Set<SemanticRelation> = premises): KarakaResolution {
            val p = ParticipantFacts("interval", AvyayaPada("द्व्यह", "द्व्यह"), cases, semanticRelations = relations)
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("भुज्"), p, listOf(p), Prayoga.KARTARI))
        }
        for (case in listOf(Vibhakti.SAPTAMI, Vibhakti.PANCHAMI)) {
            val result = resolve(setOf(case))
            assertEquals(Karaka.ANIRDHARITA, result.resolved)
            assertEquals(case, result.resolvedVibhakti)
            assertEquals(listOf("2.3.7"), result.evidence.map { it.sutra })
            assertEquals(listOf("2.3.7"), result.alternatives.single().evidence.map { it.sutra })
            assertEquals(false, result.alternatives.single().matchesObservedMorphology)
        }
        val both = resolve(setOf(Vibhakti.SAPTAMI, Vibhakti.PANCHAMI))
        assertEquals(null, both.resolvedVibhakti)
        assertEquals(true, both.alternatives.single().matchesObservedMorphology)
        for (missing in premises) {
            assertEquals(null, resolve(setOf(Vibhakti.SAPTAMI, Vibhakti.PANCHAMI), premises - missing).resolved)
        }
    }

    @Test
    fun `antara accusative requires a direct nipata link rather than generic between semantics`() {
        val relations = setOf(SemanticRelation.BETWEEN_OR_WITHOUT, SemanticRelation.ANTARA_NIPATA_LINK)
        fun resolve(cases: Set<Vibhakti>, meanings: Set<SemanticRelation>): KarakaResolution {
            val p = ParticipantFacts("dependent", AvyayaPada("dependent", "dependent"), cases, semanticRelations = meanings)
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("स्था"), p, listOf(p), Prayoga.KARTARI))
        }
        val linked = resolve(setOf(Vibhakti.DVITIYA, Vibhakti.SASTHI), relations)
        assertEquals(Karaka.ANIRDHARITA, linked.resolved)
        assertEquals(Vibhakti.DVITIYA, linked.resolvedVibhakti)
        assertEquals(listOf("2.3.4"), linked.evidence.map { it.sutra })
        for (missing in relations) {
            assertEquals(null, resolve(setOf(Vibhakti.DVITIYA), relations - missing).resolvedVibhakti)
        }
        val possessor = resolve(setOf(Vibhakti.SASTHI), setOf(SemanticRelation.BETWEEN_OR_WITHOUT,
            SemanticRelation.SESHA_RELATION))
        assertEquals(Karaka.SAMBANDHA, possessor.resolved)
        assertEquals(listOf("2.3.50"), possessor.evidence.map { it.sutra })
        assertEquals(false, resolve(setOf(Vibhakti.PRATHAMA), relations).evidence.any { it.sutra == "2.3.4" })
    }

    @Test
    fun `duration and completion cases require full extent rather than bare accusative or completion`() {
        fun resolve(relations: Set<SemanticRelation>, cases: Set<Vibhakti>): KarakaResolution {
            val p = ParticipantFacts("extent", AvyayaPada("मास", "मास"), cases, semanticRelations = relations)
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("अधी"), p, listOf(p), Prayoga.KARTARI))
        }
        val extent = setOf(SemanticRelation.CONTINUOUS_TIME_DISTANCE_EXTENT)
        assertEquals(null, resolve(emptySet(), setOf(Vibhakti.DVITIYA)).resolvedVibhakti)
        assertEquals(listOf("2.3.5"), resolve(extent, setOf(Vibhakti.DVITIYA)).evidence.map { it.sutra })
        assertEquals(null, resolve(setOf(SemanticRelation.COMPLETION_LIMIT), setOf(Vibhakti.TRTIYA)).resolvedVibhakti)
        val completed = extent + SemanticRelation.COMPLETION_LIMIT
        val result = resolve(completed, setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA))
        assertEquals(Vibhakti.TRTIYA, result.resolvedVibhakti)
        assertEquals(listOf("2.3.6"), result.evidence.map { it.sutra })
        assertEquals(null, resolve(completed, setOf(Vibhakti.DVITIYA)).resolvedVibhakti)
        assertEquals(emptyList(), resolve(emptySet(), setOf(Vibhakti.SASTHI)).evidence)
    }

    @Test
    fun `identifying mark requires positive relation and is not every instrumental`() {
        fun resolve(relations: Set<SemanticRelation>): KarakaResolution {
            val p = ParticipantFacts("mark", AvyayaPada("कमण्डलुना", "कमण्डलुना"),
                setOf(Vibhakti.TRTIYA), semanticRelations = relations)
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("दृश्"), p, listOf(p), Prayoga.KARTARI))
        }
        assertEquals(null, resolve(emptySet()).resolved)
        val mark = resolve(setOf(SemanticRelation.CHARACTERISTIC_MARK))
        assertEquals(Karaka.ANIRDHARITA, mark.resolved)
        assertEquals(Vibhakti.TRTIYA, mark.resolvedVibhakti)
        assertEquals(listOf("2.3.21"), mark.evidence.map { it.sutra })
        assertEquals(null, resolve(setOf(SemanticRelation.CHARACTERISTIC_MARK,
            SemanticRelation.MARK_INCORPORATED_IN_COMPOUND)).resolvedVibhakti)
        assertEquals(listOf("1.4.42", "2.3.18"), resolve(setOf(SemanticRelation.INSTRUMENT)).evidence.map { it.sutra })
    }

    @Test
    fun `passive nic retains causative samjna and expresses causee karman in nominative`() {
        fun resolve(root: String, p: ParticipantFacts, nic: Boolean = true): KarakaResolution {
            val verb = TingantaPada("passive", emptyList(),
                DhatuPrakriti(root, root, if (nic) listOf("णिच्") else emptyList()),
                Lakara.LAT, TingPratyaya("त", "त"), vikarana = dev.panini.vyakaranam.ast.Vikarana.YAK)
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p),
                Prayoga.KARMANI, verbNode = verb))
        }
        val causee = ParticipantFacts("causee", AvyayaPada("causee", "causee"), setOf(Vibhakti.PRATHAMA),
            semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT))
        assertEquals(listOf("1.4.52", "2.3.46"), resolve("गम्", causee).evidence.map { it.sutra })
        assertEquals(null, resolve("गम्", causee, false).resolved)
        assertEquals(null, resolve("दृश्", causee).resolved,
            "Passive ātmanepada ending alone does not license the lexical-pada vārttika")
        val exceptional = resolve("कृ", causee)
        assertEquals(listOf("1.4.53", "2.3.46"), exceptional.evidence.map { it.sutra })
        assertEquals(listOf("1.4.54", "2.3.18"), exceptional.alternatives.single().evidence.map { it.sutra })
        val causer = causee.copy(possibleVibhaktis = setOf(Vibhakti.TRTIYA),
            semanticRelations = setOf(SemanticRelation.PROMPTER_CAUSE))
        assertEquals(listOf("1.4.55", "2.3.18"), resolve("गम्", causer).evidence.map { it.sutra })
        val prior = causee.copy(semanticRelations = causee.semanticRelations + SemanticRelation.PRIOR_CAUSATIVE_AGENT)
        assertEquals(null, resolve("गम्", prior).resolved)
    }

    @Test
    fun `canonical only analysis does not add abhivad drsh varttika extension`() {
        fun resolve(root: String, ending: String, salutation: Boolean = true,
            priorCausative: Boolean = false): KarakaResolution {
            val p = ParticipantFacts("causee", AvyayaPada("causee", "causee"),
                setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA),
                semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT, SemanticRelation.INDEPENDENT_AGENT) +
                    (if (salutation) setOf(SemanticRelation.ABHIVAD_SALUTATION_SENSE) else emptySet()) +
                    (if (priorCausative) setOf(SemanticRelation.PRIOR_CAUSATIVE_AGENT) else emptySet()))
            val verb = TingantaPada("verb", if (root == "वद्") listOf("अभि") else emptyList(),
                DhatuPrakriti(root, root, listOf("णिच्")), Lakara.LAT, TingPratyaya(ending, ending))
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p),
                Prayoga.CAUSATIVE, verbNode = verb))
        }
        for (root in listOf("वद्", "दृश्")) {
            val result = resolve(root, "त")
            assertEquals(listOf("1.4.54", "2.3.18"), result.evidence.map { it.sutra }, root)
            assertEquals(Karaka.KARTR, result.resolved, root)
            assertEquals(emptyList(), result.alternatives)
            assertEquals(listOf("1.4.54", "2.3.18"), resolve(root, "त", priorCausative = true).evidence.map { it.sutra })
        }
        assertEquals(listOf("1.4.52", "2.3.2"), resolve("दृश्", "तिप्").evidence.map { it.sutra })
        assertEquals(listOf("1.4.54", "2.3.18"), resolve("वद्", "तिप्").evidence.map { it.sutra })
        assertEquals(false, resolve("वद्", "त", salutation = false).evidence.any { it.sutra == "1.4.53V" })
    }

    @Test
    fun `prior causative agent cannot use ani kartr rules in outer causative`() {
        for (root in listOf("गम्", "कृ")) {
            fun resolve(cases: Set<Vibhakti>): KarakaResolution {
                val p = ParticipantFacts("outer-causee", AvyayaPada("outer-causee", "outer-causee"), cases,
                    semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT, SemanticRelation.INDEPENDENT_AGENT,
                        SemanticRelation.PRIOR_CAUSATIVE_AGENT))
                val verb = TingantaPada("nested", emptyList(), DhatuPrakriti(root, root, listOf("णिच्", "णिच्")),
                    Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
                return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p),
                    Prayoga.CAUSATIVE, verbNode = verb))
            }
            assertUnvalidatedAgent(resolve(setOf(Vibhakti.DVITIYA)))
            val instrumental = resolve(setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA))
            assertEquals(listOf("1.4.54", "2.3.18"), instrumental.evidence.map { it.sutra }, root)
            assertEquals(Vibhakti.TRTIYA, instrumental.resolvedVibhakti, root)
            assertEquals(emptyList(), instrumental.alternatives, root)
        }
    }

    @Test
    fun `vah causee respects niyantr construction rather than generic motion class`() {
        fun resolve(construction: Set<SemanticRelation>, cases: Set<Vibhakti>): KarakaResolution {
            val p = ParticipantFacts("causee", AvyayaPada("causee", "causee"), cases,
                semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT, SemanticRelation.INDEPENDENT_AGENT) + construction)
            val verb = TingantaPada("verb", emptyList(), DhatuPrakriti("वह्", "वह्", listOf("णिच्")),
                Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
            val lexical = object : Dhatu(id = "vah", krama = 1, upadesha = "वह्", sourceSurface = "वह्",
                artha = "प्रापणे", arthaHindi = "ले जाना", arthaEnglish = "carry", gana = dev.panini.core.DhatuGana.BHVADI) {
                override val semanticRelations = setOf(SemanticRelation.MOTION)
            }
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("वह्"), p, listOf(p),
                Prayoga.CAUSATIVE, verbNode = verb, baseDhatu = lexical))
        }
        val licensed = setOf(SemanticRelation.VAH_NIYANTR_KARTRKA)
        val excluded = setOf(SemanticRelation.VAH_ANIYANTR_KARTRKA)
        val competing = setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA)
        assertEquals(listOf("1.4.52", "2.3.2"), resolve(licensed, competing).evidence.map { it.sutra })
        assertEquals(listOf("1.4.54", "2.3.18"), resolve(excluded, competing).evidence.map { it.sutra })
        assertUnvalidatedAgent(resolve(emptySet(), setOf(Vibhakti.DVITIYA)))
        assertUnvalidatedAgent(resolve(licensed + excluded, setOf(Vibhakti.DVITIYA)))
        assertUnvalidatedAgent(resolve(excluded, setOf(Vibhakti.DVITIYA)))
    }

    @Test
    fun `bhaks causee requires injurious sense and noninjurious eating retains instrumental agent`() {
        fun resolve(senses: Set<SemanticRelation>, cases: Set<Vibhakti>): KarakaResolution {
            val p = ParticipantFacts("causee", AvyayaPada("causee", "causee"), cases,
                semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT, SemanticRelation.INDEPENDENT_AGENT) + senses)
            val verb = TingantaPada("verb", emptyList(), DhatuPrakriti("भक्ष्", "भक्ष्", listOf("णिच्")),
                Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
            val lexical = object : Dhatu(id = "bhaks", krama = 1, upadesha = "भक्ष्", sourceSurface = "भक्ष्",
                artha = "अदने", arthaHindi = "खाना", arthaEnglish = "eat", gana = dev.panini.core.DhatuGana.CURADI) {
                override val semanticRelations = setOf(SemanticRelation.CONSUMPTION)
            }
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("भक्ष्"), p, listOf(p),
                Prayoga.CAUSATIVE, verbNode = verb, baseDhatu = lexical))
        }
        val competing = setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA)
        val harmful = setOf(SemanticRelation.BHAKS_INJURIOUS_SENSE)
        assertEquals(listOf("1.4.52", "2.3.2"), resolve(harmful, competing).evidence.map { it.sutra })
        val harmless = setOf(SemanticRelation.BHAKS_NONINJURIOUS_SENSE)
        assertEquals(listOf("1.4.54", "2.3.18"), resolve(harmless, competing).evidence.map { it.sutra })
        assertUnvalidatedAgent(resolve(emptySet(), setOf(Vibhakti.DVITIYA)))
        assertUnvalidatedAgent(resolve(harmful + harmless, setOf(Vibhakti.DVITIYA)))
        assertUnvalidatedAgent(resolve(harmless, setOf(Vibhakti.DVITIYA)))
    }

    @Test
    fun `causative ni ad khad exceptions override broad motion and consumption classes`() {
        for ((root, category) in listOf("नी" to SemanticRelation.MOTION,
                "अद्" to SemanticRelation.CONSUMPTION, "खाद्" to SemanticRelation.CONSUMPTION)) {
            fun resolve(cases: Set<Vibhakti>): KarakaResolution {
                val p = ParticipantFacts("causee", AvyayaPada("causee", "causee"), cases,
                    semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT, SemanticRelation.INDEPENDENT_AGENT))
                val verb = TingantaPada("causative", emptyList(), DhatuPrakriti(root, root, listOf("णिच्")),
                    Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
                val lexical = object : Dhatu(id = root, krama = 1, upadesha = root, sourceSurface = root,
                    artha = "test", arthaHindi = "परीक्षण", arthaEnglish = "test",
                    gana = dev.panini.core.DhatuGana.BHVADI) {
                    override val semanticRelations = setOf(category)
                }
                return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p),
                    Prayoga.CAUSATIVE, verbNode = verb, baseDhatu = lexical))
            }
            assertUnvalidatedAgent(resolve(setOf(Vibhakti.DVITIYA)))
            val competing = resolve(setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA))
            assertEquals(Karaka.KARTR, competing.resolved, root)
            assertEquals(Vibhakti.TRTIYA, competing.resolvedVibhakti, root)
            assertEquals(listOf("1.4.54", "2.3.18"), competing.evidence.map { it.sutra }, root)
        }
    }

    @Test
    fun `akathita inventory covers Kasika and Kaumudi roots without swallowing tool or locus`() {
        val secondary = ParticipantFacts("secondary", AvyayaPada("secondary", "secondary"),
            setOf(Vibhakti.DVITIYA), semanticRelations = setOf(SemanticRelation.SECONDARY_OBJECT))
        fun resolve(root: String, p: ParticipantFacts) = KarakaRuleEngine.resolve(KarakaRuleContext(
            DhatuIdentity(root), p, listOf(p), Prayoga.KARTARI))
        for (root in listOf("दुह्", "याच्", "रुध्", "प्रच्छ्", "भिक्ष्", "चि", "ब्रू", "शास्",
                "पच्", "दण्ड्", "जि", "मथ्", "मन्थ्", "मुष्", "नी", "हृ", "कृष्", "वह्")) {
            assertEquals(listOf("1.4.51", "2.3.2"), resolve(root, secondary).evidence.map { it.sutra }, root)
            assertEquals(null, resolve(root, secondary.copy(semanticRelations = emptySet())).resolved, root)
        }
        val tool = secondary.copy(possibleVibhaktis = setOf(Vibhakti.TRTIYA),
            semanticRelations = setOf(SemanticRelation.INSTRUMENT))
        assertEquals(listOf("1.4.42", "2.3.18"), resolve("दुह्", tool).evidence.map { it.sutra })
        val vessel = secondary.copy(possibleVibhaktis = setOf(Vibhakti.SAPTAMI),
            semanticRelations = setOf(SemanticRelation.LOCATION))
        assertEquals(listOf("1.4.45", "2.3.36"), resolve("दुह्", vessel).evidence.map { it.sutra })
        for (root in listOf("भिक्षकल्प्", "पचकल्प्", "दण्डकल्प्")) {
            assertEquals(null, resolve(root, secondary).resolved, root)
        }
    }

    @Test
    fun `abhinivesha conditioned locative usage is not unrestricted free case variation`() {
        fun resolve(locativeUsage: Boolean, cases: Set<Vibhakti>): KarakaResolution {
            val p = ParticipantFacts("locus", AvyayaPada("locus", "locus"), cases,
                semanticRelations = setOf(SemanticRelation.LOCATION) +
                    if (locativeUsage) setOf(SemanticRelation.ABHINIVESH_LOCATIVE_USAGE) else emptySet())
            val verb = TingantaPada("verb", listOf("अभि", "नि"), DhatuPrakriti("विश्", "विश्"),
                Lakara.LAT, TingPratyaya("त", "त"))
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("विश्"), p, listOf(p),
                Prayoga.KARTARI, verbNode = verb))
        }
        val competing = setOf(Vibhakti.DVITIYA, Vibhakti.SAPTAMI)
        assertEquals(listOf("1.4.47", "2.3.2"), resolve(false, competing).evidence.map { it.sutra })
        val locative = resolve(true, competing)
        assertEquals(Karaka.ADHIKARANA, locative.resolved)
        assertEquals(listOf("1.4.45", "2.3.36"), locative.evidence.map { it.sutra })
        assertEquals(emptyList(), locative.alternatives)
        val wrongLocativeCase = resolve(true, setOf(Vibhakti.DVITIYA))
        assertEquals(Karaka.ADHIKARANA, wrongLocativeCase.resolved)
        assertEquals(listOf("1.4.45"), wrongLocativeCase.evidence.map { it.sutra })
        assertEquals(null, wrongLocativeCase.resolvedVibhakti)
        val incompatibleCase = resolve(false, setOf(Vibhakti.SAPTAMI))
        assertEquals(Karaka.KARMAN, incompatibleCase.resolved)
        assertEquals(listOf("1.4.47"), incompatibleCase.evidence.map { it.sutra })
        assertEquals(null, incompatibleCase.resolvedVibhakti)
    }

    @Test
    fun `vas locus exception selects bhvadi residence rather than adadi covering homonym`() {
        val p = ParticipantFacts("locus", AvyayaPada("locus", "locus"),
            setOf(Vibhakti.DVITIYA, Vibhakti.SAPTAMI), semanticRelations = setOf(SemanticRelation.LOCATION))
        fun resolve(gana: dev.panini.core.DhatuGana): KarakaResolution {
            val verb = TingantaPada("verb", listOf("उप"), DhatuPrakriti("वस्", "वस्"),
                Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
            val root = Dhatu(id = "vas.$gana", krama = 1, upadesha = "वसँ", sourceSurface = "वस्",
                artha = "test", arthaHindi = "परीक्षण", arthaEnglish = "test", gana = gana)
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("वस्"), p, listOf(p),
                Prayoga.KARTARI, verbNode = verb, baseDhatu = root))
        }
        assertEquals(listOf("1.4.48", "2.3.2"), resolve(dev.panini.core.DhatuGana.BHVADI).evidence.map { it.sutra })
        val covering = resolve(dev.panini.core.DhatuGana.ADADI)
        assertEquals(Karaka.ADHIKARANA, covering.resolved)
        assertEquals(listOf("1.4.45", "2.3.36"), covering.evidence.map { it.sutra })
    }

    @Test
    fun `fasting sense excludes vas locus karman and restores general adhikarana`() {
        fun resolve(fasting: Boolean, cases: Set<Vibhakti>, prefix: String = "उप"): KarakaResolution {
            val p = ParticipantFacts("village", AvyayaPada("ग्राम", "ग्राम"), cases,
                semanticRelations = setOf(SemanticRelation.LOCATION) +
                    if (fasting) setOf(SemanticRelation.FASTING_SENSE) else emptySet())
            val verb = TingantaPada("verb", listOf(prefix), DhatuPrakriti("वस्", "वस्"),
                Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("वस्"), p,
                listOf(p), Prayoga.KARTARI, verbNode = verb))
        }
        val competing = setOf(Vibhakti.DVITIYA, Vibhakti.SAPTAMI)
        assertEquals(listOf("1.4.48", "2.3.2"), resolve(false, competing).evidence.map { it.sutra })
        val fasting = resolve(true, competing)
        assertEquals(Karaka.ADHIKARANA, fasting.resolved)
        assertEquals(Vibhakti.SAPTAMI, fasting.resolvedVibhakti)
        assertEquals(listOf("1.4.45", "2.3.36"), fasting.evidence.map { it.sutra })
        val wrongFastingCase = resolve(true, setOf(Vibhakti.DVITIYA))
        assertEquals(Karaka.ADHIKARANA, wrongFastingCase.resolved)
        assertEquals(listOf("1.4.45"), wrongFastingCase.evidence.map { it.sutra })
        assertEquals(null, wrongFastingCase.resolvedVibhakti)
        assertEquals(null, resolve(true, competing, "अज्ञात").resolved)
    }

    @Test
    fun `destiny inquiry uses exact root identity and requires its semantic premise`() {
        val target = ParticipantFacts("target", AvyayaPada("target", "target"), setOf(Vibhakti.CHATURTHI),
            semanticRelations = setOf(SemanticRelation.INQUIRY_DESTINY_TARGET))
        fun resolve(root: String, p: ParticipantFacts = target, verbRoot: String? = null): KarakaResolution {
            val verb = verbRoot?.let { TingantaPada("verb", emptyList(), DhatuPrakriti(it, it),
                Lakara.LAT, TingPratyaya("त", "त")) }
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p),
                Prayoga.KARTARI, verbNode = verb))
        }
        for (root in listOf("राध्", "ईक्ष्")) {
            val result = resolve(root)
            assertEquals(listOf("1.4.39", "2.3.13"), result.evidence.map { it.sutra })
            assertEquals("राधीक्ष्योर्यस्य विप्रश्नः", result.evidence.first().text)
            assertEquals(null, resolve(root, target.copy(semanticRelations = emptySet())).resolved)
        }
        for (root in listOf("राधकल्प्", "ईक्षकल्प्", "पठ्")) {
            assertEquals(null, resolve(root).resolved, root)
        }
        assertEquals(listOf("1.4.39", "2.3.13"), resolve("अज्ञात", verbRoot = "ईक्ष्").evidence.map { it.sutra })
    }

    @Test
    fun `anger rules require directed hostility and ordinary envy object remains karman`() {
        val recipient = ParticipantFacts("target", AvyayaPada("target", "target"),
            setOf(Vibhakti.DVITIYA, Vibhakti.CHATURTHI), semanticRelations = setOf(SemanticRelation.RECIPIENT))
        fun resolve(root: String, p: ParticipantFacts) = KarakaRuleEngine.resolve(KarakaRuleContext(
            DhatuIdentity(root), p, listOf(p), Prayoga.KARTARI))
        for (root in listOf("क्रुध्", "द्रुह्", "ईर्ष्या", "असूया", "अभिक्रुध्", "अभिद्रुह्")) {
            assertEquals(null, resolve(root, recipient).resolved, root)
            val hostile = recipient.copy(semanticRelations = recipient.semanticRelations + SemanticRelation.ANGER_TARGET)
            assertEquals(if (root.startsWith("अभि")) listOf("1.4.38", "2.3.2") else listOf("1.4.37", "2.3.13"),
                resolve(root, hostile).evidence.map { it.sutra }, root)
        }
        val wife = recipient.copy(possibleVibhaktis = setOf(Vibhakti.DVITIYA),
            semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT))
        assertEquals(listOf("1.4.49", "2.3.2"), resolve("ईर्ष्या", wife).evidence.map { it.sutra })
    }

    @Test
    fun `accompaniment instrumental requires nonprincipal relation and preserves principal genitive`() {
        val relations = setOf(SemanticRelation.ACCOMPANIMENT, SemanticRelation.NON_PRINCIPAL_ACCOMPANIMENT)
        fun resolve(cases: Set<Vibhakti>, meanings: Set<SemanticRelation>): KarakaResolution {
            val p = ParticipantFacts("companion", AvyayaPada("companion", "companion"), cases, semanticRelations = meanings)
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("गम्"), p, listOf(p), Prayoga.KARTARI))
        }
        val companion = resolve(setOf(Vibhakti.TRTIYA), relations)
        assertEquals(listOf("2.3.19"), companion.evidence.map { it.sutra })
        assertEquals(Vibhakti.TRTIYA, companion.resolvedVibhakti)
        assertEquals(Karaka.ANIRDHARITA, companion.resolved)
        for (missing in relations) {
            assertEquals(null, resolve(setOf(Vibhakti.TRTIYA), relations - missing).resolvedVibhakti)
        }
        val principal = resolve(setOf(Vibhakti.SASTHI), setOf(SemanticRelation.ACCOMPANIMENT,
            SemanticRelation.SESHA_RELATION))
        assertEquals(Karaka.SAMBANDHA, principal.resolved)
        assertEquals(listOf("2.3.50"), principal.evidence.map { it.sutra })
    }

    @Test
    fun `explicit hetu cause and pronominal optional cases preserve inherited conditions`() {
        val premises = setOf(SemanticRelation.CAUSE_HETU, SemanticRelation.EXPLICIT_HETU_USE,
            SemanticRelation.PRONOMINAL_HETU)
        fun resolve(cases: Set<Vibhakti>, relations: Set<SemanticRelation> = premises): KarakaResolution {
            val p = ParticipantFacts("cause", AvyayaPada("cause", "cause"), cases, semanticRelations = relations)
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("वस्"), p, listOf(p), Prayoga.KARTARI))
        }
        for (case in listOf(Vibhakti.TRTIYA, Vibhakti.SASTHI)) {
            val result = resolve(setOf(case))
            assertEquals(listOf("2.3.27"), result.evidence.map { it.sutra })
            assertEquals(case, result.resolvedVibhakti)
            assertEquals(listOf("2.3.27"), result.alternatives.single().evidence.map { it.sutra })
        }
        assertEquals(null, resolve(setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI)).resolvedVibhakti)
        assertEquals(listOf("2.3.26"), resolve(setOf(Vibhakti.SASTHI),
            premises - SemanticRelation.PRONOMINAL_HETU).evidence.map { it.sutra })
        assertEquals(null, resolve(setOf(Vibhakti.TRTIYA),
            premises - SemanticRelation.PRONOMINAL_HETU).resolvedVibhakti)
        for (missing in listOf(SemanticRelation.CAUSE_HETU, SemanticRelation.EXPLICIT_HETU_USE)) {
            assertEquals(false, resolve(setOf(Vibhakti.SASTHI), premises - missing).evidence.any {
                it.sutra in setOf("2.3.26", "2.3.27")
            })
        }
    }

    @Test
    fun `quality cause directly licenses optional cases without inventing apadana or instrument`() {
        fun resolve(cases: Set<Vibhakti>, quality: Boolean = true, feminine: Boolean = false): KarakaResolution {
            val cause = ParticipantFacts("cause", AvyayaPada("जाड्य", "जाड्य"), cases,
                semanticRelations = setOf(SemanticRelation.CAUSE_HETU) +
                    if (quality) setOf(SemanticRelation.NON_FEMININE_QUALITY_CAUSE) else emptySet(),
                linga = if (feminine) setOf(dev.panini.core.Linga.STRI) else setOf(dev.panini.core.Linga.NAPUMSAKA))
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("बन्ध्"), cause, listOf(cause), Prayoga.KARMANI))
        }
        for (case in listOf(Vibhakti.TRTIYA, Vibhakti.PANCHAMI)) {
            val result = resolve(setOf(case))
            assertEquals(Karaka.ANIRDHARITA, result.resolved)
            assertEquals(case, result.resolvedVibhakti)
            assertEquals(listOf("2.3.25"), result.evidence.map { it.sutra })
            assertEquals(listOf("2.3.25"), result.alternatives.single().evidence.map { it.sutra })
            assertEquals(false, result.alternatives.single().matchesObservedMorphology)
        }
        val both = resolve(setOf(Vibhakti.TRTIYA, Vibhakti.PANCHAMI))
        assertEquals(null, both.resolvedVibhakti)
        assertEquals(true, both.alternatives.single().matchesObservedMorphology)
        assertEquals(listOf("2.3.23"), resolve(setOf(Vibhakti.TRTIYA), false).evidence.map { it.sutra })
        assertEquals(listOf("2.3.23"), resolve(setOf(Vibhakti.TRTIYA), feminine = true).evidence.map { it.sutra })
        assertEquals(null, resolve(setOf(Vibhakti.PANCHAMI), false).resolvedVibhakti)
    }

    @Test
    fun `canonical krtya optionality does not add commentary prohibition`() {
        fun resolve(cases: Set<Vibhakti>, dual: Boolean): KarakaResolution {
            val agent = ParticipantFacts("agent", AvyayaPada("agent", "agent"), cases,
                semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT) +
                    if (dual) setOf(SemanticRelation.KRT_DUAL_ELIGIBLE_AGENT) else emptySet())
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("नी"), agent,
                listOf(agent), Prayoga.BHAVE, candidates = setOf(Karaka.KARTR),
                governingKrt = dev.panini.vyakaranam.ast.KridantaPratipadika(
                    sourceText = "नी + तव्यत्", upasargas = emptyList(),
                    dhatu = DhatuPrakriti("नी", "नी"), krtPratyaya = "तव्यत्"),
                governingKrtExpressedRoles = if (dual) emptySet() else setOf(Karaka.KARMAN),
                governingKrtOccurrenceId = "krtya-1",
                governingKrtGenitiveEligibility = if (dual) KrtGenitiveEligibility("krtya-1",
                    setOf(Karaka.KARTR, Karaka.KARMAN)) else null))
        }
        val genitive = resolve(setOf(Vibhakti.SASTHI), true)
        assertEquals(Vibhakti.SASTHI, genitive.resolvedVibhakti)
        assertEquals(listOf("1.4.54", "2.3.71"), genitive.evidence.map { it.sutra })
        assertEquals(emptyList(), genitive.rejectedRules)
        for (cases in listOf(setOf(Vibhakti.TRTIYA), setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI))) {
            val result = resolve(cases, true)
            assertEquals(if (cases.size == 1) Vibhakti.TRTIYA else null, result.resolvedVibhakti)
            assertEquals(Karaka.KARTR, result.resolved)
            assertEquals(Vibhakti.SASTHI, result.alternatives.single().vibhakti)
        }
        assertEquals(Vibhakti.SASTHI, resolve(setOf(Vibhakti.SASTHI), false).resolvedVibhakti)
    }

    @Test
    fun `krtya agent preserves optional instrumental genitive cases through linked governor`() {
        fun resolve(cases: Set<Vibhakti>, governed: Boolean = true): KarakaResolution {
            val agent = ParticipantFacts("agent", AvyayaPada("agent", "agent"), cases,
                semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT))
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("कृ"), agent,
                listOf(agent), Prayoga.BHAVE, candidates = setOf(Karaka.KARTR),
                governingKrt = if (governed) dev.panini.vyakaranam.ast.KridantaPratipadika(
                    sourceText = "कृ + तव्यत्", upasargas = emptyList(),
                    dhatu = DhatuPrakriti("कृ", "कृ"), krtPratyaya = "तव्यत्") else null,
                governingKrtExpressedRoles = if (governed) setOf(Karaka.KARMAN) else null))
        }
        for (case in listOf(Vibhakti.TRTIYA, Vibhakti.SASTHI)) {
            val result = resolve(setOf(case))
            assertEquals(Karaka.KARTR, result.resolved)
            assertEquals(case, result.resolvedVibhakti)
            assertEquals(listOf("1.4.54", "2.3.71"), result.evidence.map { it.sutra })
            val alternative = result.alternatives.single()
            assertEquals(KarakaAlternativeKind.OPTIONAL_VIBHAKTI, alternative.kind)
            assertEquals(false, alternative.matchesObservedMorphology)
            assertEquals(listOf("1.4.54", "2.3.71"), alternative.evidence.map { it.sutra })
        }
        val ambiguous = resolve(setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI))
        assertEquals(Karaka.KARTR, ambiguous.resolved)
        assertEquals(null, ambiguous.resolvedVibhakti)
        assertEquals(true, ambiguous.alternatives.single().matchesObservedMorphology)
        assertEquals(listOf("1.4.54", "2.3.18"), resolve(setOf(Vibhakti.TRTIYA), false).evidence.map { it.sutra })
    }

    @Test
    fun `manya disregard preserves optional cases and observed case ambiguity`() {
        fun resolve(cases: Set<Vibhakti>, marked: Boolean = true): KarakaResolution {
            val participant = ParticipantFacts("comparison", AvyayaPada("तृण", "तृण"), cases,
                semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT) +
                    if (marked) setOf(SemanticRelation.MAN_INANIMATE_DISREGARD_OBJECT) else emptySet())
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("मन्"),
                participant, listOf(participant), Prayoga.KARTARI, candidates = setOf(Karaka.KARMAN)))
        }
        for (case in listOf(Vibhakti.DVITIYA, Vibhakti.CHATURTHI)) {
            val result = resolve(setOf(case))
            assertEquals(Karaka.KARMAN, result.resolved)
            assertEquals(case, result.resolvedVibhakti)
            assertEquals(listOf("1.4.49", "2.3.17"), result.evidence.map { it.sutra })
            val alternative = result.alternatives.single()
            assertEquals(KarakaAlternativeKind.OPTIONAL_VIBHAKTI, alternative.kind)
            assertEquals(false, alternative.matchesObservedMorphology)
            assertEquals(listOf("1.4.49", "2.3.17"), alternative.evidence.map { it.sutra })
        }
        val ambiguous = resolve(setOf(Vibhakti.DVITIYA, Vibhakti.CHATURTHI))
        assertEquals(Karaka.KARMAN, ambiguous.resolved)
        assertEquals(null, ambiguous.resolvedVibhakti)
        assertEquals(true, ambiguous.alternatives.single().matchesObservedMorphology)
        assertEquals(listOf("1.4.49", "2.3.2"), resolve(setOf(Vibhakti.DVITIYA), false).evidence.map { it.sutra })
    }

    @Test
    fun `canonical only analysis leaves commentary withdrawal assignments unresolved`() {
        val participant = ParticipantFacts("reference", AvyayaPada("reference", "reference"), setOf(Vibhakti.PANCHAMI))
        fun resolve(root: String, prefixes: List<String>? = null): KarakaResolution {
            val verb = prefixes?.let { TingantaPada("verb", it, DhatuPrakriti(root, root),
                Lakara.LAT, TingPratyaya("त", "त")) }
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), participant,
                listOf(participant), Prayoga.KARTARI, verbNode = verb))
        }
        for (root in listOf("जुगुप्स्", "विरम्", "प्रमद्")) {
            assertEquals(null, resolve(root).resolved, root)
        }
        for (root in listOf("रम्", "मद्", "समद्", "जुगुप्सकल्प्")) {
            assertEquals(null, resolve(root).resolved, root)
        }
        assertEquals(null, resolve("रम्", listOf("वि")).resolved)
        assertEquals(null, resolve("मद्", listOf("प्र")).resolved)
        assertEquals(null, resolve("रम्", emptyList()).resolved)
        assertEquals(null, resolve("मद्", listOf("वि")).resolved)
        assertEquals(null, resolve("रम्", listOf("वि", "अज्ञात")).resolved)
    }

    @Test
    fun `protection active passive alternation preserves danger while agent and object change case`() {
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            val agent = ParticipantFacts("protector", AvyayaPada("protector", "protector"),
                setOf(if (voice == Prayoga.KARTARI) Vibhakti.PRATHAMA else Vibhakti.TRTIYA),
                semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT))
            val protected = ParticipantFacts("protected", AvyayaPada("protected", "protected"),
                setOf(if (voice == Prayoga.KARTARI) Vibhakti.DVITIYA else Vibhakti.PRATHAMA),
                semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT))
            // भ्याम् has instrumental/dative/ablative readings in both voices.
            val danger = ParticipantFacts("danger", AvyayaPada("चौराभ्याम्", "चौराभ्याम्"),
                setOf(Vibhakti.TRTIYA, Vibhakti.CHATURTHI, Vibhakti.PANCHAMI),
                semanticRelations = setOf(SemanticRelation.SOURCE, SemanticRelation.FEAR_CAUSE))
            val participants = listOf(agent, protected, danger)
            fun resolve(p: ParticipantFacts) = KarakaRuleEngine.resolve(KarakaRuleContext(
                DhatuIdentity("रक्ष्"), p, participants, voice))
            val agentResult = resolve(agent)
            assertEquals(Karaka.KARTR, agentResult.resolved, voice.name)
            assertEquals(listOf("1.4.54", if (voice == Prayoga.KARTARI) "2.3.46" else "2.3.18"),
                agentResult.evidence.map { it.sutra }, voice.name)
            val protectedResult = resolve(protected)
            assertEquals(Karaka.KARMAN, protectedResult.resolved, voice.name)
            assertEquals(listOf("1.4.49", if (voice == Prayoga.KARTARI) "2.3.2" else "2.3.46"),
                protectedResult.evidence.map { it.sutra }, voice.name)
            val dangerResult = resolve(danger)
            assertEquals(Karaka.APADANA, dangerResult.resolved, voice.name)
            assertEquals(Vibhakti.PANCHAMI, dangerResult.resolvedVibhakti, voice.name)
            assertEquals(listOf("1.4.25", "2.3.28"), dangerResult.evidence.map { it.sutra }, voice.name)
            assertEquals(null, resolve(danger.copy(semanticRelations = setOf(SemanticRelation.SOURCE))).resolved,
                "Case syncretism alone must not establish feared danger: $voice")
        }
    }

    @Test
    fun `sam jna AST establishes the special construction only for independently marked objects`() {
        val participant = ParticipantFacts("object", AvyayaPada("object", "object"),
            setOf(Vibhakti.TRTIYA), semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT))
        fun resolve(prefixes: List<String>, p: ParticipantFacts = participant): KarakaResolution {
            val verb = TingantaPada("verb", prefixes, DhatuPrakriti("ज्ञा", "ज्ञा"),
                Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("ज्ञा"), p,
                listOf(p), Prayoga.KARTARI, verbNode = verb))
        }
        val result = resolve(listOf("सम्"))
        assertEquals(Karaka.KARMAN, result.resolved)
        assertEquals(setOf(Karaka.KARANA, Karaka.KARMAN), result.candidates)
        assertEquals(listOf("1.4.49", "2.3.22"), result.evidence.map { it.sutra })
        assertEquals(true, result.evidence.first().semanticEvidence.any {
            it.relation == SemanticRelation.SAM_JNA_OBJECT && it.source == SemanticEvidenceSource.GOVERNING_CONSTRUCTION
        })
        for (prefixes in listOf(emptyList(), listOf("सम्", "अज्ञात"))) {
            val invalidCase = resolve(prefixes)
            assertEquals(Karaka.KARMAN, invalidCase.resolved)
            assertEquals(null, invalidCase.resolvedVibhakti)
            assertEquals(listOf("1.4.49"), invalidCase.evidence.map { it.sutra })
            assertEquals(false, invalidCase.evidence.first().semanticEvidence.any {
                it.relation == SemanticRelation.SAM_JNA_OBJECT
            })
        }
        assertEquals(null, resolve(listOf("सम्"), participant.copy(semanticRelations = emptySet())).resolved)
    }

    @Test
    fun `sam jna enrichment preserves explicit hypotheses and is idempotent`() {
        val participant = ParticipantFacts("object", AvyayaPada("object", "object"),
            setOf(Vibhakti.TRTIYA), semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT))
        val verb = TingantaPada("verb", listOf("सम्"), DhatuPrakriti("ज्ञा", "ज्ञा"),
            Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
        val context = KarakaRuleContext(DhatuIdentity("ज्ञा"), participant, listOf(participant),
            Prayoga.KARTARI, candidates = setOf(Karaka.KARTR), verbNode = verb)
        val enriched = VerbalConstructionEvidence.enrich(context)
        assertEquals(setOf(Karaka.KARTR, Karaka.KARMAN), enriched.candidates)
        assertEquals(enriched.participant, enriched.allParticipants.single())
        assertEquals(enriched, VerbalConstructionEvidence.enrich(enriched))
    }

    @Test
    fun `2 3 22 instrumental object requires sam jna construction and preserves case alternatives`() {
        val participant = ParticipantFacts("object", AvyayaPada("object", "object"),
            setOf(Vibhakti.TRTIYA), semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT))
        fun resolve(p: ParticipantFacts) = KarakaRuleEngine.resolve(KarakaRuleContext(
            DhatuIdentity("ज्ञा"), p, listOf(p), Prayoga.KARTARI, candidates = setOf(Karaka.KARMAN)))
        assertEquals(null, resolve(participant).resolvedVibhakti)
        val marked = participant.copy(semanticRelations = participant.semanticRelations + SemanticRelation.SAM_JNA_OBJECT)
        val result = resolve(marked)
        assertEquals(listOf("1.4.49", "2.3.22"), result.evidence.map { it.sutra })
        assertEquals(Vibhakti.TRTIYA, result.resolvedVibhakti)
        assertEquals(Vibhakti.DVITIYA, result.alternatives.single().vibhakti)
        assertEquals(false, result.alternatives.single().matchesObservedMorphology)
        val both = resolve(marked.copy(possibleVibhaktis = setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA)))
        assertEquals(Karaka.KARMAN, both.resolved)
        assertEquals(null, both.resolvedVibhakti)
        assertEquals(true, both.alternatives.single().matchesObservedMorphology)
    }

    @Test
    fun `motion goal optional cases retain same karaka and distinguish observed alternatives`() {
        listOf(setOf(Vibhakti.CHATURTHI), setOf(Vibhakti.DVITIYA, Vibhakti.CHATURTHI)).forEach { cases ->
            val participant = ParticipantFacts("goal", AvyayaPada("ग्राम", "ग्राम"), cases,
                semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.MOTION_GOAL,
                    SemanticRelation.PHYSICAL_MOTION, SemanticRelation.NON_PATH_DESTINATION))
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("गम्"), participant,
                listOf(participant), Prayoga.KARTARI, candidates = setOf(Karaka.KARMAN)))
            assertEquals(Karaka.KARMAN, result.resolved)
            assertEquals(listOf("1.4.49", "2.3.12"), result.evidence.map { it.sutra })
            assertEquals(KarakaAlternativeKind.OPTIONAL_VIBHAKTI, result.alternatives.single().kind)
            assertEquals(Vibhakti.DVITIYA, result.alternatives.single().vibhakti)
            assertEquals(Vibhakti.DVITIYA in cases, result.alternatives.single().matchesObservedMorphology)
            assertEquals(if (cases.size == 1) Vibhakti.CHATURTHI else null, result.resolvedVibhakti)
        }
    }

    @Test
    fun `2 3 70 distinguishes supplied aka future and in future or debt constructions`() {
        val participant = ParticipantFacts("agent", AvyayaPada("agent", "agent"), setOf(Vibhakti.SASTHI),
            semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT, SemanticRelation.KRT_GOVERNED_PARTICIPANT))
        fun resolve(p: ParticipantFacts) = KarakaRuleEngine.resolve(KarakaRuleContext(
            DhatuIdentity("कृ"), p, listOf(p), Prayoga.BHAVE, candidates = setOf(Karaka.KARTR)))
        assertEquals(listOf("1.4.54", "2.3.65"), resolve(participant).evidence.map { it.sutra })
        KrtGenitiveProhibition.entries.forEach { condition ->
            val result = resolve(participant.copy(krtGenitiveProhibition = condition))
            assertEquals(Karaka.KARTR, result.resolved, condition.name)
            assertEquals(null, result.resolvedVibhakti, condition.name)
            assertEquals(listOf("2.3.65"), result.rejectedRules.map { it.targetSutra })
            assertEquals(listOf("2.3.70"), result.rejectedRules.map { it.prohibition.sutra })
            assertEquals("अकेनोर्भविष्यदाधमर्ण्ययोः", result.rejectedRules.single().prohibition.text)
        }
    }

    @Test
    fun `dvikarmaka root matching rejects substring lookalikes and uses typed base root`() {
        val participant = ParticipantFacts("secondary", AvyayaPada("secondary", "secondary"),
            setOf(Vibhakti.DVITIYA), semanticRelations = setOf(SemanticRelation.SECONDARY_OBJECT))
        fun context(root: String) = KarakaRuleContext(DhatuIdentity(root), participant,
            listOf(participant), Prayoga.KARTARI)
        listOf("चिन्त्", "अर्चि", "अज्ञातदुह्").forEach { root ->
            val result = KarakaRuleEngine.resolve(context(root))
            assertEquals(null, result.resolved, root)
            assertEquals(emptyList(), result.evidence, root)
        }
        val verb = TingantaPada("प्रदोहति", listOf("प्र"), DhatuPrakriti("दुह्", "दुह्"),
            Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
        val result = KarakaRuleEngine.resolve(context("प्रदोह").copy(verbNode = verb))
        assertEquals(listOf("1.4.51", "2.3.2"), result.evidence.map { it.sutra })
    }

    @Test
    fun `causative causee marker alone does not establish the 1 4 52 root domain`() {
        listOf("लिख्", "दा", "संस्कार", "कारय").forEach { root ->
            val participant = ParticipantFacts("causee", AvyayaPada("causee", "causee"),
                setOf(Vibhakti.DVITIYA), semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT))
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(
                DhatuIdentity(root), participant, listOf(participant), Prayoga.CAUSATIVE))
            assertEquals(null, result.resolved, root)
            assertEquals(emptyList(), result.evidence, root)
        }
    }

    @Test
    fun `typed prefixed kr root retains exceptional causative identity`() {
        val participant = ParticipantFacts("causee", AvyayaPada("causee", "causee"),
            setOf(Vibhakti.DVITIYA), semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT))
        val verb = TingantaPada("प्रकारयति", listOf("प्र"), DhatuPrakriti("कृ", "कृ", listOf("णिच्")),
            Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
        val result = KarakaRuleEngine.resolve(KarakaRuleContext(
            DhatuIdentity("प्रकारय"), participant, listOf(participant), Prayoga.CAUSATIVE, verbNode = verb))
        assertEquals(listOf("1.4.53", "2.3.2"), result.evidence.map { it.sutra })
    }

    @Test
    fun `ordinary purchase does not license optional hiring dative`() {
        listOf("क्री", "परिक्री").forEach { root ->
            fun resolve(cases: Set<Vibhakti>): KarakaResolution {
                val participant = ParticipantFacts("price", AvyayaPada("price", "price"), cases,
                    semanticRelations = setOf(SemanticRelation.INSTRUMENT))
                return KarakaRuleEngine.resolve(KarakaRuleContext(
                    DhatuIdentity(root), participant, listOf(participant), Prayoga.KARTARI))
            }
            val ordinary = resolve(setOf(Vibhakti.TRTIYA, Vibhakti.CHATURTHI))
            assertEquals(Karaka.KARANA, ordinary.resolved, root)
            assertEquals(listOf("1.4.42", "2.3.18"), ordinary.evidence.map { it.sutra }, root)
            assertEquals(emptyList(), ordinary.alternatives, root)
            val wrongDative = resolve(setOf(Vibhakti.CHATURTHI))
            assertEquals(Karaka.KARANA, wrongDative.resolved, root)
            assertEquals(listOf("1.4.42"), wrongDative.evidence.map { it.sutra }, root)
            assertEquals(null, wrongDative.resolvedVibhakti, root)
            assertEquals(emptyList(), wrongDative.alternatives, root)
        }
    }

    @Test
    fun `syncretic optional karaka forms retain both compatible readings`() {
        listOf(
            Triple("दिव", Vibhakti.DVITIYA, "1.4.43"),
            Triple("परिक्री", Vibhakti.CHATURTHI, "1.4.44"),
        ).forEach { (root, optionalCase, sutra) ->
            val participant = ParticipantFacts("instrument", AvyayaPada("syncretic", "syncretic"),
                setOf(Vibhakti.TRTIYA, optionalCase), semanticRelations = setOf(SemanticRelation.INSTRUMENT) +
                    if (sutra == "1.4.44") setOf(SemanticRelation.FIXED_TERM_HIRING_PAYMENT) else emptySet())
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(
                DhatuIdentity(root), participant, listOf(participant), Prayoga.KARTARI))
            assertEquals(if (sutra == "1.4.43") Karaka.KARMAN else null, result.resolved, root)
            assertEquals(null, result.resolvedVibhakti, root)
            assertEquals(sutra, result.evidence.first().sutra, root)
            assertEquals(Karaka.KARANA, result.alternatives.single().karaka, root)
            assertEquals(true, result.alternatives.single().matchesObservedMorphology, root)
        }
    }

    @Test
    fun `special locus sutras use structured root prefix evidence before general adhikarana`() {
        data class Case(val root: String, val prefixes: List<String>, val sutra: String)
        val cases = listOf("शी", "स्था", "आस्").map { Case(it, listOf("अधि"), "1.4.46") } +
            Case("विश्", listOf("अभि", "नि"), "1.4.47") +
            listOf("उप", "अनु", "अधि", "आङ्").map { Case("वस्", listOf(it), "1.4.48") }
        fun resolve(root: String, prefixes: List<String>): KarakaResolution {
            val participant = ParticipantFacts("locus", AvyayaPada("locus", "locus"),
                setOf(Vibhakti.DVITIYA, Vibhakti.SAPTAMI), semanticRelations = setOf(SemanticRelation.LOCATION))
            val verb = TingantaPada("verb", prefixes, DhatuPrakriti(root, root), Lakara.LAT,
                TingPratyaya("तिप्", "तिप्"))
            return KarakaRuleEngine.resolve(KarakaRuleContext(
                DhatuIdentity(root), participant, listOf(participant), Prayoga.KARTARI, verbNode = verb))
        }
        cases.forEach { case ->
            val result = resolve(case.root, case.prefixes)
            assertEquals(Karaka.KARMAN, result.resolved, case.toString())
            assertEquals(listOf(case.sutra, "2.3.2"), result.evidence.map { it.sutra }, case.toString())
        }
        assertEquals(listOf("1.4.45", "2.3.36"), resolve("वस्", emptyList()).evidence.map { it.sutra })
        assertEquals(listOf("1.4.45", "2.3.36"), resolve("विश्", listOf("नि")).evidence.map { it.sutra })
        assertEquals(null, resolve("वस्", listOf("अज्ञात")).resolved)
        assertEquals(null, resolve("स्था", listOf("अज्ञात", "अधि")).resolved)
        assertEquals(null, resolve("विश्", listOf("अज्ञात", "अभि", "नि")).resolved)
        assertEquals(null, resolve("वस्", listOf("अज्ञात", "उप")).resolved)
    }

    @Test
    fun `special sampradana premises cannot be replaced by generic recipient evidence`() {
        listOf("श्लाघ", "प्रतिश्रु", "अनुगृ").forEach { root ->
            val participant = ParticipantFacts(root, AvyayaPada(root, root), setOf(Vibhakti.CHATURTHI),
                semanticRelations = setOf(SemanticRelation.RECIPIENT))
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(
                DhatuIdentity(root), participant, listOf(participant), Prayoga.KARTARI,
                verbNode = buildMockVerbNode(root), baseDhatu = findMockDhatu(root),
            ))
            assertEquals(null, result.resolved, root)
            assertEquals(emptyList(), result.evidence, root)
        }
    }

    @Test
    fun `anger sutras respect explicit AST prefixes including unknown identities`() {
        val participant = ParticipantFacts("anger-target", AvyayaPada("target", "target"),
            setOf(Vibhakti.DVITIYA, Vibhakti.CHATURTHI),
            semanticRelations = setOf(SemanticRelation.RECIPIENT, SemanticRelation.ANGER_TARGET))
        fun resolve(prefixes: List<String>, root: String = "क्रुध्"): KarakaResolution {
            val verb = TingantaPada("anger", prefixes, DhatuPrakriti(root, root),
                Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
            return KarakaRuleEngine.resolve(KarakaRuleContext(
                DhatuIdentity(root), participant, listOf(participant), Prayoga.KARTARI,
                verbNode = verb,
            ))
        }
        dev.panini.core.Upasarga.entries.forEach { prefix ->
            val result = resolve(listOf(prefix.upadesha))
            assertEquals(Karaka.KARMAN, result.resolved, prefix.name)
            assertEquals(listOf("1.4.38", "2.3.2"), result.evidence.map { it.sutra }, prefix.name)
        }
        assertEquals(listOf("1.4.37", "2.3.13"), resolve(emptyList()).evidence.map { it.sutra })
        assertEquals(null, resolve(listOf("अज्ञात")).resolved)
        assertEquals(emptyList(), resolve(listOf("अज्ञात")).evidence)
        listOf("ईर्ष्या", "असूया").forEach { root ->
            assertEquals(listOf("1.4.37", "2.3.13"), resolve(listOf("उप"), root).evidence.map { it.sutra })
        }
    }

    @Test
    fun `apadana sutras 1 4 24 through 31 require established semantic premises independently of case`() {
        listOf(
            "1.4.24" to "पलाय्", "1.4.25" to "भी", "1.4.26" to "पराजि",
            "1.4.27" to "वारय", "1.4.28" to "निली", "1.4.29" to "अधी",
            "1.4.30" to "जन्", "1.4.31" to "प्रभू",
        ).forEach { (number, root) ->
            val rule = KarakaRuleEngine.karakaRules.single { it.number == number }
            val premises = setOf(SemanticRelation.SOURCE) +
                when (number) {
                    "1.4.24" -> setOf(SemanticRelation.SEPARATION_REFERENCE)
                    "1.4.29" -> setOf(SemanticRelation.FORMAL_INSTRUCTION_SOURCE)
                    "1.4.26" -> setOf(SemanticRelation.UNBEARABLE_OBJECT)
                    "1.4.25" -> setOf(SemanticRelation.FEAR_CAUSE)
                    "1.4.27" -> setOf(SemanticRelation.PREVENTED_DESIRED_OBJECT)
                    "1.4.28" -> setOf(SemanticRelation.CONCEALMENT_OBSERVER)
                    "1.4.31" -> setOf(SemanticRelation.FIRST_MANIFESTATION_SOURCE)
                    "1.4.30" -> setOf(SemanticRelation.BIRTH_MATERIAL_CAUSE)
                    else -> emptySet()
                }
            val participant = ParticipantFacts(number, AvyayaPada(number, number),
                setOf(Vibhakti.PANCHAMI), semanticRelations = premises)
            val context = KarakaRuleContext(DhatuIdentity(root), participant, listOf(participant),
                Prayoga.KARTARI, candidates = setOf(Karaka.APADANA))
            assertEquals(true, rule.matches(context), number)
            val noPremise = participant.copy(semanticRelations = emptySet())
            assertEquals(false, rule.matches(context.copy(participant = noPremise,
                allParticipants = listOf(noPremise))), number)
            // Established meaning licenses saṃjñā independently of an
            // incompatible case hypothesis; the case stage rejects it.
            assertEquals(true, rule.matches(context.copy(candidates = setOf(Karaka.KARMAN))), number)
            for (candidates in listOf(setOf(Karaka.KARMAN), setOf(Karaka.APADANA))) {
                for (weakened in premises.map { setOf(it) } + listOf(premises)) {
                    val weak = participant.copy(semanticEvidence = premises.mapTo(mutableSetOf()) {
                        SemanticRelationEvidence(it, if (it in weakened) SemanticEvidenceSource.UNAMBIGUOUS_MORPHOLOGY
                            else SemanticEvidenceSource.CLAUSE_CONTEXT,
                            "Every defining premise must be established independently of case.")
                    })
                    assertEquals(false, rule.matches(context.copy(participant = weak,
                        allParticipants = listOf(weak), candidates = candidates)), number)
                }
            }
            assertEquals(Karaka.APADANA, (rule.apply(context) as KarakaRuleResult.Assigned).karaka, number)
        }
    }

    @Test
    fun `linked krt AST supplies governance and affix prohibition evidence`() {
        val participant = ParticipantFacts(
            id = "agent", expression = AvyayaPada("देवदत्तस्य", "देवदत्तस्य"),
            possibleVibhaktis = setOf(Vibhakti.SASTHI),
            semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT),
        )
        val base = KarakaRuleContext(
            DhatuIdentity("कृ"), participant, listOf(participant), Prayoga.BHAVE,
            candidates = setOf(Karaka.KARTR),
        )
        fun withAffix(affix: String) = base.copy(governingKrt =
            dev.panini.vyakaranam.ast.KridantaPratipadika(
                sourceText = "कृ + $affix", upasargas = emptyList(),
                dhatu = DhatuPrakriti("कृ", "कृ"), krtPratyaya = affix,
            ),
            governingKrtExpressedRoles = if (affix == "ल्युट्") emptySet() else setOf(Karaka.KARMAN),
        )
        val ordinary = KarakaRuleEngine.resolve(withAffix("ल्युट्"))
        assertEquals(listOf("1.4.54", "2.3.65"), ordinary.evidence.map { it.sutra })
        assertEquals(true, ordinary.evidence.first().semanticEvidence.any {
            it.source == SemanticEvidenceSource.GOVERNING_CONSTRUCTION
        })
        assertEquals(null, KarakaRuleEngine.resolve(withAffix("क्त")).resolvedVibhakti)
        assertEquals(listOf("1.4.54", "2.3.71"), KarakaRuleEngine.resolve(withAffix("तव्यत्")).evidence.map { it.sutra })
        assertEquals(null, KarakaRuleEngine.resolve(base).resolvedVibhakti)
    }

    @Test
    fun `krt genitive prohibition reaches the sentence resolver`() {
        val participant = ParticipantFacts(
            id = "krt-agent", expression = AvyayaPada("देवदत्तस्य", "देवदत्तस्य"),
            possibleVibhaktis = setOf(Vibhakti.SASTHI),
            semanticRelations = setOf(
                SemanticRelation.INDEPENDENT_AGENT,
                SemanticRelation.KRT_GOVERNED_PARTICIPANT,
            ),
        )
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("कृ"), participant = participant,
            allParticipants = listOf(participant), prayoga = Prayoga.BHAVE,
            candidates = setOf(Karaka.KARTR),
        )
        val allowed = KarakaRuleEngine.resolve(context)
        val prohibitedParticipant = participant.copy(isKrtProhibitedForSasthi = true)
        val prohibited = KarakaRuleEngine.resolve(context.copy(
            participant = prohibitedParticipant,
            allParticipants = listOf(prohibitedParticipant),
        ))
        assertEquals(listOf("1.4.54", "2.3.65"), allowed.evidence.map { it.sutra })
        assertEquals(Vibhakti.SASTHI, allowed.resolvedVibhakti)
        assertEquals(Karaka.KARTR, prohibited.resolved)
        assertEquals(null, prohibited.resolvedVibhakti)
        assertEquals(listOf("1.4.54"), prohibited.evidence.map { it.sutra })
        assertEquals(listOf("2.3.65"), prohibited.rejectedRules.map { it.targetSutra })
        assertEquals(listOf("2.3.69"), prohibited.rejectedRules.map { it.prohibition.sutra })
        assertEquals(emptyList(), allowed.rejectedRules)
    }

    @Test
    fun `causative causer uses 1 4 55 over general independent agent rule`() {
        val participant = ParticipantFacts(
            id = "causer", expression = AvyayaPada("देवदत्तः", "देवदत्तः"),
            possibleVibhaktis = setOf(Vibhakti.PRATHAMA),
            semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT, SemanticRelation.PROMPTER_CAUSE),
        )
        val resolution = KarakaRuleEngine.resolve(KarakaRuleContext(
            dhatu = DhatuIdentity("गम्", true), participant = participant,
            allParticipants = listOf(participant), prayoga = Prayoga.CAUSATIVE,
        ))

        assertEquals(Karaka.KARTR, resolution.resolved)
        assertEquals(listOf("1.4.55", "2.3.46"), resolution.evidence.map { it.sutra })
        assertEquals(setOf(KarakaDesignation.HETU), resolution.additionalDesignations)
        val ordinary = participant.copy(semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT))
        val ordinaryResolution = KarakaRuleEngine.resolve(KarakaRuleContext(
            dhatu = DhatuIdentity("गम्", true), participant = ordinary,
            allParticipants = listOf(ordinary), prayoga = Prayoga.KARTARI,
        ))
        assertEquals(listOf("1.4.54", "2.3.46"), ordinaryResolution.evidence.map { it.sutra })
        assertEquals(emptySet(), ordinaryResolution.additionalDesignations)
    }

    @Test
    fun `causative ordinary object is not mistaken for causee`() {
        val participant = ParticipantFacts(
            id = "object", expression = AvyayaPada("ग्रामम्", "ग्रामम्"),
            possibleVibhaktis = setOf(Vibhakti.DVITIYA),
            semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT),
        )
        val resolution = KarakaRuleEngine.resolve(KarakaRuleContext(
            dhatu = DhatuIdentity("गम्", true), participant = participant,
            allParticipants = listOf(participant), prayoga = Prayoga.CAUSATIVE,
        ))

        assertEquals(Karaka.KARMAN, resolution.resolved)
        assertEquals(listOf("1.4.49", "2.3.2"), resolution.evidence.map { it.sutra })
    }

    @Test
    fun `causative causee uses 1 4 52 and hr kr exception uses 1 4 53`() {
        fun resolve(surface: String): KarakaResolution {
            val participant = ParticipantFacts(
                id = "causee", expression = AvyayaPada("बालम्", "बालम्"),
                possibleVibhaktis = setOf(Vibhakti.DVITIYA),
                semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT),
            )
            return KarakaRuleEngine.resolve(KarakaRuleContext(
                dhatu = DhatuIdentity(surface, true), participant = participant,
                allParticipants = listOf(participant), prayoga = Prayoga.CAUSATIVE,
            ))
        }

        val regular = resolve("गम्")
        val exceptional = resolve("कृ")
        assertEquals(listOf("1.4.52", "2.3.2"), regular.evidence.map { it.sutra })
        assertEquals(false, regular.evidence.first().optional)
        assertEquals(listOf("1.4.53", "2.3.2"), exceptional.evidence.map { it.sutra })
        assertEquals(true, exceptional.evidence.first().optional)
        val declined = exceptional.alternatives.single()
        assertEquals(Karaka.KARTR, declined.karaka)
        assertEquals(Vibhakti.TRTIYA, declined.vibhakti)
        assertEquals(listOf("1.4.54", "2.3.18"), declined.evidence.map { it.sutra })
        assertEquals(true, declined.evidence.first().applied)
        assertEquals(false, declined.optionalDecision?.applied)
    }

    @Test
    fun `dvikarmaka primary and secondary objects retain distinct sutra provenance`() {
        fun resolve(relations: Set<SemanticRelation>): KarakaResolution {
            val participant = ParticipantFacts(
                id = "object", expression = AvyayaPada("पयः", "पयः"),
                possibleVibhaktis = setOf(Vibhakti.DVITIYA), semanticRelations = relations,
            )
            return KarakaRuleEngine.resolve(KarakaRuleContext(
                dhatu = DhatuIdentity("दुह्", true), participant = participant,
                allParticipants = listOf(participant), prayoga = Prayoga.KARTARI,
            ))
        }

        val primary = resolve(setOf(SemanticRelation.DESIRED_OBJECT))
        val secondary = resolve(setOf(SemanticRelation.SECONDARY_OBJECT))
        assertEquals(Karaka.KARMAN, primary.resolved)
        assertEquals(listOf("1.4.49", "2.3.2"), primary.evidence.map { it.sutra })
        assertEquals(Karaka.KARMAN, secondary.resolved)
        assertEquals(listOf("1.4.51", "2.3.2"), secondary.evidence.map { it.sutra })
    }

    @Test
    fun `single morphological candidate is not a resolved karaka without semantic samjna evidence`() {
        val participant = ParticipantFacts(
            id = "unknown",
            expression = AvyayaPada("अज्ञातः", "अज्ञातः"),
            possibleVibhaktis = setOf(Vibhakti.PRATHAMA),
        )
        val resolution = KarakaRuleEngine.resolve(KarakaRuleContext(
            dhatu = DhatuIdentity("कृ", true), participant = participant,
            allParticipants = listOf(participant), prayoga = Prayoga.KARTARI,
        ))

        assertEquals(setOf(Karaka.KARTR), resolution.candidates)
        assertEquals(null, resolution.resolved)
        assertEquals(emptyList(), resolution.evidence)
    }

    @Test
    fun `sambandha is established as non karaka relation by sasthi sese`() {
        val participant = ParticipantFacts(
            id = "relation",
            expression = AvyayaPada("रामस्य", "रामस्य"),
            possibleVibhaktis = setOf(Vibhakti.SASTHI),
            semanticRelations = setOf(SemanticRelation.SESHA_RELATION),
        )
        val resolution = KarakaRuleEngine.resolve(KarakaRuleContext(
            dhatu = DhatuIdentity("कृ", true), participant = participant,
            allParticipants = listOf(participant), prayoga = Prayoga.KARTARI,
        ))

        assertEquals(Karaka.SAMBANDHA, resolution.resolved)
        assertEquals(Vibhakti.SASTHI, resolution.resolvedVibhakti)
        assertEquals(listOf("2.3.50"), resolution.evidence.map { it.sutra })
    }

    @Test
    fun `bhyam retains all kartari karaka possibilities`() {
        assertEquals(
            setOf(Karaka.KARANA, Karaka.SAMPRADANA, Karaka.APADANA),
            KarakaInference.candidates("भ्याम्", Prayoga.KARTARI),
        )
    }

    @Test
    fun `bhyam in passive includes agent and non-agent possibilities`() {
        assertEquals(
            setOf(Karaka.KARTR, Karaka.KARANA, Karaka.SAMPRADANA, Karaka.APADANA),
            KarakaInference.candidates("भ्याम्", Prayoga.KARMANI),
        )
    }

    @Test
    fun `bhyam is resolved as sampradana for giving`() {
        assertResolution("दा", Karaka.SAMPRADANA, Vibhakti.CHATURTHI, "1.4.32", "2.3.13")
    }

    @Test
    fun `bhyam is resolved as karana for writing`() {
        assertResolution("लिख्", Karaka.KARANA, Vibhakti.TRTIYA, "1.4.42", "2.3.18")
    }

    @Test
    fun `bhyam is resolved as apadana for fleeing`() {
        assertResolution("पलाय्", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.24", "2.3.28",
            additionalRelations = setOf(SemanticRelation.SEPARATION_REFERENCE))
    }

    @Test
    fun `separation reference is event relative and not inferred from ablative alone`() {
        val source = ParticipantFacts("horse", AvyayaPada("धावतो अश्वात्", "धावतो अश्वात्"),
            setOf(Vibhakti.PANCHAMI), semanticRelations = setOf(SemanticRelation.SOURCE))
        fun resolve(p: ParticipantFacts) = KarakaRuleEngine.resolve(KarakaRuleContext(
            DhatuIdentity("पत्"), p, listOf(p), Prayoga.KARTARI))
        assertEquals(null, resolve(source).resolved)
        val reference = source.copy(semanticRelations = source.semanticRelations + SemanticRelation.SEPARATION_REFERENCE)
        assertEquals(listOf("1.4.24", "2.3.28"), resolve(reference).evidence.map { it.sutra })
        val falling = source.copy(possibleVibhaktis = setOf(Vibhakti.PRATHAMA),
            semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT))
        assertEquals(listOf("1.4.54", "2.3.46"), resolve(falling).evidence.map { it.sutra })
        val owner = source.copy(possibleVibhaktis = setOf(Vibhakti.SASTHI),
            semanticRelations = setOf(SemanticRelation.SESHA_RELATION))
        assertEquals(Karaka.SAMBANDHA, resolve(owner).resolved)
        assertEquals(listOf("2.3.50"), resolve(owner).evidence.map { it.sutra })
    }

    @Test
    fun `bhyam is resolved as apadana for origin of bhu`() {
        assertResolution("प्रभू", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.31", "2.3.28",
            additionalRelations = setOf(SemanticRelation.FIRST_MANIFESTATION_SOURCE))
    }

    @Test
    fun `bhu first manifestation is distinct from generic source and ordinary locus`() {
        val source = ParticipantFacts("himalaya", AvyayaPada("हिमवतः", "हिमवतः"),
            setOf(Vibhakti.PANCHAMI), semanticRelations = setOf(SemanticRelation.SOURCE))
        fun resolve(p: ParticipantFacts) = KarakaRuleEngine.resolve(KarakaRuleContext(
            DhatuIdentity("प्रभू"), p, listOf(p), Prayoga.KARTARI))
        assertEquals(null, resolve(source).resolved)
        val origin = source.copy(semanticRelations = source.semanticRelations + SemanticRelation.FIRST_MANIFESTATION_SOURCE)
        assertEquals(listOf("1.4.31", "2.3.28"), resolve(origin).evidence.map { it.sutra })
        val locus = source.copy(possibleVibhaktis = setOf(Vibhakti.SAPTAMI),
            semanticRelations = setOf(SemanticRelation.LOCATION))
        assertEquals(listOf("1.4.45", "2.3.36"), resolve(locus).evidence.map { it.sutra })
    }

    @Test
    fun `bhyam is resolved as apadana for birth source of jan`() {
        assertResolution("जन्", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.30", "2.3.28",
            additionalRelations = setOf(SemanticRelation.BIRTH_MATERIAL_CAUSE))
    }

    @Test
    fun `birth apadana requires an explicit material or assisting cause`() {
        val source = ParticipantFacts("cause", AvyayaPada("cause", "cause"),
            setOf(Vibhakti.PANCHAMI), semanticRelations = setOf(SemanticRelation.SOURCE))
        fun resolve(root: String, p: ParticipantFacts) = KarakaRuleEngine.resolve(KarakaRuleContext(
            DhatuIdentity(root), p, listOf(p), Prayoga.KARTARI))
        assertEquals(null, resolve("जन्", source).resolved)
        for (cause in listOf(SemanticRelation.BIRTH_MATERIAL_CAUSE, SemanticRelation.BIRTH_ASSISTING_CAUSE)) {
            val participant = source.copy(semanticRelations = source.semanticRelations + cause)
            assertEquals(listOf("1.4.30", "2.3.28"), resolve("जन्", participant).evidence.map { it.sutra })
            assertEquals(false, resolve("पठ्", participant).evidence.any { it.sutra == "1.4.30" })
        }
        val locus = source.copy(possibleVibhaktis = setOf(Vibhakti.SAPTAMI),
            semanticRelations = setOf(SemanticRelation.LOCATION))
        assertEquals(listOf("1.4.45", "2.3.36"), resolve("जन्", locus).evidence.map { it.sutra })
    }

    @Test
    fun `bhyam is resolved as sampradana for pratishru`() {
        assertResolution("प्रतिश्रु", Karaka.SAMPRADANA, Vibhakti.CHATURTHI, "1.4.40", "2.3.13",
            additionalRelations = setOf(SemanticRelation.PRIOR_ACTION_AGENT))
    }

    @Test
    fun `bhyam is resolved as sampradana for anugri`() {
        assertResolution("अनुगृ", Karaka.SAMPRADANA, Vibhakti.CHATURTHI, "1.4.41", "2.3.13",
            additionalRelations = setOf(SemanticRelation.PRIOR_ACTION_AGENT),
            lexicalRelations = setOf(SemanticRelation.RECIPIENT))
    }

    @Test
    fun `destiny inquiry target follows 1 4 39 into dative realization`() {
        val participant = ParticipantFacts(
            id = "inquiry-target",
            expression = AvyayaPada("देवदत्ताय", "देवदत्ताय"),
            possibleVibhaktis = setOf(Vibhakti.CHATURTHI),
            semanticRelations = setOf(SemanticRelation.INQUIRY_DESTINY_TARGET),
        )
        val resolution = KarakaRuleEngine.resolve(KarakaRuleContext(
            dhatu = DhatuIdentity("राध्"),
            participant = participant,
            allParticipants = listOf(participant),
            prayoga = Prayoga.KARTARI,
        ))

        assertEquals(Karaka.SAMPRADANA, resolution.resolved)
        assertEquals(Vibhakti.CHATURTHI, resolution.resolvedVibhakti)
        assertEquals(listOf("1.4.39", "2.3.13"), resolution.evidence.map { it.sutra })
    }

    @Test
    fun `general action locus follows 1 4 45 into locative realization`() {
        val participant = ParticipantFacts(
            id = "locus",
            expression = AvyayaPada("गृहे", "गृहे"),
            possibleVibhaktis = setOf(Vibhakti.SAPTAMI),
            semanticRelations = setOf(SemanticRelation.LOCATION),
        )
        val resolution = KarakaRuleEngine.resolve(KarakaRuleContext(
            dhatu = DhatuIdentity("कृ"),
            participant = participant,
            allParticipants = listOf(participant),
            prayoga = Prayoga.KARTARI,
        ))

        assertEquals(Karaka.ADHIKARANA, resolution.resolved)
        assertEquals(Vibhakti.SAPTAMI, resolution.resolvedVibhakti)
        assertEquals(listOf("1.4.45", "2.3.36"), resolution.evidence.map { it.sutra })
    }

    @Test
    fun `body deformity is licensed directly by 2 3 20 without fabricated karaka sutra`() {
        val participant = ParticipantFacts(
            id = "affected-limb",
            expression = AvyayaPada("अक्ष्णा", "अक्ष्णा"),
            possibleVibhaktis = setOf(Vibhakti.TRTIYA),
            semanticRelations = setOf(SemanticRelation.BODY_DEFORMITY),
        )
        val resolution = KarakaRuleEngine.resolve(KarakaRuleContext(
            dhatu = DhatuIdentity("दृश्"),
            participant = participant,
            allParticipants = listOf(participant),
            prayoga = Prayoga.KARTARI,
        ))

        assertEquals(setOf(Karaka.KARANA), resolution.candidates)
        assertEquals(Karaka.ANIRDHARITA, resolution.resolved)
        assertEquals(Vibhakti.TRTIYA, resolution.resolvedVibhakti)
        assertEquals(listOf("2.3.20"), resolution.evidence.map { it.sutra })
    }

    @Test
    fun `audited special 2 3 rules resolve directly without fabricated 1 4 evidence`() {
        data class Case(
            val sutra: String,
            val vibhakti: Vibhakti,
            val relation: SemanticRelation,
        )

        listOf(
            Case("2.3.3", Vibhakti.TRTIYA, SemanticRelation.VEDIC_HU_OBJECT),
            Case("2.3.6", Vibhakti.TRTIYA, SemanticRelation.COMPLETION_LIMIT),
            Case("2.3.9", Vibhakti.SAPTAMI, SemanticRelation.SUPERIORITY_REFERENCE),
            Case("2.3.24", Vibhakti.PANCHAMI, SemanticRelation.NON_AGENT_DEBT_REFERENCE),
        ).forEach { case ->
            val participant = ParticipantFacts(
                id = case.sutra,
                expression = AvyayaPada(case.sutra, case.sutra),
                possibleVibhaktis = setOf(case.vibhakti),
                semanticRelations = setOf(case.relation) + when (case.sutra) {
                    "2.3.6" -> setOf(SemanticRelation.CONTINUOUS_TIME_DISTANCE_EXTENT)
                    "2.3.9" -> setOf(SemanticRelation.KARMAPRAVACANIYA_GOVERNANCE,
                        SemanticRelation.UPA_SUPERIORITY_KARMAPRAVACANIYA_LINK)
                    else -> emptySet()
                },
            )
            val resolution = KarakaRuleEngine.resolve(KarakaRuleContext(
                dhatu = DhatuIdentity(if (case.sutra == "2.3.3") "हु" else "कृ"),
                participant = participant,
                allParticipants = listOf(participant),
                prayoga = Prayoga.KARTARI,
            ))

            assertEquals(Karaka.ANIRDHARITA, resolution.resolved, case.sutra)
            assertEquals(case.vibhakti, resolution.resolvedVibhakti, case.sutra)
            assertEquals(listOf(case.sutra), resolution.evidence.map { it.sutra }, case.sutra)
        }
    }

    @Test
    fun `indifferent target is resolved as karman`() {
        val dhatu = "पठ"
        val possibleVibhaktis = setOf(Vibhakti.DVITIYA)
        val participant = ParticipantFacts(
            id = "test_p",
            expression = AvyayaPada("विषम्", "विषम्"),
            possibleVibhaktis = possibleVibhaktis,
            semanticRelations = setOf(SemanticRelation.INDIFFERENT_OBJECT),
        )
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity(dhatu),
                participant = participant,
                allParticipants = listOf(participant),
                prayoga = Prayoga.KARTARI,
                verbNode = buildMockVerbNode(dhatu),
                baseDhatu = findMockDhatu(dhatu),
            ),
        )
        assertEquals(Karaka.KARMAN, resolution.resolved)
        assertEquals(listOf("1.4.50", "2.3.2"), resolution.evidence.map { it.sutra })
    }

    @Test
    fun `bhyam is resolved as sampradana for shlagh`() {
        assertResolution("श्लाघ", Karaka.SAMPRADANA, Vibhakti.CHATURTHI, "1.4.34", "2.3.13",
            additionalRelations = setOf(SemanticRelation.INTENDED_TO_BE_INFORMED))
    }

    @Test
    fun `instrument of div is resolved optionally as karman`() {
        val dhatu = "दिव"
        val profile = DhatuKarakaProfiles.forSurface(dhatu)
        val possibleVibhaktis = setOf(Vibhakti.DVITIYA)
        val participant = ParticipantFacts(
            id = "test_p",
            expression = AvyayaPada("अक्षान्", "अक्षान्"),
            possibleVibhaktis = possibleVibhaktis,
            semanticRelations = profile?.relations.orEmpty(),
        )
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity(dhatu),
                participant = participant,
                allParticipants = listOf(participant),
                prayoga = Prayoga.KARTARI,
                verbNode = buildMockVerbNode(dhatu),
                baseDhatu = findMockDhatu(dhatu),
            ),
        )
        assertEquals(Karaka.KARMAN, resolution.resolved)
        assertEquals(listOf("1.4.43", "2.3.2"), resolution.evidence.map { it.sutra })
        assertEquals(Karaka.KARANA, resolution.alternatives.single().karaka)
        assertEquals(Vibhakti.TRTIYA, resolution.alternatives.single().vibhakti)
        assertEquals(KarakaAlternativeKind.COEXISTENT_CASE, resolution.alternatives.single().kind)
        assertEquals(false, resolution.alternatives.single().matchesObservedMorphology)
        assertEquals(listOf("1.4.43", "2.3.18"), resolution.alternatives.single().evidence.map { it.sutra })
        assertEquals(true, resolution.alternatives.single().evidence.first().applied)
        assertEquals(null, resolution.alternatives.single().optionalDecision)
        assertEquals(setOf(Karaka.KARMAN, Karaka.KARANA), resolution.coexistingKarakas)
    }

    @Test
    fun `instrument of parikri is resolved optionally as sampradana`() {
        val dhatu = "परिक्री"
        val profile = DhatuKarakaProfiles.forSurface(dhatu)
        val possibleVibhaktis = setOf(Vibhakti.CHATURTHI)
        val participant = ParticipantFacts(
            id = "test_p",
            expression = AvyayaPada("शताय", "शताय"),
            possibleVibhaktis = possibleVibhaktis,
            semanticRelations = profile?.relations.orEmpty() + SemanticRelation.FIXED_TERM_HIRING_PAYMENT,
        )
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity(dhatu),
                participant = participant,
                allParticipants = listOf(participant),
                prayoga = Prayoga.KARTARI,
                verbNode = buildMockVerbNode(dhatu),
                baseDhatu = findMockDhatu(dhatu),
            ),
        )
        assertEquals(Karaka.SAMPRADANA, resolution.resolved)
        assertEquals(listOf("1.4.44", "2.3.13"), resolution.evidence.map { it.sutra })
        assertEquals(Karaka.KARANA, resolution.alternatives.single().karaka)
        assertEquals(Vibhakti.TRTIYA, resolution.alternatives.single().vibhakti)
        assertEquals(listOf("1.4.42", "2.3.18"), resolution.alternatives.single().evidence.map { it.sutra })
    }

    @Test
    fun `bhyam is resolved as apadana for varay`() {
        assertResolution("वारय", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.27", "2.3.28",
            additionalRelations = setOf(SemanticRelation.PREVENTED_DESIRED_OBJECT))
    }

    @Test
    fun `prevention distinguishes desired destination from restrained object and field locus`() {
        val source = ParticipantFacts("barley", AvyayaPada("यवेभ्यः", "यवेभ्यः"),
            setOf(Vibhakti.PANCHAMI), semanticRelations = setOf(SemanticRelation.SOURCE))
        fun resolve(p: ParticipantFacts) = KarakaRuleEngine.resolve(KarakaRuleContext(
            DhatuIdentity("वारय"), p, listOf(p), Prayoga.KARTARI))
        assertEquals(null, resolve(source).resolved)
        val desired = source.copy(semanticRelations = source.semanticRelations + SemanticRelation.PREVENTED_DESIRED_OBJECT)
        assertEquals(listOf("1.4.27", "2.3.28"), resolve(desired).evidence.map { it.sutra })
        val cows = source.copy(possibleVibhaktis = setOf(Vibhakti.DVITIYA),
            semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT))
        assertEquals(listOf("1.4.49", "2.3.2"), resolve(cows).evidence.map { it.sutra })
        val field = source.copy(possibleVibhaktis = setOf(Vibhakti.SAPTAMI),
            semanticRelations = setOf(SemanticRelation.LOCATION))
        assertEquals(listOf("1.4.45", "2.3.36"), resolve(field).evidence.map { it.sutra })
    }

    @Test
    fun `bhyam is resolved as apadana for nili`() {
        assertResolution("निली", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.28", "2.3.28",
            additionalRelations = setOf(SemanticRelation.CONCEALMENT_OBSERVER))
    }

    @Test
    fun `concealment apadana requires intended observer rather than generic source`() {
        val source = ParticipantFacts("observer", AvyayaPada("उपाध्यायात्", "उपाध्यायात्"),
            setOf(Vibhakti.PANCHAMI), semanticRelations = setOf(SemanticRelation.SOURCE))
        fun resolve(root: String, p: ParticipantFacts) = KarakaRuleEngine.resolve(
            KarakaRuleContext(DhatuIdentity(root), p, listOf(p), Prayoga.KARTARI))
        assertEquals(null, resolve("निली", source).resolved)
        val observer = source.copy(semanticRelations = source.semanticRelations + SemanticRelation.CONCEALMENT_OBSERVER)
        assertEquals(listOf("1.4.28", "2.3.28"), resolve("निली", observer).evidence.map { it.sutra })
        assertEquals(false, resolve("दृश्", observer).evidence.any { it.sutra == "1.4.28" })
        val objectPada = source.copy(possibleVibhaktis = setOf(Vibhakti.DVITIYA),
            semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT))
        assertEquals(listOf("1.4.49", "2.3.2"), resolve("दृश्", objectPada).evidence.map { it.sutra })
    }

    @Test
    fun `bhyam is resolved as sampradana for dharay`() {
        assertResolution("धारय", Karaka.SAMPRADANA, Vibhakti.CHATURTHI, "1.4.35", "2.3.13",
            additionalRelations = setOf(SemanticRelation.CREDITOR))
    }

    @Test
    fun `dhari generic recipient does not prove creditor relation`() {
        val participant = ParticipantFacts("recipient", AvyayaPada("test", "test"),
            setOf(Vibhakti.CHATURTHI), semanticRelations = setOf(SemanticRelation.RECIPIENT))
        val result = KarakaRuleEngine.resolve(KarakaRuleContext(
            DhatuIdentity("धारय"), participant, listOf(participant), Prayoga.KARTARI))
        assertEquals(null, result.resolved)
        assertEquals(emptyList(), result.evidence)
    }

    @Test
    fun `prefixed anger rules cover shared upasarga inventory and supersede dative`() {
        listOf("अभि", "प्रति", "उप", "वि", "अधि", "परि", "अति").forEach { prefix ->
            listOf("क्रुध्", "द्रुह्").forEach { root ->
                val participant = ParticipantFacts("target", AvyayaPada("target", "target"),
                    setOf(Vibhakti.DVITIYA, Vibhakti.CHATURTHI),
                    semanticRelations = setOf(SemanticRelation.RECIPIENT, SemanticRelation.ANGER_TARGET))
                val result = KarakaRuleEngine.resolve(KarakaRuleContext(
                    DhatuIdentity(prefix + root), participant, listOf(participant), Prayoga.KARTARI))
                assertEquals(Karaka.KARMAN, result.resolved, prefix + root)
                assertEquals(listOf("1.4.38", "2.3.2"), result.evidence.map { it.sutra }, prefix + root)
            }
        }
    }

    @Test
    fun `bhyam is resolved as apadana for paraji`() {
        assertResolution("पराजि", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.26", "2.3.28",
            additionalRelations = setOf(SemanticRelation.UNBEARABLE_OBJECT))
    }

    @Test
    fun `para ji requires unbearable sense and uses structured prefix identity`() {
        val source = ParticipantFacts("study", AvyayaPada("अध्ययनात्", "अध्ययनात्"),
            setOf(Vibhakti.PANCHAMI), semanticRelations = setOf(SemanticRelation.SOURCE))
        fun resolve(prefixes: List<String>, participant: ParticipantFacts): KarakaResolution {
            val verb = TingantaPada("verb", prefixes, DhatuPrakriti("जि", "जि"),
                Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("जि"),
                participant, listOf(participant), Prayoga.KARTARI, verbNode = verb))
        }
        assertEquals(null, resolve(listOf("परा"), source).resolved)
        val unbearable = source.copy(semanticRelations = source.semanticRelations + SemanticRelation.UNBEARABLE_OBJECT)
        assertEquals(listOf("1.4.26", "2.3.28"), resolve(listOf("परा"), unbearable).evidence.map { it.sutra })
        assertEquals(false, resolve(listOf("प्र"), unbearable).evidence.any { it.sutra == "1.4.26" })
        assertEquals(false, resolve(listOf("परा", "अज्ञात"), unbearable).evidence.any { it.sutra == "1.4.26" })
        val enemy = source.copy(possibleVibhaktis = setOf(Vibhakti.DVITIYA),
            semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT))
        assertEquals(listOf("1.4.49", "2.3.2"), resolve(listOf("परा"), enemy).evidence.map { it.sutra })
    }

    @Test
    fun `bhyam is resolved as apadana for adhi`() {
        assertResolution("अधी", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.29", "2.3.28",
            additionalRelations = setOf(SemanticRelation.FORMAL_INSTRUCTION_SOURCE))
    }

    @Test
    fun `learning root with generic source does not establish formal instruction`() {
        val participant = ParticipantFacts(
            "source", AvyayaPada("उपाध्यायात्", "उपाध्यायात्"), setOf(Vibhakti.PANCHAMI),
            semanticRelations = setOf(SemanticRelation.SOURCE),
        )
        val resolution = KarakaRuleEngine.resolve(KarakaRuleContext(
            DhatuIdentity("अधी"), participant, listOf(participant), Prayoga.KARTARI,
        ))
        assertEquals(null, resolution.resolved)
        assertEquals(emptyList(), resolution.evidence)
    }

    @Test
    fun `bhyam is resolved as sampradana for sprha`() {
        assertResolution("स्पृह", Karaka.SAMPRADANA, Vibhakti.CHATURTHI, "1.4.36", "2.3.13",
            additionalRelations = setOf(SemanticRelation.LONGED_FOR_OBJECT))
    }

    @Test
    fun `sprha distinguishes mere longing from ipsitatama object and forest locus`() {
        val recipient = ParticipantFacts("flowers", AvyayaPada("पुष्पेभ्यः", "पुष्पेभ्यः"),
            setOf(Vibhakti.CHATURTHI), semanticRelations = setOf(SemanticRelation.RECIPIENT))
        fun resolve(p: ParticipantFacts) = KarakaRuleEngine.resolve(KarakaRuleContext(
            DhatuIdentity("स्पृह"), p, listOf(p), Prayoga.KARTARI))
        assertEquals(null, resolve(recipient).resolved)
        val longing = recipient.copy(semanticRelations = recipient.semanticRelations + SemanticRelation.LONGED_FOR_OBJECT)
        assertEquals(listOf("1.4.36", "2.3.13"), resolve(longing).evidence.map { it.sutra })
        val preferred = longing.copy(possibleVibhaktis = setOf(Vibhakti.DVITIYA, Vibhakti.CHATURTHI),
            semanticRelations = longing.semanticRelations + SemanticRelation.DESIRED_OBJECT)
        assertEquals(listOf("1.4.49", "2.3.2"), resolve(preferred).evidence.map { it.sutra })
        val forest = recipient.copy(possibleVibhaktis = setOf(Vibhakti.SAPTAMI),
            semanticRelations = setOf(SemanticRelation.LOCATION))
        assertEquals(listOf("1.4.45", "2.3.36"), resolve(forest).evidence.map { it.sutra })
    }

    @Test
    fun `location of prefixed vish is resolved as karman`() {
        val dhatu = "अभिनिविश"
        val profile = DhatuKarakaProfiles.forSurface(dhatu)
        val possibleVibhaktis = setOf(Vibhakti.DVITIYA, Vibhakti.SAPTAMI)
        val participant = ParticipantFacts(
            id = "test_p",
            expression = AvyayaPada("मार्गम्", "मार्गम्"),
            possibleVibhaktis = possibleVibhaktis,
            semanticRelations = profile?.relations.orEmpty(),
        )
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity(dhatu),
                participant = participant,
                allParticipants = listOf(participant),
                prayoga = Prayoga.KARTARI,
                verbNode = buildMockVerbNode(dhatu),
                baseDhatu = findMockDhatu(dhatu),
            ),
        )
        assertEquals(Karaka.KARMAN, resolution.resolved)
        assertEquals(listOf("1.4.47", "2.3.2"), resolution.evidence.map { it.sutra })
    }

    @Test
    fun `bhyam is resolved as sampradana for anger target of krudh`() {
        assertResolution("क्रुध्", Karaka.SAMPRADANA, Vibhakti.CHATURTHI, "1.4.37", "2.3.13",
            additionalRelations = setOf(SemanticRelation.ANGER_TARGET))
    }

    @Test
    fun `bhyam is resolved as apadana for fear of bhi`() {
        assertResolution("भी", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.25", "2.3.28",
            additionalRelations = setOf(SemanticRelation.FEAR_CAUSE))
    }

    @Test
    fun `fear cause differs from forest locus and protected object across fear and protection roots`() {
        val source = ParticipantFacts("thief", AvyayaPada("चौरात्", "चौरात्"),
            setOf(Vibhakti.PANCHAMI), semanticRelations = setOf(SemanticRelation.SOURCE))
        fun resolve(root: String, p: ParticipantFacts) = KarakaRuleEngine.resolve(KarakaRuleContext(
            DhatuIdentity(root), p, listOf(p), Prayoga.KARTARI))
        for (root in listOf("भी", "त्रा", "रक्ष्", "उद्विज्")) {
            assertEquals(null, resolve(root, source).resolved, root)
            val danger = source.copy(semanticRelations = source.semanticRelations + SemanticRelation.FEAR_CAUSE)
            assertEquals(listOf("1.4.25", "2.3.28"), resolve(root, danger).evidence.map { it.sutra }, root)
            val forest = source.copy(possibleVibhaktis = setOf(Vibhakti.SAPTAMI),
                semanticRelations = setOf(SemanticRelation.LOCATION))
            assertEquals(listOf("1.4.45", "2.3.36"), resolve(root, forest).evidence.map { it.sutra }, root)
        }
        val protected = source.copy(possibleVibhaktis = setOf(Vibhakti.DVITIYA),
            semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT))
        assertEquals(listOf("1.4.49", "2.3.2"), resolve("रक्ष्", protected).evidence.map { it.sutra })
    }

    @Test
    fun `bhyam is resolved as sampradana for pleasing ruc`() {
        assertResolution("रुच", Karaka.SAMPRADANA, Vibhakti.CHATURTHI, "1.4.33", "2.3.13",
            additionalRelations = setOf(SemanticRelation.PLEASED_EXPERIENCER))
    }

    @Test
    fun `ruc and svad require pleased experiencer while pleasing subject and path remain distinct`() {
        val recipient = ParticipantFacts("experiencer", AvyayaPada("देवदत्ताय", "देवदत्ताय"),
            setOf(Vibhakti.CHATURTHI), semanticRelations = setOf(SemanticRelation.RECIPIENT))
        fun resolve(root: String, p: ParticipantFacts) = KarakaRuleEngine.resolve(KarakaRuleContext(
            DhatuIdentity(root), p, listOf(p), Prayoga.KARTARI))
        for (root in listOf("रुच्", "स्वद्")) {
            assertEquals(null, resolve(root, recipient).resolved, root)
            val experiencer = recipient.copy(semanticRelations = recipient.semanticRelations + SemanticRelation.PLEASED_EXPERIENCER)
            assertEquals(listOf("1.4.33", "2.3.13"), resolve(root, experiencer).evidence.map { it.sutra }, root)
            val path = recipient.copy(possibleVibhaktis = setOf(Vibhakti.SAPTAMI),
                semanticRelations = setOf(SemanticRelation.LOCATION))
            assertEquals(listOf("1.4.45", "2.3.36"), resolve(root, path).evidence.map { it.sutra }, root)
            val sweet = recipient.copy(possibleVibhaktis = setOf(Vibhakti.PRATHAMA),
                semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT))
            assertEquals(listOf("1.4.54", "2.3.46"), resolve(root, sweet).evidence.map { it.sutra }, root)
        }
    }

    @Test
    fun `recipient of prefixed anger verb is resolved as karman`() {
        val dhatu = "अभिक्रुध"
        val profile = DhatuKarakaProfiles.forSurface(dhatu)
        val possibleVibhaktis = setOf(Vibhakti.DVITIYA, Vibhakti.CHATURTHI)
        val participant = ParticipantFacts(
            id = "test_p",
            expression = AvyayaPada("क्रूरम्", "क्रूरम्"),
            possibleVibhaktis = possibleVibhaktis,
            semanticRelations = profile?.relations.orEmpty() + SemanticRelation.ANGER_TARGET,
        )
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity(dhatu),
                participant = participant,
                allParticipants = listOf(participant),
                prayoga = Prayoga.KARTARI,
                verbNode = buildMockVerbNode(dhatu),
                baseDhatu = findMockDhatu(dhatu),
            ),
        )
        assertEquals(Karaka.KARMAN, resolution.resolved)
        assertEquals(listOf("1.4.38", "2.3.2"), resolution.evidence.map { it.sutra })
    }

    @Test
    fun `location of prefixed dwelling verb is resolved as karman`() {
        val dhatu = "अधिवस"
        val profile = DhatuKarakaProfiles.forSurface(dhatu)
        val possibleVibhaktis = setOf(Vibhakti.DVITIYA, Vibhakti.SAPTAMI)
        val participant = ParticipantFacts(
            id = "test_p",
            expression = AvyayaPada("वैकुण्ठम्", "वैकुण्ठम्"),
            possibleVibhaktis = possibleVibhaktis,
            semanticRelations = profile?.relations.orEmpty(),
        )
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity(dhatu),
                participant = participant,
                allParticipants = listOf(participant),
                prayoga = Prayoga.KARTARI,
                verbNode = buildMockVerbNode(dhatu),
                baseDhatu = findMockDhatu(dhatu),
            ),
        )
        assertEquals(Karaka.KARMAN, resolution.resolved)
        assertEquals(listOf("1.4.48", "2.3.2"), resolution.evidence.map { it.sutra })
    }

    @Test
    fun `location of adhishi is resolved as karman`() {
        val dhatu = "अधिशी"
        val profile = DhatuKarakaProfiles.forSurface(dhatu)
        val possibleVibhaktis = setOf(Vibhakti.DVITIYA, Vibhakti.SAPTAMI)
        val participant = ParticipantFacts(
            id = "test_p",
            expression = AvyayaPada("वैकुण्ठम्", "वैकुण्ठम्"),
            possibleVibhaktis = possibleVibhaktis,
            semanticRelations = profile?.relations.orEmpty(),
        )
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity(dhatu),
                participant = participant,
                allParticipants = listOf(participant),
                prayoga = Prayoga.KARTARI,
                verbNode = buildMockVerbNode(dhatu),
                baseDhatu = findMockDhatu(dhatu),
            ),
        )
        assertEquals(Karaka.KARMAN, resolution.resolved)
        assertEquals(listOf("1.4.46", "2.3.2"), resolution.evidence.map { it.sutra })
    }

    private fun assertResolution(
        dhatu: String,
        karaka: Karaka,
        vibhakti: Vibhakti,
        semanticSutra: String,
        vibhaktiSutra: String,
        additionalRelations: Set<SemanticRelation> = emptySet(),
        lexicalRelations: Set<SemanticRelation>? = null,
    ) {
        val profile = DhatuKarakaProfiles.forSurface(dhatu)
        val possibleVibhaktis = SupAffix.candidates("भ्याम्").mapTo(mutableSetOf()) { it.vibhakti }
        val participant = ParticipantFacts(
            id = "test_p",
            expression = AvyayaPada("भ्याम्", "भ्याम्"),
            possibleVibhaktis = possibleVibhaktis,
            semanticRelations = (lexicalRelations ?: profile?.relations.orEmpty()) + additionalRelations,
            intendedObject = if (semanticSutra == "1.4.32") IntendedObject.Implied else null,
        )
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity(dhatu),
                participant = participant,
                allParticipants = listOf(participant),
                prayoga = Prayoga.KARTARI,
                verbNode = buildMockVerbNode(dhatu),
                baseDhatu = findMockDhatu(dhatu),
            ),
        )
        assertEquals(karaka, resolution.resolved)
        assertEquals(setOf(Vibhakti.TRTIYA, Vibhakti.CHATURTHI, Vibhakti.PANCHAMI), resolution.possibleVibhaktis)
        assertEquals(listOf(semanticSutra, vibhaktiSutra), resolution.evidence.map { it.sutra })
        assertEquals(vibhakti, resolution.possibleVibhaktis.single { candidate ->
            candidate == when (karaka) {
                Karaka.KARANA -> Vibhakti.TRTIYA
                Karaka.SAMPRADANA -> Vibhakti.CHATURTHI
                Karaka.APADANA -> Vibhakti.PANCHAMI
                else -> error("Unexpected test kāraka")
            }
        })
    }

    private fun buildMockVerbNode(dhatu: String): TingantaPada {
        val prefixes = listOf("प्रति", "अनु", "अधि", "अभि", "उप", "आ", "परा", "अभि-नि", "अभिनि")
        val matchedPrefix = prefixes.sortedByDescending { it.length }.firstOrNull { dhatu.startsWith(it) }
        val baseDhatu = if (matchedPrefix != null) dhatu.substring(matchedPrefix.length) else dhatu
        val upasargas = if (matchedPrefix != null) {
            if (matchedPrefix == "अभिनि" || matchedPrefix == "अभि-नि") listOf("अभि", "नि")
            else listOf(matchedPrefix)
        } else emptyList()
        return TingantaPada(
            sourceText = dhatu,
            upasargas = upasargas,
            dhatu = DhatuPrakriti(
                sourceText = baseDhatu,
                mulaDhatu = when (baseDhatu) {
                    "धारय" -> "धृ"
                    "वस", "वस्" -> "वस"
                    "शृ", "श्रु" -> "श्रु"
                    "गृ", "गृणाति" -> "गृ"
                    "शी", "शीङ्" -> "शी"
                    "स्था", "तिष्ठ" -> "स्था"
                    "आस्", "आस" -> "आस्"
                    else -> baseDhatu
                },
                sanadiPratyayas = if (baseDhatu == "धारय") listOf("णिच्") else emptyList(),
            ),
            lakara = Lakara.LAT,
            ting = TingPratyaya("", "")
        )
    }

    private fun findMockDhatu(dhatu: String): Dhatu? {
        val prefixes = listOf("प्रति", "अनु", "अधि", "अभि", "उप", "आ", "अभि-नि", "अभिनि")
        val matchedPrefix = prefixes.firstOrNull { dhatu.startsWith(it) }
        val baseDhatu = if (matchedPrefix != null) dhatu.substring(matchedPrefix.length) else dhatu
        val mulaDhatu = when (baseDhatu) {
            "वस", "वस्" -> "वस"
            "शृ", "श्रु" -> "श्रु"
            "गृ", "गृणाति" -> "गृ"
            "शी", "शीङ्" -> "शी"
            "स्था", "तिष्ठ" -> "स्था"
            "आस्", "आस" -> "आस्"
            else -> baseDhatu
        }
        if (mulaDhatu in setOf("गृ", "गॄ")) {
            return DhatuPatha.all.single { it.sourceSurface == "गॄ" && it.gana == dev.panini.core.DhatuGana.KRYADI }
        }
        return DhatuPatha.all.firstOrNull {
            it.upadesha == mulaDhatu || it.sourceSurface == mulaDhatu || it.upadesha == "${mulaDhatu}ँ" ||
            (mulaDhatu == "वस" && it.upadesha == "वसँ") || (mulaDhatu == "शी" && it.upadesha == "शीङ्")
        }
    }

    @Test
    fun `karmani prayoga resolves karman as abhihita prathama and kartr as anabhihita trtiya`() {
        val kartrParticipant = ParticipantFacts(
            id = "devadatta",
            expression = AvyayaPada("देवदत्तेन", "देवदत्तेन"),
            possibleVibhaktis = setOf(Vibhakti.TRTIYA),
            semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT),
        )
        val kartrRes = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity("कृ", true),
                participant = kartrParticipant,
                allParticipants = listOf(kartrParticipant),
                prayoga = Prayoga.KARMANI,
            ),
        )
        assertEquals(Karaka.KARTR, kartrRes.resolved)
        assertEquals(Vibhakti.TRTIYA, kartrRes.resolvedVibhakti)
        assertEquals(listOf("1.4.54", "2.3.18"), kartrRes.evidence.map { it.sutra })

        val karmanParticipant = ParticipantFacts(
            id = "kata",
            expression = AvyayaPada("कटः", "कटः"),
            possibleVibhaktis = setOf(Vibhakti.PRATHAMA),
            semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT),
        )
        val karmanRes = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity("कृ", true),
                participant = karmanParticipant,
                allParticipants = listOf(karmanParticipant),
                prayoga = Prayoga.KARMANI,
            ),
        )
        assertEquals(Karaka.KARMAN, karmanRes.resolved)
        assertEquals(Vibhakti.PRATHAMA, karmanRes.resolvedVibhakti)
        assertEquals(listOf("1.4.49", "2.3.46"), karmanRes.evidence.map { it.sutra })
    }

    @Test
    fun `bhave prayoga resolves kartr as anabhihita trtiya`() {
        val kartrParticipant = ParticipantFacts(
            id = "devadatta",
            expression = AvyayaPada("देवदत्तेन", "देवदत्तेन"),
            possibleVibhaktis = setOf(Vibhakti.TRTIYA),
            semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT),
        )
        val kartrRes = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity("भू", false),
                participant = kartrParticipant,
                allParticipants = listOf(kartrParticipant),
                prayoga = Prayoga.BHAVE,
            ),
        )
        assertEquals(Karaka.KARTR, kartrRes.resolved)
        assertEquals(Vibhakti.TRTIYA, kartrRes.resolvedVibhakti)
        assertEquals(listOf("1.4.54", "2.3.18"), kartrRes.evidence.map { it.sutra })
    }
}
