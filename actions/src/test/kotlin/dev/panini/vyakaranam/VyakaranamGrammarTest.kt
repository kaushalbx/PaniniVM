package dev.panini.vyakaranam

import dev.panini.core.Karaka
import dev.panini.core.Linga
import dev.panini.core.NominalCategory
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.parser.VyakaranamLexer
import dev.panini.parser.VyakaranamParser
import dev.panini.analysis.DhatuIdentity
import dev.panini.analysis.KarakaRuleContext
import dev.panini.analysis.KarakaRuleEngine
import dev.panini.analysis.NishedhaRuleEngine
import dev.panini.analysis.NishedhaRuleResult
import dev.panini.analysis.ParticipantFacts
import dev.panini.analysis.ParticipantRelationInferrer
import dev.panini.analysis.ProhibitionContext
import dev.panini.analysis.SemanticRelation
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import org.antlr.v4.kotlinruntime.BaseErrorListener
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.RecognitionException
import org.antlr.v4.kotlinruntime.Recognizer
import kotlin.test.Test
import kotlin.test.assertEquals
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
        val ngit = ProhibitionContext(targetSutraNumber = "1.1.2", affixItMarkers = setOf('ङ'))
        val res1 = NishedhaRuleEngine.evaluateProhibition(ngit)
        assertTrue(res1 is NishedhaRuleResult.Blocked && res1.blockerSutraNumber == "1.1.5")

        val itAugment = ProhibitionContext(targetSutraNumber = "1.1.2", isDidhiVeviOrItAugment = true)
        val res2 = NishedhaRuleEngine.evaluateProhibition(itAugment)
        assertTrue(res2 is NishedhaRuleResult.Blocked && res2.blockerSutraNumber == "1.1.6")

        val vowelConsonant = ProhibitionContext(targetSutraNumber = "1.1.9", targetPhonemeIsVowel = true, secondPhonemeIsConsonant = true)
        val res3 = NishedhaRuleEngine.evaluateProhibition(vowelConsonant)
        assertTrue(res3 is NishedhaRuleResult.Blocked && res3.blockerSutraNumber == "1.1.10")

        val setKtva = ProhibitionContext(targetSutraNumber = "KIT_STATUS", isSetKtvaAffix = true)
        val res4 = NishedhaRuleEngine.evaluateProhibition(setKtva)
        assertTrue(res4 is NishedhaRuleResult.Blocked && res4.blockerSutraNumber == "1.2.4")
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
