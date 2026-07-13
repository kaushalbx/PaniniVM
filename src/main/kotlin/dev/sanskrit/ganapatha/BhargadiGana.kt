package dev.sanskrit.ganapatha

object BhargadiGana : Gana(
    id = GanaIds.BHARGADI,
    name = "भर्गादिः",
    sourceIndex = 70,
    sutra = "4.1.178",
    sutraId = "41178",
    sutraText = "न प्राच्यभर्गादियौधेयादिभ्यः",
    sutraTransliteration = "napraachyabhargaadiyaudheyaadibhyah",
    sutraType = "V\$\$",
    kind = GanaKind.PATHA,
    members = ganaMembers {
        member("भर्ग")
        member("करूष")
        member("केकय")
        member("कश्मीर")
        member("साल्व")
        member("सुस्थाल")
        member("उरस्")
        member("कौरव्य")
    },
)
