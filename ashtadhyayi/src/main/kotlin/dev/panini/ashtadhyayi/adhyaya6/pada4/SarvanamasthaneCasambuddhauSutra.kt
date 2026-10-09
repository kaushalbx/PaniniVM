package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.isHrasva
import dev.panini.shiksha.toDirgha
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.4.8: sarvanāmasthāne cāsambuddhau.
 */
object SarvanamasthaneCasambuddhauSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.8",
    text = "सर्वनामस्थाने चासम्बुद्धौ",
    hindiExplanation = "नकारान्त अङ्ग की उपधा को दीर्घ होता है सर्वनामस्थान परे होने पर (सम्बुद्धि को छोड़कर)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640008,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Stem must end in consonant 'न्', or have received a 'num' augment substitution (7.1.70-73), or carry 'num'/'nuṭ' terms
        val isNStemOrNum = stem.varnas.lastOrNull() == Vyanjana.NA ||
            context.appliedSutras.any { it in setOf("7.1.70", "7.1.71", "7.1.72", "7.1.73") } ||
            context.terms.any { it.upadesha == "नुट्" || it.upadesha == "नुम्" }
        if (!isNStemOrNum) return false

        // 2. Affix must be Sarvanāmasthāna
        val isSarvanamasthana = affix.upadesha == "शि" || affix.id in setOf("sup-su", "sup-au", "sup-jas", "sup-am", "sup-aut")

        // 3. The upadhā may be an explicit vowel or a consonant carrying an
        // inherent a (फलन्/फलन).
        val upadha = stem.varnas.filterIsInstance<Svara>().lastOrNull() ?: return false
        return isSarvanamasthana && upadha.isHrasva
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val index = stem.varnas.indexOfLast { it is Svara }
        val source = stem.varnas[index] as Svara
        val replacement = source.toDirgha()

        return DerivationChange(
            state = context.replaceTermVarna(stem.id, index, listOf(replacement), sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.4.8: Lengthened the penultimate vowel of the 'n'-ending stem before Sarvanāmasthāna."
        )
    }
}
