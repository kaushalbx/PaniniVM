package dev.panini.analysis

import com.google.gson.JsonParser
import dev.panini.ashtadhyayi.Ashtadhyayi
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Protect the audit's inventory and evidence claims; this does not perform semantic review. */
class RuleAuditCoverageTest {
    @Test fun `audit preserves every canonical rule and distinguishes unreviewed from implemented`() {
        val ledger = File("analysis/KARAKA_RULE_COVERAGE.json").reader(Charsets.UTF_8).use {
            JsonParser.parseReader(it).asJsonObject
        }
        assertEquals(1, ledger.get("schemaVersion").asInt)
        val expected = ((23..55).map { "1.4.$it" } + (1..73).map { "2.3.$it" }).toSet()
        val records = ledger.getAsJsonArray("rules").map { it.asJsonObject }
        assertEquals(expected, records.map { it.get("number").asString }.toSet())
        assertEquals(expected.size, records.size)
        val canonical = Ashtadhyayi.registry.sutras.groupBy { it.number }
        fun normalized(text: String) = text.filterNot(Char::isWhitespace)
        for (record in records) {
            val number = record.get("number").asString
            val rule = canonical.getValue(number).single()
            assertEquals(normalized(rule.text), normalized(record.get("text").asString), number)
            assertTrue(File(record.get("implementationPath").asString).isFile, number)
            when (record.get("assessmentState").asString) {
                "pending_review" -> listOf("correctness", "integration", "inference").forEach {
                    assertTrue(record.get(it).isJsonNull, "$number: unreviewed axis cannot claim completion")
                }
                "reviewed" -> {
                    for (axis in listOf("correctness", "integration")) {
                        val assessment = record.getAsJsonObject(axis)
                        val status = assessment.get("status").asString
                        assertTrue(status in setOf("implemented", "partial", "missing", "out_of_scope"), number)
                        assertTrue(assessment.get("scope").asString.isNotBlank(), number)
                        val criteria = assessment.getAsJsonArray("criteria").map { it.asJsonObject }
                        assertTrue(criteria.isNotEmpty(), number)
                        assertEquals(criteria.size, criteria.map { it.get("id").asString }.toSet().size, number)
                        criteria.forEach { criterion ->
                            assertTrue(criterion.get("requirement").asString.isNotBlank(), number)
                            val disposition = criterion.get("disposition").asString
                            assertTrue(disposition in setOf("verified", "open"), number)
                            val evidence = criterion.getAsJsonArray("evidence").map { it.asString }
                            assertTrue(evidence.isNotEmpty(), "$number: missing evidence")
                            evidence.forEach { assertTrue(File(it).isFile, "$number: missing evidence $it") }
                        }
                        if (status == "implemented") assertTrue(criteria.all {
                            it.get("disposition").asString == "verified"
                        }, "$number: implemented axis has an open criterion")
                        if (status == "partial") assertTrue(criteria.any {
                            it.get("disposition").asString == "open"
                        }, "$number: partial axis needs a concrete remaining obligation")
                    }
                    val inference = record.getAsJsonObject("inference")
                    assertTrue(inference.has("status"), number)
                    if (inference.get("status").asString == "partial") {
                        assertTrue(inference.getAsJsonArray("unsupported").size() > 0, number)
                    }
                }
                else -> error("Unknown assessment state for $number")
            }
        }
    }
}
