package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.sutra.*

/** 5.2.51: षट्कतिकतिपयचतुरां थुक्। */
object ShatKatiKatipayaChaturamThukSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.2.51", text = "षट्कतिकतिपयचतुरां थुक्",
    hindiExplanation = "पूरणार्थे षष् और चतुर् के बाद थुक् आगम होता है।",
    type = SutraType.APAVADA, chapter = 5, pada = 2, optional = false, kramaValue = 520051,
    role = SutraRole.Apavada, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION,
    blocks = setOf("5.2.48", "5.2.49"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = context.samjnas.any { it.samjna == Samjna.PURANA } &&
        context.terms.singleOrNull()?.surface in setOf("चतुर्", "षष्")

    override fun apply(context: DerivationState): DerivationChange {
        val target = context.terms.single()
        val thuk = DerivationTerm(
            id = "purana_thuk",
            surface = "थ",
            kind = TermKind.AGAMA,
            upadesha = "थुक्",
            createdBySutra = sutra,
        )
        val dat = DerivationTerm(
            id = "purana_dat",
            surface = "",
            kind = TermKind.PRATYAYA,
            upadesha = "डट्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            context.copy(terms = listOf(target, thuk, dat)),
            "$text: ${target.surface} के पूरणार्थक डट् में थुक् आगम।",
        )
    }
}
