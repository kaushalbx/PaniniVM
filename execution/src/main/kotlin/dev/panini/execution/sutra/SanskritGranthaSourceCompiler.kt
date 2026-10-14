package dev.panini.execution.sutra

import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.core.Karaka
import dev.panini.execution.ActionDependency
import dev.panini.execution.ExecutableUkti
import dev.panini.execution.ExecuteConditional
import dev.panini.execution.ExecuteInvocation
import dev.panini.execution.ExecuteRepeat
import dev.panini.execution.ExecuteSequence
import dev.panini.execution.ExecutionNode
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionPlan
import dev.panini.execution.ExecutionPlanner
import dev.panini.execution.ExecutionResult
import dev.panini.execution.PlanningResult
import dev.panini.execution.PvmScript
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.SanskritValue
import dev.panini.execution.SmrtaPhala
import dev.panini.execution.SmrtaPhalaId
import dev.panini.execution.ValueEnvironment
import dev.panini.execution.bindingName
import dev.panini.execution.semanticResultConstraint
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.shiksha.Samjna
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.SutraBlueprintGrantha
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextCodec
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextEncoding

sealed interface SanskritGranthaSourceCompilation {
    data class Success(
        val grantha: SutraBlueprintGrantha,
        val source: String,
        val bindingTrace: List<String>,
    ) : SanskritGranthaSourceCompilation

    data class Invalid(
        val diagnostics: List<String>,
    ) : SanskritGranthaSourceCompilation
}

/** Front end from Sanskrit program text to portable segmented sūtra source. */
object SanskritGranthaSourceCompiler {
    fun compile(
        source: String,
        granthaId: GranthaId,
        conversation: SambhashanaContext = SambhashanaContext(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
        ),
    ): SanskritGranthaSourceCompilation {
        DhatuPathaRegistration.ensureRegistered()
        val lines = PvmScript.parse(source).map { it.text }
        if (lines.isEmpty()) {
            return SanskritGranthaSourceCompilation.Invalid(
                listOf("A Sanskrit grantha requires at least one executable utterance."),
            )
        }
        var currentConversation = conversation
        var resultConstraints = emptyMap<String, dev.panini.execution.SemanticValueConstraint>()
        val sutras = mutableListOf<dev.panini.sutra.runtime.SutraBlueprint>()
        val trace = mutableListOf<String>()

        lines.forEachIndexed { index, line ->
            val turn = currentConversation.turnNumber + 1
            val binding = VyakaranamExecutionAdapter.bind(
                SanskritUktiInput(
                    speaker = currentConversation.speaker,
                    listener = currentConversation.listener,
                    text = line,
                ),
                currentConversation,
            )
            val bound = when (binding) {
                is ExecutionBindingResult.Bound -> binding
                is ExecutionBindingResult.NeedsInput -> return invalidLine(index, binding.message)
                is ExecutionBindingResult.Invalid -> return invalidLine(index, binding.message)
            }
            trace += bound.trace.map { "line ${index + 1}: $it" }

            val planning = plan(bound.ukti, currentConversation, resultConstraints)
            val plans = when (planning) {
                is PlanningResult.Planned -> planning.resolvedProgram.plans
                is PlanningResult.Failed -> return invalidLine(
                    index,
                    when (val result = planning.result) {
                        is ExecutionResult.NeedsInput -> result.message
                        is ExecutionResult.Ambiguous -> result.message
                        is ExecutionResult.Failure -> result.message
                        else -> "The utterance could not be planned."
                    },
                )
            }

            val purvaphalaId = currentConversation.resultHistory.lastOrNull()?.id
            val localIds = bound.ukti.invocations.associate {
                it.id to SmrtaPhalaId.of(turn, it.id)
            }
            val referenceIds = localIds + (if (purvaphalaId != null) mapOf("पूर्वफल" to purvaphalaId) else emptyMap())
            val knownIds = sutras.mapTo(mutableSetOf()) { it.id.value }
            val globalInvocations = bound.ukti.invocations.map { invocation ->
                val rewrittenBindings = invocation.bindings.mapValues { (_, expression) ->
                    expression.rewriteReferences(referenceIds)
                }
                invocation.copy(
                    id = localIds.getValue(invocation.id),
                    bindings = rewrittenBindings,
                    ambiguousBindings = invocation.ambiguousBindings.map {
                        it.copy(expression = it.expression.rewriteReferences(referenceIds))
                    },
                )
            }
            val dependencies = buildSet {
                bound.ukti.dependencies.forEach {
                    add(ActionDependency(localIds.getValue(it.before), localIds.getValue(it.after)))
                }
                globalInvocations.forEach { invocation ->
                    invocation.bindings.values.flatMap { it.references() }
                        .filter { it in knownIds }
                        .forEach { add(ActionDependency(it, invocation.id)) }
                }
            }
            val remappedControl = bound.ukti.control.remapInvocationIds { localIds.getValue(it) }
            val globalUkti = bound.ukti.copy(
                invocations = globalInvocations,
                control = remappedControl,
                dependencies = dependencies,
            )
            sutras += ExecutableUktiSutraCompiler
                .compileBlueprintGrantha(globalUkti, granthaId)
                .sutras
            currentConversation = advance(currentConversation, plans)
            resultConstraints = resultConstraints + plans.flatMap { plan ->
                val constraint = plan.resolved.operation.semanticResultConstraint()
                val bindingName = plan.resolved.operation.resultBindingKaraka
                    ?.let(plan.resolved.context.bindings::get)
                    ?.bindingName()
                buildList {
                    add(plan.invocationId to constraint)
                    if (bindingName != null) add(bindingName to constraint)
                }
            }
        }

        val grantha = SutraBlueprintGrantha(
            id = granthaId,
            sutras = sutras,
            exports = sutras.mapTo(linkedSetOf()) { it.id },
        )
        return when (val encoded = SutraBlueprintGranthaTextCodec.encode(grantha)) {
            is SutraBlueprintGranthaTextEncoding.Success ->
                SanskritGranthaSourceCompilation.Success(grantha, encoded.text, trace)
            is SutraBlueprintGranthaTextEncoding.Invalid ->
                SanskritGranthaSourceCompilation.Invalid(
                    encoded.diagnostics.map { it.message },
                )
        }
    }

    private fun invalidLine(index: Int, message: String) =
        SanskritGranthaSourceCompilation.Invalid(listOf("Line ${index + 1}: $message"))

    private fun plan(
        ukti: ExecutableUkti,
        conversation: SambhashanaContext,
        resultConstraints: Map<String, dev.panini.execution.SemanticValueConstraint>,
    ): PlanningResult {
        val blueprint = ExecutableUktiSutraCompiler.compileBlueprintGrantha(ukti)
        val program = when (
            val result = ProgramBlueprintGranthaPlanner.plan(
                blueprint,
                ProgramBlueprintContext(
                    speaker = ukti.speaker,
                    listener = ukti.listener,
                    text = ukti.text,
                    prayojana = ukti.prayojana,
                    polarity = ukti.polarity,
                    lakara = ukti.lakara,
                ),
            )
        ) {
            is ProgramGranthaPlanning.Success -> result.program
            is ProgramGranthaPlanning.Invalid -> return PlanningResult.Failed(
                ExecutionResult.Failure(
                    dev.panini.execution.ExecutionError.INVALID_VALUE,
                    result.diagnostics.joinToString("\n") { it.message },
                ),
            )
        }
        val historicalValues = mutableMapOf<String, SanskritValue>()
        conversation.resultHistory.forEach { result ->
            val valObj = result.typedValue ?: SanskritValue.of(result.value, result.samjnas)
            historicalValues[result.id] = valObj
            historicalValues[result.invocationId] = valObj
        }
        return ExecutionPlanner.plan(
            program,
            ValueEnvironment.from(
                displayValues = conversation.mentionedEntities + conversation.previousResults,
                samjnas = conversation.mentionedEntitySamjnas + conversation.previousResultSamjnas,
                typedValues = historicalValues + conversation.previousTypedResults,
            ).mergedWith(ValueEnvironment(constraints = resultConstraints)),
        )
    }

    private fun advance(
        conversation: SambhashanaContext,
        plans: List<ExecutionPlan>,
    ): SambhashanaContext {
        val nextTurn = conversation.turnNumber + 1
        val remembered = plans.map { plan ->
            SmrtaPhala(
                id = SmrtaPhalaId.of(nextTurn, plan.invocationId),
                turnNumber = nextTurn,
                invocationId = plan.invocationId,
                value = plan.invocationId,
                samjnas = plan.resolved.operation.resultSamjnas,
            )
        }
        return conversation.copy(
            previousResults = conversation.previousResults +
                plans.associate { it.invocationId to it.invocationId },
            previousResultSamjnas = conversation.previousResultSamjnas +
                plans.associate { it.invocationId to it.resolved.operation.resultSamjnas },
            resultHistory = conversation.resultHistory + remembered,
            metadata = conversation.metadata + plans.associate { plan ->
                dev.panini.execution.ExecutionMetadata.dhatu(SmrtaPhalaId.of(nextTurn, plan.invocationId)) to
                    plan.resolved.invocation.dhatu.upadesha
            },
            turnNumber = nextTurn,
        )
    }

    private fun ExecutionExpression.rewriteReferences(localIds: Map<String, String>): ExecutionExpression =
        when (this) {
            is ExecutionExpression.Pada -> this
            is ExecutionExpression.Reference -> copy(name = localIds[name] ?: name)
            is ExecutionExpression.Coordination -> copy(
                members = members.map { it.rewriteReferences(localIds) },
            )
            is ExecutionExpression.TypedOperand -> this
        }

    private fun ExecutionExpression.references(): Set<String> = when (this) {
        is ExecutionExpression.Pada -> emptySet()
        is ExecutionExpression.Reference -> setOf(name)
        is ExecutionExpression.Coordination -> members.flatMapTo(linkedSetOf()) { it.references() }
        is ExecutionExpression.TypedOperand -> emptySet()
    }

    private fun ExecutionNode.remapInvocationIds(transform: (String) -> String): ExecutionNode = when (this) {
        is ExecuteInvocation -> ExecuteInvocation(transform(invocationId))
        is ExecuteSequence -> ExecuteSequence(nodes.map { it.remapInvocationIds(transform) })
        is ExecuteConditional -> ExecuteConditional(
            condition = condition.remapInvocationIds(transform),
            consequent = consequent.remapInvocationIds(transform),
            alternate = alternate?.remapInvocationIds(transform),
        )
        is ExecuteRepeat -> ExecuteRepeat(iterations.map { it.remapInvocationIds(transform) })
    }
}
