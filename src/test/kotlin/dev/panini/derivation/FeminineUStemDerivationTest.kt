package dev.panini.derivation

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

    @Test
    fun `derive full feminine u-stem paradigm for tanu`() = assertSubantaParadigm(
        "तनु",
        SubantaStemClass.U_STEM_FEMININE,
        """
            तनुः तनू तनवः तनुम् तनू तनूः तन्वा तनुभ्याम् तनुभिः
            तन्वै तनुभ्याम् तनुभ्यः तन्वाः तनुभ्याम् तनुभ्यः
            तन्वाः तन्वोः तनूनाम् तन्वाम् तन्वोः तनुषु
        """,
    )
}
