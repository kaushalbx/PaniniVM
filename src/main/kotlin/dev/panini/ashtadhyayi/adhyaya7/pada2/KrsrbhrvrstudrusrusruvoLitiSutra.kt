package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.Lakara
import dev.panini.derivation.*
import dev.panini.sutra.*

/** 7.2.13: कृसृभृवृस्तुद्रुस्रुश्रुवो लिटि. Restricts the LIṬ iṭ prohibition to the eight krādi roots. */
object KrsrbhrvrstudrusrusruvoLitiSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.13",
    text = "कृसृभृवृस्तुद्रुस्रुश्रुवो लिटि",
    hindiExplanation = "लिट् में इट् का निषेध केवल कृ, सृ, भृ, वृ, स्तु, द्रु, स्रु और श्रु धातुओं के लिए नियत है।",
    type = SutraType.NISHEDHA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720013,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LIT) return false
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU && it.id != "abhyasa" } ?: return false
        val affix = context.terms.lastOrNull()?.takeIf { it.kind == TermKind.PRATYAYA } ?: return false
        return dhatu.surface in KRADI_ROOTS && sutra !in affix.establishedBySutras
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            context.replaceTerm(affix.id, affix.copy(establishedBySutras = affix.establishedBySutras + sutra)),
            "7.2.13 confines the LIṬ iṭ prohibition to the eight krādi roots.",
        )
    }

    internal val KRADI_ROOTS = setOf("कृ", "सृ", "भृ", "वृ", "स्तु", "द्रु", "स्रु", "श्रु")
}
