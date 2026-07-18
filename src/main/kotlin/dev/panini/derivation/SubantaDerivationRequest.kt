package dev.panini.derivation

import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.Linga

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
    I_STEM_FEMININE("i-stem feminine", Linga.STRI),
    U_STEM_MASCULINE("u-stem masculine", Linga.PUMS),
    U_STEM_FEMININE("u-stem feminine", Linga.STRI),
    R_STEM_MASCULINE("ṛ-stem masculine", Linga.PUMS),
    N_STEM_MASCULINE("n-stem masculine", Linga.PUMS),
    S_STEM_NEUTER("s-stem neuter", Linga.NAPUMSAKA),
    II_STEM_FEMININE("ī-stem feminine", Linga.STRI),
    A_STEM_FEMININE("ā-stem feminine", Linga.STRI),
    ;

    fun accepts(pratipadika: String): Boolean = when (this) {
        A_STEM_MASCULINE, A_STEM_NEUTER -> pratipadika.last() !in independentVowelsOrMarks
        I_STEM_MASCULINE, I_STEM_FEMININE -> pratipadika.endsWith(Svara.I.matra!!) || pratipadika.endsWith(Svara.I.devanagari)
        U_STEM_MASCULINE, U_STEM_FEMININE -> pratipadika.endsWith(Svara.U.matra!!) || pratipadika.endsWith(Svara.U.devanagari)
        R_STEM_MASCULINE -> pratipadika.endsWith(Svara.R.matra!!) || pratipadika.endsWith(Svara.R.devanagari)
        N_STEM_MASCULINE -> pratipadika.endsWith("न्")
        S_STEM_NEUTER -> pratipadika.endsWith("स्")
        II_STEM_FEMININE -> pratipadika.endsWith(Svara.II.matra!!) || pratipadika.endsWith(Svara.II.devanagari)
        A_STEM_FEMININE -> pratipadika.endsWith(Svara.AA.matra!!) || pratipadika.endsWith(Svara.AA.devanagari)
    }

    companion object {
        fun guess(pratipadika: String): SubantaStemClass {
            return when {
                pratipadika.endsWith("न्") -> N_STEM_MASCULINE
                pratipadika.endsWith("स्") -> S_STEM_NEUTER
                pratipadika.endsWith("ी") -> II_STEM_FEMININE
                pratipadika.endsWith("ा") -> A_STEM_FEMININE
                pratipadika.endsWith("ि") -> {
                    if (pratipadika == "मति" || pratipadika == "भूमि" || pratipadika == "धूलि") I_STEM_FEMININE
                    else I_STEM_MASCULINE
                }
                pratipadika.endsWith("ु") -> {
                    if (pratipadika == "धेनु" || pratipadika == "तनु") U_STEM_FEMININE
                    else U_STEM_MASCULINE
                }
                pratipadika.endsWith("ृ") -> R_STEM_MASCULINE
                else -> {
                    if (pratipadika == "फल" || pratipadika == "वन" || pratipadika == "गृह" || pratipadika == "पुस्तक" || pratipadika == "जल") A_STEM_NEUTER
                    else A_STEM_MASCULINE
                }
            }
        }

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
