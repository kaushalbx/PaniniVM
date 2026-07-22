package dev.panini.vyakaranam.analysis

import dev.panini.ashtadhyayi.adhyaya1.pada4.DhruvamApayeApadanamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.KarmanaYamAbhipraitiSampradanamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.SadhakatamamKaranamSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.ApadanePancamiSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.ChaturthiSampradaneSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.KarmaniDvitiyaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.KartrkaranayostrtiyaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.SaptamyAdhikaraneCaSutra
import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra

data class KarakaEvidence(
    val sutra: String,
    val text: String,
    val reason: String,
)

data class KarakaResolution(
    val candidates: Set<Karaka>,
    val resolved: Karaka?,
    val possibleVibhaktis: Set<Vibhakti>,
    val evidence: List<KarakaEvidence>,
)

data class KarakaRuleContext(
    val dhatuSurface: String,
    val prayoga: Prayoga,
    val supUpadesha: String,
    val sakarmaka: Boolean = true,
    val candidates: Set<Karaka> = emptySet(),
    val semanticRelations: Set<SemanticRelation> = emptySet(),
)

sealed interface KarakaRuleResult {
    data class Assigned(val karaka: Karaka, val evidence: KarakaEvidence) : KarakaRuleResult
}

data class VibhaktiRuleContext(
    val karaka: Karaka,
    val morphologicalCandidates: Set<Vibhakti>,
    val abhihita: Boolean = false,
) {
    fun accepts(expectedKaraka: Karaka, vibhakti: Vibhakti): Boolean =
        !abhihita && karaka == expectedKaraka && vibhakti in morphologicalCandidates
}

sealed interface VibhaktiRuleResult {
    data class Assigned(val vibhakti: Vibhakti, val evidence: KarakaEvidence) : VibhaktiRuleResult
}

/** Semantic kāraka assignment (1.4) followed by nominal-case validation (2.3). */
object KarakaRuleEngine {
    val karakaRules: List<Sutra<KarakaRuleContext, KarakaRuleResult>> = listOf(
        DhruvamApayeApadanamSutra,
        KarmanaYamAbhipraitiSampradanamSutra,
        SadhakatamamKaranamSutra,
    ).sortedBy { it.krama }

    val vibhaktiRules: List<Sutra<VibhaktiRuleContext, VibhaktiRuleResult>> = listOf(
        KarmaniDvitiyaSutra,
        ChaturthiSampradaneSutra,
        KartrkaranayostrtiyaSutra,
        ApadanePancamiSutra,
        SaptamyAdhikaraneCaSutra,
    ).sortedBy { it.krama }

    fun resolve(context: KarakaRuleContext): KarakaResolution {
        val possibleVibhaktis = dev.panini.core.SupAffix.candidates(context.supUpadesha)
            .mapTo(mutableSetOf()) { it.vibhakti }
        val candidates = KarakaInference.candidates(context.supUpadesha, context.prayoga, context.sakarmaka)
        val semanticContext = context.copy(
            candidates = candidates,
            semanticRelations = DhatuKarakaProfiles.forSurface(context.dhatuSurface)?.relations.orEmpty(),
        )
        val semantic = karakaRules.firstOrNull { it.matches(semanticContext) }?.apply(semanticContext)
            as? KarakaRuleResult.Assigned
        val resolved = semantic?.karaka ?: candidates.singleOrNull()
        val evidence = buildList {
            semantic?.let { add(it.evidence) }
            resolved?.let { karaka ->
                val vibhaktiContext = VibhaktiRuleContext(karaka, possibleVibhaktis)
                val assignment = vibhaktiRules.firstOrNull { it.matches(vibhaktiContext) }
                    ?.apply(vibhaktiContext) as? VibhaktiRuleResult.Assigned
                assignment?.let { add(it.evidence) }
            }
        }
        return KarakaResolution(candidates, resolved, possibleVibhaktis, evidence)
    }

}
