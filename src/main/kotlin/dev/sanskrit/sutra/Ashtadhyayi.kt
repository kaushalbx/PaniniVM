package dev.sanskrit.sutra

import dev.sanskrit.patha.adhyaya1.pada1.Adhyaya1Pada1
import dev.sanskrit.sandhi.SandhiEngine

object Ashtadhyayi {
    const val expectedSutraCount: Int = 3959

    val kriyavatSutras: List<Sutra<*, *>> = SandhiEngine.defaultSutras
    val pathitaMetadata: List<SutraMetadata> =
        Adhyaya1Pada1.sutras + kriyavatSutras.map { it.metadata }

    val patha = SutraPatha(
        pathitaMetadata,
    )

    val pathitaCount: Int
        get() = patha.pathitaCount

    val kriyavatCount: Int
        get() = patha.kriyavatCount

    val remainingCount: Int
        get() = expectedSutraCount - pathitaCount
}
