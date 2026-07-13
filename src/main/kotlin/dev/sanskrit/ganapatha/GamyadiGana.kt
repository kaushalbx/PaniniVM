package dev.sanskrit.ganapatha

object GamyadiGana : Gana(
    name = "गम्यादिः",
    sourceIndex = 41,
    sutra = "3.3.3",
    sutraId = "33003",
    sutraText = "भविष्यति गम्यादयः",
    sutraTransliteration = "bhavishyatigamyaadayah",
    kind = GanaKind.PATHA,
    adhikara = Adhikara("भविष्यदधिकारः", "3.3.15"),
    members = ganaMembers {
        member("गमी")
        member("आगमी")
        member("भावी")
        member("प्रस्थायी")
        member("प्रतिरोधी")
        member("प्रतियोधी")
        member("प्रतिबोधी")
        member("प्रतियायी")
        member("प्रतियोगी")
    },
)
