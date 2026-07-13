package dev.sanskrit.ganapatha

object GopavanadiGana : Gana(
    id = GanaIds.GOPAVANADI,
    name = "गोपवनादिः",
    sourceIndex = 29,
    sutra = "2.4.67",
    sutraId = "24067",
    sutraText = "न गोपवनादिभ्यः",
    sutraTransliteration = "nagopavanaadibhyah",
    sutraType = "V\$\$",
    kind = GanaKind.PATHA,
    members = ganaMembers {
        member("गौपवन")
        member("शेयु")
        member("शिप्रु")
        member("बिन्दु")
        member("भाजन")
        member("अश्वावतान")
        member("श्यामाक")
        member("श्योनाक")
        member("श्यामक")
        member("श्यापर्ण")
        member("(बिदाद्यन्तर्गणोऽयम् [[4.1.104]])")
    },
)
