package dev.panini.vyakaranam

import dev.panini.core.Linga
import dev.panini.dhatupatha.rudhadi.YujirDhatu
import dev.panini.vyakaranam.lexicon.InMemoryVyakaranamLexicon
import dev.panini.vyakaranam.lexicon.PratipadikaEntry

fun main() {
    val lexicon = InMemoryVyakaranamLexicon(
        pratipadikas = listOf(
            PratipadikaEntry(
                text = "राम",
                linga = setOf(Linga.PUMS),
                stemClass = "अकारान्त",
            ),
            PratipadikaEntry(
                text = "फल",
                linga = setOf(Linga.NAPUMSAKA),
                stemClass = "अकारान्त",
            ),
        ),
        dhatus = listOf(YujirDhatu()),
    )

    val engine = PaniniyaVyakaranamEngine(lexicon)

    val result = engine.analyze(
        """
        राम + सुँ
        फल + अम्
        खाद् + लट् + तिप् ।
        """.trimIndent(),
    )

    result.vakyas.forEach { vakya ->
        println("प्रयोगः ${vakya.prayoga}")

        vakya.karakas.forEach { assignment ->
            println(
                "${assignment.pada.sourceText} → " +
                        "${assignment.karaka} " +
                        "(${assignment.reason})",
            )
        }

        vakya.warnings.forEach {
            println("सूचना: $it")
        }
    }
}
