package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.108: चुपेरच्चोपधायाः
object CuperAcCopadhayahSutra : UnadiSutra(
    number = "1.108",
    text = "चुपेरच्चोपधायाः",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0469" }),
    pratyaya = "कल",
    pratyayaSurface = "अल",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("चुपँ" to Samjna.Rudhi("चपल")),
    rootMorphology = mapOf(
        "चुपँ" to UnadiRootMorphology(
            conditionedStem = "चुप्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.U, Svara.A)),
        ),
    ),
    lexicalAttestations = listOf(UnadiLexicalAttestation("चपल".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.108")),
    relatedSutraNumbers = setOf("1.104"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("चपल कित् कल-प्रत्ययान्त नपुंसकलिङ्ग प्रातिपदिक है।"),
    hindiExplanation = "चुपँ धातु के चुप् आधार के बाद अनुवृत्त कित् कल प्रत्यय आता है और क् के इत्संज्ञा-लोप से अल शेष रहता है। ‘अच्चोपधायाः’ चुप् की उपधा उ के स्थान पर अ का स्पष्ट Varna-आदेश करता है। इस प्रकार चुप् → चप् और चप् + अल से चपल बनता है। वृत्ति केवल ‘चपलम्’ रूप देती है, अतः नपुंसकलिङ्ग रखा गया है और कोई अनिर्दिष्ट अर्थ नहीं जोड़ा गया।",
)
