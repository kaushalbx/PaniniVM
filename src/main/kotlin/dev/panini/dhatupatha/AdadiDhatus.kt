package dev.panini.dhatupatha

import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Complete Adadi-gaṇa imported from the MIT-shared ashtadhyayi-com data set. */
object AdadiDhatus {
    val all: List<Dhatu> = dhatuPatha(Gana.ADADI) {
        dhatu(
            "02.0001", 1, "अदँ", "अद्",
            "भक्षणे", "खाना", "to eat, to binge",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0002", 2, "हनँ", "हन्",
            "हिंसागत्योः", "मार डालना, जाना", "to kill, to destroy, to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0003", 3, "द्विषँ", "द्विष्",
            "अप्रीतौ", "द्वेष करना, शत्रुता करना", "to hate, to dislike, to grudge",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0004", 4, "दुहँ", "दुह्",
            "प्रपूरणे", "दूध निकालना, दोहना", "to milk, to extract",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.DVIKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0005", 5, "दिहँ", "दिह्",
            "उपचये", "बढ़ना, जमाना, लीपटना, पोतना", "to grow, to collect, to dishonor",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0006", 6, "लिहँ", "लिह्",
            "आस्वादने", "चाटना, चरवाना", "to lick, to taste, to pasture",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0007", 7, "चक्षिङ्", "चक्ष्",
            "व्यक्तायां वाचि", "स्पष्ट बोलना", "to articulate, to speak clearly",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0008", 8, "ईरँ", "ईर्",
            "गतौ कम्पने च", "जाना, कांपना", "to go,to shake,to tremble",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0009", 9, "ईडँ", "ईड्",
            "स्तुतौ", "प्रार्थना करना", "to pray, to worship, to adore, to laud",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0010", 10, "ईशँ", "ईश्",
            "ऐश्वर्ये", "अधिकार होना, मनानुसार काम करने की शक्ति रखना", "to rule,to command,to possess power",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0011", 11, "आसँ", "आस्",
            "उपवेशने", "बैठना", "to sit",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0012", 12, "शासुँ", "शास्",
            "इच्छायाम्", "भला करना, आशा करना, इच्छा करना", "to desire, to bless, to praise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0013", 13, "वसँ", "वस्",
            "आच्छादने", "ढकना, वस्त्र पहनना, ओढ़ना, पोशाख धारण करना", "to cover, to wear, to pull, to dress",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0014", 14, "कसिँ", "कंस्",
            "गतिशासनयोः", "जाना, दण्ड देना, शासन करना", "to go, to rule, to punish",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0015", 15, "कसँ", "कस्",
            "गतिशासनयोः", "", "to go, to rule, to punish",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0016", 16, "कशँ", "कश्",
            "गतिशासनयोः", "", "to go, to rule, to punish",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0017", 17, "णिसिँ", "निंस्",
            "चुम्बने", "चुम्बन लेना, चूमना", "to kiss",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0018", 18, "णिजिँ", "निञ्ज्",
            "शुद्धौ", "स्वच्छ करना, निर्मल करना", "to purify, to cleanse",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0019", 19, "शिजिँ", "शिञ्ज्",
            "अव्यक्ते शब्दे", "अस्पष्ट शब्द बोलना", "to murmur",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0020", 20, "पिजिँ", "पिञ्ज्",
            "वर्णे सम्पर्चने अवयवे अव्यक्ते शब्दे च", "रंगना, चमकीला करना, धुंधुरुओंका शब्द होना, टुकड़े टुकड़े करना, अस्पष्ट शब्द होना", "to color, to paint, to make shine, to murmur",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0021", 21, "पृजिँ", "पृञ्ज्",
            "वर्णे", "", "to color, to paint, to make shine",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0022", 22, "वृजीँ", "वृज्",
            "वर्जने", "छोड़ना, वर्जित करना", "to avoid,to abandon, to leave",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0023", 23, "वृजिँ", "वृञ्ज्",
            "वर्जने", "", "to avoid,to abandon, to leave",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0024", 24, "पृचीँ", "पृच्",
            "सम्पर्चने", "संपर्क करना, मिलाना", "to unite, to communicate",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0025", 25, "षूङ्", "सू",
            "प्राणिगर्भविमोचने", "उत्पन्न करना, प्रसव करना, जनना", "to procreate, to give birth",
            PadaType.ATMANEPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0026", 26, "शीङ्", "शी",
            "स्वप्ने", "सोना, शयन करना", "to sleep",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0027", 27, "यु", "यु",
            "मिश्रणेऽमिश्रणे च", "मिश्रित करना, मिलाना, मिलाप करना, पृथक पृथक करना", "to mix, to join, to group",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0028", 28, "रु", "रु",
            "शब्दे", "शब्द करना, आवाज करना", "to sound, to howl, to hum, to yelp, to roar, to cry out loud",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0029", 29, "तु", "तु",
            "गतिवृद्धिहिंसासु", "", "to go,to grow,to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0030", 30, "णु", "नु",
            "स्तुतौ", "स्तुति करना, प्रार्थना करना", "to praise, to worship",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0031", 31, "टुक्षु", "क्षु",
            "शब्दे", "शब्द करना", "to sneeze, to cough",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0032", 32, "क्ष्णु", "क्ष्णु",
            "तेजने", "तीक्ष्ण करना, तेज  करना", "to whet, to sharpen",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0033", 33, "ष्णु", "स्नु",
            "प्रस्रवणे", "टपकना, झरना, चूना", "to ooze, to drip",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0034", 34, "ऊर्णुञ्", "ऊर्णु",
            "आच्छादने", "ढकना", "to cover",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0035", 35, "द्यु", "द्यु",
            "अभिगमने", "शत्रु पर आक्रमण करना, आगे जाना, समीप जाना", "to assail,to attack, to advance towards",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0036", 36, "षु", "सु",
            "प्रसवैश्वर्ययोः", "उत्पन्न करना, पैदा करना, जनना, गर्भ धारण करना, अद्भुत सामर्थ्य होना", "to give birth, to procreate, to become pregnant, to possess power, to possess supremacy",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0037", 37, "कु", "कु",
            "शब्दे", "शब्द करना", "to hum,to moan,to sound",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0038", 38, "ष्टुञ्", "स्तु",
            "स्तुतौ", "प्रार्थना करना, स्तुति करना, प्रशंसा करना, भजन करना", "to pray, to worship, to glorify, to praise",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0039", 39, "ब्रूञ्", "ब्रू",
            "व्यक्तायां वाचि", "कहना, बोलना", "to speak, to tell, to explain",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.DVIKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0040", 40, "इण्", "इ",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0041", 41, "इङ्", "इ",
            "अध्ययने", "अध्ययन करना, अभ्यास करना, सीखना", "to study, to learn",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0042", 42, "इक्", "इ",
            "स्मरणे", "स्मरण करना, याद करना, चिन्तन करना", "to memorize, to think",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0043", 43, "वी", "वी",
            "गतिव्याप्तिप्रजनकान्त्यसनखादनेषु", "जनजाति, घेरना, आक्रमण करना, गर्भवती होना, गर्भधारण करना, चमकना, फेंकना, खाना, भक्षण करना", "to go, to pervade, to surround, to attack, to be pregnant, to conceive, to glow, to shine, to throw, to eat",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0044", 44, "या", "या",
            "प्रापणे", "जाना", "to go, to pass",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0045", 45, "वा", "वा",
            "गतिगन्धनयोः", "जाना, हवा की तरह तीव्र गति में चलना, बहना, गंध लगना", "to go,to blow, to move rapidly",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0046", 46, "भा", "भा",
            "दीप्तौ", "चमकना, प्रकाशित होना, सुन्दर दिखना", "to glow, to shine, to look attractive, to look beautiful",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0047", 47, "ष्णा", "स्ना",
            "शौचे", "स्नान करना, नहाना, शुद्ध होना", "to bathe, to cleanse, to purify",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0048", 48, "श्रा", "श्रा",
            "पाके", "पकाना, राघना, उबालना, पसीजना, पसीना निकालना", "to cook, to boil, to exude, to sweat",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0049", 49, "द्रा", "द्रा",
            "कुत्सायां गतौ", "बुरी चाल चलना, भागना, उड़ जाना, सोना", "to be spoiled, to act crucked, to fly, to run away, to sleep",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0050", 50, "प्सा", "प्सा",
            "भक्षणे", "खाना", "to devour,to eat",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0051", 51, "पा", "पा",
            "रक्षणे", "रक्षा करना, पालन करना", "to protect, to preserve",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0052", 52, "रा", "रा",
            "दाने", "देना, मिल जाना", "to give, to grant",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0053", 53, "ला", "ला",
            "आदाने", "लेना, ग्रहण करना", "to accept, to obtain",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0054", 54, "दाप्", "दा",
            "लवने", "काटना, कतराना", "to cut",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0055", 55, "ख्या", "ख्या",
            "प्रकथने", "प्रख्यात करना, कहना, व्याख्यान करना", "to explain, to make famous, to elocute",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0056", 56, "प्रा", "प्रा",
            "पूरणे", "भरना, तृप्त होना", "to fill, to be satisfied, to be content",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0057", 57, "मा", "मा",
            "माने", "नापना, तौलना, अनुमान से सिद्ध करना", "to measure,to weigh,to limit,to compare in size",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0058", 58, "वचँ", "वच्",
            "परिभाषणे", "बोलना, कहना", "to speak,to tell, to talk",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.DVIKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0059", 59, "विदँ", "विद्",
            "ज्ञाने", "समझना, जानना, दुःखी होना, ध्यान करना, मनन करना", "to understand, to learn, to know, to realize, to experience, to be sad, to meditate, to think",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0060", 60, "असँ", "अस्",
            "भुवि", "होना, रहना", "to be, to exist",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0061", 61, "मृजूँ", "मृज्",
            "शुद्धौ", "धोना, स्वच्छ करना", "to cleanse, to purify",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0062", 62, "रुदिँर्", "रुद्",
            "अश्रुविमोचने", "रोना", "to shed tears,to cry,to weep,to lament",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0063", 63, "ञिष्वपँ", "स्वप्",
            "शये", "सोना, निद्रा लेना", "to sleep",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "02.0064", 64, "श्वसँ", "श्वस्",
            "प्राणने", "सांस लेना, श्वासोच्छ्वास करना, जीना, जीते रहना", "to breathe,to live, to inhale",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0065", 65, "अनँ", "अन्",
            "प्राणने", "जीना, श्वासोच्छवास करना, समर्थ होना", "to breathe,to live, to inhale, to be able",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0066", 66, "जक्षँ", "जक्ष्",
            "भक्षहसनयोः", "कहना, हसना", "to eat,to consume,to laugh",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0067", 67, "जागृ", "जागृ",
            "निद्राक्षये", "जागना, नींद न लेना", "to be awake, to abandon sleep, to be watchful",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0068", 68, "दरिद्रा", "दरिद्रा",
            "दुर्गतौ", "दरिद्र होना, दुःखित होना, गरीब होना, निर्धन होना", "to be poor,to be distressed, to be sad",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0069", 69, "चकासृँ", "चकास्",
            "दीप्तौ", "चमकना, प्रकाशित होना", "to shine,to be bright, to be prosperous, to glow",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0070", 70, "शासुँ", "शास्",
            "अनुशिष्टौ", "आज्ञा करना, कहना, बोध करना, अधिकार करना, शासन करना, शासक होना", "to order, to tell, to explain, to educate, to inform, to rule, to command to punish",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.DVIKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0071", 71, "दीधीङ्", "दीधी",
            "दीप्तिदेवनयोः", "चमकना, प्रकाशित होना, खेलना, क्रीड़ा करना", "to glow, to shine, to play, to enjoy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0072", 72, "वेवीङ्", "वेवी",
            "गतिव्याप्तिप्रजनकान्त्यसनस्वादनेषु", "जाना, चलना, व्याप्त होना, गर्भवती होना, इच्छा करना, भेजना, खाना, स्वाद लेना", "to go, to walk to pervade, to become pregnant, to conceive, to wish, to desire, to send, to eat, to taste",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0073", 73, "षसँ", "सस्",
            "स्वप्ने", "सोना", "to sleep",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0074", 74, "षस्तिँ", "संस्त्",
            "स्वप्ने", "सोना", "to sleep",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0075", 75, "वशँ", "वश्",
            "कान्तौ", "इच्छा करना, चाहना", "to wish,to long for, to desire",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "02.0076", 76, "-", "चर्करीतं च",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "02.0077", 77, "ह्नुङ्", "ह्नु",
            "अपनयने", "छिपाना, लुकाना, चुराना, ले जाना", "to hide, to conceal,to take away,to deny",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
    }
}
