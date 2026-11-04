package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti

/** A case option can select a different relational/kāraka interpretation. */
interface CaseDependentKarakaOutcome {
    fun karakaForCase(vibhakti: Vibhakti): Karaka
}
