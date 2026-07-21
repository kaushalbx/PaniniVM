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
    ): ParsedVakya {
        val padas = context.pada().map(::buildPada)

        // A vakya now allows zero or more verbs.
        val tinganta = context.tingantaPada().firstOrNull()?.let(::buildTinganta)

        return ParsedVakya(
            padas = padas,
            tinganta = tinganta,
        )
    }

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
        when (context) {
            is VakyaParser.SimpleBaseContext ->
                buildSimplePratipadika(context.simplePratipadika())

            is VakyaParser.KridantaBaseContext ->
                buildKridanta(context.kridantaPratipadika())

            is VakyaParser.StriBaseContext ->
                ParsedNominalBase.Stri(
                    prakriti = buildNominalBase(context.nominalBase()),
                    pratyaya = requireText(
                        value = context.striPratyaya().text,
                        description = "stri pratyaya",
                    ),
                )

            is VakyaParser.TaddhitaBaseContext ->
                ParsedNominalBase.Taddhita(
                    prakriti = buildNominalBase(context.nominalBase()),
                    pratyaya = requireText(
                        value = context.taddhitaPratyaya().text,
                        description = "taddhita pratyaya",
                    ),
                )

            is VakyaParser.SamasaBaseContext ->
                ParsedNominalBase.Samasa(
                    members = listOf(
                        buildNominalBase(context.nominalBase(0)),
                        buildNominalBase(context.nominalBase(1)),
                    ),
                    separator = context.COMPOUND_SEPARATOR().text,
                )

            else ->
                error("Unsupported nominal base structure: ${context.javaClass.simpleName}")
        }

    private fun buildSimplePratipadika(
        context: VakyaParser.SimplePratipadikaContext
    ): ParsedNominalBase.Simple = when {
        context.NUMERAL() != null -> ParsedNominalBase.Simple(context.NUMERAL().text, SimpleNominalKind.NUMERAL)
        context.RESULT_REFERENCE() != null -> ParsedNominalBase.Simple(context.RESULT_REFERENCE().text, SimpleNominalKind.RESULT_REFERENCE)
        context.IDENTIFIER() != null -> ParsedNominalBase.Simple(context.IDENTIFIER().text, SimpleNominalKind.IDENTIFIER)
        else -> error("Unsupported simple pratipadika structure.")
    }

    private fun buildKridanta(
        context: VakyaParser.KridantaPratipadikaContext
    ): ParsedNominalBase.Kridanta =
        ParsedNominalBase.Kridanta(
            upasargas = context.upasarga().map { it.text },
            dhatu = requireText(
                value = context.dhatu()?.text,
                description = "kridanta dhatu",
            ),
            vikarana = context.vikarana()?.text,
            pratyaya = requireText(
                value = context.krtPratyaya()?.text,
                description = "krt pratyaya",
            ),
        )

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
