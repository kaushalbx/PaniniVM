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
        TingAffix.entries.filter { it.pada == PadaType.ATMANEPADA }.forEach { affix ->
            val required = mutableSetOf("3.3.162", "3.4.78")
            when (affix) {
                TingAffix.TA, TingAffix.ATAM, TingAffix.JHA, TingAffix.ATHAM -> required += setOf("3.4.79", "3.4.90")
                TingAffix.THAS_A -> required += setOf("3.4.80", "3.4.91")
                TingAffix.DHVAM -> required += setOf("3.4.79", "3.4.91")
                TingAffix.IT, TingAffix.VAHI, TingAffix.MAHING -> required += setOf("3.4.79", "3.4.92", "3.4.93")
                else -> Unit
            }
            add(TingantaFormPlan(affix, Lakara.LOT, required, DerivationStage.FINAL))
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
        TingAffix.entries.filter { it.pada == PadaType.ATMANEPADA }.forEach { affix ->
            val required = mutableSetOf("3.4.78")
            when (affix) {
                TingAffix.TA, TingAffix.JHA -> required += "3.4.81"
                TingAffix.THAS_A -> required += "3.4.80"
                else -> required += "3.4.79"
            }
            add(TingantaFormPlan(affix, Lakara.LIT, required, DerivationStage.FINAL))
        }
        // LUT (periphrastic future) plans
        TingAffix.entries.filter { it.pada == PadaType.PARASMAIPADA }.forEach { affix ->
            val required = mutableSetOf("3.3.15", "3.1.33", "3.4.78")
            if (affix.purusha == Purusha.PRATHAMA) required += "2.4.85"
            add(TingantaFormPlan(affix, Lakara.LUT, required, DerivationStage.FINAL))
        }
        // LUNG (general aorist) plans
        TingAffix.entries.filter { it.pada == PadaType.PARASMAIPADA }.forEach { affix ->
            add(TingantaFormPlan(affix, Lakara.LUNG, setOf("3.2.110", "3.1.43", "3.1.44", "2.4.77", "3.4.78", "6.4.71"), DerivationStage.FINAL))
        }
        TingAffix.entries.filter { it.pada == PadaType.ATMANEPADA }.forEach { affix ->
            val required = mutableSetOf("3.2.110", "3.1.43", "3.1.44", "3.4.78", "6.4.71", "7.2.42")
            if (affix != TingAffix.DHVAM) required += "8.3.59"
            if (affix == TingAffix.JHA) required += "7.1.5"
            if (affix in setOf(TingAffix.TA, TingAffix.THAS_A)) required += "8.4.41"
            if (affix == TingAffix.DHVAM) required += setOf("8.2.25", "8.3.79")
            add(TingantaFormPlan(affix, Lakara.LUNG, required, DerivationStage.FINAL))
        }
        // LET (Vedic subjunctive) plans
        TingAffix.entries.filter { it.pada == PadaType.PARASMAIPADA }.forEach { affix ->
            val required = mutableSetOf("3.4.7", "3.4.78", "3.4.94")
            if (affix == TingAffix.MIP) required += "3.1.85"
            if (affix in setOf(TingAffix.TIP, TingAffix.JHI, TingAffix.SIP)) required += "3.4.97"
            if (affix in setOf(TingAffix.VAS, TingAffix.MAS)) required += "3.4.98"
            add(TingantaFormPlan(affix, Lakara.LET, required, DerivationStage.FINAL))
        }
        TingAffix.entries.filter { it.pada == PadaType.ATMANEPADA }.forEach { affix ->
            val required = mutableSetOf("3.4.7", "3.4.78", "3.4.94", "3.4.79")
            if (affix == TingAffix.THAS_A) required += "3.4.80"
            if (affix in setOf(TingAffix.ATAM, TingAffix.ATHAM)) required += "3.4.95"
            add(TingantaFormPlan(affix, Lakara.LET, required, DerivationStage.FINAL))
        }
    }

    fun find(purusha: Purusha, vacana: Vacana, pada: PadaType, lakara: Lakara): TingantaFormPlan? =
        supported.singleOrNull { it.affix.purusha == purusha && it.affix.vacana == vacana && it.affix.pada == pada && it.lakara == lakara }

    fun all(): List<TingantaFormPlan> = supported
}
