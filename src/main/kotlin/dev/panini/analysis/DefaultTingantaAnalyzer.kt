package dev.panini.analysis

import dev.panini.derivation.Lakara
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.parser.ast.ParsedTinganta

class DefaultTingantaAnalyzer(
    private val tingResolver: TingResolver = DefaultTingResolver(),
) : TingantaAnalyzer {

    override fun analyze(
        tinganta: ParsedTinganta,
    ): TingantaAnalysis {
        tinganta.unresolvedIdentifier?.let { identifier ->
            return TingantaAnalysis(
                dhatu = null,
                unresolvedDhatu = null,
                sanadiPratyayas = emptyList(),
                vikarana = null,
                lakara = null,
                ting = null,
                unresolvedIdentifier = identifier,
            )
        }

        val dhatuText = requireNotNull(tinganta.dhatu) {
            "A segmented tiṅanta requires a dhātu."
        }

        val lakaraText = requireNotNull(tinganta.lakara) {
            "A segmented tiṅanta requires a lakāra."
        }

        val lakara = Lakara.valueOf(lakaraText)
            ?: throw IllegalArgumentException(
                "Unknown lakāra: $lakaraText",
            )

        val resolvedDhatu = DhatuPatha.findOneByUpadesha(dhatuText)

        return TingantaAnalysis(
            dhatu = resolvedDhatu,
            unresolvedDhatu = dhatuText.takeIf {
                resolvedDhatu == null
            },
            sanadiPratyayas = tinganta.sanadiPratyayas,
            vikarana = tinganta.vikarana,
            lakara = lakara,
            ting = tinganta.tingPratyaya?.let(
                tingResolver::resolve,
            ),
        )
    }
}
