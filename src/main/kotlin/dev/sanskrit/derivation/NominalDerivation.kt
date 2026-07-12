package dev.sanskrit.derivation

import dev.sanskrit.shiksha.Ayogavaha
import dev.sanskrit.shiksha.Svara
import dev.sanskrit.shiksha.Vyanjana

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
        semanticFeatures = setOf(vibhakti.semanticFeature, vacana.semanticFeature, stemClass.genderFeature),
    )
}

/** The nominal morphology currently implemented by the executable patha. */
enum class SubantaStemClass(
    val displayName: String,
    val genderFeature: SemanticFeature,
) {
    A_STEM_MASCULINE("a-stem masculine", SemanticFeature.PUMS),
    A_STEM_NEUTER("a-stem neuter", SemanticFeature.NAPUMSAKA),
    ;

    fun accepts(pratipadika: String): Boolean = when (this) {
        A_STEM_MASCULINE, A_STEM_NEUTER -> pratipadika.last() !in independentVowelsOrMarks
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

enum class Vibhakti(val semanticFeature: SemanticFeature) {
    PRATHAMA(SemanticFeature.PRATHAMA),
    DVITIYA(SemanticFeature.DVITIYA),
    TRTIYA(SemanticFeature.TRTIYA),
    CHATURTHI(SemanticFeature.CHATURTHI),
    PANCHAMI(SemanticFeature.PANCHAMI),
    SASTHI(SemanticFeature.SASTHI),
    SAPTAMI(SemanticFeature.SAPTAMI),
}

enum class Vacana(val semanticFeature: SemanticFeature) {
    EKAVACANA(SemanticFeature.EKAVACANA),
    DVIVACANA(SemanticFeature.DVIVACANA),
    BAHUVACANA(SemanticFeature.BAHUVACANA),
}
