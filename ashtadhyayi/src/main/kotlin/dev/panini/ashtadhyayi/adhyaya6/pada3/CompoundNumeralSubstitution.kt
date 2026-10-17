package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas

/** Current phonological shapes, distinct from retained lexical upadeśa. */
internal enum class CompoundNumeralStem(val varnas: List<Varna>) {
    DVI(listOf(Vyanjana.DA, Vyanjana.VA, Svara.I)),
    ASTAN(listOf(Svara.A, Vyanjana.SSA, Vyanjana.TTA, Svara.A, Vyanjana.NA)),
    TRI(listOf(Vyanjana.TA, Vyanjana.RA, Svara.I));
}

internal fun DerivationTerm.compoundNumeralStem(): CompoundNumeralStem? =
    CompoundNumeralStem.entries.singleOrNull { varnas == it.varnas }

/** Lexical inventory parsed once at declaration, never from a rule's surface. */
internal enum class CompoundNumeralUttara(val upadesha: String, val optional: Boolean) {
    DASHA("दश", false), VIMSHATI("विंशति", false), TRIMSHAT("त्रिंशत्", false),
    CATVARIMSHAT("चत्वारिंशत्", true), PANCASHAT("पञ्चाशत्", true),
    SASTI("षष्टि", true), SAPTATI("सप्तति", true), NAVATI("नवति", true);

    val varnas: List<Varna> = upadesha.toVarnas()
}

internal fun DerivationTerm.compoundNumeralUttara(): CompoundNumeralUttara? =
    CompoundNumeralUttara.entries.singleOrNull { varnas == it.varnas }

/** 6.3.47's ā replacement and 6.3.48's trayas, reused by optional 6.3.49. */
internal fun DerivationState.substituteCompoundNumeral(term: DerivationTerm, sutra: String): DerivationState =
    when (requireNotNull(term.compoundNumeralStem())) {
        CompoundNumeralStem.DVI -> replaceTermVarna(term.id, 2, listOf(Svara.AA), sutra)
        CompoundNumeralStem.ASTAN -> replaceTermVarnaRange(term.id, 3, 2, listOf(Svara.AA), mapOf(0 to 0), sutra)
        CompoundNumeralStem.TRI -> replaceTermVarnaRange(term.id, 2, 1,
            listOf(Svara.A, Vyanjana.YA, Svara.A, Vyanjana.SA), mapOf(0 to 0), sutra)
    }
