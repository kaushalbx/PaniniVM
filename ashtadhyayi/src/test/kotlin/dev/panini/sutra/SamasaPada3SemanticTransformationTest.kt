package dev.panini.sutra

import dev.panini.analysis.*
import dev.panini.ashtadhyayi.adhyaya6.pada3.*
import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.*

class SamasaPada3SemanticTransformationTest {
    private fun context(
        first: String,
        second: String,
        type: SamasaType = SamasaType.TATPURUSA,
        relations: Set<SamasaSemanticRelation> = emptySet(),
        masculine: String? = null,
        features: Set<SamasaMorphologicalFeature> = emptySet(),
        outputLinga: Linga? = null,
        firstCase: Vibhakti = Vibhakti.PRATHAMA,
    ) = SamasaRuleContext(
        listOf(SamasaPada(first, firstCase, masculineCounterpart=masculine, morphologicalFeatures=features), SamasaPada(second)),
        type, outputLinga=outputLinga, semanticRelations=relations,
    )

    @Test fun `6 3 23 requires relationship semantics`() {
        val licensed=context("पितृ","शिष्य",SamasaType.ALUK_TATPURUSA,setOf(SamasaSemanticRelation.STUDY_RELATION),firstCase=Vibhakti.SASTHI)
        assertTrue(RtoVidyaYoniSambandhebhyahSutra.matches(licensed))
        assertFalse(RtoVidyaYoniSambandhebhyahSutra.matches(licensed.copy(semanticRelations=emptySet())))
    }

    @Test fun `6 3 25 and 29 perform canonical dvandva substitutions`() {
        val relation=setOf(SamasaSemanticRelation.BLOOD_RELATION)
        val r=context("मातृ","पितृ",SamasaType.DVANDVA,relation)
        assertEquals("मातापितृ",(AnangRtoDvandveSutra.apply(r) as SamasaRuleResult.Formed).compoundStem)
        val d=context("दिव्","देव",SamasaType.DVANDVA,setOf(SamasaSemanticRelation.DEVATA_COORDINATION))
        assertEquals("द्यावादेव",(DivoDyavaSutra.apply(d) as SamasaRuleResult.Formed).compoundStem)
    }

    @Test fun `pumvadbhava requires an explicit masculine counterpart`() {
        val licensed=context("सुन्दरी","कन्या",SamasaType.KARMADHARAYA,masculine="सुन्दर",outputLinga=Linga.STRI)
        assertTrue(StriyahPumvadBhasitapumskatSutra.matches(licensed))
        assertEquals("सुन्दरकन्या",(StriyahPumvadBhasitapumskatSutra.apply(licensed) as SamasaRuleResult.Formed).compoundStem)
        assertFalse(StriyahPumvadBhasitapumskatSutra.matches(licensed.copy(padas=licensed.padas.mapIndexed { i,p -> if(i==0)p.copy(masculineCounterpart=null) else p })))
    }

    @Test fun `pumvadbhava exclusions are represented morphologically`() {
        val base=context("सुन्दरी","कन्या",SamasaType.KARMADHARAYA,masculine="सुन्दर",outputLinga=Linga.STRI)
        for (feature in setOf(SamasaMorphologicalFeature.FEMININE_UUNG,SamasaMorphologicalFeature.ORDINAL,SamasaMorphologicalFeature.PRIYADI,SamasaMorphologicalFeature.K_UPADHA,SamasaMorphologicalFeature.JATI)) {
            assertFalse(StriyahPumvadBhasitapumskatSutra.matches(base.copy(padas=listOf(base.purvaPada.copy(morphologicalFeatures=setOf(feature)),base.uttaraPada))))
        }
    }

    @Test fun `6 3 43 shortens explicit ngi feminine`() {
        val c=context("कुमारी","रूप",features=setOf(SamasaMorphologicalFeature.FEMININE_NGI))
        assertTrue(GharupaKalpaCeladBruvaSutra.matches(c))
        assertEquals("कुमारिरूप",(GharupaKalpaCeladBruvaSutra.apply(c) as SamasaRuleResult.Formed).compoundStem)
    }

    @Test fun `6 3 50 to 60 lexical substitutions do not overmatch`() {
        val cases: List<Triple<SamasaSutra, SamasaRuleContext, String>> = listOf(
            Triple(HrdayasyaHrllekhayadanalasesuSutra,context("हृदय","लेख"),"हृद्लेख"),
            Triple(PadasyaPadajyatigopahatesuSutra,context("पाद","उपहत"),"पद् उपहत"),
            Triple(PesamVasaVahanaDhisuCaSutra,context("उदक","वाहन"),"उद्वाहन"),
            Triple(ManthaudanaSaktuBinduVajraSutra,context("उदक","बिन्दु"),"उद्बिन्दु"),
        )
        cases.forEach { (rule,c,expected) -> assertTrue(rule.matches(c)); assertEquals(expected,(rule.apply(c) as SamasaRuleResult.Formed).compoundStem) }
        assertFalse(HrdayasyaHrllekhayadanalasesuSutra.matches(context("हृदय","वन")))
    }

    @Test fun `compound transformations expose structured member edits`() {
        val result=PadasyaPadajyatigopahatesuSutra.apply(context("पाद","उपहत")) as SamasaRuleResult.Formed
        assertEquals(mapOf(0 to "पद्"),result.memberEdits)
        assertEquals("पद् उपहत",result.compoundStem)
    }

    @Test fun `6 3 57 requires proper-name semantics`() {
        val c=context("उदक","पर्वत",relations=setOf(SamasaSemanticRelation.PROPER_NAME))
        assertTrue(UdakasyodahSamjnayamSutra.matches(c))
        assertFalse(UdakasyodahSamjnayamSutra.matches(c.copy(semanticRelations=emptySet())))
    }

    @Test fun `6 3 73 and 74 are separate from nañ classification`() {
        val consonant=context("न","ब्राह्मण",SamasaType.NAN_TATPURUSA)
        val vowel=context("न","आर्य",SamasaType.NAN_TATPURUSA)
        assertEquals("अब्राह्मण",(NalopoNanjahSutra.apply(consonant) as SamasaRuleResult.Formed).compoundStem)
        assertEquals("अनार्य",(TasmanNudAciSutra.apply(vowel) as SamasaRuleResult.Formed).compoundStem)
    }

    @Test fun `6 3 75 preserves nañ only in its lexical list`() {
        assertTrue(NabhraanNapaanNavedaSutra.matches(context("न","कुल",SamasaType.NAN_TATPURUSA)))
        assertFalse(NalopoNanjahSutra.matches(context("न","कुल",SamasaType.NAN_TATPURUSA)))
    }

    @Test fun `6 3 78 to 81 require their own semantic domains`() {
        val named=context("सह","देव",relations=setOf(SamasaSemanticRelation.PROPER_NAME))
        assertTrue(SahasyaSahSamjnayamSutra.matches(named))
        val ordinary=context("सह","कृत",SamasaType.AVYAYIBHAVA)
        assertTrue(AvyayibhaveCakaleSutra.matches(ordinary))
        assertFalse(AvyayibhaveCakaleSutra.matches(ordinary.copy(semanticRelations=setOf(SamasaSemanticRelation.TIME_REFERENCE))))
    }
}
