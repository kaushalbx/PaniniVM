package dev.panini.execution.binding

import dev.panini.analysis.*
import dev.panini.core.Karaka
import dev.panini.core.Linga
import dev.panini.dhatupatha.juhotyadi.DaDhatu
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.lexicon.InMemoryVyakaranamLexicon
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class ObjectMediationBindingTest {
    @Test fun `subset sum result handle dative does not establish a recipient without its objects`() {
        for (stem in listOf("धृ", "स्था")) {
            val source = "$stem + ल्युट् + ङे युज् + णिच् + लोट् + सिप् ।"
            val frame = requireNotNull(VyakaranamExecutionAdapter.analyzeGrammar(PaniniParser().parse(source)))
                .frames.single()
            val handle = frame.relations.single()
            assertTrue(handle.evidence.none { it.sutra == "1.4.32" }, "$source: $handle")
            assertFalse((handle.resolution as? FrameKarakaResolution.Resolved)?.karaka == Karaka.SAMPRADANA,
                "$source: ${handle.resolution}")
        }
    }

    @Test fun `declared result destination uses supplied object evidence without polluting grammar cache`() {
        for (ending in listOf("ङे", "भ्याम्")) {
            val source = "मान + $ending दा + लोट् + सिप् ।"
            val ukti = PaniniParser().parse(source)
            val raw = requireNotNull(VyakaranamExecutionAdapter.analyzeGrammar(ukti))
            assertTrue(raw.frames.single().relations.single().evidence.none { it.sutra == "1.4.32" })
            val conversation = dev.panini.execution.SambhashanaContext("प्रयोक्ता", "यन्त्रम्")
            val bound = assertIs<dev.panini.execution.ExecutionBindingResult.Bound>(VyakaranamExecutionAdapter.bind(
                dev.panini.execution.SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", source), ukti, conversation,
                injectedBindings = mapOf(Karaka.KARMAN to dev.panini.execution.ExecutionExpression.Reference("input"))))
            val invocation = bound.ukti.invocations.single()
            if (ending == "ङे") {
                assertTrue(Karaka.SAMPRADANA in invocation.bindings)
                assertTrue(invocation.karakaTrace.any { it.startsWith("1.4.32 ") })
            } else assertFalse(Karaka.SAMPRADANA in invocation.bindings,
                "A supplied object cannot resolve a syncretic destination by itself.")
            assertSame(raw, VyakaranamExecutionAdapter.analyzeGrammar(ukti))
            assertTrue(raw.frames.single().relations.single().evidence.none { it.sutra == "1.4.32" })
        }
    }
    @Test fun `ambiguous mediator cannot supply a definite execution recipient in either word order`() {
        val dhatu = DaDhatu()
        val analyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(listOf(
            PratipadikaEntry("देवदत्त", setOf(Linga.PUMS)),
            PratipadikaEntry("धान्य", setOf(Linga.NAPUMSAKA)),
        ), listOf(dhatu))))
        for (reversed in listOf(false, true)) {
            val words = listOf("देवदत्त + ङे", "धान्य + ङस्").let { if (reversed) it.reversed() else it }
            val source = words.joinToString(" ") + " दा + लोट् + सिप् ।"
            val vakya = PaniniParser().parse(source).grammaticalVakyas().single() as AkhyataVakya
            val recipient = if (reversed) 1 else 0
            val mediator = 1 - recipient
            val frame = analyzer.analyze(vakya, semanticContext = VakyaSemanticContext(
                participantRelations = mapOf(recipient to setOf(SemanticRelation.RECIPIENT),
                    mediator to setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SOURCE,
                        SemanticRelation.SEPARATION_REFERENCE)),
                intendedObjects = mapOf(recipient to mediator),
            ))
            assertTrue(frame.diagnostics.any { it.code == FrameDiagnosticCode.AMBIGUOUS_OBJECT_MEDIATION &&
                it.nominalIndex == recipient })
            assertIs<FrameKarakaResolution.Unassigned>(frame.relations[recipient].resolution)
            val extracted = KarakaExtractor.extractKarakas(vakya.padas, BindingContext(
                conversation = null, clauseIndex = 0, dhatu = dhatu, frame = frame, previousDhatus = emptyList()))
            assertFalse(Karaka.SAMPRADANA in extracted.bindings,
                "reversed=$reversed; extracted=$extracted; relations=${frame.relations}; padas=${vakya.padas}")
            assertTrue(Karaka.ANIRDHARITA in extracted.bindings)
        }
    }
}
