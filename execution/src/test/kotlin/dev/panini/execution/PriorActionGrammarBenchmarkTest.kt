package dev.panini.execution

import com.google.gson.JsonParser
import dev.panini.analysis.*
import dev.panini.execution.binding.DhatuCache
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.vyakaranam.ast.DhatuPrakriti
import dev.panini.vyakaranam.lexicon.StandardPratipadikaLexicon
import dev.panini.vyakaranam.lexicon.VyakaranamLexicon
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class PriorActionGrammarBenchmarkTest {
    @Test fun `independent JSON expectations distinguish passive resolution ambiguity and unsupported ownership`() {
        val manifest = javaClass.getResourceAsStream("/prior_action_grammar_expectations.json")!!
            .bufferedReader(Charsets.UTF_8).use { JsonParser.parseReader(it).asJsonObject }
        assertEquals(1, manifest.get("schemaVersion").asInt)
        val cases = manifest.getAsJsonArray("cases").flatMap { entry ->
            val base = entry.asJsonObject
            listOf(base) + base.getAsJsonArray("variants")?.map { variant ->
                val written = variant.asJsonObject
                assertTrue(written.get("rationale").asString.isNotBlank())
                base.deepCopy().also {
                    it.addProperty("id", base.get("id").asString + ":" + written.get("idSuffix").asString)
                    it.add("source", written.get("source"))
                    it.remove("variants")
                }
            }.orEmpty()
        }
        assertEquals(cases.size, cases.map { it.get("id").asString }.toSet().size)
        for (case in cases) {
            val id = case.get("id").asString
            assertTrue(case.get("rationale").asString.isNotBlank())
            val readings = case.getAsJsonObject("lexicalReadings").entrySet().associate { (stem, roles) ->
                stem to roles.asJsonArray.map { SemanticRelation.valueOf(it.asString) }.toSet()
            }
            val ukti = PaniniParser().parse(case.get("source").asString)
            val analysis = if (readings.isEmpty()) assertNotNull(VyakaranamExecutionAdapter.analyzeGrammar(ukti)) else {
                val lexicon = object : VyakaranamLexicon {
                    override fun findPratipadika(text: String) = (StandardPratipadikaLexicon.findPratipadika(text)
                        ?: if (text in readings) dev.panini.vyakaranam.lexicon.PratipadikaEntry(
                            text, setOf(dev.panini.core.Linga.PUMS)) else null)?.let {
                        if (text in readings) it.copy(semanticRelations = readings.getValue(text)) else it
                    }
                    override fun findDhatu(text: String) = DhatuCache.resolve(DhatuPrakriti(text, text))
                }
                UktiAnalyzer(VakyaAnalyzer(PadaAnalyzer(lexicon, validatePadaCompatibility = false))).analyze(ukti)
            }
            if (case.get("expectedStatus").asString == "UNSUPPORTED") {
                assertTrue(analysis.blockingDiagnostics.any { it.code == FrameDiagnosticCode.PRIOR_ACTION_STRUCTURE &&
                    it.message.startsWith(case.get("blockingPrefix").asString) }, id)
                continue
            }
            val status = case.get("expectedStatus").asString
            assertTrue(status in setOf("ANALYZED", "AGREEMENT_ERROR"), id)
            assertTrue(analysis.blockingDiagnostics.isEmpty(), "$id: ${analysis.blockingDiagnostics}")
            val expectedFrames = case.getAsJsonArray("frames").map { it.asJsonObject }
            assertEquals(expectedFrames.size, analysis.frames.size, id)
            expectedFrames.zip(analysis.frames).forEach { (expected, frame) ->
                assertEquals(expected.get("headKind").asString, if (frame.kriya!!.isFinite) "FINITE" else "NONFINITE", id)
                assertEquals(expected.get("prayoga").asString, frame.prayoga.name, id)
                val participants = expected.getAsJsonArray("participants").map { it.asJsonObject }
                assertEquals(participants.map { it.get("source").asString }.sorted(),
                    frame.relations.map { it.participant.pada.sourceText }.sorted(), id)
                participants.forEach { participant ->
                    val actual = frame.relations.single { it.participant.pada.sourceText == participant.get("source").asString }
                    assertTrue(frame.sourceVakya.padas.any { it === actual.participant.pada },
                        "$id: participant must retain its original written occurrence")
                    if (participant.has("candidates")) {
                        val ambiguous = assertIs<FrameKarakaResolution.Ambiguous>(actual.resolution, id)
                        assertEquals(participant.getAsJsonArray("candidates").map { it.asString }.toSet(), ambiguous.candidates.map { it.name }.toSet(), id)
                    } else {
                        assertEquals(participant.get("karaka").asString, assertIs<FrameKarakaResolution.Resolved>(actual.resolution, id).karaka.name, id)
                        assertEquals(participant.get("case").asString, actual.resolvedVibhakti?.name, id)
                        val chain = participant.getAsJsonArray("chain").map { it.asString }
                        chain.forEach { assertTrue(manifest.getAsJsonObject("sources").has(it), "$id: missing canonical source $it") }
                        assertEquals(chain, actual.evidence.map { it.sutra }, id)
                    }
                }
                assertEquals(expected.get("agreementMismatch")?.asBoolean ?: false,
                    frame.diagnostics.any { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH }, id)
            }
            assertEquals(status == "AGREEMENT_ERROR", analysis.frames.any { frame ->
                frame.diagnostics.any { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH } }, id)
            assertEquals(case.get("sharedAgents").asInt, analysis.links.filterIsInstance<KriyaLink.SharedParticipant>().size, id)
        }
    }
}
