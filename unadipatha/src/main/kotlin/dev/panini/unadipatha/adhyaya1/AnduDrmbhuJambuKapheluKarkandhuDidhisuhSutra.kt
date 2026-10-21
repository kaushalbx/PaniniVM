package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.divadi.JanDhatu
import dev.panini.dhatupatha.juhotyadi.DhaDhatu
import dev.panini.dhatupatha.tudadi.TudadiDhatus
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
import dev.panini.unadipatha.UnadiUpapadaFormation
import dev.panini.unadipatha.UnadiVarnaAgama
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.93: अन्दूदृम्भूजम्बूकफेलूकर्कन्धूदिधिषूः
object AnduDrmbhuJambuKapheluKarkandhuDidhisuhSutra : UnadiSutra(
    number = "1.93",
    text = "अन्दूदृम्भूजम्बूकफेलूकर्कन्धूदिधिषूः",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0064" },
        TudadiDhatus.all.single { it.id == "06.0048" },
        JanDhatu(),
        BhvadiDhatus.all.single { it.id == "01.0542" },
        AdadiDhatus.all.single { it.id == "02.0053" },
        DhaDhatu(),
        DivadiDhatus.all.single { it.id == "04.0042" },
        TudadiDhatus.all.single { it.id == "06.0039" },
        BhvadiDhatus.all.single { it.id == "01.0511" },
    ),
    pratyaya = "कू",
    pratyayaSurface = "ऊ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        "अदिँ" to Samjna.Rudhi("अन्दू"),
        "दृभीँ" to Samjna.Rudhi("दृम्भू"),
        "जनीँ" to Samjna.Rudhi("जम्बू"),
        "जमुँ" to Samjna.Rudhi("जम्बू"),
        "ला" to Samjna.Rudhi("कफेलू"),
        "डुधाञ्" to Samjna.Rudhi("कर्कन्धू"),
        "षो" to Samjna.Rudhi("दिधिषू"),
        "दृन्फँ" to Samjna.Rudhi("दृम्फू"),
        "कमुँ" to Samjna.Rudhi("कम्बू"),
    ),
    rootMeaningMap = mapOf(
        "अदिँ" to Artha.Explanation("बन्धन"),
        "ला" to Artha.Explanation("श्लेष्मातक"),
        "डुधाञ्" to Artha.Explanation("बदरी"),
        "षो" to Artha.Explanation("पुनर्भू"),
        "दृन्फँ" to Artha.Explanation("सर्पजाति"),
        "कमुँ" to Artha.Explanation("परद्रव्यापहारी"),
    ),
    rootMorphology = mapOf(
        "दृभीँ" to UnadiRootMorphology(
            conditionedStem = "दृभ्".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.NA), UnadiAgamaPlacement.AFTER_FINAL_ROOT_VOWEL, "नुम्"),
            ),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.MA)),
        ),
        "जनीँ" to UnadiRootMorphology(
            conditionedStem = "जन्".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.BA), UnadiAgamaPlacement.BEFORE_SUFFIX, "बुक्"),
            ),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.MA)),
        ),
        "जमुँ" to UnadiRootMorphology(
            conditionedStem = "जम्".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.BA), UnadiAgamaPlacement.BEFORE_SUFFIX, "बुक्"),
            ),
        ),
        "ला" to UnadiRootMorphology(
            conditionedStem = "ला".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.AA, Svara.E)),
        ),
        "कमुँ" to UnadiRootMorphology(
            conditionedStem = "कम्".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.BA), UnadiAgamaPlacement.BEFORE_SUFFIX, "बुक्"),
            ),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            output = "दृन्भू".toVarnas(),
            sourceRoot = "दृभीँ",
            morphology = UnadiRootMorphology(
                conditionedStem = "दृभ्".toVarnas(),
                varnaAgamas = listOf(
                    UnadiVarnaAgama(listOf(Vyanjana.NA), UnadiAgamaPlacement.AFTER_FINAL_ROOT_VOWEL, "नुम्"),
                ),
            ),
        ),
        UnadiDerivedVariant(
            output = "जम्बु".toVarnas(),
            sourceRoot = "जमुँ",
            meaning = Artha.Explanation("बाहुलक से ह्रस्व उ वाला रूप"),
        ),
        UnadiDerivedVariant(
            output = "दृम्फू".toVarnas(),
            sourceRoot = "दृन्फँ",
            meaning = Artha.Explanation("वैकल्पिक पाठ में सर्पजाति"),
        ),
        UnadiDerivedVariant(
            output = "कम्बू".toVarnas(),
            sourceRoot = "कमुँ",
            meaning = Artha.Explanation("वैकल्पिक पाठ में परद्रव्यापहारी"),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("अन्दू".toVarnas(), meaning = Artha.Explanation("बन्धन"), attributedSutra = "1.93"),
        UnadiLexicalAttestation("दृम्भू".toVarnas(), attributedSutra = "1.93"),
        UnadiLexicalAttestation("दृन्भू".toVarnas(), attributedSutra = "1.93"),
        UnadiLexicalAttestation("जम्बू".toVarnas(), attributedSutra = "1.93"),
        UnadiLexicalAttestation("जम्बु".toVarnas(), attributedSutra = "1.93"),
        UnadiLexicalAttestation("कफेलू".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("श्लेष्मातक"), "1.93"),
        UnadiLexicalAttestation("कर्कन्धू".toVarnas(), setOf(Linga.STRI), Artha.Explanation("बदरी"), "1.93"),
        UnadiLexicalAttestation("दिधिषू".toVarnas(), setOf(Linga.STRI), Artha.Explanation("पुनर्भू"), "1.93"),
        UnadiLexicalAttestation("दृम्फू".toVarnas(), setOf(Linga.STRI), Artha.Explanation("सर्पजाति"), "1.93"),
        UnadiLexicalAttestation("कम्बू".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("परद्रव्यापहारी"), "1.93"),
    ),
    upapadaFormations = listOf(
        UnadiUpapadaFormation(
            source = "कफम्".toVarnas(),
            output = "कफ".toVarnas(),
            finalVarnaLopas = listOf(Vyanjana.MA),
        ),
    ),
    relatedSutraNumbers = setOf("1.91"),
    isNipatita = true,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("अन्दू आदि रूप कू-प्रत्ययान्त निपातित प्रातिपदिक हैं; वृत्ति के मुख्य तथा वैकल्पिक पाठ दोनों सुरक्षित हैं।"),
    hindiExplanation = "यह सूत्र अन्दू, दृम्भू, जम्बू, कफेलू, कर्कन्धू और दिधिषू को पिछले सूत्र के कित् कू प्रत्यय से बने हुए निपातित रूप बताता है; अतः केवल वृत्ति से प्रमाणित प्रक्रियाएँ ही अलग operations में रखी गई हैं। अदिँ बन्धने से अन्दू ‘बन्धन’ मिलता है। दृभीँ ग्रन्थे में नुम् के न् का भ् से पहले परसवर्ण म् होकर दृम्भू बनता है; कुछ आचार्य परसवर्णाभाव वाला दृन्भू भी निपातन से मानते हैं। जनीँ में बुक् का ब् और न्→म् से जम्बू बनता है; जमुँ अदने से भी यही व्युत्पत्ति मानी गई है तथा बाहुलक से ह्रस्व जम्बु मिलता है। कफम् उपपद के अन्तिम म्-लोप और ला के निपातित एत्व से कफेलू ‘श्लेष्मातक’ है। कर्कं दधाति से कर्कन्धू ‘बदरी’ तथा दिधिं धैर्यं स्यति से दिधिषू ‘पुनर्भू’ हैं; इन दोनों की पूरी वर्ण-प्रक्रिया वृत्ति नहीं खोलती, इसलिए कृत्रिम आदेश नहीं गढ़े गए। वैकल्पिक पाठ में दृन्फँ से दृम्फू ‘सर्पजाति’ और कमुँ में बुक् से कम्बू ‘परद्रव्य चुराने वाला’ भी सुरक्षित हैं। जहाँ वृत्ति लिङ्ग या अर्थ नहीं बताती वहाँ वे अनुमान से नहीं जोड़े गए।",
)
