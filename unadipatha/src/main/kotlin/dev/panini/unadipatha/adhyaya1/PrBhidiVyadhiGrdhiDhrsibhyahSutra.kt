package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.dhatupatha.rudhadi.RudhadiDhatus
import dev.panini.dhatupatha.svadi.SvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra123Pr = JuhotyadiDhatus.all.single { it.id == "03.0004" }
private val sutra123Bhid = RudhadiDhatus.all.single { it.id == "07.0002" }
private val sutra123Vyadh = DivadiDhatus.all.single { it.id == "04.0078" }
private val sutra123Grdh = DivadiDhatus.all.single { it.id == "04.0161" }
private val sutra123Dhrs = SvadiDhatus.all.single { it.id == "05.0025" }

// 1.23: पॄभिदिव्यधिगृधिधृषिभ्यः
object PrBhidiVyadhiGrdhiDhrsibhyahSutra : UnadiSutra(
    number = "1.23",
    text = "पॄभिदिव्यधिगृधिधृषिभ्यः",
    roots = setOf(sutra123Pr, sutra123Bhid, sutra123Vyadh, sutra123Grdh, sutra123Dhrs),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        sutra123Pr.upadesha to Samjna.Rudhi("पुरु"),
        sutra123Bhid.upadesha to Samjna.Rudhi("भिदु"),
        sutra123Vyadh.upadesha to Samjna.Rudhi("विधु"),
        sutra123Grdh.upadesha to Samjna.Rudhi("गृधु"),
        sutra123Dhrs.upadesha to Samjna.Rudhi("धृषु"),
    ),
    rootMeaningMap = mapOf(
        sutra123Pr.upadesha to Artha.Rudhi("पुरु"),
        sutra123Bhid.upadesha to Artha.Rudhi("भिदु", "thunderbolt"),
        sutra123Vyadh.upadesha to Artha.Rudhi("विधु", "moon, camphor, Vishnu, or a demon"),
        sutra123Grdh.upadesha to Artha.Rudhi("गृधु", "desire"),
        sutra123Dhrs.upadesha to Artha.Rudhi("धृषु", "capable or bold person"),
    ),
    rootMorphology = mapOf(
        sutra123Vyadh.upadesha to UnadiRootMorphology(
            varnaSubstitutions = listOf(
                UnadiVarnaSubstitution(source = Vyanjana.YA, replacement = Svara.I),
            ),
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("पाँचों धातुओं से पृथक् रूढ़ शब्द बनते हैं।"),
    hindiExplanation = "पॄ, भिद्, व्यध्, गृध् और धृष् धातुओं के बाद पूर्वसूत्र से अनुवृत्त कु प्रत्यय होता है। क् इत्संज्ञक होकर लुप्त होता है और उ शेष रहता है। इससे क्रमशः पुरु, भिदु, विधु, गृधु और धृषु शब्द बनते हैं। " +
        "व्यध् में ‘ग्रहिज्यावयिव्यधिवष्टिविचतिवृश्चतिपृच्छतिभृज्जतीनां ङिति च’ ६.१.१६ से य् का संप्रसारण इ होकर विधु सिद्ध होता है। स्रोत के अनुसार भिदु वज्र, विधु चन्द्रमा-कर्पूर-विष्णु अथवा राक्षस, गृधु काम और धृषु दक्ष व्यक्ति का वाचक है; पुरु के लिए यहाँ अनुल्लिखित अर्थ नहीं जोड़ा गया है।",
)
