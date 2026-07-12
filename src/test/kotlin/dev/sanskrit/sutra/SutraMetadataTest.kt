package dev.sanskrit.sutra

import kotlin.test.Test
import kotlin.test.assertEquals
import dev.sanskrit.ashtadhyayi.adhyaya6.pada1.AdGunaSutra
import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.AdengGunaSutra

class SutraTest {
    @Test
    fun `executable derivation sutra exposes direct rule fields`() {
        assertEquals(SutraAction.ADESHA, AdGunaSutra.action)
        assertEquals(SutraScope.DERIVATION, AdGunaSutra.scope)
        assertEquals(SutraRole.Vidhi, AdGunaSutra.role)
    }

    @Test
    fun `base sutra carries execution context and learning examples`() {
        assertEquals(setOf(SutraInput.VARNA), AdengGunaSutra.inputs)
        assertEquals(SutraStage.SAMJNA, AdengGunaSutra.stage)
        assertEquals("ए [गुण]", AdengGunaSutra.examples.single().output)
        assertEquals("{sutra} assigns गुण संज्ञा to {target}.", AdengGunaSutra.traceTemplate)
    }

    @Test
    fun `base sutra owns its identity fields`() {
        assertEquals("6.1.87", AdGunaSutra.number)
        assertEquals("आद्गुणः", AdGunaSutra.text)
        assertEquals(610087, AdGunaSutra.kramaValue)
    }
}
