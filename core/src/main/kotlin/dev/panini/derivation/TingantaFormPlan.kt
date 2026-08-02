package dev.panini.derivation

import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.core.Purusha
import dev.panini.core.TingAffix
import dev.panini.core.Vacana

/** A form slot that the current verbal compiler can derive end to end. */
data class TingantaFormPlan(
    val affix: TingAffix,
    val lakara: Lakara,
    val requiredSutras: Set<String>,
    val finalStage: DerivationStage = DerivationStage.FINAL,
    val supportedGanas: Set<DhatuGana>? = null,
)

/** Declared coverage for the complete 18-slot tiṅ inventory across multiple lakāras. */
object TingantaFormPlans {
    private val supported = buildList {
        // LAT (present tense) plans
        TingAffix.entries.forEach { affix ->
            add(TingantaFormPlan(affix, Lakara.LAT, setOf("3.4.78"), DerivationStage.FINAL, setOf(DhatuGana.BHVADI)))
            add(TingantaFormPlan(affix, Lakara.LAT, setOf("3.4.78", "2.4.72"), DerivationStage.FINAL, setOf(DhatuGana.ADADI)))
            val juhotyadiRequired = mutableSetOf("3.4.78", "2.4.75", "6.1.10", "6.1.4")
            if (affix in setOf(TingAffix.TIP, TingAffix.SIP, TingAffix.MIP)) juhotyadiRequired += "7.3.84"
            if (affix == TingAffix.JHI) juhotyadiRequired += "7.1.4"
            add(TingantaFormPlan(affix, Lakara.LAT, juhotyadiRequired, DerivationStage.FINAL, setOf(DhatuGana.JUHOTYADI)))
            add(TingantaFormPlan(affix, Lakara.LAT, setOf("3.4.78", "3.1.69"), DerivationStage.FINAL, setOf(DhatuGana.DIVADI)))
            val svadiRequired = mutableSetOf("3.4.78", "3.1.73")
            if (affix in setOf(TingAffix.TIP, TingAffix.SIP, TingAffix.MIP)) svadiRequired += "7.3.84"
            if (affix == TingAffix.JHA) svadiRequired += "7.1.5"
            add(TingantaFormPlan(affix, Lakara.LAT, svadiRequired, DerivationStage.FINAL, setOf(DhatuGana.SVADI)))
            val tudadiRequired = mutableSetOf("3.4.78", "3.1.77")
            if (affix in setOf(TingAffix.ATAM, TingAffix.ATHAM)) tudadiRequired += "7.2.81"
            add(TingantaFormPlan(affix, Lakara.LAT, tudadiRequired, DerivationStage.FINAL, setOf(DhatuGana.TUDADI)))
            val rudhadiRequired = mutableSetOf("3.4.78", "3.1.78")
            if (affix in setOf(TingAffix.TIP, TingAffix.SIP, TingAffix.MIP)) rudhadiRequired += "8.4.2"
            if (affix in setOf(TingAffix.TIP, TingAffix.TAS, TingAffix.THAS, TingAffix.THA, TingAffix.TA)) rudhadiRequired += "8.2.40"
            if (affix.pada == PadaType.ATMANEPADA) {
                rudhadiRequired += if (affix == TingAffix.THAS_A) "3.4.80" else "3.4.79"
                if (affix == TingAffix.JHA) rudhadiRequired += "7.1.5"
            }
            add(TingantaFormPlan(affix, Lakara.LAT, rudhadiRequired, DerivationStage.FINAL, setOf(DhatuGana.RUDHADI)))
            val tanadiRequired = mutableSetOf("3.4.78", "3.1.79")
            if (affix in setOf(TingAffix.TIP, TingAffix.SIP, TingAffix.MIP)) tanadiRequired += "7.3.84"
            if (affix == TingAffix.JHA) tanadiRequired += "7.1.5"
            add(TingantaFormPlan(affix, Lakara.LAT, tanadiRequired, DerivationStage.FINAL, setOf(DhatuGana.TANADI)))
            val kryadiRequired = mutableSetOf("3.4.78", "3.1.81", "8.4.2")
            if (affix.pada == PadaType.ATMANEPADA) {
                kryadiRequired += if (affix == TingAffix.THAS_A) "3.4.80" else "3.4.79"
                if (affix == TingAffix.JHA) kryadiRequired += "7.1.5"
            }
            if (affix == TingAffix.THAS_A) kryadiRequired += "8.3.59"
            add(TingantaFormPlan(affix, Lakara.LAT, kryadiRequired, DerivationStage.FINAL, setOf(DhatuGana.KRYADI)))
            val curadiRequired = mutableSetOf("3.4.78", "3.1.25")
            if (affix in setOf(TingAffix.ATAM, TingAffix.ATHAM)) curadiRequired += "7.2.81"
            add(TingantaFormPlan(affix, Lakara.LAT, curadiRequired, DerivationStage.FINAL, setOf(DhatuGana.CURADI)))
        }
        // LRT (future tense) plans
        TingAffix.entries.filter { it.pada == PadaType.PARASMAIPADA }.forEach { affix ->
            // For LRT, AdesapratyayayohSutra (8.3.59) runs at the very end and advances the stage to FINAL.
            add(TingantaFormPlan(affix, Lakara.LRT, setOf("3.4.78"), DerivationStage.FINAL))
        }
        TingAffix.entries.filter { it.pada == PadaType.ATMANEPADA }.forEach { affix ->
            val required = mutableSetOf("3.4.78")
            required += if (affix == TingAffix.THAS_A) "3.4.80" else "3.4.79"
            add(TingantaFormPlan(affix, Lakara.LRT, required, DerivationStage.FINAL))
        }
        // LANG (imperfect past tense) plans — per-gaṇa, so requiredSutras enforces vikaraṇa
        DhatuGana.entries.forEach { gana ->
            TingAffix.entries.filter { it.pada == PadaType.PARASMAIPADA }.forEach { affix ->
                val required = (mutableSetOf("3.4.78", "6.4.71") + vikaranaSutras(gana)).toMutableSet()
                if (gana == DhatuGana.JUHOTYADI && affix == TingAffix.JHI) required += "3.4.109"
                add(TingantaFormPlan(affix, Lakara.LANG, required, DerivationStage.FINAL, setOf(gana)))
            }
            TingAffix.entries.filter { it.pada == PadaType.ATMANEPADA }.forEach { affix ->
                val required = (mutableSetOf("3.4.78", "6.4.71") + vikaranaSutras(gana)).toMutableSet()
                val hasAEndingStem = gana in setOf(DhatuGana.BHVADI, DhatuGana.DIVADI, DhatuGana.TUDADI, DhatuGana.CURADI)
                val usesAtoNgit = hasAEndingStem && affix in setOf(TingAffix.ATAM, TingAffix.ATHAM)
                if (usesAtoNgit) required += "7.2.81"
                if (affix == TingAffix.JHA) required += if (hasAEndingStem) "7.1.3" else "7.1.5"
                val finalStage = if (!hasAEndingStem ||
                    affix in setOf(TingAffix.TA, TingAffix.DHVAM, TingAffix.VAHI, TingAffix.MAHING)) {
                    DerivationStage.IT_PROCESSED
                } else {
                    DerivationStage.FINAL
                }
                add(TingantaFormPlan(affix, Lakara.LANG, required, finalStage, setOf(gana)))
            }
        }
        // LOT (imperative) plans — per-gaṇa for non-Kryādi, Kryādi kept separate
        DhatuGana.entries.filter { it != DhatuGana.KRYADI }.forEach { gana ->
            TingAffix.entries.filter { it.pada == PadaType.PARASMAIPADA }.forEach { affix ->
                val required = mutableSetOf("3.3.162", "3.4.78") + vikaranaSutras(gana)
                add(TingantaFormPlan(affix, Lakara.LOT, required, DerivationStage.FINAL, setOf(gana)))
            }
            TingAffix.entries.filter { it.pada == PadaType.ATMANEPADA }.forEach { affix ->
                val required = (mutableSetOf("3.3.162", "3.4.78") + vikaranaSutras(gana)).toMutableSet()
                when (affix) {
                    TingAffix.TA, TingAffix.ATAM, TingAffix.JHA, TingAffix.ATHAM -> required += setOf("3.4.79", "3.4.90")
                    TingAffix.THAS_A -> required += setOf("3.4.80", "3.4.91")
                    TingAffix.DHVAM -> required += setOf("3.4.79", "3.4.91")
                    TingAffix.IT, TingAffix.VAHI, TingAffix.MAHING -> required += setOf("3.4.79", "3.4.92", "3.4.93")
                    else -> Unit
                }
                add(TingantaFormPlan(affix, Lakara.LOT, required, DerivationStage.FINAL, setOf(gana)))
            }
        }
        // Kryādi LOT (unchanged — separate because 3.1.81 + 8.4.2 are both required)
        TingAffix.entries.forEach { affix ->
            val required = mutableSetOf("3.3.162", "3.4.78", "3.1.81", "8.4.2")
            if (affix.pada == PadaType.ATMANEPADA) {
                when (affix) {
                    TingAffix.TA, TingAffix.ATAM, TingAffix.JHA, TingAffix.ATHAM -> required += setOf("3.4.79", "3.4.90")
                    TingAffix.THAS_A -> required += setOf("3.4.80", "3.4.91", "8.3.59")
                    TingAffix.DHVAM -> required += setOf("3.4.79", "3.4.91")
                    TingAffix.IT, TingAffix.VAHI, TingAffix.MAHING -> required += setOf("3.4.79", "3.4.92", "3.4.93")
                    else -> Unit
                }
            }
            add(TingantaFormPlan(affix, Lakara.LOT, required, DerivationStage.FINAL, setOf(DhatuGana.KRYADI)))
        }
        // LING (vidhi-liṅ) plans — per-gaṇa, so requiredSutras enforces vikaraṇa
        DhatuGana.entries.forEach { gana ->
            TingAffix.entries.filter { it.pada == PadaType.PARASMAIPADA }.forEach { affix ->
                val required = mutableSetOf("3.3.161", "3.4.78", "3.4.103") + vikaranaSutras(gana)
                add(TingantaFormPlan(affix, Lakara.LING, required, DerivationStage.FINAL, setOf(gana)))
            }
            TingAffix.entries.filter { it.pada == PadaType.ATMANEPADA }.forEach { affix ->
                val required = mutableSetOf("3.3.161", "3.4.78", "3.4.102") + vikaranaSutras(gana)
                add(TingantaFormPlan(affix, Lakara.LING, required, DerivationStage.FINAL, setOf(gana)))
            }
        }
        // LRNG (conditional) plans
        TingAffix.entries.filter { it.pada == PadaType.PARASMAIPADA }.forEach { affix ->
            add(TingantaFormPlan(affix, Lakara.LRNG, setOf("3.4.78"), DerivationStage.FINAL))
        }
        TingAffix.entries.filter { it.pada == PadaType.ATMANEPADA }.forEach { affix ->
            val required = mutableSetOf("3.4.78", "3.1.33", "6.4.71")
            if (affix in setOf(TingAffix.ATAM, TingAffix.ATHAM)) required += "7.2.81"
            if (affix == TingAffix.JHA) required += "7.1.3"
            val finalStage = if (affix in setOf(TingAffix.TA, TingAffix.DHVAM, TingAffix.VAHI, TingAffix.MAHING)) {
                DerivationStage.IT_PROCESSED
            } else {
                DerivationStage.FINAL
            }
            add(TingantaFormPlan(affix, Lakara.LRNG, required, finalStage))
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
        TingAffix.entries.filter { it.pada == PadaType.ATMANEPADA }.forEach { affix ->
            val required = mutableSetOf("3.3.15", "3.1.33", "3.4.78")
            if (affix.purusha == Purusha.PRATHAMA) required += "2.4.85"
            when (affix) {
                TingAffix.THAS_A -> required += setOf("3.4.80", "7.4.50")
                TingAffix.ATHAM, TingAffix.VAHI, TingAffix.MAHING -> required += "3.4.79"
                TingAffix.DHVAM -> required += setOf("3.4.79", "8.2.25")
                TingAffix.IT -> required += setOf("3.4.79", "7.4.52")
                else -> Unit
            }
            add(TingantaFormPlan(affix, Lakara.LUT, required, DerivationStage.FINAL))
        }
        // LUNG (general & 7-variety aorist) plans
        TingAffix.entries.filter { it.pada == PadaType.PARASMAIPADA }.forEach { affix ->
            add(TingantaFormPlan(affix, Lakara.LUNG, setOf("3.4.78", "6.4.71"), DerivationStage.FINAL))
        }
        TingAffix.entries.filter { it.pada == PadaType.ATMANEPADA }.forEach { affix ->
            val required = mutableSetOf("3.4.78", "6.4.71")
            add(TingantaFormPlan(affix, Lakara.LUNG, required, DerivationStage.IT_PROCESSED))
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
            val required = mutableSetOf("3.4.7", "3.4.78", "3.4.94")
            required += if (affix == TingAffix.THAS_A) "3.4.80" else "3.4.79"
            if (affix in setOf(TingAffix.ATAM, TingAffix.ATHAM)) required += "3.4.95"
            add(TingantaFormPlan(affix, Lakara.LET, required, DerivationStage.FINAL))
        }
    }

    fun find(purusha: Purusha, vacana: Vacana, pada: PadaType, lakara: Lakara, gana: DhatuGana): TingantaFormPlan? =
        supported.singleOrNull {
            it.affix.purusha == purusha && it.affix.vacana == vacana && it.affix.pada == pada &&
                it.lakara == lakara && (it.supportedGanas == null || gana in it.supportedGanas)
        }

    fun all(): List<TingantaFormPlan> = supported

    /**
     * Returns the sūtra numbers that select the vikaraṇa for a given gaṇa.
     * Used as the gaṇa-specific component of requiredSutras for LANG, LOT, and LING plans.
     */
    private fun vikaranaSutras(gana: DhatuGana): Set<String> = when (gana) {
        DhatuGana.BHVADI    -> emptySet()          // शप् is selected automatically; LAT plan only required 3.4.78
        DhatuGana.ADADI     -> setOf("2.4.72")
        DhatuGana.JUHOTYADI -> setOf("2.4.75", "6.1.10", "6.1.4")
        DhatuGana.DIVADI    -> setOf("3.1.69")
        DhatuGana.SVADI     -> setOf("3.1.73")
        DhatuGana.TUDADI    -> setOf("3.1.77")
        DhatuGana.RUDHADI   -> setOf("3.1.78")
        DhatuGana.TANADI    -> setOf("3.1.79")
        DhatuGana.KRYADI    -> setOf("3.1.81")
        DhatuGana.CURADI    -> setOf("3.1.25")
    }
}
