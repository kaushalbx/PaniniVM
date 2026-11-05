package dev.panini.analysis

import dev.panini.core.*
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Karmatva
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.lexicon.*
import kotlin.test.*

class KrtGovernorFrameTest {
    @Test fun `meaning equivalent nominal secondary objects belong to their own action`() {
        for ((root, prefixes) in listOf("भाष्" to emptyList(), "वच्" to emptyList(), "वद्" to listOf("अभि"),
            "कथ्" to emptyList())) for (passive in listOf(false, true)) for (ending in listOf("ङस्", "टा")) {
            val dependent = noun("माणवक", ending)
            val governor = krt("ल्युट्", root).let {
                it.copy(pratipadika = (it.pratipadika as KridantaPratipadika).copy(upasargas = prefixes))
            }
            val outer = verb(passive = passive)
            val meanings = setOf(SemanticRelation.SECONDARY_OBJECT) +
                if (root == "कथ्") setOf(SemanticRelation.DVIKARMAKA_ACTION_SENSE) else emptySet()
            val frame = analyzer.analyze(AkhyataVakya("nominal meaning equivalent", listOf(dependent, governor, outer), outer),
                semanticContext = VakyaSemanticContext(mapOf(0 to meanings),
                    krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet(), setOf(Karaka.KARTR, Karaka.KARMAN)))))
            val relation = frame.nominalKarakas.single().relation
            assertEquals(listOf("1.4.51") + if (ending == "ङस्") listOf("2.3.66") else emptyList(), relation.evidence.map { it.sutra })
            assertEquals(if (ending == "ङस्") Vibhakti.SASTHI else null, relation.resolvedVibhakti)
            assertSame(dependent, relation.participant.pada)
        }
    }

    @Test fun `nominal desired undesired and secondary object proofs require their own established readings`() {
        for ((root, meaning, rule) in listOf(
            Triple("भुज्", SemanticRelation.DESIRED_OBJECT, "1.4.49"),
            Triple("भुज्", SemanticRelation.INDIFFERENT_OBJECT, "1.4.50"),
            Triple("दुह्", SemanticRelation.SECONDARY_OBJECT, "1.4.51"))) {
            for (passive in listOf(false, true)) for (ending in listOf("ङस्", "अम्", "टा"))
                for (supplied in listOf(false, true)) {
                val dependent = noun("फल", ending)
                val governor = krt("ल्युट्", root)
                val outer = verb(passive = passive)
                val frame = analyzer.analyze(AkhyataVakya("nominal object reading", listOf(dependent, governor, outer), outer),
                    semanticContext = VakyaSemanticContext(
                        participantRelations = if (supplied) mapOf(0 to setOf(meaning)) else emptyMap(),
                        krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet()))))
                val relation = frame.nominalKarakas.single().relation
                if (supplied) {
                    assertEquals(Karaka.KARMAN, (relation.resolution as FrameKarakaResolution.Resolved).karaka)
                    assertEquals(listOf(rule) + if (ending == "ङस्") listOf("2.3.66") else emptyList(),
                        relation.evidence.map { it.sutra })
                    assertEquals(if (ending == "ङस्") Vibhakti.SASTHI else null, relation.resolvedVibhakti)
                } else assertTrue(relation.evidence.none { it.sutra in setOf("1.4.49", "1.4.50", "1.4.51") })
                assertSame(dependent, relation.participant.pada)
            }
        }
    }

    @Test fun `nominal physical nonpath motion goal excludes krt genitive`() {
        for (passive in listOf(false, true)) for (agentDenoting in listOf(false, true))
            for (ending in listOf("अम्", "ङे", "ङस्")) {
            val goal = noun("ग्राम", ending)
            val governor = krt(if (agentDenoting) "तृच्" else "ल्युट्", "गम्")
            val finite = verb(passive = passive)
            val frame = analyzer.analyze(AkhyataVakya("nominal physical motion goal", listOf(goal, governor, finite), finite),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.DESIRED_OBJECT,
                    SemanticRelation.MOTION_GOAL, SemanticRelation.PHYSICAL_MOTION, SemanticRelation.NON_PATH_DESTINATION)),
                    krtGovernors = mapOf(0 to KrtGovernorReading(1, if (agentDenoting) setOf(Karaka.KARTR) else emptySet()))))
            val relation = frame.nominalKarakas.single().relation
            assertEquals(listOf("1.4.49") + if (ending == "ङस्") emptyList() else listOf("2.3.12"),
                relation.evidence.map { it.sutra })
            assertEquals(when (ending) { "अम्" -> Vibhakti.DVITIYA; "ङे" -> Vibhakti.CHATURTHI; else -> null },
                relation.resolvedVibhakti)
        }
    }
    @Test fun `nominal recipient waits for competing object mediation to resolve`() {
        for (other in listOf(SemanticRelation.LOCATION, SemanticRelation.INSTRUMENT, SemanticRelation.SOURCE)) {
        val otherEnding = when (other) { SemanticRelation.LOCATION -> "ङि"; SemanticRelation.INSTRUMENT -> "टा"; else -> "ङसिँ" }
        val otherRole = when (other) { SemanticRelation.LOCATION -> Karaka.ADHIKARANA; SemanticRelation.INSTRUMENT -> Karaka.KARANA; else -> Karaka.APADANA }
        val otherChain = when (other) { SemanticRelation.LOCATION -> listOf("1.4.45", "2.3.36"); SemanticRelation.INSTRUMENT -> listOf("1.4.42", "2.3.18"); else -> listOf("1.4.24", "2.3.28") }
        for (ending in listOf("ङस्", otherEnding, "अम्")) for (bound in listOf(false, true))
            for (passive in listOf(false, true)) {
            val recipient = noun("राम", "ङे")
            val mediator = noun("फल", ending)
            val governor = krt("ल्युट्", "दा")
            val finite = verb(passive = passive)
            val reading = KrtGovernorReading(2, emptySet())
            val frame = analyzer.analyze(AkhyataVakya("competing nominal mediator", listOf(recipient, mediator, governor, finite), finite),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.RECIPIENT),
                    1 to (setOf(SemanticRelation.DESIRED_OBJECT, other) +
                        if (other == SemanticRelation.SOURCE) setOf(SemanticRelation.SEPARATION_REFERENCE) else emptySet())),
                    intendedObjects = if (bound) mapOf(0 to 1) else emptyMap(),
                    krtGovernors = mapOf(0 to reading, 1 to reading)))
            val relation = frame.nominalKarakas.single { it.dependent == 0 }.relation
            val objectRelation = frame.nominalKarakas.single { it.dependent == 1 }.relation
            when (ending) {
                "ङस्" -> {
                    assertEquals(listOf("1.4.49", "2.3.66"), objectRelation.evidence.map { it.sutra })
                    assertEquals(listOf("1.4.32", "2.3.13"), relation.evidence.map { it.sutra })
                }
                otherEnding -> {
                    assertEquals(otherChain, objectRelation.evidence.map { it.sutra })
                    assertTrue(relation.evidence.none { it.sutra == "1.4.32" })
                    assertTrue(relation.unresolvedObjectMediators.isEmpty())
                }
                else -> {
                    assertEquals(setOf(Karaka.KARMAN, otherRole),
                        (objectRelation.resolution as FrameKarakaResolution.Ambiguous).candidates)
                    assertEquals(mapOf("p_1" to setOf(Karaka.KARMAN, otherRole)), relation.unresolvedObjectMediators)
                    assertTrue(relation.evidence.none { it.sutra == "1.4.32" })
                }
            }
        }
        }
    }
    @Test fun `omitted nominal object requires an explicit implied object premise`() {
        for (implied in listOf(false, true)) for (passive in listOf(false, true)) {
            val recipient = noun("राम", "ङे")
            val governor = krt("ल्युट्", "दा")
            val finite = verb(passive = passive)
            val frame = analyzer.analyze(AkhyataVakya("omitted nominal gift", listOf(recipient, governor, finite), finite),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.RECIPIENT)),
                    intendedObjects = if (implied) mapOf(0 to null) else emptyMap(),
                    krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet()))))
            val relation = frame.nominalKarakas.single().relation
            assertEquals(if (implied) listOf("1.4.32", "2.3.13") else emptyList(), relation.evidence.map { it.sutra })
            assertEquals(if (implied) Vibhakti.CHATURTHI else null, relation.resolvedVibhakti)
            assertSame(recipient, relation.participant.pada)
        }
    }
    @Test fun `nominal recipient mediation only uses objects of the same action occurrence`() {
        for (owner in listOf("same", "finite", "other_nominal")) for (bound in listOf(false, true))
            for (passive in listOf(false, true)) {
            val recipient = noun("राम", "ङे")
            val gift = noun("फल", if (owner == "finite") { if (passive) "सुँ" else "अम्" } else "ङस्")
            val governor = krt("ल्युट्", "दा")
            val otherGovernor = governor.copy()
            val finite = verb(passive = passive)
            val readings = mutableMapOf(0 to KrtGovernorReading(2, emptySet()))
            if (owner != "finite") readings[1] = KrtGovernorReading(if (owner == "same") 2 else 3, emptySet())
            val frame = analyzer.analyze(AkhyataVakya("nominal recipient scope", listOf(
                recipient, gift, governor, otherGovernor, finite), finite),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.RECIPIENT),
                    1 to setOf(SemanticRelation.DESIRED_OBJECT)),
                    intendedObjects = if (bound) mapOf(0 to 1) else emptyMap(), krtGovernors = readings))
            val relation = frame.nominalKarakas.single { it.dependent == 0 }.relation
            if (owner == "same") {
                assertEquals(listOf("1.4.32", "2.3.13"), relation.evidence.map { it.sutra })
                val objectRelation = frame.nominalKarakas.single { it.dependent == 1 }.relation
                assertEquals(listOf("1.4.49", "2.3.66"), objectRelation.evidence.map { it.sutra })
                assertEquals(relation.kriyaId, objectRelation.kriyaId)
            } else {
                assertTrue(relation.evidence.none { it.sutra == "1.4.32" }, "$owner bound=$bound passive=$passive")
                assertNull(relation.resolvedVibhakti)
            }
        }
    }
    @Test fun `nominal hiring payment preserves applied declined and unresolved optional branches`() {
        for (passive in listOf(false, true)) for (ending in listOf("ङे", "टा", "भ्याम्", "ङस्")) {
            val payment = noun("वेतन", ending)
            val governor = krt("ल्युट्", "क्री").let {
                it.copy(pratipadika = (it.pratipadika as KridantaPratipadika).copy(upasargas = listOf("परि")))
            }
            val finite = verb(passive = passive)
            val frame = analyzer.analyze(AkhyataVakya("nominal hiring payment", listOf(payment, governor, finite), finite),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.INSTRUMENT,
                    SemanticRelation.FIXED_TERM_HIRING_PAYMENT)), krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet()))))
            val relation = frame.nominalKarakas.single().relation
            assertTrue(relation.coexistingKarakas.isEmpty())
            assertNotNull(relation.primaryOptionalDecision)
            when (ending) {
                "ङे" -> assertEquals(listOf("1.4.44", "2.3.13"), relation.evidence.map { it.sutra })
                "टा" -> assertEquals(listOf("1.4.42", "2.3.18"), relation.evidence.map { it.sutra })
                else -> {
                    assertEquals(setOf(Karaka.SAMPRADANA, Karaka.KARANA),
                        (relation.resolution as FrameKarakaResolution.Ambiguous).candidates)
                    assertNull(relation.resolvedVibhakti)
                }
            }
            val branch = relation.alternatives.single { it.optionalDecision != null }
            assertEquals("1.4.44", branch.optionalDecision?.sutra)
            assertTrue(branch.evidence.all { it.applied })
            assertSame(payment, relation.participant.pada)
        }
    }
    @Test fun `instrument denoting nominal div expresses the shared object instrument power`() {
        for (passive in listOf(false, true)) {
            val means = noun("अक्ष", "सुँ")
            val governor = krt("ल्युट्", "दिव्")
            val finite = verb(passive = passive)
            val frame = analyzer.analyze(AkhyataVakya("instrument denoting nominal gaming", listOf(means, governor, finite), finite),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.INSTRUMENT)),
                    krtGovernors = mapOf(0 to KrtGovernorReading(1, setOf(Karaka.KARANA)))))
            val relation = frame.nominalKarakas.single().relation
            assertEquals(setOf(Karaka.KARMAN, Karaka.KARANA), relation.coexistingKarakas)
            assertEquals(listOf("1.4.43", "2.3.46"), relation.evidence.map { it.sutra })
            assertEquals(Vibhakti.PRATHAMA, relation.resolvedVibhakti)
            assertTrue(relation.alternatives.isEmpty())
            assertNull(relation.primaryOptionalDecision)
        }
    }
    @Test fun `nominal div retains simultaneous object and instrument designations`() {
        for (passive in listOf(false, true)) for (ending in listOf("ङस्", "टा", "अम्")) {
            val means = noun("अक्ष", ending)
            val governor = krt("ल्युट्", "दिव्")
            val finite = verb(passive = passive)
            val frame = analyzer.analyze(AkhyataVakya("nominal gaming instrument", listOf(means, governor, finite), finite),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.INSTRUMENT)),
                    krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet()))))
            val relation = frame.nominalKarakas.single().relation
            assertEquals(setOf(Karaka.KARMAN, Karaka.KARANA), relation.coexistingKarakas)
            assertEquals(listOf("1.4.43") + when (ending) {
                "ङस्" -> listOf("2.3.66")
                "टा" -> listOf("2.3.18")
                else -> emptyList()
            }, relation.evidence.map { it.sutra })
            assertEquals(when (ending) { "ङस्" -> Vibhakti.SASTHI; "टा" -> Vibhakti.TRTIYA; else -> null },
                relation.resolvedVibhakti)
            assertNull(relation.primaryOptionalDecision)
            val alternate = relation.alternatives.single { it.kind == KarakaAlternativeKind.COEXISTENT_CASE }
            assertEquals(if (ending == "टा") Vibhakti.SASTHI else Vibhakti.TRTIYA, alternate.vibhakti)
            assertNull(alternate.optionalDecision)
        }
    }
    @Test fun `nominal causative object domain preserves lexical and construction restrictions`() {
        val domains = listOf(
            Triple("नी", emptySet<SemanticRelation>(), false),
            Triple("अद्", emptySet(), false), Triple("खाद्", emptySet(), false),
            Triple("वह्", setOf(SemanticRelation.VAH_NIYANTR_KARTRKA), true),
            Triple("वह्", setOf(SemanticRelation.VAH_ANIYANTR_KARTRKA), false),
            Triple("वह्", emptySet(), false),
            Triple("वह्", setOf(SemanticRelation.VAH_NIYANTR_KARTRKA, SemanticRelation.VAH_ANIYANTR_KARTRKA), false),
            Triple("भक्ष्", setOf(SemanticRelation.BHAKS_INJURIOUS_SENSE), true),
            Triple("भक्ष्", setOf(SemanticRelation.BHAKS_NONINJURIOUS_SENSE), false),
            Triple("भक्ष्", emptySet(), false),
            Triple("भक्ष्", setOf(SemanticRelation.BHAKS_INJURIOUS_SENSE, SemanticRelation.BHAKS_NONINJURIOUS_SENSE), false))
        for ((root, senses, objectReading) in domains) for (passive in listOf(false, true)) {
            val causee = noun("देवदत्त", if (objectReading) "ङस्" else "टा")
            val governor = krt("ल्युट्", root).let { original ->
                val base = original.pratipadika as KridantaPratipadika
                original.copy(pratipadika = base.copy(dhatu = base.dhatu.copy(sanadiPratyayas = listOf("णिच्"))))
            }
            val finite = verb(passive = passive)
            val frame = analyzer.analyze(AkhyataVakya("nominal causee restrictions", listOf(causee, governor, finite), finite),
                semanticContext = VakyaSemanticContext(mapOf(0 to (senses + setOf(
                    SemanticRelation.CAUSEE_AGENT, SemanticRelation.INDEPENDENT_AGENT))),
                    krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet()))))
            val relation = frame.nominalKarakas.single().relation
            assertEquals(if (objectReading) listOf("1.4.52", "2.3.66") else listOf("1.4.54", "2.3.18"),
                relation.evidence.map { it.sutra }, "$root $senses")
        }
    }
    @Test fun `nominal causee does not borrow causative morphology from the finite verb`() {
        for (root in listOf("आस्", "कृ")) for (passive in listOf(false, true)) {
            val causee = noun("देवदत्त", "ङस्")
            val governor = krt("ल्युट्", root)
            val finite = verb("कृ", passive).let {
                it.copy(dhatu = it.dhatu.copy(sanadiPratyayas = listOf("णिच्")))
            }
            val frame = analyzer.analyze(AkhyataVakya("separate causative owners", listOf(causee, governor, finite), finite),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.CAUSEE_AGENT)),
                    krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet()))))
            val relation = frame.nominalKarakas.single().relation
            assertTrue(relation.evidence.none { it.sutra in setOf("1.4.52", "1.4.53") })
            assertNull(relation.resolvedVibhakti)
        }
    }
    @Test fun `nominal causative separates causee object from hetu agent in both outer voices`() {
        for (passive in listOf(false, true)) for ((root, causeeSutra) in listOf("आस्" to "1.4.52", "कृ" to "1.4.53")) {
            val causee = noun("देवदत्त", "ङस्")
            val causer = noun("राम", "टा")
            val governor = krt("ल्युट्", root).let { original ->
                val base = original.pratipadika as KridantaPratipadika
                original.copy(pratipadika = base.copy(dhatu = base.dhatu.copy(sanadiPratyayas = listOf("णिच्"))))
            }
            val finite = verb(passive = passive)
            val reading = KrtGovernorReading(2, emptySet())
            val frame = analyzer.analyze(AkhyataVakya("nominal causative agents", listOf(causee, causer, governor, finite), finite),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.CAUSEE_AGENT),
                    1 to setOf(SemanticRelation.PROMPTER_CAUSE)), krtGovernors = mapOf(0 to reading, 1 to reading)))
            val objectRelation = frame.nominalKarakas.single { it.dependent == 0 }.relation
            val agentRelation = frame.nominalKarakas.single { it.dependent == 1 }.relation
            assertEquals(listOf(causeeSutra, "2.3.66"), objectRelation.evidence.map { it.sutra })
            if (causeeSutra == "1.4.53") {
                val underlying = objectRelation.alternatives.single { it.vibhakti == Vibhakti.TRTIYA }
                assertEquals(listOf("1.4.54", "2.3.18"), underlying.evidence.map { it.sutra })
            }
            assertEquals(listOf("1.4.55", "2.3.18"), agentRelation.evidence.map { it.sutra })
            assertEquals(setOf(KarakaDesignation.HETU), agentRelation.additionalDesignations)
            assertTrue(objectRelation.additionalDesignations.isEmpty())
            assertEquals(objectRelation.kriyaId, agentRelation.kriyaId)
            assertNotEquals(frame.id, objectRelation.kriyaId)
        }
    }
    @Test fun `unknown nominal prefix or sanadi cannot establish genitive licensing`() {
        for (unknownPrefix in listOf(false, true)) {
            val agent = noun("देवदत्त", "ङस्")
            val governor = krt("ल्युट्", "आस्").let { original ->
                val base = original.pratipadika as KridantaPratipadika
                original.copy(pratipadika = if (unknownPrefix) base.copy(upasargas = listOf("unknown-prefix"))
                    else base.copy(dhatu = base.dhatu.copy(sanadiPratyayas = listOf("unknown-sanadi"))))
            }
            val finite = verb()
            val frame = analyzer.analyze(AkhyataVakya("unknown governor morphology", listOf(agent, governor, finite), finite),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.INDEPENDENT_AGENT)),
                    krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet(), setOf(Karaka.KARTR)))))
            val relation = frame.nominalKarakas.single().relation
            assertNull(relation.resolvedVibhakti)
            assertTrue(relation.evidence.none { it.sutra.startsWith("2.3.") })
            assertTrue(relation.evidence.flatMap { it.semanticEvidence }.none {
                it.detail.startsWith("Explicit kṛt governor")
            }, "Unknown morphology cannot establish canonical kṛt governance premises")
        }
    }
    @Test fun `unknown nominal suffix cannot license a general instrumental case`() {
        val agent = noun("देवदत्त", "टा")
        val governor = krt("unsupported-affix", "आस्")
        assertNull((governor.pratipadika as KridantaPratipadika).krtAffix)
        val finite = verb()
        val frame = analyzer.analyze(AkhyataVakya("unsupported nominal morphology", listOf(agent, governor, finite), finite),
            semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.INDEPENDENT_AGENT)),
                krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet()))))
        val relation = frame.nominalKarakas.single().relation
        assertNull(relation.resolvedVibhakti)
        assertTrue(relation.evidence.none { it.sutra.startsWith("2.3.") })
    }
    @Test fun `nested nominal governor is both an action owner and another actions participant`() {
        for (passive in listOf(false, true)) for (agentSup in listOf("ङस्", "टा")) for (objectSup in listOf("ङस्", "अम्")) {
            val agent = noun("देवदत्त", agentSup)
            val inner = krt("ल्युट्", "आस्", objectSup)
            val outerNominal = krt("ल्युट्", "कृ")
            val finite = verb(passive = passive)
            val frame = analyzer.analyze(AkhyataVakya("nested nominal actions", listOf(agent, inner, outerNominal, finite), finite),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.INDEPENDENT_AGENT),
                    1 to setOf(SemanticRelation.DESIRED_OBJECT)), krtGovernors = mapOf(
                        0 to KrtGovernorReading(1, emptySet()), 1 to KrtGovernorReading(2, emptySet()))))
            val innerAgent = frame.nominalKarakas.single { it.dependent == 0 }.relation
            val outerObject = frame.nominalKarakas.single { it.dependent == 1 }.relation
            assertEquals(listOf("1.4.54") + if (agentSup == "ङस्") listOf("2.3.65") else emptyList(),
                innerAgent.evidence.map { it.sutra })
            assertEquals(if (agentSup == "ङस्") Vibhakti.SASTHI else null, innerAgent.resolvedVibhakti)
            assertEquals(listOf("1.4.49") + if (objectSup == "ङस्") listOf("2.3.66") else emptyList(),
                outerObject.evidence.map { it.sutra })
            assertEquals(if (objectSup == "ङस्") Vibhakti.SASTHI else null, outerObject.resolvedVibhakti)
            assertEquals(KriyaId("${frame.id}/krt_1"), innerAgent.kriyaId)
            assertEquals(KriyaId("${frame.id}/krt_2"), outerObject.kriyaId)
            assertSame(agent, innerAgent.participant.pada)
            assertSame(inner, outerObject.participant.pada)
            assertTrue(frame.relations.none { it.participant.pada === agent || it.participant.pada === inner })
        }
    }
    @Test fun `identical nominal action forms retain separate occurrence identities`() {
        for (passive in listOf(false, true)) {
            val firstAgent = noun("देवदत्त", "टा")
            val secondAgent = noun("राम", "टा")
            val secondObject = noun("ग्राम", "ङस्")
            val firstGovernor = krt("ल्युट्", "आस्").let {
                it.copy(pratipadika = (it.pratipadika as KridantaPratipadika).copy(upasargas = listOf("अधि")))
            }
            val secondGovernor = firstGovernor.copy()
            assertEquals(firstGovernor, secondGovernor)
            assertNotSame(firstGovernor, secondGovernor)
            val outer = verb(passive = passive)
            val firstReading = KrtGovernorReading(3, emptySet(), setOf(Karaka.KARTR, Karaka.KARMAN))
            val secondReading = KrtGovernorReading(4, emptySet(), setOf(Karaka.KARTR, Karaka.KARMAN))
            val frame = analyzer.analyze(AkhyataVakya("two identical nominal forms", listOf(
                firstAgent, secondAgent, secondObject, firstGovernor, secondGovernor, outer), outer),
                semanticContext = VakyaSemanticContext(mapOf(
                    0 to setOf(SemanticRelation.INDEPENDENT_AGENT),
                    1 to setOf(SemanticRelation.INDEPENDENT_AGENT), 2 to setOf(SemanticRelation.LOCATION)),
                    krtGovernors = mapOf(0 to firstReading, 1 to secondReading, 2 to secondReading)))
            val relations = frame.nominalKarakas.associate { it.dependent to it.relation }
            assertNotEquals(relations.getValue(0).kriyaId, relations.getValue(1).kriyaId)
            assertEquals(relations.getValue(1).kriyaId, relations.getValue(2).kriyaId)
            assertEquals(listOf("1.4.54", "2.3.18"), relations.getValue(0).evidence.map { it.sutra })
            assertEquals(listOf("1.4.54", "2.3.18"), relations.getValue(1).evidence.map { it.sutra })
            assertEquals(listOf("1.4.46", "2.3.66"), relations.getValue(2).evidence.map { it.sutra })
            assertSame(firstAgent, relations.getValue(0).participant.pada)
            assertSame(secondAgent, relations.getValue(1).participant.pada)
            assertSame(secondObject, relations.getValue(2).participant.pada)
            relations.values.forEach { relation ->
                val governance = relation.evidence.flatMap { it.semanticEvidence }.filter {
                    it.source == SemanticEvidenceSource.GOVERNING_CONSTRUCTION && it.detail.startsWith("Explicit kṛt governor")
                }
                assertTrue(governance.isNotEmpty())
                governance.forEach {
                    assertTrue(it.detail.contains("governor occurrence: ${relation.kriyaId};"), it.detail)
                    assertTrue(it.detail.contains("governorOccurrenceId=${relation.kriyaId},"), it.detail)
                }
            }
        }
    }
    @Test fun `dual eligible nominal action restricts genitive to its special locus object`() {
        for (passive in listOf(false, true)) for (agentSup in listOf("टा", "ङस्")) {
            val agent = noun("देवदत्त", agentSup)
            val locus = noun("ग्राम", "ङस्")
            val governor = krt("ल्युट्", "आस्").let {
                it.copy(pratipadika = (it.pratipadika as KridantaPratipadika).copy(upasargas = listOf("अधि")))
            }
            val outer = verb(passive = passive)
            val reading = KrtGovernorReading(2, emptySet(), setOf(Karaka.KARTR, Karaka.KARMAN))
            val frame = analyzer.analyze(AkhyataVakya("one nominal action with two dependents", listOf(agent, locus, governor, outer), outer),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.INDEPENDENT_AGENT),
                    1 to setOf(SemanticRelation.LOCATION)), krtGovernors = mapOf(0 to reading, 1 to reading)))
            val agentRelation = frame.nominalKarakas.single { it.dependent == 0 }.relation
            val objectRelation = frame.nominalKarakas.single { it.dependent == 1 }.relation
            assertEquals(agentRelation.kriyaId, objectRelation.kriyaId)
            assertNotEquals(frame.id, agentRelation.kriyaId)
            assertEquals(Karaka.KARTR, (agentRelation.resolution as FrameKarakaResolution.Resolved).karaka)
            assertEquals(listOf("1.4.54") + if (agentSup == "टा") listOf("2.3.18") else emptyList(),
                agentRelation.evidence.map { it.sutra })
            assertEquals(if (agentSup == "टा") Vibhakti.TRTIYA else null, agentRelation.resolvedVibhakti)
            assertEquals(listOf("1.4.46", "2.3.66"), objectRelation.evidence.map { it.sutra })
            assertEquals(Vibhakti.SASTHI, objectRelation.resolvedVibhakti)
            assertSame(agent, agentRelation.participant.pada)
            assertSame(locus, objectRelation.participant.pada)
        }
    }
    private data class Case(val governor: SubantaPada, val reading: Set<Karaka>, val extra: Set<SemanticRelation>, val expected: String)
    private fun noun(stem: String, sup: String) = SubantaPada(stem, MulaPratipadika(stem, stem), SupPratyaya(sup, sup))
    private fun krt(affix: String, root: String = "कृ", sup: String = "अम्") = SubantaPada("$root + $affix",
        KridantaPratipadika("$root + $affix", emptyList(), DhatuPrakriti(root, root), affix), SupPratyaya(sup, sup))
    private fun dhatu(root: String, gana: DhatuGana = DhatuGana.BHVADI, karmatva: Karmatva = Karmatva.SAKARMAKA) = Dhatu(
        id = "krt.frame.$root", krama = 1, upadesha = root, sourceSurface = root, artha = "test",
        arthaHindi = "test", arthaEnglish = "test", gana = gana, pada = PadaType.PARASMAIPADA, karmatva = karmatva)
    private val analyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(listOf(
        PratipadikaEntry("राम", setOf(Linga.PUMS)), PratipadikaEntry("देवदत्त", setOf(Linga.PUMS))),
        listOf(dhatu("दृश्"), dhatu("लिख्", DhatuGana.TUDADI), dhatu("दा", DhatuGana.JUHOTYADI),
            dhatu("कृ", DhatuGana.TANADI), dhatu("मन्", DhatuGana.DIVADI), dhatu("भुज्", DhatuGana.RUDHADI),
            dhatu("आस्", DhatuGana.ADADI, Karmatva.AKARMAKA), dhatu("जि"),
            dhatu("शी", DhatuGana.ADADI, Karmatva.AKARMAKA), dhatu("स्था", karmatva = Karmatva.AKARMAKA),
            dhatu("नी"), dhatu("अद्", DhatuGana.ADADI), dhatu("खाद्"), dhatu("वह्"), dhatu("भक्ष्"),
            dhatu("दिव्", DhatuGana.DIVADI),
            dhatu("क्री", DhatuGana.KRYADI),
            dhatu("गम्"), dhatu("दुह्", DhatuGana.ADADI), dhatu("भाष्"), dhatu("वच्"), dhatu("वद्"), dhatu("कथ्"),
            dhatu("विश्"), dhatu("वस्", karmatva = Karmatva.AKARMAKA)))))
    private fun verb(root: String = "दृश्", passive: Boolean = false) = TingantaPada(root, emptyList(),
        DhatuPrakriti(root, root), Lakara.LAT, TingPratyaya(if (passive) "त" else "तिप्", if (passive) "त" else "तिप्"),
        vikarana = if (passive) Vikarana.YAK else null)

    @Test fun `linked krt agent uses nominal action scope and does not control finite agreement`() {
        for (passive in listOf(false, true)) {
            val dependent = noun("देवदत्त", "आम्")
            val actor = noun("राम", if (passive) "टा" else "सुँ")
            val governor = krt("ल्युट्", "आस्", sup = if (passive) "सुँ" else "अम्")
            val v = verb(passive = passive)
            val facts = mapOf(0 to setOf(SemanticRelation.INDEPENDENT_AGENT), 1 to setOf(SemanticRelation.INDEPENDENT_AGENT))
            val frame = analyzer.analyze(AkhyataVakya("separate actions", listOf(dependent, actor, governor, v), v),
                semanticContext = VakyaSemanticContext(facts, krtGovernors = mapOf(0 to KrtGovernorReading(2, emptySet()))))
            val nominal = frame.nominalKarakas.single()
            assertEquals(0, nominal.dependent)
            assertEquals(2, nominal.governor)
            assertEquals(listOf("1.4.54", "2.3.65"), nominal.relation.evidence.map { it.sutra })
            assertEquals(Vibhakti.SASTHI, nominal.relation.resolvedVibhakti)
            assertEquals(2, frame.relations.size)
            assertTrue(frame.relations.none { it.participant.pada === dependent })
            assertTrue(frame.diagnostics.none { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH })
        }
    }

    @Test fun `kta special readings and krtya alternatives reach actual sentence frames`() {
        for ((governor, reading, extra, expected) in listOf(
            Case(krt("क्त", "मन्"), setOf(Karaka.KARMAN), setOf(SemanticRelation.PRESENT_PARTICIPLE_AGENT), "2.3.67"),
            Case(krt("क्त", "भुज्"), setOf(Karaka.ADHIKARANA), emptySet(), "2.3.68"),
            Case(krt("तव्यत्"), setOf(Karaka.KARMAN), emptySet(), "2.3.71")))
            for (ending in if (expected == "2.3.71") listOf("टा") else listOf("ङस्", "टा")) {
            val dependent = noun("देवदत्त", ending)
            val v = verb("लिख्")
            val frame = analyzer.analyze(AkhyataVakya("krt reading", listOf(dependent, governor, v), v),
                semanticContext = VakyaSemanticContext(mapOf(0 to (extra + SemanticRelation.INDEPENDENT_AGENT)),
                    krtGovernors = mapOf(0 to KrtGovernorReading(1, reading))))
            val relation = frame.nominalKarakas.single().relation
            assertEquals(listOf("1.4.54") + if (ending == "ङस्" || expected == "2.3.71") listOf(expected) else emptyList(),
                relation.evidence.map { it.sutra })
            if (ending == "टा" && expected != "2.3.71") assertNull(relation.resolvedVibhakti)
            assertTrue(relation.evidence.first().semanticEvidence.none {
                it.source == SemanticEvidenceSource.DHATU_VALENCY && it.detail.contains("लिख्")
            }, "Outer लिख् valency must not be copied to the nominal action's dependent")
            if (expected == "2.3.71") assertEquals(Vibhakti.SASTHI, relation.alternatives.single().vibhakti)
        }
    }

    @Test fun `paraji unbearable source uses the nominal governors own prefix`() {
        for (passive in listOf(false, true)) for (prefix in listOf("परा", "प्र", "unknown")) for (unbearable in listOf(false, true)) {
            val dependent = noun("अध्ययन", "ङसिँ")
            val governor = krt("ल्युट्", "जि").let {
                it.copy(pratipadika = (it.pratipadika as KridantaPratipadika).copy(upasargas = listOf(prefix)))
            }
            val v = verb(passive = passive)
            val frame = analyzer.analyze(AkhyataVakya("nominal paraji source", listOf(dependent, governor, v), v),
                semanticContext = VakyaSemanticContext(mapOf(0 to (setOf(SemanticRelation.SOURCE) +
                    if (unbearable) setOf(SemanticRelation.UNBEARABLE_OBJECT) else emptySet())),
                    krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet()))))
            val relation = frame.nominalKarakas.single().relation
            if (prefix == "परा" && unbearable) {
                assertEquals(listOf("1.4.26", "2.3.28"), relation.evidence.map { it.sutra })
                assertEquals(Vibhakti.PANCHAMI, relation.resolvedVibhakti)
            } else assertTrue(relation.evidence.none { it.sutra == "1.4.26" })
        }
    }

    @Test fun `prefixed nominal loci receive special karma designation before general locus`() {
        for (passive in listOf(false, true)) for ((root, prefixes, sutra) in listOf(
            Triple("आस्", listOf("अधि"), "1.4.46"),
            Triple("शी", listOf("अधि"), "1.4.46"),
            Triple("स्था", listOf("अधि"), "1.4.46"),
            Triple("विश्", listOf("अभि", "नि"), "1.4.47"),
            Triple("वस्", listOf("उप"), "1.4.48"),
            Triple("वस्", listOf("अनु"), "1.4.48"),
            Triple("वस्", listOf("अधि"), "1.4.48"),
            Triple("वस्", listOf("आङ्"), "1.4.48"))) for (hasLocus in listOf(false, true)) {
            val dependent = noun("ग्राम", "ङस्")
            val governor = krt("ल्युट्", root).let {
                it.copy(pratipadika = (it.pratipadika as KridantaPratipadika).copy(upasargas = prefixes))
            }
            val v = verb(passive = passive)
            val frame = analyzer.analyze(AkhyataVakya("nominal locus", listOf(dependent, governor, v), v),
                semanticContext = VakyaSemanticContext(mapOf(0 to if (hasLocus) setOf(SemanticRelation.LOCATION) else emptySet()),
                    krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet(), setOf(Karaka.KARTR, Karaka.KARMAN)))))
            val relation = frame.nominalKarakas.single().relation
            if (hasLocus) {
                assertEquals(listOf(sutra, "2.3.66"), relation.evidence.map { it.sutra })
                assertEquals(Vibhakti.SASTHI, relation.resolvedVibhakti)
            } else assertTrue(relation.evidence.none { it.sutra in setOf("1.4.46", "1.4.47", "1.4.48") })
        }
    }

    @Test fun `nominal locus special rules do not borrow prefixes from the outer action`() {
        for ((root, prefixes) in listOf(
            "शी" to emptyList(), "स्था" to listOf("प्र"), "आस्" to listOf("unknown"),
            "विश्" to listOf("नि", "अभि"), "वस्" to listOf("प्र"))) {
            val dependent = noun("ग्राम", "ङस्")
            val governor = krt("ल्युट्", root).let {
                it.copy(pratipadika = (it.pratipadika as KridantaPratipadika).copy(upasargas = prefixes))
            }
            val outer = verb("आस्").copy(upasargas = listOf("अधि"))
            val frame = analyzer.analyze(AkhyataVakya("separate prefix owners", listOf(dependent, governor, outer), outer),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.LOCATION)),
                    krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet()))))
            val relation = frame.nominalKarakas.single().relation
            assertTrue(relation.evidence.none { it.sutra in setOf("1.4.46", "1.4.47", "1.4.48") }, "$root $prefixes: ${relation.evidence}")
        }
    }

    @Test fun `fasting sense of nominal upavas retains locus rather than special karman`() {
        for (passive in listOf(false, true)) {
            val dependent = noun("ग्राम", "ङि")
            val governor = krt("ल्युट्", "वस्").let {
                it.copy(pratipadika = (it.pratipadika as KridantaPratipadika).copy(upasargas = listOf("उप")))
            }
            val outer = verb(passive = passive)
            val frame = analyzer.analyze(AkhyataVakya("nominal fasting locus", listOf(dependent, governor, outer), outer),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.LOCATION, SemanticRelation.FASTING_SENSE)),
                    krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet()))))
            val relation = frame.nominalKarakas.single().relation
            assertEquals(Karaka.ADHIKARANA, (relation.resolution as FrameKarakaResolution.Resolved).karaka)
            assertEquals(listOf("1.4.45", "2.3.36"), relation.evidence.map { it.sutra })
            assertSame(dependent, relation.participant.pada)
        }
    }

    @Test fun `invalid targets cycles and conflicting readings are rejected`() {
        val v = verb()
        val dependent = noun("देवदत्त", "ङस्")
        val clause = AkhyataVakya("wrong governor", listOf(dependent, noun("राम", "सुँ"), v), v)
        assertFailsWith<IllegalArgumentException> { analyzer.analyze(clause,
            semanticContext = VakyaSemanticContext(krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet())))) }
        assertFailsWith<IllegalArgumentException> { VakyaSemanticContext(krtGovernors = mapOf(
            0 to KrtGovernorReading(1, emptySet()), 1 to KrtGovernorReading(0, emptySet()))) }
        assertFailsWith<IllegalArgumentException> { VakyaSemanticContext(krtGovernors = mapOf(
            0 to KrtGovernorReading(2, emptySet()), 1 to KrtGovernorReading(2, setOf(Karaka.KARTR)))) }
    }
}
