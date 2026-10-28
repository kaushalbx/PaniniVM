package dev.panini.execution.planning

import dev.panini.execution.binding.referenceKey

import dev.panini.core.Karaka
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionPlan
import dev.panini.execution.ExecutionPlanner
import dev.panini.execution.PlanningResult
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.SanskritValue
import dev.panini.execution.SvamRupamEngine
import dev.panini.execution.NominalValueAnalysis
import dev.panini.execution.ValueEnvironment
import dev.panini.execution.bindingName
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.execution.binding.hasAdditionalDerivation
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.execution.sutra.ExecutableUktiSutraCompiler
import dev.panini.execution.sutra.ProgramBlueprintContext
import dev.panini.execution.sutra.ProgramBlueprintGranthaPlanner
import dev.panini.execution.sutra.ProgramGranthaPlanning
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.ast.semanticKey
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity

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
        "सूचीप्रतिस्थापनम्",
        "सूचीस्थानविनिमयः",
        "सूचीस्थाननिक्षेपणम्",
        "सूचीस्थाननिष्कासनम्",
        "सूच्यन्त्यनिष्कासनम्",
        "सूचीसङ्ग्रहः",
        "सूचीस्थानम्",
        "सूचीस्थानान्वेषणम्",
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

    /** Plans an already parsed leaf without serializing and reparsing its Sanskrit. */
    fun plan(
        invocation: Invocation,
        environment: ValueEnvironment = ValueEnvironment(),
        allowStore: Boolean = false,
        injectedBindings: Map<Karaka, ExecutionExpression> = emptyMap(),
    ): ExecutionPlan? = plans(invocation, environment, allowStore, injectedBindings)?.singleOrNull()

    fun plans(
        invocation: Invocation,
        environment: ValueEnvironment = ValueEnvironment(),
        allowStore: Boolean = false,
        injectedBindings: Map<Karaka, ExecutionExpression> = emptyMap(),
    ): List<ExecutionPlan>? {
        val ukti = Ukti(invocation.sourceText, body = invocation)
        val plans = candidatePlans(ukti, environment, injectedBindings) ?: return null
        return plans.takeIf { candidates -> candidates.isNotEmpty() && candidates.all { plan ->
            isSupported(plan, environment, allowStore)
        } }
    }

    fun planAny(
        invocation: Invocation,
        environment: ValueEnvironment = ValueEnvironment(),
        injectedBindings: Map<Karaka, ExecutionExpression> = emptyMap(),
    ): ExecutionPlan? = plansAny(invocation, environment, injectedBindings)?.singleOrNull()

    fun plansAny(
        invocation: Invocation,
        environment: ValueEnvironment = ValueEnvironment(),
        injectedBindings: Map<Karaka, ExecutionExpression> = emptyMap(),
    ): List<ExecutionPlan>? {
        val ukti = Ukti(invocation.sourceText, body = invocation)
        return candidatePlans(ukti, environment, injectedBindings)?.takeIf { plans ->
            plans.isNotEmpty() && plans.all { plan ->
                plan.resolved.context.bindings.values.all(::isMaterializable)
            }
        }
    }

    /** Plans a compound parsed node while preserving its sequence/quotation AST. */
    fun plansAny(
        node: ProgramNode,
        environment: ValueEnvironment = ValueEnvironment(),
    ): List<ExecutionPlan>? {
        val ukti = Ukti(node.sourceText, body = node)
        return candidatePlans(ukti, environment, emptyMap())?.takeIf { plans ->
            plans.isNotEmpty() && plans.all { plan ->
                plan.resolved.context.bindings.values.all(::isMaterializable)
            }
        }
    }

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
            isSupported(plan, environment, allowStore)
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
    ): List<ExecutionPlan>? {
        val ukti = parser.parseOrNull(source) ?: return null
        return candidatePlans(ukti, environment, emptyMap())
    }

    private fun candidatePlans(
        ukti: Ukti,
        environment: ValueEnvironment,
        injectedBindings: Map<Karaka, ExecutionExpression>,
    ): List<ExecutionPlan>? = listOf<(String) -> dev.panini.execution.SemanticValueConstraint>(
        { dev.panini.execution.SemanticValueConstraint(
            dev.panini.execution.OperationValueType.INTEGER,
            setOf(dev.panini.shiksha.Samjna.SANKHYA, dev.panini.shiksha.Samjna.SHABDA),
        ) },
        { dev.panini.execution.SemanticValueConstraint(
            dev.panini.execution.OperationValueType.COLLECTION,
            setOf(dev.panini.shiksha.Samjna.GANA),
        ) },
        { dev.panini.execution.SemanticValueConstraint(
            dev.panini.execution.OperationValueType.TEXT,
            setOf(dev.panini.shiksha.Samjna.SHABDA),
        ) },
    ).firstNotNullOfOrNull { constraintFor ->
        candidatePlans(ukti, environment, injectedBindings, constraintFor)
    }

    private fun candidatePlans(
        parsedUkti: Ukti,
        environment: ValueEnvironment,
        injectedBindings: Map<Karaka, ExecutionExpression>,
        symbolicConstraint: (String) -> dev.panini.execution.SemanticValueConstraint,
    ): List<ExecutionPlan>? {
        val parsedSubantas = parsedUkti.grammaticalVakyas()
            .flatMap { vakya -> vakya.padas }
            .flatMap { pada ->
                when (pada) {
                    is SubantaPada -> listOf(pada)
                    is SamuccitaSubanta -> pada.members.filterIsInstance<SubantaPada>()
                    else -> emptyList()
                }
            }
            .orEmpty()
        val truthConstants = parsedSubantas.mapNotNull { pada ->
            pada.pratipadika.semanticKey().takeIf {
                dev.panini.execution.nominalTruthValue(pada.pratipadika) != null
            }
        }.toSet()
        val symbolicOperands = parsedSubantas.asSequence()
            .filterNot { pada ->
                val nominal = pada.pratipadika as? MulaPratipadika
                nominal?.lexicalIdentity == MulaPratipadikaIdentity.SANKHYA &&
                    nominal.vikaras.isEmpty() && pada.sup.affixCandidates == listOf(SupAffix.SAS)
            }
            .filter { pada ->
                val reference = pada.pratipadika.referenceKey()
                isCopularReferenceParticipant(parsedUkti, pada) ||
                    pada.sup.supports(Vibhakti.DVITIYA) ||
                    reference in environment.values || reference in environment.constraints
            }
            .flatMap { pada ->
                sequenceOf(
                    pada.pratipadika.sourceText.trim(),
                    if (isCopularReferenceParticipant(parsedUkti, pada)) pada.pratipadika.referenceKey()
                    else pada.pratipadika.semanticKey(),
                ).distinct()
            }
            .filterNot { candidate ->
                candidate in environment.values || candidate in environment.constraints ||
                    isSankhyaStem(candidate) || candidate in truthConstants
            }
            .associateWith(symbolicConstraint)
        val instrumentNames = parsedSubantas.asSequence()
            .filter { it.sup.supports(Vibhakti.TRTIYA) }
            .filterNot { isCopularReferenceParticipant(parsedUkti, it) }
            .filterNot { it.pratipadika.hasAdditionalDerivation() || it.pratipadika is dev.panini.vyakaranam.ast.KridantaPratipadika }
            .map { it.pratipadika.semanticKey() }
            .filterNot { it in environment.values || it in environment.constraints || isSankhyaStem(it) }
            .toSet()
        val symbolicInstruments = instrumentNames.takeIf {
            symbolicConstraint("").valueType == dev.panini.execution.OperationValueType.COLLECTION
        }.orEmpty().associateWith(symbolicConstraint)
        // Instrumental operation names are lexically licensed text literals unless
        // this planning branch explicitly tests a collection-valued referent.
        val literalInstruments = (instrumentNames - symbolicInstruments.keys)
            .associateWith { SanskritValue.Shabda(it) }
        val actionNames = parsedSubantas.asSequence()
            .filter { it.sup.supports(Vibhakti.TRTIYA) }
            .filter { it.pratipadika is dev.panini.vyakaranam.ast.KridantaPratipadika }
            .map { it.pratipadika.semanticKey() }
            .filterNot { it in environment.values || it in environment.constraints }
            .associateWith { SanskritValue.Shabda(it) }
        val collectionWholes = parsedUkti.grammaticalVakyas().asSequence()
            .filter { vakya -> vakya.padas.filterIsInstance<TingantaPada>()
                .any { it.canonicalDhatuIdentity() == CanonicalDhatuIdentity.AS } }
            .flatMap { it.padas.filterIsInstance<SubantaPada>().asSequence() }
            .filter { it.sup.supports(Vibhakti.SAPTAMI) }
            .map { it.pratipadika.referenceKey() }
            .filterNot { it in environment.values || it in environment.constraints }
            .associateWith {
                dev.panini.execution.SemanticValueConstraint(
                    dev.panini.execution.OperationValueType.COLLECTION,
                    setOf(dev.panini.shiksha.Samjna.GANA),
                )
            } + parsedUkti.grammaticalVakyas().asSequence()
            .filter { dev.panini.execution.CollectionMemberMorphology.protectedPadas(it.padas).isNotEmpty() }
            .flatMap { it.padas.filterIsInstance<SubantaPada>().asSequence() }
            .filter { it.sup.supports(Vibhakti.SASTHI) }
            .map { it.pratipadika.referenceKey() }
            .filterNot { it in environment.values || it in environment.constraints }
            .associateWith {
                dev.panini.execution.SemanticValueConstraint(
                    dev.panini.execution.OperationValueType.COLLECTION,
                    setOf(dev.panini.shiksha.Samjna.GANA),
                )
            }
        val bindingEnvironment = environment.mergedWith(ValueEnvironment(
            values = literalInstruments + actionNames,
            constraints = symbolicOperands + symbolicInstruments + collectionWholes,
        ))
        val segmentedSource = parsedUkti.sourceText.replace("+", " + ").replace(Regex("\\s+"), " ").trim()
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
        val bindingResult = runCatching {
            VyakaranamExecutionAdapter.bind(
                input,
                parsedUkti,
                conversation,
                environment = bindingEnvironment,
                injectedBindings = injectedBindings,
            )
        }.getOrNull()
        val ukti = (bindingResult as? ExecutionBindingResult.Bound)?.ukti ?: return null
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
            .filterNot { it in environment.values || it in environment.constraints }
            .associateWith { name -> collectionWholes[name] ?: symbolicConstraint(name) }
        val planningEnvironment = bindingEnvironment.mergedWith(ValueEnvironment(constraints = symbolicReferences))
        val symbolicNames = environment.values.keys + symbolicOperands.keys +
            symbolicInstruments.keys + symbolicReferences.keys + collectionWholes.keys
        val copularReferences = parsedSubantas.filter { isCopularReferenceParticipant(parsedUkti, it) }
            .flatMap { pada -> listOf(pada.pratipadika.sourceText.trim(), pada.pratipadika.referenceKey()).distinct()
                .map { name -> name to pada } }.groupBy({ it.first }, { it.second })
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
            ?.resolvedProgram
            ?.plans
            ?.map { plan ->
                plan.copy(
                    resolved = plan.resolved.copy(
                        context = plan.resolved.context.copy(
                            bindings = plan.resolved.context.bindings.mapValues { (karaka, expression) ->
                                expression.restoreSymbolicReferences(symbolicNames, copularReferences, karaka)
                            },
                        ),
                    ),
                )
            }
            ?: return null
    }

    private fun isCopularReferenceParticipant(ukti: Ukti, pada: SubantaPada): Boolean =
        (pada.sup.supports(Vibhakti.PRATHAMA) && ukti.grammaticalVakyas().any { vakya ->
            pada in vakya.padas &&
                vakya.padas.filterIsInstance<TingantaPada>().any { it.canonicalDhatuIdentity() == CanonicalDhatuIdentity.AS } &&
                vakya.padas.filterIsInstance<SubantaPada>().any { it.sup.supports(Vibhakti.SAPTAMI) }
        }) || (setOf(Vibhakti.PRATHAMA, Vibhakti.PANCHAMI, Vibhakti.TRTIYA).any { pada.sup.supports(it) } &&
            (pada.pratipadika as? MulaPratipadika)?.lexicalIdentity !in setOf(MulaPratipadikaIdentity.NYUNA, MulaPratipadikaIdentity.ADHIKA, MulaPratipadikaIdentity.SAMA) &&
            ukti.grammaticalVakyas().any { vakya ->
                pada in vakya.padas &&
                    vakya.padas.filterIsInstance<TingantaPada>().any { it.canonicalDhatuIdentity() == CanonicalDhatuIdentity.AS } &&
                    vakya.padas.filterIsInstance<SubantaPada>().any {
                        it.sup.supports(Vibhakti.PRATHAMA) &&
                            (it.pratipadika as? MulaPratipadika)?.lexicalIdentity in setOf(MulaPratipadikaIdentity.NYUNA, MulaPratipadikaIdentity.ADHIKA, MulaPratipadikaIdentity.SAMA)
                    }
            })

    private fun isSupported(
        plan: ExecutionPlan,
        environment: ValueEnvironment,
        allowStore: Boolean,
    ): Boolean =
        (plan.resolved.operation.name in supportedOperations ||
            (allowStore && plan.resolved.operation.name == "मूल्यदानम्")) &&
            (plan.resolved.operation.name != "सङ्ख्यातुलना" ||
                (plan.resolved.operation.trigger.requiredUpasargas.isEmpty() &&
                    plan.resolved.operation.trigger.requiredAvyayas.isEmpty())) &&
            (plan.resolved.operation.name != "विजयः" ||
                (plan.resolved.operation.trigger.requiredUpasargas.size == 1 &&
                    plan.resolved.operation.trigger.requiredUpasargaIdentities == setOf(dev.panini.core.Upasarga.VI))) &&
            (plan.resolved.operation.name != "प्रदर्शनम्" ||
                plan.resolved.context.bindings
                    .filterKeys { it in setOf(Karaka.KARMAN, Karaka.APADANA, Karaka.ADHIKARANA) }
                    .values.all { isConcrete(it, environment) }) &&
            plan.resolved.context.bindings.values.all { isEmbeddable(it, environment) }

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
        copularReferences: Map<String, List<SubantaPada>> = emptyMap(),
        resolvedKaraka: Karaka,
    ): ExecutionExpression = when (this) {
        is ExecutionExpression.Pada -> this
        is ExecutionExpression.TypedOperand -> {
            if (referenceName in symbolicNames) {
                copularOperand(requireNotNull(referenceName), resolvedKaraka, copularReferences)
                    ?: ExecutionExpression.Reference(requireNotNull(referenceName))
            } else this
        }
        is ExecutionExpression.Coordination -> copy(
            members = members.map { it.restoreSymbolicReferences(symbolicNames, copularReferences, resolvedKaraka) },
        )
        is ExecutionExpression.Reference -> if (name in symbolicNames) {
            copularOperand(name, resolvedKaraka, copularReferences) ?: this
        } else this
    }

    /** Restore morphology only after planning has established the participant's role. */
    private fun copularOperand(name: String, karaka: Karaka,
        references: Map<String, List<SubantaPada>>): ExecutionExpression.TypedOperand? {
        val candidates = references[name].orEmpty().flatMap { pada ->
            pada.sup.affixCandidates.filter { affix ->
                dev.panini.analysis.KarakaInference.infer(affix.vibhakti, dev.panini.core.Prayoga.KARTARI) == karaka
            }.map { affix -> pada to affix }
        }.distinct()
        val (pada, affix) = candidates.singleOrNull() ?: return null
        val fallback = dev.panini.execution.PuranaPratyayaResolver.semanticValue(pada)
            ?: NominalValueAnalysis.numericFallback(pada) ?: SvamRupamEngine.evaluate(pada.pratipadika)
        return ExecutionExpression.TypedOperand(fallback, affix, pada.pratipadika.referenceKey())
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
        is SanskritValue.Purana,
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
