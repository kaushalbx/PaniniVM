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
}
