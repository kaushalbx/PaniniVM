package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.3.15: khar-avasānayor visarjanīyaḥ.
 * Word-final 'r' (repha) is replaced by visarga before a khar sound
 * or at the end of a derivation (avasāna).
 */
object KharavasanayorVisarjaniyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.15",
    text = "खरवसानयोर्विसर्जनीयः",
    hindiExplanation = "पदान्त 'र्' के स्थान पर विसर्ग होता है यदि बाद में 'खर्' वर्ण हो या अवसान (विराम) हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830015,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.VISARJANIYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val target = targetTerm(context) ?: return false
        val index = context.terms.indexOf(target)
        if (index == context.terms.lastIndex) return true

        val next = context.terms[index + 1].surface.firstOrNull() ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.KHAR, next)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val target = requireNotNull(targetTerm(context))
        val source = if (target.surface.endsWith("ष्")) 'ष' else 'र'
        val newSurface = if (source == 'ष') {
            target.surface.dropLast(2) + Ayogavaha.VISARGA.devanagari
        } else {
            target.surface.dropLast(2) + Ayogavaha.VISARGA.devanagari
        }

        return DerivationChange(
            state = context.replaceTerm(target.id, target.copy(surface = newSurface))
                .copy(stage = DerivationStage.FINAL)
                .addSubstitution(VarnaSubstitution(target.id, source, Ayogavaha.VISARGA.devanagari, number)),
            explanation = "8.3.15: Replaced final 'r' with visarga (Avasāna)."
        )
    }

    private fun targetTerm(context: DerivationState): DerivationTerm? = context.terms.firstOrNull { term ->
        val isRutva = term.surface.endsWith("र्") && context.substitutions.any { substitution ->
            substitution.targetId == term.id && substitution.sutra == "8.2.66"
        }
        val isSuffixalSha = term.surface.endsWith("ष्") && term.kind == TermKind.PRATYAYA &&
            term.upadesha.endsWith("स्")
        isRutva || isSuffixalSha
    }
}
