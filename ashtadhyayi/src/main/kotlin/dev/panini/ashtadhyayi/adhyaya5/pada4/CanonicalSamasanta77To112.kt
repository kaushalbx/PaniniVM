package dev.panini.ashtadhyayi.adhyaya5.pada4

import dev.panini.analysis.*
import dev.panini.core.*
import dev.panini.shiksha.*

object AcaturadicCanonicalSutra : ActiveSamasantaSutra(77,"अचतुरविचतुरसुचतुरस्त्रीपुंसधेन्वनडुहर्क्सामवाङ्मनसाक्षिभ्रुवदारगवोर्वष्ठीवपदष्ठीवनक्तंदिवरत्रिंदिवाहर्दिवसरजसनिःश्रेयसपुरुषायुषद्व्यायुषत्र्यायुषर्ग्यजुषजातोक्षमहोक्षवृद्धोक्षोपशुनगोष्ठश्वाः",samasaType=SamasaType.TATPURUSA,priority=60){
    private val pairs=setOf("अ" to "चतुर्", "वि" to "चतुर्", "सु" to "चतुर्",
        "नक्तम्" to "दिव", "रात्रिम्" to "दिव", "अहर्" to "दिव")
    override fun matches(context:SamasaRuleContext)=context.padas.size>=2&&
        (context.purvaPada.upadesha to context.uttaraPada.upadesha) in pairs
    override fun apply(context:SamasaRuleContext):SamasaRuleResult.Formed = when(context.uttaraPada.upadesha) {
        "चतुर्" -> formedWithSuffix(context,listOf(Svara.A))
        else -> formedWithMembers(context, if(context.purvaPada.upadesha in setOf("नक्तम्","रात्रिम्"))
            mapOf(0 to (context.purvaPada.varnas.dropLast(1)+Ayogavaha.ANUSVARA)) else emptyMap())
    }
}
object BrahmahastibhyamVarcasahSutra:ActiveSamasantaSutra(78,"ब्रह्महस्तिभ्यां वर्चसः",samasaType=SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.purvaPada.upadesha in setOf("ब्रह्म","हस्तिन्")&&c.uttaraPada.upadesha=="वर्चस्";override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas + Svara.A)}
object AvasamandhebhyasTamasahSutra:ActiveSamasantaSutra(79,"अवसमन्धेभ्यस्तमसः",samasaType=SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.purvaPada.upadesha in setOf("अव","सम्","अन्ध")&&c.uttaraPada.upadesha=="तमस्";override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas + Svara.A)}
object SvasoVasiyahSreyasahSutra:ActiveSamasantaSutra(80,"श्वसो वसीयःश्रेयसः",samasaType=SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.purvaPada.upadesha=="श्वस्"&&c.uttaraPada.upadesha in setOf("वसीयस्","श्रेयस्");override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas + Svara.A)}
object AnvavataptadRahasahSutra:ActiveSamasantaSutra(81,"अन्ववतप्ताद्रहसः",samasaType=SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.purvaPada.upadesha in setOf("अनु","अव","तप्त")&&c.uttaraPada.upadesha=="रहस्";override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas + Svara.A)}
object PraterUrasahSaptamisthatSutra:ActiveSamasantaSutra(82,"प्रतेरुरसः सप्तमीस्थात्",samasaType=SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.purvaPada.upadesha=="प्रति"&&c.uttaraPada.upadesha=="उरस्"&&c.uttaraPada.vibhakti==Vibhakti.SAPTAMI;override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas + Svara.A)}
object AnugavamAyameSutra:ActiveSamasantaSutra(83,"अनुगवमायामे",samasaType=SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.purvaPada.upadesha=="अनु"&&c.uttaraPada.upadesha=="गो"&&SamasaSemanticRelation.MEASURE_DIMENSION in c.semanticRelations;override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1) + listOf(Svara.A,Vyanjana.VA,Svara.A))}
object DvistavaTristavaVedihSutra:ActiveSamasantaSutra(84,"द्विस्तावा त्रिस्तावा वेदिः",samasaType=SamasaType.DVIGU){override fun matches(c:SamasaRuleContext)=c.padas.size==2&&c.purvaPada.upadesha in setOf("द्वि","त्रि")&&c.uttaraPada.upadesha=="स्ताव"&&SamasaSemanticRelation.ALTAR_RELATIVE_DIMENSION in c.semanticRelations;override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas)}
object AhasRatrehSutra:ActiveSamasantaSutra(87,"अहस्सर्वैकदेशसंख्यातपुण्याच्च रात्रेः",samasaType=SamasaType.TATPURUSA){private val first=setOf("अहन्","सर्व","पूर्व","अपर","संख्यात","पुण्य");override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.purvaPada.upadesha in first&&c.uttaraPada.upadesha=="रात्रि";override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1) + Svara.A)}

object GorAtaddhitalukiSutra:ActiveSamasantaSutra(92,"गोरतद्धितलुकि",samasaType=SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.uttaraPada.upadesha=="गो"&&SamasaMorphologicalFeature.TADDHITA_LUK !in c.uttaraPada.morphologicalFeatures;override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1) + listOf(Svara.A, Vyanjana.VA, Svara.A))}
object AgrakhyayamUrasahSutra:ActiveSamasantaSutra(93,"अग्राख्यायामुरसः",samasaType=SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.uttaraPada.upadesha=="उरस्"&&SamasaSemanticRelation.PRAISE in c.semanticRelations;override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas + Svara.A)}
object AnoAsmayassarasamJatisamjnayohSutra:ActiveSamasantaSutra(94,"अनोऽश्मायस्सरसां जातिसंज्ञयोः",samasaType=SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.uttaraPada.upadesha in setOf("अनस्","अश्मन्","अयस्","सरस्")&&c.semanticRelations.any{it==SamasaSemanticRelation.SPECIES||it==SamasaSemanticRelation.PROPER_NAME};override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,if(c.uttaraPada.varnas.lastOrNull()==Vyanjana.NA) c.uttaraPada.varnas.dropLast(1) else c.uttaraPada.varnas + Svara.A)}
object GramakautabhyamCaTaksnahSutra:ActiveSamasantaSutra(95,"ग्रामकौटाभ्यां च तक्ष्णः",samasaType=SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.purvaPada.upadesha in setOf("ग्राम","कौट")&&c.uttaraPada.upadesha=="तक्षन्";override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1))}
object AtehSunahSutra:ActiveSamasantaSutra(96,"अतेः शुनः",samasaType=SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.purvaPada.upadesha=="अति"&&c.uttaraPada.upadesha=="श्वन्";override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1))}
object UpamanadApranisuSutra:ActiveSamasantaSutra(97,"उपमानादप्राणिषु",samasaType=SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.uttaraPada.upadesha=="श्वन्"&&SamasaSemanticRelation.NON_ANIMATE_REFERENT in c.semanticRelations;override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1))}
object UttaramrgapurvacCaSakthnahSutra:ActiveSamasantaSutra(98,"उत्तरमृगपूर्वाच्च सक्थ्नः",samasaType=SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.purvaPada.upadesha in setOf("उत्तर","मृग","पर्व")&&c.uttaraPada.upadesha=="सक्थि";override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1) + Svara.A)}
object NavoDvigohSutra:ActiveSamasantaSutra(99,"नावो द्विगोः",samasaType=SamasaType.DVIGU){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.uttaraPada.upadesha in setOf("नौ","नाव");override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,if(c.uttaraPada.varnas.lastOrNull()==Svara.AU) c.uttaraPada.varnas.dropLast(1) + listOf(Svara.AA,Vyanjana.VA,Svara.A) else c.uttaraPada.varnas)}

object AnasantanNapumsakacChandasiSutra:ActiveSamasantaSutra(103,"अनसन्तान्नपुंसकाच्छन्दसि",true,SamasaType.TATPURUSA){
    override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.outputLinga==Linga.NAPUMSAKA&&
        SamasaSemanticRelation.VEDIC_REGISTER in c.semanticRelations&&
        c.uttaraPada.varnas.takeLast(2) in setOf(listOf(Svara.A,Vyanjana.NA),listOf(Svara.A,Vyanjana.SA))
    override fun apply(c:SamasaRuleContext)=if(c.uttaraPada.varnas.lastOrNull()==Vyanjana.NA)
        formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1))
    else formedWithSuffix(c,listOf(Svara.A))
}
object BrahmanoJanapadakhyayamSutra:ActiveSamasantaSutra(104,"ब्रह्मणो जानपदाख्यायाम्",samasaType=SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.uttaraPada.upadesha=="ब्रह्मन्"&&SamasaSemanticRelation.COUNTRY_PERSON in c.semanticRelations;override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1))}
object KumahadbhyamAnyatarasyamSutra:ActiveSamasantaSutra(105,"कुमहद्भ्यामन्यतरस्याम्",true,SamasaType.TATPURUSA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.purvaPada.upadesha in setOf("कु","महत्")&&c.uttaraPada.upadesha=="ब्रह्मन्";override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1))}
object DvandvacCudasahantatSamahareSutra:ActiveSamasantaSutra(106,"द्वन्द्वाच्चुदषहान्तात् समाहारे",samasaType=SamasaType.DVANDVA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&SamasaSemanticRelation.COLLECTIVE in c.semanticRelations&&c.uttaraPada.varnas.lastOrNull() in setOf(Vyanjana.CA,Vyanjana.CHA,Vyanjana.JA,Vyanjana.JHA,Vyanjana.NYA,Vyanjana.DA,Vyanjana.SSA,Vyanjana.HA);override fun apply(c:SamasaRuleContext)=formedWithSuffix(c,listOf(Svara.A))}
object AvyayibhaveSaratprabhrtibhyahSutra : ActiveSamasantaSutra(
    107,"अव्ययीभावे शरत्प्रभृतिभ्यः",samasaType=SamasaType.AVYAYIBHAVA,priority=30,
) {
    private val words=setOf("शरद्","शरत्","विपाश्","अनस्","मनस्","उपानह्","दिव्",
        "हिमवत्","अनडुह्","दिश्","दृश्","चतुर्","यद्","तद्","पथिन्","जरा")
    override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&
        (c.uttaraPada.upadesha in words || (c.uttaraPada.upadesha=="अक्षि"&&
            c.purvaPada.upadesha in setOf("प्रति","पर","सम्","अनु")))
    override fun apply(c:SamasaRuleContext):SamasaRuleResult.Formed = when(c.uttaraPada.upadesha) {
        "पथिन्" -> formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(2)+Svara.A)
        "अक्षि" -> formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1)+Svara.A)
        "जरा" -> formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1)+listOf(Svara.A,Vyanjana.SA,Svara.A))
        else -> formedWithSuffix(c,listOf(Svara.A))
    }
}
object AnasCaSutra:ActiveSamasantaSutra(108,"अनश्च",samasaType=SamasaType.AVYAYIBHAVA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.uttaraPada.varnas.takeLast(2)==listOf(Svara.A,Vyanjana.NA);override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1))}
object NapumsakadAnyatarasyamSutra:ActiveSamasantaSutra(109,"नपुंसकादन्यतरस्याम्",true,SamasaType.AVYAYIBHAVA,20){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.uttaraPada.linga==Linga.NAPUMSAKA&&c.uttaraPada.varnas.takeLast(2)==listOf(Svara.A,Vyanjana.NA);override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1))}
object NadipaurnamasyagrahayanibhyahSutra:ActiveSamasantaSutra(110,"नदीपौर्णमास्याग्रहायणीभ्यः",true,SamasaType.AVYAYIBHAVA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.uttaraPada.upadesha in setOf("नदी","पौर्णमासी","आग्रहायणी");override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1)+Svara.A)}
object JhayahSutra:ActiveSamasantaSutra(111,"झयः",true,SamasaType.AVYAYIBHAVA){
    private val jhay=setOf(Vyanjana.KA,Vyanjana.KHA,Vyanjana.GA,Vyanjana.GHA,Vyanjana.CA,Vyanjana.CHA,Vyanjana.JA,Vyanjana.JHA,Vyanjana.TTA,Vyanjana.TTHA,Vyanjana.DDA,Vyanjana.DDHA,Vyanjana.TA,Vyanjana.THA,Vyanjana.DA,Vyanjana.DHA,Vyanjana.PA,Vyanjana.PHA,Vyanjana.BA,Vyanjana.BHA)
    override fun matches(c:SamasaRuleContext):Boolean {
        if(c.padas.size<2)return false
        return c.uttaraPada.varnas.lastOrNull() in jhay
    }
    override fun apply(c:SamasaRuleContext)=formedWithSuffix(c,listOf(Svara.A))
}
object GiresCaSenakasyaSutra:ActiveSamasantaSutra(112,"गिरेश्च सेनकस्य",true,SamasaType.AVYAYIBHAVA){override fun matches(c:SamasaRuleContext)=c.padas.size>=2&&c.uttaraPada.upadesha=="गिरि";override fun apply(c:SamasaRuleContext)=formedWithFinalMember(c,c.uttaraPada.varnas.dropLast(1)+Svara.A)}
