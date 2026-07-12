package dev.sanskrit.ashtadhyayi

import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.Adhyaya1Pada1
import dev.sanskrit.ashtadhyayi.adhyaya1.pada2.Adhyaya1Pada2
import dev.sanskrit.ashtadhyayi.adhyaya1.pada3.Adhyaya1Pada3
import dev.sanskrit.ashtadhyayi.adhyaya1.pada4.Adhyaya1Pada4
import dev.sanskrit.ashtadhyayi.adhyaya3.pada1.Adhyaya3Pada1
import dev.sanskrit.ashtadhyayi.adhyaya3.pada2.Adhyaya3Pada2
import dev.sanskrit.ashtadhyayi.adhyaya3.pada4.Adhyaya3Pada4
import dev.sanskrit.ashtadhyayi.adhyaya4.pada1.Adhyaya4Pada1
import dev.sanskrit.ashtadhyayi.adhyaya6.pada1.Adhyaya6Pada1
import dev.sanskrit.ashtadhyayi.adhyaya6.pada4.Adhyaya6Pada4
import dev.sanskrit.ashtadhyayi.adhyaya7.pada1.Adhyaya7Pada1
import dev.sanskrit.ashtadhyayi.adhyaya7.pada2.Adhyaya7Pada2
import dev.sanskrit.ashtadhyayi.adhyaya7.pada3.Adhyaya7Pada3
import dev.sanskrit.ashtadhyayi.adhyaya8.pada2.Adhyaya8Pada2
import dev.sanskrit.ashtadhyayi.adhyaya8.pada3.Adhyaya8Pada3
import dev.sanskrit.ashtadhyayi.adhyaya8.pada4.Adhyaya8Pada4
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.pratyahara.PratyaharaEngine
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAvastha
import dev.sanskrit.sutra.SutraCatalogIssue
import dev.sanskrit.sutra.SutraCatalogValidator
import dev.sanskrit.sutra.SutraPatha
import dev.sanskrit.sutra.SutraRegistry

object Ashtadhyayi {
    const val expectedSutraCount: Int = 3959
    val pratyaharaEngine = PratyaharaEngine()

    val cataloguedSutras: List<Sutra<*, *>> = listOf(
        Adhyaya1Pada1.sutras, Adhyaya1Pada2.sutras, Adhyaya1Pada3.sutras, Adhyaya1Pada4.sutras,
        Adhyaya3Pada1.sutras, Adhyaya3Pada2.sutras, Adhyaya3Pada4.sutras, Adhyaya4Pada1.sutras,
        Adhyaya6Pada1.sutras, Adhyaya6Pada4.sutras, Adhyaya7Pada1.sutras, Adhyaya7Pada2.sutras,
        Adhyaya7Pada3.sutras, Adhyaya8Pada2.sutras, Adhyaya8Pada3.sutras, Adhyaya8Pada4.sutras,
    ).flatten().distinctBy { it.sutra }

    val registry = SutraRegistry(cataloguedSutras)
    val executableSutras: List<DerivationSutra> = registry.sutras.filterIsInstance<DerivationSutra>()
        .filter { it.avastha == SutraAvastha.KRIYAVAT }
    /** Compatibility view; derived from the registry and never maintained separately. */
    val kriyavatSutras: List<Sutra<*, *>> = executableSutras.map { it as Sutra<*, *> }
    val catalogIssues: List<SutraCatalogIssue> = SutraCatalogValidator.validate(registry)
    val patha = SutraPatha(registry.sutras)
    val pathitaCount: Int get() = patha.pathitaCount
    val kriyavatCount: Int get() = patha.kriyavatCount
    val remainingCount: Int get() = expectedSutraCount - pathitaCount
}
