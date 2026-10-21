package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.bhvadi.SrDhatu
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion
import dev.panini.unadipatha.UnadiVarnaLopa
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra151SrBhvadi = SrDhatu()
private val sutra151SrJuhotyadi = JuhotyadiDhatus.all.single { it.id == "03.0018" }
private val sutra151Ghrsh = BhvadiDhatus.all.single { it.id == "01.0805" }
private val sutra151RishBhvadi = BhvadiDhatus.all.single { it.id == "01.0790" }
private val sutra151RishDivadi = DivadiDhatus.all.single { it.id == "04.0144" }
private val sutra151Lash = BhvadiDhatus.all.single { it.id == "01.1033" }
private val sutra151Shi = AdadiDhatus.all.single { it.id == "02.0026" }
private val sutra151PatBhvadi = BhvadiDhatus.all.single { it.id == "01.0333" }
private val sutra151PatCuradi = CuradiDhatus.all.single { it.id == "10.0282" }
private val sutra151Hve = BhvadiDhatus.all.single { it.id == "01.1163" }
private val sutra151Ha = JuhotyadiDhatus.all.single { it.id == "03.0009" }
private val sutra151IshGati = BhvadiDhatus.all.single { it.id == "01.0695" }
private val sutra151IshUncha = BhvadiDhatus.all.single { it.id == "01.0780" }
private val sutra151Hras = BhvadiDhatus.all.single { it.id == "01.0808" }

// 1.151: सर्वनीघृष्वरिष्वलष्वशिवपट्वप्रह्वेष्वा अस्वतन्त्रे
object SarvaNighrshvaRishvaLashvaShivaPatvaPrahveshvaAsvatantreSutra : UnadiSutra(
    number = "1.151",
    text = "सर्वनीघृष्वरिष्वलष्वशिवपट्वप्रह्वेष्वा अस्वतन्त्रे",
    roots = setOf(
        sutra151SrBhvadi, sutra151SrJuhotyadi, sutra151Ghrsh,
        sutra151RishBhvadi, sutra151RishDivadi, sutra151Lash, sutra151Shi,
        sutra151PatBhvadi, sutra151PatCuradi, sutra151Hve, sutra151Ha,
        sutra151IshGati, sutra151IshUncha, sutra151Hras,
    ),
    pratyaya = "वन्",
    pratyayaSurface = "व",
    itMarkers = setOf(ItMarker.N),
    applicationMeaning = Artha.Explanation("अस्वतन्त्र अर्थात् अकर्तृ अर्थ में"),
    rootSamjnaMap = mapOf(
        sutra151SrBhvadi.upadesha to Samjna.Rudhi("सर्व"),
        sutra151Ghrsh.upadesha to Samjna.Rudhi("निघृष्व"),
        sutra151RishBhvadi.upadesha to Samjna.Rudhi("रिष्व"),
        sutra151Lash.upadesha to Samjna.Rudhi("लष्व"),
        sutra151Shi.upadesha to Samjna.Rudhi("शिव"),
        sutra151PatBhvadi.upadesha to Samjna.Rudhi("पट्व"),
        sutra151Hve.upadesha to Samjna.Rudhi("प्रह्व"),
        sutra151Ha.upadesha to Samjna.Rudhi("प्रह्व"),
        sutra151IshGati.upadesha to Samjna.Rudhi("ईष्व"),
        sutra151Hras.upadesha to Samjna.Rudhi("ह्रस्व"),
    ),
    rootMeaningMap = mapOf(
        sutra151SrBhvadi.upadesha to Artha.Explanation("समस्त विश्व"),
        sutra151Ghrsh.upadesha to Artha.Explanation("खुर"),
        sutra151RishBhvadi.upadesha to Artha.Explanation("हिंस्र"),
        sutra151Lash.upadesha to Artha.Explanation("नर्तक"),
        sutra151Shi.upadesha to Artha.Explanation("शम्भु"),
        sutra151PatBhvadi.upadesha to Artha.Explanation("रथ अथवा भूलोक"),
        sutra151Hve.upadesha to Artha.Explanation("जिसे पुकारा जाता है"),
        sutra151Ha.upadesha to Artha.Explanation("जिसे पुकारा जाता है"),
        sutra151IshGati.upadesha to Artha.Explanation("आचार्य"),
    ),
    rootMorphology = mapOf(
        sutra151SrBhvadi.upadesha to UnadiRootMorphology(
            conditionedStem = "सृ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA))),
        ),
        sutra151Ghrsh.upadesha to UnadiRootMorphology(
            conditionedStem = "घृष्".toVarnas(),
            requiredUpasargas = setOf("नि".toVarnas()),
        ),
        sutra151RishBhvadi.upadesha to UnadiRootMorphology(conditionedStem = "रिष्".toVarnas()),
        sutra151Lash.upadesha to UnadiRootMorphology(
            conditionedStem = "लष्".toVarnas(),
            optionalVarnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.A, Svara.I)),
        ),
        sutra151Shi.upadesha to UnadiRootMorphology(
            conditionedStem = "शी".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.II, Svara.I)),
        ),
        sutra151PatBhvadi.upadesha to UnadiRootMorphology(conditionedStem = "पट्".toVarnas()),
        sutra151Hve.upadesha to UnadiRootMorphology(
            conditionedStem = "ह्वा".toVarnas(),
            requiredUpasargas = setOf("प्र".toVarnas()),
            varnaLopas = listOf(UnadiVarnaLopa(Svara.AA), UnadiVarnaLopa(Vyanjana.VA)),
        ),
        sutra151Ha.upadesha to UnadiRootMorphology(
            conditionedStem = "हा".toVarnas(),
            requiredUpasargas = setOf("प्र".toVarnas()),
            varnaLopas = listOf(UnadiVarnaLopa(Svara.AA)),
        ),
        sutra151IshGati.upadesha to UnadiRootMorphology(
            conditionedStem = "ईष्".toVarnas(),
            optionalVarnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.II, Svara.I)),
        ),
        sutra151Hras.upadesha to UnadiRootMorphology(conditionedStem = "ह्रस्".toVarnas()),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant("नि".toVarnas(), "निघृष्व".toVarnas(), Artha.Explanation("खुर"), sourceRoot = sutra151Ghrsh.upadesha),
        UnadiDerivedVariant(
            output = "लिष्व".toVarnas(),
            meaning = Artha.Explanation("नर्तक"),
            sourceRoot = sutra151Lash.upadesha,
            morphology = UnadiRootMorphology(varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.A, Svara.I))),
        ),
        UnadiDerivedVariant("प्र".toVarnas(), "प्रह्व".toVarnas(), Artha.Explanation("जिसे पुकारा जाता है"), sourceRoot = sutra151Hve.upadesha),
        UnadiDerivedVariant("प्र".toVarnas(), "प्रह्व".toVarnas(), Artha.Explanation("जिसे पुकारा जाता है"), sourceRoot = sutra151Ha.upadesha),
        UnadiDerivedVariant(
            output = "इष्व".toVarnas(),
            meaning = Artha.Explanation("आचार्य"),
            sourceRoot = sutra151IshGati.upadesha,
            morphology = UnadiRootMorphology(varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.II, Svara.I))),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("सर्व".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("समस्त विश्व"), "1.151"),
        UnadiLexicalAttestation("निघृष्व".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("खुर"), "1.151"),
        UnadiLexicalAttestation("रिष्व".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("हिंस्र"), "1.151"),
        UnadiLexicalAttestation("लष्व".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("नर्तक"), "1.151"),
        UnadiLexicalAttestation("लिष्व".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("नर्तक"), "1.151"),
        UnadiLexicalAttestation("शिव".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("शम्भु"), "1.151"),
        UnadiLexicalAttestation("पट्व".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("रथ अथवा भूलोक"), "1.151"),
        UnadiLexicalAttestation("प्रह्व".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("जिसे पुकारा जाता है"), "1.151"),
        UnadiLexicalAttestation("ईष्व".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("आचार्य"), "1.151"),
        UnadiLexicalAttestation("इष्व".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("आचार्य"), "1.151"),
        UnadiLexicalAttestation("ह्रस्व".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.151"),
    ),
    relatedSutraNumbers = setOf("1.150"),
    isNipatita = true,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("अस्वतन्त्र अर्थात् अकर्तृ अर्थ में वन्-प्रत्ययान्त निपातित शब्द"),
    hindiExplanation = "पिछले सूत्र से वन् प्रत्यय की अनुवृत्ति है; उसका अन्तिम न् इत्संज्ञक होकर लुप्त होता है और व शेष रहता है। ‘अस्वतन्त्रे’ का अर्थ यहाँ अकर्तरि है—ये स्वतंत्र कर्ता के अर्थ में नहीं, वृत्ति में बताए विशेष अर्थों में निपातित होते हैं। सृ के ऋ का गुण अर् (ऋ → अ + र् का Varna-विस्तार) करके सर् + व से नपुंसकलिङ्ग सर्व बनता है—‘जिससे समस्त विश्व पार किया गया’। नि-उपसर्गपूर्वक घृष् में वृत्ति स्पष्टतः गुणाभाव कहती है, अतः नि + घृष् + व से खुर-अर्थक निघृष्व बनता है। रिष् + व से हिंस्र-अर्थक रिष्व और लष् + व से नर्तक-अर्थक लष्व बनते हैं; अन्य आचार्यों के लिष्व पाठ में उपधा अ का वैकल्पिक इत्त्व Varna-आदेश से रखा गया है। शीङ् के ई का ह्रस्व इ होने पर शि + व से शम्भु-अर्थक शिव और पट् + व से रथ अथवा भूलोक-अर्थक पट्व बनता है। प्रपूर्वक ह्वेञ् के लिए वृत्ति आकार तथा वकार के लोप को बताती है; ह्वा आधार में उन दोनों वर्णों का लोप करके बचे ह् पर प्रत्यय का व जुड़ता है और प्रह्व बनता है। वैकल्पिक निर्वचन में प्रपूर्वक ओँहाक् के आकार का लोप करके भी प्र + ह् + व से वही प्रह्व मिलता है। ईष् + व से आचार्य-अर्थक ईष्व और ई का वैकल्पिक ह्रस्व इ होने पर अन्य-पाठ इष्व है। बाहुलक से ह्रस् + व का ह्रस्व रूप भी स्रोत में सुरक्षित है; उसका कोई पृथक् अर्थ वृत्ति नहीं देती, इसलिए अर्थ अनुमानित नहीं किया गया। सूत्र गणभेद नहीं बताता, अतः समान उपदेश वाली उपलब्ध धातुपाठ प्रविष्टियाँ सम्मिलित हैं।",
)
