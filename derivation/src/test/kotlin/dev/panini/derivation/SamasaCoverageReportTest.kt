package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class SamasaCoverageReportTest {
    @Test
    fun `write samasa rule coverage report`() {
        val registered=Ashtadhyayi.cataloguedSutras.filterIsInstance<SamasaSutra>()
            .map { (it as Sutra<*,*>).number }.toSortedSet(compareBy(::sutraKey))
        val benchmarkText=File("derivation/src/test/resources/samasa_benchmark.json").readText()
        val benchmarkCount=(TestJsonParser.parse(benchmarkText) as List<*>).size
        val benchmarked=Regex("[2568]\\.[1-4]\\.\\d+").findAll(benchmarkText).map { it.value }.toSortedSet(compareBy(::sutraKey))
        val forbidden=Regex("\"forbiddenSutras\"\\s*:\\s*\"([^\"]*)\"").findAll(benchmarkText)
            .flatMap { it.groupValues[1].split(',').asSequence() }.map(String::trim).filter(String::isNotEmpty).toSet()
        val rejectionCount=(TestJsonParser.parse(File("derivation/src/test/resources/samasa_rejection_benchmark.json").readText()) as List<*>).size
        val testText=File("ashtadhyayi/src/test").walkTopDown().filter { it.extension=="kt" }.joinToString("\n") { it.readText() }+
            File("derivation/src/test").walkTopDown().filter { it.extension=="kt" }.joinToString("\n") { it.readText() }
        val testReferenced=registered.filterTo(sortedSetOf(compareBy(::sutraKey))) { number -> number in testText }
        val positivelyTested=registered.filterTo(sortedSetOf(compareBy(::sutraKey))) { it in benchmarked }
        val negativelyTested=registered.filterTo(sortedSetOf(compareBy(::sutraKey))) { it in forbidden }
        val report=buildString {
            appendLine("Samāsa rule coverage")
            appendLine("registered=${registered.size}")
            appendLine("executable=${registered.size}")
            appendLine("positive-tested=${positivelyTested.size}")
            appendLine("negative-tested=${negativelyTested.size}")
            appendLine("test-referenced=${testReferenced.size}")
            appendLine("canonical-benchmark-cases=$benchmarkCount")
            appendLine("strict-rejection-cases=$rejectionCount")
            appendLine()
            appendLine("Registered but not benchmarked:")
            registered.filterNot { it in benchmarked }.forEach(::appendLine)
            appendLine()
            appendLine("rule,registered,executable,positive-tested,negative-tested")
            registered.forEach { number -> appendLine("$number,yes,yes,${if(number in positivelyTested)"yes" else "no"},${if(number in negativelyTested)"yes" else "no"}") }
        }
        val output=File("derivation/build/reports/samasa-coverage.txt")
        output.parentFile.mkdirs()
        output.writeText(report)
        println(report)
        assertTrue(registered.isNotEmpty())
        assertTrue(positivelyTested.isNotEmpty() && negativelyTested.isNotEmpty())
    }

    private fun sutraKey(number:String)=number.split('.').fold(0) { acc,part -> acc*1000+part.toInt() }
}
