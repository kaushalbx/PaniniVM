package dev.sanskrit.ashtadhyayi.adhyaya7.pada2

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.dhatupatha.Gana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraPriority
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 7.2.80: ato yeyah. Replaces the liṅ augment यास् with इय् after an a-final aṅga. */
object AtoYeyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.80",
    text = "अतो येयः",
    hindiExplanation = "अकारान्त अङ्ग के बाद लिङ् का यास् इय् हो जाता है।",
    type = SutraType.APAVADA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720080,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    priority = SutraPriority.APAVADA,
    blocks = setOf("7.2.79"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LING) return false

        val yasutIndex = context.terms.indexOfFirst { it.id == "yasut" }
        if (yasutIndex <= 0) return false

        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU }
        if (dhatu?.gana in setOf(Gana.ADADI, Gana.JUHOTYADI)) return false

        val precedingAnga = context.terms[yasutIndex - 1]
        val yasut = context.terms[yasutIndex]
        val endsInA = precedingAnga.surface.endsWith('अ') ||
            precedingAnga.surface.endsWith('a') ||
            precedingAnga.id in setOf("shyan", "sha")
        return endsInA && yasut.surface == "यास्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val yasut = context.terms.first { it.id == "yasut" }
        return DerivationChange(
            context.replaceTerm(yasut.id, yasut.copy(surface = "इय्")),
            "7.2.80 replaces यास् with इय् after the a-final aṅga.",
        )
    }
}
