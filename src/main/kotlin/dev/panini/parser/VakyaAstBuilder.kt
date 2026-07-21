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

        val connectives = context.vakyaChain()
            .mapNotNull { it.text }

        return ParsedUtterance(
            sambodhana = context.sambodhana()?.let(::buildSambodhana),
            statements = vakyas,
            connectives = connectives,
            hasDanda = context.DANDA() != null,
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
            pada = buildSubanta(
                context = requireContext(
                    value = context.subantaPada(),
                    description = "sambodhana subanta pada",
                ),
            ),
        )

    private fun buildVakya(
        context: VakyaParser.VakyaContext,
    ): ParsedVakya =
        ParsedVakya(
            padas = context.pada().map(::buildPada),
            tinganta = buildTinganta(
                context = requireContext(
                    value = context.tingantaPada(),
                    description = "tinganta pada",
                ),
            ),
        )

    private fun buildPada(
        context: VakyaParser.PadaContext,
    ): ParsedPada =
        when {
            context.coordinatedSubanta() != null ->
                buildCoordinatedSubanta(context.coordinatedSubanta())

            context.subantaPada() != null ->
                ParsedPada.Subanta(buildSubanta(context.subantaPada()))

            context.avyayaKridantaPada() != null ->
                buildAvyayaKridanta(context.avyayaKridantaPada())

            context.avyayaPada() != null ->
                ParsedPada.Avyaya(
                    value = requireText(
                        value = context.avyayaPada().text,
                        description = "avyaya pada text",
                    ),
                )

            else ->
                error("Unsupported pada structure in AST builder.")
        }

    private fun buildCoordinatedSubanta(
        context: VakyaParser.CoordinatedSubantaContext,
    ): ParsedPada.Coordination =
        ParsedPada.Coordination(
            members = context.subantaPada().map(::buildSubanta),
        )

    private fun buildSubanta(
        context: VakyaParser.SubantaPadaContext,
    ): ParsedSubanta =
        ParsedSubanta(
            base = buildNominalBase(
                context = requireContext(
                    value = context.nominalBase(),
                    description = "nominal base",
                ),
            ),
            supPratyaya = context.supPratyaya()?.text,
        )

    private fun buildNominalBase(
        context: VakyaParser.NominalBaseContext,
    ): ParsedNominalBase =
        when {
            context.taddhitaPratipadika() != null -> {
                val taddhitaCtx = context.taddhitaPratipadika()
                ParsedNominalBase.Taddhita(
                    prakriti = requireText(
                        value = taddhitaCtx.simplePratipadika()?.text,
                        description = "taddhita prakriti",
                    ),
                    pratyaya = requireText(
                        value = taddhitaCtx.taddhitaPratyaya()?.text,
                        description = "taddhita pratyaya",
                    ),
                )
            }

            context.kridantaPratipadika() != null -> {
                val kridantaCtx = context.kridantaPratipadika()
                ParsedNominalBase.Kridanta(
                    upasargas = kridantaCtx.upasarga().map { it.text },
                    dhatu = requireText(
                        value = kridantaCtx.dhatu()?.text,
                        description = "kridanta dhatu",
                    ),
                    vikarana = kridantaCtx.vikarana()?.text,
                    pratyaya = requireText(
                        value = kridantaCtx.krtPratyaya()?.text,
                        description = "krt pratyaya",
                    ),
                )
            }

            context.samasaPratipadika() != null -> {
                val samasaCtx = context.samasaPratipadika()
                ParsedNominalBase.Samasa(
                    members = samasaCtx.simplePratipadika().map { it.text },
                    separator = samasaCtx.COMPOUND_SEPARATOR()?.firstOrNull()?.text ?: "-",
                )
            }

            context.NUMERAL() != null ->
                ParsedNominalBase.Simple(
                    value = context.NUMERAL().text,
                    kind = SimpleNominalKind.NUMERAL,
                )

            context.RESULT_REFERENCE() != null ->
                ParsedNominalBase.Simple(
                    value = context.RESULT_REFERENCE().text,
                    kind = SimpleNominalKind.RESULT_REFERENCE,
                )

            context.IDENTIFIER() != null ->
                ParsedNominalBase.Simple(
                    value = context.IDENTIFIER().text,
                    kind = SimpleNominalKind.IDENTIFIER,
                )

            else ->
                error("Unsupported nominal base structure in AST builder.")
        }

    private fun buildAvyayaKridanta(
        context: VakyaParser.AvyayaKridantaPadaContext,
    ): ParsedPada.AvyayaKridanta =
        ParsedPada.AvyayaKridanta(
            upasargas = context.upasarga().map { it.text },
            dhatu = requireText(
                value = context.dhatu()?.text,
                description = "avyaya kridanta dhatu",
            ),
            vikarana = context.vikarana()?.text,
            pratyaya = requireText(
                value = context.avyayaKrtPratyaya()?.text,
                description = "avyaya krt pratyaya",
            ),
        )

    private fun buildTinganta(
        context: VakyaParser.TingantaPadaContext,
    ): ParsedTinganta =
        if (context.dhatu() != null) {
            ParsedTinganta(
                upasargas = context.upasarga().map { it.text },
                dhatu = context.dhatu().text,
                sanadiPratyayas = context.sanadiPratyaya().map { it.text },
                vikarana = context.vikarana()?.text,
                lakara = context.lakara()?.text,
                tingPratyaya = context.tingPratyaya()?.text,
                unresolvedIdentifier = null,
            )
        } else {
            ParsedTinganta(
                dhatu = null,
                sanadiPratyayas = emptyList(),
                vikarana = null,
                lakara = null,
                tingPratyaya = null,
                unresolvedIdentifier = requireText(
                    value = context.IDENTIFIER()?.text,
                    description = "unresolved tinganta identifier",
                ),
            )
        }

    private fun <T> requireContext(
        value: T?,
        description: String,
    ): T =
        value ?: error("Missing parse-tree context for $description.")

    private fun requireText(
        value: String?,
        description: String,
    ): String =
        value ?: error("Missing text token for $description.")
}
