package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.4.53: jhalāṃ jaś jhaśi.
 * Substitutes jaś (voiced unaspirated stops) for jhal (stops + fricatives)
 * when followed by jhaś (voiced stops).
 */
object JhalamJashJhashiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.53",
    text = "झलां जश् झशि",
    hindiExplanation = "झल् वर्णों के स्थान पर जश् आदेश होता है यदि बाद में झश् वर्ण हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840053,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    stage = dev.panini.sutra.SutraStage.SANDHI,
), DerivationSutra {
    // Every jhal directly before a jhaś, within a term or across a boundary (दध्ध्यत्र → दद्ध्यत्र).
    private fun target(context: DerivationState): dev.panini.derivation.LocatedVarna? {
        val engine = Ashtadhyayi.pratyaharaEngine
        val sounds = context.terms.flatMap { term -> term.varnas.mapIndexed { index, varna -> dev.panini.derivation.LocatedVarna(term.id, index, varna) } }
        return sounds.zipWithNext().firstOrNull { (left, right) ->
            engine.contains(Pratyahara.JHAL, left.varna) && engine.contains(Pratyahara.JHASH, right.varna) &&
                substituteFor(left.varna) != left.varna
        }?.first
    }

    override fun matches(context: DerivationState): Boolean = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val at = requireNotNull(target(context))
        val substitute = substituteFor(at.varna)
        return DerivationChange(
            state = context.replaceTermVarna(at.termId, at.index, listOf(substitute), sutra),
            explanation = "8.4.53: Substituted voiced ${substitute.devanagari} for ${at.varna.devanagari} before a jhaś."
        )
    }

    private fun substituteFor(source: Varna): Varna = when (source) {
        Vyanjana.CA, Vyanjana.CHA, Vyanjana.JA, Vyanjana.JHA -> Vyanjana.JA
        Vyanjana.TTA, Vyanjana.TTHA, Vyanjana.DDA, Vyanjana.DDHA -> Vyanjana.DDA
        Vyanjana.TA, Vyanjana.THA, Vyanjana.DA, Vyanjana.DHA -> Vyanjana.DA
        Vyanjana.KA, Vyanjana.KHA, Vyanjana.GA, Vyanjana.GHA -> Vyanjana.GA
        Vyanjana.PA, Vyanjana.PHA, Vyanjana.BA, Vyanjana.BHA -> Vyanjana.BA
        Vyanjana.SHA -> Vyanjana.JA
        Vyanjana.SSA -> Vyanjana.DDA
        Vyanjana.SA -> Vyanjana.DA
        Vyanjana.HA -> Vyanjana.GA
        else -> source
    }
}
