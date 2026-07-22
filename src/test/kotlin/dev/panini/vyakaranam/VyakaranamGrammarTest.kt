package dev.panini.vyakaranam

import dev.panini.core.Karaka
import dev.panini.core.Linga
import dev.panini.core.PadaType
import dev.panini.parser.VyakaranamLexer
import dev.panini.parser.VyakaranamParser
import dev.panini.dhatupatha.bhvadi.GamDhatu
import dev.panini.dhatupatha.bhvadi.PalayDhatu
import dev.panini.dhatupatha.juhotyadi.DaDhatu
import dev.panini.dhatupatha.tudadi.LikhDhatu
import dev.panini.dhatupatha.rudhadi.YujirDhatu
import dev.panini.vyakaranam.analysis.AnalyzedSamuccita
import dev.panini.vyakaranam.lexicon.InMemoryVyakaranamLexicon
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class VyakaranamGrammarTest {

    @Test
    fun `parses an annotated finite sentence`() {
        assertParsesUkti("राम + सुँ फल + अम् खाद् + लट् + तिप् ।")
    }

    @Test
    fun `parses derivationally annotated nominal morphology`() {
        assertParsesUkti("प्र + कृ + क्त + मतुप् + ङीप् + सुँ अस्ति + लट् + तिप् ।")
    }

    @Test
    fun `parses sanadi morphology without treating it as surface Sanskrit`() {
        assertParsesUkti("राम + सुँ भू + णिच् + सन् + लट् + तिप् ।")
    }

    @Test
    fun `parses explicit compound sup deletion`() {
        assertParsesUkti("राजन् + ङस् + लुक्-पुरुष + सुँ भू + लट् + तिप् ।")
    }

    @Test
    fun `coordinated subantas participate in karaka analysis`() {
        val lexicon = InMemoryVyakaranamLexicon(
            pratipadikas = listOf(
                PratipadikaEntry("राम", setOf(Linga.PUMS)),
                PratipadikaEntry("लक्ष्मण", setOf(Linga.PUMS)),
            ),
            dhatus = listOf(
                GamDhatu(),
            ),
        )
        val analysis = PaniniyaVyakaranamEngine(lexicon).analyze(
            "राम + सुँ, लक्ष्मण + औ च गम् + लट् + झि ।",
        ).vakyas.single()

        assertIs<AnalyzedSamuccita>(analysis.padaAnalyses.first())
        assertEquals(2, analysis.karakas.count { it.karaka == Karaka.KARTR })
    }

    @Test
    fun `vakya analysis resolves bhyam from dhatu semantics with sutra trace`() {
        val lexicon = InMemoryVyakaranamLexicon(
            pratipadikas = listOf(
                PratipadikaEntry("राम", setOf(Linga.PUMS)),
                PratipadikaEntry("पुस्तक", setOf(Linga.NAPUMSAKA)),
                PratipadikaEntry("लेखनी", setOf(Linga.STRI)),
            ),
            dhatus = listOf(
                DaDhatu(),
                LikhDhatu(),
                PalayDhatu(),
            ),
        )
        val engine = PaniniyaVyakaranamEngine(lexicon)

        fun assignment(source: String) = engine.analyze(source).vakyas.single().karakas
            .single { it.pada.sup.text == "भ्याम्" }

        val recipient = assignment("राम + भ्याम् पुस्तक + अम् दा + लट् + तिप् ।")
        assertEquals(Karaka.SAMPRADANA, recipient.karaka)
        assertTrue("1.4.32" in recipient.reason && "2.3.13" in recipient.reason)

        val instrument = assignment("लेखनी + भ्याम् लिख् + लट् + तिप् ।")
        assertEquals(Karaka.KARANA, instrument.karaka)
        assertTrue("1.4.42" in instrument.reason && "2.3.18" in instrument.reason)

        val source = assignment("राम + भ्याम् पलाय् + लट् + तिप् ।")
        assertEquals(Karaka.APADANA, source.karaka)
        assertTrue("1.4.24" in source.reason && "2.3.28" in source.reason)
    }

    @Test
    fun `multi-participant sentence disambiguates distinct ambiguous syncretic arguments`() {
        val lexicon = InMemoryVyakaranamLexicon(
            pratipadikas = listOf(
                PratipadikaEntry("राम", setOf(Linga.PUMS)),
                PratipadikaEntry("पुस्तक", setOf(Linga.NAPUMSAKA)),
            ),
            dhatus = listOf(DaDhatu()),
        )
        val engine = PaniniyaVyakaranamEngine(lexicon)
        val vakyas = engine.analyze("राम + भ्याम् पुस्तक + अम् दा + लट् + तिप् ।").vakyas.single()

        val recipient = vakyas.karakas.single { it.pada.sourceText.startsWith("राम") }
        assertEquals(Karaka.SAMPRADANA, recipient.karaka)
    }

    private fun assertParsesUkti(source: String) {
        val errors = mutableListOf<String>()
        val listener = object : BaseErrorListener() {
            override fun syntaxError(
                recognizer: Recognizer<*, *>?,
                offendingSymbol: Any?,
                line: Int,
                charPositionInLine: Int,
                msg: String?,
                e: RecognitionException?,
            ) {
                errors += "$line:$charPositionInLine ${msg.orEmpty()}"
            }
        }

        val lexer = VyakaranamLexer(CharStreams.fromString(source)).apply {
            removeErrorListeners()
            addErrorListener(listener)
        }
        val parser = VyakaranamParser(CommonTokenStream(lexer)).apply {
            removeErrorListeners()
            addErrorListener(listener)
        }

        parser.ukti()

        assertEquals(emptyList(), errors)
    }
}
