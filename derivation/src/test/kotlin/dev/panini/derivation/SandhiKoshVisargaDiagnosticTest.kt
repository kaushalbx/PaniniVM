package dev.panini.derivation

import dev.panini.shiksha.Ayogavaha
import java.io.File
import java.text.Normalizer
import kotlin.test.Test
import org.junit.jupiter.api.Assumptions.assumeTrue

/** Input-source sensitivity, NOT a morphological analyzer or an accepted corpus score. */
class SandhiKoshVisargaDiagnosticTest {
    private fun compact(text: String) = Normalizer.normalize(text.trim(), Normalizer.Form.NFC)
        .filterNot(Char::isWhitespace)

    @Test fun diagnoseSourceSensitivity() {
        val path = System.getProperty("sandhikosh.evaluation.dir")
        assumeTrue(path != null, "External diagnostic requires a corpus directory")
        val base = File(requireNotNull(path))
        val records = TestJsonParser.parse(File(base, "cases.json").readText()) as List<*>
        val evaluator = SandhiSequenceEvaluator()
        var considered = 0; var baseline = 0; var sMatches = 0; var rMatches = 0; var errors = 0
        val recoveredS = mutableListOf<String>()
        val recoveredR = mutableListOf<String>()
        val errorDetails = mutableListOf<String>()
        for (raw in records) {
            val record = raw as Map<*, *>
            val expected = record["expected"] as String
            val words = (record["split"] as String).split('+').map(String::trim)
            if (expected.isBlank() || words.size < 2 || words.any(String::isBlank)) continue
            try {
                val padas = words.mapIndexed { index, word ->
                    SandhiEngine.Pada(DerivationTerm("diagnostic_$index", word, TermKind.PRATIPADIKA))
                }
                if (padas.none { it.term.varnas.lastOrNull() == Ayogavaha.VISARGA }) continue
                considered++
                fun matches(inputs: List<SandhiEngine.Pada>) =
                    evaluator.evaluatePadas(inputs).any { compact(it.surface) == compact(expected) }
                val plain = matches(padas)
                if (plain) baseline++
                for (source in listOf(VisargaSource.S, VisargaSource.R)) {
                    val inputs = padas.map { pada ->
                        if (pada.term.varnas.lastOrNull() == Ayogavaha.VISARGA) pada.copy(visargaSource = source) else pada
                    }
                    if (matches(inputs)) {
                        if (source == VisargaSource.S) sMatches++ else rMatches++
                        if (!plain) {
                            val line = "row=${record["row"]} split=${record["split"]} hypothesis=$source"
                            if (source == VisargaSource.S) recoveredS += line else recoveredR += line
                        }
                    }
                }
            } catch (error: IllegalStateException) {
                errors++
                errorDetails += "row=${record["row"]} ${error::class.simpleName}: ${error.message}"
            } catch (error: IllegalArgumentException) {
                errors++
                errorDetails += "row=${record["row"]} ${error::class.simpleName}: ${error.message}"
            }
        }
        val summary = "considered=$considered baselineMatches=$baseline uniformSHypothesisMatches=$sMatches " +
            "uniformRHypothesisMatches=$rMatches diagnosticErrors=$errors"
        File(base, "visarga-source-sensitivity.txt").writeText(
            "DIAGNOSTIC ONLY: uniform source hypotheses are not verified analyses or accepted pass scores.\n" +
                "CONTINUATION, default grammatical choices; mixed sources and optional rules are not explored.\n" +
                summary + "\n\nNew matches under S hypothesis:\n" + recoveredS.joinToString("\n") +
                "\n\nNew matches under R hypothesis:\n" + recoveredR.joinToString("\n") +
                "\n\nDiagnostic errors (including input parsing before visarga selection):\n" + errorDetails.joinToString("\n"))
        println("VISARGA SOURCE SENSITIVITY $summary")
    }
}
