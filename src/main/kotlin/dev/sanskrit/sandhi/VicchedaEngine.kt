package dev.sanskrit.sandhi

data class VicchedaApplication(
    val sutra: String,
    val sutraText: String,
    val hindiVyakhya: String,
    val input: String,
    val words: List<String>,
)

data class VicchedaResult(
    val input: String,
    val applications: List<VicchedaApplication>,
)

class VicchedaEngine(
    private val sutras: List<SandhiSutra> = SandhiSutraPatha.sutras,
) {
    private val orderedSutras = sutras.sortedByDescending { it.krama }

    fun split(pada: String): VicchedaResult {
        require(pada.isNotBlank()) { "Pada is required." }
        val context = VicchedaContext(pada)
        val applications = orderedSutras.flatMap { sutra ->
            sutra.split(context).map { change ->
                VicchedaApplication(
                    sutra = sutra.sutra,
                    sutraText = sutra.sutraText,
                    hindiVyakhya = sutra.hindiVyakhya,
                    input = pada,
                    words = change.words,
                )
            }
        }

        return VicchedaResult(
            input = pada,
            applications = applications.distinctBy { it.sutra to it.words },
        )
    }
}
