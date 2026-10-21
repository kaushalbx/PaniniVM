package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.bhvadi.SrDhatu
import dev.panini.dhatupatha.svadi.VrDhatu
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

private val sutra126Kr = KruDhatu()
private val sutra126Sr = SrDhatu()
private val sutra126Bhr = BhvadiDhatus.all.single { it.id == "01.1045" }
private val sutra126Vr = VrDhatu()

// 1.126: अण्डन्कृसृभृवृञः
object AndanKrSrBhrVrnyahSutra : UnadiSutra(
    number = "1.126",
    text = "अण्डन्कृसृभृवृञः",
    roots = setOf(sutra126Kr, sutra126Sr, sutra126Bhr, sutra126Vr),
    pratyaya = "अण्डन्",
    pratyayaSurface = "अण्ड",
    itMarkers = setOf(ItMarker.N),
    rootSamjnaMap = mapOf(
        sutra126Kr.upadesha to Samjna.Rudhi("करण्ड"),
        sutra126Sr.upadesha to Samjna.Rudhi("सरण्ड"),
        sutra126Bhr.upadesha to Samjna.Rudhi("भरण्ड"),
        sutra126Vr.upadesha to Samjna.Rudhi("वरण्ड"),
    ),
    rootMeaningMap = mapOf(
        sutra126Sr.upadesha to Artha.Explanation("पक्षी"),
        sutra126Bhr.upadesha to Artha.Explanation("स्वामी"),
        sutra126Vr.upadesha to Artha.Explanation("मुखरोग"),
    ),
    rootMorphology = mapOf(
        sutra126Kr.upadesha to gunaMorphology("कृ"),
        sutra126Sr.upadesha to gunaMorphology("सृ"),
        sutra126Bhr.upadesha to gunaMorphology("भृ"),
        sutra126Vr.upadesha to gunaMorphology("वृ"),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("करण्ड".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.126"),
        UnadiLexicalAttestation("सरण्ड".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("पक्षी"), "1.126"),
        UnadiLexicalAttestation("भरण्ड".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("स्वामी"), "1.126"),
        UnadiLexicalAttestation("वरण्ड".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("मुखरोग"), "1.126"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कृ, सृ, भृ और वृञ् से अण्डन् प्रत्यय द्वारा करण्ड आदि चार प्रातिपदिक बनते हैं।"),
    hindiExplanation = "कृ, सृ, भृ और वृञ्—इन चार धातुओं के बाद अण्डन् प्रत्यय होता है। प्रत्यय का अन्तिम न् इत्संज्ञक होकर लुप्त होता है, इसलिए उसका श्रव्य Varna-रूप अण्ड है। प्रत्येक धातु के ऋ का गुण अर् होता है; इसे ऋ → अ + र् के स्पष्ट Varna-विस्तार से model किया गया है। इस प्रकार कर् + अण्ड से करण्ड, सर् + अण्ड से सरण्ड, भर् + अण्ड से भरण्ड और वर् + अण्ड से वरण्ड सिद्ध होते हैं। वृत्ति सरण्ड को पक्षी, भरण्ड को स्वामी और वरण्ड को मुखरोग बताती है; इन्हीं प्रमाणित अर्थों को रखा गया है। करण्ड के लिए इस स्रोत में कोई अर्थ नहीं दिया गया, इसलिए उसके लिए अर्थ अनुमान से नहीं जोड़ा गया। सभी उदाहरण प्रथमा-एकवचन के विसर्गयुक्त रूप में उद्धृत हैं, अतः चारों प्रातिपदिक पुल्लिङ्ग रखे गए हैं।",
)

private fun gunaMorphology(stem: String): UnadiRootMorphology = UnadiRootMorphology(
    conditionedStem = stem.toVarnas(),
    varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA))),
)
