package dev.panini.derivation

/**
 * Explicit forward-derivation requests attested as examples by the Kāśikā on 5.3.7.
 * This is not an inverse analyzer and contains no corpus expected outputs.
 */
object DeclaredAvyayaDerivations {
    const val source: String =
        "https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/5/5.3.7.htm"

    val requests: List<TaddhitaDerivationRequest> = listOf("तद्", "यद्", "किम्", "इदम्").map {
        TaddhitaDerivationRequest(it, DerivationalMeaning.APADANA)
    }
}
