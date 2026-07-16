package dev.sanskrit.ashtadhyayi.adhyaya7.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.dhatupatha.Gana
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 7.3.86: पुगन्तलघूपधस्य च. */
object PugantalaghupadhasyaCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.86",
    text = "पुगन्तलघूपधस्य च",
    hindiExplanation = "लघु इक् उपधा को सार्वधातुक या आर्धधातुक प्रत्यय से पहले गुण होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730086,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return dhatu.gana == Gana.CURADI &&
            context.terms.any { it.upadesha == "णिच्" } &&
            lightUpadhaIndex(dhatu.surface) != null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatu = context.terms.first { it.kind == TermKind.DHATU }
        val index = requireNotNull(lightUpadhaIndex(dhatu.surface))
        val source = dhatu.surface[index]
        val replacement = requireNotNull(Varnamala.getGuna(source))
        return DerivationChange(
            state = context.replaceTerm(
                dhatu.id,
                dhatu.copy(surface = dhatu.surface.replaceRange(index, index + 1, replacement)),
            ).copy(stage = DerivationStage.ANGAKARYA)
                .addSubstitution(VarnaSubstitution(dhatu.id, source, replacement, sutra)),
            explanation = "7.3.86 applies guṇa to the light upadhā of the Curādi root.",
        )
    }

    private fun lightUpadhaIndex(surface: String): Int? {
        val finalConsonantStart = surface.length - 2
        if (finalConsonantStart <= 0 || surface.lastOrNull() != '्') return null
        return (finalConsonantStart - 1 downTo 0).firstOrNull {
            surface[it] in setOf('ि', 'ु', 'ृ', 'ॢ')
        }
    }
}
