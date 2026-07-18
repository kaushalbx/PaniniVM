package dev.sanskrit.derivation

import kotlin.test.Test

class RStemDerivationTest {
    @Test
    fun `derive full masculine r-stem paradigm for pitr`() = assertSubantaParadigm(
        "पितृ",
        SubantaStemClass.R_STEM_MASCULINE,
        """
            पिता पितरौ पितरः पितरम् पितरौ पितॄन् पित्रा पितृभ्याम् पितृभिः
            पित्रे पितृभ्याम् पितृभ्यः पितुः पितृभ्याम् पितृभ्यः
            पितुः पित्रोः पितॄणाम् पितरि पित्रोः पितृषु
        """,
    )
}
