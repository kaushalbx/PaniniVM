package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.bhvadi.SrDhatu
import dev.panini.dhatupatha.divadi.DivadiDhatus
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
import dev.panini.unadipatha.UnadiVarnaExpansion
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val vrshadiMutAgama = UnadiVarnaAgama(
    listOf(Vyanjana.MA),
    UnadiAgamaPlacement.BEFORE_SUFFIX,
    "मुट्",
)

private fun vrshadiMutMorphology(stem: String) = UnadiRootMorphology(
    conditionedStem = stem.toVarnas(),
    varnaAgamas = listOf(vrshadiMutAgama),
)

private fun vrshadiVariant(root: String, output: String) =
    UnadiDerivedVariant(sourceRoot = root, output = output.toVarnas())

private fun vrshadiAttestation(form: String, linga: Linga) =
    UnadiLexicalAttestation(form.toVarnas(), setOf(linga), attributedSutra = "1.106")

// 1.106: वृषादिभ्यश्चित्
object VrshadibhyasCitSutra : UnadiSutra(
    number = "1.106",
    text = "वृषादिभ्यश्चित्",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0803" }, // वृषुँ
        BhvadiDhatus.all.single { it.id == "01.0973" }, // पलँ
        SrDhatu(),                                      // सृ
        BhvadiDhatus.all.single { it.id == "01.1124" }, // तॄ
        BhvadiDhatus.all.single { it.id == "01.0511" }, // कमुँ
        DivadiDhatus.all.single { it.id == "04.0130" }, // मुसँ खण्डने
        BhvadiDhatus.all.single { it.id == "01.0154" }, // लगिँ (लङ्ग्)
    ),
    rootReferences = setOf("कुटि", "कुडि", "कशि", "कौति"),
    pratyaya = "कल",
    pratyayaSurface = "अल",
    itMarkers = setOf(ItMarker.KIT, ItMarker.CIT),
    rootSamjnaMap = mapOf(
        "वृषुँ" to Samjna.Rudhi("वृषल"),
        "पलँ" to Samjna.Rudhi("पलल"),
        "सृ" to Samjna.Rudhi("सरल"),
        "तॄ" to Samjna.Rudhi("तरल"),
        "कमुँ" to Samjna.Rudhi("कम्बल"),
        "मुसँ" to Samjna.Rudhi("मुसल"),
        "लगिँ" to Samjna.Rudhi("लाङ्गल"),
        "कुटि" to Samjna.Rudhi("कुट्मल"),
        "कुडि" to Samjna.Rudhi("कुड्मल"),
        "कशि" to Samjna.Rudhi("कश्मल"),
        "कौति" to Samjna.Rudhi("कोमल"),
    ),
    rootMorphology = mapOf(
        "वृषुँ" to UnadiRootMorphology(conditionedStem = "वृष्".toVarnas()),
        "पलँ" to UnadiRootMorphology(conditionedStem = "पल्".toVarnas()),
        "सृ" to UnadiRootMorphology(
            conditionedStem = "सृ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA))),
        ),
        "तॄ" to UnadiRootMorphology(
            conditionedStem = "तॄ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.RR, listOf(Svara.A, Vyanjana.RA))),
        ),
        "कमुँ" to UnadiRootMorphology(
            conditionedStem = "कम्".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.BA), UnadiAgamaPlacement.BEFORE_SUFFIX, "बुक्"),
            ),
        ),
        "मुसँ" to UnadiRootMorphology(conditionedStem = "मुस्".toVarnas()),
        "लगिँ" to UnadiRootMorphology(
            conditionedStem = "लङ्ग्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.A, Svara.AA)),
        ),
        "कुटि" to vrshadiMutMorphology("कुट्"),
        "कुडि" to vrshadiMutMorphology("कुड्"),
        "कशि" to vrshadiMutMorphology("कश्"),
        "कौति" to UnadiRootMorphology(
            conditionedStem = "कु".toVarnas(),
            varnaAgamas = listOf(vrshadiMutAgama),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.U, Svara.O)),
        ),
    ),
    derivedVariants = listOf(
        vrshadiVariant("वृषुँ", "वृषल"), vrshadiVariant("पलँ", "पलल"),
        vrshadiVariant("सृ", "सरल"), vrshadiVariant("तॄ", "तरल"),
        vrshadiVariant("कमुँ", "कम्बल"), vrshadiVariant("मुसँ", "मुसल"),
        vrshadiVariant("लगिँ", "लाङ्गल"), vrshadiVariant("कुटि", "कुट्मल"),
        vrshadiVariant("कुडि", "कुड्मल"), vrshadiVariant("कशि", "कश्मल"),
        vrshadiVariant("कौति", "कोमल"),
    ),
    lexicalAttestations = listOf(
        vrshadiAttestation("वृषल", Linga.PUMS), vrshadiAttestation("पलल", Linga.NAPUMSAKA),
        vrshadiAttestation("सरल", Linga.PUMS), vrshadiAttestation("तरल", Linga.PUMS),
        vrshadiAttestation("कम्बल", Linga.PUMS), vrshadiAttestation("मुसल", Linga.NAPUMSAKA),
        vrshadiAttestation("लाङ्गल", Linga.NAPUMSAKA), vrshadiAttestation("कुट्मल", Linga.PUMS),
        vrshadiAttestation("कुड्मल", Linga.PUMS), vrshadiAttestation("कश्मल", Linga.NAPUMSAKA),
        vrshadiAttestation("कोमल", Linga.NAPUMSAKA),
    ),
    relatedSutraNumbers = setOf("1.104", "1.105"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("वृषल आदि चित्-कित् कल-प्रत्ययान्त प्रातिपदिक हैं।"),
    hindiExplanation = "वृषादिगण के धातुओं के बाद कल प्रत्यय होता है और इस सूत्र का ‘चित्’ प्रत्यय को चित् बनाता है। कल का क् इत्संज्ञक होकर लुप्त होता है, अतः सुनाई देने वाला अंश अल है। वृष् + अल से वृषल और पल् + अल से पलल बनते हैं। बाहुलक से सृ और तॄ में गुण होकर क्रमशः सर् तथा तर् आधार मिलते हैं, जिनसे सरल और तरल सिद्ध होते हैं। गणसूत्र १९६ ‘कमेर्बुक् च’ के अनुसार कम् और अल के बीच बुक् का ब्-वर्ण आता है: कम् + ब् + अल = कम्बल। मुसँ खण्डने से मुस् + अल = मुसल होता है। गणसूत्र १९७ ‘लङ्गेर्वृद्धिश्च’ लङ्ग् के अ की वृद्धि आ करता है, इसलिए लाङ्गल बनता है। गणसूत्र १९८ ‘कुटिकशिकौतिभ्यः प्रत्ययस्य मुट्’ में प्रत्यय से पहले मुट् का म्-वर्ण जुड़ता है; इससे कुट्मल, कश्मल तथा कौति-शाखा में बाहुलक-गुण सहित कोमल मिलते हैं। वृत्ति ‘कुडेरपि’ कहकर कुड्मल भी देती है। कुटि, कुडि, कशि और कौति को असंदिग्ध Dhātupāṭha प्रविष्टि से जबरन नहीं जोड़ा गया; वे स्रोत के ठीक textual root-references हैं। वृत्ति इन ग्यारह शब्दों के कोशार्थ नहीं बताती, इसलिए कोई कल्पित lexical अर्थ नहीं जोड़ा गया; उसके पुल्लिङ्ग/नपुंसकलिङ्ग citation-रूपों को ही attestations में सुरक्षित किया गया है।",
)
