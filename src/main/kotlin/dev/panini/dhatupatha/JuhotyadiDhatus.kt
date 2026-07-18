package dev.panini.dhatupatha

import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Complete Juhotyadi-gaṇa imported from the MIT-shared ashtadhyayi-com data set. */
object JuhotyadiDhatus {
    val all: List<Dhatu> = dhatuPatha(Gana.JUHOTYADI) {
        dhatu(
            "03.0001", 1, "हु", "हु",
            "दानादानयोः आदाने प्रीणने च", "देना, यज्ञ करना, खाना, लेना, भक्षण करना, ग्रहण करना", "to sacrifice,to eat, to consume",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0002", 2, "ञिभी", "भी",
            "भये", "भय करना, डरना", "to fear,to be afraid of,to be anxious about",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0003", 3, "ह्री", "ह्री",
            "लज्जायाम्", "लज्जित होना, शरमाना", "to shy, to be ashamed",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0004", 4, "पॄ", "पॄ",
            "पालनपूरणयोः", "पालन करना, पोषण करना, पूर्ण करना, भरना", "to take care, to nurture, to nourish, o provide, to fill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "03.0005", 5, "पृ", "पृ",
            "पालनपूरणयोः", "", "to take care, to nurture, to nourish, o provide, to fill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "03.0006", 6, "डुभृञ्", "भृ",
            "धारणपोषणयोः", "धारण करना, पोषण करना", "to wear, to bear,to support,to nourish,to protect",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0007", 7, "माङ्", "मा",
            "माने शब्दे च", "नापना, तौलना, मेमियाना", "to measure,to weigh,to limit,to compare in size,to shine,to appear,to sound",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0008", 8, "ओँहाङ्", "हा",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0009", 9, "ओँहाक्", "हा",
            "त्यागे", "त्यागना, छोड़ना, परित्याग करना", "to abandon,to leave,to desert,to omit,to neglect",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0010", 10, "डुदाञ्", "दा",
            "दाने", "देना, सौंपना", "to give, to provide, to donate, to handover",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0011", 11, "डुधाञ्", "धा",
            "धारणपोषणयोः", "धारण करना, पहनना, पालन करना, देना", "to wear, to obey, to wear, to bear,to support,to nourish,to protect",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0012", 12, "णिजिँर्", "निज्",
            "शौचपोषणयोः", "शुद्ध करना, स्वच्छ करना, पालन करना", "to wash,to purify,to cleanse,to nourish, to support",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0013", 13, "विजिँर्", "विज्",
            "पृथग्भावे", "पृथक करना, अलग होना", "to split, to separate,to divide,to distinguish,to discern",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0014", 14, "विषॢँ", "विष्",
            "व्याप्तौ", "फैलना, व्याप्त होना", "to pervade,to spread",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0015", 15, "घृ", "घृ",
            "क्षरणदीप्त्योः", "टपकना, क्षरित होना, चमकना, प्रकाशित होना", "to sprinkle,to ooze out, to drip, to shine,to glow",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0016", 16, "हृ", "हृ",
            "प्रसह्यकरणे", "बलात्कार करना", "to take forcibly, to rape",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0017", 17, "ऋ", "ऋ",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0018", 18, "सृ", "सृ",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0019", 19, "भसँ", "भस्",
            "भर्त्सनदीप्त्योः", "भयभीत करना, दोष लगाना, चमकना", "to frighten, to censure,to blame,to shine, to insult",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "03.0020", 20, "कि", "कि",
            "ज्ञाने", "जानना, समझना", "to know, to understand",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0021", 21, "कितँ", "कित्",
            "ज्ञाने", "", "to know, to understand",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "03.0022", 22, "तुरँ", "तुर्",
            "त्वरणे", "जल्दी करना", "to make haste,to be quick",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "03.0023", 23, "धिषँ", "धिष्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "03.0024", 24, "धनँ", "धन्",
            "धान्ये", "उत्पादन करना, पैदा करना", "to bear fruit,to produce crops",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "03.0025", 25, "जनँ", "जन्",
            "जनने", "उत्पन्न करना, पैदा करना", "to create, to procreate, to make",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "03.0026", 26, "गा", "गा",
            "स्तुतौ", "प्रशंसा करना, सराहना", "to praise,to acclaim",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
    }
}
