package dev.panini.derivation

import kotlin.test.Test

class NStemDerivationTest {
    @Test
    fun `derive full masculine n-stem paradigm for atman`() = assertSubantaParadigm(
        "आत्मन्",
        SubantaStemClass.N_STEM_MASCULINE,
        """
            आत्मा आत्मानौ आत्मानः आत्मानम् आत्मानौ आत्मनः आत्मना आत्मभ्याम् आत्मभिः
            आत्मने आत्मभ्याम् आत्मभ्यः आत्मनः आत्मभ्याम् आत्मभ्यः
            आत्मनः आत्मनोः आत्मनाम् आत्मनि आत्मनोः आत्मसु
        """,
    )
}
