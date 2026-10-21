package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.dhatupatha.rudhadi.RudhadiDhatus
import dev.panini.dhatupatha.svadi.SvadiDhatus
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaLopa
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra142Ish = TudadiDhatus.all.single { it.id == "06.0078" }
private val sutra142Yudh = DivadiDhatus.all.single { it.id == "04.0069" }
private val sutra142Indh = RudhadiDhatus.all.single { it.id == "07.0011" }
private val sutra142DasUpakshaya = DivadiDhatus.all.single { it.id == "04.0110" }
private val sutra142DasDarshana = CuradiDhatus.all.single { it.id == "10.0195" }
private val sutra142Shyai = BhvadiDhatus.all.single { it.id == "01.1118" }
private val sutra142DhuSvadi = SvadiDhatus.all.single { it.id == "05.0010" }
private val sutra142DhuKryadi = KryadiDhatus.all.single { it.id == "09.0020" }
private val sutra142DhuCuradi = CuradiDhatus.all.single { it.id == "10.0372" }
private val sutra142SuAdadi = AdadiDhatus.all.single { it.id == "02.0025" }
private val sutra142SuDivadi = DivadiDhatus.all.single { it.id == "04.0027" }
private val sutra142SuTudadi = TudadiDhatus.all.single { it.id == "06.0144" }

// 1.142: इषियुधीन्धिदसिश्याधूसूभ्यो मक्
object IshiYudhiIndhiDasiShyaDhuSubhyoMakSutra : UnadiSutra(
    number = "1.142",
    text = "इषियुधीन्धिदसिश्याधूसूभ्यो मक्",
    roots = setOf(
        sutra142Ish, sutra142Yudh, sutra142Indh, sutra142DasUpakshaya, sutra142DasDarshana,
        sutra142Shyai, sutra142DhuSvadi, sutra142DhuKryadi, sutra142DhuCuradi,
        sutra142SuAdadi, sutra142SuDivadi, sutra142SuTudadi,
    ),
    rootReferences = setOf("ईष्", "ईर्"),
    pratyaya = "मक्",
    pratyayaSurface = "म",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        sutra142Ish.upadesha to Samjna.Rudhi("इष्म"),
        sutra142Yudh.upadesha to Samjna.Rudhi("युध्म"),
        sutra142Indh.upadesha to Samjna.Rudhi("इध्म"),
        sutra142DasUpakshaya.upadesha to Samjna.Rudhi("दस्म"),
        sutra142DasDarshana.upadesha to Samjna.Rudhi("दस्म"),
        sutra142Shyai.upadesha to Samjna.Rudhi("श्याम"),
        sutra142DhuSvadi.upadesha to Samjna.Rudhi("धूम"),
        sutra142SuAdadi.upadesha to Samjna.Rudhi("सूम"),
        sutra142SuTudadi.upadesha to Samjna.Rudhi("सूम"),
    ),
    rootMeaningMap = mapOf(
        sutra142Ish.upadesha to Artha.Explanation("काम अथवा वसन्त"),
        sutra142Yudh.upadesha to Artha.Explanation("बाण अथवा योद्धा"),
        sutra142Indh.upadesha to Artha.Explanation("समिधा"),
        sutra142DasUpakshaya.upadesha to Artha.Explanation("यजमान"),
        sutra142DasDarshana.upadesha to Artha.Explanation("यजमान"),
        sutra142SuAdadi.upadesha to Artha.Explanation("अन्तरिक्ष"),
        sutra142SuTudadi.upadesha to Artha.Explanation("अन्तरिक्ष"),
        "ईष्" to Artha.Explanation("काम अथवा वसन्त; दीर्घ-पाठ"),
        "ईर्" to Artha.Explanation("व्रण; बाहुलक से"),
    ),
    rootMorphology = mapOf(
        sutra142Ish.upadesha to UnadiRootMorphology(conditionedStem = "इष्".toVarnas()),
        sutra142Yudh.upadesha to UnadiRootMorphology(conditionedStem = "युध्".toVarnas()),
        sutra142Indh.upadesha to UnadiRootMorphology(
            conditionedStem = "इन्ध्".toVarnas(),
            varnaLopas = listOf(UnadiVarnaLopa(Vyanjana.NA)),
        ),
        sutra142DasUpakshaya.upadesha to UnadiRootMorphology(conditionedStem = "दस्".toVarnas()),
        sutra142DasDarshana.upadesha to UnadiRootMorphology(conditionedStem = "दस्".toVarnas()),
        sutra142Shyai.upadesha to UnadiRootMorphology(
            conditionedStem = "श्यै".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.AI, Svara.AA)),
        ),
        sutra142DhuSvadi.upadesha to UnadiRootMorphology(conditionedStem = "धू".toVarnas()),
        sutra142SuAdadi.upadesha to UnadiRootMorphology(conditionedStem = "सू".toVarnas()),
        sutra142SuTudadi.upadesha to UnadiRootMorphology(conditionedStem = "सू".toVarnas()),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            output = "ईष्म".toVarnas(),
            meaning = Artha.Explanation("काम अथवा वसन्त; ईषि-पाठ में दीर्घादि"),
            sourceRoot = "ईष्",
        ),
        UnadiDerivedVariant(
            output = "ईर्म".toVarnas(),
            meaning = Artha.Explanation("व्रण"),
            sourceRoot = "ईर्",
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("इष्म".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("काम अथवा वसन्त"), "1.142"),
        UnadiLexicalAttestation("ईष्म".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("काम अथवा वसन्त; दीर्घ-पाठ"), "1.142"),
        UnadiLexicalAttestation("युध्म".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("बाण अथवा योद्धा"), "1.142"),
        UnadiLexicalAttestation("इध्म".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("समिधा"), "1.142"),
        UnadiLexicalAttestation("दस्म".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("यजमान"), "1.142"),
        UnadiLexicalAttestation("श्याम".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.142"),
        UnadiLexicalAttestation("धूम".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.142"),
        UnadiLexicalAttestation("सूम".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("अन्तरिक्ष"), "1.142"),
        UnadiLexicalAttestation("ईर्म".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("व्रण"), "1.142"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("इष्म आदि मक्-प्रत्ययान्त रूप अपने वृत्तिप्रदत्त अर्थों में सिद्ध होते हैं।"),
    hindiExplanation = "इषि, युधि, इन्धि, दसि, श्या, धू और सू के बाद मक् प्रत्यय होता है। मक् का अन्तिम क् इत्संज्ञक होकर लुप्त होता है, अतः श्रव्य प्रत्यय म है और कित्त्व सामान्य गुण को रोकता है। इष् + म से इष्म ‘काम अथवा वसन्त’ बनता है; वृत्ति के ईषि-पाठ में आरम्भिक स्वर दीर्घ रहने से ईष्म पाठान्तर भी सुरक्षित है। युध् + म से युध्म ‘बाण अथवा योद्धा’ बनता है। इन्ध् में न्-वर्ण का स्पष्ट लोप होने पर इध् + म से इध्म ‘समिधा’ सिद्ध होता है। सूत्र गणनिर्देश नहीं करता और दस् के उपक्षय तथा दर्शन-दंशन अर्थ वाले दो उपयुक्त धातुपाठीय आधार उपलब्ध हैं, इसलिए दोनों को दस्म ‘यजमान’ के साथ रखा गया है। श्यैङ् के ऐ का आादेश करके श्या + म से श्याम मिलता है। कम्पनार्थक धूञ् की स्वादि, क्र्यादि और चुरादि तीनों समान उपदेश वाली प्रविष्टियाँ तथा सू की अदादि, दिवादि और तुदादि प्रविष्टियाँ गण-प्रतिबन्ध के अभाव में सुरक्षित हैं; उनसे क्रमशः धूम और सूम ‘अन्तरिक्ष’ मिलते हैं। वृत्ति बाहुलक से ईर् + म का ईर्म ‘व्रण’ भी देती है; उसे मुख्य सात धातुओं में मिलाने के बजाय पृथक् derived variant रखा गया है। जिन रूपों का अर्थ वृत्ति ने नहीं बताया, उनके परिचित अर्थ अनुमान से नहीं जोड़े गए।",
)
