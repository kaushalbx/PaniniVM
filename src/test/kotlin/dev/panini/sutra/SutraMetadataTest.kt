package dev.panini.sutra

import kotlin.test.Test
import kotlin.test.assertEquals
import dev.panini.ashtadhyayi.adhyaya6.pada1.AdGunaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.AdengGunaSutra

class SutraTest {
    @Test
    fun `sutras expose identity execution and learning metadata`() {
        assertEquals(SutraAction.ADESHA, AdGunaSutra.action)
        assertEquals(SutraScope.DERIVATION, AdGunaSutra.scope)
        assertEquals(SutraRole.Vidhi, AdGunaSutra.role)
        assertEquals("6.1.87", AdGunaSutra.number)
        assertEquals("आद्गुणः", AdGunaSutra.text)
        assertEquals(610087, AdGunaSutra.kramaValue)
        assertEquals(setOf(SutraInput.VARNA), AdengGunaSutra.inputs)
        assertEquals(SutraStage.SAMJNA, AdengGunaSutra.stage)
        assertEquals("ए [गुण]", AdengGunaSutra.examples.single().output)
        assertEquals("{sutra} assigns गुण संज्ञा to {target}.", AdengGunaSutra.traceTemplate)
    }
}
