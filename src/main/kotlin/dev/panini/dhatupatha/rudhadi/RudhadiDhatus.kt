package dev.panini.dhatupatha.rudhadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.Gana
import dev.panini.dhatupatha.PadaType
import dev.panini.dhatupatha.dhatuPatha
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

object RudhadiDhatus {
    val all: List<Dhatu> = dhatuPatha(Gana.RUDHADI) {
        dhatu(
            "07.0001", 1, "रुधिँर्", "रुध्",
            "आवरणे", "रोकना, घेर लेना, घेरना", "to obstruct, to surround, to besiege, to enclose, to obscure",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.DVIKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "07.0002", 2, "भिदिँर्", "भिद्",
            "विदारणे", "चीरना, तोडना", "to cut, to break, to divide",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "07.0003", 3, "छिदिँर्", "छिद्",
            "द्वैधीकरणे", "छिन्न भिन्न करना", "to cut,to grind, to truncate, to incise",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "07.0004", 4, "रिचिँर्", "रिच्",
            "विरेचने", "अलग होना, मलशुद्धि होना, गर्भपात कराना, झाड़ा होना", "to separate, to abort, to purify the body, to sweep, to cleanse the body",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "07.0005", 5, "विचिँर्", "विच्",
            "पृथग्भावे", "अलग करना या होना, टूटना, छूटना", "to divide,to separate,to remove from",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "07.0006", 6, "क्षुदिँर्", "क्षुद्",
            "सम्पेषणे", "कूटना, पीसना, चूर्ण बनाना", "to crush, to grind, to powder",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(YujirDhatu())
        dhatu(
            "07.0008", 8, "उँछृदिँर्", "छृद्",
            "दीप्तिदेवनयोः", "चमकना, प्रकाशित होना, क्रीड़ा करना, खेलना", "to glow, to shine,to play",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "07.0009", 9, "उँतृदिँर्", "तृद्",
            "हिंसानादरयोः", "हिंसा करना, अपमान करना", "to kill,to destroy, to disregard, to insult",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "07.0010", 10, "कृतीँ", "कृत्",
            "वेष्टने", "घेर लेना, वेष्टति करना", "to surround, to cover",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "07.0011", 11, "ञिइन्धीँ", "इन्ध्",
            "दीप्तौ", "प्रकाशित होना", "to shine, to glow",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "07.0012", 12, "खिदँ", "खिद्",
            "दैन्ये", "खिन्न होना, दुःखी होना", "to be sad, to be distressed, to have sorrow",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(VidDhatu())
        dhatu(ShishDhatu())
        dhatu(
            "07.0015", 15, "पिषॢँ", "पिष्",
            "सञ्चूर्णने हिंसायाम् च", "चूर्ण करना, पीसना, मारना", "to grind,to powder,to crush, to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "07.0016", 16, "भन्जोँ", "भञ्ज्",
            "आमर्दने", "नष्ट करना", "to destroy",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "07.0017", 17, "भुजँ", "भुज्",
            "पालनाभ्यवहारयोः", "संरक्षण करना, पालन करना, खाना, भक्षण करना", "to protect, to preserve, to eat, to consume",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "07.0018", 18, "तृहँ", "तृह्",
            "हिंसायाम्", "मार डालना, दुःख देना", "to kill to destroy, to give pain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "07.0019", 19, "हिसिँ", "हिंस्",
            "हिंसायाम्", "मारना, दुःख देना", "to kill to destroy, to give pain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "07.0020", 20, "उन्दीँ", "उन्द्",
            "क्लेदने", "आर्द्र होना, गीला होना", "to moisten, to be wet",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "07.0021", 21, "अन्जूँ", "अञ्ज्",
            "व्यक्तिम्रक्षणकान्तिगतिषु", "जोड़ना", "to anoint, to polish, to smear",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "07.0022", 22, "तन्चूँ", "तञ्च्",
            "सङ्कोचने", "संकुचित होना, संकोच होना", "to contract, to reduce, to constrict",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "07.0023", 23, "ओँविजीँ", "विज्",
            "भयचलनयोः", "डरना, डर से कम्पित होना, कांपना, आपदग्रस्त होना, चलना", "to fear,to tremble, to shiver, to be distressed, to move",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "07.0024", 24, "वृजीँ", "वृज्",
            "वर्जने", "छोड़ना, वर्जित करना", "to avoid,to abandon, to leave behind",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "07.0025", 25, "पृचीँ", "पृच्",
            "सम्पर्के", "सम्पर्क करना, स्पर्श करना, संयोग करना", "to contact, to touch, to join, to unite",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
    }
}
