package dev.panini.vyakaranam.analysis

import dev.panini.ashtadhyayi.adhyaya1.pada4.AdharoAdhikaranamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.DhruvamApayeApadanamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.KarakeSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.KarmanaYamAbhipraitiSampradanamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.KarturIpsitatamamKarmaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.SadhakatamamKaranamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.SvatantrahKartaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.TatPrayojakoHetusCaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.AnabhihiteSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.ApadanePancamiSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.ChaturthiSampradaneSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.KarmaniDvitiyaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.KartrKarmanohKrtiSutra
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

data class DhatuIdentity(
    val surface: String,
    val sakarmaka: Boolean = true,
)

data class KarakaRuleContext(
    val dhatu: DhatuIdentity,
    val participant: ParticipantFacts,
    val allParticipants: List<ParticipantFacts>,
    val prayoga: Prayoga,
    val candidates: Set<Karaka> = emptySet(),
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
        KarakeSutra,
        DhruvamApayeApadanamSutra,
        KarmanaYamAbhipraitiSampradanamSutra,
        SadhakatamamKaranamSutra,
        AdharoAdhikaranamSutra,
        KarturIpsitatamamKarmaSutra,
        SvatantrahKartaSutra,
        TatPrayojakoHetusCaSutra,
    ).sortedBy { it.krama }

    val vibhaktiRules: List<Sutra<VibhaktiRuleContext, VibhaktiRuleResult>> = listOf(
        AnabhihiteSutra,
        KarmaniDvitiyaSutra,
        ChaturthiSampradaneSutra,
        KartrkaranayostrtiyaSutra,
        ApadanePancamiSutra,
        SaptamyAdhikaraneCaSutra,
        KartrKarmanohKrtiSutra,
    ).sortedBy { it.krama }

    fun resolve(context: KarakaRuleContext): KarakaResolution {
        val possibleVibhaktis = context.participant.possibleVibhaktis
        val candidates = context.candidates.ifEmpty {
            context.participant.possibleVibhaktis.mapNotNull { vibhakti ->
                KarakaInference.infer(vibhakti, context.prayoga, context.dhatu.sakarmaka)
            }.toSet()
        }
        val semanticContext = context.copy(candidates = candidates)
        val semantic = karakaRules.firstOrNull { it.matches(semanticContext) }?.apply(semanticContext)
            as? KarakaRuleResult.Assigned
        val resolved = semantic?.karaka ?: candidates.singleOrNull()
        val evidence = buildList {
            semantic?.let { add(it.evidence) }
            resolved?.let { karaka ->
                val isAbhihita = when (context.prayoga) {
                    Prayoga.KARTARI -> karaka == Karaka.KARTR
                    Prayoga.KARMANI -> karaka == Karaka.KARMAN
                    Prayoga.CAUSATIVE -> karaka == Karaka.KARTR
                    Prayoga.BHAVE -> false
                    Prayoga.ANIRDHARITA -> false
                }
                val vibhaktiContext = VibhaktiRuleContext(karaka, possibleVibhaktis, abhihita = isAbhihita)
                val assignment = vibhaktiRules.firstOrNull { it.matches(vibhaktiContext) }
                    ?.apply(vibhaktiContext) as? VibhaktiRuleResult.Assigned
                assignment?.let { add(it.evidence) }
            }
        }
        return KarakaResolution(candidates, resolved, possibleVibhaktis, evidence)
    }
}
