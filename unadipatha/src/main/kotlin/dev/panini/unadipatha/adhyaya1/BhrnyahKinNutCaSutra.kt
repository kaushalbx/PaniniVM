package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.122: भृञः किन्नुट् च
object BhrnyahKinNutCaSutra : UnadiSutra(
    number = "1.122",
    text = "भृञः किन्नुट् च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.1045" }),
    pratyaya = "गन्",
    pratyayaSurface = "ग",
    itMarkers = setOf(ItMarker.KIT, ItMarker.N),
    rootSamjnaMap = mapOf("भृञ्" to Samjna.Rudhi("भृङ्ग")),
    rootMeaningMap = mapOf("भृञ्" to Artha.Explanation("षिड्ग, अलि और धूम्याट का पर्याय")),
    rootMorphology = mapOf(
        "भृञ्" to UnadiRootMorphology(
            conditionedStem = "भृ".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.NA), UnadiAgamaPlacement.BEFORE_SUFFIX, "नुट्"),
            ),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.NGA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            "भृङ्ग".toVarnas(),
            setOf(Linga.PUMS),
            Artha.Explanation("षिड्ग, अलि और धूम्याट का पर्याय"),
            "1.122",
        ),
    ),
    relatedSutraNumbers = setOf("1.120", "1.121"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("भृङ्ग नुट्-सहित कित् गन्-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है।"),
    hindiExplanation = "भृञ् धातु के बाद पिछले सूत्र से गन् प्रत्यय आता है; इस सूत्र का ‘किन्’ उसे कित् करता है और ‘नुट् च’ नुट् आगम भी देता है। गन् के अन्तिम न् का इत्संज्ञा-लोप होने पर प्रत्यय का श्रव्य रूप ग है। भृञ् के भृ आधार और ग्-प्रत्यय के बीच नुट् का उ तथा ट् इत्संज्ञक होकर न्-वर्ण बचता है। अगले कण्ठ्य ग् के कारण यह न् परसवर्ण ङ् में बदलता है; इसे न् → ङ् के स्पष्ट Varna-आदेश के रूप में रखा गया है। अतः भृ + ङ् + ग से भृङ्ग बनता है। वृत्ति ‘भृङ्गाः षिड्गालिधूम्याटाः’ कहकर षिड्ग, अलि और धूम्याट को भृङ्ग के पर्याय रूप में देती है; वे इस सूत्र के अलग derived outputs नहीं हैं। भृङ्ग पुल्लिङ्ग में अभिलेखित है और इससे परे कोई अतिरिक्त अर्थ नहीं गढ़ा गया।",
)
