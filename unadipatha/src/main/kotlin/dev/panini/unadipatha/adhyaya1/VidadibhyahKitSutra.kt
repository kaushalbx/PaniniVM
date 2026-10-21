package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

// 1.118: विडादिभ्यः कित्
object VidadibhyahKitSutra : UnadiSutra(
    number = "1.118",
    text = "विडादिभ्यः कित्",
    roots = setOf(KryadiDhatus.all.single { it.id == "09.0051" }, KruDhatu()),
    rootReferences = setOf("विड"),
    pratyaya = "अङ्गच्",
    pratyayaSurface = "अङ्ग",
    itMarkers = setOf(ItMarker.KIT, ItMarker.C),
    rootSamjnaMap = mapOf(
        "विड" to Samjna.Rudhi("विडङ्ग"),
        "मृदँ" to Samjna.Rudhi("मृदङ्ग"),
        "कृ" to Samjna.Rudhi("कुरङ्ग"),
    ),
    rootMorphology = mapOf(
        "विड" to UnadiRootMorphology(conditionedStem = "विड्".toVarnas()),
        "मृदँ" to UnadiRootMorphology(conditionedStem = "मृद्".toVarnas()),
        "कृ" to UnadiRootMorphology(
            conditionedStem = "कृ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.U, Vyanjana.RA))),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("विडङ्ग".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.118"),
        UnadiLexicalAttestation("मृदङ्ग".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.118"),
        UnadiLexicalAttestation("कुरङ्ग".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.118"),
    ),
    relatedSutraNumbers = setOf("1.116", "1.117"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("विडङ्ग, मृदङ्ग और कुरङ्ग कित् अङ्गच्-प्रत्ययान्त प्रातिपदिक हैं।"),
    hindiExplanation = "विडादिगण के धातुओं के बाद पिछले सूत्रों से अनुवृत्त अङ्गच् प्रत्यय होता है और इस सूत्र से वह कित् बनता है। अतः अङ्गच् में क् की कित्संज्ञा है तथा अन्तिम च् भी इत्संज्ञक होकर लुप्त होता है; प्रत्यय का श्रव्य रूप अङ्ग है। स्रोत में विड को सौत्र textual root के रूप में रखकर विड् + अङ्ग से विडङ्ग बनता है। मृदँ के मृद् आधार से मृदङ्ग बनता है। कृ धातु की कुरङ्ग-शाखा में वृत्ति का ‘बाहुलकादुत्वं च’ ऋ को उ में बदलता और रपरत्व से उ + र् का Varna-विस्तार देता है; इससे कुर् + अङ्ग = कुरङ्ग सिद्ध होता है। विड की असंदिग्ध Dhātupāṭha entry उपलब्ध न होने से उसके लिए कोई काल्पनिक ID नहीं चुनी गई। वृत्ति तीनों रूप पुल्लिङ्ग में देती है, पर यहाँ उनके कोशार्थ नहीं बताती; इसलिए कोई lexical अर्थ नहीं जोड़ा गया।",
)
