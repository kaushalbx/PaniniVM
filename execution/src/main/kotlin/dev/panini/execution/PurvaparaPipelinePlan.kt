package dev.panini.execution

import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.PipelineStage

/** Compatibility compiler for the special 6.1.84 source directive. */
object PurvaparaPipelineCompiler {
    private val directiveMarkers = listOf("पूर्व + पर + ङस्", "पूर्व + पर", "पूर्वपरयोः")
    private val resultMarkers = listOf("एका + सुँ", "एकः")

    fun compile(source: String): Pipeline? {
        if (directiveMarkers.none(source::contains)) return null
        val withoutDirective = (directiveMarkers + resultMarkers).fold(source) { text, marker ->
            text.replace(marker, "")
        }.trim()
        val actionBoundary = withoutDirective.indexOf("कृ + लोट् + सिप्")
        val body = if (actionBoundary >= 0) withoutDirective.substring(0, actionBoundary).trim() else withoutDirective
        val conjunction = body.indexOf(" च ")
        val argumentSource = if (conjunction >= 0) body.substring(0, conjunction + 3).trim() else ""
        val stageSource = if (conjunction >= 0) body.substring(conjunction + 3).trim() else body
        val terms = stageSource.split("+ ङस्").map(String::trim).filter(String::isNotEmpty)
        val stages = buildList {
            var index = 0
            while (index < terms.size) {
                val domain = terms.getOrNull(index)
                val operation = terms.getOrNull(index + 1)
                if (domain != null && operation != null) {
                    add(
                        PipelineStage(
                            sourceText = listOfNotNull(domain, operation).joinToString(" + ङस् "),
                            domainStem = SamjnaKriyaRegistry.stripSupSuffix(domain),
                            operationStem = SamjnaKriyaRegistry.stripSupSuffix(operation),
                        ),
                    )
                }
                index += 2
            }
        }
        return Pipeline(
            sourceText = source,
            arguments = SubantaKarakaParser.extractKarmaTerms(argumentSource),
            stages = stages,
        )
    }
}
