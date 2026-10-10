package dev.panini.derivation

import dev.panini.shiksha.OrthographicSign

import dev.panini.core.DhatuGana
import dev.panini.core.ItMarker
import dev.panini.core.LopaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Accent
import dev.panini.shiksha.LexicalUse
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Varnamala
import dev.panini.shiksha.Varna
import dev.panini.shiksha.toDevanagari
import dev.panini.shiksha.OrthographicSignPlacement
import dev.panini.shiksha.SanskritText
import dev.panini.shiksha.toVarnas
import dev.panini.shiksha.toSanskritText
import dev.panini.shiksha.renderWithOrthographicSigns

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
    val blockedOperations: Map<BlockedOperationDomain, String> = emptyMap(),
    val halantyamExemptTermIds: Set<String> = emptySet(),
    val varnaComparisons: Set<VarnaComparison> = emptySet(),
    val substitutions: List<VarnaSubstitution> = emptyList(),
    /** Applied grammatical rules, separate from concrete varṇa substitutions. */
    val appliedSutras: List<String> = emptyList(),
    val svaraNimittas: List<SvaraNimitta> = emptyList(),
    val svaraAssignments: List<SvaraAssignment> = emptyList(),
    /** Prohibitions scoped to a particular adjacent pair, not the whole derivation. */
    val boundaryRuleBlocks: Map<BoundaryRuleKey, String> = emptyMap(),
) {

    fun isBlockedAtBoundary(sutra: String, leftId: String, rightId: String): Boolean =
        BoundaryRuleKey(sutra, leftId, rightId) in boundaryRuleBlocks

    fun blockAtBoundary(sutra: String, leftId: String, rightId: String, blocker: String): DerivationState =
        copy(boundaryRuleBlocks = boundaryRuleBlocks + (BoundaryRuleKey(sutra, leftId, rightId) to blocker))

    init {
        require(terms.isNotEmpty()) { "A derivation requires at least one term." }
    }

    /** Validates the it-processing boundary for workflows that have completed migration. */
    fun requireCompleteItProcessing(): DerivationState {
        require(allEffectiveTerms.none { it.itProcessingPhase != ItProcessingPhase.PROCESSED }) {
            val incomplete = allEffectiveTerms.filter { it.itProcessingPhase != ItProcessingPhase.PROCESSED }
                .joinToString { "${it.id}:${it.surface}:${it.itProcessingPhase}" }
            "A completed derivation cannot contain incomplete it-processing: $incomplete."
        }
        require(allEffectiveTerms.none { it.itDesignations.isNotEmpty() }) {
            val pending = allEffectiveTerms.filter { it.itDesignations.isNotEmpty() }
                .joinToString { "${it.id}:${it.surface}=${it.itDesignations}" }
            "A completed derivation cannot contain unconsumed it-designations: $pending."
        }
        require(allEffectiveTerms.none { it.deferredItDesignations.isNotEmpty() }) {
            val pending = allEffectiveTerms.filter { it.deferredItDesignations.isNotEmpty() }
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
            } else if (rendered.lastOrNull()?.let(Varnamala::isConsonant) == true &&
                next.firstOrNull() in setOf('आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ', 'ए', 'ऐ', 'ओ', 'औ')) {
                val vowelSign = mapOf(
                    'आ' to "ा", 'इ' to "ि", 'ई' to "ी", 'उ' to "ु", 'ऊ' to "ू", 'ऋ' to "ृ",
                    'ॠ' to "ॄ", 'ऌ' to "ॢ", 'ए' to "े", 'ऐ' to "ै", 'ओ' to "ो", 'औ' to "ौ",
                ).getValue(next.first())
                rendered + vowelSign + next.drop(1)
            } else if (rendered.endsWith('्') && next.firstOrNull() in setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ', 'े', 'ै', 'ो', 'ौ')) {
                rendered.dropLast(1) + next
            } else {
                rendered + next
            }
        }
    }

    fun surfaceBeforeTerm(termId: String): String {
        val index = terms.indexOfFirst { it.id == termId }
        require(index >= 0) { "Unknown active term $termId." }
        return combinedSurface(terms.take(index))
    }

    val allEffectiveTerms: List<DerivationTerm>
        get() = terms + droppedTerms

    val effectiveContext: DerivationalContext
        get() = context

    fun withSamjnas(additions: Set<SamjnaAssignment>): DerivationState =
        copy(samjnas = samjnas + additions)

    fun replaceTerm(id: String, replacement: DerivationTerm): DerivationState =
        copy(terms = terms.map { if (it.id == id) replacement else it })

    /**
     * Replaces a complete non-affix surface when a grammatical rule prescribes
     * a lexical/member-level substitute rather than a single-varṇa operation.
     * Affixes must use [replaceWholeAffix] so their exact it-designations receive
     * an explicit preserve, consume, or fresh-upadeśa policy.
     */
    fun replaceWholeTermSurface(id: String, surface: String, sutra: String): DerivationState {
        val term = terms.single { it.id == id }
        require(term.kind != TermKind.PRATYAYA && term.kind != TermKind.AGAMA) {
            "$sutra must use replaceWholeAffix for ${term.kind} term $id."
        }
        require(term.itDesignations.isEmpty() && term.deferredItDesignations.isEmpty()) {
            "$sutra cannot replace $id while exact it-designations remain pending."
        }
        return replaceTerm(id, term.copy(surface = surface))
    }

    /** Applies a segment-level phonological change and records its sūtra atomically. */
    fun substituteTermSurface(
        id: String,
        surface: String,
        source: Char,
        replacement: String,
        sutra: String,
        sourceVarnaIndex: Int? = null,
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
        val designations = term.itDesignations + term.deferredItDesignations
        if (designations.isNotEmpty()) {
            val oldTokens = term.phonologicalText.effectiveVarnas
            val newTokens = surface.toSanskritText().effectiveVarnas
            require(designations.flatMap { it.varnaIndices }.all { index ->
                index in oldTokens.indices && index in newTokens.indices &&
                    oldTokens[index].varna == newTokens[index].varna &&
                    oldTokens[index].sourceSpan == newTokens[index].sourceSpan
            }) { "$sutra would shift a designated varṇa on $id; use an explicit designation remap." }
        }
        return replaceTerm(id, term.copy(surface = surface))
            .addSubstitution(
                VarnaSubstitution(
                    targetId = id,
                    source = source,
                    replacement = replacement,
                    sutra = sutra,
                    originalSurface = term.surface,
                    originalOrthographicSigns = term.orthographicSigns,
                    sourceVarnaIndex = sourceVarnaIndex,
                ),
            )
    }

    /** Transitional varṇa-native entry point while substitution traces still serialize text. */
    fun substituteTermSurface(
        id: String,
        surface: String,
        source: Varna,
        replacement: List<Varna>,
        sutra: String,
        sourceVarnaIndex: Int? = null,
    ): DerivationState = substituteTermSurface(
        id = id,
        surface = surface,
        source = source.devanagari.single(),
        replacement = replacement.toDevanagari(),
        sutra = sutra,
        sourceVarnaIndex = sourceVarnaIndex,
    )

    /** Applies a phonological substitution and renders explicit non-phonological signs at the term boundary. */
    fun substituteTermVarnas(
        id: String,
        varnas: List<Varna>,
        source: Varna,
        replacement: List<Varna>,
        sutra: String,
    ): DerivationState {
        val signs = terms.single { it.id == id }.orthographicSigns
        return substituteTermVarnas(id, varnas, signs, source, replacement, sutra)
    }

    /** Applies a phonological substitution and renders explicit non-phonological signs at the term boundary. */
    fun substituteTermVarnas(
        id: String,
        varnas: List<Varna>,
        orthographicSigns: List<OrthographicSignPlacement>,
        source: Varna,
        replacement: List<Varna>,
        sutra: String,
    ): DerivationState {
        val original = terms.single { it.id == id }
        val originalTokens = original.phonologicalText.effectiveVarnas
        val adjustedSigns = orthographicSigns.map { placement ->
            placement.copy(afterVarnaCount = placement.afterVarnaCount.coerceAtMost(varnas.size))
        }
        val rendered = if (adjustedSigns.isEmpty() && originalTokens.size == varnas.size) {
            SanskritText(originalTokens.mapIndexed { index, token -> token.copy(varna = varnas[index]) }).render()
        } else {
            varnas.toDevanagari(adjustedSigns)
        }
        val substituted = substituteTermSurface(id, rendered, source, replacement, sutra)
        val term = substituted.terms.single { it.id == id }
        return substituted.replaceTerm(id, term.copy(orthographicSigns = adjustedSigns))
    }

    /** Replaces one exact occurrence, retaining unaffected annotations and remapping written signs. */
    fun replaceTermVarna(id: String, index: Int, replacement: List<Varna>, sutra: String): DerivationState {
        val original = terms.single { it.id == id }
        val tokens = original.phonologicalText.effectiveVarnas
        require(index in tokens.indices) { "$sutra requires an existing varṇa occurrence on $id." }
        val source = tokens[index]
        val nasalVowel = replacement.indexOfFirst { it is dev.panini.shiksha.Svara }
        val nasalTarget = if (source.varna in dev.panini.shiksha.VarnaToken.nasalizableSemivowels) {
            replacement.indexOfFirst { it in dev.panini.shiksha.VarnaToken.nasalizableSemivowels }
        } else nasalVowel
        val inserted = replacement.mapIndexed { position, varna ->
            dev.panini.shiksha.VarnaToken(id = dev.panini.shiksha.VarnaTokenId("replacement:$id:$index:$sutra:$position"), varna = varna,
                accent = source.accent.takeIf { position == nasalVowel },
                nasalized = source.nasalized && position == nasalTarget)
        }
        val result = tokens.take(index) + inserted + tokens.drop(index + 1)
        val signs = (original.orthographicSigns + original.phonologicalText.sourceOrthographicSigns).distinct().mapNotNull { placement ->
            if (placement.sign == dev.panini.shiksha.OrthographicSign.CHANDRABINDU &&
                placement.afterVarnaCount == index + 1 && nasalVowel < 0) null
            else placement.copy(afterVarnaCount = when {
                placement.sign == dev.panini.shiksha.OrthographicSign.CHANDRABINDU && placement.afterVarnaCount == index + 1 ->
                    index + nasalVowel + 1
                placement.afterVarnaCount > index -> placement.afterVarnaCount + replacement.size - 1
                else -> placement.afterVarnaCount
            })
        }
        val rendered = SanskritText(result).renderWithOrthographicSigns(signs)
        val substituted = substituteTermSurface(id, rendered, source.varna, replacement, sutra, sourceVarnaIndex = index)
        val term = substituted.terms.single { it.id == id }
        return substituted.replaceTerm(id, term.copy(orthographicSigns = signs))
    }

    /** One-to-one replacements at exact occurrences, with a single grammatical
     * trace summarized by the first changed occurrence (as in grouped abhyāsa rules).
     * Token annotations and all written-sign boundaries remain attached to their positions.
     */
    fun replaceTermVarnaOccurrences(id: String, replacements: Map<Int, Varna>, sutra: String): DerivationState {
        val original = terms.single { it.id == id }
        val tokens = original.phonologicalText.effectiveVarnas
        require(replacements.isNotEmpty() && replacements.keys.all { it in tokens.indices })
        val changed = tokens.mapIndexed { index, token ->
            replacements[index]?.let { token.copy(varna = it) } ?: token
        }
        val signs = (original.orthographicSigns + original.phonologicalText.sourceOrthographicSigns).distinct()
        val first = replacements.keys.min()
        val rendered = SanskritText(changed).renderWithOrthographicSigns(signs)
        val substituted = substituteTermSurface(id, rendered, tokens[first].varna, listOf(replacements.getValue(first)), sutra)
        return substituted.replaceTerm(id, substituted.terms.single { it.id == id }.copy(orthographicSigns = signs))
    }

    /** Replaces an exact range. Mapping keys are replacement positions and values
     * are source positions relative to the consumed range; only mapped annotations survive.
     */
    fun replaceTermVarnaRange(
        id: String, fromIndex: Int, count: Int, replacement: List<Varna>,
        annotationSources: Map<Int, Int>, sutra: String,
    ): DerivationState {
        val original = terms.single { it.id == id }
        val tokens = original.phonologicalText.effectiveVarnas
        require(count > 0 && fromIndex >= 0 && fromIndex <= tokens.size - count)
        require(annotationSources.all { (target, source) -> target in replacement.indices && source in 0 until count })
        require(annotationSources.values.distinct().size == annotationSources.size)
        val inserted = replacement.mapIndexed { position, varna ->
            val source = annotationSources[position]?.let { tokens[fromIndex + it] }
            dev.panini.shiksha.VarnaToken(
                dev.panini.shiksha.VarnaTokenId("range:$id:$fromIndex:$sutra:$position"), varna,
                accent = source?.accent, nasalized = source?.nasalized == true,
            )
        }
        val end = fromIndex + count
        val signs = (original.orthographicSigns + original.phonologicalText.sourceOrthographicSigns).distinct().mapNotNull { sign ->
            val mapped = annotationSources.entries.singleOrNull { it.value == sign.afterVarnaCount - fromIndex - 1 }?.key
            if (sign.sign == dev.panini.shiksha.OrthographicSign.CHANDRABINDU &&
                sign.afterVarnaCount in (fromIndex + 1)..end && mapped == null) null
            else sign.copy(afterVarnaCount = when {
                sign.afterVarnaCount <= fromIndex -> sign.afterVarnaCount
                sign.sign == dev.panini.shiksha.OrthographicSign.CHANDRABINDU && mapped != null -> fromIndex + mapped + 1
                sign.afterVarnaCount >= end -> sign.afterVarnaCount + replacement.size - count
                mapped != null -> fromIndex + mapped + 1
                else -> fromIndex + replacement.size
            })
        }
        val rendered = SanskritText(tokens.take(fromIndex) + inserted + tokens.drop(end)).renderWithOrthographicSigns(signs)
        val substituted = substituteTermSurface(id, rendered, tokens[fromIndex].varna, replacement, sutra,
            sourceVarnaIndex = fromIndex)
        return substituted.replaceTerm(id, substituted.terms.single { it.id == id }.copy(orthographicSigns = signs))
    }

    /** Deletes selected occurrences together, retaining surviving token annotations.
     * Every sign is projected onto the boundary after its surviving prefix.
     */
    fun deleteTermVarnaOccurrences(id: String, indices: Set<Int>, sutra: String): DerivationState {
        val original = terms.single { it.id == id }
        val tokens = original.phonologicalText.effectiveVarnas
        require(indices.isNotEmpty() && indices.all { it in tokens.indices })
        val signs = (original.orthographicSigns + original.phonologicalText.sourceOrthographicSigns).distinct().mapNotNull { sign ->
            if (sign.sign == dev.panini.shiksha.OrthographicSign.CHANDRABINDU && sign.afterVarnaCount - 1 in indices) null
            else sign.copy(afterVarnaCount = sign.afterVarnaCount - indices.count { it < sign.afterVarnaCount })
        }
        val rendered = SanskritText(tokens.filterIndexed { index, _ -> index !in indices }).renderWithOrthographicSigns(signs)
        val substituted = substituteTermSurface(id, rendered, tokens[indices.min()].varna, emptyList(), sutra)
        return substituted.replaceTerm(id, substituted.terms.single { it.id == id }.copy(orthographicSigns = signs))
    }

    /** Deletes one exact phonological range in a single trace entry.
     * Signs inside the deleted range collapse onto its surviving left boundary.
     */
    fun deleteTermVarnas(id: String, fromIndex: Int, count: Int, sutra: String): DerivationState {
        val original = terms.single { it.id == id }
        val tokens = original.phonologicalText.effectiveVarnas
        require(count > 0 && fromIndex >= 0 && fromIndex <= tokens.size - count) {
            "$sutra requires an existing nonempty varṇa range on $id."
        }
        val end = fromIndex + count
        val signs = (original.orthographicSigns + original.phonologicalText.sourceOrthographicSigns).distinct().mapNotNull {
            if (it.sign == dev.panini.shiksha.OrthographicSign.CHANDRABINDU && it.afterVarnaCount in (fromIndex + 1)..end) null
            else it.copy(afterVarnaCount = when {
                it.afterVarnaCount >= end -> it.afterVarnaCount - count
                it.afterVarnaCount > fromIndex -> fromIndex
                else -> it.afterVarnaCount
            })
        }
        val rendered = SanskritText(tokens.take(fromIndex) + tokens.drop(end)).renderWithOrthographicSigns(signs)
        val substituted = substituteTermSurface(id, rendered, tokens[fromIndex].varna, emptyList(), sutra)
        return substituted.replaceTerm(id, substituted.terms.single { it.id == id }.copy(orthographicSigns = signs))
    }

    /** Inserts phonological material at a varṇa boundary and records the āgama without exposing text offsets. */
    fun insertTermVarnas(
        id: String,
        beforeVarnaIndex: Int,
        insertion: List<Varna>,
        sutra: String,
    ): DerivationState {
        val term = terms.single { it.id == id }
        require(beforeVarnaIndex in 0..term.varnas.size)
        val tokens = term.phonologicalText.effectiveVarnas
        val inserted = insertion.mapIndexed { position, varna ->
            dev.panini.shiksha.VarnaToken(
                dev.panini.shiksha.VarnaTokenId("insertion:$id:$beforeVarnaIndex:$sutra:$position"), varna,
            )
        }
        val result = tokens.take(beforeVarnaIndex) + inserted + tokens.drop(beforeVarnaIndex)
        // A sign exactly at the boundary belongs to the preceding material.
        val signs = (term.orthographicSigns + term.phonologicalText.sourceOrthographicSigns).distinct().map {
            if (it.afterVarnaCount > beforeVarnaIndex) it.copy(afterVarnaCount = it.afterVarnaCount + insertion.size)
            else it
        }
        val rendered = SanskritText(result).renderWithOrthographicSigns(signs)
        val substituted = substituteTermSurface(id, rendered, '∅', insertion.toDevanagari(), sutra)
        val current = substituted.terms.single { it.id == id }
        return substituted.replaceTerm(id, current.copy(orthographicSigns = signs))
    }

    /** Concatenates an already transformed stem and its following processed term.
     * This is composition, not another substitution: the caller records its exact mutation.
     * Token annotations and signs are retained, and the consumed affix stays in the lifecycle ledger.
     */
    fun concatenateFollowingTerm(survivorId: String, consumedId: String, sutra: String): DerivationState {
        val index = terms.indexOfFirst { it.id == survivorId }
        require(index >= 0 && index < terms.lastIndex && terms[index + 1].id == consumedId) {
            "$sutra requires an ordered adjacent pair $survivorId and $consumedId."
        }
        val survivor = terms[index]
        val consumed = terms[index + 1]
        require(consumed.itProcessingPhase == ItProcessingPhase.PROCESSED &&
            consumed.itDesignations.isEmpty() && consumed.deferredItDesignations.isEmpty()) {
            "$sutra cannot compose an affix before its exact it-processing is complete."
        }
        val composed = survivor.insertDesignatedTerm(consumed, survivor.varnas.size).copy(
            sourceSuffixUpadeshas = survivor.sourceSuffixUpadeshas + if (consumed.kind == TermKind.PRATYAYA)
                consumed.sourceSuffixUpadeshas + consumed.upadesha else emptySet(),
        )
        val removed = replaceTerm(survivorId, composed).removeTerm(consumedId, sutra)
        val suffixVowels = consumed.varnas.count { it is dev.panini.shiksha.Svara }
        return if (consumed.kind != TermKind.PRATYAYA || suffixVowels == 0) removed else removed.copy(
            droppedTerms = removed.droppedTerms.map { term ->
                if (term.id == consumedId) term.copy(mergedIntoTermId = survivorId,
                    mergedAffixVowelFromEnd = suffixVowels - 1) else term
            },
        )
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
        val consumed = terms[consumedIndex]
        val consumedAffixVowelIndex = if (consumed.kind == TermKind.PRATYAYA &&
            DevanagariVowelLoci.positions(consumed.surface).isNotEmpty()
        ) DevanagariVowelLoci.positions(combinedSurface(terms.take(consumedIndex))).size else null
        val substituted = if (surface == survivor.surface) {
            // The visible result can already equal the survivor (for example,
            // अ + अ after inherent-vowel serialization); consuming the adjacent
            // term is still a real two-term substitution.
            addSubstitution(VarnaSubstitution(survivorId, source, replacement, sutra))
        } else {
            substituteTermSurface(survivorId, surface, source, replacement, sutra)
        }
        val withAffixProvenance = if (consumed.kind == TermKind.PRATYAYA) {
            val currentSurvivor = substituted.terms.single { it.id == survivorId }
            substituted.replaceTerm(survivorId, currentSurvivor.copy(
                sourceSuffixUpadeshas = currentSurvivor.sourceSuffixUpadeshas +
                    consumed.sourceSuffixUpadeshas + consumed.upadesha,
            ))
        } else substituted
        val removed = withAffixProvenance.removeTerm(consumedId, sutra)
        val survivingLocus = consumedAffixVowelIndex?.let { oldIndex ->
            val wordIndex = DevanagariVowelLoci.positions(removed.surface).indices.lastOrNull()
                ?.let(oldIndex::coerceAtMost) ?: return@let null
            val currentSurvivor = removed.terms.firstOrNull { it.id == survivorId } ?: return@let null
            val prefixCount = DevanagariVowelLoci.positions(removed.surfaceBeforeTerm(survivorId)).size
            val survivorVowelCount = DevanagariVowelLoci.positions(currentSurvivor.surface).size
            val localIndex = wordIndex - prefixCount
            if (localIndex !in 0 until survivorVowelCount) null
            else survivorId to (survivorVowelCount - 1 - localIndex)
        }
        return if (survivingLocus == null) removed else removed.copy(
            droppedTerms = removed.droppedTerms.map { dropped ->
                if (dropped.id == consumedId) dropped.copy(
                    mergedIntoTermId = survivingLocus.first,
                    mergedAffixVowelFromEnd = survivingLocus.second,
                ) else dropped
            },
        )
    }

    /** Transitional varṇa-native entry point while substitution traces still serialize text. */
    fun mergeTermsByVarnaSubstitution(
        survivorId: String,
        consumedId: String,
        surface: String,
        source: Varna,
        replacement: List<Varna>,
        sutra: String,
    ): DerivationState = mergeTermsByVarnaSubstitution(
        survivorId = survivorId,
        consumedId = consumedId,
        surface = surface,
        source = source.devanagari.single(),
        replacement = replacement.toDevanagari(),
        sutra = sutra,
    )

    /** Redistributes material across two adjacent surviving terms as one phonological operation. */
    fun redistributeAdjacentTermsByVarnaSubstitution(
        leftId: String,
        rightId: String,
        leftSurface: String,
        rightSurface: String,
        source: Char,
        replacement: String,
        sutra: String,
    ): DerivationState {
        val leftIndex = terms.indexOfFirst { it.id == leftId }
        val rightIndex = terms.indexOfFirst { it.id == rightId }
        require(leftIndex >= 0 && rightIndex == leftIndex + 1) {
            "$sutra requires adjacent ordered terms $leftId and $rightId."
        }
        val left = terms[leftIndex]
        val right = terms[rightIndex]
        require(left.surface != leftSurface || right.surface != rightSurface) {
            "$sutra must change at least one surface across $leftId and $rightId."
        }

        fun requireStableDesignations(term: DerivationTerm, newSurface: String) {
            require((term.itDesignations + term.deferredItDesignations).all { designation ->
                designation.endExclusive <= newSurface.length &&
                    newSurface.substring(designation.start, designation.endExclusive) == designation.designatedText
            }) {
                "$sutra would invalidate an exact it-designation on ${term.id}; " +
                    "use replaceWholeAffix with an explicit policy."
            }
        }
        requireStableDesignations(left, leftSurface)
        requireStableDesignations(right, rightSurface)

        return copy(
            terms = terms.map { term ->
                when (term.id) {
                    leftId -> term.copy(surface = leftSurface)
                    rightId -> term.copy(surface = rightSurface)
                    else -> term
                }
            },
        ).addSubstitution(VarnaSubstitution(leftId, source, replacement, sutra))
    }

    /** Transitional varṇa-native entry point while substitution traces still serialize text. */
    fun redistributeAdjacentTermsByVarnaSubstitution(
        leftId: String,
        rightId: String,
        leftSurface: String,
        rightSurface: String,
        source: Varna,
        replacement: List<Varna>,
        sutra: String,
    ): DerivationState = redistributeAdjacentTermsByVarnaSubstitution(
        leftId = leftId,
        rightId = rightId,
        leftSurface = leftSurface,
        rightSurface = rightSurface,
        source = source.devanagari.single(),
        replacement = replacement.toDevanagari(),
        sutra = sutra,
    )

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

    /** Varṇa-native whole-affix substitution; rendering occurs only at the term boundary. */
    fun replaceWholeAffix(
        id: String,
        varnas: List<Varna>,
        sutra: String,
        policy: WholeAffixDesignationPolicy,
        upadesha: String? = null,
        replacementId: String = id,
    ): DerivationState = replaceWholeAffix(
        id = id,
        surface = varnas.toDevanagari(),
        sutra = sutra,
        policy = policy,
        upadesha = upadesha,
        replacementId = replacementId,
    )

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

    fun blockOperation(operation: BlockedOperationDomain, blocker: String): DerivationState =
        copy(blockedOperations = blockedOperations + (operation to blocker))

    fun addComparison(comparison: VarnaComparison): DerivationState =
        copy(varnaComparisons = varnaComparisons + comparison)

    fun addSubstitution(substitution: VarnaSubstitution): DerivationState =
        copy(substitutions = substitutions + substitution)

    /** Records a phonological substitution without exposing Unicode serialization to a sūtra. */
    fun addVarnaSubstitution(
        targetId: String,
        source: Varna,
        replacement: List<Varna>,
        sutra: String,
        orthographicSigns: List<OrthographicSignPlacement> = emptyList(),
    ): DerivationState = addSubstitution(
        VarnaSubstitution(targetId, source.devanagari.single(), replacement.toDevanagari(orthographicSigns), sutra),
    )

    fun recordAppliedSutra(sutraNumber: String): DerivationState =
        copy(appliedSutras = appliedSutras + sutraNumber)

    fun assignSvara(vowelIndex: Int, accent: AccentType, sutra: String): DerivationState =
        copy(svaraAssignments = svaraAssignments + SvaraAssignment(vowelIndex, accent, SvaraAssignmentSource.Sutra(sutra)))

    fun copy(
        terms: List<DerivationTerm> = this.terms,
        droppedTerms: List<DerivationTerm> = this.droppedTerms,
        samjnas: Set<SamjnaAssignment> = this.samjnas,
        stage: DerivationStage = this.stage,
        context: DerivationalContext = this.context,
        activeAdhikaras: Set<String> = this.activeAdhikaras,
        inheritedAnuvrtti: Set<String> = this.inheritedAnuvrtti,
        blockedSutras: Map<String, String> = this.blockedSutras,
        blockedOperations: Map<BlockedOperationDomain, String> = this.blockedOperations,
        halantyamExemptTermIds: Set<String> = this.halantyamExemptTermIds,
        varnaComparisons: Set<VarnaComparison> = this.varnaComparisons,
        substitutions: List<VarnaSubstitution> = this.substitutions,
        appliedSutras: List<String> = this.appliedSutras,
        svaraNimittas: List<SvaraNimitta> = this.svaraNimittas,
        svaraAssignments: List<SvaraAssignment> = this.svaraAssignments,
        boundaryRuleBlocks: Map<BoundaryRuleKey, String> = this.boundaryRuleBlocks,
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
            blockedOperations = blockedOperations,
            halantyamExemptTermIds = halantyamExemptTermIds,
            varnaComparisons = varnaComparisons,
            substitutions = substitutions,
            appliedSutras = appliedSutras,
            svaraNimittas = svaraNimittas,
            svaraAssignments = svaraAssignments,
            boundaryRuleBlocks = boundaryRuleBlocks,
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
            blockedOperations == other.blockedOperations &&
            halantyamExemptTermIds == other.halantyamExemptTermIds &&
            varnaComparisons == other.varnaComparisons &&
            substitutions == other.substitutions
            && appliedSutras == other.appliedSutras &&
            svaraNimittas == other.svaraNimittas &&
            svaraAssignments == other.svaraAssignments && boundaryRuleBlocks == other.boundaryRuleBlocks
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
        result = 31 * result + blockedOperations.hashCode()
        result = 31 * result + halantyamExemptTermIds.hashCode()
        result = 31 * result + varnaComparisons.hashCode()
        result = 31 * result + substitutions.hashCode()
        result = 31 * result + appliedSutras.hashCode()
        result = 31 * result + svaraNimittas.hashCode()
        result = 31 * result + svaraAssignments.hashCode()
        result = 31 * result + boundaryRuleBlocks.hashCode()
        return result
    }

    override fun toString(): String {
        return "DerivationState(terms=$terms, droppedTerms=$droppedTerms, samjnas=$samjnas, stage=$stage, context=$context, activeAdhikaras=$activeAdhikaras, inheritedAnuvrtti=$inheritedAnuvrtti, blockedSutras=$blockedSutras, blockedOperations=$blockedOperations, halantyamExemptTermIds=$halantyamExemptTermIds, varnaComparisons=$varnaComparisons, substitutions=$substitutions, appliedSutras=$appliedSutras, svaraNimittas=$svaraNimittas, svaraAssignments=$svaraAssignments)"
    }
}

enum class BlockedOperationDomain { STRI_PRATYAYA_SELECTION }

data class BoundaryRuleKey(val sutra: String, val leftId: String, val rightId: String)

enum class AccentType { UDATTA, ANUDATTA, SVARITA }
enum class SvaraNimittaKind { PRATYAYA, NIT_OR_NGIT, PIT_OR_SUP, EXPLICIT_UDATTA }
data class SvaraNimitta(val kind: SvaraNimittaKind, val termId: String, val vowelIndex: Int? = null)
sealed interface SvaraAssignmentSource {
    data class Sutra(val number: String) : SvaraAssignmentSource
    data class Lexical(val source: String) : SvaraAssignmentSource
}
data class SvaraAssignment(val vowelIndex: Int, val accent: AccentType, val source: SvaraAssignmentSource)

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
    /** Exact occurrence for positional operations; null for legacy summary-only traces. */
    val sourceVarnaIndex: Int? = null,
) {
    /** Exact pre-operation term boundary used by asiddhavat visibility rollback. */
    var originalSurface: String? = null
        private set
    var originalOrthographicSigns: List<OrthographicSignPlacement>? = null
        private set

    constructor(
        targetId: String,
        source: Char,
        replacement: String,
        sutra: String,
        originalSurface: String?,
        originalOrthographicSigns: List<OrthographicSignPlacement>?,
        sourceVarnaIndex: Int? = null,
    ) : this(targetId, source, replacement, sutra, sourceVarnaIndex) {
        this.originalSurface = originalSurface
        this.originalOrthographicSigns = originalOrthographicSigns
    }
}

/** Explicit construction domain; never inferred from a term identifier. */
enum class TermCompositionDomain { SANKHYA }

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
    /** Persistent provenance for markers established by exact it-designations. */
    val itMarkerProvenance: Set<ItMarkerProvenance> = emptySet(),
    /** Accent stated by the lexical source, rather than assigned by an Aṣṭādhyāyī rule. */
    val lexicalAccent: Accent? = null,
    val lexicalAccentSource: String? = null,
    /** Zero-based vowel ordinal inside this term; null means the lexical source did not identify a usable locus. */
    val lexicalAccentVowelIndex: Int? = null,
    /** Surviving term that contains this consumed affix's vowel after a phonological merge. */
    val mergedIntoTermId: String? = null,
    /** Vowel ordinal counted from that surviving term's end, stable across changes before the locus. */
    val mergedAffixVowelFromEnd: Int? = null,
    /** Non-phonological written signs anchored to boundaries in [varnas]. */
    val orthographicSigns: List<OrthographicSignPlacement> = emptyList(),
    /** Affixes that produced an already-formed stem; never inferred from its spelling. */
    val sourceSuffixUpadeshas: Set<String> = emptySet(),
    /** Morphosyntax of a completed external pada, not the whole sandhi expression. */
    val formedPadaRupa: Rupa? = null,
    /** Original sup slot; survives ādeśa/lopa and is never inferred from a term ID or spelling. */
    val sourceSupAffix: dev.panini.core.SupAffix? = null,
    /** Original tiṅ slot, distinct from the current substitute's upadeśa. */
    val sourceTingAffix: dev.panini.core.TingAffix? = null,
    val compositionDomain: TermCompositionDomain? = null,
) {
    /**
     * Cached phonological form of [surface]. During the transition [surface]
     * remains the constructor boundary, but sūtras must reason over this field.
     * A data-class copy that changes [surface] creates a new term and therefore
     * a new cache, so the two representations cannot become stale.
     */
    val phonologicalText by lazy(LazyThreadSafetyMode.PUBLICATION) { surface.toSanskritText("term:$id") }
    val varnas: List<Varna> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        phonologicalText.effectiveVarnas.map { it.varna }
    }
    val upadeshaVarnas: List<Varna> by lazy(LazyThreadSafetyMode.PUBLICATION) { upadesha.toVarnas() }
    val compoundHeadVarnas: List<Varna>? by lazy(LazyThreadSafetyMode.PUBLICATION) {
        compoundHeadUpadesha?.toVarnas()
    }

    /** Exact written text for इत् provenance; this is orthographic bookkeeping, not phonological reasoning. */
    fun orthographicDesignationText(start: Int, endExclusive: Int): String =
        surface.substring(start, endExclusive)

    /** Projects an already-selected phonological occurrence into the legacy written इत् ledger.
     * Selection belongs to the rule; spelling repair and exact provenance belong to this boundary.
     */
    fun designateVarnaIt(index: Int, marker: ItMarker, sutra: String): ItDesignation {
        val token = phonologicalText.effectiveVarnas[index]
        val span = requireNotNull(token.sourceSpan) { "A parsed source span is required on $id." }
        return ItDesignation(
            start = span.start,
            endExclusive = span.endExclusive,
            marker = marker,
            sutra = sutra,
            designatedText = orthographicDesignationText(span.start, span.endExclusive),
            varnaIndices = setOf(index),
        )
    }

    /** Whole-affix substitution with an explicit map of surviving old occurrences to new positions.
     * Written spans are projected from the replacement parser, never shifted by character counts.
     */
    fun replaceWholeAffixWithVarnaMapping(
        replacementVarnas: List<Varna>,
        survivingPositions: Map<Int, Int>,
        consumedDesignations: Set<ItDesignation>,
        sutra: String,
    ): DerivationTerm {
        val originalTokens = phonologicalText.effectiveVarnas
        require(survivingPositions.keys.all { it in originalTokens.indices } &&
            survivingPositions.values.all { it in replacementVarnas.indices }) {
            "$sutra has an out-of-range surviving varṇa mapping on $id."
        }
        require(survivingPositions.values.distinct().size == survivingPositions.size) {
            "$sutra must map surviving varṇas to distinct replacement positions on $id."
        }
        val orderedPositions = survivingPositions.toSortedMap().values.toList()
        require(orderedPositions == orderedPositions.sorted()) {
            "$sutra must preserve the order of surviving varṇas on $id."
        }
        val sourceForTarget = survivingPositions.entries.associate { (source, target) -> target to source }
        val annotatedReplacement = replacementVarnas.mapIndexed { index, varna ->
            sourceForTarget[index]?.let { originalTokens[it].copy(varna = varna) }
                ?: dev.panini.shiksha.VarnaToken(dev.panini.shiksha.VarnaTokenId("replacement:$id:$sutra:$index"), varna)
        }
        val signs = (orthographicSigns + phonologicalText.sourceOrthographicSigns).distinct().mapNotNull { placement ->
            val oldBoundary = placement.afterVarnaCount
            val boundary = when {
                placement.sign == OrthographicSign.CHANDRABINDU ->
                    survivingPositions[oldBoundary - 1]?.plus(1) ?: return@mapNotNull null
                oldBoundary == 0 -> 0
                oldBoundary == originalTokens.size -> replacementVarnas.size
                survivingPositions.containsKey(oldBoundary - 1) -> survivingPositions.getValue(oldBoundary - 1) + 1
                else -> survivingPositions.toSortedMap().entries.firstOrNull { it.key >= oldBoundary }?.value
                    ?: replacementVarnas.size
            }
            placement.copy(afterVarnaCount = boundary)
        }
        val replacementSurface = SanskritText(annotatedReplacement).renderWithOrthographicSigns(signs)
        val replacementTokens = replacementSurface.toSanskritText().effectiveVarnas
        val all = itDesignations + deferredItDesignations
        require(consumedDesignations.all { it in all }) { "$sutra cannot consume an unknown designation on $id." }
        val remaps = all.filterNot { it in consumedDesignations }.map { designation ->
            val indices = designation.varnaIndices.mapTo(mutableSetOf()) {
                requireNotNull(survivingPositions[it]) { "$sutra must remap or consume designated varṇa $it on $id." }
            }
            val spans = indices.map { requireNotNull(replacementTokens[it].sourceSpan) }
            ItDesignationRemap(designation.start, designation.endExclusive,
                spans.minOf { it.start }, spans.maxOf { it.endExclusive }, indices)
        }
        return replaceWholeAffix(replacementSurface, upadesha, sutra,
            WholeAffixDesignationPolicy.PreserveAndRemap(remaps,
                consumedDesignations.map { ItDesignationConsumption(it.start, it.endExclusive) })).copy(orthographicSigns = signs)
    }

    /** Inserts an annotated term at a phonological boundary and reprojects both इत् ledgers.
     * [preserveMemberBoundary] retains existing written member forms for an outer insertion;
     * it is a rendering choice, never an insertion-point calculation.
     */
    fun insertDesignatedTerm(
        insertion: DerivationTerm,
        beforeVarnaIndex: Int,
        preserveMemberBoundary: Boolean = false,
    ): DerivationTerm {
        val targetTokens = phonologicalText.effectiveVarnas
        val insertedTokens = insertion.phonologicalText.effectiveVarnas
        require(beforeVarnaIndex in 0..targetTokens.size)
        val combined = targetTokens.take(beforeVarnaIndex) + insertedTokens + targetTokens.drop(beforeVarnaIndex)
        val signs = ((orthographicSigns + phonologicalText.sourceOrthographicSigns).distinct().map {
            it.copy(afterVarnaCount = if (it.afterVarnaCount > beforeVarnaIndex)
                it.afterVarnaCount + insertedTokens.size else it.afterVarnaCount)
        } + (insertion.orthographicSigns + insertion.phonologicalText.sourceOrthographicSigns).distinct().map {
            it.copy(afterVarnaCount = it.afterVarnaCount + beforeVarnaIndex)
        }).distinct()
        val rendered = if (preserveMemberBoundary) {
            require(beforeVarnaIndex == 0 || beforeVarnaIndex == targetTokens.size)
            if (beforeVarnaIndex == 0) insertion.surface + surface else surface + insertion.surface
        } else SanskritText(combined).renderWithOrthographicSigns(signs)
        val parsed = rendered.toSanskritText().effectiveVarnas
        require(parsed.map { it.varna } == combined.map { it.varna }) { "Insertion must preserve the exact varṇa sequence on $id." }
        fun project(designations: List<ItDesignation>, position: (Int) -> Int) = designations.map { designation ->
            val indices = designation.varnaIndices.mapTo(mutableSetOf(), position)
            val spans = indices.map { requireNotNull(parsed[it].sourceSpan) }
            val start = spans.minOf { it.start }
            val end = spans.maxOf { it.endExclusive }
            designation.copy(start = start, endExclusive = end,
                designatedText = rendered.substring(start, end), varnaIndices = indices)
        }
        fun targetPosition(index: Int) = if (index >= beforeVarnaIndex) index + insertedTokens.size else index
        fun insertedPosition(index: Int) = index + beforeVarnaIndex
        return copy(
            surface = rendered,
            orthographicSigns = signs,
            itDesignations = project(itDesignations, ::targetPosition) + project(insertion.itDesignations, ::insertedPosition),
            deferredItDesignations = project(deferredItDesignations, ::targetPosition) + project(insertion.deferredItDesignations, ::insertedPosition),
        )
    }

    /** Deletes exactly the designated phonological occurrences, validating written provenance first. */
    fun lopaOfDesignatedVarnas(designations: List<ItDesignation>): DerivationTerm {
        designations.forEach { designation ->
            require(designation.start >= 0 && designation.endExclusive <= surface.length &&
                orthographicDesignationText(designation.start, designation.endExclusive) == designation.designatedText) {
                "1.3.9 cannot delete stale designation ${designation.start}..${designation.endExclusive} " +
                    "(${designation.designatedText}) on $id:$surface; the substituting rule must remap or consume it."
            }
        }
        val tokens = phonologicalText.effectiveVarnas
        val deleted = designations.flatMapTo(mutableSetOf()) { it.varnaIndices }
        require(deleted.all { it in tokens.indices }) { "1.3.9 has an out-of-range varṇa designation on $id." }
        val surviving = tokens.filterIndexed { index, _ -> index !in deleted }
        val signs = (orthographicSigns + phonologicalText.sourceOrthographicSigns).distinct().mapNotNull { placement ->
            if (placement.sign == dev.panini.shiksha.OrthographicSign.CHANDRABINDU &&
                placement.afterVarnaCount - 1 in deleted) null
            else placement.copy(afterVarnaCount = placement.afterVarnaCount - deleted.count { it < placement.afterVarnaCount })
        }
        // Nasalization is already carried by surviving vowel tokens, not emitted twice as a sign.
        val renderedSigns = signs.filterNot { it.sign == dev.panini.shiksha.OrthographicSign.CHANDRABINDU &&
            surviving.getOrNull(it.afterVarnaCount - 1)?.nasalized == true }
        val rendered = buildString {
            var start = 0
            renderedSigns.sortedBy { it.afterVarnaCount }.groupBy { it.afterVarnaCount }.forEach { (boundary, placements) ->
                append(SanskritText(surviving.subList(start, boundary)).render())
                placements.forEach { append(it.sign.devanagari) }
                start = boundary
            }
            append(SanskritText(surviving.subList(start, surviving.size)).render())
        }
        return copy(surface = rendered, orthographicSigns = signs)
    }

    /** UTF-16 boundary after the first phonological varṇa in the written upadeśa. */
    fun orthographicEndAfterInitialVarna(): Int {
        require(varnas.isNotEmpty()) { "An initial varṇa is required for $id." }
        return if (varnas.first() is dev.panini.shiksha.Vyanjana && varnas.getOrNull(1) == dev.panini.shiksha.Svara.A) 1
        else listOf(varnas.first()).toDevanagari().length
    }

    /** UTF-16 boundary before the final phonological varṇa in the written upadeśa. */
    fun orthographicStartOfFinalVarna(): Int {
        require(varnas.isNotEmpty()) { "A final varṇa is required for $id." }
        return surface.length - listOf(varnas.last()).toDevanagari().length
    }

    /** UTF-16 boundary after [index], derived from the canonical phonological prefix. */
    fun orthographicBoundaryAfterVarna(index: Int): Int {
        require(index in varnas.indices) { "Varṇa index $index is outside $id." }
        return varnas.take(index + 1).toDevanagari().length
    }

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
            lexicalAccent = dhatu.svara,
            lexicalAccentSource = "Dhātupāṭha:${dhatu.id}",
            lexicalAccentVowelIndex = DevanagariVowelLoci.positions(dhatu.derivationalSurface)
                .takeIf { it.size == 1 }
                ?.let { 0 },
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
                    return designation.copy(start = remap.newStart, endExclusive = remap.newEndExclusive,
                        designatedText = newText, varnaIndices = remap.newVarnaIndices)
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
                itMarkerProvenance = emptySet(),
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
    val newVarnaIndices: Set<Int>,
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
    val marker: ItMarker,
    val sutra: String,
    /** Original designated segment; detects a designation consumed by a later whole-term substitution. */
    val designatedText: String,
    /** Exact phonological occurrences designated for lopa; written spans are provenance only. */
    val varnaIndices: Set<Int>,
) {
    init {
        require(varnaIndices.isNotEmpty() && varnaIndices.all { it >= 0 })
    }
}

data class ItMarkerProvenance(
    val marker: ItMarker,
    val designationSutra: String,
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
