package dev.panini.execution

import dev.panini.vyakaranam.ast.Ukti

/** Surface-text compatibility matcher for source forms not yet represented by the AST. */
internal object LegacySamjnaInvocationMatcher {
    fun match(
        sentenceText: String,
        candidates: List<SamjnaKriya>,
        allKriyas: List<SamjnaKriya>,
        inheritance: Map<String, String>,
        callerSourceFile: String?,
        preParsedUkti: Ukti?,
    ): SamjnaInvocation? {
        for (kriya in candidates) {
            if (kriya.isInternal && callerSourceFile != null && kriya.sourceFile != null && callerSourceFile != kriya.sourceFile) {
                continue
            }
            val instrumentalPattern = "${kriya.nameStem} + टा"
            val domains = buildList {
                kriya.domainStem?.let(::add)
                inheritance.forEach { (child, parent) ->
                    if (parent == kriya.domainStem || parent == kriya.domainStem?.let(SamjnaKriyaRegistry::stripSupSuffix)) {
                        add(child)
                    }
                }
            }
            for (domain in domains) {
                val patterns = listOf(
                    "$domain + मतुप् + ङस् $instrumentalPattern",
                    "$domain + वत् + ङस् $instrumentalPattern",
                    "$domain + ङस् $instrumentalPattern",
                )
                val matched = patterns.firstOrNull(sentenceText::contains) ?: continue
                if (kriya.domainStem != domain) {
                    val cleanDomain = domain.substringBefore("+").trim()
                    val childOverride = allKriyas.any {
                        (it.domainStem == domain || it.domainStem == cleanDomain) &&
                            it.nameStem == kriya.nameStem && it.isApavada
                    }
                    if (childOverride) continue
                }
                val index = sentenceText.indexOf(matched)
                val afterInvocation = sentenceText.substring(index + matched.length).trim()
                if (PradayaUpasargaEngine.isVerbAction(afterInvocation, preParsedUkti)) {
                    return SamjnaInvocation(
                        kriya,
                        sentenceText.substring(0, index).trim(),
                        sentenceText,
                        preParsedUkti,
                        SamjnaMatchOrigin.COMPATIBILITY,
                    )
                }
            }

            val index = sentenceText.indexOf(instrumentalPattern)
            if (index < 0) continue
            val afterInvocation = sentenceText.substring(index + instrumentalPattern.length).trim()
            if (!PradayaUpasargaEngine.isVerbAction(afterInvocation, preParsedUkti)) continue
            var karmaText = sentenceText.substring(0, index).trim()
            if (karmaText.contains("+ ङस्")) {
                val beforeGenitive = karmaText.substringBefore("+ ङस्").trim()
                karmaText = beforeGenitive.substringBeforeLast(" ").trim().ifEmpty { beforeGenitive }
            }
            return SamjnaInvocation(
                kriya,
                karmaText,
                sentenceText,
                preParsedUkti,
                SamjnaMatchOrigin.COMPATIBILITY,
            )
        }
        return null
    }
}
