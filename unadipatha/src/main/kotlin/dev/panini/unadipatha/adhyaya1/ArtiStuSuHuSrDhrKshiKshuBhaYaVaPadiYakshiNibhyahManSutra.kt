package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.adadi.VaaDhatu
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.bhvadi.RiDhatu
import dev.panini.dhatupatha.bhvadi.SrDhatu
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.dhatupatha.svadi.SvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSecondaryDerivative
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra137Arti = RiDhatu()
private val sutra137Stu = AdadiDhatus.all.single { it.id == "02.0038" }
private val sutra137Su = SvadiDhatus.all.single { it.id == "05.0001" }
private val sutra137Hu = JuhotyadiDhatus.all.single { it.id == "03.0001" }
private val sutra137Sr = SrDhatu()
private val sutra137Dhr = BhvadiDhatus.all.single { it.id == "01.1047" }
private val sutra137Kshi = BhvadiDhatus.all.single { it.id == "01.0269" }
private val sutra137Kshu = AdadiDhatus.all.single { it.id == "02.0031" }
private val sutra137Bha = AdadiDhatus.all.single { it.id == "02.0046" }
private val sutra137Ya = AdadiDhatus.all.single { it.id == "02.0044" }
private val sutra137Va = VaaDhatu()
private val sutra137Padi = DivadiDhatus.all.single { it.id == "04.0065" }
private val sutra137Yakshi = CuradiDhatus.all.single { it.id == "10.0215" }
private val sutra137Ni = BhvadiDhatus.all.single { it.id == "01.1049" }

private fun gunaOfU137() = UnadiRootMorphology(
    varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.U, Svara.O)),
)

private fun gunaOfR137() = UnadiRootMorphology(
    varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA))),
)

private fun gunaOfI137() = UnadiRootMorphology(
    varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.I, Svara.E)),
)

private fun gunaOfIi137() = UnadiRootMorphology(
    varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.II, Svara.E)),
)

// 1.137: अर्तिस्तुसुहुसृधृभिक्षुभायावापदियक्षिनीभ्यो मन्
object ArtiStuSuHuSrDhrKshiKshuBhaYaVaPadiYakshiNibhyahManSutra : UnadiSutra(
    number = "1.137",
    // The catalog preserves the website's reading धृभिक्षु; the traditional enumeration reads धृ-क्षि-क्षु.
    text = "अर्तिस्तुसुहुसृधृभिक्षुभायावापदियक्षिनीभ्यो मन्",
    roots = setOf(
        sutra137Arti, sutra137Stu, sutra137Su, sutra137Hu, sutra137Sr, sutra137Dhr, sutra137Kshi,
        sutra137Kshu, sutra137Bha, sutra137Ya, sutra137Va, sutra137Padi, sutra137Yakshi, sutra137Ni,
    ),
    pratyaya = "मन्",
    pratyayaSurface = "म",
    itMarkers = setOf(ItMarker.N),
    rootSamjnaMap = mapOf(
        sutra137Arti.upadesha to Samjna.Rudhi("अर्म"),
        sutra137Stu.upadesha to Samjna.Rudhi("स्तोम"),
        sutra137Su.upadesha to Samjna.Rudhi("सोम"),
        sutra137Hu.upadesha to Samjna.Rudhi("होम"),
        sutra137Sr.upadesha to Samjna.Rudhi("सर्म"),
        sutra137Dhr.upadesha to Samjna.Rudhi("धर्म"),
        sutra137Kshi.upadesha to Samjna.Rudhi("क्षेम"),
        sutra137Kshu.upadesha to Samjna.Rudhi("क्षोम"),
        sutra137Bha.upadesha to Samjna.Rudhi("भाम"),
        sutra137Ya.upadesha to Samjna.Rudhi("याम"),
        sutra137Va.upadesha to Samjna.Rudhi("वाम"),
        sutra137Padi.upadesha to Samjna.Rudhi("पद्म"),
        sutra137Yakshi.upadesha to Samjna.Rudhi("यक्ष्म"),
        sutra137Ni.upadesha to Samjna.Rudhi("नेम"),
    ),
    rootMeaningMap = mapOf(
        sutra137Arti.upadesha to Artha.Explanation("नेत्ररोग"),
        sutra137Stu.upadesha to Artha.Explanation("समूह अथवा सञ्चय"),
        sutra137Sr.upadesha to Artha.Explanation("गमन"),
        sutra137Kshi.upadesha to Artha.Explanation("कुशल अथवा कल्याण"),
        sutra137Bha.upadesha to Artha.Explanation("सूर्य"),
        sutra137Va.upadesha to Artha.Explanation("सुन्दर अथवा प्रतिकूल"),
        sutra137Yakshi.upadesha to Artha.Explanation("प्रधान रोग"),
    ),
    rootMorphology = mapOf(
        sutra137Arti.upadesha to gunaOfR137(),
        sutra137Stu.upadesha to gunaOfU137(),
        sutra137Su.upadesha to gunaOfU137(),
        sutra137Hu.upadesha to gunaOfU137(),
        sutra137Sr.upadesha to gunaOfR137(),
        sutra137Dhr.upadesha to gunaOfR137(),
        sutra137Kshi.upadesha to gunaOfI137(),
        sutra137Kshu.upadesha to gunaOfU137(),
        sutra137Ni.upadesha to gunaOfIi137(),
    ),
    secondaryDerivatives = listOf(
        UnadiSecondaryDerivative(
            base = "क्षोम".toVarnas(),
            affix = "अण्",
            output = "क्षौम".toVarnas(),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("अर्म".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("नेत्ररोग"), "1.137"),
        UnadiLexicalAttestation("स्तोम".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("समूह अथवा सञ्चय"), "1.137"),
        UnadiLexicalAttestation("सोम".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.137"),
        UnadiLexicalAttestation("होम".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.137"),
        UnadiLexicalAttestation("सर्म".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("गमन"), "1.137"),
        UnadiLexicalAttestation("धर्म".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.137"),
        UnadiLexicalAttestation("क्षेम".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("कुशल अथवा कल्याण"), "1.137"),
        UnadiLexicalAttestation("क्षोम".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.137"),
        UnadiLexicalAttestation("भाम".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("सूर्य"), "1.137"),
        UnadiLexicalAttestation("याम".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.137"),
        UnadiLexicalAttestation("वाम".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("सुन्दर अथवा प्रतिकूल"), "1.137"),
        UnadiLexicalAttestation("पद्म".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.137"),
        UnadiLexicalAttestation("यक्ष्म".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("प्रधान रोग"), "1.137"),
        UnadiLexicalAttestation("नेम".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.137"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("सूत्र में निर्दिष्ट चौदह रूढ़ प्रातिपदिक"),
    hindiExplanation = "ऋ, स्तु, सु, हु, सृ, धृ, क्षि, क्षु, भा, या, वा, पद्, यक्ष् और नी—इन चौदह धातुओं के बाद मन् प्रत्यय होता है। अन्तिम न् इत्संज्ञक होकर लुप्त होता है, अतः श्रव्य प्रत्यय म है। ऋ/सृ/धृ के ऋ का अर्, स्तु/सु/हु/क्षु के उ का ओ, क्षि के इ का ए और नी के ई का ए गुणादेश Varna-रूप में रखा गया है; भा, या, वा, पद् और यक्ष् में अलग स्वरादेश नहीं है। इस प्रकार क्रमशः अर्म, स्तोम, सोम, होम, सर्म, धर्म, क्षेम, क्षोम, भाम, याम, वाम, पद्म, यक्ष्म और नेम सिद्ध होते हैं। वृत्ति अर्म को नेत्ररोग, स्तोम को समूह, सर्म को गमन, क्षेम को कुशल, भाम को आदित्य, वाम को शोभन तथा दुष्ट दोनों अर्थों में और यक्ष्म को रोगराज कहती है; शेष अर्थ अनुमान से नहीं जोड़े गए। उपलब्ध वेबसाइट-पाठ में ‘धृभिक्षु’ है, पर चौदह पदों की पारम्परिक व्याख्या और लघुसिद्धान्तकौमुदी का पाठ ‘धृ-क्षि-क्षु’ देता है; इसलिए मूल catalog text अक्षुण्ण रखते हुए क्रियात्मक धातु-सूची में क्षि क्षये और टुक्षु शब्दे दोनों रखे गए हैं। ‘प्रज्ञाद्यणि क्षौमं च’ के अनुसार क्षोम पर परवर्ती अण् से क्षौम बनता है; वह सीधे मन्-प्रत्ययान्त फल नहीं, इसलिए secondary derivative के रूप में दर्ज है।",
)
