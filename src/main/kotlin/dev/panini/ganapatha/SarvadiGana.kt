package dev.panini.ganapatha

import dev.panini.shiksha.Samjna

object SarvadiGana : Gana(
    name = "सर्वादिः",
    source = GanaSource.GANAPATHA_DATA,
    sourceUrl = GanaPathaSources.DATA_URL,
    sourceIndex = 1,
    sutra = "1.1.27",
    sutraId = "11027",
    sutraText = "सर्वादीनि सर्वनामानि",
    sutraTransliteration = "sarvaadeenisarvanaamaani",
    resultSamjnas = setOf(Samjna.SARVANAMA),
    vartika = "",
    rawWords = "सर्व । विश्व । उभ । उभय । डतर । डतम । अन्य । अन्यतर । इतर । त्वत् । त्व । नेम । सम । सिम । <<पूर्वपरावरदक्षिणोत्तरापराधराणिव्यवस्थायामसंज्ञायाम्>> । <<स्वमज्ञातिधनाख्यायाम्>> । <<अन्तरं बहिर्योगोपसंव्यानयोः>> । त्यद् । तद् । यद् । एतद् । इदम् । अदस् । एक । द्वि । युष्मद् । अस्मद् । भवतु । किम् ॥",
    kind = GanaKind.PATHA,
    antarGanas = listOf(
        AntarGana(
            name = "डतराद्यन्तर्गणः",
            members = listOf("डतर", "डतम", "अन्य", "अन्यतर", "इतर"),
            derivedForms = setOf("कतर", "कतम", "यतर", "यतम", "ततर", "ततम", "एकतर", "एकतम"),
        ),
    ),
    sanskritMeaning = "सर्वादिगणे विद्यमानानाम् शब्दानाम् 'सर्वनाम' इति संज्ञा भवति ।",
    englishMeaning = "The words belonging to the सर्वादिगण are called सर्वनाम.",
    notes = "Source row includes contextual notes for पूर्वादि, स्व, and अन्तर; those constraints are now partially modeled via conditions.",
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
        member("पूर्व", ganaCondition = RelativePositionNotProperName)
        member("पर", ganaCondition = RelativePositionNotProperName)
        member("अवर", ganaCondition = RelativePositionNotProperName)
        member("दक्षिण", ganaCondition = RelativePositionNotProperName)
        member("उत्तर", ganaCondition = RelativePositionNotProperName)
        member("अपर", ganaCondition = RelativePositionNotProperName)
        member("अधर", ganaCondition = RelativePositionNotProperName)
        member("स्व", ganaCondition = NotKinshipWealthOrProperName)
        member("अन्तर", ganaCondition = ExteriorAssociationOrGarment)
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
