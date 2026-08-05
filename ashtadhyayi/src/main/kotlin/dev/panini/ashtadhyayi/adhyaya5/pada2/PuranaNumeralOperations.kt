package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind

import dev.panini.sankhya.PrimitiveSankhya
import dev.panini.sankhya.SankhyaInflectionClass

internal object PuranaNumeralClasses {
    private val decadePrimitives = setOf(
        PrimitiveSankhya.VIMSHATI, PrimitiveSankhya.TRIMSHAT, PrimitiveSankhya.CHATVARIMSHAT,
        PrimitiveSankhya.PANCHASHAT, PrimitiveSankhya.SHASHTI, PrimitiveSankhya.SAPTATI,
        PrimitiveSankhya.ASHITI, PrimitiveSankhya.NAVATI
    )
    val vimshatyadiHeads: Set<String> by lazy {
        decadePrimitives.flatMap { listOf(it.pratipadika, it.purvapada, it.uttarapada) }.toSet()
    }
    val shashtyadiHeads = setOf("षष्टि", "सप्तति", "अशीति", "नवति")
    val shatadiHeads: Set<String> by lazy {
        setOf(
            PrimitiveSankhya.SHATA, PrimitiveSankhya.SAHASRA, PrimitiveSankhya.AYUTA,
            PrimitiveSankhya.LAKSHA, PrimitiveSankhya.PRAYUTA, PrimitiveSankhya.KOTI
        ).flatMap { listOf(it.pratipadika, it.purvapada, it.uttarapada) }.toSet() + setOf("मास", "अर्धमास", "संवत्सर")
    }
}

internal fun DerivationState.hasTamat(): Boolean = terms.any { it.upadesha == "तमट्" }

internal fun DerivationState.datIndex(): Int = terms.indexOfLast { it.upadesha == "डट्" }

internal fun DerivationState.insertTamat(sutra: String, explanation: String): DerivationChange {
    val index = datIndex()
    require(index > 0) { "तमट् requires an existing ordinal डट् suffix." }
    require(!hasTamat()) { "An ordinal derivation can contain only one तमट् augment." }
    val tamat = DerivationTerm(
        id = "purana_tamat",
        surface = "तम",
        kind = TermKind.AGAMA,
        upadesha = "तमट्",
        createdBySutra = sutra,
    )
    val changedTerms = terms.toMutableList().apply { add(index, tamat) }
    return DerivationChange(copy(terms = changedTerms), explanation)
}
