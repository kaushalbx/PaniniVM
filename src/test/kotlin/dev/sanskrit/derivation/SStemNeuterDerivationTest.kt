package dev.sanskrit.derivation

import kotlin.test.Test

class SStemNeuterDerivationTest {
    @Test
    fun `derive full neuter s-stem paradigm for manas`() = assertSubantaParadigm(
        "मनस्",
        SubantaStemClass.S_STEM_NEUTER,
        """
            मनः मनसी मनांसि मनः मनसी मनांसि मनसा मनोभ्याम् मनोभिः
            मनसे मनोभ्याम् मनोभ्यः मनसः मनोभ्याम् मनोभ्यः
            मनसः मनसोः मनसाम् मनसि मनसोः मनःसु
        """,
    )
}
