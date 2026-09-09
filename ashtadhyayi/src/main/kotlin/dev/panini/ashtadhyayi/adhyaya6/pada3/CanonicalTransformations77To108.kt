package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.*
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.shiksha.Samjna
import dev.panini.sutra.*

private fun joinedWith(c: SamasaRuleContext, first: String) = first + c.padas.drop(1).joinToString("") { it.upadesha }
private fun substitution(c: SamasaRuleContext, first: String, rule: String): SamasaRuleResult {
    val remainder=c.padas.drop(1).joinToString(""){it.upadesha}
    val stem=if(remainder.firstOrNull() in initialVowels) "$first $remainder" else first+remainder
    return SamasaRuleResult.Formed(stem, "$rule supplies the prescribed pūrvapada form.")
}
private fun atLeastTwo(c: SamasaRuleContext)=c.padas.size>=2
private val initialVowels=setOf('अ','आ','इ','ई','उ','ऊ','ऋ','ॠ','ऌ','ए','ऐ','ओ','औ')

/** 6.3.77: नगोऽप्राणिष्वन्यतरस्याम्. */
object NagoApranisuAnyatarasyamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.77", text="नगोऽप्राणिष्वन्यतरस्याम्", hindiExplanation="अप्राणी अर्थ में नग में नञ् का विकल्प से प्रकृतिभाव होता है।",
    type=SutraType.ANYATARASYAM, chapter=6, pada=3, optional=true, kramaValue=630077, role=SutraRole.Vidhi, action=SutraAction.NIYAMA, scope=SutraScope.DERIVATION, samasaType=SamasaType.NAN_TATPURUSA, samasaPriority=40,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.samasaType==SamasaType.NAN_TATPURUSA && context.purvaPada.upadesha in setOf("न","नञ्") && context.uttaraPada.upadesha=="ग" && SamasaSemanticRelation.NON_ANIMATE_REFERENT in context.semanticRelations
    override fun apply(context: SamasaRuleContext)=substitution(context,"न","6.3.77")
}

/** 6.3.83: प्रकृत्याशिष्यगोवत्सहलेषु. */
object PrakrtyasisyaGoVatsaHalesuSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.83", text="प्रकृत्याशिष्यगोवत्सहलेषु", hindiExplanation="आशीर्वाद में गो, वत्स और हल को छोड़कर सह प्रकृतिभाव से रहता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630083, role=SutraRole.Niyama, action=SutraAction.NIYAMA, scope=SutraScope.DERIVATION, samasaPriority=100,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha=="सह" && SamasaSemanticRelation.BENEDICTION in context.semanticRelations && context.uttaraPada.upadesha !in setOf("गो","वत्स","हल")
    override fun apply(context: SamasaRuleContext)=SamasaRuleResult.Formed(context.padas.joinToString(""){it.upadesha},"6.3.83 preserves सह in a benediction.")
}

/** 6.3.84: समानस्य छन्दस्यमूर्धप्रभृत्युदर्केषु. */
object SamanasyaChandasyAmurdhaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.84", text="समानस्य छन्दस्यमूर्धप्रभृत्युदर्केषु", hindiExplanation="छन्दस् में निर्दिष्ट अपवादों को छोड़कर समान के स्थान पर स होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630084, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=10,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha in setOf("समान","समान्") && SamasaSemanticRelation.VEDIC_REGISTER in context.semanticRelations && context.uttaraPada.upadesha !in setOf("मूर्धन्","प्रभृति","उदर्क")
    override fun apply(context: SamasaRuleContext)=substitution(context,"स","6.3.84")
}

/** 6.3.85: ज्योतिर्जनपदरात्रिनाभिनामगोत्ररूपस्थानवर्णवयोवचनबन्धुषु. */
object JyotirJanapadaRatriNabhiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.85", text="ज्योतिर्जनपदरात्रिनाभिनामगोत्ररूपस्थानवर्णवयोवचनबन्धुषु", hindiExplanation="पठित उत्तरपदों से पहले समान के स्थान पर स होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630085, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=30,
), SamasaSutra, UniversalSamasaTransformation {
    private val words=setOf("ज्योतिस्","जनपद","रात्रि","नाभि","नामन्","गोत्र","रूप","स्थान","वर्ण","वयस्","वचन","बन्धु")
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha in setOf("समान","समान्") && context.uttaraPada.upadesha in words
    override fun apply(context: SamasaRuleContext)=substitution(context,"स","6.3.85")
}

/** 6.3.86: चरणे ब्रह्मचारिणि. */
object CaraneBrahmacariniSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.86", text="चरणे ब्रह्मचारिणि", hindiExplanation="समान चरण के ब्रह्मचारी अर्थ में समान के स्थान पर स होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630086, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=30,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha in setOf("समान","समान्") && context.uttaraPada.upadesha=="ब्रह्मचारिन्" && SamasaSemanticRelation.COMMON_VEDIC_OBSERVANCE in context.semanticRelations
    override fun apply(context: SamasaRuleContext)=substitution(context,"स","6.3.86")
}

/** 6.3.89: दृग्दृशवतुषु (compound-member portion). */
object DrgDrsVatusuSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.89", text="दृग्दृशवतुषु", hindiExplanation="दृक् और दृश उत्तरपदों से पहले समान के स्थान पर स होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630089, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=40,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha in setOf("समान","समान्") && context.uttaraPada.upadesha in setOf("दृक्","दृश")
    override fun apply(context: SamasaRuleContext)=substitution(context,"स","6.3.89")
}

/** 6.3.90: इदंकिमोरीश्की. */
object IdamKimorIskiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.90", text="इदंकिमोरीश्की", hindiExplanation="दृक्-दृश से पहले इदम् को ई और किम् को की आदेश होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630090, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=50,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha in setOf("इदम्","किम्") && context.uttaraPada.upadesha in setOf("दृक्","दृश")
    override fun apply(context: SamasaRuleContext)=substitution(context,if(context.purvaPada.upadesha=="इदम्")"ई" else "की","6.3.90")
}

/** 6.3.91: आ सर्वनाम्नः. */
object AaSarvanamnahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.91", text="आ सर्वनाम्नः", hindiExplanation="दृक्-दृश से पहले सर्वनाम के अन्त्य को आ आदेश होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630091, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=30,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && SamasaMorphologicalFeature.PRONOUN in context.purvaPada.morphologicalFeatures && context.uttaraPada.upadesha in setOf("दृक्","दृश")
    override fun apply(context: SamasaRuleContext)=substitution(context,context.purvaPada.upadesha.dropLast(1)+"ा","6.3.91")
}

/** 6.3.93: समः समि. */
object SamahSamiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.93", text="समः समि", hindiExplanation="अञ्च् से पहले सम् के स्थान पर समि होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630093, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=40,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha in setOf("सम्","सम") && context.uttaraPada.upadesha=="अञ्च्"
    override fun apply(context: SamasaRuleContext)=substitution(context,"समि","6.3.93")
}

/** 6.3.94: तिरसस्तिर्यलोपे. */
object TirasasTiryAlopeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.94", text="तिरसस्तिर्यलोपे", hindiExplanation="अलुप्त अकार वाले अञ्च् से पहले तिरस् के स्थान पर तिरि होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630094, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=40,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha=="तिरस्" && context.uttaraPada.upadesha=="अञ्च्"
    override fun apply(context: SamasaRuleContext)=substitution(context,"तिरि","6.3.94")
}

/** 6.3.95: सहस्य सध्रिः. */
object SahasyaSadhrihSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.95", text="सहस्य सध्रिः", hindiExplanation="अञ्च् से पहले सह के स्थान पर सध्रि होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630095, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=60,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha=="सह" && context.uttaraPada.upadesha=="अञ्च्"
    override fun apply(context: SamasaRuleContext)=substitution(context,"सध्रि","6.3.95")
}

/** 6.3.96: सध मादस्थयोश्छन्दसि. */
object SadhaMadaSthayosChandasiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.96", text="सध मादस्थयोश्छन्दसि", hindiExplanation="छन्दस् में माद और स्थ से पहले सह के स्थान पर सध होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630096, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=60,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha=="सह" && context.uttaraPada.upadesha in setOf("माद","स्थ") && SamasaSemanticRelation.VEDIC_REGISTER in context.semanticRelations
    override fun apply(context: SamasaRuleContext)=substitution(context,"सध","6.3.96")
}

/** 6.3.97: द्व्यन्तरुपसर्गेभ्योऽप ईत्. */
object DvyAntarUpasargebhyoApaItSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.97", text="द्व्यन्तरुपसर्गेभ्योऽप ईत्", hindiExplanation="द्वि, अन्तर् अथवा उपसर्ग के बाद अप् को ई आदेश होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630097, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=40,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.uttaraPada.upadesha=="अप्" && (context.purvaPada.upadesha in setOf("द्वि","अन्तर्") || Samjna.UPASARGA in context.purvaPada.samjnas)
    override fun apply(context: SamasaRuleContext)=SamasaRuleResult.Formed(context.purvaPada.upadesha+"ई","6.3.97 substitutes ई for अप्.")
}

/** 6.3.98: ऊदनोर्देशे. */
object UdAnorDeseSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.98", text="ऊदनोर्देशे", hindiExplanation="देशार्थ में अनु के बाद अप् को ऊ आदेश होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630098, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=50,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha=="अनु" && context.uttaraPada.upadesha=="अप्" && SamasaSemanticRelation.LOCALITY in context.semanticRelations
    override fun apply(context: SamasaRuleContext)=SamasaRuleResult.Formed("अनू","6.3.98 substitutes ऊ for अप् after अनु.")
}

/** 6.3.99: अषष्ठ्यतृतीयास्थस्यान्यस्य दुग्... (compound-member portion). */
object AsasthyAtrtiyasthasyAnyasyaDugSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.99", text="अषष्ठ्यतृतीयास्थस्यान्यस्य दुगाशीराशास्थास्थितोत्सुकोतिकारकरागच्छेषु", hindiExplanation="षष्ठी और तृतीया से भिन्न अन्य को पठित उत्तरपदों से पहले दुक् आगम होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630099, role=SutraRole.Vidhi, action=SutraAction.AGAMA, scope=SutraScope.DERIVATION, samasaPriority=40,
), SamasaSutra, UniversalSamasaTransformation {
    private val words=setOf("आशिस्","आशा","आस्था","आस्थित","उत्सुक","ऊति","कारक","राग")
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha=="अन्य" && context.purvaPada.vibhakti !in setOf(Vibhakti.SASTHI,Vibhakti.TRTIYA) && context.uttaraPada.upadesha in words
    override fun apply(context: SamasaRuleContext)=substitution(context,"अन्यद्","6.3.99")
}

/** 6.3.100: अर्थे विभाषा. */
object ArtheVibhasaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.100", text="अर्थे विभाषा", hindiExplanation="अर्थ उत्तरपद से पहले अन्य को विकल्प से दुक् आगम होता है।",
    type=SutraType.VIBHASHA, chapter=6, pada=3, optional=true, kramaValue=630100, role=SutraRole.Vidhi, action=SutraAction.AGAMA, scope=SutraScope.DERIVATION, samasaPriority=40,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha=="अन्य" && context.uttaraPada.upadesha=="अर्थ"
    override fun apply(context: SamasaRuleContext)=substitution(context,"अन्यद्","6.3.100")
}

/** 6.3.101: कोः कत्तत्पुरुषेऽचि. */
object KohKatTatpuruseAciSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.101", text="कोः कत्तत्पुरुषेऽचि", hindiExplanation="अजादि उत्तरपद वाले तत्पुरुष में कु के स्थान पर कत् होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630101, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaType=SamasaType.TATPURUSA, samasaPriority=30,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.samasaType==SamasaType.TATPURUSA && context.purvaPada.upadesha=="कु" && context.uttaraPada.upadesha.firstOrNull() in initialVowels
    override fun apply(context: SamasaRuleContext)=substitution(context,"कत्","6.3.101")
}

/** 6.3.102: रथवदयोश्च. */
object RathaVadayosCaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.102", text="रथवदयोश्च", hindiExplanation="रथ और वद से पहले कु के स्थान पर कद् होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630102, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=40,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha=="कु" && context.uttaraPada.upadesha in setOf("रथ","वद")
    override fun apply(context: SamasaRuleContext)=substitution(context,"कद्","6.3.102")
}

/** 6.3.103: तृणे च जातौ. */
object TrneCaJatauSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.103", text="तृणे च जातौ", hindiExplanation="जातिवाची तृण से पहले कु के स्थान पर कत् होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630103, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=40,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha=="कु" && context.uttaraPada.upadesha=="तृण" && SamasaSemanticRelation.SPECIES in context.semanticRelations
    override fun apply(context: SamasaRuleContext)=substitution(context,"कत्","6.3.103")
}

/** 6.3.104: का पथ्यक्षयोः. */
object KaPathyAksayohSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.104", text="का पथ्यक्षयोः", hindiExplanation="पथिन् और अक्ष से पहले कु के स्थान पर का होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630104, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=50,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha=="कु" && context.uttaraPada.upadesha in setOf("पथिन्","अक्ष")
    override fun apply(context: SamasaRuleContext)=substitution(context,"का","6.3.104")
}

/** 6.3.105: ईषदर्थे. */
object IsadArtheSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.105", text="ईषदर्थे", hindiExplanation="ईषदर्थ में कु के स्थान पर का होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630105, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=50,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha=="कु" && SamasaSemanticRelation.SLIGHT_DEGREE in context.semanticRelations
    override fun apply(context: SamasaRuleContext)=substitution(context,"का","6.3.105")
}

/** 6.3.106: विभाषा पुरुषे. */
object VibhasaPuruseSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.106", text="विभाषा पुरुषे", hindiExplanation="पुरुष से पहले कु के स्थान पर विकल्प से का होता है।",
    type=SutraType.VIBHASHA, chapter=6, pada=3, optional=true, kramaValue=630106, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=50,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha=="कु" && context.uttaraPada.upadesha=="पुरुष"
    override fun apply(context: SamasaRuleContext)=substitution(context,"का","6.3.106")
}

/** 6.3.107: कवं चोष्णे. */
object KavamCosneSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.107", text="कवं चोष्णे", hindiExplanation="उष्ण उत्तरपद से पहले कु के स्थान पर कवम् होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630107, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=60,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha=="कु" && context.uttaraPada.upadesha=="उष्ण"
    override fun apply(context: SamasaRuleContext)=substitution(context,"कवम्","6.3.107")
}

/** 6.3.108: पथि च च्छन्दसि. */
object PathiCaChandasiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.108", text="पथि च च्छन्दसि", hindiExplanation="छन्दस् में पथिन् से पहले कु के स्थान पर का होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630108, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=60,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=atLeastTwo(context) && context.purvaPada.upadesha=="कु" && context.uttaraPada.upadesha=="पथिन्" && SamasaSemanticRelation.VEDIC_REGISTER in context.semanticRelations
    override fun apply(context: SamasaRuleContext)=substitution(context,"का","6.3.108")
}
