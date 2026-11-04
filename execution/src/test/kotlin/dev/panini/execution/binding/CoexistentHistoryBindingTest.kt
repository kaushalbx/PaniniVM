package dev.panini.execution.binding

import dev.panini.analysis.*
import dev.panini.core.*
import dev.panini.dhatupatha.divadi.DivDhatu
import dev.panini.execution.*
import dev.panini.execution.sutra.ProgramSutraArthaCodec
import dev.panini.vyakaranam.lexicon.*
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class CoexistentHistoryBindingTest {
    @TempDir lateinit var directory: Path
    @Test
    fun `instrumental and passive div projections preserve simultaneous names without case guessing`() {
        val dhatu = DivDhatu()
        val analyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(
            listOf(PratipadikaEntry("अक्ष", setOf(Linga.PUMS))), listOf(dhatu))))
        data class Case(val ending: String, val passive: Boolean, val role: Karaka?, val caseRule: String?)
        for (case in listOf(Case("टा", false, Karaka.KARANA, "2.3.18"),
            Case("सुँ", true, Karaka.KARMAN, "2.3.46"), Case("टा", true, null, null))) {
            val verb = if (case.passive) "दिव् + यक् + लट् + त" else "दिव् + लोट् + सिप्"
            val vakya = PaniniParser().parse("अक्ष + ${case.ending} $verb ।").grammaticalVakyas().single()
            val frame = analyzer.analyze(vakya, semanticContext = VakyaSemanticContext(
                participantRelations = mapOf(0 to setOf(SemanticRelation.INSTRUMENT))))
            assertEquals(if (case.passive) Prayoga.KARMANI else Prayoga.KARTARI, frame.prayoga)
            val relation = frame.relations.single()
            assertEquals(setOf(Karaka.KARMAN, Karaka.KARANA), relation.coexistingKarakas, case.toString())
            assertEquals(listOfNotNull("1.4.43", case.caseRule), relation.evidence.map { it.sutra }, case.toString())
            val value = SanskritValue.Shabda("captured dice")
            val extracted = KarakaExtractor.extractKarakas(vakya.padas, BindingContext(null, 0, dhatu, frame,
                previousDhatus = emptyList(), environment = ValueEnvironment(mapOf("अक्ष" to value))))
            if (case.role == null) {
                assertTrue(extracted.historyParticipants.isEmpty())
                assertFalse(Karaka.KARMAN in extracted.bindings)
                assertFalse(Karaka.KARANA in extracted.bindings)
            } else {
                assertEquals(setOf(case.role), extracted.bindings.keys)
                assertEquals(case.role, extracted.historyParticipants.single().projection)
                val captured = ExecutionContext(extracted.bindings, variables = mapOf("अक्ष" to value),
                    historyParticipants = extracted.historyParticipants).historyParticipantValues()
                assertEquals(listOf(value), captured[Karaka.KARMAN])
                assertEquals(listOf(value), captured[Karaka.KARANA])
            }
        }
    }

    @Test
    fun `canonical div instrument retains both names through binding and execution resolution`() {
        val dhatu = DivDhatu()
        val vakya = PaniniParser().parse("अक्ष + अम् दिव् + लोट् + सिप् ।").grammaticalVakyas().single()
        val analyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(
            listOf(PratipadikaEntry("अक्ष", setOf(Linga.PUMS))), listOf(dhatu))))
        val frame = analyzer.analyze(vakya, semanticContext = VakyaSemanticContext(
            participantRelations = mapOf(0 to setOf(SemanticRelation.INSTRUMENT))))
        val relation = frame.relations.single()
        assertEquals(setOf(Karaka.KARMAN, Karaka.KARANA), relation.coexistingKarakas, frame.toString())
        assertEquals(listOf("1.4.43", "2.3.2"), relation.evidence.map { it.sutra })
        val seven = SanskritValue.Sankhya(7, "सप्त")
        val original = SanskritValue.Suchi(listOf(seven, seven))
        val extracted = KarakaExtractor.extractKarakas(vakya.padas, BindingContext(
            null, 0, dhatu, frame, previousDhatus = emptyList(),
            environment = ValueEnvironment(values = mapOf("अक्ष" to original))))
        assertEquals(setOf(Karaka.KARMAN), extracted.bindings.keys)
        assertEquals(1, extracted.historyParticipants.size)
        val transported = extracted.historyParticipants.map {
            ProgramSutraArthaCodec.decodeHistoryParticipant(ProgramSutraArthaCodec.encodeHistoryParticipant(it))
        }
        val invocation = DhatuInvocation("canonical-div-history", dhatu, extracted.bindings,
            historyParticipants = transported)
        val resolved = assertIs<OperationResolution.Resolved>(OperationResolver.resolve(
            invocation, mapOf("अक्ष" to original))).value
        val captured = resolved.context.historyParticipantValues().mapValues { (_, values) -> values.map { it.historySnapshot() } }
        assertEquals(listOf(original), captured[Karaka.KARMAN])
        assertEquals(listOf(original), captured[Karaka.KARANA])
        val result = assertIs<ExecutionResult.Success>(resolved.operation.action.execute(resolved.context, resolved.operation))
        assertEquals(7L, (result.typedValue as SanskritValue.Sankhya).value)
        assertEquals(listOf(original), captured[Karaka.KARANA])
        assertEquals(setOf(Karaka.KARMAN), resolved.context.bindings.keys)
        val memory = dev.panini.execution.memory.KriyaMemory(listOf(
            dev.panini.execution.memory.RememberedKriya(1, frame, result.typedValue, captured)))
        dev.panini.execution.memory.FileKriyaMemoryStore(directory.toFile()).save("canonical-div", memory)
        var reconstructed = false
        val reloaded = assertNotNull(dev.panini.execution.memory.FileKriyaMemoryStore(directory.toFile())
            .load("canonical-div") { reconstructed = true; listOf(frame) })
        assertTrue(reconstructed, "The fresh store must decode the file rather than reuse its analysis cache.")
        assertEquals(captured, reloaded.entries.single().participants)
        assertEquals(listOf(original), reloaded.entries.single().participants[Karaka.KARANA])
    }
}
