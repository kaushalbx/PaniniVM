package dev.panini.derivation

import dev.panini.linganushasanam.PaninianPratipadikaLexicon

/** Built once per test JVM: deriving every declared paradigm is the costliest setup in this module. */
object DeclaredLexiconFixture {
    val declared: DerivedVisargaLexicon by lazy { DerivedVisargaLexicon.build(PaninianPratipadikaLexicon.declaredPratipadikas()) }
}
