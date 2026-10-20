package dev.panini.linganushasanam

import dev.panini.core.Linga

/**
 * Complete normalized text of the 191 sūtras in Pāṇini's Liṅgānuśāsanam.
 *
 * Source text: https://sanskritdocuments.org/doc_z_misc_major_works/lingAnushAsanam.html
 * The source prints 10 twice after 8 and 102 twice after 100; sequence order is
 * normalized here to 9/10 and 101/102 respectively.
 */
internal object CanonicalLinganushasanamCorpus {
    private val adhikaraNumbers = setOf("1", "2", "35", "60", "118", "127", "173", "178", "185")

    data class Entry(
        val number: String,
        val text: String,
        val section: LinganushasanaSection,
        val lingas: Set<Linga>,
        val kind: LinganushasanaRuleKind = LinganushasanaRuleKind.VIDHI,
    )

    private val transmittedEntries: List<Entry> = listOf(
        Entry("1", "लिङ्गम् ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("2", "स्त्री ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("3", "ऋकारान्ता मातृदुहितृयातृस्वसृननान्दरः । (यातृ स्थाने पोतृ इति क्वचित्)", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("4", "अन्यूप्रत्ययान्ता धातुः ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("5", "अशनिभरण्यरणय पुंसि च ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("6", "मिन्यन्तः ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("7", "वह्निवृष्ण्यग्नयः पुंसि ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("8", "श्रोणियोन्यूर्मय पुंसि च ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("9", "क्तिन्नन्तः ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("10", "ईकारान्तश्च ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("11", "ऊङ्ङ्याबन्तश्च ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("12", "य्वन्तमेकाक्षरम् ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("13", "विंशत्यादिरानवतेः ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("14", "दुन्धुभिरक्षेषु ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("15", "नाभिरक्षत्रिये ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("16", "उभावन्यत्र पुंसि ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("17", "तलन्तः ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("18", "भूमिविद्युत्सरिल्लतावनिताभिधानानि ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("19", "यादो नपुंसकम् ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("20", "भास्स्रुक्स्रग्दिग्दृगुष्णिगुपानहः ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("21", "स्थूणोर्णे नपुंसके च ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("22", "गृहशशाभ्यां क्लीबे ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("23", "प्रावड्विप्रुड्रुड्विटत्विषः ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("24", "दर्विविदिवेदिखनिशाण्यभ्रिवेणिकृष्योषध्यङ्गुलयः ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("25", "तिथिनाडिरुचिवीचिनालिधूलिकिकिकेलिच्छविरात्र्यादयः ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("26", "शष्कुलिराजिकुट्यवन्तिवर्त्तिभ्रुकुटित्रुटिबलिपङ्क्तयः ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("27", "प्रतिपदापद्विपत्सम्पच्छरत्संसत्परिषदुषःसंवित्क्षुत्पुन्मुत्समिधः ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("28", "आशीर्धूः पूर्गीर्द्वारः ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("29", "अप्सुमनस्समासिकतावर्षाणां बहुत्वञ्ज ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("30", "स्रक्त्वग्ज्योग्वाग्यवागूनौस्फिचः ।(क्वचित् स्रक् स्थाने स्रुक् अथवा शुक, स्फिच स्थाने स्फिज इति पाठः)", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("31", "तटिसीमासम्बध्याः ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("32", "चूल्लिवेणिखार्यश्च । (क्वचित्पुस्तके चुल्लि इति पाठः ।)", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("33", "ताराधाराज्योत्स्नादयश्च ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("34", "शलाका स्त्रियां नित्यम् ।", LinganushasanaSection.STRILINGA, setOf(Linga.STRI)),
        Entry("35", "पुमान् ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("36", "घञबन्तः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("37", "घाजन्तश्च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("38", "भयलिङ्गभगपदानि नपुंसके ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("39", "नङन्तः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("40", "याच्ञा स्त्रियाम् ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("41", "क्यन्तो घुः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("42", "इषुधिः स्त्री च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("43", "देवासुरात्मस्वर्गगिरिसमुद्रनखकेशदन्तस्तनभुजकण्ठ- खड्गशरपङ्काभिधानानि ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("44", "त्रिविष्टपत्रिभुवने नपुंसके । (त्रिपिष्टप इति पाठान्तरः)", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("45", "द्यौः स्त्रियाम् ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("46", "इषुबाहू स्त्रियाञ्च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("47", "बाणकाण्डौ नपुंसके च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("48", "नान्तः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("49", "क्रतुपुरुषकपोलगुल्फमेघाभिधानानि ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("50", "अभ्रं नपुंसकम् ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("51", "उकारान्तः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("52", "धेनुरज्जुकुहुसरयुतनुरेणुप्रियङ्गवः स्त्रियाम् ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("53", "समासे रज्जुः पुंसि च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("54", "श्मश्रुजानुस्वाद्वश्रुजतुत्रपुतालूनि नपुंसके ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("55", "वसु चार्थवाचि ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("56", "मद्गुमधुशीधुसानुकमण्डलूनि नपुंसके च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("57", "रुत्वन्तः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("58", "दारुकसेरुजत्रुवस्तुमस्तूनि नपुंसके । (क्वचित् जतु इति पाठः ।)", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("59", "सक्तुर्नपुंसके च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("60", "प्राग्रश्मेरकारान्तः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("61", "कोपधः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("62", "चिबुकशालूकप्रातिपादिकांशुकोल्मुकानि नपुंसके ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("63", "कण्टकानीकसरकमोदकचसकमस्तकपुस्तकतटाकनिष्कशुष्क- वर्चस्कपिनाकभाण्डकपिण्डककटकदण्डकपिटकतालकफल- ककल्कपुलाकानि नपुंसके च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("64", "टोपधः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("65", "किरीटमुकुटललाटवटवीटश‍ृङ्गाटकरटलोष्टानि नपुंसके ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("66", "कुटकूटकपटकपाटकर्पटनटनिकटकीटकटानि नपुंसके च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("67", "णोपधः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("68", "ऋणलवणपर्णतोरणरणोष्णानि नपुंसके ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("69", "कार्षापणस्वर्णसुवर्णव्रणचरणवृषणविषाणचूर्णतृणानि नपुंसके च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("70", "थोपधः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("71", "काष्ठपृष्ठरिक्थोक्थानि नपुंसके ।(क्वचित् रिक्थ स्थाने सिक्थ इति पाठभेदः)", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("72", "काष्ठा दिगर्था स्त्रियाम् ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("73", "तीर्थप्रोथयूथगूथानि नपुंसके च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("74", "नोपधः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("75", "जघनाजिनतुहिनकाननवनवृजिनविपिनवेतनशासनसोपानमिथुन- श्मशानरत्ननिम्नचिह्नानि नपुंसके ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("76", "मानयानाभिधाननलिनपुलिनोद्यानशयनासनस्थानचन्दनालानसमान- भवनवसनसम्भावनविभा-वनविमानानि नपुंसके च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("77", "पोपधः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("78", "पापरूपोडुपतल्पशिल्पपुष्पशष्पसमीपान्तरीपाणि नपुंसके । (ताप इत्यपि केचित्)", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("79", "शूर्पकुतपकुणपद्वीपविटपानि नपुंसके च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("80", "भोपधः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("81", "तलभं नपुंसकम् ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("82", "जृम्भं नपुंसके च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("83", "मोपधः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("84", "रुक्मसिध्मयुग्मेध्मगुल्माध्यात्मकुङ्कुमानि नपुंसके ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("85", "सङ्ग्रामदाडिमकुसुमाश्रमक्षेमक्षौमहोमोद्दामानि नपुंसके च", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("86", "योपधः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("87", "किसलयहृदयेन्द्रियोत्तरीयाणि नपुंसके ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("88", "गोमयकषायमलयान्वयाव्ययानि नपुंसके च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("89", "रोपधः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("90", "द्वाराग्रस्फारतक्रवक्रवप्रक्षिप्रक्षुद्रच्छिद्रनारतीरदूर- कृच्छ्ररन्ध्रास्रशुभ्रभीरगभीरक्रूरविचित्रकेयूर- केदारोदराजस्रशरीरकन्दरपञ्जराजरजठराजिरवैरचामर- पुष्करगह्वरकुहरकुटीरकुलीरचत्वरकाश्मीरनीराम्बरशिशिर- तन्त्रयन्त्रक्षत्रक्षेत्रमित्रकलत्रच्छत्रमूत्रसूत्रवक्त्र- नेत्रगोत्राङ्गुलित्रतनुत्रास्त्रशस्त्रशास्त्रवस्त्रपत्रपात्र- नक्षत्राणि नपुंसके । (क्वचित् नार स्थाने पार, अस्र स्थाने अश्र, शुभ्र स्थाने श्वभ्र, तनुत्र स्थाने भलत्र । क्वचिन्न कुहर)", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("91", "शुक्रमदेवतायाम् ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("92", "चक्रवज्रान्धकारसारावारपारक्षीरतोमरश‍ृङ्गारभृङ्गार- मन्दारोशीरतिमिरशिशिराणि नपुंसके च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("93", "षोपधः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("94", "शिरीषर्जीषाम्बरीषपीयूषपुरीषकिल्विषकल्मषाणि नपुंसके । (ऋजीष स्थाने जोष इति केचित्)", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("95", "यूषकरीषमिषविषवर्षाणि नपुंसके च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("96", "सोपधः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("97", "मानसबिसबुससाहसानि नपुंसके । (मानस स्थाने पनस इति क्वचित्)", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("98", "चमसांसरसनिर्यासोपवासकार्पासवासमासकासकांसमांसानि नपुंसके च । (चमसांसरस स्थाने चमससरस इति क्वचितपाठः)", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("99", "कंसं चाप्राणिनि ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("100", "रश्मिदिवसाभिधानानि ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("101", "दीधितिः स्त्रियाम् ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("102", "दिनाहनी नपुंसके ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("103", "मानाभिधानानि ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("104", "द्रोणाढकौ नपुंसके च ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("105", "खारीमानिके स्त्रियाम् ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("106", "दाराक्षतलाजासूनां बहुत्वञ्ज ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("107", "नाड्यपजनोपपदानि व्रणाङ्गपदानि ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("108", "मरुद्गरुदुत्तरदृत्विजः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("109", "ऋषिराशिदृतिग्रन्थिक्रिमिध्वनिबालिकौलिमौलिरविकविकपिमुनयः (दृति स्थाने धृति, ग्रन्थि स्थाने प्रहि इति च केचित्)", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("110", "ध्वजगजमुञ्जपुञ्जाः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("111", "हस्तकुन्तान्तवातव्रातदूतधूर्तसूतचूतमुहूर्ताः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("112", "षण्डमण्डकरण्डभरण्डवरण्डतुण्डगण्डमुण्डपाषण्डशिखण्डाः । (रण्ड इत्यपि केचित्)", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("113", "वंशांशपुरोडाशाः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("114", "ह्रदकन्दकुन्दबुद्बुदशब्दाः ।", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("115", "अर्घपथिमथ्यृभुक्षिस्तम्बनितम्बपूगाः । (मथि स्थाने सखि इति केचित्)", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("116", "पल्लवपल्वलकफरेफकटाहनिर्व्यूहमठमणितरङ्गतुरङ्ग- गन्धस्कन्धमृदङ्गसङ्गसमुद्गपुङ्खाः । (पल्वल स्थाने पल्लल, निर्व्यूह स्थाने निर्य्यूह च इति केचित्)", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("117", "सारथ्यतिथिकुक्षिबस्तिपाण्यञ्जलयः । (क्वचिन्न पाणि)", LinganushasanaSection.PUMLINGA, setOf(Linga.PUMS)),
        Entry("118", "नपुंसकम् ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("119", "भावे ल्युडन्तः ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("120", "निष्ठा च ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("121", "त्वष्यञौ तद्धितौ ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("122", "कर्मणि च ब्राह्मणादिगुणवचनेभ्यः ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("123", "यद्यढग्यगञण्वुञ्छाश्च भावकर्म्मणि ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("124", "अव्ययीभावः ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("125", "द्वन्द्वैकत्वम् ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("126", "अभाषायां हेमन्तशिशिरावहोरात्रे च ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("127", "अनङ्कर्मधारयस्तत्पुरुषः ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("128", "अनल्पे छाया ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("129", "राजाऽमनुष्यपूर्वा सभा ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("130", "सुरासेनाच्छायाशालानिशाः स्त्रियाञ्च ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("131", "परवत् ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("132", "रात्राह्नाहाः पुंसि ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("133", "सङ्ख्यापूर्वा रात्रिः ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("134", "अपथपुण्याहे नपुंसके ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("135", "द्विगुः स्त्रियाञ्च ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("136", "इसुसन्तः ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("137", "अर्चिः स्त्रियाञ्च ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("138", "छदिः स्त्रियामेव ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("139", "मुखनयनलोहवनमांसरुधिरकार्मुकविवरजलहलधनान्नाभिधानानि ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("140", "सीरार्थौदनाः पुंसि ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("141", "वक्त्रनेत्रारण्यगाण्डीवानि पुंसि च ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("142", "अटवी स्त्रियाम् ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("143", "लोपधः ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("144", "तूलोपलतालकुसूलतरलकम्बलदेवलवृषलाः पुंसि ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("145", "शीलमूलमङ्गलसालकमलतलमुसलकुण्डलपललमृणाल- बालनिगलपलालबिडालखिलशूलाः पुंसि च ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("146", "शतादिः सङ्ख्या ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("147", "शतायुतप्रयुताः पुंसि च ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("148", "लक्षाकोटी स्त्रियाम् ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("149", "शङ्कुः पुंसि । (केचित् पाठे नैत्तसूत्रम्)", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("150", "सहस्रः पुंसि च । (केचित् पाठे पुंसि च इति स्थाने क्वचित् इति पाठः)", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("151", "मन् द्व्यच्कोऽकर्त्तरि ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("152", "ब्रह्मन्पुंसि च । (केचित् पाठे ब्रह्म इत्येव)", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("153", "नामरोमणी नपुंसके ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("154", "असन्तो द्व्यच्कः ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("155", "अप्सराः स्त्रियाम् ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("156", "त्रान्तः ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("157", "यात्रामात्राभस्त्रादंष्ट्रावस्त्राः स्त्रियामेव ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("158", "भृत्रामित्रच्छात्रपुत्रमन्त्रवृत्रमेढ्रोष्ट्राः पुंसि । (केचित्पाठे मेढ्रोष्ट्र स्थाने मैत्र इति पाठः)", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("159", "पत्रपात्रपवित्रसूत्रच्छत्राः पुंसि च ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("160", "बलकुसुमशुल्बपत्तनरणाभिधानानि ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("161", "पद्मकमलोत्पलानि पुंसि च ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("162", "आहवसङ्ग्रामौ पुंसि ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("163", "आजिः स्त्रियामेव ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("164", "फलजातिः ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("165", "वृक्षजातिः स्त्रियामेव ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("166", "वियज्जगत्सकृच्छकन्पृषच्छकृद्यकृदुदश्वितः । (शकत् स्थाने कृषत् इति केचित्)", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("167", "नवनीतावनतानृतामृतनिमित्तवित्तचित्तपित्तव्रतरजतवृत्तपलितानि । (अवनत स्थाने अवत इति केचित्)", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("168", "श्राद्धकुलिशदैवपीठकुण्डाङ्काङ्गदधिसक्थ्यक्ष्यस्थ्यास्पदाकाश कण्ववीजानि ।(क्वचित् भाण्डोऽपि)", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("169", "दैवं पुंसि च ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("170", "धान्याज्यशस्यरूप्यपण्यवर्ण्यधिष्ण्यहव्यकव्यकाव्यसत्यापत्य- मूल्यशिक्यकुड्यमद्यहर्म्यतूर्यसैन्यानि ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("171", "द्वन्द्वबर्हदुःखबडिशपिच्छविम्बकुटुम्बकवचकवरवृन्दराकाणि । (विम्ब स्थाने क्वचिद्विश्व)", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("172", "अक्षमिन्द्रिये ।", LinganushasanaSection.NAPUMSAKALINGA, setOf(Linga.NAPUMSAKA)),
        Entry("173", "स्त्रीपुंसयोः ।", LinganushasanaSection.STRIPUMSALINGA, setOf(Linga.STRI, Linga.PUMS)),
        Entry("174", "गोमणियष्टिमुष्टिपाटलिबस्तिशाल्मलित्रुटिमसिमरीचयः ।", LinganushasanaSection.STRIPUMSALINGA, setOf(Linga.STRI, Linga.PUMS)),
        Entry("175", "मृत्युशीधुकर्कन्धुकिष्कुकण्डुरेणवः । (मृत्यु स्थाने मन्यु, किष्कु स्थाने सिन्धु इति केचित्)", LinganushasanaSection.STRIPUMSALINGA, setOf(Linga.STRI, Linga.PUMS)),
        Entry("176", "गुणवचनमुकारान्तं नपुंसकञ्च ।", LinganushasanaSection.STRIPUMSALINGA, setOf(Linga.STRI, Linga.PUMS)),
        Entry("177", "अपत्यार्थस्तद्धिते ।", LinganushasanaSection.STRIPUMSALINGA, setOf(Linga.STRI, Linga.PUMS)),
        Entry("178", "पुंनपुंसकयोः ।", LinganushasanaSection.PUMNAPUMSAKALINGA, setOf(Linga.PUMS, Linga.NAPUMSAKA)),
        Entry("179", "धृतभूतमुस्तक्ष्वेलितैरावतपुस्तबुस्तलोहिताः ।", LinganushasanaSection.PUMNAPUMSAKALINGA, setOf(Linga.PUMS, Linga.NAPUMSAKA)),
        Entry("180", "श‍ृङ्गार्धनिदाघोद्यमशल्यदृढाः ।", LinganushasanaSection.PUMNAPUMSAKALINGA, setOf(Linga.PUMS, Linga.NAPUMSAKA)),
        Entry("181", "व्रजकुञ्जकुथकूर्चप्रस्थदर्पार्भार्धर्चदर्भपुच्छाः ।", LinganushasanaSection.PUMNAPUMSAKALINGA, setOf(Linga.PUMS, Linga.NAPUMSAKA)),
        Entry("182", "कबन्धौषधायुधान्ताः ।", LinganushasanaSection.PUMNAPUMSAKALINGA, setOf(Linga.PUMS, Linga.NAPUMSAKA)),
        Entry("183", "दण्डमण्डखण्डशवसैन्धवपार्श्वकाशकुशकाशाङ्कुशकुलिशाः ।", LinganushasanaSection.PUMNAPUMSAKALINGA, setOf(Linga.PUMS, Linga.NAPUMSAKA)),
        Entry("184", "गृहमेहदेहपट्टपटहाष्टापदार्बुदककुदाश्च ।", LinganushasanaSection.PUMNAPUMSAKALINGA, setOf(Linga.PUMS, Linga.NAPUMSAKA)),
        Entry("185", "अविशिष्टलिङ्गम् ।", LinganushasanaSection.AVISISHTALINGA, Linga.entries.toSet()),
        Entry("186", "अव्ययं कतियुष्मदस्मदः । (कति स्थाने डति इति केचित्)", LinganushasanaSection.AVISISHTALINGA, Linga.entries.toSet()),
        Entry("187", "ष्णान्ता सङ्ख्या ।", LinganushasanaSection.AVISISHTALINGA, Linga.entries.toSet()),
        Entry("188", "गुणवचनञ्च ।", LinganushasanaSection.AVISISHTALINGA, Linga.entries.toSet()),
        Entry("189", "कृत्याश्च ।", LinganushasanaSection.AVISISHTALINGA, Linga.entries.toSet()),
        Entry("190", "करणाधिकरणयोर्ल्युट् ।", LinganushasanaSection.AVISISHTALINGA, Linga.entries.toSet()),
        Entry("191", "सर्वादीनि सर्वनामानि ।", LinganushasanaSection.AVISISHTALINGA, Linga.entries.toSet()),
    )

    /** Gender scope after applying each rule's explicit exception/option wording. */
    val entries: List<Entry> = transmittedEntries.map { entry ->
        entry.copy(
            lingas = genderScope(entry.number, entry.section),
            kind = if (entry.number in adhikaraNumbers) LinganushasanaRuleKind.ADHIKARA else LinganushasanaRuleKind.VIDHI,
        )
    }

    private fun genderScope(number: String, section: LinganushasanaSection): Set<Linga> {
        val feminineAndMasculine = setOf(Linga.STRI, Linga.PUMS)
        val masculineAndNeuter = setOf(Linga.PUMS, Linga.NAPUMSAKA)
        val feminineAndNeuter = setOf(Linga.STRI, Linga.NAPUMSAKA)

        return when (number) {
            "5", "8" -> feminineAndMasculine
            "7", "16" -> setOf(Linga.PUMS)
            "19", "22" -> setOf(Linga.NAPUMSAKA)
            "21" -> feminineAndNeuter

            "38", "44", "50", "54", "55", "58", "62", "65", "68", "71", "75", "78",
            "81", "84", "87", "90", "91", "94", "97", "102" -> setOf(Linga.NAPUMSAKA)
            "40", "45", "52", "72", "101", "105" -> setOf(Linga.STRI)
            "42", "46" -> feminineAndMasculine
            "47", "56", "59", "63", "66", "69", "73", "76", "79", "82", "85", "88",
            "92", "95", "98", "104" -> masculineAndNeuter

            "126" -> masculineAndNeuter
            "130", "135", "137" -> feminineAndNeuter
            "131" -> Linga.entries.toSet()
            "132", "133", "140", "144", "149", "152", "158", "162" -> setOf(Linga.PUMS)
            "138", "142", "148", "155", "157", "163", "165" -> setOf(Linga.STRI)
            "141", "145", "147", "150", "159", "161", "169" -> masculineAndNeuter
            "176" -> Linga.entries.toSet()

            else -> when (section) {
                LinganushasanaSection.STRILINGA -> setOf(Linga.STRI)
                LinganushasanaSection.PUMLINGA -> setOf(Linga.PUMS)
                LinganushasanaSection.NAPUMSAKALINGA -> setOf(Linga.NAPUMSAKA)
                LinganushasanaSection.STRIPUMSALINGA -> feminineAndMasculine
                LinganushasanaSection.PUMNAPUMSAKALINGA -> masculineAndNeuter
                LinganushasanaSection.AVISISHTALINGA -> Linga.entries.toSet()
                LinganushasanaSection.VISESYANIGHNALINGA,
                LinganushasanaSection.SAMASALINGA -> Linga.entries.toSet()
            }
        }
    }
}
