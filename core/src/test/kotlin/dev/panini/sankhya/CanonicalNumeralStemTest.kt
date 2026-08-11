package dev.panini.sankhya

import kotlin.test.Test
import kotlin.test.assertEquals

class CanonicalNumeralStemTest {
    @Test
    fun `surface numerals before segmentation receive canonical suggestions`() {
        val source = "पञ्च + शस् षट् + शस् दश + अम् ।"

        assertEquals(
            listOf("पञ्चन्", "षष्", "दशन्"),
            CanonicalNumeralStem.suggestions(source).map { it.canonical },
        )
    }

    @Test
    fun `canonical stems rendered words and comments are left alone`() {
        val source = "पञ्चन् + शस् षष् + शस् ।\n# दश + अम्\nपञ्च"

        assertEquals(emptyList(), CanonicalNumeralStem.suggestions(source))
    }
}
