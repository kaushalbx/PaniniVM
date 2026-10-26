package dev.panini.execution

import dev.panini.vyakaranam.ast.PrakriyaPrecedence
import dev.panini.vyakaranam.ast.PrakriyaVisibility
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.KatapayadiPada
import dev.panini.vyakaranam.ast.AryabhatiyaPada
import dev.panini.vyakaranam.ast.BhutasamkhyaPada
import dev.panini.vyakaranam.ast.semanticKey
import dev.panini.execution.binding.referenceKey

/**
 * A user-defined reusable prakriyā declared by a grammatical प्रक्रिया statement.
 */
data class Prakriya(
    val nameSegmented: String,
    val nameStem: String,
    val body: List<PvmScriptStatement.Sentence>,
    val sourceFile: String? = null,
    val domainStem: String? = null,
    val visibility: PrakriyaVisibility = PrakriyaVisibility.PUBLIC,
    val precedence: PrakriyaPrecedence = PrakriyaPrecedence.DEFAULT,
    val signatureOverride: PrakriyaSignature? = null,
    val headerResult: SubantaPada? = null,
    val headerPredicate: dev.panini.vyakaranam.ast.TingantaPada? = null,
) {
    init {
        require(body.all { it.program != null }) {
            "Every reusable प्रक्रिया body sentence must be valid, parsed Sanskrit."
        }
    }

    val signature: PrakriyaSignature by lazy {
        signatureOverride ?: PrakriyaSignatureCompiler.compile(body, headerResult, headerPredicate)
    }

    val isInternal: Boolean get() = visibility == PrakriyaVisibility.INTERNAL

    val nishedhaGuards: List<PvmScriptStatement.Sentence> = body.filter { it.isNishedha }
    val vidhiSentences: List<PvmScriptStatement.Sentence> = body.filterNot {
        it.isNishedha || PrakriyaSignatureDeclarationParser.isDeclaration(it)
    }

    /** Definition-owned analysis; invalidated naturally when a new immutable definition is registered. */
    val bodyAnalysis: ProcedureBodyAnalysis by lazy(LazyThreadSafetyMode.PUBLICATION) {
        ProcedureBodyAnalysis.analyze(this)
    }
}

/**
 * Global registry of saṃjñā kriyās for a project/session.
 */
class PrakriyaRegistry {

    private val registry = linkedMapOf<String, MutableList<Prakriya>>()
    private val inheritanceMap = mutableMapOf<String, String>() // childStem -> parentStem
    private val schemas = linkedMapOf<String, TaddhitaStructSchema>()

    fun registerSchema(schema: TaddhitaStructSchema) {
        val existing = schemas[schema.nameStem]
        require(existing == null || existing == schema) {
            "Conflicting Panini schema '${schema.nameStem}': ${existing?.fields} and ${schema.fields}."
        }
        schemas[schema.nameStem] = schema
    }

    fun resolveSchema(nameStem: String): TaddhitaStructSchema? = schemas[nameStem]

    fun registerInheritance(relation: InheritanceRelation) {
        val existing = inheritanceMap[relation.childStem]
        require(existing == null || existing == relation.parentStem) {
            "Conflicting parent domains for '${relation.childStem}': '$existing' and '${relation.parentStem}'."
        }
        val visited = mutableSetOf(relation.childStem)
        var ancestor: String? = relation.parentStem
        while (ancestor != null) {
            require(visited.add(ancestor)) { "Cyclic domain inheritance involving '${relation.childStem}'." }
            ancestor = inheritanceMap[ancestor]
        }
        inheritanceMap[relation.childStem] = relation.parentStem
    }

    fun getParentClass(childStem: String): String? = inheritanceMap[childStem]

    fun register(kriya: Prakriya) {
        val key = if (kriya.domainStem != null) "${kriya.domainStem}::${kriya.nameStem}" else kriya.nameStem
        for (alias in setOf(key, kriya.nameStem)) {
            val definitions = registry.getOrPut(alias) { mutableListOf() }
            if (kriya !in definitions) definitions.add(kriya)
        }
    }

    fun resolve(stem: String, callerSourceFile: String? = null): Prakriya? {
        val list = registry[stem] ?: return null
        return list.lastOrNull { kriya ->
            !kriya.isInternal || kriya.sourceFile == null || callerSourceFile == kriya.sourceFile
        }
    }

    fun all(): List<Prakriya> = registry.values.flatten().distinct()

    fun isEmpty(): Boolean = registry.isEmpty()

    val size: Int get() = registry.size

    /** Resolves an already parsed/synthesized pipeline stage without reconstructing Sanskrit text. */
    fun resolveStructuredInvocation(
        operationStem: String,
        domainStem: String?,
        arguments: List<PrakriyaArgument>,
        sourceText: String,
        callerSourceFile: String? = null,
        analyzedArgumentTypes: List<PrakriyaValueType>? = null,
    ): PrakriyaInvocation? {
        require(analyzedArgumentTypes == null || analyzedArgumentTypes.size == arguments.size)
        val argumentTypes = analyzedArgumentTypes ?: arguments.map { argument ->
            argument.value?.let(PrakriyaValueClassifier::classifyValue)
                ?: argument.pada?.let(PrakriyaValueClassifier::classifyPada)
                ?: PrakriyaValueType.SHABDA
        }
        val candidates = registry.values.flatten()
            .distinct()
            .asSequence()
            .filter { it.nameStem == operationStem && domainMatches(it.domainStem, domainStem) }
            .filterNot { it.isInternal && it.sourceFile != null && callerSourceFile != it.sourceFile }
            .sortedWith(
                compareByDescending<Prakriya> {
                    AntaratamaOverloadEngine.matchTypes(it.signature, argumentTypes) != AntaratamaOverloadEngine.TypeMatch.MISMATCH
                }.thenByDescending { it.precedence.rank }
                    .thenByDescending { AntaratamaOverloadEngine.matchTypes(it.signature, argumentTypes).rank },
            )
            .toList()
        val kriya = candidates.firstOrNull() ?: return null
        val bestMatch = AntaratamaOverloadEngine.matchTypes(kriya.signature, argumentTypes)
        val ambiguous = bestMatch != AntaratamaOverloadEngine.TypeMatch.MISMATCH && candidates.drop(1).any {
            it.precedence == kriya.precedence &&
                AntaratamaOverloadEngine.matchTypes(it.signature, argumentTypes) == bestMatch
        }
        val ambiguityMessage = if (analyzedArgumentTypes != null)
            "Ambiguous pipeline overload for '$operationStem' with analyzed argument types $argumentTypes."
        else "Ambiguous procedure overload for '$operationStem'."
        return PrakriyaInvocation(
            kriya = kriya,
            karmaText = arguments.joinToString(" ") { it.term },
            fullText = sourceText,
            argumentValues = arguments.map(PrakriyaArgument::value),
            arguments = arguments,
            argumentSyntax = arguments.mapNotNull(PrakriyaArgument::pada),
            resolutionError = ambiguityMessage.takeIf { ambiguous },
        )
    }

    /** Detects a reusable prakriyā from a parsed invocation without reparsing rendered text. */
    fun detectInvocation(
        ukti: dev.panini.vyakaranam.ast.Ukti,
        callerSourceFile: String? = null,
        injectedKarman: InjectedKarmanBinding? = null,
        resolveActionResult: ((dev.panini.execution.binding.NamedActionResultReference) -> SanskritValue?)? = null,
        resolveValue: ((String) -> SanskritValue?)? = null,
        staticAnalysis: Boolean = false,
        resolvePriorResult: (() -> SanskritValue?)? = null,
    ): PrakriyaInvocation? {
        if (registry.isEmpty()) return null

        val allKriyas = all()
        val knownStems = allKriyas.mapTo(mutableSetOf()) { it.nameStem }
        val shape = PrakriyaInvocationMatcher.match(ukti, knownStems) ?: return null
        val injectedText = injectedKarman?.reference?.let { "$it + अम्" }.orEmpty()
        val karmaText = listOf(injectedText, shape.karmaText)
            .filter(String::isNotBlank)
            .joinToString(" ")
        val resultReferences = dev.panini.execution.binding.NamedActionResultReferenceResolver.resolve(shape.argumentPadas)
        val resultValues = java.util.IdentityHashMap<Pada, SanskritValue>()
        resultReferences.forEach { reference ->
            if (reference.orderingAgrees) {
                resolveActionResult?.invoke(reference)?.let { resultValues[reference.result] = it }
            }
        }
        fun argumentType(pada: Pada): PrakriyaValueType = resultValues[pada]
            ?.let(PrakriyaValueClassifier::classifyValue) ?: if (staticAnalysis) {
                PrakriyaValueClassifier.classifyStaticPada(pada)
            } else PrakriyaValueClassifier.classifyPada(pada)
        val operandPadas = dev.panini.execution.binding.NamedActionResultReferenceResolver.operandPadas(shape.argumentPadas)
        operandPadas.filter(Pada::isAccusative).forEach { pada ->
            PrakriyaValueClassifier.literalValue(pada)?.let { resultValues[pada] = it }
        }
        operandPadas.filterIsInstance<SubantaPada>().filter {
            it.isAccusative() && NaturalSemanticNormalizer.isPriorResult(it) &&
                resultReferences.none { reference -> reference.result === it }
        }.forEach { pada -> resolvePriorResult?.invoke()?.let { resultValues[pada] = it } }
        operandPadas.filterIsInstance<SubantaPada>().filter { it.isAccusative() &&
            !NaturalSemanticNormalizer.isPriorResult(it) }.forEach { pada ->
            if (resultValues[pada] == null) resolveValue?.invoke(pada.pratipadika.referenceKey())
                ?.let { resultValues[pada] = it }
        }
        val writtenPadas = operandPadas.filter(Pada::isAccusative)
        val writtenTerms = writtenPadas.map(Pada::argumentTerm)
        val argumentTerms = listOfNotNull(injectedKarman?.reference) + writtenTerms
        val argumentTypes = listOfNotNull(
            injectedKarman?.value?.let(PrakriyaValueClassifier::classifyValue)
                ?: injectedKarman?.let {
                    if (staticAnalysis) PrakriyaValueType.UNCONSTRAINED else PrakriyaValueType.SHABDA
                },
        ) + writtenPadas.map(::argumentType)
        val scores = java.util.IdentityHashMap<Prakriya, Int>()
        fun score(kriya: Prakriya): Int = scores.getOrPut(kriya) {
            val orderedTypes = if (injectedKarman == null) {
                val resolved = NamedPrakriyaArgumentResolver.resolve(
                    operandPadas.filterIsInstance<SubantaPada>(), kriya.signature,
                )
                if (resolved is PrakriyaArgumentResolution.Failure) {
                    return@getOrPut AntaratamaOverloadEngine.TypeMatch.MISMATCH.rank
                }
                (resolved as? PrakriyaArgumentResolution.Success)?.arguments?.map { argument ->
                    argument.argument.value?.let(PrakriyaValueClassifier::classifyValue)
                        ?: argument.argument.pada?.let(::argumentType)
                        ?: if (staticAnalysis) PrakriyaValueType.UNCONSTRAINED else PrakriyaValueType.SHABDA
                } ?: argumentTypes
            } else {
                argumentTypes
            }
            AntaratamaOverloadEngine.matchTypes(kriya.signature, orderedTypes).rank
        }
        val candidates = allKriyas.sortedWith(
            compareByDescending<Prakriya> { score(it) > 0 }
                .thenByDescending { it.precedence.rank }
                .thenByDescending { score(it) },
        )
        val matching = candidates.filter { candidate ->
            candidate.nameStem == shape.operationStem &&
                domainMatches(candidate.domainStem, shape.domainStem) &&
                (!candidate.isInternal || candidate.sourceFile == null || callerSourceFile == candidate.sourceFile)
        }
        val kriya = matching.firstOrNull() ?: return null
        val ambiguous = (scores[kriya] ?: 0) > 0 && matching.drop(1).any {
            it.precedence == kriya.precedence && scores[it] == scores[kriya]
        }
        return PrakriyaInvocation(
            kriya = kriya,
            karmaText = karmaText,
            fullText = ukti.sourceText,
            ukti = ukti,
            argumentValues =
                (if (injectedKarman != null) listOf(injectedKarman.value) else emptyList()) +
                    writtenPadas.map { resultValues[it] },
            arguments =
                listOfNotNull(injectedKarman?.let { injected ->
                    PrakriyaArgument(injected.reference, value = injected.value, origin = PrakriyaArgumentOrigin.PIPE)
                }) + writtenPadas.map { pada ->
                    PrakriyaArgument(
                        term = pada.argumentTerm(),
                        pada = pada,
                        origin = PrakriyaArgumentOrigin.WRITTEN,
                        actionResult = resultReferences.singleOrNull { it.result === pada },
                        value = resultValues[pada],
                    )
                },
            argumentSyntax = operandPadas,
            resolutionError = if (ambiguous) "Ambiguous procedure overload for '${shape.operationStem}'." else null,
        )
    }

    private fun domainMatches(expected: String?, actual: String?): Boolean {
        if (expected == null || actual == null) return expected == actual
        if (expected == actual) return true
        var ancestor = inheritanceMap[actual]
        while (ancestor != null) {
            if (ancestor == expected) return true
            ancestor = inheritanceMap[ancestor]
        }
        return false
    }

}

data class PrakriyaInvocation(
    val kriya: Prakriya,
    val karmaText: String,
    val fullText: String,
    val ukti: dev.panini.vyakaranam.ast.Ukti? = null,
    val argumentValues: List<SanskritValue?> = emptyList(),
    val arguments: List<PrakriyaArgument> = emptyList(),
    val argumentSyntax: List<Pada> = emptyList(),
    val resolutionError: String? = null,
)

enum class PrakriyaArgumentOrigin { WRITTEN, PIPE }

/** One prakriyā operand, preserving both its grammatical AST and semantic value when known. */
data class PrakriyaArgument(
    val term: String,
    val pada: Pada? = null,
    val value: SanskritValue? = null,
    val origin: PrakriyaArgumentOrigin,
    val actionResult: dev.panini.execution.binding.NamedActionResultReference? = null,
)

private fun Pada.isAccusative(): Boolean {
    return (this as? dev.panini.vyakaranam.ast.SupBearingPada)?.sup
        ?.supports(dev.panini.core.Vibhakti.DVITIYA) == true
}

private fun Pada.argumentTerm(): String = when (this) {
    is SubantaPada -> pratipadika.referenceKey()
    is SankhyaPada -> stems.joinToString(" + ")
    is SankhyaPuranaPada -> stems.joinToString(" + ")
    is KatapayadiPada -> word
    is AryabhatiyaPada -> word
    is BhutasamkhyaPada -> terms.joinToString(" + ")
    else -> error("Only a parsed accusative nominal can be a procedure argument: $sourceText")
}

private val PrakriyaPrecedence.rank: Int
    get() = when (this) {
        PrakriyaPrecedence.DEFAULT -> 0
        PrakriyaPrecedence.NITYA -> 1
        PrakriyaPrecedence.ANTARANGA -> 2
        PrakriyaPrecedence.APAVADA -> 3
    }
