package dev.panini.parser

import dev.panini.parser.ast.ParsedNominalBase
import dev.panini.parser.ast.ParsedPada
import dev.panini.parser.ast.ParsedSambodhana
import dev.panini.parser.ast.ParsedSubanta
import dev.panini.parser.ast.ParsedTinganta
import dev.panini.parser.ast.ParsedUtterance
import dev.panini.parser.ast.ParsedVakya
import dev.panini.parser.ast.SimpleNominalKind


/**
 * Converts the generated ANTLR parse-tree contexts into the source AST.
 */
class VakyaAstBuilder {

    fun build(
        context: VakyaParser.UtteranceContext,
    ): ParsedUtterance {
        val vakyas = context.vakya().map(::buildVakya)

        val connectives = context.CONNECTIVE()
            .mapNotNull { it.text }

        return ParsedUtterance(
            sambodhana = context.sambodhana()?.let(::buildSambodhana),
            statements = vakyas,
            connectives = connectives,
        )
    }

    private fun buildSambodhana(
        context: VakyaParser.SambodhanaContext,
    ): ParsedSambodhana =
        ParsedSambodhana(
            particle = requireText(
                value = context.HE()?.text,
                description = "sambodhana particle",
            ),
            pada = buildSubanta(context.subantaPada()),
        )

    private fun buildVakya(
        context: VakyaParser.VakyaContext,
    ): ParsedVakya =
        ParsedVakya(
            /*
             * Because both occurrences of `pada*` use the same rule name,
             * ANTLR exposes them through one combined `pada()` collection.
             * ANTLR retains their source order.
             */
            padas = context.pada().map(::buildPada),
            tinganta = buildTinganta(context.tingantaPada()),
        )

    private fun buildPada(
        context: VakyaParser.PadaContext,
    ): ParsedPada =
        when {
            context.coordinatedSubanta() != null ->
                buildCoordination(context.coordinatedSubanta()!!)

            context.subantaPada() != null ->
                ParsedPada.Subanta(
                    buildSubanta(context.subantaPada()!!),
                )

            context.avyayaKridantaPada() != null ->
                buildAvyayaKridanta(context.avyayaKridantaPada()!!)

            context.avyayaPada() != null ->
                ParsedPada.Avyaya(
                    value = requireText(
                        context.avyayaPada()?.text,
                        "avyaya",
                    ),
                )

            else -> throw IllegalStateException(
                "Unsupported pada parse-tree alternative: ${context.text}",
            )
        }

    private fun buildCoordination(
        context: VakyaParser.CoordinatedSubantaContext,
    ): ParsedPada.Coordination =
        ParsedPada.Coordination(
            members = context.subantaPada().map(::buildSubanta),
        )

    private fun buildSubanta(
        context: VakyaParser.SubantaPadaContext,
    ): ParsedSubanta =
        ParsedSubanta(
            base = buildNominalBase(context.nominalBase()),
            supPratyaya = context.supPratyaya()?.text,
        )

    private fun buildNominalBase(
        context: VakyaParser.NominalBaseContext,
    ): ParsedNominalBase =
        when {
            context.taddhitaPratipadika() != null ->
                buildTaddhita(context.taddhitaPratipadika()!!)

            context.kridantaPratipadika() != null ->
                buildKridanta(context.kridantaPratipadika()!!)

            context.samasaPratipadika() != null ->
                buildSamasa(context.samasaPratipadika()!!)

            context.NUMERAL() != null ->
                ParsedNominalBase.Simple(
                    value = requireText(
                        context.NUMERAL()?.text,
                        "numeral",
                    ),
                    kind = SimpleNominalKind.NUMERAL,
                )

            context.RESULT_REFERENCE() != null ->
                ParsedNominalBase.Simple(
                    value = requireText(
                        context.RESULT_REFERENCE()?.text,
                        "result reference",
                    ),
                    kind = SimpleNominalKind.RESULT_REFERENCE,
                )

            context.IDENTIFIER() != null ->
                ParsedNominalBase.Simple(
                    value = requireText(
                        context.IDENTIFIER()?.text,
                        "nominal identifier",
                    ),
                    kind = SimpleNominalKind.IDENTIFIER,
                )

            else -> throw IllegalStateException(
                "Unsupported nominal-base alternative: ${context.text}",
            )
        }

    private fun buildSamasa(
        context: VakyaParser.SamasaPratipadikaContext,
    ): ParsedNominalBase.Samasa {
        val members = context.simplePratipadika()
            .map { simple ->
                requireText(
                    value = simple.text,
                    description = "samāsa member",
                )
            }

        val separator = context.COMPOUND_SEPARATOR()
            .firstOrNull()
            ?.text
            ?: "-"

        return ParsedNominalBase.Samasa(
            members = members,
            separator = separator,
        )
    }

    private fun buildKridanta(
        context: VakyaParser.KridantaPratipadikaContext,
    ): ParsedNominalBase.Kridanta =
        ParsedNominalBase.Kridanta(
            dhatu = requireText(
                value = context.dhatu()?.text,
                description = "kṛdanta dhātu",
            ),
            vikarana = context.vikarana()?.text,
            pratyaya = requireText(
                value = context.krtPratyaya()?.text,
                description = "kṛt suffix",
            ),
        )

    private fun buildTaddhita(
        context: VakyaParser.TaddhitaPratipadikaContext,
    ): ParsedNominalBase.Taddhita =
        ParsedNominalBase.Taddhita(
            prakriti = requireText(
                value = context.simplePratipadika()?.text,
                description = "taddhita prakṛti",
            ),
            pratyaya = requireText(
                value = context.taddhitaPratyaya()?.text,
                description = "taddhita suffix",
            ),
        )

    private fun buildAvyayaKridanta(
        context: VakyaParser.AvyayaKridantaPadaContext,
    ): ParsedPada.AvyayaKridanta =
        ParsedPada.AvyayaKridanta(
            dhatu = requireText(
                value = context.dhatu()?.text,
                description = "avyaya-kṛdanta dhātu",
            ),
            vikarana = context.vikarana()?.text,
            pratyaya = requireText(
                value = context.avyayaKrtPratyaya()?.text,
                description = "avyaya kṛt suffix",
            ),
        )

    private fun buildTinganta(
        context: VakyaParser.TingantaPadaContext,
    ): ParsedTinganta {
        /*
         * Second grammar alternative:
         *
         *     tingantaPada : IDENTIFIER
         */
        if (context.dhatu() == null) {
            return ParsedTinganta(
                dhatu = null,
                lakara = null,
                unresolvedIdentifier = requireText(
                    value = context.IDENTIFIER()?.text,
                    description = "unresolved tiṅanta",
                ),
            )
        }

        return ParsedTinganta(
            dhatu = requireText(
                value = context.dhatu()?.text,
                description = "tiṅanta dhātu",
            ),
            sanadiPratyayas = context.sanadiPratyaya()
                .mapNotNull { it.text },
            vikarana = context.vikarana()?.text,
            lakara = requireText(
                value = context.lakara()?.text,
                description = "lakāra",
            ),
            tingPratyaya = context.tingPratyaya()?.text,
        )
    }

    private fun requireText(
        value: String?,
        description: String,
    ): String =
        value
            ?.takeIf(String::isNotBlank)
            ?: throw IllegalStateException(
                "The parser produced a blank or missing $description.",
            )
}
