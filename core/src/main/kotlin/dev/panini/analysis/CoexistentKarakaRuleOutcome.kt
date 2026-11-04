package dev.panini.analysis

import dev.panini.core.Karaka

/** One kāraka power bears simultaneous names; expression through one expresses the others. */
interface CoexistentKarakaRuleOutcome {
    val coexistingKarakas: Set<Karaka>
}
