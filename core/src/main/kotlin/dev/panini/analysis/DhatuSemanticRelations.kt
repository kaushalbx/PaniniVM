package dev.panini.analysis

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha

/** Represents semantic classification, synonymy, antonymy, and derivational relations for Dhātus. */
object DhatuSemanticRelations {

    val synonymGroups: Map<SemanticRelation, List<List<String>>> = mapOf(
        SemanticRelation.EXISTENCE to listOf(
            listOf("भू", "अस्", "विद्")
        ),
        SemanticRelation.MOTION to listOf(
            listOf("गम्", "या", "व्रज्", "चर्", "सृ")
        ),
        SemanticRelation.SPEECH to listOf(
            listOf("वद्", "ब्रू", "भाष्", "कथ्", "वच्")
        ),
        SemanticRelation.COGNITION to listOf(
            listOf("ज्ञा", "बुध्", "विद्", "मन्")
        ),
        SemanticRelation.CONSUMPTION to listOf(
            listOf("भुज्", "खाद्", "अश्")
        ),
        SemanticRelation.CREATION to listOf(
            listOf("कृ", "जन्", "रच्")
        ),
        SemanticRelation.DESTRUCTION to listOf(
            listOf("हन्", "नश्", "मृ")
        ),
        SemanticRelation.GIVING to listOf(
            listOf("दा", "यच्छ्")
        ),
        SemanticRelation.RECEIVING to listOf(
            listOf("ग्रह्", "आदा")
        ),
        SemanticRelation.SEEING_PERCEPTION to listOf(
            listOf("दृश्", "पश्")
        ),
        SemanticRelation.FEAR_EMOTION to listOf(
            listOf("भी", "त्रस्")
        ),
        SemanticRelation.ANGER_EMOTION to listOf(
            listOf("क्रुध्", "द्रुह्", "ईर्ष्य")
        ),
    )

    val antonymPairs: List<Pair<String, String>> = listOf(
        Pair("गम्", "आगम्"),
        Pair("दा", "ग्रह्"),
        Pair("जन्", "मृ"),
        Pair("अस्", "नश्"),
    )

    /** Returns all synonyms for a given dhātu root surface. */
    fun getSynonyms(root: String): List<String> {
        val synonyms = mutableSetOf<String>()
        synonymGroups.values.forEach { groupList ->
            groupList.forEach { group ->
                if (root in group) {
                    synonyms.addAll(group - root)
                }
            }
        }
        return synonyms.toList()
    }

    /** Returns all antonyms for a given dhātu root surface. */
    fun getAntonyms(root: String): List<String> {
        val antonyms = mutableSetOf<String>()
        antonymPairs.forEach { (r1, r2) ->
            if (r1 == root) antonyms.add(r2)
            if (r2 == root) antonyms.add(r1)
        }
        return antonyms.toList()
    }

    /** Returns all dhātu root surfaces matching a specific semantic category. */
    fun getByCategory(category: SemanticRelation): List<String> {
        return synonymGroups[category]?.flatten()?.distinct() ?: emptyList()
    }

    /** Returns all semantic category relations associated with a root. */
    fun getCategoriesFor(root: String): Set<SemanticRelation> {
        val categories = mutableSetOf<SemanticRelation>()
        synonymGroups.forEach { (category, groupList) ->
            if (groupList.any { root in it }) {
                categories.add(category)
            }
        }
        return categories
    }
}
