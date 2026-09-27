package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.PadaType
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.2.1: सिचि वृद्धिः परस्मैपदेषु. Applies Vṛddhi vowel grade to root vowels before सिच् in Parasmaipada. */
object SiciVrddhihParasmaipadesuSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.1",
    text = "सिचि वृद्धिः परस्मैपदेषु",
    hindiExplanation = "परस्मैपद सिच् परे होने पर इगन्त धातु के स्वर को वृद्धि होती है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720001,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    private val parasmaipadaEndings = setOf("तिप्", "तस्", "झि", "सिप्", "थस्", "थ", "मिप्", "वस्", "मस्")

    override fun matches(context: DerivationState): Boolean {
        val hasSic = context.terms.any { it.upadesha == "सिँच्" }
        if (!hasSic) return false
        val isParasmaipada = context.effectiveContext.rupa.pada == PadaType.PARASMAIPADA ||
            context.terms.lastOrNull()?.upadesha in parasmaipadaEndings
        if (!isParasmaipada) return false
        val stem = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        if (vrddhiTargets.values.any { stem.varnas == it }) return false
        return vrddhiKey(stem) != null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms.first { it.kind == TermKind.DHATU }
        val source = requireNotNull(vrddhiKey(stem))
        val result = vrddhiTargets.getValue(source)
        return DerivationChange(
            context.substituteTermVarnas(stem.id, result, source, result, sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            "7.2.1 applies Vṛddhi to root vowel before सिच् in Parasmaipada.",
        )
    }

    private fun vrddhiKey(stem: dev.panini.derivation.DerivationTerm): Vyanjana? =
        sourcePatterns.entries.firstOrNull { (_, patterns) ->
            patterns.any { pattern -> stem.varnas.take(pattern.size) == pattern } ||
                patterns.any { pattern -> stem.upadeshaVarnas.take(pattern.size) == pattern }
        }?.key

    private val sourcePatterns: Map<Vyanjana, List<List<Varna>>> = mapOf(
        Vyanjana.HA to listOf(listOf(Vyanjana.HA, Svara.R), listOf(Vyanjana.HA, Svara.A, Vyanjana.RA)),
        Vyanjana.NA to listOf(listOf(Vyanjana.NA, Svara.II), listOf(Vyanjana.NA, Svara.E)),
        Vyanjana.KA to listOf(listOf(Vyanjana.KA, Svara.R), listOf(Vyanjana.KA, Svara.A, Vyanjana.RA)),
        Vyanjana.JA to listOf(listOf(Vyanjana.JA, Svara.I), listOf(Vyanjana.JA, Svara.E)),
    )
    private val vrddhiTargets: Map<Vyanjana, List<Varna>> = mapOf(
        Vyanjana.HA to listOf(Vyanjana.HA, Svara.AA, Vyanjana.RA),
        Vyanjana.NA to listOf(Vyanjana.NA, Svara.AI),
        Vyanjana.KA to listOf(Vyanjana.KA, Svara.AA, Vyanjana.RA, Svara.A),
        Vyanjana.JA to listOf(Vyanjana.JA, Svara.AI),
    )
}
