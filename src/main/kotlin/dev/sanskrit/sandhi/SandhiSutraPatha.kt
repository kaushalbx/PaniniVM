package dev.sanskrit.sandhi

import dev.sanskrit.patha.adhyaya6.pada1.Adhyaya6Pada1
import dev.sanskrit.patha.adhyaya8.pada3.Adhyaya8Pada3

object SandhiSutraPatha {
    val sutras: List<SandhiSutra> =
        Adhyaya6Pada1.sutras + Adhyaya8Pada3.sutras
}
