package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.vyakaranam.ast.KrtPratyayaIdentity
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
    val isMemoized: Boolean = PrakriyaHeaderIdentityParser.hasOperationKrtPratyayaIdentity(
        nameSegmented,
        KrtPratyayaIdentity.KTA,
    ),
) {
    init {
        require(body.all { it.program != null }) {
            "Every reusable प्रक्रिया body sentence must be valid, parsed Sanskrit."
        }
    }

    val signature: PrakriyaSignature by lazy { signatureOverride ?: PrakriyaSignatureCompiler.compile(body) }

    val isInternal: Boolean get() = visibility == PrakriyaVisibility.INTERNAL

    val nishedhaGuards: List<PvmScriptStatement.Sentence> = body.filter { it.isNishedha }
    val vidhiSentences: List<PvmScriptStatement.Sentence> = body.filterNot {
        it.isNishedha || PrakriyaSignatureDeclarationParser.isDeclaration(it)
    }
}

/**
 * Global registry of saṃjñā kriyās for a project/session.
 */
class PrakriyaRegistry {

    private val registry = linkedMapOf<String, MutableList<Prakriya>>()
    private val memoizedCache = mutableMapOf<String, ExecutionResult>()
    private val inheritanceMap = mutableMapOf<String, String>() // childStem -> parentStem
    private val schemas = linkedMapOf<String, TaddhitaStructSchema>()

    fun registerSchema(schema: TaddhitaStructSchema) {
        schemas[schema.nameStem] = schema
    }

    fun resolveSchema(nameStem: String): TaddhitaStructSchema? = schemas[nameStem]

    fun registerInheritance(relation: InheritanceRelation) {
        inheritanceMap[relation.childStem] = relation.parentStem
    }

    fun getParentClass(childStem: String): String? = inheritanceMap[childStem]

    fun getCachedResult(kriyaStem: String, argsKey: String): ExecutionResult? =
        memoizedCache["$kriyaStem::$argsKey"]

    fun cacheResult(kriyaStem: String, argsKey: String, result: ExecutionResult) {
        memoizedCache["$kriyaStem::$argsKey"] = result
    }

    fun register(kriya: Prakriya) {
        val key = if (kriya.domainStem != null) "${kriya.domainStem}::${kriya.nameStem}" else kriya.nameStem
        registry.getOrPut(key) { mutableListOf() }.add(kriya)
        registry.getOrPut(kriya.nameStem) { mutableListOf() }.add(kriya)
    }

    fun resolve(stem: String, callerSourceFile: String? = null): Prakriya? {
        val list = registry[stem] ?: return null
        val kriya = list.lastOrNull() ?: return null
        if (kriya.isInternal && kriya.sourceFile != null && callerSourceFile != kriya.sourceFile) {
            return null // File-private saṃjñā hidden from external caller
        }
        return kriya
    }

    fun all(): List<Prakriya> = registry.values.flatten().distinctBy { it.nameStem + (it.domainStem ?: "") }

    fun isEmpty(): Boolean = registry.isEmpty()

    val size: Int get() = registry.size

    /** Resolves an already parsed/synthesized pipeline stage without reconstructing Sanskrit text. */
    fun resolveStructuredInvocation(
        operationStem: String,
        domainStem: String?,
        arguments: List<PrakriyaArgument>,
        sourceText: String,
        callerSourceFile: String? = null,
    ): PrakriyaInvocation? {
        val argumentTypes = arguments.map { argument ->
            argument.value?.let(PrakriyaValueClassifier::classifyValue)
                ?: argument.pada?.let(PrakriyaValueClassifier::classifyPada)
                ?: PrakriyaValueType.SHABDA
        }
        val kriya = registry.values.flatten()
            .distinctBy { System.identityHashCode(it) }
            .asSequence()
            .filter { it.nameStem == operationStem && domainMatches(it.domainStem, domainStem) }
            .filterNot { it.isInternal && it.sourceFile != null && callerSourceFile != it.sourceFile }
            .sortedWith(
                compareByDescending<Prakriya> { it.precedence.rank }
                    .thenByDescending { AntaratamaOverloadEngine.matchTypes(it.signature, argumentTypes).rank },
            )
            .firstOrNull() ?: return null
        return PrakriyaInvocation(
            kriya = kriya,
            karmaText = arguments.joinToString(" ") { it.term },
            fullText = sourceText,
            argumentValues = arguments.map(PrakriyaArgument::value),
            arguments = arguments,
            argumentSyntax = arguments.mapNotNull(PrakriyaArgument::pada),
        )
    }

    /** Detects a reusable prakriyā from a parsed invocation without reparsing rendered text. */
    fun detectInvocation(
        ukti: dev.panini.vyakaranam.ast.Ukti,
        callerSourceFile: String? = null,
        injectedKarman: InjectedKarmanBinding? = null,
        resolveActionResult: ((dev.panini.execution.binding.NamedActionResultReference) -> SanskritValue?)? = null,
    ): PrakriyaInvocation? {
        if (registry.isEmpty()) return null

        val allKriyas = registry.values.flatten().distinctBy { System.identityHashCode(it) }
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
            ?.let(PrakriyaValueClassifier::classifyValue) ?: PrakriyaValueClassifier.classifyPada(pada)
        val operandPadas = dev.panini.execution.binding.NamedActionResultReferenceResolver.operandPadas(shape.argumentPadas)
        val writtenPadas = operandPadas.filter(Pada::isAccusative)
        val writtenTerms = writtenPadas.map(Pada::argumentTerm)
        val argumentTerms = listOfNotNull(injectedKarman?.reference) + writtenTerms
        val argumentTypes = listOfNotNull(
            injectedKarman?.value?.let(PrakriyaValueClassifier::classifyValue)
                ?: injectedKarman?.let { PrakriyaValueType.SHABDA },
        ) + writtenPadas.map(::argumentType)
        val candidates = allKriyas.sortedWith(
            compareByDescending<Prakriya> { it.precedence.rank }
                .thenByDescending {
                    val orderedTypes = if (injectedKarman == null) {
                        val resolved = NamedPrakriyaArgumentResolver.resolve(
                            operandPadas.filterIsInstance<SubantaPada>(),
                            it.signature,
                        )
                        (resolved as? PrakriyaArgumentResolution.Success)?.arguments?.map { argument ->
                            argument.argument.value?.let(PrakriyaValueClassifier::classifyValue)
                                ?: argument.argument.pada?.let(::argumentType)
                                ?: PrakriyaValueType.SHABDA
                        } ?: argumentTypes
                    } else {
                        argumentTypes
                    }
                    AntaratamaOverloadEngine.matchTypes(it.signature, orderedTypes).rank
                },
        )
        val kriya = candidates.firstOrNull { candidate ->
            candidate.nameStem == shape.operationStem &&
                domainMatches(candidate.domainStem, shape.domainStem) &&
                (!candidate.isInternal || candidate.sourceFile == null || callerSourceFile == candidate.sourceFile)
        } ?: return null
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
        )
    }

    private fun domainMatches(expected: String?, actual: String?): Boolean {
        if (expected == null || actual == null) return expected == actual
        if (expected == actual) return true
        return inheritanceMap[actual] == expected
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
    val supText = when (this) {
        is SubantaPada -> sup.text
        is SankhyaPada -> sup.text
        is SankhyaPuranaPada -> sup.text
        is KatapayadiPada -> sup.text
        is AryabhatiyaPada -> sup.text
        is BhutasamkhyaPada -> sup.text
        else -> return false
    }
    return SupAffix.fromUpadesha(supText)?.vibhakti == dev.panini.core.Vibhakti.DVITIYA
}

private fun Pada.argumentTerm(): String = when (this) {
    is SubantaPada -> pratipadika.semanticKey()
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
