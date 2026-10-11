package dev.panini.execution

import dev.panini.execution.binding.NumeralPadaBinder
import dev.panini.execution.binding.referenceKey
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.NamaVakya
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.ProgramNodeTransformer
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.WhileLoop
import dev.panini.vyakaranam.ast.semanticKey

/** Rebinds prakriyā placeholders in the already-parsed body AST. */
object PrakriyaAstArgumentBinder {
    private const val KEY_PREFIX = "__pvm_parameter_"

    fun referenceKey(index: Int): String = "$KEY_PREFIX$index"

    /** Nominals with an established member/history/call role are not positional placeholders. */
    fun protectedParameterPadas(padas: List<Pada>, knownOperationStems: Set<String> = emptySet()): Set<Pada> {
        val protected = java.util.Collections.newSetFromMap(java.util.IdentityHashMap<Pada, Boolean>())
        val referencePadas = padas.flatMap { if (it is SamuccitaSubanta) it.members else listOf(it) }
        protected.addAll(CollectionMemberMorphology.protectedPadas(referencePadas))
        protected.addAll(dev.panini.execution.binding.KarakaReferenceResolver.protectedPadas(referencePadas))
        dev.panini.execution.binding.NamedActionResultReferenceResolver.resolve(referencePadas).forEach { reference ->
            protected.add(reference.result)
            protected.add(reference.modifier)
            if (reference.hasOrderingQualifier) protected.addAll(reference.orderingQualifiers)
        }
        val verb = padas.filterIsInstance<dev.panini.vyakaranam.ast.TingantaPada>().singleOrNull()
        if (verb != null && knownOperationStems.isNotEmpty()) {
            val text = padas.joinToString(" ") { it.sourceText }
            val ukti = dev.panini.vyakaranam.ast.Ukti(sourceText = text,
                body = Invocation(AkhyataVakya(text, padas, verb)))
            PrakriyaInvocationMatcher.match(ukti, knownOperationStems)?.let { call ->
                padas.filterTo(protected) { pada ->
                    val members = (pada as? SamuccitaSubanta)?.members ?: listOf(pada)
                    members.none { member -> call.argumentPadas.any { it === member } }
                }
            }
        }
        return protected
    }

    fun bind(node: ProgramNode, parameters: List<PrakriyaParameter>, argumentCount: Int,
        knownOperationStems: Set<String> = emptySet(),
        preserveInvocation: (Invocation) -> Boolean = { false }): ProgramNode {
        val names = parameters.mapIndexed { index, parameter -> parameter.nameStem to index }.toMap()
        val localNames = mutableSetOf<String>()
        fun ordinalIndex(pada: Pada): Int? =
            (PuranaPratyayaResolver.ordinalValue(pada) ?: NumeralPadaBinder.extractOrdinalValue(pada))
                ?.takeIf { it in 1L..argumentCount.toLong() }?.let { (it - 1).toInt() }
        fun parameterIndex(pada: SubantaPada): Int? {
            val stem = pada.pratipadika.referenceKey()
            if (stem in localNames) return null
            val named = names[stem]
            return named ?: ordinalIndex(pada)
        }
        fun reference(pada: SubantaPada, index: Int): SubantaPada {
            val key = referenceKey(index)
            return pada.copy(
                sourceText = "$key + ${pada.sup.text}",
                pratipadika = MulaPratipadika(key, key),
            )
        }
        fun reference(pada: SankhyaPuranaPada, index: Int): SubantaPada {
            val key = referenceKey(index)
            return SubantaPada(
                sourceText = "$key + ${pada.sup.text}",
                pratipadika = MulaPratipadika(key, key),
                sup = pada.sup,
            )
        }
        fun reference(pada: SankhyaPada, index: Int): SubantaPada {
            val key = referenceKey(index)
            return SubantaPada(
                sourceText = "$key + ${pada.sup.text}",
                pratipadika = MulaPratipadika(key, key),
                sup = pada.sup,
            )
        }
        fun bindPada(pada: Pada): Pada = when (pada) {
            is SankhyaPada -> {
                val index = ordinalIndex(pada)
                index?.let { reference(pada, it) } ?: pada
            }
            is SankhyaPuranaPada -> {
                val index = ordinalIndex(pada)
                index?.let { reference(pada, it) } ?: pada
            }
            is SubantaPada -> {
                val nominal = pada.pratipadika as? MulaPratipadika
                if (
                    nominal?.lexicalIdentity == MulaPratipadikaIdentity.SAMAVAYA &&
                    nominal.vikaras.isEmpty() && pada.sup.text == dev.panini.core.SupAffix.AM.upadesha &&
                    pada.pratipadika.semanticKey() !in names && argumentCount > 0
                ) {
                    val members = (0 until argumentCount).map { reference(pada, it) }
                    SamuccitaSubanta(members.joinToString(" ") { it.sourceText }, members)
                } else {
                    parameterIndex(pada)?.let { reference(pada, it) } ?: pada
                }
            }
            is SamuccitaSubanta -> {
                val members = pada.members.map { member ->
                    parameterIndex(member)?.let { reference(member, it) } ?: member
                }
                pada.copy(sourceText = members.joinToString(" ") { it.sourceText }, members = members)
            }
            else -> pada
        }
        fun bindPadas(originalPadas: List<Pada>): List<Pada> {
            // An ordinal modifying a named फल or kāraka selects discourse history, not
            // the corresponding procedure parameter. Preserve that whole relation.
            val protected = protectedParameterPadas(originalPadas, knownOperationStems)
            return originalPadas.map { if (it in protected) it else bindPada(it) }
        }
        fun bindInvocation(node: Invocation): Invocation {
            if (preserveInvocation(node)) return node
            val padas = bindPadas(node.vakya.padas)
            val source = padas.joinToString(" ") { it.sourceText }
            val vakya = when (val original = node.vakya) {
                is AkhyataVakya -> original.copy(sourceText = source, padas = padas)
                is NamaVakya -> original.copy(sourceText = source, padas = padas)
            }
            return node.copy(vakya = vakya, implicitValuePada = node.implicitValuePada?.let(::bindPada))
        }
        return object : ProgramNodeTransformer() {
            override fun visitForEach(node: dev.panini.vyakaranam.ast.ForEach): ProgramNode {
                val collection = bindPada(node.collection) as SubantaPada
                val name = node.member.pratipadika.referenceKey()
                val added = localNames.add(name)
                return try { node.copy(collection = collection, body = transform(node.body)) }
                finally { if (added) localNames.remove(name) }
            }
            override fun visitInvocation(node: Invocation): ProgramNode = bindInvocation(node)

            override fun visitQuotation(node: Quotation): ProgramNode = node.copy(
                quoted = bindInvocation(node.quoted),
                reporting = transform(node.reporting),
            )

            override fun visitConditional(node: Conditional): ProgramNode = node.copy(
                condition = transform(node.condition),
                consequent = transform(node.consequent),
                alternate = node.alternate?.let(::transform),
                surfacePipelineTarget = node.surfacePipelineTarget?.let(::transform),
            )

            override fun visitWhileLoop(node: WhileLoop): ProgramNode = node.copy(
                condition = bindInvocation(node.condition),
                body = transform(node.body),
                exhausted = node.exhausted?.let(::transform),
                resultTarget = node.resultTarget?.let(::transform),
            )

            override fun visitPipeline(node: Pipeline): ProgramNode {
                val boundPadas = bindPadas(node.argumentPadas)
                return node.copy(
                    arguments = node.arguments.mapIndexed { index, argument ->
                        val original = node.argumentPadas.getOrNull(index)
                        val bound = boundPadas.getOrNull(index)
                        if (bound === original) argument
                        else (bound as? SubantaPada)?.pratipadika?.semanticKey() ?: argument
                    },
                    argumentPadas = boundPadas,
                    renderPadas = bindPadas(node.renderPadas),
                )
            }
        }.transform(node)
    }
}
