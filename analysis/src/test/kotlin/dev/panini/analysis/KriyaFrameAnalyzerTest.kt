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
import dev.panini.vyakaranam.ast.UktiStructure
import dev.panini.vyakaranam.lexicon.InMemoryVyakaranamLexicon
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class KriyaFrameAnalyzerTest {
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
                vakyas = listOf(condition, consequent),
                structure = UktiStructure.Conditional(hasAlternate = false),
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
            vakyas = listOf(ktvaVakya, mainVakya),
            structure = UktiStructure.Sequence,
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
