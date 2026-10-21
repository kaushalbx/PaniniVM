package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.bhvadi.GamDhatu
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.juhotyadi.DhaDhatu
import dev.panini.dhatupatha.svadi.SvadiDhatus
import dev.panini.dhatupatha.tanadi.TanDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.69: सितनिगमिमसिसच्यविधाञ्क्रुशिभ्यस्तुन्
object SiTaniGamiMasiSaciAviDhanKrusibhyasTunSutra : UnadiSutra(
    number = "1.69",
    text = "सितनिगमिमसिसच्यविधाञ्क्रुशिभ्यस्तुन्",
    roots = setOf(
        SvadiDhatus.all.single { it.id == "05.0002" },
        TanDhatu(),
        GamDhatu(),
        DivadiDhatus.all.single { it.id == "04.0131" },
        BhvadiDhatus.all.single { it.id == "01.0187" },
        BhvadiDhatus.all.single { it.id == "01.0684" },
        DhaDhatu(),
        BhvadiDhatus.all.single { it.id == "01.0992" },
    ),
    pratyaya = "तुन्",
    pratyayaSurface = "तु",
    itMarkers = setOf(ItMarker.NIT),
    blocksItAgama = true,
    rootSamjnaMap = mapOf(
        "षिञ्" to Samjna.Rudhi("सेतु"),
        "तनुँ" to Samjna.Rudhi("तन्तु"),
        "गमॢँ" to Samjna.Rudhi("गन्तु"),
        "मसीँ" to Samjna.Rudhi("मस्तु"),
        "षचँ" to Samjna.Rudhi("सक्तु"),
        "अवँ" to Samjna.Rudhi("ओतु"),
        "डुधाञ्" to Samjna.Rudhi("धातु"),
        "क्रुशँ" to Samjna.Rudhi("क्रोष्टु"),
    ),
    rootMeaningMap = mapOf(
        "षिञ्" to Artha.Explanation("जो बाँधता है"),
        "मसीँ" to Artha.Explanation("दधिमण्ड"),
        "अवँ" to Artha.Explanation("बिडाल अर्थात् बिल्ली"),
        "क्रुशँ" to Artha.Explanation("क्रोष्टा अर्थात् सियार"),
    ),
    rootMorphology = mapOf(
        "षिञ्" to UnadiRootMorphology(
            conditionedStem = "सि".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.I, Svara.E)),
        ),
        "गमॢँ" to UnadiRootMorphology(
            conditionedStem = "गम्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.MA, Vyanjana.NA)),
        ),
        "षचँ" to UnadiRootMorphology(
            conditionedStem = "सच्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.CA, Vyanjana.KA)),
        ),
        "अवँ" to UnadiRootMorphology(
            conditionedStem = "अव्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.VA, Svara.UU)),
        ),
        "क्रुशँ" to UnadiRootMorphology(
            conditionedStem = "क्रुश्".toVarnas(),
            varnaSubstitutions = listOf(
                UnadiVarnaSubstitution(Svara.U, Svara.O),
                UnadiVarnaSubstitution(Vyanjana.SHA, Vyanjana.SSA),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("सेतु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("जो बाँधता है"), "1.69"),
        UnadiLexicalAttestation("तन्तु".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.69"),
        UnadiLexicalAttestation("गन्तु".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.69"),
        UnadiLexicalAttestation("मस्तु".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("दधिमण्ड"), "1.69"),
        UnadiLexicalAttestation("सक्तु".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.69"),
        UnadiLexicalAttestation("ओतु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("बिडाल अर्थात् बिल्ली"), "1.69"),
        UnadiLexicalAttestation("धातु".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.69"),
        UnadiLexicalAttestation("क्रोष्टु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("क्रोष्टा अर्थात् सियार"), "1.69"),
    ),
    relatedSutraNumbers = setOf("7.2.9", "6.4.20", "7.1.95"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("सेतु आदि आठ तुन्-प्रत्ययान्त प्रातिपदिक अपने वृत्तिप्रदत्त रूप और अर्थों में सिद्ध होते हैं।"),
    hindiExplanation = "सूत्र सि, तनि, गमि, मसि, सचि, अवि, धाञ् और क्रुशि—इन आठ धातुओं के बाद तुन् प्रत्यय करता है। तुन् का अन्तिम न् इत्संज्ञक होकर लुप्त होता है और तु शेष रहता है; 7.2.9 ‘तितुत्र...’ से इस तु-प्रत्यय के पहले इट्-आगम निषिद्ध है। षिञ् के सि आधार में इ का गुण ए होकर सेतु बनता है, जिसका निर्वचन ‘जो बाँधता है’ है। तन् + तु से तन्तु, गम् के म् का त् के सामने न् होकर गन्तु, मस् + तु से मस्तु और सच् के च् का कुत्व होकर सक्तु बनता है। वृत्ति मस्तु को दधिमण्ड बताती है। अव् के व् को 6.4.20 से ऊठ् अर्थात् ऊ-आदेश मिलता है; अ + ऊ के गुण से ओ आधार और तु के योग से ओतु बनता है, जिसका अर्थ बिडाल है। धा + तु से धातु बनता है। क्रुश् में उ का गुण ओ, श् का ष् और ष् + त् का ष्ट् होकर क्रोष्टु बनता है; 7.1.95 इस शब्द के विशेष विभक्ति-व्यवहार की पुष्टि करता है और वृत्ति में इसका अर्थ क्रोष्टा अर्थात् सियार है। जिन रूपों का पृथक् अर्थ वृत्ति में नहीं है, उनके अर्थ अनुमान से नहीं जोड़े गए।",
)
