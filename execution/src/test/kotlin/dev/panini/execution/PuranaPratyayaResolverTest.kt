package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals

class PuranaPratyayaResolverTest {

    @Test
    fun `replaces lexical and segmented ordinal padas structurally`() {
        assertEquals(
            "पञ्च + अम् द्वि + तीय + अम् च युज् + लोट् + सिप् ।",
            PuranaPratyayaResolver.replacePatterns(
                "प्रथम + अम् द्वि + तीय + अम् च युज् + लोट् + सिप् ।",
                index = 0,
                rawArgVal = "पञ्च",
            ),
        )
        assertEquals(
            "प्रथ् + अमच् + अम् सप्त + अम् च युज् + लोट् + सिप् ।",
            PuranaPratyayaResolver.replacePatterns(
                "प्रथ् + अमच् + अम् द्वि+तीय+अम् च युज् + लोट् + सिप् ।",
                index = 1,
                rawArgVal = "सप्त",
            ),
        )
    }

    @Test
    fun `does not replace cardinal padas sharing the requested value`() {
        val source = "द्वि + अम् प्रथम + अम् च युज् + लोट् + सिप् ।"
        assertEquals(
            "द्वि + अम् नव + अम् च युज् + लोट् + सिप् ।",
            PuranaPratyayaResolver.replacePatterns(source, index = 0, rawArgVal = "नव"),
        )
    }

    @Test
    fun `preserves structurally accusative argument without canonical spacing`() {
        assertEquals(
            "पञ्च+अम् द्वि + तीय + अम् च युज् + लोट् + सिप् ।",
            PuranaPratyayaResolver.replacePatterns(
                "प्रथम + अम् द्वि + तीय + अम् च युज् + लोट् + सिप् ।",
                index = 0,
                rawArgVal = "पञ्च+अम्",
            ),
        )
    }
}
