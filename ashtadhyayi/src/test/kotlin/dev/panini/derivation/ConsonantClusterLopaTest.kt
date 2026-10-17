package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya8.pada4.HaloYamamYamiLopahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.JharoJhariSavarneSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.*
import kotlin.test.*

class ConsonantClusterLopaTest {
    private fun state(surface: String) = DerivationState(listOf(DerivationTerm("word", surface, TermKind.PRATIPADIKA)))

    @Test fun `yam is distinct from jhar and requires the corresponding following sound`() {
        assertEquals(setOf(Vyanjana.YA, Vyanjana.VA, Vyanjana.RA, Vyanjana.LA, Vyanjana.NYA,
            Vyanjana.MA, Vyanjana.NGA, Vyanjana.NNA, Vyanjana.NA),
            Vyanjana.entries.filter { Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.YAM, it) }.toSet())
        for (surface in listOf("अन्नम्", "माहात्म्यम्", "शार्ङ्गम्"))
            assertFalse(HaloYamamYamiLopahSutra.matches(state(surface)), surface)
        for (surface in listOf("प्रत्तम्", "तर्प्ता", "मूर्ध्नः"))
            assertFalse(JharoJhariSavarneSutra.matches(state(surface)), surface)
        val yam = state("आदित्य्यः")
        assertTrue(HaloYamamYamiLopahSutra.matches(yam))
        assertFalse(JharoJhariSavarneSutra.matches(yam))
        assertEquals("आदित्यः", HaloYamamYamiLopahSutra.apply(yam).state.surface)
    }

    @Test fun `internal jhar deletion preserves annotations identity and exact position`() {
        val original = state("कृँष्णर्द्धिःऽ")
        assertTrue(JharoJhariSavarneSutra.matches(original))
        assertFalse(HaloYamamYamiLopahSutra.matches(original))
        val changed = JharoJhariSavarneSutra.apply(original).state
        val term = changed.terms.single()
        assertEquals("कृँष्णर्धिःऽ", term.surface)
        assertEquals(original.terms.single().id, term.id)
        assertEquals(original.terms.single().upadesha, term.upadesha)
        assertTrue(term.phonologicalText.effectiveVarnas[1].nasalized)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, term.varnas.size)), term.orthographicSigns)
        assertEquals(6, changed.substitutions.single().sourceVarnaIndex)
        assertFalse(JharoJhariSavarneSutra.matches(changed))
    }

    @Test fun `hal target and follower may belong to three distinct carriers`() {
        val original = DerivationState(listOf(
            DerivationTerm("hal", "र्", TermKind.PRATIPADIKA),
            DerivationTerm("empty", "", TermKind.PRATYAYA),
            DerivationTerm("target", "द्", TermKind.PRATIPADIKA),
            DerivationTerm("follower", "धि", TermKind.PRATIPADIKA),
        ))
        assertTrue(JharoJhariSavarneSutra.matches(original))
        val result = JharoJhariSavarneSutra.apply(original).state
        assertEquals(original.terms.map { it.id }, result.terms.map { it.id })
        assertTrue(result.terms.single { it.id == "target" }.varnas.isEmpty())
        assertEquals("target", result.substitutions.single().targetId)
        assertEquals(0, result.substitutions.single().sourceVarnaIndex)
    }

    @Test fun `both optional internal branches are retained without cycles`() {
        val engine = SandhiEngine()
        assertEquals(setOf("आदित्यःअत्र", "आदित्य्यःअत्र"), engine.joinAll("आदित्य्यः", "अत्र").map(engine::render).toSet())
        assertEquals(setOf("कृष्णर्धिःअत्र", "कृष्णर्द्धिःअत्र"), engine.joinAll("कृष्णर्द्धिः", "अत्र").map(engine::render).toSet())
        for (result in engine.joinAll("कृष्णर्द्धिः", "अत्र")) {
            assertEquals(DerivationStage.FINAL, result.final.stage)
            assertTrue(result.applications.count { it.sutra == "8.4.65" } <= 1)
        }
    }

    @Test fun `tinganta caller can explicitly select lopa or retention without root exceptions`() {
        val request = TingantaDerivationRequest("रुधिँर्", dev.panini.core.Vacana.EKAVACANA,
            dev.panini.core.Purusha.PRATHAMA, dev.panini.core.Lakara.LAT,
            pada = dev.panini.core.PadaType.ATMANEPADA)
        val lopa = TingantaEngine().derive(request)
        val retained = TingantaEngine(config = DerivationConfig(optionalRulePolicy = OptionalRulePolicy.CUSTOM,
            optionalRuleSelector = { it != "8.4.65" })).derive(request)
        assertEquals("रुन्धे", lopa.final.surface)
        assertEquals("रुन्द्धे", retained.final.surface)
        assertTrue(lopa.applications.any { it.sutra == "8.4.65" })
        assertFalse(retained.applications.any { it.sutra == "8.4.65" })
        assertEquals(DerivationStage.FINAL, retained.final.stage)
        assertEquals(DerivationStage.FINAL, lopa.final.stage)
    }

    @Test fun `separate clusters can independently retain the earlier and delete the later`() {
        val engine = SandhiEngine()
        val results = engine.joinAll("सार्द्धम्", "अर्ज्जनम्")
        assertEquals(setOf("सार्धमर्जनम्", "सार्धमर्ज्जनम्", "सार्द्धमर्जनम्", "सार्द्धमर्ज्जनम्"),
            results.map(engine::render).toSet())
        assertTrue(results.all { it.final.stage == DerivationStage.FINAL })
        assertTrue(results.all { result -> result.applications.count { it.sutra == "8.4.65" } <= 2 })
        assertEquals(setOf("कार्यमार्यम्", "कार्यमार्य्यम्", "कार्य्यमार्यम्", "कार्य्यमार्य्यम्"),
            engine.joinAll("कार्य्यम्", "आर्य्यम्").map(engine::render).toSet())
    }
}
