package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
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
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.92: ऋतेरम् च = ऋतेः अम् च
object RterAmCaSutra : UnadiSutra(
    number = "1.92",
    text = "ऋतेरम् च",
    roots = emptySet(),
    rootReferences = setOf("ऋति"),
    pratyaya = "कू",
    pratyayaSurface = "ऊ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("ऋति" to Samjna.Rudhi("रन्तू")),
    rootMeaningMap = mapOf("ऋति" to Artha.Explanation("देवनदी अथवा सत्यवाक्")),
    rootMorphology = mapOf(
        "ऋति" to UnadiRootMorphology(
            // वृत्ति ऋति को सौत्र और तकारान्त धातु बताती है; इ धात्वंश नहीं है।
            conditionedStem = "ऋत्".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(
                    varnas = "अम्".toVarnas(),
                    placement = UnadiAgamaPlacement.AFTER_FINAL_ROOT_VOWEL,
                    upadesha = "अम्",
                ),
            ),
            varnaSubstitutions = listOf(
                UnadiVarnaSubstitution(Svara.R, Vyanjana.RA),
                UnadiVarnaSubstitution(Vyanjana.MA, Vyanjana.NA),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("रन्तू".toVarnas(), setOf(Linga.STRI), Artha.Explanation("देवनदी"), "1.92"),
        UnadiLexicalAttestation("रन्तू".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("सत्यवाक् अर्थात् सत्य बोलने वाला"), "1.92"),
    ),
    relatedSutraNumbers = setOf("1.91", "6.1.77", "8.4.58"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("रन्तू कित् कू-प्रत्ययान्त प्रातिपदिक है और देवनदी अथवा सत्यवाक् का वाचक है।"),
    hindiExplanation = "वृत्ति ‘ऋतिः सौत्रो धातुः’ कहती है, इसलिए धातुपाठ की किसी मिलती-जुलती प्रविष्टि को आरोपित न करके ऋति को अलग सौत्र धातु-सन्दर्भ रखा गया है। यहाँ धातु तकारान्त ऋत् है; अन्त का इ केवल निर्देशार्थ है। पिछले सूत्र से कित् कू प्रत्यय मिलता है, जिसका क् लुप्त होकर ऊ शेष रहता है। सूत्र-पाठ ‘ऋतेरम्’ का पदच्छेद ‘ऋतेः अम्’ है और वृत्ति का सन्धियुक्त ‘कूरमागमः’ भी ‘कूः अम्-आगमः’ बताता है। अम् धातु के ऋ-वर्ण के बाद आता है; स्वर के सामने ऋ का यण् र् और त् के पहले म् का परसवर्ण न् होने पर रन्त् आधार बनता है। उसमें ऊ जुड़कर रन्तू सिद्ध होता है। यह स्त्रीलिङ्ग में देवनदी और पुल्लिङ्ग में सत्य बोलने वाले का वाचक है।",
)
