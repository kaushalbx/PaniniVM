package dev.panini.analysis

import dev.panini.core.*
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Karmatva
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.lexicon.*
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class CreditorSentenceTest {
    @Test fun `parsed creditor clauses preserve designation across order voice and syncretic endings`() {
        val lexicalRoot = Dhatu("creditor.dhr", 1, "धृङ्", "धृ", "अवस्थाने", "रहना", "remain",
            DhatuGana.TUDADI, karmatva = Karmatva.AKARMAKA)
        val analyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(listOf(
            PratipadikaEntry("देवदत्त", setOf(Linga.PUMS)),
            PratipadikaEntry("शत", setOf(Linga.NAPUMSAKA)),
        ), listOf(lexicalRoot))))
        for (passive in listOf(false, true)) for (causative in listOf(false, true))
            for (ending in listOf("ङे", "भ्याम्", "टा")) for (reversed in listOf(false, true)) {
                val credit = "देवदत्त + $ending"
                val objectWord = "शत + ${if (passive) "सुँ" else "अम्"}"
                val words = if (reversed) listOf(objectWord, credit) else listOf(credit, objectWord)
                val verb = "धृ + " + (if (causative) "णिच् + " else "") +
                    (if (passive) "यक् + लट् + त" else "लट् + तिप्")
                val vakya = PaniniParser().parse(words.joinToString(" ") + " $verb ।")
                    .grammaticalVakyas().single()
                val creditorIndex = if (reversed) 1 else 0
                val frame = analyzer.analyze(vakya, semanticContext = VakyaSemanticContext(
                    participantRelations = mapOf(creditorIndex to setOf(SemanticRelation.RECIPIENT, SemanticRelation.CREDITOR),
                        1 - creditorIndex to setOf(SemanticRelation.DESIRED_OBJECT))))
                val relation = frame.relations[creditorIndex]
                val label = "passive=$passive causative=$causative ending=$ending reversed=$reversed"
                assertEquals(if (passive && !causative) Prayoga.BHAVE else if (passive) Prayoga.KARMANI
                    else if (causative) Prayoga.CAUSATIVE else Prayoga.KARTARI,
                    frame.prayoga, label)
                if (causative) {
                    assertEquals(FrameKarakaResolution.Resolved(Karaka.SAMPRADANA), relation.resolution, label)
                    assertEquals(if (ending == "टा") listOf("1.4.35") else listOf("1.4.35", "2.3.13"),
                        relation.evidence.map { it.sutra }, label)
                    assertEquals(if (ending == "टा") null else Vibhakti.CHATURTHI, relation.resolvedVibhakti, label)
                } else assertTrue(relation.evidence.none { it.sutra == "1.4.35" }, label)
            }
    }
}
