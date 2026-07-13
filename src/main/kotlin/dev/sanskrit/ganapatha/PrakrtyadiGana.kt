package dev.sanskrit.ganapatha

object PrakrtyadiGana : Gana(
    id = GanaIds.PRAKRTYADI,
    name = "प्रकृत्यादिः",
    sourceIndex = 21,
    sutra = "2.3.18",
    sutraId = "23018",
    sutraText = "कर्तृकरणयोस्तृतीया",
    sutraTransliteration = "kartrukaranayostruteeyaa",
    sutraType = "V${'$'}${'$'}",
    kind = GanaKind.PATHA,
    members = ganaMembers {
        member("प्रकृति")
        member("प्राय")
        member("गोत्र")
        member("सम")
        member("विषम")
        member("द्विद्रोण")
        member("पञ्चक")
        member("साहस्र")
    },
)
