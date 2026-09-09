package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.*
import dev.panini.sutra.*

private fun firstAndRest(c: SamasaRuleContext, first: String) = first + c.padas.drop(1).joinToString("") { it.upadesha }
private fun shortIk(word: String) = when {
    word.endsWith("ी") -> word.dropLast(1) + "ि"
    word.endsWith("ू") -> word.dropLast(1) + "ु"
    word.endsWith("ॄ") -> word.dropLast(1) + "ृ"
    word.endsWith("ई") -> word.dropLast(1) + "इ"
    word.endsWith("ऊ") -> word.dropLast(1) + "उ"
    word.endsWith("ॠ") -> word.dropLast(1) + "ऋ"
    else -> word
}
private fun longIkFinal(word: String) = word.lastOrNull() in setOf('ी','ू','ॄ','ई','ऊ','ॠ')
private fun vowelFinal(word: String) = word.lastOrNull() in setOf('अ','आ','इ','ई','उ','ऊ','ऋ','ॠ','ऌ','ए','ऐ','ओ','औ','ा','ि','ी','ु','ू','ृ','ॄ','े','ै','ो','ौ')
private fun mum(c: SamasaRuleContext, rule: String) = SamasaRuleResult.Formed(
    c.purvaPada.upadesha + "म्" + c.padas.drop(1).joinToString("") { it.upadesha }, "$rule adds the canonical मुम् augment.",
    memberEdits=mapOf(0 to c.purvaPada.upadesha+"म्"),
)

/** 6.3.61: इको ह्रस्वोऽङ्यो गालवस्य. */
object IkoHrasvoAnyyoGalavasyaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.61", text="इको ह्रस्वोऽङ्यो गालवस्य", hindiExplanation="गालव के मत में ङी को छोड़कर दीर्घ इक् का समास में ह्रस्व होता है।",
    type=SutraType.VIBHASHA, chapter=6, pada=3, optional=true, kramaValue=630061, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=5,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=context.padas.size>=2 && SamasaSemanticRelation.GALAVA_OPINION in context.semanticRelations && longIkFinal(context.purvaPada.upadesha) && SamasaMorphologicalFeature.FEMININE_NGI !in context.purvaPada.morphologicalFeatures
    override fun apply(context: SamasaRuleContext): SamasaRuleResult { val first=shortIk(context.purvaPada.upadesha); return SamasaRuleResult.Formed(firstAndRest(context,first),"6.3.61 optionally shortens the final long ik vowel.",memberEdits=mapOf(0 to first)) }
}

/** 6.3.62: एक तद्धिते च (compound portion). */
object EkaTaddhiteCaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.62", text="एक तद्धिते च", hindiExplanation="एका का समास में ह्रस्व एक होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630062, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=20,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=context.padas.size>=2 && context.purvaPada.upadesha=="एका"
    override fun apply(context: SamasaRuleContext)=SamasaRuleResult.Formed(firstAndRest(context,"एक"),"6.3.62 substitutes short एक for एका.",memberEdits=mapOf(0 to "एक"))
}

/** 6.3.63: ङ्यापोः संज्ञाछन्दसोर्बहुलम् (compound portion). */
object NgyapohSamjnaChandasorBahulamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.63", text="ङ्यापोः संज्ञाछन्दसोर्बहुलम्", hindiExplanation="संज्ञा और छन्दस् में ङी तथा आप् का बहुलता से ह्रस्व होता है।",
    type=SutraType.VIBHASHA, chapter=6, pada=3, optional=true, kramaValue=630063, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=30,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=context.padas.size>=2 && context.purvaPada.morphologicalFeatures.any { it==SamasaMorphologicalFeature.FEMININE_NGI || it==SamasaMorphologicalFeature.FEMININE_AAP } && context.semanticRelations.any { it==SamasaSemanticRelation.PROPER_NAME || it==SamasaSemanticRelation.VEDIC_REGISTER }
    override fun apply(context: SamasaRuleContext): SamasaRuleResult { val p=context.purvaPada.upadesha; val s=when { p.endsWith("ा")->p.dropLast(1); else->shortIk(p) }; return SamasaRuleResult.Formed(firstAndRest(context,s),"6.3.63 optionally shortens a feminine ending in its licensed register.",memberEdits=mapOf(0 to s)) }
}

/** 6.3.65: इष्टकेषीकामालानां चिततूलभारिषु. */
object IstakesikaMalanamCitaTulaBharisuSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.65", text="इष्टकेषीकामालानां चिततूलभारिषु", hindiExplanation="इष्टका-चित, इषीका-तूल और माला-भारिन् में पूर्वपद का ह्रस्व होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630065, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=40,
), SamasaSutra, UniversalSamasaTransformation {
    private val pairs=mapOf("इष्टका" to "चित","इषीका" to "तूल","माला" to "भारिन्")
    override fun matches(context: SamasaRuleContext)=context.padas.size>=2 && pairs[context.purvaPada.upadesha]==context.uttaraPada.upadesha
    override fun apply(context: SamasaRuleContext): SamasaRuleResult { val p=context.purvaPada.upadesha; val s=if(p.endsWith("ा"))p.dropLast(1) else shortIk(p); return SamasaRuleResult.Formed(firstAndRest(context,s),"6.3.65 shortens the listed pūrvapada.",memberEdits=mapOf(0 to s)) }
}

/** 6.3.66: खित्यनव्ययस्य. */
object KhityAnavyayasyaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.66", text="खित्यनव्ययस्य", hindiExplanation="खित्-व्युत्पन्न उत्तरपद से पहले अनव्यय पूर्वपद का अन्त्य स्वर ह्रस्व होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630066, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=10,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=context.padas.size>=2 && SamasaMorphologicalFeature.KHIT_DERIVED in context.uttaraPada.morphologicalFeatures && SamasaMorphologicalFeature.INDECLINABLE !in context.purvaPada.morphologicalFeatures && longIkFinal(context.purvaPada.upadesha)
    override fun apply(context: SamasaRuleContext): SamasaRuleResult { val first=shortIk(context.purvaPada.upadesha); return SamasaRuleResult.Formed(firstAndRest(context,first),"6.3.66 shortens the final before a khit-derived uttarapada.",memberEdits=mapOf(0 to first)) }
}

/** 6.3.67: अरुर्द्विषदजन्तस्य मुम्. */
object ArurDvisadAjantasyaMumSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.67", text="अरुर्द्विषदजन्तस्य मुम्", hindiExplanation="खित् उत्तरपद से पहले अरुस्, द्विषत् और अजन्त अनव्यय को मुम् आगम होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630067, role=SutraRole.Vidhi, action=SutraAction.AGAMA, scope=SutraScope.DERIVATION, samasaPriority=20,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=context.padas.size>=2 && SamasaMorphologicalFeature.KHIT_DERIVED in context.uttaraPada.morphologicalFeatures && SamasaMorphologicalFeature.INDECLINABLE !in context.purvaPada.morphologicalFeatures && (context.purvaPada.upadesha in setOf("अरुस्","द्विषत्") || vowelFinal(context.purvaPada.upadesha))
    override fun apply(context: SamasaRuleContext)=mum(context,"6.3.67")
}

/** 6.3.68: इच एकाचोऽम्प्रत्ययवच्च. */
object IcaEkacoAmPratyayavacCaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.68", text="इच एकाचोऽम्प्रत्ययवच्च", hindiExplanation="खित् उत्तरपद से पहले इकन्त एकाच् पूर्वपद को अम् आगम होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630068, role=SutraRole.Vidhi, action=SutraAction.AGAMA, scope=SutraScope.DERIVATION, samasaPriority=30,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=context.padas.size>=2 && SamasaMorphologicalFeature.KHIT_DERIVED in context.uttaraPada.morphologicalFeatures && SamasaMorphologicalFeature.MONOSYLLABIC in context.purvaPada.morphologicalFeatures && context.purvaPada.upadesha.lastOrNull() in setOf('इ','ई','उ','ऊ','ऋ','ॠ','ि','ी','ु','ू','ृ','ॄ')
    override fun apply(context: SamasaRuleContext)=mum(context,"6.3.68")
}

/** 6.3.69: वाचंयमपुरंदरौ च. */
object VacamyamaPurandarauCaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.69", text="वाचंयमपुरंदरौ च", hindiExplanation="वाचंयम और पुरन्दर निपातित रूप हैं।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630069, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=50,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=context.padas.size>=2 && (context.purvaPada.upadesha to context.uttaraPada.upadesha) in setOf("वाच्" to "यम","पुर्" to "दर")
    override fun apply(context: SamasaRuleContext)=SamasaRuleResult.Formed(if(context.purvaPada.upadesha=="वाच्")"वाचंयम" else "पुरन्दर","6.3.69 supplies the prescribed irregular form.",wholeStemOverride=true)
}

/** 6.3.70: कारे सत्यागदस्य. */
object KareSatyagadasyaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.70", text="कारे सत्यागदस्य", hindiExplanation="कार उत्तरपद होने पर सत्य और अगद को मुम् आगम होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630070, role=SutraRole.Vidhi, action=SutraAction.AGAMA, scope=SutraScope.DERIVATION, samasaPriority=40,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=context.padas.size>=2 && context.purvaPada.upadesha in setOf("सत्य","अगद") && context.uttaraPada.upadesha=="कार"
    override fun apply(context: SamasaRuleContext)=mum(context,"6.3.70")
}

/** 6.3.71: श्येनतिलस्य पाते ञे (compound-member portion). */
object SyenaTilasyaPateNyeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.71", text="श्येनतिलस्य पाते ञे", hindiExplanation="ञ्-प्रत्यययुक्त पात उत्तरपद पर श्येन और तिल को मुम् आगम होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630071, role=SutraRole.Vidhi, action=SutraAction.AGAMA, scope=SutraScope.DERIVATION, samasaPriority=40,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=context.padas.size>=2 && context.purvaPada.upadesha in setOf("श्येन","तिल") && context.uttaraPada.upadesha=="पात" && SamasaMorphologicalFeature.KHIT_DERIVED in context.uttaraPada.morphologicalFeatures
    override fun apply(context: SamasaRuleContext)=mum(context,"6.3.71")
}

/** 6.3.72: रात्रेः कृति विभाषा. */
object RatrehKrtiVibhasaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.72", text="रात्रेः कृति विभाषा", hindiExplanation="कृत्-व्युत्पन्न उत्तरपद से पहले रात्रि को विकल्प से मुम् आगम होता है।",
    type=SutraType.VIBHASHA, chapter=6, pada=3, optional=true, kramaValue=630072, role=SutraRole.Vidhi, action=SutraAction.AGAMA, scope=SutraScope.DERIVATION, samasaPriority=30,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=context.padas.size>=2 && context.purvaPada.upadesha=="रात्रि" && SamasaMorphologicalFeature.KRIT_DERIVED in context.uttaraPada.morphologicalFeatures
    override fun apply(context: SamasaRuleContext)=mum(context,"6.3.72")
}
