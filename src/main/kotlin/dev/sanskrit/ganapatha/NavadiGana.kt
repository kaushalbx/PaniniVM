package dev.sanskrit.ganapatha

object NavadiGana : Gana(
    id = GanaIds.NAVADI,
    name = "नावादिः",
    sourceIndex = 20,
    sutra = "2.3.17",
    sutraId = "23017",
    sutraText = "मन्यकर्मण्यनादरे विभाषाऽप्राणिषु",
    sutraTransliteration = "manyakarmanyanaadarevibhaashaapraanishu",
    sutraType = "V${'$'}${'$'}",
    kind = GanaKind.PATHA,
    members = ganaMembers {
        member("नौ")
        member("काक")
        member("अन्न")
        member("शुक")
        member("शृगाल")
    },
)
