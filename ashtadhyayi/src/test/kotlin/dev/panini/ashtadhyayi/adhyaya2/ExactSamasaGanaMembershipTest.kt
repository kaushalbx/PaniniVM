package dev.panini.ashtadhyayi.adhyaya2

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaRuleContext
import dev.panini.ashtadhyayi.adhyaya2.pada1.KumarahShramanadibhihSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.YajakadibhishchaSutra
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.ganapatha.ShramanadiGana
import dev.panini.ganapatha.YajakadiGana
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExactSamasaGanaMembershipTest {
    private fun kumara(right: String) = SamasaRuleContext(
        listOf(SamasaPada("कुमार"), SamasaPada(right)), SamasaType.KARMADHARAYA,
    )

    private fun yajaka(right: String, vibhakti: Vibhakti = Vibhakti.SASTHI) = SamasaRuleContext(
        listOf(SamasaPada("ब्राह्मण", vibhakti), SamasaPada(right)), SamasaType.TATPURUSA,
    )

    @Test
    fun `every listed shramanadi stem remains applicable`() {
        for (member in ShramanadiGana.members) {
            assertTrue(KumarahShramanadibhihSutra.matches(kumara(member.text)), member.text)
        }
    }

    @Test
    fun `every listed yajakadi stem remains applicable only with sasthi`() {
        for (member in YajakadiGana.members) {
            for (vibhakti in Vibhakti.entries) {
                assertEquals(vibhakti == Vibhakti.SASTHI,
                    YajakadibhishchaSutra.matches(yajaka(member.text, vibhakti)), "${member.text}: $vibhakti")
            }
        }
    }

    @Test
    fun `truncated prefixes and unrelated extensions are not gana membership`() {
        for (word in listOf("", "श्रम", "प्रव्रजि", "गर्भ", "अध्याप", "श्रमणाकल्प")) {
            assertFalse(KumarahShramanadibhihSutra.matches(kumara(word)), word)
        }
        for (word in listOf("", "याज", "पूज", "परिचा", "रथ", "रथगण", "याजककल्प")) {
            assertFalse(YajakadibhishchaSutra.matches(yajaka(word)), word)
        }
    }
}
