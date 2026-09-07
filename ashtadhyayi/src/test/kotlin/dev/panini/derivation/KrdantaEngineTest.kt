package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SutraStage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class KrdantaEngineTest {

    private val engine = KrdantaEngine()

    @Test
    fun `ktva generates bhutva and krtva`() {
        val res1 = engine.derive(KrdantaDerivationRequest("भू", Samjna.KTVA))
        assertEquals("भूत्वा", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.4.21" })

        val res2 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.KTVA))
        assertEquals("कृत्वा", res2.final.surface)
    }

    @Test
    fun `lyap generates sambhuya and anukrtya with tuk-agama`() {
        val res1 = engine.derive(KrdantaDerivationRequest("भू", Samjna.KTVA, upasarga = "सम्"))
        assertEquals("संभूय", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.4.21" || it.sutra == "7.1.37" })

        val res2 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.KTVA, upasarga = "अनु"))
        assertEquals("अनुकृत्य", res2.final.surface)
        assertTrue(res2.applications.any { it.sutra == "3.4.21" || it.sutra == "7.1.37" })
        assertTrue(res2.applications.any { it.sutra == "6.1.71" })
    }

    @Test
    fun `tumun generates bhavitum and kartum`() {
        val res1 = engine.derive(KrdantaDerivationRequest("भू", Samjna.TUMUN))
        assertEquals("भवितुम्", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.3.158" })
        assertTrue(res1.applications.any { it.sutra == "7.2.35" })

        val res2 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.TUMUN))
        assertEquals("कर्तुम्", res2.final.surface)
    }

    @Test
    fun `krtya affixes generate kartavya karaniya and karya`() {
        val res1 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.TAVYA))
        assertEquals("कर्तव्य", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.1.96" })

        val res2 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.ANIYAR))
        assertEquals("करणीय", res2.final.surface)
        assertTrue(res2.applications.any { it.sutra == "3.1.96" })

        val res3 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.NYAT))
        assertEquals("कार्य", res3.final.surface)
        assertTrue(res3.applications.any { it.sutra == "3.1.124" })
    }

    @Test
    fun `nistha affixes generate bhuta and krta`() {
        val res1 = engine.derive(KrdantaDerivationRequest("भू", Samjna.KTA))
        assertEquals("भूत", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "1.1.26" })

        val res2 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.KTA))
        assertEquals("कृत", res2.final.surface)
    }

    @Test
    fun `nvul trc and ghan generate bhavaka kartr and bhava`() {
        val res1 = engine.derive(KrdantaDerivationRequest("भू", Samjna.NVUL))
        assertEquals("भावक", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.1.133" })

        val res2 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.TRC))
        assertEquals("कर्तृ", res2.final.surface)

        val res3 = engine.derive(KrdantaDerivationRequest("भू", Samjna.GHAN))
        assertEquals("भाव", res3.final.surface)
        assertTrue(res3.applications.any { it.sutra == "3.3.18" })
    }

    @Test
    fun `ghan derives pvm action stems phonologically`() {
        val expected = mapOf(
            "युज्" to "योग",
            "शिष्" to "शेष",
            "मूल्" to "मूल",
            "भज्" to "भाग",
            "हृ" to "हार",
        )

        expected.forEach { (dhatu, surface) ->
            assertEquals(surface, engine.derive(KrdantaDerivationRequest(dhatu, Samjna.GHAN)).final.surface)
        }
    }

    @Test
    fun `lyut derives pvm action stems phonologically`() {
        val expected = mapOf(
            "युज्" to "योजन",
            "गण" to "गणन",
            "धृ" to "धरण",
            "स्था" to "स्थान",
            "जन्" to "जनन",
            "हृ" to "हरण",
        )

        expected.forEach { (dhatu, surface) ->
            val result = engine.derive(KrdantaDerivationRequest(dhatu, Samjna.LYUT))
            assertEquals(surface, result.final.surface)
            val introduction = result.applications.first { it.sutra == "3.3.115" }
            val raw = introduction.after.terms.last()
            assertEquals("ल्युट्", raw.surface)
            assertEquals(ItProcessingPhase.RAW_UPADESHA, raw.itProcessingPhase)
            assertEquals("3.3.115", raw.createdBySutra)
            assertTrue(result.applications.map { it.sutra }.containsAll(setOf("1.3.8", "1.3.3", "1.3.9", "7.1.1")),
                result.applications.joinToString("\n") { "${it.sutra}: ${it.before.surface} -> ${it.after.surface}" })
            result.final.requireCompleteItProcessing()
        }
    }

    @Test
    fun `source affixes resolve through typed krdanta capability`() {
        assertEquals("योग", engine.deriveSourceStem("युज्", "घञ्").surface)
        assertEquals("योजन", engine.deriveSourceStem("युज्", "ल्युट्").surface)
        assertEquals("धरण", engine.deriveSourceStem("धृ", "अन").surface)
        assertEquals("हार", engine.deriveSourceStem("हृ", "घञ्").surface)
        assertTrue(assertIs<KrdantaSourceStem.Productive>(engine.deriveSourceStem("युज्", "घञ्")).supportsAStemDeclension)
        assertEquals("हृत", engine.deriveSourceStem("हृ", "क्त").surface)
        assertEquals("पठित", engine.deriveSourceStem("पठ्", "क्त").surface)
        assertEquals("क्षेप", engine.deriveSourceStem("क्षिप्", "घञ्").surface)
        assertEquals(
            KrdantaSourceStem.Unresolved.Reason.UNKNOWN_DHATU,
            assertIs<KrdantaSourceStem.Unresolved>(engine.deriveSourceStem("अज्ञात", "घञ्")).reason,
        )
        assertEquals(
            KrdantaSourceStem.Unresolved.Reason.UNKNOWN_KRT_AFFIX,
            assertIs<KrdantaSourceStem.Unresolved>(engine.deriveSourceStem("युज्", "अज्ञात")).reason,
        )
    }

    @Test
    fun `sanadi is processed before the krt affix with complete provenance`() {
        val result = engine.derive(
            KrdantaDerivationRequest("शुध्", Samjna.TAVYA, sanadiPratyayas = listOf("णिच्")),
        )

        assertEquals("शोधयितव्य", result.final.surface)
        assertTrue(
            result.applications.map { it.sutra }.containsAll(
                setOf("3.1.26", "3.1.96", "1.3.3", "1.3.7", "1.3.9", "7.2.35", "7.3.84", "6.1.78"),
            ),
            result.applications.joinToString { it.sutra },
        )
        assertTrue(result.final.terms.any { it.upadesha == "णिच्" })
        result.final.requireCompleteItProcessing()
        assertEquals(
            result.final.surface,
            engine.deriveSourceStem("शुध्", "तव्यत्", listOf("णिच्")).surface,
        )
    }

    @Test
    fun `nic follows the krt-specific it and lopa paths`() {
        val expected = mapOf(
            Samjna.KTA to "शोधित",
            Samjna.KTVA to "शोधयित्वा",
            Samjna.TUMUN to "शोधयितुम्",
            Samjna.TAVYA to "शोधयितव्य",
            Samjna.GHAN to "शोध",
            Samjna.LYUT to "शोधन",
        )

        expected.forEach { (samjna, surface) ->
            val result = engine.derive(KrdantaDerivationRequest("शुध्", samjna, sanadiPratyayas = listOf("णिच्")))
            assertEquals(surface, result.final.surface, samjna.toString())
            result.final.requireCompleteItProcessing()
            if (samjna in setOf(Samjna.GHAN, Samjna.LYUT)) {
                assertTrue(result.applications.any { it.sutra == "6.4.51" })
            }
        }
    }

    @Test
    fun `krdanta provenance contains only explicitly staged rules`() {
        val requests = listOf(
            KrdantaDerivationRequest("कृ", Samjna.KTVA, upasarga = "अनु"),
            KrdantaDerivationRequest("भू", Samjna.TUMUN),
        )

        requests.forEach { request ->
            val result = engine.derive(request)
            assertTrue(result.applications.isNotEmpty())
            result.applications.forEach { application ->
                val sutra = Ashtadhyayi.registry.require(application.sutra)
                assertTrue(sutra.stage != SutraStage.UNSPECIFIED, "${application.sutra} lacks pipeline metadata")
            }
            assertEquals(
                result.final,
                assertIs<DerivationEvent.Completed>(result.events.last()).finalState,
            )
        }
    }

    @Test
    fun `krdanta grading and junctions remain visible in the trace`() {
        fun sutras(dhatu: String, samjna: Samjna): Set<String> {
            val result = engine.derive(KrdantaDerivationRequest(dhatu, samjna))
            result.final.requireCompleteItProcessing()
            return result.applications.mapTo(linkedSetOf()) { it.sutra }
        }

        assertTrue(sutras("भू", Samjna.TUMUN).containsAll(setOf("7.3.84", "6.1.78")))
        assertTrue("7.2.115" in sutras("कृ", Samjna.NYAT))
        assertTrue(sutras("भू", Samjna.NVUL).containsAll(setOf("7.2.115", "7.1.1", "6.1.78")))
        assertTrue(sutras("युज्", Samjna.GHAN).containsAll(setOf("7.3.86", "7.3.52")))
        assertTrue(sutras("युज्", Samjna.LYUT).containsAll(setOf("7.3.86", "7.1.1")))
    }
}
