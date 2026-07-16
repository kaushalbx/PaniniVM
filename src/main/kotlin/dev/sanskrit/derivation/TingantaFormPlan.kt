package dev.sanskrit.derivation

import dev.sanskrit.dhatupatha.PadaType

/** A form slot that the current verbal compiler can derive end to end. */
data class TingantaFormPlan(
    val affix: TingAffix,
    val lakara: Lakara,
    val requiredSutras: Set<String>,
    val finalStage: DerivationStage = DerivationStage.FINAL,
)

/** Declared coverage for the complete 18-slot tiṅ inventory across multiple lakāras. */
object TingantaFormPlans {
    private val supported = buildList {
        // LAT (present tense) plans
        TingAffix.entries.forEach { affix ->
            add(TingantaFormPlan(affix, Lakara.LAT, setOf("3.4.78"), DerivationStage.FINAL))
        }
        // LRT (future tense) plans
        TingAffix.entries.forEach { affix ->
            // For LRT, AdesapratyayayohSutra (8.3.59) runs at the very end and advances the stage to FINAL.
            add(TingantaFormPlan(affix, Lakara.LRT, setOf("3.4.78"), DerivationStage.FINAL))
        }
        // LANG (imperfect past tense) plans
        TingAffix.entries.forEach { affix ->
            add(TingantaFormPlan(affix, Lakara.LANG, setOf("3.4.78"), DerivationStage.FINAL))
        }
        // LOT (imperative) plans
        TingAffix.entries.filter { it.pada == PadaType.PARASMAIPADA }.forEach { affix ->
            add(TingantaFormPlan(affix, Lakara.LOT, setOf("3.3.162", "3.4.78"), DerivationStage.FINAL))
        }
        // LING (vidhi-liṅ) plans
        TingAffix.entries.filter { it.pada == PadaType.PARASMAIPADA }.forEach { affix ->
            add(TingantaFormPlan(affix, Lakara.LING, setOf("3.3.161", "3.4.78", "3.4.103"), DerivationStage.FINAL))
        }
        TingAffix.entries.filter { it.pada == PadaType.ATMANEPADA }.forEach { affix ->
            add(TingantaFormPlan(affix, Lakara.LING, setOf("3.3.161", "3.4.78", "3.4.102"), DerivationStage.FINAL))
        }
        // LRNG (conditional) plans
        TingAffix.entries.forEach { affix ->
            add(TingantaFormPlan(affix, Lakara.LRNG, setOf("3.4.78"), DerivationStage.FINAL))
        }
        // LIT (perfect tense) plans
        TingAffix.entries.filter { it.pada == PadaType.PARASMAIPADA }.forEach { affix ->
            add(TingantaFormPlan(affix, Lakara.LIT, setOf("3.4.78", "3.4.82"), DerivationStage.FINAL))
        }
        // LUT (periphrastic future) plans
        TingAffix.entries.filter { it.pada == PadaType.PARASMAIPADA }.forEach { affix ->
            val required = mutableSetOf("3.3.15", "3.1.33", "3.4.78")
            if (affix.purusha == Purusha.PRATHAMA) required += "2.4.85"
            add(TingantaFormPlan(affix, Lakara.LUT, required, DerivationStage.FINAL))
        }
    }

    fun find(purusha: Purusha, vacana: Vacana, pada: PadaType, lakara: Lakara): TingantaFormPlan? =
        supported.singleOrNull { it.affix.purusha == purusha && it.affix.vacana == vacana && it.affix.pada == pada && it.lakara == lakara }

    fun all(): List<TingantaFormPlan> = supported
}
