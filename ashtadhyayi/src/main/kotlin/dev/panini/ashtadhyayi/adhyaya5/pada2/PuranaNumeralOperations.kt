package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind

internal object PuranaNumeralClasses {
    val vimshatyadiHeads = setOf(
        "विंशति", "त्रिंशत्", "चत्वारिंशत्", "पञ्चाशत्",
        "षष्टि", "सप्तति", "अशीति", "नवति",
    )
    val shashtyadiHeads = setOf("षष्टि", "सप्तति", "अशीति", "नवति")
    val shatadiHeads = setOf("शत", "सहस्र", "अयुत", "लक्ष", "प्रयुत", "कोटि")
}

internal fun DerivationState.hasTamat(): Boolean = terms.any { it.upadesha == "तमट्" }

internal fun DerivationState.datIndex(): Int = terms.indexOfLast { it.upadesha == "डट्" }

internal fun DerivationState.insertTamat(sutra: String, explanation: String): DerivationChange {
    val index = datIndex()
    require(index > 0) { "तमट् requires an existing ordinal डट् suffix." }
    require(!hasTamat()) { "An ordinal derivation can contain only one तमट् augment." }
    val tamat = DerivationTerm(
        id = "purana_tamat",
        surface = "तम",
        kind = TermKind.AGAMA,
        upadesha = "तमट्",
        createdBySutra = sutra,
    )
    val changedTerms = terms.toMutableList().apply { add(index, tamat) }
    return DerivationChange(copy(terms = changedTerms), explanation)
}
