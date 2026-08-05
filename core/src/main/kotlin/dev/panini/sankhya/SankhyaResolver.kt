package dev.panini.sankhya

import dev.panini.shiksha.Samjna

/**
 * Unified resolver for Saṅkhyā (numeral) stems across PaniniVM modules.
 * Handles primitive numerals, Sūtra 1.1.23 quantifiers (bahu, gaṇa, ḍati, vatu),
 * and compound numeral stems.
 */
object SankhyaResolver {
    private val tatiQuantifiers = setOf("बहु", "गण", "कति", "यति", "तति")

    fun isSankhya(stem: String, samjnas: Set<Samjna> = emptySet()): Boolean {
        if (samjnas.contains(Samjna.SANKHYA)) return true

        if (PrimitiveSankhya.fromAnnotatedPratipadika(stem) != null) return true

        if (stem in tatiQuantifiers || stem.endsWith("वत्")) return true

        return PrimitiveSankhya.entries.any { prim ->
            stem.endsWith(prim.pratipadika) || stem.endsWith(prim.purvapada) || stem.endsWith(prim.uttarapada)
        }
    }
}
