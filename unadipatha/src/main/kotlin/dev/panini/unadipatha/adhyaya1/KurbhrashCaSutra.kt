package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.unadipatha.UnadiSanvatTreatment
import dev.panini.unadipatha.UnadiSutra

private val sutra122Bhr = JuhotyadiDhatus.all.single { it.id == "03.0006" }
private val sutra122Kr = KruDhatu()
private val sutra122Han = AdadiDhatus.all.single { it.id == "02.0002" }
private val sutra122Pa = AdadiDhatus.all.single { it.id == "02.0051" }

// 1.22: कुर्भ्रश्च
object KurbhrashCaSutra : UnadiSutra(
    number = "1.22",
    text = "कुर्भ्रश्च",
    roots = setOf(sutra122Bhr, sutra122Kr, sutra122Han, sutra122Pa),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        sutra122Bhr.upadesha to Samjna.Rudhi("बभ्रु"),
        sutra122Kr.upadesha to Samjna.Rudhi("चक्रु"),
        sutra122Han.upadesha to Samjna.Rudhi("जघ्नु"),
        sutra122Pa.upadesha to Samjna.Rudhi("पपु"),
    ),
    rootMeaningMap = mapOf(
        sutra122Bhr.upadesha to Artha.Rudhi("बभ्रु", "a sage or Vishnu"),
        sutra122Kr.upadesha to Artha.Rudhi("चक्रु", "doer"),
        sutra122Han.upadesha to Artha.Rudhi("जघ्नु", "slayer"),
        sutra122Pa.upadesha to Artha.Rudhi("पपु", "protector"),
    ),
    sanvatTreatment = UnadiSanvatTreatment(),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("भृ तथा चकार से संगृहीत अन्य धातुओं से पृथक् रूढ़ और कर्तृवाचक शब्द बनते हैं।"),
    hindiExplanation = "भृ धातु के बाद कु प्रत्यय होता है। क् इत्संज्ञक होकर लुप्त होता है और उ शेष रहता है। पूर्वसूत्रों से सन्वत्-भाव की अनुवृत्ति के कारण अभ्यासादि प्रक्रिया होकर बभ्रु रूप बनता है; यह किसी ऋषि अथवा विष्णु का नाम है। " +
        "सूत्र का चकार अन्य प्रमाणित प्रयोगों को भी ग्रहण करता है: कृ से चक्रु अर्थात् कर्ता, हन् से जघ्नु अर्थात् हन्ता और रक्षणार्थक पा से पपु अर्थात् पालक सिद्ध होते हैं। इन अतिरिक्त धातुओं और अर्थों को स्रोत-टीका में दिए उदाहरणों तक ही सीमित रखा गया है।",
)
