package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import dev.panini.core.TaddhitaAffix
import dev.panini.core.TingAffix
import dev.panini.derivation.hasCurrentAffix
import dev.panini.vyakaranam.ast.Vikarana
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varnamala
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.lastVarna
import dev.panini.shiksha.replaceLastVarna
import dev.panini.shiksha.toDevanagari
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/** 6.1.87: a/ā + ac -> guṇa. */
object AdGunaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.87",
    text = "आद्गुणः",
    hindiExplanation = "अ या आ के बाद अच् आए तो पूर्व और पर के स्थान पर एक गुणादेश होता है।",
    type = SutraType.UTSARGA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610087,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = SutraStage.POST_RUTVA,
    blocks = setOf("6.1.77"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        val rutvaFollowUp = context.substitutions.lastOrNull()?.takeIf { it.sutra in setOf("6.1.113", "6.1.114") }
        if (rutvaFollowUp != null && context.terms.any { it.id == rutvaFollowUp.targetId && it.varnas.takeLast(2) == listOf(Svara.A, Svara.U) }) return true
        if (context.terms.size < 2) return false
        if (context.effectiveContext.rupa.lakara == Lakara.LOT && context.terms.last().hasCurrentAffix(TingAffix.JHI)) return false
        val engine = Ashtadhyayi.pratyaharaEngine
        return context.terms.indices.any { index ->
            if (index == context.terms.lastIndex) return@any false
            val leftTerm = context.terms[index]
            val rightTerm = context.terms[index + 1]
            if (rightTerm.upadesha == "इट्" && rightTerm.varnas.lastOrNull() == Vyanjana.TTA) return@any false
            val isTaddhita = "4.1.76" in context.activeAdhikaras ||
                TaddhitaAffix.entries.any(rightTerm::hasCurrentAffix)
            if (isTaddhita) return@any false
            val right = rightTerm.varnas.firstOrNull() ?: return@any false
            val leftVarnas = leftTerm.varnas
            val isA = leftVarnas.lastOrNull() in setOf(Svara.A, Svara.AA)
            val previousEndsInEc = index > 0 && context.terms[index - 1].varnas.lastOrNull()?.let {
                engine.contains(Pratyahara.EC, it)
            } == true
            val isFutureSya = leftTerm.kind == TermKind.PRATYAYA && leftTerm.upadesha == Vikarana.SYA.upadesha &&
                context.effectiveContext.rupa.lakara in setOf(Lakara.LRT, Lakara.LRNG)
            val isAdadiShap = leftTerm.id == "shap" && context.terms.any { it.kind == TermKind.DHATU && it.gana == DhatuGana.ADADI }
            !isAdadiShap && (!previousEndsInEc || isFutureSya) &&
                (leftTerm.kind == TermKind.PRATIPADIKA || leftVarnas.getOrNull(leftVarnas.lastIndex - 1) != Vyanjana.NA) &&
                isA && engine.contains(Pratyahara.AC, right)
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        if (context.substitutions.lastOrNull()?.sutra in setOf("6.1.113", "6.1.114")) {
            val targetId = context.substitutions.last().targetId
            val target = context.terms.firstOrNull { it.id == targetId && it.varnas.takeLast(2) == listOf(Svara.A, Svara.U) }
            if (target != null) return DerivationChange(
                state = context.replaceTermVarnaRange(target.id, target.varnas.size - 2, 2,
                    listOf(Svara.O), annotationSources = mapOf(0 to 0), sutra = sutra)
                    .copy(stage = DerivationStage.PADA_FORMED),
                explanation = "6.1.87: Guṇa substitution ओ for अ + उ from रुँ."
            )
        }
        val terms = context.terms
        val index = terms.indices.first { position ->
            position < terms.lastIndex &&
                terms[position].varnas.lastOrNull() in setOf(Svara.A, Svara.AA) &&
                !(terms[position].id == "shap" && terms.any { it.kind == TermKind.DHATU && it.gana == DhatuGana.ADADI }) &&
                !(terms[position + 1].upadesha == "इट्" && terms[position + 1].varnas.lastOrNull() == Vyanjana.TTA) &&
                !("4.1.76" in context.activeAdhikaras ||
                    TaddhitaAffix.entries.any(terms[position + 1]::hasCurrentAffix)) &&
                (terms[position].kind == TermKind.PRATIPADIKA ||
                    terms[position].varnas.let { it.getOrNull(it.lastIndex - 1) != Vyanjana.NA }) &&
                (position == 0 ||
                    terms[position - 1].varnas.lastOrNull()?.let {
                        !Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.EC, it)
                    } != false ||
                    (terms[position].kind == TermKind.PRATYAYA && terms[position].upadesha == Vikarana.SYA.upadesha &&
                        context.effectiveContext.rupa.lakara in setOf(Lakara.LRT, Lakara.LRNG))) &&
                terms[position + 1].varnas.firstOrNull()?.let {
                    Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AC, it)
                } == true
        }
        val leftTerm = terms[index]
        val rightTerm = terms[index + 1]

        val leftVarnas = leftTerm.varnas
        val rightVarnas = rightTerm.varnas
        val leftVowel = leftVarnas.last() as Svara
        val rightVowel = rightVarnas.first() as Svara
        val substitute = Varnamala.getGuna(rightVowel) ?: listOf(Svara.A)
        val isBeginningAugment = leftTerm.kind == TermKind.AGAMA &&
            !leftTerm.mergeIntoAugmentTarget &&
            leftTerm.augmentTargetId == rightTerm.id &&
            "1.1.46" in leftTerm.establishedBySutras

        if (isBeginningAugment) {
            val replaced = if (substitute == listOf(leftVowel)) context else
                context.replaceTermVarna(leftTerm.id, leftVarnas.lastIndex, substitute, sutra)
            val composed = replaced.deleteTermVarnas(rightTerm.id, 0, 1, sutra)
                .concatenatePrecedingAugment(rightTerm.id, leftTerm.id, sutra)
            return DerivationChange(
                state = composed.copy(stage = DerivationStage.PADA_FORMED),
                explanation = "6.1.87: Guṇa substitution (${substitute.toDevanagari()}) for ${leftVowel.devanagari} + ${rightVowel.devanagari}.",
            )
        }
        val replaced = if (substitute == listOf(leftVowel)) context else
            context.replaceTermVarna(leftTerm.id, leftVarnas.lastIndex, substitute, sutra)
        val composed = replaced.deleteTermVarnas(rightTerm.id, 0, 1, sutra)
            .concatenateAfterInitialVowelCoalescence(leftTerm.id, rightTerm, sutra,
                transferDeferredDesignations = true)

        return DerivationChange(
            state = composed.copy(stage = DerivationStage.PADA_FORMED),
            explanation = "6.1.87: Guṇa substitution (${substitute.toDevanagari()}) for ${leftVowel.devanagari} + ${rightVowel.devanagari}."
        )
    }
}
