package dev.panini.ashtadhyayi

import dev.panini.core.TaddhitaAdesha
import dev.panini.core.TaddhitaAffix
import dev.panini.core.TypedAffix

/** Existing supported identity domain; not a claim that all taddhita affixes are implemented. */
internal val initialVrddhiTaddhitaIdentities: List<TypedAffix> =
    TaddhitaAffix.entries + TaddhitaAdesha.entries
