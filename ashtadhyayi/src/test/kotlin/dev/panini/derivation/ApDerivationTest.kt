package dev.panini.derivation

import kotlin.test.Test

class ApDerivationTest {
    @Test
    fun `derive full feminine a-stem paradigm for rama`() = assertSubantaParadigm(
        "रमा",
        SubantaStemClass.A_STEM_FEMININE,
        """
            रमा रमे रमाः रमाम् रमे रमाः रमया रमाभ्याम् रमाभिः
            रमायै रमाभ्याम् रमाभ्यः रमायाः रमाभ्याम् रमाभ्यः
            रमायाः रमयोः रमाणाम् रमायाम् रमयोः रमासु
        """,
    )

    @Test
    fun `derive full feminine a-stem paradigm for sita`() = assertSubantaParadigm(
        "सीता",
        SubantaStemClass.A_STEM_FEMININE,
        """
            सीता सीते सीताः सीताम् सीते सीताः सीतया सीताभ्याम् सीताभिः
            सीतायै सीताभ्याम् सीताभ्यः सीतायाः सीताभ्याम् सीताभ्यः
            सीतायाः सीतयोः सीतानाम् सीतायाम् सीतयोः सीतासु
        """,
    )
}
