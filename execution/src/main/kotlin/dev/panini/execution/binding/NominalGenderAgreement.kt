package dev.panini.execution.binding

import dev.panini.core.Linga
import dev.panini.linganushasanam.PaninianPratipadikaLexicon
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.lexicon.StandardPratipadikaLexicon

/** Partial lexical agreement: unknown gender is not evidence of a mismatch. */
internal object NominalGenderAgreement {
    fun genders(pada: Pada?): Set<Linga> {
        if (pada is SankhyaPuranaPada) {
            if (pada.striAffix != null) {
                dev.panini.execution.OrdinalFeminineDerivation.derive(pada)
                return setOf(Linga.STRI)
            }
            return setOf(Linga.PUMS, Linga.NAPUMSAKA)
        }
        val stem = (pada as? SubantaPada)?.pratipadika ?: return emptySet()
        val vikaras = when (stem) {
            is MulaPratipadika -> stem.vikaras
            is SankhyaPratipadika -> stem.vikaras
            is KridantaPratipadika -> stem.vikaras
            is UnadyantaPratipadika -> stem.vikaras
            is SamasaPratipadika -> stem.vikaras
        }
        if (vikaras.lastOrNull()?.isRecognizedStriAffix == true) return setOf(Linga.STRI)
        if (vikaras.isNotEmpty()) return emptySet()
        val lexicalForm = when (stem) {
            is MulaPratipadika -> stem.text
            is SankhyaPratipadika -> stem.semanticValue.word
            else -> return emptySet()
        }
        return StandardPratipadikaLexicon.findPratipadika(lexicalForm)?.linga.orEmpty()
    }

    /** Authoritative gender evidence; unlike [genders], this is not used to narrow legacy attachment candidates. */
    fun knownGenders(pada: Pada?): Set<Linga> {
        val standard = genders(pada)
        if (standard.isNotEmpty()) return standard
        val stem = (pada as? SubantaPada)?.pratipadika as? MulaPratipadika ?: return emptySet()
        if (stem.vikaras.isNotEmpty()) return emptySet()
        return PaninianPratipadikaLexicon.findPratipadika(stem.text)?.linga.orEmpty()
    }

    fun compatible(modifier: Pada?, head: Pada): Boolean {
        val modifierGenders = genders(modifier)
        val headGenders = genders(head)
        return modifierGenders.isEmpty() || headGenders.isEmpty() ||
            modifierGenders.any { it in headGenders }
    }
}
