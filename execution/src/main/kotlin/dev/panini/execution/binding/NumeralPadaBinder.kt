package dev.panini.execution.binding

import dev.panini.aryabhatiya.AryabhatiyaDecoder
import dev.panini.bhutasamkhya.BhutasamkhyaDecoder
import dev.panini.core.Karaka
import dev.panini.execution.ExecutionExpression
import dev.panini.katapayadi.KatapayadiDecoder
import dev.panini.sankhya.PrimitiveSankhya
import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.vyakaranam.ast.AryabhatiyaPada
import dev.panini.vyakaranam.ast.BhutasamkhyaPada
import dev.panini.vyakaranam.ast.KatapayadiPada
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya

/**
 * Centralises all numeric-pada decoding and the shared three-step binding pattern:
 *
 *   decode value → construct synthetic [SubantaPada] → infer kārakas → add binding
 *
 * The four simple numeral pada types ([SankhyaPuranaPada], [KatapayadiPada],
 * [AryabhatiyaPada], [BhutasamkhyaPada]) each get a typed [bind] overload that
 * reduces their dispatch arm in [KarakaExtractor] to a single line.
 *
 * The [SankhyaPada] arm retains its own complex op-stem lookahead logic inside
 * [KarakaExtractor], but delegates the final step via [bindDecoded] and the
 * lookahead scan via [extractNumeralValue] and [evaluateStems].
 */
internal object NumeralPadaBinder {
    private val sankhyaEvaluator = SankhyaEvaluator()
    private val katapayadiDecoder = KatapayadiDecoder()
    private val aryabhatiyaDecoder = AryabhatiyaDecoder()
    private val bhutasamkhyaDecoder = BhutasamkhyaDecoder()

    /**
     * Decodes [pada] to a Long if it carries a numeric value, otherwise returns null.
     * Used by the [SankhyaPada] op-stem arm to peek ahead at the next numeral pada.
     */
    internal fun extractNumeralValue(pada: Pada): Long? = when (pada) {
        is SankhyaPada -> pada.value ?: sankhyaEvaluator.evaluateStems(pada.stems).value
        is SubantaPada -> (pada.pratipadika as? SankhyaPratipadika)?.value
            ?: PrimitiveSankhya.fromAnnotatedPratipadika(pada.pratipadika.sourceText)?.value
        is KatapayadiPada -> pada.value ?: katapayadiDecoder.decode(pada.word)
        is AryabhatiyaPada -> pada.value ?: aryabhatiyaDecoder.decode(pada.word)
        is BhutasamkhyaPada -> pada.value ?: bhutasamkhyaDecoder.decodeTerms(pada.terms)
        else -> null
    }

    /**
     * Evaluates a list of Sanskrit numeral stems to a numeric result.
     * Exposed for the [SankhyaPada] op-stem arm which builds [fullStems] before calling [bindDecoded].
     */
    internal fun evaluateStems(stems: List<String>) = sankhyaEvaluator.evaluateStems(stems)

    // ---- Typed bind overloads -------------------------------------------------------
    // Each decodes its pada then delegates to [bindDecoded].

    internal fun bind(
        pada: SankhyaPuranaPada,
        inferKarakas: (SubantaPada) -> Set<Karaka>,
        addBinding: (ExecutionExpression, Set<Karaka>) -> Unit,
    ) = bindDecoded(
        pada.sourceText, pada.sup,
        pada.value ?: sankhyaEvaluator.evaluateStems(pada.stems).value,
        inferKarakas, addBinding,
    )

    internal fun bind(
        pada: KatapayadiPada,
        inferKarakas: (SubantaPada) -> Set<Karaka>,
        addBinding: (ExecutionExpression, Set<Karaka>) -> Unit,
    ) = bindDecoded(
        pada.sourceText, pada.sup,
        pada.value ?: katapayadiDecoder.decode(pada.word),
        inferKarakas, addBinding,
    )

    internal fun bind(
        pada: AryabhatiyaPada,
        inferKarakas: (SubantaPada) -> Set<Karaka>,
        addBinding: (ExecutionExpression, Set<Karaka>) -> Unit,
    ) = bindDecoded(
        pada.sourceText, pada.sup,
        pada.value ?: aryabhatiyaDecoder.decode(pada.word),
        inferKarakas, addBinding,
    )

    internal fun bind(
        pada: BhutasamkhyaPada,
        inferKarakas: (SubantaPada) -> Set<Karaka>,
        addBinding: (ExecutionExpression, Set<Karaka>) -> Unit,
    ) = bindDecoded(
        pada.sourceText, pada.sup,
        pada.value ?: bhutasamkhyaDecoder.decodeTerms(pada.terms),
        inferKarakas, addBinding,
    )

    /**
     * Shared three-step binding pattern used by all typed [bind] overloads and
     * the [SankhyaPada] op-stem arm: construct synthetic [SubantaPada] → infer
     * kārakas → add binding.
     */
    internal fun bindDecoded(
        sourceText: String,
        sup: SupPratyaya,
        value: Long,
        inferKarakas: (SubantaPada) -> Set<Karaka>,
        addBinding: (ExecutionExpression, Set<Karaka>) -> Unit,
    ) {
        val sub = SubantaPada(sourceText, SankhyaPratipadika(sourceText, value), sup)
        addBinding(ExecutionExpression.sankhya(value, sourceText), inferKarakas(sub))
    }
}
