package dev.panini.derivation

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.SemanticEvidenceSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class SubantaKarakaDerivationTest {
    @Test
    fun `requesting recipient form does not prove an omitted object`() {
        val request = KarakaSubantaDerivationRequest("राम", Karaka.SAMPRADANA, Vacana.EKAVACANA, "दा")
        val error = assertFailsWith<UnresolvedKarakaDerivationException> {
            SubantaEngine().deriveFromKaraka(request)
        }
        assertTrue(error.resolution.evidence.none { it.sutra == "1.4.32" })
        val derived = SubantaEngine().deriveFromKaraka(request.copy(
            intendedObject = dev.panini.analysis.IntendedObject.Implied))
        assertEquals("रामाय", derived.final.surface)
        assertEquals(listOf("1.4.32", "2.3.13"), requireNotNull(derived.karakaResolution).evidence.map { it.sutra })
    }
    @Test
    fun `unresolved karaka derivation does not invent nominative`() {
        val error = assertFailsWith<UnresolvedKarakaDerivationException> {
            SubantaEngine().deriveFromKaraka(KarakaSubantaDerivationRequest(
                pratipadika = "राम", karaka = Karaka.APADANA, vacana = Vacana.EKAVACANA,
                dhatu = "पत्", semanticRelations = setOf(SemanticRelation.SOURCE),
            ))
        }
        assertEquals(null, error.resolution.resolvedVibhakti)
        assertEquals(emptyList(), error.resolution.evidence)
    }

    @Test
    fun `optional case derivation retains ambiguity instead of inventing nominative`() {
        val error = assertFailsWith<UnresolvedKarakaDerivationException> {
            SubantaEngine().deriveFromKaraka(KarakaSubantaDerivationRequest(
                pratipadika = "जाड्य", karaka = Karaka.ANIRDHARITA, vacana = Vacana.EKAVACANA,
                dhatu = "बन्ध्", semanticRelations = setOf(SemanticRelation.CAUSE_HETU,
                    SemanticRelation.NON_FEMININE_QUALITY_CAUSE),
            ))
        }
        assertEquals(null, error.resolution.resolvedVibhakti)
        assertEquals(listOf("2.3.25"), error.resolution.evidence.map { it.sutra })
        assertTrue(error.resolution.alternatives.any { it.matchesObservedMorphology })
    }
    @Test
    fun `comparative vati provenance reaches indeclinable classification`() {
        val result = SubantaEngine().derive(SubantaDerivationRequest(
            "ब्राह्मणवत्", Vibhakti.SASTHI, Vacana.EKAVACANA,
            sourceSuffixUpadeshas = setOf("वति"),
        ))
        assertEquals("ब्राह्मणवत्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "1.1.37" })
        assertTrue(result.applications.any { it.sutra == "2.4.82" })
    }
    @Test
    fun `possessive consonant stem retains instrumental and locative endings`() {
        for ((case, expected) in listOf(
            Vibhakti.TRTIYA to "सङ्ख्यावता",
            Vibhakti.SAPTAMI to "सङ्ख्यावति",
        )) {
            val result = SubantaEngine().derive(SubantaDerivationRequest(
                "सङ्ख्यावत्", case, Vacana.EKAVACANA,
            ))
            assertEquals(expected, result.final.surface)
            assertTrue(result.applications.none { it.sutra == "2.4.82" })
        }
    }
    @Test
    fun `possessive consonant stem retains its genitive ending`() {
        val result = SubantaEngine().derive(SubantaDerivationRequest(
            "सङ्ख्यावत्", Vibhakti.SASTHI, Vacana.EKAVACANA,
        ))
        assertEquals("सङ्ख्यावतः", result.final.surface,
            result.applications.joinToString("\n") { "${it.sutra}: ${it.after.surface}" })
        assertTrue(result.applications.none { it.sutra == "2.4.82" })
    }
    @Test
    fun `a stems ending in na retain instrumental guna`() {
        val result = SubantaEngine().derive(SubantaDerivationRequest(
            "गणन", Vibhakti.TRTIYA, Vacana.EKAVACANA, dev.panini.core.Linga.NAPUMSAKA,
        ))
        assertEquals("गणनेन", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "7.1.12" })
        assertTrue(result.applications.any { it.sutra == "6.1.87" })
    }


    @Test
    fun `derives correct subanta for recipient`() {
        val result = SubantaEngine().deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = "राम",
                karaka = Karaka.SAMPRADANA,
                intendedObject = dev.panini.analysis.IntendedObject.Implied,
                vacana = Vacana.EKAVACANA,
                dhatu = "दा",
                isSakarmaka = true,
                prayoga = Prayoga.KARTARI
            )
        )
        // Recipient of giving is resolved to Chaturthi.
        // For a-stem masculine "राम" -> "रामाय"
        assertEquals(Vibhakti.CHATURTHI, result.initial.context.rupa.vibhakti)
        assertEquals("रामाय", result.final.surface)
        val evidence = requireNotNull(result.karakaResolution).evidence.first().semanticEvidence
        assertTrue(evidence.any { it.relation == SemanticRelation.RECIPIENT &&
            it.source == SemanticEvidenceSource.CLAUSE_CONTEXT })
        assertTrue(evidence.any { it.relation == SemanticRelation.DESIRED_OBJECT &&
            it.source == SemanticEvidenceSource.DHATU_VALENCY })
    }

    @Test
    fun `requested role does not erase explicitly supplied competing meanings`() {
        val error = assertFailsWith<UnresolvedKarakaDerivationException> {
            SubantaEngine().deriveFromKaraka(KarakaSubantaDerivationRequest(
                pratipadika = "राम", karaka = Karaka.SAMPRADANA,
                intendedObject = dev.panini.analysis.IntendedObject.Implied,
                vacana = Vacana.EKAVACANA, dhatu = "दा",
                semanticRelations = setOf(SemanticRelation.RECIPIENT, SemanticRelation.DESIRED_OBJECT),
            ))
        }
        assertEquals(null, error.resolution.resolved)
        assertEquals(setOf(Karaka.SAMPRADANA, Karaka.KARMAN), error.resolution.candidates)
        assertEquals(null, error.resolution.resolvedVibhakti)
    }

    @Test
    fun `derives correct subanta for instrument`() {
        val result = SubantaEngine().deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = "लेखनी",
                karaka = Karaka.KARANA,
                vacana = Vacana.EKAVACANA,
                dhatu = "लिख",
                isSakarmaka = true,
                prayoga = Prayoga.KARTARI
            )
        )
        // Instrument of writing is resolved to Trtiya.
        // For i-stem feminine "लेखनी" -> "लेखन्या" (via 7.3.116, etc.)
        assertEquals(Vibhakti.TRTIYA, result.initial.context.rupa.vibhakti)
        assertEquals("लेखन्या", result.final.surface)
    }

    @Test
    fun `respects abhihita blocking in active and passive prayogas`() {
        val engine = SubantaEngine()

        // Active voice (kartari): Karman (object) is unexpressed (anabhihita)
        // -> Dvitiya -> "रामम्"
        val activeRes = engine.deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = "राम",
                karaka = Karaka.KARMAN,
                vacana = Vacana.EKAVACANA,
                dhatu = "दा",
                isSakarmaka = true,
                prayoga = Prayoga.KARTARI
            )
        )
        assertEquals(Vibhakti.DVITIYA, activeRes.initial.context.rupa.vibhakti)
        assertEquals("रामम्", activeRes.final.surface)

        // Passive voice (karmani): Karman (object) is expressed (abhihita) by verb
        // -> blocks Dvitiya -> 2.3.46 licenses Prathama -> "रामः"
        val passiveRes = engine.deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = "राम",
                karaka = Karaka.KARMAN,
                vacana = Vacana.EKAVACANA,
                dhatu = "दा",
                isSakarmaka = true,
                prayoga = Prayoga.KARMANI
            )
        )
        assertEquals(Vibhakti.PRATHAMA, passiveRes.initial.context.rupa.vibhakti)
        assertEquals("रामः", passiveRes.final.surface)
        assertTrue(requireNotNull(passiveRes.karakaResolution).evidence.any { it.sutra == "2.3.46" })
    }

    @Test
    fun `attaches karaka resolution to derivation result`() {
        val result = SubantaEngine().deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = "राम",
                karaka = Karaka.SAMPRADANA,
                intendedObject = dev.panini.analysis.IntendedObject.Implied,
                vacana = Vacana.EKAVACANA,
                dhatu = "दा"
            )
        )
        val resolution = result.karakaResolution
        kotlin.test.assertNotNull(resolution)
        assertEquals(Karaka.SAMPRADANA, resolution.resolved)
        assertEquals(Vibhakti.CHATURTHI, resolution.resolvedVibhakti)
        kotlin.test.assertTrue(resolution.evidence.any { it.sutra == "1.4.32" })
        kotlin.test.assertTrue(resolution.evidence.any { it.sutra == "2.3.13" })
    }

    @Test
    fun `supports custom semantic relations overrides`() {
        val result = SubantaEngine().deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = "राम",
                karaka = Karaka.SAMPRADANA,
                intendedObject = dev.panini.analysis.IntendedObject.Implied,
                vacana = Vacana.EKAVACANA,
                dhatu = "पठ्",
                semanticRelations = setOf(SemanticRelation.RECIPIENT)
            )
        )
        assertEquals(Vibhakti.CHATURTHI, result.initial.context.rupa.vibhakti)
        assertEquals("रामाय", result.final.surface)
    }

    @Test
    fun `supports upapada accompaniment mapping`() {
        val result = SubantaEngine().deriveFromKaraka(
            KarakaSubantaDerivationRequest(
                pratipadika = "राम",
                karaka = Karaka.ANIRDHARITA,
                vacana = Vacana.EKAVACANA,
                dhatu = "गम्",
                upapada = "सह",
                semanticRelations = setOf(dev.panini.analysis.SemanticRelation.ACCOMPANIMENT,
                    dev.panini.analysis.SemanticRelation.NON_PRINCIPAL_ACCOMPANIMENT),
            )
        )
        assertEquals(Vibhakti.TRTIYA, result.initial.context.rupa.vibhakti)
        assertEquals("रामेण", result.final.surface)
    }
}
