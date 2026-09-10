package dev.panini.sutra

import dev.panini.analysis.*
import dev.panini.ashtadhyayi.adhyaya5.pada4.*
import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class Samasanta114To160EvidenceTest {
    @Test
    fun `every formative rule from 5 4 114 through 160 has licensed evidence`() {
        val formed = listOf(
            AngulerDaruniSutra to c("द्वि", "अङ्गुलि", semantics = setOf(SamasaSemanticRelation.WOODEN_OBJECT)),
            DvitribhyamSaMurdhnahSutra to c("द्वि", "मूर्धन्"),
            ApPuraniPramanyohSutra to c("द्वि", "प्रथम", lastFeatures = setOf(SamasaMorphologicalFeature.ORDINAL)),
            AntarbahirbhyamCaLomnahSutra to c("अन्तर्", "लोमन्"),
            AnNasikayahSamjnayamNasamCasthulatSutra to c("दीर्घ", "नासिका", semantics = setOf(SamasaSemanticRelation.PROPER_NAME)),
            UpasargacCaSutra to c("प्र", "नासिका", firstSamjnas = setOf(Samjna.UPASARGA)),
            SupratasusvasudivaCanonicalSutra to c("शारि", "कुक्षि"),
            NanjDuhSubhyoHalisakthyorAnyatarasyamSutra to c("सु", "हलि"),
            NityamAsicPrajaMedhayohSutra to c("सु", "प्रजा"),
            BahuprajasChandasiSutra to c("बहु", "प्रजा", semantics = setOf(SamasaSemanticRelation.VEDIC_REGISTER)),
            DharmadAnicKevalatSutra to c("सु", "धर्म"),
            JambhaSuharitaTrnaSomebhyahSutra to c("सोम", "जम्भ"),
            DaksinerMaLubdhayogeSutra to c("दक्षिण", "हस्त", semantics = setOf(SamasaSemanticRelation.HUNTER_ASSOCIATION)),
            IcKarmavyatihareSutra to c("केश", "केश", semantics = setOf(SamasaSemanticRelation.RECIPROCAL_ACTION)),
            DvidandyadibhyasCaSutra to c("द्वि", "दण्ड", semantics = setOf(SamasaSemanticRelation.LEXICAL_GANA_MEMBERSHIP)),
            PrasambhyamJanunorJnuhSutra to c("प्र", "जानु"),
            UrdhvadVibhasaSutra to c("ऊर्ध्व", "जानु"),
            UdhasoAnanSutra to c("पूर्ण", "ऊधस्"),
            DhanusasCaSutra to c("दृढ", "धनुस्"),
            VaSamjnayamSutra to c("शत", "धनुस्", semantics = setOf(SamasaSemanticRelation.PROPER_NAME)),
            JayayaNinSutra to c("युव", "जाया"),
            GandhasyedutputisusurabhibhyahSutra to c("सु", "गन्ध"),
            AlpakhyayamSutra to c("अल्प", "गन्ध", semantics = setOf(SamasaSemanticRelation.SMALL_QUANTITY)),
            UpamanacCaSutra to c("पद्म", "गन्ध", semantics = setOf(SamasaSemanticRelation.QUALIFIER_QUALIFIED)),
            PadasyaLopoAhastyadibhyahSutra to c("दीर्घ", "पाद", semantics = setOf(SamasaSemanticRelation.QUALIFIER_QUALIFIED)),
            KumbhapadisuCaSutra to c("कुम्भ", "पाद"),
            SankhyasupurvasyaSutra to c("सु", "पाद"),
            VayasiDantasyaDatruSutra to c("द्वि", "दन्त", firstSamjnas = setOf(Samjna.SANKHYA), semantics = setOf(SamasaSemanticRelation.AGE_STAGE)),
            ChandasiCaSutra to c("शुक्ल", "दन्त", semantics = setOf(SamasaSemanticRelation.VEDIC_REGISTER)),
            StriyamSamjnayamSutra to c("शुक्ल", "दन्त", outputLinga = Linga.STRI, semantics = setOf(SamasaSemanticRelation.PROPER_NAME)),
            VibhasaSyavarokabhyamSutra to c("श्याव", "दन्त"),
            AgrantasuddhasubhravrsavarahebhyasCaSutra to c("शुभ्र", "दन्त"),
            KakudasyavasthayamLopahSutra to c("नत", "ककुद", semantics = setOf(SamasaSemanticRelation.ANIMAL_CONDITION)),
            TrikakutParvateSutra to c("त्रि", "ककुद", semantics = setOf(SamasaSemanticRelation.PROPER_NAME)),
            UdvibhyamKakudasyaSutra to c("उत्", "ककुद"),
            PurnadVibhasaSutra to c("पूर्ण", "ककुद"),
            SuhrdDurhrdauMitramitrayohSutra to c("सु", "हृदय"),
            UrahPrabhrtibhyahKapSutra to c("व्यूढ", "उरस्"),
            InahStriyamSutra to c("बहु", "स्वामिन्", outputLinga = Linga.STRI),
            NadyrtaschaSutra to c("बहु", "कुमारी", lastSamjnas = setOf(Samjna.NADI)),
            SesadVibhasaCanonicalSutra to c("बहु", "बल", semantics = setOf(SamasaSemanticRelation.RESIDUAL_KAP_OPTION)),
            NispravanisCaSutra to c("निष्", "प्रवाणी"),
        )
        formed.forEach { (rule, context) ->
            assertTrue(rule.matches(context), "${number(rule)} did not match its licensed context")
            assertIs<SamasaRuleResult.Formed>(rule.apply(context), number(rule))
        }
    }

    @Test
    fun `kap prohibitions from 5 4 155 through 159 have executable evidence`() {
        val prohibited = listOf(
            NaSamjnayamSutra to c("बहु", "बल", semantics = setOf(SamasaSemanticRelation.PROPER_NAME)),
            IyasasCaSutra to c("बहु", "श्रेयस्", lastFeatures = setOf(SamasaMorphologicalFeature.IYAS_ENDING)),
            VanditeBhratuhSutra to c("पूजित", "भ्रातृ", semantics = setOf(SamasaSemanticRelation.PRAISED_REFERENT)),
            RtasChandasiSutra to c("बहु", "कर्तृ", semantics = setOf(SamasaSemanticRelation.VEDIC_REGISTER)),
            NaditantryohSvangeSutra to c("दीर्घ", "नाडी", semantics = setOf(SamasaSemanticRelation.BODY_PART)),
        )
        prohibited.forEach { (rule, context) ->
            assertTrue(rule.matches(context), "${number(rule)} prohibition did not match")
            assertIs<SamasaRuleResult.NotApplicable>(rule.apply(context), number(rule))
        }
    }

    @Test
    fun `semantic rules reject missing evidence and lexical near misses`() {
        assertFalse(AngulerDaruniSutra.matches(c("द्वि", "अङ्गुलि")))
        assertFalse(AnNasikayahSamjnayamNasamCasthulatSutra.matches(c("स्थूल", "नासिका", semantics = setOf(SamasaSemanticRelation.PROPER_NAME))))
        assertFalse(BahuprajasChandasiSutra.matches(c("बहु", "प्रजा")))
        assertFalse(IcKarmavyatihareSutra.matches(c("केश", "केश")))
        assertFalse(DvidandyadibhyasCaSutra.matches(c("द्वि", "दण्ड")))
        assertFalse(VayasiDantasyaDatruSutra.matches(c("द्वि", "दन्त", firstSamjnas = setOf(Samjna.SANKHYA))))
        assertFalse(KakudasyavasthayamLopahSutra.matches(c("नत", "ककुद")))
        assertFalse(SesadVibhasaCanonicalSutra.matches(c("बहु", "बल")))
    }

    private fun c(
        first: String,
        last: String,
        type: SamasaType = SamasaType.BAHUVRIHI,
        outputLinga: Linga? = null,
        semantics: Set<SamasaSemanticRelation> = emptySet(),
        firstSamjnas: Set<Samjna> = emptySet(),
        lastSamjnas: Set<Samjna> = emptySet(),
        lastFeatures: Set<SamasaMorphologicalFeature> = emptySet(),
    ) = SamasaRuleContext(
        padas = listOf(
            SamasaPada(first, samjnas = firstSamjnas),
            SamasaPada(last, samjnas = lastSamjnas, morphologicalFeatures = lastFeatures),
        ),
        samasaType = type,
        outputLinga = outputLinga,
        semanticRelations = semantics,
    )

    private fun number(rule: SamasaSutra) = (rule as Sutra<*, *>).number
}
