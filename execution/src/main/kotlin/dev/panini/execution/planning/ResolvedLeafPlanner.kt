package dev.panini.execution.planning

import dev.panini.core.Karaka
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionPlan
import dev.panini.execution.ExecutionPlanner
import dev.panini.execution.PlanningResult
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.SanskritValue
import dev.panini.execution.ValueEnvironment
import dev.panini.execution.bindingName
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.execution.sutra.ExecutableUktiSutraCompiler
import dev.panini.execution.sutra.ProgramBlueprintContext
import dev.panini.execution.sutra.ProgramBlueprintGranthaPlanner
import dev.panini.execution.sutra.ProgramGranthaPlanning

/** Shared resolved-leaf planning boundary for interpreters, compilers, and tooling. */
object ResolvedLeafPlanner {
    private val parser = dev.panini.vyakaranam.parser.PaniniParser()
    private val supportedOperations = setOf(
        "सङ्ख्यायोजनम्",
        "सङ्ख्यावियोगः",
        "सङ्ख्यागुणनम्",
        "सङ्ख्याहरणम्",
        "सङ्ख्याशेषः",
        "सङ्ख्यातुलना",
        "सूच्याकारः",
        "सूचीसंयोगः",
        "सूचीसंयोजनम्",
        "सूचीसङ्क्षेपः",
        "सूचीनिक्षेपणम्",
        "सूचीस्थानम्",
        "सूचीविभागः",
        "सूचीविलोमः",
        "सूचीप्रसारणम्",
        "सूचीशोधनम्",
        "सूच्युद्धरणम्",
        "सूच्यस्तित्वम्",
        "प्रत्येकवृत्तिः",
        "विजयः",
        "प्रदर्शनम्",
    )

    fun plan(
        source: String,
        environment: ValueEnvironment = ValueEnvironment(),
        allowStore: Boolean = false,
    ): ExecutionPlan? = plans(source, environment, allowStore)?.singleOrNull()

    fun resultBindingName(source: String): String? {
        val segmentedSource = source.replace("+", " + ").replace(Regex("\\s+"), " ").trim()
        val conversation = SambhashanaContext("प्रयोक्ता", "यन्त्रम्")
        val input = SanskritUktiInput(
            speaker = conversation.speaker,
            listener = conversation.listener,
            text = segmentedSource,
        )
        val invocation = ((runCatching {
            VyakaranamExecutionAdapter.bind(input, conversation)
        }.getOrNull() as? ExecutionBindingResult.Bound)?.ukti?.invocations)?.singleOrNull() ?: return null
        val operation = invocation.selectedOperation?.let { selected ->
            invocation.dhatu.operations.singleOrNull { it.name == selected }
        } ?: invocation.dhatu.operations.singleOrNull { it.resultBindingKaraka != null }
        val karaka = operation?.resultBindingKaraka ?: return null
        return invocation.bindings[karaka]?.bindingName()
    }

    fun plans(
        source: String,
        environment: ValueEnvironment = ValueEnvironment(),
        allowStore: Boolean = false,
    ): List<ExecutionPlan>? {
        val plans = candidatePlans(source, environment) ?: return null
        return plans.takeIf { candidates -> candidates.isNotEmpty() && candidates.all { plan ->
            (plan.resolved.operation.name in supportedOperations ||
                (allowStore && plan.resolved.operation.name == "मूल्यदानम्")) &&
                (plan.resolved.operation.name != "सङ्ख्यातुलना" ||
                    (plan.resolved.operation.trigger.requiredUpasargas.isEmpty() &&
                        plan.resolved.operation.trigger.requiredAvyayas.isEmpty())) &&
                (plan.resolved.operation.name != "विजयः" ||
                    plan.resolved.operation.trigger.requiredUpasargas == setOf("वि")) &&
                (plan.resolved.operation.name != "प्रदर्शनम्" ||
                    plan.resolved.context.bindings
                        .filterKeys { it in setOf(Karaka.KARMAN, Karaka.APADANA, Karaka.ADHIKARANA) }
                        .values.all { isConcrete(it, environment) }) &&
                plan.resolved.context.bindings.values.all { isEmbeddable(it, environment) }
        } }
    }

    fun plansAny(
        source: String,
        environment: ValueEnvironment = ValueEnvironment(),
    ): List<ExecutionPlan>? = candidatePlans(source, environment)?.takeIf { plans ->
        plans.isNotEmpty() && plans.all { plan ->
            plan.resolved.context.bindings.values.all(::isMaterializable)
        }
    }

    fun planAny(
        source: String,
        environment: ValueEnvironment = ValueEnvironment(),
    ): ExecutionPlan? = plansAny(source, environment)?.singleOrNull()

    private fun candidatePlans(
        source: String,
        environment: ValueEnvironment,
    ): List<ExecutionPlan>? = listOf<(String) -> SanskritValue>(
        { name -> SanskritValue.Sankhya(1L, name) },
        { _ -> SanskritValue.Suchi(emptyList()) },
        { name -> SanskritValue.Shabda(name) },
    ).firstNotNullOfOrNull { placeholder ->
        candidatePlans(source, environment, placeholder)
    }

    private fun candidatePlans(
        source: String,
        environment: ValueEnvironment,
        placeholder: (String) -> SanskritValue,
    ): List<ExecutionPlan>? {
        val parsedSubantas = parser.parseOrNull(source)?.grammaticalVakyas()
            ?.flatMap { vakya -> vakya.padas }
            ?.flatMap { pada ->
                when (pada) {
                    is dev.panini.vyakaranam.ast.SubantaPada -> listOf(pada)
                    is dev.panini.vyakaranam.ast.SamuccitaSubanta -> pada.members
                    else -> emptyList()
                }
            }
            .orEmpty()
        val symbolicOperands = parsedSubantas.asSequence()
            .filter { it.sup.text in setOf("अम्", "औट्", "शस्") }
            .flatMap { pada ->
                val fullName = pada.pratipadika.sourceText.trim()
                sequenceOf(fullName, fullName.substringBefore('+').trim())
            }
            .filterNot { it in environment.values || isSankhyaStem(it) }
            .associateWith(placeholder)
        val symbolicInstruments = parsedSubantas.asSequence()
            .filter { it.sup.text in setOf("टा", "भ्याम्", "भिस्") }
            .map { it.pratipadika.sourceText.substringBefore('+').trim() }
            .filterNot { it in environment.values || isSankhyaStem(it) }
            .associateWith(SanskritValue::Shabda)
        val bindingEnvironment = ValueEnvironment(environment.values + symbolicOperands + symbolicInstruments)
        val segmentedSource = source.replace("+", " + ").replace(Regex("\\s+"), " ").trim()
        val conversation = SambhashanaContext(
            "प्रयोक्ता",
            "यन्त्रम्",
            previousResults = environment.values["LastResult"]?.let {
                mapOf("LastResult" to it.toDisplayText())
            }.orEmpty(),
            previousTypedResults = environment.values,
        )
        val input = SanskritUktiInput(
            speaker = conversation.speaker,
            listener = conversation.listener,
            text = segmentedSource,
        )
        val ukti = (runCatching {
            VyakaranamExecutionAdapter.bind(input, conversation, environment = bindingEnvironment)
        }.getOrNull()
            as? ExecutionBindingResult.Bound)?.ukti ?: return null
        val program = when (val planned = ProgramBlueprintGranthaPlanner.plan(
            ExecutableUktiSutraCompiler.compileBlueprintGrantha(ukti),
            ProgramBlueprintContext(
                speaker = ukti.speaker,
                listener = ukti.listener,
                text = ukti.text,
                prayojana = ukti.prayojana,
                polarity = ukti.polarity,
                lakara = ukti.lakara,
            ),
        )) {
            is ProgramGranthaPlanning.Success -> planned.program
            is ProgramGranthaPlanning.Invalid -> return null
        }
        val symbolicReferences = ukti.invocations
            .flatMap { invocation -> invocation.bindings.values.flatMap { it.references() } }
            .filterNot { it in environment.values }
            .associateWith(placeholder)
        val planningEnvironment = ValueEnvironment(bindingEnvironment.values + symbolicReferences)
        val symbolicNames = symbolicOperands.keys + symbolicReferences.keys
        val explicitlyDisambiguatedOperations = setOf("सूचीसंयोजनम्", "सूचीशोधनम्", "सूचीसङ्क्षेपः")
        val selectedOperation = ukti.invocations.singleOrNull()?.let { invocation ->
            invocation.selectedOperation ?: invocation.dhatu.operations
                .filter { operation -> operation.name in explicitlyDisambiguatedOperations }
                .singleOrNull { operation -> operation.trigger.matches(invocation.grammaticalFeatures) }
                ?.name
        }
        val selectedProgram = selectedOperation?.let { operation ->
            program.copy(
                ukti = program.ukti.copy(
                    invocations = program.ukti.invocations.map { invocation ->
                        if (invocation.dhatu.operations.any { it.name == operation }) {
                            invocation.copy(selectedOperation = operation)
                        } else invocation
                    },
                ),
            )
        } ?: program
        val planning = ExecutionPlanner.plan(selectedProgram, planningEnvironment)
        return (planning as? PlanningResult.Planned)
            ?.plans
            ?.map { plan ->
                plan.copy(
                    resolved = plan.resolved.copy(
                        context = plan.resolved.context.copy(
                            bindings = plan.resolved.context.bindings.mapValues { (_, expression) ->
                                expression.restoreSymbolicReferences(symbolicNames)
                            },
                        ),
                    ),
                )
            }
            ?: return null
    }

    private fun isSankhyaStem(stem: String): Boolean = runCatching {
        dev.panini.sankhya.SankhyaEvaluator().evaluateStems(listOf(stem))
    }.isSuccess

    private fun ExecutionExpression.references(): List<String> = when (this) {
        is ExecutionExpression.Reference -> listOf(name)
        is ExecutionExpression.Coordination -> members.flatMap { it.references() }
        is ExecutionExpression.Pada,
        is ExecutionExpression.TypedOperand,
        -> emptyList()
    }

    private fun ExecutionExpression.restoreSymbolicReferences(
        symbolicNames: Set<String>,
    ): ExecutionExpression = when (this) {
        is ExecutionExpression.Pada -> {
            val placeholderName = when (val placeholder = value) {
                is SanskritValue.Sankhya -> placeholder.word
                is SanskritValue.Shabda -> placeholder.text
                else -> null
            }
            if (prakriti in symbolicNames && (placeholderName == prakriti || value is SanskritValue.Suchi)) {
                ExecutionExpression.Reference(prakriti)
            } else {
                this
            }
        }
        is ExecutionExpression.TypedOperand -> {
            val placeholderName = when (val placeholder = value) {
                is SanskritValue.Sankhya -> placeholder.word
                is SanskritValue.Shabda -> placeholder.text
                else -> null
            }
            if (placeholderName in symbolicNames) {
                ExecutionExpression.Reference(requireNotNull(placeholderName))
            } else {
                this
            }
        }
        is ExecutionExpression.Coordination -> copy(
            members = members.map { it.restoreSymbolicReferences(symbolicNames) },
        )
        is ExecutionExpression.Reference -> this
    }

    private fun isMaterializable(expression: ExecutionExpression): Boolean = when (expression) {
        is ExecutionExpression.Pada -> expression.value?.let(::isSupportedConstant) ?: true
        is ExecutionExpression.TypedOperand -> isSupportedConstant(expression.value)
        is ExecutionExpression.Coordination -> expression.members.all(::isMaterializable)
        is ExecutionExpression.Reference -> true
    }

    private fun isConcrete(
        expression: ExecutionExpression,
        environment: ValueEnvironment,
    ): Boolean = when (expression) {
        is ExecutionExpression.Pada -> expression.value is SanskritValue.Sankhya
        is ExecutionExpression.TypedOperand -> isSupportedConstant(expression.value)
        is ExecutionExpression.Coordination -> expression.members.all { isConcrete(it, environment) }
        is ExecutionExpression.Reference -> expression.name in environment.values
    }

    private fun isEmbeddable(
        expression: ExecutionExpression,
        environment: ValueEnvironment,
    ): Boolean = when (expression) {
        is ExecutionExpression.Pada -> expression.value?.let(::isSupportedConstant) ?: true
        is ExecutionExpression.TypedOperand -> isSupportedConstant(expression.value)
        is ExecutionExpression.Coordination -> expression.members.all { isEmbeddable(it, environment) }
        is ExecutionExpression.Reference -> expression.name in environment.values
    }

    private fun isSupportedConstant(value: SanskritValue): Boolean = when (value) {
        is SanskritValue.Sankhya,
        is SanskritValue.Range,
        is SanskritValue.Rational,
        is SanskritValue.Shabda,
        is SanskritValue.Gana,
        is SanskritValue.Suchi,
        is SanskritValue.Rupa,
        is SanskritValue.Satya,
        SanskritValue.Lopa,
        -> true
    }
}
