package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Vyanjana

/** Shared ś→ch operation; canonical aṭ and the vārttika's additional am domain remain separate. */
internal fun DerivationState.shaToChaTargets(additionalAm: Boolean): List<LocatedVarna> =
    findVarnaTriplets { previous, middle, next ->
        val engine = Ashtadhyayi.pratyaharaEngine
        val owner = terms.single { it.id == previous.termId }
        isPadaFinalVarna(owner, previous.index) &&
            engine.contains(Pratyahara.JHAY, previous.varna) && middle.varna == Vyanjana.SHA &&
            if (additionalAm) engine.contains(Pratyahara.AM, next.varna) && !engine.contains(Pratyahara.AT, next.varna)
            else engine.contains(Pratyahara.AT, next.varna)
    }

internal fun DerivationState.applyShaToCha(occurrence: LocatedVarna, identity: String): DerivationChange =
    DerivationChange(replaceTermVarna(occurrence.termId, occurrence.index, listOf(Vyanjana.CHA), identity),
        "$identity replaces ś with ch at ${occurrence.termId}:${occurrence.index} after pada-final jhay.")
