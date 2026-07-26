package dev.panini.dhatupatha.svadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.dhatuPatha
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

object SvadiDhatus {
    val all: List<Dhatu> = dhatuPatha(DhatuGana.SVADI) {
        dhatu(
            "05.0001", 1, "षुञ्", "सु",
            "अभिषवे", "स्नान करना, यंत्रादि द्वारा अर्क निकालना, मथना, मद्य चुआना", "to bathe, to extract, to distill, to juice, to agitate, to churn",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0002", 2, "षिञ्", "सि",
            "बन्धने", "बांधना, गूंथना", "to tie,to bind, to quill, to interlock",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0003", 3, "शिञ्", "शि",
            "निशाने", "तीक्ष्ण करना, पैना करना, पतला करना", "to sharpen, to whet",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0004", 4, "डुमिञ्", "मि",
            "प्रक्षेपणे", "फेंकना, फैलाना", "to throw,to scatter",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0005", 5, "चिञ्", "चि",
            "चयने", "चुनना, बटोरना, एकत्र करना", "to collect, to select, to pick",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.DVIKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0006", 6, "स्तृञ्", "स्तृ",
            "आच्छादने", "आच्छादित करना, ढकना, फैलाना, विस्तार होना या करना, बिछाना", "to cover,to eclipse, to spread, to expand",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0007", 7, "कृञ्", "कृ",
            "हिंसायाम्", "मार डालना, दुःख देना, सताना", "to kill, to destroy, to irritate",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0008", 8, "वृञ्", "वृ",
            "वरणे", "पसंद करना, नियोजित करना, नियमित करना", "to choose,to select,to marry,to finalize",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0009", 9, "धुञ्", "धु",
            "कम्पने", "कांपना, हिलना", "to shake,to tremble",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0010", 10, "धूञ्", "धू",
            "कम्पने", "", "to shake,to tremble",
            PadaType.UBHAYAPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0011", 11, "टुदु", "दु",
            "उपतापे", "दुःख देना, जलना, तप्त करना, जलाना", "to give pain,to burn,to heat, to set on fire",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0012", 12, "हि", "हि",
            "गतौ वृद्धौ च", "जाना, बढ़ना", "to go,to grow",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0013", 13, "पृ", "पृ",
            "प्रीतौ", "तृप्त करना, संतुष्ट करना", "to satisfy, to please",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0014", 14, "स्पृ", "स्पृ",
            "प्रीतिपालनयोः प्रीतिचलनयोश्च", "सन्तुष्ट करना, प्रसन्न करना, पालन करना, जाना", "to satisfy, to please, to nurture, to nourish, to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0015", 15, "स्मृ", "स्मृ",
            "प्रीतिबलनयोः", "", "to satisfy, to please, to force",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0016", 16, "आपॢँ", "आप्",
            "व्याप्तौ", "व्याप्त होना, व्यापना", "to obtain,to pervade,to occupy,to reach, to get",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0017", 17, "शकॢँ", "शक्",
            "शक्तौ", "शक्तिवान होना, समर्थ होना, शकना", "to be able,to be powerful",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0018", 18, "राधँ", "राध्",
            "संसिद्धौ", "पूरा करना, सिद्ध करना", "to accomplish, to attain, to fulfill, to achieve",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0019", 19, "साधँ", "साध्",
            "संसिद्धौ", "सिद्ध करना, जय पाना, साधना करना", "to accomplish, to attain, to fulfill,  to achieve",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0020", 20, "अशूँ", "अश्",
            "व्याप्तौ सङ्घाते च", "फैलना, राशि करना, ढेर करना", "to pervade,to heap,to pile up",
            PadaType.ATMANEPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0021", 21, "ष्टिघँ", "स्तिघ्",
            "आस्कन्दने", "हल्ला करना, तिरस्कार करना, वध करना", "to attack, to hate, to kill, to destroy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0022", 22, "तिकँ", "तिक्",
            "आस्कन्दने गतौ च", "तिरस्कार करना, वध करना, जाना", "to attack, to hate, to kill, to destroy, to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0023", 23, "तिगँ", "तिग्",
            "आस्कन्दने गतौ च", "तिरस्कार करना, वध करना, जाना", "to attack, to hate, to kill, to destroy, to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0024", 24, "षघँ", "सघ्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0025", 25, "ञिधृषाँ", "धृष्",
            "प्रागल्भ्ये", "गर्व करना, अपने को बड़ा समझना", "to boast, to be proud, to show off",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0026", 26, "दन्भुँ", "दम्भ्",
            "दम्भने", "ठगना, वंचना करना", "to deceive,to cheat",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0027", 27, "ऋधुँ", "ऋध्",
            "वृद्धौ", "बढ़ना", "to increase,to prosper",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0028", 28, "तृपँ", "तृप्",
            "प्रीणने", "", "to satisfy, to please",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0029", 29, "अहँ", "अह्",
            "व्याप्तौ", "फैलना, विस्तृत होना", "to pervade, to expand, to spread",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0030", 30, "दघँ", "दघ्",
            "घातने पालने च", "मारना, दुःख देना, संरक्षण करना, पोषण करना", "to kill, to destroy, to protect",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0031", 31, "चमुँ", "चम्",
            "भक्षणे", "खाना", "to eat,to drink",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0032", 32, "रि", "रि",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to damage, to hurt",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0033", 33, "क्षि", "क्षि",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to damage, to hurt",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0034", 34, "चिरि", "चिरि",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to damage, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0035", 35, "जिरि", "जिरि",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to damage, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0036", 36, "दाशँ", "दाश्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to damage, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "05.0037", 37, "दृ", "दृ",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to damage, to hurt",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "05.0038", 38, "ऋक्षि", "ऋक्ष्",
            "हिंसायाम्", "", "to kill, to destroy, to damage, to hurt",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(SuDhatu())
    }
}
