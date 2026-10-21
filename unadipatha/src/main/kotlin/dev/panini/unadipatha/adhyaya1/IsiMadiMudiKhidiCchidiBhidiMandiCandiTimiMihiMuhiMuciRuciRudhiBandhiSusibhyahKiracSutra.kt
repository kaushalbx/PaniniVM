package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.dhatupatha.rudhadi.RudhadiDhatus
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSecondaryDerivative
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaLopa

// 1.51: इषिमदिमुदिखिदिच्छिदिभिदिमन्दिचन्दितिमिमिहिमुहिमुचिरुचिरुधिबन्धिशुषिभ्यः किरच्
object IsiMadiMudiKhidiCchidiBhidiMandiCandiTimiMihiMuhiMuciRuciRudhiBandhiSusibhyahKiracSutra : UnadiSutra(
    number = "1.51",
    text = "इषिमदिमुदिखिदिच्छिदिभिदिमन्दिचन्दितिमिमिहिमुहिमुचिरुचिरुधिबन्धिशुषिभ्यः किरच्",
    roots = setOf(
        TudadiDhatus.all.single { it.id == "06.0078" },
        DivadiDhatus.all.single { it.id == "04.0105" },
        BhvadiDhatus.all.single { it.id == "01.0016" },
        DivadiDhatus.all.single { it.id == "04.0066" },
        RudhadiDhatus.all.single { it.id == "07.0003" },
        RudhadiDhatus.all.single { it.id == "07.0002" },
        BhvadiDhatus.all.single { it.id == "01.0013" },
        BhvadiDhatus.all.single { it.id == "01.0071" },
        DivadiDhatus.all.single { it.id == "04.0017" },
        BhvadiDhatus.all.single { it.id == "01.1147" },
        DivadiDhatus.all.single { it.id == "04.0095" },
        TudadiDhatus.all.single { it.id == "06.0166" },
        BhvadiDhatus.all.single { it.id == "01.0847" },
        RudhadiDhatus.all.single { it.id == "07.0001" },
        KryadiDhatus.all.single { it.id == "09.0044" },
        DivadiDhatus.all.single { it.id == "04.0080" },
    ),
    pratyaya = "किरच्",
    pratyayaSurface = "इर",
    itMarkers = setOf(ItMarker.KIT, ItMarker.CIT),
    rootSamjnaMap = mapOf(
        "इषुँ" to Samjna.Rudhi("इषिर"),
        "मदीँ" to Samjna.Rudhi("मदिर"),
        "मुदँ" to Samjna.Rudhi("मुदिर"),
        "खिदँ" to Samjna.Rudhi("खिदिर"),
        "छिदिँर्" to Samjna.Rudhi("छिदिर"),
        "भिदिँर्" to Samjna.Rudhi("भिदिर"),
        "मदिँ" to Samjna.Rudhi("मन्दिर"),
        "चदिँ" to Samjna.Rudhi("चन्दिर"),
        "तिमँ" to Samjna.Rudhi("तिमिर"),
        "मिहँ" to Samjna.Rudhi("मिहिर"),
        "मुहँ" to Samjna.Rudhi("मुहिर"),
        "मुचॢँ" to Samjna.Rudhi("मुचिर"),
        "रुचँ" to Samjna.Rudhi("रुचिर"),
        "रुधिँर्" to Samjna.Rudhi("रुधिर"),
        "बन्धँ" to Samjna.Rudhi("बधिर"),
        "शुषँ" to Samjna.Rudhi("शुषिर"),
    ),
    rootMeaningMap = mapOf(
        "इषुँ" to Artha.Explanation("अग्नि"),
        "मदीँ" to Artha.Explanation("सुरा"),
        "मुदँ" to Artha.Explanation("कामुक अथवा मेघ"),
        "खिदँ" to Artha.Explanation("चन्द्रमा"),
        "छिदिँर्" to Artha.Explanation("तलवार अथवा कुठार"),
        "भिदिँर्" to Artha.Explanation("वज्र"),
        "मदिँ" to Artha.Explanation("घर"),
        "चदिँ" to Artha.Explanation("चन्द्रमा अथवा हाथी"),
        "तिमँ" to Artha.Explanation("अन्धकार अथवा नेत्ररोग"),
        "मिहँ" to Artha.Explanation("सूर्य"),
        "मुहँ" to Artha.Explanation("काम अथवा असभ्य व्यक्ति"),
        "मुचॢँ" to Artha.Explanation("दाता"),
        "शुषँ" to Artha.Explanation("छिद्र"),
    ),
    rootMorphology = mapOf(
        "बन्धँ" to UnadiRootMorphology(
            conditionedStem = "बन्ध्".toVarnas(),
            varnaLopas = listOf(UnadiVarnaLopa(Vyanjana.NA)),
        ),
    ),
    secondaryDerivatives = listOf(
        UnadiSecondaryDerivative(
            base = "मदिर".toVarnas(),
            affix = "टाप्",
            output = "मदिरा".toVarnas(),
            meaning = Artha.Explanation("सुरा"),
        ),
        UnadiSecondaryDerivative(
            base = "मन्दिर".toVarnas(),
            affix = "टाप्",
            output = "मन्दिरा".toVarnas(),
            meaning = Artha.Explanation("घर"),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("इषिर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("अग्नि"), "1.51"),
        UnadiLexicalAttestation("मदिरा".toVarnas(), setOf(Linga.STRI), Artha.Explanation("सुरा"), "1.51"),
        UnadiLexicalAttestation("मुदिर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("कामुक अथवा मेघ"), "1.51"),
        UnadiLexicalAttestation("खिदिर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("चन्द्रमा"), "1.51"),
        UnadiLexicalAttestation("छिदिर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("तलवार अथवा कुठार"), "1.51"),
        UnadiLexicalAttestation("भिदिर".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("वज्र"), "1.51"),
        UnadiLexicalAttestation("मन्दिर".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("घर"), "1.51"),
        UnadiLexicalAttestation("मन्दिरा".toVarnas(), setOf(Linga.STRI), Artha.Explanation("घर"), "1.51"),
        UnadiLexicalAttestation("चन्दिर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("चन्द्रमा अथवा हाथी"), "1.51"),
        UnadiLexicalAttestation("तिमिर".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("अन्धकार अथवा नेत्ररोग"), "1.51"),
        UnadiLexicalAttestation("मिहिर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("सूर्य"), "1.51"),
        UnadiLexicalAttestation("मुहिर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("काम अथवा असभ्य व्यक्ति"), "1.51"),
        UnadiLexicalAttestation("मुचिर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("दाता"), "1.51"),
        UnadiLexicalAttestation("रुचिर".toVarnas(), attributedSutra = "1.51"),
        UnadiLexicalAttestation("रुधिर".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.51"),
        UnadiLexicalAttestation("बधिर".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.51"),
        UnadiLexicalAttestation("शुषिर".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("छिद्र"), "1.51"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("इस बहुधातुक नियम से इषिर आदि सोलह किरच्-प्रत्ययान्त प्रातिपदिक सिद्ध होते हैं; उनके रूढ़ अर्थ वृत्ति के अनुसार प्रत्येक धातु के साथ पृथक् रखे गए हैं।"),
    hindiExplanation = "सूत्र में इषि से शुषि तक सोलह धातुओं के बाद किरच् प्रत्यय विधान किया गया है। किरच् का आरम्भिक क् ‘लशक्वतद्धिते’ से और अन्तिम च् ‘हलन्त्यम्’ से इत्संज्ञक होकर लुप्त होते हैं; अतः प्रत्यय का श्रव्य Varna-क्रम इर रहता है। अधिकांश रूप धातु के प्रचलित आधार में इर जोड़ने से सीधे सिद्ध होते हैं—इषिर, मदिर, मुदिर, खिदिर, छिदिर, भिदिर, मन्दिर, चन्दिर, तिमिर, मिहिर, मुहिर, मुचिर, रुचिर, रुधिर और शुषिर। बन्ध् के विशेष रूप में नकार का लोप Varna-क्रिया के रूप में रखकर बध् + इर से बधिर सिद्ध किया गया है। मदिर और मन्दिर से स्त्रीलिङ्ग में टाप् लगने पर मदिरा और मन्दिरा भी वृत्ति में प्रमाणित हैं। वृत्ति जिन रूपों के अर्थ स्पष्ट करती है, उन्हीं को अर्थ-सहित रखा गया है: इषिर अग्नि, मदिरा सुरा, मुदिर कामुक या मेघ, खिदिर चन्द्रमा, छिदिर तलवार या कुठार, भिदिर वज्र, मन्दिर/मन्दिरा घर, चन्दिर चन्द्रमा या हाथी, तिमिर अन्धकार या नेत्ररोग, मिहिर सूर्य, मुहिर काम या असभ्य व्यक्ति, मुचिर दाता और शुषिर छिद्र। रुचिर, रुधिर तथा बधिर के लिए इस प्रविष्टि में केवल रूप दिया गया है, इसलिए उनके अप्रदत्त अर्थ model में अनुमान से नहीं जोड़े गए। ‘शुष्कम् इत्यन्ये’ वैकल्पिक मत का उल्लेख है, पर उसके लिए इस वृत्ति में कोई स्पष्ट किरच्-सम्बद्ध Varna-प्रक्रिया नहीं दी गई; इसलिए उसे अप्रमाणित परिवर्तन बनाकर नहीं जोड़ा गया।",
)
