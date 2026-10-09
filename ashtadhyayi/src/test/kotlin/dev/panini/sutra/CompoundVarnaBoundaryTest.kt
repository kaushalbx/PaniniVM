package dev.panini.sutra

import dev.panini.analysis.*
import dev.panini.ashtadhyayi.adhyaya5.pada4.*
import dev.panini.ashtadhyayi.adhyaya2.pada2.TatraTenedamitiSarupeSutra
import dev.panini.ashtadhyayi.adhyaya6.pada3.*
import dev.panini.core.SamasaType
import dev.panini.shiksha.toVarnas
import kotlin.test.*

class CompoundVarnaBoundaryTest {
    @Test fun `altar nipatana requires the specific relative dimension sense`() {
        for(first in listOf("द्वि","त्रि")) {
            val input=context(first,"स्ताव",SamasaType.DVIGU,
                setOf(SamasaSemanticRelation.ALTAR_RELATIVE_DIMENSION))
            assertTrue(DvistavaTristavaVedihSutra.matches(input))
            val result=DvistavaTristavaVedihSutra.apply(input)
            assertEquals(first+"स्ताव",result.compoundStem)
            assertEquals(mapOf(1 to "स्ताव"),result.memberEdits)
            assertFalse(result.wholeStemOverride)
            assertFalse(DvistavaTristavaVedihSutra.matches(input.copy(semanticRelations=emptySet())))
            assertFalse(DvistavaTristavaVedihSutra.matches(input.copy(semanticRelations=setOf(SamasaSemanticRelation.MEASURE_DIMENSION))))
        }
    }

    @Test fun `sarat gana uses lexical membership and explicit varna operations`() {
        fun input(last:String)=context("उप",last,SamasaType.AVYAYIBHAVA)
        for(last in listOf("शरद्","विपाश्","मनस्","दिश्")) {
            assertTrue(AvyayibhaveSaratprabhrtibhyahSutra.matches(input(last)))
            assertEquals("अ",AvyayibhaveSaratprabhrtibhyahSutra.apply(input(last)).samasantaSuffix)
        }
        assertEquals(mapOf(1 to "पथ"),AvyayibhaveSaratprabhrtibhyahSutra.apply(input("पथिन्")).memberEdits)
        assertEquals(mapOf(1 to "जरस"),AvyayibhaveSaratprabhrtibhyahSutra.apply(input("जरा")).memberEdits)
        val eye=context("प्रति","अक्षि",SamasaType.AVYAYIBHAVA)
        assertTrue(AvyayibhaveSaratprabhrtibhyahSutra.matches(eye))
        assertEquals(mapOf(1 to "अक्ष"),AvyayibhaveSaratprabhrtibhyahSutra.apply(eye).memberEdits)
        assertFalse(AvyayibhaveSaratprabhrtibhyahSutra.matches(input("अक्षि")))
        for(last in listOf("वर्ष","रात्रि","अहन्")) assertFalse(AvyayibhaveSaratprabhrtibhyahSutra.matches(input(last)))
    }

    @Test fun `vedic an and as prescriptions require their exact phonological suffixes`() {
        fun input(last: String) = context("देव", last, relations = setOf(SamasaSemanticRelation.VEDIC_REGISTER))
            .copy(outputLinga = dev.panini.core.Linga.NAPUMSAKA)
        assertTrue(AnasantanNapumsakacChandasiSutra.optional)
        val an = input("सामन्")
        assertTrue(AnasantanNapumsakacChandasiSutra.matches(an))
        assertEquals(mapOf(1 to "साम"), AnasantanNapumsakacChandasiSutra.apply(an).memberEdits)
        val asInput = input("छन्दस्")
        assertTrue(AnasantanNapumsakacChandasiSutra.matches(asInput))
        val result = AnasantanNapumsakacChandasiSutra.apply(asInput)
        assertEquals("अ", result.samasantaSuffix)
        assertEquals("देवछन्दस", result.compoundStem)
        assertFalse(AnasantanNapumsakacChandasiSutra.matches(input("दिव्")))
        assertFalse(AnasantanNapumsakacChandasiSutra.matches(input("पुमान्")))
        assertFalse(AnasantanNapumsakacChandasiSutra.matches(input("श्रेयस्").copy(semanticRelations = emptySet())))
    }

    @Test fun `optional avyayibhava prescriptions replace final vowels or attach a explicitly`() {
        for ((last, expected) in listOf("नदी" to "नद", "पौर्णमासी" to "पौर्णमास", "आग्रहायणी" to "आग्रहायण")) {
            val input = context("उप", last, SamasaType.AVYAYIBHAVA)
            assertTrue(NadipaurnamasyagrahayanibhyahSutra.matches(input))
            assertEquals(mapOf(1 to expected), NadipaurnamasyagrahayanibhyahSutra.apply(input).memberEdits)
        }
        assertTrue(GiresCaSenakasyaSutra.optional)
        assertEquals(mapOf(1 to "गिर"), GiresCaSenakasyaSutra.apply(context("उप", "गिरि", SamasaType.AVYAYIBHAVA)).memberEdits)
        val jhay = JhayahSutra.apply(context("उप", "समिध्", SamasaType.AVYAYIBHAVA))
        assertEquals("अ", jhay.samasantaSuffix)
        assertEquals("उपसमिध", jhay.compoundStem)
        assertTrue(jhay.memberEdits.isEmpty())
    }

    @Test fun `country brahman prescription uses the correct lexical stem`() {
        val relations = setOf(SamasaSemanticRelation.COUNTRY_PERSON)
        val input = context("अवन्ति", "ब्रह्मन्", relations = relations)
        assertTrue(BrahmanoJanapadakhyayamSutra.matches(input))
        val result = BrahmanoJanapadakhyayamSutra.apply(input)
        assertEquals(mapOf(1 to "ब्रह्म"), result.memberEdits)
        assertEquals("अवन्तिब्रह्म", result.compoundStem)
        assertFalse(result.wholeStemOverride)
        assertFalse(BrahmanoJanapadakhyayamSutra.matches(context("अवन्ति", "ब्राह्मण", relations = relations)))
        assertFalse(BrahmanoJanapadakhyayamSutra.matches(input.copy(semanticRelations = emptySet())))
    }

    @Test fun `collective dvandva selects an explicit a suffix after eligible consonants`() {
        val input = context("वाक्", "त्वच्", SamasaType.DVANDVA, setOf(SamasaSemanticRelation.COLLECTIVE))
        assertTrue(DvandvacCudasahantatSamahareSutra.matches(input))
        val result = DvandvacCudasahantatSamahareSutra.apply(input)
        assertEquals("अ", result.samasantaSuffix)
        assertEquals("वाक्त्वच", result.compoundStem)
        assertTrue(result.memberEdits.isEmpty())
        assertFalse(result.wholeStemOverride)
        assertFalse(DvandvacCudasahantatSamahareSutra.matches(input.copy(semanticRelations = emptySet())))
    }

    @Test fun `acaturadi prescriptions record suffix and member operations explicitly`() {
        for (first in listOf("अ", "वि", "सु")) {
            val result = AcaturadicCanonicalSutra.apply(context(first, "चतुर्"))
            assertEquals("अ", result.samasantaSuffix)
            assertTrue(result.memberEdits.isEmpty())
            assertEquals(first + "चतुर", result.compoundStem)
        }
        for ((first, expected) in listOf("नक्तम्" to "नक्तं", "रात्रिम्" to "रात्रिं")) {
            val result = AcaturadicCanonicalSutra.apply(context(first, "दिव"))
            assertEquals(mapOf(0 to expected), result.memberEdits)
            assertFalse(result.wholeStemOverride)
        }
        val unchanged = AcaturadicCanonicalSutra.apply(context("अहर्", "दिव"))
        assertTrue(unchanged.memberEdits.isEmpty())
        assertEquals("अहर्दिव", unchanged.compoundStem)
        assertFalse(unchanged.wholeStemOverride)
        assertFalse(AcaturadicCanonicalSutra.matches(context("अह", "र्दिव")))
    }

    @Test fun `hunter nipatana requires irma and leaves guna to boundary sandhi`() {
        val relations = setOf(SamasaSemanticRelation.HUNTER_ASSOCIATION)
        val input = context("दक्षिण", "ईर्म", SamasaType.BAHUVRIHI, relations)
        assertTrue(DaksinerMaLubdhayogeSutra.matches(input))
        val result = DaksinerMaLubdhayogeSutra.apply(input)
        assertEquals(mapOf(1 to "ईर्मन्"), result.memberEdits)
        assertEquals("दक्षिणईर्मन्", result.compoundStem)
        assertFalse(result.wholeStemOverride)
        assertFalse(DaksinerMaLubdhayogeSutra.matches(context("दक्षिण", "हस्त", SamasaType.BAHUVRIHI, relations)))
        assertFalse(DaksinerMaLubdhayogeSutra.matches(input.copy(semanticRelations = emptySet())))
    }

    @Test fun `prescribed 120 forms retain members and operate on exact varna positions`() {
        for ((first, last, expected) in listOf(
            Triple("सु", "प्रात", "प्रात"), Triple("सु", "श्वस्", "श्व"),
            Triple("सु", "दिव", "दिव"), Triple("शारि", "कुक्षि", "कुक्ष"),
            Triple("चतुर्", "अश्र", "अश्र"), Triple("एणी", "पाद", "पद"),
            Triple("अज", "पाद", "पद"), Triple("प्रोष्ठ", "पाद", "पद"),
        )) {
            val input = context(first, last, SamasaType.BAHUVRIHI)
            assertTrue(SupratasusvasudivaCanonicalSutra.matches(input))
            val result = SupratasusvasudivaCanonicalSutra.apply(input)
            assertEquals(mapOf(1 to expected), result.memberEdits)
            assertFalse(result.wholeStemOverride)
            assertNull(result.samasantaSuffix)
        }
        assertFalse(SupratasusvasudivaCanonicalSutra.matches(context("सु", "पाद", SamasaType.BAHUVRIHI)))
    }

    @Test fun `nispravani nipatana preserves both independently addressable members`() {
        for (first in listOf("निस्", "निष्")) {
            val input=context(first, "प्रवाणी", SamasaType.BAHUVRIHI)
            assertTrue(NispravanisCaSutra.matches(input))
            val result=NispravanisCaSutra.apply(input) as SamasaRuleResult.Formed
            assertEquals(mapOf(0 to "निष्", 1 to "प्रवाणि"), result.memberEdits)
            assertEquals("निष्प्रवाणि", result.compoundStem)
            assertFalse(result.wholeStemOverride)
        }
    }

    @Test fun `regular samasanta members leave joining sandhi to its own rules`() {
        val inputs = listOf(
            Triple(UpasargadAdhvanahSutra, context("प्र", "अध्वन्"), "अध्व"),
            Triple(TatpurusasyangulehSankhyavyayadehSutra, context("त्रि", "अङ्गुलि"), "अङ्गुल"),
            Triple(AnugavamAyameSutra, context("अनु", "गो"), "गव"),
        )
        for ((rule, input, expected) in inputs) {
            val result = rule.apply(input) as SamasaRuleResult.Formed
            assertEquals(mapOf(1 to expected), result.memberEdits)
            assertFalse(result.wholeStemOverride)
        }
    }

    @Test fun `tac operates on final member without taking over mahat substitution`() {
        for ((last, expected) in listOf("राजन्" to "राज", "अहन्" to "अह", "सखि" to "सख")) {
            val input=context("महत्", last, SamasaType.KARMADHARAYA)
            assertTrue(RajahahSakhibhyasTacSutra.matches(input))
            val result=RajahahSakhibhyasTacSutra.apply(input) as SamasaRuleResult.Formed
            assertEquals(mapOf(1 to expected), result.memberEdits)
            assertEquals(input.purvaPada.varnas + expected.toVarnas(), result.compoundStem.toVarnas())
        }
        assertFalse(RajahahSakhibhyasTacSutra.matches(context("परम", "राजन")))
    }

    @Test fun `kap selection tests phonological r and preserves all members`() {
        fun c(last: String) = context("बहु", last, SamasaType.BAHUVRIHI)
        for (last in listOf("ऋ", "कर्तृ")) assertTrue(NadyrtaschaSutra.matches(c(last)))
        for (last in listOf("ॠ", "कर्तॄ", "कर्त्र्", "कर्तृन्")) assertFalse(NadyrtaschaSutra.matches(c(last)))
        assertTrue(UrahPrabhrtibhyahKapSutra.matches(c("उरस्")))
        assertFalse(UrahPrabhrtibhyahKapSutra.matches(c("उरस")))
        val input=SamasaRuleContext(listOf(SamasaPada("अति"), SamasaPada("बहु"), SamasaPada("कर्तृ")), SamasaType.BAHUVRIHI)
        val result=NadyrtaschaSutra.apply(input) as SamasaRuleResult.Formed
        assertEquals("अतिबहुकर्तृक", result.compoundStem)
        assertEquals("क", result.samasantaSuffix)
        assertTrue(result.memberEdits.isEmpty())
    }

    @Test fun `samasanta lexical alternants use exact final member varnas`() {
        for ((last, expected) in listOf("अनस्" to "अनस", "अश्मन्" to "अश्म", "अयस्" to "अयस", "सरस्" to "सरस")) {
            val input = context("महा", last, relations = setOf(SamasaSemanticRelation.PROPER_NAME))
            assertTrue(AnoAsmayassarasamJatisamjnayohSutra.matches(input))
            assertEquals(mapOf(1 to expected), (AnoAsmayassarasamJatisamjnayohSutra.apply(input) as SamasaRuleResult.Formed).memberEdits)
        }
        for (last in listOf("नौ", "नाव")) {
            val input = context("द्वि", last, SamasaType.DVIGU)
            assertTrue(NavoDvigohSutra.matches(input))
            assertEquals(mapOf(1 to "नाव"), (NavoDvigohSutra.apply(input) as SamasaRuleResult.Formed).memberEdits)
        }
    }

    @Test fun `sama loman operation preserves all preceding members`() {
        val input = SamasaRuleContext(listOf(SamasaPada("अति"), SamasaPada("प्रति"), SamasaPada("लोमन्")), SamasaType.TATPURUSA)
        val result = AcPratyanvavapurvatSamalomnahSutra.apply(input) as SamasaRuleResult.Formed
        assertEquals(mapOf(2 to "लोम"), result.memberEdits)
        assertEquals("अतिप्रतिलोम", result.compoundStem)
        assertFalse(result.wholeStemOverride)
    }

    @Test fun `numerical bahuvrihi selects explicit a suffix`() {
        val input = context("पञ्च", "पूली", SamasaType.BAHUVRIHI, setOf(SamasaSemanticRelation.NUMERICAL_REFERENT))
        assertTrue(BahuvrihauSankhyeyeDajabahuganatSutra.matches(input))
        val result = BahuvrihauSankhyeyeDajabahuganatSutra.apply(input) as SamasaRuleResult.Formed
        assertEquals("अ", result.samasantaSuffix)
        assertTrue(result.memberEdits.isEmpty())
        assertFalse(result.wholeStemOverride)
    }

    @Test fun `regular samasanta edits retain final member boundaries`() {
        val cases = listOf(
            Triple(BrahmahastibhyamVarcasahSutra, context("ब्रह्म", "वर्चस्"), "वर्चस"),
            Triple(AhasRatrehSutra, context("सर्व", "रात्रि"), "रात्र"),
            Triple(GramakautabhyamCaTaksnahSutra, context("ग्राम", "तक्षन्"), "तक्ष"),
            Triple(GorAtaddhitalukiSutra, context("राज", "गो"), "गव"),
        )
        for ((rule, input, expected) in cases) {
            assertTrue(rule.matches(input), rule.number)
            val result = rule.apply(input) as SamasaRuleResult.Formed
            assertEquals(mapOf(1 to expected), result.memberEdits, rule.number)
            assertFalse(result.wholeStemOverride, rule.number)
            assertEquals(input.purvaPada.upadesha + expected, result.compoundStem, rule.number)
        }
    }

    private fun context(first: String, last: String, type: SamasaType = SamasaType.TATPURUSA,
                        relations: Set<SamasaSemanticRelation> = emptySet()) =
        SamasaRuleContext(listOf(SamasaPada(first), SamasaPada(last)), type, semanticRelations = relations)

    @Test fun `water-filled single consonant onset excludes clusters and vowels`() {
        fun c(last: String) = context("उदक", last, relations = setOf(SamasaSemanticRelation.WATER_FILLED))
        assertTrue(EkahaladauPurayitavyeAnyatarasyamSutra.matches(c("कुम्भ")))
        assertFalse(EkahaladauPurayitavyeAnyatarasyamSutra.matches(c("स्थाली")))
        assertFalse(EkahaladauPurayitavyeAnyatarasyamSutra.matches(c("ऌ")))
        assertFalse(EkahaladauPurayitavyeAnyatarasyamSutra.matches(c("")))
    }

    @Test fun `collective dvandva recognizes specified final consonants not written virama`() {
        fun c(last: String) = context("वाक्", last, SamasaType.DVANDVA, setOf(SamasaSemanticRelation.COLLECTIVE))
        for (last in listOf("वाच्", "स्रज्", "दृषद्", "उपानह्"))
            assertTrue(DvandvacCudasahantatSamahareSutra.matches(c(last)), last)
        for (last in listOf("समित्", "वाक्", "वन", "वाच"))
            assertFalse(DvandvacCudasahantatSamahareSutra.matches(c(last)), last)
    }

    @Test fun `an ending recognizes inherent a before final n`() {
        assertTrue(AnasCaSutra.matches(context("उप", "राजन्", SamasaType.AVYAYIBHAVA)))
        assertFalse(AnasCaSutra.matches(context("उप", "राजान्", SamasaType.AVYAYIBHAVA)))
        assertFalse(AnasCaSutra.matches(context("उप", "राजनि", SamasaType.AVYAYIBHAVA)))
    }

    @Test fun `jhay excludes vowel-final nasals and semivowels`() {
        fun c(last: String) = context("उप", last, SamasaType.AVYAYIBHAVA)
        assertTrue(JhayahSutra.matches(c("वाच्")))
        for (last in listOf("वाच", "वन्", "वाय्")) assertFalse(JhayahSutra.matches(c(last)))
    }

    @Test fun `shortening treats independent and dependent vowels equally`() {
        fun c(first: String) = context(first, "रूप", relations = setOf(SamasaSemanticRelation.GALAVA_OPINION))
        for ((first, shortened) in listOf("ई" to "इ", "कुमारी" to "कुमारि", "ऊ" to "उ", "ॠ" to "ऋ")) {
            val input = c(first)
            assertTrue(IkoHrasvoAnyyoGalavasyaSutra.matches(input))
            assertEquals(shortened, (IkoHrasvoAnyyoGalavasyaSutra.apply(input) as SamasaRuleResult.Formed).memberEdits[0])
        }
    }

    @Test fun `agra suffix includes the inherent initial vowel`() {
        assertTrue(AgrantasuddhasubhravrsavarahebhyasCaSutra.matches(context("समग्र", "दन्त", SamasaType.BAHUVRIHI)))
        assertFalse(AgrantasuddhasubhravrsavarahebhyasCaSutra.matches(context("समग्री", "दन्त", SamasaType.BAHUVRIHI)))
    }

    @Test fun `in ending distinguishes short i from long i`() {
        fun c(last: String) = context("बहु", last, SamasaType.BAHUVRIHI).copy(outputLinga = dev.panini.core.Linga.STRI)
        assertTrue(InahStriyamSutra.matches(c("करिन्")))
        assertTrue(InahStriyamSutra.matches(c("इन्")))
        assertFalse(InahStriyamSutra.matches(c("करीन्")))
        assertFalse(InahStriyamSutra.matches(c("करिन")))
    }

    @Test fun `vedic r prohibition tests final vowel not last written character`() {
        fun c(last: String) = context("सु", last, SamasaType.BAHUVRIHI, setOf(SamasaSemanticRelation.VEDIC_REGISTER))
        assertTrue(RtasChandasiSutra.matches(c("कर्तृ")))
        assertTrue(RtasChandasiSutra.matches(c("ऋ")))
        assertFalse(RtasChandasiSutra.matches(c("कर्तॄ")))
        assertFalse(RtasChandasiSutra.matches(c("ऋक्")))
    }

    @Test fun `pronoun final substitution removes one varna not a Unicode character`() {
        // The a + ā of tad/etad is an intermediate boundary, not an orthographic mātrā edit.
        for ((first, expected) in listOf("तद्" to "तआ", "एतद्" to "एतआ", "अन्य" to "अन्या")) {
            val c = context(first, "दृश").let {
                it.copy(padas = listOf(it.purvaPada.copy(morphologicalFeatures = setOf(SamasaMorphologicalFeature.PRONOUN)), it.uttaraPada))
            }
            assertTrue(AaSarvanamnahSutra.matches(c))
            assertEquals(expected, (AaSarvanamnahSutra.apply(c) as SamasaRuleResult.Formed).memberEdits[0])
        }
    }

    @Test fun `combat vowel changes preserve preceding consonants and both member identities`() {
        for ((word, expected, first, last) in listOf(
            listOf("केश", "केशाकेशि", "केशा", "केशि"),
            listOf("दण्ड", "दण्डादण्डि", "दण्डा", "दण्डि"),
            listOf("मुष्टि", "मुष्टामुष्टि", "मुष्टा", "मुष्टि"),
            listOf("बाहु", "बाहाबाहि", "बाहा", "बाहि"),
        )) {
            val c = context(word, word, SamasaType.BAHUVRIHI, setOf(SamasaSemanticRelation.RECIPROCAL_ACTION))
            assertTrue(TatraTenedamitiSarupeSutra.matches(c))
            val classified = TatraTenedamitiSarupeSutra.apply(c) as SamasaRuleResult.Formed
            assertTrue(classified.memberEdits.isEmpty())
            val lengthened = AnyesamApiDrsyateSutra.apply(c) as SamasaRuleResult.Formed
            val ic = IcKarmavyatihareSutra.apply(c) as SamasaRuleResult.Formed
            assertEquals(mapOf(0 to first), lengthened.memberEdits)
            assertEquals(mapOf(1 to last), ic.memberEdits)
            assertEquals(expected, lengthened.memberEdits.getValue(0) + ic.memberEdits.getValue(1))
        }
    }

    @Test fun `ik lengthening treats every short and long vowel as a phonological token`() {
        for ((first, expected) in listOf("इ" to "ई", "नदि" to "नदी", "उ" to "ऊ", "ऋ" to "ॠ", "ऌ" to "ॡ", "नदी" to "नदी", "ई" to "ई")) {
            val c = context(first, "वह")
            assertTrue(IkoVaheApilohSutra.matches(c), first)
            val formed = IkoVaheApilohSutra.apply(c) as SamasaRuleResult.Formed
            assertEquals(expected, formed.memberEdits[0], first)
        }
        assertFalse(IkoVaheApilohSutra.matches(context("पीलु", "वह")))
        assertFalse(IkoVaheApilohSutra.matches(context("नद्", "वह")))
    }

    @Test fun `ownership mark lengthening preserves inherent a and excludes consonant finals`() {
        fun c(first: String) = context(first, "कर्ण", relations = setOf(SamasaSemanticRelation.OWNERSHIP_MARK))
        assertTrue(KarneLaksanasyaSutra.matches(c("दात्र")))
        assertEquals("दात्रा", (KarneLaksanasyaSutra.apply(c("दात्र")) as SamasaRuleResult.Formed).memberEdits[0])
        assertFalse(KarneLaksanasyaSutra.matches(c("दात्र्")))
        assertFalse(KarneLaksanasyaSutra.matches(c("मणि")))
    }

    @Test fun `trikakut elision preserves the compound members before final devoicing`() {
        val c = context("त्रि", "ककुद", SamasaType.BAHUVRIHI,
            setOf(SamasaSemanticRelation.PROPER_NAME))
        assertTrue(TrikakutParvateSutra.matches(c))
        val result = TrikakutParvateSutra.apply(c) as SamasaRuleResult.Formed
        assertEquals(mapOf(1 to "ककुद्"), result.memberEdits)
        assertEquals("त्रिककुद्", result.compoundStem)
        assertFalse(result.wholeStemOverride)
        assertNull(result.samasantaSuffix)
        assertFalse(TrikakutParvateSutra.matches(c.copy(semanticRelations = emptySet())))
    }

    @Test fun `kap is recorded as a suffix rather than an inferred member edit`() {
        val c = context("बहु", "करिन्", SamasaType.BAHUVRIHI).copy(outputLinga = dev.panini.core.Linga.STRI)
        val result = InahStriyamSutra.apply(c) as SamasaRuleResult.Formed
        assertEquals(dev.panini.core.SamasantaAffix.KAP, result.samasantaAffix)
        assertEquals("क", result.samasantaSuffix)
        assertTrue(result.memberEdits.isEmpty())
        assertFalse(result.wholeStemOverride)
        assertEquals("बहुकरिन्क", result.compoundStem)
    }

    @Test fun `final-member substitution records only its exact member`() {
        val c = context("सु", "गन्ध", SamasaType.BAHUVRIHI)
        val result = GandhasyedutputisusurabhibhyahSutra.apply(c) as SamasaRuleResult.Formed
        assertEquals(mapOf(1 to "गन्धि"), result.memberEdits)
        assertNull(result.samasantaSuffix)
        assertFalse(result.wholeStemOverride)
        assertEquals("सुगन्धि", result.compoundStem)
    }
}
