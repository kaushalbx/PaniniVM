package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.dhatupatha.svadi.AshDhatu
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSecondaryDerivative
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra149Ash = AshDhatu()
private val sutra149Prush = KryadiDhatus.all.single { it.id == "09.0063" }
private val sutra149LatBhvadi = BhvadiDhatus.all.single { it.id == "01.0335" }
private val sutra149LatCuradi = CuradiDhatus.all.single { it.id == "10.0359" }
private val sutra149KanShabda = BhvadiDhatus.all.single { it.id == "01.0517" }
private val sutra149KanGati = BhvadiDhatus.all.single { it.id == "01.0903" }
private val sutra149KanCuradi = CuradiDhatus.all.single { it.id == "10.0240" }
private val sutra149Khat = BhvadiDhatus.all.single { it.id == "01.0346" }
private val sutra149Vish = TudadiDhatus.all.single { it.id == "06.0160" }

// 1.149: अशूप्रुषिलटिकणिखटिविशिभ्यः क्वन्
object AshuPrushiLatiKaniKhatiVishibhyahKvanSutra : UnadiSutra(
    number = "1.149",
    text = "अशूप्रुषिलटिकणिखटिविशिभ्यः क्वन्",
    roots = setOf(
        sutra149Ash, sutra149Prush, sutra149LatBhvadi, sutra149LatCuradi,
        sutra149KanShabda, sutra149KanGati, sutra149KanCuradi, sutra149Khat, sutra149Vish,
    ),
    pratyaya = "क्वन्",
    pratyayaSurface = "व",
    itMarkers = setOf(ItMarker.KIT, ItMarker.N),
    rootSamjnaMap = mapOf(
        sutra149Ash.upadesha to Samjna.Rudhi("अश्व"),
        sutra149Prush.upadesha to Samjna.Rudhi("प्रुष्व"),
        sutra149LatBhvadi.upadesha to Samjna.Rudhi("लट्व"),
        sutra149KanShabda.upadesha to Samjna.Rudhi("कण्व"),
        sutra149Khat.upadesha to Samjna.Rudhi("खट्व"),
        sutra149Vish.upadesha to Samjna.Rudhi("विश्व"),
    ),
    rootMeaningMap = mapOf(
        sutra149Prush.upadesha to Artha.Explanation("पुल्लिङ्ग में ऋतु अथवा सूर्य; स्त्रीलिङ्ग में जलकणिका"),
        sutra149LatBhvadi.upadesha to Artha.Explanation("पक्षी-विशेष अथवा फल"),
        sutra149KanShabda.upadesha to Artha.Explanation("पाप"),
    ),
    rootMorphology = mapOf(
        sutra149Ash.upadesha to UnadiRootMorphology(conditionedStem = "अश्".toVarnas()),
        sutra149Prush.upadesha to UnadiRootMorphology(conditionedStem = "प्रुष्".toVarnas()),
        sutra149LatBhvadi.upadesha to UnadiRootMorphology(conditionedStem = "लट्".toVarnas()),
        sutra149KanShabda.upadesha to UnadiRootMorphology(
            conditionedStem = "कण्".toVarnas(),
            optionalVarnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.A, Svara.I)),
        ),
        sutra149Khat.upadesha to UnadiRootMorphology(conditionedStem = "खट्".toVarnas()),
        sutra149Vish.upadesha to UnadiRootMorphology(conditionedStem = "विश्".toVarnas()),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(output = "प्रुष्व".toVarnas(), sourceRoot = sutra149Prush.upadesha, meaning = Artha.Explanation("ऋतु अथवा सूर्य")),
        UnadiDerivedVariant(output = "प्रुष्वा".toVarnas(), sourceRoot = sutra149Prush.upadesha, meaning = Artha.Explanation("जलकणिका")),
        UnadiDerivedVariant(output = "कण्व".toVarnas(), sourceRoot = sutra149KanShabda.upadesha, meaning = Artha.Explanation("पाप")),
        UnadiDerivedVariant(
            output = "किण्व".toVarnas(),
            sourceRoot = sutra149KanShabda.upadesha,
            meaning = Artha.Explanation("पाप"),
            morphology = UnadiRootMorphology(varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.A, Svara.I))),
        ),
    ),
    secondaryDerivatives = listOf(
        UnadiSecondaryDerivative("प्रुष्व".toVarnas(), "टाप्", "प्रुष्वा".toVarnas(), Artha.Explanation("जलकणिका")),
        UnadiSecondaryDerivative("लट्व".toVarnas(), "टाप्", "लट्वा".toVarnas(), Artha.Explanation("पक्षी-विशेष अथवा फल")),
        UnadiSecondaryDerivative("खट्व".toVarnas(), "टाप्", "खट्वा".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("अश्व".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.149"),
        UnadiLexicalAttestation("प्रुष्व".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("ऋतु अथवा सूर्य"), "1.149"),
        UnadiLexicalAttestation("प्रुष्वा".toVarnas(), setOf(Linga.STRI), Artha.Explanation("जलकणिका"), "1.149"),
        UnadiLexicalAttestation("लट्वा".toVarnas(), setOf(Linga.STRI), Artha.Explanation("पक्षी-विशेष अथवा फल"), "1.149"),
        UnadiLexicalAttestation("कण्व".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("पाप"), "1.149"),
        UnadiLexicalAttestation("किण्व".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("पाप"), "1.149"),
        UnadiLexicalAttestation("खट्वा".toVarnas(), setOf(Linga.STRI), attributedSutra = "1.149"),
        UnadiLexicalAttestation("विश्व".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.149"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "अशूँ, प्रुषँ, लटँ, कणँ, खटँ और विशँ धातुओं के बाद क्वन् प्रत्यय होता है। क्वन् का आरम्भिक क् और अन्तिम न् इत्संज्ञक होकर लुप्त होते हैं, इसलिए श्रव्य प्रत्यय व है और कित्त्व बना रहता है। अश् + व से अश्व, प्रुष् + व से प्रुष्व, लट् + व से लट्व, कण् + व से कण्व, खट् + व से खट्व और विश् + व से विश्व सिद्ध होते हैं। वृत्ति प्रुषँ को स्नेहनादि अर्थ वाला बताती है; प्रुष्व पुल्लिङ्ग में ऋतु अथवा सूर्य और टाप् से बना प्रुष्वा स्त्रीलिङ्ग में जलकणिका है। लट्व पर टाप् से लट्वा बनती है, जो पक्षी-विशेष अथवा फल का वाचक है। कण्व नपुंसकलिङ्ग में पाप है; बाहुलक से कण् के अ का वैकल्पिक इत्त्व—अ → इ का स्पष्ट Varna-आदेश—होने पर किण्व भी मिलता है। खट्व पर टाप् से खट्वा और विश् + व से नपुंसकलिङ्ग विश्व बनता है। लटि और कणि के लिए सूत्र गणभेद नहीं करता, इसलिए उपलब्ध समान-पाठ धातुपाठ प्रविष्टियाँ सुरक्षित हैं। जहाँ वृत्ति अर्थ नहीं बताती वहाँ परिचित कोशार्थ अनुमान से नहीं जोड़े गए।",
)
