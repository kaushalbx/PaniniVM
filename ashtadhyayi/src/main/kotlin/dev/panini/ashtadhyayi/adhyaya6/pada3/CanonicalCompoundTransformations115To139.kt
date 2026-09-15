package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.*
import dev.panini.shiksha.Samjna
import dev.panini.sutra.*

private fun compoundContext(c: SamasaRuleContext)=c.padas.size>=2
private fun laterMembers(c: SamasaRuleContext)=c.padas.drop(1).joinToString(""){it.upadesha}
private fun changedFirst(c: SamasaRuleContext, first: String, rule: String): SamasaRuleResult {
    val remainder=laterMembers(c)
    val stem=if(remainder.firstOrNull() in setOf('अ','आ','इ','ई','उ','ऊ','ऋ','ॠ','ऌ','ए','ऐ','ओ','औ')) "$first $remainder" else first+remainder
    return SamasaRuleResult.Formed(stem,"$rule performs the prescribed compound-member substitution.",memberEdits=mapOf(0 to first))
}
private fun lengthenFinal(word:String)=when {
    word.endsWith("ि")->word.dropLast(1)+"ी"; word.endsWith("ु")->word.dropLast(1)+"ू"; word.endsWith("ृ")->word.dropLast(1)+"ॄ"
    word.endsWith("इ")->word.dropLast(1)+"ई"; word.endsWith("उ")->word.dropLast(1)+"ऊ"; word.endsWith("ऋ")->word.dropLast(1)+"ॠ"
    word.endsWith("ा")->word; word.endsWith("अ")->word.dropLast(1)+"आ"; else->word+"ा"
}
private fun hasIkFinal(word:String)=word.lastOrNull() in setOf('इ','ई','उ','ऊ','ऋ','ॠ','ि','ी','ु','ू','ृ','ॄ')

/** 6.3.115: कर्णे लक्षणस्याविष्टाष्टपञ्चमणिभिन्नच्छिन्नच्छिद्रस्रुवस्वस्तिकस्य. */
object KarneLaksanasyaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(number="6.3.115",text="कर्णे लक्षणस्याविष्टाष्टपञ्चमणिभिन्नछिन्नछिद्रस्रुवस्वस्तिकस्य",hindiExplanation="लक्षणार्थक पूर्वपद का कर्ण से पहले दीर्घ होता है, पठित अपवादों को छोड़कर।",type=SutraType.NITYA,chapter=6,pada=3,optional=false,kramaValue=630115,role=SutraRole.Vidhi,action=SutraAction.ADESHA,scope=SutraScope.DERIVATION,samasaPriority=30),SamasaSutra,UniversalSamasaTransformation {
    private val excluded=setOf("विष्ट","अष्टन्","पञ्चन्","मणि","भिन्न","छिन्न","छिद्र","स्रुव","स्वस्तिक")
    override fun matches(context:SamasaRuleContext)=compoundContext(context)&&context.uttaraPada.upadesha=="कर्ण"&&SamasaSemanticRelation.OWNERSHIP_MARK in context.semanticRelations&&context.purvaPada.upadesha !in excluded
    override fun apply(context:SamasaRuleContext)=changedFirst(context,lengthenFinal(context.purvaPada.upadesha),"6.3.115")
}

/** 6.3.116: नहिवृतिवृषिव्यधिरुचिसहितनिषु क्वौ. */
object NahiVrtiVrsiVyadhiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(number="6.3.116",text="नहिवृतिवृषिव्यधिरुचिसहितनिषु क्वौ",hindiExplanation="क्वि-व्युत्पन्न पठित धातु-उत्तरपदों से पहले पूर्वपद का दीर्घ होता है।",type=SutraType.NITYA,chapter=6,pada=3,optional=false,kramaValue=630116,role=SutraRole.Vidhi,action=SutraAction.ADESHA,scope=SutraScope.DERIVATION,samasaPriority=20),SamasaSutra,UniversalSamasaTransformation {
    private val roots=setOf("नह्","वृत्","वृष्","व्यध्","रुच्","सह्","तन्")
    override fun matches(context:SamasaRuleContext)=compoundContext(context)&&context.uttaraPada.upadesha in roots&&SamasaMorphologicalFeature.KVI_DERIVED in context.uttaraPada.morphologicalFeatures
    override fun apply(context:SamasaRuleContext)=changedFirst(context,lengthenFinal(context.purvaPada.upadesha),"6.3.116")
}

/** 6.3.117: वनगिर्योः संज्ञायां कोटरकिंशुलकादीनाम्. */
object VanaGiryohSamjnayamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(number="6.3.117",text="वनगिर्योः संज्ञायां कोटरकिंशुलकादीनाम्",hindiExplanation="संज्ञा में निर्दिष्ट गणों के वन और गिरि से पहले दीर्घ होता है।",type=SutraType.NITYA,chapter=6,pada=3,optional=false,kramaValue=630117,role=SutraRole.Vidhi,action=SutraAction.ADESHA,scope=SutraScope.DERIVATION,samasaPriority=30),SamasaSutra,UniversalSamasaTransformation {
    override fun matches(context:SamasaRuleContext)=compoundContext(context)&&SamasaSemanticRelation.PROPER_NAME in context.semanticRelations&&((context.uttaraPada.upadesha=="वन"&&SamasaMorphologicalFeature.KOTARADI in context.purvaPada.morphologicalFeatures)||(context.uttaraPada.upadesha=="गिरि"&&SamasaMorphologicalFeature.KIMSULAKADI in context.purvaPada.morphologicalFeatures))
    override fun apply(context:SamasaRuleContext)=changedFirst(context,lengthenFinal(context.purvaPada.upadesha),"6.3.117")
}

/** 6.3.121: इको वहेऽपीलोः. */
object IkoVaheApilohSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(number="6.3.121",text="इकः वहे अपीलोः",hindiExplanation="पीलु को छोड़कर वह उत्तरपद से पहले इक् का दीर्घ होता है।",type=SutraType.NITYA,chapter=6,pada=3,optional=false,kramaValue=630121,role=SutraRole.Vidhi,action=SutraAction.ADESHA,scope=SutraScope.DERIVATION,samasaPriority=30),SamasaSutra,UniversalSamasaTransformation {
    override fun matches(context:SamasaRuleContext)=compoundContext(context)&&context.uttaraPada.upadesha=="वह"&&context.purvaPada.upadesha!="पीलु"&&hasIkFinal(context.purvaPada.upadesha)
    override fun apply(context:SamasaRuleContext)=changedFirst(context,lengthenFinal(context.purvaPada.upadesha),"6.3.121")
}

/** 6.3.122: उपसर्गस्य घञ्यमनुष्ये बहुलम्. */
object UpasargasyaGhanyAmanusyeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(number="6.3.122",text="उपसर्गस्य घञ्यमनुष्ये बहुलम्",hindiExplanation="अमनुष्यवाची घञ् उत्तरपद से पहले उपसर्ग का बहुलता से दीर्घ होता है।",type=SutraType.VIBHASHA,chapter=6,pada=3,optional=true,kramaValue=630122,role=SutraRole.Vidhi,action=SutraAction.ADESHA,scope=SutraScope.DERIVATION,samasaPriority=20),SamasaSutra,UniversalSamasaTransformation {
    override fun matches(context:SamasaRuleContext)=compoundContext(context)&&Samjna.UPASARGA in context.purvaPada.samjnas&&SamasaMorphologicalFeature.GHAN_DERIVED in context.uttaraPada.morphologicalFeatures&&SamasaSemanticRelation.NON_ANIMATE_REFERENT in context.semanticRelations
    override fun apply(context:SamasaRuleContext)=changedFirst(context,lengthenFinal(context.purvaPada.upadesha),"6.3.122")
}

/** 6.3.123: इकः काशे. */
object IkahKaseSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(number="6.3.123",text="इकः काशे",hindiExplanation="काश उत्तरपद से पहले इकन्त उपसर्ग का दीर्घ होता है।",type=SutraType.NITYA,chapter=6,pada=3,optional=false,kramaValue=630123,role=SutraRole.Vidhi,action=SutraAction.ADESHA,scope=SutraScope.DERIVATION,samasaPriority=30),SamasaSutra,UniversalSamasaTransformation {
    override fun matches(context:SamasaRuleContext)=compoundContext(context)&&context.uttaraPada.upadesha=="काश"&&Samjna.UPASARGA in context.purvaPada.samjnas&&hasIkFinal(context.purvaPada.upadesha)
    override fun apply(context:SamasaRuleContext)=changedFirst(context,lengthenFinal(context.purvaPada.upadesha),"6.3.123")
}

/** 6.3.125: अष्टनः संज्ञायाम्. */
object AstanaSamjnayamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(number="6.3.125",text="अष्टनः संज्ञायाम्",hindiExplanation="संज्ञा में अष्टन् का अष्टा आदेश होता है।",type=SutraType.NITYA,chapter=6,pada=3,optional=false,kramaValue=630125,role=SutraRole.Vidhi,action=SutraAction.ADESHA,scope=SutraScope.DERIVATION,samasaPriority=40),SamasaSutra,UniversalSamasaTransformation {
    override fun matches(context:SamasaRuleContext)=compoundContext(context)&&context.purvaPada.upadesha=="अष्टन्"&&SamasaSemanticRelation.PROPER_NAME in context.semanticRelations
    override fun apply(context:SamasaRuleContext)=changedFirst(context,"अष्टा","6.3.125")
}

/** 6.3.126: छन्दसि च. */
object ChandasiCaAstanaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(number="6.3.126",text="छन्दसि च",hindiExplanation="छन्दस् में अष्टन् का अष्टा आदेश होता है।",type=SutraType.NITYA,chapter=6,pada=3,optional=false,kramaValue=630126,role=SutraRole.Vidhi,action=SutraAction.ADESHA,scope=SutraScope.DERIVATION,samasaPriority=40),SamasaSutra,UniversalSamasaTransformation {
    override fun matches(context:SamasaRuleContext)=compoundContext(context)&&context.purvaPada.upadesha=="अष्टन्"&&SamasaSemanticRelation.VEDIC_REGISTER in context.semanticRelations
    override fun apply(context:SamasaRuleContext)=changedFirst(context,"अष्टा","6.3.126")
}

/** 6.3.127: चितेः कपि. */
object CitehKapiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(number="6.3.127",text="चितेः कपि",hindiExplanation="कप् परे चिति का अन्त्य स्वर दीर्घ होता है।",type=SutraType.NITYA,chapter=6,pada=3,optional=false,kramaValue=630127,role=SutraRole.Vidhi,action=SutraAction.ADESHA,scope=SutraScope.DERIVATION,samasaPriority=40),SamasaSutra,UniversalSamasaTransformation {
    override fun matches(context:SamasaRuleContext)=compoundContext(context)&&context.purvaPada.upadesha=="चिति"&&SamasaMorphologicalFeature.KAP_DERIVED in context.uttaraPada.morphologicalFeatures
    override fun apply(context:SamasaRuleContext)=changedFirst(context,"चिती","6.3.127")
}

/** 6.3.128: विश्वस्य वसुराटोः. */
object VisvasyaVasuRatohSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(number="6.3.128",text="विश्वस्य वसुराटोः",hindiExplanation="वसु और राट् से पहले विश्व का विश्वा आदेश होता है।",type=SutraType.NITYA,chapter=6,pada=3,optional=false,kramaValue=630128,role=SutraRole.Vidhi,action=SutraAction.ADESHA,scope=SutraScope.DERIVATION,samasaPriority=40),SamasaSutra,UniversalSamasaTransformation {
    override fun matches(context:SamasaRuleContext)=compoundContext(context)&&context.purvaPada.upadesha=="विश्व"&&context.uttaraPada.upadesha in setOf("वसु","राट्")
    override fun apply(context:SamasaRuleContext)=changedFirst(context,"विश्वा","6.3.128")
}

/** 6.3.129: नरे संज्ञायाम्. */
object NareSamjnayamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(number="6.3.129",text="नरे संज्ञायाम्",hindiExplanation="संज्ञा में नर से पहले विश्व का विश्वा आदेश होता है।",type=SutraType.NITYA,chapter=6,pada=3,optional=false,kramaValue=630129,role=SutraRole.Vidhi,action=SutraAction.ADESHA,scope=SutraScope.DERIVATION,samasaPriority=40),SamasaSutra,UniversalSamasaTransformation {
    override fun matches(context:SamasaRuleContext)=compoundContext(context)&&context.purvaPada.upadesha=="विश्व"&&context.uttaraPada.upadesha=="नर"&&SamasaSemanticRelation.PROPER_NAME in context.semanticRelations
    override fun apply(context:SamasaRuleContext)=changedFirst(context,"विश्वा","6.3.129")
}

/** 6.3.130: मित्रे चर्षौ. */
object MitreCarsauSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(number="6.3.130",text="मित्रे चर्षौ",hindiExplanation="ऋषि-संज्ञा में मित्र से पहले विश्व का विश्वा आदेश होता है।",type=SutraType.NITYA,chapter=6,pada=3,optional=false,kramaValue=630130,role=SutraRole.Vidhi,action=SutraAction.ADESHA,scope=SutraScope.DERIVATION,samasaPriority=50),SamasaSutra,UniversalSamasaTransformation {
    override fun matches(context:SamasaRuleContext)=compoundContext(context)&&context.purvaPada.upadesha=="विश्व"&&context.uttaraPada.upadesha=="मित्र"&&SamasaSemanticRelation.RISHI_NAME in context.semanticRelations
    override fun apply(context:SamasaRuleContext)=changedFirst(context,"विश्वा","6.3.130")
}

/** 6.3.139: संप्रसारणस्य. */
object SamprasaranasyaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(number="6.3.139",text="सम्प्रसारणस्य",hindiExplanation="समासपूर्वपद में सम्प्रसारण स्वर दीर्घ होता है।",type=SutraType.NITYA,chapter=6,pada=3,optional=false,kramaValue=630139,role=SutraRole.Vidhi,action=SutraAction.ADESHA,scope=SutraScope.DERIVATION,samasaPriority=20),SamasaSutra,UniversalSamasaTransformation {
    override fun matches(context:SamasaRuleContext)=compoundContext(context)&&SamasaMorphologicalFeature.SAMPRASARANA_FINAL in context.purvaPada.morphologicalFeatures
    override fun apply(context:SamasaRuleContext)=changedFirst(context,lengthenFinal(context.purvaPada.upadesha),"6.3.139")
}
