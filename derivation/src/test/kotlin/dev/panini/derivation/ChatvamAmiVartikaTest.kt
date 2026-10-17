package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya8.pada4.ShashChoAtiSutra
import dev.panini.ashtadhyayi.vartika.ChatvamAmiVartika
import dev.panini.derivation.sutra.AshtadhyayiRuntimeGrantha
import dev.panini.derivation.sutra.DerivationAvastha
import dev.panini.derivation.sutra.DerivationSutraEffectInterpreter
import dev.panini.shiksha.*
import dev.panini.sutra.runtime.*
import kotlin.test.*

class ChatvamAmiVartikaTest {
    private fun state(right: String, pada: Boolean = true): DerivationState = DerivationState(
        listOf(DerivationTerm("left", "तच्", TermKind.PRATIPADIKA),
            DerivationTerm("right", right, TermKind.PRATIPADIKA)),
        samjnas = if (pada) setOf(SamjnaAssignment("left", Samjna.PADA)) else emptySet())

    @Test fun `am extension keeps canonical at and pada qualification distinct`() {
        for (right in listOf("श्लोकेन", "श्मश्रुणा")) {
            assertTrue(ChatvamAmiVartika.matches(state(right)))
            assertFalse(ShashChoAtiSutra.matches(state(right)))
            assertFalse(ChatvamAmiVartika.matches(state(right, pada = false)))
        }
        assertTrue(ShashChoAtiSutra.matches(state("शेते")))
        assertFalse(ShashChoAtiSutra.matches(state("शेते", pada = false)))
        assertFalse(ChatvamAmiVartika.matches(state("शेते")))
        assertFalse(ChatvamAmiVartika.matches(state("श्क")))
    }

    @Test fun `attested l and m cases retain both optional outputs and owned traces`() {
        val engine = SandhiEngine()
        for (right in listOf("श्लोकेन", "श्मश्रुणा")) {
            val results = engine.joinAll("तत्", right)
            val transformed = if (right == "श्लोकेन") "तच्छ्लोकेन" else "तच्छ्मश्रुणा"
            val retained = if (right == "श्लोकेन") "तच्श्लोकेन" else "तच्श्मश्रुणा"
            assertEquals(setOf(transformed, retained), results.map(engine::render).toSet())
            val applied = results.single { engine.render(it) == transformed }.applications.single { it.sutra == ChatvamAmiVartika.sutra }
            assertEquals(ChatvamAmiVartika.source, applied.source)
            assertTrue(results.all { it.final.stage == DerivationStage.FINAL })
            assertTrue(results.all { result -> result.applications.none { it.sutra == "8.4.63" } })
        }
    }

    @Test fun `extension is registered without fabricating a canonical slot and source round trips`() {
        val rule = ChatvamAmiVartika
        assertSame(rule, Ashtadhyayi.registry.require(rule.sutra))
        assertEquals(1, Ashtadhyayi.registry.sutras.count { it.number == rule.sutra })
        assertTrue(Ashtadhyayi.executableSutras.any { it === rule })
        assertTrue(Ashtadhyayi.cataloguedSutras.none { it.number == rule.sutra })
        assertNull(Ashtadhyayi.patha.get(rule.sutra))
        assertEquals(Ashtadhyayi.cataloguedSutras.size, Ashtadhyayi.pathitaCount)
        assertTrue(Ashtadhyayi.catalogIssues.none { it.sutra == rule.sutra })
        assertSame(ShashChoAtiSutra, Ashtadhyayi.registry.require("8.4.63"))
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single { it.id.value == rule.sutra }
        assertEquals("8.4.63", assertIs<SutraSource.Vartika>(runtime.source).parentSutra)
        val blueprint = runtime.toBlueprint()
        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(SutraBlueprintTextCodec.encode(blueprint))
        assertEquals(blueprint, assertIs<SutraBlueprintTextDecoding.Success>(SutraBlueprintTextCodec.decode(encoded.text)).blueprint)
        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("owned-vartika", listOf(runtime)), DerivationAvastha(state("श्लोकेन"))))
        assertEquals("तच्छ्लोकेन", result.state.derivation.surface)
    }

    @Test fun `exact sha substitution preserves following nasalization signs and identities`() {
        val original = state("श्लोँकेनऽ")
        val changed = ChatvamAmiVartika.apply(original).state
        assertEquals(original.terms.first(), changed.terms.first())
        val term = changed.terms.last()
        assertEquals("छ्लोँकेनऽ", term.surface)
        assertEquals(original.terms.last().id, term.id)
        assertEquals(original.terms.last().upadesha, term.upadesha)
        assertEquals(0, changed.substitutions.single().sourceVarnaIndex)
        assertTrue(term.phonologicalText.effectiveVarnas.any { it.varna == Svara.O && it.nasalized })
    }
}
