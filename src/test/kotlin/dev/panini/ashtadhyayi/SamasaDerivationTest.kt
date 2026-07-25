package dev.panini.ashtadhyayi

import dev.panini.ashtadhyayi.adhyaya2.pada1.AvyayamVibhaktiSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.DvitiyaShritatitaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.PancamiBhayenaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.AnekamAnyapadartheSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.CartheDvandvahSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.ShashthiSutra
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SamasaDerivationTest {

    @Test
    fun `test AvyayamVibhaktiSutra forms Avyayibhava compound`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("avyaya", "उप", TermKind.PRATIPADIKA, upadesha = "उप"),
                DerivationTerm("pratipadika", "कृष्णम्", TermKind.PRATIPADIKA, upadesha = "कृष्णम्")
            )
        ).withSamjnas(setOf(SamjnaAssignment("avyaya", Samjna.AVYAYA)))
        assertTrue(AvyayamVibhaktiSutra.matches(state))
        val change = AvyayamVibhaktiSutra.apply(state)
        assertEquals("उपकृष्णम्", change.state.terms[0].surface)
    }

    @Test
    fun `test DvitiyaShritatitaSutra forms Dvitiya Tatpurusha compound`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("p1", "कृष्ण", TermKind.PRATIPADIKA, upadesha = "कृष्ण"),
                DerivationTerm("p2", "श्रितः", TermKind.PRATIPADIKA, upadesha = "श्रितः")
            )
        )
        assertTrue(DvitiyaShritatitaSutra.matches(state))
        val change = DvitiyaShritatitaSutra.apply(state)
        assertEquals("कृष्णश्रितः", change.state.terms[0].surface)
    }

    @Test
    fun `test PancamiBhayenaSutra forms Pancami Tatpurusha compound`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("p1", "चोर", TermKind.PRATIPADIKA, upadesha = "चोर"),
                DerivationTerm("p2", "भयम्", TermKind.PRATIPADIKA, upadesha = "भयम्")
            )
        )
        assertTrue(PancamiBhayenaSutra.matches(state))
        val change = PancamiBhayenaSutra.apply(state)
        assertEquals("चोरभयम्", change.state.terms[0].surface)
    }

    @Test
    fun `test ShashthiSutra forms Shashthi Tatpurusha compound`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("p1", "राज", TermKind.PRATIPADIKA, upadesha = "राज"),
                DerivationTerm("p2", "पुरुष", TermKind.PRATIPADIKA, upadesha = "पुरुष")
            )
        )
        assertTrue(ShashthiSutra.matches(state))
        val change = ShashthiSutra.apply(state)
        assertEquals("राजपुरुष", change.state.terms[0].surface)
    }

    @Test
    fun `test CartheDvandvahSutra forms Dvandva compound`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("p1", "राम", TermKind.PRATIPADIKA, upadesha = "राम"),
                DerivationTerm("p2", "कृष्ण", TermKind.PRATIPADIKA, upadesha = "कृष्ण")
            )
        )
        assertTrue(CartheDvandvahSutra.matches(state))
        val change = CartheDvandvahSutra.apply(state)
        assertEquals("रामकृष्ण", change.state.terms[0].surface)
    }

    @Test
    fun `test AnekamAnyapadartheSutra forms Bahuvrihi compound`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("p1", "पीत", TermKind.PRATIPADIKA, upadesha = "पीत"),
                DerivationTerm("p2", "अम्बर", TermKind.PRATIPADIKA, upadesha = "अम्बर")
            )
        )
        assertTrue(AnekamAnyapadartheSutra.matches(state))
        val change = AnekamAnyapadartheSutra.apply(state)
        assertEquals("पीतअम्बर", change.state.terms[0].surface)
    }
}
