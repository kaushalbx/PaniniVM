package dev.panini.analysis

import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.PadaType
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

class KriyaFrameAnalyzerTest {
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
            val frame = localAnalyzer.analyze(original.copy(padas = listOf(agent, objectPada, verb), tinganta = verb))
            assertEquals(dev.panini.core.Prayoga.KARMANI, frame.prayoga)
            assertEquals(listOf(Karaka.KARTR, Karaka.KARMAN), frame.relations.map {
                assertIs<FrameKarakaResolution.Resolved>(it.resolution).karaka
            })
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
            val frame = analyzer.analyze(original.copy(padas = participants.toList() + verb, tinganta = verb))
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
            return analyzer.analyze(original.copy(padas = listOf(group, verb), tinganta = verb))
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
        assertEquals(0, analyzer.analyze(akhyata(rama, shyama)).diagnostics.count {
            it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH
        })
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

    @Test
    fun `one kriya frame preserves several participants of the same karaka`() {
        val rama = subanta("राम + सुँ", "राम", "सुँ")
        val phala = subanta("फल + अम्", "फल", "अम्")
        val pushpa = subanta("पुष्प + अम्", "पुष्प", "अम्")
        val vakya = akhyata(rama, phala, pushpa)

        val frame = requireNotNull(analyzer.analyze(vakya))

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
                sourceText = "यदि ... तर्हि ...",
                body = Conditional(
                    sourceText = "यदि ... तर्हि ...",
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
    fun `apadana karaka is resolved for jugupsa and virama verbs`() {
        val participant = subanta("पाप + ङसिँ", "पाप", "ङसिँ")
        val vakya = akhyata(participant)

        val frame = requireNotNull(analyzer.analyze(vakya))
        assertEquals(1, frame.relations.size)
        val resolved = assertIs<FrameKarakaResolution.Resolved>(
            frame.relations.single().resolution,
        )
        assertEquals(Karaka.APADANA, resolved.karaka)
    }

    @Test
    fun `purvakalika and shared participant links are inferred for ktva and lyap clauses`() {
        val ktvaTinganta = TingantaPada(
            sourceText = "फ्रेम् + क्त्वा",
            upasargas = emptyList(),
            dhatu = DhatuPrakriti("फ्रेम्", "फ्रेम्"),
            lakara = Lakara.LAT,
            ting = TingPratyaya("तिप्", "तिप्"),
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

        val analysis = UktiAnalyzer(analyzer).analyze(ukti)
        val purvakalika = analysis.links.filterIsInstance<KriyaLink.Purvakalika>()
        val sharedAgent = analysis.links.filterIsInstance<KriyaLink.SharedParticipant>()

        assertEquals(1, purvakalika.size)
        assertEquals(KriyaId("kriya-1"), purvakalika.single().source)
        assertEquals(KriyaId("kriya-2"), purvakalika.single().target)
        assertEquals("राम + सुँ", sharedAgent.single().participantSource)
    }

    private fun subanta(source: String, stem: String, sup: String): SubantaPada =
        SubantaPada(
            sourceText = source,
            pratipadika = MulaPratipadika(stem, stem),
            sup = SupPratyaya(sup, sup),
        )

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
