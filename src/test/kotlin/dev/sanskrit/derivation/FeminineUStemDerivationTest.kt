package dev.sanskrit.derivation

import kotlin.test.Test

class FeminineUStemDerivationTest {
    @Test
    fun `derive full feminine u-stem paradigm for dhenu`() = assertSubantaParadigm(
        "धेनु",
        SubantaStemClass.U_STEM_FEMININE,
        """
            धेनुः धेनू धेनवः धेनुम् धेनू धेनूः धेन्वा धेनुभ्याम् धेनुभिः
            धेन्वै धेनुभ्याम् धेनुभ्यः धेन्वाः धेनुभ्याम् धेनुभ्यः
            धेन्वाः धेन्वोः धेनूनाम् धेन्वाम् धेन्वोः धेनुषु
        """,
    )
}
