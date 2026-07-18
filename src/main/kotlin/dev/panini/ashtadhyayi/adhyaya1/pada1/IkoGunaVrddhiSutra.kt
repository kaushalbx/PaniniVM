package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.PhonologicalRequest
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 1.1.3: iko guṇavṛddhī.
 * Supplies the prescribed guna or vrddhi substitute for an ik vowel.
 * Uses 1.1.50 (Sthāne'ntaratamaḥ) logic to select the most phonetically similar substitute.
 */
object IkoGunaVrddhiSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.3",
    text = "इको गुणवृद्धी",
    hindiExplanation = "इक् के स्थान पर विहित गुण या वृद्धि का आदेश होता है।",
    englishExplanation = "Supplies the prescribed guna or vrddhi substitute for an ik vowel.",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110003,
    role = SutraRole.Atidesha,
    action = SutraAction.ATIDESHA,
    scope = SutraScope.DERIVATION,
    inputs = setOf(SutraInput.VARNA, SutraInput.SEMANTIC_FEATURE),
    dependencies = setOf("1.1.1", "1.1.2", "1.1.50"),
    stage = SutraStage.ANGAKARYA,
    traceTemplateValue = "{sutra} substitutes the requested गुण or वृद्धि form of इक्.",
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val hasRequest = context.effectiveContext.phonologicalRequest != null
        if (!hasRequest) return false

        val engine = Ashtadhyayi.pratyaharaEngine
        return context.terms.any { term ->
            term.surface.any { engine.contains(Pratyahara.IK, it) }
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val isVrddhi = context.effectiveContext.phonologicalRequest == PhonologicalRequest.VRDDHI
        val candidates = if (isVrddhi) vrddhiVowels else gunaVowels

        var state = context
        val engine = Ashtadhyayi.pratyaharaEngine

        context.terms.forEach { term ->
            term.surface.forEach { sourceChar ->
                if (engine.contains(Pratyahara.IK, sourceChar)) {
                    val replacement = SthaneAntaratamahSutra.selectBest(sourceChar, candidates)

                    // Pāṇinian special case: 1.1.51 (uraṇ raparaḥ).
                    // When 'a' replaces 'ṛ', it is followed by 'r'.
                    val finalReplacement = if ((sourceChar == 'ऋ' || sourceChar == 'ॠ' || sourceChar == 'ृ' || sourceChar == 'ॄ') && (replacement == "अ" || replacement == "ा")) {
                         replacement + Vyanjana.RA.halanta
                    } else if ((sourceChar == 'ऌ' || sourceChar == 'ॢ') && (replacement == "अ" || replacement == "ा")) {
                         replacement + Vyanjana.LA.halanta
                    } else {
                        replacement
                    }

                    state = state.replaceTerm(
                        term.id,
                        term.copy(surface = term.surface.replaceFirst(sourceChar.toString(), finalReplacement))
                    ).addSubstitution(VarnaSubstitution(term.id, sourceChar, finalReplacement, sutra))
                }
            }
        }

        return DerivationChange(
            state,
            "$sutra applies ${if (isVrddhi) "वृद्धि" else "गुण"} substitution via similarity (1.1.50)."
        )
    }
}

private val gunaVowels = setOf("अ", "ए", "ओ")
private val vrddhiVowels = setOf("आ", "ऐ", "औ")
