package dev.panini.derivation

import kotlin.test.Test

class NadiDerivationTest {
    @Test
    fun `derive full feminine i-stem paradigm for nadi`() = assertSubantaParadigm(
        "नदी",
        SubantaStemClass.II_STEM_FEMININE,
        """
            नदी नद्यौ नद्यः नदीम् नद्यौ नदीः नद्या नदीभ्याम् नदीभिः
            नद्यै नदीभ्याम् नदीभ्यः नद्याः नदीभ्याम् नदीभ्यः
            नद्याः नद्योः नदीनाम् नद्याम् नद्योः नदीषु
        """,
    )
}
