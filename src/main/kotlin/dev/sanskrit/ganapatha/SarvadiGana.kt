package dev.sanskrit.ganapatha

object SarvadiGana : Gana(
    id = GanaIds.SARVADI,
    name = "सर्वादिः",
    source = GanaSource.GANAPATHA_DATA,
    sourceUrl = GanaPathaSources.DATA_URL,
    sourceIndex = 1,
    sutra = "1.1.27",
    sutraId = "11027",
    sutraText = "सर्वादीनि सर्वनामानि",
    sutraTransliteration = "sarvaadeenisarvanaamaani",
    sutraType = "S${'$'}सर्वनामसंज्ञा${'$'}",
    vartika = "",
    rawWords = "सर्व । विश्व । उभ । उभय । डतर । डतम । अन्य । अन्यतर । इतर । त्वत् । त्व । नेम । सम । सिम । <<पूर्वपरावरदक्षिणोत्तरापराधराणिव्यवस्थायामसंज्ञायाम्>> । <<स्वमज्ञातिधनाख्यायाम्>> । <<अन्तरं बहिर्योगोपसंव्यानयोः>> । त्यद् । तद् । यद् । एतद् । इदम् । अदस् । एक । द्वि । युष्मद् । अस्मद् । भवतु । किम् ॥",
    kind = GanaKind.PATHA,
    sanskritMeaning = "सर्वादिगणे विद्यमानानाम् शब्दानाम् 'सर्वनाम' इति संज्ञा भवति ।",
    englishMeaning = "The words belonging to the सर्वादिगण are called सर्वनाम.",
    notes = "Source row includes contextual notes for पूर्वादि, स्व, and अन्तर; those constraints are not modeled yet.",
    members = ganaMembers {
        member("सर्व")
        member("विश्व")
        member("उभ")
        member("उभय")
        member("डतर")
        member("डतम")
        member("अन्य")
        member("अन्यतर")
        member("इतर")
        member("त्वत्")
        member("त्व")
        member("नेम")
        member("सम")
        member("सिम")
        member("त्यद्")
        member("तद्")
        member("यद्")
        member("एतद्")
        member("इदम्")
        member("अदस्")
        member("एक")
        member("द्वि")
        member("युष्मद्")
        member("अस्मद्")
        member("भवतु")
        member("किम्")
    },
)
