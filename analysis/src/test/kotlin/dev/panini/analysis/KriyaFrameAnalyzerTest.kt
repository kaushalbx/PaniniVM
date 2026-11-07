package dev.panini.analysis

import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.core.Vibhakti
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Karmatva
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.DhatuPrakriti
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import dev.panini.vyakaranam.ast.TingPratyaya
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.lexicon.InMemoryVyakaranamLexicon
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class KriyaFrameAnalyzerTest {
    @Test
    fun `sentence frame retains vocative designation without verbal agent assignment`() {
        val local = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(listOf(
            dev.panini.vyakaranam.lexicon.PratipadikaEntry("राम", setOf(dev.panini.core.Linga.PUMS),
                semanticRelations = setOf(SemanticRelation.DIRECT_ADDRESS))), listOf(dhatu))))
        val verb = akhyata().tinganta
        val clause = akhyata().copy(padas = listOf(subanta("राम + सुँ", "राम", "सुँ"), verb), tinganta = verb)
        val relation = local.analyze(clause).relations.single()
        assertEquals(FrameKarakaResolution.Resolved(Karaka.SAMBODHANA), relation.resolution)
        assertEquals(listOf("2.3.47"), relation.evidence.map { it.sutra })
        assertEquals(listOf(NominalDesignation.AMANTRITA, NominalDesignation.SAMBUDDHI),
            relation.nominalDesignations.map { it.designation })
    }

    @Test
    fun `passive causative sentence carries supplied causee and causer premises through sutra chains`() {
        val lexical = InMemoryVyakaranamLexicon(listOf(
            dev.panini.vyakaranam.lexicon.PratipadikaEntry("बाल", setOf(dev.panini.core.Linga.PUMS),
                semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT)),
            dev.panini.vyakaranam.lexicon.PratipadikaEntry("राम", setOf(dev.panini.core.Linga.PUMS),
                semanticRelations = setOf(SemanticRelation.PROMPTER_CAUSE)),
        ), listOf(Dhatu(id = "gam.passive.test", krama = 1, upadesha = "गम्", sourceSurface = "गम्",
            artha = "गतौ", arthaHindi = "जाना", arthaEnglish = "go", gana = DhatuGana.BHVADI,
            pada = PadaType.PARASMAIPADA, karmatva = Karmatva.AKARMAKA)))
        val causee = subanta("बाल + सुँ", "बाल", "सुँ")
        val causer = subanta("राम + टा", "राम", "टा")
        val verb = TingantaPada("गम् + णिच् + यक् + लट् + त", emptyList(),
            DhatuPrakriti("गम्", "गम्", listOf("णिच्")), Lakara.LAT, TingPratyaya("त", "त"),
            vikarana = dev.panini.vyakaranam.ast.Vikarana.YAK)
        val frame = VakyaAnalyzer(PadaAnalyzer(lexical)).analyze(AkhyataVakya("passive causative",
            listOf(causee, causer, verb), verb))
        assertEquals(dev.panini.core.Prayoga.KARMANI, frame.prayoga)
        assertEquals(listOf("1.4.52", "2.3.46"), frame.relations[0].evidence.map { it.sutra })
        assertEquals(Vibhakti.PRATHAMA, frame.relations[0].resolvedVibhakti)
        assertEquals(listOf("1.4.55", "2.3.18"), frame.relations[1].evidence.map { it.sutra })
        assertEquals(Vibhakti.TRTIYA, frame.relations[1].resolvedVibhakti)
        assertEquals(setOf(KarakaDesignation.HETU), frame.relations[1].additionalDesignations)
        assertEquals(emptySet(), frame.relations[0].additionalDesignations)
    }

    @Test
    fun `adverb qualification follows its clause across word order and intervening participants`() {
        val slowly = AvyayaPada("शनैः", "शनैः")
        val whenQuestion = AvyayaPada("कदा", "कदा")
        val agent = subanta("राम + सुँ", "राम", "सुँ")
        val base = akhyata()
        listOf(
            listOf(slowly, agent, whenQuestion, base.tinganta),
            listOf(base.tinganta, whenQuestion, agent, slowly),
        ).forEach { padas ->
            val frame = analyzer.analyze(base.copy(padas = padas), KriyaId("adverb-clause"))
            val qualifications = frame.qualifications.associateBy { it.value }
            assertEquals(setOf("शनैः", "कदा"), qualifications.keys)
            assertEquals(KriyaQualificationKind.MANNER, qualifications.getValue("शनैः").kind)
            assertEquals(AdverbialFunction.QUESTION, qualifications.getValue("कदा").semantics?.function)
            qualifications.values.forEach {
                assertEquals(frame.id, it.kriyaId)
                assertEquals(padas.indexOf(it.source.pada), it.sourceIndex)
            }
        }
    }

    @Test
    fun `sentence frame exposes nonadjacent nominal attachment through standard lexicon`() {
        val localAnalyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(
            listOfNotNull(
                dev.panini.vyakaranam.lexicon.StandardPratipadikaLexicon.findPratipadika("नील"),
                dev.panini.vyakaranam.lexicon.StandardPratipadikaLexicon.findPratipadika("उत्पल"),
            ), listOf(Dhatu(
                id = "nominal.test", krama = 1, upadesha = "फ्रेम्", sourceSurface = "फ्रेम्",
                artha = "परीक्षणे", arthaHindi = "परीक्षण", arthaEnglish = "test",
                gana = DhatuGana.BHVADI, pada = PadaType.PARASMAIPADA, karmatva = Karmatva.SAKARMAKA,
            )),
        )))
        val modifier = subanta("नील + अम्", "नील", "अम्")
        val head = subanta("उत्पल + अम्", "उत्पल", "अम्")
        val verb = akhyata().tinganta
        val clause = akhyata().copy(padas = listOf(modifier, verb, head), tinganta = verb)
        val frame = localAnalyzer.analyze(clause)
        val nominal = requireNotNull(frame.nominalRelations)
        assertEquals(listOf(0 to 1), nominal.candidates.map { it.modifier to it.head })
        assertEquals(setOf(0), nominal.independentReadings)
        assertEquals(setOf(NominalMeaning.PROPERTY), nominal.candidates.single().meanings)
    }

    @Test
    fun `yak selects passive voice on causatives of transitive and intransitive roots`() {
        for (karmatva in listOf(Karmatva.SAKARMAKA, Karmatva.AKARMAKA)) {
            val localAnalyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(
                emptyList(), listOf(Dhatu(
                    id = "test.causative", krama = 1, upadesha = "फ्रेम्", sourceSurface = "फ्रेम्",
                    artha = "परीक्षणे", arthaHindi = "परीक्षण", arthaEnglish = "test",
                    gana = DhatuGana.BHVADI, pada = PadaType.PARASMAIPADA, karmatva = karmatva,
                )),
            )))
            val agent = subanta("राम + टा", "राम", "टा")
            val objectPada = subanta("बाल + औ", "बाल", "औ")
            val original = akhyata(agent, objectPada)
            val verb = original.tinganta.copy(
                dhatu = original.tinganta.dhatu.copy(sanadiPratyayas = listOf("णिच्")),
                vikarana = dev.panini.vyakaranam.ast.Vikarana.YAK,
                ting = TingPratyaya("आताम्", "आताम्"),
            )
            val frame = localAnalyzer.analyze(original.copy(padas = listOf(agent, objectPada, verb), tinganta = verb),
                semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.INDEPENDENT_AGENT),
                    1 to setOf(SemanticRelation.DESIRED_OBJECT))))
            assertEquals(dev.panini.core.Prayoga.KARMANI, frame.prayoga)
            assertEquals(listOf(Karaka.KARTR, Karaka.KARMAN), frame.relations.map {
                assertIs<FrameKarakaResolution.Resolved>(it.resolution).karaka
            })
            assertEquals(
                listOf(listOf("1.4.54", "2.3.18"), listOf("1.4.49", "2.3.46")),
                frame.relations.map { relation -> relation.evidence.map { it.sutra } },
            )
            frame.relations.forEach { relation ->
                assertEquals(true, relation.evidence.first().semanticEvidence.any {
                    it.source == SemanticEvidenceSource.VOICE_CONSTRUCTION
                })
            }
            assertEquals(0, frame.diagnostics.count { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH })
        }
    }

    @Test
    fun `bhave requires impersonal singular third person independent of agents`() {
        val bhaveAnalyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(
            emptyList(), listOf(Dhatu(
                id = "test.bhave", krama = 1, upadesha = "फ्रेम्", sourceSurface = "फ्रेम्",
                artha = "परीक्षणे", arthaHindi = "होना", arthaEnglish = "to be",
                gana = DhatuGana.BHVADI, pada = PadaType.PARASMAIPADA,
                karmatva = Karmatva.AKARMAKA,
            )),
        )))
        fun mismatches(ending: String, vararg participants: dev.panini.vyakaranam.ast.Pada): Int {
            val original = akhyata(*participants)
            val verb = original.tinganta.copy(
                ting = TingPratyaya(ending, ending),
                vikarana = dev.panini.vyakaranam.ast.Vikarana.YAK,
            )
            val frame = bhaveAnalyzer.analyze(original.copy(
                padas = participants.toList() + verb, tinganta = verb,
            ))
            assertEquals(dev.panini.core.Prayoga.BHAVE, frame.prayoga)
            return frame.diagnostics.count { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH }
        }
        val dualAgent = subanta("राम + भ्याम्", "राम", "भ्याम्")
        val pluralAgent = subanta("राम + भिस्", "राम", "भिस्")
        assertEquals(0, mismatches("त"))
        assertEquals(0, mismatches("त", dualAgent))
        assertEquals(0, mismatches("त", pluralAgent))
        assertEquals(1, mismatches("आताम्", dualAgent))
        assertEquals(1, mismatches("झ", pluralAgent))
        assertEquals(1, mismatches("थास्"))
        assertEquals(1, mismatches("इट्"))
    }

    @Test
    fun `yak requires atmanepada even for a parasmaipada lexical root`() {
        val original = akhyata()
        val verb = original.tinganta.copy(vikarana = dev.panini.vyakaranam.ast.Vikarana.YAK)
        assertFailsWith<IllegalArgumentException> {
            analyzer.analyze(original.copy(padas = listOf(verb), tinganta = verb))
        }
    }

    @Test
    fun `passive agreement follows nominative object not instrumental agent`() {
        val agent = subanta("राम + टा", "राम", "टा")
        val dualAgent = subanta("राम + भ्याम्", "राम", "भ्याम्")
        val singularObject = subanta("फल + सुँ", "फल", "सुँ")
        val dualObject = subanta("फल + औ", "फल", "औ")
        fun mismatches(ending: String, vararg participants: dev.panini.vyakaranam.ast.Pada): Int {
            val original = akhyata(*participants)
            val verb = original.tinganta.copy(
                ting = TingPratyaya(ending, ending),
                vikarana = dev.panini.vyakaranam.ast.Vikarana.YAK,
            )
            val clause = original.copy(padas = participants.toList() + verb, tinganta = verb)
            val initial = analyzer.analyze(clause)
            // This agreement fixture selects the nominatives as desired patients.
            val patients = initial.nominalRelations!!.nominalPadas.mapIndexedNotNull { index, nominal ->
                index.takeIf { nominal.supCandidates.all { it.vibhakti == Vibhakti.PRATHAMA } }
            }.associateWith { setOf(SemanticRelation.DESIRED_OBJECT) }
            val frame = analyzer.analyze(clause, semanticContext = VakyaSemanticContext(patients))
            assertEquals(dev.panini.core.Prayoga.KARMANI, frame.prayoga)
            return frame.diagnostics.count { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH }
        }
        assertEquals(0, mismatches("आताम्", agent, dualObject))
        assertEquals(1, mismatches("त", agent, dualObject))
        assertEquals(0, mismatches("त", dualAgent, singularObject))
        assertEquals(1, mismatches("आताम्", dualAgent, singularObject))
        assertEquals(0, mismatches("आताम्", dualObject))
        assertEquals(0, mismatches("आताम्", agent,
            subanta("पुष्प + अम्", "पुष्प", "अम्"), dualObject))
        assertEquals(0, mismatches("त", dualAgent)) // No expressed object: do not use the agent.
        val group = dev.panini.vyakaranam.ast.SamuccitaSubanta("coordination", listOf(
            singularObject, subanta("पुष्प + सुँ", "पुष्प", "सुँ"),
        ))
        assertEquals(0, mismatches("आताम्", agent, group))
        assertEquals(1, mismatches("त", agent, group))
    }

    @Test
    fun `explicit coordinated agents agree by combined grammatical number`() {
        val rama = subanta("राम + सुँ", "राम", "सुँ")
        val shyama = subanta("श्याम + सुँ", "श्याम", "सुँ")
        val hari = subanta("हरि + सुँ", "हरि", "सुँ")
        fun mismatches(members: List<SubantaPada>, ending: String): Int {
            val group = dev.panini.vyakaranam.ast.SamuccitaSubanta("coordination", members)
            val original = akhyata(group)
            val verb = original.tinganta.copy(ting = TingPratyaya(ending, ending))
            return analyzeWithSelectedAgents(original.copy(padas = listOf(group, verb), tinganta = verb))
                .diagnostics.count { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH }
        }
        assertEquals(0, mismatches(listOf(rama, shyama), "तस्"))
        assertEquals(1, mismatches(listOf(rama, shyama), "तिप्"))
        assertEquals(0, mismatches(listOf(rama, shyama, hari), "झि"))
        assertEquals(1, mismatches(listOf(rama, shyama, hari), "तस्"))
        assertEquals(0, mismatches(listOf(subanta("बाल + औ", "बाल", "औ"), rama), "झि"))
        assertEquals(0, mismatches(listOf(subanta("बाल + जस्", "बाल", "जस्"), rama), "झि"))
        // A coordinated recipient must not increase the agent's grammatical number.
        assertEquals(0, mismatches(listOf(rama, subanta("श्याम + ङे", "श्याम", "ङे")), "तिप्"))
        assertEquals(0, analyzeWithSelectedAgents(akhyata(rama, shyama)).diagnostics.count {
            it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH
        })
    }

    @Test
    fun `every coordinated agent retains its own exact sutra evidence`() {
        val members = listOf(
            subanta("राम + सुँ", "राम", "सुँ"),
            subanta("श्याम + सुँ", "श्याम", "सुँ"),
        )
        val group = dev.panini.vyakaranam.ast.SamuccitaSubanta("रामः च श्यामः च", members)
        val frame = analyzeWithSelectedAgents(akhyata(group))

        assertEquals(2, frame.relations.size)
        frame.relations.forEach { relation ->
            assertEquals(Karaka.KARTR,
                assertIs<FrameKarakaResolution.Resolved>(relation.resolution).karaka)
            assertEquals(listOf("1.4.54", "2.3.46"), relation.evidence.map { it.sutra })
        }
    }

    @Test
    fun `voice follows parsed morphology rather than display source`() {
        val ordinary = akhyata()
        val misleading = ordinary.tinganta.copy(sourceText = "इ यि णिच् यक् चिण्")
        assertEquals(dev.panini.core.Prayoga.KARTARI, analyzer.analyze(
            ordinary.copy(padas = listOf(misleading), tinganta = misleading),
        ).prayoga)
        val causative = ordinary.tinganta.copy(sourceText = "presentation only",
            dhatu = ordinary.tinganta.dhatu.copy(sanadiPratyayas = listOf("णिच्")))
        assertEquals(dev.panini.core.Prayoga.CAUSATIVE, analyzer.analyze(
            ordinary.copy(padas = listOf(causative), tinganta = causative),
        ).prayoga)
        val passive = ordinary.tinganta.copy(sourceText = "presentation only",
            ting = TingPratyaya("त", "त"),
            vikarana = dev.panini.vyakaranam.ast.Vikarana.YAK)
        assertEquals(dev.panini.core.Prayoga.KARMANI, analyzer.analyze(
            ordinary.copy(padas = listOf(passive), tinganta = passive),
        ).prayoga)
    }

    @Test
    fun `os needs an established locus reading beyond root valency`() {
        val stha = Dhatu(
            id = "test.stha", krama = 1, upadesha = "स्था", sourceSurface = "स्था",
            artha = "गतिनिवृत्तौ", arthaHindi = "ठहरना", arthaEnglish = "stand",
            gana = DhatuGana.BHVADI, pada = PadaType.PARASMAIPADA,
            karmatva = Karmatva.AKARMAKA,
        )
        val localAnalyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(emptyList(), listOf(stha))))
        fun analyze(vararg participants: SubantaPada, context: VakyaSemanticContext = VakyaSemanticContext()): KriyaFrame {
            val verb = TingantaPada(
                sourceText = "तिष्ठति", upasargas = emptyList(),
                dhatu = DhatuPrakriti("स्था", "स्था"), lakara = Lakara.LAT,
                ting = TingPratyaya("तिप्", "तिप्"),
            )
            return localAnalyzer.analyze(AkhyataVakya(
                sourceText = participants.joinToString(" ") { it.sourceText } + " तिष्ठति",
                padas = participants.toList() + verb, tinganta = verb,
            ), semanticContext = context)
        }

        val pada = subanta("गृह + ओस्", "गृह", "ओस्")
        val unresolved = analyze(pada)
        assertTrue(unresolved.relations.single().evidence.none { it.sutra == "1.4.45" })
        val frame = analyze(pada, context = VakyaSemanticContext(
            participantRelations = mapOf(0 to setOf(SemanticRelation.LOCATION))))
        assertEquals(Karaka.ADHIKARANA,
            assertIs<FrameKarakaResolution.Resolved>(frame.relations.single().resolution).karaka)
        assertEquals(emptyList(), frame.diagnostics.filter { it.code == FrameDiagnosticCode.AMBIGUOUS_KARAKA })
    }

    @Test
    fun `os remains ambiguous when location valency cannot select one participant`() {
        val stha = Dhatu(
            id = "test.stha.ambiguous", krama = 1, upadesha = "स्था", sourceSurface = "स्था",
            artha = "गतिनिवृत्तौ", arthaHindi = "ठहरना", arthaEnglish = "stand",
            gana = DhatuGana.BHVADI, pada = PadaType.PARASMAIPADA,
            karmatva = Karmatva.AKARMAKA,
        )
        val localAnalyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(emptyList(), listOf(stha))))
        val verb = TingantaPada("तिष्ठति", emptyList(), DhatuPrakriti("स्था", "स्था"), Lakara.LAT,
            TingPratyaya("तिप्", "तिप्"))
        val participants = listOf(
            subanta("गृह + ओस्", "गृह", "ओस्"),
            subanta("वन + ओस्", "वन", "ओस्"),
        )
        val frame = localAnalyzer.analyze(AkhyataVakya("गृहयोः वनयोः तिष्ठति", participants + verb, verb))

        assertEquals(2, frame.relations.count { it.resolution is FrameKarakaResolution.Ambiguous })
        val messages = frame.diagnostics.filter { it.code == FrameDiagnosticCode.AMBIGUOUS_KARAKA }.map { it.message }
        assertEquals(2, messages.size)
        messages.forEach { message ->
            kotlin.test.assertTrue(message.contains("ओस् द्विवचनम् षष्ठी/सप्तमी-साधारणः सुप्"), message)
            kotlin.test.assertTrue(message.contains("सम्बन्ध अथवा अधिकरण"), message)
        }
    }

    @Test
    fun `coexistent karaka names and case alternatives survive into frame and diagnostic`() {
        val div = Dhatu(
            id = "test.div", krama = 1, upadesha = "दिव", sourceSurface = "दिव",
            artha = "क्रीडायाम्", arthaHindi = "खेलना", arthaEnglish = "play",
            gana = DhatuGana.DIVADI, pada = PadaType.PARASMAIPADA,
            karmatva = Karmatva.SAKARMAKA,
            semanticRelations = setOf(SemanticRelation.INSTRUMENT),
        )
        val localAnalyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(emptyList(), listOf(div))))
        val participant = subanta("अक्ष + अम्", "अक्ष", "अम्")
        val verb = TingantaPada("दीव्यति", emptyList(), DhatuPrakriti("दिव", "दिव"), Lakara.LAT,
            TingPratyaya("तिप्", "तिप्"))
        val frame = localAnalyzer.analyze(AkhyataVakya("अक्षं दीव्यति", listOf(participant, verb), verb),
            semanticContext = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.INSTRUMENT))))

        assertEquals(Karaka.KARMAN,
            assertIs<FrameKarakaResolution.Resolved>(frame.relations.single().resolution).karaka)
        assertEquals(Karaka.KARANA, frame.relations.single().alternatives.single().karaka)
        assertEquals(Vibhakti.TRTIYA, frame.relations.single().alternatives.single().vibhakti)
        assertEquals(true, frame.relations.single().evidence.first().semanticEvidence.any {
            it.source == SemanticEvidenceSource.DHATU_VALENCY && it.relation == SemanticRelation.INSTRUMENT
        })
        assertEquals(setOf(Karaka.KARMAN, Karaka.KARANA), frame.relations.single().coexistingKarakas)
        assertEquals(null, frame.relations.single().primaryOptionalDecision)
        assertEquals(0, frame.diagnostics.count { it.code == FrameDiagnosticCode.OPTIONAL_KARAKA })
        assertEquals(1, frame.diagnostics.count { it.code == FrameDiagnosticCode.OPTIONAL_VIBHAKTI })
    }

    @Test
    fun `unannotated dvikarmaka accusatives retain uncertainty without canonical proof`() {
        val duh = Dhatu(
            id = "test.duh", krama = 1, upadesha = "दुह्", sourceSurface = "दुह्",
            artha = "प्रपूरणे", arthaHindi = "दुहना", arthaEnglish = "milk",
            gana = DhatuGana.ADADI, pada = PadaType.PARASMAIPADA,
            karmatva = Karmatva.SAKARMAKA,
        )
        val localAnalyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(emptyList(), listOf(duh))))
        val cow = subanta("गो + अम्", "गो", "अम्")
        val milk = subanta("पयस् + अम्", "पयस्", "अम्")
        val verb = TingantaPada("दोग्धि", emptyList(), DhatuPrakriti("दुह्", "दुह्"), Lakara.LAT,
            TingPratyaya("तिप्", "तिप्"))
        val frame = localAnalyzer.analyze(AkhyataVakya("गां पयो दोग्धि", listOf(cow, milk, verb), verb))

        assertEquals(2, frame.relations.size)
        frame.relations.forEach { relation ->
            val unresolved = assertIs<FrameKarakaResolution.Unassigned>(relation.resolution)
            assertEquals(setOf(Karaka.KARMAN), unresolved.candidates)
            assertTrue(unresolved.reason.contains("primary/secondary"))
            assertEquals(emptyList(), relation.evidence)
            assertEquals(emptyList(), relation.alternatives)
        }
        assertEquals(2, frame.diagnostics.count {
            it.code == FrameDiagnosticCode.UNASSIGNED_PARTICIPANT
        })
    }

    private val dhatu = Dhatu(
        id = "test.1",
        krama = 1,
        upadesha = "फ्रेम्",
        sourceSurface = "फ्रेम्",
        artha = "परीक्षणे",
        arthaHindi = "देना",
        arthaEnglish = "to give",
        gana = DhatuGana.JUHOTYADI,
        pada = PadaType.PARASMAIPADA,
        karmatva = Karmatva.SAKARMAKA,
    )
    private val analyzer = VakyaAnalyzer(
        PadaAnalyzer(InMemoryVyakaranamLexicon(emptyList(), listOf(dhatu))),
    )

    @Test fun `hetu cause attachment survives reversal intervening adverb and intervening verb`() {
        val food = subanta("अन्न + ङस्", "अन्न", "ङस्")
        val hetu = subanta("हेतु + ङस्", "हेतु", "ङस्")
        val now = AvyayaPada("अधुना", "अधुना")
        val verb = akhyata().tinganta
        val orders = listOf(
            listOf<dev.panini.vyakaranam.ast.Pada>(food, now, hetu, verb),
            listOf(hetu, now, food, verb),
            listOf(food, verb, now, hetu),
        )
        for (order in orders) {
            val index = order.filterIsInstance<SubantaPada>().indexOfFirst { it === food }
            val frame = analyzer.analyze(AkhyataVakya("हेतु attachment", order, verb),
                semanticContext = VakyaSemanticContext(participantRelations =
                    mapOf(index to setOf(SemanticRelation.CAUSE_HETU))))
            val link = kotlin.test.assertNotNull(frame.nominalCaseLinks).established.single()
            assertEquals(index, link.dependent)
            assertEquals("2.3.26", link.sutra)
            assertEquals(2, frame.relations.size)
            frame.relations.forEach {
                assertEquals(listOf("2.3.26"), it.evidence.map { evidence -> evidence.sutra })
                assertEquals(Vibhakti.SASTHI, it.resolvedVibhakti)
                assertEquals(FrameKarakaResolution.Resolved(Karaka.ANIRDHARITA), it.resolution)
            }
        }
    }

    @Test fun `canonical coordination shares recipient valency without resolving separate competitors`() {
        val giving = Dhatu(id = "giving", krama = 1, upadesha = "दा", sourceSurface = "दा",
            artha = "दाने", arthaHindi = "देना", arthaEnglish = "give", gana = DhatuGana.JUHOTYADI,
            pada = PadaType.PARASMAIPADA, karmatva = Karmatva.SAKARMAKA)
        val local = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(emptyList(), listOf(giving))))
        val obj = subanta("फल + अम्", "फल", "अम्")
        val rama = subanta("राम + भ्याम्", "राम", "भ्याम्")
        val syama = subanta("श्याम + भ्याम्", "श्याम", "भ्याम्")
        val originalVerb = akhyata().tinganta
        val verb = originalVerb.copy(dhatu = originalVerb.dhatu.copy(mulaDhatu = "दा"))
        val group = dev.panini.vyakaranam.ast.SamuccitaSubanta("राम श्याम च", listOf(rama, syama))
        val objectMeaning = VakyaSemanticContext(mapOf(0 to setOf(SemanticRelation.DESIRED_OBJECT)))
        val frame = local.analyze(AkhyataVakya("", listOf(obj, group, verb), verb), semanticContext = objectMeaning)
        frame.relations.drop(1).forEach {
            assertEquals(FrameKarakaResolution.Resolved(Karaka.SAMPRADANA), it.resolution)
            assertEquals(listOf("1.4.32", "2.3.13"), it.evidence.map { evidence -> evidence.sutra })
        }
        val separate = local.analyze(AkhyataVakya("", listOf(obj, rama, syama, verb), verb), semanticContext = objectMeaning)
        separate.relations.drop(1).forEach { assertIs<FrameKarakaResolution.Ambiguous>(it.resolution) }
        val writtenCha = local.analyze(AkhyataVakya("", listOf(obj, rama, syama, AvyayaPada("च", "च"), verb), verb), semanticContext = objectMeaning)
        writtenCha.relations.drop(1).forEach {
            assertEquals(FrameKarakaResolution.Resolved(Karaka.SAMPRADANA), it.resolution)
            assertEquals(listOf("1.4.32", "2.3.13"), it.evidence.map { evidence -> evidence.sutra })
        }
        val separated = local.analyze(AkhyataVakya("", listOf(obj, rama, AvyayaPada("अधुना", "अधुना"),
            syama, AvyayaPada("च", "च"), verb), verb), semanticContext = objectMeaning)
        separated.relations.drop(1).forEach { assertIs<FrameKarakaResolution.Ambiguous>(it.resolution) }
    }

    @Test
    fun `one kriya frame preserves several participants of the same karaka`() {
        val rama = subanta("राम + सुँ", "राम", "सुँ")
        val phala = subanta("फल + अम्", "फल", "अम्")
        val pushpa = subanta("पुष्प + अम्", "पुष्प", "अम्")
        val vakya = akhyata(rama, phala, pushpa)

        val frame = requireNotNull(analyzer.analyze(vakya, semanticContext = VakyaSemanticContext(
            mapOf(0 to setOf(SemanticRelation.INDEPENDENT_AGENT), 1 to setOf(SemanticRelation.DESIRED_OBJECT),
                2 to setOf(SemanticRelation.DESIRED_OBJECT)))))

        assertEquals(3, frame.relations.size)
        assertEquals(
            listOf(Karaka.KARTR, Karaka.KARMAN, Karaka.KARMAN),
            frame.relations.map {
                assertIs<FrameKarakaResolution.Resolved>(it.resolution).karaka
            },
        )
    }

    @Test
    fun `avyaya qualifies the kriya instead of becoming a karaka`() {
        val rama = subanta("राम + सुँ", "राम", "सुँ")
        val quickly = AvyayaPada("शीघ्रम्", "शीघ्रम्")
        val vakya = akhyata(rama, quickly)

        val frame = requireNotNull(analyzer.analyze(vakya))

        assertEquals(1, frame.relations.size)
        assertEquals(1, frame.qualifications.size)
        assertEquals(KriyaQualificationKind.MANNER, frame.qualifications.single().kind)
        assertEquals("शीघ्रम्", frame.qualifications.single().value)
    }

    @Test
    fun `syncretic participant remains ambiguous in the frame`() {
        val participant = subanta("देव + भ्याम्", "देव", "भ्याम्")
        val vakya = akhyata(participant)

        val frame = requireNotNull(analyzer.analyze(vakya))
        val ambiguous = assertIs<FrameKarakaResolution.Ambiguous>(
            frame.relations.single().resolution,
        )

        assertEquals(
            setOf(Karaka.KARANA, Karaka.SAMPRADANA, Karaka.APADANA),
            ambiguous.candidates,
        )
    }

    @Test
    fun `ukti analysis gives each kriya a stable frame and links a condition`() {
        val condition = akhyata(subanta("राम + सुँ", "राम", "सुँ"))
        val consequent = akhyata(subanta("फल + अम्", "फल", "अम्"))
        val analysis = UktiAnalyzer(analyzer).analyze(
            Ukti(
                sourceText = "यदि ... तर्हि ... + सुँ असँ + लट् + तिप्",
                body = Conditional(
                    sourceText = "यदि ... तर्हि ... + सुँ असँ + लट् + तिप्",
                    condition = Invocation(condition),
                    consequent = Invocation(consequent),
                ),
            ),
        )

        assertEquals(listOf("kriya-1", "kriya-2"), analysis.frames.map { it.id.value })
        assertEquals(
            KriyaLink.Condition(KriyaId("kriya-1"), KriyaId("kriya-2")),
            analysis.links.single(),
        )
    }

    @Test
    fun `canonical only analysis leaves jugupsa commentary role unassigned`() {
        val participant = subanta("पाप + ङसिँ", "पाप", "ङसिँ")
        val base = akhyata(participant)
        val verb = base.tinganta.copy(dhatu = DhatuPrakriti("जुगुप्स्", "जुगुप्स्"),
            ting = TingPratyaya("त", "त"))
        val vakya = base.copy(padas = listOf(participant, verb), tinganta = verb)
        val localAnalyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(
            emptyList(), listOf(Dhatu(
                id = "jugupsa.test", krama = 1, upadesha = "जुगुप्स्", sourceSurface = "जुगुप्स्",
                artha = "जुगुप्सायाम्", arthaHindi = "घृणा", arthaEnglish = "aversion",
                gana = DhatuGana.BHVADI, pada = PadaType.ATMANEPADA, karmatva = Karmatva.AKARMAKA,
            )),
        )))

        val frame = localAnalyzer.analyze(vakya)
        assertEquals(1, frame.relations.size)
        assertIs<FrameKarakaResolution.Unassigned>(
            frame.relations.single().resolution,
        )
        assertEquals(emptyList(), frame.relations.single().evidence)
    }

    @Test
    fun `purvakalika and shared participant links are inferred for ktva and lyap clauses`() {
        val ktvaTinganta = TingantaPada(
            sourceText = "फ्रेम् + क्त्वा",
            upasargas = emptyList(),
            dhatu = DhatuPrakriti("फ्रेम्", "फ्रेम्"),
            lakara = Lakara.LAT,
            ting = TingPratyaya("तिप्", "तिप्"),
            priorAction = dev.panini.vyakaranam.ast.AvyayaKridantaDerivation(
                emptyList(), DhatuPrakriti("फ्रेम्", "फ्रेम्"), "क्त्वा"),
        )
        val ktvaVakya = AkhyataVakya(
            sourceText = "गत्वा",
            padas = listOf(ktvaTinganta),
            tinganta = ktvaTinganta,
        )
        val mainVakya = akhyata(subanta("राम + सुँ", "राम", "सुँ"))

        val ukti = Ukti(
            sourceText = "गत्वा रामः पश्चाद् आगच्छति",
            body = Sequence(
                sourceText = "गत्वा रामः पश्चाद् आगच्छति",
                statements = listOf(Invocation(ktvaVakya), Invocation(mainVakya)),
            ),
        )

        val analysis = UktiAnalyzer { vakya, id -> analyzeWithSelectedAgents(vakya, id) }.analyze(ukti)
        val purvakalika = analysis.links.filterIsInstance<KriyaLink.Purvakalika>()
        val sharedAgent = analysis.links.filterIsInstance<KriyaLink.SharedParticipant>()

        assertEquals(1, purvakalika.size)
        assertEquals(KriyaId("kriya-1"), purvakalika.single().source)
        assertEquals(KriyaId("kriya-2"), purvakalika.single().target)
        assertEquals("राम + सुँ", sharedAgent.single().participantSource)
        assertEquals(Karaka.KARTR, sharedAgent.single().karaka)
        assertEquals(setOf(KriyaId("kriya-2")), sharedAgent.single().explicitIn)
        assertEquals(setOf(KriyaId("kriya-1")), sharedAgent.single().omittedIn)
    }

    @Test
    fun `purpose links require typed infinitive rather than text resemblance`() {
        for (typed in listOf(false, true)) {
            val infinitive = AvyayaPada("तुम्", "तुम्", if (typed)
                dev.panini.vyakaranam.ast.AvyayaKridantaDerivation(
                    emptyList(), DhatuPrakriti("फ्रेम्", "फ्रेम्"), "तुमुँन्") else null)
            val source = akhyata(infinitive).copy(sourceText = "तुम्")
            val target = akhyata()
            val analysis = UktiAnalyzer(analyzer).analyze(Ukti("परीक्षण", body = Sequence(
                "परीक्षण", listOf(Invocation(source), Invocation(target)))))
            assertEquals(if (typed) 1 else 0, analysis.links.filterIsInstance<KriyaLink.Purpose>().size)
        }
    }

    @Test
    fun `prior action links require morphology and do not cross alternative branches`() {
        for (suffix in listOf("क्त्वा", "ल्यप्")) {
            val prior = AvyayaPada("unrelated spelling", "unrelated spelling",
                dev.panini.vyakaranam.ast.AvyayaKridantaDerivation(
                    emptyList(), DhatuPrakriti("फ्रेम्", "फ्रेम्"), suffix))
            val source = priorActionVakya(prior)
            val target = akhyata()
            val sequence = UktiAnalyzer(analyzer).analyze(Ukti("", body = Sequence("",
                listOf(Invocation(source), Invocation(target)))))
            assertEquals(1, sequence.links.filterIsInstance<KriyaLink.Purvakalika>().size)
            val conditional = UktiAnalyzer(analyzer).analyze(Ukti("", body = Conditional("",
                condition = Invocation(target), consequent = Invocation(source), alternate = Invocation(target))))
            assertEquals(0, conditional.links.filterIsInstance<KriyaLink.Purvakalika>().size)
        }
        val source = akhyata(AvyayaPada("क्त्वा", "क्त्वा")).copy(sourceText = "गत्वा")
        val analysis = UktiAnalyzer(analyzer).analyze(Ukti("", body = Sequence("",
            listOf(Invocation(source), Invocation(akhyata())))))
        assertEquals(0, analysis.links.filterIsInstance<KriyaLink.Purvakalika>().size)
    }

    @Test
    fun `shared agents compare nominal identity rather than taking first available agent`() {
        val prior = AvyayaPada("prior", "prior", dev.panini.vyakaranam.ast.AvyayaKridantaDerivation(
            emptyList(), DhatuPrakriti("फ्रेम्", "फ्रेम्"), "क्त्वा"))
        val rama = subanta("राम + सुँ", "राम", "सुँ")
        val others = listOf(
            rama.copy(sourceText = "different source formatting"),
            subanta("देव + सुँ", "देव", "सुँ"),
            rama.copy(pratipadika = MulaPratipadika("राम", "राम", listOf(
                dev.panini.vyakaranam.ast.StriVikara("टाप्", "टाप्")))),
            subanta("राम + औ", "राम", "औ"),
        )
        for ((index, other) in others.withIndex()) {
            val analysis = UktiAnalyzer { vakya, id -> analyzeWithSelectedAgents(vakya, id) }.analyze(Ukti("", body = Sequence("", listOf(
                Invocation(priorActionVakya(prior, rama)), Invocation(akhyata(other))))))
            assertEquals(if (index == 0) 1 else 0,
                analysis.links.filterIsInstance<KriyaLink.SharedParticipant>().size)
            assertEquals(if (index == 0) 0 else 1,
                analysis.diagnostics.count { it.code == FrameDiagnosticCode.CONFLICTING_SHARED_AGENT })
        }
    }

    @Test
    fun `purpose link does not establish an omitted shared agent`() {
        val purpose = AvyayaPada("purpose", "purpose", dev.panini.vyakaranam.ast.AvyayaKridantaDerivation(
            emptyList(), DhatuPrakriti("फ्रेम्", "फ्रेम्"), "तुमुँन्"))
        val analysis = UktiAnalyzer(analyzer).analyze(Ukti("", body = Sequence("", listOf(
            Invocation(akhyata(purpose)), Invocation(akhyata(subanta("राम + सुँ", "राम", "सुँ")))))))
        assertEquals(1, analysis.links.filterIsInstance<KriyaLink.Purpose>().size)
        assertEquals(0, analysis.links.filterIsInstance<KriyaLink.SharedParticipant>().size)
    }

    @Test
    fun `coordinated agents share every member without depending on member order`() {
        val prior = AvyayaPada("prior", "prior", dev.panini.vyakaranam.ast.AvyayaKridantaDerivation(
            emptyList(), DhatuPrakriti("फ्रेम्", "फ्रेम्"), "क्त्वा"))
        val rama = subanta("राम + सुँ", "राम", "सुँ")
        val deva = subanta("देव + सुँ", "देव", "सुँ")
        fun coordinated(members: List<SubantaPada>) =
            dev.panini.vyakaranam.ast.SamuccitaSubanta("coordinated", members)
        val analysis = UktiAnalyzer { vakya, id -> analyzeWithSelectedAgents(vakya, id) }.analyze(Ukti("", body = Sequence("", listOf(
            Invocation(priorActionVakya(prior, coordinated(listOf(rama, deva)))),
            Invocation(akhyata(coordinated(listOf(deva, rama))))))))
        assertEquals(setOf(rama.sourceText, deva.sourceText),
            analysis.links.filterIsInstance<KriyaLink.SharedParticipant>().map { it.participantSource }.toSet())
        assertEquals(emptyList(), analysis.diagnostics)
    }

    @Test
    fun `both conditional exits connect to following sentence`() {
        for (hasAlternate in listOf(false, true)) {
            fun invocation() = Invocation(akhyata())
            val branch = Conditional("", invocation(), invocation(),
                if (hasAlternate) invocation() else null)
            val analysis = UktiAnalyzer(analyzer).analyze(Ukti("", body = Sequence("",
                listOf(branch, invocation()), listOf("ततः"))))
            val edges = analysis.links.filterIsInstance<KriyaLink.Coordination>()
            val next = KriyaId(if (hasAlternate) "kriya-4" else "kriya-3")
            assertEquals(setOf(
                KriyaId("kriya-2") to next,
                KriyaId(if (hasAlternate) "kriya-3" else "kriya-1") to next,
            ), edges.map { it.source to it.target }.toSet())
            assertEquals(setOf("ततः"), edges.map { it.connector }.toSet())
        }
    }

    @Test
    fun `nested conditional retains every branch exit`() {
        fun invocation() = Invocation(akhyata())
        val inner = Conditional("", invocation(), invocation(), invocation())
        val outer = Conditional("", invocation(), inner, invocation())
        val analysis = UktiAnalyzer(analyzer).analyze(Ukti("", body = Sequence("",
            listOf(outer, invocation()))))
        assertEquals(setOf(3, 4, 5).map { KriyaId("kriya-$it") to KriyaId("kriya-6") }.toSet(),
            analysis.links.filterIsInstance<KriyaLink.Coordination>().map { it.source to it.target }.toSet())
    }

    @Test
    fun `loop analysis retains condition exhaustion result and following frame identities`() {
        fun invocation(label: String) = Invocation(akhyata().copy(sourceText = label))
        val loop = dev.panini.vyakaranam.ast.WhileLoop("", invocation("test"), invocation("body"),
            maximumIterationStems = listOf("दशन्"), exhausted = invocation("exhausted"),
            resultTarget = invocation("result"))
        val analysis = UktiAnalyzer(analyzer).analyze(Ukti("", body = Sequence("",
            listOf(loop, invocation("following")))))
        assertEquals(listOf("test", "body", "exhausted", "result", "following"),
            analysis.frames.map { it.vakya.sourceText })
        fun id(n: Int) = KriyaId("kriya-$n")
        assertEquals(listOf(KriyaLink.Condition(id(1), id(2))),
            analysis.links.filterIsInstance<KriyaLink.Condition>())
        assertEquals(listOf(KriyaLink.LoopBack(id(2), id(1))),
            analysis.links.filterIsInstance<KriyaLink.LoopBack>())
        assertEquals(listOf(KriyaLink.Exhaustion(id(2), id(3))),
            analysis.links.filterIsInstance<KriyaLink.Exhaustion>())
        assertEquals(setOf(1, 2, 3).map { KriyaLink.LoopResult(id(it), id(4)) }.toSet(),
            analysis.links.filterIsInstance<KriyaLink.LoopResult>().toSet())
        assertEquals(listOf(KriyaLink.Coordination(id(4), id(5), "")),
            analysis.links.filterIsInstance<KriyaLink.Coordination>())
    }

    /** These agreement/ownership fixtures independently select their written nominatives as agents. */
    private fun analyzeWithSelectedAgents(vakya: dev.panini.vyakaranam.ast.Vakya, id: KriyaId = KriyaId("kriya-1")): KriyaFrame {
        val nominals = vakya.padas.flatMap { when (it) {
            is SubantaPada -> listOf(it)
            is dev.panini.vyakaranam.ast.SamuccitaSubanta -> it.members.filterIsInstance<SubantaPada>()
            else -> emptyList()
        } }
        val readings = nominals.mapIndexedNotNull { index, pada ->
            index.takeIf { pada.sup.affixCandidates.isNotEmpty() && pada.sup.affixCandidates.all { it.vibhakti == Vibhakti.PRATHAMA } }
        }.associateWith { setOf(SemanticRelation.INDEPENDENT_AGENT) }
        return analyzer.analyze(vakya, id, VakyaSemanticContext(readings))
    }

    private fun subanta(source: String, stem: String, sup: String): SubantaPada =
        SubantaPada(
            sourceText = source,
            pratipadika = MulaPratipadika(stem, stem),
            sup = SupPratyaya(sup, sup),
        )

    private fun priorActionVakya(prior: AvyayaPada, vararg participants: dev.panini.vyakaranam.ast.Pada): AkhyataVakya {
        val base = akhyata(*participants)
        val head = base.tinganta.copy(sourceText = prior.sourceText,
            priorAction = prior.derivation as dev.panini.vyakaranam.ast.AvyayaKridantaDerivation)
        return base.copy(padas = participants.toList() + head, tinganta = head)
    }

    private fun akhyata(vararg participants: dev.panini.vyakaranam.ast.Pada): AkhyataVakya {
        val tinganta = TingantaPada(
            sourceText = "फ्रेम् + लट् + तिप्",
            upasargas = emptyList(),
            dhatu = DhatuPrakriti("फ्रेम्", "फ्रेम्"),
            lakara = Lakara.LAT,
            ting = TingPratyaya("तिप्", "तिप्"),
        )
        return AkhyataVakya(
            sourceText = participants.joinToString(" ") { it.sourceText } + " ददाति",
            padas = participants.toList() + tinganta,
            tinganta = tinganta,
        )
    }
}
