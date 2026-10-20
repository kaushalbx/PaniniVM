package dev.panini.linganushasanam

import dev.panini.core.LingaSemanticClass
import dev.panini.core.SamasaType
import dev.panini.core.Vacana
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas

/** Safe, structured matchers for canonical rules not represented by dedicated objects. */
internal object CanonicalRuleMatcher {
    private val conditionalNumbers = setOf(
        "4", "6", "9", "10", "11", "12", "13", "14", "15", "16", "17", "22", "29", "39", "41", "48",
        "53", "55", "57", "61", "64", "67", "70", "72", "74", "77", "80", "83", "86", "89", "91", "93", "96", "99",
        "100", "103", "106", "107", "119", "120", "121", "122", "123", "124", "125", "126", "128", "129", "130", "131", "132", "133",
        "135", "136", "143", "146", "151", "154", "156", "164", "165", "172", "176", "177", "182", "186", "187",
        "188", "189", "190", "191",
    )

    private val exactStems: Map<String, Set<String>> = mapOf(
        "3" to setOf("मातृ", "दुहितृ", "यातृ", "पोतृ", "स्वसृ", "ननान्दृ"),
        "5" to setOf("अशनि", "भरणी", "अरणि"),
        "7" to setOf("वह्नि", "वृष्णि", "अग्नि"),
        "8" to setOf("श्रोणि", "योनि", "ऊर्मि"),
        "15" to setOf("नाभि"),
        "18" to setOf("भूमि", "विद्युत्", "सरित्", "लता", "वनिता"),
        "19" to setOf("यादस्"),
        "20" to setOf("भास्", "स्रुच्", "स्रज्", "दिश्", "दृश्", "उष्णिह्", "उपानह्"),
        "21" to setOf("स्थूणा", "ऊर्णा"),
        "23" to setOf("प्रावृष्", "विप्रुष्", "रुत्", "विट्", "त्विष्"),
        "24" to setOf("दर्वि", "विदि", "वेदि", "खनि", "शाणि", "अभ्रि", "वेणि", "कृषि", "ओषधि", "अङ्गुलि"),
        "25" to setOf("तिथि", "नाडि", "रुचि", "वीचि", "नालि", "धूलि", "किकि", "केलि", "छवि", "रात्रि"),
        "26" to setOf("शष्कुलि", "राजि", "कुटि", "अवन्ति", "वर्ति", "भ्रुकुटि", "त्रुटि", "बलि", "पङ्क्ति"),
        "27" to setOf("प्रतिपद्", "आपद्", "विपद्", "सम्पद्", "शरद्", "संसद्", "परिषद्", "उषस्", "संविद्", "क्षुध्", "पुमुत्", "समिध्"),
        "28" to setOf("आशिस्", "धुर्", "पुर्", "गीर्", "द्वार्"),
        "30" to setOf("स्रज्", "त्वच्", "ज्योक्", "वाच्", "यवागू", "नौ", "स्फिच्", "स्फिज्"),
        "31" to setOf("तटित्", "सीमा", "सम्बाधा"),
        "32" to setOf("चुल्लि", "वेणि", "खारि"),
        "33" to setOf("तारा", "धारा", "ज्योत्स्ना"),
        "34" to setOf("शलाका"),
        "40" to setOf("याच्ञा"),
        "38" to setOf("भय", "लिङ्ग", "भग", "पद"),
        "42" to setOf("इषुधि"),
        "44" to setOf("त्रिविष्टप", "त्रिपिष्टप", "त्रिभुवन"),
        "45" to setOf("दिव्", "द्यौ"),
        "46" to setOf("इषु", "बाहु"),
        "47" to setOf("बाण", "काण्ड"),
        "49" to setOf("क्रतु", "पुरुष", "कपोल", "गुल्फ", "मेघ"),
        "50" to setOf("अभ्र"),
        "52" to setOf("धेनु", "रज्जु", "कुहु", "सरयु", "तनु", "रेणु", "प्रियङ्गु"),
        "54" to setOf("श्मश्रु", "जानु", "स्वादु", "अश्रु", "जतु", "त्रपु", "तालु"),
        "56" to setOf("मद्गु", "मधु", "शीधु", "सानु", "कमण्डलु"),
        "58" to setOf("दारु", "कसेरु", "जत्रु", "वस्तु", "मस्तु"),
        "59" to setOf("सक्तु"),
        "62" to setOf("चिबुक", "शालूक", "प्रातिपदिक", "अंशुक", "उल्मुक"),
        "63" to setOf(
            "कण्टक", "अनीक", "सरक", "मोदक", "चसक", "मस्तक", "पुस्तक", "तटाक", "निष्क", "शुष्क",
            "वर्चस्क", "पिनाक", "भाण्डक", "पिण्डक", "कटक", "दण्डक", "पिटक", "तालक", "फलक", "कल्क", "पुलाक",
        ),
        "65" to setOf("किरीट", "मुकुट", "ललाट", "वट", "वीट", "शृङ्गाटक", "रट", "लोष्ट"),
        "66" to setOf("कुट", "कूट", "कपट", "कपाट", "कर्पट", "नट", "निकट", "कीट", "कट"),
        "68" to setOf("ऋण", "लवण", "पर्ण", "तोरण", "रण", "उष्ण"),
        "69" to setOf("कार्षापण", "स्वर्ण", "सुवर्ण", "व्रण", "चरण", "वृषण", "विषाण", "चूर्ण", "तृण"),
        "71" to setOf("काष्ठ", "पृष्ठ", "रिक्थ", "सिक्थ", "उक्थ"),
        "73" to setOf("तीर्थ", "प्रोथ", "यूथ", "गूथ"),
        "75" to setOf("जघन", "अजिन", "तुहिन", "कानन", "वन", "वृजिन", "विपिन", "वेतन", "शासन", "सोपान", "मिथुन", "श्मशान", "रत्न", "निम्न", "चिह्न"),
        "76" to setOf("मान", "यान", "अभिधान", "नलिन", "पुलिन", "उद्यान", "शयन", "आसन", "स्थान", "चन्दन", "आलान", "समान", "भवन", "वसन", "सम्भावन", "विभावन", "विमान"),
        "78" to setOf("पाप", "रूप", "उडुप", "तल्प", "शिल्प", "पुष्प", "शष्प", "समीप", "अन्तरीप"),
        "79" to setOf("शूर्प", "कुतप", "कुणप", "द्वीप", "विटप"),
        "81" to setOf("तलभ"),
        "82" to setOf("जृम्भ"),
        "84" to setOf("रुक्म", "सिध्म", "युग्म", "इध्म", "गुल्म", "अध्यात्म", "कुङ्कुम"),
        "85" to setOf("सङ्ग्राम", "दाडिम", "कुसुम", "आश्रम", "क्षेम", "क्षौम", "होम", "उद्दाम"),
        "87" to setOf("किसलय", "हृदय", "इन्द्रिय", "उत्तरीय"),
        "88" to setOf("गोमय", "कषाय", "मलय", "अन्वय", "अव्यय"),
        "90" to setOf(
            "द्वार", "अग्र", "स्फार", "तक्र", "वक्र", "वप्र", "क्षिप्र", "क्षुद्र", "नार", "पार", "तीर", "दूर",
            "कृच्छ्र", "रन्ध्र", "अस्र", "अश्र", "शुभ्र", "श्वभ्र", "भीर", "गभीर", "क्रूर", "विचित्र", "केयूर",
            "केदार", "उदर", "अजस्र", "शरीर", "कन्दर", "मन्दार", "पञ्जर", "अजर", "जठर", "अजिर", "वैर",
            "चामर", "पुष्कर", "गह्वर", "कुहर", "कुटीर", "कुलीर", "चत्वर", "काश्मीर", "नीर", "अम्बर", "शिशिर",
            "तन्त्र", "यन्त्र", "क्षत्र", "क्षेत्र", "मित्र", "कलत्र", "चित्र", "छत्र", "मूत्र", "सूत्र", "वक्त्र",
            "नेत्र", "गोत्र", "अङ्गुलित्र", "तनुत्र", "भलत्र", "अस्त्र", "शस्त्र", "शास्त्र", "वस्त्र", "पत्र", "पात्र", "नक्षत्र",
        ),
        "92" to setOf("चक्र", "वज्र", "अन्धकार", "सार", "आवार", "पार", "क्षीर", "तोमर", "शृङ्गार", "भृङ्गार", "मन्दार", "उशीर", "तिमिर", "शिशिर"),
        "94" to setOf("शिरीष", "ऋजीष", "अम्बरीष", "पीयूष", "पुरीष", "किल्विष", "कल्मष"),
        "95" to setOf("यूष", "करीष", "मिष", "विष", "वर्ष"),
        "97" to setOf("मानस", "पनस", "बिस", "बुस", "साहस"),
        "98" to setOf("चमस", "अंस", "रस", "निर्यास", "उपवास", "कार्पास", "वास", "मास", "कास", "कांस", "मांस"),
        "101" to setOf("दीधिति"),
        "102" to setOf("दिन", "अहन्"),
        "104" to setOf("द्रोण", "आढक"),
        "105" to setOf("खारी", "मानिका"),
        "108" to setOf("मरुत्", "गरुत्", "उत्तर", "ऋत्विज्"),
        "134" to setOf("अपथ", "पुण्याह"),
        "137" to setOf("अर्चिस्"),
        "138" to setOf("छदिस्"),
        "139" to setOf("मुख", "नयन", "लोह", "वन", "मांस", "रुधिर", "कार्मुक", "विवर", "जल", "हल", "धन", "अन्न"),
        "140" to setOf("सीर", "अर्थ", "ओदन"),
        "141" to setOf("वक्त्र", "नेत्र", "अरण्य", "गाण्डीव"),
        "142" to setOf("अटवी"),
        "144" to setOf("तूल", "उपल", "ताल", "कुसूल", "तरल", "कम्बल", "देवल", "वृषल"),
        "148" to setOf("लक्षा", "कोटी"),
        "149" to setOf("शङ्कु"),
        "150" to setOf("सहस्र"),
        "152" to setOf("ब्रह्मन्"),
        "153" to setOf("नामन्", "रोमन्"),
        "155" to setOf("अप्सरस्"),
        "157" to setOf("यात्रा", "मात्रा", "भस्त्रा", "दंष्ट्रा", "वस्त्रा"),
        "159" to setOf("पत्र", "पात्र", "पवित्र", "सूत्र", "छत्र"),
        "160" to setOf("बल", "कुसुम", "शुल्ब", "पत्तन", "रण"),
        "161" to setOf("पद्म", "कमल", "उत्पल"),
        "162" to setOf("आहव", "सङ्ग्राम"),
        "163" to setOf("आजि"),
        "169" to setOf("दैव"),
        "166" to setOf("वियत्", "जगत्", "सकृत्", "शकन्", "पृषत्", "शकृत्", "यकृत्", "उदश्वित्"),
        "167" to setOf("नवनीत", "अवनत", "अनृत", "अमृत", "निमित्त", "वित्त", "चित्त", "पित्त", "व्रत", "रजत", "वृत्त", "पलित"),
        "168" to setOf("श्राद्ध", "कुलिश", "दैव", "पीठ", "कुण्ड", "अङ्क", "अङ्ग", "दधि", "सक्थि", "अक्षि", "अस्थि", "आस्पद", "आकाश", "कण्व", "बीज", "भाण्ड"),
        "170" to setOf("धान्य", "आज्य", "शस्य", "रूप्य", "पण्य", "वर्ण्य", "धिष्ण्य", "हव्य", "कव्य", "काव्य", "सत्य", "अपत्य", "मूल्य", "शिक्य", "कुड्य", "मद्य", "हर्म्य", "तूर्य", "सैन्य"),
        "171" to setOf("द्वन्द्व", "बर्ह", "दुःख", "बडिश", "पिच्छ", "बिम्ब", "विम्ब", "कुटुम्ब", "कवच", "कवर", "वृन्द", "राका"),
        "147" to setOf("शत", "अयुत", "प्रयुत"),
        "174" to setOf("गो", "मणि", "यष्टि", "मुष्टि", "पाटलि", "बस्ति", "शाल्मलि", "त्रुटि", "मसि", "मरीचि"),
        "175" to setOf("मृत्यु", "मन्यु", "शीधु", "कर्कन्धु", "किष्कु", "सिन्धु", "कण्डु", "रेणु"),
        "179" to setOf("धृत", "भूत", "मुस्त", "क्ष्वेलित", "ऐरावत", "पुस्त", "बुस्त", "लोहित"),
        "180" to setOf("शृङ्ग", "अर्ध", "निदाघ", "उद्यम", "शल्य", "दृढ"),
        "181" to setOf("व्रज", "कुञ्ज", "कुथ", "कूर्च", "प्रस्थ", "दर्प", "अर्भ", "अर्धर्च", "दर्भ", "पुच्छ"),
        "184" to setOf("गृह", "मेह", "देह", "पट्ट", "पटह", "अष्टापद", "अर्बुद", "ककुद"),
    )

    val modeledNumbers: Set<String> get() = exactStems.keys + conditionalNumbers

    private val structuralNumbers = setOf(
        "10", "12", "17", "48", "57", "61", "64", "67", "70", "74", "77", "80", "83", "86", "89",
        "93", "96", "133", "136", "143", "151", "154", "156", "176", "182", "187",
    )

    /** Lexical exceptions outrank contextual rules, which outrank broad phonological classes. */
    fun priorityFor(number: String): Int = when (number) {
        in exactStems -> 50
        in setOf("10", "17", "136") -> 30
        in structuralNumbers -> 10
        else -> 40
    }

    fun matches(number: String, context: LingaRuleContext): Boolean {
        val stemVarnas = context.pratipadika.toVarnas()
        if (exactStems[number].orEmpty().any { stemVarnas == it.toVarnas() }) return true
        val upadha = context.upadha?.toVarnas()?.firstOrNull { it is Vyanjana }
            ?: stemVarnas.dropLast(1).lastOrNull()
        val isAkaranta = stemVarnas.lastOrNull() == Svara.A

        return when (number) {
            "4" -> context.isDhatuDerivative && context.pratyaya in setOf("अनि", "ऊ")
            "6" -> context.isDhatuDerivative && context.pratyaya in setOf("मि", "नि")
            "9" -> context.pratyaya == "क्तिन्"
            "10" -> stemVarnas.lastOrNull() == Svara.II
            "11" -> context.pratyaya in setOf("ऊङ्", "ङीप्", "ङीष्", "टाप्", "डाप्", "चाप्")
            "12" -> context.syllableCount == 1 && stemVarnas.lastOrNull() in setOf(Svara.I, Svara.II, Svara.U, Svara.UU)
            "13" -> context.numericalValue in 20L..89L
            "14" -> context.pratipadika.phonologicallyEquals("दुन्दुभि") && LingaSemanticClass.AKSHA_DICE in context.semanticClasses
            "15" -> context.pratipadika.phonologicallyEquals("नाभि") && LingaSemanticClass.NON_KSHATRIYA in context.semanticClasses
            "16" -> context.pratipadika.phonologicallyIn(setOf("दुन्दुभि", "नाभि")) &&
                LingaSemanticClass.CONTRASTED_OTHER_SENSE in context.semanticClasses
            "17" -> context.pratyaya == "तल्" || stemVarnas.endsWithVarnas("ता")
            "22" -> context.padas == listOf("गृह", "स्थूणा") || context.padas == listOf("शश", "ऊर्णा")
            "29" -> context.vacana == Vacana.BAHUVACANA && context.pratipadika.phonologicallyIn(setOf("अप्", "सुमनस्", "समासिकता", "वर्षा"))
            "39" -> context.pratyaya == "नङ्"
            "41" -> context.pratyaya == "कि" && LingaSemanticClass.GHU_ROOT in context.semanticClasses
            "48" -> stemVarnas.lastOrNull() == Vyanjana.NA
            "53" -> context.pratipadika.phonologicallyEquals("रज्जु") && context.samasaType != null
            "55" -> context.pratipadika.phonologicallyEquals("वसु") && LingaSemanticClass.ARTHA_WEALTH in context.semanticClasses
            "57" -> stemVarnas.endsWithVarnas("रु") || stemVarnas.endsWithVarnas("तु")
            "61" -> isAkaranta && upadha == Vyanjana.KA
            "64" -> isAkaranta && upadha == Vyanjana.TTA
            "67" -> isAkaranta && upadha == Vyanjana.NNA
            "70" -> isAkaranta && upadha == Vyanjana.THA
            "72" -> context.pratipadika.phonologicallyEquals("काष्ठा") && LingaSemanticClass.DIRECTION in context.semanticClasses
            "74" -> isAkaranta && upadha == Vyanjana.NA
            "77" -> isAkaranta && upadha == Vyanjana.PA
            "80" -> isAkaranta && upadha == Vyanjana.BHA
            "83" -> isAkaranta && upadha == Vyanjana.MA
            "86" -> isAkaranta && upadha == Vyanjana.YA
            "89" -> isAkaranta && upadha == Vyanjana.RA
            "91" -> context.pratipadika.phonologicallyEquals("शुक्र") && LingaSemanticClass.NON_DEITY in context.semanticClasses
            "93" -> isAkaranta && upadha == Vyanjana.SSA
            "96" -> isAkaranta && upadha == Vyanjana.SA
            "99" -> context.pratipadika.phonologicallyEquals("कंस") && LingaSemanticClass.NON_ANIMATE in context.semanticClasses
            "100" -> LingaSemanticClass.RAY_OR_DAY in context.semanticClasses
            "103" -> LingaSemanticClass.MEASURE in context.semanticClasses
            "106" -> context.vacana == Vacana.BAHUVACANA && context.pratipadika.phonologicallyIn(setOf("दार", "अक्षत", "लाज", "असु"))
            "107" -> LingaSemanticClass.WOUND_OR_LIMB in context.semanticClasses
            "119" -> context.pratyaya == "ल्युट्" && LingaSemanticClass.BHAVA in context.semanticClasses
            "120" -> context.pratyaya in setOf("क्त", "क्तवतुँ") && LingaSemanticClass.BHAVA in context.semanticClasses
            "121" -> context.pratyaya in setOf("त्व", "ष्यञ्")
            "122" -> context.isQualityExpression && context.pratyaya in setOf("त्व", "ष्यञ्") &&
                context.semanticClasses.any { it == LingaSemanticClass.BHAVA || it == LingaSemanticClass.KARMAN }
            "123" -> context.pratyaya in setOf("यत्", "य", "ढक्", "यक्", "अञ्", "अण्", "वुञ्", "छ") &&
                context.semanticClasses.any { it == LingaSemanticClass.BHAVA || it == LingaSemanticClass.KARMAN }
            "124" -> context.samasaType == SamasaType.AVYAYIBHAVA
            "125" -> context.samasaType == SamasaType.DVANDVA && context.vacana == Vacana.EKAVACANA
            "126" -> context.isVedicUsage && context.pratipadika.phonologicallyIn(setOf("हेमन्तशिशिर", "अहोरात्र"))
            "128" -> context.samasaType == SamasaType.TATPURUSA && context.padas.lastOrNull() == "छाया" &&
                LingaSemanticClass.ABUNDANT in context.semanticClasses
            "129" -> context.samasaType == SamasaType.TATPURUSA && context.padas.lastOrNull() == "सभा" &&
                context.semanticClasses.any { it == LingaSemanticClass.KING_TITLE || it == LingaSemanticClass.NON_HUMAN }
            "130" -> context.samasaType == SamasaType.TATPURUSA &&
                context.padas.lastOrNull() in setOf("सुरा", "सेना", "छाया", "शाला", "निशा")
            "132" -> context.samasaType != null && context.padas.lastOrNull() in setOf("रात्र", "अह्न", "अह")
            "133" -> stemVarnas.endsWithVarnas("रात्रि") && context.numericalValue != null
            "135" -> context.samasaType == SamasaType.DVIGU
            "136" -> stemVarnas.takeLast(2) in setOf(
                listOf(Svara.I, Vyanjana.SA),
                listOf(Svara.U, Vyanjana.SA),
            )
            "143" -> upadha == Vyanjana.LA
            "146" -> context.numericalValue != null && context.numericalValue >= 100
            "151" -> stemVarnas.endsWithVarnas("मन्") && context.syllableCount == 2 &&
                LingaSemanticClass.ACTION_AGENT !in context.semanticClasses
            "154" -> stemVarnas.endsWithVarnas("अस्") && context.syllableCount == 2
            "156" -> stemVarnas.endsWithVarnas("त्र")
            "164" -> LingaSemanticClass.FRUIT_KIND in context.semanticClasses
            "165" -> LingaSemanticClass.TREE_KIND in context.semanticClasses
            "172" -> context.pratipadika.phonologicallyEquals("अक्ष") && LingaSemanticClass.SENSE_ORGAN in context.semanticClasses
            "176" -> context.isQualityExpression && stemVarnas.lastOrNull() in setOf(Svara.U, Svara.UU)
            "177" -> LingaSemanticClass.DESCENDANT in context.semanticClasses
            "182" -> stemVarnas.endsWithVarnas("कबन्ध") || stemVarnas.endsWithVarnas("औषध") ||
                stemVarnas.endsWithVarnas("आयुध")
            "186" -> context.isIndeclinable || context.pratipadika.phonologicallyIn(setOf("कति", "युष्मद्", "अस्मद्"))
            "187" -> context.numericalValue != null && stemVarnas.lastOrNull() in
                setOf(Vyanjana.SSA, Vyanjana.NA, Vyanjana.NNA)
            "131" -> context.samasaType in setOf(SamasaType.TATPURUSA, SamasaType.NAN_TATPURUSA, SamasaType.UPAPADA_TATPURUSA, SamasaType.DVANDVA) &&
                context.governingLinga != null
            "188" -> context.isQualityExpression && context.governingLinga != null
            "189" -> context.governingLinga != null &&
                context.pratyaya in setOf("तव्य", "तव्यत्", "अनीयर्", "यत्", "ण्यत्", "क्यप्")
            "190" -> context.pratyaya == "ल्युट्" && context.semanticClasses.any {
                it == LingaSemanticClass.INSTRUMENT || it == LingaSemanticClass.LOCATION
            } && context.governingLinga != null
            "191" -> context.isPronoun && context.governingLinga != null
            else -> false
        }
    }

}
