package dev.panini.derivation

import kotlin.test.Test

class NeuterAStemDerivationTest {
    @Test
    fun `derive full neuter a-stem paradigm for phala`() = assertSubantaParadigm(
        "फल",
        SubantaStemClass.A_STEM_NEUTER,
        """
            फलम् फले फलानि फलम् फले फलानि फलेन फलाभ्याम् फलैः
            फलाय फलाभ्याम् फलेभ्यः फलात् फलाभ्याम् फलेभ्यः
            फलस्य फलयोः फलानाम् फले फलयोः फलेषु
        """,
    )

    @Test
    fun `derive full neuter a-stem paradigm for jala`() = assertSubantaParadigm(
        "जल",
        SubantaStemClass.A_STEM_NEUTER,
        """
            जलम् जले जलानि जलम् जले जलानि जलेन जलाभ्याम् जलैः
            जलाय जलाभ्याम् जलेभ्यः जलात् जलाभ्याम् जलेभ्यः
            जलस्य जलयोः जलानाम् जले जलयोः जलेषु
        """,
    )
}
