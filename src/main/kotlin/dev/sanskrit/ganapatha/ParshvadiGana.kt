package dev.sanskrit.ganapatha

object ParshvadiGana : Gana(
    id = GanaIds.PARSHVADI,
    name = "पार्श्वादिः",
    sourceIndex = 40,
    sutra = "3.2.15",
    sutraId = "32015",
    sutraText = "अधिकरणे शेतेः",
    sutraTransliteration = "adhikaranesheteh",
    sutraType = "V\$\$",
    kind = GanaKind.PATHA,
    members = ganaMembers {
        member("पार्श्व")
        member("उदर")
        member("पृष्ठ")
        member("उत्तान")
        member("अवमूर्धन")
    },
)
