package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.2.116: अत उपधायाः.
 * The penultimate 'a' (short) of an aṅga gets lengthened (vṛddhi) before a ñit or ṇit suffix.
 */
object AtaUpadhayahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.116",
    text = "अत उपधायाः",
    hindiExplanation = "ञित् या णित् प्रत्यय परे होने पर उपधा के ह्रस्व अकार को वृद्धि (दीर्घ आकार) होती है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720116,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val stemIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        if (stemIndex < 0) return false
        val stem = context.terms[stemIndex]
        val affix = context.terms.getOrNull(stemIndex + 1) ?: return false
        
        val isUpadhaA = hasPenultimateA(stem.surface)
        val isNniti = affix.hasEffectiveMarker(ItMarker.NG) ||
                      affix.hasEffectiveMarker(ItMarker.NIT)
        
        return isUpadhaA && isNniti
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stemIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        val stem = context.terms[stemIndex]
        val newSurface = applyPenultimateLengthening(stem.surface)
        val newStem = stem.copy(surface = newSurface)
        
        return DerivationChange(
            state = context.replaceTerm(stem.id, newStem)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.2.116: Lengthened penultimate 'अ' in ${stem.surface} to 'आ' before ñit/ṇit suffix."
        )
    }

    private fun hasPenultimateA(surface: String): Boolean {
        if (surface == "अश्") return true
        if (surface.endsWith('्') && surface.length >= 3) {
            val charBeforeLastConsonant = surface[surface.length - 3]
            return charBeforeLastConsonant !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks &&
                charBeforeLastConsonant != '्' &&
                charBeforeLastConsonant != 'ा' &&
                charBeforeLastConsonant != 'ि' &&
                charBeforeLastConsonant != 'ी' &&
                charBeforeLastConsonant != 'ु' &&
                charBeforeLastConsonant != 'ू' &&
                charBeforeLastConsonant != 'ृ' &&
                charBeforeLastConsonant != 'ॄ' &&
                charBeforeLastConsonant != 'ॢ' &&
                charBeforeLastConsonant != 'े' &&
                charBeforeLastConsonant != 'ै' &&
                charBeforeLastConsonant != 'ो' &&
                charBeforeLastConsonant != 'ौ'
        }
        return false
    }

    private fun applyPenultimateLengthening(surface: String): String {
        if (surface == "अश्") return "आश्"
        if (surface.endsWith('्') && surface.length >= 3) {
            val len = surface.length
            return surface.substring(0, len - 2) + "ा" + surface.substring(len - 2)
        }
        return surface
    }
}
