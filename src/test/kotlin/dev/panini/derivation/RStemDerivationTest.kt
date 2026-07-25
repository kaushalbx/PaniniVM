package dev.panini.derivation

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

    @Test
    fun `derive full masculine r-stem paradigm for bhratr`() = assertSubantaParadigm(
        "भ्रातृ",
        SubantaStemClass.R_STEM_MASCULINE,
        """
            भ्राता भ्रातरौ भ्रातरः भ्रातरम् भ्रातरौ भ्रातॄन् भ्रात्रा भ्रातृभ्याम् भ्रातृभिः
            भ्रात्रे भ्रातृभ्याम् भ्रातृभ्यः भ्रातुः भ्रातृभ्याम् भ्रातृभ्यः
            भ्रातुः भ्रात्रोः भ्रातॄणाम् भ्रातरि भ्रात्रोः भ्रातृषु
        """,
    )
}
