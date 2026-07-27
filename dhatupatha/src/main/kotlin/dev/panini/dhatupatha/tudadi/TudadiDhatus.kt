package dev.panini.dhatupatha.tudadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.dhatuPatha
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

object TudadiDhatus {
    val all: List<Dhatu> = dhatuPatha(DhatuGana.TUDADI) {
        dhatu(
            "06.0001", 1, "तुदँ", "तुद्",
            "व्यथने", "दुःख देना, पीड़ा करना, घाव करना", "to wound,to strike,to pain,to irritate, to hurt, to cause pain, to attack",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0002", 2, "णुदँ", "नुद्",
            "प्रेरणे", "प्रेरणा करना", "to inspire",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0003", 3, "दिशँ", "दिश्",
            "अतिसर्जने", "दान देना", "to give, to donate, to grant, to leave",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0004", 4, "भ्रस्जँ", "भ्रज्ज्",
            "पाके", "पकाना, भुजना", "to fry,to roast,to parch",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(KshipDhatu())
        dhatu(
            "06.0006", 6, "कृषँ", "कृष्",
            "विलेखने", "जोतना, हल चलाना", "to plow, to furrow, to cultivate",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0007", 7, "ऋषीँ", "ऋष्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0008", 8, "जुषीँ", "जुष्",
            "प्रीतिसेवनयोः", "सेवा करना, प्रसन्न होना, सन्तुष्ट करना", "to serve, to please, to satisfy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0009", 9, "ओँविजीँ", "विज्",
            "भयचलनयोः", "डरना, डर से कम्पित होना, कांपना, अपद्ग्रस्त होना, विपत्ति में पड़ना", "to fear,to tremble, to shiver, to have disaster",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0010", 10, "ओँलजीँ", "लज्",
            "व्रीडायाम्", "लज्जित होना, मुँह छिपाना", "to be ashamed,to be shy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0011", 11, "ओँलस्जीँ", "लज्ज्",
            "व्रीडायाम्", "लज्जित होना", "to be ashamed,to be shy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0012", 12, "ओँव्रस्चूँ", "व्रश्च्",
            "छेदने", "छेद करना, कतरना", "to cut,to make a hole, to tear",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0013", 13, "व्यचँ", "व्यच्",
            "व्याजीकरणे", "ठगना, फ़साना", "to deceive,to cheat",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0014", 14, "उछिँ", "उञ्छ्",
            "उञ्छे", "थोड़ा थोड़ा एकत्र करना, थोड़ा थोड़ा बटोरना, बीनना", "to collect, to gather, to glean",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0015", 15, "उछीँ", "उच्छ्",
            "विवासे", "पूरा करना, समाप्त करना, छोड़ना, त्यागना", "to dwell abroad,to retire, to exile",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0016", 16, "ऋछँ", "ऋच्छ्",
            "गतीन्द्रियप्रलयमूर्तिभावेषु", "जाना, कठिन होना, इन्द्रिय का बलघट जाना", "to go, to become weak, to lose power of senses, to harden",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0017", 17, "मिछँ", "मिच्छ्",
            "उत्क्लेशे", "पीड़ा देना, कष्ट देना", "to annoy,to hurt,to obstruct",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0018", 18, "जर्जँ", "जर्ज्",
            "परिभाषणभर्त्सनयोः", "बोलना, हिंसा करना, ताड़ना, करना", "to say,to kill, to blame, to abuse",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0019", 19, "चर्चँ", "चर्च्",
            "परिभाषणभर्त्सनयोः", "बोलना, निन्दा करना, दोष लगाना, भयभीत करना", "to say,to kill, to blame, to abuse",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0020", 20, "झर्झँ", "झर्झ्",
            "परिभाषणभर्त्सनयोः", "बोलना, निन्दा करना, दोष लगाना, भयभीत करना", "to say,to kill, to blame, to abuse",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0021", 21, "त्वचँ", "त्वच्",
            "संवरणे", "आच्छादित करना, ढांकना", "to cover",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0022", 22, "ऋचँ", "ऋच्",
            "स्तुतौ दीप्तौ च", "स्तुति करना, प्रार्थना करना", "to praise, to pray, to glow, to shine",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0023", 23, "उब्जँ", "उब्ज्",
            "आर्जवे", "सीधी रीति से बर्ताव करना", "to be straight (non-twisted)",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0024", 24, "उज्झँ", "उज्झ्",
            "उत्सर्गे", "छोडना, त्यागना", "to abandon,to leave",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0025", 25, "लुभँ", "लुभ्",
            "विमोहने", "मतिभ्रंश होना, भ्रान्त होना", "to be confused, to lose senses, to act weird",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0026", 26, "रिफँ", "रिफ्",
            "कत्थनयुद्धनिन्दाहिंसादानेषु", "बोलना, युद्ध करना, निंदा करना, देना, दान देना", "to speak, to boast, to fight, to insult, to give",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0027", 27, "रिहँ", "रिह्",
            "कत्थनयुद्धनिन्दाहिंसादानेषु", "", "to speak, to boast, to fight, to insult, to give",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0028", 28, "तृपँ", "तृप्",
            "तृप्तौ", "तृप्त होना, प्रसन्न करना", "to satisfy, to be satisfied, to please, to be pleased",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0029", 29, "ऋहँ", "ऋह्",
            "कत्थनयुद्धनिन्दाहिंसादानेषु", "", "to speak, to boast, to fight, to insult, to give",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0030", 30, "तृफँ", "तृफ्",
            "तृप्तौ", "", "to satisfy, to be satisfied, to please, to be pleased",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0031", 31, "तृन्फँ", "तृम्फ्",
            "तृप्तौ", "तृप्त होना, सन्तुष्ट होना", "to satisfy, to be satisfied, to please, to be pleased",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0032", 32, "तुपँ", "तुप्",
            "हिंसायाम्", "हिंसा करना, मारना", "to kill, to injure, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0033", 33, "तुन्पँ", "तुम्प्",
            "हिंसायाम्", "हिंसा करना, मारना", "to kill, to injure, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0034", 34, "तुफँ", "तुफ्",
            "हिंसायाम्", "मार डालना", "to kill, to injure, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0035", 35, "तुन्फँ", "तुम्फ्",
            "हिंसायाम्", "मार डालना", "to kill, to injure, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0036", 36, "दृपँ", "दृप्",
            "उत्क्लेशे", "पीड़ा करना, दुःख देना", "to give pain, to injure, to torment",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0037", 37, "स्तृन्हूँ", "स्तृंह्",
            "हिंसायाम्", "मार डालना, दुःख देना", "to kill, to destroy, to hurt, to strike",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0038", 38, "दृफँ", "दृफ्",
            "उत्क्लेशे", "", "to give pain, to injure, to torment",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0039", 39, "दृन्फँ", "दृम्फ्",
            "उत्क्लेशे", "पीड़ा करना, दुःख देना", "to give pain, to injure, to torment",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0040", 40, "ऋफँ", "ऋफ्",
            "हिंसायाम्", "हिंसा करना, मार डालना", "to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0041", 41, "ऋन्फँ", "ऋम्फ्",
            "हिंसायाम्", "हिंसा करना, मार डालना", "to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0042", 42, "गुफँ", "गुफ्",
            "ग्रन्थे", "गूंथना, गुम्फन करना, रचना", "to string together, to tie together",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0043", 43, "गुन्फँ", "गुम्फ्",
            "ग्रन्थे", "गूंथना, गुम्फन करना", "to string together, to tie together",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0044", 44, "उभँ", "उभ्",
            "पूरणे", "भरना, पूर्ण करना", "to fill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0045", 45, "उन्भँ", "उम्भ्",
            "पूरणे", "भरना", "to fill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0046", 46, "शुभँ", "शुभ्",
            "शोभायाम्", "सुन्दर होना, शोभायमान होना", "to shine, to look beautiful",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0047", 47, "शुन्भँ", "शुम्भ्",
            "शोभायाम्", "सुन्दर होना, शोभायमान होना", "to shine, to look beautiful",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0048", 48, "दृभीँ", "दृभ्",
            "ग्रन्थे", "गूंथना, रचना", "to string together, to tie together",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0049", 49, "चृतीँ", "चृत्",
            "हिंसाग्रन्थनयोः", "पीड़ा देना, मार डालना, एकत्र करके बांधना, गूंथना", "to kill, to destroy, to hurt, to string together, to tie together, to collect",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0050", 50, "विधँ", "विध्",
            "विधाने", "विधान करना, नियम बनाना", "to mention, to make a rule, to state",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0051", 51, "जुडँ", "जुड्",
            "गतौ बन्धने च", "जाना, बांधना", "to go, to bind",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0052", 52, "जुनँ", "जुन्",
            "गतौ बन्धने च", "", "to go, to bind",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0053", 53, "मृडँ", "मृड्",
            "सुखने", "सुख देना, प्रसन्न करना, सुखी होना", "to make happy,to rejoice,to be happy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0054", 54, "पृडँ", "पृड्",
            "सुखने", "सुख देना", "to make happy,to rejoice,to be happy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0055", 55, "पृणँ", "पृण्",
            "प्रीणने", "आनन्द करना, संतोष पाना", "to be happy, to be contented",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0056", 56, "वृणँ", "वृण्",
            "प्रीणने", "आनन्द करना, संतोष पाना", "to be happy, to be contented",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0057", 57, "मृणँ", "मृण्",
            "हिंसायाम्", "मार डालना", "to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0058", 58, "तुणँ", "तुण्",
            "कौटिल्ये", "टेढ़ा होना, वक्र होना, बुरी निति से वर्तना", "to be crooked, to tilt, to curve, to bend, to ill-behave",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0059", 59, "पुणँ", "पुण्",
            "कर्मणि शुभे", "पवित्र होना, शुद्ध होना, धार्मिक कार्य करना", "to be pure, to be virtuous,to do a holy work",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0060", 60, "मुणँ", "मुण्",
            "प्रतिज्ञाने", "प्रण करना, वचन देना", "to promise, to vow",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0061", 61, "कुणँ", "कुण्",
            "शब्दोपकरणयोः", "शब्द करना, दानादिक से संरक्षण करना, संभालना", "to sound, to gift, to support financially, to take care",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0062", 62, "शुनँ", "शुन्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0063", 63, "द्रुणँ", "द्रुण्",
            "हिंसागतिकौटिल्येषु", "हिंसा करना, टेढ़ा होना, वक्र होना, जाना", "to kill, to destroy, to bend, to curve, to tilt, to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0064", 64, "घुणँ", "घुण्",
            "भ्रमणे", "घूमना, भ्रमण करना", "to move, to roam around",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0065", 65, "घूर्णँ", "घूर्ण्",
            "भ्रमणे", "घूमना", "to move, to roam around",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0066", 66, "षुरँ", "सुर्",
            "ऐश्वर्यदीप्त्योः", "ऐश्वर्यवान होना, प्रकाशित होना", "to rule, to be powerful, to shine",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0067", 67, "कुरँ", "कुर्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0068", 68, "खुरँ", "खुर्",
            "छेदने खण्डने च", "काटना", "to cut, to break",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0069", 69, "मुरँ", "मुर्",
            "संवेष्टने परिवेष्टने च", "घेरना, लपेटना", "to surround, to cover",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0070", 70, "क्षुरँ", "क्षुर्",
            "विलेखने", "कतरना, चीरना, छेड़ना, लकीर खींचना", "to cut, to split, to scratch, to divide, to delineate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0071", 71, "घुरँ", "घुर्",
            "भीमार्थशब्दयोः", "भयंकर होना, शब्द करना, आवाज करना, धुर्राना", "to frighten,to sound, to snore",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0072", 72, "पुरँ", "पुर्",
            "अग्रगमने", "अग्रभाग में जाना, आगे जाना, मुख्य होना, अग्रसर होना", "to proceed,to lead, to go ahead",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0073", 73, "वृहूँ", "वृह्",
            "उद्यमने", "यत्न करना", "to try, to put effort",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0074", 74, "बृहूँ", "बृह्",
            "उद्यमने", "", "to try, to put effort",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0075", 75, "तृहूँ", "तृह्",
            "हिंसायाम्", "मार डालना, दुःख देना", "to kill, to destroy, to hurt, to strike",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0076", 76, "स्तृहूँ", "स्तृह्",
            "हिंसायाम्", "मार डालना, दुःख देना", "to kill, to destroy, to hurt, to strike",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0077", 77, "तृन्हूँ", "तृंह्",
            "हिंसायाम्", "मार डालना, दुःख देना", "to kill, to destroy, to hurt, to strike",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0078", 78, "इषुँ", "इष्",
            "इच्छायाम्", "इच्छा करना, चाहना", "to wish, to desire, to want",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0079", 79, "मिषँ", "मिष्",
            "स्पर्धायाम्", "स्पर्धा करना, होड़ लगाना", "to compete, to contend, to rival",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0080", 80, "किलँ", "किल्",
            "श्वैत्यक्रीडनयोः", "सफ़ेद होना, खेलना", "to become white,to play",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0081", 81, "तिलँ", "तिल्",
            "स्नेहने", "तेल लगाना, सिद्ध होना, चिकना होना", "to oil, to be greasy, to anoint",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0082", 82, "चिलँ", "चिल्",
            "वसने", "कपडे पहनना", "to dress",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0083", 83, "चलँ", "चल्",
            "विलसने विकसने च", "खेलना, क्रीड़ा करना", "to foster,to cherish,to play",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0084", 84, "इलँ", "इल्",
            "स्वप्नक्षेपणयोः", "सोना, नींद लेना, फेंकना, उड़ाना, बिखेरना", "to sleep, to throw, to cast, to throw in air",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0085", 85, "विलँ", "विल्",
            "संवरणे", "वस्त्र पहनना, ओढ़ना, छिद्र करना, चीरना", "to cover,to conceal,to cut, to make a hole",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0086", 86, "बिलँ", "बिल्",
            "भेदने", "भेदन करना, छेद करना", "to break,to split,to divide",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0087", 87, "णिलँ", "निल्",
            "गहने", "कुछ का कुछ समझना, घना होना, जमना, छिप जाना", "to misunderstand,to solidify, to hide, to precipitate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0088", 88, "हिलँ", "हिल्",
            "भावकरणे", "हाव भाव करना, नखरा करना, लीला करना, क्रीड़ा करना", "to act, to show tantrums, to enact",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0089", 89, "शिलँ", "शिल्",
            "उञ्छे", "बीनना, एक एक कर के बीनना", "to glean",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0090", 90, "षिलँ", "सिल्",
            "उञ्छे", "बीनना, एक एक कर के बीनना", "to glean",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0091", 91, "मिलँ", "मिल्",
            "श्लेषणे", "मिलना, संयुक्त होना, जुड़ना", "to join, to unite, to meet",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0092", 92, "लिखँ", "लिख्",
            "अक्षरविन्यासे", "लिखना", "to write,to scratch,to draw, to scribble",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0093", 93, "कुटँ", "कुट्",
            "कौटिल्ये", "टेढ़ा होना, उगाना, फसाना", "to be crooked,to bend, to grow, to cheat",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0094", 94, "पुटँ", "पुट्",
            "संश्लेषणे", "आलिंगन करना, गले लगाना", "to hug, to embrace",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0095", 95, "कुचँ", "कुच्",
            "सङ्कोचने", "आकुंचित होना या करना", "to contract,to shrink, to shorten",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0096", 96, "गुजँ", "गुज्",
            "शब्दे", "शब्द करना, गुंजारव करना", "to hum, to buzz,to sound inarticulately,to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0097", 97, "गुडँ", "गुड्",
            "रक्षायाम्", "संरक्षण करना, बनाना", "to protect, to create",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0098", 98, "डिपँ", "डिप्",
            "क्षेपे", "फेंकना, उड़ाना", "to throw, to throw in air",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0099", 99, "छुरँ", "छुर्",
            "छेदने", "कतरना, तोडना", "to cut,to engrave",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0100", 100, "स्फुटँ", "स्फुट्",
            "विकसने", "खिलना, प्रफुल्लित होना", "to blow,to blossom, to burst,to break open",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0101", 101, "मुटँ", "मुट्",
            "आक्षेपमर्दनयोः", "निन्दा करना, दोष देना, मर्दन करना", "to object, to insult, to blame, to crush, to grid",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0102", 102, "त्रुटँ", "त्रुट्",
            "छेदने", "छेद करना, कतरना, तोडना", "to cut,to break",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0103", 103, "तुटँ", "तुट्",
            "कलहकर्मणि", "विवाद करना, झगड़ा करना", "to quarrel,to argue, to fight",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0104", 104, "चुटँ", "चुट्",
            "छेदने", "छेद करना", "to cut, to pierce",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0105", 105, "छुटँ", "छुट्",
            "छेदने", "छेद करना", "to cut, to pierce",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0106", 106, "जुडँ", "जुड्",
            "बन्धने", "बांधना, जोड़ना", "to join, to tie",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0107", 107, "जुटँ", "जुट्",
            "बन्धने", "", "to join, to tie",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0108", 108, "कडँ", "कड्",
            "मदे", "दुःख व आनन्द में लीन होना", "to be involved in sorrow, to be invoked in happiness",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0109", 109, "लुटँ", "लुट्",
            "संश्लेषणे", "संयोग करना, मिलाप करना, जोड़ना, आलिंगन करना", "to join,to unite, to bring together, to connect",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0110", 110, "लुठँ", "लुठ्",
            "संश्लेषणे", "", "to join,to unite, to bring together, to connect",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0111", 111, "कडँ", "कड्",
            "घसने", "", "to join,to unite, to bring together, to connect",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0112", 112, "कृडँ", "कृड्",
            "घनत्वे", "दृढ या कठिन होना, ज़माना", "to thicken, to solidify",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0113", 113, "कुडँ", "कुड्",
            "बाल्ये", "बालक के समान खेलना, खाना, बटोरना, जमा करना", "to act as a child,to kid, to heap, to collect",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0114", 114, "पुडँ", "पुड्",
            "उत्सर्गे", "त्यागना", "to leave, to quit",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0115", 115, "घुटँ", "घुट्",
            "प्रतिघाते", "मारना, मन मसोस करना, घुटते रहना, प्रतिकार करना", "to strike against,to strike back, to combat, to oppose",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0116", 116, "तुडँ", "तुड्",
            "तोडने", "तोड़ना, कतरना, दुःख देना", "to split,to cut, to to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0117", 117, "थुडँ", "थुड्",
            "संवरणे", "आच्छादित करना, लपेटना", "to cover",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0118", 118, "स्थुडँ", "स्थुड्",
            "संवरणे", "आच्छादित करना, लपेटना", "to cover",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0119", 119, "खुडँ", "खुड्",
            "संवरणे", "", "to cover",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0120", 120, "छुडँ", "छुड्",
            "संवरणे", "", "to cover",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0121", 121, "स्फुरँ", "स्फुर्",
            "सञ्चलने", "हिलना, स्फुरित होना", "to throb, to pulse, to move, to palpitate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0122", 122, "स्फुलँ", "स्फुल्",
            "सञ्चलने", "हिलना, स्फुरित होना", "to throb, to pulse, to move, to palpitate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0123", 123, "स्फरँ", "स्फर्",
            "सञ्चलने", "", "to throb, to pulse, to move, to palpitate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0124", 124, "स्फलँ", "स्फल्",
            "सञ्चलने", "", "to throb, to pulse, to move, to palpitate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0125", 125, "स्फुडँ", "स्फुड्",
            "संवरणे", "वस्त्रादि से वेष्टित करना, लपेटना, आच्छादित करना", "to cover, to conceal",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0126", 126, "चुडँ", "चुड्",
            "संवरणे", "लपेटना", "to cover, to conceal",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0127", 127, "व्रुडँ", "व्रुड्",
            "संवरणे", "डूबना, ढेर करना, ढकना", "to cover, to conceal",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0128", 128, "क्रुडँ", "क्रुड्",
            "निमज्जने", "डूबना", "to sink",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0129", 129, "भृडँ", "भृड्",
            "निमज्जने", "डूबना", "to sink",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0130", 130, "हुडँ", "हुड्",
            "सङ्घाते", "", "to collect,to heap, to pile up",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0131", 131, "गुरीँ", "गुर्",
            "उद्यमने", "प्रयत्न करना, उद्योग करना", "to try, to make effort",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0132", 132, "णू", "नू",
            "स्तवने", "स्तुति करना", "to praise",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0133", 133, "धू", "धू",
            "विधूनने", "कम्पित करना, कांपना", "to shake,to agitate, to tremble",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0134", 134, "गु", "गु",
            "पुरीषोत्सर्गे", "मल त्याग करना, दस्त करना, पैखाना करना", "to excrete",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0135", 135, "ध्रु", "ध्रु",
            "गतिस्थैर्ययोः", "जाना, स्थिर होना", "to be firm,to be fixed, to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0136", 136, "कुङ्", "कु",
            "शब्दे", "शब्द करना, जोर चे चिल्लाना", "to speak loudly, to shout, to chirp",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0137", 137, "कूङ्", "कू",
            "शब्दे", "", "to hum,to moan,to speak inarticulately, to sound",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0138", 138, "पृङ्", "पृ",
            "व्यायामे", "उद्यम करना", "to try, to put effort",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0139", 139, "मृङ्", "मृ",
            "प्राणत्यागे", "मारना, देह त्याग करना", "to die",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0140", 140, "रि", "रि",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0141", 141, "पि", "पि",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0142", 142, "धि", "धि",
            "धारणे", "धारण करना, पास रखना या होना", "to hold,to possess, to become",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0143", 143, "क्षि", "क्षि",
            "निवासगत्योः", "निवास करना, जाना", "to dwell, to stay, to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0144", 144, "षू", "सू",
            "प्रेरणे", "प्रेरणा करना, कार्य में लगाना, उड़ाना", "to inspire, to put to work, to make something fly",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0145", 145, "कॄ", "कॄ",
            "विक्षेपे", "फेंक देना", "to discard, to throw away",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(GrDhatu())
        dhatu(
            "06.0147", 147, "दृङ्", "दृ",
            "आदरे", "आदर करना, सत्कार करना", "to respect, to praise, to honor",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0148", 148, "धृङ्", "धृ",
            "अवस्थाने", "रहना, स्थिर रहना, धारण करना", "to be,to exist,to be steady, to wear",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0149", 149, "प्रछँ", "प्रच्छ्",
            "ज्ञीप्सायाम्", "पूछना, जानने की इच्छा करना", "to ask, to seek, to question",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.DVIKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0150", 150, "सृजँ", "सृज्",
            "विसर्गे", "रचना करना, बनाना", "to create, to generate, to produce",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0151", 151, "टुमस्जोँ", "मज्ज्",
            "शुद्धौ", "स्नान करना, नहाना", "to bathe, to submerge",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0152", 152, "रुजोँ", "रुज्",
            "भङ्गे", "दुःख या रोग से पीड़ित होना, टेढ़ा होना, टूट जाना", "to have pain, to be ill, to bend, to break",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0153", 153, "भुजोँ", "भुज्",
            "कौटिल्ये", "वक्र होना, टेढ़ा होना", "to bend,to curve,to be crooked",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0154", 154, "छुपँ", "छुप्",
            "स्पर्शे", "छूना, स्पर्श करना", "to touch",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0155", 155, "रुशँ", "रुश्",
            "हिंसायाम्", "मार डालना", "to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0156", 156, "रिशँ", "रिश्",
            "हिंसायाम्", "मार डालना", "to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0157", 157, "लिशँ", "लिश्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0158", 158, "स्पृशँ", "स्पृश्",
            "संस्पर्शने", "छूना, स्पर्श करना", "to touch",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0159", 159, "विछँ", "विच्छ्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0160", 160, "विशँ", "विश्",
            "प्रवेशने", "अन्दर प्रवेश करना, घुसना", "to enter",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0161", 161, "मृशँ", "मृश्",
            "आमर्शने", "छूना, स्पर्श करना", "to touch",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0162", 162, "णुदँ", "नुद्",
            "प्रेरणे", "प्रेरणा करना", "to inspire, to urge",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0163", 163, "षदॢँ", "सद्",
            "विशरणगत्यवसादनेषु", "चलना, शक्तिहीन होना, जाना, खिन्न होना", "to go, to lose energy, to move, to be sad",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0164", 164, "शदॢँ", "शद्",
            "शातने", "", "to go, to lose energy, to move, to be sad",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0165", 165, "मिलँ", "मिल्",
            "सङ्गमे", "मिलना, संयुक्त होना, जुड़ना", "to unite, to join, to get together",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0166", 166, "मुचॢँ", "मुच्",
            "मोक्षणे", "मुक्त करना, छोड़ना, त्याग करना", "to free,to liberate,to leave, to release,to loosen,to abandon",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0167", 167, "लुपॢँ", "लुप्",
            "छेदने", "छेद करना, कतरना, चीरना, टुकड़े टुकड़े करना", "to cut, to break, to slice",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0168", 168, "विदॢँ", "विद्",
            "लाभे", "प्राप्त करना", "to obtain, to receive",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0169", 169, "लिपँ", "लिप्",
            "उपदेहे", "लीपना, पोतना, विलेपन करना", "to anoint,to besmear,to cover,to spread over,to stain",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0170", 170, "षिचँ", "सिच्",
            "क्षरणे", "सींचना, छीटा देना", "to sprinkle,to moisten",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "06.0171", 171, "कृतीँ", "कृत्",
            "छेदने", "कतरना, काटना", "to cut, to break, to slice",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0172", 172, "खिदँ", "खिद्",
            "परिघाते", "दुःख देना, सताना", "to distress, to irritate",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0173", 173, "पिशँ", "पिश्",
            "अवयवे", "टुकड़े टुकड़े करना, चीरना, पीसना", "to rip off, to grind, to cut into small parts",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "06.0174", 174, "फुलँ", "फुल्",
            "सञ्चलने", "हिलना, स्फुरित होना", "to throb, to pulse, to move, to palpitate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
    }
}
