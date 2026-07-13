package dev.sanskrit.dhatupatha

import dev.sanskrit.shiksha.Accent
import dev.sanskrit.shiksha.ItStatus
import dev.sanskrit.shiksha.Karmatva

/** Complete Curadi-gaṇa imported from the MIT-shared ashtadhyayi-com data set. */
object CuradiDhatus {
    val all: List<Dhatu> = dhatuPatha(Gana.CURADI) {
        dhatu(
            "10.0001", 1, "चुरँ", "चुर्",
            "स्तेये", "चोरी करना", "to steal",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0002", 2, "चितिँ", "चिन्त्",
            "स्मृत्याम्", "चिन्तन करना, स्मरण करना, याद करना, चिन्ता करना", "to contemplate,to think,to discuss,to remember, to worry",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0003", 3, "यत्रिँ", "यन्त्र्",
            "सङ्कोचे", "संकोच करना, लजाना", "to restrain,to shy",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0004", 4, "स्फुडिँ", "स्फुण्ड्",
            "परिहासे", "विनोद करना, हसी करना, थट्टा करना", "to make fun of, to laugh at, to joke",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0005", 5, "स्फुटिँ", "स्फुण्ट्",
            "परिहासे", "", "to make fun of, to laugh at, to joke",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0006", 6, "लक्षँ", "लक्ष्",
            "दर्शनाङ्कनयोः", "देखना, चिन्ह करना, संकेत लगाना, तारतम्य देखना, विवेचन करना", "to observe,to see,to look, to mark, to point, to deliberate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0007", 7, "कुद्रिँ", "कुन्द्र्",
            "अनृतभाषणे", "झूठ बोलना", "to tell a lie",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0008", 8, "कुदृँ", "कुद्",
            "अनृतभाषणे", "", "to tell a lie",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0009", 9, "स्पुडिँ", "स्पुण्ड्",
            "परिहासे", "विनोद करना, हसी करना, थट्टा करना", "to make fun of, to laugh at, to joke",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0010", 10, "लडँ", "लड्",
            "उपसेवायाम्", "पालन करना, चाहना", "to serve, to obey, to desire, to wish",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0011", 11, "मिदिँ", "मिन्द्",
            "स्नेहने", "स्निग्ध होना, पिघलना, पोतना, प्रीती करना, नरम होना", "to soften, to melt, to become greasy, to love",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0012", 12, "मिदँ", "मिद्",
            "स्नेहने", "", "to soften, to melt, to become greasy, to love",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0013", 13, "ओँलडिँ", "लण्ड्",
            "उत्क्षेपणे", "ऊपर फेंकना, ऊपर उड़ाना", "to throw up",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0014", 14, "ओलडिँ", "ओलण्ड्",
            "उत्क्षेपणे", "", "to throw up",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0015", 15, "जलँ", "जल्",
            "अपवारणे", "ढांकना, निवारण करना, जाल से ढांकना", "to cover,to hide, to cover by net",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0016", 16, "लजँ", "लज्",
            "अपवारणे", "", "to cover,to hide, to cover by net",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0017", 17, "पीडँ", "पीड्",
            "अवगाहने", "दुःख देना, पीड़ा करना, चेताना", "to harass,to annoy,to irritate,to give pain",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0018", 18, "नटँ", "नट्",
            "अवस्पन्दने", "दिखाना", "to enact, to dance, to act, to show",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0019", 19, "श्रथँ", "श्रथ्",
            "प्रयत्ने", "प्रयत्न करना, जाना", "to make effort, to try, to set out",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0020", 20, "बधँ", "बध्",
            "संयमने", "नियंत्रित करना, बांधना", "to bind,to restrain,to control",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0021", 21, "बन्धँ", "बन्ध्",
            "संयमने", "", "to bind,to restrain,to control",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0022", 22, "पॄ", "पॄ",
            "पूरणे", "भरना, पूर्ण करना", "to fill, to complete",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0023", 23, "उर्जँ", "ऊर्ज्",
            "बलप्राणनयोः", "शक्तिमान होण ,पराक्रमी होना, जीना, जिलाना", "to be strong,to live with energy, to win",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0024", 24, "पक्षँ", "पक्ष्",
            "परिग्रहे", "ग्रहण करना, लेना, एक पक्ष का स्वीकार करना, एक और होना", "to seize,to accept,to take side",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0025", 25, "वर्णँ", "वर्ण्",
            "प्रेरणे", "आज्ञा करना, प्रेरणा करना, रंग देना, रंगना, खींचना, संकोच करना", "to order, to motivate, to inspire, to paint, to color",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0026", 26, "चुर्णँ", "चूर्ण्",
            "प्रेरणे ", "आज्ञा करना, प्रेरणा करना, रंग देना, रंगना, खींचना, संकोच करना", "to order, to motivate, to inspire, to paint, to color, to pull",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0027", 27, "प्रथँ", "प्रथ्",
            "प्रख्याने", "विस्तार करना, फैलाना, जावना", "to expand scope, to spread",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0028", 28, "पृथँ", "पृथ्",
            "प्रक्षेपे", "फेंकना, उड़ाना, प्रेरणा करना, भेजना", "to throw,to send,to direct, to inspire",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0029", 29, "पथँ", "पथ्",
            "प्रक्षेपे", "", "to throw,to send,to direct, to inspire",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0030", 30, "षन्बँ", "सम्ब्",
            "सम्बन्धने", "संयोग करना, मिलाप करना, जोड़ना", "to join, to articulate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0031", 31, "शन्बँ", "शम्ब्",
            "सम्बन्धने", "ढेर करना, बटोरना, एकत्र करना, जोड़ना", "to join, to articulate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0032", 32, "सान्बँ", "साम्ब्",
            "सम्बन्धने", "", "to join, to articulate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0033", 33, "भक्षँ", "भक्ष्",
            "अदने", "भोजन करना, खाना", "to eat",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0034", 34, "कुट्टँ", "कुट्ट्",
            "छेदनभर्त्सनयोः", "कतरना, निन्दा करना, दोष लगाना, रगड़ना", "to cut, to criticise, to blame",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0035", 35, "पुट्टँ", "पुट्ट्",
            "अल्पीभावे", "कम होना, अल्प होना, बटोरना", "to decrease,to diminish, to reduce, to shrink",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0036", 36, "चुट्टँ", "चुट्ट्",
            "अल्पीभावे", "कम होना, अल्प होना, बटोरना", "to decrease,to diminish, to reduce, to shrink",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0037", 37, "अट्टँ", "अट्ट्",
            "अनादरे", "अनादर करना, अपमान करना", "to insult, to dishonor, to disrespect",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0038", 38, "षुट्टँ", "सुट्ट्",
            "अनादरे", "अनादर करना, अपमान करना", "to insult, to dishonor, to disrespect",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0039", 39, "लुन्टँ", "लुण्ट्",
            "स्तेये", "", "to steal, to loot",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0040", 40, "लुन्ठँ", "लुण्ठ्",
            "स्तेये", "चुराना, लूटना", "to steal, to loot",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0041", 41, "शठँ", "शठ्",
            "असंस्कारगत्योः", "ठीक न बनाना, आधा ही छोड़ना, असंस्कृत रखना, जाना, चलना", "to cheat,to fraud, to leave unfinished, to leave unattended, to ill-behave",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0042", 42, "श्वठँ", "श्वठ्",
            "असंस्कारगत्योः", "ठीक न बनाना, आधा ही छोड़ना, असंस्कृत रखना, जाना, चलना", "to cheat,to fraud, to leave unfinished, to leave unattended, to ill-behave",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0043", 43, "श्वठिँ", "श्वण्ठ्",
            "असंस्कारगत्योः", "", "to cheat,to fraud, to leave unfinished, to leave unattended, to ill-behave",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0044", 44, "तुजँ", "तुज्",
            "हिंसाबलादाननिकेतनेषु", "", "to kill, to destroy, to hurt, to injure, to become powerful, to take, to dwell, to stay",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0045", 45, "तुजिँ", "तुञ्ज्",
            "हिंसाबलादाननिकेतनेषु", "मार डालना, बलवान होना, ग्रहण करना, रहना, वसति करना", "to kill, to destroy, to hurt, to injure, to become powerful, to take, to dwell, to stay",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0046", 46, "पिजँ", "पिज्",
            "हिंसाबलादाननिकेतनेषु", "", "to kill, to destroy, to hurt, to injure, to become powerful, to take, to dwell, to stay",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0047", 47, "पिजिँ", "पिञ्ज्",
            "हिंसाबलादाननिकेतनेषु", "मार डालना, बलवान होना, ग्रहण करना, रहना, वसति करना", "to kill, to destroy, to hurt, to injure, to become powerful, to take, to dwell, to stay",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0048", 48, "लजिँ", "लञ्ज्",
            "हिंसाबलादाननिकेतनेषु", "", "to kill, to destroy, to hurt, to injure, to become powerful, to take, to dwell, to stay",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0049", 49, "लुजिँ", "लुञ्ज्",
            "हिंसाबलादाननिकेतनेषु", "", "to kill, to destroy, to hurt, to injure, to become powerful, to take, to dwell, to stay",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0050", 50, "पिसँ", "पिस्",
            "गतौ", "जाना", "to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0051", 51, "षान्त्वँ", "सान्त्व्",
            "सामप्रयोगे", "सांत्वना देना, समाधान करना, विवेक की बातें करना", "to pacify, to appease, to console",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0052", 52, "सान्त्वँ", "सान्त्व्",
            "सामप्रयोगे", "", "to pacify, to appease, to console",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0053", 53, "श्वल्कँ", "श्वल्क्",
            "परिभाषणे", "बोलना, भाषण करना", "to speak, to tell, to narrate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0054", 54, "वल्कँ", "वल्क्",
            "परिभाषणे", "बोलना, भाषण करना", "to speak, to tell, to narrate, to see",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0055", 55, "ष्णिहँ", "स्निह्",
            "स्नेहने", "स्नेह करना", "to love, to be kind",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0056", 56, "स्फिटँ", "स्फिट्",
            "हिंसायाम्", "", "to kill, to destroy",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0057", 57, "स्मिटँ", "स्मिट्",
            "अनादरे", "अनादर करना, अपमान करना", "to insult, to disrespect, to dishonor",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0058", 58, "ष्मिङ्", "स्मि",
            "अनादरे", "", "to smile,to expand,to bloom",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0059", 59, "श्लिषँ", "श्लिष्",
            "श्लेषणे", "मिलाप करना, आलिंगन करना, गले लगाना, सटे रहना, चिपके रहना", "to hug,to embrace,to stay attached",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0060", 60, "पथिँ", "पन्थ्",
            "गतौ", "जाना", "to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0061", 61, "पिछँ", "पिच्छ्",
            "कुट्टने", "कूटना, कतरना, चीरना, बहुत दुःख देना", "to cut, to split, to tamp, to slice, to torture",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0062", 62, "छदिँ", "छन्द्",
            "संवरणे", "ढकना, आच्छादन करना, लपेटना", "to cover",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0063", 63, "श्रणँ", "श्रण्",
            "दाने", "देना, दान करना", "to give",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0064", 64, "तडँ", "तड्",
            "आघाते", "मारना, ताड़न करना", "to strike,to beat, to hit",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0065", 65, "खडँ", "खड्",
            "भेदने", "टुकड़े करना, खण्डित करना", "to break",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0066", 66, "खडिँ", "खण्ड्",
            "भेदने", "टुकड़े करना, खण्डित करना, काटना, विभाग करना", "to break in pieces,to break,to cut,to tear to pieces",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0067", 67, "कडिँ", "कण्ड्",
            "भेदने", "भेदन करना, टुकड़े करना, काटना, विभाग करना", "to break in pieces,to break,to cut,to tear to pieces",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0068", 68, "कुडिँ", "कुण्ड्",
            "रक्षणे", "रक्षा करना, संभालना", "to protect",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0069", 69, "गुडिँ", "गुण्ड्",
            "वेष्टने", "घेरना, घेर लेना, पीसना, चूर्ण करना, संरक्षण करना", "to enclose,to cover,to envelope, to protect, to powder",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0070", 70, "कुठिँ", "कुण्ठ्",
            "रक्षणे", "", "to protect",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0071", 71, "गुठिँ", "गुण्ठ्",
            "रक्षणे", "", "to protect",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0072", 72, "खुडिँ", "खुण्ड्",
            "खण्डने", "टुकड़े करना, चीरना", "to cut,to slice, to break into pieces",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0073", 73, "वटिँ", "वण्ट्",
            "विभाजने", "अलग करना, पृथक करना, हिस्सा करना, बांटना", "to divide, to separate, to isolate, to partition",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0074", 74, "वडिँ", "वण्ड्",
            "विभाजने", "", "to divide, to separate, to isolate, to partition",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0075", 75, "चडिँ", "चण्ड्",
            "कोपे", "", "to be angry",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0076", 76, "मडिँ", "मण्ड्",
            "भूषायां हर्षे च", "अलंकृत करना, आनंदित होना", "to adorn,to decorate, to be happy",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0077", 77, "भडिँ", "भण्ड्",
            "कल्याणे", "शुभ कार्य करना", "to do auspicious work",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0078", 78, "छर्दँ", "छर्द्",
            "वमने", "वमन करना", "to vomit",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0079", 79, "पुस्तँ", "पुस्त्",
            "आदरानादरयोः", "सत्कार करना, आदर करना, तिरस्कार करना, अनादर करना, धिक्कारना", "to honour,to dishonour,to respect,to disrespect",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0080", 80, "बुस्तँ", "बुस्त्",
            "आदरानादरयोः", "सत्कार करना, आदर करना, तिरस्कार करना, अनादर करना, धिक्कारना", "to honour,to dishonour,to respect,to disrespect",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0081", 81, "चुदँ", "चुद्",
            "सञ्चोदने", "प्रेरणा करना, पूछना, प्रश्न करना, प्रार्थना करना", "to inspire, to question, to ask, to request",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0082", 82, "नक्कँ", "नक्क्",
            "नाशने", "उच्छेद करना, नाश करना", "to kill, to destroy, to uproot",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0083", 83, "धक्कँ", "धक्क्",
            "नाशने", "उच्छेद करना, नाश करना", "to kill, to destroy, to uproot",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0084", 84, "चक्कँ", "चक्क्",
            "व्यथने", "दुःख देना, दुःख पाना", "to suffer,to give pain",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0085", 85, "चुक्कँ", "चुक्क्",
            "व्यथने", "दुःख देना, दुःख पाना", "to suffer,to give pain",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0086", 86, "क्षलँ", "क्षल्",
            "शौचकर्मणि", "स्वच्छ करना, पवित्र करना, धोना", "to wash,to cleanse, to purify",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0087", 87, "तलँ", "तल्",
            "प्रतिष्ठायाम्", "स्थापना करना, बिठाना, पूर्ण होना, सिद्ध करना", "to establish, to make someone sit, to complete",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0088", 88, "तुलँ", "तुल्",
            "उन्माने", "तोलना, वजन करना", "to weigh,to measure",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0089", 89, "दुलँ", "दुल्",
            "उत्क्षेपे", "उचकना, उठाना, फेंकना, डोलना, हिलना, झूला झुलाना", "to uproot, to remove, to throw, to shake, to move, to swing",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0090", 90, "पुलँ", "पुल्",
            "महत्त्वे", "राशि होना, ढेर होना, बढ़ना, ऊँचा होना", "to be great, to be large, to rise up, to be heaped up",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0091", 91, "चुलँ", "चुल्",
            "समुच्छ्राये", "बढ़ाना, ऊँचा करना, भिगोना, डुबोना", "to raise,to elevate, to make something sink, to make wet",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0092", 92, "मूलँ", "मूल्",
            "रोहणे", "बीजारोपण करना, बोना, कलम करना", "to crop, to root, to plant, to sow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0093", 93, "कलँ", "कल्",
            "क्षेपे", "उड़ाना, फेंकना", "to throw, to  make something fly",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0094", 94, "विलँ", "विल्",
            "क्षेपे", "उड़ाना, फेंकना, प्रेरणा करना", "to throw, to  make something fly",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0095", 95, "बिलँ", "बिल्",
            "भेदने", "छेद करना, चीरना", "to break,to split,to divide, to cut",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0096", 96, "तिलँ", "तिल्",
            "स्नेहने", "तेल लगाना, स्निग्ध होना, चिकना होना", "to oil, to become greasy, to anoint",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0097", 97, "चलँ", "चल्",
            "भृतौ", "पालना, बढ़ाना", "to foster,to bring up, to cherish",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0098", 98, "पालँ", "पाल्",
            "रक्षणे", "पालन करना, संरक्षण करना", "to protect,to govern",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0099", 99, "पलँ", "पल्",
            "रक्षणे", "", "to protect,to govern",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0100", 100, "लूषँ", "लूष्",
            "हिंसायाम्", "हिंसा करना, मार डालना", "to kill, to destroy",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0101", 101, "शुल्बँ", "शुल्ब्",
            "माने", "नापना, गिनना, तोलना", "to weigh, to measure",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0102", 102, "शूर्पँ", "शूर्प्",
            "माने", "नापना, तोलना", "to weigh, to measure",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0103", 103, "चुटँ", "चुट्",
            "छेदने", "कतरना, चोट मारना", "to cut, to strike",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0104", 104, "मुटँ", "मुट्",
            "सञ्चूर्णने", "चूर्ण करना, मर्दन करना", "to crush,to grind,to powder",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0105", 105, "उलडिँ", "उलण्ड्",
            "उत्क्षेपणे", "", "to throw up",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0106", 106, "पडिँ", "पण्ड्",
            "नाशने", "नष्ट करना", "to destroy",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0107", 107, "पसिँ", "पंस्",
            "नाशने", "नष्ट करना", "to destroy",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0108", 108, "मार्ग", "मार्ग",
            "संस्कारगत्योः", "संस्कार करना, पूर्ण करना, तैयार करना, सिद्ध करना, जाना, घूमना", "to do cultural rites, to cultivate, to complete, to prepare, to turn, to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0109", 109, "व्रजँ", "व्रज्",
            "संस्कारगत्योः", "संस्कार करना, पूर्ण करना, तैयार करना, सिद्ध करना, जाना, घूमना", "to do cultural rites, to cultivate, to complete, to prepare, to turn, to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0110", 110, "शुल्कँ", "शुल्क्",
            "अतिस्पर्शने", "कर लगाना, उत्पत्ति कर देना, उत्पन्न करना", "to tax, to pay tax, to produce goods",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0111", 111, "चपिँ", "चम्प्",
            "गत्याम्", "जाना", "to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0112", 112, "क्षपिँ", "क्षम्प्",
            "क्षान्त्याम्", "सहन करना, कहना, दया करना, चमकना", "to endure,to tolerate, to forgive, to shine",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0113", 113, "क्षजिँ", "क्षञ्ज्",
            "कृच्छ्रजीवने", "", "to live miserably, to live hard life",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0114", 114, "छजिँ", "छञ्ज्",
            "कृच्छ्रजीवने", "कठिन जीवन व्यतीत करना", "to live miserably, to live hard life",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0115", 115, "श्वर्तँ", "श्वर्त्",
            "गत्याम्", "जाना", "to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0116", 116, "स्वर्तँ", "स्वर्त्",
            "कृच्छ्रजीवने, गत्याम्", "", "to live miserably, to live hard life, to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0117", 117, "श्वभ्रँ", "श्वभ्र्",
            "गत्याम्", "जाना", "to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0118", 118, "ज्ञपँ", "ज्ञप्",
            "ज्ञाने ज्ञापने च", "जानना, समझना, सिखाना, समझाना, आनन्दित करना, प्रसन्न करना, मारना, ठोकना, तीक्ष्ण करना", "to know, to understand, to teach, to educate, to explain, to clarify, to make happy, to hit, to sharpen",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0119", 119, "यमँ", "यम्",
            "परिवेषणे", "स्वाधीन रखना, काबू में रखना, पोषण करना, खाने को देना", "to keep under control, to nourish, to nurture, to serve food",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0120", 120, "चहँ", "चह्",
            "परिकल्कने", "ठगना, दुष्कर्मी होना", "to cheat, to do misdeed",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0121", 121, "चपँ", "चप्",
            "परिकल्पने", "", "to cheat, to do misdeed",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0122", 122, "रहँ", "रह्",
            "त्यागे", "छोड़ना, त्याग करना", "to abandon,to quit, to leave",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0123", 123, "बलँ", "बल्",
            "प्राणने", "बलयुक्त होना या करना, स्पष्ट करना", "to have power, to gain power, to explain",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0124", 124, "चिञ्", "चि",
            "चयने", "ढूंढना, बटोरना, एकत्र करना", "to find, to gather, to collect",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.DVIKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0125", 125, "घट्टँ", "घट्ट्",
            "चलने", "जाना, स्थानांतरण करना", "to go, to change places",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0126", 126, "मुस्तँ", "मुस्त्",
            "सङ्घाते", "ढेर करना, बटोरना, एकत्र करना, राशि करना", "to collect, to pile, to make a heap",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0127", 127, "खट्टँ", "खट्ट्",
            "संवरणे", "आच्छादन करना, छिपाना, ढांकना", "to cover, to hide",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0128", 128, "षट्टँ", "सट्ट्",
            "हिंसायाम्", "मार डालना, दुःख देना, पीड़ा देना", "to kill, to destroy, to hurt, to irritate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0129", 129, "स्फिट्टँ", "स्फिट्ट्",
            "हिंसायाम्", "मार डालना, दुःख देना, पीड़ा देना", "to kill, to destroy",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0130", 130, "चुबिँ", "चुम्ब्",
            "हिंसायाम्", "मार डालना, दुःख देना, पीड़ा देना", "to kill, to destroy, to hurt, to irritate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0131", 131, "पुलँ", "पुल्",
            "सङ्घाते", "ढेर लगाना, बटोरना, संचित करना", "to be great, to be large, to rise up, to be heaped up",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0132", 132, "पूर्णँ", "पूर्ण्",
            "सङ्घाते", "", "to collect, to make a heap, to make a pile",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0133", 133, "पुणँ", "पुण्",
            "सङ्घाते", "", "to collect, to make a heap, to make a pile",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0134", 134, "पुन्सँ", "पुंस्",
            "अभिवर्धने", "बढ़ना, वृद्धि होना, बढ़ाना, वृद्धि करना", "to grow, to prosper",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0135", 135, "टकिँ", "टङ्क्",
            "बन्धने", "बांधना, जोड़ना, टांकना", "to bind,to build",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0136", 136, "व्यपँ", "व्यप्",
            "क्षये", "", "to throw",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0137", 137, "व्ययँ", "व्यय्",
            "क्षये", "", "to throw",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0138", 138, "पूलँ", "पूल्",
            "सङ्घाते", "ढेर लगाना, बटोरना, संचित करना", "to be great, to be large, to rise up, to be heaped up",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0139", 139, "धूसँ", "धूस्",
            "कान्तिकरणे", "शोभित होना, अलंकृत होना", "to be decorated",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0140", 140, "धूषँ", "धूष्",
            "कान्तिकरणे", "", "to be decorated",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0141", 141, "धूशँ", "धूश्",
            "कान्तिकरणे", "", "to be decorated",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0142", 142, "कीटँ", "कीट्",
            "वर्णे", "रंगना, रंग में डुबोना, बांधना, जंग लगाना", "to tinge,to color",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0143", 143, "चूर्णँ", "चूर्ण्",
            "सङ्कोचने", "सिकुड़ना", "to hestitate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0144", 144, "पूजँ", "पूज्",
            "पूजायाम्", "पूजा करना, अर्चना करना, सम्मान करना", "to worship, to pray, to honor, to respect",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0145", 145, "अर्कँ", "अर्क्",
            "स्तवने", "प्रशंसा करना, स्तुति करना, गरम करना", "to praise,to heat",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0146", 146, "शुठँ", "शुठ्",
            "आलस्ये", "अलसाना, आलस्य करना", "to be lazy",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0147", 147, "शुठिँ", "शुण्ठ्",
            "शोषणे", "सूखना, सूखाना, शोषण करना", "to dry,to become dry, to extract, to exploit",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0148", 148, "जुडँ", "जुड्",
            "प्रेरणे", "प्रेरणा करना", "to direct, to inspire",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0149", 149, "गजँ", "गज्",
            "शब्दार्थे", "शब्द करना", "to sound",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0150", 150, "मार्जँ", "मार्ज्",
            "शब्दार्थे", "शब्द करना", "to sound",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0151", 151, "मर्चँ", "मर्च्",
            "शब्दार्थे", "शब्द करना", "to sound",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0152", 152, "घृ", "घृ",
            "प्रस्रवणे", "बूँद बूँद गिरना, चूना, टपकना", "to sprinkle,to drip, to ooze",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0153", 153, "पचिँ", "पञ्च्",
            "विस्तारवचने", "फैलाना, पसारना, प्रपंच करना", "to spread, to expand",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0154", 154, "तिजँ", "तिज्",
            "निशाने", "तीक्ष्ण करना, पैना करना, धार लगाना (आ)- क्षमा करना, सहना", "to sharpen, to whet",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0155", 155, "कॄतँ", "कॄत्",
            "संशब्दने", "प्रसिद्ध करना, कीर्तित होना", "to make famous, to be famous",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0156", 156, "वर्धँ", "वर्ध्",
            "छेदनपूरणयोः", "काटना, चीरना, भरना, पूर्ण करना", "to cut,to fill, to complete",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0157", 157, "कुबिँ", "कुम्ब्",
            "आच्छादने", "आच्छादित करना, ढांकना", "to cover",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0158", 158, "कुभिँ", "कुम्भ्",
            "आच्छादने", "", "to cover",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0159", 159, "लुबिँ", "लुम्ब्",
            "अदर्शने", "नष्ट होना, गुप्त होना, अदृश्य होना", "to be invisible,to be hidden, to be destroyed",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0160", 160, "तुबिँ", "तुम्ब्",
            "अदर्शने", "नष्ट होना, गुप्त होना, अदृश्य होना", "to be invisible,to be hidden, to be destroyed",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0161", 161, "ह्लपँ", "ह्लप्",
            "व्यक्तायां वाचि", "स्पष्ट उच्चारण करना, बोलना", "to speak articulately, to talk clearly",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0162", 162, "क्लपँ", "क्लप्",
            "व्यक्तायां वाचि", "", "to speak articulately, to talk clearly",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0163", 163, "ह्रपँ", "ह्रप्",
            "व्यक्तायां वाचि", "", "to speak articulately, to talk clearly",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0164", 164, "चुटिँ", "चुण्ट्",
            "छेदने", "कतरना, तोड़ना, चुटिया मारना,नोचना", "to cut, to prick",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0165", 165, "ब्रीसँ", "ब्रीस्",
            "हिंसायाम्", "मार डालना, दुःख देना", "to kill, to destroy, to hurt",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0166", 166, "दहिँ", "दंह्",
            "रक्षणे मोक्षणे च", "संरक्षण करना, मुक्त करना", "to protect,to release",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0167", 167, "इलँ", "इल्",
            "प्रेरणे", "प्रेरणा करना, प्रोत्साहित करना", "to inspire, to motivate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0168", 168, "म्रक्षँ", "म्रक्ष्",
            "म्लेच्छने", "मिश्रित करना, अशुद्ध करना", "to mix, to make impure",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0169", 169, "अस्तँ", "अस्त्",
            "सङ्घाते", "ढेर करना, बटोरना, एकत्र करना, राशि करना", "to collect, to pile, to make a heap",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0170", 170, "म्लेछँ", "म्लेच्छ्",
            "अव्यक्तायां वाचि", "अस्पष्ट या अशुद्ध बोलना, असम्बद्ध संभाषण करना, म्लेच्छ भाषा बोलना, जंगली भाषा बोलना", "to speak inarticulately, to speak uncultured, to talk rude, to talk indescent",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0171", 171, "छपिँ", "छम्प्",
            "गत्याम्", "जाना", "to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0172", 172, "ब्रूसँ", "ब्रूस्",
            "हिंसायाम्", "मार डालना, दुःख देना", "to kill, to destroy, to hurt",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0173", 173, "बर्हँ", "बर्ह्",
            "हिंसायाम्", "मार डालना, दुःख देना", "to kill, to destroy, to hurt",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0174", 174, "श्रणुँ", "श्रण्",
            "दाने", "देना, दान करना", "to give",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0175", 175, "पिच्चँ", "पिच्च्",
            "कुट्टने", "कूटना, कतरना, चीरना, बहुत दुःख देना", "to cut, to split, to tamp, to slice, to torture",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0176", 176, "बुलँ", "बुल्",
            "निमज्जने", "डुबाना", "to immerse",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0177", 177, "गर्जँ", "गर्ज्",
            "शब्दे", "", "to roar,to growl,to thunder",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0178", 178, "गर्दँ", "गर्द्",
            "शब्दे", "", "to roar,to growl,to thunder",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0179", 179, "गर्धँ", "गर्ध्",
            "अभिकाङ्क्षायाम्", "", "to desire, to wish",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0180", 180, "गुर्दँ", "गूद्",
            "पूर्वनिकेतने", "रहना, वास करना, बसना, आमंत्रण करना", "to dwell, to reside, to stay, to invite",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0181", 181, "पूर्वँ", "पूर्व्",
            "निकेतने", "", "to dwell, to reside, to stay, to invite",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0182", 182, "जसिँ", "जंस्",
            "रक्षणे मोक्षणे च", "संरक्षण करना, मुक्त करना", "to protect,to release",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0183", 183, "ईडँ", "ईड्",
            "स्तुतौ", "स्तुति करना, प्रशंसा करना", "to praise",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0184", 184, "जसुँ", "जस्",
            "हिंसायाम्", "मार डालना, दुःख देना", "to kill, to hurt, to hit, to strike, to injure",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0185", 185, "पिडिँ", "पिण्ड्",
            "सङ्घाते", "ढेर करना, राशि करना", "to heap,to unite, to pile up",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0186", 186, "पर्थँ", "पर्थ्",
            "प्रक्षेपे", "फेंकना, उड़ाना, प्रेरणा करना, भेजना", "to throw,to send,to direct, to inspire",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0187", 187, "रुषँ", "रुष्",
            "रोषे", "क्रोध करना, गुस्सा करना", "to be angry",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0188", 188, "रुटँ", "रुट्",
            "रोषे", "", "to be angry",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0189", 189, "डिपँ", "डिप्",
            "क्षेपे", "फेंकना, मारना, एकत्र करना", "to throw, to hit, to collect",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0190", 190, "ष्टुपँ", "स्तुप्",
            "समुच्छ्राये", "ढेर लगना, राशि करना", "to heap,to unite, to pile up",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0191", 191, "ष्टूपँ", "स्तूप्",
            "समुच्छ्राये", "", "to heap,to unite, to pile up",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0192", 192, "चितँ", "चित्",
            "सञ्चेतने", "विचार करना, चिंतन करना, स्मरण करना, याद करना", "to think to perceive, to meditate, to remember, to memorize",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0193", 193, "दशिँ", "दंश्",
            "दंशने", "डसना, काटना, दंश मारना", "to bite,to sting",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0194", 194, "दसिँ", "दंस्",
            "दर्शनदंशनयोः", "देखना, काटना, डसना", "to see, to bite,to sting",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0195", 195, "दसँ", "दस्",
            "दर्शनदंशनयोः", "", "to see, to bite,to sting",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0196", 196, "डपँ", "डप्",
            "सङ्घाते", "एकत्र करना, बटोरना, राशि करना", "to collect, to bring together, to pile up, to heap",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0197", 197, "डिपँ", "डिप्",
            "सङ्घाते", "फेकना, एकत्र करना, बटोरना", "to collect, to bring together, to pile up, to heap",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0198", 198, "तत्रिँ", "तन्त्र्",
            "कुटुम्बधारणे", "फैलाना, कुटुंब पोषण करना, प्रधान होना", "to support the family, to govern, to lead",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0199", 199, "मत्रिँ", "मन्त्र्",
            "गुप्तपरिभाषणे", "गुप्त भाषण करना", "to consult, to advise, to discuss privately",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0200", 200, "स्पशँ", "स्पश्",
            "ग्रहणसंश्लेषणयोः", "लेना, संयोग करना, जोड़ना", "to take, to join, to unite",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0201", 201, "तर्जँ", "तर्ज्",
            "तर्जने", "धिक्कारना, निन्दा करना, डराना, धुडकाना", "to hate, to insult, to ignore, to blame, to criticize, to frighten",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0202", 202, "भर्त्सँ", "भर्त्स्",
            "तर्जने", "धिक्कारना, निन्दा करना, डराना, धुडकाना", "to hate, to insult, to ignore, to blame, to criticize, to frighten",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0203", 203, "बस्तँ", "बस्त्",
            "अर्दने", "दुःख देना, मार डालना, लजाना", "to kill, to destroy, to hit, to cause pain",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0204", 204, "गन्धँ", "गन्ध्",
            "अर्दने", "दुःख देना, मार डालना, लजाना", "to kill, to destroy, to hit, to cause pain",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0205", 205, "किलँ", "किल्",
            "क्षेपे", "उड़ाना, फेंकना, प्रेरणा करना", "to throw, to  make something fly",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0206", 206, "पिलँ", "पिल्",
            "क्षेपे", "उड़ाना, फेंकना, प्रेरणा करना", "to throw, to  make something fly",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0207", 207, "विष्कँ", "विष्क्",
            "हिंसायाम्", "मार डालना", "to kill, to destroy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0208", 208, "हिष्कँ", "हिष्क्",
            "हिंसायाम्", "", "to kill, to destroy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0209", 209, "निष्कँ", "निष्क्",
            "परिमाणे", "मापना, तोलना, गिनना", "to measure,to weigh",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0210", 210, "ललँ", "लल्",
            "ईप्सायाम्", "इच्छा करना, चाहना,  रमन करना, रति करना", "to desire,to seek,to play, to enjoy, to delight",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0211", 211, "कूणँ", "कूण्",
            "सङ्कोचे", "संकुचित होना, ऐंठना", "to constrict, to contract",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0212", 212, "तूणँ", "तूण्",
            "पूरणे", "भरना, पूर्ण करना", "to complete, to fill, to fulfill",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0213", 213, "भ्रूणँ", "भ्रूण्",
            "आशाविशङ्कयोः", "आशा करना, भरोसा करना, शंका करना, गर्भ धारण करना", "to hope, to believe, to doubt, to question, to become pregnant",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0214", 214, "शठँ", "शठ्",
            "श्लाघायाम्", "प्रशंसा करना, स्तुति करना", "to praise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0215", 215, "यक्षँ", "यक्ष्",
            "पूजायाम्", "आराधना करना, पूजा करना, सत्कार करना", "to pray, to worship, to respect",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0216", 216, "स्यमँ", "स्यम्",
            "वितर्के", "चिंतन करना, मनन करना, तर्क वितर्क करना, विचार करना", "to consider,to think,to sound,to shout",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0217", 217, "गूरँ", "गूर्",
            "उद्यमने", "प्रयत्न करना, उद्योग करना", "to make an effort, to try",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0218", 218, "शमँ", "शम्",
            "आलोचने", "प्रसिद्ध करना, स्पष्टता से दिखाना, जाहिर करना", "to publish, to advertise, to explain, to clarify",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0219", 219, "लक्षँ", "लक्ष्",
            "आलोचने", "आलोचना करना", "to criticize",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0220", 220, "कुत्सँ", "कुत्स्",
            "अवक्षेपणे निन्दने च", "दोष लगाना, निन्दा करना, तिरस्कार करना", "to insult, to criticize, to blame, to hate",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0221", 221, "त्रुटँ", "त्रुट्",
            "छेदने", "कतरना, तोड़ना, टूटना", "to cut,to break",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0222", 222, "कुटँ", "कुट्",
            "छेदने", "", "to cut,to break",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0223", 223, "गलँ", "गल्",
            "स्रवणे", "टपकना", "to drip, to flow, to ooze out",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0224", 224, "भलँ", "भल्",
            "आभण्डने", "निरूपण करना, वाद विवाद करना", "to analyze, to argue",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0225", 225, "कूटँ", "कूट्",
            "आप्रदाने अवसादने च", "नहीं देना, छल करना, अस्पष्ट गूढ़ या मालूम न हो ऐसा करना, राशि करना", "to avoid giving, to cheat, to do indistinctly, to collect",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0226", 226, "कुट्टँ", "कुट्ट्",
            "प्रतापने", "गरम करना", "to heat",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0227", 227, "वन्चुँ", "वञ्च्",
            "प्रलम्भने", "ठगना, फंसाना, प्रतारणा करना", "to cheat",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0228", 228, "वृषँ", "वृष्",
            "शक्तिबन्धने", "गर्भवती होना, अमानवी पराक्रम करना, पराक्रमी होना, प्रजोत्पत्ति करने का सामर्थ्य होना", "to be impregnated, to show great courage, to be capable of bearing offsprings",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0229", 229, "मदँ", "मद्",
            "तृप्तियोगे", "तृप्त करना", "to please, satisfy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0230", 230, "दिवुँ", "दिव्",
            "परिकूजने", "दुःख देना, शोक करना", "to irritate, to cause pain, to mourn to grieve",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0231", 231, "गृ", "गृ",
            "विज्ञाने", "समझना, जानना", "to know, to understand",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0232", 232, "विदँ", "विद्",
            "चेतनाख्याननिवासेषु", "शरीर की सुध रखना, समझना, जानना", "to improve body, to know, to understand",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0233", 233, "मानँ", "मान्",
            "स्तम्भे", "गर्व करना, स्थिर करना, गर्वीला होना", "to boast, to be egoistic, to be proud, to stabilize",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0234", 234, "मनँ", "मन्",
            "स्तम्भे", "", "to boast, to be egoistic, to be proud, to stabilize",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0235", 235, "यु", "यु",
            "जुगुप्सायाम्", "अपमान करना, दोष लगाना, निन्दा करना", "to insult, to blame, to criticise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0236", 236, "कुस्मँ", "कुस्म्",
            "कुत्सितस्मये", "अयोग्य रीती से हसना", "to smile inappropriately, to smile with an insult",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0237", 237, "चर्चँ", "चर्च्",
            "अध्ययने", "पढ़ना, अध्ययन करना", "to learn, to study",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0238", 238, "बुक्कँ", "बुक्क्",
            "भषणे", "भौंकना, कुत्ते के समान शब्द करना", "to bark",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0239", 239, "शब्दँ", "शब्द्",
            "आविष्कारे, भषणे", "शब्द करना, भाषण करना, प्रकट करना", "to utter, to sound, to explain",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0240", 240, "कणँ", "कण्",
            "निमीलने", "आंखे मूंदना", "to wink",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0241", 241, "जभिँ", "जम्भ्",
            "नाशने", "नष्ट करना", "to kill, to destroy",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0242", 242, "षूदँ", "सूद्",
            "क्षरणे आस्रवणे आप्रवणे घाते च", "टपकना, झरना, घाव करना, मार डालना, मारने का यत्न करना", "to drip, to ooze, to kill, to try to kill, to wound",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0243", 243, "जसुँ", "जस्",
            "ताडने", "ताड़न करना, उपेक्षा करना, मारना", "to kill, to strike, to hit, to ignore",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0244", 244, "पशँ", "पश्",
            "बन्धने", "बांधना, बेडी डालना, फांस लगाना", "to lockup, to bind, to hang till death",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0245", 245, "अमँ", "अम्",
            "रोगे", "बीमार होना, रोग ग्रस्त होना, अजीर्ण रोग युक्त होना", "to be ill,to be sick, to have stomach ache",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0246", 246, "चटँ", "चट्",
            "भेदने", "मार डालना, तोडना", "to kill, to injure, to break",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0247", 247, "स्फुटँ", "स्फुट्",
            "भेदने", "कतरना, छेदना, तोडना, चीरना, विकसित करना या दुःख देना", "to cut, to split, to crack, to open, to trouble",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0248", 248, "घटँ", "घट्",
            "सङ्घाते", "घोटना, हिलाना, बटोरना", "to crush, to chop, to gather",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0249", 249, "दिवुँ", "दिव्",
            "मर्दने", "मर्दन करना", "to rub, to massage",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0250", 250, "अर्जँ", "अर्ज्",
            "प्रतियत्ने", "अर्जन करना, उद्योग करना, तैयार करना", "to earn, to do business, to prepare",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0251", 251, "घुषिँर्", "घुष्",
            "विशब्दने", "मन में विचार कर कहना, घोषित करना, तरह तरह के शब्द करना", "to announce, to speak loudly",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0252", 252, "क्रन्दँ", "क्रन्द्",
            "सातत्ये", "बुलाना, पुकारना", "to call out",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0253", 253, "लसँ", "लस्",
            "शिल्पयोगे", "चतुर होना, कुशल होना, कला- कौशल जानना", "to be skilled, to be expert, to know art",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0254", 254, "तसिँ", "तंस्",
            "अलङ्करणे", "सजाना, अलंकृत करना", "to adorn, to decorate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0255", 255, "भूषँ", "भूष्",
            "अलङ्करणे", "सजाना, अलंकृत करना", "to adorn, to decorate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0256", 256, "मोक्षँ", "मोक्ष्",
            "मोचने", "", "to leave, to abandon",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0257", 257, "अर्हँ", "अर्ह्",
            "पूजायाम्", "पूजा करना, सत्कार करना, पूजनीय होना, पूजा योग्य होना, योग्य होना", "to be worthty, to be eligible, to have merit",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0258", 258, "ज्ञा", "ज्ञा",
            "नियोगे", "आज्ञा करना", "to order",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0259", 259, "भजँ", "भज्",
            "विश्राणने", "देना, दान करना, पकाना, सिद्ध करना, अन्नादि तैयार करना, अलग करना", "to give, to donate, to cook, to make readm to prepare food, to segregate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0260", 260, "शृधुँ", "शृध्",
            "प्रहसने प्रसहने च", "सहन करना, सहना, अनादर करना, अपमान करना, पराभव करना", "to tolerate, to insert, to disrespect, to defeat",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0261", 261, "यतँ", "यत्",
            "निकारोपस्कारयोः", "दुःख देना, मारना, कष्ट देना, मना करना, रोकना", "to cause pain, to kill, to irritate, to disallow,  to stop",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0262", 262, "रकँ", "रक्",
            "आस्वादने", "स्वाद लेना, चखना", "to taste",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0263", 263, "लगँ", "लग्",
            "आस्वादने", "स्वाद लेना, चखना", "to taste",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0264", 264, "रघँ", "रघ्",
            "आस्वादने", "", "to taste",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0265", 265, "रगँ", "रग्",
            "आस्वादने", "", "to taste",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0266", 266, "अन्चुँ", "अञ्च्",
            "विशेषणे", "विशेषित करना, सम्मानित करना, हटाना, पृथक करना", "to characterise, to honor, to remove, to separate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0267", 267, "लिगिँ", "लिङ्ग्",
            "चित्रीकरणे", "विभिन्न रंगों से रंग देना", "to paint in various colors",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0268", 268, "मुदँ", "मुद्",
            "संसर्गे", "मिश्रित करना, एकत्र करना", "to mix, to unite",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0269", 269, "त्रसँ", "त्रस्",
            "धारणे ग्रहणे वारणे च", "पकड़ना, हरण करना, जबरन लेना, मना करना, डराना", "to catch, to steal, to take forcefully, to disobey, to frighten",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0270", 270, "उँध्रसँ", "ध्रस्",
            "उञ्छे", "बीनना, एक एक करके चुनना", "to pick, to glean",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0271", 271, "उध्रसँ", "उध्रस्",
            "उञ्छे", "", "to pick, to glean",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0272", 272, "मुचँ", "मुच्",
            "प्रमोचने मोदने च", "छोड़ना, द्रव्यादि देना", "to free,to liberate,to release,to loosen,to abandon,to grant,to give money",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0273", 273, "वसँ", "वस्",
            "स्नेहच्छेदापहरणेषु", "स्नेह करना, कतरना, छेदा करना, नष्ट करना, अपहरण करना, मोह करना", "to love, to cut, to destroy, to kidnap, to desire",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0274", 274, "चरँ", "चर्",
            "संशये", "संदेह करना, संशय करना", "to doubt",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0275", 275, "च्यु", "च्यु",
            "सहने हसने च", "सहना, सहन करना, हँसना", "to endure, to tolerate, to laugh",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0276", 276, "च्युसँ", "च्युस्",
            "सहने हसने च", "", "to endure, to tolerate, to laugh",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0277", 277, "भू", "भू",
            "अवकल्कने", "मिलाना, मिश्रित करना, चिंतन करना", "to mix, to think",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0278", 278, "कृपँ", "कृप्",
            "अवकल्कने", "कल्पना करना, विचार करना, मिश्रित करना, चित्रित करना, रंगना", "to mix, to think, to imagine, to paint",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0279", 279, "ग्रसँ", "ग्रस्",
            "ग्रहणे", "ग्रहण करना, पकड़ लेना, हरण करना", "to seize, to catch, to steal",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0280", 280, "पुषँ", "पुष्",
            "धारणे", "धारण करना, पालन करना", "to wear, to obey",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0281", 281, "दलँ", "दल्",
            "विदारणे", "चीरना, फाड़ना, टुकड़े करना", "to break, to cut, to slice, to do pieces",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0282", 282, "पटँ", "पट्",
            "भाषायाम्", "चमकना, बोलना", "to speak, to talk, to shine, to glow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0283", 283, "पुटँ", "पुट्",
            "भाषायाम्", "चमकना, बोलना, प्रकाशित होना", "to speak, to talk, to shine, to glow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0284", 284, "लुटँ", "लुट्",
            "भाषायाम्", "चमकना, भाषण देना", "to speak, to talk, to shine, to glow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0285", 285, "तुजिँ", "तुञ्ज्",
            "भाषायाम्", "चमकना, भाषण देना", "to speak, to talk, to shine, to glow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0286", 286, "मिजिँ", "मिञ्ज्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0287", 287, "पिजिँ", "पिञ्ज्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0288", 288, "लकँ", "लक्",
            "आस्वादने", "स्वाद लेना, चखना", "to taste",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0289", 289, "लुजिँ", "लुञ्ज्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0290", 290, "भजिँ", "भञ्ज्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0291", 291, "लघिँ", "लङ्घ्",
            "भाषायाम्", "चमकना, आगे बढ़ना, लांघना, बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0292", 292, "त्रसिँ", "त्रंस्",
            "भाषायाम्", "बोलना, कहना, चमकना", "to speak, to talk, to shine, to glow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0293", 293, "पिसिँ", "पिंस्",
            "भाषायाम्", "चमकना,कहना", "to speak, to talk, to shine, to glow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0294", 294, "कुसिँ", "कुंस्",
            "भाषायाम्", "चमकना,कहना", "to speak, to talk, to shine, to glow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0295", 295, "दशिँ", "दंश्",
            "भाषायाम्", "कहना, डंक मारने के समान बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0296", 296, "कुशिँ", "कुंश्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0297", 297, "घटँ", "घट्",
            "भाषायाम्", "चमकना, प्रकाशित होना", "to speak, to talk, to shine, to glow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0298", 298, "घटिँ", "घण्ट्",
            "भाषायाम्", "चमकना, प्रकाशित होना, शब्द करना, बोलना", "to speak, to talk, to shine, to glow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0299", 299, "बृहिँ", "बृंह्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0300", 300, "बर्हँ", "बर्ह्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0301", 301, "बल्हँ", "बल्ह्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0302", 302, "गुपँ", "गुप्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0303", 303, "धूपँ", "धूप्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0304", 304, "विछँ", "विच्छ्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0305", 305, "चीवँ", "चीव्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0306", 306, "पुथँ", "पुथ्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0307", 307, "लोकृँ", "लोक्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0308", 308, "लोचृँ", "लोच्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0309", 309, "णदँ", "नद्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0310", 310, "कुपँ", "कुप्",
            "भाषायाम्", "बोलना, क्रोध करना", "to speak, to talk, to be angry",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0311", 311, "तर्कँ", "तर्क्",
            "भाषायाम्", "बोलना, कहना, प्रकाशित होना, चमकना, तर्क करना, कल्पना करना, वाद करना, शंका करना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0312", 312, "वृतुँ", "वृत्",
            "भाषायाम्", "", "to speak, to talk, to shine, to glow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0313", 313, "वृधुँ", "वृध्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0314", 314, "रुटँ", "रुट्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0315", 315, "लजिँ", "लञ्ज्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0316", 316, "अजिँ", "अञ्ज्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0317", 317, "दसिँ", "दंस्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0318", 318, "भृशिँ", "भृंश्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0319", 319, "रुशिँ", "रुंश्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0320", 320, "शीकँ", "शीक्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0321", 321, "रुसिँ", "रुंस्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0322", 322, "नटिँ", "नण्ट्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0323", 323, "पुटिँ", "पुण्ट्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0324", 324, "जि", "जि",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0325", 325, "चि", "चि",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0326", 326, "रधिँ", "रन्ध्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0327", 327, "लघिँ", "लङ्घ्",
            "भाषायाम्", "चमकना, आगे बढ़ जाना, बोलना", "to shine, to glow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0328", 328, "अहिँ", "अंह्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0329", 329, "रहिँ", "रंह्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0330", 330, "महिँ", "मंह्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0331", 331, "लडिँ", "लण्ड्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0332", 332, "तडँ", "तड्",
            "भाषायाम्", "चमकना, धमकाना", "to speak, to talk, to shine, to glow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0333", 333, "नलँ", "नल्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0334", 334, "पूरीँ", "पूर्",
            "आप्यायने", "तृप्त करना, पूर्ण करना, पूर्ण होना, भरना", "to satisfy, to complete, to fullfull, to fill",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0335", 335, "रुजँ", "रुज्",
            "हिंसायाम्", "मारना, हिंसा करना, दुःख देना", "to kill, to destroy, to cause pain",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0336", 336, "ष्वदँ", "स्वद्",
            "आस्वादने", "स्वाद लेना, चखना", "to taste",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0337", 337, "स्वादँ", "स्वाद्",
            "आस्वादने", "", "to taste",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0338", 338, "युजँ", "युज्",
            "संयमने", "संयत करना, बांधना, वश में रखना", "to restrain, to control, to bind",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0339", 339, "पृचँ", "पृच्",
            "संयमने", "स्पर्श करना, छूना, अटकाना, हरकत करना, संयमन करना", "to touch, to hold, to control, to restrain",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0340", 340, "अर्चँ", "अर्च्",
            "पूजायाम्", "पूजा करना, अर्चना करना", "to pray, to worship",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0341", 341, "षहँ", "सह्",
            "मर्षणे", "सहना, सहन करना, शक्तिमान होना, संतुष्ट होना", "to bear, to tolerate, to be powerful, to be satisfied",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0342", 342, "ईरँ", "ईर्",
            "क्षेपे", "फेंकना", "to throw",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0343", 343, "ली", "ली",
            "द्रवीकरणे", "पतला करना, गलाना", "to melt, to make thin",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0344", 344, "वृजीँ", "वृज्",
            "वर्जने", "छोड़ना, वर्जित करना", "to leave, to abandon",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0345", 345, "वृञ्", "वृ",
            "आवरणे", "आच्छादित करना, ढंकना", "to cover",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0346", 346, "जॄ", "जॄ",
            "वयोहानौ", "वृद्ध होना, जीर्ण होना", "to grow old",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0347", 347, "ज्रि", "ज्रि",
            "वयोहानौ", "वृद्ध होना, जीर्ण होना", "to grow old",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0348", 348, "रिचँ", "रिच्",
            "वियोजनसम्पर्चनयोः", "एकत्र करना, जोड़ना, बांधना, अलग अलग करना, फैलाना, दस्त करना, पेट साफ़ करना, रेचक दवा देना", "to mix, to join, to tie, to separate, to spread, to take laxative",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0349", 349, "शिषँ", "शिष्",
            "असर्वोपयोगे", "शेष रखना, बचा रखना, पूरा खर्च न करना", "to retain, to save",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0350", 350, "तपँ", "तप्",
            "दाहे", "तप्त होना, जलना, जलाना, तप्त करना, मन या शरीर में जलना", "to become hot, to glow, to burn, to be jealous",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0351", 351, "तृपँ", "तृप्",
            "तृप्तौ सन्दीपने प्रीणने च", "तृप्त होना, प्रसन्न होना, तृप्त करना, प्रसन्न करना", "to be satisfied, to be happy to satisfy, to make happy",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0352", 352, "छृदीँ", "छृद्",
            "सन्दीपने", "जलाना, प्रज्वलित करना", "to ignite, to set on fire",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0353", 353, "चृपँ", "चृप्",
            "सन्दीपने", "", "to ignite, to set on fire",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0354", 354, "छृपँ", "छृप्",
            "सन्दीपने", "", "to ignite, to set on fire",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0355", 355, "तृपँ", "तृप्",
            "दीपने सन्दीपने", "", "to glow, to shine",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0356", 356, "दृपँ", "दृप्",
            "सन्दीपने", "", "to ignite, to set on fire",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0357", 357, "दृभीँ", "दृभ्",
            "भये", "सम्बन्ध लगाना, सन्दर्भ लगाना, डरना", "to relate, to give reference, to be afraid",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0358", 358, "दृभँ", "दृभ्",
            "सन्दर्भे", "सन्दर्भ लगाना", "to relate, to give reference",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0359", 359, "लटँ", "लट्",
            "भाषायाम्", "चमकना, भाषण देना", "to speak, to talk, to shine, to glow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0360", 360, "श्रथँ", "श्रथ्",
            "मोक्षणे हिंसायां च", "मुक्त करना, छोड़ना, मारना, पीड़ा देना", "to set free, to  leave, to kill, to destroy, to hurt",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0361", 361, "मी", "मी",
            "गतौ", "जाना", "to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0362", 362, "ग्रन्थँ", "ग्रन्थ्",
            "बन्धने", "बांधना, गांठ लगाना", "to tie, to bind",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0363", 363, "शीकँ", "शीक्",
            "आमर्षणे", "छूना, स्पर्श करना, शांत होना, सहना", "to touch, to be quiet, to tolerate, to suffer",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0364", 364, "चीकँ", "चीक्",
            "आमर्षणे", "सहन करना, सहना, उतावला होना, असहिष्णु होना", "to tolerate, to suffer, to be eager, to be hasty, to be intolerant",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0365", 365, "अर्दँ", "अर्द्",
            "हिंसायाम्", "मारना, वध करना, दुःख देना, सताना", "to kill, to detroy, to irritate, to cause pain",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0366", 366, "हिसिँ", "हिंस्",
            "हिंसायाम्", "हिंसा करना, मारना", "to kill, to destroy, to irritate, to cause pain",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0367", 367, "अर्हँ", "अर्ह्",
            "पूजायाम्", "पूजा करना, अर्चना करना", "to be worthty, to be eligible, to have merit",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0368", 368, "षदँ", "सद्",
            "पद्यर्थे", "चढ़ाई करना, जाना", "to attack, to climb, to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0369", 369, "शुन्धँ", "शुन्ध्",
            "शौचकर्मणि", "शुद्ध होना, शुद्ध करना", "to cleanse, to purify",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0370", 370, "छदँ", "छद्",
            "अपवारणे", "हटाना, छिपाना, आच्छादित करना", "to remove, to get rid of, to hide, to keep out of sight",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0371", 371, "जुषँ", "जुष्",
            "परितर्कणे परितर्पणे च", "विचार करना, चाहना, पीड़ा करना, मार डालना, तर्क करना, सुख देना, संतुष्ट होना", "to think, to wish, to kill, to destroy, to guess, to make happy, to satisfy, to be satisfied",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0372", 372, "धूञ्", "धू",
            "कम्पने", "कपाना, कम्पित करना, हिलाना, शोभित करना", "to shake, to move, to decorate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0373", 373, "प्रीञ्", "प्री",
            "तर्पणे कान्तौ च", "प्रीती करना, तृप्त करना, कामना करना, कांतिमान होना", "to love, to satisfy, to desire, to wish, to be radiant",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0374", 374, "श्रन्थँ", "श्रन्थ्",
            "सन्दर्भे", "रचना करना, क्रम से रखना, गूंथना, गुल्फित करना", "to compose, to order, to intertwine",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0375", 375, "ग्रन्थँ", "ग्रन्थ्",
            "सन्दर्भे", "ग्रन्थ लिखना, सन्दर्भ लगाना", "to refer, to reference",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0376", 376, "आपॢँ", "आप्",
            "लम्भने", "प्राप्त होना, पाना", "to obtain, to get, to receive",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0377", 377, "तनुँ", "तन्",
            "श्रद्धोपकरणयोः", "श्रद्धा करना, आश्रय देना, सहायता करना, शब्द करना, मार डालना, पीड़ा करना", "to believe, to have faith, to provide shelter, to help, to sound, to kill, to destroy, to hurt",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0378", 378, "चनँ", "चन्",
            "श्रद्धोपहननयोः", "", "to believe, to have faith, to provide shelter, to help, to sound, to kill, to destroy, to hurt",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0379", 379, "वदँ", "वद्",
            "सन्देशवचने", "कहना, बोलना, सन्देश वचन कहना, भाषण करना", "to inform,to communicate,to speak,to tell,to describe,to utter, to read",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0380", 380, "वचँ", "वच्",
            "परिभाषणे", "बोलना, कहना, समझाना, पढ़ना", "to inform,to communicate,to speak,to tell,to describe,to utter, to read",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0381", 381, "मानँ", "मान्",
            "पूजायाम्", "सत्कार करना, मानना", "to respect, to believe",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0382", 382, "भू", "भू",
            "प्राप्तौ", "प्राप्त करना, मिल जाना, चिंतन करना", "to obtain, to receive, to get, to think",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0383", 383, "गर्हँ", "गर्ह्",
            "विनिन्दने", "दोष लगाना, निन्दा करना", "to blame, to criticize",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0384", 384, "मार्गँ", "मार्ग्",
            "अन्वेषणे", "अन्वेषण करना, ढूंढना, स्वच्छ करना, शुद्ध करना", "to investigate, to search, to cleanse, to purify",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0385", 385, "कठिँ", "कण्ठ्",
            "शोके", "उत्कंठित होना, शोक करना", "to be excited, to mourn, to grieve",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0386", 386, "मृजूँ", "मृज्",
            "शौचालङ्कारयोः", "स्वच्छ करना, धोना, पवित्र होना, अलंकृत करना", "to cleanse, to purify, to  to decorate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0387", 387, "मृषँ", "मृष्",
            "तितिक्षायाम्", "सहन करना", "to tolerate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0388", 388, "धृषँ", "धृष्",
            "प्रसहने", "जीतना, पराभव करना, अधीर होना, घबरा जाना", "to conquer, to win, to be eager, to be afraid",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0389", 389, "कथ", "कथ",
            "वाक्यप्रबन्धे", "कहना, व्याख्यान करना, बयान करना", "to speak, to tell, to state, to explain, to narrate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0390", 390, "वर", "वर",
            "ईप्सायाम्", "इच्छा करना, चाहना, आशा करना", "to wish, to desire, to hope",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0391", 391, "गण", "गण",
            "सङ्ख्याने", "गिनना", "to count, to enumerate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0392", 392, "शठ", "शठ",
            "सम्यगवभाषणे", "दुर्भाषण करना, दुर्वचन कहना, मौन धारण करना, चुप रहना", "to ill-speak, to abuse, to tell lies, to keep quiet",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0393", 393, "श्वठ", "श्वठ",
            "सम्यगवभाषणे", "दुर्भाषण देना, दुर्वचन कहना, गाली बकना", "to ill-speak, to abuse, to tell lies, to keep quiet",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0394", 394, "पठ", "पठ",
            "ग्रन्थे वेष्टने च", "गूंथना, लपेटना, हिस्से करना", "to clothe, to knead, to cover, to make parts",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0395", 395, "वठ", "वठ",
            "ग्रन्थे", "गूंथना, लपेटना", "to tie, to join",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0396", 396, "रह", "रह",
            "त्यागे", "त्यागना, छोड़ना", "to leave, to abandon",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0397", 397, "रन्ह्", "रंह्",
            "गतौ", "", "to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0398", 398, "स्तन", "स्तन",
            "देवशब्दे", "मेघ की गर्जना होना", "to have thunderstorm, to have cloud collision",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0399", 399, "गद", "गद",
            "देवशब्दे", "मेघ की गर्जना होना", "to have thunderstorm, to have cloud collision",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0400", 400, "पत", "पत",
            "गतौ", "जाना, नीचे गिरना, उतरना", "to fall down, to go down, to descend, to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0401", 401, "पष", "पष",
            "गतौ", "जाना", "to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0402", 402, "स्वर", "स्वर",
            "आक्षेपे", "शब्द करना, आवाज करना, दोष लगाना, निन्दा करना, आक्षेप करना", "to sound, to blame, to insult, to object",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0403", 403, "रच", "रच",
            "प्रतियत्ने", "रचना करना, शिल्पकार्य करना, ग्रन्थ बनाना", "to create, to craft, to compose",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0404", 404, "कल", "कल",
            "गतौ सङ्ख्याने च", "जाना, गिनना", "to go, to count, to enumerate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0405", 405, "चह", "चह",
            "परिकल्कने", "पीसना, कूटना", "to grind",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0406", 406, "मह", "मह",
            "पूजायाम्", "सम्मान करना, पूजा करना", "to worship, to pray, to honor, to respect",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0407", 407, "सार", "सार",
            "दौर्बल्ये", "दुर्बल होना", "to be weak",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0408", 408, "कृप", "कृप",
            "दौर्बल्ये", "दुर्बल होना", "to be weak",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0409", 409, "श्रथ", "श्रथ",
            "दौर्बल्ये", "दुर्बल होना", "to be weak",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0410", 410, "स्पृह", "स्पृह",
            "ईप्सायाम्", "इच्छा करना, चाहना", "to desire, to wish",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0411", 411, "भाम", "भाम",
            "क्रोधे", "क्रोध करना, धुडकना", "to reject, to be angry",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0412", 412, "सूच", "सूच",
            "पैशुन्ये", "उपकार की इच्छा से कहना, सूचना करना, बात कहना, दूसरे की न्यूनता दिखाना", "to indicate, to do favor, to explain, to point mistakes",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0413", 413, "खेट", "खेट",
            "भक्षणे", "खाना, भक्षण करना", "to eat",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0414", 414, "खेडँ", "खेड्",
            "भक्षणे", "", "to eat",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0415", 415, "खोट", "खोट",
            "भक्षणे", "", "to eat",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0416", 416, "क्षोट", "क्षोट",
            "क्षेपे", "भेजना, फेंकना", "to project, to throw",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0417", 417, "गोम", "गोम",
            "उपलेपने", "लेपना, पोतना", "to coat, to put a layer",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0418", 418, "कुमार", "कुमार",
            "क्रीडायाम्", "बालक के समान खेलना, क्रीड़ा करना", "to play like a child",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0419", 419, "शील", "शील",
            "उपधारणे", "धारण करना, पहचानना", "to wear, to recognize",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0420", 420, "साम", "साम",
            "सान्त्वप्रयोगे", "सांत्वना देना, समाधान करना, शांत करना", "to consolidate, to make happy, to pacify",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0421", 421, "वेल", "वेल",
            "कालोपदेशे", "काल गणना करना, समय की गिनती करना, उपदेश करना, समय पर समझाना", "to measure time, to preach, to explain on time",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0422", 422, "काल", "काल",
            "कालोपदेशे", "", "to measure time, to preach, to explain on time",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0423", 423, "पल्पूल", "पल्पूल",
            "लवनवपनयोः", "काटना, कतरना, शुद्ध करना, स्वच्छ करना, गिराना", "to cut, to purify, to cleanse, to drop",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0424", 424, "वात", "वात",
            "सुखसेवनयोः", "सुखी होना, आनन्द करना, सेवा करना, प्रेम करना, जाना", "to be happy, to rejoice, to serve, to love to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0425", 425, "गवेष", "गवेष",
            "मार्गणे", "ढूंढना, पता लगाना", "to search, to investigate, to find",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0426", 426, "वास", "वास",
            "उपसेवायाम्", "वासित करना, सुगन्धित करना, धुप देना", "to make someone stay, to make something fragrant, to burn incense sticks",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0427", 427, "निवास", "निवास",
            "आच्छादने", "आच्छादित करना, लपेटना, ठहरना", "to cover, to surround",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0428", 428, "भाज", "भाज",
            "पृथक्कर्मणि", "टुकड़े टुकड़े करना", "to cut into pieces",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0429", 429, "सभाज", "सभाज",
            "प्रीतिदर्शनयोः प्रीतिसेवनयोः च", "प्रीती करना, स्नेह करना, सेवा करना, देखना", "to love, to show affection, to serve, to see",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0430", 430, "ऊन", "ऊन",
            "परिहाणे", "कम करना, घटाना, संक्षेप करना", "to reduce, to shorten",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0431", 431, "ध्वन", "ध्वन",
            "शब्दे", "शब्द करना, आवाज करना", "to sound",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0432", 432, "कूट", "कूट",
            "परितापे परिदाहे च", "दुःख देना, जलाना, दग्ध करना", "to irritate, to cause pain, to set on fire, to make something burn",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0433", 433, "सन्केत", "सङ्केत",
            "आमन्त्रणे", "आमंत्रण करना, बुलाना, संकेत करना", "to invite, to call, to sign",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0434", 434, "ग्राम", "ग्राम",
            "आमन्त्रणे", "बुलाना, बुद्धिपूर्वक कहना", "to invite, to call, to sign, to speak intelligently",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0435", 435, "कुण", "कुण",
            "आमन्त्रणे", "बुलाना, ज्ञानपूर्वक कहना", "to invite, to call, to sign,  to speak intelligently",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0436", 436, "गुण", "गुण",
            "आमन्त्रणे", "बुलाना, बुद्धिपूर्वक कहना", "to invite, to call, to sign,  to speak intelligently",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0437", 437, "केत", "केत",
            "श्रावणे आमन्त्रणे च", "बुलाना, आमंत्रित करना, श्रवण करना", "to call, to invite, to hear",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0438", 438, "कूणँ", "कूण्",
            "सङ्कोचने", "संकुचित होना", "to contract, to shrink",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0439", 439, "स्तेन", "स्तेन",
            "चौर्ये", "चुराना", "to steal",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0440", 440, "पद", "पद",
            "गतौ", "जाना, स्थानांतर करना", "to go, to change places",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0441", 441, "गृह", "गृह",
            "ग्रहणे", "लेना, स्वीकारना", "to take, to accept",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0442", 442, "मृग", "मृग",
            "अन्वेषणे", "अन्वेषण करना, शिकार करना, ढूंढना", "to investigate, to search, to prey, to kill an animal",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0443", 443, "कुह", "कुह",
            "विस्मापने", "आश्चर्य या चमत्कार दिखाना, मोहित करना", "to show miracle",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0444", 444, "शूर", "शूर",
            "विक्रान्तौ", "पराक्रमी होना, शूरवीर होना, बहादुरी दिखाना", "to be powerful, to be courageous, to be brave, to have victory",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0445", 445, "वीर", "वीर",
            "विक्रान्तौ", "पराक्रमी होना, शूरवीर होना, पराक्रम करना", "to be powerful, to be courageous, to be brave, to have victory",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0446", 446, "स्थूल", "स्थूल",
            "परिबृहणे", "मोटा होना, स्थूल होना, शरीर पुष्ट होना", "to become fat, to become obese, to gain weight, to be healthy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0447", 447, "अर्थ", "अर्थ",
            "उपयाच्ञायाम्", "मांगना, याचना करना", "to beg, to request",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0448", 448, "सत्र", "सत्र",
            "सन्तानक्रियायाम्", "फैलाना, विस्तार करना, सम्बन्ध करना, संसर्गी होना", "to spread, to extend, to contact, to be connected",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0449", 449, "गर्व", "गर्व",
            "माने", "अभिमान करना", "to boast, to be egoistic, to be proud",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0450", 450, "सूत्र", "सूत्र",
            "वेष्टने विमोचने ग्रन्थने च", "सूत से लपेटना, रस्सी बांधना", "to put in thread, to cover with thread",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0451", 451, "मूत्र", "मूत्र",
            "प्रस्रवणे", "मूतना, पेशाब करना", "to urinate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0452", 452, "रूक्ष", "रूक्ष",
            "पारुष्ये", "कठिन होना, रुक्ष होना, कठोर वचन बोलना, नीरस होना, शुष्क होना", "to become hard, to become dry, to speak harshly, to be dull",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0453", 453, "पार", "पार",
            "कर्मसमाप्तौ", "कार्य पूर्ण करना", "to end, to complete",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0454", 454, "तीर", "तीर",
            "कर्मसमाप्तौ", "कार्य पूर्ण करना, पार करना", "to end, to complete",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0455", 455, "पुट", "पुट",
            "संसर्गे", "संसर्ग करना, आलिंगन करना, एक में एक अटकाना", "to come in contact, to embrace, to hug, to join, to lock in",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0456", 456, "कत्र", "कत्र",
            "शैथिल्ये", "ढीला करना, शिथिलता करना, छोड़ना, मुक्त करना", "to loosen, to leave, to abandon",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0457", 457, "कर्तँ", "कर्त्",
            "शैथिल्ये", "", "to loosen, to leave, to abandon",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0458", 458, "वल्क", "वल्क",
            "दर्शने", "देखना", "to see",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0459", 459, "चित्र", "चित्र",
            "चित्रीकरणे", "चित्र बनाना, तस्वीर खींचना", "to paint, to draw, to create a portrait",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0460", 460, "अन्स", "अंस",
            "समाघाते", "मार डालना, चोट पहुंचाना", "to kill, to destroy, to hurt",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0461", 461, "वट", "वट",
            "विभाजने", "विभाजन करना, अलग अलग करना, गूंथना", "to divide, to separate, to split, to isolate, to partition",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0462", 462, "छुटँ", "छुट्",
            "छेदने", "कतरना, चोट मारना", "to cut, to strike",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0463", 463, "लज", "लज",
            "प्रकाशने", "प्रकट होना, स्पष्ट होना", "to appear, to be clear",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0464", 464, "वटिँ", "वण्ट्",
            "प्रकाशने", "", "to appear, to be clear",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0465", 465, "लजिँ", "लञ्ज्",
            "प्रकाशने", "", "to appear, to be clear",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0466", 466, "मिश्र", "मिश्र",
            "सम्पर्के", "मिश्रित करना, एकत्र करना", "to mix, to add together, to combine",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0467", 467, "सन्ग्राम", "सङ्ग्राम",
            "युद्धे", "युद्ध करना, लड़ाई करना", "to have war, to fight",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0468", 468, "स्तोम", "स्तोम",
            "श्लाघायाम्", "प्रशंसा करना, स्तुति करना, आत्मश्लाघा करना, मुँह देखकर बोलना, खुशामद करना", "to praise, to boast, to cheer",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0469", 469, "छिद्र", "छिद्र",
            "कर्णभेदने", "कानोंको  छिदवाना", "to pierce ears",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0470", 470, "कर्णँ", "कर्ण",
            "भेदने", "", "to pierce ears",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0471", 471, "अन्ध", "अन्ध",
            "दृष्ट्युपघाते", "अन्धा होना, दिखाई न देना, आँखे मूंदना", "to be blind, to lose vision",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0472", 472, "दन्ड", "दण्ड",
            "दण्डनिपाते", "शासन करना, दण्ड देना", "to punish, to fine",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.DVIKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0473", 473, "अन्क", "अङ्क",
            "पदे लक्षणे च", "चिन्ह करना, गिनना, टहलना, अकड़ के चलना, गोद में लेना", "to mark, to count, to roam around, to limp, to take in lap",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0474", 474, "अन्ग", "अङ्ग",
            "पदे लक्षणे च", "चिन्ह करना, गिनना, टहलना, अकड़ के चलना, गोद में लेना", "to mark, to count, to roam around, to limp, to take in lap",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0475", 475, "सुख", "सुख",
            "तत्क्रियायाम्", "सुखी करना, आनंदानुभव करना", "to make happy, to be happy",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0476", 476, "दुःख", "दुःख",
            "तत्क्रियायाम्", "दुःख देना", "to make sad, to be sad",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0477", 477, "रस", "रस",
            "आस्वादनस्नेहनयोः", "स्वाद लेना, चखना, प्रीती करना, प्यार करना", "to tastem to love, to have affection",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0478", 478, "व्यय", "व्यय",
            "वित्तसमुत्सर्गे", "खर्च करना, व्यय करना", "to spend, to expense",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0479", 479, "रूप", "रूप",
            "रूपक्रियायाम्", "बनाना, आकार बनाना, रचना करना, मन में रूपाकृति बनाना", "to create a shape, to formulate, to create a mental picture",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0480", 480, "छेद", "छेद",
            "द्वैधीकरणे", "छेद करना", "to cut",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0481", 481, "छद", "छद",
            "अपवारणे", "हटाना, छिपाना, बचाना", "to remove, to hide, to conceal",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0482", 482, "लाभ", "लाभ",
            "प्रेरणे", "प्रेरणा करना", "to inspire",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0483", 483, "व्रण", "व्रण",
            "गात्रविचूर्णने", "क्षत करना, घाव करना, जख्मी करना", "to attack, to hurt, to damage someone, to wound",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0484", 484, "वर्ण", "वर्ण",
            "वर्णक्रियाविस्तारगुणवचनेषु", "वर्णन करना, विस्तृत करना, फैलाना, प्रशंसा करना, चमकना, प्रकाशित करना", "to explain, to elaborate, to spread, to praise, to glow, to shine",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0485", 485, "पर्ण", "पर्ण",
            "हरितभावे", "हरा करना, हरा होना", "to become green, to color green",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0486", 486, "विष्क", "विष्क",
            "दर्शने", "देखना", "to see",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0487", 487, "क्षिप", "क्षिप",
            "प्रेरणे", "फेंकना", "to inspire",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0488", 488, "वस", "वस",
            "निवासे", "निवास करना, वसना", "to live, to dwell, to stay",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0489", 489, "तुत्थ", "तुत्थ",
            "आवरणे", "आच्छादित करना, परदा डालना", "to cover",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0490", 490, "पल्यूल", "पल्यूल",
            "लवनवपनयोः", "", "to cut, to purify, to cleanse, to drop",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0491", 491, "रुठँ", "रुठ्",
            "भाषायाम्", "बोलना", "to speak, to talk",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0492", 492, "धेक", "धेक",
            "दर्शने", "देखना", "to see",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "10.0493", 493, "-", "ज्ञपादयो मितः",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0494", 494, "-", "नान्ये मितोऽहेतौ",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0495", 495, "-", "कुस्म नाम्नो वा",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0496", 496, "-", "आ कुस्मादात्मनेपदिनः",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0497", 497, "-", "आ गर्वादात्मनेपदिनः",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0498", 498, "-", "आ धृषाद्वा",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0499", 499, "-", "आ स्वदः सकर्मकात्",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0500", 500, "-", "हन्त्यर्थाश्च",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0501", 501, "-", "प्रातिपदिकाद्धात्वर्थे बहुलमिष्ठवच्च",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0502", 502, "-", "तत्करोति तदाचष्टे",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0503", 503, "-", "तेनातिक्रामति",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0504", 504, "-", "धातुरूपं च",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0505", 505, "-", "कर्तृकरणात् धात्वर्थे",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0506", 506, "-", "बहुलमेतन्निदर्शनम्",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0507", 507, "-", "णिङङ्गान्निरसने",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0508", 508, "-", "श्वेताश्वाश्वतरगालोडिताह्वरकाणामश्वतरेतकलोपश्च",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "10.0509", 509, "-", "पुच्छादिषु धात्वर्थ इत्येव सिद्धम्",
            "", "", "",
            null, null, null, null,
        )
    }
}
