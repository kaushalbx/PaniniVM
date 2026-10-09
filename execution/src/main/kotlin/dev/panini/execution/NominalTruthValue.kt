package dev.panini.execution

import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.Pratipadika

/** Literal truth identity does not erase derivation on its nominal base. */
fun nominalTruthValue(pratipadika: Pratipadika): SanskritValue.Satya? {
    val nominal = pratipadika as? MulaPratipadika ?: return null
    if (nominal.vikaras.isNotEmpty()) return null
    return when (nominal.lexicalIdentity) {
        MulaPratipadikaIdentity.SATYA -> SanskritValue.Satya(true)
        MulaPratipadikaIdentity.ASATYA -> SanskritValue.Satya(false)
        else -> null
    }
}
