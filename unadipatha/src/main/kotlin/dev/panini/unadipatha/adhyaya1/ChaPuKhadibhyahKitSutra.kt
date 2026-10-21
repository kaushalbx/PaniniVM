package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.121: छापूखडिभ्यः कित्
object ChaPuKhadibhyahKitSutra : UnadiSutra(
    number = "1.121",
    text = "छापूखडिभ्यः कित्",
    roots = setOf(
        DivadiDhatus.all.single { it.id == "04.0041" },
        BhvadiDhatus.all.single { it.id == "01.1121" },
        KryadiDhatus.all.single { it.id == "09.0014" },
        CuradiDhatus.all.single { it.id == "10.0065" },
        BhvadiDhatus.all.single { it.id == "01.0341" },
    ),
    pratyaya = "गन्",
    pratyayaSurface = "ग",
    itMarkers = setOf(ItMarker.KIT, ItMarker.N),
    rootSamjnaMap = mapOf(
        "छो" to Samjna.Rudhi("छाग"),
        "पूङ्" to Samjna.Rudhi("पूग"),
        "पूञ्" to Samjna.Rudhi("पूग"),
        "खडँ" to Samjna.Rudhi("खड्ग"),
        "षिटँ" to Samjna.Rudhi("षिड्ग"),
    ),
    rootMeaningMap = mapOf("षिटँ" to Artha.Explanation("तरल")),
    rootMorphology = mapOf(
        "छो" to UnadiRootMorphology(
            conditionedStem = "छो".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.O, Svara.AA)),
        ),
        "पूङ्" to UnadiRootMorphology(conditionedStem = "पू".toVarnas()),
        "पूञ्" to UnadiRootMorphology(conditionedStem = "पू".toVarnas()),
        "खडँ" to UnadiRootMorphology(conditionedStem = "खड्".toVarnas()),
        "षिटँ" to UnadiRootMorphology(
            conditionedStem = "षिट्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.TTA, Vyanjana.DDA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("छाग".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.121"),
        UnadiLexicalAttestation("पूग".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.121"),
        UnadiLexicalAttestation("खड्ग".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.121"),
        UnadiLexicalAttestation("षिड्ग".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("तरल"), "1.121"),
    ),
    relatedSutraNumbers = setOf("1.120"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("छाग, पूग, खड्ग और बाहुलक से षिड्ग कित् गन्-प्रत्ययान्त प्रातिपदिक हैं।"),
    hindiExplanation = "छो/छा, पू और खड् धातुओं के बाद पिछले सूत्र से गन् प्रत्यय आता है और इस सूत्र से वह कित् होता है। गन् का अन्तिम न् इत्संज्ञक होकर लुप्त होता है, इसलिए श्रव्य प्रत्यय ग है। छो की सूत्रनिर्दिष्ट छा-शाखा को ओ से आ के Varna-आदेश से दिखाया गया है; छा + ग से छाग बनता है। धातुपाठ में पू की भ्वादि पूङ् और क्र्यादि पूञ् दोनों प्रविष्टियाँ हैं और सूत्र गणभेद नहीं करता, इसलिए दोनों से पू + ग = पूग सुरक्षित है। खडँ के खड् आधार से खड्ग बनता है। वृत्ति बाहुलक से ‘षिट् अनादरे’ भी ग्रहण करती है: यहाँ सामान्य आरम्भिक ष् का स्-भाव नहीं होता और ष् बना रहता है; ग् से पहले ट् का जश्त्व ड् का Varna-आदेश देता है, अतः षिट् → षिड् और षिड् + ग = षिड्ग। वृत्ति षिड्गः का अर्थ तरल बताती है। अन्य तीन रूपों के कोशार्थ इस स्रोतांश में नहीं हैं, इसलिए वे नहीं गढ़े गए।",
)
