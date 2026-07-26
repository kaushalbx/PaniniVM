package dev.panini.derivation

import kotlin.test.Test

class FeminineIStemDerivationTest {
    @Test
    fun `derive full feminine i-stem paradigm for mati`() = assertSubantaParadigm(
        "मति",
        SubantaStemClass.I_STEM_FEMININE,
        """
            मतिः मती मतयः मतिम् मती मतीः मत्या मतिभ्याम् मतिभिः
            मत्यै मतिभ्याम् मतिभ्यः मत्याः मतिभ्याम् मतिभ्यः
            मत्याः मत्योः मतीनाम् मत्याम् मत्योः मतिषु
        """,
    )

    @Test
    fun `derive full feminine i-stem paradigm for bhumi`() = assertSubantaParadigm(
        "भूमि",
        SubantaStemClass.I_STEM_FEMININE,
        """
            भूमिः भूमी भूमयः भूमिम् भूमी भूमीः भूम्या भूमिभ्याम् भूमिभिः
            भूम्यै भूमिभ्याम् भूमिभ्यः भूम्याः भूमिभ्याम् भूमिभ्यः
            भूम्याः भूम्योः भूमीनाम् भूम्याम् भूम्योः भूमिषु
        """,
    )
}
