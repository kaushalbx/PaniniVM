package dev.panini.execution.sutra

import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.core.Karaka
import dev.panini.execution.ActionDependency
import dev.panini.execution.DevanagariDigits
import dev.panini.execution.ExecutableUkti
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
import dev.panini.execution.ValueEnvironment
import dev.panini.execution.bindingName
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

            val planning = plan(bound.ukti, currentConversation)
            val plans = when (planning) {
                is PlanningResult.Planned -> planning.plans
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
                it.id to "उक्ति-${DevanagariDigits.render(turn)}/${it.id}"
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
            val globalUkti = bound.ukti.copy(
                invocations = globalInvocations,
                dependencies = dependencies,
            )
            sutras += ExecutableUktiSutraCompiler
                .compileBlueprintGrantha(globalUkti, granthaId)
                .sutras
            currentConversation = advance(currentConversation, plans)
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
        val historicalValues = conversation.resultHistory.associate {
            it.id to (it.typedValue ?: SanskritValue.of(it.value, it.samjnas))
        }.toMutableMap()
        if (conversation.resultHistory.isNotEmpty()) {
            val last = conversation.resultHistory.last()
            val lastObj = last.typedValue ?: SanskritValue.of(last.value, last.samjnas)
            historicalValues["फल"] = lastObj
            historicalValues["पूर्वफल"] = lastObj
        }
        if (conversation.resultHistory.size >= 2) {
            val prevPrev = conversation.resultHistory[conversation.resultHistory.size - 2]
            val prevPrevObj = prevPrev.typedValue ?: SanskritValue.of(prevPrev.value, prevPrev.samjnas)
            historicalValues["पूर्वपूर्वफल"] = prevPrevObj
        } else if (conversation.resultHistory.size == 1) {
            conversation.previousTypedResults["द्वि"]?.let {
                historicalValues["पूर्वपूर्वफल"] = it
            }
        }
        return ExecutionPlanner.plan(
            program,
            ValueEnvironment.from(
                displayValues = conversation.mentionedEntities + conversation.previousResults,
                samjnas = conversation.mentionedEntitySamjnas + conversation.previousResultSamjnas,
                typedValues = historicalValues + conversation.previousTypedResults,
            ),
        )
    }

    private fun advance(
        conversation: SambhashanaContext,
        plans: List<ExecutionPlan>,
    ): SambhashanaContext {
        val nextTurn = conversation.turnNumber + 1
        val mockResults = plans.associate { it.invocationId to simulatedResult(it) }
        val localBindings = plans.mapNotNull { plan ->
            val karaka = plan.resolved.operation.resultBindingKaraka ?: return@mapNotNull null
            val name = plan.resolved.context.bindings[karaka]?.bindingName() ?: return@mapNotNull null
            name to mockResults.getValue(plan.invocationId)
        }.toMap()
        val remembered = plans.map { plan ->
            SmrtaPhala(
                id = "उक्ति-${DevanagariDigits.render(nextTurn)}/${plan.invocationId}",
                turnNumber = nextTurn,
                invocationId = plan.invocationId,
                value = "<${plan.invocationId}>",
                samjnas = plan.resolved.operation.resultSamjnas,
                typedValue = mockResults.getValue(plan.invocationId),
            )
        }
        return conversation.copy(
            previousResults = conversation.previousResults +
                plans.associate { it.invocationId to "<${it.invocationId}>" },
            previousResultSamjnas = conversation.previousResultSamjnas +
                plans.associate { it.invocationId to it.resolved.operation.resultSamjnas },
            previousTypedResults = conversation.previousTypedResults + mockResults + localBindings,
            resultHistory = conversation.resultHistory + remembered,
            turnNumber = nextTurn,
        )
    }

    private val sankhyaGenerator = dev.panini.sankhya.SankhyaGenerator()

    private fun simulatedResult(plan: ExecutionPlan): SanskritValue {
        val operands = plan.resolved.context.bindings[Karaka.KARMAN]
            ?.let(plan.resolved.context::resolveValues)
            .orEmpty()
        val sankhyaOperands = operands.filterIsInstance<SanskritValue.Sankhya>().map { it.value }
        if (sankhyaOperands.isNotEmpty()) {
            val opName = plan.resolved.operation.name
            val resVal = when (opName) {
                "सङ्ख्यायोगः", "panini.math.add" -> sankhyaOperands.sum()
                "सङ्ख्याअन्तरम्", "panini.math.subtract" -> if (sankhyaOperands.size >= 2) sankhyaOperands[0] - sankhyaOperands[1] else sankhyaOperands[0]
                "सङ्ख्यागुणनम्", "panini.math.multiply" -> sankhyaOperands.fold(1L) { a, b -> a * b }
                else -> sankhyaOperands.sum()
            }
            val word = sankhyaGenerator.cardinal(resVal).final.surface
            return SanskritValue.Sankhya(resVal, word)
        }
        if (plan.resolved.operation.resultBindingKaraka != null) {
            if (operands.isNotEmpty()) {
                return if (operands.size == 1) operands.single() else SanskritValue.Suchi(operands)
            }
        }
        if (Samjna.SATYA in plan.resolved.operation.resultSamjnas) return SanskritValue.Satya(true)
        return SanskritValue.Shabda("<${plan.invocationId}>", plan.resolved.operation.resultSamjnas)
    }

    private fun ExecutionExpression.rewriteReferences(localIds: Map<String, String>): ExecutionExpression =
        when (this) {
            is ExecutionExpression.Pada -> this
            is ExecutionExpression.Reference -> copy(name = localIds[name] ?: name)
            is ExecutionExpression.Coordination -> copy(
                members = members.map { it.rewriteReferences(localIds) },
            )
        }

    private fun ExecutionExpression.references(): Set<String> = when (this) {
        is ExecutionExpression.Pada -> emptySet()
        is ExecutionExpression.Reference -> setOf(name)
        is ExecutionExpression.Coordination -> members.flatMapTo(linkedSetOf()) { it.references() }
    }
}
