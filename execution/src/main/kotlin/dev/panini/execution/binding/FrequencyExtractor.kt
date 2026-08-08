package dev.panini.execution.binding

import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.KriyaQualificationKind
import dev.panini.sankhya.SankhyaAbhyasaMarkers
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SankhyaAbhyasaPada

/**
 * Helpers for determining how many times a verbal action should be performed.
 * Covers both per-clause frequency qualifiers and whole-utterance repetition counts.
 */
internal object FrequencyExtractor {
    internal fun isAbhyasa(stems: List<String>): Boolean = stems.any(SankhyaAbhyasaMarkers::isFrequency)

    internal fun numericStems(stems: List<String>): List<String> =
        SankhyaAbhyasaMarkers.numericFrequencyStems(stems)

    /**
     * Extracts the per-clause frequency repetition count from [padas] and [frame].
     * Returns null when no explicit frequency qualifier is present.
     *
     * Handles:
     * - अभ्यास-सङ्ख्या padas (e.g. *कृत्वः forms)
     * - KriyaQualificationKind.FREQUENCY qualifiers (सकृत्, द्विः, त्रिः, पुनः …)
     */
    internal fun extractFrequencyCount(padas: List<Pada>, frame: KriyaFrame): Int? {
        val sankhyaAbhyasa = padas.filterIsInstance<SankhyaAbhyasaPada>().firstOrNull()
        if (sankhyaAbhyasa != null) {
            val numStems = numericStems(sankhyaAbhyasa.stems)
            val evaluated = if (numStems.isNotEmpty()) {
                sharedSankhyaEvaluator.evaluateStems(numStems)
            } else {
                sharedSankhyaEvaluator.evaluateStems(sankhyaAbhyasa.stems)
            }
            return evaluated.value.toInt()
        }
        val freqQual = frame.qualifications.firstOrNull { it.kind == KriyaQualificationKind.FREQUENCY }
        if (freqQual != null) {
            val text = freqQual.value
            // Delegate to SankhyaEvaluator — it natively handles:
            //   सकृत्, द्विः, त्रिः, चतुः (parseStandaloneFrequency)
            //   *कृत्वः / *कृत्वस् compound forms (evaluateStems suffix branch)
            val evaluated = runCatching { sharedSankhyaEvaluator.evaluateStems(listOf(text)) }.getOrNull()
            if (evaluated != null && evaluated.value > 0) return evaluated.value.toInt()
            // पुनः / पुनर् are pragmatic repetition markers ("again" = do once more),
            // not purely numerical — retain as minimal policy.
            if (AvyayaFunction.fromForm(text) == AvyayaFunction.REPETITION) return 2
        }
        return null
    }
}
