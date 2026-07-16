package dev.sanskrit.ashtadhyayi.adhyaya6.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 6.4.88: भुवो वुग्लुङ्लिटोः. */
object BhuvoVuglunglitoSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.88", text = "भुवो वुग्लुङ्लिटोः",
    hindiExplanation = "लुङ् और लिट् में भू धातु के बाद वुक् का आगम होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 4, optional = false, kramaValue = 640088,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DHATU,
    blocks = setOf("7.2.115", "7.3.84"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara in setOf(Lakara.LUNG, Lakara.LIT) &&
            context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" && it.matchesUpadesha("भू") }.let { index ->
                index >= 0 && context.terms.getOrNull(index + 1)?.surface?.firstOrNull() in setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ए', 'ऐ', 'ओ', 'औ')
            } &&
            context.terms.none { it.id == "vuk" }

    override fun apply(context: DerivationState): DerivationChange {
        val index = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" && it.matchesUpadesha("भू") }
        val vuk = DerivationTerm("vuk", "व्", TermKind.AGAMA, upadesha = "वुक्", createdBySutra = "6.4.88")
        return DerivationChange(
            context.copy(terms = context.terms.take(index + 1) + vuk + context.terms.drop(index + 1)),
            "6.4.88 inserts the effective व् of वुक् after भू in ${context.effectiveContext.rupa.lakara}.",
        )
    }
}
