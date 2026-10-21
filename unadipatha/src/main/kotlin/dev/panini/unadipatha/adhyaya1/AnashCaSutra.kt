package dev.panini.unadipatha.adhyaya1

import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.unadipatha.UnadiSutra

// 1.8: अणश्च
object AnashCaSutra : UnadiSutra(
    number = "1.8",
    text = "अणश्च",
    roots = setOf(
        // The pāṭha names अण् without a gaṇa discriminator; retain both exact homonyms.
        DivadiDhatus.all.single { it.id == "04.0071" },
        BhvadiDhatus.all.single { it.id == "01.0512" },
        BhvadiDhatus.all.single { it.id == "01.0359" },
        BhvadiDhatus.all.single { it.id == "01.0377" },
    ),
    pratyaya = "उ",
    pratyayaSurface = "उ",
    rootSamjnaMap = mapOf(
        "अणँ" to Samjna.Rudhi("अणु"),
        "कटीँ" to Samjna.Rudhi("कटु"),
        "वटिँ" to Samjna.Rudhi("वटु"),
    ),
    rootMeaningMap = mapOf(
        "अणँ" to Artha.Rudhi("अणु", "minute particle"),
        "कटीँ" to Artha.Rudhi("कटु", "pungent taste"),
        "वटिँ" to Artha.Rudhi("वटु", "young student"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("अणु, कटु और वटु शब्दों के रूढ़ अर्थ पृथक् हैं।"),
    hindiExplanation = "अण् धातु के बाद उ प्रत्यय होकर अणु शब्द बनता है, जिसका प्रयोग अत्यन्त छोटे कण या अल्प अंश के अर्थ में होता है। सूत्र के ‘च’ से कटि और वटि धातुओं का समुच्चय होता है; उनसे क्रमशः कटु और वटु शब्द सिद्ध होते हैं। " +
        "कटु रसना को तीक्ष्ण लगने वाले स्वाद का तथा वटु नवयुवक अथवा विद्यार्थी का रूढ़ नाम है। मूल पाठ अण् के गण का भेद नहीं बताता, इसलिए धातुपाठ में उपलब्ध दोनों अणँ प्रविष्टियाँ सुरक्षित रखी गई हैं।",
)
