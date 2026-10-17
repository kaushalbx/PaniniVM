package dev.panini.ashtadhyayi.vartika

import dev.panini.ashtadhyayi.adhyaya8.pada4.shaToChaTargets
import dev.panini.ashtadhyayi.adhyaya8.pada4.applyShaToCha
import dev.panini.derivation.*
import dev.panini.sutra.*
import dev.panini.sutra.runtime.SutraSource

/** Attested under 8.4.63; the identity is a software namespace, not a fabricated sūtra number. */
object ChatvamAmiVartika : Sutra<DerivationState, DerivationChange>(
    number = "vartika:8.4.63:chatvam-ami", text = "छत्वममि",
    hindiExplanation = "पदान्त झय् के बाद श् को अम् परे विकल्प से छ् होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 4, optional = true, kramaValue = 840063,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override val source = SutraSource.Vartika("8.4.63", text,
        "https://ashtadhyayi.com/sutraani/8/4/63")
    override fun matches(context: DerivationState) = context.shaToChaTargets(additionalAm = true).isNotEmpty()
    override fun apply(context: DerivationState) =
        context.applyShaToCha(requireNotNull(context.shaToChaTargets(additionalAm = true).firstOrNull()), sutra)
    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val occurrences = state.shaToChaTargets(additionalAm = true)
        require(occurrences.isNotEmpty())
        return occurrences.map { state.applyShaToCha(it, sutra) } +
            DerivationChange(state, "$sutra declined.", applied = false)
    }
}
