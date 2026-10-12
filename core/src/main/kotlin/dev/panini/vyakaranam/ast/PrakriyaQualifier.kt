package dev.panini.vyakaranam.ast

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti

/** Supported grammatical qualifiers of a singular reusable procedure declaration. */
enum class PrakriyaDefinitionQualifier { PRAKRIYA, APAVADA, NITYA, ANTARANGA }

/** Recognition only: unknown or further-derived forms do not inherit a base modifier's meaning. */
fun SubantaPada.prakriyaDefinitionQualifier(): PrakriyaDefinitionQualifier? {
    if (!sup.supports(Vibhakti.PRATHAMA, Vacana.EKAVACANA)) return null
    return when (val stem = pratipadika) {
        is MulaPratipadika -> if (stem.vikaras.any { !it.isRecognizedStriAffix }) null else when (stem.lexicalIdentity) {
            MulaPratipadikaIdentity.PRAKRIYA -> PrakriyaDefinitionQualifier.PRAKRIYA
            MulaPratipadikaIdentity.APAVADA -> PrakriyaDefinitionQualifier.APAVADA
            MulaPratipadikaIdentity.NITYA -> PrakriyaDefinitionQualifier.NITYA
            MulaPratipadikaIdentity.ANTARANGA -> PrakriyaDefinitionQualifier.ANTARANGA
            else -> null
        }
        is KridantaPratipadika -> PrakriyaDefinitionQualifier.APAVADA.takeIf {
            stem.lexicalIdentity == KridantaLexicalIdentity.APAVADA && stem.vikaras.all { it.isRecognizedStriAffix }
        }
        else -> null
    }
}
