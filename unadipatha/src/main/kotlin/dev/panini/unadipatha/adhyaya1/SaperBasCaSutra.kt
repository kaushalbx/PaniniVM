package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.105: शपेर्बश्च
object SaperBasCaSutra : UnadiSutra(
    number = "1.105",
    text = "शपेर्बश्च",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.1155" },
        DivadiDhatus.all.single { it.id == "04.0064" },
    ),
    pratyaya = "कल",
    pratyayaSurface = "अल",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("शपँ" to Samjna.Rudhi("शबल")),
    rootMorphology = mapOf(
        "शपँ" to UnadiRootMorphology(
            conditionedStem = "शप्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.PA, Vyanjana.BA)),
        ),
    ),
    lexicalAttestations = listOf(UnadiLexicalAttestation("शबल".toVarnas(), attributedSutra = "1.105")),
    relatedSutraNumbers = setOf("1.104"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("शबल कित् कल-प्रत्ययान्त प्रातिपदिक है।"),
    hindiExplanation = "शपँ धातु के बाद पिछले सूत्र से कित् कल प्रत्यय आता है; प्रारम्भिक क् इत्संज्ञक होकर लुप्त होता है और अल शेष रहता है। सूत्र का ‘बश्च’ शप् आधार के अन्तिम प् के स्थान पर ब् का स्पष्ट Varna-आदेश करता है। इससे शप् आधार शब् बनता है और शब् + अल से शबल प्रातिपदिक सिद्ध होता है। धातुपाठ में आक्रोशार्थक शपँ की भ्वादि और दिवादि दोनों समान प्रविष्टियाँ हैं तथा सूत्र गणभेद नहीं करता, इसलिए दोनों सुरक्षित हैं। वृत्ति केवल रूप देती है; अर्थ और लिङ्ग अनुमान से नहीं जोड़े गए।",
)
