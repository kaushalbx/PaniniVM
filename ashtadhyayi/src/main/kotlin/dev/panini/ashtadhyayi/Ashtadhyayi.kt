package dev.panini.ashtadhyayi

import dev.panini.ashtadhyayi.adhyaya1.pada1.Adhyaya1Pada1
import dev.panini.ashtadhyayi.adhyaya1.pada2.Adhyaya1Pada2
import dev.panini.ashtadhyayi.adhyaya1.pada3.Adhyaya1Pada3
import dev.panini.ashtadhyayi.adhyaya1.pada4.Adhyaya1Pada4
import dev.panini.ashtadhyayi.adhyaya2.pada1.Adhyaya2Pada1
import dev.panini.ashtadhyayi.adhyaya2.pada2.Adhyaya2Pada2
import dev.panini.ashtadhyayi.adhyaya2.pada3.Adhyaya2Pada3
import dev.panini.ashtadhyayi.adhyaya2.pada4.Adhyaya2Pada4
import dev.panini.ashtadhyayi.adhyaya3.pada1.Adhyaya3Pada1
import dev.panini.ashtadhyayi.adhyaya3.pada2.Adhyaya3Pada2
import dev.panini.ashtadhyayi.adhyaya3.pada3.Adhyaya3Pada3
import dev.panini.ashtadhyayi.adhyaya3.pada4.Adhyaya3Pada4
import dev.panini.ashtadhyayi.adhyaya4.pada1.Adhyaya4Pada1
import dev.panini.ashtadhyayi.adhyaya4.pada2.Adhyaya4Pada2
import dev.panini.ashtadhyayi.adhyaya4.pada3.Adhyaya4Pada3
import dev.panini.ashtadhyayi.adhyaya4.pada4.Adhyaya4Pada4
import dev.panini.ashtadhyayi.adhyaya5.pada2.Adhyaya5Pada2
import dev.panini.ashtadhyayi.adhyaya6.pada1.Adhyaya6Pada1
import dev.panini.ashtadhyayi.adhyaya6.pada3.Adhyaya6Pada3
import dev.panini.ashtadhyayi.adhyaya6.pada4.Adhyaya6Pada4
import dev.panini.ashtadhyayi.adhyaya7.pada1.Adhyaya7Pada1
import dev.panini.ashtadhyayi.adhyaya7.pada2.Adhyaya7Pada2
import dev.panini.ashtadhyayi.adhyaya7.pada3.Adhyaya7Pada3
import dev.panini.ashtadhyayi.adhyaya7.pada4.Adhyaya7Pada4
import dev.panini.ashtadhyayi.adhyaya8.pada2.Adhyaya8Pada2
import dev.panini.ashtadhyayi.adhyaya8.pada3.Adhyaya8Pada3
import dev.panini.ashtadhyayi.adhyaya8.pada4.Adhyaya8Pada4
import dev.panini.derivation.DerivationSutra
import dev.panini.ganapatha.Gana
import dev.panini.pratyahara.PratyaharaEngine
import dev.panini.sutra.*

object Ashtadhyayi {
    const val expectedSutraCount: Int = 3959
    val pratyaharaEngine = PratyaharaEngine()

    private val implementedSutras: List<Sutra<*, *>> = listOf(
        Adhyaya1Pada1.sutras, Adhyaya1Pada2.sutras, Adhyaya1Pada3.sutras, Adhyaya1Pada4.sutras,
        Adhyaya2Pada1.sutras, Adhyaya2Pada2.sutras, Adhyaya2Pada3.sutras, Adhyaya2Pada4.sutras,
        Adhyaya3Pada1.sutras, Adhyaya3Pada2.sutras, Adhyaya3Pada3.sutras, Adhyaya3Pada4.sutras, Adhyaya4Pada1.sutras,
        Adhyaya4Pada2.sutras,
        Adhyaya4Pada3.sutras,
        Adhyaya4Pada4.sutras,
        Adhyaya5Pada2.sutras,
        Adhyaya6Pada1.sutras, Adhyaya6Pada3.sutras, Adhyaya6Pada4.sutras, Adhyaya7Pada1.sutras, Adhyaya7Pada2.sutras,
        Adhyaya7Pada3.sutras, Adhyaya7Pada4.sutras, Adhyaya8Pada2.sutras, Adhyaya8Pada3.sutras, Adhyaya8Pada4.sutras,
        listOf(dev.panini.ashtadhyayi.adhyaya8.pada1.PadasyaAdhikaraSutra),
    ).flatten()
    val cataloguedSutras: List<Sutra<*, *>> = implementedSutras

    val registry = SutraRegistry(cataloguedSutras)
    val executableSutras: List<DerivationSutra> = registry.sutras.filterIsInstance<DerivationSutra>()
    private val executableSutrasByStage: Map<SutraStage, List<DerivationSutra>> =
        executableSutras.groupBy(DerivationSutra::stage)

    /** Canonical executable views, derived from grammatical metadata. */
    val sandhiSutras: List<DerivationSutra> = executableSutrasFor(SutraStage.sandhiPhases)
    val sankhyaSutras: List<DerivationSutra> = executableSutrasFor(
        listOf(SutraStage.ANGAKARYA, SutraStage.PADA_FORMATION) + SutraStage.sandhiPhases,
    )
    val puranaSankhyaSutras: List<DerivationSutra> = executableSutrasFor(
        SutraStage.PRATYAYA_SELECTION,
        SutraStage.ANGAKARYA,
        SutraStage.PADA_FORMATION,
        SutraStage.THUK_PHONOLOGY,
    )
    val krdantaSutras: List<DerivationSutra> = executableSutrasFor(
        SutraStage.PRATYAYA_SELECTION,
        SutraStage.ANGAKARYA,
        SutraStage.IT_PROCESSING,
    )
    val striPratyayaSutras: List<DerivationSutra> =
        executableSutrasUnder("4.1.3")
    val samasaSutras: List<DerivationSutra> =
        executableSutras.filter { (it as? Sutra<*, *>)?.let { s -> s.chapter == 2 && s.pada in 1..2 } == true }

    /**
     * Kotlin rules that can execute in the shared runtime through matches/apply.
     */
    val runtimeSutras: List<Sutra<*, *>> = registry.sutras.filter {
        it is DerivationSutra
    }

    /** Compatibility view; derived from the registry and never maintained separately. */
    val kriyavatSutras: List<Sutra<*, *>> = executableSutras.map { it as Sutra<*, *> }
    val catalogIssues: List<SutraCatalogIssue> =
        SutraCatalogValidator.validate(registry) + AshtadhyayiSutraValidator.validate(registry.sutras)
    val patha = SutraPatha(registry.sutras)
    val pathitaCount: Int get() = patha.pathitaCount
    val kriyavatCount: Int get() = patha.kriyavatCount
    val remainingCount: Int get() = expectedSutraCount - pathitaCount

    val adhikaraSutras: List<Sutra<*, *>> by lazy {
        registry.sutras.filter { it.role is SutraRole.Adhikara }
    }
    val paribhasaSutras: List<Sutra<*, *>> by lazy {
        registry.sutras.filter { it.role is SutraRole.Paribhasha }
    }

    /** Resolves an implemented sūtra associated with a Gaṇapāṭha entry. */
    fun sutraFor(gana: Gana): Sutra<*, *> = registry.require(gana.sutra)

    fun getSutrasByRole(role: SutraRole): List<Sutra<*, *>> = registry.sutras.filter { it.role == role }

    fun executableSutrasUnder(adhikaraNumber: String): List<DerivationSutra> =
        registry.governedBy(adhikaraNumber).filterIsInstance<DerivationSutra>()

    fun executableSutrasAt(stage: SutraStage): List<DerivationSutra> =
        executableSutrasByStage[stage].orEmpty()

    fun executableSutrasFor(vararg stages: SutraStage): List<DerivationSutra> =
        executableSutrasFor(stages.asList())

    fun executableSutrasFor(stages: Iterable<SutraStage>): List<DerivationSutra> {
        val included = stages.toSet()
        return executableSutras.filter { it.stage in included }
    }

    fun sandhiSutrasAt(stage: SutraStage): List<DerivationSutra> = sandhiSutras.at(stage)
    fun sankhyaSutrasAt(stage: SutraStage): List<DerivationSutra> = sankhyaSutras.at(stage)
    fun puranaSankhyaSutrasAt(stage: SutraStage): List<DerivationSutra> = puranaSankhyaSutras.at(stage)
    fun krdantaSutrasAt(stage: SutraStage): List<DerivationSutra> = krdantaSutras.at(stage)
    fun striPratyayaSutrasAt(stage: SutraStage): List<DerivationSutra> = striPratyayaSutras.at(stage)

    fun requireExecutable(number: String): DerivationSutra =
        registry.require(number) as? DerivationSutra
            ?: error("Sūtra $number is not executable.")

    private fun List<DerivationSutra>.at(stage: SutraStage): List<DerivationSutra> =
        filter { it.stage == stage }
}
