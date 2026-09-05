package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.core.ItMarker
import dev.panini.core.Lakara
import dev.panini.core.Purusha
import dev.panini.core.PadaType
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
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/** 6.4.112: श्नाभ्यस्तयोरातः. */
object ShnabhyastayorAtahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.112", text = "श्नाभ्यस्तयोरातः",
    hindiExplanation = "कित् या ङित् सार्वधातुक के परे श्ना का आकार लुप्त होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 4, optional = false, kramaValue = 640112,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.PRATYAYA,
    dependencies = setOf("6.4.1", "3.4.113"), stage = SutraStage.ANGAKARYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val shna = shna(context) ?: return false
        val lingAtmanepada = context.effectiveContext.rupa.lakara == Lakara.LING &&
            context.effectiveContext.rupa.pada == PadaType.ATMANEPADA
        return isKngitSarvadhatuka(context, shna) &&
            (lingAtmanepada || nextInitial(context, shna)?.let(Varnamala::isVowel) == true)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val shna = requireNotNull(shna(context))
        return DerivationChange(
            context.replaceTerm(shna.id, shna.copy(surface = shna.surface.dropLast(1) + "्"))
                .addSubstitution(VarnaSubstitution(shna.id, 'आ', "", sutra)),
            "6.4.112 elides the ā of श्ना before a vowel-initial k/ṅ-it sārvadhātuka.",
        )
    }
}

internal fun shna(context: DerivationState) = context.terms.firstOrNull {
    it.kind == TermKind.PRATYAYA && it.matchesUpadesha("श्ना") && it.surface.endsWith('ा')
}

internal fun nextInitial(context: DerivationState, shna: dev.panini.derivation.DerivationTerm): Char? {
    val following = context.terms.drop(context.terms.indexOf(shna) + 1)
    val first = following.firstOrNull { it.surface.isNotEmpty() } ?: return null
    // 7.1.3/7.1.5 later give झि and झ their vowel-initial substitutes.  The
    // āṅga operation is conditioned by that grammatical substitute, not by
    // the temporary raw spelling of the tiṅ termination.
    return when (first.upadesha) {
        "झि", "झ" -> 'अ'
        else -> first.surface.first()
    }
}

internal fun isKngitSarvadhatuka(context: DerivationState, shna: dev.panini.derivation.DerivationTerm): Boolean {
    val following = context.terms.drop(context.terms.indexOf(shna) + 1)
    val ending = following.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
    if (context.effectiveContext.rupa.lakara == Lakara.LOT &&
        context.effectiveContext.rupa.purusha == Purusha.UTTAMA &&
        context.allEffectiveTerms.none { "3.4.92" in it.establishedBySutras }
    ) return false
    val sarvadhatuka = context.samjnas.any { it.targetId == ending.id && it.samjna == Samjna.SARVADHATUKA }
    val explicitKngit = following.any { it.hasEffectiveMarker(ItMarker.KIT) || it.hasEffectiveMarker(ItMarker.NGIT) }
    return sarvadhatuka && (explicitKngit || !ending.hasEffectiveMarker(ItMarker.P))
}
