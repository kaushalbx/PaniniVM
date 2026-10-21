package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Accent
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama
import dev.panini.unadipatha.UnadiVarnaLopa

// 1.15: सृजेरसुम् च
object SrjerAsumCaSutra : UnadiSutra(
    number = "1.15",
    text = "सृजेरसुम् च",
    roots = setOf(DivadiDhatus.all.single { it.id == "04.0075" }),
    pratyaya = "उ",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("सृजँ" to Samjna.Rudhi("रज्जु")),
    rootMeaningMap = mapOf("सृजँ" to Artha.Rudhi("रज्जु", "rope, braid, or cord")),
    // म् is the it-marker of असुम्; the operative augment is अस् and is inserted after the final root vowel.
    varnaAgamas = listOf(
        UnadiVarnaAgama(
            varnas = listOf(Svara.A, Vyanjana.SA),
            placement = UnadiAgamaPlacement.AFTER_FINAL_ROOT_VOWEL,
        ),
    ),
    // सलोपः continues from 1.14 and removes the initial स् of सृज्.
    varnaLopas = listOf(UnadiVarnaLopa(Vyanjana.SA)),
    assignedAccent = Accent.UDATTA,
    accentVowelIndex = 0,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("रज्जु", "rope, braid, or cord"),
    hindiExplanation = "सृजँ धातु के बाद उ प्रत्यय होता है। पूर्वसूत्र से स्-लोप की अनुवृत्ति होने के कारण धातु का आरम्भिक स् लुप्त होता है। सूत्र का असुम् मित्-आगम है; म् इत्संज्ञक होकर लुप्त होता है और शेष अस् धातु के अन्तिम स्वर ऋ के बाद रखा जाता है। " +
        "इसके बाद सामान्य यणादेश से ऋ के स्थान पर र् तथा आगामी ज् के प्रभाव से अस् का स् ज् होकर रज्जु रूप सिद्ध होता है। १.९ से अनुवृत्त नित् के कारण प्रथम स्वर उदात्त है। रज्जु का रूढ़ अर्थ रस्सी, वेणी अथवा डोरी है।",
)
