package dev.sanskrit.sandhi

class SandhiEngine(
    private val sutras: List<SandhiSutra> = SandhiSutraPatha.sutras,
) {
    private val orderedSutras = sutras.sortedByDescending { it.krama }

    fun join(words: List<String>): SandhiResult {
        require(words.isNotEmpty()) { "At least one word is required." }

        val applications = mutableListOf<SutraApplication>()
        var current = words.first()

        for (next in words.drop(1)) {
            val context = SandhiContext(current, next)
            val application = orderedSutras.firstNotNullOfOrNull { sutra ->
                sutra.tryApply(context)?.let { change -> sutra to change }
            }

            current = if (application == null) {
                "$current$next"
            } else {
                val (sutra, change) = application
                applications += SutraApplication(
                    sutra = sutra.sutra,
                    sutraText = sutra.sutraText,
                    hindiVyakhya = sutra.hindiVyakhya,
                    before = context.before,
                    after = change.combined,
                )
                change.combined
            }
        }

        return SandhiResult(
            input = words,
            output = current,
            applications = applications,
        )
    }

    companion object {
        val defaultSutras: List<SandhiSutra> = SandhiSutraPatha.sutras
    }
}
