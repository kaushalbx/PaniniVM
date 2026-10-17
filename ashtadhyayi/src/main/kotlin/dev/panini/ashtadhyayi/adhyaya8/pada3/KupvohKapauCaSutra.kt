package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

/** KHAR is inherited: only the voiceless members of ku/pu are in this domain. */
internal fun kupuVisargaAllophone(follower: Varna): Ayogavaha? = when (follower) {
    Vyanjana.KA, Vyanjana.KHA -> Ayogavaha.JIHVAMULIYA
    Vyanjana.PA, Vyanjana.PHA -> Ayogavaha.UPADHMANIYA
    else -> null
}

/** Both ca-prescribed outcomes apply 8.3.37; neither admits the ordinary s branch. */
object KupvohKapauCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.37", text = "कुप्वोः ःकःपौ च",
    hindiExplanation = "खर् में आने वाले क-वर्ग और प-वर्ग के पहले विसर्ग को क्रमशः जिह्वामूलीय, उपध्मानीय अथवा विसर्ग आदेश होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830037,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI, blocks = setOf("8.3.34"),
), DerivationSutra {
    override val hasAlternatives: Boolean = true
    private fun target(context: DerivationState): VisargaBoundary? = visargaBoundaries(context).firstOrNull {
        !context.isBlockedAtBoundary(sutra, it.left.id, it.right.id) &&
            kupuVisargaAllophone(it.following.first()) != null
    }
    override fun matches(context: DerivationState): Boolean = target(context) != null

    private fun protected(context: DerivationState, target: VisargaBoundary): DerivationState =
        context.blockAtBoundary("8.3.34", target.left.id, target.right.id, sutra)
            .blockAtBoundary(sutra, target.left.id, target.right.id, sutra)

    override fun apply(context: DerivationState): DerivationChange {
        val target = requireNotNull(target(context))
        val replacement = requireNotNull(kupuVisargaAllophone(target.following.first()))
        return DerivationChange(protected(context, target).replaceTermVarna(target.left.id,
            target.left.varnas.lastIndex, listOf(replacement), sutra),
            "8.3.37 substitutes $replacement before the corresponding voiceless ku/pu member.")
    }

    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val target = requireNotNull(target(state))
        return listOf(apply(state), DerivationChange(protected(state, target),
            "8.3.37 retains visarga as the additional ca-prescribed outcome; s-substitution is excluded.",
            advancesTripadiFrontier = false))
    }
}
