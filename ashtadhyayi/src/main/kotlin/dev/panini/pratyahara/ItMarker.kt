package dev.panini.pratyahara

import dev.panini.shiksha.Vyanjana

enum class ItMarker(
    /** The consonant that closes a Māheśvara sūtra and is an it by 1.3.3 हलन्त्यम्. */
    val varna: Vyanjana,
) {
    N(Vyanjana.NNA),
    K(Vyanjana.KA),
    NG(Vyanjana.NGA),
    C(Vyanjana.CA),
    TT(Vyanjana.TTA),
    NN(Vyanjana.NNA),
    M(Vyanjana.MA),
    NY(Vyanjana.NYA),
    SH(Vyanjana.SSA),
    SHA(Vyanjana.SHA),
    V(Vyanjana.VA),
    Y(Vyanjana.YA),
    R(Vyanjana.RA),
    L(Vyanjana.LA),
}
