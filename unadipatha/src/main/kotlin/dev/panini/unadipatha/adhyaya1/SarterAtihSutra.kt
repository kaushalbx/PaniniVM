package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.SrDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

private val sutra131Sr = SrDhatu()
private val sutra131Meaning = Artha.Explanation("वात, मेघ अथवा वैदिक प्रयोग में मधुमक्षिका")

// 1.131: सर्तेरटिः
object SarterAtihSutra : UnadiSutra(
    number = "1.131",
    text = "सर्तेरटिः",
    roots = setOf(sutra131Sr),
    pratyaya = "अटि",
    pratyayaSurface = "अट्",
    itMarkers = setOf(ItMarker.I),
    rootSamjnaMap = mapOf(sutra131Sr.upadesha to Samjna.Rudhi("सरट्")),
    rootMeaningMap = mapOf(sutra131Sr.upadesha to sutra131Meaning),
    rootMorphology = mapOf(
        sutra131Sr.upadesha to UnadiRootMorphology(
            conditionedStem = "सृ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA))),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("सरट्".toVarnas(), meaning = sutra131Meaning, attributedSutra = "1.131"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = sutra131Meaning,
    hindiExplanation = "गतिअर्थक सृ धातु के बाद अटि प्रत्यय होता है। अटि का अन्तिम इ इत्संज्ञक होकर लुप्त होता है, इसलिए प्रत्यय का श्रव्य रूप अट् है। सृ के ऋ का गुण अर् होता है, जिसे ऋ → अ + र् के स्पष्ट Varna-विस्तार से रखा गया है। अतः सर् + अट् से सरट् बनता है। वृत्ति ‘सरट् स्याद्वातमेघयोः’ कहकर इसके वात और मेघ अर्थ देती है। उसी वृत्ति में ऋग्वेदभाष्य का ‘सरड्भ्यो मधुमक्षिकाभ्यः’ उदाहरण भी है: वहाँ मूल प्रातिपदिक सरट् ही है और भ्यस् से पहले ट् का जश्त्व होकर ड् दिखाई देता है। इसलिए सरड् को अलग derived word नहीं बनाया गया; मधुमक्षिका को सरट् का प्रमाणित वैदिक अर्थ रखा गया है। स्रोत इस consonant-stem का लिङ्ग स्पष्ट नहीं करता, इसलिए केवल अर्थ देखकर लिङ्ग अनुमान से नहीं जोड़ा गया।",
)
