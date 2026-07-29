package dev.panini.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada1.AdarsanamLopaSutra
import dev.panini.ashtadhyayi.AshtadhyayiSutraValidator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AshtadhyayiSutraValidatorTest {


    @Test
    fun `accepts parsable segmented source`() {
        val issues = AshtadhyayiSutraValidator.validate(
            listOf(sutra("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।")),
        )

        assertTrue(issues.isEmpty(), issues.joinToString { it.message })
    }

    @Test
    fun `reports invalid segmented source against sutra number`() {
        val issues = AshtadhyayiSutraValidator.validate(listOf(sutra("+")))

        assertEquals(1, issues.size)
        assertEquals("1.1.1", issues.single().sutra)
        assertTrue(issues.single().message.startsWith("Invalid segmented source"))
    }

    private fun sutra(segmentedSource: String) = object : Sutra<Unit, Unit>(
        number = "1.1.1",
        text = "वृद्धिरादैच्",
        segmentedSource = segmentedSource,
        hindiExplanation = "",
        type = SutraType.SAMJNA,
        chapter = 1,
        pada = 1,
        optional = false,
        kramaValue = 110001,
        role = SutraRole.Samjna,
        action = SutraAction.SAMJNA,
        scope = SutraScope.DERIVATION,
    ) {
        override fun matches(context: Unit) = false
        override fun apply(context: Unit) = Unit
    }
}
