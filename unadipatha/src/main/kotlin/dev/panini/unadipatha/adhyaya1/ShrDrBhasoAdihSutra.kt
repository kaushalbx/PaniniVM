package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.dhatupatha.kryadi.DrDhatu
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra127Shr = KryadiDhatus.all.single { it.id == "09.0015" }
private val sutra127Dr = DrDhatu()
private val sutra127Bhas = JuhotyadiDhatus.all.single { it.id == "03.0019" }

// 1.127: शॄदॄभसोऽदिः
object ShrDrBhasoAdihSutra : UnadiSutra(
    number = "1.127",
    text = "शॄदॄभसोऽदिः",
    roots = setOf(sutra127Shr, sutra127Dr, sutra127Bhas),
    pratyaya = "अदि",
    pratyayaSurface = "अद्",
    itMarkers = setOf(ItMarker.I),
    rootSamjnaMap = mapOf(
        sutra127Shr.upadesha to Samjna.Rudhi("शरत्"),
        sutra127Dr.upadesha to Samjna.Rudhi("दरत्"),
        sutra127Bhas.upadesha to Samjna.Rudhi("भसत्"),
    ),
    rootMeaningMap = mapOf(
        sutra127Dr.upadesha to Artha.Explanation("हृदय अथवा कूल"),
        sutra127Bhas.upadesha to Artha.Explanation("जघन"),
    ),
    rootMorphology = mapOf(
        sutra127Shr.upadesha to adiMorphology("शॄ", hasLongR = true),
        sutra127Dr.upadesha to adiMorphology("दॄ", hasLongR = true),
        sutra127Bhas.upadesha to adiMorphology("भस्", hasLongR = false),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("शरत्".toVarnas(), setOf(Linga.STRI), attributedSutra = "1.127"),
        UnadiLexicalAttestation("दरत्".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("हृदय अथवा कूल"), "1.127"),
        UnadiLexicalAttestation("भसत्".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("जघन"), "1.127"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("शॄ, दॄ और भस् से अदि प्रत्यय लगकर शरत्, दरत् और भसत् प्रातिपदिक बनते हैं।"),
    hindiExplanation = "शॄ, दॄ और भस् धातुओं के बाद अदि प्रत्यय होता है। अदि का अन्तिम इ इत्संज्ञक होकर लुप्त होता है, इसलिए प्रत्यय का रूप अद् रहता है। शॄ और दॄ के ॠ का गुण अर् होता है; इसे ॠ → अ + र् के Varna-विस्तार से model किया गया है। प्रत्यय का पदान्त द् त् में बदलने पर शर् + अद् → शरत्, दर् + अद् → दरत् और भस् + अद् → भसत् बनते हैं। वृत्ति में ‘दरद्धृदयकूलयोः’ दरत् के बाद हृदय शब्द आने से बना संहितापाठ है; उसका मूल रूप दरत् ही है। इसी प्रकार ‘भसज्जघनम्’ भसत् + जघनम् की संधि है और मूल प्रातिपदिक भसत् है। वृत्ति दरत् को हृदय अथवा कूल और भसत् को जघन के अर्थ में रखती है। शरत् का कोई अर्थ इस संक्षिप्त वृत्ति में नहीं दिया गया, इसलिए उसके लिए अर्थ अनुमान से नहीं जोड़ा गया।",
)

private fun adiMorphology(stem: String, hasLongR: Boolean): UnadiRootMorphology = UnadiRootMorphology(
    conditionedStem = stem.toVarnas(),
    varnaExpansions = if (hasLongR) {
        listOf(UnadiVarnaExpansion(Svara.RR, listOf(Svara.A, Vyanjana.RA)))
    } else {
        emptyList()
    },
    varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.DA, Vyanjana.TA)),
)
