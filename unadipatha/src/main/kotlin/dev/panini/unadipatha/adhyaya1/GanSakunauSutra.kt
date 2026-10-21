package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama
import dev.panini.unadipatha.UnadiVarnaExpansion
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.124: गण् शकुनौ
object GanSakunauSutra : UnadiSutra(
    number = "1.124",
    text = "गण् शकुनौ",
    roots = setOf(KryadiDhatus.all.single { it.id == "09.0015" }),
    pratyaya = "गण्",
    pratyayaSurface = "ग",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("शॄञ्" to Samjna.Rudhi("शार्ङ्ग")),
    applicationMeaning = Artha.Explanation("शकुन अर्थात् पक्षी के अर्थ में"),
    rootMeaningMap = mapOf("शॄञ्" to Artha.Explanation("पक्षी")),
    rootMorphology = mapOf(
        "शॄञ्" to UnadiRootMorphology(
            conditionedStem = "शॄ".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.NA), UnadiAgamaPlacement.BEFORE_SUFFIX, "नुट्"),
            ),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.RR, listOf(Svara.AA, Vyanjana.RA))),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.NGA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("शार्ङ्ग".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("पक्षी"), "1.124"),
    ),
    relatedSutraNumbers = setOf("1.122", "1.123"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("शार्ङ्ग शकुन-अर्थ में नुट्-सहित गण्-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है।"),
    hindiExplanation = "शॄञ् धातु से शकुन अर्थात् पक्षी के अर्थ में गण् प्रत्यय होता है। गण् का अन्तिम ण् इत्संज्ञक होकर लुप्त होता है, अतः श्रव्य प्रत्यय ग है; णित्त्व के कारण धातु के ॠ की वृद्धि आर् होती है, जिसे ॠ → आ + र् के Varna-विस्तार के रूप में रखा गया है। वृत्ति स्पष्ट करती है कि पिछले सूत्र से ‘नुट् च’ की अनुवृत्ति होती है। इसलिए शार् आधार और ग् के बीच नुट् का न् आता है; अगले कण्ठ्य ग् के कारण न् परसवर्ण ङ् में बदलता है। इस प्रकार शार् + ङ् + ग से शार्ङ्ग बनता है। ‘शकुनौ’ को पृथक् applicationMeaning बनाया गया है और वृत्ति के ‘शार्ङ्गः’ के अनुसार पुल्लिङ्ग रखा गया है। पक्षी से आगे कोई विशिष्ट जाति स्रोत में नहीं कही गई, इसलिए वह नहीं जोड़ी गई।",
)
