package dev.panini.vyakaranam.ast

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha

/** Extensible programming meaning bound to a Sanskrit lexical meaning. */
data class MorphemeSemanticConcept(val id: String, val canonicalName: String) {
    companion object {
        val OUTCOME = MorphemeSemanticConcept("outcome", "परिणाम")
        val STATE = MorphemeSemanticConcept("state", "अवस्था")
    }
}

/** The kṛdanta sense in which a kṛt affix is prescribed. */
enum class KridantaArtha {
    BHAVA,
}

/** Extensible lexical meaning of the derived nominal, independent of any VM operation. */
@JvmInline
value class KridantaLexicalMeaning(val id: String) {
    init {
        require(id.isNotBlank()) { "A lexical meaning requires an identity." }
    }

    companion object {
        val RESULTING_CHANGE = KridantaLexicalMeaning("resulting-change")
        val SETTLED_STATE = KridantaLexicalMeaning("settled-state")
    }
}

/** Auditable grammatical authority for a semantic rule. */
data class MorphemeSemanticEvidence(
    val sutra: String,
    val text: String,
    val explanation: String,
)

/** Typed grammatical conditions; no rendered or fused word is matched here. */
data class MorphemeSemanticPattern(
    val upasargas: List<String>,
    val dhatuIds: Set<String>,
    val krtPratyaya: KrtPratyayaIdentity,
    val kridantaArtha: KridantaArtha,
) {
    init {
        require(dhatuIds.isNotEmpty()) { "A semantic rule requires at least one Dhātupāṭha identity." }
    }

    internal fun matches(
        pratipadika: KridantaPratipadika,
        dhatu: Dhatu,
    ): Boolean =
        pratipadika.upasargas == upasargas &&
            dhatu.id in dhatuIds &&
            pratipadika.krtPratyayaIdentity == krtPratyaya
}

/** A declarative lexical-semantic rule over a Pāṇinian derivational structure. */
data class MorphemeSemanticRule(
    val id: String,
    val pattern: MorphemeSemanticPattern,
    val lexicalMeaning: KridantaLexicalMeaning,
    val evidence: List<MorphemeSemanticEvidence>,
) {
    init {
        require(id.isNotBlank()) { "A semantic rule requires an identity." }
        require(evidence.isNotEmpty()) { "A semantic rule requires grammatical evidence." }
    }
}

/** Explicit boundary between Sanskrit lexical meaning and programming use. */
data class MorphemeProgrammingBinding(
    val lexicalMeaning: KridantaLexicalMeaning,
    val concept: MorphemeSemanticConcept,
)

/** Stable semantic identity independent of inflection and rendered surface spelling. */
data class MorphemeSemanticIdentity(
    val upasargas: List<String>,
    val dhatu: Dhatu,
    val krtPratyaya: KrtPratyayaIdentity,
    val kridantaArtha: KridantaArtha,
    val lexicalMeaning: KridantaLexicalMeaning,
    val taddhitaClasses: List<TaddhitaPratyayaClass>,
    val concept: MorphemeSemanticConcept,
    val canonicalName: String,
    val ruleId: String,
    val evidence: List<MorphemeSemanticEvidence>,
)

/**
 * Extensible resolver for morpheme meanings. Rules are data, so new vocabulary
 * does not require another branch in the AST or runtime.
 */
class MorphemeSemanticRegistry(
    rules: Iterable<MorphemeSemanticRule>,
    bindings: Iterable<MorphemeProgrammingBinding> = standardBindings,
) {
    private val rules = rules.toList().also { registered ->
        require(registered.map(MorphemeSemanticRule::id).distinct().size == registered.size) {
            "Morpheme semantic rule ids must be unique."
        }
    }
    private val bindings = bindings.toList().let { registered ->
        registered.associateBy(MorphemeProgrammingBinding::lexicalMeaning).also {
            require(it.size == registered.size) { "Each lexical meaning may have only one programming binding." }
        }
    }

    fun resolve(pratipadika: KridantaPratipadika): MorphemeSemanticIdentity? {
        val dhatu = resolveDhatu(pratipadika.dhatu.mulaDhatu) ?: return null
        val matches = rules.filter { it.pattern.matches(pratipadika, dhatu) }
        if (matches.isEmpty()) return null
        require(matches.size == 1) {
            "Ambiguous morpheme semantics for ${pratipadika.sourceText}: ${matches.joinToString { it.id }}"
        }
        val rule = matches.single()
        val binding = bindings[rule.lexicalMeaning] ?: return null
        return MorphemeSemanticIdentity(
            upasargas = pratipadika.upasargas,
            dhatu = dhatu,
            krtPratyaya = rule.pattern.krtPratyaya,
            kridantaArtha = rule.pattern.kridantaArtha,
            lexicalMeaning = rule.lexicalMeaning,
            taddhitaClasses = pratipadika.vikaras.filterIsInstance<TaddhitaVikara>()
                .mapNotNull(TaddhitaVikara::pratyayaClass),
            concept = binding.concept,
            canonicalName = binding.concept.canonicalName,
            ruleId = rule.id,
            evidence = rule.evidence,
        )
    }

    private fun resolveDhatu(source: String): Dhatu? = DhatuPatha.all
        .filter { it.sourceSurface == source }
        .maxByOrNull { it.preferredForSourceDerivation }

    companion object {
        val standardBindings = listOf(
            MorphemeProgrammingBinding(
                KridantaLexicalMeaning.RESULTING_CHANGE,
                MorphemeSemanticConcept.OUTCOME,
            ),
            MorphemeProgrammingBinding(
                KridantaLexicalMeaning.SETTLED_STATE,
                MorphemeSemanticConcept.STATE,
            ),
        )

        val standard = MorphemeSemanticRegistry(
            listOf(
                MorphemeSemanticRule(
                    id = "kridanta.pari-nam-ghan.outcome",
                    pattern = MorphemeSemanticPattern(
                        upasargas = listOf("परि"),
                        dhatuIds = setOf("01.1136"),
                        krtPratyaya = KrtPratyayaIdentity.GHAN,
                        kridantaArtha = KridantaArtha.BHAVA,
                    ),
                    lexicalMeaning = KridantaLexicalMeaning.RESULTING_CHANGE,
                    evidence = listOf(
                        MorphemeSemanticEvidence(
                            sutra = "3.3.18",
                            text = "भावे",
                            explanation = "घञ् forms an action or realized-state abstract in bhāva.",
                        ),
                    ),
                ),
                MorphemeSemanticRule(
                    id = "kridanta.ava-stha-ang.state",
                    pattern = MorphemeSemanticPattern(
                        upasargas = listOf("अव"),
                        dhatuIds = setOf("01.1077"),
                        krtPratyaya = KrtPratyayaIdentity.ANG,
                        kridantaArtha = KridantaArtha.BHAVA,
                    ),
                    lexicalMeaning = KridantaLexicalMeaning.SETTLED_STATE,
                    evidence = listOf(
                        MorphemeSemanticEvidence(
                            sutra = "3.3.104",
                            text = "षिद्भिदादिभ्योऽङ्",
                            explanation = "अङ् supplies the feminine action-noun derivation represented by the grammar.",
                        ),
                    ),
                ),
            ),
        )
    }
}

fun Pratipadika.morphemeSemanticIdentity(
    registry: MorphemeSemanticRegistry = MorphemeSemanticRegistry.standard,
): MorphemeSemanticIdentity? =
    (this as? KridantaPratipadika)?.let(registry::resolve)
