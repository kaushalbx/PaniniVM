package dev.panini.execution

import dev.panini.core.StriAffix
import dev.panini.derivation.StriPratyayaEngine
import dev.panini.derivation.StriPratyayaRequest
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.vyakaranam.ast.SankhyaPuranaPada

/** Shares actual affix licensing between agreement analysis and readable generation. */
object OrdinalFeminineDerivation {
    private val feminine = StriPratyayaEngine()

    fun derive(pada: SankhyaPuranaPada): String {
        val affix = requireNotNull(pada.striAffix) { "An ordinal feminine derivation requires its source affix." }
        val ordinal = requireNotNull(PuranaPratyayaResolver.ordinalValue(pada)) { "Unresolved ordinal feminine base." }
        val source = PuranaPratyayaResolver.sourceDerivation(pada, ordinal)
        val samjna = when (affix) {
            StriAffix.TAP -> Samjna.TAP
            StriAffix.NGIP -> Samjna.NIP
            StriAffix.NGISH -> Samjna.NIS
            StriAffix.NGIN -> Samjna.NIN
            else -> throw IllegalArgumentException("Unsupported ordinal feminine affix: ${affix.upadesha}.")
        }
        val markers = source.final.terms.filter { it.kind == TermKind.PRATYAYA }.flatMap { it.itMarkers }.toSet()
        return feminine.derive(StriPratyayaRequest(source.final.surface, samjna, markers)).final.surface
    }
}
