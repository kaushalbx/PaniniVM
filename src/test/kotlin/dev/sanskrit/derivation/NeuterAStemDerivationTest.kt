package dev.sanskrit.derivation

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
}
