package dev.panini.execution

import dev.panini.core.SupAffix

/**
 * A user-defined reusable kriyā, named via the संज्ञा-सूत्र pattern.
 */
data class SamjnaKriya(
    val nameSegmented: String,
    val nameStem: String,
    val body: List<PvmScriptStatement.Sentence>,
    val sourceFile: String? = null,
    val domainStem: String? = null,
    val isApavada: Boolean = false,
    val isAntaranga: Boolean = false,
    val isNitya: Boolean = false,
    val isInternal: Boolean = false,
    val isMemoized: Boolean = SamjnaHeaderIdentityParser.hasOperationKrtPratyaya(
        nameSegmented,
        MEMOIZING_KRT_PRATYAYAS,
    ),
) {
    val signature: SamjnaSignature by lazy { SamjnaSignatureCompiler.compile(body) }

    val precedence: SamjnaPrecedence get() = when {
        isApavada -> SamjnaPrecedence.APAVADA
        isAntaranga -> SamjnaPrecedence.ANTARANGA
        isNitya -> SamjnaPrecedence.NITYA
        else -> SamjnaPrecedence.DEFAULT
    }

    val nishedhaGuards: List<PvmScriptStatement.Sentence> = body.filter { it.isNishedha }
    val vidhiSentences: List<PvmScriptStatement.Sentence> = body.filterNot { it.isNishedha }
}

private val MEMOIZING_KRT_PRATYAYAS = setOf("क्त")

/**
 * Global registry of saṃjñā kriyās for a project/session.
 */
class SamjnaKriyaRegistry {

    private val registry = linkedMapOf<String, MutableList<SamjnaKriya>>()
    private val memoizedCache = mutableMapOf<String, ExecutionResult>()
    private val inheritanceMap = mutableMapOf<String, String>() // childStem -> parentStem

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
        if (kriya.isInternal && callerSourceFile != null && kriya.sourceFile != null && callerSourceFile != kriya.sourceFile) {
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
    ): SamjnaInvocation? {
        val normalizedOperation = SamjnaInvocationMatcher.normalizeIdentity(operationStem)
        val candidates = registry.values.flatten()
            .distinctBy { System.identityHashCode(it) }
            .asSequence()
            .filter {
                SamjnaInvocationMatcher.normalizeIdentity(it.nameStem) == normalizedOperation &&
                    domainMatches(it.domainStem, domainStem)
            }
            .filterNot {
                it.isInternal && callerSourceFile != null && it.sourceFile != null && callerSourceFile != it.sourceFile
            }
            .sortedWith(
                compareByDescending<SamjnaKriya> { it.precedence.rank }
                    .thenByDescending { AntaratamaOverloadEngine.match(it.signature, argumentTerms).rank },
            )
            .toList()
        val kriya = candidates.firstOrNull() ?: return null
        val karmaText = argumentTerms.joinToString(" ") { "$it + अम्" }
        return SamjnaInvocation(kriya, karmaText, sourceText)
    }

    fun detectInvocation(sentenceText: String, callerSourceFile: String? = null, preParsedUkti: dev.panini.vyakaranam.ast.Ukti? = null): SamjnaInvocation? {
        if (registry.isEmpty()) return null

        val isAntaranga = AntarangaScopeEngine.detectAntaranga(sentenceText, preParsedUkti)
        val textToProcess = if (isAntaranga) {
            AntarangaScopeEngine.stripAntarangaDirective(sentenceText, preParsedUkti)
        } else {
            sentenceText
        }

        val allKriyas = registry.values.flatten().distinctBy { System.identityHashCode(it) }
        val knownStems = allKriyas.mapTo(mutableSetOf()) {
            SamjnaInvocationMatcher.normalizeIdentity(it.nameStem)
        }
        val invocationShape = SamjnaInvocationMatcher.match(textToProcess, knownStems, preParsedUkti)
        val karmaText = invocationShape?.karmaText ?: textToProcess
        val argTerms = SubantaKarakaParser.extractKarmaTerms(karmaText, preParsedUkti)

        val candidates = allKriyas.sortedWith(
            compareByDescending<SamjnaKriya> { it.precedence.rank }
                .thenByDescending { AntaratamaOverloadEngine.match(it.signature, argTerms).rank },
        )
        if (invocationShape != null) {
            candidates.firstOrNull { kriya ->
                SamjnaInvocationMatcher.normalizeIdentity(kriya.nameStem) == invocationShape.operationStem &&
                    domainMatches(kriya.domainStem, invocationShape.domainStem)
            }?.let { kriya ->
                if (!kriya.isInternal || callerSourceFile == null || kriya.sourceFile == null || callerSourceFile == kriya.sourceFile) {
                    return SamjnaInvocation(
                        kriya = kriya,
                        karmaText = invocationShape.karmaText,
                        fullText = sentenceText,
                        ukti = invocationShape.ukti,
                    )
                }
            }
        }

        return null
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
)
