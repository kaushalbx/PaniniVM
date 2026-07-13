package dev.sanskrit.dhatupatha

import dev.sanskrit.shiksha.Accent
import dev.sanskrit.shiksha.ItStatus
import dev.sanskrit.shiksha.Karmatva

/** Complete Bhvadi-gaṇa imported from the MIT-shared ashtadhyayi-com data set. */
object BhvadiDhatus {
    val all: List<Dhatu> = dhatuPatha(Gana.BHVADI) {
        dhatu(
            "01.0001", 1, "भू", "भू",
            "सत्तायाम्", "होना", "to exist, to become, to be, to happen",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0002", 2, "एधँ", "एध्",
            "वृद्धौ", "बढना", "to grow, to increase, to prosper, to extend, to swell, to rise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0003", 3, "स्पर्धँ", "स्पर्ध्",
            "सङ्घर्षे", "संघर्ष करना", "to challenge, to compete, to conflict, to rival, to struggle",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0004", 4, "गाधृँ", "गाध्",
            "प्रतिष्ठालिप्सयोर्ग्रन्थे च", "यश प्राप्त करना, लेने की अभिलाषा करना", "to praise oneself, to desire, to tie together, to string together, to seek, to stay together, to stand firmly, to compile",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0005", 5, "बाधृँ", "बाध्",
            "लोडने, रोटने", "रोकना , बाधा देना, दुःख देना", "to oppress, to torment, to harass, to destroy, to infect, to torture",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0006", 6, "नाधृँ", "नाध्",
            "याच्ञोपतापैश्वर्याशीष्षु", "याचना करना, रोगी होना, श्रीमान् होना, आशीर्वाद देना", "to seek help, to beg, to irritate, to bless, to master",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0007", 7, "नाथृँ", "नाथ्",
            "याच्ञोपतापैश्वर्याशीष्षु", "याचना करना, रोगी होना, श्रीमान् होना, आशीर्वाद देना", "to seek help, to beg, to irritate, to bless, to master",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0008", 8, "दधँ", "दध्",
            "धारणे", "धारण करना, पालन करना, देना, अर्पण करना", "to hold, to possess",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0009", 9, "स्कुदिँ", "स्कुन्द्",
            "आप्रवणे", "चलना, कूदना, ऊपर उठाना", "to jump, to raise, to lift up",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0010", 10, "श्विदिँ", "श्विन्द्",
            "श्वैत्ये", "सफेद होना, शुभ्र होना", "to be white",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0011", 11, "वदिँ", "वन्द्",
            "अभिवादनस्तुत्योः", "सत्कारपूर्वक कुशल प्रश्न पूछना, नमस्कार करना, स्तुति करना", "to worship, to show honor, to do homage, to show respect, to venerate, to bow, to salute, to praise, to adore, to greet",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0012", 12, "भदिँ", "भन्द्",
            "कल्याणे सुखे च", "शुभ कर्म करना, सुखी होना", "to be prosperous, to be glad, to be happy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0013", 13, "मदिँ", "मन्द्",
            "स्तुतिमोदमदस्वप्नकान्तिगतिषु", "स्तुति करना, तुष्ट करना, आनन्द करना, उन्मत्त होना, सोना, चाहना, चमकना, प्रकाशित होना", "to praise, to be glad, to be proud, to sleep, to shine, to move slowly, to intoxicate",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0014", 14, "स्पदिँ", "स्पन्द्",
            "किञ्चिच्चलने", "धीरे धीरे चलना, काँपना, थरथराना", "to vibrate, to shake, to move a little, to palpitate, to quake, to tremble, to kick",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0015", 15, "क्लिदिँ", "क्लिन्द्",
            "परिदेवने", "रोना, शोक करना", "to lament",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0016", 16, "मुदँ", "मुद्",
            "हर्षे", "आनन्दित होना, प्रसन्न होना", "to be glad,to be happy, to rejoice, to delight",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0017", 17, "ददँ", "दद्",
            "दाने", "दान करना, देना, त्याग करना", "to give,to offer,to present",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0018", 18, "ष्वदँ", "स्वद्",
            "आस्वादने", "स्वाद लेना, चखना", "to taste, to eat,to please the tongue, to have delight",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0019", 19, "स्वर्दँ", "स्वर्द्",
            "आस्वादने", "स्वाद लेना, चखना", " to taste,to eat,to please the tongue, to have delight",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0020", 20, "उर्दँ", "ऊर्द्",
            "माने क्रीडायां आस्वादने च", "नापना, गिनना, क्रीडा करना, खेलना", "to measure,to leap, to jump, to taste,to grant,to be cheerful",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0021", 21, "कुर्दँ", "कूर्द्",
            "क्रीडायाम् एव", "खेलना", "to leap, to jump",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0022", 22, "खुर्दँ", "खूर्द्",
            "क्रीडायाम् एव", "खेलना", "to leap, to jump",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0023", 23, "गुर्दँ", "गूर्द्",
            "क्रीडायाम् एव", "खेलना", "to leap, to jump",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0024", 24, "गुदँ", "गुद्",
            "क्रीडायाम् एव", "खेलना", "to leap, to jump",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0025", 25, "षूदँ", "सूद्",
            "क्षरणे", "टपकना, झरना, मार डालना, मारने की कोशिश करना", "to trickle, to leak, to ooze out, to distill, to emerge drop by drop",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0026", 26, "ह्रादँ", "ह्राद्",
            "अव्यक्ते शब्दे", "अस्पष्ट शब्द करना", "to make a sound",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0027", 27, "ह्लादीँ", "ह्लाद्",
            "अव्यक्ते शब्दे सुखे च", "सुखी होना, प्रयत्न करना, अस्पष्ट शब्द करना", "to be  glad, to be pleasant, to make a sound",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0028", 28, "स्वादँ", "स्वाद्",
            "आस्वादने", "स्वाद लेना, चखना", "to taste,to please the tongue, to have delight",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0029", 29, "पर्दँ", "पर्द्",
            "कुत्सिते शब्दे", "वायु त्याग करना", "to fart",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0030", 30, "यतीँ", "यत्",
            "प्रयत्ने", "प्रयत्न करना, उद्योग करना", "to try, to strive, to make effort, to attempt",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0031", 31, "युतृँ", "युत्",
            "भासने", "चमकना, प्रकाशित होना", "to shine",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0032", 32, "जुतृँ", "जुत्",
            "भासने", "चमकना, प्रकाशित होना", "to shine",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0033", 33, "विथृँ", "विथ्",
            "याचने", "मांगना, याचना करना", "to beg,to ask",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.DVIKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0034", 34, "वेथृँ", "वेथ्",
            "याचने", "मांगना, याचना करना", "to beg,to ask",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.DVIKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0035", 35, "श्रथिँ", "श्रन्थ्",
            "शैथिल्ये", "शिथिल करना, ढीला करना, शिथिल होना, ढीला होना", "to be tired, to be fatigued",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0036", 36, "ग्रथिँ", "ग्रन्थ्",
            "कौटिल्ये", "वक्र होना, टेढा होना, दुष्ट होना, गांठ बांधना, गूथना", "to be crooked, to be wicked",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0037", 37, "कत्थँ", "कत्थ्",
            "श्लाघायाम्", "प्रशंसा करना, स्तुति करना, झूटी बढाई करना", "to praise,to boast,to flatter",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0038", 38, "अतँ", "अत्",
            "सातत्यगमने", "जाना, सदैव जाते रहना", "to run, to walk with speed, to move continuously",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0039", 39, "चितीँ", "चित्",
            "संज्ञाने", "अच्छी तरह विचार करना, चिंतन करना, होश में आना", "to perceive,to think,to recover consciousness,to observe, to see,to regain consciousness",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0040", 40, "च्युतिँर्", "च्युत्",
            "आसेचने", "सींचना, भीगोना, बहना", "to sprinkle, to flow,to trickle, to fall, to deviate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0041", 41, "श्चुतिँर्", "श्चुत्",
            "आसेचने", "", "to flow,to trickle, to fall, to deviate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0042", 42, "श्च्युतिँर्", "श्च्युत्",
            "क्षरणे", "टपकना, झरना, सींचना, प्रोक्षण करना, छिडकना", "to flow,to trickle, to fall, to deviate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0043", 43, "ज्युतृँ", "ज्युत्",
            "भासने", "", "to illumine, to shine",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0044", 44, "मथिँ", "मन्थ्",
            "हिंसासङ्क्लेशनयोः", "मार डालना, दुःख देना, दुःख भोगना, पीडित होना", "to strike,to destroy, to kill, to crush, to tear off, to injure, to cause pain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0045", 45, "कुथिँ", "कुन्थ्",
            "हिंसासङ्क्लेशनयोः", "मार डालना, दुःख देना, दुःख भोगना, पीडित होना", "to strike,to destroy, to kill, to crush, to tear off, to injure, to cause pain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0046", 46, "पुथिँ", "पुन्थ्",
            "हिंसासङ्क्लेशनयोः", "मार डालना, दुःख देना, दुःख भोगना, पीडित होना", "to strike,to destroy, to kill, to crush, to tear off, to injure, to cause pain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0047", 47, "लुथिँ", "लुन्थ्",
            "हिंसासङ्क्लेशनयोः", "मार डालना, दुःख देना, दुःख भोगना, पीडित होना", "to strike,to destroy, to kill, to crush, to tear off, to injure, to cause pain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0048", 48, "मन्थँ", "मन्थ्",
            "विलोडने", "बिलोना, मथना, पीड़ा देना", "to churn, to shake, to stir, to strike,to agitate, to splash",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.DVIKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0049", 49, "षिधँ", "सिध्",
            "गत्याम्", "जाना", "to regulate, to punish, to hinder, to instruct, to scare away, to drive off, to restrain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0050", 50, "षिधूँ", "सिध्",
            "शास्त्रे (शासने) माङ्गल्ये च", "आज्ञा करना, शासन करना, मङ्गल कर्म करना", "to rule, to be auspicious",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0051", 51, "खादृँ", "खाद्",
            "भक्षणे", "खाना", "to eat,to consume",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0052", 52, "खदँ", "खद्",
            "स्थैर्ये हिंसायां भक्षणे च", "स्थिर रहना,मार डालना, सताना, खाना", "to be firm,to be steady, to hurt, to kill, to eat, to consume, to bite",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0053", 53, "बदँ", "बद्",
            "स्थैर्ये", "स्थिर रहना", "to be firm, to be steady",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0054", 54, "गदँ", "गद्",
            "व्यक्तायां वाचि", "स्पष्ट बोलना", "to speak, to tell, to explain, to talk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0055", 55, "रदँ", "रद्",
            "विलेखने", "विदारण करना, चीरना, खोदना", "to cut, to split, to scratch, to divide",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0056", 56, "णदँ", "नद्",
            "अव्यक्ते शब्दे", "अस्पष्ट शब्द करना, आवाज करना", "to thunder, to roar, to make a loud sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0057", 57, "अर्दँ", "अर्द्",
            "गतौ याचने च", "जाना,मांगना, याचना करना,मारना", "to go,to beg, to request",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0058", 58, "नर्दँ", "नर्द्",
            "शब्दे", "शब्द करना", "to make a sound, to shout",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0059", 59, "गर्दँ", "गर्द्",
            "शब्दे", "शब्द करना", "to make a sound, to shout",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0060", 60, "तर्दँ", "तर्द्",
            "हिंसायाम्", "मारना, हिंसा करना", "to injure,to hurt,to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0061", 61, "कर्दँ", "कर्द्",
            "कुत्सिते शब्दे", "कौवे के समान शब्द करना, पेट गुड़गुड़ाना, अन्त्रकुजन होना", "to rumble,to caw",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0062", 62, "खर्दँ", "खर्द्",
            "दन्दशूके (सर्पदंशे)", "चबाना, दांतों से काटना", "to bite,to sting",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0063", 63, "अतिँ", "अन्त्",
            "बन्धने", "बांधना, पाना, हासिल करना", "to bind",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0064", 64, "अदिँ", "अन्द्",
            "बन्धने", "बांधना, पाना, हासिल करना", "to bind",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0065", 65, "इदिँ", "इन्द्",
            "परमैश्वर्ये", "अमानवीय पराक्रम होना,ईश्वरी शक्ति होना, ऐश्वर्य युक्त होना", "to be powerful, to be gifted, to have divine power",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0066", 66, "बिदिँ", "बिन्द्",
            "अवयवे", "टुकड़े होना, विभक्त होना, अलग hona", "to split,to cleave,to divide",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0067", 67, "भिदिँ", "भिन्द्",
            "अवयवे", "", "to split,to cleave,to divide",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0068", 68, "गडिँ", "गण्ड्",
            "वदनैकदेशे", "गालोंमें रोग होना, गण्डमाला होना", "to have cheek disease",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0069", 69, "णिदिँ", "निन्द्",
            "कुत्सायाम्", "निन्दा करना, दोष लगाना", "to blame,to censure,to condemn, to insult, to ridicule, to revile, to criticise",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0070", 70, "टुनदिँ", "नन्द्",
            "समृद्धौ", "वृद्धि होना, बढ़ती होना", "to prosper",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0071", 71, "चदिँ", "चन्द्",
            "आह्लादे दीप्तौ च", "आनन्द पाना, खुश होना, चमकना", "to be glad, to shine",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0072", 72, "त्रदिँ", "त्रन्द्",
            "चेष्टायाम्", "प्रयत्न करना, उद्यम करना", "to try, to be busy, to be active",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0073", 73, "कदिँ", "कन्द्",
            "आह्वाने रोदने च", "पुकारना, रोना", "to call out,to cry,to cry out continually,to lament,to grieve, to shed tears",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0074", 74, "क्रदिँ", "क्रन्द्",
            "आह्वाने रोदने च", "पुकारना, रोना", "to call out,to cry,to cry out continually,to lament,to grieve, to shed tears",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0075", 75, "क्लदिँ", "क्लन्द्",
            "आह्वाने रोदने च", "पुकारना, रोना", "to call out,to cry,to cry out continually,to lament,to grieve, to shed tears",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0076", 76, "क्लिदिँ", "क्लिन्द्",
            "परिदेवने", "रोना, शोक करना", "to lament to grieve, to shed tears, to have sorrow",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0077", 77, "शुन्धँ", "शुन्ध्",
            "शुद्धौ", "शुद्ध होना, पवित्र होना", "to purify, to cleanse",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0078", 78, "शीकृँ", "शीक्",
            "सेचने", "सींचना", "to sprinkle",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0079", 79, "सीकृँ", "सीक्",
            "सेचने", "", "to sprinkle",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0080", 80, "लोकृँ", "लोक्",
            "दर्शने", "देखना", "to see,to perceive,to look",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0081", 81, "श्लोकृँ", "श्लोक्",
            "सङ्घाते", "श्लोक बनाना, कविता करना, रचना करना", "to compose, to write poem",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0082", 82, "श्रेकृँ", "श्रेक्",
            "गतौ", "", "to compose, to write poem",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0083", 83, "द्रेकृँ", "द्रेक्",
            "शब्दोत्साहयोः", "शब्द करना, उत्साह करना, आनन्द प्रकट करना", "to sound,to show joy, to be exhilarated",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0084", 84, "ध्रेकृँ", "ध्रेक्",
            "शब्दोत्साहयोः", "शब्द करना, उत्साह करना, आनन्द प्रकट करना", "to sound,to show joy, to be exhilarated",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0085", 85, "रेकृँ", "रेक्",
            "शङ्कायाम्", "शंका करना", "to doubt, to suspect",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0086", 86, "सेकृँ", "सेक्",
            "गतौ", "जाना", "to go,to move",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0087", 87, "स्रेकृँ", "स्रेक्",
            "गतौ", "जाना", "to go,to move",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0088", 88, "स्रकिँ", "स्रङ्क्",
            "गतौ", "जाना", "to go,to move",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0089", 89, "श्रकिँ", "श्रङ्क्",
            "गतौ", "जाना", "to go,to move",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0090", 90, "श्लकिँ", "श्लङ्क्",
            "गतौ", "जाना", "to go,to move",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0091", 91, "शकिँ", "शङ्क्",
            "शङ्कायाम्", "शंका करना, संशय करना", "to doubt,to suspect",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0092", 92, "अकिँ", "अङ्क्",
            "लक्षणे", "चिह्न करना, टेढा जाना", "to go, to blame,to censure,to condemn, to insult, to ridicule, to revile, to criticise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0093", 93, "वकिँ", "वङ्क्",
            "कौटिल्ये", "वक्र होना, टेढा होना, दुष्टता करना, नमना, टेढ़ा जाना", "to be crooked, to bend, to move in curve",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0094", 94, "मकिँ", "मङ्क्",
            "मण्डने", "संवारना, अलंकृत करना", "to decorate",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0095", 95, "ककँ", "कक्",
            "लौल्ये", "गर्व करना, चञ्चल होना, प्यासा होना", "to be proud,to be unsteady,to be thirsty",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0096", 96, "कुकँ", "कुक्",
            "आदाने", "लेना", "to take,to accept,to seize",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0097", 97, "वृकँ", "वृक्",
            "आदाने", "लेना", "to accept,to seize",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0098", 98, "चकँ", "चक्",
            "तृप्तौ प्रतीघाते च", "तृप्त होना, संतुष्ट होना, चकमा देना, धोखा देना", "to be satisfied, to be contended, to repel, to cheat, to betray",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0099", 99, "ककिँ", "कङ्क्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0100", 100, "वकिँ", "वङ्क्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0101", 101, "श्वकिँ", "श्वङ्क्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0102", 102, "त्रकिँ", "त्रङ्क्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0103", 103, "ढौकृँ", "ढौक्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0104", 104, "त्रौकृँ", "त्रौक्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0105", 105, "ष्वष्कँ", "ष्वष्क्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0106", 106, "वस्कँ", "वस्क्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0107", 107, "मस्कँ", "मस्क्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0108", 108, "टिकृँ", "टिक्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0109", 109, "टीकृँ", "टीक्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0110", 110, "तिकृँ", "तिक्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0111", 111, "तीकृँ", "तीक्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0112", 112, "रघिँ", "रङ्घ्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0113", 113, "लघिँ", "लङ्घ्",
            "गतौ भोजननिवृत्तौ च", "जाना, उपवास करना", "to go, to fast, to abstain from food",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0114", 114, "ष्वक्कँ", "ष्वक्क्",
            "गतौ", "", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0115", 115, "अघिँ", "अङ्घ्",
            "गत्याक्षेपे", "गति करना, आरम्भ करना, निन्दा करना", "to set out, to go, to commence, to blame",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0116", 116, "वघिँ", "वङ्घ्",
            "गत्याक्षेपे", "जाना, निन्दा करना", "to set out, to go, to commence, to blame",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0117", 117, "मघिँ", "मङ्घ्",
            "गत्याक्षेपे गत्यारम्भे कैतवे च", "जाना, निन्दा करना", "to set out, to go, to commence, to blame, to cheat",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0118", 118, "राघृँ", "राघ्",
            "सामर्थ्ये", "समर्थ होना, योग्य होना", "to be competent, to be able, to be powerful",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0119", 119, "लाघृँ", "लाघ्",
            "सामर्थ्ये", "समर्थ होना", "to be competent, to be able, to be powerful",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0120", 120, "द्राघृँ", "द्राघ्",
            "सामर्थ्ये आयामे च", "शक्तिमान होना, लम्बा करना, तानना", "to be competent, to be able, to be powerful, to stretch, to spread",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0121", 121, "ध्राघृँ", "ध्राघ्",
            "सामर्थ्ये", "", "to be competent, to be able, to be powerful",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0122", 122, "श्लाघृँ", "श्लाघ्",
            "कत्थने", "प्रशंसा करना, आत्म स्तुति करना, फुसलाना", "to praise,to flatter,to boast",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0123", 123, "फक्कँ", "फक्क्",
            "नीचैर्गतौ", "धीरे धीरे जाना, रेंगना, खराब आचरण करना", "to go slowly, to misbehave",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0124", 124, "तकँ", "तक्",
            "हसने", "हसना, उपहास करना, मज़ाक उडाना", "to make fun, to laugh at",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0125", 125, "तकिँ", "तङ्क्",
            "कृच्छ्रजीवने", "दुःख से, तंगी से जीवन बिताना", "to live in distress",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0126", 126, "बुक्कँ", "बुक्क्",
            "भषणे", "भौंकना, कुत्ते के समान शब्द करना", "to bark",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0127", 127, "शुकँ", "शुक्",
            "गतौ", "", "to move, to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0128", 128, "कखँ", "कख्",
            "हसने", "हसना", "to laugh",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0129", 129, "ओखृँ", "ओख्",
            "शोषणालमर्थयोः", "शुष्क होना, सूखना, कान्तिमान होना, अलंकृत करना", "to dry out, to decorate, to be sufficient, to refuse, to prevent, to stop",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0130", 130, "राखृँ", "राख्",
            "शोषणालमर्थयोः", "सूखना, शुष्क होना, भूषित करना, कार्यक्षम होना, रोकना, निषेध करना", "to dry out, to decorate, to be sufficient, to refuse, to prevent, to stop",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0131", 131, "लाखृँ", "लाख्",
            "शोषणालमर्थयोः", "सूखना, शुष्क होना, भूषित करना, कार्यक्षम होना, रोकना, निषेध करना", "to dry out, to decorate, to be sufficient, to refuse, to prevent, to stop",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0132", 132, "द्राखृँ", "द्राख्",
            "शोषणालमर्थयोः", "सूखना, शुष्क होना, भूषित करना, कार्यक्षम होना, रोकना, निषेध करना", "to dry out, to decorate, to be sufficient, to refuse, to prevent, to stop",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0133", 133, "ध्राखृँ", "ध्राख्",
            "शोषणालमर्थयोः", "सूखना, शुष्क होना, भूषित करना, कार्यक्षम होना, रोकना, निषेध करना", "to dry out, to decorate, to be sufficient, to refuse, to prevent, to stop",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0134", 134, "शाखृँ", "शाख्",
            "व्याप्तौ", "शाखा फैलना, डालें पैदा होना", "to pervade, to spread, to penetrate, to occupy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0135", 135, "श्लाखृँ", "श्लाख्",
            "व्याप्तौ", "व्याप्त होना, फैलना", "to pervade,to spread, to penetrate, to occupy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0136", 136, "उखँ", "उख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0137", 137, "उखिँ", "उङ्ख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0138", 138, "वखँ", "वख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0139", 139, "वखिँ", "वङ्ख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0140", 140, "मखँ", "मख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0141", 141, "मखिँ", "मङ्ख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0142", 142, "णखँ", "नख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0143", 143, "णखिँ", "नङ्ख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0144", 144, "रखँ", "रख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0145", 145, "रखिँ", "रङ्ख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0146", 146, "लखँ", "लख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0147", 147, "लखिँ", "लङ्ख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0148", 148, "इखँ", "इख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0149", 149, "इखिँ", "इङ्ख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0150", 150, "जभँ", "जभ्",
            "गात्रविनामे", "जम्हाई लेना", "to yawn,to gape",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0151", 151, "ईखिँ", "ईङ्ख्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0152", 152, "वल्गँ", "वल्ग्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0153", 153, "रगिँ", "रङ्ग्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0154", 154, "लगिँ", "लङ्ग्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0155", 155, "अगिँ", "अङ्ग्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0156", 156, "वगिँ", "वङ्ग्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0157", 157, "मगिँ", "मङ्ग्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0158", 158, "तगिँ", "तङ्ग्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0159", 159, "त्वगिँ", "त्वङ्ग्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0160", 160, "श्वेलृँ", "श्वेल्",
            "चलने", "चलना, जाना", "to go, walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0161", 161, "श्रगिँ", "श्रङ्ग्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0162", 162, "श्लगिँ", "श्लङ्ग्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0163", 163, "इगिँ", "इङ्ग्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0164", 164, "रिगिँ", "रिङ्ग्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0165", 165, "लिगिँ", "लिङ्ग्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0166", 166, "खर्खँ", "खर्ख्",
            "हसने", "", "to laugh",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0167", 167, "कक्ख", "कक्ख्",
            "हसने", "", "to laugh",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0168", 168, "रिखँ", "रिख्",
            "गतौ", "", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0169", 169, "घर्बँ", "घर्ब्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0170", 170, "नर्बँ", "नर्ब्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0171", 171, "भर्बँ", "भर्ब्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0172", 172, "त्रखँ", "त्रख्",
            "गतौ", "", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0173", 173, "त्रिखिँ", "त्रिङ्ख्",
            "गतौ", "", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0174", 174, "शिखिँ", "शिङ्ख्",
            "गतौ", "", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0175", 175, "युगिँ", "युङ्ग्",
            "वर्जने", "त्याग देना, छोड़ देना", "to abandon,to desert, to leave",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0176", 176, "जुगिँ", "जुङ्ग्",
            "वर्जने", "त्याग देना", "to abandon,to desert, to leave",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0177", 177, "बुगिँ", "बुङ्ग्",
            "वर्जने", "त्याग देना", "to abandon,to desert, to leave",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0178", 178, "वुगिँ", "वुङ्ग्",
            "वर्जने", "", "to abandon,to desert, to leave",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0179", 179, "घघँ", "घघ्",
            "हसने", "हसना, अहसास करना", "to laugh, to tease",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0180", 180, "घग्घँ", "घग्घ्",
            "हसने", "", "to laugh, to tease",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0181", 181, "वर्बँ", "वर्ब्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0182", 182, "बभ्रँ", "बभ्र्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0183", 183, "मघिँ", "मङ्घ्",
            "मण्डने", "संवारना, भूषित करना, अलंकृत करना", "to decorate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0184", 184, "शिघिँ", "शिङ्घ्",
            "आघ्राणे", "सूंघना", "to smell",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0185", 185, "मजँ", "मज्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0186", 186, "वर्चँ", "वर्च्",
            "दीप्तौ", "प्रकाशित होना, चमकना", "to shine, to glow",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0187", 187, "षचँ", "सच्",
            "सेचने सेवने च", "सींचना, गीला करना, सेवा करना, सेवा करके संतुष्ट रहना", "to sprinkle,to serve, to satisfy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0188", 188, "लोचृँ", "लोच्",
            "दर्शने", "देखना", "to see,to perceive",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0189", 189, "शचँ", "शच्",
            "व्यक्तायां वाचि", "स्पष्ट बोलना", "to articulate, to speak clearly",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0190", 190, "श्वचँ", "श्वच्",
            "गतौ", "जाना", "to go, to move",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0191", 191, "श्वचिँ", "श्वञ्च्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0192", 192, "कचँ", "कच्",
            "बन्धने", "बांधना", "to bind, to tie",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0193", 193, "कचिँ", "कञ्च्",
            "दीप्तिबन्धनयोः", "बांधना, चमकना, प्रकाशित होना", "to shine, to glow, to bind, to tie",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0194", 194, "काचिँ", "काञ्च्",
            "दीप्तिबन्धनयोः", "चमकना, बांधना", "to shine, to glow, to bind, to tie",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0195", 195, "मचँ", "मच्",
            "कल्कने कथने च", "गर्व करना, दुराचारी होना, पीसना, कूटना", "to boast, to be egoistic, to cheat, to tear off, to powder, to speak, to talk",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0196", 196, "मुचिँ", "मुञ्च्",
            "कल्कने कथने च", "कहना, बोलना, पीसना, कूटना, ढगना, गर्व करना", "to boast, to be egoistic, to cheat, to tear off, to powder, to speak, to talk",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0197", 197, "मचिँ", "मञ्च्",
            "धारणोच्छ्रायपूजनेषु", "धारण करना, ऊंचा उठाना, मचान बनाना, पूजित होना", "to wear, to hold, to raise, to get respect",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0198", 198, "पचिँ", "पञ्च्",
            "व्यक्तीकरणे", "स्पष्ट करना", "to articulate, to speak clearly",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0199", 199, "ष्टुचँ", "स्तुच्",
            "प्रसादे", "संतुष्ट होना, प्रसन्न होना", "to be pleased, to be happy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0200", 200, "ऋजँ", "ऋज्",
            "गतिस्थानार्जनोपार्जनेषु", "जाना, खड़ा रहना, स्थिर होना, वलिष्ट होना, जीना, संपादन करना, प्राप्त करना, मिलाना", "to go, to stand, to stand still, to gain, to live, to be healthy, to earn, to obtain, to acquire",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0201", 201, "ऋजिँ", "ऋञ्ज्",
            "भर्जने", "भूजना", "to fry, to parch",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0202", 202, "भृजीँ", "भृज्",
            "भर्जने", "भूंजना", "to fry, to parch",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0203", 203, "एजृँ", "एज्",
            "दीप्तौ", "प्रकाशित होना, कान्तिमान होना", "to glow, to shine",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0204", 204, "भ्रेजृँ", "भ्रेज्",
            "दीप्तौ", "प्रकाशित होना", "to glow, to shine",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0205", 205, "भ्राजृँ", "भ्राज्",
            "दीप्तौ", "चमकना, प्रकाशित होना", "to glow, to shine",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0206", 206, "काडृँ", "काड्",
            "अनादरे", "अपमान करना", "to disregard,to insult",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0207", 207, "ईजँ", "ईज्",
            "गतिकुत्सनयोः", "जाना, दोष लगाना, निन्दा करना", "to go, to blame,to censure,to condemn, to insult, to ridicule, to revile, to criticise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0208", 208, "पेबृ", "पेब्",
            "सेवने", "सेवा करना, चाकरी करना", "to serve, to devote oneself, practise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0209", 209, "प्लेबृँ", "प्लेब्",
            "सेवने", "", "to serve, to devote oneself, practise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0210", 210, "शुचँ", "शुच्",
            "शोके", "चिन्ता करना, शोक करना", "to worry, to sorrow, to grieve",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0211", 211, "कुचँ", "कुच्",
            "शब्दे तारे", "पक्षी के सामान जोर से शब्द करना", "to utter a shrill cry, to make a birdlike sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0212", 212, "कुन्चँ", "कुञ्च्",
            "गतिकौटिल्याल्पीभावयोः", "जाना, टेढा होना, टेढा करना, अल्प होना, अल्प करना", "to go, to bend, to reduce, to contract, to diminish",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0213", 213, "क्रुन्चँ", "क्रुञ्च्",
            "गतिकौटिल्याल्पीभावयोः", "जाना, टेढा होना, टेढा करना, अल्प होना, अल्प करना", "to go, to bend, to reduce, to contract, to diminish",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0214", 214, "लुन्चँ", "लुञ्च्",
            "अपनयने", "कतरना, चीरना, तोडना, छीलना, छाल निकलना, बाल उखाडना", "to cut, to break, to peel off, to pluck, to pull out, to tear off",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0215", 215, "अन्चुँ", "अञ्च्",
            "गतिपूजनयोः", "जाना, पूजा करना", "to go, to pray, to honor, to worship, to respect",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0216", 216, "वन्चुँ", "वञ्च्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0217", 217, "चन्चुँ", "चञ्च्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0218", 218, "तन्चुँ", "तञ्च्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0219", 219, "त्वन्चुँ", "त्वञ्च्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0220", 220, "म्रुन्चुँ", "म्रुञ्च्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0221", 221, "म्लुन्चुँ", "म्लुञ्च्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0222", 222, "म्रुचुँ", "म्रुच्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0223", 223, "म्लुचुँ", "म्लुच्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0224", 224, "ग्रुचुँ", "ग्रुच्",
            "स्तेयकरणे", "चोरी करना", "to rob,to steal",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0225", 225, "ग्लुचुँ", "ग्लुच्",
            "स्तेयकरणे", "चोरी करना", "to rob,to steal",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0226", 226, "कुजुँ", "कुज्",
            "स्तेयकरणे", "चोरी करना", "to rob,to steal",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0227", 227, "खुजुँ", "खुज्",
            "स्तेयकरणे", "चोरी करना", "to rob,to steal",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0228", 228, "ग्लुन्चुँ", "ग्लुञ्च्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0229", 229, "षस्जँ", "सज्ज्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0230", 230, "गुजँ", "गुज्",
            "अव्यक्ते शब्दे", "", "to hum, to buzz,to sound inarticulately, to utter unclearly",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0231", 231, "गुजिँ", "गुञ्ज्",
            "अव्यक्ते शब्दे", "अस्पष्ट बोलना, गुंजारव करना", "to hum, to buzz,to sound inarticulately, to utter unclearly",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0232", 232, "अर्चँ", "अर्च्",
            "पूजायाम्", "पूजा करना, मान करना", "to pray, to honor, to worship, to respect",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0233", 233, "म्लेछँ", "म्लेच्छ्",
            "अव्यक्ते शब्दे", "अस्पष्ट शब्द करना, असम्बद्ध भाषण करना", "to speak indistinctly, to murmur, to blabber",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0234", 234, "लछँ", "लच्छ्",
            "लक्षणे", "चिह्न करना, निशान करना", "to mark",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0235", 235, "लाछिँ", "लाञ्छ्",
            "लक्षणे", "चिह्न करना, निशान करना", "to mark",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0236", 236, "वाछिँ", "वाञ्छ्",
            "इच्छायाम्", "इच्छा करना, चाहना", "to want, to wish, to desire",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0237", 237, "आछिँ", "आञ्छ्",
            "आयामे", "बढ़ाना, लम्बा करना", "to elongate, to expand, to spread, to dialate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0238", 238, "ह्रीछँ", "ह्रीच्छ्",
            "लज्जायाम्", "लज्जा करना", "to be shy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0239", 239, "हुर्छाँ", "हूर्छ्",
            "कौटिल्ये", "गिरना, दूर हटना", "to fall off, to move away, to retract",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0240", 240, "मुर्छाँ", "मूर्छ्",
            "मोहसमुच्छ्राययोः", "मूर्च्छित होना, मुरझाना", "to faint,to be unconscious, to be shy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0241", 241, "स्फुर्छाँ", "स्फूर्छ्",
            "विस्तृतौ", "विस्तार करना", "to expand, to spread",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0242", 242, "युछँ", "युच्छ्",
            "प्रमादे", "दुर्लक्ष्य करना, असावधान रहना, प्रमाद करना", "to err,to neglect, to ignore, to make a mistake",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0243", 243, "उछिँ", "उञ्छ्",
            "उञ्छे", "थोड़ा थोड़ा एकत्र करना, थोड़ा थोड़ा बटोरना, बीनना", "to collect piece by piece, to gather, to accumulate, to pile up",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0244", 244, "उछीँ", "उच्छ्",
            "विवासे", "पूरा करना, समाप्त करना, छोड़ना, त्यागना", "to complete, to terminate, to conclude, to leave, to go away",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0245", 245, "ध्रजँ", "ध्रज्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0246", 246, "ध्रजिँ", "ध्रञ्ज्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0247", 247, "अटिँ", "अण्ट्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0248", 248, "रेबृँ", "रेब्",
            "प्लवगतौ", "उड़कर जाना, तैरकर पार करना", "to leap, to jump, to cross by flying, to cross by swimming",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0249", 249, "धृजँ", "धृज्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0250", 250, "धृजिँ", "धृञ्ज्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0251", 251, "ध्वजँ", "ध्वज्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0252", 252, "ध्वजिँ", "ध्वञ्ज्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0253", 253, "षलृँ", "सल्",
            "गतौ", "", "to go, walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0254", 254, "कूजँ", "कूज्",
            "अव्यक्ते शब्दे", "अस्पष्ट शब्द करना, कूजना", "to sing, to make an indistinct sound, to speak inarticulately",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0255", 255, "भृषुँ", "भृष्",
            "सङ्घर्षे", "लड़ाई करना", "to grind,to strike,to rub,to brush,to polish",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0256", 256, "अर्जँ", "अर्ज्",
            "अर्जने", "सम्पादन करना, पाना, पैदा करना", "to procure,to earn, to obtain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0257", 257, "षर्जँ", "सर्ज्",
            "अर्जने", "उपार्जन करना, प्राप्त करना", "to procure,to earn, to obtain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0258", 258, "गर्जँ", "गर्ज्",
            "शब्दे", "शब्द करना, गरजना", "to roar,to growl,to thunder",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0259", 259, "तर्जँ", "तर्ज्",
            "भर्त्सने", "डराना", "to menace,to censure,to threaten,to terrify,to scold,to mock",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0260", 260, "कर्जँ", "कर्ज्",
            "व्यथने", "दुःख देना", "to cause pain, to irritate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0261", 261, "खर्जँ", "खर्ज्",
            "व्यथने पूजने मार्जने च", "दुःख देना, सताना, आतिश पूजन करना, सम्मान करना", "to cause pain, to irritate, to worship, to respect",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0262", 262, "अजँ", "अज्",
            "गतिक्षेपणयोः", "जाना, हांकना, दौड़ाना, फेंकना", "to go,to drive,to throw,to ride",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0263", 263, "तेजँ", "तेज्",
            "पालने", "पालन करना, रक्षा करना", "to protect",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0264", 264, "खजँ", "खज्",
            "मन्थे", "मथना, हिलाना, मंथन करना", "to churn,to agitate,to limp",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0265", 265, "कवँ", "कव्",
            "शब्दे", "", "to be intoxicated, to be drunk, to be confused",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0266", 266, "खजिँ", "खञ्ज्",
            "गतिवैकल्ये", "लंगड़ा होना, लंगड़ाना", "to be unable to walk, to have disability, to limp",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0267", 267, "एजृँ", "एज्",
            "कम्पने", "कांपना", "to shiver,to tremble, to shake",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0268", 268, "टुओँस्फूर्जाँ", "स्फूर्ज्",
            "वज्रनिर्घोषे", "मेघ की गर्जना होना, गड़गड़ाना", "to thunder,to explode,to burst",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0269", 269, "क्षि", "क्षि",
            "क्षये", "नष्ट होना, सूक्ष्म होना, ह्रास होना, कम होना", "to decay, to get killed, to get destroyed, to wane, to go away, to reduce, to shrink",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.0270", 270, "क्षीजँ", "क्षीज्",
            "अव्यक्ते शब्दे", "अस्पष्ट शब्द करना, कराहना, खीजना, दुःखी होकर बड़बड़ाना", "to speak inarticulately, to speak in sorrow, to whine, to be angry",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0271", 271, "लजँ", "लज्",
            "भर्जने", "भूंजना,तलना, भूनना", "to fry, to roast",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0272", 272, "लजिँ", "लञ्ज्",
            "भर्जने", "भूजना,तलना, भूनना", "to fry, to roast",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0273", 273, "लाजँ", "लाज्",
            "भर्जने भर्त्सने च", "भूनना, दोष लगाना, निन्दा करना", "to fry, to roast, to blame,to traduce,to insult",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0274", 274, "लाजिँ", "लाञ्ज्",
            "भर्जने भर्त्सने च", "भूनना, दोष लगाना", "to fry, to roast, to blame,to traduce,to insult",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0275", 275, "जजँ", "जज्",
            "युद्धे", "युद्ध करना, लड़ाई करना, मारना", "to fight, to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0276", 276, "जजिँ", "जञ्ज्",
            "युद्धे", "युद्ध करना, लड़ाई करना", "to fight, to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0277", 277, "तुजँ", "तुज्",
            "हिंसायाम्", "दुःख देना, मार डालना, हिंसा करना", "to hurt,to injure,to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0278", 278, "तुजिँ", "तुञ्ज्",
            "पालने", "पालन करना", "to follow, to obey",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0279", 279, "गजँ", "गज्",
            "शब्दे मदने च", "शब्द करना, मदोन्मत्त होना, वेसुध होना", "to sound, to be intoxicated, to be drunk, to be confused",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0280", 280, "गजिँ", "गञ्ज्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0281", 281, "गृजँ", "गृज्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0282", 282, "गृजिँ", "गृञ्ज्",
            "गर्जने", "शब्द करना, गर्जना करना", "to sound,to roar,to grumble",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0283", 283, "मुजँ", "मुज्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0284", 284, "मुजिँ", "मुञ्ज्",
            "शब्दे", "शब्द करना,गूंजना, भवरे के समान गूंजना", "to sound, to hum, to buzz",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0285", 285, "वजँ", "वज्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0286", 286, "व्रजँ", "व्रज्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0287", 287, "अट्टँ", "अट्ट्",
            "अतिक्रमहिंसयोः", "अधिक होना, मार डालना, दुःख देना", "to transgress,to kill, to hurt",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0288", 288, "वेष्टँ", "वेष्ट्",
            "वेष्टने", "लपेटना, घेरना", "to surround, to cover",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0289", 289, "चेष्टँ", "चेष्ट्",
            "चेष्टायाम्", "चेष्टा करना, कोशिश करना", "to act, to try, to put effort",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0290", 290, "गोष्टँ", "गोष्ट्",
            "सङ्घाते", "बटोरना, एकत्र करना, ढेर लगाना, ढेर करना", "to assemble,to collect,to pile up",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0291", 291, "लोष्टँ", "लोष्ट्",
            "सङ्घाते", "बटोरना, एकत्र करना, ढेर लगाना, ढेर करना", "to assemble,to collect,to pile up",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0292", 292, "घट्टँ", "घट्ट्",
            "चलने", "चलना", "to walk",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0293", 293, "स्फुटँ", "स्फुट्",
            "विकसने", "खिलना, विकसित होना, प्रफुल्लित होना", "to blow,to blossom,to burst,to break open,to split open",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0294", 294, "अठिँ", "अण्ठ्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0295", 295, "वठिँ", "वण्ठ्",
            "एकचर्यायाम्", "अकेला जाना", "to go alone",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0296", 296, "मठिँ", "मण्ठ्",
            "शोके", "दुःख करना, चिन्ता करना, रोकना", "to sorrow, to grieve, to be anxious",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0297", 297, "कठिँ", "कण्ठ्",
            "शोके", "दुःख करना, चिन्ता करना, रोकना", "to sorrow, to grieve, to be anxious",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0298", 298, "मुठिँ", "मुण्ठ्",
            "पालने", "पालन करना", "to protect",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0299", 299, "हेठँ", "हेठ्",
            "विबाधायाम्", "रोकना, निष्ठुर होना, क्रूर होना", "to hinder, to be wicked, to be cruel",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0300", 300, "एठँ", "एठ्",
            "विबाधायाम्", "रोकना, निष्ठुर होना, क्रूर होना", "to hinder, to be wicked, to be cruel",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0301", 301, "हिडिँ", "हिण्ड्",
            "गत्यनादरयोः", "घूमना, अनादर होना, अनादर करना", "to roam, to disrespect, to insult",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0302", 302, "हुडिँ", "हुण्ड्",
            "सङ्घाते", "बटोरना, एकत्र करना, मान्य करना, वरण करना", "to accept, to agree, to take away",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0303", 303, "कुडिँ", "कुण्ड्",
            "दाहे", "जलना", "to burn, to maim, to mutilate",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0304", 304, "वडिँ", "वण्ड्",
            "विभाजने", "अलग करना", "to separate, to divide",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0305", 305, "मडिँ", "मण्ड्",
            "विभाजने", "अलग करना", "to separate, to divide",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0306", 306, "भडिँ", "भण्ड्",
            "परिभाषणे", "उपहास करना, बोलना", "to speak, to tease, to mock",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0307", 307, "पिडिँ", "पिण्ड्",
            "सङ्घाते", "ढेर करना, राशि करना", "to heap, to pile up, to gather",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0308", 308, "मुडिँ", "मुण्ड्",
            "मार्जने", "स्वछ करना, स्वछ होना", "to cleanse, to purify",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0309", 309, "तुडिँ", "तुण्ड्",
            "तोडने", "तोडना, दुःख देना, हिंसा करना", "to disregard,to condemn, to kill, to injure, to cause pain",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0310", 310, "हुडिँ", "हुण्ड्",
            "वरणे हरणे च", "स्वीकार करना, मान्य करना, हरण करना", "to accept, to agree, to take away",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0311", 311, "स्फुडिँ", "स्फुण्ड्",
            "विकसने", "", "to grow, to blossom",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0312", 312, "चडिँ", "चण्ड्",
            "कोपे", "क्रोध करना", "to be angry",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0313", 313, "शडिँ", "शण्ड्",
            "रुजायां सङ्घाते च", "रोगी होना, बीमार होना, एकत्र करना, ढेर करना", "to have disease, to be ill, to heap, to pile up, to gather",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0314", 314, "तडिँ", "तण्ड्",
            "ताडने", "मारना, ताडना", "to strike, to hit",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0315", 315, "पडिँ", "पण्ड्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0316", 316, "कडिँ", "कण्ड्",
            "मदे", "दुःख या आनन्द में लीन होना, नशा में बेसुध होना", "to be intoxicated,  to be drunk, to be confused",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0317", 317, "खडिँ", "खण्ड्",
            "मन्थे", "मथना, टुकड़ा टुकड़ा करना, हिंसा करना", "to break, to fragment, to shatter, to cut, to tear",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0318", 318, "हेडृँ", "हेड्",
            "अनादरे", "अपमान करना, अनादर करना", "to disregard,to insult",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0319", 319, "होडृँ", "होड्",
            "अनादरे", "अपमान करना", "to disregard,to insult",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0320", 320, "बाडृँ", "बाड्",
            "आप्लाव्ये", "स्नान करना, नहाना, अंग धोना", "to bathe",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0321", 321, "वाडृँ", "वाड्",
            "आप्लाव्ये", "", "to bathe",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0322", 322, "द्राडृँ", "द्राड्",
            "विशरणे", "चीरना, टुकड़े टुकड़े करना, फाड़ना", "to cut,to split, to fragment, to tear, to shatter",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0323", 323, "ध्राडृँ", "ध्राड्",
            "विशरणे", "चीरना, टुकड़े टुकड़े करना, फाड़ना", "to cut,to split, to fragment, to tear, to shatter",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0324", 324, "शाडृँ", "शाड्",
            "श्लाघायाम्", "प्रशंसा करना, स्तुति करना", "to praise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0325", 325, "शौटृँ", "शौट्",
            "गर्वे", "गर्व करना", "to be proud",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0326", 326, "यौटृँ", "यौट्",
            "बन्धे", "बांधना, वश में रखना", "to bind, to keep under control",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0327", 327, "मेडृँ", "मेड्",
            "उन्मादे", "", "to be mad, to be mentally ill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0328", 328, "म्रेडृँ", "म्रेड्",
            "उन्मादे", "पागल होना", "to be mad, to be mentally ill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0329", 329, "म्लेटृँ", "म्लेट्",
            "उन्मादे", "पागल होना", "to be mad, to be mentally ill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0330", 330, "चटेँ", "चट्",
            "वर्षावरणयोः", "", "to rain, to approach, to cover, to surround",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0331", 331, "कटेँ", "कट्",
            "वर्षावरणयोः", "बरसना, घेरना, समीप जाना", "to rain, to approach, to cover, to surround",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0332", 332, "अटँ", "अट्",
            "गतौ", "जाना", "to wander",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0333", 333, "पटँ", "पट्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0334", 334, "रटँ", "रट्",
            "परिभाषणे", "रटना, याद करना, बोलना, सम्भाषण करना", "to memorize, to recall, to chat",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0335", 335, "लटँ", "लट्",
            "बाल्ये", "बालक के समान चेष्टा करना या बोलना", "to kid, to be childish, to speak less",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0336", 336, "शटँ", "शट्",
            "रुजाविशरणगत्यवसादनेषु", "रोगी होना, बीमार होना, छेद करना, जाना, थकना,श्रान्त होना, उदास होना", "to be sick,to divide,to pierce,to go,to be weary, to be tired, to be sad",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0337", 337, "वटँ", "वट्",
            "वेष्टने", "घेरना, गूंथना, एकत्र करना", "to cover,to surround,to string, to collect, to bring together",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0338", 338, "किटँ", "किट्",
            "त्रासे", "डरना, दुःख देना, सताना", "to frighten,to irritate, to cause pain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0339", 339, "खिटँ", "खिट्",
            "त्रासे", "डरना, दुःख देना, सताना", "to frighten,to irritate, to cause pain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0340", 340, "शिटँ", "शिट्",
            "अनादरे", "अपमान करना", "to insult, to disregard",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0341", 341, "षिटँ", "सिट्",
            "अनादरे", "अपमान करना", "to insult, to disregard",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0342", 342, "जटँ", "जट्",
            "सङ्घाते", "जमा होना, एकत्र होना", "to become matted, to become entangled",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0343", 343, "झटँ", "झट्",
            "सङ्घाते", "जमा होना, एकत्र होना", "to become matted, to become entangled",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0344", 344, "भटँ", "भट्",
            "भृतौ", "धारण करना, पास रखना, भाड़े पर लेना", "to rent, to hire, to employ",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0345", 345, "तटँ", "तट्",
            "उच्छ्राये", "ऊँचा होना, वृद्धिगत होना", "to go up, to go high, to raise",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0346", 346, "खटँ", "खट्",
            "काङ्क्षायाम्", "इच्छा करना", "to wish, to desire",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0347", 347, "णटँ", "नट्",
            "नृत्तौ", "नृत्य करना, नाचना", "to dance, to act",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0348", 348, "पिटँ", "पिट्",
            "शब्दसङ्घातयोः", "शब्द करना, ढेर करना", "to sound, to heap, to pile up",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0349", 349, "हटँ", "हट्",
            "दीप्तौ", "प्रकाशित होना", "to glow, to shine",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0350", 350, "षटँ", "सट्",
            "अवयवे", "भाग होना, हिस्सा होना", "to be a part",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0351", 351, "लुटँ", "लुट्",
            "विलोडने", "विलोडना, कांपना, हिलना", "to agitate, to churn, to stir, to roll",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0352", 352, "लुडँ", "लुड्",
            "विलोडने", "", "to agitate, to churn, to stir, to roll",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0353", 353, "चिटँ", "चिट्",
            "परप्रैष्ये", "सेवक होना, सेवक के समान आज्ञा का पालन करना", "to obey, to be slave",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0354", 354, "विटँ", "विट्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0355", 355, "बिटँ", "बिट्",
            "आक्रोशे", "शाप देना, गाली देना", "to swear,to curse,to shout, to abuse",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0356", 356, "हिटँ", "हिट्",
            "आक्रोशे", "", "to swear,to curse,to shout, to abuse",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0357", 357, "इटँ", "इट्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0358", 358, "किटँ", "किट्",
            "गतौ", "जाना", "to frighten,to irritate, to cause pain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0359", 359, "कटीँ", "कट्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0360", 360, "चुटिँ", "चुण्ट्",
            "अल्पीभावे", "कम होना, चुटकी भर परिमाण", "to reduce, to shrink, to contract",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0361", 361, "मडिँ", "मण्ड्",
            "भूषायाम्", "अलंकृत करना", "to adorn, to decorate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0362", 362, "कुडिँ", "कुण्ड्",
            "वैकल्ये", "कुण्ठित करना", "to be lame,to be dull",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0363", 363, "कुठिँ", "कुण्ठ्",
            "वैकल्ये", "", "to be lame,to be dull",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0364", 364, "मुडँ", "मुड्",
            "मर्दने", "मर्दन करना, घिसना, दवाना", "to press, to massage, to rub",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0365", 365, "प्रुडँ", "प्रुड्",
            "मर्दने विमर्दने", "मर्दन करना, घिसना, दवाना", "to press, to massage, to rub",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0366", 366, "मुटँ", "मुट्",
            "मर्दने", "", "to press, to massage, to rub",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0367", 367, "पुडँ", "पुड्",
            "मर्दने", "", "to press, to massage, to rub",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0368", 368, "चुडिँ", "चुण्ड्",
            "अल्पीभावे", "कम होना, चुटकी भर परिमाण", "to reduce, to shrink, to contract",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0369", 369, "मुडिँ", "मुण्ड्",
            "खण्डने", "चूर्ण करना, क्षौर करना, मुण्डन करना, हजामत करना", "to powder, to grind, to shave",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0370", 370, "पुडिँ", "पुण्ड्",
            "खण्डने", "", "to powder, to grind, to shave",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0371", 371, "रुटिँ", "रुण्ट्",
            "स्तेये", "चोरी करना", "to steal, to rob",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0372", 372, "लुटिँ", "लुण्ट्",
            "स्तेये", "चोरी करना", "to steal, to rob",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0373", 373, "रुठिँ", "रुण्ठ्",
            "स्तेये", "", "to steal,to rob",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0374", 374, "लुठिँ", "लुण्ठ्",
            "स्तेये", "", "to steal,to rob",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0375", 375, "रुडिँ", "रुण्ड्",
            "स्तेये", "", "to steal, to rob",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0376", 376, "लुडिँ", "लुण्ड्",
            "स्तेये", "", "to steal, to rob",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0377", 377, "वटिँ", "वण्ट्",
            "विभाजने", "", "to partition, to share to divide",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0378", 378, "स्फटिँ", "स्फण्ट्",
            "विशरणे", "हिंसा करना, नष्ट होना,विखेरना, विस्फोट होना", "to burst,to break open,to split open, to destroy, to shatter, explode",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0379", 379, "स्फुटिँर्", "स्फुट्",
            "विशरणे", "हिंसा करना, नष्ट होना,विखेरना, विस्फोट होना", "to burst,to break open,to split open, to destroy, to shatter, explode",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0380", 380, "स्फुटिँ", "स्फुण्ट्",
            "विशरणे", "", "to burst,to break open,to split open, to destroy, to shatter, explode",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0381", 381, "पठँ", "पठ्",
            "व्यक्तायां वाचि", "पढ़ना, सीखना", "to learn, to read, to study, to recite",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0382", 382, "वठँ", "वठ्",
            "स्थौल्ये", "मोटा होना, शक्तिवान होना, स्थूल होना", "to become fat, to gain weight",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0383", 383, "हौडृँ", "हौड्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0384", 384, "मठँ", "मठ्",
            "मदनिवासयोः", "गर्वीला होना, रहना", "to be egoistic, to stay",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0385", 385, "कठँ", "कठ्",
            "कृच्छ्रजीवने", "कष्ट से जीवन बिताना", "to drag on the days of life, to live in distress",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0386", 386, "रठँ", "रठ्",
            "परिभाषणे", "", "to speak, to memorise",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0387", 387, "रटँ", "रट्",
            "परिभाषणे", "बोलना, भाषण करना, रटना", "to memorize, to recall, to chat",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0388", 388, "हठँ", "हठ्",
            "प्लुतिशठत्वयोः बलात्कारे च", "फुदकना, दुष्ट होना, घातकी होना, बलात्कार करना, जुल्म करना", "to leap,to jump, to be wicked,to treat with violence, to rape, to do crime",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0389", 389, "रुठँ", "रुठ्",
            "उपघाते", "मारना, नीचे गिराना", "to hit, to push down, to throw down, to knock down",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0390", 390, "लुठँ", "लुठ्",
            "उपघाते", "मारना, नीचे गिराना", "to hit, to push down, to throw down, to knock down",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0391", 391, "ऊठँ", "ऊठ्",
            "उपघाते", "", "to hit, to push down, to throw down, to knock down",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0392", 392, "उठँ", "उठ्",
            "उपघाते", "मारना, ठोकना, नीचे गिराना", "to hit, to push down, to throw down, to knock down",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0393", 393, "पिठँ", "पिठ्",
            "हिंसासङ्क्लेशनयोः", "मार डालना, दुःख देना, दुःख पाना, कष्ट का अनुभव करना", "to kill, to give pain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0394", 394, "शठँ", "शठ्",
            "कैतवे हिंसासङ्क्लेशनयोः द्यूते स्पर्धायां च", "ठगना, मार देना, कष्ट देना", "to cheat,to defraud, to kill, to give pain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0395", 395, "शुठँ", "शुठ्",
            "प्रतिघाते गतिप्रतिघाते च", "रोकना, जाने में विघ्न होना", "to stop, to hinder, to restrain, to oppose",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0396", 396, "शूठँ", "शूठ्",
            "गतिप्रतिघाते", "", "to stop, to hinder, to restrain, to oppose",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0397", 397, "कुठिँ", "कुण्ठ्",
            "प्रतिघाते", "रोकना, रूकावट पैदा करना", "to stop, to hinder, to restrain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0398", 398, "लुठिँ", "लुण्ठ्",
            "आलस्ये प्रतिघाते च", "आलस्य करना, रूकावट होना", "to be lazy, to combat, to resist",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0399", 399, "शुठिँ", "शुण्ठ्",
            "शोषणे", "सूखना, सुखाना", "to dry,to become dry",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0400", 400, "रुठिँ", "रुण्ठ्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0401", 401, "लुठिँ", "लुण्ठ्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0402", 402, "चुड्डँ", "चुड्ड्",
            "भावकरणे", "अभिप्राय बताना, इशारा करना", "to indicate, to give opinion, to hint, to give clue",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0403", 403, "अड्डँ", "अड्ड्",
            "अभियोगे", "सब और से जोड़ना, अपराध लगाना, प्रार्थना करना", "to join from all sides, to pray, to do a crime",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0404", 404, "कड्डँ", "कड्ड्",
            "कार्कश्ये", "निष्ठुर होना, कठोर होना", "to be harsh, to be rough, to be ruthless",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0405", 405, "क्रीडृँ", "क्रीड्",
            "विहारे", "विहार करना, खेलना, मन बहलाना", "to amuse oneself, to enjoy, to play, to gamble, to make a noise",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0406", 406, "तुडृँ", "तुड्",
            "तोडने", "तोडना, कतरना, हिंसा करना,दुःख देना", "to split, to tear, to cut, to break, to hurt, to injure, to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0407", 407, "तूडृँ", "तूड्",
            "तोडने", "", "to split, to tear, to cut, to break, to hurt, to injure, to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0408", 408, "हुडृँ", "हुड्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0409", 409, "हूडृँ", "हूड्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0410", 410, "होडृँ", "होड्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0411", 411, "रौडृँ", "रौड्",
            "अनादरे", "अपमान करना", "to disrespect, to insult",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0412", 412, "रोडृँ", "रोड्",
            "उन्मादे", "उन्मत्त होना, पागल होना", "to be mad, to lose mental stability",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0413", 413, "लोडृँ", "लोड्",
            "उन्मादे", "उन्मत्त होना, पागल होना", "to be mad, to lose mental stability",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0414", 414, "अडँ", "अड्",
            "उद्यमे", "उद्यम करना, प्रयत्न करना", "to exert,to try, to put effort",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0415", 415, "लडँ", "लड्",
            "विलासे", "क्रीडा करना, मौज करना", "to enjoy, to play",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0416", 416, "ललँ", "लल्",
            "विलासे", "", "to enjoy, to play",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0417", 417, "कडँ", "कड्",
            "मदे", "दुःख या आनन्द में लीन होना, नशा में लीन होना", "to be drunk, to be intoxicated, to be confused",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0418", 418, "कडिँ", "कण्ड्",
            "मदे", "", "to be drunk, to be intoxicated, to be confused",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0419", 419, "गडिँ", "गण्ड्",
            "वदनैकदेशे", "गालोंमें रोग होना, गण्डमाला होना", "to have cheek disease",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0420", 420, "तिपृँ", "तिप्",
            "क्षरणे", "सींचना, प्रोक्षण करना, झरना, चूना", "to drop, to ooze, to sprinkle",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.0421", 421, "तेपृँ", "तेप्",
            "क्षरणे कम्पने च", "सींचना, प्रोक्षण करना, झरना, चूना", "to drop, to ooze, to sprinkle, to shiver, to shake,  to tremble",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0422", 422, "ष्टिपृँ", "स्तिप्",
            "क्षरणे", "सींचना, प्रोक्षण करना, झरना, चूना", "to drop, to ooze, to sprinkle",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0423", 423, "ष्टेपृँ", "स्तेप्",
            "क्षरणे", "सींचना, प्रोक्षण करना, झरना, चूना", "to drop, to ooze, to sprinkle",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0424", 424, "ग्लेपृँ", "ग्लेप्",
            "दैन्ये", "पराधीन होना, दरिद्र होना, कांपना, जाना", "to be poor, to be miserable",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0425", 425, "टुवेपृँ", "वेप्",
            "कम्पने", "कांपना", "to tremble,to shake",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0426", 426, "केपृँ", "केप्",
            "कम्पने गतौ च", "कांपना, जाना", "to tremble,to shake, to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0427", 427, "गेपृँ", "गेप्",
            "कम्पने गतौ च", "कांपना, जाना", "to tremble,to shake, to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0428", 428, "ग्लेपृँ", "ग्लेप्",
            "कम्पने गतौ च", "कांपना, जाना", "to shiver, to tremble, to shake",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0429", 429, "मेपृँ", "मेप्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0430", 430, "रेपृँ", "रेप्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0431", 431, "लेपृँ", "लेप्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0432", 432, "हेपृँ", "हेप्",
            "गतौ", "", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0433", 433, "धेपृँ", "धेप्",
            "गतौ", "", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0434", 434, "त्रपूँष्", "त्रप्",
            "लज्जायाम्", "लज्जा करना", "to be ashamed, to be abashed,to be embarrassed, to shy",
            PadaType.ATMANEPADA, ItStatus.VET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0435", 435, "कपिँ", "कम्प्",
            "चलने", "चलना, कांपना", "to shake, to tremble, to quake, to shiver",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0436", 436, "रबिँ", "रम्ब्",
            "शब्दे", "शब्द करना, शोर करना", "to sound, to make noise, to shout",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0437", 437, "लबिँ", "लम्ब्",
            "शब्दे", "शब्द करना, शोर करना", "to sound, to make noise, to shout",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0438", 438, "अबिँ", "अम्ब्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0439", 439, "लबिँ", "लम्ब्",
            "अवस्रंसने शब्दे च", "शब्द करना, लटकना, आधा नीचे गिरना", "to hang down",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0440", 440, "कबृँ", "कब्",
            "वर्णे", "प्रशंसा करना, स्तुति करना, रंग देना, रंगना", "to praise, to color, to paint",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0441", 441, "क्लीबृँ", "क्लीब्",
            "अधार्ष्ट्ये", "दुर्बल होना, वीर्य रहित होना, लज्जालु होना, डरपोक होना", "to behave like a eunuch,to be timorous, to be modest, to be unassuming, to be nervous, to be shy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0442", 442, "क्षीबृँ", "क्षीब्",
            "मदे", "", "to be drunk, to be intoxicated, to be confused",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0443", 443, "क्षीवृँ", "क्षीव्",
            "मदे", "मदोन्मत्त होना, मस्त होना", "to be drunk, to be intoxicated, to be confused",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0444", 444, "शीभृँ", "शीभ्",
            "कत्थने", "प्रशंसा करना, शेखी मरना, आत्मस्तुति करना", "to boast, to praise oneself",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0445", 445, "लौडृँ", "लौड्",
            "उन्मादे", "उन्मत्त होना, पागल होना", "to be mad, to lose mental stability",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0446", 446, "चीभृँ", "चीभ्",
            "कत्थने", "प्रशंसा करना, शेखी मरना, आत्मस्तुति करना", "to boast, to praise oneself",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0447", 447, "रेभृँ", "रेभ्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0448", 448, "अभिँ", "अम्भ्",
            "शब्दे", "", "to sound",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0449", 449, "रभिँ", "रम्भ्",
            "शब्दे", "", "to sound",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0450", 450, "षिधुँ", "सिध्",
            "गत्याम्", "जाना", "to regulate, to punish, to hinder, to instruct, to scare away, to drive off, to restrain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0451", 451, "ष्टभिँ", "स्तम्भ्",
            "प्रतिबन्धे", "रूकावट डालना", "to stop,to hinder,to become stiff,  to paralyze,to fix firmly",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0452", 452, "स्कभिँ", "स्कम्भ्",
            "प्रतिबन्धे", "रूकावट डालना", "to stop,to hinder,to become stiff,  to paralyze,to fix firmly",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0453", 453, "जभीँ", "जभ्",
            "गात्रविनामे", "जम्हाई लेना", "to yawn,to gape",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0454", 454, "जृभिँ", "जृम्भ्",
            "गात्रविनामे", "जम्हाई लेना", "to yawn,to gape",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0455", 455, "शल्भँ", "शल्भ्",
            "कत्थने", "प्रशंसा करना, शेखी मरना, आत्मस्तुति करना", "to boast, to praise oneself",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0456", 456, "वल्भँ", "वल्भ्",
            "भोजने", "भोजन करना", "to eat,to devour",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0457", 457, "गल्भँ", "गल्भ्",
            "धार्ष्ट्ये", "धैर्य रखना, साहस करना", "to be bold, to be confident, to show courage",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0458", 458, "श्रन्भुँ", "श्रम्भ्",
            "प्रमादे", "चूकना, गलती करना,", "to err, to make mistake",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0459", 459, "स्रन्भुँ", "स्रम्भ्",
            "प्रमादे", "", "to err, to make mistake",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0460", 460, "ष्टुभुँ", "स्तुभ्",
            "स्तम्भे", "अवरोध करना, मुर्ख होना", "to stop,to hinder,to become stiff,  to paralyze,to fix firmly",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0461", 461, "गुपूँ", "गुप्",
            "रक्षणे", "रक्षा करना", "to protect, to hide, to conceal",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0462", 462, "धूपँ", "धूप्",
            "सन्तापे", "गरम होना, तपाना", "to heat, to fumigate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0463", 463, "जपँ", "जप्",
            "व्यक्तायां वाचि मानसे च", "बोलना, बकना, जप करना, मन में बोलना", "to speak, to meditate, to mutter, to speak nonsense",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0464", 464, "जल्पँ", "जल्प्",
            "व्यक्तायां वाचि", "बोलना, बकना, जप करना, मन में बोलना", "to speak, to meditate, to mutter, to speak nonsense",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0465", 465, "चपँ", "चप्",
            "सान्त्वने", "शान्त करना", "to console,to sooth,to pacify",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0466", 466, "षपँ", "सप्",
            "समवाये", "पूर्ण ज्ञान होना, संलग्न होना, मिलाप होना", "to connect, to join, to understand completely",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0467", 467, "रपँ", "रप्",
            "व्यक्तायां वाचि", "स्पष्ट बोलना", "to talk,to speak, to articulate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0468", 468, "लपँ", "लप्",
            "व्यक्तायां वाचि", "स्पष्ट बोलना", "to talk,to speak, to articulate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0469", 469, "चुपँ", "चुप्",
            "मन्दायां गतौ", "धीरे धीरे चलना", "to move slowly",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0470", 470, "तुपँ", "तुप्",
            "हिंसायाम्", "हिंसा करना", "to injure,to hurt,to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0471", 471, "तुन्पँ", "तुम्प्",
            "हिंसायाम्", "हिंसा करना", "to injure,to hurt,to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0472", 472, "त्रुपँ", "त्रुप्",
            "हिंसायाम्", "हिंसा करना", "to injure,to hurt,to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0473", 473, "त्रुन्पँ", "त्रुम्प्",
            "हिंसायाम्", "हिंसा करना", "to injure,to hurt,to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0474", 474, "तुफँ", "तुफ्",
            "हिंसायाम्", "हिंसा करना", "to injure,to hurt,to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0475", 475, "तुन्फँ", "तुम्फ्",
            "हिंसायाम्", "हिंसा करना", "to injure,to hurt,to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0476", 476, "त्रुफँ", "त्रुफ्",
            "हिंसायाम्", "हिंसा करना", "to injure,to hurt,to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0477", 477, "त्रुन्फँ", "त्रुम्फ्",
            "हिंसायाम्", "हिंसा करना", "to injure,to hurt,to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0478", 478, "पर्पँ", "पर्प्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0479", 479, "रफँ", "रफ्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0480", 480, "रफिँ", "रम्फ्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0481", 481, "अर्बँ", "अर्ब्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0482", 482, "पर्बँ", "पर्ब्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0483", 483, "लर्बँ", "लर्ब्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0484", 484, "बर्बँ", "बर्ब्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0485", 485, "मर्बँ", "मर्ब्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0486", 486, "कर्बँ", "कर्ब्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0487", 487, "खर्बँ", "खर्ब्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0488", 488, "गर्बँ", "गर्ब्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0489", 489, "शर्बँ", "शर्ब्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0490", 490, "षर्बँ", "सर्ब्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0491", 491, "चर्बँ", "चर्ब्",
            "गतौ अर्दने च", "जाना", "to go, to trouble, to distress",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0492", 492, "कुबिँ", "कुम्ब्",
            "छादने आच्छादने च", "आच्छादित करना", "to cover",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0493", 493, "लुबिँ", "लुम्ब्",
            "अर्दने", "मार डालना, दुःख देना, नोचना", "to kill, to cause pain, to torment, to irritate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0494", 494, "तुबिँ", "तुम्ब्",
            "अर्दने", "मार डालना, दुःख देना, नोचना", "to be invisible,to trouble,to distress,to hurt, to tear, to claw, to pluck",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0495", 495, "चुबिँ", "चुम्ब्",
            "वक्त्रसंयोगे", "चूमना, चुम्बन लेना", "to kiss",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0496", 496, "षृभुँ", "सृभ्",
            "हिंसायाम्", "मारना", "to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0497", 497, "षृन्भुँ", "सृम्भ्",
            "हिंसायाम्", "मारना", "to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0498", 498, "षिभुँ", "सिभ्",
            "हिंसायाम्", "", "to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0499", 499, "षिन्भुँ", "सिम्भ्",
            "हिंसायाम्", "", "to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0500", 500, "शुभँ", "शुभ्",
            "भाषणे भासने हिंसायां च", "भाषण करना, बोलना, मारना, शोभा पाना, चमकना", "to speak, to shine,to glow, to speak,to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0501", 501, "शुन्भँ", "शुम्भ्",
            "भाषणे भासने हिंसायां दीप्तौ च", "भाषण करना, बोलना, मारना, शोभा पाना, चमकना", "to speak, to shine,to glow, to speak,to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0502", 502, "घिणिँ", "घिण्ण्",
            "ग्रहणे", "लेना", "to take",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0503", 503, "घुणिँ", "घुण्ण्",
            "ग्रहणे", "लेना", "to take",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0504", 504, "घृणिँ", "घृण्ण्",
            "ग्रहणे", "लेना", "to take",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0505", 505, "घुणँ", "घुण्",
            "भ्रमणे", "घूमना, भ्रमण करना", "to roam round",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0506", 506, "घुर्णँ", "घूर्ण्",
            "भ्रमणे", "घूमना, भ्रमण करना", "to roam round",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0507", 507, "पणँ", "पण्",
            "व्यवहारे स्तुतौ च", "उद्योग करना, व्यवहार करना, प्रशंसा करना, स्तुति करना", "to deal, to transact, to do business, to barter, to praise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0508", 508, "पनँ", "पन्",
            "व्यवहारे स्तुतौ च", "उद्योग करना, व्यवहार करना, प्रशंसा करना, स्तुति करना", "to deal, to transact, to do business, to barter, to praise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0509", 509, "भामँ", "भाम्",
            "क्रोधे", "क्रोध करना", "to be angry",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0510", 510, "क्षमूँष्", "क्षम्",
            "सहने", "सहन करना, क्षमा करना", "to endure, to suffer, to forgive",
            PadaType.ATMANEPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0511", 511, "कमुँ", "कम्",
            "कान्तौ", "चाहना, इच्छा करना", "to love,to desire, to long for, to have intercourse with",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0512", 512, "अणँ", "अण्",
            "शब्दे", "शब्द करना", "to breathe,to utter a voice,to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0513", 513, "रणँ", "रण्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0514", 514, "वणँ", "वण्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0515", 515, "भणँ", "भण्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0516", 516, "मणँ", "मण्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0517", 517, "कणँ", "कण्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0518", 518, "क्वणँ", "क्वण्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0519", 519, "व्रणँ", "व्रण्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0520", 520, "भ्रणँ", "भ्रण्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0521", 521, "ध्वणँ", "ध्वण्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0522", 522, "धणँ", "धण्",
            "शब्दे", "", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0523", 523, "ओणृँ", "ओण्",
            "अपनयने", "दूर करना, दूर ले जाना", "to remove,to discard, to push away",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0524", 524, "शोणृँ", "शोण्",
            "वर्णगत्योः", "लाल होना, जाना", "to be red,to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0525", 525, "श्रोणृँ", "श्रोण्",
            "सङ्घाते", "एकत्र करना, बटोरना", "to heap together,to collect, to pile together",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0526", 526, "श्लोणृँ", "श्लोण्",
            "सङ्घाते", "एकत्र होना", "to heap together,to collect, to pile together",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0527", 527, "पैणृँ", "पैण्",
            "गतिप्रेरणश्लेषणेषु", "जाना, आज्ञा करना, स्पर्श करना, आलिंगन करना", "to go,to inspire, to send,to touch,to hug, to embrace",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0528", 528, "प्रैणृँ", "प्रैण्",
            "गतिप्रेरणश्लेषणेषु", "", "to go,to inspire, to send,to touch,to hug, to embrace",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0529", 529, "ध्रणँ", "ध्रण्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0530", 530, "बणँ", "बण्",
            "शब्दे", "", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0531", 531, "कनीँ", "कन्",
            "दीप्तिकान्तिगतिषु", "चमकना, प्रकाशित होना, समीप जाना, समीप आना", "to glow, to shine, to approach",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0532", 532, "ष्टनँ", "स्तन्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0533", 533, "वनँ", "वन्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0534", 534, "वनँ", "वन्",
            "सम्भक्तौ", "सेवा करना, चाकरी करना, आपद्ग्रस्त होना", "to serve, to be slave, to have problem",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0535", 535, "षणँ", "सन्",
            "सम्भक्तौ", "सेवा करना, चाकरी करना, आपद्ग्रस्त होना", "to serve, to help, to be slave, to have problem",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0536", 536, "अमँ", "अम्",
            "गतौ शब्दे सम्भक्तौ च", "जाना, शब्द करना, सेवा करना", "to go, to sound, to serve, to help, to be slave",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0537", 537, "द्रमँ", "द्रम्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0538", 538, "हम्मँ", "हम्म्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0539", 539, "मीमृँ", "मीम्",
            "गतौ शब्दे च", "जाना, शब्द करना", "to go, to talk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0540", 540, "चमुँ", "चम्",
            "अदने", "खाना", "to eat",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0541", 541, "छमुँ", "छम्",
            "अदने", "खाना", "to eat",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0542", 542, "जमुँ", "जम्",
            "अदने", "खाना", "to eat",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0543", 543, "झमुँ", "झम्",
            "अदने", "खाना", "to eat",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0544", 544, "जिमुँ", "जिम्",
            "अदने", "", "to eat",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0545", 545, "क्रमुँ", "क्रम्",
            "पादविक्षेपे", "निर्भयता से जाना, रक्षण करना, बढ़ना", "to walk,to step,to go ahead,to cross,to leap,to ascend, to protect, to approach",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0546", 546, "अयँ", "अय्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0547", 547, "वयँ", "वय्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0548", 548, "पयँ", "पय्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0549", 549, "मयँ", "मय्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0550", 550, "चयँ", "चय्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0551", 551, "तयँ", "तय्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0552", 552, "णयँ", "नय्",
            "गतौ रक्षणे च", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0553", 553, "दयँ", "दय्",
            "दानगतिरक्षणहिंसाऽदानेषु", "दान देना, जाना, पालन करना, रक्षा करना, मार डालना, दुख देना, ग्रहण करना", "to accept, to recieve, to obtain, to go, to protect, to kill, to hurt, to injure, to accept, to take",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0554", 554, "रयँ", "रय्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0555", 555, "ययँ", "यय्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0556", 556, "ऊयीँ", "ऊय्",
            "तन्तुसन्ताने", "सीना, बुनना", "to weave,to sew",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0557", 557, "पूयीँ", "पूय्",
            "विशरणे दुर्गन्धे च", "तोडना, चीरना, दुर्गन्ध आना, बदबू आना", "to split,to stink, to cut",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0558", 558, "क्नूयीँ", "क्नूय्",
            "शब्दे उन्दने च", "शब्द करना, गीला होना", "to sound,to be wet",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0559", 559, "क्ष्मायीँ", "क्ष्माय्",
            "विधूनने", "हिलना, कांपना, हिलाना, कपाना", "to tremble,to shake, to quake, to shake",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0560", 560, "स्फायीँ", "स्फाय्",
            "वृद्धौ", "मोटा होना, बढ़ना", "to grow,to increase,to expand",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0561", 561, "ओँप्यायीँ", "प्याय्",
            "वृद्धौ", "बढ़ना", "to grow,to increase,to expand",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0562", 562, "तायृँ", "ताय्",
            "सन्तानपालनयोः", "संरक्षण करना, पालन करना", "to spread,to protect",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0563", 563, "शलँ", "शल्",
            "चलनसंवरणयोः", "चलना, ढकना", "to go, to cover",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0564", 564, "वलँ", "वल्",
            "संवरणे सञ्चरणे च", "आच्छादित करना, ढकना, घेरना, जाना", "to cover, to hide, to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0565", 565, "वल्लँ", "वल्ल्",
            "संवरणे सञ्चरणे च", "आच्छादित करना, ढकना, घेरना, जाना", "to cover, to hide, to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0566", 566, "मलँ", "मल्",
            "धारणे", "धारण करना", "to wear, to hold, to possess",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0567", 567, "मल्लँ", "मल्ल्",
            "धारणे", "धारण करना", "to wear, to hold, to possess",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0568", 568, "भलँ", "भल्",
            "परिभाषणहिंसादानेषु", "बोलना, मारना, देना", "to speak, to kill, to destroy, to injure, to give, to donate",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0569", 569, "भल्लँ", "भल्ल्",
            "परिभाषणहिंसादानेषु", "बोलना, मारना, देना", "to speak, to kill, to destroy, to injure, to give, to donate",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0570", 570, "कलँ", "कल्",
            "शब्दसङ्ख्यानयोः", "शब्द करना, गिनना", "to sound, to count",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0571", 571, "कल्लँ", "कल्ल्",
            "अव्यक्ते शब्दे अशब्दे च", "अस्पष्ट बोलना, गूंगा होना, चुप होना", "to sound indistinctly, to be quiet",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0572", 572, "तेवृँ", "तेव्",
            "देवने", "खेलना", "to play, to sport",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0573", 573, "देवृँ", "देव्",
            "देवने", "खेलना, क्रीड़ा करना", "to play, to sport",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0574", 574, "षेवृँ", "सेव्",
            "सेवने", "सेवा करना, चाकरी करना", "to serve, to devote oneself, practise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0575", 575, "गेवृँ", "गेव्",
            "सेवने", "सेवा करना, चाकरी करना", "to serve, to devote oneself, practise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0576", 576, "ग्लेवृँ", "ग्लेव्",
            "सेवने", "सेवा करना, चाकरी करना", "to serve, to devote oneself, practise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0577", 577, "पेवृँ", "पेव्",
            "सेवने", "सेवा करना, चाकरी करना", "to serve, to devote oneself, practise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0578", 578, "मेवृँ", "मेव्",
            "सेवने", "सेवा करना, चाकरी करना", "to serve, to devote oneself, practise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0579", 579, "म्लेवृँ", "म्लेव्",
            "सेवने", "सेवा करना, चाकरी करना", "to serve, to devote oneself, practise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0580", 580, "शेवृँ", "शेव्",
            "सेवने", "", "to serve, to devote oneself, practise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0581", 581, "खेवृँ", "खेव्",
            "सेवने", "", "to serve, to devote oneself, practise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0582", 582, "प्लेवृँ", "प्लेव्",
            "सेवने", "", "to serve, to devote oneself, practise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0583", 583, "केवृँ", "केव्",
            "सेवने", "", "to serve, to devote oneself, practise",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0584", 584, "रेवृँ", "रेव्",
            "प्लवगतौ", "उड़कर जाना, तैरकर पार करना", "to leap, to jump, to cross by flying, to cross by swimming",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0585", 585, "मव्यँ", "मव्य्",
            "बन्धने", "बांधना, रोकना", "to hinder, to tie, to bind, to stop, to resist",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0586", 586, "सूर्क्ष्यँ", "सूर्क्ष्य्",
            "ईर्ष्यायाम्", "ईर्ष्या करना, द्वेष करना", "to envy, to grudge",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0587", 587, "ईर्क्ष्यँ", "ईर्क्ष्य्",
            "ईर्ष्यायाम्", "ईर्ष्या करना, द्वेष करना", "to envy, to grudge",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0588", 588, "ईर्ष्यँ", "ईर्ष्य्",
            "ईर्ष्यायाम्", "ईर्ष्या करना, द्वेष करना", "to envy, to grudge",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0589", 589, "हयँ", "हय्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0590", 590, "शुच्यँ", "शुच्य्",
            "अभिषवे", "स्नान करना, सार निकलना, मथना, छापना", "to bathe, to give bath, to extract, to churn, to print",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0591", 591, "चुच्यँ", "चुच्य्",
            "अभिषवे", "", "to bathe, to give bath, to extract, to churn, to print",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0592", 592, "हर्यँ", "हर्य्",
            "गतिकान्त्योः", "जाना, चमकना, प्रकाशित होना", "to go,to glow, to shine",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0593", 593, "अलँ", "अल्",
            "भूषणपर्याप्तिवारणेषु", "भूषित करना, निवारण करना, पूरा करना", "to decorate, to satisfy, to fulfill, to get rid of, to complete",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0594", 594, "ञिफलाँ", "फल्",
            "विशरणे", "विकसना, फल लग जाना", "to blossom, to bear fruits",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0595", 595, "मीलँ", "मील्",
            "निमेषणे", "आँखे मूदना, पलक मारना", "to blink, to wink",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0596", 596, "श्मीलँ", "श्मील्",
            "निमेषणे", "आँखे मूदना, पलक मारना", "to blink, to wink",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0597", 597, "स्मीलँ", "स्मील्",
            "निमेषणे", "आँखे मूदना, पलक मारना", "to blink, to wink",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0598", 598, "क्ष्मीलँ", "क्ष्मील्",
            "निमेषणे", "आँखे मूदना, पलक मारना", "to blink, to wink",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0599", 599, "पीलँ", "पील्",
            "प्रतिष्टम्भे", "मुर्ख होना, थमाना, रोकना", "to be mad, to be insane, to stop, to resist, to obstruct",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0600", 600, "णीलँ", "नील्",
            "वर्णे", "रंगना, रंगाना, नील रंग लगाना", "to color, to dye, to paint, to darken",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0601", 601, "शीलँ", "शील्",
            "समाधौ", "मनन करना, मन को एकाग्र करना, पूजा करना", "to meditate,to practise, to concentrate, to think, to pray, to worship",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0602", 602, "कीलँ", "कील्",
            "बन्धने", "बांधना, कीलोंसे मजबूत करना", "to nail",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0603", 603, "कूलँ", "कूल्",
            "आवरणे", "आच्छादित करना, ढांकना", "to cover",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0604", 604, "शूलँ", "शूल्",
            "रुजायां सङ्घाते सङ्कोषे च", "पेट दुखना, पीड़ा होना, आंव पड़ना, बीमार होना, शूल पर चढ़ना", "to be ill,to have stomach ache, to have pain",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0605", 605, "तूलँ", "तूल्",
            "निष्कर्षे", "सार निकालना", "to extract",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0606", 606, "पूलँ", "पूल्",
            "सङ्घाते", "ढेर करना, बटोरना, सञ्चित करना", "to collect,to gather,to heap up,to assemble",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0607", 607, "मूलँ", "मूल्",
            "प्रतिष्ठायाम्", "जड़ जमाना", "to root, to be firm, to establish",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0608", 608, "फलँ", "फल्",
            "निष्पत्तौ", "सफल होना", "to succeed, to complete, to conclude, to get good results",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0609", 609, "चुल्लँ", "चुल्ल्",
            "भावकरणे", "अपना अभिप्राय बताना", "to give opinion",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0610", 610, "फुल्लँ", "फुल्ल्",
            "विकसने", "विक्सित होना, खिलना", "to develop, to glow, to blossom, to flourish",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0611", 611, "चिल्लँ", "चिल्ल्",
            "शैथिल्ये भावकरणे च", "ढिलाई करना", "to loosen, to express, to speak out",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0612", 612, "तिलँ", "तिल्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0613", 613, "तिल्लँ", "तिल्ल्",
            "गतौ", "", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0614", 614, "वेलृँ", "वेल्",
            "चलने", "चलना, जाना", "to go, walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0615", 615, "चेलृँ", "चेल्",
            "चलने", "चलना, जाना", "to go, walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0616", 616, "केलृँ", "केल्",
            "चलने", "चलना, जाना", "to go, walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0617", 617, "खेलृँ", "खेल्",
            "चलने", "चलना, जाना", "to go, walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0618", 618, "क्ष्वेलृँ", "क्ष्वेल्",
            "चलने", "चलना, जाना", "to go, walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0619", 619, "वेल्लँ", "वेल्ल्",
            "चलने", "चलना, जाना", "to go, walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0620", 620, "चेल्लँ", "चेल्ल्",
            "चलने", "", "to go, walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0621", 621, "पेलृँ", "पेल्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0622", 622, "फेलृँ", "फेल्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0623", 623, "शेलृँ", "शेल्",
            "गतौ", "जाना", "to go, walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0624", 624, "षेलृँ", "सेल्",
            "गतौ", "", "to go, walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0625", 625, "स्खलँ", "स्खल्",
            "सञ्चलने", "जाना, गिरना, च्युत होना, ठोकर लगना", "to stumble,to totter,to deviate,to blunder,to err,to stammer,to drop",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0626", 626, "खलँ", "खल्",
            "सञ्चये चलने च", "सङ्ग्रह करना, एकट्ठा करना", "to gather, to collect,to move, to shake",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0627", 627, "गलँ", "गल्",
            "अदने स्रवणे च", "निगलना, खाना, भक्षण करना", "to eat,to swallow, to drip,to flow,to dissolve",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0628", 628, "षलँ", "सल्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0629", 629, "दलँ", "दल्",
            "विशरणे विदारणे च", "कुम्भलाना, म्लान होना, चीरना, फाड़ना", "to break,to split,to expand,to open,to cut,to decline, to wither, to tear, to fade",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0630", 630, "श्वलँ", "श्वल्",
            "आशुगमने", "तेजी से जाना", "to go fast",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0631", 631, "श्वल्लँ", "श्वल्ल्",
            "आशुगमने", "तेजी से जाना", "to go fast",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0632", 632, "खोलृँ", "खोल्",
            "गतिप्रतिघाते", "लंगड़ाना", "to limp",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0633", 633, "खोरृँ", "खोर्",
            "गतिप्रतिघाते", "लंगड़ाना", "to limp",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0634", 634, "धोरृँ", "धोर्",
            "गतिचातुर्ये", "अच्छी रीति से गमन करना, चतुराई से चलना, जल्दी से चलना", "to go quickly,to run,to trot,to be skillful",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0635", 635, "त्सरँ", "त्सर्",
            "छद्मगतौ", "टेढ़ा जाना, कपट पूर्वक जाना, छिपकर जाना", "to creep,to crawl,to proceed crookedly, to proceed fraudulently",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0636", 636, "क्मरँ", "क्मर्",
            "हूर्छने", "शरीर या मन से टेढ़ा होना, वंचक होना, ठग बनना", "to be crooked",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0637", 637, "अभ्रँ", "अभ्र्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0638", 638, "वभ्रँ", "वभ्र्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0639", 639, "मभ्रँ", "मभ्र्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0640", 640, "चरँ", "चर्",
            "गतौ भक्षणे च", "जाना", "to go, to walk, to eat, to graze",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0641", 641, "ष्ठिवुँ", "ष्ठिव्",
            "निरसने", "थूकना", "to spit",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0642", 642, "जि", "जि",
            "जये", "उत्कर्ष होना", "to prosper, to flourish",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.0643", 643, "जीवँ", "जीव्",
            "प्राणधारणे", "जीना", "to live,to revive,to live upon, to survive",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0644", 644, "पीवँ", "पीव्",
            "स्थौल्ये", "मोटा होना, स्थूल होना, पुष्ट होना", "to be fat, to be thick",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0645", 645, "मीवँ", "मीव्",
            "स्थौल्ये", "मोटा होना, स्थूल होना, पुष्ट होना", "to be fat, to be thick",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0646", 646, "तीवँ", "तीव्",
            "स्थौल्ये", "मोटा होना, स्थूल होना, पुष्ट होना", "to be fat, to be thick",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0647", 647, "णीवँ", "नीव्",
            "स्थौल्ये", "मोटा होना, स्थूल होना, पुष्ट होना", "to be fat, to be thick",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0648", 648, "क्षीवुँ", "क्षीव्",
            "निरसने", "मुँह से थूक बाहर निकलना, थूकना, कै करना", "to spit",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0649", 649, "क्षेवुँ", "क्षेव्",
            "निरसने", "थूकना, कै करना", "to spit",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0650", 650, "उर्वीँ", "ऊर्व्",
            "हिंसायाम्", "मारना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0651", 651, "तुर्वीँ", "तूर्व्",
            "हिंसायाम्", "मारना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0652", 652, "थुर्वीँ", "थूर्व्",
            "हिंसायाम्", "मारना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0653", 653, "दुर्वीँ", "दूर्व्",
            "हिंसायाम्", "मारना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0654", 654, "धुर्वीँ", "धूर्व्",
            "हिंसायाम्", "मारना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0655", 655, "गुर्वीँ", "गूर्व्",
            "उद्यमने", "उद्यम करना", "to endeavour",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0656", 656, "मुर्वीँ", "मूर्व्",
            "बन्धने", "बांधना, रोकना", "to bind, to hinder",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0657", 657, "पुर्वँ", "पूर्व्",
            "पूरणे", "पूर्ण करना", "to fill, to complete",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0658", 658, "पर्वँ", "पर्व्",
            "पूरणे", "पूर्ण करना, भरना", "to fill, to complete",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0659", 659, "मर्वँ", "मर्व्",
            "पूरणे", "पूर्ण करना, भरना", "to fill, to complete",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0660", 660, "चर्वँ", "चर्व्",
            "अदने", "खाना, चबाना", "to chew,to eat,to bite",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0661", 661, "भर्वँ", "भर्व्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0662", 662, "पर्षँ", "पर्ष्",
            "स्नेहने", "गीला होना, भीगना, चिकना होना", "to be wet, to be slippery",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0663", 663, "बहिँ", "बंह्",
            "वृद्धौ", "बढ़ना", "to grow",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0664", 664, "कर्वँ", "कर्व्",
            "दर्पे", "अहंकार करना", "to boast, to be egoistic, to be proud",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0665", 665, "खर्वँ", "खर्व्",
            "दर्पे", "अहंकार करना", "to boast, to be egoistic, to be proud",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0666", 666, "गर्वँ", "गर्व्",
            "दर्पे", "अहंकार करना", "to boast, to be egoistic, to be proud",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0667", 667, "अर्वँ", "अर्व्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0668", 668, "शर्वँ", "शर्व्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0669", 669, "षर्वँ", "सर्व्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0670", 670, "इविँ", "इन्व्",
            "व्याप्तौ", "व्याप्त होना", "to pervade,to occupy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0671", 671, "पिविँ", "पिन्व्",
            "सेवने सेचने च", "सींचना, गीला करना, सेवा करना", "to sprinkle,to make wet, to serve",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0672", 672, "मिविँ", "मिन्व्",
            "सेवने सेचने संस्रने च", "सींचना, गीला करना, सेवा करना", "to sprinkle,to make wet, to serve",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0673", 673, "णिविँ", "निन्व्",
            "सेवने सेचने संस्रने च", "सींचना, गीला करना, सेवा करना", "to sprinkle,to make wet, to serve",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0674", 674, "षिविँ", "सिन्व्",
            "सेवने सेचने च", "", "to sprinkle,to make wet, to serve",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0675", 675, "हिविँ", "हिन्व्",
            "प्रीणने", "तृप्त होना, तृप्त करना, शान्त होना", "to please,to satisfy, to calm down",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0676", 676, "दिविँ", "दिन्व्",
            "प्रीणने", "तृप्त होना, तृप्त करना, शान्त होना", "to please,to satisfy, to calm down",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0677", 677, "धिविँ", "धिन्व्",
            "प्रीणने", "तृप्त करना", "to please,to satisfy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0678", 678, "जिविँ", "जिन्व्",
            "प्रीणने", "सन्तुष्ट होना", "to be satisfied",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0679", 679, "रिविँ", "रिन्व्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0680", 680, "रविँ", "रन्व्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0681", 681, "धविँ", "धन्व्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0682", 682, "कृविँ", "कृन्व्",
            "गतौ हिंसाकरणयोश्च", "मारना, सताना, दुख देना, जाना", "to go, to kill, to hurt, to destroy, to irritate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0683", 683, "मवँ", "मव्",
            "बन्धने", "बांधना, रोकना", "to tie, to restrict, to hinder",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0684", 684, "अवँ", "अव्",
            "रक्षणगतिकान्तिप्रीतितृप्त्यवगमप्रवेशश्रवणस्वाम्यर्थयाचनक्रियेच्छादीप्त्यवाप्त्यालिङ्गनहिंसादानभागवृद्धिषु", "संरक्षण करना, जाना, कामना करना, प्रेम करना, संतुष्ट करना, आनन्दित करना, प्रवेश करना, सुनना, मालिक होना, आज्ञा करना, मांगना, कर्म करना, कान्तियुक्त होना,प्राप्त होना, आलिंगन करना, मार डालना, दुख होना, ग्रहण करना, ग्रहण होना, बढ़ाना, शक्तिमान होना", "to protect,to go, to desire, to wish, to love, to please, to satisfy, to enter, to hear, to own, to order, to command, to request, to beg, to act, to glow, to shine, to obtain, to hug, to embrace, to kill, to hurt, to grow, to gain power",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0685", 685, "धावुँ", "धाव्",
            "गतिशुद्ध्योः", "जाना, दौड़ना, स्वच्छ करना", "to run,to flow,to cleanse,to wash",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0686", 686, "धुक्षँ", "धुक्ष्",
            "सन्दीपनक्लेशनजीवनेषु", "जलाना, श्रान्त होना, थकना, जीना", "to kindle,to get tired, to be weary,to live",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0687", 687, "धिक्षँ", "धिक्ष्",
            "सन्दीपनक्लेशनजीवनेषु", "जलाना, श्रान्त होना, थकना, जीना", "to kindle,to get tired, to be weary,to live",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0688", 688, "वृक्षँ", "वृक्ष्",
            "वरणे", "योजित करना, पसन्द करना, ढकना", "to cover, to like, to prefer, to choose, to select",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0689", 689, "शिक्षँ", "शिक्ष्",
            "विद्योपादाने", "विद्या प्राप्त करना, अभ्यास करना, अध्ययन करना, सीखना", "to learn,to practise, to study, to gain knowledge",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0690", 690, "भिक्षँ", "भिक्ष्",
            "भिक्षायामलाभे लाभे च", "याचना करना, मांगना, प्राप्त करना, प्राप्त न होना", "to ask,to beg, to request",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0691", 691, "क्लेशँ", "क्लेश्",
            "अव्यक्तायां वाचि", "अस्पष्ट शब्द करना, दुख देना, सताना", "to speak inarticulately,to irritate, to give pain",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0692", 692, "दक्षँ", "दक्ष्",
            "वृद्धौ शीघ्रार्थे च", "समृद्ध होना, शीघ्र कार्य करना,चतुर होना", "to prosper, to grow, to act fastm to be quick, to be alert",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0693", 693, "दीक्षँ", "दीक्ष्",
            "मौण्ड्येज्योपनयननियमव्रतादेशेषु", "मुण्डन करना, यज्ञ करना, दीक्षा देना, उपनयन करना, आत्मनिग्रह करना, धर्म सिखाना, आदेश देना", "to shave, to perform a sacrifice, to initiate a mantra, to perform sacred thread ceremony, to practise self-restraint, to teach religious activities",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0694", 694, "ईक्षँ", "ईक्ष्",
            "दर्शने", "देखना, अवलोकन करना", "to see, to perceive",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0695", 695, "ईषँ", "ईष्",
            "गतिहिंसादर्शनेषु", "जाना, मारना, देखना", "to go, to kill, to see",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0696", 696, "भाषँ", "भाष्",
            "व्यक्तायां वाचि", "स्पष्ट बोलना", "to articulate, to explain, to elocute",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0697", 697, "वर्षँ", "वर्ष्",
            "स्नेहने", "गीला होना, भीगना, चिकना होना", "to be wet, to be slippery",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0698", 698, "गेषृँ", "गेष्",
            "अन्विच्छायाम्", "ढूँढना, पता लगाना", "to search,to investigate, to find out, to seek",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0699", 699, "ग्लेषृँ", "ग्लेष्",
            "अन्विच्छायाम्", "", "to search,to investigate, to find out, to seek",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0700", 700, "पेषृँ", "पेष्",
            "प्रयत्ने", "कोशिश करना", "to try, to attempt",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0701", 701, "एषृँ", "एष्",
            "प्रयत्ने", "", "to try, to attempt",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0702", 702, "येषृँ", "येष्",
            "प्रयत्ने", "", "to try, to attempt",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0703", 703, "जेषृँ", "जेष्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0704", 704, "णेषृँ", "नेष्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0705", 705, "एषृँ", "एष्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0706", 706, "प्रेषृँ", "प्रेष्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0707", 707, "रेषृँ", "रेष्",
            "अव्यक्ते शब्दे", "अस्पष्ट शब्द करना, हिनहिनाना", "to snicker, to sound inarticulately",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0708", 708, "हेषृँ", "हेष्",
            "अव्यक्ते शब्दे", "अस्पष्ट शब्द करना, हिनहिनाना", "to snicker, to sound inarticulately",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0709", 709, "ह्रेषृँ", "ह्रेष्",
            "अव्यक्ते शब्दे", "अस्पष्ट शब्द करना, हिनहिनाना", "to snicker, to sound inarticulately",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0710", 710, "कासृँ", "कास्",
            "शब्दकुत्सायाम्", "खांसना, खंखारना", "to cough",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0711", 711, "भासृँ", "भास्",
            "दीप्तौ", "चमकना, प्रकाशित होना", "to shine,to appear,to become evident, to glow",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0712", 712, "णासृँ", "नास्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0713", 713, "रासृँ", "रास्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0714", 714, "णसँ", "नस्",
            "कौटिल्ये", "टेढ़ा होना, वक्र होना, नम होना", "to be crooked",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0715", 715, "भ्यसँ", "भ्यस्",
            "भये", "भय करना, डरना", "to fear, to be afraid",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0716", 716, "शसिँ", "शंस्",
            "इच्छायाम्", "इच्छा करना", "to wish",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0717", 717, "ग्रसुँ", "ग्रस्",
            "अदने", "खाना, निगलना", "to eat,to devour,to swallow,to consume,to seize,to eclipse",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0718", 718, "ग्लसुँ", "ग्लस्",
            "अदने", "खाना", "to eat,to devour,to swallow,to consume,to seize,to eclipse",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0719", 719, "ईहँ", "ईह्",
            "चेष्टायाम्", "प्रयत्न करना", "to try",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0720", 720, "वहिँ", "वंह्",
            "वृद्धौ", "बढ़ना", "to grow",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0721", 721, "महिँ", "मंह्",
            "वृद्धौ", "बढ़ना", "to grow",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0722", 722, "अहिँ", "अंह्",
            "गतौ", "जाना", "to grow",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0723", 723, "गर्हँ", "गर्ह्",
            "कुत्सायाम्", "निन्दा करना, दोष लगाना", "to blame,to censure",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0724", 724, "गल्हँ", "गल्ह्",
            "कुत्सायाम्", "निन्दा करना", "to blame,to censure",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0725", 725, "बर्हँ", "बर्ह्",
            "प्राधान्ये", "श्रेष्ठ होना", "to be eminent, to be superior",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0726", 726, "बल्हँ", "बल्ह्",
            "प्राधान्ये", "श्रेष्ठ होना", "to be eminent, to be superior",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0727", 727, "वर्हँ", "वर्ह्",
            "परिभाषणहिंसाच्छादनेषु", "बोलना, मारना, दुख देना, आच्छादित करना, ढकना", "to speak,to kill,to hurt, to cover",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0728", 728, "वल्हँ", "वल्ह्",
            "परिभाषणहिंसाच्छादनेषु", "बोलना, मारना, दुख देना, आच्छादित करना, ढकना", "to speak,to kill,to hurt, to cover",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0729", 729, "प्लिहँ", "प्लिह्",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0730", 730, "वेहृँ", "वेह्",
            "प्रयत्ने", "कोशिश करना", "to endeavour, to try",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0731", 731, "जेहृँ", "जेह्",
            "प्रयत्ने गतौ च", "कोशिश करना, जाना", "to endeavour, to try",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0732", 732, "बाहृँ", "बाह्",
            "प्रयत्ने", "कोशिश करना", "to endeavour, to try",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0733", 733, "द्राहृँ", "द्राह्",
            "निद्राक्षये निक्षेपे च", "जगना, जागृत रहना", "to wake up, to stay awake",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0734", 734, "काशृँ", "काश्",
            "दीप्तौ", "चमकना", "to shine, to glow",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0735", 735, "ऊहँ", "ऊह्",
            "वितर्के", "तर्क करना, कल्पना करना", "to conjecture,to infer,to guess, to reason, to imagine, to hypothesize",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0736", 736, "गाहूँ", "गाह्",
            "विलोडने", "नष्ट करना, मर्म भेद करना", "to destroy, to counterattack",
            PadaType.ATMANEPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0737", 737, "गृहूँ", "गृह्",
            "ग्रहणे", "लेना, स्वीकार करना", "to take,to seize, to accept",
            PadaType.ATMANEPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0738", 738, "ग्लहँ", "ग्लह्",
            "ग्रहणे अपादाने च", "लेना", "to take,to seize, to accept",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0739", 739, "घषँ", "घष्",
            "कान्तिकरणे", "स्वच्छ करना, चमकाना, साफ करना", "to cleanse, to purify",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0740", 740, "घुषिँ", "घुंष्",
            "कान्तिकरणे", "स्वच्छ करना, चमकाना, साफ करना", "to cleanse, to purify",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0741", 741, "घुषिँर्", "घुष्",
            "अविशब्दने", "मन में विचार कर कहना, प्रशंसा करना, तरह तरह के शब्द करना", "to make indistinct sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0742", 742, "अक्षूँ", "अक्ष्",
            "व्याप्तौ", "व्याप्त होना", "to pervade,to reach",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0743", 743, "तक्षूँ", "तक्ष्",
            "तनूकरणे", "तेज करना, छीलना", "to sharpen, to peel off",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0744", 744, "त्वक्षूँ", "त्वक्ष्",
            "तनूकरणे", "छोटा करना", "to reduce in size",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0745", 745, "उक्षँ", "उक्ष्",
            "सेचने", "सींचना, गीला करना", "to sprinkle,to wet",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0746", 746, "रक्षँ", "रक्ष्",
            "पालने", "पालन करना", "to protect,to watch,to take care of",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0747", 747, "णिक्षँ", "निक्ष्",
            "चुम्बने", "चुम्बन लेना", "to kiss",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0748", 748, "त्रक्षँ", "त्रक्ष्",
            "गतौ", "", "to kiss",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0749", 749, "ष्ट्रक्षँ", "स्त्रक्ष्",
            "गतौ", "", "to kiss",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0750", 750, "तृक्षँ", "तृक्ष्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0751", 751, "ष्टृक्षँ", "स्तृक्ष्",
            "गतौ", "जाना", "to kiss",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0752", 752, "णक्षँ", "नक्ष्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0753", 753, "वक्षँ", "वक्ष्",
            "रोषे सङ्घाते च", "क्रोध करना, बटोरना, ढेर करना", "to be angry,to collect, to gather",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0754", 754, "मृक्षँ", "मृक्ष्",
            "सङ्घाते", "एकत्र करना, बटोरना", "to collect, to heap, to pile up",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0755", 755, "म्रक्षँ", "म्रक्ष्",
            "सङ्घाते", "", "to collect, to heap, to pile up",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0756", 756, "तक्षँ", "तक्ष्",
            "त्वचने", "आच्छादित करना, ग्रहण करना, एक ओर होना", "to cover",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0757", 757, "पक्षँ", "पक्ष्",
            "परिग्रहे", "", "to take side",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0758", 758, "सूर्क्षँ", "सूर्क्ष्",
            "आदरे", "आदर करना, सम्मान करना", "to respect, to honor",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0759", 759, "षलृँ", "सल्",
            "गतौ", "", "to go, walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0760", 760, "काक्षिँ", "काङ्क्ष्",
            "काङ्क्षायाम्", "चाहना, इच्छा करना", "to desire, to long for, to wish",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0761", 761, "वाक्षिँ", "वाङ्क्ष्",
            "काङ्क्षायाम्", "चाहना, इच्छा करना", "to desire, to long for, to wish",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0762", 762, "माक्षिँ", "माङ्क्ष्",
            "काङ्क्षायाम्", "चाहना, इच्छा करना", "to desire, to long for, to wish",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0763", 763, "द्राक्षिँ", "द्राङ्क्ष्",
            "काङ्क्षायाम् घोरवाशिते च", "कांव कांव करना, इच्छा करना, चाहना", "to desire, to long for, to wish, to caw",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0764", 764, "ध्राक्षिँ", "ध्राङ्क्ष्",
            "काङ्क्षायाम् घोरवाशिते च", "कांव कांव करना, इच्छा करना, चाहना", "to desire, to long for, to wish, to caw",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0765", 765, "ध्वाक्षिँ", "ध्वाङ्क्ष्",
            "काङ्क्षायाम् घोरवाशिते च", "कांव कांव करना, इच्छा करना, चाहना", "to desire, to long for, to wish, to caw",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0766", 766, "शुच्यीँ", "शुच्य्",
            "अभिषवे", "स्नान करना, सार निकलना, मथना, छापना", "to bathe, to give bath, to extract, to churn, to print",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0767", 767, "चूषँ", "चूष्",
            "पाने", "चूसना", "to suck",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0768", 768, "तूषँ", "तूष्",
            "तुष्टौ", "तृप्त होना", "to be satisfied, to be happy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0769", 769, "पूषँ", "पूष्",
            "वृद्धौ", "बढ़ना, पालन करना", "to grow",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0770", 770, "मूषँ", "मूष्",
            "स्तेये", "चोरी करना", "to steal to rob",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0771", 771, "लूषँ", "लूष्",
            "भूषायाम्", "शृङ्गार करना, सुशोभित करना", "to decorate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0772", 772, "रूषँ", "रूष्",
            "भूषायाम्", "शृङ्गार करना, सुशोभित करना", "to decorate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0773", 773, "शूषँ", "शूष्",
            "प्रसवे", "उत्पन्न करना, प्रसूत करना", "to deliver, to give birth",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0774", 774, "षूषँ", "सूष्",
            "प्रसवे", "", "to deliver, to give birth",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0775", 775, "यूषँ", "यूष्",
            "हिंसायाम्", "हिंसा करना, मारना", "to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0776", 776, "जूषँ", "जूष्",
            "हिंसायाम्", "हिंसा करना, मारना", "to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0777", 777, "भूषँ", "भूष्",
            "अलङ्कारे", "अंलकृत करना, सजाना", "to adorn,to decorate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0778", 778, "ग्लुहूँ", "ग्लुह्",
            "ग्रहणे", "लेना, स्वीकार करना", "to take,to seize, to accept",
            PadaType.ATMANEPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0779", 779, "ऊषँ", "ऊष्",
            "रुजायाम्", "रोगी होना, बीमार होना", "to be ill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0780", 780, "ईषँ", "ईष्",
            "उञ्छे", "एक एक दाना उठाना, बीनना", "to glean, to gather particle by particle",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0781", 781, "कषँ", "कष्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0782", 782, "खषँ", "खष्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0783", 783, "शिषँ", "शिष्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.0784", 784, "जषँ", "जष्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0785", 785, "झषँ", "झष्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0786", 786, "शषँ", "शष्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0787", 787, "वषँ", "वष्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0788", 788, "मषँ", "मष्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0789", 789, "रुषँ", "रुष्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0790", 790, "रिषँ", "रिष्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0791", 791, "भषँ", "भष्",
            "भर्त्सने", "भोंकना, कुत्ते के समान शब्द करना", "to bark,to revile, to abuse, to criticise",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0792", 792, "उषँ", "उष्",
            "दाहे", "जलना", "to burn",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0793", 793, "जिषुँ", "जिष्",
            "सेचने", "प्रोक्षण करना, सींचना", "to sprinkle",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0794", 794, "विषुँ", "विष्",
            "सेचने", "प्रोक्षण करना, सींचना", "to sprinkle",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0795", 795, "मिषुँ", "मिष्",
            "सेचने", "प्रोक्षण करना, सींचना", "to sprinkle",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0796", 796, "मुषँ", "मुष्",
            "हिंसायाम्", "मारना, हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0797", 797, "पुषँ", "पुष्",
            "पुष्टौ", "पालन करना, पोषण करना", "to nourish,to bring up,to support, to protect, to take care, to feed",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0798", 798, "श्रिषुँ", "श्रिष्",
            "दाहे", "जलाना, दग्ध करना", "to set on fire",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0799", 799, "श्लिषुँ", "श्लिष्",
            "दाहे", "जलाना, दग्ध करना", "to set on fire",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0800", 800, "प्रुषुँ", "प्रुष्",
            "दाहे", "जलाना", "to set on fire",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0801", 801, "प्लुषुँ", "प्लुष्",
            "दाहे", "जलाना", "to set on fire",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0802", 802, "पृषुँ", "पृष्",
            "सेचनहिंसासङ्क्लेशनेषु", "हिंसा करना, सींचना, कष्ट देना", "to sprinkle,to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0803", 803, "वृषुँ", "वृष्",
            "सेचनहिंसासङ्क्लेशनेषु", "सींचना, मारना, कष्ट देना", "to sprinkle,to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0804", 804, "मृषुँ", "मृष्",
            "सेचने सहने च", "सींचना, सहन करना", "to sprinkle, to suffer, to endure, to bear, to tolerate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0805", 805, "घृषुँ", "घृष्",
            "सङ्घर्षे", "लड़ाई करना", "to grind,to strike,to rub,to brush,to polish",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0806", 806, "हृषुँ", "हृष्",
            "अलीके", "झूट बोलना, मिथ्या बोलना", "to be delighted , to rejoice , to be happy, to fill with pleasure, to speak lie",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0807", 807, "तुसँ", "तुस्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0808", 808, "ह्रसँ", "ह्रस्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0809", 809, "ह्लसँ", "ह्लस्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0810", 810, "रसँ", "रस्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0811", 811, "लसँ", "लस्",
            "श्लेषणक्रीडनयोः", "आलिंगन करना, गले लगाना, खेलना, रमण करना", "to embrace,to hug, to linger, to enjoy, to come to light, to play",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0812", 812, "घसॢँ", "घस्",
            "अदने", "खाना", "to eat,to devour, to binge, to overeat",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.0813", 813, "जर्जँ", "जर्ज्",
            "परिभाषणहिंसातर्जनेषु", "बोलना, हिंसा करना, तोड़फोड़ करना", "to speak, to to blame,to kill,to reprove, to hit",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0814", 814, "चर्चँ", "चर्च्",
            "परिभाषणहिंसातर्जनेषु", "बोलना, हिंसा करना, तोड़फोड़ करना", "to speak, to to blame,to kill,to reprove, to hit",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0815", 815, "झर्झँ", "झर्झ्",
            "परिभाषणहिंसातर्जनेषु", "बोलना, हिंसा करना, तोड़फोड़ करना", "to speak, to to blame,to kill,to reprove, to hit",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0816", 816, "पिसृँ", "पिस्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0817", 817, "पेसृँ", "पेस्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0818", 818, "विसृँ", "विस्",
            "गतौ", "", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0819", 819, "वेसृँ", "वेस्",
            "गतौ", "", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0820", 820, "पिशृँ", "पिश्",
            "गतौ", "", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0821", 821, "पेशृँ", "पेश्",
            "गतौ", "", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0822", 822, "हसेँ", "हस्",
            "हसने", "हसना", "to laugh,to smile,to joke, to ridicule",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0823", 823, "णिशँ", "निश्",
            "समाधौ", "समाधौ", "to meditate upon, to think calmly",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0824", 824, "मिशँ", "मिश्",
            "शब्दे रोषकृते गतौ च", "शब्द करना, क्रोध करना, बंधना, जाना", "to make noise,to be angry, to tie, to bind, to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0825", 825, "मशँ", "मश्",
            "शब्दे रोषकृते गतौ च", "शब्द करना, क्रोध करना, जाना", "to make noise,to be angry, to tie, to bind, to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0826", 826, "शवँ", "शव्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0827", 827, "शशँ", "शश्",
            "प्लुतगतौ", "कूदते हुए जाना, फुदकते हुए जाना", "to jump,to dance, to leap",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0828", 828, "शसुँ", "शस्",
            "हिंसायाम्", "हिंसा करना, मारना", "to kill, to destroy",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0829", 829, "शन्सुँ", "शंस्",
            "स्तुतौ", "स्तुति करना, प्रशंसा करना", "to praise, to complement",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0830", 830, "चहँ", "चह्",
            "परिकल्कने", "गर्वीला होना, ठगना, दुष्कर्मी होना", "to be wicked,to be proud,to cheat, to boast",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0831", 831, "महँ", "मह्",
            "पूजायाम्", "पूजा करना", "to worship,to revere",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0832", 832, "रहँ", "रह्",
            "त्यागे", "त्यागना, छोडना", "to abandon,to quit, to leave",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0833", 833, "रहिँ", "रंह्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0834", 834, "दृहँ", "दृह्",
            "वृद्धौ", "बढ़ना, बुद्धि होना", "to grow, to prosper",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0835", 835, "दृहिँ", "दृंह्",
            "वृद्धौ", "बढ़ना", "to grow, to prosper",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0836", 836, "बृहँ", "बृह्",
            "वृद्धौ", "बढ़ना, बुद्धि होना", "to grow, to prosper",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0837", 837, "बृहिँ", "बृंह्",
            "वृद्धौ शब्दे च", "बढ़ना, शब्द करना", "to grow, to prosper, to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0838", 838, "तुहिँर्", "तुह्",
            "अर्दने", "पीड़ा करना, दुख देना, हिंसा करना", "to hurt,to kill,to pain, to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0839", 839, "दुहिँर्", "दुह्",
            "अर्दने", "पीड़ा करना, दुख देना, हिंसा करना", "to hurt,to kill,to pain, to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0840", 840, "उहिँर्", "उह्",
            "अर्दने", "पीड़ा करना", "to hurt,to kill,to pain, to kill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0841", 841, "अर्हँ", "अर्ह्",
            "पूजायाम्", "पूजा करना", "to be worthty, to be eligible, to have merit",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0842", 842, "द्युतँ", "द्युत्",
            "दीप्तौ", "चमकना, प्रकाशित होना", "to glow, to illumine",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0843", 843, "श्विताँ", "श्वित्",
            "वर्णे", "सफेद होना, शुभ्र होना", "to become white, to become clear",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0844", 844, "ञिमिदाँ", "मिद्",
            "स्नेहने", "स्नेह करना, स्निग्ध होना, पिघलना, पोतना", "to be greasy,to be soft,to melt,to love",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0845", 845, "ञिष्विदाँ", "स्विद्",
            "स्नेहनमोचनयोः गात्रप्रस्रवणे च", "चिकना करना, त्यागना", "to be greasy, to be soft, to smoothen,to quit",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0846", 846, "ञिक्ष्विदाँ", "क्ष्विद्",
            "स्नेहनमोचनयोः", "", "to be greasy, to be soft, to smoothen,to quit",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0847", 847, "रुचँ", "रुच्",
            "दीप्तावभिप्रीतौ च", "चमकना, प्रकाशित होना, आनन्द करना, प्रसन्न होना, उत्साह करना, रुचना", "to shine, to glow, to please, to like, to enjoy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0848", 848, "घुटँ", "घुट्",
            "परिवर्तने", "लोटना, पीछे आना, बदलना", "to return, to retract, to come back, to revert",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0849", 849, "रुटँ", "रुट्",
            "प्रतिघाते", "प्रतिबन्ध करना, रोकना, धकेलना, धक्का मारना", "to resist, to strike, to oppose, to push, to oppose",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0850", 850, "लुटँ", "लुट्",
            "प्रतिघाते", "प्रतिबन्ध करना, रोकना, धकेलना, धक्का मारना", "to resist, to strike, to oppose, to push, to oppose",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0851", 851, "लुठँ", "लुठ्",
            "प्रतिघाते", "प्रतिबन्ध करना, रोकना, धकेलना, धक्का मारना", "to resist, to strike, to oppose, to push, to oppose",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0852", 852, "वृहँ", "वृह्",
            "वृद्धौ", "बढ़ना, बुद्धि होना", "to grow, to prosper",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0853", 853, "शुभँ", "शुभ्",
            "दीप्तौ", "प्रकाशित होना, शोभा पाना", "to glow, to shine, to appear beautiful, to suite",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0854", 854, "क्षुभँ", "क्षुभ्",
            "सञ्चलने", "मथना, घबड़ाना, क्रोध करना, क्षुब्ध होना", "to be agitated,to shake,to disturb,to tremble,to be angry",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0855", 855, "णभँ", "नभ्",
            "हिंसायाम् अभावे च", "मारना", "to kill, to destroy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0856", 856, "तुभँ", "तुभ्",
            "हिंसायाम्", "मारना", "to kill, to destroy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0857", 857, "स्रन्सुँ", "स्रंस्",
            "अवस्रंसने", "गिरना, खिसना, अधःपतन होना", "to fall down,to drop,to slip off,to hang down, to slide, to deteriorate",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0858", 858, "ध्वन्सुँ", "ध्वंस्",
            "अवस्रंसने गतौ च", "गिरना, खिसना, अधःपतन होना, जाना", "to fall down,to drop,to slip off,to hang down, to slide, to deteriorate",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0859", 859, "भ्रन्सुँ", "भ्रंस्",
            "अवस्रंसने", "गिरना, खिसना, अधःपतन होना", "to fall down,to drop,to slip off,to hang down, to slide, to deteriorate",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0860", 860, "भ्रन्शुँ", "भ्रंश्",
            "अवस्रंसने", "", "to fall down,to drop,to slip off,to hang down, to slide, to deteriorate",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0861", 861, "स्रन्भुँ", "स्रम्भ्",
            "विश्वासे", "विश्वास करना", "to trust, to believe",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0862", 862, "वृतुँ", "वृत्",
            "वर्तने", "रहना", "to be, to happen, to be present",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0863", 863, "वृधुँ", "वृध्",
            "वृद्धौ", "बढ़ना, अधिक होना", "to increase, to grow, to prosper",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0864", 864, "शृधुँ", "शृध्",
            "उन्दने शब्दकुत्सायाम् च", "अधोवायु छोड़ना", "to moisten, to wet, to fart",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0865", 865, "स्यन्दूँ", "स्यन्द्",
            "प्रस्रवणे", "टपकना, झरना", "to ooze, to drip, to trickle",
            PadaType.ATMANEPADA, ItStatus.VET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0866", 866, "कृपूँ", "कृप्",
            "सामर्थ्ये", "शक्तिमान होना, समर्थ होना", "to be able, to be capable, to be powerful",
            PadaType.ATMANEPADA, ItStatus.VET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0867", 867, "घटँ", "घट्",
            "चेष्टायाम्", "होना, रचना करना, चेष्टा करना", "to act, to become, to arrange, to try, to make an effort",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0868", 868, "व्यथँ", "व्यथ्",
            "भयसञ्चलनयोः", "डरना, क्षुब्ध होना, दुख भोगना", "to be vexed,to fear,to be angry, to be irritated",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0869", 869, "प्रथँ", "प्रथ्",
            "प्रख्याने", "प्रसिद्ध होना, जाहीर होना, विस्तार होना", "to become famous,to arise, to declare, to manifest,to publish, to expand the scope",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0870", 870, "प्रसँ", "प्रस्",
            "विस्तारे", "विस्तार करना", "to extend,to expand",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0871", 871, "म्रदँ", "म्रद्",
            "मर्दने", "मर्दन करना, कूटना, पीसना", "to crush,to grind",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0872", 872, "स्खदँ", "स्खद्",
            "स्खदने", "जीतना, कतरना, स्थिर करना, दुख देना", "to win, to cut, to tear, to stabilize, to cause pain, to irritate",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0873", 873, "क्षजिँ", "क्षञ्ज्",
            "गतिदानयोः", "जाना, दान देना", "to go, to give",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0874", 874, "दक्षँ", "दक्ष्",
            "गतिहिंसनशासनवृद्धिशीघ्रार्थेषु", "जाना, मारना", "to go, to kill, to destroy",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0875", 875, "कृपँ", "कृप्",
            "कृपायां गतौ च", "", "to pitty, to do favor, to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0876", 876, "क्रपँ", "क्रप्",
            "कृपायां गतौ च", "दया करना, जाना", "to pitty, to do favor, to go",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0877", 877, "वृहिँ", "वृंह्",
            "वृद्धौ शब्दे च", "बढ़ना, शब्द करना", "to grow, to prosper, to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0878", 878, "कदिँ", "कन्द्",
            "वैक्लव्ये", "घबड़ाना, दुखी होना", "to be afraid, to to grieve,to cry,to shed tears",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0879", 879, "क्रदिँ", "क्रन्द्",
            "वैक्लव्ये", "घबड़ाना, दुखी होना", "to be afraid, to to grieve,to cry,to shed tears",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0880", 880, "क्लदिँ", "क्लन्द्",
            "वैक्लव्ये", "घबड़ाना, दुखी होना", "to be afraid, to to grieve,to cry,to shed tears",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0881", 881, "कदँ", "कद्",
            "वैक्लव्ये", "", "to be afraid, to to grieve,to cry,to shed tears",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0882", 882, "क्रदँ", "क्रद्",
            "वैक्लव्ये", "", "to be afraid, to to grieve,to cry,to shed tears",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0883", 883, "क्लदँ", "क्लद्",
            "वैक्लव्ये", "", "to be afraid, to to grieve,to cry,to shed tears",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0884", 884, "ञित्वराँ", "त्वर्",
            "सम्भ्रमे", "जल्दी करना, जल्दी जाना", "to hurry up",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0885", 885, "ज्वरँ", "ज्वर्",
            "रोगे", "बुखार होना, ज्वर होना, बीमार होना", "to have fever, to be ill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0886", 886, "गडँ", "गड्",
            "सेचने", "सींचना", "to irrigate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0887", 887, "हेडँ", "हेड्",
            "वेष्टने", "लपेटना", "to wrap, to cover, to surround,to clothe",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0888", 888, "वटँ", "वट्",
            "परिभाषणे", "बकना, बकबक करना", "to talk rubbish, to blabbr",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0889", 889, "भटँ", "भट्",
            "परिभाषणे", "बकना, बकबक करना", "to talk rubbish, to blabbr",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0890", 890, "णटँ", "नट्",
            "नृत्तौ गतौ च", "नाचना, नृत्य करना, अभिनय करना", "to dance,to act, to move",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0891", 891, "ष्टकँ", "स्तक्",
            "प्रतिघाते", "रोकना, हरकत करना", "to stop, to hinder, to resist, to block",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0892", 892, "चकँ", "चक्",
            "तृप्तौ", "संतुष्ट होना", "to be satisfied,to be content",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0893", 893, "कखेँ", "कख्",
            "हसने", "हसना", "to laugh",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0894", 894, "रगेँ", "रग्",
            "शङ्कायाम्", "सन्देह करना, शंका करना", "to doubt, to suspect",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0895", 895, "लगेँ", "लग्",
            "सङ्गे", "संयोग होना, मिलाप होना, स्पर्श होना, छूना", "to adhere to, to become united, to come in contact, to approach,to touch",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0896", 896, "ह्रगेँ", "ह्रग्",
            "संवरणे", "आच्छादित करना, ढकना", "to wrap, to cover, to conceal",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0897", 897, "ह्लगेँ", "ह्लग्",
            "संवरणे", "आच्छादित करना, ढकना", "to wrap, to cover, to conceal",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0898", 898, "षगेँ", "सग्",
            "संवरणे", "ढकना", "to wrap, to cover, to conceal",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0899", 899, "ष्टगेँ", "स्तग्",
            "संवरणे", "ढकना", "to wrap, to cover, to conceal",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0900", 900, "कगेँ", "कग्",
            "अनेकार्थाः", "क्रिया करना", "to do some action",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0901", 901, "अकँ", "अक्",
            "कुटिलायां गतौ", "टेढ़ा चलना", "to move tortuously, to limp",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0902", 902, "अगँ", "अग्",
            "कुटिलायां गतौ", "टेढ़ा चलना", "to move tortuously, to limp",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0903", 903, "कणँ", "कण्",
            "गतौ शब्दे च", "जाना, शब्द करना", "to go, to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0904", 904, "रणँ", "रण्",
            "गतौ शब्दे च", "जाना, शब्द करना", "to go, to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0905", 905, "चणँ", "चण्",
            "दाने गतौ च", "दान देना, जाना", "to go, to give",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0906", 906, "शणँ", "शण्",
            "दाने गतौ च", "दान देना, जाना", "to go, to give",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0907", 907, "श्रणँ", "श्रण्",
            "दाने गतौ च", "दान देना, जाना", "to go, to give",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0908", 908, "श्रथँ", "श्रथ्",
            "हिंसायाम्", "मारना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0909", 909, "ष्ठगेँ", "स्थग्",
            "संवरणे", "ढकना", "to wrap, to cover, to conceal",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0910", 910, "श्लथँ", "श्लथ्",
            "हिंसायाम्", "", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0911", 911, "क्नथँ", "क्नथ्",
            "हिंसायाम्", "मारना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0912", 912, "क्रथँ", "क्रथ्",
            "हिंसायाम्", "मारना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0913", 913, "क्लथँ", "क्लथ्",
            "हिंसायाम्", "मारना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0914", 914, "चनँ", "चन्",
            "हिंसायाम्", "", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0915", 915, "वनँ", "वन्",
            "हिंसायाम्", "हिंसा करना", "to kill, to destroy, to hurt",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0916", 916, "ज्वलँ", "ज्वल्",
            "दीप्तौ", "प्रकाशित होना, प्रकाशित करना, जलाना, जलना", "to shine, to glow, to blaze, to flame",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0917", 917, "ह्वलँ", "ह्वल्",
            "चलने", "चलना", "to walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0918", 918, "ह्मलँ", "ह्मल्",
            "चलने", "चलना", "to walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0919", 919, "स्मृ", "स्मृ",
            "आध्याने", "स्मरण करना, याद करना", "to remember,to recollect,to think upon,to recite mentally,to memorize",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.0920", 920, "दॄ", "दॄ",
            "भये", "डरना, भय करना", "to be afraid",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0921", 921, "नॄ", "नॄ",
            "नये", "ले जाना", "to carry",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0922", 922, "श्रा", "श्रा",
            "पाके", "पकाना", "to cook,to boil",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.0923", 923, "ज्ञा", "ज्ञा",
            "मारणतोषणनिशामनेषु", "मारना,चोट पोहोचाना, तृप्त होना, अनुभव करना, समझना", "to kill, to hurt, to please, to satisfy, to perceive, to understand",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0924", 924, "चलँ", "चल्",
            "कम्पने", "कांपना, हिलना", "to move, to shake, to walk",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0925", 925, "छदिः", "छद्",
            "ऊर्जने", "बलवान होना, बलवान करना", "to be powerful, to be energized",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0926", 926, "लडँ", "लड्",
            "विलासे जिह्वोन्मथने च", "क्रीड़ा करना, मौज करना", "to enjoy, to play",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0927", 927, "मदीँ", "मद्",
            "हर्षग्लेपनयोः", "हर्षित होना, थकना, श्रान्त होना", "to enjoy, to be happy, to be tired, to be distressed",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0928", 928, "ध्वनँ", "ध्वन्",
            "शब्दे", "शब्द करना, आवाज करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0929", 929, "शमोँ", "शम्",
            "दर्शने", "देखना", "to look at, to inspect",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0930", 930, "यमँ", "यम्",
            "अपरिवेषणे", "उपवास रखना, न खाना", "to fast, to avoid eating",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0931", 931, "स्खदिँर्", "स्खद्",
            "विद्रावणे विदारणे च", "नष्ट करना, काटना, तोडना", "to destroy,to cut, to break apart",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0932", 932, "स्वनँ", "स्वन्",
            "अवतंसने", "सजाना, सुशोभित करना", "to decorate, to beautify",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0933", 933, "-", "घटादयो मितः",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "01.0934", 934, "-", "जनीजॄष्क्नसुरञ्जोऽमन्ताश्च",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "01.0935", 935, "-", "ज्वलह्वलह्मलनमामनुपसर्गाद्वा",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "01.0936", 936, "-", "ग्लास्नावनुवमां च",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "01.0937", 937, "-", "न कम्यमिचमाम्",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "01.0938", 938, "-", "शमो दर्शने",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "01.0939", 939, "-", "यमोऽपरिवेषणे",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "01.0940", 940, "-", "स्खदिरवपरिभ्यां च",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "01.0941", 941, "-", "घटादयः षितः",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "01.0942", 942, "तृहँ", "तृह्",
            "वृद्धौ", "बढ़ना, बुद्धि होना", "to grow, to prosper",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0943", 943, "तृहिँ", "तृंह्",
            "वृद्धौ", "बढ़ना", "to grow, to prosper",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0944", 944, "-", "दलिवलिस्खलिरणिध्वनित्रपिक्षपयश्चेति भोजः",
            "", "", "",
            null, null, null, null,
        )
        dhatu(
            "01.0945", 945, "रुगिँ", "रुङ्ग्",
            "वर्जने", "त्याग देना, छोड़ देना", "to abandon,to desert, to leave",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0946", 946, "खुडिँ", "खुण्ड्",
            "गतिवैकल्ये", "रोकना", "to halt, to stop",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0947", 947, "म्लेडृँ", "म्लेड्",
            "उन्मादे", "पागल होना", "to be mad, to be mentally ill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0948", 948, "मेटृँ", "मेट्",
            "उन्मादे", "", "to be mad, to be mentally ill",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0949", 949, "बिडँ", "बिड्",
            "आक्रोशे", "शाप देना, गाली देना", "to swear,to curse,to shout, to abuse",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0950", 950, "मक्षँ", "मक्ष्",
            "रोषे सङ्घाते च", "क्रोध करना, बटोरना, ढेर करना", "to be angry,to collect, to gather",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0951", 951, "वनुँ", "वन्",
            "अनेकार्थत्वे", "अनेक अर्थों में प्रयुक्त", "various meanings",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0952", 952, "वाहृँ", "वाह्",
            "प्रयत्ने", "कोशिश करना", "to endeavour, to try",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0953", 953, "रमँ", "रम्",
            "क्रीडायाम्", "रमण करना, क्रीड़ा करना, खेलना", "to enjoy, to rejoice, to play",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.0954", 954, "डुयाचृँ", "याच्",
            "याच्ञायाम्", "याचना करना, मांगना", "to beg,to beg,to ask,to solicit",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.DVIKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0955", 955, "फणँ", "फण्",
            "गतिदीप्त्योः", "जाना, तेजोहीन करना", "to go,to take away charm",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0956", 956, "राजृँ", "राज्",
            "दीप्तौ", "चमकना, सुशोभित होना", "to shine, to glitter, to glow, to reign, to be lustrous",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0957", 957, "टुभ्राजृँ", "भ्राज्",
            "दीप्तौ", "चमकना, सुशोभित होना", "to shine, to glitter, to glow, to reign, to be lustrous",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0958", 958, "टुभ्राशृँ", "भ्राश्",
            "दीप्तौ", "चमकना", "to shine, to glitter, to glow, to reign, to be lustrous",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0959", 959, "टुभ्लाशृँ", "भ्लाश्",
            "दीप्तौ", "चमकना", "to shine, to glitter, to glow, to reign, to be lustrous",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0960", 960, "स्यमुँ", "स्यम्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0961", 961, "स्वनँ", "स्वन्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0962", 962, "ध्वनँ", "ध्वन्",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0963", 963, "षमँ", "सम्",
            "अवैकल्ये", "नहीं घबड़ाना, एक समान रहना", "to be calm, to be coolheaded",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0964", 964, "ष्टमँ", "स्तम्",
            "अवैकल्ये", "नहीं घबड़ाना, एक समान रहना", "to be calm, to be coolheaded",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0965", 965, "ज्वलँ", "ज्वल्",
            "दीप्तौ", "चमकना, सुशोभित होना", "to blaze,to glow,to shine, to burn",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0966", 966, "चलँ", "चल्",
            "कम्पने", "हिलना, चलना, कांपना", "to move, to walk, to shake",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0967", 967, "जलँ", "जल्",
            "घातने", "तीक्ष्ण होना, तेजस्वी होना, पैना होना", "to be sharp,to be pointed",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0968", 968, "टलँ", "टल्",
            "वैक्लव्ये", "विह्वल होना, दुखित होना, हृदरोगी होना", "to be confused, to be distressed",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0969", 969, "ट्वलँ", "ट्वल्",
            "वैकल्ये", "व्याकुल होना", "to be confused, to be distressed",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0970", 970, "ष्ठलँ", "स्थल्",
            "स्थाने", "स्थिर होना, थमना, स्तब्ध होना, खड़ा होना", "to stand firm, to stand, to be stunned",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0971", 971, "हलँ", "हल्",
            "विलेखने", "जोतना, हल चलाना", "to plow, to cultivate, to till, to furrow",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0972", 972, "णलँ", "नल्",
            "गन्धे बन्धने च", "सूंघना, बास आना, बांधना, हिंसा करना'", "to smell,to bind",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0973", 973, "पलँ", "पल्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0974", 974, "बलँ", "बल्",
            "प्राणने धान्यावरोधने च", "बल युक्त होना, जीना, धान्य संचय करना, द्रव्य को रोकना", "to breathe, to be powerful, to collect grains",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0975", 975, "पुलँ", "पुल्",
            "महत्त्वे", "राशि होना, ढेर होना, बढ़ना, ऊँचा होना", "to be great, to pile up, to grow, to be large",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0976", 976, "कुलँ", "कुल्",
            "संस्त्याने बन्धुषु च", "बटोरना, अपने के समान वर्तना, सजातीयता से रहना", "to collect, to gather, to treat equally, to treat like family",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0977", 977, "शलँ", "शल्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0978", 978, "हुलँ", "हुल्",
            "गतौ हिंसायां संवरणे च", "", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0979", 979, "पतॢँ", "पत्",
            "गतौ", "नीचे जाना, गिरना, उतरना, अमानवी पराक्रम करना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0980", 980, "क्षलँ", "क्षल्",
            "सञ्चलने", "टपकना, झरना, चूना", "to flow,to ooze,to drip",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0981", 981, "क्वथेँ", "क्वथ्",
            "निष्पाके", "उबालना, पकाना, काढ़ा बनाना", "to boil,to cook, to decoct",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0982", 982, "पथेँ", "पथ्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0983", 983, "मथेँ", "मथ्",
            "विलोडने", "मथना, विचार करना, मनन करना", "to churn, to think, to meditate, to ponder",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.DVIKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0984", 984, "टुवमँ", "वम्",
            "उद्गिरणे", "कै होना, वमन होना", "to vomit,to drop,to emit,to give out,to be sick",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0985", 985, "भ्रमुँ", "भ्रम्",
            "चलने", "चलना, घूमना, भ्रमण करना", "to walk, to wander, to roam",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0986", 986, "क्षरँ", "क्षर्",
            "सञ्चलने", "टपकना, झरना, चूना", "to flow,to ooze,to drip",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0987", 987, "द्वृ", "द्वृ",
            "स्थगने", "ढकना, घिरना, छिपाना", "to cover,to surround, to conceal",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.0988", 988, "षहँ", "सह्",
            "मर्षणे", "क्षमा करना, सहन करना", "to tolerate, to bear, to forgive",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0989", 989, "रमुँ", "रम्",
            "क्रीडायाम्", "रमण करना, क्रीड़ा करना, खेलना", "to enjoy, to rejoice, to play",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.0990", 990, "षदॢँ", "सद्",
            "विशरणगत्यवसादनेषु", "जाना, चलना, शक्तिहीन होना, म्लान होना, सूखना, मुरझाना, नष्ट करना", "to go, to lose power, to be tired, to dry up, to destroy, to wither, to despond, to lose interest",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.0991", 991, "शदॢँ", "शद्",
            "शातने", "जीर्ण होना, धीरे धीरे कम होना, मुरझाना, गिरना, नीचे फेकना", "to perish,to fall,to wither,to decay, to wear off, to fall, to wane,to throw down",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.0992", 992, "क्रुशँ", "क्रुश्",
            "आह्वाने रोदने च", "पुकारना, रोना", "to call out, to shout, to cry",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.0993", 993, "कुचँ", "कुच्",
            "सम्पर्चनकौटिल्यप्रतिष्टम्भविलेखनेषु", "संपर्क करना, स्वच्छ करना, स्पर्श करना, टेढ़ा होना, टेढ़ा लिखना, रेखा खींचना", "to contract,to cleanse, to touch, to shrink,to be crooked,to bend, to write, to delineate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0994", 994, "बुधँ", "बुध्",
            "अवगमने", "जानना, समझना", "to know,to understand,to be awakened with knowledge, to be restored to senses,to think, to learn",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0995", 995, "रुहँ", "रुह्",
            "बीजजन्मनि प्रादुर्भावे च", "बीज से उत्पन्न होना, बीज का उगाना, जन्म होना, जन्मा लेना", "to grow from seed,to manifest, to rise,to ascend, to take birth, to be born",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.0996", 996, "कसँ", "कस्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0997", 997, "हिक्कँ", "हिक्क्",
            "अव्यक्ते शब्दे", "अस्पष्ट बोलना, हिचकी आना", "to hiccup, to mutter",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0998", 998, "अन्चुँ", "अञ्च्",
            "गतौ याचने च", "जाना, मांगना", "to go, to beg,to ask,to solicit",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.0999", 999, "अचुँ", "अच्",
            "गतौ याचने च", "", "to go, to beg,to ask,to solicit",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1000", 1000, "अचिँ", "अञ्च्",
            "गतौ याचने च", "", "to go, to beg,to ask,to solicit",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1001", 1001, "टुयाचृँ", "याच्",
            "याच्ञायाम्", "याचना करना, मांगना", "to beg,to beg,to ask,to solicit",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.DVIKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1002", 1002, "रेटृँ", "रेट्",
            "परिभाषणे", "पक्षियोंका बोलना", "to speak,to ask,to request",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1003", 1003, "चतेँ", "चत्",
            "याचने", "मांगना", "to beg,to beg,to ask,to solicit",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1004", 1004, "चदेँ", "चद्",
            "याचने", "मांगना", "to beg,to beg,to ask,to solicit",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1005", 1005, "प्रोथृँ", "प्रोथ्",
            "पर्याप्तौ", "शक्तिमान होना, योग्य होना, पूर्ण होना", "to be powerful, to be capable, to be competent",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1006", 1006, "मिदृँ", "मिद्",
            "मेधाहिंसनयोः", "समझना, जानना, पीड़ा देना, दुःख देना, हानि करना", "to understand, to know, to hurt, to injure, to cause trouble",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1007", 1007, "मेदृँ", "मेद्",
            "मेधाहिंसनयोः सङ्गमे च", "समझना, हिंसा करना", "to understand, to know, to hurt, to injure, to cause trouble, to collect, to merge together",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1008", 1008, "मिथृँ", "मिथ्",
            "मेधाहिंसनयोः", "", "to understand, to know, to hurt, to injure, to cause trouble",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1009", 1009, "मेथृँ", "मेथ्",
            "मेधाहिंसनयोः", "", "to understand, to know, to hurt, to injure, to cause trouble",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1010", 1010, "मिधृँ", "मिध्",
            "मेधाहिंसनयोः", "", "to understand, to know, to hurt, to injure, to cause trouble",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1011", 1011, "मेधृँ", "मेध्",
            "मेधाहिंसनयोः सङ्गमे च", "इकठ्ठा करना, संगती करना, मेल करना, समझना, मारना, दुःख देना", "to understand, to know, to hurt, to injure, to cause trouble, to join, to unite",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1012", 1012, "णिदृँ", "निद्",
            "कुत्सासन्निकर्षयोः", "दोष लगाना, निंदा करना, समीप जाना या आना", "to ridicule, to insult, to reach, to go closer",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1013", 1013, "णेदृँ", "नेद्",
            "कुत्सासन्निकर्षयोः", "दोष लगाना, निंदा करना, समीप जाना या आना", "to ridicule, to insult, to reach, to go closer",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1014", 1014, "शृधुँ", "शृध्",
            "उन्दने", "मार डालना, दुःख देना, आर्द्र करना, गीला करना या होना", "to kill, to hurt, to wet, to moist",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1015", 1015, "मृधुँ", "मृध्",
            "उन्दने", "मार डालना, दुःख देना, आर्द्र करना, गीला करना या होना", "to kill, to hurt, to wet, to moist",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1016", 1016, "बुधिँर्", "बुध्",
            "बोधने", "जानना, समझना", "to know,to understand,to be awakened with knowledge, to be restored to senses,to think, to learn",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1017", 1017, "उँबुन्दिँर्", "बुन्द्",
            "निशामने", "जानना, समझना, सूक्ष्मदृष्टि से जानना", "to perceive,to apprehend, to comprehend, to understand with keen senses",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1018", 1018, "वेणृँ", "वेण्",
            "गतिज्ञानचिन्तानिशामनवादित्रग्रहणेषु", "जाना, समझना, जानना, स्मरण करना, याद करना, विचार करना, तारतम्य देखना, वाद्ययंत्र बजाना, वाद्ययंत्र हाथ में लेना", "to go,to know,to understand, to remember, to memorize, to think, to play a musical instrument, to pick a musical instrument",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1019", 1019, "वेनृँ", "वेन्",
            "गतिज्ञानचिन्तानिशामनवादित्रग्रहणेषु", "", "to go,to know,to understand, to remember, to memorize, to think, to play a musical instrument, to pick a musical instrument",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1020", 1020, "खनुँ", "खन्",
            "अवदारणे", "खोदना", "to dig",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1021", 1021, "चीवृँ", "चीव्",
            "आदानसंवरणयोः", "लेना, पहना, पकड़ना", "to accept, to takem to wear, to hold",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1022", 1022, "चीपृँ", "चीप्",
            "आदानसंवरणयोः", "", "to accept, to takem to wear, to hold",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1023", 1023, "चायृँ", "चाय्",
            "पूजानिशामनयोः", "पूजा करना, जानना, समझना", "to worship, to know, to understand",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1024", 1024, "व्ययँ", "व्यय्",
            "वित्तत्यागे गतौ च", "जाना, खर्च करना", "to spend, to do an expense, to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1025", 1025, "दाशृँ", "दाश्",
            "दाने", "देना, आहुति देना", "to give,to offer an oblation",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1026", 1026, "भेषृँ", "भेष्",
            "भये", "डरना, जाना", "to fear",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1027", 1027, "भ्रेषृँ", "भ्रेष्",
            "गतौ", "जाना", "to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1028", 1028, "भ्लेषृँ", "भ्लेष्",
            "गतौ", "जाना", "to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1029", 1029, "असँ", "अस्",
            "गतिदीप्त्यादानेषु", "जाना, चमकना, लेना", "to go, to shine, to glow, to take",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1030", 1030, "अषँ", "अष्",
            "गतिदीप्त्यादानेषु", "", "to go, to shine, to glow, to take",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1031", 1031, "यमँ", "यम्",
            "उपरमे", "प्रतिबन्ध करना, रोकना", "to stop, to resist, to hinder",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1032", 1032, "स्पशँ", "स्पश्",
            "बाधनस्पर्शनयोः", "अवरोध करना, रोकना, एकत्र करना, स्पर्श करना, छूना", "to obstruct,to stop, to hinder, to touch, to collect",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1033", 1033, "लषँ", "लष्",
            "कान्तौ", "इच्छा करना, चाहना", "to wish, to desire",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1034", 1034, "चषँ", "चष्",
            "भक्षणे", "खाना, स्वाद लेना", "to eat, to taste",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1035", 1035, "छषँ", "छष्",
            "हिंसायाम्", "मारना", "to kill, to destroy, to hurt, to injure",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1036", 1036, "झषँ", "झष्",
            "आदानसंवरणयोः", "ग्रहण करना, लेना, वस्त्रादि धारण करना, वस्त्र पहनना", "to hurt,to injure,to kill,to take,to put on, to wear",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1037", 1037, "भ्रक्षँ", "भ्रक्ष्",
            "अदने", "खाना", "to eat",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1038", 1038, "भ्लक्षँ", "भ्लक्ष्",
            "आदानसंवरणयोः", "खाना", "to eat",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1039", 1039, "भक्षँ", "भक्ष्",
            "अदने", "", "to eat",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1040", 1040, "प्लक्षँ", "प्लक्ष्",
            "अदने", "", "to eat",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1041", 1041, "दासृँ", "दास्",
            "दाने", "देना, सौंपना", "to give, to handover",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1042", 1042, "माहृँ", "माह्",
            "माने", "नापना, गिनना, तौलना", "to measure, to weigh",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1043", 1043, "गुहूँ", "गुह्",
            "संवरणे", "छिपाना, वस्त्रादि से ढकना", "to cover, to hide, to clothe",
            PadaType.UBHAYAPADA, ItStatus.VET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1044", 1044, "श्रिञ्", "श्रि",
            "सेवायाम्", "सेवा करना", "to serve",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1045", 1045, "भृञ्", "भृ",
            "भरणे", "पूर्ण करना, भरण-पोषण करना, भरना", "to nourish, to protect, to fill, to maintain, to protect, to upbring",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1046", 1046, "हृञ्", "हृ",
            "हरणे", "ले जाना, हरण करना, चोरी करना", "to take way, to carry, to steal, to acquire",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.DVIKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1047", 1047, "धृञ्", "धृ",
            "धारणे", "धारण करना, उद्धार करना", "to wear, to support,to possess, to hold",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1048", 1048, "षूर्क्ष्यँ", "सूर्क्ष्य्",
            "ईर्ष्यायाम्", "ईर्ष्या करना, द्वेष करना", "to envy, to grudge",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1049", 1049, "णीञ्", "नी",
            "प्रापणे", "प्राप्त होना, ले जाना, पाना", "to obtain, to carry, to take",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.DVIKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1050", 1050, "धेट्", "धे",
            "पाने", "प्राशन करना, पीना", "to drink",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1051", 1051, "ग्लै", "ग्लै",
            "हर्षक्षये", "म्लान होना, ग्लानियुक्त होना, जम्हाई लेना", "to be tired, to wane, to be fatigued, to be exhausted",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1052", 1052, "म्लै", "म्लै",
            "हर्षक्षये", "ग्लान होना, ग्लानियुक्त होना, जम्हाई लेना", "to be tired, to wane, to be fatigued, to be exhausted",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1053", 1053, "द्यै", "द्यै",
            "न्यक्करणे", "धिक्कार करना, तिरस्कार करना", "to hate, to despise,to disfigure, to condemn",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1054", 1054, "द्रै", "द्रै",
            "स्वप्ने", "सोना, नींद लेना", "to sleep",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1055", 1055, "ध्रै", "ध्रै",
            "तृप्तौ", "तृप्त होना, संतुष्ट होना", "to be pleased, to be satisfied",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1056", 1056, "ध्यै", "ध्यै",
            "चिन्तायाम्", "ध्यान करना, चिन्तन करना, मनन करना, विचार करना", "to think,to meditate,to recollect, to concentrate  upon",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1057", 1057, "रै", "रै",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1058", 1058, "स्त्यै", "स्त्यै",
            "शब्दसङ्घातयोः", "शब्द करना, आवाज करना, भीड़ होना, घेरना", "to sound, to gather, to be crowded, to surround",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1059", 1059, "ष्ट्यै", "स्त्यै",
            "शब्दसङ्घातयोः", "शब्द करना, भीड़ होना", "to sound, to gather, to be crowded, to surround",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1060", 1060, "खै", "खै",
            "खदने", "खोदना, सताना, दुख देना, स्थिर रहना", "to dig, to irritate, to cause pain, to be steady",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1061", 1061, "क्षै", "क्षै",
            "क्षये", "नष्ट होना, ह्रास होना, कम होना, म्लान होना", "to wane,to decline,to decay,to reduce,to contract, to shrink",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1062", 1062, "जै", "जै",
            "क्षये", "नष्ट होना, ह्रास होना, कम होना, म्लान होना", "to wane,to decline,to decay,to reduce,to contract, to shrink",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1063", 1063, "षै", "सै",
            "क्षये", "नष्ट होना, ह्रास होना, कम होना, म्लान होना", "to wane,to decline,to decay,to reduce,to contract, to shrink",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1064", 1064, "कै", "कै",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1065", 1065, "गै", "गै",
            "शब्दे", "शब्द करना, गाना", "to sing",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1066", 1066, "शै", "शै",
            "पाके", "पकाना, पकना", "to cook",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1067", 1067, "श्रै", "श्रै",
            "पाके", "पकना, पकाना", "to cook",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1068", 1068, "स्रै", "स्रै",
            "पाके", "", "to cook",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1069", 1069, "पै", "पै",
            "शोषणे", "सूखना, कुम्हलाना", "to dry,to wither",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1070", 1070, "ओँवै", "वै",
            "शोषणे", "सुखना, सुखाना, शुष्क होना", "to dry,to wither",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1071", 1071, "ष्टै", "स्तै",
            "वेष्टने शोभायां च", "घेरना, लपेटना, वेष्टित करना, सुशोभित होना", "to cover, to wrap, to decorate",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1072", 1072, "ष्णै", "स्नै",
            "वेष्टने शोभायां च", "घेरना, इकठ्ठा करना, शोभित होना", "to cover, to wrap, to decorate",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1073", 1073, "दैप्", "दै",
            "शोधने", "शुद्ध करना", "to purify,to cleanse",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1074", 1074, "पा", "पा",
            "पाने", "पीना", "to drink",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1075", 1075, "घ्रा", "घ्रा",
            "गन्धोपादाने घ्राणे च", "सूंघना", "to smell",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1076", 1076, "ध्मा", "ध्मा",
            "शब्दाग्निसंयोगयोः", "फूंकना, आग सुलगाना, प्रदीप्त करना, जलाना, मुँह से बन्सी आदि वाद्य बजाना", "to blow,to breathe out,to produce sound by blowing,to blow a fire,to manufacture by blowing, to play a conch",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1077", 1077, "ष्ठा", "स्था",
            "गतिनिवृत्तौ", "स्थिर होना, ठहरना", "to stay, to stand",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1078", 1078, "म्ना", "म्ना",
            "अभ्यासे", "विचार करना, मनन करना", "to study,to repeat,to remember,to think",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1079", 1079, "दाण्", "दा",
            "दाने", "देना", "to give",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1080", 1080, "ह्वृ", "ह्वृ",
            "कौटिल्ये", "वक्र होना, टेढ़ा होना", "to be crooked",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1081", 1081, "स्वृ", "स्वृ",
            "शब्दोपतापयोः", "शब्द करना, रोगी होना, दुःख देना, सताना", "to sound, to be ill, to irritate, to trouble",
            PadaType.PARASMAIPADA, ItStatus.VET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1082", 1082, "स्मृ", "स्मृ",
            "चिन्तायाम्", "स्मरण करना, याद करना", "to remember,to recollect,to memorize",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1083", 1083, "वृ", "वृ",
            "संवरणे", "ढकना, घिरना, छिपाना", "to cover,to surround, to conceal",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1084", 1084, "ह्वृ", "ह्वृ",
            "संवरणे", "", "to cover, to hide, to enclose",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1085", 1085, "सृ", "सृ",
            "गतौ", "जाना, सरकना", "to go,to move,to approach,to slip",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1086", 1086, "ऋ", "ऋ",
            "गतिप्रापणयोः", "जाना, संपादन करना, प्राप्त करना, मिलाना, पहुंचना", "to go, to obtain, to reach",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1087", 1087, "गृ", "गृ",
            "सेचने", "सींचना, गीला करना", "to irrigate, to moisten, to wet",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1088", 1088, "घृ", "घृ",
            "सेचने", "सींचना, गीला करना", "to irrigate, to moisten, to wet",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1089", 1089, "ध्वृ", "ध्वृ",
            "हूर्छने", "टेढ़ा करना, नवाना, वक्र करना", "to bend,to twist",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1090", 1090, "स्रु", "स्रु",
            "गतौ", "जाना, टपकना, झरना, चूना, बहना", "to go,to drip, to ooze, to flow",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1091", 1091, "षु", "सु",
            "प्रसवैश्वर्ययोः", "उत्पन्न करना, पैदा करना, गर्भ धारण करना, अद्भुत सामर्थ्य होना, अमानवी पराक्रम होना", "to give birth, to become pregnant, to conceive, to possess power, to possess supremacy",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1092", 1092, "श्रु", "श्रु",
            "श्रवणे", "सुनना, श्रवण करना", "to hear, to listen",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1093", 1093, "ध्रु", "ध्रु",
            "स्थैर्ये", "अचल होना, स्थिर होना", "to be firm, to be fixed",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1094", 1094, "दु", "दु",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1095", 1095, "द्रु", "द्रु",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1096", 1096, "जि", "जि",
            "अभिभवे (न्यूनीभवने न्यूनीकरणे च)", "जीतना, पराभव करना", "to win, to conquer, to dislike, to refrain",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.DVIKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1097", 1097, "ज्रि", "ज्रि",
            "अभिभवे (न्यूनीभवने न्यूनीकरणे च)", "जीतना", "to conquer,to win, to have victory",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1098", 1098, "जृ", "जृ",
            "गतौ", "", "to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1099", 1099, "ष्मिङ्", "स्मि",
            "ईषद्धसने", "मुस्कुराना, मंद हास्य करना", "to smile a bit",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1100", 1100, "गुङ्", "गु",
            "अव्यक्ते शब्दे", "अस्पष्ट बोलना", "to speak inarticulately",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1101", 1101, "गाङ्", "गा",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1102", 1102, "उङ्", "उ",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1103", 1103, "कुङ्", "कु",
            "शब्दे", "शब्द करना, अस्पष्ट बोलना,", "to hum, to speak inarticulately, to sound",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1104", 1104, "खुङ्", "खु",
            "शब्दे", "", "to sound",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1105", 1105, "गुङ्", "गु",
            "शब्दे", "", "to speak, to make noise",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1106", 1106, "घुङ्", "घु",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1107", 1107, "ङुङ्", "ङु",
            "शब्दे", "शब्द करना", "to sound",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1108", 1108, "च्युङ्", "च्यु",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1109", 1109, "ज्युङ्", "ज्यु",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1110", 1110, "छ्युङ्", "छ्यु",
            "गतौ", "", "to go",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1111", 1111, "प्रुङ्", "प्रु",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1112", 1112, "प्लुङ्", "प्लु",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1113", 1113, "क्लुङ्", "क्लु",
            "गतौ", "", "to go",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1114", 1114, "रुङ्", "रु",
            "गतिरेषणयोः", "जाना, चलना,मारना, दुःख देना, क्रोध करना", "to go, to move, to kill, to be angry, to cause pain",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1115", 1115, "धृङ्", "धृ",
            "अवबन्धने विध्वंसने च", "गिर पड़ना, नष्ट होना", "to fall down, to get destroyed",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1116", 1116, "मेङ्", "मे",
            "प्रणिदाने", "विनिमय करना, सुपुर्द करना", "to exchange, to handover",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1117", 1117, "देङ्", "दे",
            "रक्षणे", "रक्षण करना, पोषण करना", "to protect",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1118", 1118, "श्यैङ्", "श्यै",
            "गतौ", "जाना", "to go",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1119", 1119, "प्यैङ्", "प्यै",
            "वृद्धौ", "बढ़ना, उन्नति होना", "to grow, to increase, to swell;",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1120", 1120, "त्रैङ्", "त्रै",
            "पालने", "पालन करना, रक्षा करना", "to protect, to secure",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1121", 1121, "पूङ्", "पू",
            "पवने", "पवित्र बनना या होना", "to purify, to cleanse",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1122", 1122, "मूङ्", "मू",
            "बन्धने", "बांधना, अकड़ना", "to tie, to get stuck",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1123", 1123, "डीङ्", "डी",
            "विहायसा गतौ", "आकाश में उड़ना", "to fly",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1124", 1124, "तॄ", "तॄ",
            "प्लवनतरणयोः", "पर जाना, तैरना", "to cross, to float, to swim",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1125", 1125, "गुपँ", "गुप्",
            "गोपने निन्दायां च", "बचाना, संरक्षण करना, छिपाना, दोष लगाना, निन्दा करना", "to protect, to conceal, to hide, to blame, to criticize",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1126", 1126, "तिजँ", "तिज्",
            "निशाने क्षमायाम् च", "तीक्ष्ण करना, धार लगाना, चमकाना, क्षमा करना, सहना", "to sharpen, to whet, to endure, to tolerate, to bear, to forgive",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1127", 1127, "मानँ", "मान्",
            "पूजायाम् जिज्ञासायां च", "ज्ञान प्राप्ति की इच्छा करना", "to worship, to investigate,to examine,to wish to know",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1128", 1128, "बधँ", "बध्",
            "बन्धने चित्तविकारे च", "बांधना, निन्दा करना, द्वेष करना", "to bind,to restrain,to loathe,to hate, to criticize",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1129", 1129, "रभँ", "रभ्",
            "राभस्ये", "आनन्द करना, प्रसन्न होना", "to be happy, to be glad",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1130", 1130, "डुलभँष्", "लभ्",
            "प्राप्तौ", "प्राप्त होना, मिलना", "to get,to obtain,to take,to have,to find",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1131", 1131, "ष्वन्जँ", "स्वञ्ज्",
            "परिष्वङ्गे", "आलिंगन करना, गले लगाना", "to hug,to embrace",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1132", 1132, "हदँ", "हद्",
            "पुरीषोत्सर्गे", "मल त्याग करना", "to excrete",
            PadaType.ATMANEPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1133", 1133, "ञिष्विदाँ", "स्विद्",
            "अव्यक्ते शब्दे", "अस्पष्ट शब्द करना", "to be greedy,to perspire,to be disturbed,to quit",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1134", 1134, "स्कन्दिँर्", "स्कन्द्",
            "गतिशोषणयोः", "जाना, शोषण करना", "to go, to dry",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1135", 1135, "यभँ", "यभ्",
            "मैथुने", "मैथुन करना", "to copulate",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1136", 1136, "णमँ", "नम्",
            "प्रह्वत्वे शब्दे च", "नमस्कार करना, वंदन करना, सम्मान देना, नमना, शब्द करना", "to salute,to greet, to respect, to bend, to sound",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1137", 1137, "गमॢँ", "गम्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1138", 1138, "सृपॢँ", "सृप्",
            "गतौ", "जाना", "to go",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1139", 1139, "यमँ", "यम्",
            "उपरमे", "प्रतिबन्ध करना, रोकना", "to stop, to resist, to hinder",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1140", 1140, "तपँ", "तप्",
            "सन्तापे", "संतप्त होना, जलना, जलाना, तप्त करना, मन में या शरीर में जलना", "to be angry, to burn, to become hot, to envy, to glow, to shine, to perform penance,to heat, to suffer pain,to hurt",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1141", 1141, "त्यजँ", "त्यज्",
            "हानौ", "छोड़ना, त्यागना, देना, दान करना", "to abandon,to leave,to quit,to let go,to renounce",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1142", 1142, "षन्जँ", "सञ्ज्",
            "सङ्गे", "आलिंगन करना, गले लगाना, सटे रहना, चिपके रहना", "to hug, to embrace, to stay in close contact, to cling, to stick to",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1143", 1143, "दृशिँर्", "दृश्",
            "प्रेक्षणे", "देखना", "to see,to look",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1144", 1144, "दन्शँ", "दंश्",
            "दशने", "काटना, डसना, दंश मारना", "to bite,to sting",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1145", 1145, "कृषँ", "कृष्",
            "विलेखने", "कृषिकर्म करना, जोतना, हल चलाना", "to farm, to plow, to cultivate",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.DVIKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1146", 1146, "दहँ", "दह्",
            "भस्मीकरणे", "जलाना, नष्ट करना, दुःख देना", "to reduce to ashes,to burn, to destroy, to cause pain",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1147", 1147, "मिहँ", "मिह्",
            "सेचने", "सींचना, गीला करना,प्रोक्षण करना, पेशाब करना", "to irrigate, to wet, to sprinkle, to urinate",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1148", 1148, "कितँ", "कित्",
            "निवासे रोगापनयने व्याधिप्रतीकारे निग्रहे अपनयने नाशने संशये च", "निवास करना, रोग का प्रतिकार करना, चिकित्सा करना", "to stay, to cure, to carry out medical examination, to fight disease",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1149", 1149, "दानँ", "दान्",
            "खण्डने आर्जवे च", "खंडन करना, तोडना", "to cut off,to break",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1150", 1150, "शानँ", "शान्",
            "तेजने निशाने च", "तेज करना, तीक्ष्ण करना, पैदा करना", "to sharpen, to make pointed",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1151", 1151, "डुपचँष्", "पच्",
            "पाके", "पकाना", "to cook",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.DVIKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1152", 1152, "षचँ", "सच्",
            "समवाये", "अच्छी तरह से समझना, संबंधी होना, संसर्गी होना", "to understand clearly, to be related, to be connected, to carry germs",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1153", 1153, "भजँ", "भज्",
            "सेवायाम्", "भजना, भजन करना", "to worship,to honour,to pray, to serve",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1154", 1154, "रन्जँ", "रञ्ज्",
            "रागे", "रंग देना, रंगना", "to paint, to color",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1155", 1155, "शपँ", "शप्",
            "आक्रोशे", "शपथ करना, सौगंध खाना, प्रतिज्ञा करना, श्राप देना, गाली देना", "to swear, to take oath, to pledge, to curse, to abuse",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1156", 1156, "त्विषँ", "त्विष्",
            "दीप्तौ", "प्रकाशित होना,चमकना", "to glow, to shine",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1157", 1157, "यजँ", "यज्",
            "देवपूजासङ्गतिकरणदानेषु", "यज्ञ करना, हवन करना, देवपूजा करना,संगति करना, देना", "to sacrifice, to offer to a deity, to worship, to get associated with, to give",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1158", 1158, "डुवपँ", "वप्",
            "बीजसन्ताने गर्भाधाने छेदने बीजतन्तुसन्ताने मुण्डबीजोप्त्योः वपने घर्षणे तन्तुनिर्माणे च", "बीज बोना, बोना, अन्नादि काटना, केश काटना", "to sow,to plant,to chop",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1159", 1159, "वहँ", "वह्",
            "प्रापणे", "बहना, झरना, ढोना, ढो ले जाना", "to flow, to propel, to carry, to  haul, to drag",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.DVIKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1160", 1160, "वसँ", "वस्",
            "निवासे", "निवास करना, टिकना", "to dwell,to live, to stay",
            PadaType.PARASMAIPADA, ItStatus.ANIT, Karmatva.AKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1161", 1161, "वेञ्", "वे",
            "तन्तुसन्ताने", "बुनना, बटना", "to weave,to sew,to compose, to knit",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1162", 1162, "व्येञ्", "व्ये",
            "संवरणे", "आच्छादन करना, ढकना, सीना", "to cover,to hide",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1163", 1163, "ह्वेञ्", "ह्वे",
            "स्पर्धायां शब्दे च", "स्पर्धा करना, बड़ावरी करना, बुलाना, पुकारना", "to vie with,to challenge,to compete, to call, to invoke",
            PadaType.UBHAYAPADA, ItStatus.ANIT, Karmatva.SAKARMAKA, Accent.ANUDATTA,
        )
        dhatu(
            "01.1164", 1164, "वदँ", "वद्",
            "व्यक्तायां वाचि", "कहना, स्पष्ट बोलना", "to talk, to speak, to tell, to describe, to inform, to explain to utter, to communicate",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1165", 1165, "टुओँश्वि", "श्वि",
            "गतिवृद्ध्योः", "जाना, बढ़ना, सफ़ेद होना", "to go,to increase,to grow,to swell, to be whitened",
            PadaType.PARASMAIPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "01.1166", 1166, "ऋति", "ऋत्",
            "जुगुप्सायां कृपायां च", "", "to pity, to hate, to criticize, to abuse, to favour",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.AKARMAKA, Accent.UDATTA,
        )
    }
}
