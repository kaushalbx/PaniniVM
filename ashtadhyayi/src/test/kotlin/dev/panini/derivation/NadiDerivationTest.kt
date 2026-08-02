package dev.panini.derivation

import dev.panini.core.Linga
import kotlin.test.Test

class NadiDerivationTest {
    @Test
    fun `derive full feminine i-stem paradigm for nadi`() = assertSubantaParadigm(
        "नदी",
        Linga.STRI,
        """
            नदी नद्यौ नद्यः नदीम् नद्यौ नदीः नद्या नदीभ्याम् नदीभिः
            नद्यै नदीभ्याम् नदीभ्यः नद्याः नदीभ्याम् नदीभ्यः
            नद्याः नद्योः नदीनाम् नद्याम् नद्योः नदीषु
        """,
    )

    @Test
    fun `derive full feminine i-stem paradigm for devi`() = assertSubantaParadigm(
        "देवी",
        Linga.STRI,
        """
            देवी देव्यौ देव्यः देवीम् देव्यौ देवीः देव्या देवीभ्याम् देवीभिः
            देव्यै देवीभ्याम् देवीभ्यः देव्याः देवीभ्याम् देवीभ्यः
            देव्याः देव्योः देवीनाम् देव्याम् देव्योः देवीषु
        """,
    )
}
