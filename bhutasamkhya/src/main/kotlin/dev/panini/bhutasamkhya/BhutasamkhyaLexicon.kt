package dev.panini.bhutasamkhya

/**
 * Lexicon of classical Bhutasamkhya symbolic terms mapped to their numeric values.
 */
object BhutasamkhyaLexicon {

    private val symbolValueMap: Map<String, Long> = mapOf(
        // 0: शून्य, ख, अम्बर, नभस्, गगन, आकाश
        "शून्य" to 0L, "ख" to 0L, "अम्बर" to 0L, "नभस्" to 0L, "गगन" to 0L, "आकाश" to 0L,

        // 1: रूप, चन्द्र, इन्दु, भूमि, पृथ्वी, धरणी
        "रूप" to 1L, "चन्द्र" to 1L, "इन्दु" to 1L, "भूमि" to 1L, "पृथ्वी" to 1L, "धरणी" to 1L,

        // 2: नेत्र, अक्षि, चक्षुस्, बाहु, कर, यम, अश्विन्
        "नेत्र" to 2L, "अक्षि" to 2L, "चक्षुस्" to 2L, "बाहु" to 2L, "कर" to 2L, "यम" to 2L, "अश्विन्" to 2L,

        // 3: राम, गुण, काल, अग्नि, पावक, शिखिन्, लोक
        "राम" to 3L, "गुण" to 3L, "काल" to 3L, "अग्नि" to 3L, "पावक" to 3L, "शिखिन्" to 3L, "लोक" to 3L,

        // 4: वेद, समुद्र, अब्धि, अर्णव, युग, कृत
        "वेद" to 4L, "समुद्र" to 4L, "अब्धि" to 4L, "अर्णव" to 4L, "युग" to 4L, "कृत" to 4L,

        // 5: बाण, शर, भूत, प्राण, इन्द्रिय, विषय
        "बाण" to 5L, "शर" to 5L, "भूत" to 5L, "प्राण" to 5L, "इन्द्रिय" to 5L, "विषय" to 5L,

        // 6: रस, अङ्ग, ऋतु, दर्शन, शास्त्र
        "रस" to 6L, "अङ्ग" to 6L, "ऋतु" to 6L, "दर्शन" to 6L, "शास्त्र" to 6L,

        // 7: अद्रि, गिरि, पर्वत, मुनि, ऋषि, स्वर, धातु
        "अद्रि" to 7L, "गिरि" to 7L, "पर्वत" to 7L, "मुनि" to 7L, "ऋषि" to 7L, "स्वर" to 7L, "धातु" to 7L,

        // 8: वसु, सर्प, नाग, गज, दन्तिन्, सिद्धि
        "वसु" to 8L, "सर्प" to 8L, "नाग" to 8L, "गज" to 8L, "दन्तिन्" to 8L, "सिद्धि" to 8L,

        // 9: ग्रह, नन्द, छिद्र, अङ्क
        "ग्रह" to 9L, "नन्द" to 9L, "छिद्र" to 9L, "अङ्क" to 9L,

        // 10: दिक्, अङ्गुलि, अवतार
        "दिक्" to 10L, "अङ्गुलि" to 10L, "अवतार" to 10L,

        // 12: सूर्य, अर्क, आदित्य, मास
        "सूर्य" to 12L, "अर्क" to 12L, "आदित्य" to 12L, "मास" to 12L
    )

    fun getValue(term: String): Long? = symbolValueMap[term]

    fun isSymbol(term: String): Boolean = term in symbolValueMap
}
