package dev.panini.derivation

import dev.panini.core.Linga
import kotlin.test.Test

class ApDerivationTest {
    @Test
    fun `derive full feminine a-stem paradigm for rama`() = assertSubantaParadigm(
        "रमा",
        Linga.STRI,
        """
            रमा रमे रमाः रमाम् रमे रमाः रमया रमाभ्याम् रमाभिः
            रमायै रमाभ्याम् रमाभ्यः रमायाः रमाभ्याम् रमाभ्यः
            रमायाः रमयोः रमाणाम् रमायाम् रमयोः रमासु
        """,
        NominalStemFormation.AP,
    )

    @Test
    fun `derive full feminine a-stem paradigm for sita`() = assertSubantaParadigm(
        "सीता",
        Linga.STRI,
        """
            सीता सीते सीताः सीताम् सीते सीताः सीतया सीताभ्याम् सीताभिः
            सीतायै सीताभ्याम् सीताभ्यः सीतायाः सीताभ्याम् सीताभ्यः
            सीतायाः सीतयोः सीतानाम् सीतायाम् सीतयोः सीतासु
        """,
        NominalStemFormation.AP,
    )
}
