package dev.sanskrit.sandhi

import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.BaseSutra
import dev.sanskrit.sutra.SutraMetadata

data class SutraApplication(
    val sutra: String,
    val sutraText: String,
    val hindiVyakhya: String,
    val before: String,
    val after: String,
)

data class SandhiResult(
    val input: List<String>,
    val output: String,
    val applications: List<SutraApplication>,
)

data class SandhiContext(
    val left: String,
    val right: String,
) {
    val before: String = "$left $right"
}

interface SandhiSutra : Sutra<SandhiContext, BoundaryChange> {
    override val metadata: SutraMetadata

    fun tryApply(context: SandhiContext): BoundaryChange? =
        if (matches(context)) apply(context) else null

    fun split(context: VicchedaContext): List<VicchedaChange> = emptyList()
}

abstract class BaseSandhiSutra(
    metadata: SutraMetadata,
) : BaseSutra<SandhiContext, BoundaryChange>(metadata), SandhiSutra

data class BoundaryChange(
    val combined: String,
    val explanation: String? = null,
)

data class VicchedaContext(
    val pada: String,
)

data class VicchedaChange(
    val left: String,
    val right: String,
    val explanation: String? = null,
) {
    val words: List<String> = listOf(left, right)
}
