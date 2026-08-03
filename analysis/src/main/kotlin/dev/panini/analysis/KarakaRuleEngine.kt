package dev.panini.analysis

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraType

/** Semantic kāraka assignment (1.4) followed by nominal-case validation (2.3). */
object KarakaRuleEngine {
    val karakaRules: List<Sutra<KarakaRuleContext, KarakaRuleResult>> by lazy {
        @Suppress("UNCHECKED_CAST")
        Ashtadhyayi.registry.sutras
            .filter { sutra ->
                sutra !is DerivationSutra &&
                    sutra.chapter == 1 &&
                    sutra.pada == 4 &&
                    sutra.action == SutraAction.SAMJNA &&
                    (sutra.number == "1.4.23" || sutra.adhikara.contains("1.4.23"))
            }
            .sortedBy { it.krama } as List<Sutra<KarakaRuleContext, KarakaRuleResult>>
    }

    val vibhaktiRules: List<Sutra<VibhaktiRuleContext, VibhaktiRuleResult>> by lazy {
        @Suppress("UNCHECKED_CAST")
        Ashtadhyayi.registry.sutras
            .filter { sutra ->
                sutra !is DerivationSutra &&
                    sutra.type != SutraType.NISHEDHA &&
                    sutra.action != SutraAction.NISHEDHA &&
                    sutra.chapter == 2 &&
                    sutra.pada == 3
            }
            .sortedBy { it.krama } as List<Sutra<VibhaktiRuleContext, VibhaktiRuleResult>>
    }

    fun resolve(context: KarakaRuleContext): KarakaResolution {
        val possibleVibhaktis = context.participant.possibleVibhaktis
        val candidates = context.candidates.ifEmpty {
            context.participant.possibleVibhaktis.mapNotNull { vibhakti ->
                KarakaInference.infer(vibhakti, context.prayoga, context.dhatu.sakarmaka)
            }.toSet()
        }
        val semanticContext = context.copy(candidates = candidates)
        val semantic = karakaRules.firstOrNull { rule ->
            val prohibition = NishedhaRuleEngine.evaluateProhibition(ProhibitionContext(targetSutraNumber = rule.number))
            prohibition !is NishedhaRuleResult.Blocked && try { rule.matches(semanticContext) } catch (_: ClassCastException) { false }
        }?.apply(semanticContext) as? KarakaRuleResult.Assigned
        val resolved = semantic?.karaka ?: candidates.singleOrNull()
        var resolvedVibhakti: Vibhakti? = null
        val evidence = buildList {
            semantic?.let { add(it.evidence) }
            resolved?.let { karaka ->
                val isAbhihita = when (context.prayoga) {
                    Prayoga.KARTARI -> karaka == Karaka.KARTR
                    Prayoga.KARMANI -> karaka == Karaka.KARMAN
                    Prayoga.CAUSATIVE -> karaka == Karaka.KARTR
                    Prayoga.BHAVE -> false // In Bhāve, tin denotes bhāva (action), so Kartṛ is unexpressed (anabhihita) -> Tṛtīyā
                    Prayoga.ANIRDHARITA -> false
                }
                val vibhaktiContext = VibhaktiRuleContext(karaka, possibleVibhaktis, abhihita = isAbhihita, participant = context.participant)
                val assignment = vibhaktiRules.firstOrNull { rule ->
                    val prohibition = NishedhaRuleEngine.evaluateProhibition(ProhibitionContext(targetSutraNumber = rule.number))
                    val adhikaraEligible = isVibhaktiEligible(rule.krama, vibhaktiContext)
                    prohibition !is NishedhaRuleResult.Blocked && adhikaraEligible && try { rule.matches(vibhaktiContext) } catch (_: ClassCastException) { false }
                }?.apply(vibhaktiContext) as? VibhaktiRuleResult.Assigned
                assignment?.let {
                    resolvedVibhakti = it.vibhakti
                    add(it.evidence)
                }
            }
        }
        return KarakaResolution(candidates, resolved, possibleVibhaktis, evidence, resolvedVibhakti)
    }

    private data class AdhikaraDomain(
        val role: SutraRole.Adhikara,
        val startKrama: Int,
        val endKrama: Int,
    )

    private val activeAdhikaraDomains: List<AdhikaraDomain> by lazy {
        Ashtadhyayi.adhikaraSutras.mapNotNull { domain ->
            val role = domain.role as? SutraRole.Adhikara ?: return@mapNotNull null
            val start = role.customStartKrama ?: domain.krama
            AdhikaraDomain(role, start, role.endKrama)
        }
    }

    /** Evaluates whether a vibhakti rule is governed and permitted by active Adhikara domains (e.g. 2.3.1 Anabhihite). */
    fun isVibhaktiEligible(sutraKrama: Int, context: VibhaktiRuleContext): Boolean {
        return activeAdhikaraDomains.all { domain ->
            if (sutraKrama in domain.startKrama..domain.endKrama) {
                domain.role.isContextEligible(context)
            } else {
                true
            }
        }
    }
}
