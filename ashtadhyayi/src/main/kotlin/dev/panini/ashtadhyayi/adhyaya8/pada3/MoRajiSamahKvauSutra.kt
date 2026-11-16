package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.*

/**
 * 8.3.25 मो राजि समः क्वौ — the म् of सम् stays म् (not 8.3.23's anusvāra) before राज् ending in क्विप्:
 * सम् + राट् → सम्राट्. Only the bare क्विबन्त word राज्/राट्/राड् is taken: an inflected form does not by
 * its spelling show the क्विप् (राजः may be राजन्'s compound-final राज).
 */
object MoRajiSamahKvauSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.25", text = "मो राजि समः क्वौ",
    hindiExplanation = "क्विप्-प्रत्ययान्त राज् परे होने पर सम् के मकार को मकार ही रहता है।",
    type = SutraType.APAVADA, chapter = 8, pada = 3, optional = false, kramaValue = 830025,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI, blocks = setOf("8.3.23"),
), DerivationSutra {
    private val sam = "सम्".toVarnas()
    private val raj = setOf("राज्".toVarnas(), "राट्".toVarnas(), "राड्".toVarnas())
    private fun target(state: DerivationState): Pair<DerivationTerm, DerivationTerm>? {
        for (index in 0 until state.terms.lastIndex) {
            val left = state.terms[index]
            val right = state.terms[index + 1]
            if (left.varnas != sam || right.varnas !in raj) continue
            if (state.isBlockedAtBoundary("8.3.23", left.id, right.id)) continue
            return left to right
        }
        return null
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (left, right) = requireNotNull(target(context))
        return DerivationChange(context.blockAtBoundary("8.3.23", left.id, right.id, sutra),
            "8.3.25 keeps the म् of सम् before क्विबन्त राज्.")
    }
}
