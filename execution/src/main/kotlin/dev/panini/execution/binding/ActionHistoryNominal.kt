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
    require(upasargaIdentities.all { it != null } && dhatu.sanadiAffixes.all { it != null }) {
        "Action history cannot discard an unrecognized upasarga or sanādi affix."
    }
}

internal fun KridantaPratipadika.resolveActionHistoryDhatu(): dev.panini.dhatupatha.Dhatu {
    requireActionHistoryNominal()
    return requireNotNull(DhatuCache.resolve(dhatu)) {
        "The named action in this history reference has no resolved dhātu identity."
    }
}

internal fun KridantaPratipadika.resolveActionHistoryIdentity(): dev.panini.core.ActionIdentity {
    val lexical = resolveActionHistoryDhatu()
    return dev.panini.core.ActionIdentity(lexical.id, upasargaIdentities.filterNotNull(), dhatu.sanadiAffixes.filterNotNull())
}
