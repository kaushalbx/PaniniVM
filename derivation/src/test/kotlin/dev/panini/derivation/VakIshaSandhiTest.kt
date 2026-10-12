package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada2.JhalamJashonteSutra
import kotlin.test.*

class VakIshaSandhiTest {
    @Test fun `vak isha voices the left pada without changing its identity`() {
        val engine = SandhiEngine()
        val result = engine.join("वाक्", "ईशः")
        assertEquals("वागीशः", engine.render(result))
        val application = result.applications.single { it.sutra == "8.2.39" }
        assertEquals("वाग्", application.after.terms.first().surface)
        assertEquals(application.before.terms.first().id, application.after.terms.first().id)
        assertEquals(application.before.terms.last(), application.after.terms.last())
    }

    @Test fun `unformed internal root is not treated as a completed left pada`() {
        val state = DerivationState(listOf(
            DerivationTerm("root", "वाक्", TermKind.DHATU),
            DerivationTerm("affix", "ईशः", TermKind.PRATYAYA),
        ))
        assertFalse(JhalamJashonteSutra.matches(state))
    }
}
