package dev.panini.ashtadhyayi.adhyaya2

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaRuleContext
import dev.panini.ashtadhyayi.adhyaya2.pada2.RajadantadisuSutra
import dev.panini.core.SamasaType
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RajadantadiStemIdentityTest {
    private fun context(left: String, right: String) = SamasaRuleContext(
        listOf(SamasaPada(left), SamasaPada(right)), SamasaType.TATPURUSA,
    )

    @Test
    fun `exact compound stems retain lexical membership`() {
        for ((left, right) in listOf("राज" to "दन्त", "लिप्त" to "वासित", "स्नातक" to "राजन्",
            "भार्या" to "पति", "गुण" to "वृद्धि")) {
            assertTrue(RajadantadisuSutra.matches(context(left, right)), "$left + $right")
        }
    }

    @Test
    fun `partial compounds and unrelated words cannot match list prefixes`() {
        for ((left, right) in listOf("राज" to "द", "राज" to "पुरुष", "लिप्त" to "वासि", "गुण" to "वृद्ध")) {
            assertFalse(RajadantadisuSutra.matches(context(left, right)), "$left + $right")
        }
        assertFalse(RajadantadisuSutra.matches(context("राज", "दन्त").copy(samasaType = SamasaType.BAHUVRIHI)))
    }
}
