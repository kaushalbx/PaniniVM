package dev.panini.ashtadhyayi.adhyaya5.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.SarvadiGana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 5.3.2 restricts the prāgdiśīya affixes to kim, eligible pronouns before
 * dvi in the Sarvādi Gaṇapāṭha, and numerical bahu.
 */
object KimSarvanamaBahubhyoAdvyadibhyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.3.2", text = "किंसर्वनामबहुभ्योऽद्व्यादिभ्यः",
    hindiExplanation = "किम्, द्व्यादि से पूर्व के सर्वनाम और संख्यावाची बहु से प्राग्दिशीय प्रत्यय होते हैं।",
    type = SutraType.ADHIKARA, chapter = 5, pada = 3, optional = false, kramaValue = 530002,
    role = SutraRole.Adhikara(530026), action = SutraAction.ADHIKARA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("5.3.1" !in context.activeAdhikaras || number in context.activeAdhikaras) return false
        val stem = context.allEffectiveTerms.singleOrNull { it.kind == TermKind.PRATIPADIKA } ?: return false
        if (stem.upadesha == "किम्" || stem.upadesha == "बहु") return true
        val member = SarvadiGana.eligibleMember(stem.upadesha, stem.lexicalUses) ?: return false
        return member.text !in dvyadiMembers
    }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.copy(activeAdhikaras = context.activeAdhikaras + number),
        "5.3.2 establishes an eligible kim/sarvanāma/bahu base before the dv-yādi exclusion.",
    )

    /** द्व्यादि is the tail of the transmitted Sarvādi order beginning with द्वि. */
    private val dvyadiMembers: Set<String> = SarvadiGana.memberTexts
        .dropWhile { it != "द्वि" }
        .toSet()
}
