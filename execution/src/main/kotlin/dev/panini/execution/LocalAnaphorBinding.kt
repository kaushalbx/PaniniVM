package dev.panini.execution

import dev.panini.vyakaranam.ast.*
import dev.panini.execution.binding.referenceKey

/** Shared invocation/predicate binding; aliases retain live antecedent identity. */
internal object LocalAnaphorBinding {
    data class Bound(val invocation: Invocation, val scope: ExecutionScope)
    data class BoundProgram(val program: ProgramNode, val scope: ExecutionScope)
    sealed interface Diagnostic : SemanticDiagnostic<String> {

        data class Anaphoric(
            val detail: AnaphoricOperandProjection.Diagnostic<LocalReferentIdentity>,
        ) : Diagnostic {
            override val code: String get() = detail.code.name
            override val message: String get() = detail.message
            override val span: DocumentSourceSpan? get() = detail.span
            override val offendingText: String get() = detail.offendingText
        }

        data class InvalidBinding(
            override val message: String,
            override val span: DocumentSourceSpan? = null,
            override val offendingText: String = "",
        ) : Diagnostic {
            override val code: String = "INVALID_BINDING"
        }
    }

    fun Diagnostic.render(): String = buildString {
        append(code).append(": ").append(message)
        span?.let { append(" at offsets ").append(it.start).append("..").append(it.endExclusive) }
        if (offendingText.isNotEmpty()) append(" [").append(offendingText).append(']')
    }

    fun Diagnostic.executionDiagnostic(): ExecutionDiagnostic = ExecutionDiagnostic.from(this)
    sealed interface Analysis<out T> {
        data class Resolved<T>(val value: T) : Analysis<T>
        data class Invalid(val diagnostic: Diagnostic) : Analysis<Nothing>
    }

    /** One alias per identity lets runtime assignment maps serve later sequence stages. */
    fun analyzeLinear(node: ProgramNode, scope: ExecutionScope,
        operations: Set<String> = emptySet()): Analysis<BoundProgram> = try {
        analyzeLinearUnchecked(node, scope, operations)
    } catch (failure: IllegalArgumentException) {
        val invocations = node.grammaticalInvocations()
        val spans = invocations.mapNotNull(Invocation::relationSpan)
        Analysis.Invalid(Diagnostic.InvalidBinding(
            failure.message ?: "Invalid local binding.",
            spans.takeIf { it.isNotEmpty() }?.let { DocumentSourceSpan(
                it.minOf(DocumentSourceSpan::start), it.maxOf(DocumentSourceSpan::endExclusive)) },
            node.sourceText,
        ))
    }

    private fun analyzeLinearUnchecked(node: ProgramNode, scope: ExecutionScope,
        operations: Set<String>): Analysis<BoundProgram> {
        fun linear(program: ProgramNode): Boolean = program is Invocation ||
            program is Sequence && program.statements.all(::linear)
        if (!linear(node)) return Analysis.Resolved(BoundProgram(node, scope))
        val candidates = scope.procedureBindings?.anaphoricReferents().orEmpty().map {
            AgreementCandidate(it.identity, ReferentAgreement(it.grammaticalNumbers, it.grammaticalGenders))
        }
        var prefix = "__pvm_anaphor_"
        while (scope.procedureBindings?.names.orEmpty().any { it.startsWith(prefix) } ||
            scope.environment.values.keys.any { it.startsWith(prefix) }) prefix += "_"
        val aliases = linkedMapOf<LocalReferentIdentity, String>()
        var invalid: AnaphoricOperandProjection.Diagnostic<LocalReferentIdentity>? = null
        fun project(program: ProgramNode): ProgramNode = when (program) {
            is Invocation -> when (val analysis = AnaphoricOperandProjection.analyze(program, candidates,
                protectedPadas = PrakriyaAstArgumentBinder.protectedParameterPadas(program.vakya.padas, operations),
                referenceName = { identity -> aliases.getOrPut(identity) { "$prefix${aliases.size}" } })) {
                is AnaphoricOperandProjection.Analysis.Resolved -> analysis.projection.invocation
                is AnaphoricOperandProjection.Analysis.Invalid -> {
                    invalid = analysis.diagnostic
                    program
                }
            }
            is Sequence -> program.copy(statements = program.statements.map(::project))
            else -> program
        }
        val projected = project(node)
        invalid?.let { return Analysis.Invalid(Diagnostic.Anaphoric(it)) }
        if (aliases.isEmpty()) return Analysis.Resolved(BoundProgram(node, scope))
        fun unify(program: ProgramNode): ProgramNode = when (program) {
            is Invocation -> {
                val protected = PrakriyaAstArgumentBinder.protectedParameterPadas(program.vakya.padas, operations)
                fun nominal(pada: SubantaPada): SubantaPada {
                    if (protected.any { it === pada }) return pada
                    val identity = scope.procedureBindings?.identity(pada.pratipadika.referenceKey()) ?: return pada
                    val name = aliases[identity] ?: return pada
                    return pada.copy(sourceText = "$name + ${pada.sup.text}",
                        pratipadika = MulaPratipadika(name, name)).inheritLocation(pada)
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
        return Analysis.Resolved(BoundProgram(unify(projected), scope.copy(procedureBindings = bindings,
            environment = scope.environment.mergedWith(ValueEnvironment.live(bindings.values)))))
    }

    fun analyze(node: Invocation, scope: ExecutionScope,
        operations: Set<String> = emptySet()): Analysis<Bound> = try {
        analyzeUnchecked(node, scope, operations)
    } catch (failure: IllegalArgumentException) {
        Analysis.Invalid(Diagnostic.InvalidBinding(
            failure.message ?: "Invalid local binding.", node.relationSpan(), node.sourceText))
    }

    private fun analyzeUnchecked(node: Invocation, scope: ExecutionScope,
        operations: Set<String>): Analysis<Bound> {
        val referents = scope.procedureBindings?.anaphoricReferents().orEmpty()
        var prefix = "__pvm_anaphor_"
        while (scope.procedureBindings?.names.orEmpty().any { it.startsWith(prefix) } ||
            scope.environment.values.keys.any { it.startsWith(prefix) }) prefix += "_"
        val projection = when (val analysis = AnaphoricOperandProjection.analyze(node, referents.map {
            AgreementCandidate(it.identity, ReferentAgreement(it.grammaticalNumbers, it.grammaticalGenders))
        }, prefix = prefix, protectedPadas = PrakriyaAstArgumentBinder.protectedParameterPadas(
            node.vakya.padas, operations))) {
            is AnaphoricOperandProjection.Analysis.Resolved -> analysis.projection
            is AnaphoricOperandProjection.Analysis.Invalid ->
                return Analysis.Invalid(Diagnostic.Anaphoric(analysis.diagnostic))
        }
        if (projection.operands.isEmpty()) return Analysis.Resolved(Bound(node, scope))
        val bindings = requireNotNull(scope.procedureBindings).withReferentAliases(
            projection.operands.associate { it.name to it.identity })
        return Analysis.Resolved(Bound(projection.invocation, scope.copy(procedureBindings = bindings,
            environment = scope.environment.mergedWith(ValueEnvironment.live(bindings.values)))))
    }

}
