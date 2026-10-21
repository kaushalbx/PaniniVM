package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSecondaryDerivative
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.110: छो गुक् ह्रस्वश्च
object ChoGukHrasvasCaSutra : UnadiSutra(
    number = "1.110",
    text = "छो गुक् ह्रस्वश्च",
    roots = setOf(DivadiDhatus.all.single { it.id == "04.0041" }),
    pratyaya = "कल",
    pratyayaSurface = "अल",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("छो" to Samjna.Rudhi("छगल")),
    rootMorphology = mapOf(
        "छो" to UnadiRootMorphology(
            conditionedStem = "छो".toVarnas(),
            varnaAgamas = listOf(UnadiVarnaAgama(listOf(Vyanjana.GA), UnadiAgamaPlacement.BEFORE_SUFFIX, "गुक्")),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.O, Svara.A)),
        ),
    ),
    secondaryDerivatives = listOf(
        UnadiSecondaryDerivative("छगल".toVarnas(), "प्रज्ञादि-अण्", "छागल".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("छगल".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.110"),
        UnadiLexicalAttestation("छागल".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.110"),
    ),
    relatedSutraNumbers = setOf("1.104"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("छगल कल-प्रत्ययान्त प्रातिपदिक है; प्रज्ञादि-व्युत्पत्ति से छागल भी प्रमाणित है।"),
    hindiExplanation = "छो धातु के बाद अनुवृत्त कित् कल प्रत्यय होता है। कल के क् का इत्संज्ञा-लोप होने पर अल शेष रहता है। सूत्र ‘गुक्’ आगम देता है; उसके उ और क् इत् होकर ग्-वर्ण धातु तथा प्रत्यय के बीच आता है। ‘ह्रस्वश्च’ से छो के ओ का ह्रस्व अादेश अ होता है। अतः छो → छ, फिर छ + ग् + अल से छगल सिद्ध होता है। वृत्ति ‘प्रज्ञादित्वाच्छागलः’ कहकर प्रज्ञादिगण के परवर्ती तद्धित-विधान से छागल रूप भी देती है; उसे मूल कल-निर्माण में मिलाने के बजाय पृथक् secondary derivative रखा गया है। स्रोत दोनों रूप पुल्लिङ्ग में उद्धृत करता है, पर यहाँ उनका कोई अलग कोशार्थ नहीं बताता।",
)
