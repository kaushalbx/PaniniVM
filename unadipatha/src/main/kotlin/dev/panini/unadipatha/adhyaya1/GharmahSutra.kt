package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

private val sutra146GhrBhvadi = BhvadiDhatus.all.single { it.id == "01.1088" }
private val sutra146GhrJuhotyadi = JuhotyadiDhatus.all.single { it.id == "03.0015" }
private val sutra146GhrCuradi = CuradiDhatus.all.single { it.id == "10.0152" }

// 1.146: घर्मः
object GharmahSutra : UnadiSutra(
    number = "1.146",
    text = "घर्मः",
    roots = setOf(sutra146GhrBhvadi, sutra146GhrJuhotyadi, sutra146GhrCuradi),
    pratyaya = "मक्",
    pratyayaSurface = "म",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("घृ" to Samjna.Rudhi("घर्म")),
    rootMorphology = mapOf(
        "घृ" to UnadiRootMorphology(
            conditionedStem = "घृ".toVarnas(),
            varnaExpansions = listOf(
                UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA)),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("घर्म".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.146"),
    ),
    isNipatita = true,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "घृ धातु से मक् प्रत्यय और धातु के ऋ का गुण इस सूत्र में निपातित किया गया है। मक् का अन्तिम क् इत्संज्ञक होकर लुप्त होता है और श्रव्य प्रत्यय म रहता है। ऋ के गुण अर् को केवल label के रूप में नहीं, बल्कि ऋ → अ + र् के स्पष्ट Varna-विस्तार से रखा गया है; अतः घृ → घर् और घर् + म से घर्म प्रातिपदिक सिद्ध होता है। धातुपाठ में घृ की भ्वादि, जुहोत्यादि और चुरादि तीन समान उपदेश वाली प्रविष्टियाँ हैं तथा सूत्र कोई गण-प्रतिबन्ध नहीं देता, इसलिए तीनों को सुरक्षित रखा गया है। वृत्ति ‘घृधातोर्मक् गुणश्च निपात्यते’ कहकर प्रक्रिया बताती है और सूत्र ‘घर्मः’ रूप को पुल्लिङ्ग में उद्धृत करता है, पर इस स्थान पर कोई कोशार्थ नहीं देता; अतः परिचित उष्णता आदि अर्थ अनुमान से नहीं जोड़े गए।",
)
