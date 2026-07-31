package dev.panini.execution.binding

import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.SamasaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.UnadyantaPratipadika

/**
 * Returns the canonical base text for this [Pratipadika]:
 * - For [SankhyaPratipadika] and [UnadyantaPratipadika] the annotated source text is used.
 * - For [MulaPratipadika] the stem text is used.
 * - For [KridantaPratipadika] the root dhātu stem is used.
 * - For [SamasaPratipadika] member base texts are joined with "-".
 */
internal fun Pratipadika.baseText(): String = when (this) {
    is SankhyaPratipadika -> sourceText
    is MulaPratipadika -> text
    is KridantaPratipadika -> dhatu.mulaDhatu
    is UnadyantaPratipadika -> sourceText
    is SamasaPratipadika -> angas.joinToString("-") { it.pratipadika.baseText() }
}
