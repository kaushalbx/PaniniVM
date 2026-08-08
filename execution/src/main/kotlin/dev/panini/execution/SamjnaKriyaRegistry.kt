package dev.panini.execution

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
    val isMemoized: Boolean = nameSegmented.contains("+ क्त"),
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

    fun detectInvocation(sentenceText: String, callerSourceFile: String? = null, preParsedUkti: dev.panini.vyakaranam.ast.Ukti? = null): SamjnaInvocation? {
        if (registry.isEmpty()) return null

        val isAntaranga = AntarangaScopeEngine.detectAntaranga(sentenceText, preParsedUkti)
        val textToProcess = if (isAntaranga) AntarangaScopeEngine.stripAntarangaDirective(sentenceText) else sentenceText

        val allKriyas = registry.values.flatten().distinctBy { System.identityHashCode(it) }
        val argTerms = SubantaKarakaParser.extractKarmaTerms(textToProcess, preParsedUkti)

        val candidates = allKriyas.sortedWith(
            compareByDescending<SamjnaKriya> { it.precedence.rank }
                .thenByDescending { AntaratamaOverloadEngine.match(it.signature, argTerms).rank },
        )
        for (kriya in candidates) {
            if (kriya.isInternal && callerSourceFile != null && kriya.sourceFile != null && callerSourceFile != kriya.sourceFile) {
                continue // File-private saṃjñā hidden from external caller
            }

            val segmentedStem = kriya.nameStem
            val instrumentalPattern = "$segmentedStem + टा"

            // 1. Check Genitive Case domain qualification: "<domainStem> + ङस् <segmentedStem> + टा"
            if (kriya.domainStem != null) {
                val domainsToTry = mutableListOf(kriya.domainStem)
                // Add all child classes that inherit from kriya.domainStem
                inheritanceMap.forEach { (child, parent) ->
                    if (parent == kriya.domainStem || parent == stripSupSuffix(kriya.domainStem)) {
                        domainsToTry.add(child)
                    }
                }

                for (domain in domainsToTry) {
                    val genitivePattern = "$domain + ङस् $instrumentalPattern"
                    val genitiveMatupPattern = "$domain + मतुप् + ङस् $instrumentalPattern"
                    val genitiveVatupPattern = "$domain + वत् + ङस् $instrumentalPattern"

                    var genitiveIdx = textToProcess.indexOf(genitiveMatupPattern)
                    var matchedPattern = genitiveMatupPattern
                    if (genitiveIdx < 0) {
                        genitiveIdx = textToProcess.indexOf(genitiveVatupPattern)
                        matchedPattern = genitiveVatupPattern
                    }
                    if (genitiveIdx < 0) {
                        genitiveIdx = textToProcess.indexOf(genitivePattern)
                        matchedPattern = genitivePattern
                    }

                    if (genitiveIdx >= 0) {
                        if (kriya.domainStem != domain) {
                            val cleanDomain = domain.substringBefore("+").trim()
                            val exactChildMethod = registry.values.flatten().firstOrNull {
                                (it.domainStem == domain || it.domainStem == cleanDomain) && it.nameStem == kriya.nameStem && it.isApavada
                            }
                            if (exactChildMethod != null) {
                                continue
                            }
                        }
                        val afterGenitive = textToProcess.substring(genitiveIdx + matchedPattern.length).trim()
                        if (PradayaUpasargaEngine.isVerbAction(afterGenitive, preParsedUkti)) {
                            val karmaText = textToProcess.substring(0, genitiveIdx).trim()
                            return SamjnaInvocation(
                                kriya = kriya,
                                karmaText = karmaText,
                                fullText = sentenceText,
                                ukti = preParsedUkti,
                            )
                        }
                    }
                }
            }

            // 2. Check Unqualified instrumental invocation: "<segmentedStem> + टा"
            val patternIdx = textToProcess.indexOf(instrumentalPattern)
            if (patternIdx < 0) continue

            val afterInstrumental = textToProcess.substring(patternIdx + instrumentalPattern.length).trim()
            if (!PradayaUpasargaEngine.isVerbAction(afterInstrumental, preParsedUkti)) continue

            var karmaText = textToProcess.substring(0, patternIdx).trim()
            if (karmaText.contains("+ ङस्")) {
                val ngasIdx = karmaText.indexOf("+ ङस्")
                val textBeforeNgas = karmaText.substring(0, ngasIdx).trim()
                karmaText = textBeforeNgas.substringBeforeLast(" ").trim().ifEmpty { textBeforeNgas }
            }

            return SamjnaInvocation(
                kriya = kriya,
                karmaText = karmaText,
                fullText = sentenceText,
                ukti = preParsedUkti,
            )
        }
        return null
    }

    companion object {
        private val SUP_SUFFIXES = setOf(
            "सुँ", "औ", "जस्", "अम्", "औट्", "शस्",
            "टा", "भ्याम्", "भिस्", "ङे", "भ्याम्", "भ्यस्",
            "ङसि", "भ्याम्", "भ्यस्", "ङस्", "ओस्", "आम्",
            "ङि", "ओस्", "सुप्",
        )

        internal fun stripSupSuffix(nameSegmented: String): String {
            val parts = nameSegmented.split("+").map { it.trim() }
            if (parts.size <= 1) return nameSegmented
            val lastPart = parts.last()
            return if (lastPart in SUP_SUFFIXES) {
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
