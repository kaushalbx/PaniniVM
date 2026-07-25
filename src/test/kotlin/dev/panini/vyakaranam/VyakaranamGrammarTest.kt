package dev.panini.vyakaranam

import dev.panini.core.Karaka
import dev.panini.core.Linga
import dev.panini.core.NominalCategory
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.dhatupatha.bhvadi.GamDhatu
import dev.panini.dhatupatha.bhvadi.PalayDhatu
import dev.panini.dhatupatha.juhotyadi.DaDhatu
import dev.panini.dhatupatha.tudadi.LikhDhatu
import dev.panini.parser.VyakaranamLexer
import dev.panini.parser.VyakaranamParser
import dev.panini.vyakaranam.analysis.AnalyzedSamuccita
import dev.panini.vyakaranam.analysis.DhatuIdentity
import dev.panini.vyakaranam.analysis.KarakaRuleContext
import dev.panini.vyakaranam.analysis.KarakaRuleEngine
import dev.panini.vyakaranam.analysis.ParticipantFacts
import dev.panini.vyakaranam.analysis.ParticipantRelationInferrer
import dev.panini.vyakaranam.analysis.SemanticRelation
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.lexicon.InMemoryVyakaranamLexicon
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import org.antlr.v4.kotlinruntime.BaseErrorListener
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.RecognitionException
import org.antlr.v4.kotlinruntime.Recognizer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class VyakaranamGrammarTest {

    // ── Parser smoke tests ────────────────────────────────────────────────────

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

    // ── Full-engine integration tests ─────────────────────────────────────────

    @Test
    fun `coordinated subantas participate in karaka analysis`() {
        val lexicon = InMemoryVyakaranamLexicon(
            pratipadikas = listOf(
                PratipadikaEntry("राम", setOf(Linga.PUMS)),
                PratipadikaEntry("लक्ष्मण", setOf(Linga.PUMS)),
            ),
            dhatus = listOf(GamDhatu()),
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
            dhatus = listOf(DaDhatu(), LikhDhatu(), PalayDhatu()),
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

    // ── Participant-relation inferencing ──────────────────────────────────────

    @Test
    fun `verifies dynamic participant relation inferencing from nominal categories`() {
        val locEntry = PratipadikaEntry("गृह", setOf(Linga.NAPUMSAKA), categories = setOf(NominalCategory.PLACE_LOCATION))
        val locRelations = ParticipantRelationInferrer.infer(locEntry, setOf(Vibhakti.SAPTAMI), emptySet())
        assertTrue(SemanticRelation.LOCATION in locRelations)

        val toolEntry = PratipadikaEntry("लेखनी", setOf(Linga.STRI), categories = setOf(NominalCategory.INSTRUMENT_TOOL))
        val toolRelations = ParticipantRelationInferrer.infer(toolEntry, setOf(Vibhakti.TRTIYA), emptySet())
        assertTrue(SemanticRelation.INSTRUMENT in toolRelations)
    }

    // ── Core kāraka + anabhihite boundary tests ───────────────────────────────

    @Test
    fun `verifies core karaka samjna rules and anabhihite governing behavior`() {
        val possible = setOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA, Vibhakti.TRTIYA, Vibhakti.SAPTAMI)

        // 1.4.45 Adhikarana
        val locRes = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity("स्था"),
                participant = ParticipantFacts("p1", AvyayaPada("गृहे", "गृहे"), possible, setOf(SemanticRelation.LOCATION)),
                allParticipants = emptyList(), prayoga = Prayoga.KARTARI,
            )
        )
        assertEquals(Karaka.ADHIKARANA, locRes.resolved)
        assertTrue(locRes.evidence.any { it.sutra == "1.4.45" })

        // 1.4.49 Karma
        val objRes = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity("कृ"),
                participant = ParticipantFacts("p2", AvyayaPada("कटम्", "कटम्"), possible, setOf(SemanticRelation.DESIRED_OBJECT)),
                allParticipants = emptyList(), prayoga = Prayoga.KARTARI,
            )
        )
        assertEquals(Karaka.KARMAN, objRes.resolved)
        assertTrue(objRes.evidence.any { it.sutra == "1.4.49" })
        assertTrue(objRes.evidence.any { it.sutra == "2.3.2" })

        // 2.3.1 Anabhihite — abhihita karma in karmani gets prathama
        val abhihitaRes = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity("कृ"),
                participant = ParticipantFacts("p3", AvyayaPada("कटः", "कटः"), setOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA), setOf(SemanticRelation.DESIRED_OBJECT)),
                allParticipants = emptyList(), prayoga = Prayoga.KARMANI,
            )
        )
        assertEquals(Karaka.KARMAN, abhihitaRes.resolved)
        assertTrue(abhihitaRes.evidence.any { it.sutra == "2.3.1" })

        // 1.4.54 Kartr
        val agentRes = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity("पच्"),
                participant = ParticipantFacts("p4", AvyayaPada("देवदत्तः", "देवदत्तः"), possible, setOf(SemanticRelation.INDEPENDENT_AGENT)),
                allParticipants = emptyList(), prayoga = Prayoga.KARTARI,
            )
        )
        assertEquals(Karaka.KARTR, agentRes.resolved)
        assertTrue(agentRes.evidence.any { it.sutra == "1.4.54" })

        // 1.4.55 Hetu-kartr in causative
        val causeRes = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity("कारयति"),
                participant = ParticipantFacts("p5", AvyayaPada("यज्ञदत्तः", "यज्ञदत्तः"), possible, setOf(SemanticRelation.PROMPTER_CAUSE)),
                allParticipants = emptyList(), prayoga = Prayoga.CAUSATIVE,
            )
        )
        assertEquals(Karaka.KARTR, causeRes.resolved)
        assertTrue(causeRes.evidence.any { it.sutra == "1.4.55" })
    }

    // ── Prohibition (niṣedha) engine ──────────────────────────────────────────

    @Test
    fun `prohibition engine correctly blocks guna and vrddhi for kit ngit and special roots`() {
        val ngit = dev.panini.vyakaranam.analysis.ProhibitionContext(targetSutraNumber = "1.1.2", affixItMarkers = setOf('ङ'))
        val res1 = dev.panini.vyakaranam.analysis.NishedhaRuleEngine.evaluateProhibition(ngit)
        assertTrue(res1 is dev.panini.vyakaranam.analysis.NishedhaRuleResult.Blocked && res1.blockerSutraNumber == "1.1.5")

        val itAugment = dev.panini.vyakaranam.analysis.ProhibitionContext(targetSutraNumber = "1.1.2", isDidhiVeviOrItAugment = true)
        val res2 = dev.panini.vyakaranam.analysis.NishedhaRuleEngine.evaluateProhibition(itAugment)
        assertTrue(res2 is dev.panini.vyakaranam.analysis.NishedhaRuleResult.Blocked && res2.blockerSutraNumber == "1.1.6")

        val vowelConsonant = dev.panini.vyakaranam.analysis.ProhibitionContext(targetSutraNumber = "1.1.9", targetPhonemeIsVowel = true, secondPhonemeIsConsonant = true)
        val res3 = dev.panini.vyakaranam.analysis.NishedhaRuleEngine.evaluateProhibition(vowelConsonant)
        assertTrue(res3 is dev.panini.vyakaranam.analysis.NishedhaRuleResult.Blocked && res3.blockerSutraNumber == "1.1.10")

        val setKtva = dev.panini.vyakaranam.analysis.ProhibitionContext(targetSutraNumber = "KIT_STATUS", isSetKtvaAffix = true)
        val res4 = dev.panini.vyakaranam.analysis.NishedhaRuleEngine.evaluateProhibition(setKtva)
        assertTrue(res4 is dev.panini.vyakaranam.analysis.NishedhaRuleResult.Blocked && res4.blockerSutraNumber == "1.2.4")
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun assertParsesUkti(source: String) {
        val errors = mutableListOf<String>()
        val listener = object : BaseErrorListener() {
            override fun syntaxError(
                recognizer: Recognizer<*, *>,
                offendingSymbol: Any?,
                line: Int,
                charPositionInLine: Int,
                msg: String,
                e: RecognitionException?,
            ) {
                errors += "$line:$charPositionInLine $msg"
            }
        }
        val lexer = VyakaranamLexer(CharStreams.fromString(source)).apply {
            removeErrorListeners(); addErrorListener(listener)
        }
        val parser = VyakaranamParser(CommonTokenStream(lexer)).apply {
            removeErrorListeners(); addErrorListener(listener)
        }
        parser.ukti()
        assertEquals(emptyList(), errors)
    }
}
