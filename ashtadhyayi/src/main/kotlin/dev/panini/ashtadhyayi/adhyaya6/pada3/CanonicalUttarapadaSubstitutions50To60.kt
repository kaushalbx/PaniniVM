package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.*
import dev.panini.sutra.*

private fun hasFirst(c: SamasaRuleContext, word: String) = c.padas.size >= 2 && c.purvaPada.upadesha == word
private fun replaceFirst(c: SamasaRuleContext, replacement: String, rule: String): SamasaRuleResult {
    val remainder=c.padas.drop(1).joinToString(""){it.upadesha}
    val stem=if(remainder.firstOrNull() in setOf('अ','आ','इ','ई','उ','ऊ','ऋ','ॠ','ऌ','ए','ऐ','ओ','औ')) "$replacement $remainder" else replacement+remainder
    return SamasaRuleResult.Formed(stem,"$rule substitutes $replacement for the pūrvapada.",memberEdits=mapOf(0 to replacement))
}

/** 6.3.50: हृदयस्य हृल्लेखयदणलासेषु (compound-member portion: लेख, लास). */
object HrdayasyaHrllekhayadanalasesuSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.50", text="हृदयस्य हृल्लेखयदणलासेषु", hindiExplanation="लेख और लास उत्तरपद होने पर हृदय के स्थान पर हृद् होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630050, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=20,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=hasFirst(context,"हृदय") && context.uttaraPada.upadesha in setOf("लेख","लास")
    override fun apply(context: SamasaRuleContext)=replaceFirst(context,"हृद्","6.3.50")
}

/** 6.3.51: वा शोकष्यञ्रोगेषु (compound-member portion: शोक, रोग). */
object VaSokasyanRogesuSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.51", text="वा शोकष्यञ्रोगेषु", hindiExplanation="शोक और रोग उत्तरपद होने पर हृदय के स्थान पर विकल्प से हृद् होता है।",
    type=SutraType.VIBHASHA, chapter=6, pada=3, optional=true, kramaValue=630051, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=20,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=hasFirst(context,"हृदय") && context.uttaraPada.upadesha in setOf("शोक","रोग")
    override fun apply(context: SamasaRuleContext)=replaceFirst(context,"हृद्","6.3.51")
}

/** 6.3.52: पादस्य पदाज्यातिगोपहतेषु. */
object PadasyaPadajyatigopahatesuSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.52", text="पादस्य पदाज्यातिगोपहतेषु", hindiExplanation="आजि, आति, ग और उपहत उत्तरपद होने पर पाद के स्थान पर पद् होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630052, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=20,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=hasFirst(context,"पाद") && context.uttaraPada.upadesha in setOf("आजि","आति","ग","उपहत")
    override fun apply(context: SamasaRuleContext)=replaceFirst(context,"पद्","6.3.52")
}

/** 6.3.54: हिमकाषिहतिषु च. */
object HimakasihatisuCaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.54", text="हिमकाषिहतिषु च", hindiExplanation="हिम, काषिन् और हति उत्तरपद होने पर पाद के स्थान पर पद् होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630054, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=20,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=hasFirst(context,"पाद") && context.uttaraPada.upadesha in setOf("हिम","काशिन्","काषिन्","हति")
    override fun apply(context: SamasaRuleContext)=replaceFirst(context,"पद्","6.3.54")
}

/** 6.3.56: वा घोषमिश्रशब्देषु. */
object VaGhosamisrasabdesuSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.56", text="वा घोषमिश्रशब्देषु", hindiExplanation="घोष, मिश्र और शब्द उत्तरपद होने पर पाद के स्थान पर विकल्प से पद् होता है।",
    type=SutraType.VIBHASHA, chapter=6, pada=3, optional=true, kramaValue=630056, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=20,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=hasFirst(context,"पाद") && context.uttaraPada.upadesha in setOf("घोष","मिश्र","शब्द")
    override fun apply(context: SamasaRuleContext)=replaceFirst(context,"पद्","6.3.56")
}

/** 6.3.57: उदकस्योदः संज्ञायाम्. */
object UdakasyodahSamjnayamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.57", text="उदकस्योदः संज्ञायाम्", hindiExplanation="संज्ञा में उदक के स्थान पर उद् होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630057, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=30,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=hasFirst(context,"उदक") && SamasaSemanticRelation.PROPER_NAME in context.semanticRelations
    override fun apply(context: SamasaRuleContext)=replaceFirst(context,"उद्","6.3.57")
}

/** 6.3.58: पेषंवासवाहनधिषु च. */
object PesamVasaVahanaDhisuCaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.58", text="पेषंवासवाहनधिषु च", hindiExplanation="पेषम्, वास, वाहन और धि उत्तरपद होने पर उदक के स्थान पर उद् होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630058, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=20,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=hasFirst(context,"उदक") && context.uttaraPada.upadesha in setOf("पेषम्","पेषं","वास","वाहन","धि")
    override fun apply(context: SamasaRuleContext)=replaceFirst(context,"उद्","6.3.58")
}

/** 6.3.59: एकहलादौ पूरयितव्येऽन्यतरस्याम्. */
object EkahaladauPurayitavyeAnyatarasyamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.59", text="एकहलादौ पूरयितव्येऽन्यतरस्याम्", hindiExplanation="जल से भरे जाने योग्य एक-हलादि उत्तरपद पर उदक के स्थान पर विकल्प से उद् होता है।",
    type=SutraType.VIBHASHA, chapter=6, pada=3, optional=true, kramaValue=630059, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=20,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=hasFirst(context,"उदक") && SamasaSemanticRelation.WATER_FILLED in context.semanticRelations && context.uttaraPada.upadesha.firstOrNull() !in setOf('अ','आ','इ','ई','उ','ऊ','ऋ','ए','ऐ','ओ','औ')
    override fun apply(context: SamasaRuleContext)=replaceFirst(context,"उद्","6.3.59")
}

/** 6.3.60: मन्थौदनसक्तुबिन्दुवज्रभारहारवीवधगाहेषु च. */
object ManthaudanaSaktuBinduVajraSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.60", text="मन्थौदनसक्तुबिन्दुवज्रभारहारवीवधगाहेषु च", hindiExplanation="पठित उत्तरपदों से पहले उदक के स्थान पर विकल्प से उद् होता है।",
    type=SutraType.VIBHASHA, chapter=6, pada=3, optional=true, kramaValue=630060, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=20,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=hasFirst(context,"उदक") && context.uttaraPada.upadesha in setOf("मन्थ","ओदन","सक्तु","बिन्दु","वज्र","भार","हार","वीवध","गाह")
    override fun apply(context: SamasaRuleContext)=replaceFirst(context,"उद्","6.3.60")
}
