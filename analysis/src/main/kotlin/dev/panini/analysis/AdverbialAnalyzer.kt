package dev.panini.analysis

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti

enum class AdverbialMorphology { INDECLINABLE, CASE_FORM }
enum class AdverbialFunction { MODIFICATION, QUESTION, NEGATION, PROHIBITION, RELATIVE, CORRELATIVE, DISTRIBUTION }
enum class AdverbialScope { EVENT, PROPOSITION, CONSTITUENT, CLAUSE_RELATION, COLLECTION_MEMBERS }

data class AdverbialSemantics(
    val dimension: KriyaQualificationKind,
    val function: AdverbialFunction = AdverbialFunction.MODIFICATION,
    val value: String,
    /** These are scope possibilities, not an executable choice. */
    val scopes: Set<AdverbialScope> = setOf(AdverbialScope.EVENT),
    val repetitionCount: Long? = null,
)

/** Lexically licensed hypotheses; no arbitrary -am form becomes an adverb. */
object AdverbialAnalyzer {
    private fun meaning(kind: KriyaQualificationKind, value: String,
        function: AdverbialFunction = AdverbialFunction.MODIFICATION,
        scopes: Set<AdverbialScope> = setOf(AdverbialScope.EVENT)) =
        AdverbialSemantics(kind, function, value, scopes)

    private val lexicon = buildMap<String, AdverbialSemantics> {
        fun entries(words: List<String>, kind: KriyaQualificationKind) = words.forEach { put(it, meaning(kind, it)) }
        entries(listOf("शीघ्रम्", "द्रुतम्", "मन्दम्", "शनैः", "सहसा", "वृथा"), KriyaQualificationKind.MANNER)
        entries(listOf("अधुना", "इदानीम्", "ह्यः", "श्वः"), KriyaQualificationKind.TIME)
        entries(listOf("सदा", "सर्वदा", "पुनः", "पुनर्", "वारम्", "सकृत्"), KriyaQualificationKind.FREQUENCY)
        entries(listOf("अत्र", "तत्र", "सर्वत्र", "अन्यत्र", "उपरि", "अधः"), KriyaQualificationKind.PLACE)
        entries(listOf("अल्पम्", "किञ्चित्", "पर्याप्तम्", "बहु", "अतिशयम्"), KriyaQualificationKind.DEGREE)
        entries(listOf("भृशम्", "अत्यन्तम्"), KriyaQualificationKind.INTENSITY)
        // Degree can qualify an event or another constituent. Word identity
        // does not establish which adjective, adverb, or predicate it scopes over.
        listOf("अल्पम्", "किञ्चित्", "पर्याप्तम्", "बहु", "अतिशयम्", "भृशम्", "अत्यन्तम्").forEach { word ->
            put(word, getValue(word).copy(scopes = setOf(AdverbialScope.EVENT, AdverbialScope.CONSTITUENT)))
        }
        entries(listOf("कृपया"), KriyaQualificationKind.COURTESY)
        listOf("कदा" to KriyaQualificationKind.TIME, "कुत्र" to KriyaQualificationKind.PLACE,
            "कथम्" to KriyaQualificationKind.MANNER).forEach { (word, kind) ->
            put(word, meaning(kind, word, AdverbialFunction.QUESTION, setOf(AdverbialScope.PROPOSITION)))
        }
        listOf("न", "नहि", "मा").forEach { word -> put(word, meaning(
            KriyaQualificationKind.NEGATION, word,
            if (word == "मा") AdverbialFunction.PROHIBITION else AdverbialFunction.NEGATION,
            setOf(AdverbialScope.PROPOSITION, AdverbialScope.CONSTITUENT),
        )) }
        listOf("यदा", "तदा", "यावत्", "तावत्").forEach { word -> put(word, meaning(
            if (word in setOf("यावत्", "तावत्")) KriyaQualificationKind.TEMPORAL_EXTENT else KriyaQualificationKind.TIME,
            word, if (word.startsWith("य")) AdverbialFunction.RELATIVE else AdverbialFunction.CORRELATIVE,
            setOf(AdverbialScope.CLAUSE_RELATION),
        )) }
        put("यत्र", meaning(KriyaQualificationKind.PLACE, "यत्र", AdverbialFunction.RELATIVE,
            setOf(AdverbialScope.CLAUSE_RELATION)))
        // तत्र also has an independent deictic reading. Retain that reading
        // until a clause-level resolver supplies evidence for a correlation.
        put("प्रत्येकम्", meaning(KriyaQualificationKind.DISTRIBUTION, "प्रत्येकम्",
            AdverbialFunction.DISTRIBUTION, setOf(AdverbialScope.COLLECTION_MEMBERS)))
    }

    // Lexical licensing is separate from inflection: appending म् cannot derive
    // the accusative of every stem, and word identity alone does not license it.
    private val caseFormLexicon = mapOf(
        "शीघ्र" to "शीघ्रम्", "द्रुत" to "द्रुतम्", "मन्द" to "मन्दम्",
        "अल्प" to "अल्पम्", "किञ्चित्" to "किञ्चित्", "पर्याप्त" to "पर्याप्तम्",
        "बहु" to "बहु", "अतिशय" to "अतिशयम्", "भृश" to "भृशम्",
        "अत्यन्त" to "अत्यन्तम्",
    ).mapValues { (_, form) -> lexicon.getValue(form) }

    fun licensesCaseForm(stem: String?): Boolean = stem in caseFormLexicon

    fun analyze(kriyaId: KriyaId, analyses: List<PadaAnalysis>): List<KriyaQualification> =
        analyses.mapIndexedNotNull { index, analysis ->
            when (analysis) {
                is AnalyzedAvyaya -> {
                    val word = analysis.pada.form
                    val count = CountingFrequencyExpressions.count(analysis.pada)
                    val semantics = if (count != null) meaning(KriyaQualificationKind.FREQUENCY, word)
                        .copy(repetitionCount = count) else lexicon[word]
                    // Preserve unknown avyayas without assigning an invented meaning.
                    KriyaQualification(kriyaId, analysis, semantics?.dimension ?: KriyaQualificationKind.OTHER,
                        word, index, semantics)
                }
                is AnalyzedSubanta -> {
                    val sub = analysis.analysis
                    val stem = sub.lexicalEntry?.text ?: ((sub.pada as? dev.panini.vyakaranam.ast.SubantaPada)?.pratipadika
                        as? dev.panini.vyakaranam.ast.MulaPratipadika)?.text ?: return@mapIndexedNotNull null
                    val semantics = caseFormLexicon[stem] ?: return@mapIndexedNotNull null
                    if ((sub.linga.isNotEmpty() && Linga.NAPUMSAKA !in sub.linga) || sub.supCandidates.none {
                        it.vibhakti == Vibhakti.DVITIYA && it.vacana == Vacana.EKAVACANA
                    }) return@mapIndexedNotNull null
                    KriyaQualification(kriyaId, analysis, semantics.dimension, stem, index, semantics,
                        AdverbialMorphology.CASE_FORM, competingNominalReading = true)
                }
                else -> null
            }
        }
}
