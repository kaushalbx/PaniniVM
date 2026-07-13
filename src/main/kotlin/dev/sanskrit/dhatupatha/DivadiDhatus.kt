package dev.sanskrit.dhatupatha

import dev.sanskrit.shiksha.Accent
import dev.sanskrit.shiksha.ItStatus
import dev.sanskrit.shiksha.Karmatva

/** Complete Divadi-gaṇa imported from the MIT-shared ashtadhyayi-com data set. */
object DivadiDhatus {
    val all: List<Dhatu> = dhatuPatha(Gana.DIVADI) {
        dhatu(
            "04.0001", 1, "दिवुँ", "दिव्",
            "क्रीडाविजिगीषाव्यवहारद्युतिस्तुतिमोदमदस्वप्नकान्तिगतिषु", "खेलना, जीतने की इच्छा करना, व्यवहार करना, तेजस्वी होना, चमकना, प्रशंसा करना, स्तुति करना, प्रसन्न होना या करना, गर्व करना, सो जाना, चाहना, जाना", "to play, to gamble, to dice, to desire to win, to transact, to glow, to shine, to praise, to please, to boast, to sleep, to wish, to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0002", 2, "षिवुँ", "सिव्",
            "तन्तुसन्ताने", "सीना, सिलाई करना", "to weave, to sew",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0003", 3, "स्रिवुँ", "स्रिव्",
            "गतिशोषणयोः", "जाना, शुष्क होना, सूखना", "to go, to dry up",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0004", 4, "ष्ठिवुँ", "ष्ठिव्",
            "निरसने", "थूकना, पानी आदि को मुह से फेंकना", "to spit, to rinse the mouth",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0005", 5, "ष्णुसुँ", "स्नुस्",
            "अदने आदाने अदर्शने च", "खाना, निगलना, ग्रहण करना, लेना, अदर्शन होना, नहीं दिखाई देना", "to eat, to swallow, to consume, to take, to disappear, to be invisible",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0006", 6, "ष्णसुँ", "स्नस्",
            "अदने निरसने च", "खाना, निगलना, ग्रहण करना, लेना, अदर्शन होना, नहीं दिखाई देना", "to spit, to rinse the mouth",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0007", 7, "क्नसुँ", "क्नस्",
            "ह्वरणदीप्त्योः", "मन से या शरीर से वक्र होना, चमकना", "to be crooked,to shine, to glow",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0008", 8, "व्युषँ", "व्युष्",
            "दाहे", "जलना, जलाना, भूनना, दग्ध करना, अलग करना", "to burn, to set on fire, to fry, to split",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0009", 9, "प्लुषँ", "प्लुष्",
            "दाहे", "जलाना, जलना", "to burn, to set on fire, to fry, to split",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0010", 10, "नृतीँ", "नृत्",
            "गात्रविक्षेपे", "नाचना, नृत्य करना", "to dance,to gesticulate, to enact",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0011", 11, "त्रसीँ", "त्रस्",
            "उद्वेगे", "डरना", "to fear,to tremble",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0012", 12, "कुथँ", "कुथ्",
            "पूतीभावे", "बदबू आना, दुर्गन्ध आना", "to have rotten smell, to have bad smell",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0013", 13, "पुथँ", "पुथ्",
            "हिंसायाम्", "दुःख देना, मारना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0014", 14, "गुधँ", "गुध्",
            "परिवेष्टने", "घेरना", "to surround, to cover",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0015", 15, "क्षिपँ", "क्षिप्",
            "प्रेरणे", "फेकना", "to throw",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0016", 16, "पुष्पँ", "पुष्प्",
            "विकसने", "पुष्पयुक्त होना, फूलना, विकसित होना", "to blossom, to flourish, to flower",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0017", 17, "तिमँ", "तिम्",
            "आर्द्रीभावे", "आद्र होना, गीला होना", "to be wet,to be moist",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0018", 18, "तीमँ", "तीम्",
            "आर्द्रीभावे", "", "to be wet,to be moist",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0019", 19, "ष्टिमँ", "स्तिम्",
            "आर्द्रीभावे", "गीला होना, भीगना", "to be wet,to be moist",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0020", 20, "ष्टीमँ", "स्तीम्",
            "आर्द्रीभावे", "गीला होना, भीगना", "to be wet,to be moist",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0021", 21, "व्रीडँ", "व्रीड्",
            "चोदने लज्जायां च", "लज्जित होना, शरमाना, प्रेरित करना", "to be shy, to inspire",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0022", 22, "इषँ", "इष्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0023", 23, "षहँ", "सह्",
            "चक्यर्थे", "तृप्त होना, सहन करना, प्रतिरोध करना", "to be satisfied, to be patient, to tolerate, to resist",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0024", 24, "षुहँ", "सुह्",
            "चक्यर्थे", "तृप्त होना, सहन करना, प्रतिरोध करना", "to be satisfied, to be patient, to tolerate, to resist",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0025", 25, "जॄष्", "जॄ",
            "वयोहानौ", "जीर्ण होना, वृद्ध होना", "to grow old",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0026", 26, "झॄष्", "झॄ",
            "वयोहानौ", "वृद्ध होना", "to grow old",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0027", 27, "षूङ्", "सू",
            "प्राणिप्रसवे", "गर्भ धारण करना, जनना, उत्पन्न करना", "to procreate, to conceive, to give birth",
            PadaType.ATMANEPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0028", 28, "दूङ्", "दू",
            "परितापे", "दुःख देना", "to feel sad",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0029", 29, "दीङ्", "दी",
            "क्षये", "ह्रास होना, झरना, नाश होना", "to perish,to decay, to deteriorate",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0030", 30, "डीङ्", "डी",
            "विहायसा गतौ", "आकाश में उड़ जाना, उड़ना", "to pass through air, to fly",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0031", 31, "धीङ्", "धी",
            "आधारे आदाने अनादरे च", "धारण करना, आधारभूत होना, गुप्त होना, छिप जाना", "to support,to hold,to take, to despise, to become hidden, to disappear",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0032", 32, "मीङ्", "मी",
            "हिंसायाम्", "मारना, देह त्याग करना", "to kill, to die, to leave body",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0033", 33, "रीङ्", "री",
            "स्रवणे", "सुनना", "to ooze,to flow",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0034", 34, "लीङ्", "ली",
            "श्लेषणे", "युक्त होना, मिलना", "to adhere,to be absorbed,to dissolve, to unite",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0035", 35, "व्रीङ्", "व्री",
            "वृणोत्यर्थे", "पसन्द करना, ढूंढ के निकालना, बीनना", "to choose,to select,to find, to glean",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0036", 36, "पीङ्", "पी",
            "पाने", "पीना", "to drink",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0037", 37, "माङ्", "मा",
            "माने", "नापना, तौलना", "to measure,to weigh,to limit,to compare in size",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0038", 38, "ईङ्", "ई",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0039", 39, "प्रीङ्", "प्री",
            "प्रीतौ", "प्रीती करना", "to please,to love, to show affection",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0040", 40, "शो", "शो",
            "तनूकरणे", "तीक्ष्ण करना, शान धराना, पैदा करना", "to whet,to sharpen",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0041", 41, "छो", "छो",
            "छेदने", "काटना, कतरना", "to cut,to mow",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0042", 42, "षो", "सो",
            "अन्तकर्मणि", "विध्वंस करना, नष्ट करना, नष्ट होना, मग्न होना", "to kill, to destroy, to terminate, to  finish, to end",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0043", 43, "दो", "दो",
            "अवखण्डने", "कतरना, विभाग करना", "to cut,to mow",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0044", 44, "जनीँ", "जन्",
            "प्रादुर्भावे", "उत्पन्न करना, पैदा होना", "to be born,to become, to come to existence",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0045", 45, "दीपीँ", "दीप्",
            "दीप्तौ", "प्रकाशित होना, चमकना", "to shine,to burn,to be illustrious, to glow",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0046", 46, "पूरीँ", "पूर्",
            "आप्यायने", "पूर्ण करना, भरना", "to fill,to complete, to fullfill",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0047", 47, "तूरीँ", "तूर्",
            "गतित्वरणहिंसनयोः", "जल्दी करना, दुःख देना, सताना", "to hurry, to destroy, to irritate",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0048", 48, "धूरीँ", "धूर्",
            "हिंसागत्योः", "मारना, हिंसा करना, जाना", "to kill, to destroy, to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0049", 49, "गूरीँ", "गूर्",
            "हिंसागत्योः", "मारना, जाना", "to kill, to destroy, to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0050", 50, "घूरीँ", "घूर्",
            "हिंसावयोहान्योः", "हिंसा करना, दुःख देना, वृद्ध होना", "to kill,to torture, to become old",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0051", 51, "जूरीँ", "जूर्",
            "हिंसावयोहान्योः", "हिंसा करना, दुःख देना, वृद्ध होना", "to kill,to torture, to become old",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0052", 52, "शूरीँ", "शूर्",
            "हिंसास्तम्भनयोः", "मार डालना, दुःख देना, निश्चल होना", "to kill,to torture, to become old",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0053", 53, "चूरीँ", "चूर्",
            "दाहे", "जलाना, भस्म करना", "to burn to ashes",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0054", 54, "तपँ", "तप्",
            "ऐश्वर्ये", "", "to be powerful",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0055", 55, "वृतुँ", "वृत्",
            "वरणे", "पसन्द करना, सेवा करना, नौकरी करना", "to choose, to like, to serve, to do employment",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0056", 56, "वावृतुँ", "वावृत्",
            "वरणे", "", "to choose, to like, to serve, to do employment",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0057", 57, "क्लिशँ", "क्लिश्",
            "उपतापे", "रोगी होना, दुखी होना, दुःख सहन करना", "to be ill, to be sad, to suffer",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0058", 58, "काशृँ", "काश्",
            "दीप्तौ", "चमकना", "to glow, to shine",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0059", 59, "वाशृँ", "वाश्",
            "शब्दे", "शब्द करना", "to howl,to growl,to roar",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0060", 60, "मृषँ", "मृष्",
            "तितिक्षायाम्", "सहन करना, क्षमा करना", "to tolerate,to bear,to endure,to suffer,to forgive",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0061", 61, "ईँशुचिँर्", "शुच्",
            "पूतीभावे", "शुद्ध होना, स्नान करना, पवित्र होना", "to be wet,to be clean,to be purified",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0062", 62, "णहँ", "नह्",
            "बन्धने", "बांधना", "to tie",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0063", 63, "रन्जँ", "रञ्ज्",
            "रागे", "रंगना, किसी वस्तु में अनुरक्त होना, मोहित होना", "to color, to be involved in, to be attracted to",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0064", 64, "शपँ", "शप्",
            "आक्रोशे", "शपथ करना, श्राप देना", "to swear, to take oath, to curse",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0065", 65, "पदँ", "पद्",
            "गतौ", "जाना", "to go,to attain",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0066", 66, "खिदँ", "खिद्",
            "दैन्ये", "खिन्न होना, दुःख सहन करना, दीनता प्रकट करना", "to be sad, to be displeased, to be distressed",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0067", 67, "विदँ", "विद्",
            "सत्तायाम्", "विद्यमान होना", "to exist",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0068", 68, "बुधँ", "बुध्",
            "अवगमने", "जानना", "to know, to understand",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0069", 69, "युधँ", "युध्",
            "सम्प्रहारे", "लड़ाई करना, युद्ध करना, संग्राम करना", "to fight",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0070", 70, "रुधँ", "रुध्",
            "कामे", "कृपालु होना, दया करना, अनुमोदन देना, शोक करना, रोना, चाहना", "to pitty, to be compassionate, to agree, to approve, to mourn, to grieve, to desire, to wish",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0071", 71, "अणँ", "अण्",
            "प्राणने", "जीना", "to live, to breath",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0072", 72, "अनँ", "अन्",
            "प्राणने", "", "to breath,to breathe",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0073", 73, "मनँ", "मन्",
            "ज्ञाने", "जानना, समझना", "to believe, to know, to understand, to consider, to think",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0074", 74, "युजँ", "युज्",
            "समाधौ", "चित्त स्थिर करना, मन को रोकना", "to concentrate, to focus, to abstain from senses, to meditate",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0075", 75, "सृजँ", "सृज्",
            "विसर्गे", "छोड़ना, विविध प्रकार से उत्पन्न करना, रचना करना", "to discharge, to let loose, to let off",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0076", 76, "लिशँ", "लिश्",
            "अल्पीभावे", "कम करना", "to decrease,to reduce",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0077", 77, "राधँ", "राध्",
            "वृद्धिसिद्धिद्रोहदैवपर्यालोचनादिषु च", "बढ़ना", "to grow",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0078", 78, "व्यधँ", "व्यध्",
            "ताडने", "मारना, पीटना, दुःख देना, चुभाना, छेदना", "to strike,to hurt,to stab,to pierce, to hit, to cause pain, to pinch",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0079", 79, "पुषँ", "पुष्",
            "पुष्टौ", "पालन करना, पोषण करना", "to nurture, to nourish, to maintain,to promote,to bring up,to support",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0080", 80, "शुषँ", "शुष्",
            "शोषणे", "शुष्क होना, सूखना", "to become dry",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0081", 81, "तुषँ", "तुष्",
            "प्रीतौ", "संतुष्ट होना, खुश होना", "to be pleased, to be delighted, to be happy",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0082", 82, "दुषँ", "दुष्",
            "वैकृत्ये", "दुःखित होना या करना, दूषित करना", "to sin, to soil, to make impure, to cause pain, to hurt",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0083", 83, "श्लिषँ", "श्लिष्",
            "आलिङ्गने", "आलिंगन करना, गले लगाना, चिपके रहना", "to hug,to embrace",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0084", 84, "शकँ", "शक्",
            "मर्षणे", "शक्य होना, शकना", "to be able,to be possible",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0085", 85, "ष्विदाँ", "स्विद्",
            "गात्रप्रक्षरणे", "पसीना आना, पसीना छूटना", "to sweat, to perspire",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0086", 86, "क्रुधँ", "क्रुध्",
            "क्रोधे", "क्रोध करना", "to be angry",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0087", 87, "क्षुधँ", "क्षुध्",
            "बुभुक्षायाम्", "भूख लगना", "to be hungry",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0088", 88, "शुधँ", "शुध्",
            "शौचे", "शुद्ध होना, पवित्र होना", "to become pure",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0089", 89, "षिधुँ", "सिध्",
            "संराद्धौ", "सिद्ध होना, जीत होना", "to be thoroughly prepared,to be accomplished,to be achieved, to be ready",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0090", 90, "रधँ", "रध्",
            "हिंसासंराद्ध्योः", "मार डालना, दुःख देना, पक्व होना या पकना", "to kill,to injure,to hurt, to be ripen, to be ready",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0091", 91, "णशँ", "नश्",
            "अदर्शने", "अदर्शन होना, नाश होना, दिखाई न देना", "to disappear,to perish,to get destroyed, to be invisible",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0092", 92, "तृपँ", "तृप्",
            "प्रीणने", "तृप्त होना या करना, प्रसन्न होना या करना", "to satisfy, to be satisfied, to please, to be pleased",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0093", 93, "दृपँ", "दृप्",
            "हर्षमोहनयोः", "प्रसन्न होना, गर्व करना", "to be glad,to be proud",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0094", 94, "द्रुहँ", "द्रुह्",
            "जिघांसायाम्", "द्वेष करना, मारने के लिए प्रयत्न करना", "to hate, to try to hurt",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0095", 95, "मुहँ", "मुह्",
            "वैचित्त्ये", "पागल होना, बुद्धि भ्रष्ट होना", "to lose senses,to faint, to be foolish, to err",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0096", 96, "ष्णुहँ", "स्नुह्",
            "उद्गिरणे", "कै करना, वमन करना", "to vomit",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0097", 97, "ष्णिहँ", "स्निह्",
            "प्रीतौ", "स्नेह करना, प्रेम करना", "to feel affection for,to love,to be kind to",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0098", 98, "शमुँ", "शम्",
            "उपशमे", "शांत होना, शमन करना", "to be calm, to be pacified, to be quiet",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0099", 99, "तमुँ", "तम्",
            "काङ्क्षायाम्", "इच्छा करना, दुःखित होना", "to desire, to wish, to be sad",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0100", 100, "दमुँ", "दम्",
            "उपशमे", "शांत करना, दमन करना", "to be calm, to be pacified, to be quiet",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0101", 101, "श्रमुँ", "श्रम्",
            "तपसि खेदे च", "तपश्चर्या करना, व्रत करना, थकना, दुःखित होना", "to do penance, to be fatigued, to be tired",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0102", 102, "भ्रमुँ", "भ्रम्",
            "अनवस्थाने", "अस्थिर होना, भ्रमण करना", "to be unsteady,to wander to roam",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0103", 103, "क्षमूँ", "क्षम्",
            "सहने", "सहन करना, क्षमा करना", "to endure, to suffer, to tolerate, to forgive",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0104", 104, "क्लमुँ", "क्लम्",
            "ग्लानौ", "श्रमित होना, थकना, कुम्हलाना", "to be fatigued, to be tired",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0105", 105, "मदीँ", "मद्",
            "हर्षे ग्लेपने च", "प्रसन्न होना, थकना, श्रान्त होना", "to be happy, to be tired, to be intoxicated",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0106", 106, "असुँ", "अस्",
            "क्षेपणे", "फेकना, निराश होना", "to throw, to be disappointed",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0107", 107, "यसुँ", "यस्",
            "प्रयत्ने", "प्रयास करना", "to endeavour,to strive, to try",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0108", 108, "जसुँ", "जस्",
            "मोक्षणे", "मुक्त करना, छोड़ देना", "to leave, to set free",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0109", 109, "तसुँ", "तस्",
            "उपक्षये", "फेंकना, उड़ा देना", "to throw, to bounce, to fly away",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0110", 110, "दसुँ", "दस्",
            "उपक्षये", "फेंकना", "to throw, to bounce, to fly away",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0111", 111, "वसुँ", "वस्",
            "स्तम्भे", "निश्चल होना, सीधा होना", "to be firm to be fixed, to be straight",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0112", 112, "बसुँ", "बस्",
            "स्तम्भे", "", "to be firm to be fixed, to be straight",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0113", 113, "युसँ", "युस्",
            "विभागे", "अलग करना, विभाग करना, जलाना, भूनना, दग्ध करना", "to split, to divide, to separate, to partition, to burn, to fry",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0114", 114, "व्युषँ", "व्युष्",
            "विभागे", "अलग करना, विभाग करना, जलाना, भूनना, दग्ध करना", "to split, to divide, to separate, to partition, to burn, to fry",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0115", 115, "व्युसँ", "व्युस्",
            "विभागे", "", "to split, to divide, to separate, to partition, to burn, to fry",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0116", 116, "ब्युसँ", "ब्युस्",
            "विभागे", "", "to split, to divide, to separate, to partition, to burn, to fry",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0117", 117, "बुसँ", "बुस्",
            "विभागे", "", "to split, to divide,to separate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0118", 118, "वुसँ", "वुस्",
            "विभागे", "", "to split, to divide, to separate, to partition",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0119", 119, "प्युषँ", "प्युष्",
            "विभागे", "", "to split, to divide, to separate, to partition",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0120", 120, "प्युसँ", "प्युस्",
            "विभागे", "", "to split, to divide, to separate, to partition",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0121", 121, "पुषँ", "पुष्",
            "विभागे", "", "to partition, to divide",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0122", 122, "प्लुषँ", "प्लुष्",
            "दाहे", "जलाना", "to burn",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0123", 123, "विसँ", "विस्",
            "प्रेरणे", "प्रेरणा करना", "to inspire",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0124", 124, "बिसँ", "बिस्",
            "प्रेरणे", "", "to inspire",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0125", 125, "कुसँ", "कुस्",
            "संश्लेषणे", "मिलना", "to embrace,to surround, to get together",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0126", 126, "कुशँ", "कुश्",
            "संश्लेषणे", "", "to embrace,to surround, to get together",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0127", 127, "ञिष्विदाँ", "स्विद्",
            "गात्रप्रक्षरणे", "पसीना आना, पसीना छूटना", "to sweat, to perspire",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "04.0128", 128, "क्षमूँष्", "क्षम्",
            "सहने", "सहन करना, क्षमा करना", "to endure, to suffer, to tolerate, to forgive",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0129", 129, "बुसँ", "बुस्",
            "उत्सर्गे", "त्यागना", "to discharge,to emit",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0130", 130, "मुसँ", "मुस्",
            "खण्डने", "टुकड़े टुकड़े करना", "to piece, to cut, to petition",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0131", 131, "मसीँ", "मस्",
            "परिणामे", "रूपान्तर करना, आकार बदलना", "to transform, to shape",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0132", 132, "समीँ", "सम्",
            "परिणामे", "", "to transform, to shape",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0133", 133, "लुटँ", "लुट्",
            "विलोडने", "विलोडना, कांपना, हिलना", "to churn,to agitate,to shake, to move",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0134", 134, "लुठँ", "लुठ्",
            "विलोडने", "", "to churn,to agitate,to shake, to move",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0135", 135, "उचँ", "उच्",
            "समवाये", "एकत्र होना, इकठ्ठा करना या होना", "to be gather, to be collected",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0136", 136, "भृशुँ", "भृश्",
            "अधःपतने", "गिरना, भ्रष्ट होना", "to churn,to agitate,to shake, to move",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0137", 137, "स्तिमँ", "स्तिम्",
            "आर्द्रीभावे", "गीला होना, भीगना", "to be wet,to be moist",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0138", 138, "भ्रन्शुँ", "भ्रंश्",
            "अधःपतने", "गिरना, पतन होना", "to churn,to agitate,to shake, to move",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0139", 139, "वृशँ", "वृश्",
            "आवरणे", "आच्छादन करना", "to cover",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0140", 140, "कृशँ", "कृश्",
            "तनूकरणे", "कृश होना, पतला होना", "to be thin",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0141", 141, "ञितृषाँ", "तृष्",
            "पिपासायाम्", "प्यास लगाना", "to be thirsty, to be desirous",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0142", 142, "हृषँ", "हृष्",
            "तुष्टौ", "संतुष्ट होना, प्रसन्न होना", "to be delighted , to rejoice , to be happy, to fill with pleasure",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0143", 143, "रुषँ", "रुष्",
            "हिंसायाम्", "मारना", "to be angry",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0144", 144, "रिषँ", "रिष्",
            "हिंसायाम्", "मारना", "to be angry",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0145", 145, "डिपँ", "डिप्",
            "क्षेपे", "फेंकना,उड़ाना", "to throw, to make fly",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0146", 146, "कुपँ", "कुप्",
            "क्रोधे", "क्रोध करना", "to be angry",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0147", 147, "गुपँ", "गुप्",
            "व्याकुलत्वे", "व्याकुल होना", "to be confused, to be disturbed,to be distressed",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0148", 148, "युपँ", "युप्",
            "विमोहने", "चित्त विकल होना, भ्रान्त होना, घबड़ा जाना", "to be confused, to be afraid",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0149", 149, "रुपँ", "रुप्",
            "विमोहने", "चित्त विकल होना, भ्रान्त होना, घबड़ा जाना", "to be confused, to be afraid",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0150", 150, "लुपँ", "लुप्",
            "विमोहने", "चित्त विकल होना, भ्रान्त होना, घबड़ा जाना", "to be confused, to be afraid",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0151", 151, "ष्टुपँ", "स्तुप्",
            "समुच्छ्राये", "", "to grow, to rise, to escalate, to accumulate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0152", 152, "ष्टूपँ", "स्तूप्",
            "समुच्छ्राये", "", "to grow, to rise, to escalate, to accumulate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0153", 153, "लुभँ", "लुभ्",
            "गार्द्ध्ये", "आशा करना, चाहना, लोभ करना", "to long for, to wish, to be greedy, to desire",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0154", 154, "क्षुभँ", "क्षुभ्",
            "सञ्चलने", "मथना, क्रोध करना", "to agitate, to be agitated,to shake,to be disturbed,to tremble",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0155", 155, "णभँ", "नभ्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0156", 156, "तुभँ", "तुभ्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0157", 157, "क्लिदूँ", "क्लिद्",
            "आर्द्रीभावे", "आर्द्र होना, गीला होना", "to be wet, to be moist",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0158", 158, "ञिमिदाँ", "मिद्",
            "स्नेहने", "पिघलना, मृदुल होना", "to be greasy,to be soft,to melt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0159", 159, "ञिक्ष्विदाँ", "क्ष्विद्",
            "स्नेहनमोचनयोः", "तेल की मालिश करना, चुपडना, मुक्त करना", "to massage, to set free",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0160", 160, "ऋधुँ", "ऋध्",
            "वृद्धौ", "बढ़ना, वृद्धि होना", "to increase,to prosper,to grow",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0161", 161, "गृधुँ", "गृध्",
            "अभिकाङ्क्षायाम्", "चाहना", "to desire, to long",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "04.0162", 162, "-", "स्वादयः ओदितः",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "04.0163", 163, "-", "राधोऽकर्मकाद् वृद्धावेव",
            "", "", "",
            null, null, null, null,
        )
    }
}
