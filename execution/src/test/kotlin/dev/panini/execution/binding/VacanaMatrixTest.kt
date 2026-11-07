package dev.panini.execution.binding

import dev.panini.analysis.FrameKarakaResolution
import dev.panini.core.Karaka
import dev.panini.vyakaranam.parser.PaniniParser
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every basic role in all three vacanas. Open results are listed in a report; a wrong kāraka fails. */
class VacanaMatrixTest {
    private data class Row(val rule: String, val karaka: Karaka, val endings: List<String>, val sentence: (String) -> String)

    private val verbs = listOf("तिप्", "तस्", "झि")
    private val rows = listOf(
        Row("kartā", Karaka.KARTR, listOf("सुँ", "औ", "जस्")) { "राम + $it गम् + लट् + ${verbs[listOf("सुँ", "औ", "जस्").indexOf(it)]}" },
        Row("karma", Karaka.KARMAN, listOf("अम्", "औट्", "शस्")) { "राम + सुँ ग्राम + $it गम् + लट् + तिप्" },
        Row("expressed karma", Karaka.KARMAN, listOf("सुँ", "औ", "जस्")) { "राम + टा ग्राम + $it गम् + यक् + लट् + ${listOf("त", "आताम्", "झ")[listOf("सुँ", "औ", "जस्").indexOf(it)]}" },
        Row("passive kartā", Karaka.KARTR, listOf("टा", "भ्याम्", "भिस्")) { "राम + $it ग्राम + सुँ गम् + यक् + लट् + त" },
        Row("karaṇa", Karaka.KARANA, listOf("टा", "भ्याम्", "भिस्")) { "राम + सुँ हस्त + $it डुकृञ् + लट् + तिप्" },
        Row("sampradāna", Karaka.SAMPRADANA, listOf("ङे", "भ्याम्", "भ्यस्")) { "राम + सुँ ब्राह्मण + $it गो + अम् दा + लट् + तिप्" },
        Row("apādāna", Karaka.APADANA, listOf("ङसिँ", "भ्याम्", "भ्यस्")) { "पर्ण + सुँ वृक्ष + $it पत् + लट् + तिप्" },
        Row("adhikaraṇa", Karaka.ADHIKARANA, listOf("ङि", "ओस्", "सुप्")) { "वृक्ष + $it वसँ + लट् + तिप्" },
    )

    @Test fun `each role holds in every vacana`() {
        val parser = PaniniParser()
        val report = StringBuilder()
        val wrong = mutableListOf<String>()
        rows.forEach { row -> row.endings.forEach { ending ->
            val source = row.sentence(ending)
            val frame = VyakaranamExecutionAdapter.analyzeGrammar(parser.parse("$source ।"))!!.frames.single()
            val target = frame.relations.first { relation -> relation.participant.pada.sourceText.endsWith("+$ending") && relation.resolution.let { it !is FrameKarakaResolution.Resolved || it.karaka == row.karaka } }
            val got = target.resolution
            val label = if (got is FrameKarakaResolution.Resolved) got.karaka.name else "OPEN"
            report.appendLine("${row.rule} $ending: $label")
            if (label != "OPEN" && label != row.karaka.name) wrong += "${row.rule} $ending $label"
        } }
        File("build").mkdirs()
        File("build/vacana_matrix.txt").writeText(report.toString(), Charsets.UTF_8)
        assertEquals(emptyList(), wrong)
    }
}
