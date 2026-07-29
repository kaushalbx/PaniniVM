package dev.panini.sutra

/** Sūtra establishing one technical name for one grammatical concept. */
interface SamjnaVidhayakaSutra : ArthavatSutra {
    override val artha: SamjnaDefinitionArtha
}

/** Sūtra establishing multiple technical names for one grammatical concept. */
interface SamjnaSamuhVidhayakaSutra : ArthavatSutra {
    override val artha: SamjnaSetDefinitionArtha
}

/** Sūtra establishing one technical name for multiple grammatical concepts. */
interface SamjniSamuhVidhayakaSutra : ArthavatSutra {
    override val artha: SamjniSetDefinitionArtha
}

/** Paribhāṣā sūtra establishing a grammatical interpretation principle. */
interface ParibhashaVidhayakaSutra : ArthavatSutra {
    override val artha: InterpretivePrincipleArtha
}

/** Sūtra assigning a technical name to material selected from derivation context. */
interface PrasangikaSamjnaVidhayakaSutra : ArthavatSutra {
    override val artha: ContextualSamjnaAssignmentArtha
}

/** Sūtra prohibiting another sūtra for material selected from derivation context. */
interface PrasangikaNishedhaSutra : ArthavatSutra {
    override val artha: ContextualProhibitionArtha
}
