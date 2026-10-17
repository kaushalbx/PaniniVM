package dev.panini.ashtadhyayi.vartika

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya8.pada3.VisargaBoundary
import dev.panini.ashtadhyayi.adhyaya8.pada3.visargaBoundaries
import dev.panini.derivation.*
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.*
import dev.panini.sutra.runtime.SutraSource

/** Kāśikā's attested extension under 8.3.36, not another canonical pāṭha slot. */
object KharpareShariVisargaLopaVartika : Sutra<DerivationState, DerivationChange>(
    number = "vartika:8.3.36:kharpare-shari-lopa", text = "खर्परे शरि वा लोपो वक्तव्यः",
    hindiExplanation = "खर् से युक्त शर् परे विसर्ग का विकल्प से लोप होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 3, optional = true, kramaValue = 830036,
    role = SutraRole.Apavada, action = SutraAction.LOPA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SIBILANT_SANDHI, blocks = setOf("8.3.34", "8.3.36", "8.3.37"),
), DerivationSutra {
    override val source = SutraSource.Vartika("8.3.36", text,
        "https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.36.htm")

    private fun targets(context: DerivationState): List<VisargaBoundary> = visargaBoundaries(context).filter { boundary ->
        !context.isBlockedAtBoundary(sutra, boundary.left.id, boundary.right.id) &&
            !context.isBlockedAtBoundary("8.3.36", boundary.left.id, boundary.right.id) &&
            boundary.following.size == 2 &&
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.SHAR, boundary.following[0]) &&
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.KHAR, boundary.following[1])
    }.toList()

    override fun matches(context: DerivationState): Boolean = targets(context).isNotEmpty()

    private fun delete(context: DerivationState, boundary: VisargaBoundary) = DerivationChange(
        context.replaceTermVarna(boundary.left.id, boundary.left.varnas.lastIndex, emptyList(), sutra),
        "The vārttika under 8.3.36 optionally deletes visarga before śar immediately followed by khar.")

    override fun apply(context: DerivationState): DerivationChange = delete(context, targets(context).first())

    override fun applyAll(state: DerivationState): List<DerivationChange> =
        targets(state).map { delete(state, it) } + DerivationChange(state, "$sutra declined.", applied = false)
}
