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
        SubantaFormPlan(SupAffix.SU, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.AU, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.JAS, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.AM, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.AUT, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.SAS, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.TA, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.BHYAM_3, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.BHYAM_4, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.BHYAM_5, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.BHIS, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.NGE, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.BHYAS_4, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.BHYAS_5, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.NGASI, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.NGAS, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.OS_6, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.AM_6, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.OS_7, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.NGI, setOf("4.1.2")),
        SubantaFormPlan(SupAffix.SUP, setOf("4.1.2")),
    )

    fun find(vibhakti: Vibhakti, vacana: Vacana): SubantaFormPlan? =
        supported.singleOrNull { it.affix.vibhakti == vibhakti && it.affix.vacana == vacana }

    fun all(): List<SubantaFormPlan> = supported
}
