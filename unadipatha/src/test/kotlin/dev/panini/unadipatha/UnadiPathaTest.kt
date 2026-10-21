package dev.panini.unadipatha

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.core.SanadiAffix
import dev.panini.dhatupatha.adadi.VaaDhatu
import dev.panini.dhatupatha.bhvadi.PumsDhatu
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Accent
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toDevanagari
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.Svara
import dev.panini.unadipatha.adhyaya1.UnadiAdhyaya1Registry
import dev.panini.unadipatha.adhyaya2.UnadiAdhyaya2Registry
import dev.panini.unadipatha.adhyaya3.UnadiAdhyaya3Registry
import dev.panini.unadipatha.adhyaya4.UnadiAdhyaya4Registry
import dev.panini.unadipatha.adhyaya5.UnadiAdhyaya5Registry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UnadiPathaTest {

    @Test
    fun `central patha is composed from adhyaya registries`() {
        val registries = listOf(
            UnadiAdhyaya1Registry.modeledSutras,
            UnadiAdhyaya2Registry.modeledSutras,
            UnadiAdhyaya3Registry.modeledSutras,
            UnadiAdhyaya4Registry.modeledSutras,
            UnadiAdhyaya5Registry.modeledSutras,
        )
        assertEquals(registries.flatten(), UnadiPatha.modeledSourceSutras)
        registries.forEachIndexed { index, sutras ->
            assertTrue(sutras.all { it.number.startsWith("${index + 1}.") })
        }
        assertEquals((1..157).map { "1.$it" }, UnadiAdhyaya1Registry.modeledSutras.map { it.number })
    }

    @Test
    fun `complete source catalog is ordered sutra by sutra`() {
        assertEquals(748, UnadiPatha.sourceSutras.size)
        assertEquals(listOf(157, 123, 160, 238, 70), (1..5).map { UnadiPathaCatalog.adhyaya(it).size })
        assertEquals("कृवापाजिमिस्वदिसाध्यशूभ्य उण्", UnadiPathaCatalog.find("1.1")?.text)
        assertEquals("उण्", UnadiPathaCatalog.find("1.1")?.pratyaya)
        assertEquals("मङ्गेरलच्", UnadiPathaCatalog.find("5.70")?.text)
        assertTrue(UnadiPathaCatalog.find("1.1")!!.siddhantaKaumudi.isNotBlank())
    }

    @Test
    fun `structured source migration starts in canonical order`() {
        assertEquals((1..157).map { "1.$it" }, UnadiPatha.modeledSourceSutras.map { it.number })
        UnadiPatha.modeledSourceSutras.forEach { modeled ->
            val source = UnadiPathaCatalog.find(modeled.number)
            assertEquals(source?.text, modeled.text)
            assertEquals(source?.pratyaya, modeled.pratyaya)
        }
        assertEquals(setOf("सन्", "चर्", "चट्"), UnadiPatha.modeledSourceSutras[2].rootReferences)
        assertEquals("उ", UnadiPatha.modeledSourceSutras[2].pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(
            UnadiPatha.modeledSourceSutras[2].rootReferences,
            UnadiPatha.modeledSourceSutras[2].rootReferenceVarnas.values.map { it.toDevanagari() }.toSet(),
        )
    }

    @Test
    fun `canonical sutras contain no source-only placeholders`() {
        assertEquals(157, UnadiPatha.sutras.size)
        assertTrue(
            UnadiPatha.sutras.all { sutra ->
                sutra.pratyayaSurface.isNotEmpty() ||
                    (sutra.rootPratyayaMap.isNotEmpty() && sutra.rootPratyayaMap.values.all { it.surface.isNotEmpty() })
            },
        )
        assertEquals(UnadiPatha.sutras.map { it.number }, UnadiPatha.sutras.map { it.number }.distinct())
    }

    @Test
    fun `canonical sutras provide proper Hindi explanations`() {
        UnadiPatha.sutras.forEach { sutra ->
            val explanation = assertNotNull(sutra.hindiExplanation, sutra.number)
            assertTrue(explanation.length >= 50, "Hindi explanation is too short for ${sutra.number}")
            assertTrue("प्रत्यय" in explanation, "Hindi explanation must state the प्रत्यय for ${sutra.number}")
        }
    }

    @Test
    fun `sutra 1 5 models its root meaning output and ra to la operation`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.5" }
        assertEquals("तॄ", sutra.roots.single().upadesha)
        assertEquals(Artha.Karaka.KARANA, sutra.meaning)
        assertEquals("तालु", sutra.rootOutputVarnas.getValue("तॄ").toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.RA, Vyanjana.LA),
            sutra.varnaSubstitutions.single(),
        )
    }

    @Test
    fun `sutra 1 6 models krka upapada and ca to ka operation`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.6" }
        assertEquals("वचँ", sutra.roots.single().upadesha)
        assertEquals("कृक", sutra.upapadas.single().toDevanagari())
        assertEquals("कृकवाकु", sutra.rootOutputVarnas.getValue("वचँ").toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.CA, Vyanjana.KA),
            sutra.varnaSubstitutions.single(),
        )
        assertEquals(Artha.Karaka.KARTA, sutra.meaning)
    }

    @Test
    fun `sutra 1 7 resolves every root and preserves per-root meanings`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.7" }
        assertEquals(10, sutra.roots.size)
        assertTrue(sutra.rootReferences.isEmpty())
        assertEquals(10, sutra.rootOutputVarnas.size)
        assertEquals(10, sutra.rootMeaningMap.size)
        val masj = sutra.roots.single { it.id == "06.0151" }
        val match = sutra.matchFor(masj)
        assertEquals("मद्गु", match.derivedStemVarnas?.toDevanagari())
        assertEquals(Artha.Rudhi("मद्गु", "water bird"), match.meaning)
        assertEquals(
            listOf(
                UnadiVarnaSubstitution(Vyanjana.JA, Vyanjana.GA),
                UnadiVarnaSubstitution(Vyanjana.SA, Vyanjana.DA),
            ),
            sutra.varnaSubstitutions,
        )
    }

    @Test
    fun `sutra 1 8 preserves homonymous ana roots and ca extensions`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.8" }
        assertEquals(setOf("04.0071", "01.0512", "01.0359", "01.0377"), sutra.roots.map { it.id }.toSet())
        assertEquals("अणु", sutra.rootOutputVarnas.getValue("अणँ").toDevanagari())
        assertEquals("कटु", sutra.rootOutputVarnas.getValue("कटीँ").toDevanagari())
        assertEquals("वटु", sutra.rootOutputVarnas.getValue("वटिँ").toDevanagari())
        assertEquals(Artha.Rudhi("कटु", "pungent taste"), sutra.rootMeaningMap.getValue("कटीँ"))
    }

    @Test
    fun `sutra 1 9 restricts ana to grain meaning and assigns initial udatta`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.9" }
        assertEquals(setOf("04.0071", "01.0512"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf(ItMarker.NIT), sutra.itMarkers)
        assertEquals(Artha.Rudhi("धान्य", "grain"), sutra.applicationMeaning)
        assertEquals(Accent.UDATTA, sutra.assignedAccent)
        assertEquals(0, sutra.accentVowelIndex)
        assertEquals("अणु", sutra.rootOutputVarnas.getValue("अणँ").toDevanagari())
    }

    @Test
    fun `sutra 1 10 resolves all cited root surfaces and the avayava bidi extension`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.10" }
        assertEquals(
            setOf("शृ", "स्वृ", "स्निह्", "त्रप्", "अस्", "वस्", "हन्", "क्लिद्", "बन्ध्", "मन्", "बिन्द्"),
            sutra.roots.map { it.sourceSurface }.toSet(),
        )
        val bidi = sutra.roots.single { it.upadesha == "बिदिँ" }
        assertEquals("बिन्द्", bidi.sourceSurface)
        assertTrue("अवयवे" in bidi.artha)
        val outputsBySurface = sutra.roots.associate { root ->
            root.sourceSurface to sutra.matchFor(root).derivedStemVarnas?.toDevanagari()
        }
        assertEquals("शरु", outputsBySurface["शृ"])
        assertEquals("स्वरु", outputsBySurface["स्वृ"])
        assertEquals("बिन्दु", outputsBySurface["बिन्द्"])
        assertTrue(sutra.rootReferences.isEmpty())
    }

    @Test
    fun `sutra 1 11 models samprasarana and dha substitution with varnas`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.11" }
        assertEquals("स्यन्दूँ", sutra.roots.single().upadesha)
        assertEquals("सिन्धु", sutra.rootOutputVarnas.getValue("स्यन्दूँ").toDevanagari())
        assertEquals(
            listOf(
                UnadiVarnaSubstitution(Vyanjana.YA, Svara.I),
                UnadiVarnaSubstitution(Vyanjana.DA, Vyanjana.DHA),
            ),
            sutra.varnaSubstitutions,
        )
        assertEquals("सिन्धु", (sutra.meaning as Artha.Rudhi).devanagari)
        assertTrue("संप्रसारण" in sutra.hindiExplanation.orEmpty())
        assertTrue("देश" in sutra.hindiExplanation.orEmpty())
    }

    @Test
    fun `sutra 1 12 replaces the initial u with i and derives indu`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.12" }
        assertEquals("उन्दीँ", sutra.roots.single().upadesha)
        assertEquals("इन्दु", sutra.rootOutputVarnas.getValue("उन्दीँ").toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Svara.U, Svara.I),
            sutra.varnaSubstitutions.single(),
        )
        assertEquals(Artha.Rudhi("इन्दु", "moon"), sutra.meaning)
        assertTrue("आरम्भिक उ-वर्ण" in sutra.hindiExplanation.orEmpty())
    }

    @Test
    fun `sutra 1 13 is kit shortens initial ii and derives isu`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.13" }
        assertEquals("01.0695", sutra.roots.single().id)
        assertEquals(setOf(ItMarker.KIT, ItMarker.NIT), sutra.itMarkers)
        assertEquals(UnadiVarnaSubstitution(Svara.II, Svara.I), sutra.varnaSubstitutions.single())
        assertEquals("इषु", sutra.rootOutputVarnas.getValue("ईषँ").toDevanagari())
        assertEquals(Artha.Rudhi("इषु", "arrow"), sutra.meaning)
    }

    @Test
    fun `sutra 1 14 deletes initial sa and derives kandu`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.14" }
        assertEquals("स्कन्दिँर्", sutra.roots.single().upadesha)
        assertEquals(listOf(UnadiVarnaLopa(Vyanjana.SA)), sutra.varnaLopas)
        assertEquals("कन्दु", sutra.rootOutputVarnas.getValue("स्कन्दिँर्").toDevanagari())
        assertTrue("विशेष अर्थ नहीं जोड़ा" in sutra.hindiExplanation.orEmpty())
    }

    @Test
    fun `sutra 1 15 models asum augmentation sa lopa and rajju`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.15" }
        assertEquals("04.0075", sutra.roots.single().id)
        assertEquals(setOf(ItMarker.NIT), sutra.itMarkers)
        assertEquals(listOf(UnadiVarnaLopa(Vyanjana.SA)), sutra.varnaLopas)
        assertEquals(
            listOf(
                UnadiVarnaAgama(
                    listOf(Svara.A, Vyanjana.SA),
                    UnadiAgamaPlacement.AFTER_FINAL_ROOT_VOWEL,
                ),
            ),
            sutra.varnaAgamas,
        )
        assertEquals("रज्जु", sutra.rootOutputVarnas.getValue("सृजँ").toDevanagari())
        assertEquals(Accent.UDATTA, sutra.assignedAccent)
        assertEquals(0, sutra.accentVowelIndex)
    }

    @Test
    fun `sutra 1 16 exchanges initial ka and final ta to derive tarku`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.16" }
        assertEquals("07.0010", sutra.roots.single().id)
        assertEquals(
            UnadiVarnaViparyaya(Vyanjana.KA, Vyanjana.TA),
            sutra.varnaViparyayas.single(),
        )
        assertEquals("तर्कु", sutra.rootOutputVarnas.getValue("कृतीँ").toDevanagari())
        assertTrue("वेष्टनार्थक" in sutra.hindiExplanation.orEmpty())
    }

    @Test
    fun `sutra 1 17 requires ni upasarga applies kutva and derives nyanku`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.17" }
        assertEquals("01.0215", sutra.roots.single().id)
        assertEquals("नि", sutra.upasargas.single().toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.CA, Vyanjana.KA),
            sutra.varnaSubstitutions.single(),
        )
        assertEquals("न्यङ्कु", sutra.rootOutputVarnas.getValue("अन्चुँ").toDevanagari())
        assertTrue("७.३.५३" in sutra.hindiExplanation.orEmpty())
    }

    @Test
    fun `sutra 1 18 keeps five root-specific operations separate`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.18" }
        assertEquals(setOf("01.0608", "01.0333", "01.1136", "04.0073", "04.0044"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf("फल्गु", "पटु", "नाकु", "मधु", "जतु"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals(
            UnadiVarnaAgama(listOf(Vyanjana.GA), UnadiAgamaPlacement.BEFORE_SUFFIX),
            sutra.rootMorphology.getValue("फलँ").varnaAgamas.single(),
        )
        val pati = sutra.rootMorphology.getValue("पटँ")
        assertEquals(setOf(SanadiAffix.NIC), pati.requiredSanadiAffixes)
        assertEquals("पाटि", pati.conditionedStem?.toDevanagari())
        assertEquals("पट्", pati.stemAdesha?.toDevanagari())
        assertEquals("नाक्", sutra.rootMorphology.getValue("णमँ").stemAdesha?.toDevanagari())
        assertEquals(UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.DHA), sutra.rootMorphology.getValue("मनँ").varnaSubstitutions.single())
        assertEquals(UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.TA), sutra.rootMorphology.getValue("जनीँ").varnaSubstitutions.single())
    }

    @Test
    fun `sutra 1 19 continues ga agama but not the expired nit scope`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.19" }
        assertEquals("01.0564", sutra.roots.single().id)
        assertEquals(
            UnadiVarnaAgama(listOf(Vyanjana.GA), UnadiAgamaPlacement.BEFORE_SUFFIX),
            sutra.varnaAgamas.single(),
        )
        assertEquals("वल्गु", sutra.rootOutputVarnas.getValue("वलँ").toDevanagari())
        assertTrue(ItMarker.NIT !in sutra.itMarkers)
        assertEquals(null, sutra.assignedAccent)
    }

    @Test
    fun `sutra 1 20 is kit and applies sanvat treatment to syai`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.20" }
        assertEquals("श्यैङ्", sutra.roots.single().upadesha)
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals(UnadiSanvatTreatment(), sutra.sanvatTreatment)
        assertEquals("शिशु", sutra.rootOutputVarnas.getValue("श्यैङ्").toDevanagari())
        assertEquals(Artha.Rudhi("शिशु", "child"), sutra.meaning)
    }

    @Test
    fun `sutra 1 21 explicitly reduplicates ya and blocks san itva`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.21" }
        assertEquals("02.0044", sutra.roots.single().id)
        assertEquals(UnadiSanvatTreatment(explicitDve = true, blocksItva = true), sutra.sanvatTreatment)
        assertEquals("ययु", sutra.rootOutputVarnas.getValue("या").toDevanagari())
        assertTrue("अश्वमेध" in sutra.hindiExplanation.orEmpty())
    }

    @Test
    fun `sutra 1 22 applies kit ku and sanvat to all attested ca extensions`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.22" }
        assertEquals(setOf("03.0006", "08.0010", "02.0002", "02.0051"), sutra.roots.map { it.id }.toSet())
        assertEquals("कु", sutra.pratyaya)
        assertEquals("उ", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals(UnadiSanvatTreatment(), sutra.sanvatTreatment)
        assertEquals(setOf("बभ्रु", "चक्रु", "जघ्नु", "पपु"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals(Artha.Rudhi("चक्रु", "doer"), sutra.rootMeaningMap.getValue("डुकृञ्"))
        assertEquals(Artha.Rudhi("पपु", "protector"), sutra.rootMeaningMap.getValue("पा"))
    }

    @Test
    fun `sutra 1 23 derives five ku words and limits samprasarana to vyadh`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.23" }
        assertEquals(setOf("03.0004", "07.0002", "04.0078", "04.0161", "05.0025"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals(setOf("पुरु", "भिदु", "विधु", "गृधु", "धृषु"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals(setOf("व्यधँ"), sutra.rootMorphology.keys)
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.YA, Svara.I),
            sutra.rootMorphology.getValue("व्यधँ").varnaSubstitutions.single(),
        )
        assertEquals(Artha.Rudhi("भिदु", "thunderbolt"), sutra.rootMeaningMap.getValue("भिदिँर्"))
    }

    @Test
    fun `sutra 1 24 expands vocalic r to ur for kuru and guru`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.24" }
        assertEquals(setOf("08.0010", "09.0033"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf("कुरु", "गुरु"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals(
            listOf(
                UnadiVarnaExpansion(Svara.R, listOf(Svara.U, Vyanjana.RA)),
                UnadiVarnaExpansion(Svara.RR, listOf(Svara.U, Vyanjana.RA)),
            ),
            sutra.varnaExpansions,
        )
        assertTrue(sutra.rootMeaningMap.values.all { it is Artha.Rudhi && it.english == null })
    }

    @Test
    fun `sutra 1 25 preserves three prefix-conditioned variants`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.25" }
        assertEquals("01.1077", sutra.roots.single().id)
        assertEquals(setOf("अप", "दुस्", "सु"), sutra.upasargas.map { it.toDevanagari() }.toSet())
        assertEquals(setOf("अपष्ठु", "दुष्ठु", "सुष्ठु"), sutra.derivedVariants.map { it.output.toDevanagari() }.toSet())
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.SA, Vyanjana.SSA),
            sutra.varnaSubstitutions.single(),
        )
        assertEquals(Artha.Rudhi("अपष्ठु", "adverse"), sutra.derivedVariants.single { it.output.toDevanagari() == "अपष्ठु" }.meaning)
        assertEquals(null, sutra.derivedVariants.single { it.output.toDevanagari() == "दुष्ठु" }.let { (it.meaning as Artha.Rudhi).english })
    }

    @Test
    fun `sutra 1 26 changes the medial a of rap to i and derives ripu`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.26" }
        assertEquals("01.0467", sutra.roots.single().id)
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals(UnadiVarnaSubstitution(Svara.A, Svara.I), sutra.varnaSubstitutions.single())
        assertEquals("रिपु", sutra.rootOutputVarnas.getValue("रपँ").toDevanagari())
        assertEquals(Artha.Rudhi("रिपु", "enemy"), sutra.meaning)
        assertTrue("उपधा" in sutra.hindiExplanation.orEmpty())
    }

    @Test
    fun `sutra 1 27 pairs six roots with their own prescribed operations`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.27" }
        assertEquals(setOf("01.0256", "01.1143", "01.0511", "01.0536", "10.0245", "01.0005"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf("पशि"), sutra.rootReferences)
        assertEquals(setOf("ऋजु", "पशु", "कन्तु", "अन्धु", "पांशु", "बाहु"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals("ऋज्", sutra.rootMorphology.getValue("अर्जँ").stemAdesha?.toDevanagari())
        assertEquals("पश्", sutra.rootMorphology.getValue("दृशिँर्").stemAdesha?.toDevanagari())
        assertEquals(
            UnadiVarnaAgama(listOf(Vyanjana.TA), UnadiAgamaPlacement.BEFORE_SUFFIX),
            sutra.rootMorphology.getValue("कमुँ").varnaAgamas.single(),
        )
        assertEquals(
            UnadiVarnaAgama(listOf(Vyanjana.DHA), UnadiAgamaPlacement.BEFORE_SUFFIX),
            sutra.rootMorphology.getValue("अमँ").varnaAgamas.single(),
        )
        assertEquals(UnadiVarnaExpansion(Svara.A, listOf(Svara.AA)), sutra.rootMorphology.getValue("पशि").varnaExpansions.single())
        assertEquals(UnadiVarnaSubstitution(Vyanjana.DHA, Vyanjana.HA), sutra.rootMorphology.getValue("बाधृँ").varnaSubstitutions.single())
    }

    @Test
    fun `sutra 1 28 applies samprasarana to three roots and sa lopa only to bhrasj`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.28" }
        assertEquals(setOf("01.0869", "10.0027", "01.0871", "06.0004"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf("पृथु", "मृदु", "भृगु"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals(UnadiVarnaSubstitution(Vyanjana.RA, Svara.R), sutra.rootMorphology.getValue("प्रथँ").varnaSubstitutions.single())
        assertEquals(UnadiVarnaSubstitution(Vyanjana.RA, Svara.R), sutra.rootMorphology.getValue("म्रदँ").varnaSubstitutions.single())
        val bhrasj = sutra.rootMorphology.getValue("भ्रस्जँ")
        assertEquals(
            listOf(
                UnadiVarnaSubstitution(Vyanjana.RA, Svara.R),
                UnadiVarnaSubstitution(Vyanjana.JA, Vyanjana.GA),
            ),
            bhrasj.varnaSubstitutions,
        )
        assertEquals(listOf(UnadiVarnaLopa(Vyanjana.SA)), bhrasj.varnaLopas)
    }

    @Test
    fun `sutra 1 29 deletes each nasal and keeps raghu as an optional variant`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.29" }
        assertEquals(setOf("01.0113", "01.0663"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf("लघु", "बहु"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        val langhi = sutra.rootMorphology.getValue("लघिँ")
        assertEquals(listOf(UnadiVarnaLopa(Vyanjana.NGA)), langhi.varnaLopas)
        assertEquals(listOf(UnadiVarnaSubstitution(Vyanjana.LA, Vyanjana.RA)), langhi.optionalVarnaSubstitutions)
        val banhi = sutra.rootMorphology.getValue("बहिँ")
        assertEquals(listOf(UnadiVarnaLopa(Ayogavaha.ANUSVARA)), banhi.varnaLopas)
        assertEquals("रघु", sutra.derivedVariants.single().output.toDevanagari())
        assertEquals(Artha.Rudhi("रघु", "a particular king"), sutra.derivedVariants.single().meaning)
    }

    @Test
    fun `sutra 1 30 deletes the nu sequence and derives long-vowel uru`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.30" }
        assertEquals("02.0034", sutra.roots.single().id)
        assertEquals(
            listOf(UnadiVarnaSequenceLopa(listOf(Vyanjana.NNA, Svara.U))),
            sutra.varnaSequenceLopas,
        )
        assertEquals("ऊरु", sutra.rootOutputVarnas.getValue("ऊर्णुञ्").toDevanagari())
        assertEquals(Artha.Rudhi("ऊरु", "thigh"), sutra.meaning)
    }

    @Test
    fun `sutra 1 31 continues nu lopa and shortens uu in the meaning great`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.31" }
        assertEquals("02.0034", sutra.roots.single().id)
        assertEquals(Artha.Rudhi("महत्", "great"), sutra.applicationMeaning)
        assertEquals(UnadiVarnaSubstitution(Svara.UU, Svara.U), sutra.varnaSubstitutions.single())
        assertEquals(listOf(UnadiVarnaSequenceLopa(listOf(Vyanjana.NNA, Svara.U))), sutra.varnaSequenceLopas)
        assertEquals("उरु", sutra.rootOutputVarnas.getValue("ऊर्णुञ्").toDevanagari())
    }

    @Test
    fun `sutra 1 32 selects the attaching slish and replaces final ssa with ka`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.32" }
        assertEquals("04.0083", sutra.roots.single().id)
        assertTrue("आलिङ्गने" in sutra.roots.single().artha)
        assertEquals(UnadiVarnaSubstitution(Vyanjana.SSA, Vyanjana.KA), sutra.varnaSubstitutions.single())
        assertEquals("श्लिकु", sutra.rootOutputVarnas.getValue("श्लिषँ").toDevanagari())
        assertEquals(Artha.Rudhi("श्लिकु", "servant or raised flame"), sutra.meaning)
    }

    @Test
    fun `sutra 1 33 keeps each root paired with its own lexical condition and dit lopa`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.33" }
        assertEquals(setOf("01.1020", "09.0015"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf(ItMarker.KIT, ItMarker.DIT), sutra.itMarkers)

        val khani = sutra.rootMorphology.getValue("खनुँ")
        assertEquals("खन्", khani.conditionedStem?.toDevanagari())
        assertEquals("आ", khani.requiredUpasargas.single().toDevanagari())
        assertEquals(listOf(Svara.A, Vyanjana.NA), khani.varnaSequenceLopas.single().target)
        assertEquals("आखु", sutra.rootOutputVarnas.getValue("खनुँ").toDevanagari())

        val shr = sutra.rootMorphology.getValue("शॄञ्")
        assertEquals("शॄ", shr.conditionedStem?.toDevanagari())
        assertEquals("पर", shr.requiredUpapadas.single().toDevanagari())
        assertEquals(listOf(Svara.RR), shr.varnaSequenceLopas.single().target)
        assertEquals("परशु", sutra.rootOutputVarnas.getValue("शॄञ्").toDevanagari())
        assertEquals("पर्शु", sutra.derivedVariants.single().output.toDevanagari())
        assertEquals("पर", sutra.derivedVariants.single().upapada?.toDevanagari())
    }

    @Test
    fun `sutra 1 34 derives both attested dru compounds from explicit upapadas`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.34" }
        assertEquals("01.1095", sutra.roots.single().id)
        assertEquals(setOf(ItMarker.KIT, ItMarker.DIT), sutra.itMarkers)
        val morphology = sutra.rootMorphology.getValue("द्रु")
        assertEquals(setOf("हरि", "मित"), morphology.requiredUpapadas.map { it.toDevanagari() }.toSet())
        assertEquals(UnadiVarnaLopa(Svara.U), morphology.varnaLopas.single())
        assertEquals(setOf("हरिद्रु", "मितद्रु"), sutra.derivedVariants.map { it.output.toDevanagari() }.toSet())
        assertEquals(
            Artha.Rudhi("हरिद्रु", "tree"),
            sutra.derivedVariants.single { it.output.toDevanagari() == "हरिद्रु" }.meaning,
        )
        assertEquals(
            Artha.Rudhi("मितद्रु", "ocean"),
            sutra.derivedVariants.single { it.output.toDevanagari() == "मितद्रु" }.meaning,
        )
    }

    @Test
    fun `sutra 1 35 separates shata conditioned and bahulaka standalone dru outputs`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.35" }
        assertEquals("01.1095", sutra.roots.single().id)
        assertEquals(setOf(ItMarker.KIT, ItMarker.DIT), sutra.itMarkers)
        assertEquals(UnadiVarnaLopa(Svara.U), sutra.rootMorphology.getValue("द्रु").varnaLopas.single())
        assertEquals(setOf("शतद्रु", "द्रु"), sutra.derivedVariants.map { it.output.toDevanagari() }.toSet())
        val shatadru = sutra.derivedVariants.single { it.output.toDevanagari() == "शतद्रु" }
        assertEquals("शत", shatadru.upapada?.toDevanagari())
        assertEquals(Artha.Explanation("जो सौ प्रकार से बहता है"), shatadru.meaning)
        assertEquals(
            Artha.Rudhi("द्रु", "tree or branch"),
            sutra.derivedVariants.single { it.output.toDevanagari() == "द्रु" }.meaning,
        )
    }

    @Test
    fun `sutra 1 36 preserves five nipatita words their operations and source limits`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.36" }
        assertEquals(setOf("01.1020", "02.0051", "01.0154", "01.0895"), sutra.roots.map { it.id }.toSet())
        assertEquals(
            setOf("खरु", "शङ्कु", "पीयु", "नीलङ्गु", "नीलाङ्गु", "लिगु"),
            sutra.derivedVariants.map { it.output.toDevanagari() }.toSet(),
        )
        assertEquals("खर्", sutra.rootMorphology.getValue("खनुँ").stemAdesha?.toDevanagari())
        val pa = sutra.rootMorphology.getValue("पा")
        assertEquals("पिब्", pa.conditionedStem?.toDevanagari())
        assertEquals("पी", pa.stemAdesha?.toDevanagari())
        assertEquals(Vyanjana.YA, pa.varnaAgamas.single().varnas.single())
        assertEquals("नि", sutra.rootMorphology.getValue("लगिँ").requiredUpasargas.single().toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Svara.A, Svara.I),
            sutra.rootMorphology.getValue("लगेँ").varnaSubstitutions.single(),
        )
        assertEquals(null, sutra.derivedVariants.single { it.output.toDevanagari() == "शङ्कु" }.sourceRoot)
        assertEquals("लगिँ", sutra.derivedVariants.single { it.output.toDevanagari() == "नीलाङ्गु" }.sourceRoot)
    }

    @Test
    fun `sutra 1 37 models attested members while retaining open akritigana status`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.37" }
        assertEquals("02.0044", sutra.roots.single().id)
        assertTrue(sutra.isAkritiGana)
        val morphology = sutra.rootMorphology.getValue("या")
        assertEquals(setOf("मृग", "देव", "मित्र"), morphology.requiredUpapadas.map { it.toDevanagari() }.toSet())
        assertEquals(UnadiVarnaLopa(Svara.AA), morphology.varnaLopas.single())
        assertEquals(setOf("मृगयु", "देवयु", "मित्रयु"), sutra.derivedVariants.map { it.output.toDevanagari() }.toSet())
        assertTrue(sutra.derivedVariants.all { it.sourceRoot == "या" })
        assertEquals(
            Artha.Rudhi("मित्रयु", "one versed in worldly conduct"),
            sutra.derivedVariants.single { it.output.toDevanagari() == "मित्रयु" }.meaning,
        )
    }

    @Test
    fun `sutra 1 38 models urac nipatana and the attested ankura variant`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.38" }
        assertEquals(setOf(ItMarker.CIT), sutra.itMarkers)
        assertEquals("उर", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertTrue(sutra.isNipatita)
        assertEquals(setOf("चङ्कि"), sutra.rootReferences)
        assertEquals(
            setOf("मन्दुरा", "वाशुरा", "मथुरा", "चतुर", "चङ्कुर", "अङ्कुर", "अङ्कूर"),
            sutra.derivedVariants.map { it.output.toDevanagari() }.toSet(),
        )
        assertEquals("मथ्", sutra.rootMorphology.getValue("मथिँ").conditionedStem?.toDevanagari())
        assertEquals("चङ्क्", sutra.rootMorphology.getValue("चङ्कि").conditionedStem?.toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Svara.U, Svara.UU),
            sutra.rootMorphology.getValue("अकिँ").optionalVarnaSubstitutions.single(),
        )
        assertEquals(null, (sutra.derivedVariants.single { it.output.toDevanagari() == "मथुरा" }.meaning as Artha.Rudhi).english)
        assertEquals(Artha.Rudhi("चङ्कुर", "chariot"), sutra.derivedVariants.single { it.output.toDevanagari() == "चङ्कुर" }.meaning)
    }

    @Test
    fun `sutra 1 39 gives vyath samprasarana and makes urac kit`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.39" }
        assertEquals("01.0868", sutra.roots.single().id)
        assertTrue("भयसञ्चलनयोः" in sutra.roots.single().artha)
        assertEquals(setOf(ItMarker.CIT, ItMarker.KIT), sutra.itMarkers)
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.YA, Svara.I),
            sutra.varnaSubstitutions.single(),
        )
        assertEquals("विथुर", sutra.rootOutputVarnas.getValue("व्यथँ").toDevanagari())
        assertEquals(Artha.Rudhi("विथुर", "thief or demon"), sutra.meaning)
    }

    @Test
    fun `sutra 1 40 preserves unsupported mukura root and structured dardura reduplication`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.40" }
        assertEquals("09.0026", sutra.roots.single().id)
        assertTrue("विदारणे" in sutra.roots.single().artha)
        assertEquals(setOf("मुकुर", "मकुर", "दर्दुर"), sutra.derivedVariants.map { it.output.toDevanagari() }.toSet())
        assertEquals(null, sutra.derivedVariants.single { it.output.toDevanagari() == "मुकुर" }.sourceRoot)
        assertEquals("दॄ", sutra.derivedVariants.single { it.output.toDevanagari() == "दर्दुर" }.sourceRoot)
        val reduplication = assertNotNull(sutra.rootMorphology.getValue("दॄ").reduplication)
        assertTrue(reduplication.explicitDve)
        assertEquals("रुक्", reduplication.abhyasaAgamaUpadesha)
        assertEquals(listOf(Vyanjana.RA), reduplication.abhyasaAgamaVarnas)
        assertTrue(reduplication.tiLopa)
        assertEquals("दर्दुर", sutra.rootOutputVarnas.getValue("दॄ").toDevanagari())
    }

    @Test
    fun `sutra 1 41 preserves each nipatita agama and ur uur alternative`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.41" }
        assertTrue(sutra.isNipatita)
        assertEquals(setOf("04.0105", "01.0440", "09.0044", "01.0096"), sutra.roots.map { it.id }.toSet())
        assertEquals(
            setOf("मद्गुर", "कर्बुर", "बन्धुर", "बन्धूर", "कुकुर", "कुक्कुर"),
            sutra.derivedVariants.map { it.output.toDevanagari() }.toSet(),
        )
        val guk = sutra.rootMorphology.getValue("मदीँ").varnaAgamas.single()
        assertEquals("गुक्", guk.upadesha)
        assertEquals(listOf(Vyanjana.GA), guk.varnas)
        val rum = sutra.rootMorphology.getValue("कबृँ").varnaAgamas.single()
        assertEquals("रुम्", rum.upadesha)
        assertEquals(UnadiAgamaPlacement.AFTER_FINAL_ROOT_VOWEL, rum.placement)
        assertEquals(
            UnadiVarnaSubstitution(Svara.U, Svara.UU),
            sutra.rootMorphology.getValue("बन्धँ").optionalVarnaSubstitutions.single(),
        )
        val kuk = sutra.rootMorphology.getValue("कुकँ").optionalVarnaAgamas.single()
        assertEquals("कुक्", kuk.upadesha)
        assertEquals(listOf(Vyanjana.KA), kuk.varnas)
    }

    @Test
    fun `sutra 1 42 separates the uran stem from its secondary taddhita`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.42" }
        assertEquals("04.0106", sutra.roots.single().id)
        assertTrue("क्षेपणे" in sutra.roots.single().artha)
        assertEquals(setOf(ItMarker.NIT), sutra.itMarkers)
        assertEquals("उर", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals("असुर", sutra.rootOutputVarnas.getValue("असुँ").toDevanagari())
        val secondary = sutra.secondaryDerivatives.single()
        assertEquals("असुर", secondary.base.toDevanagari())
        assertEquals("अण्", secondary.affix)
        assertEquals("आसुर", secondary.output.toDevanagari())
    }

    @Test
    fun `sutra 1 43 keeps short uran output distinct from fifth chapter long forms`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.43" }
        assertEquals("04.0131", sutra.roots.single().id)
        assertTrue("परिणामे" in sutra.roots.single().artha)
        assertEquals("मसुर", sutra.rootOutputVarnas.getValue("मसीँ").toDevanagari())
        assertEquals(setOf("5.3"), sutra.relatedSutraNumbers)
        assertEquals(
            setOf("मसुर", "मसुरा", "मसूर", "मसूरा", "मसूरी"),
            sutra.lexicalAttestations.map { it.form.toDevanagari() }.toSet(),
        )
        assertTrue(
            sutra.lexicalAttestations.filter { "ऊ" in it.form.toDevanagari() }
                .all { it.attributedSutra == "5.3" },
        )
        assertEquals(
            setOf(Linga.STRI),
            sutra.lexicalAttestations.single { it.form.toDevanagari() == "मसूरी" }.lingas,
        )
    }

    @Test
    fun `sutra 1 44 models shu formation apta restriction and shvashura`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.44" }
        assertEquals("05.0020", sutra.roots.single().id)
        assertTrue("व्याप्तौ" in sutra.roots.single().artha)
        assertEquals(Artha.Explanation("आप्त अर्थात् वैवाहिक सम्बन्धी"), sutra.applicationMeaning)
        assertEquals("शु", sutra.upapadas.single().toDevanagari())
        val formation = sutra.upapadaFormations.single()
        assertEquals("आशु", formation.source.toDevanagari())
        assertEquals("शु", formation.output.toDevanagari())
        assertEquals(listOf(Svara.AA), formation.initialVarnaLopas)
        assertEquals("श्वशुर", sutra.rootOutputVarnas.getValue("अशूँ").toDevanagari())
        assertEquals(
            setOf("श्वशुर", "श्वश्रू"),
            sutra.lexicalAttestations.map { it.form.toDevanagari() }.toSet(),
        )
        assertEquals(
            setOf(Linga.PUMS),
            sutra.lexicalAttestations.single { it.form.toDevanagari() == "श्वशुर" }.lingas,
        )
    }

    @Test
    fun `sutra 1 45 removes both it markers of tishac and selects attested roots`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.45" }
        assertEquals(setOf("01.0684", "01.0831"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf("अवँ", "महँ"), sutra.roots.map { it.upadesha }.toSet())
        assertEquals(setOf(ItMarker.T, ItMarker.CIT), sutra.itMarkers)
        assertEquals("इष", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals("अविष", sutra.rootOutputVarnas.getValue("अवँ").toDevanagari())
        assertEquals("महिष", sutra.rootOutputVarnas.getValue("महँ").toDevanagari())
        assertEquals(null, (sutra.rootMeaningMap.getValue("महँ") as Artha.Rudhi).english)
    }

    @Test
    fun `sutra 1 46 lengthens am and preserves non feminine lexical usage`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.46" }
        assertEquals(setOf("01.0536", "10.0245"), sutra.roots.map { it.id }.toSet())
        assertEquals(
            UnadiVarnaSubstitution(Svara.A, Svara.AA),
            sutra.varnaSubstitutions.single(),
        )
        assertEquals("आमिष", sutra.rootOutputVarnas.getValue("अमँ").toDevanagari())
        val attestation = sutra.lexicalAttestations.single()
        assertEquals("आमिष", attestation.form.toDevanagari())
        assertEquals(setOf(Linga.PUMS, Linga.NAPUMSAKA), attestation.lingas)
        assertEquals(Artha.Explanation("मांस अथवा भोग्य वस्तु"), attestation.meaning)
    }

    @Test
    fun `sutra 1 47 applies u vrddhi and keeps gender specific rauhisha meanings`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.47" }
        assertEquals("01.0995", sutra.roots.single().id)
        assertEquals(
            UnadiVarnaSubstitution(Svara.U, Svara.AU),
            sutra.varnaSubstitutions.single(),
        )
        assertEquals("रौहिष", sutra.rootOutputVarnas.getValue("रुहँ").toDevanagari())
        assertEquals(2, sutra.lexicalAttestations.size)
        assertEquals(
            setOf(Linga.PUMS, Linga.NAPUMSAKA),
            sutra.lexicalAttestations.flatMap { it.lingas }.toSet(),
        )
        assertEquals(
            setOf(Artha.Explanation("मृग-विशेष"), Artha.Explanation("तृण")),
            sutra.lexicalAttestations.mapNotNull { it.meaning }.toSet(),
        )
        assertTrue(sutra.rootOutputVarnas.values.none { it.toDevanagari() in setOf("रङ्कु", "शम्बर") })
    }

    @Test
    fun `sutra 1 48 models optional nit with both tavisha variants and lexical meanings`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.48" }
        assertTrue(sutra.roots.isEmpty())
        assertEquals(setOf("तव"), sutra.rootReferences)
        assertEquals(setOf(ItMarker.T, ItMarker.CIT), sutra.itMarkers)
        assertEquals(setOf(ItMarker.NIT), sutra.optionalItMarkers)
        assertEquals("इष", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Svara.A, Svara.AA),
            sutra.rootMorphology.getValue("तव").optionalVarnaSubstitutions.single(),
        )
        assertEquals(
            setOf("तविष", "ताविष"),
            sutra.derivedVariants.map { it.output.toDevanagari() }.toSet(),
        )
        assertEquals(
            setOf("तविष", "ताविष", "तविषी", "ताविषी"),
            sutra.lexicalAttestations.map { it.form.toDevanagari() }.toSet(),
        )
        assertEquals(
            setOf(Linga.PUMS, Linga.STRI),
            sutra.lexicalAttestations.flatMap { it.lingas }.toSet(),
        )
        assertTrue(sutra.hindiExplanation.orEmpty().contains("सौत्र धातु"))
        assertTrue(sutra.hindiExplanation.orEmpty().contains("वेदभाष्य"))
    }

    @Test
    fun `sutra 1 49 requires nanj before vyath and preserves gender specific meanings`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.49" }
        assertEquals("01.0868", sutra.roots.single().id)
        assertEquals(setOf("नञ्"), sutra.upapadas.map { it.toDevanagari() }.toSet())
        val formation = sutra.upapadaFormations.single()
        assertEquals("नञ्", formation.source.toDevanagari())
        assertEquals("अ", formation.output.toDevanagari())
        assertEquals(setOf("6.3.73"), sutra.relatedSutraNumbers)
        assertEquals("अव्यथिष", sutra.derivedVariants.single().output.toDevanagari())
        assertEquals(
            setOf("अव्यथिष", "अव्यथिषी"),
            sutra.lexicalAttestations.map { it.form.toDevanagari() }.toSet(),
        )
        assertEquals(
            setOf(Linga.PUMS, Linga.STRI),
            sutra.lexicalAttestations.flatMap { it.lingas }.toSet(),
        )
        assertEquals(
            setOf(Artha.Explanation("समुद्र अथवा सूर्य"), Artha.Explanation("पृथ्वी अथवा रात्रि")),
            sutra.lexicalAttestations.mapNotNull { it.meaning }.toSet(),
        )
        assertTrue(sutra.hindiExplanation.orEmpty().contains("नलोपो नञः"))
    }

    @Test
    fun `sutra 1 50 inserts effective b of buk before tishac`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.50" }
        assertEquals("06.0080", sutra.roots.single().id)
        assertEquals("किल्बिष", sutra.rootOutputVarnas.getValue("किलँ").toDevanagari())
        val agama = sutra.rootMorphology.getValue("किलँ").varnaAgamas.single()
        assertEquals(listOf(Vyanjana.BA), agama.varnas)
        assertEquals(UnadiAgamaPlacement.BEFORE_SUFFIX, agama.placement)
        assertEquals("बुक्", agama.upadesha)
        val attestation = sutra.lexicalAttestations.single()
        assertEquals("किल्बिष", attestation.form.toDevanagari())
        assertEquals(setOf(Linga.NAPUMSAKA), attestation.lingas)
        assertEquals(Artha.Explanation("पाप, रोग अथवा अपराध"), attestation.meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("किल् + ब् + इष"))
    }

    @Test
    fun `sutra 1 51 models all sixteen kirac roots and attested forms`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.51" }
        assertEquals(16, sutra.roots.size)
        assertEquals(setOf(ItMarker.KIT, ItMarker.CIT), sutra.itMarkers)
        assertEquals("इर", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(
            setOf(
                "इषिर", "मदिर", "मुदिर", "खिदिर", "छिदिर", "भिदिर", "मन्दिर", "चन्दिर",
                "तिमिर", "मिहिर", "मुहिर", "मुचिर", "रुचिर", "रुधिर", "बधिर", "शुषिर",
            ),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        assertEquals(
            UnadiVarnaLopa(Vyanjana.NA),
            sutra.rootMorphology.getValue("बन्धँ").varnaLopas.single(),
        )
        assertEquals(
            setOf("मदिरा", "मन्दिरा"),
            sutra.secondaryDerivatives.map { it.output.toDevanagari() }.toSet(),
        )
        assertEquals(17, sutra.lexicalAttestations.size)
        assertEquals(
            Artha.Explanation("अन्धकार अथवा नेत्ररोग"),
            sutra.lexicalAttestations.single { it.form.toDevanagari() == "तिमिर" }.meaning,
        )
        assertEquals(
            Artha.Explanation("छिद्र"),
            sutra.lexicalAttestations.single { it.form.toDevanagari() == "शुषिर" }.meaning,
        )
        assertEquals(null, sutra.lexicalAttestations.single { it.form.toDevanagari() == "रुचिर" }.meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("अर्थ model में अनुमान से नहीं जोड़े गए"))
    }

    @Test
    fun `sutra 1 52 makes kirac nit and derives ashira by vrddhi`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.52" }
        assertEquals("09.0059", sutra.roots.single().id)
        assertEquals(setOf(ItMarker.KIT, ItMarker.CIT, ItMarker.NIT), sutra.itMarkers)
        assertEquals(
            UnadiVarnaSubstitution(Svara.A, Svara.AA),
            sutra.varnaSubstitutions.single(),
        )
        assertEquals("आशिर", sutra.rootOutputVarnas.getValue("अशँ").toDevanagari())
        val attestation = sutra.lexicalAttestations.single()
        assertEquals(setOf(Linga.PUMS), attestation.lingas)
        assertEquals(Artha.Explanation("अग्नि अथवा राक्षस"), attestation.meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("अनिवार्य णित्संज्ञा"))
    }

    @Test
    fun `sutra 1 53 preserves each nipatita branch as explicit varna morphology`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.53" }
        assertTrue(sutra.isNipatita)
        assertEquals(7, sutra.roots.size)
        assertEquals(setOf(ItMarker.KIT, ItMarker.CIT), sutra.itMarkers)
        assertEquals(
            setOf("अजिर", "शिशिर", "शिथिल", "स्फिर", "खदिर", "शिबिर"),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        val shithila = sutra.rootMorphology.getValue("श्रथँ")
        assertEquals(UnadiVarnaLopa(Vyanjana.RA), shithila.varnaLopas.single())
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.RA, Vyanjana.LA),
            shithila.pratyayaVarnaSubstitutions.single(),
        )
        assertEquals(
            "आय्",
            sutra.rootMorphology.getValue("स्फायीँ").varnaSequenceLopas.single().target.toDevanagari(),
        )
        val sthira = sutra.derivedVariants.single { it.output.toDevanagari() == "स्थिर" }
        assertEquals(UnadiVarnaLopa(Svara.AA), sthira.morphology!!.varnaLopas.single())
        val sthavira = sutra.derivedVariants.single { it.output.toDevanagari() == "स्थविर" }
        assertEquals("वुक्", sthavira.morphology!!.varnaAgamas.single().upadesha)
        assertEquals(
            setOf("अजिर", "शिशिर", "शिथिल", "स्थिर", "स्फिर", "स्थविर", "खदिर", "शिबिर"),
            sutra.lexicalAttestations.map { it.form.toDevanagari() }.toSet(),
        )
        assertEquals(null, sutra.lexicalAttestations.single { it.form.toDevanagari() == "खदिर" }.meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("प्रत्यय के र् का लत्त्व"))
    }

    @Test
    fun `sutra 1 54 distinguishes sautra bhad and models ilac vowel operations`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.54" }
        assertEquals(setOf("भड"), sutra.rootReferences)
        assertEquals(setOf(ItMarker.CIT), sutra.itMarkers)
        assertEquals("इल", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals("भडिल", sutra.rootOutputVarnas.getValue("भड").toDevanagari())
        assertEquals("भण्डिल", sutra.rootOutputVarnas.getValue("भडिँ").toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Svara.U, Svara.O),
            sutra.rootMorphology.getValue("कुकँ").varnaSubstitutions.single(),
        )
        assertEquals(
            UnadiVarnaExpansion(Svara.UU, listOf(Svara.A, Vyanjana.VA)),
            sutra.rootMorphology.getValue("भू").varnaExpansions.single(),
        )
        val mahela = sutra.derivedVariants.single { it.output.toDevanagari() == "महेला" }
        assertEquals(
            UnadiVarnaSubstitution(Svara.I, Svara.E),
            mahela.morphology!!.pratyayaVarnaSubstitutions.single(),
        )
        assertEquals("कुटिल", sutra.derivedVariants.single { it.sourceRoot == "कुटँ" }.output.toDevanagari())
        assertEquals(14, sutra.lexicalAttestations.size)
        assertEquals(
            setOf(Artha.Explanation("दूत"), Artha.Explanation("कल्याण")),
            sutra.lexicalAttestations.filter { it.form.toDevanagari() == "भण्डिल" }.mapNotNull { it.meaning }.toSet(),
        )
        assertTrue(sutra.hindiExplanation.orEmpty().contains("ऊ → अव् Varna-expansion"))
    }

    @Test
    fun `sutra 1 55 substitutes p for final m and derives kapila`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.55" }
        assertEquals("01.0511", sutra.roots.single().id)
        assertEquals(setOf(ItMarker.CIT), sutra.itMarkers)
        assertEquals("इल", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.MA, Vyanjana.PA),
            sutra.varnaSubstitutions.single(),
        )
        assertEquals("कपिल", sutra.rootOutputVarnas.getValue("कमुँ").toDevanagari())
        val attestation = sutra.lexicalAttestations.single()
        assertEquals(setOf(Linga.PUMS), attestation.lingas)
        assertEquals(null, attestation.meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("प्-आगम नहीं"))
    }

    @Test
    fun `sutra 1 56 makes ilac kit and blocks guna in gupila and guhila`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.56" }
        assertEquals(setOf("01.0461", "01.1126", "01.1043"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf(ItMarker.CIT, ItMarker.KIT), sutra.itMarkers)
        assertEquals(
            setOf("गुपिल", "तिजिल", "गुहिल"),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        assertEquals(
            setOf(Artha.Explanation("राजा"), Artha.Explanation("चन्द्रमा"), Artha.Explanation("वन")),
            sutra.lexicalAttestations.mapNotNull { it.meaning }.toSet(),
        )
        assertTrue(sutra.hindiExplanation.orEmpty().contains("गोपिल या गोहिल के स्थान पर"))
    }

    @Test
    fun `sutra 1 57 treats pathika as meaning of pathila not invented suffix mutation`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.57" }
        assertTrue(sutra.isNipatita)
        assertEquals(setOf(ItMarker.CIT, ItMarker.KIT), sutra.itMarkers)
        assertEquals("मिथिल", sutra.rootOutputVarnas.getValue("मिथृँ").toDevanagari())
        assertEquals("पथिल", sutra.rootOutputVarnas.getValue("पथेँ").toDevanagari())
        assertEquals("मिथिला", sutra.secondaryDerivatives.single().output.toDevanagari())
        assertEquals(
            setOf("मिथिला", "पथिल"),
            sutra.lexicalAttestations.map { it.form.toDevanagari() }.toSet(),
        )
        assertTrue(sutra.lexicalAttestations.none { it.form.toDevanagari() == "पथिक" })
        assertEquals(
            Artha.Explanation("पथिक अथवा यात्री"),
            sutra.lexicalAttestations.single { it.form.toDevanagari() == "पथिल" }.meaning,
        )
        assertTrue(sutra.hindiExplanation.orEmpty().contains("अप्रमाणित ल् → क् परिवर्तन नहीं गढ़ा गया"))
    }

    @Test
    fun `sutra 1 58 models all six erak outputs and keeps kuthi source only`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.58" }
        assertEquals(setOf("कुठ"), sutra.rootReferences)
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals("एर", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(
            setOf("पतेर", "कठेर", "कुठेर", "गडेर", "गुडेर", "दंशेर"),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        assertEquals("कुठ्", sutra.rootMorphology.getValue("कुठ").conditionedStem!!.toDevanagari())
        assertEquals(6, sutra.lexicalAttestations.size)
        assertEquals(
            Artha.Explanation("कष्ट से जीवन बिताने वाला"),
            sutra.lexicalAttestations.single { it.form.toDevanagari() == "कठेर" }.meaning,
        )
        assertTrue(sutra.lexicalAttestations.none { it.form.toDevanagari() == "छठेर" })
        assertTrue(sutra.hindiExplanation.orEmpty().contains("OCR-दोष"))
    }

    @Test
    fun `sutra 1 59 removes nasal element of kumb and derives kubera`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.59" }
        assertEquals("01.0492", sutra.roots.single().id)
        assertEquals("कुबेर", sutra.rootOutputVarnas.getValue("कुबिँ").toDevanagari())
        val morphology = sutra.rootMorphology.getValue("कुबिँ")
        assertEquals("कुम्ब्", morphology.conditionedStem!!.toDevanagari())
        assertEquals(UnadiVarnaLopa(Vyanjana.MA), morphology.varnaLopas.single())
        val attestation = sutra.lexicalAttestations.single()
        assertEquals(setOf(Linga.PUMS), attestation.lingas)
        assertEquals(null, attestation.meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("म् का Varna-लोप"))
    }

    @Test
    fun `sutra 1 60 substitutes final d with t and derives shatera`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.60" }
        assertEquals("06.0164", sutra.roots.single().id)
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals("एर", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals("शतेर", sutra.rootOutputVarnas.getValue("शदॢँ").toDevanagari())
        val morphology = sutra.rootMorphology.getValue("शदॢँ")
        assertEquals("शद्", morphology.conditionedStem!!.toDevanagari())
        assertEquals(UnadiVarnaSubstitution(Vyanjana.DA, Vyanjana.TA), morphology.varnaSubstitutions.single())
        assertEquals(Artha.Explanation("शत्रु"), sutra.lexicalAttestations.single().meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("द् के स्थान पर त्"))
    }

    @Test
    fun `sutra 1 61 preserves four nipatita erak forms and source meanings`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.61" }
        assertTrue(sutra.isNipatita)
        assertEquals(setOf("10.0092", "04.0014", "01.1043", "04.0095"), sutra.roots.map { it.id }.toSet())
        assertEquals(
            setOf("मूलेर", "गुधेर", "गुहेर", "मुहेर"),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        assertEquals(
            mapOf(
                "मूलेर" to Artha.Explanation("जटा"),
                "गुधेर" to Artha.Explanation("गोप्ता"),
                "गुहेर" to Artha.Explanation("लोहघातक"),
                "मुहेर" to Artha.Explanation("मूर्ख"),
            ),
            sutra.lexicalAttestations.associate { it.form.toDevanagari() to it.meaning },
        )
        assertTrue(sutra.lexicalAttestations.all { it.lingas == setOf(Linga.PUMS) })
        assertTrue(sutra.hindiExplanation.orEmpty().contains("एरगन्ता निपात्यन्ते"))
    }

    @Test
    fun `sutra 1 62 substitutes b with p and derives kapota`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.62" }
        assertEquals("01.0440", sutra.roots.single().id)
        assertEquals("ओत", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals("कपोत", sutra.rootOutputVarnas.getValue("कबृँ").toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.BA, Vyanjana.PA),
            sutra.rootMorphology.getValue("कबृँ").varnaSubstitutions.single(),
        )
        assertEquals(Artha.Explanation("पक्षी"), sutra.lexicalAttestations.single().meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("ब् के स्थान पर प्"))
    }

    @Test
    fun `sutra 1 63 applies dit ti lopa and retains ugit information`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.63" }
        assertEquals("02.0046", sutra.roots.single().id)
        assertEquals(setOf(ItMarker.DIT, ItMarker.U), sutra.itMarkers)
        assertEquals("वत्", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals("भवत्", sutra.rootOutputVarnas.getValue("भा").toDevanagari())
        assertEquals(UnadiVarnaLopa(Svara.AA), sutra.rootMorphology.getValue("भा").varnaLopas.single())
        assertTrue(sutra.hindiExplanation.orEmpty().contains("भवान्"))
        assertTrue(sutra.hindiExplanation.orEmpty().contains("टि"))
    }

    @Test
    fun `sutra 1 64 derives both oran stems without invented meanings`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.64" }
        assertEquals(setOf("01.0385", "01.0098"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf(ItMarker.NIT), sutra.itMarkers)
        assertEquals("ओर", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf("कठोर", "चकोर"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertTrue(sutra.lexicalAttestations.all { it.meaning == null })
        assertTrue(sutra.hindiExplanation.orEmpty().contains("अर्थ अनुमान से नहीं जोड़े गए"))
    }

    @Test
    fun `sutra 1 65 models both explicit final lopa operations for kisora`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.65" }
        assertTrue(sutra.isNipatita)
        assertEquals("09.0021", sutra.roots.single().id)
        val upapada = sutra.upapadaFormations.single()
        assertEquals("किम्", upapada.source.toDevanagari())
        assertEquals("कि", upapada.output.toDevanagari())
        assertEquals(listOf(Vyanjana.MA), upapada.finalVarnaLopas)
        assertEquals(UnadiVarnaLopa(Svara.R), sutra.rootMorphology.getValue("शॄ").varnaLopas.single())
        assertEquals("किशोर", sutra.rootOutputVarnas.getValue("शॄ").toDevanagari())
        assertEquals(setOf("किशोर", "सहोर"), sutra.lexicalAttestations.map { it.form.toDevanagari() }.toSet())
        assertTrue(sutra.hindiExplanation.orEmpty().contains("अन्तिम म्-वर्ण"))
    }

    @Test
    fun `sutra 1 66 models all five olac outputs and nasal lopa`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.66" }
        assertEquals(setOf(ItMarker.CIT), sutra.itMarkers)
        assertEquals("ओल", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf("कपोल", "गडोल", "गण्डोल", "कटोल", "पटोल"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        val kapi = sutra.rootMorphology.getValue("कपिँ")
        assertEquals("कम्प्", kapi.conditionedStem!!.toDevanagari())
        assertEquals(UnadiVarnaLopa(Vyanjana.MA), kapi.varnaLopas.single())
        assertEquals(Artha.Explanation("कटु"), sutra.lexicalAttestations.single { it.form.toDevanagari() == "कटोल" }.meaning)
        assertEquals(null, sutra.lexicalAttestations.single { it.form.toDevanagari() == "पटोल" }.meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("नासिक्य प्रभाव"))
    }

    @Test
    fun `sutra 1 67 expands long i to ay and derives mayura`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.67" }
        assertEquals("09.0004", sutra.roots.single().id)
        assertEquals("ऊर", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals("मयूर", sutra.rootOutputVarnas.getValue("मीञ्").toDevanagari())
        assertEquals(
            UnadiVarnaExpansion(Svara.II, listOf(Svara.A, Vyanjana.YA)),
            sutra.rootMorphology.getValue("मीञ्").varnaExpansions.single(),
        )
        assertEquals(null, sutra.lexicalAttestations.single().meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("ई के अय्-वर्ण-विस्तार"))
    }

    @Test
    fun `sutra 1 68 applies samprasarana and derives sindura`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.68" }
        assertEquals("01.0865", sutra.roots.single().id)
        assertEquals("सिन्दूर", sutra.rootOutputVarnas.getValue("स्यन्दूँ").toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.YA, Svara.I),
            sutra.rootMorphology.getValue("स्यन्दूँ").varnaSubstitutions.single(),
        )
        assertEquals(setOf(Linga.NAPUMSAKA), sutra.lexicalAttestations.single().lingas)
        assertEquals(null, sutra.lexicalAttestations.single().meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("य् को उसके सम्प्रसारण-स्वर इ"))
    }

    @Test
    fun `sutra 1 69 models all eight tun outputs and blocks it agama`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.69" }
        assertTrue(sutra.blocksItAgama)
        assertEquals(setOf(ItMarker.NIT), sutra.itMarkers)
        assertEquals("तु", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(
            setOf("सेतु", "तन्तु", "गन्तु", "मस्तु", "सक्तु", "ओतु", "धातु", "क्रोष्टु"),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        assertEquals(UnadiVarnaSubstitution(Vyanjana.VA, Svara.UU), sutra.rootMorphology.getValue("अवँ").varnaSubstitutions.single())
        assertEquals(
            listOf(
                UnadiVarnaSubstitution(Svara.U, Svara.O),
                UnadiVarnaSubstitution(Vyanjana.SHA, Vyanjana.SSA),
            ),
            sutra.rootMorphology.getValue("क्रुशँ").varnaSubstitutions,
        )
        assertEquals(Artha.Explanation("दधिमण्ड"), sutra.lexicalAttestations.single { it.form.toDevanagari() == "मस्तु" }.meaning)
        assertEquals(null, sutra.lexicalAttestations.single { it.form.toDevanagari() == "धातु" }.meaning)
        assertTrue(setOf("7.2.9", "6.4.20", "7.1.95").all { it in sutra.relatedSutraNumbers })
        assertTrue(sutra.hindiExplanation.orEmpty().contains("आठ धातुओं"))
    }

    @Test
    fun `sutra 1 70 retains inherited tun and adds kit for pitu`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.70" }
        assertEquals("01.1074", sutra.roots.single().id)
        assertEquals(setOf(ItMarker.NIT, ItMarker.KIT), sutra.itMarkers)
        assertTrue(sutra.blocksItAgama)
        assertEquals("पि", sutra.rootMorphology.getValue("पा").conditionedStem!!.toDevanagari())
        assertEquals("पितु", sutra.rootOutputVarnas.getValue("पा").toDevanagari())
        assertEquals(Artha.Explanation("वह्नि अथवा दिवाकर"), sutra.lexicalAttestations.single().meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("अनुमानित आ → इ नियम नहीं गढ़ा गया"))
    }

    @Test
    fun `sutra 1 71 keeps r under kit and derives rtu`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.71" }
        assertEquals("01.1086", sutra.roots.single().id)
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertTrue(sutra.blocksItAgama)
        assertEquals("ऋ", sutra.rootMorphology.getValue("ऋ").conditionedStem!!.toDevanagari())
        assertEquals("ऋतु", sutra.rootOutputVarnas.getValue("ऋ").toDevanagari())
        assertEquals(Artha.Explanation("स्त्रीपुष्प अथवा काल"), sutra.lexicalAttestations.single().meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("कित्त्व के कारण ऋ का गुण नहीं होता"))
    }

    @Test
    fun `sutra 1 72 models seven tu outputs and separates yatu genders`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.72" }
        assertEquals(7, sutra.roots.size)
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertTrue(sutra.blocksItAgama)
        assertEquals(
            setOf("कन्तु", "मन्तु", "जन्तु", "गातु", "भानु", "यातु", "हेतु"),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.TA, Vyanjana.NA),
            sutra.rootMorphology.getValue("भा").pratyayaVarnaSubstitutions.single(),
        )
        assertEquals(UnadiVarnaSubstitution(Svara.I, Svara.E), sutra.rootMorphology.getValue("हि").varnaSubstitutions.single())
        val yatu = sutra.lexicalAttestations.filter { it.form.toDevanagari() == "यातु" }
        assertEquals(setOf(setOf(Linga.PUMS), setOf(Linga.NAPUMSAKA)), yatu.map { it.lingas }.toSet())
        assertEquals(setOf(Artha.Explanation("अध्वग अथवा काल"), Artha.Explanation("रक्षस्")), yatu.mapNotNull { it.meaning }.toSet())
        assertTrue(sutra.hindiExplanation.orEmpty().contains("दोनों लिङ्गार्थ अलग"))
    }

    @Test
    fun `sutra 1 73 applies ki stem substitution before guna`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.73" }
        val morphology = sutra.rootMorphology.getValue("चायृँ")
        assertEquals("चाय्", morphology.conditionedStem!!.toDevanagari())
        assertEquals("कि", morphology.stemAdesha!!.toDevanagari())
        assertEquals(UnadiVarnaSubstitution(Svara.I, Svara.E), morphology.varnaSubstitutions.single())
        assertEquals("केतु", sutra.rootOutputVarnas.getValue("चायृँ").toDevanagari())
        assertEquals(Artha.Explanation("ग्रह अथवा पताका"), sutra.lexicalAttestations.single().meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("कि-आदेश"))
    }

    @Test
    fun `sutra 1 74 shortens aa and derives aptu`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.74" }
        assertEquals("05.0016", sutra.roots.single().id)
        assertEquals(
            UnadiVarnaSubstitution(Svara.AA, Svara.A),
            sutra.rootMorphology.getValue("आपॢँ").varnaSubstitutions.single(),
        )
        assertEquals("अप्तु", sutra.rootOutputVarnas.getValue("आपॢँ").toDevanagari())
        assertEquals(Artha.Explanation("शरीर"), sutra.lexicalAttestations.single().meaning)
    }

    @Test
    fun `sutra 1 75 derives vastu without invented meaning`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.75" }
        assertEquals("02.0013", sutra.roots.single().id)
        assertEquals(setOf(ItMarker.NIT), sutra.itMarkers)
        assertTrue(sutra.blocksItAgama)
        assertEquals("वस्तु", sutra.rootOutputVarnas.getValue("वसँ").toDevanagari())
        assertEquals(setOf(Linga.NAPUMSAKA), sutra.lexicalAttestations.single().lingas)
        assertEquals(null, sutra.lexicalAttestations.single().meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("अर्थ अनुमान से नहीं जोड़े गए"))
    }

    @Test
    fun `sutra 1 76 applies vrddhi and preserves non feminine vastu`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.76" }
        assertEquals(Artha.Explanation("अगार अर्थात् गृह अथवा गृहभूमि"), sutra.applicationMeaning)
        assertEquals(UnadiVarnaSubstitution(Svara.A, Svara.AA), sutra.rootMorphology.getValue("वसँ").varnaSubstitutions.single())
        assertEquals("वास्तु", sutra.rootOutputVarnas.getValue("वसँ").toDevanagari())
        assertEquals(setOf(Linga.PUMS, Linga.NAPUMSAKA), sutra.lexicalAttestations.single().lingas)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("अ-वर्ण की वृद्धि"))
    }

    @Test
    fun `sutra 1 77 removes kit k and derives kratu`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.77" }
        assertEquals("कतु", sutra.pratyaya)
        assertEquals("अतु", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals("क्रतु", sutra.rootOutputVarnas.getValue("कृञ्").toDevanagari())
        assertEquals(Artha.Explanation("यज्ञ"), sutra.lexicalAttestations.single().meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("नियमित यण् र्"))
    }

    @Test
    fun `sutra 1 78 removes cit c and preserves both catuh outputs`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.78" }
        assertEquals("चतु", sutra.pratyaya)
        assertEquals("तु", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.CIT), sutra.itMarkers)
        assertEquals(setOf("एधतु", "वहतु"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals(
            setOf(Artha.Explanation("पुरुष"), Artha.Explanation("अनड्वान् अर्थात् बैल")),
            sutra.lexicalAttestations.mapNotNull { it.meaning }.toSet(),
        )
    }

    @Test
    fun `sutra 1 79 derives jivatu and preserves non feminine gender`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.79" }
        assertEquals("01.0643", sutra.roots.single().id)
        assertEquals("आतु", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertTrue(sutra.itMarkers.isEmpty())
        assertEquals("जीवातु", sutra.rootOutputVarnas.getValue("जीवँ").toDevanagari())
        assertEquals(setOf(Linga.PUMS, Linga.NAPUMSAKA), sutra.lexicalAttestations.single().lingas)
        assertEquals(Artha.Explanation("भक्त, जीवित अथवा जीवनौषध"), sutra.lexicalAttestations.single().meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("स्त्रीलिङ्ग का निषेध"))
    }

    @Test
    fun `sutra 1 80 applies vrddhi and derives jaivatrka`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.80" }
        assertEquals("आतृक", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.NIT), sutra.itMarkers)
        assertEquals(
            UnadiVarnaSubstitution(Svara.II, Svara.AI),
            sutra.rootMorphology.getValue("जीवँ").varnaSubstitutions.single(),
        )
        assertEquals("जैवातृक", sutra.rootOutputVarnas.getValue("जीवँ").toDevanagari())
        assertEquals(Artha.Explanation("इन्दु, भिषक्, आयुष्मान् अथवा कृषीवल"), sutra.lexicalAttestations.single().meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("ई-वर्ण की वृद्धि"))
    }

    @Test
    fun `sutra 1 81 derives all six u forms and keeps karsu meanings separate`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.81" }
        assertEquals(6, sutra.roots.size)
        assertEquals("ऊ", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(
            setOf("कर्षू", "चमू", "तनू", "धनू", "सर्जू", "खर्जू"),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        assertEquals(
            UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA)),
            sutra.rootMorphology.getValue("कृषँ").varnaExpansions.single(),
        )
        val karsu = sutra.lexicalAttestations.filter { it.form.toDevanagari() == "कर्षू" }
        assertEquals(setOf(setOf(Linga.PUMS), setOf(Linga.STRI)), karsu.map { it.lingas }.toSet())
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "चमू" }.meaning == null)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("अर्थ अनुमान से नहीं जोड़े गए"))
    }

    @Test
    fun `sutra 1 82 applies guna expansion and derives marju`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.82" }
        assertEquals("02.0061", sutra.roots.single().id)
        assertEquals(
            UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA)),
            sutra.rootMorphology.getValue("मृजूँ").varnaExpansions.single(),
        )
        assertEquals("मर्जू", sutra.rootOutputVarnas.getValue("मृजूँ").toDevanagari())
        assertEquals(Artha.Explanation("शुद्धिकृत् अर्थात् शुद्ध करने वाला"), sutra.lexicalAttestations.single().meaning)
    }

    @Test
    fun `sutra 1 83 replaces final h with dh and derives vadhu`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.83" }
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.HA, Vyanjana.DHA),
            sutra.rootMorphology.getValue("वहँ").varnaSubstitutions.single(),
        )
        assertEquals("वधू", sutra.rootOutputVarnas.getValue("वहँ").toDevanagari())
        assertEquals(setOf(Linga.STRI), sutra.lexicalAttestations.single().lingas)
        assertEquals(Artha.Explanation("जाया, स्नुषा अथवा स्त्री"), sutra.lexicalAttestations.single().meaning)
    }

    @Test
    fun `sutra 1 84 expands s into cch and derives kacchu`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.84" }
        assertEquals(
            UnadiVarnaExpansion(Vyanjana.SSA, listOf(Vyanjana.CA, Vyanjana.CHA)),
            sutra.rootMorphology.getValue("कषँ").varnaExpansions.single(),
        )
        assertEquals("कच्छू", sutra.rootOutputVarnas.getValue("कषँ").toDevanagari())
        assertEquals(Artha.Explanation("पामा अर्थात् खुजली का रोग"), sutra.lexicalAttestations.single().meaning)
    }

    @Test
    fun `sutra 1 85 models nit vrddhi for all three outputs`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.85" }
        assertEquals(setOf(ItMarker.NIT), sutra.itMarkers)
        assertEquals(4, sutra.roots.size)
        assertEquals(
            setOf("कासू", "पादू", "आरू"),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        assertEquals(
            UnadiVarnaLopa(Ayogavaha.ANUSVARA),
            sutra.rootMorphology.getValue("कसिँ").varnaLopas.single(),
        )
        assertEquals(
            UnadiVarnaExpansion(Svara.R, listOf(Svara.AA, Vyanjana.RA)),
            sutra.rootMorphology.getValue("ऋ").varnaExpansions.single(),
        )
        assertEquals(
            setOf(setOf(Linga.STRI), setOf(Linga.PUMS)),
            sutra.lexicalAttestations.map { it.lingas }.toSet(),
        )
    }

    @Test
    fun `sutra 1 86 inherits nit and replaces n with d`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.86" }
        assertEquals(setOf(ItMarker.NIT), sutra.itMarkers)
        assertEquals(setOf("01.0512", "04.0071"), sutra.roots.map { it.id }.toSet())
        assertEquals(
            listOf(
                UnadiVarnaSubstitution(Svara.A, Svara.AA),
                UnadiVarnaSubstitution(Vyanjana.NNA, Vyanjana.DDA),
            ),
            sutra.rootMorphology.getValue("अणँ").varnaSubstitutions,
        )
        assertEquals("आडू", sutra.rootOutputVarnas.getValue("अणँ").toDevanagari())
        assertEquals(setOf(Linga.NAPUMSAKA), sutra.lexicalAttestations.single().lingas)
    }

    @Test
    fun `sutra 1 87 removes num n and derives alabu under nanj`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.87" }
        assertEquals(setOf(ItMarker.NIT), sutra.itMarkers)
        assertEquals("01.0439", sutra.roots.single().id)
        assertEquals(setOf("नञ्"), sutra.upapadas.map { it.toDevanagari() }.toSet())
        assertEquals("लन्ब्", sutra.rootMorphology.getValue("लबिँ").conditionedStem?.toDevanagari())
        assertEquals(
            UnadiVarnaLopa(Vyanjana.NA),
            sutra.rootMorphology.getValue("लबिँ").varnaLopas.single(),
        )
        assertEquals("अलाबू", sutra.rootOutputVarnas.getValue("लबिँ").toDevanagari())
        assertEquals(setOf(Linga.STRI), sutra.lexicalAttestations.single().lingas)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("नुम् के न्-वर्ण का लोप"))
    }

    @Test
    fun `sutra 1 88 uses erang stem adesha and keeps short u variant distinct`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.88" }
        assertEquals("09.0021", sutra.roots.single().id)
        assertEquals(setOf("क"), sutra.upapadas.map { it.toDevanagari() }.toSet())
        assertEquals("शेर्", sutra.rootMorphology.getValue("शॄ").stemAdesha?.toDevanagari())
        assertEquals("कशेरू", sutra.rootOutputVarnas.getValue("शॄ").toDevanagari())
        assertEquals("कशेरु", sutra.derivedVariants.single().output.toDevanagari())
        assertEquals(
            setOf(Linga.PUMS, Linga.NAPUMSAKA),
            sutra.lexicalAttestations.single { it.form.toDevanagari() == "कशेरु" }.lingas,
        )
        assertTrue(sutra.hindiExplanation.orEmpty().contains("पृथक् derived variant"))
    }

    @Test
    fun `sutra 1 89 records dut agama and derives tardu`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.89" }
        val morphology = sutra.rootMorphology.getValue("तॄ")
        assertEquals(
            UnadiVarnaExpansion(Svara.RR, listOf(Svara.A, Vyanjana.RA)),
            morphology.varnaExpansions.single(),
        )
        assertEquals(listOf(Vyanjana.DA), morphology.varnaAgamas.single().varnas)
        assertEquals(UnadiAgamaPlacement.BEFORE_SUFFIX, morphology.varnaAgamas.single().placement)
        assertEquals("दुट्", morphology.varnaAgamas.single().upadesha)
        assertEquals("तर्दू", sutra.rootOutputVarnas.getValue("तॄ").toDevanagari())
    }

    @Test
    fun `sutra 1 90 deletes i and aa and derives dardru`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.90" }
        assertEquals(
            listOf(UnadiVarnaLopa(Svara.I), UnadiVarnaLopa(Svara.AA)),
            sutra.rootMorphology.getValue("दरिद्रा").varnaLopas,
        )
        assertEquals("दर्द्रू", sutra.rootOutputVarnas.getValue("दरिद्रा").toDevanagari())
        assertEquals(Artha.Explanation("कुष्ठप्रभेद"), sutra.lexicalAttestations.single().meaning)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("दोनों पृथक् Varna-लोपों"))
    }

    @Test
    fun `sutra 1 91 removes kit k and derives both ku outputs`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.91" }
        assertEquals("कू", sutra.pratyaya)
        assertEquals("ऊ", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals(setOf("04.0010", "01.0864"), sutra.roots.map { it.id }.toSet())
        assertEquals(
            setOf("नृतू", "शृधू"),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        assertEquals(setOf(Linga.PUMS), sutra.lexicalAttestations.single { it.form.toDevanagari() == "नृतू" }.lingas)
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "शृधू" }.lingas.isEmpty())
        assertTrue(sutra.hindiExplanation.orEmpty().contains("लिङ्ग अनुमान से नहीं जोड़ा गया"))
    }

    @Test
    fun `sutra 1 92 preserves sautra root and models am agama`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.92" }
        assertTrue(sutra.roots.isEmpty())
        assertEquals(setOf("ऋति"), sutra.rootReferences)
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        val morphology = sutra.rootMorphology.getValue("ऋति")
        assertEquals("ऋत्", morphology.conditionedStem?.toDevanagari())
        assertEquals("अम्", morphology.varnaAgamas.single().varnas.toDevanagari())
        assertEquals(UnadiAgamaPlacement.AFTER_FINAL_ROOT_VOWEL, morphology.varnaAgamas.single().placement)
        assertEquals("अम्", morphology.varnaAgamas.single().upadesha)
        assertEquals(
            listOf(
                UnadiVarnaSubstitution(Svara.R, Vyanjana.RA),
                UnadiVarnaSubstitution(Vyanjana.MA, Vyanjana.NA),
            ),
            morphology.varnaSubstitutions,
        )
        assertEquals("रन्तू", sutra.rootOutputVarnas.getValue("ऋति").toDevanagari())
        assertEquals(
            setOf(setOf(Linga.STRI), setOf(Linga.PUMS)),
            sutra.lexicalAttestations.map { it.lingas }.toSet(),
        )
        assertTrue(sutra.hindiExplanation.orEmpty().contains("ऋतेः अम्"))
    }

    @Test
    fun `sutra 1 93 preserves nipatita main and alternate readings`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.93" }
        assertTrue(sutra.isNipatita)
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals(9, sutra.roots.size)
        assertEquals(
            setOf("अन्दू", "दृम्भू", "जम्बू", "कफेलू", "कर्कन्धू", "दिधिषू", "दृम्फू", "कम्बू"),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        assertEquals(
            setOf("दृन्भू", "जम्बु", "दृम्फू", "कम्बू"),
            sutra.derivedVariants.map { it.output.toDevanagari() }.toSet(),
        )
        val drbhi = sutra.rootMorphology.getValue("दृभीँ")
        assertEquals("नुम्", drbhi.varnaAgamas.single().upadesha)
        assertEquals(UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.MA), drbhi.varnaSubstitutions.single())
        val jani = sutra.rootMorphology.getValue("जनीँ")
        assertEquals("बुक्", jani.varnaAgamas.single().upadesha)
        assertEquals(listOf(Vyanjana.BA), jani.varnaAgamas.single().varnas)
        assertEquals("कफम्", sutra.upapadaFormations.single().source.toDevanagari())
        assertEquals("कफ", sutra.upapadaFormations.single().output.toDevanagari())
        assertTrue(sutra.hindiExplanation.orEmpty().contains("कृत्रिम आदेश नहीं गढ़े गए"))
    }

    @Test
    fun `sutra 1 94 derives marut and garut from uti`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.94" }
        assertEquals("उति", sutra.pratyaya)
        assertEquals("उत्", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.I), sutra.itMarkers)
        assertEquals(setOf("मरुत्", "गरुत्"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals(
            UnadiVarnaExpansion(Svara.RR, listOf(Svara.A, Vyanjana.RA)),
            sutra.rootMorphology.getValue("गॄ").varnaExpansions.single(),
        )
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "मरुत्" }.meaning == null)
    }

    @Test
    fun `sutra 1 95 inserts mut before uti`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.95" }
        val agama = sutra.rootMorphology.getValue("गॄ").varnaAgamas.single()
        assertEquals("मुट्", agama.upadesha)
        assertEquals(listOf(Vyanjana.MA), agama.varnas)
        assertEquals(UnadiAgamaPlacement.BEFORE_SUFFIX, agama.placement)
        assertEquals("गर्मुत्", sutra.rootOutputVarnas.getValue("गॄ").toDevanagari())
        assertTrue(sutra.lexicalAttestations.single().lingas.isEmpty())
    }

    @Test
    fun `sutra 1 96 derives harsula and preserves bahulaka catula`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.96" }
        assertEquals("उल", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.CIT), sutra.itMarkers)
        assertEquals("हर्षुल", sutra.rootOutputVarnas.getValue("हृषँ").toDevanagari())
        assertEquals("चटुल", sutra.rootOutputVarnas.getValue("चटेँ").toDevanagari())
        assertEquals("चटुल", sutra.derivedVariants.single().output.toDevanagari())
        assertEquals(setOf(Linga.NAPUMSAKA), sutra.lexicalAttestations.single { it.form.toDevanagari() == "चटुल" }.lingas)
    }

    @Test
    fun `sutra 1 97 derives four iti outputs and preserves sautra yus`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.97" }
        assertEquals("इति", sutra.pratyaya)
        assertEquals("इत्", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.I), sutra.itMarkers)
        assertEquals(setOf("युष्"), sutra.rootReferences)
        assertEquals(4, sutra.roots.size)
        assertEquals(
            setOf("हरित्", "सरित्", "रोहित्", "योषित्"),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        assertEquals(UnadiVarnaSubstitution(Svara.U, Svara.O), sutra.rootMorphology.getValue("रुहँ").varnaSubstitutions.single())
        assertEquals(setOf(Linga.STRI), sutra.lexicalAttestations.single { it.form.toDevanagari() == "योषित्" }.lingas)
        assertTrue(sutra.hindiExplanation.orEmpty().contains("बिना मनमाना विस्तार"))
    }

    @Test
    fun `sutra 1 98 explicitly models nic requirement and luk`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.98" }
        assertEquals("10.0064", sutra.roots.single().id)
        val morphology = sutra.rootMorphology.getValue("तडँ")
        assertEquals(setOf(SanadiAffix.NIC), morphology.requiredSanadiAffixes)
        assertEquals(setOf(SanadiAffix.NIC), morphology.lopaSanadiAffixes)
        assertEquals("तडित्", sutra.rootOutputVarnas.getValue("तडँ").toDevanagari())
        assertTrue(sutra.lexicalAttestations.single().meaning == null)
        assertTrue(sutra.lexicalAttestations.single().lingas.isEmpty())
        val lopaField = kotlin.test.assertIs<dev.panini.sutra.runtime.SutraArthaValue.Sequence>(
            kotlin.test.assertIs<dev.panini.sutra.runtime.SutraArthaValue.Record>(
                kotlin.test.assertIs<dev.panini.sutra.runtime.SutraArthaValue.Record>(
                    sutra.artha.fields.getValue("rootMorphology"),
                ).fields.getValue("तडँ"),
            ).fields.getValue("lopaSanadiAffixes"),
        )
        assertEquals(listOf(dev.panini.sutra.runtime.SutraArthaValue.Text("णिच्")), lopaField.values)
    }

    @Test
    fun `sutra 1 99 applies retroflex parasavarna without confusing kliba with gender`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.99" }
        assertEquals(UnadiVarnaSubstitution(Vyanjana.MA, Vyanjana.NNA), sutra.rootMorphology.getValue("शमुँ").varnaSubstitutions.single())
        assertEquals("शण्ढ", sutra.rootOutputVarnas.getValue("शमुँ").toDevanagari())
        assertTrue(sutra.lexicalAttestations.all { it.lingas == setOf(Linga.PUMS) })
        assertEquals(setOf(Artha.Explanation("गोपति"), Artha.Explanation("क्लीब")), sutra.lexicalAttestations.mapNotNull { it.meaning }.toSet())
    }

    @Test
    fun `sutra 1 100 keeps kamatha genders and unsupported jaratha derivation distinct`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.100" }
        assertEquals("कमठ", sutra.rootOutputVarnas.getValue("कमुँ").toDevanagari())
        assertEquals(setOf(setOf(Linga.PUMS), setOf(Linga.NAPUMSAKA)), sutra.lexicalAttestations.filter { it.form.toDevanagari() == "कमठ" }.map { it.lingas }.toSet())
        assertEquals("जरठ", sutra.derivedVariants.single().output.toDevanagari())
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "जरठ" }.meaning == null)
    }

    @Test
    fun `sutra 1 101 applies explicit vrddhi and derives ramatha`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.101" }
        assertEquals(UnadiVarnaSubstitution(Svara.A, Svara.AA), sutra.rootMorphology.getValue("रमुँ").varnaSubstitutions.single())
        assertEquals("रामठ", sutra.rootOutputVarnas.getValue("रमुँ").toDevanagari())
        assertEquals(setOf(Linga.NAPUMSAKA), sutra.lexicalAttestations.single().lingas)
        assertEquals(Artha.Explanation("हिङ्गु"), sutra.lexicalAttestations.single().meaning)
    }

    @Test
    fun `sutras 1 102 and 1 103 preserve their distinct retroflex formations`() {
        val sankha = UnadiPatha.sutras.single { it.number == "1.102" }
        assertEquals(UnadiVarnaSubstitution(Vyanjana.MA, Vyanjana.NGA), sankha.rootMorphology.getValue("शमुँ").varnaSubstitutions.single())
        assertEquals("शङ्ख", sankha.rootOutputVarnas.getValue("शमुँ").toDevanagari())
        val kantha = UnadiPatha.sutras.single { it.number == "1.103" }
        assertEquals(setOf("01.0517", "01.0903"), kantha.roots.map { it.id }.toSet())
        assertEquals("कण्ठ", kantha.rootOutputVarnas.getValue("कणँ").toDevanagari())
        assertTrue(kantha.lexicalAttestations.single().meaning == null)
    }

    @Test
    fun `sutra 1 104 removes kit k and preserves both attested feminine outputs`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.104" }
        assertEquals("कल", sutra.pratyaya)
        assertEquals("अल", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals(setOf("06.0028", "06.0030"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf("तृपला", "त्रिफला"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertTrue(sutra.lexicalAttestations.all { it.lingas == setOf(Linga.STRI) })
        assertTrue(sutra.hindiExplanation.orEmpty().contains("काल्पनिक संप्रसारण अथवा वर्णादेश नहीं गढ़ा गया"))
    }

    @Test
    fun `sutra 1 105 replaces final p with b and derives sabala`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.105" }
        assertEquals(setOf("01.1155", "04.0064"), sutra.roots.map { it.id }.toSet())
        assertEquals(UnadiVarnaSubstitution(Vyanjana.PA, Vyanjana.BA), sutra.rootMorphology.getValue("शपँ").varnaSubstitutions.single())
        assertEquals("शबल", sutra.rootOutputVarnas.getValue("शपँ").toDevanagari())
        assertTrue(sutra.lexicalAttestations.single().meaning == null)
        assertTrue(sutra.lexicalAttestations.single().lingas.isEmpty())
    }

    @Test
    fun `sutra 1 106 structures cit guna buk vrddhi and mut branches`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.106" }
        assertEquals("वृषादिभ्यश्चित्", sutra.text)
        assertEquals("कल", sutra.pratyaya)
        assertEquals("अल", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.KIT, ItMarker.CIT), sutra.itMarkers)
        assertEquals(
            setOf("01.0803", "01.0973", "01.1085", "01.1124", "01.0511", "04.0130", "01.0154"),
            sutra.roots.map { it.id }.toSet(),
        )
        assertEquals(setOf("कुटि", "कुडि", "कशि", "कौति"), sutra.rootReferences)
        assertEquals(
            setOf("वृषल", "पलल", "सरल", "तरल", "कम्बल", "मुसल", "लाङ्गल", "कुट्मल", "कुड्मल", "कश्मल", "कोमल"),
            sutra.derivedVariants.map { it.output.toDevanagari() }.toSet(),
        )
        assertEquals("बुक्", sutra.rootMorphology.getValue("कमुँ").varnaAgamas.single().upadesha)
        listOf("कुटि", "कुडि", "कशि", "कौति").forEach { root ->
            assertEquals("मुट्", sutra.rootMorphology.getValue(root).varnaAgamas.single().upadesha)
        }
        assertEquals(
            UnadiVarnaSubstitution(Svara.A, Svara.AA),
            sutra.rootMorphology.getValue("लगिँ").varnaSubstitutions.single(),
        )
        assertTrue(sutra.lexicalAttestations.all { it.meaning == null })
        assertTrue(sutra.hindiExplanation.orEmpty().contains("गणसूत्र १९६"))
        assertTrue(sutra.hindiExplanation.orEmpty().contains("गणसूत्र १९८"))
    }

    @Test
    fun `sutras 1 107 through 1 110 preserve each stated operation`() {
        val mrj = UnadiPatha.sutras.single { it.number == "1.107" }
        assertEquals("मल", mrj.rootOutputVarnas.getValue("मृजूँ").toDevanagari())
        assertEquals(
            listOf(Svara.R, Vyanjana.JA),
            mrj.rootMorphology.getValue("मृजूँ").varnaSequenceLopas.single().target,
        )

        val cup = UnadiPatha.sutras.single { it.number == "1.108" }
        assertEquals("चपल", cup.rootOutputVarnas.getValue("चुपँ").toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Svara.U, Svara.A),
            cup.rootMorphology.getValue("चुपँ").varnaSubstitutions.single(),
        )

        val sakSam = UnadiPatha.sutras.single { it.number == "1.109" }
        assertEquals(setOf(ItMarker.KIT, ItMarker.NIT), sakSam.itMarkers)
        assertEquals(setOf("शकल", "शमल"), sakSam.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())

        val cho = UnadiPatha.sutras.single { it.number == "1.110" }
        assertEquals("छगल", cho.rootOutputVarnas.getValue("छो").toDevanagari())
        assertEquals("गुक्", cho.rootMorphology.getValue("छो").varnaAgamas.single().upadesha)
        assertEquals(
            UnadiVarnaSubstitution(Svara.O, Svara.A),
            cho.rootMorphology.getValue("छो").varnaSubstitutions.single(),
        )
        assertEquals("छागल", cho.secondaryDerivatives.single().output.toDevanagari())
        assertTrue(cho.hindiExplanation.orEmpty().contains("secondary derivative"))
    }

    @Test
    fun `sutra 1 111 preserves nyamanta outputs sabhava and source meanings`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.111" }
        assertEquals("ड", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(
            setOf("दण्ड", "रण्डा", "खण्ड", "भण्ड", "वण्ड", "अण्ड", "षण्ड", "शण्ड", "गण्ड", "चण्ड", "पण्ड", "पण्डा"),
            sutra.derivedVariants.map { it.output.toDevanagari() }.toSet(),
        )
        assertEquals(
            listOf(
                UnadiVarnaSubstitution(Vyanjana.SSA, Vyanjana.SA),
                UnadiVarnaSubstitution(Vyanjana.SA, Vyanjana.SSA),
            ),
            sutra.rootMorphology.getValue("षणु").varnaSubstitutions,
        )
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.SSA, Vyanjana.SHA),
            sutra.derivedVariants.single { it.output.toDevanagari() == "शण्ड" }.morphology!!.varnaSubstitutions.single(),
        )
        val panda = sutra.lexicalAttestations.single { it.form.toDevanagari() == "पण्ड" }
        assertEquals(setOf(Linga.PUMS), panda.lingas)
        assertEquals(Artha.Explanation("क्लीब"), panda.meaning)
        assertEquals(
            Artha.Explanation("बुद्धि"),
            sutra.lexicalAttestations.single { it.form.toDevanagari() == "पण्डा" }.meaning,
        )
    }

    @Test
    fun `sutra 1 112 applies kit da to the attested kavarga roots`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.112" }
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals(setOf("06.0061", "01.0511", "01.1100", "06.0064"), sutra.roots.map { it.id }.toSet())
        assertEquals(
            setOf("कुण्ड", "काण्ड", "गुड", "घुण्ड"),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        assertEquals(
            listOf(
                UnadiVarnaSubstitution(Svara.A, Svara.AA),
                UnadiVarnaSubstitution(Vyanjana.MA, Vyanjana.NNA),
            ),
            sutra.rootMorphology.getValue("कमुँ").varnaSubstitutions,
        )
        assertEquals(
            Artha.Explanation("भ्रमर"),
            sutra.lexicalAttestations.single { it.form.toDevanagari() == "घुण्ड" }.meaning,
        )
        assertTrue(sutra.lexicalAttestations.filter { it.form.toDevanagari() != "घुण्ड" }.all { it.meaning == null })
    }

    @Test
    fun `sutra 1 113 assigns a distinct suffix to each root`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.113" }
        assertEquals(
            mapOf("ष्ठा" to "आलच्", "चतेँ" to "वालच्", "मृजूँ" to "आलीयच्"),
            sutra.rootPratyayaMap.mapValues { it.value.upadesha },
        )
        assertEquals(
            mapOf("ष्ठा" to "आल", "चतेँ" to "वाल", "मृजूँ" to "आलीय"),
            sutra.rootPratyayaMap.mapValues { it.value.surface },
        )
        val matches = sutra.roots.associate { root -> root.upadesha to sutra.matchFor(root) }
        assertEquals("आलच्", matches.getValue("ष्ठा").pratyaya)
        assertEquals("वाल", matches.getValue("चतेँ").pratyayaSurfaceVarnas.toDevanagari())
        assertEquals("आलीयच्", matches.getValue("मृजूँ").pratyaya)
        assertEquals(
            setOf("स्थाल", "स्थाली", "चत्वाल", "मार्जालीय"),
            sutra.derivedVariants.map { it.output.toDevanagari() }.toSet(),
        )
        assertEquals(
            UnadiVarnaExpansion(Svara.R, listOf(Svara.AA, Vyanjana.RA)),
            sutra.rootMorphology.getValue("मृजूँ").varnaExpansions.single(),
        )
        assertEquals(
            Artha.Explanation("बिडाल"),
            sutra.lexicalAttestations.single { it.form.toDevanagari() == "मार्जालीय" }.meaning,
        )
    }

    @Test
    fun `sutra 1 114 keeps candala primary and candala with vrddhi secondary`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.114" }
        assertEquals("आलञ्", sutra.pratyaya)
        assertEquals("आल", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.NYIT), sutra.itMarkers)
        assertEquals(setOf("01.0979", "01.0312"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf("पाताल", "चण्डाल"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals(
            UnadiVarnaSubstitution(Svara.A, Svara.AA),
            sutra.rootMorphology.getValue("पतॢँ").varnaSubstitutions.single(),
        )
        assertEquals("चण्डाल", sutra.secondaryDerivatives.single().base.toDevanagari())
        assertEquals("चाण्डाल", sutra.secondaryDerivatives.single().output.toDevanagari())
        assertEquals("प्रज्ञादि-अण्", sutra.secondaryDerivatives.single().affix)
        assertTrue(sutra.lexicalAttestations.all { it.meaning == null })
    }

    @Test
    fun `sutra 1 115 models all eight kalan formations without invented meanings`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.115" }
        assertEquals("कालन्", sutra.pratyaya)
        assertEquals("आल", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.KIT, ItMarker.N), sutra.itMarkers)
        assertEquals(8, sutra.roots.size)
        assertEquals(
            setOf("तमाल", "विशाल", "बिडाल", "मृणाल", "कुलाल", "कपाल", "पलाल", "पञ्चाल"),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        assertEquals("कप्", sutra.rootMorphology.getValue("कपिँ").conditionedStem!!.toDevanagari())
        assertTrue(sutra.rootMorphology.getValue("कपिँ").varnaLopas.isEmpty())
        assertTrue(sutra.lexicalAttestations.all { it.meaning == null })
        assertEquals(
            setOf(Linga.NAPUMSAKA),
            sutra.lexicalAttestations.single { it.form.toDevanagari() == "कपाल" }.lingas,
        )
    }

    @Test
    fun `sutra 1 116 restricts angac to the bird meaning`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.116" }
        assertEquals("अङ्गच्", sutra.pratyaya)
        assertEquals("अङ्ग", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.C), sutra.itMarkers)
        assertEquals("पतङ्ग", sutra.rootOutputVarnas.getValue("पतॢँ").toDevanagari())
        assertEquals(Artha.Explanation("पक्षी के अर्थ में"), sutra.applicationMeaning)
        assertEquals(Artha.Explanation("पक्षी"), sutra.lexicalAttestations.single().meaning)
        assertEquals(setOf(Linga.PUMS), sutra.lexicalAttestations.single().lingas)

        val heterogeneous = UnadiPatha.sutras.single { it.number == "1.113" }
        assertTrue(heterogeneous.rootPratyayaMap.values.all { it.itMarkers == setOf(ItMarker.C) })
    }

    @Test
    fun `sutra 1 117 gives guna expansions for taranga and lavanga`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.117" }
        assertEquals(setOf("01.1124", "09.0016"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf("तरङ्ग", "लवङ्ग"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals(
            UnadiVarnaExpansion(Svara.RR, listOf(Svara.A, Vyanjana.RA)),
            sutra.rootMorphology.getValue("तॄ").varnaExpansions.single(),
        )
        assertEquals(
            UnadiVarnaExpansion(Svara.UU, listOf(Svara.A, Vyanjana.VA)),
            sutra.rootMorphology.getValue("लूञ्").varnaExpansions.single(),
        )
        assertTrue(sutra.lexicalAttestations.all { it.meaning == null })
    }

    @Test
    fun `sutra 1 118 makes angac kit and records bahulaka utva`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.118" }
        assertEquals(setOf(ItMarker.KIT, ItMarker.C), sutra.itMarkers)
        assertEquals(setOf("विड"), sutra.rootReferences)
        assertEquals(setOf("09.0051", "08.0010"), sutra.roots.map { it.id }.toSet())
        assertEquals(
            setOf("विडङ्ग", "मृदङ्ग", "कुरङ्ग"),
            sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet(),
        )
        assertEquals(
            UnadiVarnaExpansion(Svara.R, listOf(Svara.U, Vyanjana.RA)),
            sutra.rootMorphology.getValue("कृ").varnaExpansions.single(),
        )
        assertTrue(sutra.lexicalAttestations.all { it.meaning == null && it.lingas == setOf(Linga.PUMS) })
    }

    @Test
    fun `sutra 1 119 applies vrddhi separately to sr and vr`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.119" }
        assertEquals(setOf("01.1085", "05.0008"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf(ItMarker.C), sutra.itMarkers)
        assertEquals(setOf("सारङ्ग", "वारङ्ग"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        listOf("सृ", "वृञ्").forEach { root ->
            assertEquals(
                UnadiVarnaExpansion(Svara.R, listOf(Svara.AA, Vyanjana.RA)),
                sutra.rootMorphology.getValue(root).varnaExpansions.single(),
            )
        }
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "सारङ्ग" }.meaning == null)
        assertEquals(
            Artha.Explanation("खड्गादि की मुष्टि"),
            sutra.lexicalAttestations.single { it.form.toDevanagari() == "वारङ्ग" }.meaning,
        )
    }

    @Test
    fun `sutra 1 120 models gan final n lopa and homorganic nasal`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.120" }
        assertEquals("गन्", sutra.pratyaya)
        assertEquals("ग", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.N), sutra.itMarkers)
        assertEquals(setOf("01.1137", "02.0001"), sutra.roots.map { it.id }.toSet())
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.MA, Vyanjana.NGA),
            sutra.rootMorphology.getValue("गमॢँ").varnaSubstitutions.single(),
        )
        assertEquals(setOf("गङ्गा", "अद्ग"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "गङ्गा" }.meaning == null)
        assertEquals(
            Artha.Explanation("पुरोडाश"),
            sutra.lexicalAttestations.single { it.form.toDevanagari() == "अद्ग" }.meaning,
        )
    }

    @Test
    fun `sutra 1 121 preserves kit regular roots and bahulaka sit branch`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.121" }
        assertEquals(setOf(ItMarker.KIT, ItMarker.N), sutra.itMarkers)
        assertEquals(setOf("04.0041", "01.1121", "09.0014", "10.0065", "01.0341"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf("छाग", "पूग", "खड्ग", "षिड्ग"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals(
            UnadiVarnaSubstitution(Svara.O, Svara.AA),
            sutra.rootMorphology.getValue("छो").varnaSubstitutions.single(),
        )
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.TTA, Vyanjana.DDA),
            sutra.rootMorphology.getValue("षिटँ").varnaSubstitutions.single(),
        )
        assertEquals(
            Artha.Explanation("तरल"),
            sutra.lexicalAttestations.single { it.form.toDevanagari() == "षिड्ग" }.meaning,
        )
        assertTrue(sutra.lexicalAttestations.filter { it.form.toDevanagari() != "षिड्ग" }.all { it.meaning == null })
    }

    @Test
    fun `sutra 1 122 models nut and its homorganic nasal result`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.122" }
        assertEquals(setOf("01.1045"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf(ItMarker.KIT, ItMarker.N), sutra.itMarkers)
        assertEquals("भृङ्ग", sutra.rootOutputVarnas.getValue("भृञ्").toDevanagari())
        val morphology = sutra.rootMorphology.getValue("भृञ्")
        assertEquals("नुट्", morphology.varnaAgamas.single().upadesha)
        assertEquals(listOf(Vyanjana.NA), morphology.varnaAgamas.single().varnas)
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.NGA),
            morphology.varnaSubstitutions.single(),
        )
        assertEquals(
            Artha.Explanation("षिड्ग, अलि और धूम्याट का पर्याय"),
            sutra.lexicalAttestations.single().meaning,
        )
        assertTrue(sutra.derivedVariants.isEmpty())
    }

    @Test
    fun `sutra 1 123 inherits kit and nut and makes long r short`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.123" }
        assertEquals(setOf("09.0015"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf(ItMarker.KIT, ItMarker.N), sutra.itMarkers)
        assertEquals("शृङ्ग", sutra.rootOutputVarnas.getValue("शॄञ्").toDevanagari())
        val morphology = sutra.rootMorphology.getValue("शॄञ्")
        assertEquals("नुट्", morphology.varnaAgamas.single().upadesha)
        assertEquals(
            listOf(
                UnadiVarnaSubstitution(Svara.RR, Svara.R),
                UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.NGA),
            ),
            morphology.varnaSubstitutions,
        )
        assertEquals(setOf(Linga.NAPUMSAKA), sutra.lexicalAttestations.single().lingas)
        assertTrue(sutra.lexicalAttestations.single().meaning == null)
    }

    @Test
    fun `sutra 1 124 uses nit gan with inherited nut in bird meaning`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.124" }
        assertEquals("गण्", sutra.pratyaya)
        assertEquals("ग", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.NIT), sutra.itMarkers)
        assertEquals(Artha.Explanation("शकुन अर्थात् पक्षी के अर्थ में"), sutra.applicationMeaning)
        assertEquals("शार्ङ्ग", sutra.rootOutputVarnas.getValue("शॄञ्").toDevanagari())
        val morphology = sutra.rootMorphology.getValue("शॄञ्")
        assertEquals("नुट्", morphology.varnaAgamas.single().upadesha)
        assertEquals(
            UnadiVarnaExpansion(Svara.RR, listOf(Svara.AA, Vyanjana.RA)),
            morphology.varnaExpansions.single(),
        )
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.NGA),
            morphology.varnaSubstitutions.single(),
        )
        assertEquals(Artha.Explanation("पक्षी"), sutra.lexicalAttestations.single().meaning)
    }

    @Test
    fun `sutra 1 125 keeps gak and ga distinct per root`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.125" }
        assertEquals(
            mapOf("मुदँ" to "गक्", "गॄ" to "ग"),
            sutra.rootPratyayaMap.mapValues { it.value.upadesha },
        )
        assertEquals(
            mapOf("मुदँ" to setOf(ItMarker.KIT), "गॄ" to emptySet()),
            sutra.rootPratyayaMap.mapValues { it.value.itMarkers },
        )
        assertEquals(
            mapOf("मुदँ" to "मुद्ग", "गॄ" to "गर्ग"),
            sutra.rootOutputVarnas.mapValues { it.value.toDevanagari() },
        )
        assertEquals(
            UnadiVarnaExpansion(Svara.RR, listOf(Svara.A, Vyanjana.RA)),
            sutra.rootMorphology.getValue("गॄ").varnaExpansions.single(),
        )
        assertTrue(sutra.lexicalAttestations.all { it.lingas == setOf(Linga.PUMS) })
        assertTrue(sutra.lexicalAttestations.all { it.meaning == null })
    }

    @Test
    fun `sutra 1 126 applies andan with guna to four r roots`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.126" }
        assertEquals("अण्डन्", sutra.pratyaya)
        assertEquals("अण्ड", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.N), sutra.itMarkers)
        assertEquals(setOf("08.0010", "01.1085", "01.1045", "05.0008"), sutra.roots.map { it.id }.toSet())
        assertEquals(
            mapOf("डुकृञ्" to "करण्ड", "सृ" to "सरण्ड", "भृञ्" to "भरण्ड", "वृञ्" to "वरण्ड"),
            sutra.rootOutputVarnas.mapValues { it.value.toDevanagari() },
        )
        sutra.rootMorphology.values.forEach { morphology ->
            assertEquals(
                UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA)),
                morphology.varnaExpansions.single(),
            )
        }
        assertEquals(
            mapOf("सरण्ड" to "पक्षी", "भरण्ड" to "स्वामी", "वरण्ड" to "मुखरोग"),
            sutra.lexicalAttestations.mapNotNull { attestation ->
                val meaning = attestation.meaning as? Artha.Explanation
                meaning?.let { attestation.form.toDevanagari() to it.hindi }
            }.toMap(),
        )
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "करण्ड" }.meaning == null)
    }

    @Test
    fun `sutra 1 127 preserves base forms behind commentary sandhi`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.127" }
        assertEquals("अदि", sutra.pratyaya)
        assertEquals("अद्", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.I), sutra.itMarkers)
        assertEquals(setOf("09.0015", "09.0026", "03.0019"), sutra.roots.map { it.id }.toSet())
        assertEquals(
            mapOf("शॄञ्" to "शरत्", "दॄ" to "दरत्", "भसँ" to "भसत्"),
            sutra.rootOutputVarnas.mapValues { it.value.toDevanagari() },
        )
        assertEquals(
            UnadiVarnaExpansion(Svara.RR, listOf(Svara.A, Vyanjana.RA)),
            sutra.rootMorphology.getValue("शॄञ्").varnaExpansions.single(),
        )
        sutra.rootMorphology.values.forEach { morphology ->
            assertEquals(
                UnadiVarnaSubstitution(Vyanjana.DA, Vyanjana.TA),
                morphology.varnaSubstitutions.single(),
            )
        }
        assertEquals(Artha.Explanation("हृदय अथवा कूल"), sutra.rootMeaningMap.getValue("दॄ"))
        assertEquals(Artha.Explanation("जघन"), sutra.rootMeaningMap.getValue("भसँ"))
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "शरत्" }.meaning == null)
    }

    @Test
    fun `sutra 1 128 inherits adi and explicitly models shuk and hrasva`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.128" }
        assertEquals("दृणातेर्दृग् ह्रस्वश्च", sutra.text)
        assertEquals("अदि", sutra.pratyaya)
        assertEquals(setOf(ItMarker.I), sutra.itMarkers)
        assertEquals(setOf("1.127"), sutra.relatedSutraNumbers)
        assertEquals("दृषत्", sutra.rootOutputVarnas.getValue("दॄ").toDevanagari())
        val morphology = sutra.rootMorphology.getValue("दॄ")
        assertEquals("षुक्", morphology.varnaAgamas.single().upadesha)
        assertEquals(listOf(Vyanjana.SSA), morphology.varnaAgamas.single().varnas)
        assertEquals(UnadiAgamaPlacement.BEFORE_SUFFIX, morphology.varnaAgamas.single().placement)
        assertEquals(
            listOf(
                UnadiVarnaSubstitution(Svara.RR, Svara.R),
                UnadiVarnaSubstitution(Vyanjana.DA, Vyanjana.TA),
            ),
            morphology.varnaSubstitutions,
        )
        assertEquals(setOf(Linga.STRI), sutra.lexicalAttestations.single().lingas)
        assertTrue(sutra.lexicalAttestations.single().meaning == null)
    }

    @Test
    fun `sutra 1 129 models dit ti lopa without treating sarvadayah as an output`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.129" }
        assertEquals("अदि", sutra.pratyaya)
        assertEquals(setOf(ItMarker.I, ItMarker.DIT), sutra.itMarkers)
        assertEquals(setOf("01.1141", "08.0001", "01.1157"), sutra.roots.map { it.id }.toSet())
        assertEquals(
            mapOf("त्यजँ" to "त्यद्", "तनुँ" to "तद्", "यजँ" to "यद्"),
            sutra.rootOutputVarnas.mapValues { it.value.toDevanagari() },
        )
        assertEquals(
            listOf(Svara.A, Vyanjana.JA),
            sutra.rootMorphology.getValue("त्यजँ").varnaSequenceLopas.single().target,
        )
        assertEquals(
            listOf(Svara.A, Vyanjana.NA),
            sutra.rootMorphology.getValue("तनुँ").varnaSequenceLopas.single().target,
        )
        assertEquals(
            listOf(Svara.A, Vyanjana.JA),
            sutra.rootMorphology.getValue("यजँ").varnaSequenceLopas.single().target,
        )
        assertTrue(Samjna.SARVANAMA in sutra.baseSamjnas)
        assertEquals(setOf("त्यद्", "तद्", "यद्"), sutra.lexicalAttestations.map { it.form.toDevanagari() }.toSet())
        assertTrue(sutra.lexicalAttestations.all { it.lingas == setOf(Linga.PUMS, Linga.STRI, Linga.NAPUMSAKA) })
        assertTrue(sutra.lexicalAttestations.all { it.meaning == null })
    }

    @Test
    fun `sutra 1 130 derives etad with guna and tut but not inherited dit`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.130" }
        assertEquals("अदि", sutra.pratyaya)
        assertEquals(setOf(ItMarker.I), sutra.itMarkers)
        assertTrue(ItMarker.DIT !in sutra.itMarkers)
        assertEquals(setOf("02.0040"), sutra.roots.map { it.id }.toSet())
        assertEquals("एतद्", sutra.rootOutputVarnas.getValue("इण्").toDevanagari())
        val morphology = sutra.rootMorphology.getValue("इण्")
        assertEquals(UnadiVarnaSubstitution(Svara.I, Svara.E), morphology.varnaSubstitutions.single())
        assertEquals("तुट्", morphology.varnaAgamas.single().upadesha)
        assertEquals(listOf(Vyanjana.TA), morphology.varnaAgamas.single().varnas)
        assertEquals(UnadiAgamaPlacement.BEFORE_SUFFIX, morphology.varnaAgamas.single().placement)
        assertTrue(Samjna.SARVANAMA in sutra.baseSamjnas)
        assertEquals(setOf(Linga.PUMS, Linga.STRI, Linga.NAPUMSAKA), sutra.lexicalAttestations.single().lingas)
        assertTrue(sutra.lexicalAttestations.single().meaning == null)
    }

    @Test
    fun `sutra 1 131 keeps vedic sarad as an inflection of sarat`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.131" }
        assertEquals("अटि", sutra.pratyaya)
        assertEquals("अट्", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.I), sutra.itMarkers)
        assertEquals(setOf("01.1085"), sutra.roots.map { it.id }.toSet())
        assertEquals("सरट्", sutra.rootOutputVarnas.getValue("सृ").toDevanagari())
        assertEquals(
            UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA)),
            sutra.rootMorphology.getValue("सृ").varnaExpansions.single(),
        )
        assertEquals(
            Artha.Explanation("वात, मेघ अथवा वैदिक प्रयोग में मधुमक्षिका"),
            sutra.lexicalAttestations.single().meaning,
        )
        assertEquals(setOf("सरट्"), sutra.lexicalAttestations.map { it.form.toDevanagari() }.toSet())
        assertTrue(sutra.derivedVariants.none { it.output.toDevanagari() == "सरड्" })
        assertTrue(sutra.lexicalAttestations.single().lingas.isEmpty())
    }

    @Test
    fun `sutra 1 132 inherits ati and removes the nasal varna from langh`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.132" }
        assertEquals("अटि", sutra.pratyaya)
        assertEquals("अट्", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.I), sutra.itMarkers)
        assertEquals(setOf("1.131"), sutra.relatedSutraNumbers)
        assertEquals(setOf("01.0113"), sutra.roots.map { it.id }.toSet())
        assertEquals("लघट्", sutra.rootOutputVarnas.getValue("लघिँ").toDevanagari())
        assertEquals(
            UnadiVarnaLopa(Vyanjana.NGA),
            sutra.rootMorphology.getValue("लघिँ").varnaLopas.single(),
        )
        assertEquals(Artha.Explanation("वायु"), sutra.lexicalAttestations.single().meaning)
        assertEquals(setOf(Linga.PUMS), sutra.lexicalAttestations.single().lingas)
    }

    @Test
    fun `sutra 1 133 models nic lopa and suffix final ja to ka`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.133" }
        assertEquals("अजि", sutra.pratyaya)
        assertEquals("अज्", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.I), sutra.itMarkers)
        assertEquals(setOf("10.0453"), sutra.roots.map { it.id }.toSet())
        assertEquals("पारक्", sutra.rootOutputVarnas.getValue("पार").toDevanagari())
        val morphology = sutra.rootMorphology.getValue("पार")
        assertEquals(setOf(SanadiAffix.NIC), morphology.requiredSanadiAffixes)
        assertEquals(setOf(SanadiAffix.NIC), morphology.lopaSanadiAffixes)
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.JA, Vyanjana.KA),
            morphology.pratyayaVarnaSubstitutions.single(),
        )
        assertEquals(Artha.Explanation("सुवर्ण"), sutra.lexicalAttestations.single().meaning)
        assertEquals(setOf(Linga.PUMS), sutra.lexicalAttestations.single().lingas)
    }

    @Test
    fun `sutra 1 134 makes inherited aji kit and applies samprasarana`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.134" }
        assertEquals("अजि", sutra.pratyaya)
        assertEquals(setOf(ItMarker.I, ItMarker.KIT), sutra.itMarkers)
        assertEquals(setOf("1.133"), sutra.relatedSutraNumbers)
        assertEquals(setOf("01.0869", "10.0027"), sutra.roots.map { it.id }.toSet())
        assertEquals("पृथक्", sutra.rootOutputVarnas.getValue("प्रथँ").toDevanagari())
        val morphology = sutra.rootMorphology.getValue("प्रथँ")
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.RA, Svara.R),
            morphology.varnaSubstitutions.single(),
        )
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.JA, Vyanjana.KA),
            morphology.pratyayaVarnaSubstitutions.single(),
        )
        assertTrue(Samjna.AVYAYA in sutra.baseSamjnas)
        assertTrue(sutra.lexicalAttestations.single().lingas.isEmpty())
        assertTrue(sutra.lexicalAttestations.single().meaning == null)
    }

    @Test
    fun `sutra 1 135 combines hrasva shuk and suffix final change`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.135" }
        assertEquals("अजि", sutra.pratyaya)
        assertEquals(setOf(ItMarker.I), sutra.itMarkers)
        assertEquals(setOf("1.133"), sutra.relatedSutraNumbers)
        assertEquals(setOf("03.0002"), sutra.roots.map { it.id }.toSet())
        assertEquals("भिषक्", sutra.rootOutputVarnas.getValue("ञिभी").toDevanagari())
        val morphology = sutra.rootMorphology.getValue("ञिभी")
        assertEquals(UnadiVarnaSubstitution(Svara.II, Svara.I), morphology.varnaSubstitutions.single())
        assertEquals("षुक्", morphology.varnaAgamas.single().upadesha)
        assertEquals(listOf(Vyanjana.SSA), morphology.varnaAgamas.single().varnas)
        assertEquals(UnadiAgamaPlacement.BEFORE_SUFFIX, morphology.varnaAgamas.single().placement)
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.JA, Vyanjana.KA),
            morphology.pratyayaVarnaSubstitutions.single(),
        )
        assertTrue(sutra.lexicalAttestations.single().meaning == null)
        assertTrue(sutra.lexicalAttestations.single().lingas.isEmpty())
    }

    @Test
    fun `sutra 1 136 keeps yush as a textual root and tvam aham as inflections`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.136" }
        assertEquals("मदिक्", sutra.pratyaya)
        assertEquals("मद्", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals(setOf("युष्"), sutra.rootReferences)
        assertEquals(setOf("02.0060"), sutra.roots.map { it.id }.toSet())
        assertEquals(
            mapOf("युष्" to "युष्मद्", "असँ" to "अस्मद्"),
            sutra.rootOutputVarnas.mapValues { it.value.toDevanagari() },
        )
        assertEquals(Accent.UDATTA, sutra.assignedAccent)
        assertEquals(1, sutra.accentVowelIndex)
        assertTrue(Samjna.SARVANAMA in sutra.baseSamjnas)
        assertEquals(setOf("युष्मद्", "अस्मद्"), sutra.lexicalAttestations.map { it.form.toDevanagari() }.toSet())
        assertTrue(sutra.derivedVariants.none { it.output.toDevanagari() in setOf("त्वम्", "अहम्") })
        assertTrue(sutra.lexicalAttestations.all { it.lingas.isEmpty() })
    }

    @Test
    fun `sutra 1 137 models fourteen man formations and the secondary kshauma`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.137" }
        assertEquals("मन्", sutra.pratyaya)
        assertEquals("म", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.N), sutra.itMarkers)
        assertEquals(
            setOf(
                "01.1086", "02.0038", "05.0001", "03.0001", "01.1085", "01.1047", "01.0269",
                "02.0031", "02.0046", "02.0044", "02.0045", "04.0065", "10.0215", "01.1049",
            ),
            sutra.roots.map { it.id }.toSet(),
        )
        assertEquals(
            mapOf(
                "ऋ" to "अर्म", "ष्टुञ्" to "स्तोम", "षुञ्" to "सोम", "हु" to "होम",
                "सृ" to "सर्म", "धृञ्" to "धर्म", "क्षि" to "क्षेम", "टुक्षु" to "क्षोम",
                "भा" to "भाम", "या" to "याम", "वा" to "वाम", "पदँ" to "पद्म",
                "यक्षँ" to "यक्ष्म", "णीञ्" to "नेम",
            ),
            sutra.rootOutputVarnas.mapValues { it.value.toDevanagari() },
        )
        assertEquals(
            UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA)),
            sutra.rootMorphology.getValue("ऋ").varnaExpansions.single(),
        )
        assertEquals(
            UnadiVarnaSubstitution(Svara.I, Svara.E),
            sutra.rootMorphology.getValue("क्षि").varnaSubstitutions.single(),
        )
        assertEquals(
            UnadiVarnaSubstitution(Svara.II, Svara.E),
            sutra.rootMorphology.getValue("णीञ्").varnaSubstitutions.single(),
        )
        assertEquals("क्षोम", sutra.secondaryDerivatives.single().base.toDevanagari())
        assertEquals("अण्", sutra.secondaryDerivatives.single().affix)
        assertEquals("क्षौम", sutra.secondaryDerivatives.single().output.toDevanagari())
        assertTrue("धृ-क्षि-क्षु" in assertNotNull(sutra.hindiExplanation))
        assertEquals(14, sutra.lexicalAttestations.size)
    }

    @Test
    fun `sutra 1 138 applies sanvat and aa lopa to the jahati root`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.138" }
        assertEquals("जहातेः सन्वदालोपश्च", sutra.text)
        assertEquals("03.0009", sutra.roots.single().id)
        assertEquals("ओँहाक्", sutra.roots.single().upadesha)
        assertEquals("त्यागे", sutra.roots.single().artha)
        assertEquals("मन्", sutra.pratyaya)
        assertEquals("म", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.N), sutra.itMarkers)
        assertEquals(UnadiSanvatTreatment(), sutra.sanvatTreatment)
        val morphology = sutra.rootMorphology.getValue("ओँहाक्")
        assertEquals("जिहा", morphology.conditionedStem?.toDevanagari())
        assertEquals(UnadiVarnaLopa(Svara.AA), morphology.varnaLopas.single())
        assertEquals("जिह्म", sutra.rootOutputVarnas.getValue("ओँहाक्").toDevanagari())
        assertEquals(setOf(Linga.PUMS), sutra.lexicalAttestations.single().lingas)
        assertEquals(Artha.Explanation("टेढ़ा अथवा मन्द"), sutra.lexicalAttestations.single().meaning)
        assertEquals(setOf("1.137"), sutra.relatedSutraNumbers)
    }

    @Test
    fun `sutra 1 139 deletes suffix ti and derives indeclinable om`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.139" }
        assertEquals("01.0684", sutra.roots.single().id)
        assertEquals("अवँ", sutra.roots.single().upadesha)
        assertEquals("मन्", sutra.pratyaya)
        assertEquals("म्", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.N), sutra.itMarkers)
        assertEquals("अव्", sutra.rootMorphology.getValue("अवँ").conditionedStem?.toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.VA, Svara.UU),
            sutra.rootMorphology.getValue("अवँ").varnaSubstitutions.single(),
        )
        assertEquals("ओम्", sutra.rootOutputVarnas.getValue("अवँ").toDevanagari())
        assertTrue(Samjna.AVYAYA in sutra.baseSamjnas)
        assertTrue(sutra.lexicalAttestations.single().lingas.isEmpty())
        assertEquals(setOf("1.137", "6.4.20"), sutra.relatedSutraNumbers)
        assertTrue("स्वरादिपाठ" in assertNotNull(sutra.hindiExplanation))
    }

    @Test
    fun `sutra 1 140 replaces final s with aa and derives grama`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.140" }
        assertEquals("01.0717", sutra.roots.single().id)
        assertEquals("ग्रसुँ", sutra.roots.single().upadesha)
        assertEquals("अदने", sutra.roots.single().artha)
        assertEquals("मन्", sutra.pratyaya)
        assertEquals("म", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.N), sutra.itMarkers)
        val morphology = sutra.rootMorphology.getValue("ग्रसुँ")
        assertEquals("ग्रस्", morphology.conditionedStem?.toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.SA, Svara.AA),
            morphology.varnaSubstitutions.single(),
        )
        assertEquals("ग्राम", sutra.rootOutputVarnas.getValue("ग्रसुँ").toDevanagari())
        assertEquals(setOf(Linga.PUMS), sutra.lexicalAttestations.single().lingas)
        assertTrue(sutra.lexicalAttestations.single().meaning == null)
        assertEquals(setOf("1.137"), sutra.relatedSutraNumbers)
    }

    @Test
    fun `sutra 1 141 makes inherited man kit for four roots`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.141" }
        assertEquals(setOf("01.0684", "04.0002", "05.0002", "04.0080"), sutra.roots.map { it.id }.toSet())
        assertEquals("मन्", sutra.pratyaya)
        assertEquals("म", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.N, ItMarker.KIT), sutra.itMarkers)
        assertEquals(
            mapOf("अवँ" to "ऊम", "षिवुँ" to "स्यूम", "षिञ्" to "सिम", "शुषँ" to "शुष्म"),
            sutra.rootOutputVarnas.mapValues { it.value.toDevanagari() },
        )
        assertEquals("ऊ", sutra.rootMorphology.getValue("अवँ").stemAdesha?.toDevanagari())
        assertEquals(
            listOf(
                UnadiVarnaSubstitution(Svara.I, Vyanjana.YA),
                UnadiVarnaSubstitution(Vyanjana.VA, Svara.UU),
            ),
            sutra.rootMorphology.getValue("षिवुँ").varnaSubstitutions,
        )
        assertEquals(
            mapOf("ऊम" to setOf(Linga.NAPUMSAKA), "स्यूम" to setOf(Linga.PUMS), "सिम" to setOf(Linga.PUMS), "शुष्म" to setOf(Linga.NAPUMSAKA)),
            sutra.lexicalAttestations.associate { it.form.toDevanagari() to it.lingas },
        )
        assertTrue("अग्निसमीपयोः" in assertNotNull(sutra.hindiExplanation))
        assertEquals(setOf("1.137", "6.4.20"), sutra.relatedSutraNumbers)
    }

    @Test
    fun `sutra 1 142 models mak outputs and attested variants`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.142" }
        assertEquals("मक्", sutra.pratyaya)
        assertEquals("म", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals(
            setOf("06.0078", "04.0069", "07.0011", "04.0110", "10.0195", "01.1118", "05.0010", "09.0020", "10.0372", "02.0025", "04.0027", "06.0144"),
            sutra.roots.map { it.id }.toSet(),
        )
        assertEquals(setOf("ईष्", "ईर्"), sutra.rootReferences)
        assertEquals(UnadiVarnaLopa(Vyanjana.NA), sutra.rootMorphology.getValue("ञिइन्धीँ").varnaLopas.single())
        assertEquals(
            UnadiVarnaSubstitution(Svara.AI, Svara.AA),
            sutra.rootMorphology.getValue("श्यैङ्").varnaSubstitutions.single(),
        )
        assertEquals(setOf("ईष्म", "ईर्म"), sutra.derivedVariants.map { it.output.toDevanagari() }.toSet())
        assertEquals("व्रण", (sutra.derivedVariants.single { it.output.toDevanagari() == "ईर्म" }.meaning as Artha.Explanation).hindi)
        assertEquals(setOf("इष्म", "ईष्म", "युध्म", "इध्म", "दस्म", "श्याम", "धूम", "सूम", "ईर्म"), sutra.lexicalAttestations.map { it.form.toDevanagari() }.toSet())
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "श्याम" }.meaning == null)
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "धूम" }.meaning == null)
    }

    @Test
    fun `sutra 1 143 applies kutva before inherited mak`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.143" }
        assertEquals(setOf("07.0007", "01.0847", "01.1126", "10.0154"), sutra.roots.map { it.id }.toSet())
        assertEquals("मक्", sutra.pratyaya)
        assertEquals("म", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals(
            mapOf("युजिँर्" to "युग्म", "रुचँ" to "रुक्म", "तिजँ" to "तिग्म"),
            sutra.rootOutputVarnas.mapValues { it.value.toDevanagari() },
        )
        assertEquals(UnadiVarnaSubstitution(Vyanjana.JA, Vyanjana.GA), sutra.rootMorphology.getValue("युजिँर्").varnaSubstitutions.single())
        assertEquals(UnadiVarnaSubstitution(Vyanjana.CA, Vyanjana.KA), sutra.rootMorphology.getValue("रुचँ").varnaSubstitutions.single())
        assertEquals(UnadiVarnaSubstitution(Vyanjana.JA, Vyanjana.GA), sutra.rootMorphology.getValue("तिजँ").varnaSubstitutions.single())
        assertEquals(setOf("युग्म", "रुक्म", "तिग्म"), sutra.lexicalAttestations.map { it.form.toDevanagari() }.toSet())
        assertTrue(sutra.lexicalAttestations.all { it.lingas == setOf(Linga.NAPUMSAKA) && it.meaning == null })
        assertEquals(setOf("1.142"), sutra.relatedSutraNumbers)
    }

    @Test
    fun `sutra 1 144 substitutes hi for han before inherited mak`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.144" }
        assertEquals("02.0002", sutra.roots.single().id)
        assertEquals("मक्", sutra.pratyaya)
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        val morphology = sutra.rootMorphology.getValue("हनँ")
        assertEquals("हन्", morphology.conditionedStem?.toDevanagari())
        assertEquals("हि", morphology.stemAdesha?.toDevanagari())
        assertEquals("हिम", sutra.rootOutputVarnas.getValue("हनँ").toDevanagari())
        assertEquals(setOf(Linga.NAPUMSAKA), sutra.lexicalAttestations.single().lingas)
        assertTrue(sutra.lexicalAttestations.single().meaning == null)
        assertEquals(setOf("1.142"), sutra.relatedSutraNumbers)
    }

    @Test
    fun `sutra 1 145 optionally inserts effective sh of shuk`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.145" }
        assertEquals("03.0002", sutra.roots.single().id)
        assertEquals("मक्", sutra.pratyaya)
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertEquals("भीम", sutra.rootOutputVarnas.getValue("ञिभी").toDevanagari())
        val agama = sutra.rootMorphology.getValue("ञिभी").optionalVarnaAgamas.single()
        assertEquals("षुक्", agama.upadesha)
        assertEquals(listOf(Vyanjana.SSA), agama.varnas)
        assertEquals(UnadiAgamaPlacement.BEFORE_SUFFIX, agama.placement)
        assertEquals(setOf("भीम", "भीष्म"), sutra.derivedVariants.map { it.output.toDevanagari() }.toSet())
        assertEquals(setOf("भीम", "भीष्म"), sutra.lexicalAttestations.map { it.form.toDevanagari() }.toSet())
        assertTrue(sutra.lexicalAttestations.all { it.lingas == setOf(Linga.PUMS) && it.meaning == null })
        assertEquals(setOf("1.142"), sutra.relatedSutraNumbers)
    }

    @Test
    fun `sutra 1 146 nipatayati mak and guna for ghr`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.146" }
        assertEquals(setOf("01.1088", "03.0015", "10.0152"), sutra.roots.map { it.id }.toSet())
        assertEquals("मक्", sutra.pratyaya)
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertTrue(sutra.isNipatita)
        assertEquals(
            UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA)),
            sutra.rootMorphology.getValue("घृ").varnaExpansions.single(),
        )
        assertEquals("घर्म", sutra.rootOutputVarnas.getValue("घृ").toDevanagari())
        assertEquals(setOf(Linga.PUMS), sutra.lexicalAttestations.single().lingas)
        assertTrue(sutra.lexicalAttestations.single().meaning == null)
    }

    @Test
    fun `sutra 1 147 preserves opaque grishma nipatana`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.147" }
        assertEquals("01.0717", sutra.roots.single().id)
        assertEquals("मक्", sutra.pratyaya)
        assertEquals(setOf(ItMarker.KIT), sutra.itMarkers)
        assertTrue(sutra.isNipatita)
        assertTrue(sutra.rootMorphology.isEmpty())
        assertEquals("ग्रीष्म", sutra.rootOutputVarnas.getValue("ग्रसुँ").toDevanagari())
        assertEquals(setOf(Linga.PUMS), sutra.lexicalAttestations.single().lingas)
        assertTrue(sutra.lexicalAttestations.single().meaning == null)
        assertEquals(setOf("1.142"), sutra.relatedSutraNumbers)
    }

    @Test
    fun `sutra 1 148 applies samprasarana and preserves prthivi variants`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.148" }
        assertEquals(setOf("01.0869", "10.0027"), sutra.roots.map { it.id }.toSet())
        assertEquals("षिवन्", sutra.pratyaya)
        assertEquals("इव", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.SH, ItMarker.N), sutra.itMarkers)
        assertEquals(
            UnadiVarnaSubstitution(Vyanjana.RA, Svara.R),
            sutra.rootMorphology.getValue("प्रथँ").varnaSubstitutions.single(),
        )
        assertEquals("पृथिवी", sutra.rootOutputVarnas.getValue("प्रथँ").toDevanagari())
        assertEquals(setOf("पृथवी", "पृथिवी", "पृथ्वी"), sutra.derivedVariants.map { it.output.toDevanagari() }.toSet())
        assertEquals("पृथिव", sutra.secondaryDerivatives.single().base.toDevanagari())
        assertEquals("ङीष्", sutra.secondaryDerivatives.single().affix)
        assertEquals("पृथिवी", sutra.secondaryDerivatives.single().output.toDevanagari())
        assertTrue(sutra.lexicalAttestations.all { it.lingas == setOf(Linga.STRI) && it.meaning == null })
        assertTrue("षवन्नित्येके" in assertNotNull(sutra.hindiExplanation))
    }

    @Test
    fun `sutra 1 149 derives six kvan stems and attested feminine forms`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.149" }
        assertEquals(setOf("05.0020", "09.0063", "01.0335", "10.0359", "01.0517", "01.0903", "10.0240", "01.0346", "06.0160"), sutra.roots.map { it.id }.toSet())
        assertEquals("क्वन्", sutra.pratyaya)
        assertEquals("व", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.KIT, ItMarker.N), sutra.itMarkers)
        assertEquals(setOf("अश्व", "प्रुष्व", "लट्व", "कण्व", "खट्व", "विश्व"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals(
            UnadiVarnaSubstitution(Svara.A, Svara.I),
            sutra.rootMorphology.getValue("कणँ").optionalVarnaSubstitutions.single(),
        )
        assertEquals(setOf("प्रुष्व", "प्रुष्वा", "कण्व", "किण्व"), sutra.derivedVariants.map { it.output.toDevanagari() }.toSet())
        assertEquals(setOf("प्रुष्वा", "लट्वा", "खट्वा"), sutra.secondaryDerivatives.map { it.output.toDevanagari() }.toSet())
        assertEquals(setOf(Linga.NAPUMSAKA), sutra.lexicalAttestations.single { it.form.toDevanagari() == "विश्व" }.lingas)
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "विश्व" }.meaning == null)
    }

    @Test
    fun `sutra 1 150 applies guna before van and preserves nominal and nipata uses`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.150" }
        assertEquals(setOf("02.0040", "02.0026"), sutra.roots.map { it.id }.toSet())
        assertEquals("वन्", sutra.pratyaya)
        assertEquals("व", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.N), sutra.itMarkers)
        assertEquals("एव", sutra.rootOutputVarnas.getValue("इण्").toDevanagari())
        assertEquals("शेव", sutra.rootOutputVarnas.getValue("शीङ्").toDevanagari())
        assertEquals(
            UnadiVarnaSubstitution(Svara.I, Svara.E),
            sutra.rootMorphology.getValue("इण्").varnaSubstitutions.single(),
        )
        assertEquals(
            UnadiVarnaSubstitution(Svara.II, Svara.E),
            sutra.rootMorphology.getValue("शीङ्").varnaSubstitutions.single(),
        )
        assertEquals(setOf(Linga.PUMS), sutra.lexicalAttestations.filter { it.form.toDevanagari() == "एव" }.flatMap { it.lingas }.toSet())
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "एव" && it.lingas.isEmpty() }.meaning != null)
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "शेव" }.meaning == null)
        assertTrue("असत्वे निपातोऽयम्" in assertNotNull(sutra.hindiExplanation))
    }

    @Test
    fun `sutra 1 151 models each asvatantra van formation and optional reading`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.151" }
        assertEquals(14, sutra.roots.size)
        assertEquals("वन्", sutra.pratyaya)
        assertEquals("व", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.N), sutra.itMarkers)
        assertTrue(sutra.isNipatita)
        assertEquals(setOf("1.150"), sutra.relatedSutraNumbers)
        assertEquals(setOf("नि"), sutra.rootMorphology.getValue("घृषुँ").requiredUpasargas.map { it.toDevanagari() }.toSet())
        assertTrue(sutra.rootMorphology.getValue("घृषुँ").varnaSubstitutions.isEmpty())
        assertEquals(UnadiVarnaSubstitution(Svara.II, Svara.I), sutra.rootMorphology.getValue("शीङ्").varnaSubstitutions.single())
        assertEquals(UnadiVarnaSubstitution(Svara.A, Svara.I), sutra.rootMorphology.getValue("लषँ").optionalVarnaSubstitutions.single())
        assertEquals(setOf("निघृष्व", "लिष्व", "प्रह्व", "इष्व"), sutra.derivedVariants.map { it.output.toDevanagari() }.toSet())
        assertEquals(setOf("सर्व", "निघृष्व", "रिष्व", "लष्व", "लिष्व", "शिव", "पट्व", "प्रह्व", "ईष्व", "इष्व", "ह्रस्व"), sutra.lexicalAttestations.map { it.form.toDevanagari() }.toSet())
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "ह्रस्व" }.meaning == null)
        assertTrue("अकर्तरि" in assertNotNull(sutra.hindiExplanation))
    }

    @Test
    fun `sutra 1 152 keeps branch specific accent and stated varna operations`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.152" }
        assertEquals(setOf("02.0026", "02.0044", "02.0006", "06.0146", "05.0016"), sutra.roots.map { it.id }.toSet())
        assertEquals(setOf("मी"), sutra.rootReferences)
        assertEquals("वण्", sutra.pratyaya)
        assertEquals("व", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.N), sutra.itMarkers)
        assertEquals(setOf("शेव", "यह्व", "जिह्वा", "ग्रीवा", "आप्वा", "मीवा"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        val sheva = sutra.rootMorphology.getValue("शीङ्")
        assertEquals(Accent.UDATTA, sheva.assignedAccent)
        assertEquals(1, sheva.accentVowelIndex)
        val yahva = sutra.rootMorphology.getValue("या")
        assertEquals(UnadiVarnaSubstitution(Svara.AA, Svara.A), yahva.varnaSubstitutions.single())
        assertEquals("हुग्", yahva.varnaAgamas.single().upadesha)
        assertEquals(UnadiVarnaSubstitution(Vyanjana.LA, Vyanjana.JA), sutra.rootMorphology.getValue("लिहँ").varnaSubstitutions.single())
        assertEquals("ईड्", sutra.rootMorphology.getValue("गॄ").varnaAgamas.single().upadesha)
        assertEquals(setOf(Linga.PUMS), sutra.lexicalAttestations.single { it.form.toDevanagari() == "आप्वा" }.lingas)
        assertTrue("टाप्" in assertNotNull(sutra.hindiExplanation))
    }

    @Test
    fun `sutra 1 153 applies long r guna before va for all four roots`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.153" }
        assertEquals(setOf("09.0018", "09.0031", "06.0145", "09.0033", "06.0146", "09.0015", "09.0021", "01.0920", "09.0026"), sutra.roots.map { it.id }.toSet())
        assertEquals("व", sutra.pratyaya)
        assertEquals("व", sutra.pratyayaSurfaceVarnas.toDevanagari())
        assertTrue(sutra.itMarkers.isEmpty())
        assertEquals(setOf("कर्व", "गर्व", "शर्व", "दर्व"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        sutra.rootMorphology.values.forEach { morphology ->
            assertEquals(UnadiVarnaExpansion(Svara.RR, listOf(Svara.A, Vyanjana.RA)), morphology.varnaExpansions.single())
        }
        assertEquals("काम अथवा आखु", (sutra.lexicalAttestations.single { it.form.toDevanagari() == "कर्व" }.meaning as Artha.Explanation).hindi)
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "गर्व" }.meaning == null)
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "शर्व" }.meaning == null)
    }

    @Test
    fun `sutras 1 154 through 1 156 preserve kanin n stems and conditioned branches`() {
        val sutra154 = UnadiPatha.sutras.single { it.number == "1.154" }
        assertEquals("कनिन्", sutra154.pratyaya)
        assertEquals("अन्", sutra154.pratyayaSurfaceVarnas.toDevanagari())
        assertEquals(setOf(ItMarker.KIT, ItMarker.I), sutra154.itMarkers)
        assertEquals(setOf("युवन्", "वृषन्", "तक्षन्", "राजन्", "धन्वन्", "द्युवन्", "प्रतिदिवन्"), sutra154.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals(setOf("प्रति"), sutra154.rootMorphology.getValue("दिवूँ").requiredUpasargas.map { it.toDevanagari() }.toSet())

        val sutra155 = UnadiPatha.sutras.single { it.number == "1.155" }
        assertEquals(setOf("सप्तन्", "अष्टन्"), sutra155.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals("तुट्", sutra155.rootMorphology.getValue("सप्").varnaAgamas.single().upadesha)
        assertEquals(Vyanjana.TTA, sutra155.rootMorphology.getValue("अशूँ").varnaAgamas.single().varnas.single())
        assertTrue(Samjna.SANKHYA in sutra155.baseSamjnas)

        val sutra156 = UnadiPatha.sutras.single { it.number == "1.156" }
        assertEquals("अहन्", sutra156.rootOutputVarnas.getValue("ओँहाक्").toDevanagari())
        assertEquals("नञ्", sutra156.upapadaFormations.single().source.toDevanagari())
        assertEquals("अ", sutra156.upapadaFormations.single().output.toDevanagari())
        assertEquals(setOf(Linga.NAPUMSAKA), sutra156.lexicalAttestations.single().lingas)
    }

    @Test
    fun `sutra 1 157 completes adhyaya one with thirteen nipatita kanin stems`() {
        val sutra = UnadiPatha.sutras.single { it.number == "1.157" }
        assertTrue(sutra.isNipatita)
        assertEquals(setOf("उक्ष्", "पूष्"), sutra.rootReferences)
        assertEquals(13, sutra.rootOutputVarnas.size + sutra.derivedVariants.count { it.output.toDevanagari() == "मातरिश्वन्" })
        assertEquals(setOf("श्वन्", "उक्षन्", "पूषन्", "प्लीहन्", "क्लेदन्", "स्नेहन्", "मूर्धन्", "मज्जन्", "अर्यमन्", "विश्वप्सन्", "परिज्मन्", "मघवन्"), sutra.rootOutputVarnas.values.map { it.toDevanagari() }.toSet())
        assertEquals(UnadiVarnaSubstitution(Svara.U, Svara.UU), sutra.rootMorphology.getValue("मुहँ").varnaSubstitutions.first())
        assertEquals(UnadiVarnaSubstitution(Vyanjana.HA, Vyanjana.DHA), sutra.rootMorphology.getValue("मुहँ").varnaSubstitutions.last())
        assertEquals("रम्", sutra.rootMorphology.getValue("मुहँ").varnaAgamas.single().upadesha)
        assertEquals(UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.MA), sutra.rootMorphology.getValue("जनीँ").varnaSubstitutions.single())
        assertEquals("वुक्", sutra.rootMorphology.getValue("मह").varnaAgamas.single().upadesha)
        assertEquals(13, sutra.lexicalAttestations.size)
        assertTrue(sutra.lexicalAttestations.single { it.form.toDevanagari() == "श्वा" }.meaning == null)
    }

    @Test
    fun `nit and initial udatta continue from sutra 1 9 through current range`() {
        UnadiPatha.sutras.filter { it.number.substringAfter('.').toInt() in 9..18 }.forEach { sutra ->
            assertTrue(ItMarker.NIT in sutra.itMarkers, "${sutra.number} must retain नित्")
            assertEquals(Accent.UDATTA, sutra.assignedAccent, sutra.number)
            assertEquals(0, sutra.accentVowelIndex, sutra.number)
        }
    }

    @Test
    fun `all unadi blueprints preserve their own collection ownership`() {
        for (sutra in UnadiPatha.sutras) {
            val blueprint = sutra.toBlueprint()
            val source = kotlin.test.assertIs<dev.panini.sutra.runtime.SutraSource.Unadipatha>(blueprint.source)
            assertEquals(sutra.number, source.number)
            assertEquals(sutra.text, source.text)
            assertEquals(sutra.sutraId, blueprint.id)
            val encoded = kotlin.test.assertIs<dev.panini.sutra.runtime.SutraBlueprintTextEncoding.Success>(
                dev.panini.sutra.runtime.SutraBlueprintTextCodec.encode(blueprint),
            )
            val decoded = kotlin.test.assertIs<dev.panini.sutra.runtime.SutraBlueprintTextDecoding.Success>(
                dev.panini.sutra.runtime.SutraBlueprintTextCodec.decode(encoded.text),
            )
            assertEquals(blueprint, decoded.blueprint)
        }
    }

    @Test
    fun testForwardPratyayaLookup() {
        val matches = UnadiPatha.findPratyaya(KruDhatu())
        assertTrue(matches.isNotEmpty(), "KruDhatu should match Uṇādi sūtras")

        val match1 = matches.firstOrNull { it.sutraNumber == "1.1" }
        assertTrue(match1 != null, "Should find sūtra 1.1 for KruDhatu")
        assertEquals("उण्", match1.pratyaya)
        assertEquals("उ", match1.pratyayaSurface)
        assertTrue(match1.itMarkers.contains(ItMarker.NIT))
    }

    @Test
    fun testReverseSamjnaLookupForKanin() {
        val matches = UnadiPatha.findSamjna(KruDhatu(), "कनिन्")
        assertTrue(matches.isNotEmpty(), "Should find reverse match for (KruDhatu, कनिन्)")

        val match = matches.first()
        assertEquals("4.1", match.sutraNumber)
        assertTrue(match.samjnas.contains(Samjna.Rudhi("कर्ण")))
        assertTrue(match.itMarkers.contains(ItMarker.KIT))
    }

    @Test
    fun testReverseSamjnaLookupForUn() {
        val matches = UnadiPatha.findSamjna(VaaDhatu(), "उण्")
        assertTrue(matches.isNotEmpty(), "Should find reverse match for (VaaDhatu, उण्)")

        val match = matches.first()
        assertEquals("1.1", match.sutraNumber)
        assertTrue(match.samjnas.contains(Samjna.Rudhi("वायु")))
        assertEquals(Artha.Karaka.KARTA, match.meaning)
    }

    @Test
    fun testEtymologicalNiruktaLookup() {
        val matches = UnadiPatha.findByWord("पुमः")
        assertTrue(matches.isNotEmpty(), "Should find etymological sūtra for 'पुमः'")

        val match = matches.first()
        assertEquals("3.1", match.sutraNumber)
        assertEquals("असुन्", match.pratyaya)
        assertEquals(PumsDhatu().upadesha, match.dhatu.upadesha)
    }

    @Test
    fun testExpandedUnadiSutras() {
        // Legacy GamerIniSutra reused 1.156 incorrectly; canonical नञि जहातेः now owns that number.
        val gamMatches = UnadiPatha.findByWord("गामिन्")
        assertTrue(gamMatches.none { it.sutraNumber == "1.156" })
        assertEquals("अहन्", UnadiPatha.sutras.single { it.number == "1.156" }.rootOutputVarnas.getValue("ओँहाक्").toDevanagari())

        // Canonical 1.28 is प्रथिम्रदिभ्रस्जां..., which supersedes the legacy numbering collision.
        val bhrguMatches = UnadiPatha.findByWord("भृगु")
        assertTrue(bhrguMatches.isNotEmpty(), "Should find canonical sūtra for 'भृगु'")
        assertTrue(bhrguMatches.any { it.sutraNumber == "1.28" })

        // Test 2.115 SmroNisSutra
        val smarMatches = UnadiPatha.findByWord("स्मर")
        assertTrue(smarMatches.isNotEmpty(), "Should find sūtra for 'स्मर'")
        assertTrue(smarMatches.any { it.sutraNumber == "2.115" })

        // Test 4.135 PhaleGrahirAtmambharishcaSutra
        val phalaMatches = UnadiPatha.findByWord("फलग्रहि")
        assertTrue(phalaMatches.isNotEmpty(), "Should find sūtra for 'फलग्रहि'")
        assertTrue(phalaMatches.any { it.sutraNumber == "4.135" })

        // Test 5.5 ShrVrbhyamAnakSutra
        val varunaMatches = UnadiPatha.findByWord("वरुण")
        assertTrue(varunaMatches.isNotEmpty(), "Should find sūtra for 'वरुण'")
        assertTrue(varunaMatches.any { it.sutraNumber == "5.5" })
    }
}
