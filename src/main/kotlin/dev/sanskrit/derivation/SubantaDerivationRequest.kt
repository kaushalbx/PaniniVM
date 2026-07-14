package dev.sanskrit.derivation

import dev.sanskrit.shiksha.Ayogavaha
import dev.sanskrit.shiksha.Svara
import dev.sanskrit.shiksha.Vyanjana
import dev.sanskrit.shiksha.Linga

/** Typed input for a nominal (sup) derivation. */
data class SubantaDerivationRequest(
    val pratipadika: String,
    val vibhakti: Vibhakti,
    val vacana: Vacana,
    val stemClass: SubantaStemClass = SubantaStemClass.A_STEM_MASCULINE,
) {
    init {
        require(pratipadika.isNotBlank()) { "A prātipadika is required." }
        require(stemClass.accepts(pratipadika)) {
            "$pratipadika is not supported as ${stemClass.displayName}; select a supported stem class."
        }
    }

    fun initialState(): DerivationState = DerivationState(
        terms = listOf(DerivationTerm("pratipadika", pratipadika, TermKind.PRATIPADIKA)),
        context = DerivationalContext(
            rupa = Rupa(linga = stemClass.linga, vibhakti = vibhakti, vacana = vacana),
        ),
    )
}

/** The nominal morphology currently implemented by the executable patha. */
enum class SubantaStemClass(
    val displayName: String,
    val linga: Linga,
) {
    A_STEM_MASCULINE("a-stem masculine", Linga.PUMS),
    A_STEM_NEUTER("a-stem neuter", Linga.NAPUMSAKA),
    I_STEM_MASCULINE("i-stem masculine", Linga.PUMS),
    U_STEM_MASCULINE("u-stem masculine", Linga.PUMS),
    A_STEM_FEMININE("ā-stem feminine", Linga.STRI),
    ;

    fun accepts(pratipadika: String): Boolean = when (this) {
        A_STEM_MASCULINE, A_STEM_NEUTER -> pratipadika.last() !in independentVowelsOrMarks
        I_STEM_MASCULINE -> pratipadika.endsWith(Svara.I.matra!!) || pratipadika.endsWith(Svara.I.devanagari)
        U_STEM_MASCULINE -> pratipadika.endsWith(Svara.U.matra!!) || pratipadika.endsWith(Svara.U.devanagari)
        A_STEM_FEMININE -> pratipadika.endsWith(Svara.AA.matra!!) || pratipadika.endsWith(Svara.AA.devanagari)
    }

    private companion object {
        val independentVowelsOrMarks: Set<Char> = buildSet {
            Svara.entries.forEach { svara ->
                add(svara.devanagari.single())
                svara.matra?.single()?.let(::add)
            }
            Ayogavaha.entries.forEach { add(it.devanagari.single()) }
            add(CANDRABINDU)
            add(Vyanjana.VIRAMA)
        }

        /** Candrabindu is a combining sign, not an independent varṇa enum entry. */
        const val CANDRABINDU: Char = 'ँ'
    }
}

enum class Vibhakti {
    PRATHAMA, DVITIYA, TRTIYA, CHATURTHI, PANCHAMI, SASTHI, SAPTAMI,
}

enum class Vacana {
    EKAVACANA, DVIVACANA, BAHUVACANA,
}
