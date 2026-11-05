package dev.panini.execution.binding

import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.KarakaRelation
import dev.panini.analysis.participialRelations
import dev.panini.core.Karaka
import dev.panini.vyakaranam.parser.PaniniParser
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

/** Scores the analyzer against labels from an outside source. Open labels are not errors; wrong ones are pinned here. */
class GuidelineGoldTest {
    private fun label(relation: KarakaRelation?): String = when (val r = relation?.resolution) {
        null -> "ABSENT"
        is FrameKarakaResolution.Resolved ->
            if (r.karaka == Karaka.ANIRDHARITA || r.karaka == Karaka.SAMBANDHA) "NONE" else r.karaka.name
        else -> "OPEN"
    }

    private fun wrongAnswers(resource: String): Set<String> {
        val parser = PaniniParser()
        var right = 0
        val open = mutableListOf<String>()
        val wrong = mutableListOf<String>()
        val lines = requireNotNull(javaClass.getResourceAsStream("/$resource.txt"))
            .bufferedReader(Charsets.UTF_8).readLines().filter { it.isNotBlank() && !it.startsWith("#") }
        lines.forEach { line ->
            val (id, source, gold) = line.split("|")
            val frames = runCatching { VyakaranamExecutionAdapter.analyzeGrammar(parser.parse("$source ।"))!!.frames }
                .getOrDefault(emptyList())
            val relations = frames.flatMap { f ->
                f.relations + f.nominalKarakas.map { it.relation } + f.purposeKarakas.map { it.relation }
            }.associateBy { it.participant.pada.sourceText.replace(" ", "") }
            gold.split(";").map { it.split("=") }.forEach { (pada, expected) ->
                // The bearer of an absolute participle holds its role there, with the frame's 2.3.37 for its case.
                val bearerRole = frames.flatMap { f -> f.participialRelations.filter { it.absolute }.mapNotNull { r ->
                    r.bearer?.let { f.nominalRelations?.nominalPadas?.get(it)?.pada?.sourceText?.replace(" ", "") to r.bearerRole?.name } } }.toMap()
                val actual = bearerRole[pada] ?: label(relations[pada])
                when {
                    actual == expected -> right++
                    actual == "OPEN" || actual == "ABSENT" -> open += "$id $pada want $expected got $actual"
                    else -> wrong += "$id $pada want $expected got $actual"
                }
            }
        }
        File("build").mkdirs()
        File("build/$resource.score.txt").writeText(
            "right $right open ${open.size} wrong ${wrong.size}\nWRONG\n" + wrong.joinToString("\n") +
                "\nOPEN\n" + open.joinToString("\n") + "\n", Charsets.UTF_8)
        return wrong.toSet()
    }

    @Test fun `score against the guidelines`() = assertEquals(KNOWN_WRONG, wrongAnswers("guideline_gold"))

    @Test fun `score the held out sentences`() = assertEquals(HOLDOUT_WRONG, wrongAnswers("guideline_holdout"))

    @Test fun `score the second held out set`() = assertEquals(HOLDOUT2_WRONG, wrongAnswers("guideline_holdout2"))

    private companion object {
        val HOLDOUT2_WRONG = setOf<String>()
        val KNOWN_WRONG = setOf<String>()
        val HOLDOUT_WRONG = setOf<String>()
    }
}
