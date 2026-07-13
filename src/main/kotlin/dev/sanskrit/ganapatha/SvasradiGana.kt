package dev.sanskrit.ganapatha

object SvasradiGana : Gana(
    id = GanaIds.SVASRADI,
    name = "स्वस्रादिः",
    sourceIndex = 46,
    sutra = "4.1.10",
    sutraId = "41010",
    sutraText = "न षट्स्वस्रादिभ्यः",
    sutraTransliteration = "nashatsvasraadibhyah",
    sutraType = "V\$\$",
    kind = GanaKind.PATHA,
    members = ganaMembers {
        member("स्वसृ")
        member("दुहितृ")
        member("ननान्दृ")
        member("यातृ")
        member("मातृ")
        member("तिसृ")
        member("चतसृ")
    },
)
