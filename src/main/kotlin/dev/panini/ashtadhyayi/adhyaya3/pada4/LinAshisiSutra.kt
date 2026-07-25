package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.4.116 लिङाशिषि.
 * Assigns Ārdhadhātuka saṃjñā to Āśīrliṅ affixes.
 */
object LinAshisiSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.116", text = "लिङाशिषि",
    hindiExplanation = "आशीर्वाद अर्थ वाले लिङ् (आशीर्लिङ्) लकार के स्थान पर विहित प्रत्ययों की 'आर्धधातुक' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 3, pada = 4, optional = false, kramaValue = 340116,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LING &&
        context.terms.any { term ->
            term.kind == TermKind.PRATYAYA &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.ARDHADHATUKA }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val newSamjnas = context.terms.filter { it.kind == TermKind.PRATYAYA }.map { SamjnaAssignment(it.id, Samjna.ARDHADHATUKA) }.toSet()
        return DerivationChange(
            state = context.withSamjnas(newSamjnas),
            explanation = "3.4.116 assigns Ārdhadhātuka saṃjñā to Āśīrliṅ affixes.",
        )
    }
}
