package dev.sanskrit.ganapatha

object SamanadiGana : Gana(
    id = GanaIds.SAMANADI,
    name = "समानादिः",
    sourceIndex = 47,
    sutra = "4.1.35",
    sutraId = "41035",
    sutraText = "नित्यं सपत्न्यादिषु",
    sutraTransliteration = "nityamsapatnyaadishu",
    sutraType = "V\$\$",
    kind = GanaKind.PATHA,
    notes = "Source row contains contextual annotations; only member entries are modeled here.",
    members = ganaMembers {
        member("समान")
        member("एक")
        member("वीर")
        member("पिण्ड")
        member("श्व")
        member("शिरी")
        member("भ्रातृ")
        member("भद्र")
        member("पुत्र")
    },
)
