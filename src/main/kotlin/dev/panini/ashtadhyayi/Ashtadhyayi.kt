package dev.panini.ashtadhyayi

import dev.panini.ashtadhyayi.adhyaya1.pada1.Adhyaya1Pada1
import dev.panini.ashtadhyayi.adhyaya1.pada2.Adhyaya1Pada2
import dev.panini.ashtadhyayi.adhyaya1.pada3.Adhyaya1Pada3
import dev.panini.ashtadhyayi.adhyaya1.pada4.Adhyaya1Pada4
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
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraCatalogIssue
import dev.panini.sutra.SutraCatalogValidator
import dev.panini.sutra.SutraPatha
import dev.panini.sutra.SutraRegistry

object Ashtadhyayi {
    const val expectedSutraCount: Int = 3959
    val pratyaharaEngine = PratyaharaEngine()

    private val implementedSutras: List<Sutra<*, *>> = listOf(
        Adhyaya1Pada1.sutras, Adhyaya1Pada2.sutras, Adhyaya1Pada3.sutras, Adhyaya1Pada4.sutras,
        Adhyaya2Pada3.sutras, Adhyaya2Pada4.sutras,
        Adhyaya3Pada1.sutras, Adhyaya3Pada2.sutras, Adhyaya3Pada3.sutras, Adhyaya3Pada4.sutras, Adhyaya4Pada1.sutras,
        Adhyaya4Pada2.sutras,
        Adhyaya4Pada3.sutras,
        Adhyaya4Pada4.sutras,
        Adhyaya5Pada2.sutras,
        Adhyaya6Pada1.sutras, Adhyaya6Pada3.sutras, Adhyaya6Pada4.sutras, Adhyaya7Pada1.sutras, Adhyaya7Pada2.sutras,
        Adhyaya7Pada3.sutras, Adhyaya7Pada4.sutras, Adhyaya8Pada2.sutras, Adhyaya8Pada3.sutras, Adhyaya8Pada4.sutras,
    ).flatten()
    val cataloguedSutras: List<Sutra<*, *>> = implementedSutras

    val registry = SutraRegistry(cataloguedSutras)
    val executableSutras: List<DerivationSutra> = registry.sutras.filterIsInstance<DerivationSutra>()
    /** Compatibility view; derived from the registry and never maintained separately. */
    val kriyavatSutras: List<Sutra<*, *>> = executableSutras.map { it as Sutra<*, *> }
    val catalogIssues: List<SutraCatalogIssue> = SutraCatalogValidator.validate(registry)
    val patha = SutraPatha(registry.sutras)
    val pathitaCount: Int get() = patha.pathitaCount
    val kriyavatCount: Int get() = patha.kriyavatCount
    val remainingCount: Int get() = expectedSutraCount - pathitaCount

    /** Resolves an implemented sūtra associated with a Gaṇapāṭha entry. */
    fun sutraFor(gana: Gana): Sutra<*, *> = registry.require(gana.sutra)
}
