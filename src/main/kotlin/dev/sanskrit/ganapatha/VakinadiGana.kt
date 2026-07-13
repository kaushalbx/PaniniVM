package dev.sanskrit.ganapatha

object VakinadiGana : Gana(
    id = GanaIds.VAKINADI,
    name = "वाकिनादिः",
    sourceIndex = 68,
    sutra = "4.1.158",
    sutraId = "41158",
    sutraText = "वाकिनादीनां कुक् च",
    sutraTransliteration = "vaakinaadeenaamkukcha",
    sutraType = "V\$\$",
    kind = GanaKind.PATHA,
    notes = "Source row contains contextual annotations; only member entries are modeled here.",
    members = ganaMembers {
        member("वाकिन")
        member("गाधेर")
        member("कार्कश")
        member("काक")
        member("लंका")
    },
)
