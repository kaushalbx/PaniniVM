package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada3.*
import dev.panini.shiksha.*
import kotlin.test.*

class KupuVisargaSandhiTest {
    private val engine = SandhiEngine()

    @Test fun `four voiceless kupu followers have allophone and visarga outcomes only`() {
        for ((right, allophone) in listOf("करोति" to Ayogavaha.JIHVAMULIYA, "खनति" to Ayogavaha.JIHVAMULIYA,
            "पचति" to Ayogavaha.UPADHMANIYA, "फलति" to Ayogavaha.UPADHMANIYA)) {
            val results = engine.joinAll("वृक्षः", right)
            assertEquals(setOf("वृक्ष${allophone.devanagari}$right", "वृक्षः$right"), results.map(engine::render).toSet())
            assertEquals(setOf(allophone, Ayogavaha.VISARGA), results.map { it.final.terms.first().varnas.last() }.toSet())
            assertTrue(results.all { r -> r.applications.any { it.sutra == "8.3.37" } })
            assertTrue(results.all { r -> r.applications.none { it.sutra == "8.3.34" } })
            assertTrue(results.all { r -> r.events.any { it is DerivationEvent.BranchCreated && it.sutra == "8.3.37" && it.branchCount == 2 } })
        }
    }

    @Test fun `ca retention is a prescribed alternative not optional rule omission`() {
        val retained = engine.join("वृक्षः", "करोति", DerivationConfig(alternativeChoices = mapOf("8.3.37" to 1)))
        assertEquals("वृक्षःकरोति", engine.render(retained))
        assertEquals(listOf("8.3.37"), retained.applications.map { it.sutra })
        assertTrue(0 in retained.final.nonAdvancingTripadiApplications)
        val ordinarySkip = engine.join("वृक्षः", "करोति", DerivationConfig(optionalRulePolicy = OptionalRulePolicy.SKIP_ALL))
        assertEquals(Ayogavaha.JIHVAMULIYA, ordinarySkip.final.terms.first().varnas.last())
    }

    @Test fun `sharpare exception excludes both allophones`() {
        for ((left, right) in listOf("वासः" to "क्षौमम्", "अद्भिः" to "प्सातम्")) {
            val results = engine.joinAll(left, right)
            assertEquals(setOf(left + right), results.map(engine::render).toSet())
            assertTrue(results.all { r -> r.applications.any { it.sutra == "8.3.35" } })
            assertTrue(results.all { r -> r.applications.none { it.sutra == "8.3.37" } })
        }
    }

    @Test fun `voiced and nasal ku pu members are outside inherited khar`() {
        for (right in listOf("ग", "घ", "ङ", "ब", "भ", "म")) {
            val state = DerivationState(listOf(DerivationTerm("left", "अः", TermKind.PRATIPADIKA),
                DerivationTerm("right", right, TermKind.PRATIPADIKA)))
            assertFalse(KupvohKapauCaSutra.matches(state), right)
        }
    }

    @Test fun `allophone preserves annotations signs identity and marker history`() {
        val left = DerivationTerm("left", "अँःऽ", TermKind.PRATIPADIKA, createdBySutra = "4.1.2",
            sourceSuffixUpadeshas = setOf("क्त"), itMarkerProvenance = setOf(
                ItMarkerProvenance(dev.panini.core.ItMarker.U, "1.3.2", "ुँ")))
        val state = DerivationState(listOf(left, DerivationTerm("right", "क", TermKind.PRATIPADIKA)))
        val changed = KupvohKapauCaSutra.apply(state).state
        val term = changed.terms.first()
        assertEquals(listOf(Svara.A, Ayogavaha.JIHVAMULIYA), term.varnas)
        assertTrue(term.phonologicalText.effectiveVarnas.first().nasalized)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), term.orthographicSigns)
        assertEquals(left.id, term.id)
        assertEquals(left.upadesha, term.upadesha)
        assertEquals(left.createdBySutra, term.createdBySutra)
        assertEquals(left.itMarkerProvenance, term.itMarkerProvenance)
        assertEquals(left.sourceSuffixUpadeshas, term.sourceSuffixUpadeshas)
        assertEquals(1, changed.substitutions.single().sourceVarnaIndex)
    }

    @Test fun `two prescribed kupu choices branch independently`() {
        val state = DerivationState(listOf(DerivationTerm("a", "अः", TermKind.PRATIPADIKA),
            DerivationTerm("b", "कः", TermKind.PRATIPADIKA), DerivationTerm("c", "प", TermKind.PRATIPADIKA)))
        val results = DerivationEngine(listOf(KupvohKapauCaSutra, VisarjaniyasyaSahSutra)).deriveAll(
            state, DerivationConfig(computeSvara = false))
        assertEquals(4, results.size)
        assertEquals(setOf(
            listOf(Ayogavaha.JIHVAMULIYA, Ayogavaha.UPADHMANIYA), listOf(Ayogavaha.JIHVAMULIYA, Ayogavaha.VISARGA),
            listOf(Ayogavaha.VISARGA, Ayogavaha.UPADHMANIYA), listOf(Ayogavaha.VISARGA, Ayogavaha.VISARGA)),
            results.map { it.final.terms.take(2).map { t -> t.varnas.last() } }.toSet())
        assertTrue(results.all { it.applications.size == 2 })
        for (result in results) {
            assertEquals(result.final.terms.take(2).withIndex().filter { it.value.varnas.last() == Ayogavaha.VISARGA }
                .map { it.index }.toSet(), result.final.nonAdvancingTripadiApplications)
        }
    }

    @Test fun `retention and allophone choices preserve independent earlier s work`() {
        val state = DerivationState(listOf(DerivationTerm("a", "अः", TermKind.PRATIPADIKA),
            DerivationTerm("b", "कः", TermKind.PRATIPADIKA), DerivationTerm("c", "तत्र", TermKind.PRATIPADIKA)))
        val results = DerivationEngine(listOf(KupvohKapauCaSutra, VisarjaniyasyaSahSutra)).deriveAll(
            state, DerivationConfig(computeSvara = false))
        assertEquals(2, results.size)
        assertTrue(results.all { it.final.terms[1].surface == "कस्" })
        assertTrue(results.all { it.applications.map { a -> a.sutra } == listOf("8.3.34", "8.3.37") })
    }
}
