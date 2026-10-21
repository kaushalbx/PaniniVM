package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama
import dev.panini.unadipatha.UnadiVarnaLopa
import dev.panini.unadipatha.UnadiVarnaSequenceLopa
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.53: अजिरशिशिरशिथिलस्थिरस्फिरस्थविरखदिराः
object AjiraSisiraSithilaSthiraSphiraSthaviraKhadirahSutra : UnadiSutra(
    number = "1.53",
    text = "अजिरशिशिरशिथिलस्थिरस्फिरस्थविरखदिराः",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0262" },
        BhvadiDhatus.all.single { it.id == "01.0827" },
        BhvadiDhatus.all.single { it.id == "01.0908" },
        BhvadiDhatus.all.single { it.id == "01.1077" },
        BhvadiDhatus.all.single { it.id == "01.0560" },
        BhvadiDhatus.all.single { it.id == "01.0052" },
        AdadiDhatus.all.single { it.id == "02.0026" },
    ),
    pratyaya = "किरच्",
    pratyayaSurface = "इर",
    itMarkers = setOf(ItMarker.KIT, ItMarker.CIT),
    rootSamjnaMap = mapOf(
        "अजँ" to Samjna.Rudhi("अजिर"),
        "शशँ" to Samjna.Rudhi("शिशिर"),
        "श्रथँ" to Samjna.Rudhi("शिथिल"),
        "स्फायीँ" to Samjna.Rudhi("स्फिर"),
        "खदँ" to Samjna.Rudhi("खदिर"),
        "शीङ्" to Samjna.Rudhi("शिबिर"),
    ),
    rootMeaningMap = mapOf(
        "अजँ" to Artha.Explanation("आँगन"),
        "शशँ" to Artha.Explanation("ऋतु-भेद, तुषार अथवा शीतल"),
        "स्फायीँ" to Artha.Explanation("बहुत अथवा प्रचुर"),
    ),
    rootMorphology = mapOf(
        "शशँ" to UnadiRootMorphology(
            conditionedStem = "शश्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.A, Svara.I)),
        ),
        "श्रथँ" to UnadiRootMorphology(
            conditionedStem = "श्रथ्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.A, Svara.I)),
            varnaLopas = listOf(UnadiVarnaLopa(Vyanjana.RA)),
            pratyayaVarnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.RA, Vyanjana.LA)),
        ),
        "स्फायीँ" to UnadiRootMorphology(
            conditionedStem = "स्फाय्".toVarnas(),
            varnaSequenceLopas = listOf(UnadiVarnaSequenceLopa("आय्".toVarnas())),
        ),
        "शीङ्" to UnadiRootMorphology(
            conditionedStem = "शी".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.II, Svara.I)),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.BA), UnadiAgamaPlacement.BEFORE_SUFFIX, "बुक्"),
            ),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            sourceRoot = "ष्ठा",
            output = "स्थिर".toVarnas(),
            meaning = Artha.Explanation("निश्चल"),
            morphology = UnadiRootMorphology(
                conditionedStem = "स्था".toVarnas(),
                varnaLopas = listOf(UnadiVarnaLopa(Svara.AA)),
            ),
        ),
        UnadiDerivedVariant(
            sourceRoot = "ष्ठा",
            output = "स्थविर".toVarnas(),
            morphology = UnadiRootMorphology(
                conditionedStem = "स्था".toVarnas(),
                varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.AA, Svara.A)),
                varnaAgamas = listOf(
                    UnadiVarnaAgama(listOf(Vyanjana.VA), UnadiAgamaPlacement.BEFORE_SUFFIX, "वुक्"),
                ),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("अजिर".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("आँगन"), "1.53"),
        UnadiLexicalAttestation("शिशिर".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("ऋतु-भेद, तुषार अथवा शीतल"), "1.53"),
        UnadiLexicalAttestation("शिथिल".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.53"),
        UnadiLexicalAttestation("स्थिर".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("निश्चल"), "1.53"),
        UnadiLexicalAttestation("स्फिर".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("बहुत अथवा प्रचुर"), "1.53"),
        UnadiLexicalAttestation("स्थविर".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.53"),
        UnadiLexicalAttestation("खदिर".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.53"),
        UnadiLexicalAttestation("शिबिर".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.53"),
    ),
    isNipatita = true,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("अजिर आदि सात निपातित किरच्-प्रत्ययान्त शब्द और वृत्ति से प्राप्त शिबिर इस नियम में अपने-अपने स्पष्ट रूपविधान सहित संगृहीत हैं।"),
    hindiExplanation = "यह सूत्र अजिर, शिशिर, शिथिल, स्थिर, स्फिर, स्थविर और खदिर—इन रूपों को किरच् प्रत्यय के साथ निपातित करता है; वृत्ति बाहुलक से शिबिर भी बताती है। किरच् में आरम्भिक क् तथा अन्तिम च् इत्संज्ञक होकर लुप्त होते हैं और इर शेष रहता है। अज् + इर से अजिर सीधे बनता है। शश् की उपधा अ का इत्त्व करके शिश् + इर से शिशिर बनता है। श्रथ् में उपधा अ का इत्त्व, र्-लोप और प्रत्यय के र् का लत्त्व होने से शिथ् + इल से शिथिल बनता है। स्था से स्थिर वाले पक्ष में टि अर्थात् अन्तिम आ का लोप है; स्फाय् से स्फिर में अन्तिम आय्-क्रम का टि-लोप है। स्थविर वाले पृथक् पक्ष में स्था के आकार का ह्रस्व अ और वुक् का प्रभावी व् प्रत्यय से पहले जुड़ता है, जिससे स्थविर सिद्ध होता है। खद् + इर से खदिर सीधे बनता है। बाहुलक से शीङ् धातु में ई का ह्रस्व इ और बुक् का प्रभावी ब् जोड़कर शिबिर बनता है। प्रत्येक परिवर्तन Varna अथवा Varna-क्रम के रूप में रखा गया है। वृत्ति अजिर को आँगन, शिशिर को ऋतु-भेद/तुषार/शीतल, स्थिर को निश्चल और स्फिर को प्रचुर बताती है। शिथिल, स्थविर, खदिर और शिबिर के अर्थ इस प्रविष्टि में नहीं दिए गए, अतः उनके परिचित किन्तु यहाँ अप्रमाणित अर्थ नहीं जोड़े गए हैं।",
)
