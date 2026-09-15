package dev.panini.sutra

import dev.panini.analysis.*
import dev.panini.ashtadhyayi.adhyaya5.pada4.*
import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class Samasanta69To112EvidenceTest {
    @Test
    fun `every canonical rule from 5 4 69 through 112 has executable evidence`() {
        val formed = listOf(
            KimanhKsepeSutra to c("किम्", "राजन्", semantics = setOf(SamasaSemanticRelation.CENSURE)),
            PathoVibhasaSutra to c("नञ्", "पथिन्", SamasaType.NAN_TATPURUSA),
            BahuvrihauSankhyeyeDajabahuganatSutra to c("द्वि", "जन", SamasaType.BAHUVRIHI, semantics = setOf(SamasaSemanticRelation.NUMERICAL_REFERENT)),
            RkPurAbDhurPathamSutra to c("विष्णु", "पुर"),
            AcPratyanvavapurvatSamalomnahSutra to c("प्रति", "सामन्"),
            AksnoAdarsanatSutra to c("पर", "अक्षि", SamasaType.AVYAYIBHAVA, semantics = setOf(SamasaSemanticRelation.NOT_LITERAL_EYE)),
            AcaturadicCanonicalSutra to c("अ", "चतुर्"),
            BrahmahastibhyamVarcasahSutra to c("ब्रह्म", "वर्चस्"),
            AvasamandhebhyasTamasahSutra to c("अव", "तमस्"),
            SvasoVasiyahSreyasahSutra to c("श्वस्", "श्रेयस्"),
            AnvavataptadRahasahSutra to c("अनु", "रहस्"),
            PraterUrasahSaptamisthatSutra to c("प्रति", "उरस्", lastVibhakti = Vibhakti.SAPTAMI),
            AnugavamAyameSutra to c("अनु", "गो", semantics = setOf(SamasaSemanticRelation.MEASURE_DIMENSION)),
            DvistavaTristavaVedihSutra to c("द्वि", "स्ताव", SamasaType.DVIGU),
            UpasargadAdhvanahSutra to c("प्र", "अध्वन्", firstSamjnas = setOf(Samjna.UPASARGA)),
            TatpurusasyangulehSankhyavyayadehSutra to c("द्वि", "अङ्गुलि"),
            AhasRatrehSutra to c("सर्व", "रात्रि"),
            AhnoHnaEtebhyahSutra to c("सर्व", "अहन्"),
            UttamaEkabhyamChaSutra to c("उत्तम", "अहन्"),
            RajahahSakhibhyasTacSutra to c("परम", "राजन्"),
            GorAtaddhitalukiSutra to c("बहु", "गो"),
            AgrakhyayamUrasahSutra to c("अग्र", "उरस्", semantics = setOf(SamasaSemanticRelation.PRAISE)),
            AnoAsmayassarasamJatisamjnayohSutra to c("लौह", "अयस्", semantics = setOf(SamasaSemanticRelation.SPECIES)),
            GramakautabhyamCaTaksnahSutra to c("ग्राम", "तक्षन्"),
            AtehSunahSutra to c("अति", "श्वन्"),
            UpamanadApranisuSutra to c("पाषाण", "श्वन्", semantics = setOf(SamasaSemanticRelation.NON_ANIMATE_REFERENT)),
            UttaramrgapurvacCaSakthnahSutra to c("उत्तर", "सक्थि"),
            NavoDvigohSutra to c("द्वि", "नौ", SamasaType.DVIGU),
            ArdhacChaSutra to c("अर्ध", "नौ"),
            KharyahPracamSutra to c("अर्ध", "खारी"),
            DvitribhyamAnjalehSutra to c("द्वि", "अञ्जलि", SamasaType.DVIGU),
            AnasantanNapumsakacChandasiSutra to c("महत्", "मनस्", outputLinga = Linga.NAPUMSAKA, semantics = setOf(SamasaSemanticRelation.VEDIC_REGISTER)),
            BrahmanoJanapadakhyayamSutra to c("देश", "ब्राह्मण", semantics = setOf(SamasaSemanticRelation.COUNTRY_PERSON)),
            KumahadbhyamAnyatarasyamSutra to c("कु", "ब्रह्मन्"),
            DvandvacCudasahantatSamahareSutra to c("वाक्", "वाच्", SamasaType.DVANDVA, semantics = setOf(SamasaSemanticRelation.COLLECTIVE)),
            AvyayibhaveSaratprabhrtibhyahSutra to c("प्रति", "शरद्", SamasaType.AVYAYIBHAVA),
            AnasCaSutra to c("उप", "अन्", SamasaType.AVYAYIBHAVA),
            NapumsakadAnyatarasyamSutra to c("उप", "अन्", SamasaType.AVYAYIBHAVA, lastLinga = Linga.NAPUMSAKA),
            NadipaurnamasyagrahayanibhyahSutra to c("उप", "नदी", SamasaType.AVYAYIBHAVA),
            JhayahSutra to c("उप", "वाच्", SamasaType.AVYAYIBHAVA),
            GiresCaSenakasyaSutra to c("उप", "गिरि", SamasaType.AVYAYIBHAVA),
        )
        formed.forEach { (rule, context) ->
            assertTrue(rule.matches(context), "${number(rule)} did not match its licensed context")
            assertIs<SamasaRuleResult.Formed>(rule.apply(context), number(rule))
        }

        val prohibited = listOf(
            NaPujanatSutra to c("सु", "राजन्", semantics = setOf(SamasaSemanticRelation.PRAISE)),
            NanjastatpurusatSutra to c("नञ्", "पथिन्", SamasaType.NAN_TATPURUSA),
            NaSamkhyaderAhnahSutra to c("द्वि", "अहन्", SamasaType.DVIGU),
        )
        prohibited.forEach { (rule, context) ->
            assertTrue(rule.matches(context), "${number(rule)} prohibition did not match")
            assertIs<SamasaRuleResult.NotApplicable>(rule.apply(context), number(rule))
        }
    }

    @Test
    fun `semantic samasanta rules reject lexical near misses`() {
        assertFalse(KimanhKsepeSutra.matches(c("किम्", "राजन्")))
        assertFalse(AksnoAdarsanatSutra.matches(c("पर", "अक्षि", SamasaType.AVYAYIBHAVA)))
        assertFalse(BahuvrihauSankhyeyeDajabahuganatSutra.matches(c("द्वि", "जन", SamasaType.BAHUVRIHI)))
        assertFalse(AnugavamAyameSutra.matches(c("अनु", "गो")))
        assertFalse(AnasantanNapumsakacChandasiSutra.matches(c("महत्", "मनस्", outputLinga = Linga.NAPUMSAKA)))
    }

    private fun c(
        first: String,
        last: String,
        type: SamasaType = SamasaType.TATPURUSA,
        lastVibhakti: Vibhakti = Vibhakti.PRATHAMA,
        outputLinga: Linga? = null,
        lastLinga: Linga? = null,
        semantics: Set<SamasaSemanticRelation> = emptySet(),
        firstSamjnas: Set<Samjna> = emptySet(),
    ) = SamasaRuleContext(
        padas = listOf(
            SamasaPada(first, samjnas = firstSamjnas),
            SamasaPada(last, vibhakti = lastVibhakti, linga = lastLinga),
        ),
        samasaType = type,
        outputLinga = outputLinga,
        semanticRelations = semantics,
    )

    private fun number(rule: SamasaSutra) = (rule as Sutra<*, *>).number
}
