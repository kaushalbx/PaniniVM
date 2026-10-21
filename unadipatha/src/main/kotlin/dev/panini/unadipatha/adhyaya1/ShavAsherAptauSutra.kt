package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.svadi.SvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiUpapadaFormation

// 1.44: शावशेराप्तौ
object ShavAsherAptauSutra : UnadiSutra(
    number = "1.44",
    text = "शावशेराप्तौ",
    roots = setOf(SvadiDhatus.all.single { it.id == "05.0020" }),
    pratyaya = "उरन्",
    pratyayaSurface = "उर",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("अशूँ" to Samjna.Rudhi("श्वशुर")),
    rootMeaningMap = mapOf("अशूँ" to Artha.Rudhi("श्वशुर", "father of one's husband or wife")),
    applicationMeaning = Artha.Explanation("आप्त अर्थात् वैवाहिक सम्बन्धी"),
    upapadas = setOf("शु".toVarnas()),
    upapadaFormations = listOf(
        UnadiUpapadaFormation(
            source = "आशु".toVarnas(),
            output = "शु".toVarnas(),
            initialVarnaLopas = listOf(Svara.AA),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "श्वशुर".toVarnas(),
            lingas = setOf(Linga.PUMS),
            meaning = Artha.Rudhi("श्वशुर", "father of one's husband or wife"),
            attributedSutra = "1.44",
        ),
        UnadiLexicalAttestation(
            form = "श्वश्रू".toVarnas(),
            lingas = setOf(Linga.STRI),
            meaning = Artha.Rudhi("श्वश्रू", "mother of one's husband or wife"),
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("श्वशुर", "father of one's husband or wife"),
    hindiExplanation = "आप्त अर्थात् वैवाहिक सम्बन्धी के अर्थ में, शु उपपद के साथ व्याप्ति अर्थ वाले स्वादिगणीय अशूँ धातु के बाद उरन् प्रत्यय होता है। उरन् का अन्तिम न् इत्संज्ञक होकर लुप्त होता है और उर शेष रहता है। यहाँ शु, शीघ्र अर्थ वाले आशु शब्द का निपातित रूप है; आशु के आरम्भिक आ-वर्ण के लोप से शु प्राप्त होता है। " +
        "शु + अश् के संयोग में नियमित स्वर-सन्धि से श्वश् आधार और उर प्रत्यय के योग से श्वशुर प्रातिपदिक सिद्ध होता है। इसका अर्थ पति अथवा पत्नी का पिता है। वृत्ति साथ में श्वश्रू शब्द को पति अथवा पत्नी की माता के अर्थ में उद्धृत करती है; उसे lexical attestation के रूप में रखा गया है, इस उरन् प्रत्यय का प्रत्यक्ष समानान्तर output नहीं बनाया गया है।",
)
