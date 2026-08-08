package dev.panini.derivation

import dev.panini.core.DhatuGana
import dev.panini.core.ItMarker
import dev.panini.core.LopaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.LexicalUse
import dev.panini.shiksha.Samjna

/**
 * The shared state passed through an Ashtadhyayi derivation.
 */
class DerivationState(
    val terms: List<DerivationTerm>,
    val droppedTerms: List<DerivationTerm> = emptyList(),
    val samjnas: Set<SamjnaAssignment> = emptySet(),
    val stage: DerivationStage = DerivationStage.INITIAL,
    val context: DerivationalContext = DerivationalContext(),
    val activeAdhikaras: Set<String> = emptySet(),
    val inheritedAnuvrtti: Set<String> = emptySet(),
    val blockedSutras: Map<String, String> = emptyMap(),
    val varnaComparisons: Set<VarnaComparison> = emptySet(),
    val substitutions: List<VarnaSubstitution> = emptyList(),
) {

    init {
        require(terms.isNotEmpty()) { "A derivation requires at least one term." }
    }

    val surface: String
        get() = combinedSurface(terms)

    val rawJoinedSurface: String
        get() {
            val nonExistentOrEmptyFiltered = terms.filter { it.surface.isNotEmpty() }
            if (nonExistentOrEmptyFiltered.size > 1) {
                return nonExistentOrEmptyFiltered.joinToString(" + ") { it.surface }
            }
            return combinedSurface(terms)
        }

    private fun combinedSurface(termList: List<DerivationTerm>): String {
        return termList.fold("") { rendered, term ->
            val next = term.surface
            if (rendered.endsWith('्') && next.firstOrNull() == 'अ') {
                rendered.dropLast(1) + next.drop(1)
            } else if (rendered.endsWith('्') && next.firstOrNull() == 'आ') {
                rendered.dropLast(1) + "ा" + next.drop(1)
            } else if (rendered.endsWith('्') && next.firstOrNull() in setOf('इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ', 'ए', 'ऐ', 'ओ', 'औ')) {
                val vowelSign = when (next.first()) {
                    'इ' -> "ि"
                    'ई' -> "ी"
                    'उ' -> "ु"
                    'ऊ' -> "ू"
                    'ऋ' -> "ृ"
                    'ॠ' -> "ॄ"
                    'ऌ' -> "ॢ"
                    'ए' -> "े"
                    'ऐ' -> "ै"
                    'ओ' -> "ो"
                    'औ' -> "ौ"
                    else -> error("Unsupported independent vowel ${next.first()}")
                }
                rendered.dropLast(1) + vowelSign + next.drop(1)
            } else if (rendered.endsWith('्') && next.firstOrNull() in setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ', 'े', 'ै', 'ो', 'ौ')) {
                rendered.dropLast(1) + next
            } else {
                rendered + next
            }
        }
    }

    val allEffectiveTerms: List<DerivationTerm>
        get() = terms + droppedTerms

    val effectiveContext: DerivationalContext
        get() = context

    fun withSamjnas(additions: Set<SamjnaAssignment>): DerivationState =
        copy(samjnas = samjnas + additions)

    fun replaceTerm(id: String, replacement: DerivationTerm): DerivationState =
        copy(terms = terms.map { if (it.id == id) replacement else it })

    fun removeTerm(id: String, sutra: String? = null): DerivationState {
        val term = terms.find { it.id == id } ?: return this
        return copy(
            terms = terms.filter { it.id != id },
            droppedTerms = droppedTerms + term.copy(
                surface = "",
                droppedBySutra = sutra,
                originalSurfaceBeforeDrop = term.surface
            )
        )
    }

    fun addTerm(term: DerivationTerm): DerivationState {
        require(terms.none { it.id == term.id }) { "A derivation term id must be unique: ${term.id}" }
        return copy(terms = terms + term)
    }

    /** Inserts a stem-forming affix before a liṅ augment, or directly before tiṅ. */
    fun insertBeforeTingOrLingAugment(term: DerivationTerm): DerivationState {
        require(terms.none { it.id == term.id }) { "A derivation term id must be unique: ${term.id}" }
        val insertionIndex = terms.indexOfFirst { it.id == "yasut" || it.id == "siyut" }
            .takeIf { it >= 0 }
            ?: terms.lastIndex
        return copy(terms = terms.take(insertionIndex) + term + terms.drop(insertionIndex))
    }

    fun activateAdhikara(sutraNumber: String): DerivationState =
        copy(activeAdhikaras = activeAdhikaras + sutraNumber)

    fun carryAnuvrtti(item: String): DerivationState =
        copy(inheritedAnuvrtti = inheritedAnuvrtti + item)

    fun blockSutra(sutraNumber: String, blocker: String): DerivationState =
        copy(blockedSutras = blockedSutras + (sutraNumber to blocker))

    fun addComparison(comparison: VarnaComparison): DerivationState =
        copy(varnaComparisons = varnaComparisons + comparison)

    fun addSubstitution(substitution: VarnaSubstitution): DerivationState =
        copy(substitutions = substitutions + substitution)

    fun copy(
        terms: List<DerivationTerm> = this.terms,
        droppedTerms: List<DerivationTerm> = this.droppedTerms,
        samjnas: Set<SamjnaAssignment> = this.samjnas,
        stage: DerivationStage = this.stage,
        context: DerivationalContext = this.context,
        activeAdhikaras: Set<String> = this.activeAdhikaras,
        inheritedAnuvrtti: Set<String> = this.inheritedAnuvrtti,
        blockedSutras: Map<String, String> = this.blockedSutras,
        varnaComparisons: Set<VarnaComparison> = this.varnaComparisons,
        substitutions: List<VarnaSubstitution> = this.substitutions,
    ): DerivationState {
        return DerivationState(
            terms = terms,
            droppedTerms = droppedTerms,
            samjnas = samjnas,
            stage = stage,
            context = context,
            activeAdhikaras = activeAdhikaras,
            inheritedAnuvrtti = inheritedAnuvrtti,
            blockedSutras = blockedSutras,
            varnaComparisons = varnaComparisons,
            substitutions = substitutions,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DerivationState) return false
        return terms == other.terms &&
            droppedTerms == other.droppedTerms &&
            samjnas == other.samjnas &&
            stage == other.stage &&
            context == other.context &&
            activeAdhikaras == other.activeAdhikaras &&
            inheritedAnuvrtti == other.inheritedAnuvrtti &&
            blockedSutras == other.blockedSutras &&
            varnaComparisons == other.varnaComparisons &&
            substitutions == other.substitutions
    }

    override fun hashCode(): Int {
        var result = terms.hashCode()
        result = 31 * result + droppedTerms.hashCode()
        result = 31 * result + samjnas.hashCode()
        result = 31 * result + stage.hashCode()
        result = 31 * result + context.hashCode()
        result = 31 * result + activeAdhikaras.hashCode()
        result = 31 * result + inheritedAnuvrtti.hashCode()
        result = 31 * result + blockedSutras.hashCode()
        result = 31 * result + varnaComparisons.hashCode()
        result = 31 * result + substitutions.hashCode()
        return result
    }

    override fun toString(): String {
        return "DerivationState(terms=$terms, droppedTerms=$droppedTerms, samjnas=$samjnas, stage=$stage, context=$context, activeAdhikaras=$activeAdhikaras, inheritedAnuvrtti=$inheritedAnuvrtti, blockedSutras=$blockedSutras, varnaComparisons=$varnaComparisons, substitutions=$substitutions)"
    }
}

data class VarnaComparison(
    val leftTermId: String, val rightTermId: String,
    val left: Char, val right: Char,
    val samePlaceAndEffort: Boolean,
    val leftIsVowel: Boolean, val rightIsVowel: Boolean,
    val forbidden: Boolean = false,
)

data class VarnaSubstitution(
    val targetId: String, val source: Char,
    val replacement: String, val sutra: String,
)

data class DerivationTerm(
    val id: String,
    val surface: String,
    val kind: TermKind,
    val itMarkers: Set<ItMarker> = emptySet(),
    val upadesha: String = surface,
    val deletionType: LopaType? = null,
    val sthaniProps: SthaniProperties? = null,
    val lexicalUses: Set<LexicalUse> = emptySet(),
    val itStatus: ItStatus? = null,
    val gana: DhatuGana? = null,
    val blocksNicGuna: Boolean = false,
    val droppedBySutra: String? = null,
    val originalSurfaceBeforeDrop: String? = null,
    val createdBySutra: String? = null,
    val establishedBySutras: Set<String> = emptySet(),
    /** Underlying lexical head of a compound term, when rules target head identity after surface sandhi. */
    val compoundHeadUpadesha: String? = null,
) {
    companion object {
        /** Preserves Dhātupāṭha metadata when a root enters a derivation. */
        fun fromDhatu(dhatu: Dhatu, id: String = "dhatu"): DerivationTerm = DerivationTerm(
            id = id,
            surface = dhatu.derivationalSurface,
            kind = TermKind.DHATU,
            upadesha = dhatu.upadesha,
            itStatus = dhatu.itStatus,
            gana = dhatu.gana,
            blocksNicGuna = dhatu.blocksNicGuna,
        )
    }

    fun hasEffectiveMarker(marker: ItMarker): Boolean =
        marker in itMarkers || (sthaniProps?.itMarkers?.contains(marker) == true)

    fun matchesUpadesha(value: String): Boolean =
        upadesha == value || sthaniProps?.upadesha == value
}

data class SthaniProperties(
    val upadesha: String?,
    val itMarkers: Set<ItMarker>
)

enum class TermKind { DHATU, PRATIPADIKA, PRATYAYA, AGAMA, AUGMENT }
enum class DerivationStage { INITIAL, PRATYAYA_SELECTED, IT_PROCESSED, ANGAKARYA, PADA_FORMED, FINAL }

data class SamjnaAssignment(val targetId: String, val samjna: Samjna)
