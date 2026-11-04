package dev.panini.analysis

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.Strictness
import dev.panini.core.*
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Karmatva
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.lexicon.*
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.*

/** Executes every JSON case; no embedded substitute examples. */
class KarakaJsonBenchmarkTest {
    private val gson = GsonBuilder().setStrictness(Strictness.STRICT).setPrettyPrinting().create()
    private fun JsonObject.text(name: String): String? = get(name)?.takeUnless { it.isJsonNull }?.asString
    private fun JsonObject.strings(name: String): List<String> = getAsJsonArray(name)?.map { it.asString }.orEmpty()

    @Test fun `execute all structured karaka benchmark records and write grouped report`() {
        val resource = requireNotNull(javaClass.classLoader.getResourceAsStream("karaka_benchmark.json"))
        val dataset = resource.bufferedReader(Charsets.UTF_8).use { gson.fromJson(it, JsonObject::class.java) }
        assertEquals(1, dataset.get("schemaVersion").asInt)
        assertEquals("canonical_sutras_only", dataset.text("scope"))
        assertEquals("nominal_occurrence", dataset.text("premiseScope"))
        val cases = dataset.getAsJsonArray("cases").map { it.asJsonObject }
        assertTrue(cases.isNotEmpty())
        assertEquals(cases.size, cases.map { it.text("id") }.toSet().size, "Benchmark IDs must be unique")
        val failures = mutableListOf<String>()
        var annotated = 0
        var annotatedPassed = 0
        val results = cases.map { record ->
            val id = requireNotNull(record.text("id"))
            val input = record.getAsJsonObject("input")
            val annotatedInput = input.getAsJsonArray("padas").any {
                it.asJsonObject.strings("premises").isNotEmpty() || it.asJsonObject.strings("lexicalRelations").isNotEmpty() || it.asJsonObject.has("intendedObject") ||
                    it.asJsonObject.has("krtGovernor") || it.asJsonObject.has("avyayaGovernor") || it.asJsonObject.has("purposeGovernor")
            }
            if (annotatedInput) annotated++
            val result = JsonObject().apply { addProperty("id", id); addProperty("suppliedSemanticPremises", annotatedInput) }
            try {
                if (record.text("kind") == "canonical_example_with_supplied_premises") {
                    assertTrue(dataset.getAsJsonObject("sources").has(requireNotNull(record.text("source"))))
                } else assertEquals("generated_regression", record.text("kind"))
                verify(record)
                if (annotatedInput) annotatedPassed++
                result.addProperty("passed", true)
            } catch (failure: AssertionError) {
                failures += "$id: ${failure.message}"
                result.addProperty("passed", false)
                result.addProperty("failure", failure.message)
            }
            result
        }
        val report = JsonObject().apply {
            addProperty("inputLevel", dataset.text("inputLevel"))
            addProperty("doesNotMeasure", "raw text parsing or independent semantic-premise discovery")
            addProperty("suppliedPremisesScope", "nominal_occurrence")
            addProperty("total", cases.size); addProperty("passed", cases.size - failures.size)
            addProperty("failed", failures.size)
            addProperty("withSuppliedPremises", annotated); addProperty("withSuppliedPremisesPassed", annotatedPassed)
            addProperty("withoutSuppliedPremises", cases.size - annotated)
            addProperty("withoutSuppliedPremisesPassed", cases.size - failures.size - annotatedPassed)
            add("cases", gson.toJsonTree(results))
        }
        val output = Path.of("analysis/build/reports/karaka-benchmark.json")
        Files.createDirectories(output.parent)
        Files.writeString(output, gson.toJson(report))
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
    }

    private fun verify(record: JsonObject) {
        val input = record.getAsJsonObject("input")
        val expected = record.getAsJsonObject("expected")
        require(listOf("chain", "karaka", "case", "alternatives", "candidateCount", "establishedCount").all(expected::has)) {
            "Missing expected-result field in ${record.text("id") }"
        }
        val words = input.getAsJsonArray("padas").map { it.asJsonObject }
        require(words.isNotEmpty())
        require(input.get("dependent").asInt in words.indices)
        val padas = words.map {
            val stem = requireNotNull(it.text("stem"))
            val affix = SupAffix.valueOf(requireNotNull(it.text("sup")))
            val krt = it.getAsJsonObject("krt")
            val pratipadika = if (krt == null) MulaPratipadika(stem, stem) else KridantaPratipadika(stem,
                krt.strings("upasargas"), DhatuPrakriti(requireNotNull(krt.text("root")), requireNotNull(krt.text("root")),
                    krt.strings("sanadi")),
                requireNotNull(krt.text("affix")),
                derivationEvidence = krt.getAsJsonObject("derivation")?.let { evidence ->
                    KrtDerivationEvidence(requireNotNull(evidence.text("governingSutra")),
                        KrtDerivationSense.valueOf(requireNotNull(evidence.text("sense"))))
                })
            if (krt != null) assertEquals(krt.get("recognizedAffix")?.asBoolean ?: true,
                (pratipadika as KridantaPratipadika).krtAffix != null,
                "Kṛt suffix identity in ${record.text("id")}: ${krt.text("affix")}")
            SubantaPada("$stem + ${affix.upadesha}", pratipadika, SupPratyaya(affix.upadesha, affix.upadesha))
        }
        val root = requireNotNull(input.text("root"))
        val voice = requireNotNull(input.text("voice"))
        require(voice in setOf("KARTARI", "KARMANI", "BHAVE", "CAUSATIVE", "PASSIVE_CAUSATIVE"))
        val passive = voice in setOf("KARMANI", "BHAVE", "PASSIVE_CAUSATIVE")
        val causative = voice in setOf("CAUSATIVE", "PASSIVE_CAUSATIVE")
        val verb = TingantaPada("benchmark verb", input.strings("upasargas"), DhatuPrakriti(root, root,
            if (causative) listOf("णिच्") else emptyList()),
            Lakara.valueOf(input.text("lakara") ?: "LAT"),
            TingPratyaya(if (passive) "त" else "तिप्", if (passive) "त" else "तिप्"),
            vikarana = if (passive) Vikarana.YAK else null)
        val entries = words.map {
            val stem = requireNotNull(it.text("stem"))
            val gender = when (stem) { "हित", "आयुष्य", "अन्न", "लेखन" -> Linga.NAPUMSAKA; "तुला", "उपमा", "लेखनी", "भूति", "सम्पत्ति" -> Linga.STRI; else -> Linga.PUMS }
            PratipadikaEntry(stem, setOf(gender),
                semanticRelations = it.strings("lexicalRelations").mapTo(mutableSetOf()) { name -> SemanticRelation.valueOf(name) })
        }.distinctBy { it.text }
        fun gana(root: String) = when (root) {
            "अस्", "आस्", "शी", "इ", "अद्" -> DhatuGana.ADADI; "कृ" -> DhatuGana.TANADI; "दा" -> DhatuGana.JUHOTYADI
            "मन्", "पद्", "दिव्" -> DhatuGana.DIVADI; "लिख्" -> DhatuGana.TUDADI; "भुज्" -> DhatuGana.RUDHADI
            "क्री" -> DhatuGana.KRYADI; else -> DhatuGana.BHVADI
        }
        val dhatu = Dhatu(id = "benchmark.$root", krama = 1, upadesha = root, sourceSurface = root,
            artha = "benchmark", arthaHindi = "benchmark", arthaEnglish = "benchmark",
            gana = input.text("gana")?.let(DhatuGana::valueOf) ?: gana(root),
            pada = PadaType.PARASMAIPADA, karmatva = Karmatva.valueOf(requireNotNull(input.text("transitivity"))))
        val krtDhatus = words.mapNotNull { it.getAsJsonObject("krt") }.map {
            val krtRoot = requireNotNull(it.text("root"))
            Dhatu(id = "benchmark.krt.$krtRoot", krama = 1, upadesha = krtRoot, sourceSurface = krtRoot,
                artha = "benchmark krt", arthaHindi = "benchmark", arthaEnglish = "benchmark",
                gana = gana(krtRoot), pada = if (krtRoot == "पद्") PadaType.ATMANEPADA else PadaType.PARASMAIPADA,
                karmatva = Karmatva.valueOf(it.text("transitivity") ?: "SAKARMAKA"))
        }
        val purposeDhatus = words.mapNotNull { it.getAsJsonObject("purposeGovernor") }.map {
            val purposeRoot = requireNotNull(it.text("root"))
            Dhatu(id = "benchmark.purpose.$purposeRoot", krama = 1, upadesha = purposeRoot, sourceSurface = purposeRoot,
                artha = "benchmark purpose action", arthaHindi = "benchmark", arthaEnglish = "benchmark",
                gana = gana(purposeRoot), pada = PadaType.PARASMAIPADA,
                karmatva = Karmatva.valueOf(it.text("transitivity") ?: "SAKARMAKA"))
        }
        val analyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(entries,
            (listOf(dhatu) + krtDhatus + purposeDhatus).distinctBy { it.sourceSurface })))
        val intendedObjects = words.mapIndexedNotNull { index, word ->
            val binding = word.getAsJsonObject("intendedObject") ?: return@mapIndexedNotNull null
            val target = when (requireNotNull(binding.text("kind"))) {
                "written" -> binding.get("index").asInt
                "implied" -> null
                else -> error("Unknown object binding kind")
            }
            index to target
        }.toMap()
        val krtGovernors = words.mapIndexedNotNull { index, word ->
            val binding = word.getAsJsonObject("krtGovernor") ?: return@mapIndexedNotNull null
            val expressed = if (binding.get("expressedRoles")?.isJsonNull != false) null
                else binding.strings("expressedRoles").mapTo(mutableSetOf()) { Karaka.valueOf(it) }
            val eligible = if (binding.get("genitiveEligibleRoles")?.isJsonNull != false) null
                else binding.strings("genitiveEligibleRoles").mapTo(mutableSetOf()) { Karaka.valueOf(it) }
            index to KrtGovernorReading(binding.get("index").asInt, expressed, eligible)
        }.toMap()
        val avyayaGovernors = words.mapIndexedNotNull { index, word ->
            val binding = word.getAsJsonObject("avyayaGovernor") ?: return@mapIndexedNotNull null
            val form = requireNotNull(binding.text("form"))
            val derivation = binding.text("affix")?.let {
                AvyayaTaddhitaDerivation(requireNotNull(binding.text("base")), it)
            }
            index to AvyayaPada(form, form, derivation)
        }.toMap()
        val purposeReadings = mutableMapOf<String, PurposeActionGovernor>()
        val purposeGovernors = words.mapIndexedNotNull { index, word ->
            val binding = word.getAsJsonObject("purposeGovernor") ?: return@mapIndexedNotNull null
            val id = requireNotNull(binding.text("id"))
            val root = requireNotNull(binding.text("root"))
            val dhatu = DhatuPrakriti(root, root, binding.strings("sanadi"))
            val candidate = when (binding.text("kind")) {
                "IMPLIED" -> PurposeActionGovernor.Implied(id, dhatu, binding.strings("upasargas"))
                "WRITTEN" -> {
                    val form = requireNotNull(binding.text("form"))
                    PurposeActionGovernor.Written(id, AvyayaPada(form, form,
                        AvyayaKridantaDerivation(binding.strings("upasargas"), dhatu, KrtAffix.TUMUN.upadesha)))
                }
                else -> error("Unknown purpose-governor kind")
            }
            val selected = purposeReadings.getOrPut(id) { candidate }
            require(selected == candidate) { "Conflicting purpose action descriptions in JSON" }
            index to selected
        }.toMap()
        val context = VakyaSemanticContext(words.mapIndexed { index, word ->
            index to word.strings("premises").mapTo(mutableSetOf()) { SemanticRelation.valueOf(it) }
        }.filter { it.second.isNotEmpty() }.toMap(), intendedObjects, krtGovernors, avyayaGovernors, purposeGovernors)
        val writtenPurposes = purposeReadings.values.filterIsInstance<PurposeActionGovernor.Written>().map { it.pada }
        val frame = analyzer.analyze(AkhyataVakya(record.text("example") ?: requireNotNull(record.text("id")), padas + avyayaGovernors.values + writtenPurposes + verb, verb),
            semanticContext = context)
        val expectedVoice = if (voice == "PASSIVE_CAUSATIVE") Prayoga.KARMANI else Prayoga.valueOf(voice)
        assertEquals(expectedVoice, frame.prayoga, "The fixture must name its actual inferred prayoga.")
        val dependent = input.get("dependent").asInt
        assertEquals(krtGovernors.size, frame.nominalKarakas.size)
        assertEquals(purposeGovernors.size, frame.purposeKarakas.size)
        val relation = if (dependent in purposeGovernors) frame.purposeKarakas.single { it.dependent == dependent }.let {
            assertEquals(purposeGovernors.getValue(dependent), it.governor)
            assertNotEquals(frame.id, it.relation.kriyaId)
            assertTrue(frame.relations.none { finite -> finite.participant.pada === padas[dependent] })
            it.relation
        } else if (dependent !in krtGovernors) frame.relations.single { it.participant.pada === padas[dependent] }
            else frame.nominalKarakas.single { it.dependent == dependent }.let {
                assertEquals(krtGovernors.getValue(dependent).governorIndex, it.governor)
                assertEquals(KriyaId("${frame.id}/krt_${it.governor}"), it.relation.kriyaId)
                it.relation.evidence.flatMap { proof -> proof.semanticEvidence }.filter { premise ->
                    premise.detail.startsWith("Explicit kṛt governor")
                }.forEach { premise ->
                    assertTrue(premise.detail.contains("governor occurrence: ${it.relation.kriyaId};"), premise.detail)
                }
                assertTrue(frame.relations.none { finite -> finite.participant.pada === padas[dependent] })
                it.relation
            }
        assertEquals(expected.strings("chain"), relation.evidence.map { it.sutra })
        assertEquals(expected.text("karaka"), (relation.resolution as? FrameKarakaResolution.Resolved)?.karaka?.name)
        assertEquals(expected.text("case"), relation.resolvedVibhakti?.name)
        expected.text("blockingSutra")?.let { prohibition ->
            assertTrue(relation.rejectedRules.any { it.prohibition.sutra == prohibition },
                "Expected canonical prohibition $prohibition in ${record.text("id")}: ${relation.rejectedRules}")
        }
        assertEquals(expected.strings("additionalDesignations").toSet(),
            relation.additionalDesignations.map { it.name }.toSet())
        if (expected.has("roleCandidates")) {
            assertEquals(expected.strings("roleCandidates").toSet(),
                (relation.resolution as? FrameKarakaResolution.Ambiguous)?.candidates?.map { it.name }?.toSet())
        }
        assertEquals(expected.strings("alternatives").toSet(), relation.alternatives.mapNotNull { it.vibhakti?.name }.toSet())
        if (expected.has("competingAnalyses")) {
            val competing = expected.getAsJsonArray("competingAnalyses").map { item ->
                val alternative = item.asJsonObject
                Triple(alternative.text("karaka"), alternative.text("case"), alternative.strings("chain"))
            }
            assertEquals(competing, relation.alternatives.filter { it.kind == KarakaAlternativeKind.COMPETING_ANALYSIS }
                .map { Triple(it.karaka.name, it.vibhakti?.name, it.evidence.map { proof -> proof.sutra }) })
        }
        val links = requireNotNull(frame.nominalCaseLinks)
        val expectedJoint = expected.getAsJsonArray("jointAlternatives")?.map { item ->
            val alternative = item.asJsonObject
            Triple(alternative.getAsJsonArray("members").map { it.asInt }.toSet(),
                alternative.text("case"), alternative.strings("chain"))
        }.orEmpty()
        assertEquals(expectedJoint, links.jointAlternatives.map { Triple(it.members, it.vibhakti.name, it.evidence.map { proof -> proof.sutra }) })
        links.jointAlternatives.forEach { alternative ->
            assertTrue(alternative.evidence.all { it.optional && !it.applied })
        }
        assertEquals(expected.get("candidateCount").asInt, links.candidates.count { it.dependent == dependent })
        assertEquals(expected.get("establishedCount").asInt, links.established.count { it.dependent == dependent })
        assertEquals(expected.strings("missingPremises").toSet(), links.candidates.filter { it.dependent == dependent }
            .flatMap { it.missingSemanticPremises }.map { it.name }.toSet())
        if (expected.has("diagnostics")) {
            assertEquals(expected.strings("diagnostics").toSet(), frame.diagnostics.filter { it.nominalIndex == dependent }
                .map { it.code.name }.toSet())
        }
        val expectedMediators = expected.getAsJsonObject("unresolvedObjectMediators")?.entrySet()?.associate { (index, roles) ->
            "p_$index" to roles.asJsonArray.map { Karaka.valueOf(it.asString) }.toSet()
        }.orEmpty()
        assertEquals(expectedMediators, relation.unresolvedObjectMediators)
        if (expected.has("coexistingKarakas")) assertEquals(expected.strings("coexistingKarakas").toSet(),
            relation.coexistingKarakas.map { it.name }.toSet())
        if (relation.coexistingKarakas.isNotEmpty()) assertNull(relation.primaryOptionalDecision)
        val optionalBranches = relation.alternatives.filter {
            it.kind in setOf(KarakaAlternativeKind.OPTIONAL_APPLY, KarakaAlternativeKind.OPTIONAL_DECLINE)
        }
        if (optionalBranches.isNotEmpty()) {
            assertNotNull(relation.primaryOptionalDecision)
            optionalBranches.forEach {
                assertNotNull(it.optionalDecision)
                assertTrue(it.evidence.all { proof -> proof.applied }, "A declined decision is not designation proof.")
            }
        }
        if (expected.has("primaryOptionalDecision")) {
            val decision = expected.getAsJsonObject("primaryOptionalDecision")
            assertEquals(decision.text("sutra"), relation.primaryOptionalDecision?.sutra)
            assertEquals(decision.get("applied").asBoolean, relation.primaryOptionalDecision?.applied)
        }
        if (expected.has("optionalBranches")) {
            val branches = expected.getAsJsonArray("optionalBranches").map { value ->
                val branch = value.asJsonObject
                listOf(branch.text("karaka"), branch.text("case"), branch.strings("chain"),
                    branch.text("kind"), branch.get("observed").asBoolean, branch.text("decisionSutra"),
                    branch.get("applied").asBoolean)
            }
            assertEquals(branches, optionalBranches.map {
                listOf(it.karaka.name, it.vibhakti?.name, it.evidence.map { proof -> proof.sutra },
                    it.kind.name, it.matchesObservedMorphology, it.optionalDecision?.sutra, it.optionalDecision?.applied)
            })
        }
        if (expected.has("coexistentBranches")) {
            val branches = expected.getAsJsonArray("coexistentBranches").map { value ->
                val branch = value.asJsonObject
                listOf(branch.text("karaka"), branch.text("case"), branch.strings("chain"), branch.get("observed").asBoolean)
            }
            val actual = relation.alternatives.filter { it.kind == KarakaAlternativeKind.COEXISTENT_CASE }
            actual.forEach { assertNull(it.optionalDecision); assertTrue(it.evidence.all { proof -> proof.applied && !proof.optional }) }
            assertEquals(branches, actual.map { listOf(it.karaka.name, it.vibhakti?.name,
                it.evidence.map { proof -> proof.sutra }, it.matchesObservedMorphology) })
        }
    }
}
