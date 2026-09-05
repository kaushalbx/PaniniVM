package dev.panini.derivation

import dev.panini.core.DhatuGana
import dev.panini.core.ItMarker
import dev.panini.core.LopaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.LexicalUse
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Varnamala

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
            val pending = terms.filter { it.itDesignations.isNotEmpty() }
                .joinToString { "${it.id}:${it.surface}=${it.itDesignations}" }
            "A completed derivation cannot contain unconsumed it-designations: $pending."
        }
        require(terms.none { it.deferredItDesignations.isNotEmpty() }) {
            val pending = terms.filter { it.deferredItDesignations.isNotEmpty() }
                .joinToString { "${it.id}:${it.surface}=${it.deferredItDesignations}" }
            "A completed derivation cannot contain deferred it-designations: $pending."
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
            if (rendered.lastOrNull()?.let(Varnamala::isConsonant) == true && next.firstOrNull() == 'अ') {
                rendered + next.drop(1)
            } else if (rendered.endsWith('्') && next.firstOrNull() == 'अ') {
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

    /** Applies a segment-level phonological change and records its sūtra atomically. */
    fun substituteTermSurface(
        id: String,
        surface: String,
        source: Char,
        replacement: String,
        sutra: String,
    ): DerivationState {
        val term = terms.singleOrNull { it.id == id }
            ?: error("Varṇa substitution $sutra requires exactly one term named $id.")
        require(surface != term.surface) { "$sutra must change the surface of $id." }
        require(term.itDesignations.all { designation ->
            designation.endExclusive <= surface.length &&
                surface.substring(designation.start, designation.endExclusive) == designation.designatedText
        }) {
            "$sutra would invalidate an exact it-designation on $id; use replaceWholeAffix with an explicit policy."
        }
        require(term.deferredItDesignations.all { designation ->
            designation.endExclusive <= surface.length &&
                surface.substring(designation.start, designation.endExclusive) == designation.designatedText
        }) {
            "$sutra would invalidate a deferred it-designation on $id; use replaceWholeAffix with an explicit policy."
        }
        return replaceTerm(id, term.copy(surface = surface))
            .addSubstitution(VarnaSubstitution(id, source, replacement, sutra))
    }

    /** Merges two adjacent terms while preserving the survivor and lifecycle-dropping the consumed term. */
    fun mergeTermsByVarnaSubstitution(
        survivorId: String,
        consumedId: String,
        surface: String,
        source: Char,
        replacement: String,
        sutra: String,
    ): DerivationState {
        require(survivorId != consumedId) { "$sutra cannot merge a term into itself." }
        val survivorIndex = terms.indexOfFirst { it.id == survivorId }
        val consumedIndex = terms.indexOfFirst { it.id == consumedId }
        require(survivorIndex >= 0 && consumedIndex >= 0) {
            "$sutra requires both $survivorId and $consumedId for a term merger."
        }
        require(kotlin.math.abs(survivorIndex - consumedIndex) == 1) {
            "$sutra can merge only adjacent terms: $survivorId and $consumedId."
        }
        val survivor = terms[survivorIndex]
        val substituted = if (surface == survivor.surface) {
            // The visible result can already equal the survivor (for example,
            // अ + अ after inherent-vowel serialization); consuming the adjacent
            // term is still a real two-term substitution.
            addSubstitution(VarnaSubstitution(survivorId, source, replacement, sutra))
        } else {
            substituteTermSurface(survivorId, surface, source, replacement, sutra)
        }
        return substituted.removeTerm(consumedId, sutra)
    }

    /** Replaces an entire affix while making the fate of every exact it-designation explicit. */
    fun replaceWholeAffix(
        id: String,
        surface: String,
        sutra: String,
        policy: WholeAffixDesignationPolicy,
        upadesha: String? = null,
        replacementId: String = id,
    ): DerivationState {
        val term = terms.singleOrNull { it.id == id }
            ?: error("Whole-affix substitution $sutra requires exactly one term named $id.")
        require(term.kind == TermKind.PRATYAYA || term.kind == TermKind.AGAMA || term.kind == TermKind.AUGMENT) {
            "Whole-affix substitution $sutra cannot target non-affix term $id."
        }
        val replacementUpadesha = upadesha ?: when (policy) {
            WholeAffixDesignationPolicy.FreshUpadesha -> surface
            else -> term.upadesha
        }
        val replaced = replaceTerm(
            id,
            term.replaceWholeAffix(surface, replacementUpadesha, sutra, policy).copy(id = replacementId),
        )
        if (replacementId == id) return replaced
        return replaced.copy(
            samjnas = replaced.samjnas.mapTo(mutableSetOf()) { assignment ->
                if (assignment.targetId == id) assignment.copy(targetId = replacementId) else assignment
            },
            halantyamExemptTermIds = replaced.halantyamExemptTermIds
                .let { ids -> if (id in ids) ids - id + replacementId else ids },
        )
    }

    fun removeTerm(id: String, sutra: String? = null): DerivationState {
        val term = terms.find { it.id == id } ?: return this
        require(sutra != null || term.kind !in affixKinds) {
            "Removing affix $id requires a sūtra so its designations can be consumed explicitly."
        }
        return copy(
            terms = terms.filter { it.id != id },
            droppedTerms = droppedTerms + if (sutra == null) {
                term.copy(surface = "", originalSurfaceBeforeDrop = term.surface)
            } else {
                dropTermWithLifecycle(term, sutra)
            },
        )
    }

    fun addTerm(term: DerivationTerm): DerivationState {
        require(terms.none { it.id == term.id }) { "A derivation term id must be unique: ${term.id}" }
        return copy(terms = terms + term)
    }

    /** Inserts a stem-forming affix before a liṅ augment, or directly before tiṅ. */
    fun insertBeforeTingOrLingAugment(term: DerivationTerm): DerivationState {
        require(terms.none { it.id == term.id }) { "A derivation term id must be unique: ${term.id}" }
        val tingId = terms.last().id
        val insertionIndex = terms.indexOfFirst {
            it.id == "yasut" || it.id == "siyut" ||
                (it.kind == TermKind.AGAMA &&
                    !it.mergeIntoAugmentTarget &&
                    it.augmentTargetId == tingId &&
                    "1.1.46" in it.establishedBySutras)
        }
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

private val affixKinds = setOf(TermKind.PRATYAYA, TermKind.AGAMA, TermKind.AUGMENT)

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
    /** Written upadeśa material retained for provenance but excluded from the operative surface. */
    val nonOperativeUpadeshaSegments: List<NonOperativeUpadeshaSegment> = emptyList(),
    /** Explicit lifecycle of an upadeśa as it moves through 1.3.2–1.3.9. */
    val itProcessingPhase: ItProcessingPhase = dev.panini.derivation.ItProcessingPhase.PROCESSED,
    /** Exact spans designated as इत् in the current upadeśa. */
    val itDesignations: List<ItDesignation> = emptyList(),
    /** Exact designations whose lopa waits for intervening substitution rules. */
    val deferredItDesignations: List<ItDesignation> = emptyList(),
    /** The term into which an āgama is placed by 1.1.46. */
    val augmentTargetId: String? = null,
    /** Whether 1.1.46 should fold this āgama into its target immediately. */
    val mergeIntoAugmentTarget: Boolean = true,
    /** Underlying lexical head of a compound term, when rules target head identity after surface sandhi. */
    val compoundHeadUpadesha: String? = null,
) {
    init {
        nonOperativeUpadeshaSegments.forEach { segment ->
            require(segment.start >= 0 && segment.endExclusive <= upadesha.length && segment.start < segment.endExclusive) {
                "Non-operative upadeśa segment ${segment.start}..${segment.endExclusive} is outside $id:$upadesha."
            }
            require(upadesha.substring(segment.start, segment.endExclusive) == segment.text) {
                "Non-operative upadeśa segment ${segment.start}..${segment.endExclusive} (${segment.text}) is stale on $id:$upadesha."
            }
        }
    }

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
                require(policy.remaps.size + policy.consumed.size == allDesignations.size) {
                    "$sutra must explicitly remap or consume every designation on $id " +
                        "(${allDesignations.size} designations, ${policy.remaps.size} remaps, ${policy.consumed.size} consumed)."
                }
                fun remap(designation: ItDesignation): ItDesignation? {
                    val matchingRemaps = policy.remaps.filter {
                        it.oldStart == designation.start && it.oldEndExclusive == designation.endExclusive
                    }
                    val matchingConsumptions = policy.consumed.filter {
                        it.oldStart == designation.start && it.oldEndExclusive == designation.endExclusive
                    }
                    require(matchingRemaps.size + matchingConsumptions.size == 1) {
                        "$sutra must uniquely remap or consume ${designation.start}..${designation.endExclusive} on $id."
                    }
                    if (matchingConsumptions.isNotEmpty()) return null
                    val remap = matchingRemaps.single()
                    require(remap.newStart >= 0 && remap.newEndExclusive <= replacementSurface.length && remap.newStart < remap.newEndExclusive)
                    val newText = replacementSurface.substring(remap.newStart, remap.newEndExclusive)
                    return designation.copy(start = remap.newStart, endExclusive = remap.newEndExclusive, designatedText = newText)
                }
                val remappedActive = itDesignations.mapNotNull(::remap)
                val remappedDeferred = deferredItDesignations.mapNotNull(::remap)
                val consumedMarkers = allDesignations.filter { designation ->
                    policy.consumed.any {
                        it.oldStart == designation.start && it.oldEndExclusive == designation.endExclusive
                    }
                }.mapTo(mutableSetOf()) { it.marker }
                copy(
                    surface = replacementSurface,
                    upadesha = replacementUpadesha,
                    itDesignations = remappedActive,
                    deferredItDesignations = remappedDeferred,
                    itProcessingPhase = if (remappedActive.isNotEmpty()) ItProcessingPhase.DESIGNATED else itProcessingPhase,
                    sthaniProps = if (consumedMarkers.isEmpty()) sthaniProps else SthaniProperties(
                        upadesha = sthaniProps?.upadesha ?: upadesha,
                        itMarkers = sthaniProps?.itMarkers.orEmpty() + consumedMarkers,
                    ),
                )
            }
            WholeAffixDesignationPolicy.Consume -> copy(
                surface = replacementSurface,
                upadesha = replacementUpadesha,
                nonOperativeUpadeshaSegments = emptyList(),
                itDesignations = emptyList(),
                deferredItDesignations = emptyList(),
                itProcessingPhase = ItProcessingPhase.PROCESSED,
                sthaniProps = inherited,
            )
            WholeAffixDesignationPolicy.FreshUpadesha -> copy(
                surface = replacementSurface,
                upadesha = replacementUpadesha,
                nonOperativeUpadeshaSegments = emptyList(),
                itMarkers = emptySet(),
                itDesignations = emptyList(),
                deferredItDesignations = emptyList(),
                itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
                // A fresh annotated upadeśa supersedes the former affix's
                // it-status. Keep its identity for substitution provenance,
                // but do not let consumed markers govern the new raw affix.
                sthaniProps = SthaniProperties(upadesha = inherited.upadesha, itMarkers = emptySet()),
                createdBySutra = sutra,
            )
        }
    }
}

sealed interface WholeAffixDesignationPolicy {
    data class PreserveAndRemap(
        val remaps: List<ItDesignationRemap>,
        val consumed: List<ItDesignationConsumption> = emptyList(),
    ) : WholeAffixDesignationPolicy
    data object Consume : WholeAffixDesignationPolicy
    data object FreshUpadesha : WholeAffixDesignationPolicy
}

/** Consumes every designation before an affix is moved out of the active derivation. */
fun consumeAffixForDrop(
    term: DerivationTerm,
    sutra: String,
    droppedSurface: String = "",
): DerivationTerm {
    require(term.kind == TermKind.PRATYAYA || term.kind == TermKind.AGAMA || term.kind == TermKind.AUGMENT) {
        "$sutra cannot consume non-affix term ${term.id}."
    }
    val originalSurface = term.surface
    return term.replaceWholeAffix(
        replacementSurface = droppedSurface,
        replacementUpadesha = term.upadesha,
        sutra = sutra,
        policy = WholeAffixDesignationPolicy.Consume,
    ).copy(
        droppedBySutra = sutra,
        originalSurfaceBeforeDrop = originalSurface,
    )
}

/** Drops a merged term, enforcing explicit designation consumption whenever that term is an affix. */
fun dropTermWithLifecycle(term: DerivationTerm, sutra: String): DerivationTerm =
    if (term.kind == TermKind.PRATYAYA || term.kind == TermKind.AGAMA || term.kind == TermKind.AUGMENT) {
        consumeAffixForDrop(term, sutra)
    } else {
        term.copy(surface = "", droppedBySutra = sutra, originalSurfaceBeforeDrop = term.surface)
    }

data class ItDesignationRemap(
    val oldStart: Int,
    val oldEndExclusive: Int,
    val newStart: Int,
    val newEndExclusive: Int,
)

data class ItDesignationConsumption(val oldStart: Int, val oldEndExclusive: Int)

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

/** An exact written span that explains an upadeśa but never enters grammatical operations. */
data class NonOperativeUpadeshaSegment(
    val start: Int,
    val endExclusive: Int,
    val text: String,
    val function: NonOperativeUpadeshaFunction,
)

enum class NonOperativeUpadeshaFunction {
    UCCARANARTHA,
}

data class SthaniProperties(
    val upadesha: String?,
    val itMarkers: Set<ItMarker>
)

enum class TermKind { DHATU, PRATIPADIKA, PRATYAYA, AGAMA, AUGMENT }
enum class DerivationStage { INITIAL, PRATYAYA_SELECTED, IT_PROCESSED, ANGAKARYA, PADA_FORMED, FINAL }

data class SamjnaAssignment(val targetId: String, val samjna: Samjna)
