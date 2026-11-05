package dev.panini.execution

import dev.panini.analysis.*
import dev.panini.core.Karaka
import dev.panini.core.KrtAffix
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParser
import java.io.File
import kotlin.test.*

class PriorActionGrammarTest {
    @Test fun `preposed agent ownership also supports bhave and multiple prior actions`() {
        for (source in listOf(
            "राम + टा एक + अम् मुद्र् + णिच् + क्त्वा आसँ + यक् + लट् + त ।",
            "राम + टा एक + अम् युज् + णिच् + क्त्वा द्वि + अम् मुद्र् + णिच् + क्त्वा फल + सुँ डुकृञ् + यक् + लट् + त ।")) {
            val analysis = analyzeWithSelectedReadings(source)
            assertTrue(analysis.blockingDiagnostics.isEmpty(), analysis.blockingDiagnostics.toString())
            val main = analysis.frames.last()
            val agent = main.relations.single { it.participant.pada.sourceText == "राम+टा" }
            assertEquals(listOf("1.4.54", "2.3.18"), agent.evidence.map { it.sutra })
            val shared = analysis.links.filterIsInstance<KriyaLink.SharedParticipant>()
            assertEquals(analysis.frames.size - 1, shared.size)
            shared.forEach {
                assertEquals(main.id, it.target)
                assertEquals(setOf(main.id), it.explicitIn)
            }
        }
    }
    @Test fun `preposed passive agent requires independent semantic evidence and preserves its occurrence`() {
        val source = "राम + टा एक + अम् मुद्र् + णिच् + क्त्वा फल + औ डुकृञ् + यक् + लट् + आताम् ।"
        val analysis = analyzeWithSelectedReadings(source)
        assertTrue(analysis.blockingDiagnostics.isEmpty(), analysis.blockingDiagnostics.toString())
        assertEquals(2, analysis.frames.size)
        val main = analysis.frames.last()
        val original = main.sourceVakya.padas.filterIsInstance<SubantaPada>().single { it.sourceText == "राम+टा" }
        val agent = main.relations.single { it.participant.pada === original }
        assertEquals(Karaka.KARTR, (agent.resolution as FrameKarakaResolution.Resolved).karaka)
        assertEquals(listOf("1.4.54", "2.3.18"), agent.evidence.map { it.sutra })
        assertTrue(analysis.frames.first().relations.none { it.participant.pada === original })
        assertEquals(1, analysis.links.filterIsInstance<KriyaLink.SharedParticipant>().size)
        val unsupported = analyze(source)
        assertTrue(unsupported.blockingDiagnostics.any { it.message.startsWith("PRIOR_ACTION_PASSIVE_PREPOSED_INSTRUMENTAL") })
    }
    @Test fun `multiple prior actions do not resolve an ambiguous passive instrumental agent`() {
        val analysis = analyze("एक + अम् युज् + णिच् + क्त्वा द्वि + अम् मुद्र् + णिच् + क्त्वा राम + टा फल + सुँ डुकृञ् + यक् + लट् + त ।")
        assertEquals(3, analysis.frames.size)
        assertTrue(analysis.links.filterIsInstance<KriyaLink.SharedParticipant>().isEmpty())
        val instrumental = analysis.frames.last().relations.single { it.participant.pada.sourceText == "राम+टा" }
        assertEquals(setOf(Karaka.KARTR, Karaka.KARANA),
            (instrumental.resolution as FrameKarakaResolution.Ambiguous).candidates)
    }
    @Test fun `each prior action shares the established finite controller in a written chain`() {
        val analysis = analyze("एक + अम् युज् + णिच् + क्त्वा द्वि + अम् मुद्र् + णिच् + क्त्वा राम + सुँ फल + अम् डुकृञ् + उ + लट् + तिप् ।")
        assertEquals(3, analysis.frames.size)
        val main = analysis.frames.last()
        val shared = analysis.links.filterIsInstance<KriyaLink.SharedParticipant>()
        assertEquals(2, shared.size)
        for (prior in analysis.frames.dropLast(1)) {
            val link = shared.single { it.source == prior.id && it.target == main.id }
            assertEquals(setOf(main.id), link.explicitIn)
            assertEquals(setOf(prior.id), link.omittedIn)
            assertSame(main.sourceVakya, prior.sourceVakya)
        }
        assertEquals(2, analysis.links.filterIsInstance<KriyaLink.Purvakalika>().size)
    }
    private fun analyze(source: String) = assertNotNull(
        VyakaranamExecutionAdapter.analyzeGrammar(PaniniParser().parse(source)))

    /** Selected agent and desired-object readings for these fixtures; no general lexical claim. */
    private fun analyzeWithSelectedReadings(source: String): UktiAnalysis {
        val lexicon = object : dev.panini.vyakaranam.lexicon.VyakaranamLexicon {
            override fun findPratipadika(text: String): dev.panini.vyakaranam.lexicon.PratipadikaEntry? {
                val entry = dev.panini.vyakaranam.lexicon.StandardPratipadikaLexicon.findPratipadika(text)
                    ?: if (text in setOf("एक", "द्वि", "फल")) dev.panini.vyakaranam.lexicon.PratipadikaEntry(
                        text, setOf(dev.panini.core.Linga.PUMS)) else null
                return when (text) {
                    "राम" -> entry?.copy(semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT))
                    "एक", "द्वि", "फल" -> entry?.copy(semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT))
                    else -> entry
                }
            }
            override fun findDhatu(text: String) = dev.panini.execution.binding.DhatuCache.resolve(DhatuPrakriti(text, text))
        }
        return UktiAnalyzer(VakyaAnalyzer(PadaAnalyzer(lexicon, validatePadaCompatibility = false)))
            .analyze(PaniniParser().parse(source))
    }

    @Test fun `composite range choice retains exclusion and selection owners under one source clause`() {
        val ukti = PaniniParser().parse("क्रम + अम् वृज् + णिच् + क्त्वा सङ्ख्या + अम् चिञ् + श्नु + लोट् + सिप् ।")
        val original = ukti.grammaticalVakyas().single()
        val analysis = assertNotNull(VyakaranamExecutionAdapter.analyzeGrammar(ukti))
        assertEquals(listOf("02.0022", "05.0005"), analysis.frames.map { it.kriya?.dhatu?.id })
        assertEquals(listOf("क्रम+अम्", "सङ्ख्या+अम्"), analysis.frames.map { it.relations.single().participant.pada.sourceText })
        analysis.frames.forEach { frame ->
            assertSame(original, frame.sourceVakya)
            assertEquals(Karaka.KARMAN, (frame.relations.single().resolution as FrameKarakaResolution.Resolved).karaka)
            assertEquals(frame.id, frame.relations.single().kriyaId)
        }
    }

    @Test fun `finite action licenses the nominative controller written before or after ktva`() {
        for (source in listOf(
            "राम + सुँ एक + अम् युज् + णिच् + क्त्वा फल + अम् डुकृञ् + उ + लट् + तिप् ।",
            "एक + अम् युज् + णिच् + क्त्वा राम + सुँ फल + अम् डुकृञ् + उ + लट् + तिप् ।")) {
            val ukti = PaniniParser().parse(source)
            val original = ukti.grammaticalVakyas().single().padas.filterIsInstance<SubantaPada>()
                .single { (it.pratipadika as? MulaPratipadika)?.text == "राम" }
            val analysis = assertNotNull(VyakaranamExecutionAdapter.analyzeGrammar(ukti))
            val prior = analysis.frames[0]
            val main = analysis.frames[1]
            assertEquals(listOf(1, 2), analysis.frames.map { it.relations.size })
            val agent = main.relations.single { it.participant.pada === original }
            assertEquals(Karaka.KARTR, (agent.resolution as FrameKarakaResolution.Resolved).karaka)
            assertEquals(listOf("1.4.54", "2.3.46"), agent.evidence.map { it.sutra })
            assertTrue(prior.relations.none { it.participant.pada === original })
            val shared = analysis.links.filterIsInstance<KriyaLink.SharedParticipant>().single()
            assertEquals(setOf(main.id), shared.explicitIn)
            assertEquals(setOf(prior.id), shared.omittedIn)
        }
    }

    @Test fun `controller before ktva still governs finite number agreement`() {
        for ((ending, mismatch) in listOf("तिप्" to true, "तस्" to false)) {
            val analysis = analyze("राम + औ एक + अम् युज् + णिच् + क्त्वा फल + अम् डुकृञ् + उ + लट् + $ending ।")
            assertEquals(mismatch, analysis.frames.last().diagnostics.any {
                it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH }, ending)
            assertTrue(analysis.frames.first().diagnostics.none { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH })
        }
    }

    @Test fun `ordinary kr agent is not a prompter merely because of the lexical root`() {
        for ((derivation, sutra) in listOf("उ" to "1.4.54", "णिच्" to "1.4.55")) {
            val frame = analyze("राम + सुँ फल + अम् डुकृञ् + $derivation + लट् + तिप् ।").frames.single()
            val agent = frame.relations.single { it.participant.pada.sourceText == "राम+सुँ" }
            assertEquals(listOf(sutra, "2.3.46"), agent.evidence.map { it.sutra }, derivation)
            assertEquals(derivation == "णिच्", KarakaDesignation.HETU in agent.additionalDesignations)
        }
    }

    @Test fun `two sum print operands belong to the nonfinite printing action rather than the stop`() {
        val source = File("examples/algorithms/two_sum.pvm").readText()
        val document = PaniniParser().parseDocument(source)
        val invocation = document.items.filterIsInstance<Ukti>().flatMap { it.body.grammaticalInvocations() }
            .single { it.vakya.padas.any { pada ->
                ((pada as? AvyayaPada)?.derivation as? AvyayaKridantaDerivation)?.krtAffix == KrtAffix.KTVA
            } }
        val analysis = assertNotNull(VyakaranamExecutionAdapter.analyzeGrammar(Ukti(invocation.sourceText, body = invocation)))
        val prior = analysis.frames[0]
        val stop = analysis.frames[1]
        assertEquals(2, analysis.frames.size)
        assertFalse(prior.kriya!!.isFinite)
        assertEquals(KrtAffix.KTVA, prior.kriya!!.nonfiniteDerivation?.krtAffix)
        assertEquals("10.0510", prior.kriya!!.dhatu?.id)
        val span = assertNotNull(prior.kriya!!.analysis.pada.sourceSpan)
        assertEquals("मुद्र् + णिच् + क्त्वा", source.substring(span.start, span.endExclusive))
        assertEquals(listOf(Karaka.SAMBANDHA, Karaka.KARMAN), prior.relations.map {
            (it.resolution as FrameKarakaResolution.Resolved).karaka })
        assertEquals(listOf("1.4.49", "2.3.2"), prior.relations.last().evidence.map { it.sutra })
        assertEquals("01.0989", stop.kriya!!.dhatu?.id)
        assertTrue(stop.kriya!!.isFinite)
        assertTrue(stop.relations.isEmpty())
        assertEquals(KriyaLink.Purvakalika(prior.id, stop.id), analysis.links.filterIsInstance<KriyaLink.Purvakalika>().single())
    }

    @Test fun `word order within prior operands preserves ownership and original nominal identity`() {
        for (operands in listOf("एक + अम् द्वि + औट् च", "द्वि + औट् एक + अम् च")) {
            val ukti = PaniniParser().parse("$operands युज् + णिच् + क्त्वा फल + अम् मुद्र् + णिच् + लोट् + सिप् ।")
            val original = ukti.grammaticalVakyas().single().padas.filterIsInstance<SupBearingPada>()
            val analysis = assertNotNull(VyakaranamExecutionAdapter.analyzeGrammar(ukti))
            assertEquals(listOf(2, 1), analysis.frames.map { it.relations.size })
            analysis.frames.flatMap { it.relations }.forEach { relation ->
                assertTrue(original.any { it === relation.participant.pada })
                assertEquals(Karaka.KARMAN, (relation.resolution as FrameKarakaResolution.Resolved).karaka)
            }
        }
    }

    @Test fun `grammar does not invent an omitted display result or silently select ambiguous instrumental ownership`() {
        val analysis = analyze("एक + अम् युज् + णिच् + क्त्वा मुद्र् + णिच् + लोट् + सिप् ।")
        assertEquals(listOf(1, 0), analysis.frames.map { it.relations.size })
        val passive = analyze("राम + टा एक + अम् मुद्र् + णिच् + क्त्वा फल + सुँ डुकृञ् + यक् + लट् + त ।")
        assertTrue(passive.blockingDiagnostics.any { it.code == FrameDiagnosticCode.PRIOR_ACTION_STRUCTURE &&
            it.message.startsWith("PRIOR_ACTION_PASSIVE_PREPOSED_INSTRUMENTAL:") })
    }

    @Test fun `passive prior chain distinguishes object patient and instrumental agent under an established agent reading`() {
        for (source in listOf(
            "एक + अम् मुद्र् + णिच् + क्त्वा राम + टा फल + औ डुकृञ् + यक् + लट् + आताम् ।",
            "फल + औ एक + अम् मुद्र् + णिच् + क्त्वा राम + टा डुकृञ् + यक् + लट् + आताम् ।")) {
            val analysis = analyzeWithSelectedReadings(source)
            assertTrue(analysis.blockingDiagnostics.isEmpty(), analysis.blockingDiagnostics.toString())
            val prior = analysis.frames[0]
            val main = analysis.frames[1]
            assertEquals(dev.panini.core.Prayoga.KARMANI, main.prayoga)
            assertEquals(1, prior.relations.size)
            assertEquals(listOf("1.4.49", "2.3.2"), prior.relations.single().evidence.map { it.sutra })
            val agent = main.relations.single { it.participant.pada.sourceText == "राम+टा" }
            val patient = main.relations.single { it.participant.pada.sourceText == "फल+औ" }
            assertEquals(Karaka.KARTR, (agent.resolution as FrameKarakaResolution.Resolved).karaka)
            assertEquals(listOf("1.4.54", "2.3.18"), agent.evidence.map { it.sutra })
            assertEquals(Karaka.KARMAN, (patient.resolution as FrameKarakaResolution.Resolved).karaka)
            assertEquals(listOf("1.4.49", "2.3.46"), patient.evidence.map { it.sutra })
            assertTrue(main.diagnostics.none { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH })
            val shared = analysis.links.filterIsInstance<KriyaLink.SharedParticipant>().single()
            assertEquals(agent.participant.pada.sourceText, shared.participantSource)
            assertEquals(setOf(main.id), shared.explicitIn)
            assertEquals(setOf(prior.id), shared.omittedIn)
        }
    }

    @Test fun `passive finite number follows patient rather than shared agent`() {
        val analysis = analyzeWithSelectedReadings("एक + अम् मुद्र् + णिच् + क्त्वा राम + टा फल + औ डुकृञ् + यक् + लट् + त ।")
        assertTrue(analysis.frames.last().diagnostics.any { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH })
        assertTrue(analysis.frames.first().diagnostics.none { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH })
    }

    @Test fun `passive instrumental without an established reading does not create a shared agent`() {
        val analysis = analyze("एक + अम् मुद्र् + णिच् + क्त्वा राम + टा फल + सुँ डुकृञ् + यक् + लट् + त ।")
        val instrumental = analysis.frames.last().relations.single { it.participant.pada.sourceText == "राम+टा" }
        val unresolved = assertIs<FrameKarakaResolution.Ambiguous>(instrumental.resolution)
        assertEquals(setOf(Karaka.KARTR, Karaka.KARANA), unresolved.candidates)
        assertTrue(analysis.links.filterIsInstance<KriyaLink.SharedParticipant>().isEmpty())
    }

    @Test fun `passive omitted agent is not replaced by the nominative patient`() {
        val analysis = analyzeWithSelectedReadings("फल + सुँ एक + अम् मुद्र् + णिच् + क्त्वा डुकृञ् + यक् + लट् + त ।")
        assertTrue(analysis.blockingDiagnostics.isEmpty())
        assertEquals(listOf(1, 1), analysis.frames.map { it.relations.size })
        assertEquals(Karaka.KARMAN, (analysis.frames.last().relations.single().resolution as FrameKarakaResolution.Resolved).karaka)
        assertTrue(analysis.links.filterIsInstance<KriyaLink.SharedParticipant>().isEmpty())
    }

    @Test fun `bhave prior chain keeps its agent instrumental and its finite form impersonal`() {
        for ((ending, mismatch) in listOf("त" to false, "आताम्" to true)) {
            val analysis = analyzeWithSelectedReadings("एक + अम् मुद्र् + णिच् + क्त्वा राम + टा आसँ + यक् + लट् + $ending ।")
            val main = analysis.frames.last()
            assertEquals(dev.panini.core.Prayoga.BHAVE, main.prayoga)
            assertEquals(listOf("1.4.54", "2.3.18"), main.relations.single().evidence.map { it.sutra })
            assertEquals(mismatch, main.diagnostics.any { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH })
            assertEquals(1, analysis.links.filterIsInstance<KriyaLink.SharedParticipant>().size)
        }
    }
}
