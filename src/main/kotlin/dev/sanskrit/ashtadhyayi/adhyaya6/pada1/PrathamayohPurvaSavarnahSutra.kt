package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 6.1.102: prathamayoḥ pūrvasavarṇaḥ.
 * In the first two vibhaktis (Prathama and Dvitiya), when an Ak vowel is followed
 * by a vowel, a single substitute homogeneous with the former (pūrvasavarṇa dīrgha) occurs.
 */
object PrathamayohPurvaSavarnahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.102",
    text = "प्रथमयोः पूर्वसवर्णः",
    hindiExplanation = "प्रथमा और द्वितीया विभक्ति के अच् परे होने पर पूर्व-सवर्ण दीर्घ एकादेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610102,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        (context.stage == DerivationStage.IT_PROCESSED || context.stage == DerivationStage.PADA_FORMED) && context.terms.lastOrNull()?.id in setOf(
            "sup-jas",
            "sup-sas"
        )

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        return DerivationChange(
            context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface + "ास्"),
                stage = DerivationStage.PADA_FORMED
            ), "6.1.102 combines अ + अ as आ."
        )
    }
}
