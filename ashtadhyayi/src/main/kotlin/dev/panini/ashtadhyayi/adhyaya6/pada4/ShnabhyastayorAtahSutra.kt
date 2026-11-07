package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.core.ItMarker
import dev.panini.core.Lakara
import dev.panini.core.Purusha
import dev.panini.core.PadaType
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
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
        reduplicated(context)?.let { root ->
            if (!isKngitSarvadhatuka(context, root)) return false
            // देहि, धेहि: 6.4.119 gives ए before हि, not this lopa.
            if (context.effectiveContext.rupa.lakara == Lakara.LOT && "3.4.87" in context.appliedSutras) return false
            // 6.4.113 gives other roots ī before a consonant; a ghu root (दा, धा) is excepted, so it loses ā there too.
            return nextInitial(context, root) is Svara || root.upadesha in setOf("डुदाञ्", "डुधाञ्", "दा", "धा")
        }
        val shna = shna(context) ?: return false
        val lingAtmanepada = context.effectiveContext.rupa.lakara == Lakara.LING &&
            context.effectiveContext.rupa.pada == PadaType.ATMANEPADA
        return isKngitSarvadhatuka(context, shna) &&
            (lingAtmanepada || nextInitial(context, shna) is Svara)
    }

    override fun apply(context: DerivationState): DerivationChange {
        reduplicated(context)?.let { root ->
            return DerivationChange(context.deleteTermVarnas(root.id, root.varnas.lastIndex, 1, sutra),
                "6.4.112 elides the ā of a reduplicated base before a k/ṅ-it sārvadhātuka.")
        }
        val shna = requireNotNull(shna(context))
        return DerivationChange(
            context.deleteTermVarnas(shna.id, shna.varnas.lastIndex, 1, sutra),
            "6.4.112 elides the ā of श्ना before a vowel-initial k/ṅ-it sārvadhātuka.",
        )
    }
}

/** A root ending in ā after its abhyāsa: ददा-, दधा-. */
private fun reduplicated(context: DerivationState) = context.terms.firstOrNull {
    it.kind == TermKind.DHATU && it.varnas.lastOrNull() == Svara.AA
}?.takeIf { context.samjnas.any { samjna -> samjna.targetId == "abhyasa" && samjna.samjna == Samjna.ABHYASA } }

internal fun shna(context: DerivationState) = context.terms.firstOrNull {
    it.kind == TermKind.PRATYAYA && it.upadesha == "श्ना" && it.varnas.lastOrNull() == Svara.AA
}

internal fun nextInitial(context: DerivationState, shna: dev.panini.derivation.DerivationTerm): dev.panini.shiksha.Varna? {
    val following = context.terms.drop(context.terms.indexOf(shna) + 1)
    val first = following.firstOrNull { it.varnas.isNotEmpty() } ?: return null
    // 7.1.3/7.1.5 later give झि and झ their vowel-initial substitutes.  The
    // āṅga operation is conditioned by that grammatical substitute, not by
    // the temporary raw spelling of the tiṅ termination.
    return when (first.upadesha) {
        "झि", "झ" -> Svara.A
        else -> first.varnas.first()
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
