package dev.panini.execution

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import com.google.gson.Strictness
import dev.panini.analysis.*
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParser
import java.io.File
import kotlin.test.*

/** Reviewed expectations are checked in; observed analysis is report-only. */
class KarakaCorpusBenchmarkTest {
    private val gson = GsonBuilder().setStrictness(Strictness.STRICT).serializeNulls().setPrettyPrinting().create()
    private fun normalized(text: String) = text.replace(Regex("\\s+"), "")

    @Test fun `inventory corpus clauses and verify reviewed grammatical expectations`() {
        val manifest = requireNotNull(javaClass.getResourceAsStream("/karaka_corpus_expectations.json"))
            .bufferedReader(Charsets.UTF_8).use { gson.fromJson(it, JsonObject::class.java) }
        assertEquals(1, manifest.get("schemaVersion").asInt)
        assertEquals("canonical_sutras_only", manifest.get("scope").asString)
        assertEquals("none", manifest.get("suppliedSemanticPremises").asString)
        val reviews = manifest.getAsJsonArray("reviews").map { it.asJsonObject }
        assertTrue(reviews.isNotEmpty(), "Corpus review coverage must not silently disappear")
        assertEquals(reviews.size, reviews.map { it.get("id").asString }.toSet().size)
        reviews.groupBy { review ->
            Triple(review.get("path").asString, normalized(review.get("clause").asString),
                review.get("occurrence").asInt)
        }.values.forEach { sameOccurrence ->
            assertEquals(1, sameOccurrence.size,
                "Multiple answer keys target one source occurrence: ${sameOccurrence.map { it.get("id").asString }}")
        }
        reviews.forEach { review ->
            actionReviews(review).forEach { action -> action.getAsJsonArray("participants").forEach { participant ->
                participant.asJsonObject.getAsJsonArray("chain").forEach { sutra ->
                    assertTrue(sutra.asString.matches(Regex("[1-8]\\.[1-4]\\.\\d+")))
                    assertTrue(manifest.getAsJsonObject("sources").has(sutra.asString), "Missing source for $sutra")
                }
            } }
        }
        val matched = mutableSetOf<String>()
        val failures = mutableListOf<String>()
        val rows = mutableListOf<JsonObject>()
        val files = mutableListOf<JsonObject>()
        val parser = PaniniParser()
        val corpus = listOf(File("examples"), File("projects")).flatMap { root ->
            require(root.isDirectory)
            root.walkTopDown().filter { it.isFile && it.extension == "pvm" }.toList()
        }.sortedBy { it.invariantSeparatorsPath }
        assertTrue(corpus.isNotEmpty())
        // Imported procedures are declared in other project files, so names are gathered across the corpus.
        val corpusProcedureStems = corpus.flatMap { file -> runCatching {
            PrakriyaRegistry().apply { registerDeclarations(PvmScript.fromDocument(parser.parseDocument(file.readText(Charsets.UTF_8))), file.path) }
                .all().map { it.nameStem } }.getOrDefault(emptyList()) }.toSet()
        variantProcedureStems = corpusProcedureStems
        corpus.forEach { file ->
            val path = file.invariantSeparatorsPath
            val source = file.readText(Charsets.UTF_8)
            val fileRow = JsonObject().apply { addProperty("path", path) }
            files += fileRow
            val document = try { parser.parseDocument(source) } catch (failure: Exception) {
                fileRow.addProperty("status", "PARSE_ERROR")
                fileRow.addProperty("reason", failure.message)
                failures += "$path: ${failure.message}"
                return@forEach
            }
            fileRow.addProperty("status", "PARSED")
            fileRow.addProperty("declarationItems", document.items.count { it !is Ukti })
            val occurrences = mutableMapOf<String, Int>()
            document.items.forEachIndexed { itemIndex, node ->
                val invocations = when (node) {
                    is Ukti -> node.body.grammaticalInvocations()
                    is ProgramNode -> node.grammaticalInvocations()
                    else -> emptyList()
                }
                invocations.forEachIndexed { clauseIndex, invocation ->
                    val text = normalized(invocation.sourceText)
                    val occurrence = occurrences.getOrDefault(text, 0)
                    occurrences[text] = occurrence + 1
                    val review = reviews.singleOrNull {
                        it.get("path").asString == path && normalized(it.get("clause").asString) == text &&
                            it.get("occurrence").asInt == occurrence
                    }
                    val row = JsonObject().apply {
                        addProperty("id", "$path#item-$itemIndex/clause-$clauseIndex")
                        addProperty("path", path); addProperty("clause", invocation.sourceText)
                        addProperty("occurrence", occurrence)
                        addProperty("reviewStatus", if (review == null) "UNREVIEWED" else "PENDING")
                        invocation.relationSpan()?.let { span ->
                            require(span.endExclusive <= source.length) { "Clause span escapes $path" }
                            addProperty("start", span.start); addProperty("endExclusive", span.endExclusive)
                            addProperty("line", source.take(span.start).count { it == '\n' } + 1)
                            addProperty("sourceExcerpt", source.substring(span.start, span.endExclusive))
                        }
                    }
                    rows += row
                    try {
                        val clauseUkti = Ukti(invocation.sourceText, body = invocation)
                        val analysis = requireNotNull(VyakaranamExecutionAdapter.analyzeProcedureCall(clauseUkti, corpusProcedureStems)
                            ?: VyakaranamExecutionAdapter.analyzeGrammar(clauseUkti))
                        val frames = analysis.frames
                        require(frames.isNotEmpty()) { "No supported grammar frame" }
                        val blocking = analysis.blockingDiagnostics.map { it.code.name }.sorted()
                        row.addProperty("analysisStatus", if (blocking.isEmpty()) "ANALYZED" else "ANALYZED_WITH_BLOCKING_DIAGNOSTICS")
                        row.add("blockingDiagnostics", gson.toJsonTree(blocking))
                        val priorLinks = gson.toJsonTree(analysis.links.filterIsInstance<KriyaLink.Purvakalika>().map { link ->
                            linkedMapOf("source" to frames.indexOfFirst { it.id == link.source },
                                "target" to frames.indexOfFirst { it.id == link.target })
                        })
                        row.add("priorActionLinks", priorLinks)
                        row.add("frames", gson.toJsonTree(frames.map { frame -> linkedMapOf(
                            "headKind" to headKind(frame),
                            "headSource" to frame.kriya?.analysis?.pada?.sourceText?.let(::normalized),
                            "participants" to frame.relations.map(::participant),
                            "qualifications" to qualifications(frame),
                            "jointCaseAlternatives" to jointCases(frame)) }))
                        if (frames.size == 1) {
                            row.add("participants", gson.toJsonTree(frames.single().relations.map(::participant)))
                            row.add("qualifications", qualifications(frames.single()))
                            row.add("jointCaseAlternatives", jointCases(frames.single()))
                        }
                        if (review != null) {
                            val id = review.get("id").asString
                            assertTrue(matched.add(id), "Review matched multiple clauses: $id")
                            assertTrue(review.get("rationale").asString.isNotBlank())
                            assertEquals(review.getAsJsonArray("blockingDiagnostics") ?: gson.toJsonTree(emptyList<String>()),
                                gson.toJsonTree(blocking), "$id: blocking diagnostics")
                            review.getAsJsonArray("priorActionLinks")?.let { assertEquals(it, priorLinks, "$id: prior-action links") }
                            val expectedActions = actionReviews(review)
                            assertEquals(expectedActions.size, frames.size, "$id: action ownership frame count")
                            expectedActions.zip(frames).forEach { (expected, frame) ->
                                assertEquals(expected.getAsJsonArray("participants"),
                                    gson.toJsonTree(frame.relations.map(::participant)), "$id: action ${frame.id}")
                                expected.get("headKind")?.let { assertEquals(it.asString,
                                    headKind(frame), "$id: head kind") }
                                expected.get("headSource")?.let { assertEquals(normalized(it.asString),
                                    frame.kriya?.analysis?.pada?.sourceText?.let(::normalized), "$id: head source") }
                                checkQualifications(expected, frame)
                                assertEquals(expected.getAsJsonArray("jointCaseAlternatives") ?: JsonArray(),
                                    jointCases(frame), "$id: joint case alternatives")
                            }
                            if (frames.size == 1) row.add("wordOrderVariants", checkWordOrders(review, invocation, frames.single()))
                            else require((review.getAsJsonArray("wordOrders")?.size() ?: 0) == 0) {
                                "Multi-action permutations must specify action-local occurrence alignment."
                            }
                            row.addProperty("reviewStatus", "PASS")
                        }
                    } catch (failure: Throwable) {
                        if (failure !is Exception && failure !is AssertionError) throw failure
                        if (!row.has("analysisStatus")) row.addProperty("analysisStatus", "UNSUPPORTED_OR_FAILED")
                        row.addProperty("reason", failure.message)
                        if (review != null) {
                            matched += review.get("id").asString
                            row.addProperty("reviewStatus", "FAIL")
                            failures += "${review.get("id").asString}: ${failure.message}"
                        }
                    }
                }
            }
        }
        reviews.filter { it.get("id").asString !in matched }.forEach {
            failures += "Stale or unmatched review: ${it.get("id").asString}"
        }
        val report = JsonObject().apply {
            addProperty("scope", "PVM grammar and declared program constructions; no execution or manually supplied semantic premises")
            addProperty("declarations", "Declaration items are counted separately; declaration headers are not clause-reviewed.")
            addProperty("files", corpus.size); addProperty("clauses", rows.size)
            addProperty("grammaticalFrames", rows.sumOf { it.getAsJsonArray("frames")?.size() ?: 0 })
            addProperty("reviewedFrames", rows.filter { it.get("reviewStatus").asString == "PASS" }
                .sumOf { it.getAsJsonArray("frames")?.size() ?: 0 })
            addProperty("reviewedPassed", rows.count { it.get("reviewStatus").asString == "PASS" })
            addProperty("wordOrderVariantsPassed", rows.sumOf { it.getAsJsonArray("wordOrderVariants")?.size() ?: 0 })
            addProperty("unreviewed", rows.count { it.get("reviewStatus").asString == "UNREVIEWED" })
            addProperty("analyzed", rows.count { it.get("analysisStatus").asString.startsWith("ANALYZED") })
            addProperty("blockedAnalyses", rows.count { it.get("analysisStatus").asString == "ANALYZED_WITH_BLOCKING_DIAGNOSTICS" })
            addProperty("unsupportedOrFailed", rows.count { it.get("analysisStatus").asString == "UNSUPPORTED_OR_FAILED" })
            addProperty("missingSpans", rows.count { !it.has("start") })
            addProperty("parseErrors", files.count { it.get("status").asString == "PARSE_ERROR" })
            addProperty("failures", failures.size)
            add("fileInventory", gson.toJsonTree(files)); add("clauseInventory", gson.toJsonTree(rows))
            add("errors", gson.toJsonTree(failures))
        }
        File("execution/build/reports/karaka-corpus.json").apply { parentFile.mkdirs(); writeText(gson.toJson(report)) }
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
    }

    private fun actionReviews(review: JsonObject): List<JsonObject> =
        review.getAsJsonArray("frames")?.map { it.asJsonObject } ?: listOf(review)

    private fun headKind(frame: KriyaFrame) = when {
        frame.kriya == null -> "NOMINAL"
        !frame.kriya!!.isFinite -> "NONFINITE"
        else -> "FINITE"
    }

    /** Reuse occurrences, but compare every variant to the reviewed answer key. */
    private var variantProcedureStems: Set<String> = emptySet()

    private fun checkWordOrders(review: JsonObject, original: Invocation, frame: KriyaFrame): JsonArray {
        val results = JsonArray()
        review.getAsJsonArray("wordOrders")?.forEach { permutation ->
            val order = permutation.asJsonArray.map { it.asInt }
            val padas = original.vakya.padas
            assertEquals(padas.indices.toList(), order.sorted(), "Word order must permute every pada exactly once")
            assertNotEquals(padas.indices.toList(), order, "Variant must change word order")
            val reordered = order.map(padas::get)
            val vakya = when (val clause = original.vakya) {
                is AkhyataVakya -> clause.copy(padas = reordered)
                is NamaVakya -> clause.copy(padas = reordered)
            }
            val variantUkti = Ukti(original.sourceText, body = original.copy(vakya = vakya))
            val analysis = requireNotNull(VyakaranamExecutionAdapter.analyzeProcedureCall(variantUkti, variantProcedureStems)
                ?: VyakaranamExecutionAdapter.analyzeGrammar(variantUkti))
            val actual = analysis.frames.single().relations.sortedBy { relation ->
                frame.relations.indexOfFirst { it.participant.pada === relation.participant.pada }
                    .also { require(it >= 0) { "Permutation replaced a nominal occurrence" } }
            }
            checkQualifications(review, analysis.frames.single())
            assertEquals(frame.relations.size, actual.size)
            assertEquals(review.getAsJsonArray("participants"), gson.toJsonTree(actual.map(::participant)),
                "${review.get("id").asString}: word order $order")
            assertEquals(review.getAsJsonArray("jointCaseAlternatives") ?: JsonArray(),
                jointCases(analysis.frames.single(), frame), "AST joint case alternatives")
            assertEquals(review.getAsJsonArray("blockingDiagnostics") ?: gson.toJsonTree(emptyList<String>()),
                gson.toJsonTree(analysis.blockingDiagnostics.map { it.code.name }.sorted()))
            // Reparse the written morphological fragments as a second path.
            // Unique source fragments identify participants across fresh ASTs;
            // repeated identical fragments need an explicit alignment scheme.
            val sourceOrder = frame.relations.map { normalized(it.participant.pada.sourceText) }
            require(sourceOrder.distinct().size == sourceOrder.size) { "Text variants need unique participant fragments" }
            val text = reordered.joinToString(" ") { it.sourceText } + " ।"
            val reparsedUkti = PaniniParser().parse(text)
            val reparsed = requireNotNull(VyakaranamExecutionAdapter.analyzeProcedureCall(reparsedUkti, variantProcedureStems)
                ?: VyakaranamExecutionAdapter.analyzeGrammar(reparsedUkti))
            val reparsedRelations = reparsed.frames.single().relations.sortedBy { relation ->
                sourceOrder.indexOf(normalized(relation.participant.pada.sourceText))
                    .also { require(it >= 0) { "Reparsed variant changed a participant fragment" } }
            }
            checkQualifications(review, reparsed.frames.single())
            assertEquals(review.getAsJsonArray("participants"), gson.toJsonTree(reparsedRelations.map(::participant)),
                "${review.get("id").asString}: reparsed word order $order")
            assertEquals(review.getAsJsonArray("jointCaseAlternatives") ?: JsonArray(),
                jointCases(reparsed.frames.single(), frame, true), "Reparsed joint case alternatives")
            assertEquals(review.getAsJsonArray("blockingDiagnostics") ?: gson.toJsonTree(emptyList<String>()),
                gson.toJsonTree(reparsed.blockingDiagnostics.map { it.code.name }.sorted()))
            results.add(JsonObject().apply {
                add("order", permutation); addProperty("status", "PASS")
                addProperty("input", "AST permutation retaining original source spans")
                addProperty("reparsedSource", text); addProperty("reparsedStatus", "PASS")
            })
        }
        return results
    }

    private fun qualifications(frame: KriyaFrame) = gson.toJsonTree(frame.qualifications.map { qualification ->
        linkedMapOf("kind" to qualification.kind.name,
            "function" to qualification.semantics?.function?.name,
            "scopes" to qualification.semantics?.scopes?.map { it.name }?.sorted(),
            "repetitionCount" to qualification.semantics?.repetitionCount)
    }).asJsonArray

    private fun checkQualifications(review: JsonObject, frame: KriyaFrame) {
        review.getAsJsonArray("qualifications")?.let { expected ->
            assertEquals(expected, qualifications(frame), "${review.get("id").asString}: action qualifications")
        }
    }

    private fun jointCases(frame: KriyaFrame, reference: KriyaFrame = frame, byText: Boolean = false) =
        gson.toJsonTree(frame.nominalCaseLinks?.jointAlternatives.orEmpty().map { alternative ->
            val nominals = requireNotNull(frame.nominalRelations).nominalPadas
            val original = requireNotNull(reference.nominalRelations).nominalPadas
            val members = alternative.members.map { index ->
                val current = nominals[index].pada
                original.indices.filter {
                    if (byText) normalized(original[it].pada.sourceText) == normalized(current.sourceText)
                    else original[it].pada === current
                }.single()
            }.sorted()
            linkedMapOf("members" to members, "case" to alternative.vibhakti.name,
                "chain" to alternative.evidence.map { it.sutra })
        }).asJsonArray

    private fun participant(relation: KarakaRelation): Map<String, Any?> = linkedMapOf(
        "source" to normalized(relation.participant.pada.sourceText),
        "resolution" to when (relation.resolution) {
            is FrameKarakaResolution.Resolved -> "RESOLVED"
            is FrameKarakaResolution.Ambiguous -> "AMBIGUOUS"
            is FrameKarakaResolution.Unassigned -> "UNASSIGNED"
        },
        "karaka" to (relation.resolution as? FrameKarakaResolution.Resolved)?.karaka?.name,
        "case" to relation.resolvedVibhakti?.name,
        "chain" to relation.evidence.map { it.sutra },
        "candidates" to when (val result = relation.resolution) {
            is FrameKarakaResolution.Resolved -> emptyList()
            is FrameKarakaResolution.Ambiguous -> result.candidates.map { it.name }.sorted()
            is FrameKarakaResolution.Unassigned -> result.candidates.map { it.name }.sorted()
        },
        "additionalDesignations" to relation.additionalDesignations.map { it.name }.sorted(),
        "alternatives" to relation.alternatives.map { alternative -> linkedMapOf(
            "case" to alternative.vibhakti?.name, "kind" to alternative.kind.name,
            "chain" to alternative.evidence.map { it.sutra }, "observed" to alternative.matchesObservedMorphology,
        ) },
    )
}
