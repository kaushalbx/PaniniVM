package dev.sanskrit.ganapatha

object SampadadiGana : Gana(
    id = GanaIds.SAMPADADI,
    name = "संपदादिः",
    sourceIndex = 43,
    sutra = "3.3.108",
    sutraId = "33108",
    sutraText = "रोगाख्यायां ण्वुल् बहुलम्",
    sutraTransliteration = "rogaakhyaayaamnvulbahulam",
    sutraType = "V\$\$",
    kind = GanaKind.PATHA,
    members = ganaMembers {
        member("संपद्")
        member("विपद्")
        member("आपद्")
        member("प्रतिपद्")
        member("परिषद्")
    },
)
