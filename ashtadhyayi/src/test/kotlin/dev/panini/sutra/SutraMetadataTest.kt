package dev.panini.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada1.AdengGunaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.SadhakatamamKaranamSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.ChaturthiSampradaneSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.AdGunaSutra
import kotlin.test.Test
import kotlin.test.assertEquals

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

    @Test
    fun `karaka and vibhakti rules use the common sutra model`() {
        assertEquals(SutraScope.VAKYA, SadhakatamamKaranamSutra.scope)
        assertEquals(SutraRole.Samjna, SadhakatamamKaranamSutra.role)
        assertEquals(SutraAction.SAMJNA, SadhakatamamKaranamSutra.action)
        assertEquals(setOf("1.4.23"), SadhakatamamKaranamSutra.adhikara)
        assertEquals(140042, SadhakatamamKaranamSutra.kramaValue)

        assertEquals(SutraScope.VAKYA, ChaturthiSampradaneSutra.scope)
        assertEquals(SutraRole.Vidhi, ChaturthiSampradaneSutra.role)
        assertEquals(SutraAction.VIDHI, ChaturthiSampradaneSutra.action)
        assertEquals(setOf("2.3.1"), ChaturthiSampradaneSutra.adhikara)
        assertEquals(setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE), ChaturthiSampradaneSutra.inputs)
    }
}
