package dev.sanskrit.ganapatha

object BhidadiGana : Gana(
    name = "भिदादिः",
    sourceIndex = 42,
    sutra = "3.3.104",
    sutraId = "33104",
    sutraText = "षिद्भिदादिभ्योऽङ्",
    sutraTransliteration = "shidbhidaadibhyong",
    kind = GanaKind.PATHA,
    notes = "Source row contains contextual annotations; only member entries are modeled here.",
    members = ganaMembers {
        member("विदा")
        member("क्षिपा")
        member("श्रद्धा")
        member("मेधा")
        member("गोधा")
        member("हारा")
        member("क्षिया")
        member("रेखा")
        member("चूडा")
        member("पीडा")
        member("वपा")
        member("वसा")
        member("सृजा")
        member("कृपा")
    },
)
