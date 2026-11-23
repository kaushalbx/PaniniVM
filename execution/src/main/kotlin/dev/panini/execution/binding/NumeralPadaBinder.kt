package dev.panini.execution.binding

import dev.panini.aryabhatiya.AryabhatiyaDecoder
import dev.panini.bhutasamkhya.BhutasamkhyaDecoder
import dev.panini.core.Karaka
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.SanskritValue
import dev.panini.katapayadi.KatapayadiDecoder
import dev.panini.sankhya.PrimitiveSankhya
import dev.panini.sankhya.SankhyaExpression
import dev.panini.vyakaranam.ast.AryabhatiyaPada
import dev.panini.vyakaranam.ast.BhutasamkhyaPada
import dev.panini.vyakaranam.ast.KatapayadiPada
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada

/**
 * Centralises all numeric-pada decoding and the shared three-step binding pattern:
 *
 *   decode value → retain the original pada's grammatical analysis → add binding
 *
 * The simple numeral pada types ([SankhyaPuranaPada], [KatapayadiPada],
 * [AryabhatiyaPada], [BhutasamkhyaPada]) each get a typed [bind] overload that
 * reduces their dispatch arm in [KarakaExtractor] to a single line.
 *
 * The [SankhyaPada] arm retains its own complex op-stem lookahead logic inside
 * [KarakaExtractor], but delegates the final step via [bindDecoded] and the
 * lookahead scan via [extractNumeralValue] and [evaluateStems].
 */
internal object NumeralPadaBinder {
    private val katapayadiDecoder = KatapayadiDecoder()
    private val aryabhatiyaDecoder = AryabhatiyaDecoder()
    private val bhutasamkhyaDecoder = BhutasamkhyaDecoder()

    internal fun resolveSemanticValue(pada: Pada): SanskritValue.Sankhya? {
        if (dev.panini.execution.PuranaPratyayaResolver.ordinalValue(pada) != null) return null
        val value = extractNumeralValue(pada) ?: return null
        requireIntrinsicVacana(pada, value)
        val word = when (pada) {
            is SankhyaPada -> writtenCardinalStem(pada) ?: pada.stems.joinToString(" ")
            is KatapayadiPada -> pada.word
            is AryabhatiyaPada -> pada.word
            is BhutasamkhyaPada -> pada.terms.joinToString(" ")
            is SubantaPada -> NumeralAstNormalizer.resolve(pada.pratipadika)?.semanticValue?.word
                ?: pada.pratipadika.sourceText
            else -> return null
        }
        return SanskritValue.Sankhya(value, word)
    }

    /**
     * A cardinal below विंशति carries its own number in every case. The parser checks primitive numerals;
     * compound ones (एकादशन्, एक + दशन्) are known only once evaluated, so they are checked here the same way:
     * some reading of the written sup must agree with the numeral's number.
     */
    private fun requireIntrinsicVacana(pada: Pada, written: Long) {
        val sup = when (pada) {
            is SankhyaPada -> pada.sup
            is SubantaPada -> pada.sup
            else -> return
        }
        // The grammatical head decides the number: एक + ऊन + विंशति is a singular विंशति (19), while
        // एक + दशन् is a plural दशन् count (11). A segmented construction's head is its final stem.
        val value = (pada as? SankhyaPada)?.stems?.lastOrNull()
            ?.let { PrimitiveSankhya.fromAnnotatedPratipadika(it)?.value }
            ?.takeIf { it >= 20L } ?: written
        val candidates = dev.panini.core.SupAffix.candidates(sup.text)
        if (dev.panini.sankhya.SankhyaVacana.acceptsAny(value, candidates)) return
        val stem = when (pada) {
            is SankhyaPada -> pada.stems.joinToString(" + ")
            is SubantaPada -> pada.pratipadika.sourceText
            else -> null
        }
        throw dev.panini.execution.NumeralDiagnostics.numberAgreement(pada, stem, value, candidates.first())
    }

    /**
     * Decodes [pada] to a Long if it carries a numeric value, otherwise returns null.
     * Used by the [SankhyaPada] op-stem arm to peek ahead at the next numeral pada.
     */
    internal fun extractNumeralValue(pada: Pada): Long? {
        if (dev.panini.execution.PuranaPratyayaResolver.ordinalValue(pada) != null) return null
        return when (pada) {
        is SankhyaPada -> pada.value ?: sharedSankhyaEvaluator.evaluateStems(pada.stems).value
        is SubantaPada -> if (NumeralAstNormalizer.hasNonGenderDerivation(pada.pratipadika)) null
            else NumeralAstNormalizer.resolve(pada.pratipadika)?.semanticValue?.value
                ?: PrimitiveSankhya.fromAnnotatedPratipadika(pada.pratipadika.sourceText)?.value
        is KatapayadiPada -> pada.value ?: katapayadiDecoder.decode(pada.word)
        is AryabhatiyaPada -> pada.value ?: aryabhatiyaDecoder.decode(pada.word)
        is BhutasamkhyaPada -> pada.value ?: bhutasamkhyaDecoder.decodeTerms(pada.terms)
        else -> null
        }
    }

    /** Extracts an ordinal value only when numeric morphology evaluates to pūraṇa. */
    internal fun extractOrdinalValue(pada: Pada): Long? {
        val lexical = (pada as? SubantaPada)?.pratipadika as? dev.panini.vyakaranam.ast.MulaPratipadika
        if (lexical?.lexicalIdentity?.ordinalValue != null && lexical.lexicalOrdinalValue == null) return null
        // A typed pūraṇa node already owns its semantic value. All clients of
        // ordinal extraction (memory, kāraka queries, and parameters) share this
        // precedence instead of evaluating source stems before inspecting it.
        dev.panini.execution.PuranaPratyayaResolver.ordinalValue(pada)?.let { return it }
        val expression = when (pada) {
            is SankhyaPuranaPada -> runCatching { sharedSankhyaEvaluator.evaluateStems(pada.stems) }.getOrNull()
            is SubantaPada -> runCatching {
                sharedSankhyaEvaluator.evaluateStems(listOf(pada.pratipadika.baseText()))
            }.getOrNull()
            else -> null
        }
        return (expression as? SankhyaExpression.Purana)?.value
    }

    /**
     * Evaluates a list of Sanskrit numeral stems to a numeric result.
     * Exposed for the [SankhyaPada] op-stem arm which builds [fullStems] before calling [bindDecoded].
     */
    internal fun evaluateStems(stems: List<String>) = sharedSankhyaEvaluator.evaluateStems(stems)

    // ---- Typed bind overloads -------------------------------------------------------
    // Cardinals delegate to [bindDecoded]; ordinals preserve their own value type.

    internal fun bind(
        pada: SankhyaPuranaPada,
        inferKarakas: (dev.panini.vyakaranam.ast.SupBearingPada) -> Set<Karaka>,
        addBinding: (ExecutionExpression, Set<Karaka>) -> Unit,
    ) = bindOrdinal(pada, inferKarakas, addBinding)

    internal fun bindOrdinal(
        pada: dev.panini.vyakaranam.ast.SupBearingPada,
        inferKarakas: (dev.panini.vyakaranam.ast.SupBearingPada) -> Set<Karaka>,
        addBinding: (ExecutionExpression, Set<Karaka>) -> Unit,
    ) {
        val value = requireNotNull(dev.panini.execution.PuranaPratyayaResolver.semanticValue(pada)) {
            "Ordinal morphology must resolve to a positive ordinal value."
        }
        addBinding(ExecutionExpression.Pada(pada.sourceText, value.samjnas, value = value), inferKarakas(pada))
    }

    internal fun bind(
        pada: KatapayadiPada,
        inferKarakas: (dev.panini.vyakaranam.ast.SupBearingPada) -> Set<Karaka>,
        addBinding: (ExecutionExpression, Set<Karaka>) -> Unit,
    ) = bindDecoded(
        pada,
        pada.value ?: katapayadiDecoder.decode(pada.word),
        inferKarakas, addBinding,
    )

    internal fun bind(
        pada: AryabhatiyaPada,
        inferKarakas: (dev.panini.vyakaranam.ast.SupBearingPada) -> Set<Karaka>,
        addBinding: (ExecutionExpression, Set<Karaka>) -> Unit,
    ) = bindDecoded(
        pada,
        pada.value ?: aryabhatiyaDecoder.decode(pada.word),
        inferKarakas, addBinding,
    )

    internal fun bind(
        pada: BhutasamkhyaPada,
        inferKarakas: (dev.panini.vyakaranam.ast.SupBearingPada) -> Set<Karaka>,
        addBinding: (ExecutionExpression, Set<Karaka>) -> Unit,
    ) = bindDecoded(
        pada,
        pada.value ?: bhutasamkhyaDecoder.decodeTerms(pada.terms),
        inferKarakas, addBinding,
    )

    /**
     * Shared three-step binding pattern used by all typed [bind] overloads and
     * the [SankhyaPada] op-stem arm. Numeric decoding never replaces the
     * grammatical occurrence used to look up its kāraka resolution.
     */
    internal fun bindDecoded(
        pada: dev.panini.vyakaranam.ast.SupBearingPada,
        value: Long,
        inferKarakas: (dev.panini.vyakaranam.ast.SupBearingPada) -> Set<Karaka>,
        addBinding: (ExecutionExpression, Set<Karaka>) -> Unit,
    ) {
        addBinding(ExecutionExpression.sankhya(value, (pada as? SankhyaPada)?.let(::writtenCardinalStem)
            ?: pada.sourceText), inferKarakas(pada))
    }

    /**
     * A segmented cardinal carries the prātipadika of its written construction, as a primitive literal carries
     * its stem (पञ्चन्): एक + ऊन + विंशति is एकोनविंशति, never the same-value नवदशन्. Operator
     * constructions (वर्ग, गुणित, …) do not name a numeral stem and keep their source text.
     */
    internal fun writtenCardinalStem(pada: SankhyaPada): String? {
        val markers = dev.panini.sankhya.SankhyaOperationMarkers
        if (pada.stems.any { markers.isMultiplication(it) || markers.isDivision(it) || markers.isSquare(it) ||
                markers.isCube(it) || markers.isSquareRoot(it) || markers.isConstruction(it) }) return null
        return runCatching {
            val expression = sharedSankhyaEvaluator.evaluateStems(pada.stems)
            if (expression is dev.panini.sankhya.SankhyaExpression.Purana) return null
            sharedSankhyaGenerator.pratipadika(expression)
        }.getOrNull()
    }
}
