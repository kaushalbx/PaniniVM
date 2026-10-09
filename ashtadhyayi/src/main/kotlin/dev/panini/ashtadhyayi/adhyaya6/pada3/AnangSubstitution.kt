package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari

/** The final ṅ is it; the n in ānaṅ survives until pada-final n-lopa. */
internal fun anangFirstMember(context: SamasaRuleContext, sutra: String): SamasaRuleResult.Formed {
    val replacement = context.purvaPada.varnas.dropLast(1) + listOf(Svara.AA, Vyanjana.NA)
    return SamasaRuleResult.Formed(
        (replacement + context.padas.drop(1).flatMap { it.varnas }).toDevanagari(),
        "$sutra substitutes ān for the final Varṇa; pada-final n-lopa remains a separate operation.",
        memberEdits = mapOf(0 to replacement.toDevanagari()),
    )
}
