package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.*
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/** 6.4.105: the hi ending is elided after an a-final aṅga. */
object AtoHehSutra : Sutra<DerivationState, DerivationChange>(
    "6.4.105", "अतो हेः", "अकारान्त अङ्ग के परे हि का लुक् होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 4, optional = false, kramaValue = 640105,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LOT &&
            context.terms.lastOrNull()?.varnas == hiVarnas &&
            context.terms.dropLast(1).lastOrNull { it.varnas.isNotEmpty() }?.varnas?.lastOrNull() == Svara.A

    override fun apply(context: DerivationState) = DerivationChange(
        context.replaceWholeAffix(context.terms.last().id, "", sutra, WholeAffixDesignationPolicy.Consume),
        "6.4.105 elides hi after an a-final aṅga.",
    )
}

/** 6.4.106: hi is elided after suffix-final u not preceded by a consonant cluster. */
object UtashCaPratyayadAsamyogapurvatSutra : Sutra<DerivationState, DerivationChange>(
    "6.4.106", "उतश्च प्रत्ययादसंयोगपूर्वात्", "असंयोगपूर्व उकारान्त प्रत्यय के परे हि का लुक् होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 4, optional = false, kramaValue = 640106,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LOT || context.terms.lastOrNull()?.varnas != hiVarnas) return false
        val preceding = context.terms.dropLast(1).filter { it.varnas.isNotEmpty() }
        val suffix = preceding.lastOrNull() ?: return false
        if (suffix.kind != TermKind.PRATYAYA || suffix.varnas.lastOrNull() != Svara.U) return false
        val varnas = preceding.flatMap { it.varnas }.dropLast(1)
        return !(varnas.lastOrNull() is Vyanjana && varnas.getOrNull(varnas.lastIndex - 1) is Vyanjana)
    }
    override fun apply(context: DerivationState) = DerivationChange(
        context.replaceWholeAffix(context.terms.last().id, "", sutra, WholeAffixDesignationPolicy.Consume),
        "6.4.106 elides hi after non-cluster-preceded suffix-final u.",
    )
}

/** 6.4.110: kṛ + u takes u for its a before any k/ṅ-it sārvadhātuka: kurutaḥ, kuru, but karoti. */
object AtaUtSarvadhatukeSutra : Sutra<DerivationState, DerivationChange>(
    "6.4.110", "अत उत् सार्वधातुके", "करोतेः उकारान्त अङ्ग के अकार को क्ङित् सार्वधातुक परे उकार होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 4, optional = false, kramaValue = 640110,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val root = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return root.varnas == listOf(Vyanjana.KA, Svara.A, Vyanjana.RA) && context.terms.any { it.kind == TermKind.PRATYAYA && it.upadesha == "उ" } &&
            "6.4.110" !in context.appliedSutras &&
            (("3.4.87" in context.appliedSutras && context.effectiveContext.rupa.lakara == Lakara.LOT) ||
                isKngitSarvadhatuka(context, context.terms.first { it.kind == TermKind.PRATYAYA && it.upadesha == "उ" }))
    }
    override fun apply(context: DerivationState): DerivationChange {
        val root = context.terms.first { it.kind == TermKind.DHATU }
        return DerivationChange(context.replaceTermVarna(root.id, 1, listOf(Svara.U), sutra),
            "6.4.110 substitutes u in the kṛ aṅga before a k/ṅ-it sārvadhātuka ending.")
    }
}

/** 6.4.108: नित्यं करोतेः. The u of kṛ + u is always lost before m or v: kurvaḥ, kurmaḥ. */
object NityamKarotehSutra : Sutra<DerivationState, DerivationChange>(
    "6.4.108", "नित्यं करोतेः", "करोति के उकार का मकार और वकार आदि प्रत्यय परे रहते नित्य लोप होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 4, optional = false, kramaValue = 640108,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    private fun u(context: DerivationState) = context.terms.firstOrNull { it.kind == TermKind.PRATYAYA && it.upadesha == "उ" && it.varnas == listOf(Svara.U) }
    override fun matches(context: DerivationState): Boolean {
        val root = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        val suffix = u(context) ?: return false
        val next = context.terms.drop(context.terms.indexOf(suffix) + 1).firstOrNull { it.varnas.isNotEmpty() } ?: return false
        return (root.varnas == listOf(Vyanjana.KA, Svara.U, Vyanjana.RA) || root.varnas == listOf(Vyanjana.KA, Svara.A, Vyanjana.RA)) &&
            next.varnas.first() in setOf(Vyanjana.MA, Vyanjana.VA) && isKngitSarvadhatuka(context, suffix)
    }
    override fun apply(context: DerivationState): DerivationChange {
        val suffix = requireNotNull(u(context))
        return DerivationChange(context.deleteTermVarnas(suffix.id, 0, 1, sutra), "6.4.108 drops the u of kṛ + u before m or v.")
    }
}

private val hiVarnas = listOf(Vyanjana.HA, Svara.I)
