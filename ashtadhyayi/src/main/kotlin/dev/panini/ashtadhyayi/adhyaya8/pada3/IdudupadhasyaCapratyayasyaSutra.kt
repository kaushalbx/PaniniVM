package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

/**
 * 8.3.41 इदुदुपधस्य चाप्रत्ययस्य: before ku/pu (कुप्वोः, from 8.3.37) a visarga whose upadhā is इ or उ
 * and which is not the end of a pratyaya becomes ष्: निस् + क्राम = निष्क्राम, दुस् + कृत = दुष्कृत.
 *
 * Only a visarga known not to come from a pratyaya is taken. An upasarga (निस्, दुस्) is such a word;
 * the visarga of अग्निः, from the सुँ of the case ending, is not, so अग्निः करोति keeps 8.3.37.
 */
object IdudupadhasyaCapratyayasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.41", text = "इदुदुपधस्य चाप्रत्ययस्य",
    hindiExplanation = "इकार या उकार उपधा वाले, प्रत्यय से भिन्न के विसर्ग को कवर्ग और पवर्ग परे षकार होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830041,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI, blocks = setOf("8.3.34", "8.3.37"),
), DerivationSutra {
    private fun target(context: DerivationState): VisargaBoundary? = visargaBoundaries(context).firstOrNull { boundary ->
        val upadha = boundary.left.varnas.getOrNull(boundary.left.varnas.lastIndex - 1)
        !context.isBlockedAtBoundary(sutra, boundary.left.id, boundary.right.id) &&
            upadha in setOf(Svara.I, Svara.U) &&
            kupuVisargaAllophone(boundary.following.first()) != null &&
            SamjnaAssignment(boundary.left.id, Samjna.UPASARGA) in context.samjnas
    }

    override fun matches(context: DerivationState): Boolean = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val target = requireNotNull(target(context))
        val protected = context.blockAtBoundary("8.3.34", target.left.id, target.right.id, sutra)
            .blockAtBoundary("8.3.37", target.left.id, target.right.id, sutra)
            .blockAtBoundary(sutra, target.left.id, target.right.id, sutra)
        return DerivationChange(protected.replaceTermVarna(target.left.id, target.left.varnas.lastIndex,
            listOf(Vyanjana.SSA), sutra),
            "8.3.41 replaces the visarga of ${target.left.surface} with ष् before ku/pu.")
    }
}
