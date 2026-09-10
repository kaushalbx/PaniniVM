package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VasransudhvasvanaduhamDahSutraTest {
    @Test
    fun `applies final d substitution throughout canonical domain`() {
        val expected = mapOf(
            "विद्वस्" to "विद्वद्",
            "मेधावस्" to "मेधावद्",
            "स्रंस्" to "स्रंद्",
            "ध्वंस्" to "ध्वंद्",
            "अनडुह्" to "अनडुद्",
        )

        expected.forEach { (source, replacement) ->
            val context = context(source)
            assertTrue(VasransudhvasvanaduhamDahSutra.matches(context), source)
            val result: SamasaRuleResult.Formed = VasransudhvasvanaduhamDahSutra.apply(context)
            assertEquals(mapOf(0 to replacement), result.memberEdits)
            assertEquals(replacement + "आलय", result.compoundStem)
        }
    }

    @Test
    fun `does not treat every s-final stem as vas`() {
        assertFalse(VasransudhvasvanaduhamDahSutra.matches(context("मनस्")))
    }

    private fun context(first: String) = SamasaRuleContext(
        padas = listOf(SamasaPada(first), SamasaPada("आलय")),
        samasaType = SamasaType.TATPURUSA,
    )
}
