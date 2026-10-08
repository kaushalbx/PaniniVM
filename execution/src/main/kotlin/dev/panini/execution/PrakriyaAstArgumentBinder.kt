package dev.panini.execution

import dev.panini.execution.binding.NumeralPadaBinder
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

    fun bind(node: ProgramNode, parameters: List<PrakriyaParameter>, argumentCount: Int): ProgramNode {
        val names = parameters.mapIndexed { index, parameter -> parameter.nameStem to index }.toMap()
        fun ordinalIndex(pada: Pada): Int? =
            (PuranaPratyayaResolver.ordinalValue(pada) ?: NumeralPadaBinder.extractOrdinalValue(pada))
                ?.takeIf { it in 1L..argumentCount.toLong() }?.let { (it - 1).toInt() }
        fun parameterIndex(pada: SubantaPada): Int? {
            val stem = pada.pratipadika.semanticKey()
            return names[stem] ?: ordinalIndex(pada)
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
                if (
                    (pada.pratipadika as? MulaPratipadika)?.lexicalIdentity ==
                    MulaPratipadikaIdentity.SAMAVAYA && argumentCount > 0
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
            val protected = java.util.Collections.newSetFromMap(java.util.IdentityHashMap<Pada, Boolean>())
            protected.addAll(dev.panini.execution.binding.KarakaReferenceResolver.protectedPadas(originalPadas))
            dev.panini.execution.binding.NamedActionResultReferenceResolver.resolve(originalPadas).forEach { reference ->
                protected.add(reference.result)
                protected.add(reference.modifier)
                if (reference.hasOrderingQualifier) {
                    protected.addAll(reference.orderingQualifiers)
                }
            }
            return originalPadas.map { if (it in protected) it else bindPada(it) }
        }
        fun bindInvocation(node: Invocation): Invocation {
            val padas = bindPadas(node.vakya.padas)
            val source = padas.joinToString(" ") { it.sourceText }
            val vakya = when (val original = node.vakya) {
                is AkhyataVakya -> original.copy(sourceText = source, padas = padas)
                is NamaVakya -> original.copy(sourceText = source, padas = padas)
            }
            return node.copy(vakya = vakya)
        }
        return object : ProgramNodeTransformer() {
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
