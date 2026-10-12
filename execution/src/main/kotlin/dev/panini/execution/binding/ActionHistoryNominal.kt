package dev.panini.execution.binding

import dev.panini.core.KrtAffix
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.isRecognizedStriAffix

/** Supported history-reference semantics, not a claim that every kṛt is an event noun. */
internal fun KridantaPratipadika.requireActionHistoryNominal() {
    require(krtAffix in setOf(KrtAffix.LYUT, KrtAffix.GHAN)) {
        "Action history requires a supported action nominal (ल्युट् or घञ्); other kṛt meanings need their own reference semantics."
    }
    require(vikaras.all { it.isRecognizedStriAffix }) {
        "A further-derived action noun requires its own reference semantics, not its base action's history."
    }
}
