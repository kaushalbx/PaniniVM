package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.vyakaranam.ast.KrtPratyayaIdentity
import dev.panini.vyakaranam.ast.ProcedurePrecedence
import dev.panini.vyakaranam.ast.ProcedureVisibility

/**
 * A user-defined reusable kriyā, named via the संज्ञा-सूत्र pattern.
 */
data class SamjnaKriya(
    val nameSegmented: String,
    val nameStem: String,
    val body: List<PvmScriptStatement.Sentence>,
    val sourceFile: String? = null,
    val domainStem: String? = null,
    val visibility: ProcedureVisibility = ProcedureVisibility.PUBLIC,
    val precedence: ProcedurePrecedence = ProcedurePrecedence.DEFAULT,
    val signatureOverride: SamjnaSignature? = null,
    val isMemoized: Boolean = SamjnaHeaderIdentityParser.hasOperationKrtPratyayaIdentity(
        nameSegmented,
        KrtPratyayaIdentity.KTA,
    ),
) {
    val signature: SamjnaSignature by lazy { signatureOverride ?: SamjnaSignatureCompiler.compile(body) }

    val isInternal: Boolean get() = visibility == ProcedureVisibility.INTERNAL

    val nishedhaGuards: List<PvmScriptStatement.Sentence> = body.filter { it.isNishedha }
    val vidhiSentences: List<PvmScriptStatement.Sentence> = body.filterNot {
        it.isNishedha || SamjnaSignatureDeclarationParser.isDeclaration(it)
    }
}

/**
 * Global registry of saṃjñā kriyās for a project/session.
 */
class SamjnaKriyaRegistry {

    private val registry = linkedMapOf<String, MutableList<SamjnaKriya>>()
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

    fun register(kriya: SamjnaKriya) {
        val key = if (kriya.domainStem != null) "${kriya.domainStem}::${kriya.nameStem}" else kriya.nameStem
        registry.getOrPut(key) { mutableListOf() }.add(kriya)
        registry.getOrPut(kriya.nameStem) { mutableListOf() }.add(kriya)
    }

    fun resolve(stem: String, callerSourceFile: String? = null): SamjnaKriya? {
        val list = registry[stem] ?: return null
        val kriya = list.lastOrNull() ?: return null
        if (kriya.isInternal && kriya.sourceFile != null && callerSourceFile != kriya.sourceFile) {
            return null // File-private saṃjñā hidden from external caller
        }
        return kriya
    }

    fun all(): List<SamjnaKriya> = registry.values.flatten().distinctBy { it.nameStem + (it.domainStem ?: "") }

    fun isEmpty(): Boolean = registry.isEmpty()

    val size: Int get() = registry.size

    fun resolveStructuredInvocation(
        operationStem: String,
        domainStem: String?,
        argumentTerms: List<String>,
        sourceText: String,
        callerSourceFile: String? = null,
        argumentValues: List<SanskritValue?> = emptyList(),
    ): SamjnaInvocation? {
        val normalizedOperation = SamjnaInvocationMatcher.normalizeIdentity(operationStem)
        val candidates = registry.values.flatten()
            .distinctBy { System.identityHashCode(it) }
            .asSequence()
            .filter {
                SamjnaInvocationMatcher.normalizeIdentity(it.nameStem) == normalizedOperation &&
                    domainMatches(it.domainStem, domainStem)
            }
            .filterNot { it.isInternal && it.sourceFile != null && callerSourceFile != it.sourceFile }
            .sortedWith(
                compareByDescending<SamjnaKriya> { it.precedence.rank }
                    .thenByDescending { AntaratamaOverloadEngine.match(it.signature, argumentTerms).rank },
            )
            .toList()
        val kriya = candidates.firstOrNull() ?: return null
        val karmaText = argumentTerms.joinToString(" ") { "$it + अम्" }
        return SamjnaInvocation(kriya, karmaText, sourceText, argumentValues = argumentValues)
    }

    /** Detects a reusable procedure from a parsed invocation without reparsing rendered text. */
    fun detectInvocation(
        ukti: dev.panini.vyakaranam.ast.Ukti,
        callerSourceFile: String? = null,
        injectedKarman: Pair<String, SanskritValue?>? = null,
    ): SamjnaInvocation? {
        if (registry.isEmpty()) return null

        val allKriyas = registry.values.flatten().distinctBy { System.identityHashCode(it) }
        val knownStems = allKriyas.mapTo(mutableSetOf()) {
            SamjnaInvocationMatcher.normalizeIdentity(it.nameStem)
        }
        val shape = SamjnaInvocationMatcher.match(ukti, knownStems) ?: return null
        val injectedText = injectedKarman?.first?.let { "$it + अम्" }.orEmpty()
        val karmaText = listOf(injectedText, shape.karmaText)
            .filter(String::isNotBlank)
            .joinToString(" ")
        val writtenTerms = SubantaKarakaParser.extractKarmaTerms(shape.karmaText, shape.ukti)
        val argumentTerms = listOfNotNull(injectedKarman?.first) + writtenTerms
        val candidates = allKriyas.sortedWith(
            compareByDescending<SamjnaKriya> { it.precedence.rank }
                .thenByDescending {
                    val resolved = NamedSamjnaArgumentResolver.resolve(karmaText, it.signature)
                    val ordered = (resolved as? SamjnaArgumentResolution.Success)?.terms ?: argumentTerms
                    AntaratamaOverloadEngine.match(it.signature, ordered).rank
                },
        )
        val kriya = candidates.firstOrNull { candidate ->
            SamjnaInvocationMatcher.normalizeIdentity(candidate.nameStem) == shape.operationStem &&
                domainMatches(candidate.domainStem, shape.domainStem) &&
                (!candidate.isInternal || candidate.sourceFile == null || callerSourceFile == candidate.sourceFile)
        } ?: return null
        return SamjnaInvocation(
            kriya = kriya,
            karmaText = karmaText,
            fullText = ukti.sourceText,
            ukti = ukti,
            argumentValues =
                (if (injectedKarman != null) listOf(injectedKarman.second) else emptyList()) +
                    List(writtenTerms.size) { null },
        )
    }

    private fun domainMatches(expected: String?, actual: String?): Boolean {
        if (expected == null || actual == null) return expected == actual
        val normalizedExpected = SamjnaInvocationMatcher.normalizeIdentity(stripSupSuffix(expected))
        val normalizedActual = SamjnaInvocationMatcher.normalizeIdentity(stripSupSuffix(actual))
        if (normalizedExpected == normalizedActual) return true
        val parent = inheritanceMap[actual] ?: inheritanceMap[normalizedActual] ?: return false
        return SamjnaInvocationMatcher.normalizeIdentity(stripSupSuffix(parent)) == normalizedExpected
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

data class SamjnaInvocation(
    val kriya: SamjnaKriya,
    val karmaText: String,
    val fullText: String,
    val ukti: dev.panini.vyakaranam.ast.Ukti? = null,
    val argumentValues: List<SanskritValue?> = emptyList(),
)

private val ProcedurePrecedence.rank: Int
    get() = when (this) {
        ProcedurePrecedence.DEFAULT -> 0
        ProcedurePrecedence.NITYA -> 1
        ProcedurePrecedence.ANTARANGA -> 2
        ProcedurePrecedence.APAVADA -> 3
    }
