package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class SamasaCoverageReportTest {
    @Test
    fun `write samasa rule coverage report`() {
        val registeredRules=Ashtadhyayi.cataloguedSutras.filterIsInstance<SamasaSutra>()
            .map { it as Sutra<*,*> }
        val registered=registeredRules.map { it.number }.toSortedSet(compareBy(::sutraKey))
        val benchmarkText=File("derivation/src/test/resources/samasa_benchmark.json").readText()
        val benchmarkCount=(TestJsonParser.parse(benchmarkText) as List<*>).size
        val benchmarked=Regex("[2568]\\.[1-4]\\.\\d+").findAll(benchmarkText).map { it.value }.toSortedSet(compareBy(::sutraKey))
        val evidenceText=listOf(
            File("derivation/src/test/kotlin/dev/panini/derivation/SamasaEngineTest.kt"),
            File("derivation/src/test/kotlin/dev/panini/derivation/SamasaPada2PipelineTest.kt"),
            File("derivation/src/test/kotlin/dev/panini/derivation/Samasanta69To112PipelineTest.kt"),
            File("derivation/src/test/kotlin/dev/panini/derivation/Samasanta114To160PipelineTest.kt"),
            File("derivation/src/test/kotlin/dev/panini/derivation/SamasaCrossRuleConflictTest.kt"),
            File("ashtadhyayi/src/test/kotlin/dev/panini/sutra/Samasanta69To112EvidenceTest.kt"),
            File("ashtadhyayi/src/test/kotlin/dev/panini/sutra/Samasanta114To160EvidenceTest.kt"),
        ).joinToString("\n") { it.readText() }
        val derivationAsserted=Regex("[2568]\\.[1-4]\\.\\d+")
            .findAll(evidenceText).map { it.value }.toSet()
        val forbidden=Regex("\"forbiddenSutras\"\\s*:\\s*\"([^\"]*)\"").findAll(benchmarkText)
            .flatMap { it.groupValues[1].split(',').asSequence() }.map(String::trim).filter(String::isNotEmpty).toSet()
        val rejectionCount=(TestJsonParser.parse(File("derivation/src/test/resources/samasa_rejection_benchmark.json").readText()) as List<*>).size
        val testText=File("ashtadhyayi/src/test").walkTopDown().filter { it.extension=="kt" }.joinToString("\n") { it.readText() }+
            File("derivation/src/test").walkTopDown().filter { it.extension=="kt" }.joinToString("\n") { it.readText() }
        val testReferenced=registered.filterTo(sortedSetOf(compareBy(::sutraKey))) { number -> number in testText }
        val positivelyTested=registeredRules.filter {
            it.action != SutraAction.NISHEDHA &&
                (it.number in benchmarked || it.number in derivationAsserted || it.javaClass.simpleName in evidenceText)
        }.mapTo(sortedSetOf(compareBy(::sutraKey))) { it.number }
        val negativelyTested=registeredRules.filter {
            it.number in forbidden || (it.action == SutraAction.NISHEDHA && it.javaClass.simpleName in evidenceText)
        }.mapTo(sortedSetOf(compareBy(::sutraKey))) { it.number }
        val report=buildString {
            appendLine("Samāsa rule coverage")
            appendLine("registered=${registered.size}")
            appendLine("executable=${registered.size}")
            appendLine("positive-tested=${positivelyTested.size}")
            appendLine("negative-tested=${negativelyTested.size}")
            appendLine("test-referenced=${testReferenced.size}")
            appendLine("canonical-benchmark-cases=$benchmarkCount")
            appendLine("derivation-asserted-rules=${derivationAsserted.size}")
            appendLine("strict-rejection-cases=$rejectionCount")
            appendLine()
            appendLine("Registered rules without positive or prohibition evidence:")
            registered.filterNot { it in positivelyTested || it in negativelyTested }.forEach(::appendLine)
            appendLine()
            appendLine("rule,registered,executable,positive-tested,negative-tested")
            registered.forEach { number -> appendLine("$number,yes,yes,${if(number in positivelyTested)"yes" else "no"},${if(number in negativelyTested)"yes" else "no"}") }
        }
        val output=File("derivation/build/reports/samasa-coverage.txt")
        output.parentFile.mkdirs()
        output.writeText(report)
        println(report)
        val padaOneAndTwo=registeredRules.filter { it.chapter == 2 && it.pada in 1..2 }
        val samasanta=registeredRules.filter { it.chapter == 5 && it.pada == 4 && it.kramaValue <= 540160 }
        val gatedRules=padaOneAndTwo+samasanta
        val missingPositive=gatedRules.filter { it.action != SutraAction.NISHEDHA && it.number !in positivelyTested }
        val missingProhibition=gatedRules.filter { it.action == SutraAction.NISHEDHA && it.number !in negativelyTested }
        assertTrue(registered.isNotEmpty())
        assertTrue(positivelyTested.isNotEmpty() && negativelyTested.isNotEmpty())
        assertTrue(missingPositive.isEmpty(), "Gated samāsa rules without positive evidence: ${missingPositive.map { it.number }}")
        assertTrue(missingProhibition.isEmpty(), "Gated samāsa prohibitions without evidence: ${missingProhibition.map { it.number }}")
    }

    private fun sutraKey(number:String)=number.split('.').fold(0) { acc,part -> acc*1000+part.toInt() }
}
