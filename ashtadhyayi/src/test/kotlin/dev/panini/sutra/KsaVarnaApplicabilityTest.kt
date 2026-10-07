package dev.panini.sutra

import dev.panini.ashtadhyayi.adhyaya3.pada1.ShalIgupadhadAnitahKsahSutra
import dev.panini.derivation.*
import dev.panini.shiksha.ItStatus
import kotlin.test.*

class KsaVarnaApplicabilityTest {
    private fun state(root: String, status: ItStatus? = ItStatus.ANIT, kind: TermKind = TermKind.DHATU) =
        DerivationState(listOf(
            DerivationTerm("root", root, kind, itStatus = status),
            DerivationTerm("cli", "च्लि", TermKind.PRATYAYA),
        ), stage = DerivationStage.PRATYAYA_SELECTED)

    @Test fun `ksa requires shal final ik penult and anit metadata`() {
        for (root in listOf("दिश्", "दृश्", "द्विष्", "दुह्"))
            assertTrue(ShalIgupadhadAnitahKsahSutra.matches(state(root)), root)
        for (root in listOf("लिख्", "दाश्", "दिशत्", "दिश"))
            assertFalse(ShalIgupadhadAnitahKsahSutra.matches(state(root)), root)
        assertFalse(ShalIgupadhadAnitahKsahSutra.matches(state("दिश्", ItStatus.SET)))
        assertFalse(ShalIgupadhadAnitahKsahSutra.matches(state("दिश्", null)))
        assertFalse(ShalIgupadhadAnitahKsahSutra.matches(state("दिश्", kind = TermKind.PRATIPADIKA)))
    }

    @Test fun `ksa does not inspect the bare parent behind an intervening suffix`() {
        val initial = state("दिश्")
        val derived = initial.copy(terms = listOf(initial.terms[0], DerivationTerm("nic", "णिच्", TermKind.PRATYAYA), initial.terms[1]))
        assertFalse(ShalIgupadhadAnitahKsahSutra.matches(derived))
    }

    @Test fun `ksa replacement preserves affix identity and root metadata`() {
        val initial = state("दिश्")
        val changed = ShalIgupadhadAnitahKsahSutra.apply(initial).state
        assertEquals(initial.terms[0], changed.terms[0])
        assertEquals("cli", changed.terms[1].id)
        assertEquals("क्स", changed.terms[1].surface)
        assertEquals("च्लि", changed.terms[1].sthaniProps?.upadesha)
    }
}
