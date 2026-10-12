package dev.panini.ashtadhyayi

import dev.panini.core.TaddhitaAdesha
import dev.panini.core.TaddhitaAffix
import dev.panini.core.TypedAffix

/** Existing supported identity domain; not a claim that all taddhita affixes are implemented. */
internal val initialVrddhiTaddhitaIdentities: List<TypedAffix> =
    listOf(TaddhitaAffix.AN, TaddhitaAffix.INY, TaddhitaAffix.YANY, TaddhitaAffix.PHAK,
        TaddhitaAffix.DHAK, TaddhitaAffix.VAT, TaddhitaAffix.TASIL, TaddhitaAffix.TRAL) + TaddhitaAdesha.entries
