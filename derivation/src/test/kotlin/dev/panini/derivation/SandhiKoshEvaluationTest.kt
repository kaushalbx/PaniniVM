package dev.panini.derivation

import java.io.File
import java.text.Normalizer

/** Explicit corpus benchmark runner; deliberately not part of the JUnit test suite. */
object SandhiKoshEvaluationRunner {
    private fun normalized(value: String) = Normalizer.normalize(value.trim(), Normalizer.Form.NFC)
    private fun compact(value: String) = normalized(value).filterNot { it.isWhitespace() }
    private fun csv(value: Any?) = "\"" + value.toString().replace("\"", "\"\"") + "\""

    @JvmStatic
    fun main(args: Array<String>) {
        val basePath = System.getProperty("sandhikosh.evaluation.dir")
            ?: error("External corpus evaluation requires sandhikosh.evaluation.dir")
        val base = File(requireNotNull(basePath))
        val records = TestJsonParser.parse(File(base, "cases.json").readText()) as List<*>
        val lexicalPolicy = System.getProperty("sandhikosh.lexical.analysis", "none")
        require(lexicalPolicy in setOf("none", "declared")) { "Unknown lexical analysis policy: $lexicalPolicy" }
        val lexicon = if (lexicalPolicy == "declared") DerivedVisargaLexicon.build(
            dev.panini.linganushasanam.PaninianPratipadikaLexicon.declaredPratipadikas(),
            avyayaRequests = DeclaredAvyayaDerivations.requests,
            ganaAvyayaUpadeshas = DeclaredSvaradiAvyayas.upadeshas) else null
        check(lexicon == null || (lexicon.failures.isEmpty() && lexicon.avyayaFailures.isEmpty() &&
            lexicon.unresolvedSources.isEmpty())) {
            "Declared lexical analysis has incomplete derivations; not silently continuing"
        }
        val evaluator = SandhiSequenceEvaluator(lexicon = lexicon)
        val alternatives = System.getProperty("sandhikosh.alternatives", "false").toBoolean()
        val endpoint = SpeechBoundary.valueOf(System.getProperty("sandhikosh.final.boundary", "CONTINUATION"))
        val suffix = (if (alternatives) "-alternatives" else "-sequence") +
            (if (lexicon != null) "-declared-lexicon" else "") +
            (if (endpoint == SpeechBoundary.AVASANA) "-avasana" else "")
        var pass = 0; var fail = 0; var errors = 0; var skipped = 0; var exact = 0
        var analyzedRows = 0; var analyzedWordOccurrences = 0
        val failureCounts = mutableMapOf<String, Int>()
        val failureExamples = mutableMapOf<String, MutableList<String>>()
        val errorExamples = mutableMapOf<String, MutableList<String>>()
        val provenanceFailures = mutableListOf<String>()
        File(base, "results$suffix.csv").bufferedWriter(Charsets.UTF_8).use { out ->
            out.appendLine(listOf("sheet","row","id","type","split","expected","actual","status","exact_match","all_steps_final","sutras","error","nominal_proofs").joinToString(","))
            records.forEachIndexed { index, raw ->
                val record = raw as Map<*, *>
                val expected = record["expected"] as String
                val split = record["split"] as String
                val parts = split.split('+').map(String::trim)
                var actual = ""; var status = ""; var error = ""; var terminal = true
                val sutras = mutableListOf<String>()
                var nominalProofs = ""
                if (expected.isBlank() || parts.size < 2 || parts.any(String::isBlank)) {
                    status = "SKIPPED"; skipped++
                } else {
                    try {
                        val analyzed = parts.count { lexicon?.padaCandidates(it)?.isNotEmpty() == true }
                        if (analyzed > 0) analyzedRows++
                        analyzedWordOccurrences += analyzed
                        val outcomes = evaluator.evaluate(parts, alternatives, finalBoundary = endpoint)
                        val selected = outcomes.firstOrNull { normalized(it.surface) == normalized(expected) }
                            ?: outcomes.firstOrNull { compact(it.surface) == compact(expected) } ?: outcomes.first()
                        actual = selected.surface
                        terminal = outcomes.all { outcome -> outcome.steps.all { it.final.stage == DerivationStage.FINAL } }
                        sutras += selected.steps.flatMap { result -> result.applications.map { it.sutra } }
                        nominalProofs = selected.inputs.filter { it.sourceDerivation != null }.joinToString("; ") { pada ->
                            "${pada.term.surface}:${pada.term.upadesha}:${pada.rupa.vibhakti}:${pada.rupa.vacana}:" +
                                requireNotNull(pada.sourceDerivation).applications.joinToString(" ") { it.sutra }
                        }
                        if (outcomes.any { normalized(it.surface) == normalized(expected) }) exact++
                        if (outcomes.any { compact(it.surface) == compact(expected) }) { status = "PASS"; pass++ }
                        else {
                            status = "FAIL"; fail++
                            if (analyzed > 0) provenanceFailures +=
                                "row=${record["row"]} analyzed=$analyzed/${parts.size} $split | expected=$expected | actual=$actual | proofs=$nominalProofs | sutras=${sutras.joinToString(" ")}"
                            val boundary = if (parts.size == 2) {
                                val initial = selected.steps.first().initial
                                "${initial.terms.first().varnas.lastOrNull()} + ${initial.terms.last().varnas.firstOrNull()}"
                            } else "MULTIPART(${parts.size})"
                            val key = "${record["type"]}: $boundary"
                            failureCounts[key] = failureCounts.getOrDefault(key, 0) + 1
                            val examples = failureExamples.getOrPut(key) { mutableListOf() }
                            if (examples.size < 4) examples += "row=${record["row"]} $split | expected=$expected | actual=$actual | sutras=${sutras.joinToString(" ")}"
                        }
                    } catch (e: Exception) {
                        status = "ERROR"; errors++; error = "${e.javaClass.simpleName}: ${e.message}"
                        val category = if (e is SandhiSequenceEvaluator.InvalidPadaInput)
                            "INVALID_PADA_INPUT" else "DERIVATION_RUNTIME: ${e.javaClass.simpleName}"
                        errorExamples.getOrPut(category) { mutableListOf() }
                            .add("row=${record["row"]} $split | error=${error.take(600)}")
                    }
                }
                out.appendLine(listOf(record["sheet"],record["row"],record["id"],record["type"],split,expected,actual,status,
                    normalized(actual) == normalized(expected),terminal,sutras.joinToString(" "),error,nominalProofs).joinToString(",", transform=::csv))
                out.flush()
                if ((index+1)%100 == 0) println("SANDHIKOSH ${index+1}/${records.size}: pass=$pass fail=$fail errors=$errors skipped=$skipped exact=$exact")
            }
        }
        val summary = "total=${records.size} pass=$pass fail=$fail errors=$errors skipped=$skipped exact=$exact" +
            (if (lexicon != null) " lexical_policy=$lexicalPolicy analyzed_rows=$analyzedRows analyzed_word_occurrences=$analyzedWordOccurrences" else "")
        File(base,"summary$suffix.txt").writeText(summary)
        File(base,"provenance-failures$suffix.txt").writeText(
            "Failed rows with at least one forward-derived lexical proof. This is an evidence inventory, not an assertion that every input or the expected form is correct.\n" +
                provenanceFailures.joinToString("\n"))
        File(base,"failure-groups$suffix.txt").writeText(failureCounts.entries.sortedByDescending { it.value }
            .joinToString("\n\n") { (key, count) -> "$key: $count\n${failureExamples.getValue(key).joinToString("\n")}" })
        File(base,"error-groups$suffix.txt").writeText(errorExamples.entries.joinToString("\n\n") {
            (type, examples) -> "$type: ${examples.size}\n${examples.joinToString("\n")}"
        })
        println("SANDHIKOSH FINAL $summary")
    }
}
