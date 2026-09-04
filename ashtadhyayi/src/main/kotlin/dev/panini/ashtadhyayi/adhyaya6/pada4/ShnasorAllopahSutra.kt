package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.core.ItMarker
import dev.panini.core.Lakara
import dev.panini.core.Purusha
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.VarnaSubstitution
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 6.4.111: श्नसोरल्लोपः. The अ of श्नम् (and अस्) is lost before k/ṅ-it sārvadhātuka endings. */
object ShnasorAllopahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.111",
    text = "श्नसोरल्लोपः",
    hindiExplanation = "कित् अथवा ङित् सार्वधातुक प्रत्यय परे होने पर श्नम् के नकारोत्तर अकार का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640111,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("6.4.1", "1.1.47", "3.4.113"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val stem = context.terms.firstOrNull {
            it.kind == TermKind.DHATU && "1.1.47" in it.establishedBySutras
        } ?: return false
        val ending = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        val rupa = context.effectiveContext.rupa
        if (rupa.lakara == Lakara.LOT && rupa.purusha == Purusha.UTTAMA &&
            context.allEffectiveTerms.none { "3.4.92" in it.establishedBySutras }
        ) return false
        if (rupa.lakara == Lakara.LOT && ending.matchesUpadesha("सिप्") &&
            context.substitutions.none { it.sutra == "3.4.87" }
        ) return false
        val isSarvadhatuka = context.samjnas.any { it.targetId == ending.id && it.samjna == Samjna.SARVADHATUKA }
        // 1.2.4 makes an apit sārvadhātuka affix ṅ-it. Reading the surviving
        // p-it provenance is sufficient here; no surface or deletion is inferred.
        val separateKngit = context.terms.any {
            it.id != ending.id && (it.hasEffectiveMarker(ItMarker.KIT) || it.hasEffectiveMarker(ItMarker.NGIT))
        }
        // 1.2.4 supplies ṅ-it status to an apit sārvadhātuka ending. A later
        // explicit p-it assignment (3.4.92) blocks that atideśa.
        val isKngit = separateKngit || !ending.hasEffectiveMarker(ItMarker.P)
        return isSarvadhatuka && isKngit && inherentAAfterInfixN(stem.surface) != null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms.first { it.kind == TermKind.DHATU && "1.1.47" in it.establishedBySutras }
        val nIndex = requireNotNull(inherentAAfterInfixN(stem.surface))
        val replacement = stem.surface.substring(0, nIndex + 1) + "्" + stem.surface.substring(nIndex + 1)
        return DerivationChange(
            context.replaceTerm(stem.id, stem.copy(surface = replacement))
                .addSubstitution(VarnaSubstitution(stem.id, 'अ', "", sutra)),
            "6.4.111 deletes the inherent अ after the surviving न of श्नम् before a k/ṅ-it sārvadhātuka ending.",
        )
    }

    private fun inherentAAfterInfixN(surface: String): Int? =
        surface.indices.firstOrNull { index ->
            surface[index] == 'न' && surface.getOrNull(index + 1)?.let(Varnamala::isConsonant) == true
        }
}
