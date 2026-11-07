package dev.panini.execution

import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.parser.PaniniParser
import java.io.File
import kotlin.test.Test

/** Kārakas, not positions, should decide what a sentence does: reordering its words must not change the program's output. */
class KarakaWordOrderTest {
    private fun output(source: String): List<String>? = runCatching {
        val results = PaniniVM().evalScript(source)
        if (results.any { it is ExecutionResult.Failure }) null
        else results.filterIsInstance<ExecutionResult.Success>().filter { it.outputKind == OutputKind.CONSOLE }.map { it.value }
    }.getOrNull()

    /** Every plain sentence of a document, top level or in a procedure body, with where it stands. */
    private fun sentences(document: dev.panini.vyakaranam.ast.ProgramDocument) = document.items.flatMapIndexed { index, item ->
        when (item) {
            is Ukti -> listOf(item.body to document.itemSpans[index])
            is dev.panini.vyakaranam.ast.Prakriya -> item.body.zip(document.prakriyaBodySpans[index].orEmpty())
            else -> emptyList()
        }
    }

    @Test fun `no sentence depends on word order`() {
        val parser = PaniniParser()
        val lines = mutableListOf<String>(); var same = 0; var checked = 0
        listOf(File("examples"), File("projects")).flatMap { it.walkTopDown().toList() }
            .filter { it.extension == "pvm" && listOf("random", "lotto", "dice", "prisoners", "guessing").none { word -> word in it.name } }.forEach { file ->
            val source = file.readText(Charsets.UTF_8)
            val baseline = output(source) ?: return@forEach
            val document = runCatching { parser.parseDocument(source) }.getOrNull() ?: return@forEach
            sentences(document).forEach { (body, span) ->
                val vakya = (body as? Invocation)?.vakya as? AkhyataVakya ?: return@forEach
                val padas = vakya.padas
                if (padas.size < 3 || padas.any { it is AvyayaPada }) return@forEach
                // Genitives in a chain relate to one another, and only their order tells the owner from the owned.
                if (padas.count { it.sourceText.replace(" ", "").endsWith("+ङस्") } > 1) return@forEach
                // Two nominatives are subject and predicate, and only their order tells which is which.
                if (padas.count { Regex("\\+(सुँ|औ|जस्)$").containsMatchIn(it.sourceText) } > 1) return@forEach
                val terminator = source.substring(span.start, span.endExclusive).trimEnd().last()
                val reordered = padas.reversed().joinToString(" ") { it.sourceText } + " " + terminator
                val changed = source.substring(0, span.start) + reordered + source.substring(span.endExclusive)
                checked++
                val result = output(changed)
                if (result == baseline) same++ else lines += file.name + " | " + padas.joinToString(" ") { it.sourceText } + " | " + result?.take(3) + " vs " + baseline.take(3)
            }
        }
        File("build").mkdirs()
        File("build/wordorder.txt").writeText("checked $checked same $same different ${lines.size}\n" + lines.joinToString("\n"), Charsets.UTF_8)
        kotlin.test.assertEquals(emptyList(), lines)
    }

    @Test fun `passive and causative run from their karakas in any order`() {
        for (source in listOf("पञ्चन् + जस् राम + टा मुद्र् + यक् + लोट् + झ ।", "राम + टा पञ्चन् + जस् मुद्र् + यक् + लोट् + झ ।",
            "मुद्र् + यक् + लोट् + झ पञ्चन् + जस् राम + टा ।", "गुरु + सुँ शिष्य + टा पञ्चन् + शस् मुद्र् + णिच् + लोट् + तिप् ।",
            "पञ्चन् + शस् शिष्य + टा गुरु + सुँ मुद्र् + णिच् + लोट् + तिप् ।")) kotlin.test.assertEquals(listOf("पञ्चन्"), output(source), source)
    }

    @Test fun `every way of running a program rejects a verbless sentence`() {
        val verbless = "सत्य + सुँ ।"
        val eval = runCatching { PaniniVM().eval(verbless) }
        kotlin.test.assertTrue(eval.isFailure || eval.getOrNull() is ExecutionResult.Failure, "eval: $eval")
        val script = runCatching { PaniniVM().evalScript(verbless) }
        kotlin.test.assertTrue(script.isFailure || script.getOrThrow().any { it is ExecutionResult.Failure }, "evalScript: $script")
    }
}
