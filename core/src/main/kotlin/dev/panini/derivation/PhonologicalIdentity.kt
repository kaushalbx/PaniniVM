package dev.panini.derivation

import dev.panini.shiksha.Varna
import dev.panini.shiksha.toVarnas

/**
 * Named phonological identities used by rules whose domain is a present form,
 * rather than an immutable upadeśa.  Keeping their spelling here prevents a
 * sūtra from interpreting Devanāgarī text as phonological data.
 */
enum class PhonologicalIdentity(private val spelling: String) {
    LABH("लभ्"),
    SRAMBH("स्रम्भ्"),
    BHU("भू"),
    KR("कृ"),
    SR("सृ"),
    BHR("भृ"),
    VR("वृ"),
    STU("स्तु"),
    DRU("द्रु"),
    SRU("स्रु"),
    SHRU("श्रु"),
    HR("हृ"),
    JI("जि"),
    CI("चि"),
    NI("नी"),
    YU("यु"),
    VU("वु"),
    A("अ"),
    ATUS("अतुस्"),
    US("उस्"),
    ATHUS("अथुस्"),
    VA("व"),
    MA("म"),
    E("ए"),
    AATE("आते"),
    IRE("इरे"),
    SE("से"),
    AATHE("आथे"),
    DHVE("ध्वे"),
    VAHE("वहे"),
    MAHE("महे");

    val varnas: List<Varna> by lazy(LazyThreadSafetyMode.PUBLICATION) { spelling.toVarnas() }
}

fun DerivationTerm.hasCurrentForm(identity: PhonologicalIdentity): Boolean = varnas == identity.varnas

fun DerivationTerm.hasAnyCurrentForm(vararg identities: PhonologicalIdentity): Boolean =
    identities.any(::hasCurrentForm)

fun DerivationTerm.containsCurrentSequence(identity: PhonologicalIdentity): Boolean =
    varnas.windowed(identity.varnas.size).any { it == identity.varnas }
