package dev.panini.execution.binding

import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.KriyaQualificationKind
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SankhyaAbhyasaPada

/**
 * Helpers for determining how many times a verbal action should be performed.
 * Covers both per-clause frequency qualifiers and whole-utterance repetition counts.
 */
internal object FrequencyExtractor {
    /**
     * Suffix stems in an अभ्यास-सङ्ख्या that indicate repetition count, not a
     * numeric kāraka argument value.  These are filtered out before numeric evaluation.
     */
    internal val ABHYASA_SUFFIX_STEMS = setOf("कृत्वः", "कृत्वा", "कृत्वसुच्", "सुच्")

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
            val numStems = sankhyaAbhyasa.stems.filterNot { it in ABHYASA_SUFFIX_STEMS }
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
            if (text == "पुनः" || text == "पुनर्") return 2
        }
        return null
    }
}
