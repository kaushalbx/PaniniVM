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
    val halantyamExemptTermIds: Set<String> = emptySet(),
    val varnaComparisons: Set<VarnaComparison> = emptySet(),
    val substitutions: List<VarnaSubstitution> = emptyList(),
) {

    init {
        require(terms.isNotEmpty()) { "A derivation requires at least one term." }
    }

    /** Validates the it-processing boundary for workflows that have completed migration. */
    fun requireCompleteItProcessing(): DerivationState {
        require(terms.none { it.itProcessingPhase != ItProcessingPhase.PROCESSED }) {
            val incomplete = terms.filter { it.itProcessingPhase != ItProcessingPhase.PROCESSED }
                .joinToString { "${it.id}:${it.surface}:${it.itProcessingPhase}" }
            "A completed derivation cannot contain incomplete it-processing: $incomplete."
        }
        require(terms.none { it.itDesignations.isNotEmpty() }) {
            "A completed derivation cannot contain unconsumed it-designations."
        }
        require(terms.none { it.deferredItDesignations.isNotEmpty() }) {
            "A completed derivation cannot contain deferred it-designations."
        }
        return this
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

    /** Replaces an entire affix while making the fate of every exact it-designation explicit. */
    fun replaceWholeAffix(
        id: String,
        surface: String,
        sutra: String,
        policy: WholeAffixDesignationPolicy,
        upadesha: String = surface,
    ): DerivationState {
        val term = terms.singleOrNull { it.id == id }
            ?: error("Whole-affix substitution $sutra requires exactly one term named $id.")
        require(term.kind == TermKind.PRATYAYA || term.kind == TermKind.AGAMA || term.kind == TermKind.AUGMENT) {
            "Whole-affix substitution $sutra cannot target non-affix term $id."
        }
        return replaceTerm(id, term.replaceWholeAffix(surface, upadesha, sutra, policy))
    }

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
        halantyamExemptTermIds: Set<String> = this.halantyamExemptTermIds,
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
            halantyamExemptTermIds = halantyamExemptTermIds,
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
            halantyamExemptTermIds == other.halantyamExemptTermIds &&
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
        result = 31 * result + halantyamExemptTermIds.hashCode()
        result = 31 * result + varnaComparisons.hashCode()
        result = 31 * result + substitutions.hashCode()
        return result
    }

    override fun toString(): String {
        return "DerivationState(terms=$terms, droppedTerms=$droppedTerms, samjnas=$samjnas, stage=$stage, context=$context, activeAdhikaras=$activeAdhikaras, inheritedAnuvrtti=$inheritedAnuvrtti, blockedSutras=$blockedSutras, halantyamExemptTermIds=$halantyamExemptTermIds, varnaComparisons=$varnaComparisons, substitutions=$substitutions)"
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
    /** Explicit lifecycle of an upadeśa as it moves through 1.3.2–1.3.9. */
    val itProcessingPhase: ItProcessingPhase = dev.panini.derivation.ItProcessingPhase.PROCESSED,
    /** Exact spans designated as इत् in the current upadeśa. */
    val itDesignations: List<ItDesignation> = emptyList(),
    /** Exact designations whose lopa waits for intervening substitution rules. */
    val deferredItDesignations: List<ItDesignation> = emptyList(),
    /** The term into which an āgama is placed by 1.1.46. */
    val augmentTargetId: String? = null,
    /** Underlying lexical head of a compound term, when rules target head identity after surface sandhi. */
    val compoundHeadUpadesha: String? = null,
) {
    val itProcessingPending: Boolean
        get() = itProcessingPhase == dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA ||
            itProcessingPhase == dev.panini.derivation.ItProcessingPhase.DESIGNATED

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

    fun replaceWholeAffix(
        replacementSurface: String,
        replacementUpadesha: String,
        sutra: String,
        policy: WholeAffixDesignationPolicy,
    ): DerivationTerm {
        val allDesignations = itDesignations + deferredItDesignations
        val inherited = SthaniProperties(
            upadesha = sthaniProps?.upadesha ?: upadesha,
            itMarkers = sthaniProps?.itMarkers.orEmpty() + itMarkers,
        )
        return when (policy) {
            is WholeAffixDesignationPolicy.PreserveAndRemap -> {
                require(policy.remaps.size == allDesignations.size) {
                    "$sutra must explicitly remap every designation on $id (${allDesignations.size} designations, ${policy.remaps.size} remaps)."
                }
                val remapped = allDesignations.map { designation ->
                    val remap = policy.remaps.singleOrNull {
                        it.oldStart == designation.start && it.oldEndExclusive == designation.endExclusive
                    } ?: error("$sutra has no unique remap for ${designation.start}..${designation.endExclusive} on $id.")
                    require(remap.newStart >= 0 && remap.newEndExclusive <= replacementSurface.length && remap.newStart < remap.newEndExclusive)
                    val newText = replacementSurface.substring(remap.newStart, remap.newEndExclusive)
                    designation.copy(start = remap.newStart, endExclusive = remap.newEndExclusive, designatedText = newText)
                }
                val activeCount = itDesignations.size
                copy(
                    surface = replacementSurface,
                    upadesha = replacementUpadesha,
                    itDesignations = remapped.take(activeCount),
                    deferredItDesignations = remapped.drop(activeCount),
                    itProcessingPhase = if (activeCount > 0) ItProcessingPhase.DESIGNATED else itProcessingPhase,
                )
            }
            WholeAffixDesignationPolicy.Consume -> copy(
                surface = replacementSurface,
                upadesha = replacementUpadesha,
                itDesignations = emptyList(),
                deferredItDesignations = emptyList(),
                itProcessingPhase = ItProcessingPhase.PROCESSED,
                sthaniProps = inherited,
            )
            WholeAffixDesignationPolicy.FreshUpadesha -> copy(
                surface = replacementSurface,
                upadesha = replacementUpadesha,
                itMarkers = emptySet(),
                itDesignations = emptyList(),
                deferredItDesignations = emptyList(),
                itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
                sthaniProps = inherited,
                createdBySutra = sutra,
            )
        }
    }
}

sealed interface WholeAffixDesignationPolicy {
    data class PreserveAndRemap(val remaps: List<ItDesignationRemap>) : WholeAffixDesignationPolicy
    data object Consume : WholeAffixDesignationPolicy
    data object FreshUpadesha : WholeAffixDesignationPolicy
}

data class ItDesignationRemap(
    val oldStart: Int,
    val oldEndExclusive: Int,
    val newStart: Int,
    val newEndExclusive: Int,
)

enum class ItProcessingPhase {
    /** The term is already an effective form or has no it-processing to perform. */
    PROCESSED,
    /** The raw upadeśa is waiting for 1.3.2–1.3.8 to designate exact spans. */
    RAW_UPADESHA,
    /** At least one exact span has been designated and awaits 1.3.9. */
    DESIGNATED,
    /** Substitution rules must finish before the replacement upadeśa can enter it-processing. */
    DEFERRED_SUBSTITUTION,
}

data class ItDesignation(
    val start: Int,
    val endExclusive: Int,
    val replacementAfterLopa: String = "",
    val marker: ItMarker,
    val sutra: String,
    /** Original designated segment; detects a designation consumed by a later whole-term substitution. */
    val designatedText: String,
)

data class SthaniProperties(
    val upadesha: String?,
    val itMarkers: Set<ItMarker>
)

enum class TermKind { DHATU, PRATIPADIKA, PRATYAYA, AGAMA, AUGMENT }
enum class DerivationStage { INITIAL, PRATYAYA_SELECTED, IT_PROCESSED, ANGAKARYA, PADA_FORMED, FINAL }

data class SamjnaAssignment(val targetId: String, val samjna: Samjna)
