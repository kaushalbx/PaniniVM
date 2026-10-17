package dev.panini.derivation

import dev.panini.shiksha.*
import kotlin.test.*

class CompletedPadaVisargaTest {
    private val engine = SandhiEngine()
    private fun pada(id: String, surface: String, source: VisargaSource? = null) =
        SandhiEngine.Pada(DerivationTerm(id, surface, TermKind.PRATIPADIKA, upadesha = surface),
            visargaSource = source)

    @Test fun `derived nominal pada retains established ru at its next boundary`() {
        val derived = SubantaEngine().derive(SubantaDerivationRequest("राम", dev.panini.core.Vibhakti.PRATHAMA,
            dev.panini.core.Vacana.EKAVACANA, dev.panini.core.Linga.PUMS))
        val input = engine.padaFromDerivation(derived, "derived-ramah")
        val term = input.term
        assertEquals(Ayogavaha.VISARGA, term.varnas.last())
        assertTrue(term.hasRutvaAt(term.varnas.lastIndex))
        assertSame(derived, input.sourceDerivation)
        val joined = engine.join(input, pada("next", "अस्ति"))
        assertEquals("रामोऽस्ति", engine.render(joined))
        assertTrue(joined.applications.any { it.sutra == "6.1.113" })
        assertTrue(joined.applications.none { it.sutra == "8.2.66" })
        assertEquals(VisargaSource.ESTABLISHED_RU, joined.initial.terms.first().completedPadaVisargaInput?.source)
    }

    @Test fun `handing off ru after a khar boundary preserves original source record`() {
        for ((khar, allophone) in listOf("क" to Ayogavaha.JIHVAMULIYA, "प" to Ayogavaha.UPADHMANIYA)) {
            val results = engine.joinAll(pada("left", "रामः", VisargaSource.S), pada("khar", khar))
            assertEquals(setOf(Ayogavaha.VISARGA, allophone), results.map { it.final.terms.first().varnas.last() }.toSet())
            for (first in results) {
                val left = first.final.terms.first()
                assertTrue(left.hasRutvaAt(left.varnas.lastIndex))
                val next = engine.join(SandhiEngine.Pada(left), pada("next", "भवति"))
                assertEquals("रामोभवति", engine.render(next))
                assertEquals(left.completedPadaVisargaInput, next.initial.terms.first().completedPadaVisargaInput)
                assertTrue(next.applications.none { it.sutra == "8.2.66" })
            }
        }
    }

    @Test fun `established ru input cannot be asserted from spelling alone`() {
        assertFailsWith<IllegalArgumentException> {
            engine.join(pada("left", "रामः", VisargaSource.ESTABLISHED_RU), pada("next", "अस्ति"))
        }
        val plain = engine.join(pada("left", "रामः"), pada("next", "अस्ति"))
        assertTrue(plain.initial.terms.first().rutvaOccurrences.isEmpty())
        assertTrue(plain.initial.terms.first().completedPadaVisargaInput == null)
    }

    @Test fun `morphological handoff cannot collapse an arbitrary completed sandhi phrase`() {
        val phrase = engine.join(pada("left", "देव"), pada("right", "इति"))
        assertFailsWith<IllegalArgumentException> { engine.padaFromDerivation(phrase, "not-one-word") }
    }

    @Test fun `handoff scopes internal prefix and root classifications to their source derivation`() {
        // Structural fixture, not a claim of a grammatical derivation trace.
        val prefix = DerivationTerm("prefix", "प्र", TermKind.PRATIPADIKA)
        val root = DerivationTerm("root", "भवति", TermKind.DHATU, upadesha = "भू")
        val state = DerivationState(listOf(prefix, root), stage = DerivationStage.FINAL,
            context = DerivationalContext(rupa = Rupa(lakara = dev.panini.core.Lakara.LAT)),
            samjnas = setOf(SamjnaAssignment(prefix.id, Samjna.UPASARGA),
                SamjnaAssignment(root.id, Samjna.DHATU), SamjnaAssignment(root.id, Samjna.PADA)))
        val result = DerivationResult(state, state, emptyList(), emptyList())
        val input = engine.padaFromDerivation(result, "completed-verb")
        assertEquals(prefix.varnas + root.varnas, input.term.varnas)
        assertEquals(root.upadesha, input.term.upadesha)
        assertEquals(TermKind.PRATIPADIKA, input.term.kind)
        assertEquals(setOf(Samjna.PADA), input.samjnas)
        assertSame(result, input.sourceDerivation)
        assertEquals(state.samjnas, input.sourceDerivation?.final?.samjnas)
    }

    @Test fun `explicit s source enters genuine rutva and utva lifecycle`() {
        for ((right, expected, rule) in listOf(Triple("अस्ति", "रामोऽस्ति", "6.1.113"),
            Triple("भवति", "रामोभवति", "6.1.114"), Triple("इति", "राम इति", "8.3.19"))) {
            val result = engine.join(pada("left", "रामः", VisargaSource.S), pada("right", right))
            assertEquals(expected, engine.render(result))
            assertTrue(result.applications.any { it.sutra == "8.2.66" })
            assertTrue(result.applications.any { it.sutra == rule })
            assertEquals(CompletedPadaVisargaInput("रामः", VisargaSource.S),
                result.final.allEffectiveTerms.single { it.id == "left" }.completedPadaVisargaInput)
            assertTrue(result.initial.substitutions.isEmpty())
            assertTrue(result.applications.none { it.sutra == "completed-pada-input" })
        }
    }

    @Test fun `explicit lexical r is not mistaken for rutva`() {
        for (right in listOf("अस्ति", "भवति")) {
            val result = engine.join(pada("left", "पुनः", VisargaSource.R), pada("right", right))
            assertEquals("पुनर्$right".toVarnas(), result.final.terms.flatMap { it.varnas })
            assertTrue(result.applications.none { it.sutra in setOf("8.2.66", "6.1.113", "6.1.114", "8.3.17") })
            assertEquals(CompletedPadaVisargaInput("पुनः", VisargaSource.R), result.final.terms.first().completedPadaVisargaInput)
        }
    }

    @Test fun `unspecified written input is not assigned an invented source`() {
        val result = engine.join("रामः", "अस्ति")
        assertTrue(result.initial.terms.all { it.completedPadaVisargaInput == null })
        assertTrue(result.applications.none { it.sutra == "8.2.66" })
    }

    @Test fun `lexical r takes visarga before khar and at explicit pause`() {
        val beforeKhar = engine.join(pada("left", "पुनः", VisargaSource.R), pada("right", "करोति"),
            DerivationConfig(optionalRulePolicy = OptionalRulePolicy.SKIP_ALL))
        assertTrue(beforeKhar.applications.any { it.sutra == "8.3.15" })
        assertEquals("पुनᳵकरोति", engine.render(beforeKhar))
        assertEquals(setOf("पुनःकरोति", "पुनᳵकरोति"), engine.joinAll(
            pada("left", "पुनः", VisargaSource.R), pada("right", "करोति")).map(engine::render).toSet())
        val paused = engine.join(pada("left", "अत्र"), pada("right", "पुनः", VisargaSource.R),
            finalBoundary = SpeechBoundary.AVASANA)
        assertEquals("अत्रपुनः", engine.render(paused))
        assertTrue(paused.applications.any { it.sutra == "8.3.15" })
        val continuing = engine.join(pada("left", "अत्र"), pada("right", "पुनः", VisargaSource.R))
        assertEquals("अत्रपुनः", engine.render(continuing))
        assertNull(continuing.initial.terms.last().completedPadaVisargaInput)
        assertTrue(continuing.applications.none { it.sutra == "8.3.15" })
    }

    @Test fun `decoding preserves annotations signs ownership and original identity`() {
        val input = DerivationTerm("word", "राँमःऽ", TermKind.PRATIPADIKA, createdBySutra = "fixture",
            completedPadaOrigins = List(5) { setOf("word") })
        val before = DerivationState(listOf(input))
        val after = before.withCompletedPadaVisargaSource("word", VisargaSource.S)
        val term = after.terms.single()
        assertEquals("राँमस्ऽ", term.surface)
        assertEquals(input.completedPadaOrigins, term.completedPadaOrigins)
        assertEquals(input.upadesha, term.upadesha)
        assertEquals(input.createdBySutra, term.createdBySutra)
        assertTrue(term.phonologicalText.effectiveVarnas[1].nasalized)
        assertEquals(input.surface, term.completedPadaVisargaInput?.surface)
        assertTrue(after.substitutions.isEmpty())
        assertFailsWith<IllegalArgumentException> { after.withCompletedPadaVisargaSource("word", VisargaSource.S) }
        assertFailsWith<IllegalArgumentException> {
            DerivationState(listOf(input.copy(surface = "राम"))).withCompletedPadaVisargaSource("word", VisargaSource.S)
        }
        assertFailsWith<IllegalArgumentException> {
            DerivationState(listOf(input.copy(itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)))
                .withCompletedPadaVisargaSource("word", VisargaSource.S)
        }
    }

    @Test fun `completed lexical repha predicate does not broaden internal stem domain`() {
        val internal = DerivationState(listOf(DerivationTerm("root", "पुनर्", TermKind.DHATU),
            DerivationTerm("suffix", "क", TermKind.PRATYAYA)), stage = DerivationStage.PADA_FORMED)
        assertFalse(dev.panini.ashtadhyayi.adhyaya8.pada3.KharavasanayorVisarjaniyahSutra.matches(internal))
    }
}
