package dev.panini.ashtadhyayi

import dev.panini.ashtadhyayi.adhyaya1.pada3.LasakvataddhiteSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.TasyaLopahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.IgupadhaJnyaPriKirahKahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.SupiSthahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.AbhikshnyeNamulCaSutra
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.KrdantaDerivationRequest
import dev.panini.derivation.KrdantaEngine
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KrtSutraDerivationTest {
    private fun root(upadesha: String) = DerivationTerm("dhatu", upadesha, TermKind.DHATU, upadesha = upadesha)

    private fun requesting(samjna: Samjna, vararg terms: DerivationTerm, context: DerivationalContext = DerivationalContext()) =
        DerivationState(terms = terms.toList(), samjnas = setOf(SamjnaAssignment("dhatu", samjna)), context = context)

    @Test
    fun `3_4_22 gives namul only when repetition is asked for`() {
        val state = requesting(Samjna.NAMUL, root("पा"))
        assertTrue(AbhikshnyeNamulCaSutra.matches(state))
        val added = AbhikshnyeNamulCaSutra.apply(state).state.allEffectiveTerms.last()
        assertEquals("णमुल्", added.upadesha)
        assertEquals("णमुँल्", added.surface)
        assertTrue(added.itProcessingPending)
        // A plain भाव request is not ābhīkṣṇya: ल्युट् or घञ् serve it.
        assertFalse(AbhikshnyeNamulCaSutra.matches(DerivationState(terms = listOf(root("पा")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA))))
        assertFalse(AbhikshnyeNamulCaSutra.matches(requesting(Samjna.GHAN, root("पा"))))
    }

    @Test
    fun `3_1_135 gives ka to igupadha roots and the three it names`() {
        for (upadesha in listOf("बुध्", "ज्ञा", "प्री", "कॄ")) {
            val state = requesting(Samjna.KA, root(upadesha))
            assertTrue(IgupadhaJnyaPriKirahKahSutra.matches(state), upadesha)
            val added = IgupadhaJnyaPriKirahKahSutra.apply(state).state.allEffectiveTerms.last()
            assertEquals("क", added.upadesha)
            assertEquals("3.1.135", added.createdBySutra)
            assertEquals(ItProcessingPhase.RAW_UPADESHA, added.itProcessingPhase)
            val designated = LasakvataddhiteSutra.apply(IgupadhaJnyaPriKirahKahSutra.apply(state).state).state
            assertEquals("1.3.8", designated.terms.last().itDesignations.single().sutra)
            assertEquals("अ", TasyaLopahSutra.apply(designated).state.terms.last().surface)
        }
        // पठ् has अ as its penultimate, and nothing asks for क without a request.
        assertFalse(IgupadhaJnyaPriKirahKahSutra.matches(requesting(Samjna.KA, root("पठ्"))))
        assertFalse(IgupadhaJnyaPriKirahKahSutra.matches(DerivationState(terms = listOf(root("बुध्")))))
        assertFalse(IgupadhaJnyaPriKirahKahSutra.matches(requesting(Samjna.NVUL, root("बुध्"))))
    }

    @Test
    fun `3_2_4 gives ka to stha after a subanta upapada`() {
        val upapada = DerivationTerm("upapada", "गृह", TermKind.PRATIPADIKA, upadesha = "गृह")
        val state = requesting(Samjna.KA, upapada, root("स्था"))
        assertTrue(SupiSthahSutra.matches(state))
        val added = SupiSthahSutra.apply(state).state.allEffectiveTerms.last()
        assertEquals("क", added.upadesha)
        assertEquals("3.2.4", added.createdBySutra)
        // Without an upapada, or with only an upasarga, सुपि does not hold.
        assertFalse(SupiSthahSutra.matches(requesting(Samjna.KA, root("स्था"))))
        val upasarga = DerivationTerm("upasarga_1", "प्र", TermKind.PRATIPADIKA, upadesha = "प्र")
        assertFalse(SupiSthahSutra.matches(DerivationState(terms = listOf(upasarga, root("स्था")),
            samjnas = setOf(SamjnaAssignment("dhatu", Samjna.KA), SamjnaAssignment(upasarga.id, Samjna.UPASARGA)))))
        assertFalse(SupiSthahSutra.matches(DerivationState(terms = listOf(upapada, root("स्था")))))
    }

    @Test
    fun `ka and namul derive whole forms`() {
        val engine = KrdantaEngine()
        fun derive(dhatu: String, samjna: Samjna) = engine.derive(KrdantaDerivationRequest(dhatu, samjna))
        val budha = derive("बुध्", Samjna.KA)
        assertTrue(budha.applications.any { it.sutra == "3.1.135" }, budha.applications.map { it.sutra }.toString())
        assertEquals("बुध", budha.final.surface)
        val karam = derive("कृ", Samjna.NAMUL)
        assertTrue(karam.applications.any { it.sutra == "3.4.22" }, karam.applications.map { it.sutra }.toString())
        assertEquals("कारम्", karam.final.surface)
    }

    @Test
    fun `connected krt rules are in the executable grammar`() {
        val numbers = Ashtadhyayi.executableSutras.mapNotNull { (it as? dev.panini.sutra.Sutra<*, *>)?.number }.toSet()
        for (number in listOf("2.4.74", "3.1.135", "3.2.4", "3.4.22")) assertTrue(number in numbers, number)
    }
}
