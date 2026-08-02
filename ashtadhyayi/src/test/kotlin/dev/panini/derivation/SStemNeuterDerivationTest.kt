package dev.panini.derivation

import dev.panini.core.Linga
import kotlin.test.Test

class SStemNeuterDerivationTest {
    @Test
    fun `derive full neuter s-stem paradigm for manas`() = assertSubantaParadigm(
        "मनस्",
        Linga.NAPUMSAKA,
        """
            मनः मनसी मनांसि मनः मनसी मनांसि मनसा मनोभ्याम् मनोभिः
            मनसे मनोभ्याम् मनोभ्यः मनसः मनोभ्याम् मनोभ्यः
            मनसः मनसोः मनसाम् मनसि मनसोः मनःसु
        """,
    )

    @Test
    fun `derive full neuter s-stem paradigm for payas`() = assertSubantaParadigm(
        "पयस्",
        Linga.NAPUMSAKA,
        """
            पयः पयसी पयांसि पयः पयसी पयांसि पयसा पयोभ्याम् पयोभिः
            पयसे पयोभ्याम् पयोभ्यः पयसः पयोभ्याम् पयोभ्यः
            पयसः पयसोः पयसाम् पयसि पयसोः पयःसु
        """,
    )
}
