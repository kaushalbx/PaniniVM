package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class LocusStructuralIdentityTest {
    @Test fun `locus designations require action locus meaning independently of morphology`() {
        data class Construction(val root: String, val prefixes: List<String>, val rule: String)
        for ((root, prefixes, rule) in listOf(
            Construction("पच्", emptyList(), "1.4.45"),
            Construction("स्था", listOf("अधि"), "1.4.46"),
            Construction("विश्", listOf("अभि", "नि"), "1.4.47"),
            Construction("वस्", listOf("उप"), "1.4.48"))) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI, Prayoga.BHAVE))
                for (source in SemanticEvidenceSource.entries) {
                val participant = locus.copy(semanticEvidence = setOf(SemanticRelationEvidence(
                    SemanticRelation.LOCATION, source, "Proposed action locus.")))
                val verb = TingantaPada("verb", prefixes, DhatuPrakriti(root, root), Lakara.LAT,
                    TingPratyaya("तिप्", "तिप्"))
                val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), participant,
                    listOf(participant), voice, verbNode = verb))
                val established = source in setOf(SemanticEvidenceSource.CLAUSE_CONTEXT, SemanticEvidenceSource.LEXICAL_ENTRY)
                assertEquals(established, result.evidence.any { it.sutra == rule }, "$rule $voice $source")
            }
        }
    }

    @Test fun `conditioned locus readings cannot be established by morphological or valency hypotheses`() {
        data class Construction(val root: String, val prefixes: List<String>, val meaning: SemanticRelation, val rule: String)
        for ((root, prefixes, meaning, specialRule) in listOf(
            Construction("विश्", listOf("अभि", "नि"), SemanticRelation.ABHINIVESH_LOCATIVE_USAGE, "1.4.47"),
            Construction("वस्", listOf("उप"), SemanticRelation.FASTING_SENSE, "1.4.48"),
        )) {
            for (source in SemanticEvidenceSource.entries) {
                val participant = locus.copy(semanticRelations = locus.semanticRelations + meaning,
                    semanticEvidence = setOf(SemanticRelationEvidence(meaning, source, "Conditioned reading premise.")))
                val verb = TingantaPada("verb", prefixes,
                    DhatuPrakriti(root, root), Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
                val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), participant,
                    listOf(participant), Prayoga.KARTARI, verbNode = verb))
                val established = source in setOf(SemanticEvidenceSource.CLAUSE_CONTEXT, SemanticEvidenceSource.LEXICAL_ENTRY)
                assertEquals(if (established) listOf("1.4.45", "2.3.36") else listOf(specialRule),
                    result.evidence.map { it.sutra }, "$meaning $source")
                assertEquals(if (established) Vibhakti.SAPTAMI else null, result.resolvedVibhakti)
            }
        }
    }

    private val locus = ParticipantFacts("locus", AvyayaPada("locus", "locus"), setOf(Vibhakti.SAPTAMI),
        semanticRelations = setOf(SemanticRelation.LOCATION))

    @Test fun `flat spellings cannot activate special locus rules or suppress the general designation`() {
        for (root in listOf("अभिनिविश", "अभिनिविशकल्पित", "अधिशी", "उपवसकल्पित")) {
            val context = KarakaRuleContext(DhatuIdentity(root), locus, listOf(locus), Prayoga.KARTARI)
            val result = KarakaRuleEngine.resolve(context)
            assertEquals(listOf("1.4.45", "2.3.36"), result.evidence.map { it.sutra }, root)
            assertEquals(Karaka.ADHIKARANA, result.resolved, root)
        }
    }

    @Test fun `abhi ni vish requires structured root and ordered prefixes`() {
        val rule = dev.panini.ashtadhyayi.adhyaya1.pada4.AbhinivishasCaSutra
        for ((prefixes, expected) in listOf(listOf("अभि", "नि") to true, listOf("नि", "अभि") to false,
            listOf("अभि", "unknown") to false, emptyList<String>() to false)) {
            val verb = TingantaPada("verb", prefixes, DhatuPrakriti("विश्", "विश्"), Lakara.LAT,
                TingPratyaya("ते", "त"))
            assertEquals(expected, rule.matches(KarakaRuleContext(DhatuIdentity("अभिनिविश"), locus,
                listOf(locus), Prayoga.KARTARI, verbNode = verb)), prefixes.toString())
        }
    }
}
