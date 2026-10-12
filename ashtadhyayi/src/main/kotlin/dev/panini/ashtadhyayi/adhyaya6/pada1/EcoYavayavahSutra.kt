package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.Lakara
import dev.panini.core.SanadiAffix
import dev.panini.core.KrtAffix
import dev.panini.derivation.matchesAffix
import dev.panini.derivation.hasCurrentAffix
import dev.panini.derivation.TermKind
import dev.panini.core.TingAffix
import dev.panini.vyakaranam.ast.Vikarana
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.firstVarna
import dev.panini.shiksha.lastVarna
import dev.panini.shiksha.toDevanagari
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/** 6.1.78: eco'yavāyāvaḥ. Substitute ay, av, āy, āv for e, o, ai, au before a vowel. */
object EcoYavayavahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.78",
    text = "एचोऽयवायावः",
    hindiExplanation = "एच् (ए, ओ, ऐ, औ) के बाद अच् (कोई स्वर) आए तो क्रम से अय्, अव्, आय् या आव् आदेश होते हैं।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610078,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        if (futureStemPending(context)) return false

        val engine = Ashtadhyayi.pratyaharaEngine
        for (i in 0 until context.terms.size - 1) {
            if (context.isBlockedAtBoundary(sutra, context.terms[i].id, context.terms[i + 1].id)) continue
            val rightTerm = context.terms[i + 1]
            if (nicGradeStillPending(context, rightTerm.id)) continue
            if (context.effectiveContext.rupa.lakara == Lakara.LET &&
                rightTerm.hasCurrentAffix(TingAffix.JHI) && "3.4.94" !in context.appliedSutras
            ) continue
            if (lotEndingReplacementPending(context, rightTerm.varnas)) continue
            val left = context.terms[i].varnas.lastOrNull() ?: continue
            val right = rightTerm.varnas.firstOrNull() ?: continue
            if (engine.contains(Pratyahara.EC, left) && engine.contains(Pratyahara.AC, right)) {
                return true
            }
        }
        return false
    }

    override fun apply(context: DerivationState): DerivationChange {
        if (futureStemPending(context)) return DerivationChange(context, "6.1.78: future stem formation is still pending")
        val engine = Ashtadhyayi.pratyaharaEngine
        for (i in 0 until context.terms.size - 1) {
            if (context.isBlockedAtBoundary(sutra, context.terms[i].id, context.terms[i + 1].id)) continue
            val leftTerm = context.terms[i]
            val rightTerm = context.terms[i+1]
            if (nicGradeStillPending(context, rightTerm.id)) continue
            if (context.effectiveContext.rupa.lakara == Lakara.LET &&
                rightTerm.hasCurrentAffix(TingAffix.JHI) && "3.4.94" !in context.appliedSutras
            ) continue
            if (lotEndingReplacementPending(context, rightTerm.varnas)) continue
            val leftVarna = leftTerm.varnas.lastOrNull() ?: continue
            val rightVarna = rightTerm.varnas.firstOrNull() ?: continue
            if (engine.contains(Pratyahara.EC, leftVarna) && engine.contains(Pratyahara.AC, rightVarna)) {
                val replacement = requireNotNull(adesha[leftVarna])
                // Completed external padas retain their boundary for Tripadi y/v-lopa.
                if (leftTerm.formedPadaRupa != null && rightTerm.formedPadaRupa != null) {
                    return DerivationChange(
                        context.replaceTermVarna(leftTerm.id, leftTerm.varnas.lastIndex, replacement, sutra),
                        "6.1.78: substituted ${replacement.toDevanagari()} at the external pada boundary.",
                    )
                }
                val newSamjnas = context.samjnas.map {
                    if (it.targetId == rightTerm.id && it.samjna != Samjna.PRATYAYA) it.copy(targetId = leftTerm.id) else it
                }.toSet()
                val merged = context.replaceTermVarna(leftTerm.id, leftTerm.varnas.lastIndex, replacement, sutra)
                    .concatenateFollowingTerm(leftTerm.id, rightTerm.id, sutra)
                    .copy(stage = DerivationStage.PADA_FORMED, samjnas = newSamjnas)
                val survivor = merged.terms.single { it.id == leftTerm.id }

                return DerivationChange(
                    state = merged.replaceTerm(
                        leftTerm.id, survivor.copy(sthaniProps = leftTerm.sthaniProps ?: rightTerm.sthaniProps),
                    ),
                    explanation = "6.1.78: substituted ${replacement.toDevanagari()} for $leftVarna before vowel."
                )
            }
        }
        return DerivationChange(context, "6.1.78: No match found")
    }

    private val adesha: Map<Varna, List<Varna>> = mapOf(
        Svara.E to listOf(Svara.A, Vyanjana.YA),
        Svara.O to listOf(Svara.A, Vyanjana.VA),
        Svara.AI to listOf(Svara.AA, Vyanjana.YA),
        Svara.AU to listOf(Svara.AA, Vyanjana.VA),
    )

    private fun lotEndingReplacementPending(context: DerivationState, varnas: List<Varna>): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LOT &&
            varnas in pendingLotEndings &&
            "3.4.90" !in context.appliedSutras

    private val pendingLotEndings: Set<List<Varna>> = setOf(
        listOf(Vyanjana.TA, Svara.E),
        listOf(Svara.E, Vyanjana.TA, Svara.E),
        listOf(Svara.AA, Vyanjana.TA, Svara.E),
        listOf(Vyanjana.NA, Vyanjana.TA, Svara.E),
        listOf(Svara.A, Vyanjana.NA, Vyanjana.TA, Svara.E),
        listOf(Svara.A, Vyanjana.TA, Svara.E),
        listOf(Svara.E, Vyanjana.THA, Svara.E),
        listOf(Svara.AA, Vyanjana.THA, Svara.E),
    )

    private fun futureStemPending(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara in setOf(Lakara.LRT, Lakara.LRNG) &&
            context.allEffectiveTerms.none {
                it.kind == TermKind.PRATYAYA && it.upadesha == Vikarana.SYA.upadesha
            }

    private fun nicGradeStillPending(context: DerivationState, rightTermId: String): Boolean {
        val nic = context.terms.firstOrNull {
            it.id == rightTermId && it.matchesAffix(SanadiAffix.NIC) && it.varnas == listOf(Svara.I)
        }
            ?: return false
        return context.allEffectiveTerms.none { it.id == "shap" } &&
            context.terms.dropWhile { it.id != nic.id }.drop(1).none { affix ->
                krtAfterNic.any(affix::matchesAffix)
            }
    }

    private val krtAfterNic = listOf(KrtAffix.KTA, KrtAffix.KTAVATU, KrtAffix.KTVA,
        KrtAffix.LYAP, KrtAffix.TUMUN, KrtAffix.TAVYAT, KrtAffix.ANIYAR,
        KrtAffix.NYAT, KrtAffix.NVUL, KrtAffix.TRC, KrtAffix.GHAN, KrtAffix.LYUT)
}
