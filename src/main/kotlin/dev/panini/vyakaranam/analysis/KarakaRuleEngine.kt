package dev.panini.vyakaranam.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti

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
)

/** Semantic kāraka assignment (1.4) followed by nominal-case validation (2.3). */
object KarakaRuleEngine {
    fun resolve(context: KarakaRuleContext): KarakaResolution {
        val possibleVibhaktis = dev.panini.core.SupAffix.candidates(context.supUpadesha)
            .mapTo(mutableSetOf()) { it.vibhakti }
        val candidates = KarakaInference.candidates(context.supUpadesha, context.prayoga, context.sakarmaka)
        val semantic = semanticRule(context.dhatuSurface, candidates)
        val resolved = semantic?.karaka ?: candidates.singleOrNull()
        val evidence = buildList {
            semantic?.let { add(it.evidence) }
            resolved?.let { karaka ->
                VibhaktiKarakaRules.forKaraka(karaka)?.let { rule ->
                    if (rule.vibhakti in possibleVibhaktis) {
                        add(KarakaEvidence(rule.sutra, rule.text, "${rule.vibhakti} realizes $karaka in this construction."))
                    }
                }
            }
        }
        return KarakaResolution(candidates, resolved, possibleVibhaktis, evidence)
    }

    private fun semanticRule(surface: String, candidates: Set<Karaka>): SemanticDecision? {
        val relations = DhatuKarakaProfiles.forSurface(surface)?.relations.orEmpty()
        return listOf(
            SemanticRelation.RECIPIENT to KarmanaYamAbhipraitiSampradanam,
            SemanticRelation.INSTRUMENT to SadhakatamamKaranam,
            SemanticRelation.SOURCE to DhruvamApayeApadanam,
        ).firstNotNullOfOrNull { (relation, rule) ->
            rule.takeIf { relation in relations }?.decide(candidates)
        }
    }
}

private data class SemanticDecision(val karaka: Karaka, val evidence: KarakaEvidence)

private interface KarakaSemanticRule {
    val sutra: String
    val text: String
    val karaka: Karaka
    fun decide(candidates: Set<Karaka>): SemanticDecision? = karaka.takeIf { it in candidates }?.let {
        SemanticDecision(it, KarakaEvidence(sutra, text, reason()))
    }
    fun reason(): String
}

private object DhruvamApayeApadanam : KarakaSemanticRule {
    override val sutra = "1.4.24"
    override val text = "ध्रुवमपायेऽपादानम्"
    override val karaka = Karaka.APADANA
    override fun reason() = "The participant is the fixed point from which separation occurs."
}

private object KarmanaYamAbhipraitiSampradanam : KarakaSemanticRule {
    override val sutra = "1.4.32"
    override val text = "कर्मणा यमभिप्रैति स सम्प्रदानम्"
    override val karaka = Karaka.SAMPRADANA
    override fun reason() = "The participant is the intended recipient of the object."
}

private object SadhakatamamKaranam : KarakaSemanticRule {
    override val sutra = "1.4.42"
    override val text = "साधकतमं करणम्"
    override val karaka = Karaka.KARANA
    override fun reason() = "The participant is construed as the most effective instrument."
}

private data class VibhaktiKarakaRule(
    val sutra: String,
    val text: String,
    val karaka: Karaka,
    val vibhakti: Vibhakti,
)

private object VibhaktiKarakaRules {
    private val rules = listOf(
        VibhaktiKarakaRule("2.3.2", "कर्मणि द्वितीया", Karaka.KARMAN, Vibhakti.DVITIYA),
        VibhaktiKarakaRule("2.3.13", "चतुर्थी सम्प्रदाने", Karaka.SAMPRADANA, Vibhakti.CHATURTHI),
        VibhaktiKarakaRule("2.3.18", "कर्तृकरणयोस्तृतीया", Karaka.KARANA, Vibhakti.TRTIYA),
        VibhaktiKarakaRule("2.3.18", "कर्तृकरणयोस्तृतीया", Karaka.KARTR, Vibhakti.TRTIYA),
        VibhaktiKarakaRule("2.3.28", "अपादाने पञ्चमी", Karaka.APADANA, Vibhakti.PANCHAMI),
        VibhaktiKarakaRule("2.3.36", "सप्तम्यधिकरणे च", Karaka.ADHIKARANA, Vibhakti.SAPTAMI),
    )

    fun forKaraka(karaka: Karaka): VibhaktiKarakaRule? = rules.firstOrNull { it.karaka == karaka }
}
