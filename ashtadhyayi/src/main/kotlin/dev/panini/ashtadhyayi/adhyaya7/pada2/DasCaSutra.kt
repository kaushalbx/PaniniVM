package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.derivation.*
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/** Nyāsa 7.2.109: idamaḥ continues, sau does not; d -> m before vibhakti. */
object DasCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.109",
    text = "दश्च",
    hindiExplanation = "विभक्ति परे होने पर इदम् के दकार को मकार आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720109,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1"),
    blocks = setOf("7.3.102", "7.3.103"),
), DerivationSutra {
    private fun target(context: DerivationState): DerivationTerm? {
        if (context.terms.lastOrNull()?.sourceSupAffix == null) return null
        val stem = context.terms.getOrNull(context.terms.lastIndex - 1) ?: return null
        return stem.takeIf {
            it.upadesha == "इदम्" && it.pendingBaseComposition == null &&
                it.varnas.getOrNull(1) == Vyanjana.DA &&
                context.substitutions.any { change -> change.targetId == it.id && change.sutra == "7.2.102" }
        }
    }

    override fun matches(context: DerivationState) = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val stem = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(stem.id, 1, listOf(Vyanjana.MA), sutra)
            .copy(stage = DerivationStage.ANGAKARYA),
            "7.2.109 independently replaces the d occurrence of idam with m before a case affix.")
    }
}
