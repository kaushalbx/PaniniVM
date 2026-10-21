package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Accent
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.unadipatha.UnadiSutra

// 1.9: धान्ये नित्
object DhanyeNitSutra : UnadiSutra(
    number = "1.9",
    text = "धान्ये नित्",
    roots = setOf(
        // As in 1.8, the source does not distinguish the two अणँ homonyms by gaṇa.
        DivadiDhatus.all.single { it.id == "04.0071" },
        BhvadiDhatus.all.single { it.id == "01.0512" },
    ),
    pratyaya = "उ",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("अणँ" to Samjna.Rudhi("अणु")),
    rootMeaningMap = mapOf("अणँ" to Artha.Rudhi("अणु", "a kind of grain")),
    applicationMeaning = Artha.Rudhi("धान्य", "grain"),
    assignedAccent = Accent.UDATTA,
    accentVowelIndex = 0,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("अणु", "a kind of grain"),
    hindiExplanation = "जब अण् धातु से बनने वाला शब्द धान्य के अर्थ में प्रयुक्त हो, तब उ प्रत्यय नित् माना जाता है। नित् होने के कारण निर्मित शब्द के प्रथम स्वर पर उदात्त होता है। " +
        "इस विशेष अर्थ में अणु किसी सूक्ष्म कण मात्र का नहीं, बल्कि धान्य अथवा व्रीहि के एक भेद का नाम है। मूल पाठ गणभेद नहीं बताता, इसलिए 1.8 की तरह दोनों अणँ धातुपाठ-प्रविष्टियाँ सुरक्षित हैं।",
)
