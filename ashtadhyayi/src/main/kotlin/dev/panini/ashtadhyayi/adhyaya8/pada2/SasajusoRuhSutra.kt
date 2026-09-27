package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.VarnaSubstitution
import dev.panini.derivation.WholeAffixDesignationPolicy
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.OrthographicSign
import dev.panini.shiksha.OrthographicSignPlacement
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

object SasajusoRuhSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.66",
    text = "ससजुषो रुः",
    hindiExplanation = "पदान्त स् तथा सजुष् के अन्तिम ष् के स्थान पर रुँ आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820066,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.RUTVA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val eligibleStage = context.stage == DerivationStage.IT_PROCESSED ||
            context.stage == DerivationStage.PADA_FORMED ||
            context.stage == DerivationStage.FINAL
        if (!eligibleStage) return false

        val finalVarnas = context.terms.lastOrNull()?.varnas ?: return false
        return finalVarnas.lastOrNull() == Vyanjana.SA ||
            finalVarnas.takeLast(sajus.size) == sajus ||
            internalPadaIndex(context) >= 0
    }

    override fun apply(context: DerivationState): DerivationChange {
        val internalIndex = internalPadaIndex(context)
        val target = context.terms[internalIndex.takeIf { it >= 0 } ?: context.terms.lastIndex]
        val source = if (target.varnas.takeLast(sajus.size) == sajus) Vyanjana.SSA else Vyanjana.SA
        fun withFreshRutva(term: dev.panini.derivation.DerivationTerm): dev.panini.derivation.DerivationTerm {
            val varnas = term.varnas.dropLast(1) + listOf(Vyanjana.RA, Svara.U)
            val signs = listOf(OrthographicSignPlacement(OrthographicSign.CHANDRABINDU, varnas.size))
            val rawRutva = varnas.toDevanagari(signs)
            return term.replaceWholeAffix(
                replacementSurface = rawRutva,
                replacementUpadesha = rawRutva,
                sutra = number,
                policy = WholeAffixDesignationPolicy.FreshUpadesha,
            ).copy(orthographicSigns = signs)
        }
        val changed = internalIndex.takeIf { it >= 0 }?.let { index ->
            val target = context.terms[index]
            context.copy(terms = context.terms.toMutableList().also {
                it[index] = withFreshRutva(target)
            })
        } ?: context.copy(
            terms = context.terms.dropLast(1) + context.terms.last()
                .let(::withFreshRutva)
        )
        return DerivationChange(
            changed.addSubstitution(VarnaSubstitution(target.id, source.devanagari.single(), "रुँ", number)),
            "8.2.66 substitutes रुँ for पद-final ${source}्.",
        )
    }

    private fun internalPadaIndex(context: DerivationState): Int = context.terms.indices.firstOrNull { index ->
        if (index >= context.terms.lastIndex || context.terms[index].varnas.lastOrNull() != Vyanjana.SA) return@firstOrNull false
        val leftId = context.terms[index].id
        val rightId = context.terms[index + 1].id
        val bothPadas = context.terms.all { it.kind == TermKind.PRATIPADIKA } &&
            listOf(leftId, rightId).all { id -> context.samjnas.any { it.targetId == id && it.samjna == Samjna.PADA } }
        val bothSankhya = listOf(leftId, rightId).all { id -> context.samjnas.any { it.targetId == id && it.samjna == Samjna.SANKHYA } }
        bothPadas || bothSankhya
    } ?: -1

    private val sajus: List<Varna> = listOf(Vyanjana.SA, Svara.A, Vyanjana.JA, Svara.U, Vyanjana.SSA)
}
