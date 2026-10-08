package dev.panini.execution.binding

import dev.panini.core.Linga
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.lexicon.StandardPratipadikaLexicon

/** Partial lexical agreement: unknown gender is not evidence of a mismatch. */
internal object NominalGenderAgreement {
    fun genders(pada: Pada?): Set<Linga> {
        val stem = (pada as? SubantaPada)?.pratipadika ?: return emptySet()
        val vikaras = when (stem) {
            is MulaPratipadika -> stem.vikaras
            is SankhyaPratipadika -> stem.vikaras
            is KridantaPratipadika -> stem.vikaras
            is UnadyantaPratipadika -> stem.vikaras
            is SamasaPratipadika -> stem.vikaras
        }
        if (vikaras.any { it is StriVikara }) return setOf(Linga.STRI)
        val lexicalForm = when (stem) {
            is MulaPratipadika -> stem.text
            is SankhyaPratipadika -> stem.semanticValue.word
            else -> return emptySet()
        }
        return StandardPratipadikaLexicon.findPratipadika(lexicalForm)?.linga.orEmpty()
    }

    fun compatible(modifier: Pada?, head: Pada): Boolean {
        val modifierGenders = genders(modifier)
        val headGenders = genders(head)
        return modifierGenders.isEmpty() || headGenders.isEmpty() ||
            modifierGenders.any { it in headGenders }
    }
}
