package dev.panini.derivation

import dev.panini.core.Karaka
import dev.panini.core.Linga
import dev.panini.core.NominalCategory
import dev.panini.core.Prayoga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.analysis.SemanticRelation
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.analysis.ParticipantFacts

/** Typed input for a nominal (sup) derivation. */
data class SubantaDerivationRequest(
    val pratipadika: String,
    val vibhakti: Vibhakti,
    val vacana: Vacana,
    val linga: Linga = Linga.PUMS,
) {
    init {
        require(pratipadika.isNotBlank()) { "A prātipadika is required." }
    }

    fun initialState(): DerivationState = DerivationState(
        terms = listOf(DerivationTerm("pratipadika", pratipadika, TermKind.PRATIPADIKA)),
        context = DerivationalContext(
            rupa = Rupa(linga = linga, vibhakti = vibhakti, vacana = vacana),
        ),
    )
}

/** Input request for deriving a nominal form from a semantic Kāraka relation. */
data class KarakaSubantaDerivationRequest(
    val pratipadika: String,
    val karaka: Karaka,
    val vacana: Vacana,
    val dhatu: String,
    val linga: Linga = Linga.PUMS,
    val isSakarmaka: Boolean = true,
    val prayoga: Prayoga = Prayoga.KARTARI,
    val semanticRelations: Set<SemanticRelation>? = null,
    val categories: Set<NominalCategory>? = null,
    val upapada: String? = null,
    val otherParticipants: List<ParticipantFacts>? = null,
)

