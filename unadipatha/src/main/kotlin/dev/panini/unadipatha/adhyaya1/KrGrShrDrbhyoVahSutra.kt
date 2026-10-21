package dev.panini.unadipatha.adhyaya1

import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.kryadi.DrDhatu
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.dhatupatha.kryadi.ShrDhatu
import dev.panini.dhatupatha.tudadi.GrDhatu
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

private val sutra153KrKryadiUbhaya = KryadiDhatus.all.single { it.id == "09.0018" }
private val sutra153KrKryadiParasmai = KryadiDhatus.all.single { it.id == "09.0031" }
private val sutra153KrTudadi = TudadiDhatus.all.single { it.id == "06.0145" }
private val sutra153GrKryadi = KryadiDhatus.all.single { it.id == "09.0033" }
private val sutra153GrTudadi = GrDhatu()
private val sutra153ShrUbhaya = KryadiDhatus.all.single { it.id == "09.0015" }
private val sutra153ShrParasmai = ShrDhatu()
private val sutra153DrBhvadi = BhvadiDhatus.all.single { it.id == "01.0920" }
private val sutra153DrKryadi = DrDhatu()

private fun gunaOfLongR153(stem: String) = UnadiRootMorphology(
    conditionedStem = stem.toVarnas(),
    varnaExpansions = listOf(UnadiVarnaExpansion(Svara.RR, listOf(Svara.A, Vyanjana.RA))),
)

// 1.153: कॄगॄशॄदॄभ्यो वः
object KrGrShrDrbhyoVahSutra : UnadiSutra(
    number = "1.153",
    text = "कॄगॄशॄदॄभ्यो वः",
    roots = setOf(
        sutra153KrKryadiUbhaya, sutra153KrKryadiParasmai, sutra153KrTudadi,
        sutra153GrKryadi, sutra153GrTudadi,
        sutra153ShrUbhaya, sutra153ShrParasmai,
        sutra153DrBhvadi, sutra153DrKryadi,
    ),
    pratyaya = "व",
    pratyayaSurface = "व",
    rootSamjnaMap = mapOf(
        sutra153KrKryadiUbhaya.upadesha to Samjna.Rudhi("कर्व"),
        sutra153KrKryadiParasmai.upadesha to Samjna.Rudhi("कर्व"),
        sutra153GrKryadi.upadesha to Samjna.Rudhi("गर्व"),
        sutra153ShrUbhaya.upadesha to Samjna.Rudhi("शर्व"),
        sutra153ShrParasmai.upadesha to Samjna.Rudhi("शर्व"),
        sutra153DrBhvadi.upadesha to Samjna.Rudhi("दर्व"),
    ),
    rootMeaningMap = mapOf(
        sutra153KrKryadiUbhaya.upadesha to Artha.Explanation("काम अथवा आखु"),
        sutra153KrKryadiParasmai.upadesha to Artha.Explanation("काम अथवा आखु"),
        sutra153DrBhvadi.upadesha to Artha.Explanation("राक्षस"),
    ),
    rootMorphology = mapOf(
        sutra153KrKryadiUbhaya.upadesha to gunaOfLongR153("कॄ"),
        sutra153KrKryadiParasmai.upadesha to gunaOfLongR153("कॄ"),
        sutra153GrKryadi.upadesha to gunaOfLongR153("गॄ"),
        sutra153ShrUbhaya.upadesha to gunaOfLongR153("शॄ"),
        sutra153ShrParasmai.upadesha to gunaOfLongR153("शॄ"),
        sutra153DrBhvadi.upadesha to gunaOfLongR153("दॄ"),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("कर्व".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("काम अथवा आखु"), "1.153"),
        UnadiLexicalAttestation("गर्व".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.153"),
        UnadiLexicalAttestation("शर्व".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.153"),
        UnadiLexicalAttestation("दर्व".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("राक्षस"), "1.153"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "कॄ, गॄ, शॄ और दॄ धातुओं के बाद व प्रत्यय होता है। प्रत्यय में कोई इत्संज्ञक वर्ण नहीं है, इसलिए उसका पूरा व रूप श्रव्य रहता है। चारों धातुओं के दीर्घ ॠ का गुण अर् होता है; इसे ॠ → अ + र् के स्पष्ट Varna-विस्तार के रूप में रखा गया है। अतः कॄ → कर्, गॄ → गर्, शॄ → शर् और दॄ → दर् आधार बनते हैं; इनके साथ व जोड़ने पर क्रमशः कर्व, गर्व, शर्व और दर्व सिद्ध होते हैं। वृत्ति कर्व को काम अथवा आखु और दर्व को राक्षस बताती है। गर्व तथा शर्व के लिए इस स्थान पर कोई अर्थ नहीं दिया गया, इसलिए उनके परिचित कोशार्थ अनुमान से नहीं जोड़े गए हैं। सभी रूप ‘कर्वः’ आदि पुल्लिङ्ग रूप में उद्धृत हैं। सूत्र किसी गण का प्रतिबन्ध नहीं करता, इसलिए ठीक इन्हीं उपदेशों वाली उपलब्ध क्र्यादि, तुदादि तथा भ्वादि धातुपाठ प्रविष्टियाँ सुरक्षित रखी गई हैं।",
)
