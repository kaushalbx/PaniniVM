package dev.sanskrit.derivation

/** A form slot that the current nominal compiler can derive end to end. */
data class SubantaFormPlan(
    val affix: SupAffix,
    val requiredSutras: Set<String>,
    val finalStage: DerivationStage = DerivationStage.FINAL,
)

/** Declared coverage, separate from the complete 21-slot sup inventory. */
object SubantaFormPlans {
    private val supported = listOf(
        SubantaFormPlan(SupAffix.SU, setOf("4.1.2", "1.3.9", "8.3.15")),
        SubantaFormPlan(SupAffix.AU, setOf("4.1.2", "6.1.88")),
        SubantaFormPlan(SupAffix.JAS, setOf("4.1.2", "1.3.9", "6.1.102", "8.2.66", "8.3.15")),
        SubantaFormPlan(SupAffix.AM, setOf("4.1.2", "6.1.107")),
        SubantaFormPlan(SupAffix.AUT, setOf("4.1.2", "1.3.3", "1.3.9", "6.1.88")),
        SubantaFormPlan(SupAffix.SAS, setOf("4.1.2", "1.3.8", "1.3.9", "6.1.102", "6.1.103")),
        SubantaFormPlan(SupAffix.TA, setOf("4.1.2", "7.1.12", "6.1.87")),
        SubantaFormPlan(SupAffix.BHYAM_3, setOf("4.1.2", "7.3.102")),
        SubantaFormPlan(SupAffix.BHYAM_4, setOf("4.1.2", "7.3.102")),
        SubantaFormPlan(SupAffix.BHYAM_5, setOf("4.1.2", "7.3.102")),
        SubantaFormPlan(SupAffix.BHIS, setOf("4.1.2", "7.1.9", "6.1.88", "8.2.66", "8.3.15")),
        SubantaFormPlan(SupAffix.NGE, setOf("4.1.2", "7.1.13", "7.3.102")),
        SubantaFormPlan(SupAffix.BHYAS_4, setOf("4.1.2", "7.3.103", "8.2.66", "8.3.15")),
        SubantaFormPlan(SupAffix.BHYAS_5, setOf("4.1.2", "7.3.103", "8.2.66", "8.3.15")),
        SubantaFormPlan(SupAffix.NGASI, setOf("4.1.2", "7.1.12", "6.1.101")),
        SubantaFormPlan(SupAffix.NGAS, setOf("4.1.2", "7.1.12")),
        SubantaFormPlan(SupAffix.OS_6, setOf("4.1.2", "7.3.104", "6.1.78", "8.2.66", "8.3.15")),
        SubantaFormPlan(SupAffix.AM_6, setOf("4.1.2", "7.1.54", "6.1.101")),
        SubantaFormPlan(SupAffix.OS_7, setOf("4.1.2", "7.3.104", "6.1.78", "8.2.66", "8.3.15")),
        SubantaFormPlan(SupAffix.NGI, setOf("4.1.2", "1.3.8", "1.3.9", "6.1.87")),
        SubantaFormPlan(SupAffix.SUP, setOf("4.1.2", "1.3.3", "1.3.9", "7.3.103", "8.3.59")),
    )

    fun find(vibhakti: Vibhakti, vacana: Vacana): SubantaFormPlan? =
        supported.singleOrNull { it.affix.vibhakti == vibhakti && it.affix.vacana == vacana }

    fun all(): List<SubantaFormPlan> = supported
}
