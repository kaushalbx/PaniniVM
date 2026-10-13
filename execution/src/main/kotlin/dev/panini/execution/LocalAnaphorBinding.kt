package dev.panini.execution

import dev.panini.vyakaranam.ast.*
import dev.panini.execution.binding.referenceKey

/** Shared invocation/predicate binding; aliases retain live antecedent identity. */
internal object LocalAnaphorBinding {
    data class Bound(val invocation: Invocation, val scope: ExecutionScope)
    data class BoundProgram(val program: ProgramNode, val scope: ExecutionScope)

    /** One alias per identity lets runtime assignment maps serve later sequence stages. */
    fun resolveLinear(node: ProgramNode, scope: ExecutionScope, operations: Set<String> = emptySet()): BoundProgram {
        fun linear(program: ProgramNode): Boolean = program is Invocation ||
            program is Sequence && program.statements.all(::linear)
        if (!linear(node)) return BoundProgram(node, scope)
        val candidates = scope.procedureBindings?.anaphoricReferents().orEmpty().map {
            AgreementCandidate(it.identity, ReferentAgreement(it.grammaticalNumbers, it.grammaticalGenders))
        }
        var prefix = "__pvm_anaphor_"
        while (scope.procedureBindings?.names.orEmpty().any { it.startsWith(prefix) } ||
            scope.environment.values.keys.any { it.startsWith(prefix) }) prefix += "_"
        val aliases = linkedMapOf<LocalReferentIdentity, String>()
        fun project(program: ProgramNode): ProgramNode = when (program) {
            is Invocation -> AnaphoricOperandProjection.project(program, candidates,
                protectedPadas = PrakriyaAstArgumentBinder.protectedParameterPadas(program.vakya.padas, operations),
                referenceName = { identity -> aliases.getOrPut(identity) { "$prefix${aliases.size}" } }).invocation
            is Sequence -> program.copy(statements = program.statements.map(::project))
            else -> program
        }
        val projected = project(node)
        if (aliases.isEmpty()) return BoundProgram(node, scope)
        fun unify(program: ProgramNode): ProgramNode = when (program) {
            is Invocation -> {
                val protected = PrakriyaAstArgumentBinder.protectedParameterPadas(program.vakya.padas, operations)
                fun nominal(pada: SubantaPada): SubantaPada {
                    if (protected.any { it === pada }) return pada
                    val identity = scope.procedureBindings?.identity(pada.pratipadika.referenceKey()) ?: return pada
                    val name = aliases[identity] ?: return pada
                    return pada.copy(sourceText = "$name + ${pada.sup.text}", pratipadika = MulaPratipadika(name, name))
                }
                val padas = program.vakya.padas.map { pada ->
                    if (protected.any { it === pada }) pada else when (pada) {
                        is SubantaPada -> nominal(pada)
                        is SamuccitaSubanta -> pada.copy(members = pada.members.map { if (it is SubantaPada) nominal(it) else it })
                        else -> pada
                    }
                }
                val vakya = when (val original = program.vakya) {
                    is AkhyataVakya -> original.copy(padas = padas)
                    is NamaVakya -> original.copy(padas = padas)
                }
                program.copy(vakya = vakya, implicitValuePada = program.implicitValuePada?.let {
                    if (it is SubantaPada) nominal(it) else it
                })
            }
            is Sequence -> program.copy(statements = program.statements.map(::unify))
            else -> program
        }
        val bindings = requireNotNull(scope.procedureBindings).withReferentAliases(
            aliases.entries.associate { (identity, name) -> name to identity })
        return BoundProgram(unify(projected), scope.copy(procedureBindings = bindings,
            environment = scope.environment.mergedWith(ValueEnvironment.live(bindings.values))))
    }

    fun resolve(node: Invocation, scope: ExecutionScope, operations: Set<String> = emptySet()): Bound {
        val referents = scope.procedureBindings?.anaphoricReferents().orEmpty()
        var prefix = "__pvm_anaphor_"
        while (scope.procedureBindings?.names.orEmpty().any { it.startsWith(prefix) } ||
            scope.environment.values.keys.any { it.startsWith(prefix) }) prefix += "_"
        val projection = AnaphoricOperandProjection.project(node, referents.map {
            AgreementCandidate(it.identity, ReferentAgreement(it.grammaticalNumbers, it.grammaticalGenders))
        }, prefix = prefix, protectedPadas = PrakriyaAstArgumentBinder.protectedParameterPadas(
            node.vakya.padas, operations))
        if (projection.operands.isEmpty()) return Bound(node, scope)
        val bindings = requireNotNull(scope.procedureBindings).withReferentAliases(
            projection.operands.associate { it.name to it.identity })
        return Bound(projection.invocation, scope.copy(procedureBindings = bindings,
            environment = scope.environment.mergedWith(ValueEnvironment.live(bindings.values))))
    }
}
