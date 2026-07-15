package dev.sanskrit.ashtadhyayi.adhyaya7.pada4

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

/** 7.4.71: तस्मान्नुड् द्विहलः. */
object TasmanNudDvihalahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.71", text = "तस्मान्नुड् द्विहलः",
    hindiExplanation = "लिट् में दीर्घ हुए अकारवाले अभ्यास के बाद दो हलों वाले धातु से पहले नुट् का आगम होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 4, optional = false, kramaValue = 740071,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DHATU,
), DerivationSutra {
    private val consonants = setOf(
        'क', 'ख', 'ग', 'घ', 'ङ', 'च', 'छ', 'ज', 'झ', 'ञ', 'ट', 'ठ', 'ड', 'ढ', 'ण',
        'त', 'थ', 'द', 'ध', 'न', 'प', 'फ', 'ब', 'भ', 'म', 'य', 'र', 'ल', 'व', 'श', 'ष', 'स', 'ह',
    )

    override fun matches(context: DerivationState): Boolean {
        val abhyasaIndex = context.terms.indexOfFirst { it.id == "abhyasa" }
        val dhatu = context.terms.drop(abhyasaIndex + 1).firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LIT &&
            abhyasaIndex >= 0 && context.terms[abhyasaIndex].surface.startsWith('आ') &&
            dhatu.surface.count { it in consonants } >= 2 && context.terms.none { it.id == "nut" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasaIndex = context.terms.indexOfFirst { it.id == "abhyasa" }
        val nut = DerivationTerm("nut", "न्", TermKind.AGAMA, upadesha = "नुट्")
        return DerivationChange(
            context.copy(terms = context.terms.take(abhyasaIndex + 1) + nut + context.terms.drop(abhyasaIndex + 1)),
            "7.4.71 inserts the effective न् of नुट् after the lengthened abhyāsa.",
        )
    }
}
