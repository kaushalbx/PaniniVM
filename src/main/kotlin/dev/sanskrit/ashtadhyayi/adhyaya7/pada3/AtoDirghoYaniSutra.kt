package dev.sanskrit.ashtadhyayi.adhyaya7.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 7.3.101: ato dīrgho yañi. 
 * The final 'a' of an aṅga is lengthened when followed by a 'yañ'-initial suffix.
 * Primarily used in verbal derivation (e.g., bhavāmi).
 */
object AtoDirghoYaniSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.101",
    text = "अतो दीर्घो यञि",
    hindiExplanation = "यञादि प्रत्यय परे होने पर अदन्त अङ्ग को दीर्घ होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730101,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false
        
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isAEnding = stem.surface.endsWith('अ')
        val startsWithYan = affix.surface.firstOrNull() in setOf('य', 'व', 'र', 'ल', 'ञ', 'म', 'ङ', 'ण', 'न')
        
        return isAEnding && startsWithYan
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val newSurface = stem.surface.dropLast(1) + "ा"
        
        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.101: Lengthened final 'a' before yañ-initial suffix."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(stem.id, 'अ', "ा", sutra))) }
    }
}
