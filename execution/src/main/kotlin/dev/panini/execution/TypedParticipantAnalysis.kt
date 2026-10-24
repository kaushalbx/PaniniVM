package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vacana
import dev.panini.execution.binding.NominalModifierAttachment
import dev.panini.execution.binding.referenceKey
import dev.panini.vyakaranam.ast.*

/** A position within a semantic input category, not a position in the whole call. */
data class TypedParticipantSlot(val type: PrakriyaValueType, val ordinal: Long) {
    init {
        require(type in setOf(PrakriyaValueType.SANKHYA, PrakriyaValueType.SHABDA, PrakriyaValueType.SUCHI))
        require(ordinal > 0)
    }
}

/** Occurrence indices refer to the original scope, including repeated equal-looking nouns. */
data class TypedParticipantCandidate(
    val slot: TypedParticipantSlot,
    val modifierIndex: Int,
    val headIndex: Int,
    val agreeingSlots: Set<SupAffix>,
    val knownGenderAgreement: Boolean,
)

enum class TypedParticipantDiagnostic { NO_AGREEING_TYPE_HEAD, AMBIGUOUS_TYPE_HEADS, SHARED_TYPE_HEAD }

data class TypedParticipantAttachment(
    val modifierIndex: Int,
    val candidates: List<TypedParticipantCandidate>,
    val diagnostic: TypedParticipantDiagnostic?,
) {
    val unique: TypedParticipantCandidate? get() = candidates.singleOrNull().takeIf { diagnostic == null }
}

/**
 * Morphological analysis only: no source-text matching and no nearest-word tie breaker.
 * The caller supplies the grammatical phrase scope and already established roles.
 * A collection member's ordinal must be excluded by its whole/member relation before
 * this analysis; a genitive noun alone is not sufficient evidence for that relation.
 */
object TypedParticipantAnalysis {
    fun parameterName(slot: TypedParticipantSlot): String = "__pvm_typed_${slot.type.name}_${slot.ordinal}"

    fun references(padas: List<Pada>, knownOperationStems: Set<String> = emptySet(),
        localNames: Set<String> = emptySet()): List<TypedParticipantCandidate> {
        val protected = PrakriyaAstArgumentBinder.protectedParameterPadas(padas, knownOperationStems)
        val attachments = analyze(padas, padas.indices.filterTo(mutableSetOf()) {
            padas[it] in protected || (padas[it] as? SubantaPada)?.pratipadika?.referenceKey() in localNames
        })
        return attachments.map { attachment ->
            requireNotNull(attachment.unique) { "Invalid typed participant attachment: ${attachment.diagnostic}." }
        }
    }

    private fun occurrences(body: List<PvmScriptStatement.Sentence>): List<Pair<TypedParticipantSlot, SubantaPada>> {
        val occurrences = mutableListOf<Pair<TypedParticipantSlot, SubantaPada>>()
        val localNames = mutableSetOf<String>()
        fun collect(padas: List<Pada>) {
            occurrences += references(padas, localNames = localNames).map { it.slot to (padas[it.headIndex] as SubantaPada) }
            padas.filterIsInstance<SamuccitaSubanta>().forEach { group -> group.conjuncts().forEach(::collect) }
        }
        val collector = object : ProgramNodeTransformer() {
            override fun visitInvocation(node: Invocation): ProgramNode {
                collect(node.vakya.padas)
                return node
            }
            override fun visitPipeline(node: Pipeline): ProgramNode {
                collect(node.argumentPadas)
                return node
            }
            override fun visitWhileLoop(node: WhileLoop): ProgramNode {
                transform(node.condition)
                return super.visitWhileLoop(node)
            }
            override fun visitQuotation(node: Quotation): ProgramNode {
                transform(node.quoted)
                return super.visitQuotation(node)
            }
            override fun visitConditional(node: Conditional): ProgramNode {
                node.surfacePipelineTarget?.let(::transform)
                return super.visitConditional(node)
            }
            override fun visitForEach(node: ForEach): ProgramNode {
                val names = (listOfNotNull(node.member, node.index) +
                    node.relativeSelections.map { it.binding })
                    .map { it.pratipadika.referenceKey() }
                val added = names.filter(localNames::add)
                return try { super.visitForEach(node) }
                finally { added.forEach(localNames::remove) }
            }
            override fun visitQuantifiedCollectionPredicate(
                node: dev.panini.vyakaranam.ast.QuantifiedCollectionPredicate,
            ): ProgramNode {
                val name = node.member.pratipadika.referenceKey()
                val added = localNames.add(name)
                return try { super.visitQuantifiedCollectionPredicate(node) }
                finally { if (added) localNames.remove(name) }
            }
        }
        body.filterNot { PrakriyaSignatureDeclarationParser.isDeclaration(it) }
            .forEach { sentence -> sentence.program?.let(collector::transform) }
        return occurrences
    }

    fun declarations(body: List<PvmScriptStatement.Sentence>): Map<String, SubantaPada> =
        occurrences(body).associate { (slot, head) -> parameterName(slot) to head }

    fun inferParameters(body: List<PvmScriptStatement.Sentence>): List<PrakriyaParameter> {
        val slots = occurrences(body).map { it.first }.distinct()
        val highest = slots.groupBy { it.type }.mapValues { (_, slots) -> slots.maxOf { it.ordinal } }
        require(highest.values.sumOf { it.coerceAtMost(4097) } <= 4096) {
            "Typed participant inference exceeds the analysis budget of 4096 slots."
        }
        return highest.toSortedMap(compareBy { it.ordinal }).flatMap { (type, count) ->
            (1L..count).map { ordinal ->
                val slot = TypedParticipantSlot(type, ordinal)
                PrakriyaParameter(parameterName(slot), type, ordinal)
            }
        }
    }

    fun typeOf(pada: Pada): PrakriyaValueType? {
        val stem = (pada as? SubantaPada)?.pratipadika as? MulaPratipadika ?: return null
        if (stem.vikaras.isNotEmpty()) return null
        return when (stem.lexicalIdentity) {
            MulaPratipadikaIdentity.SANKHYA -> PrakriyaValueType.SANKHYA
            MulaPratipadikaIdentity.SHABDA -> PrakriyaValueType.SHABDA
            MulaPratipadikaIdentity.SUCHI -> PrakriyaValueType.SUCHI
            else -> null
        }
    }

    fun analyze(padas: List<Pada>, protectedIndices: Set<Int> = emptySet()): List<TypedParticipantAttachment> {
        val heads = padas.indices.filter { it !in protectedIndices && typeOf(padas[it]) != null }
        if (heads.isEmpty()) return emptyList()
        val attachments = padas.mapIndexedNotNull { modifierIndex, modifier ->
            if (modifierIndex in protectedIndices) return@mapIndexedNotNull null
            // A parameter modifier is grammatical evidence, not merely a rank
            // hint: realize it to validate its source ordinal derivation.
            val ordinal = PuranaPratyayaResolver.semanticValue(modifier)?.value ?: return@mapIndexedNotNull null
            val attachment = NominalModifierAttachment.analyze(modifier, heads.map(padas::get))
            val candidates = attachment.candidates.mapNotNull { candidate ->
                val singularSlots = candidate.slots.filterTo(linkedSetOf()) { it.vacana == Vacana.EKAVACANA }
                if (singularSlots.isEmpty()) return@mapNotNull null
                val headIndex = heads[candidate.headIndex]
                TypedParticipantCandidate(TypedParticipantSlot(requireNotNull(typeOf(padas[headIndex])), ordinal),
                    modifierIndex, headIndex, singularSlots, candidate.knownGenderAgreement)
            }
            TypedParticipantAttachment(modifierIndex, candidates, when (candidates.size) {
                0 -> TypedParticipantDiagnostic.NO_AGREEING_TYPE_HEAD
                1 -> null
                else -> TypedParticipantDiagnostic.AMBIGUOUS_TYPE_HEADS
            })
        }
        // Two individually compatible ordinals cannot each claim the same noun
        // occurrence as an independently inferred input. Preserve the evidence,
        // but require phrase-level ownership instead of manufacturing two slots.
        val sharedHeads = attachments.mapNotNull { it.unique?.headIndex }
            .groupingBy { it }.eachCount().filterValues { it > 1 }.keys
        return attachments.map { attachment ->
            if (attachment.unique?.headIndex in sharedHeads)
                attachment.copy(diagnostic = TypedParticipantDiagnostic.SHARED_TYPE_HEAD)
            else attachment
        }
    }
}
