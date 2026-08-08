package dev.panini.execution.binding

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.SamasaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.UnadyantaPratipadika

internal fun SubantaPada.hasVibhakti(vibhakti: Vibhakti): Boolean =
    SupAffix.candidates(sup.text).any { it.vibhakti == vibhakti }

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

/**
 * Case-independent identity of a named PVM value.
 *
 * Unlike [baseText], this retains the derivational structure that distinguishes
 * e.g. जन् + ल्युट् from जन् + घञ्, and retains every member of a samāsa.
 * The external sup belongs to [SubantaPada], so it deliberately cannot affect
 * this key.
 */
internal fun Pratipadika.referenceKey(): String = when (this) {
    is SankhyaPratipadika -> sourceText
    is MulaPratipadika -> text
    is KridantaPratipadika -> buildList {
        addAll(upasargas)
        add(dhatu.mulaDhatu)
        addAll(dhatu.sanadiPratyayas)
        add(krtPratyaya)
    }.joinToString("+")
    is UnadyantaPratipadika -> sourceText
    is SamasaPratipadika -> angas.joinToString("-") { it.pratipadika.referenceKey() }
}
