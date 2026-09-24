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

    fun resolveStructuredInvocation(
        operationStem: String,
        domainStem: String?,
        argumentTerms: List<String>,
        sourceText: String,
        callerSourceFile: String? = null,
        argumentValues: List<SanskritValue?> = emptyList(),
    ): PrakriyaInvocation? {
        val normalizedOperation = PrakriyaInvocationMatcher.normalizeIdentity(operationStem)
        val candidates = registry.values.flatten()
            .distinctBy { System.identityHashCode(it) }
            .asSequence()
            .filter {
                PrakriyaInvocationMatcher.normalizeIdentity(it.nameStem) == normalizedOperation &&
                    domainMatches(it.domainStem, domainStem)
            }
            .filterNot { it.isInternal && it.sourceFile != null && callerSourceFile != it.sourceFile }
            .sortedWith(
                compareByDescending<Prakriya> { it.precedence.rank }
                    .thenByDescending { AntaratamaOverloadEngine.match(it.signature, argumentTerms).rank },
            )
            .toList()
        val kriya = candidates.firstOrNull() ?: return null
        val karmaText = argumentTerms.joinToString(" ") { "$it + अम्" }
        return PrakriyaInvocation(
            kriya,
            karmaText,
            sourceText,
            argumentValues = argumentValues,
            arguments = argumentTerms.mapIndexed { index, term ->
                PrakriyaArgument(
                    term = term,
                    value = argumentValues.getOrNull(index),
                    origin = PrakriyaArgumentOrigin.WRITTEN,
                )
            },
        )
    }

    /** Detects a reusable prakriyā from a parsed invocation without reparsing rendered text. */
    fun detectInvocation(
        ukti: dev.panini.vyakaranam.ast.Ukti,
        callerSourceFile: String? = null,
        injectedKarman: InjectedKarmanBinding? = null,
    ): PrakriyaInvocation? {
        if (registry.isEmpty()) return null

        val allKriyas = registry.values.flatten().distinctBy { System.identityHashCode(it) }
        val knownStems = allKriyas.mapTo(mutableSetOf()) {
            PrakriyaInvocationMatcher.normalizeIdentity(it.nameStem)
        }
        val shape = PrakriyaInvocationMatcher.match(ukti, knownStems) ?: return null
        val injectedText = injectedKarman?.reference?.let { "$it + अम्" }.orEmpty()
        val karmaText = listOf(injectedText, shape.karmaText)
            .filter(String::isNotBlank)
            .joinToString(" ")
        val writtenPadas = shape.argumentPadas.filter(Pada::isAccusative)
        val writtenTerms = writtenPadas.map(Pada::argumentTerm)
        val argumentTerms = listOfNotNull(injectedKarman?.reference) + writtenTerms
        val candidates = allKriyas.sortedWith(
            compareByDescending<Prakriya> { it.precedence.rank }
                .thenByDescending {
                    val resolved = if (injectedKarman == null) {
                        NamedPrakriyaArgumentResolver.resolve(
                            shape.argumentPadas.filterIsInstance<SubantaPada>(),
                            it.signature,
                        )
                    } else {
                        NamedPrakriyaArgumentResolver.resolve(karmaText, it.signature)
                    }
                    val ordered = (resolved as? PrakriyaArgumentResolution.Success)?.terms ?: argumentTerms
                    AntaratamaOverloadEngine.match(it.signature, ordered).rank
                },
        )
        val kriya = candidates.firstOrNull { candidate ->
            PrakriyaInvocationMatcher.normalizeIdentity(candidate.nameStem) == shape.operationStem &&
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
                    List(writtenTerms.size) { null },
            arguments =
                listOfNotNull(injectedKarman?.let { injected ->
                    PrakriyaArgument(injected.reference, value = injected.value, origin = PrakriyaArgumentOrigin.PIPE)
                }) + writtenPadas.map { pada ->
                    PrakriyaArgument(
                        term = pada.argumentTerm(),
                        pada = pada,
                        origin = PrakriyaArgumentOrigin.WRITTEN,
                    )
                },
            argumentSyntax = shape.argumentPadas,
        )
    }

    private fun domainMatches(expected: String?, actual: String?): Boolean {
        if (expected == null || actual == null) return expected == actual
        val normalizedExpected = PrakriyaInvocationMatcher.normalizeIdentity(stripSupSuffix(expected))
        val normalizedActual = PrakriyaInvocationMatcher.normalizeIdentity(stripSupSuffix(actual))
        if (normalizedExpected == normalizedActual) return true
        val parent = inheritanceMap[actual] ?: inheritanceMap[normalizedActual] ?: return false
        return PrakriyaInvocationMatcher.normalizeIdentity(stripSupSuffix(parent)) == normalizedExpected
    }

    companion object {
        internal fun stripSupSuffix(nameSegmented: String): String {
            val parts = nameSegmented.split("+").map { it.trim() }
            if (parts.size <= 1) return nameSegmented
            val lastPart = parts.last()
            return if (SupAffix.fromUpadesha(lastPart) != null) {
                parts.dropLast(1).joinToString(" + ")
            } else {
                nameSegmented
            }
        }
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
)

private fun Pada.isAccusative(): Boolean {
    val supText = when (this) {
        is SubantaPada -> sup.text
        is SankhyaPada -> sup.text
        is SankhyaPuranaPada -> sup.text
        is KatapayadiPada -> sup.text
        is AryabhatiyaPada -> sup.text
        else -> return false
    }
    return SupAffix.fromUpadesha(supText)?.vibhakti == dev.panini.core.Vibhakti.DVITIYA
}

private fun Pada.argumentTerm(): String = when (this) {
    is SubantaPada -> pratipadika.sourceText.trim()
    is SankhyaPada -> stems.joinToString(" + ")
    is SankhyaPuranaPada -> stems.joinToString(" + ")
    is KatapayadiPada -> word
    is AryabhatiyaPada -> word
    else -> sourceText.substringBeforeLast('+').trim()
}

private val PrakriyaPrecedence.rank: Int
    get() = when (this) {
        PrakriyaPrecedence.DEFAULT -> 0
        PrakriyaPrecedence.NITYA -> 1
        PrakriyaPrecedence.ANTARANGA -> 2
        PrakriyaPrecedence.APAVADA -> 3
    }
