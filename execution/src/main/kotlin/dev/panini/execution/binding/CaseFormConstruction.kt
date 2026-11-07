package dev.panini.execution.binding

import dev.panini.analysis.DhatuArthaClass
import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.hasArtha
import dev.panini.analysis.PurposeActionGovernor
import dev.panini.analysis.SemanticRelation
import dev.panini.core.KrtAffix
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.AvyayaKridantaDerivation
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada

/**
 * Relations that the case together with the stem's class decides, for any root.
 * Reason (2.3.23, 2.3.25) is absent on purpose: दण्डेन and अध्ययनेन differ only in meaning.
 */
internal object CaseFormConstruction {
    private val accusativeGovernors = setOf("परितः", "अभितः", "सर्वतः", "उभयतः")
    private val dativeGovernors = setOf("नमः", "स्वाहा", "स्वधा", "वषट्")
    private val bhavaAffixes = setOf(KrtAffix.GHAN, KrtAffix.KTIN, KrtAffix.LYUT, KrtAffix.A, KrtAffix.YUC, KrtAffix.KI)
    private val bodyParts = setOf("अक्षि", "नेत्र", "नयन", "चक्षुस्", "पाद", "चरण", "पाणि", "कर", "हस्त", "कर्ण", "श्रोत्र", "पृष्ठ", "चर्मन्", "त्वच्")
    private val deformities = setOf("काण", "खञ्ज", "कुणि", "कुण्ठ", "कुष्ठ", "कुब्ज", "बधिर")
    /** Words that end in तर without being तरप् comparatives. */
    private val notComparatives = setOf("अन्तर", "उत्तर", "इतर", "अपर", "तर")

    fun infer(frame: KriyaFrame): Map<Int, Set<SemanticRelation>> {
        val nominals = frame.nominalRelations?.nominalPadas.orEmpty()
        fun pratipadika(index: Int) = (nominals[index].pada as? SubantaPada)?.pratipadika
        fun stem(index: Int) = (pratipadika(index) as? MulaPratipadika)?.text
        fun onlyCase(index: Int, case: Vibhakti) = nominals[index].supCandidates.isNotEmpty() &&
            nominals[index].supCandidates.all { it.vibhakti == case }
        // भ्याम् and भ्यस् do not show which case they are, so a rule defined by the root may claim them.
        fun fits(index: Int, case: Vibhakti) = nominals[index].supCandidates.any { it.vibhakti == case } &&
            nominals[index].supCandidates.none { it.vibhakti == Vibhakti.PRATHAMA || it.vibhakti == Vibhakti.DVITIYA }
        fun allowsCase(index: Int, case: Vibhakti) = nominals[index].supCandidates.any { it.vibhakti == case }
        val deformed = nominals.indices.any { stem(it) in deformities }
        val comparative = nominals.indices.any { index -> onlyCase(index, Vibhakti.PRATHAMA) &&
            stem(index)?.let { it.endsWith("तर") && it !in notComparatives } == true }
        // 2.3.41 मनुष्याणां क्षत्रियः शूरतमः: a plural genitive or locative beside a तम superlative.
        val superlative = nominals.indices.any { index -> onlyCase(index, Vibhakti.PRATHAMA) && stem(index)?.endsWith("तम") == true }
        val pada = frame.kriya?.analysis?.pada
        val roots = setOfNotNull(pada?.dhatu?.mulaDhatu, frame.kriya?.dhatu?.sourceSurface)
        val prefixes = pada?.upasargas.orEmpty()
        val root = frame.kriya?.dhatu
        fun rooted(vararg names: String) = names.any { it in roots }
        val causativeStem = dev.panini.core.SanadiAffix.NIC in pada?.dhatu?.sanadiAffixes.orEmpty()
        return buildMap {
            nominals.indices.forEach { index ->
                when {
                    // 2.3.23 अध्ययनेन वसति: an action noun is no tool, and a mere copula needs no means, so these name the reason.
                    onlyCase(index, Vibhakti.TRTIYA) && !root.hasArtha(DhatuArthaClass.DOING) && stem(index) !in VoiceRoleConstruction.timeWords && (
                        (pratipadika(index) as? KridantaPratipadika)?.krtAffix in bhavaAffixes ||
                            (root.hasArtha(DhatuArthaClass.BEING) && !deformed && stem(index) != null && nominals.indices.none { it != index && onlyCase(it, Vibhakti.TRTIYA) })) &&
                        nominals.indices.none { stem(it) in setOf("तुल्य", "सम", "समान", "सदृश", "न्यून", "अधिक") } && frame.prayoga == dev.panini.core.Prayoga.KARTARI ->
                        put(index, setOf(SemanticRelation.CAUSE_HETU))
                    // 1.4.24 with repetition, वृक्षाद् वृक्षं कूर्दन्ति: the place left in each leap.
                    fits(index, Vibhakti.PANCHAMI) && nominals.indices.any { it != index && stem(it) == stem(index) && onlyCase(it, Vibhakti.DVITIYA) } ->
                        put(index, setOf(SemanticRelation.SOURCE, SemanticRelation.SEPARATION_REFERENCE))
                    onlyCase(index, Vibhakti.DVITIYA) && nominals.indices.any { it != index && stem(it) == stem(index) && onlyCase(it, Vibhakti.PANCHAMI) } ->
                        put(index, setOf(SemanticRelation.DESIRED_OBJECT))
                    // Rules defined by the root and the case together: भयहेतु 1.4.25, रुचि 1.4.33, क्रोध 1.4.37, स्मरण 2.3.52.
                    fits(index, Vibhakti.PANCHAMI) && (root.hasArtha(DhatuArthaClass.FEAR) || rooted("त्रा", "त्रैङ्")) ->
                        put(index, setOf(SemanticRelation.FEAR_CAUSE, SemanticRelation.FEAR_PROTECTION_ACTION_SENSE))
                    fits(index, Vibhakti.CHATURTHI) && root.hasArtha(DhatuArthaClass.DELIGHT) ->
                        put(index, setOf(SemanticRelation.PLEASED_EXPERIENCER, SemanticRelation.RUCI_ACTION_SENSE))
                    fits(index, Vibhakti.CHATURTHI) && prefixes.isEmpty() && root.hasArtha(DhatuArthaClass.ANGER) ->
                        put(index, setOf(SemanticRelation.ANGER_TARGET, SemanticRelation.ANGER_ACTION_SENSE))
                    onlyCase(index, Vibhakti.SASTHI) && rooted("स्मृ") ->
                        put(index, setOf(SemanticRelation.REMEMBERING_SENSE, SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION))
                    // 1.4.29 उपाध्यायादधीते: the teacher one learns from.
                    fits(index, Vibhakti.PANCHAMI) && root.hasArtha(DhatuArthaClass.STUDY) -> put(index, setOf(SemanticRelation.FORMAL_INSTRUCTION_SOURCE))
                    // 1.4.24 पर्वतादवरोहति, 1.4.26 अध्ययनात् पराजयते, 1.4.27 यवेभ्यो गां वारयति, 1.4.30 गोमयाद् वृश्चिको जायते, 1.4.31 हिमवतो गङ्गा प्रभवति.
                    fits(index, Vibhakti.PANCHAMI) && "अव" in prefixes && rooted("रुह्", "रुहँ") ->
                        put(index, setOf(SemanticRelation.SOURCE, SemanticRelation.SEPARATION_REFERENCE))
                    fits(index, Vibhakti.PANCHAMI) && "परा" in prefixes && rooted("जि") -> put(index, setOf(SemanticRelation.UNBEARABLE_OBJECT))
                    fits(index, Vibhakti.PANCHAMI) && causativeStem && rooted("वृ", "वृञ्", "वृङ्") ->
                        put(index, setOf(SemanticRelation.PREVENTED_DESIRED_OBJECT))
                    onlyCase(index, Vibhakti.DVITIYA) && causativeStem && rooted("वृ", "वृञ्", "वृङ्") -> put(index, setOf(SemanticRelation.DESIRED_OBJECT))
                    fits(index, Vibhakti.PANCHAMI) && rooted("जन्", "जनँ", "जनी") -> put(index, setOf(SemanticRelation.BIRTH_MATERIAL_CAUSE))
                    fits(index, Vibhakti.PANCHAMI) && "प्र" in prefixes && rooted("भू", "भूँ") -> put(index, setOf(SemanticRelation.FIRST_MANIFESTATION_SOURCE))
                    // 1.4.35 देवदत्ताय शतं धारयति: the creditor, and the debt as the object.
                    fits(index, Vibhakti.CHATURTHI) && causativeStem && rooted("धृ", "धृञ्", "धृङ्") -> put(index, setOf(SemanticRelation.CREDITOR))
                    onlyCase(index, Vibhakti.DVITIYA) && causativeStem && rooted("धृ", "धृञ्", "धृङ्") -> put(index, setOf(SemanticRelation.DESIRED_OBJECT))
                    // 1.4.38 देवदत्तम् अभिक्रुध्यति: with an upasarga the target is karma.
                    onlyCase(index, Vibhakti.DVITIYA) && prefixes.isNotEmpty() && rooted("क्रुध्", "क्रुधँ", "द्रुह्", "द्रुहँ") ->
                        put(index, setOf(SemanticRelation.ANGER_TARGET, SemanticRelation.ANGER_ACTION_SENSE))
                    // Vārttika on 2.3.36: साधुर्देवदत्तो मातरि.
                    onlyCase(index, Vibhakti.SAPTAMI) && nominals.indices.any { stem(it) in setOf("साधु", "असाधु") } ->
                        put(index, setOf(SemanticRelation.SADHU_ASADHU_REFERENCE))
                    // 1.4.46, 1.4.48: ग्रामम् अधिशेते, ग्रामम् उपवसति. The locus is named as the object.
                    onlyCase(index, Vibhakti.DVITIYA) && ("अधि" in prefixes && rooted("शीङ्", "शी", "स्था", "ष्ठा", "आस्", "आसँ") ||
                        prefixes.any { it in setOf("उप", "अनु", "अधि", "आङ्") } && rooted("वस्", "वसँ")) ->
                        put(index, setOf(SemanticRelation.LOCATION))
                    superlative && nominals[index].supCandidates.isNotEmpty() && nominals[index].supCandidates.all {
                        (it.vibhakti == Vibhakti.SASTHI || it.vibhakti == Vibhakti.SAPTAMI) && it.vacana == Vacana.BAHUVACANA } ->
                        put(index, setOf(SemanticRelation.GROUP_SELECTION, SemanticRelation.NIRDHARANA_REFERENCE))
                    // 2.3.6 मासेन अधीते: a span of time or distance in the instrumental.
                    onlyCase(index, Vibhakti.TRTIYA) && (stem(index) in VoiceRoleConstruction.timeWords || stem(index) in VoiceRoleConstruction.distanceWords) ->
                        put(index, setOf(SemanticRelation.COMPLETION_LIMIT, SemanticRelation.CONTINUOUS_TIME_DISTANCE_EXTENT))
                    // 2.3.15 अध्ययनाय वसति: a bhāva noun in the dative stands for tumun.
                    fits(index, Vibhakti.CHATURTHI) && (pratipadika(index) as? KridantaPratipadika)?.krtAffix in bhavaAffixes ->
                        put(index, setOf(SemanticRelation.PURPOSE_ACTION, SemanticRelation.ACTION_NOUN_DENOTATION))
                    // 2.3.20 अक्ष्णा काणः: the body part that shows the deformity.
                    deformed && onlyCase(index, Vibhakti.TRTIYA) && stem(index) in bodyParts ->
                        put(index, setOf(SemanticRelation.BODY_DEFORMITY))
                    // 2.3.42 माथुराः पाटलीपुत्रकेभ्यः आढ्यतराः: the group a comparative separates from.
                    comparative && allowsCase(index, Vibhakti.PANCHAMI) -> put(index, setOf(SemanticRelation.COMPARATIVE_DISTINCTION,
                        SemanticRelation.NIRDHARANA_REFERENCE, SemanticRelation.SEPARATE_COMPARISON_GROUPS))
                }
            }
        }
    }

    /**
     * Upapada governance, from the case beside the written avyaya: रामेण सह (2.3.19), रामेण विना (2.3.32),
     * and ग्रामं परितः (vārttika on 2.3.2). The nearest word in the governed case is taken, so the two need not touch.
     */
    fun upapadaDependents(sentence: AkhyataVakya, frame: KriyaFrame): Map<Int, Set<SemanticRelation>> {
        val nominals = frame.nominalRelations?.nominalPadas.orEmpty()
        return buildMap {
            sentence.padas.forEachIndexed { position, pada ->
                val form = (pada as? AvyayaPada)?.form ?: return@forEachIndexed
                val neighbours = sentence.padas.indices.sortedBy { kotlin.math.abs(it - position) }.map { sentence.padas[it] }
                    .mapNotNull { near -> nominals.indexOfFirst { it.pada === near }.takeIf { it >= 0 } }
                fun inCase(vararg cases: Vibhakti) = neighbours.firstOrNull { index -> nominals[index].supCandidates.isNotEmpty() &&
                    nominals[index].supCandidates.all { it.vibhakti in cases } }
                when (form) {
                    "सह" -> inCase(Vibhakti.TRTIYA)?.let { put(it, setOf(SemanticRelation.ACCOMPANIMENT, SemanticRelation.NON_PRINCIPAL_ACCOMPANIMENT)) }
                    "विना", "पृथक्", "नाना" -> inCase(Vibhakti.TRTIYA, Vibhakti.PANCHAMI, Vibhakti.DVITIYA)?.let {
                        put(it, setOf(SemanticRelation.EXCLUSION_VINA, SemanticRelation.PRTHAG_VINA_NANA_LINK)) }
                    "परितः", "अभितः", "सर्वतः", "उभयतः" -> inCase(Vibhakti.DVITIYA)?.let { put(it, emptySet()) }
                    "नमः", "स्वाहा", "स्वधा", "वषट्" -> inCase(Vibhakti.CHATURTHI)?.let { put(it, emptySet()) }
                }
            }
        }
    }

    /** The written avyaya governing the accusative beside it, for the vārttika उभसर्वतसोः कार्या. */
    fun avyayaGovernors(sentence: AkhyataVakya, frame: KriyaFrame): Map<Int, AvyayaPada> {
        val nominals = frame.nominalRelations?.nominalPadas.orEmpty()
        return buildMap {
            sentence.padas.forEachIndexed { position, pada ->
                val avyaya = (pada as? AvyayaPada)?.takeIf { it.form in dativeGovernors || it.form in accusativeGovernors } ?: return@forEachIndexed
                val case = if (avyaya.form in dativeGovernors) Vibhakti.CHATURTHI else Vibhakti.DVITIYA
                sentence.padas.indices.sortedBy { kotlin.math.abs(it - position) }.map { sentence.padas[it] }
                    .mapNotNull { near -> nominals.indexOfFirst { it.pada === near }.takeIf { it >= 0 } }
                    .firstOrNull { index -> nominals[index].supCandidates.isNotEmpty() && nominals[index].supCandidates.all { it.vibhakti == case } }
                    ?.let { put(it, avyaya) }
            }
        }
    }

    /**
     * Which accusative belongs to a non-finite action. Word order is free, so nothing requires adjacency: the nearest
     * candidate belongs to it if it stands nearer that action than the finite verb, or if another candidate is left for the verb.
     */
    fun ownedBy(padas: List<dev.panini.vyakaranam.ast.Pada>, candidates: List<Pair<Int, dev.panini.vyakaranam.ast.Pada>>,
        action: dev.panini.vyakaranam.ast.Pada, finite: dev.panini.vyakaranam.ast.Pada?): Int? {
        val at = padas.indexOfFirst { it === action }
        val verb = padas.indexOfFirst { it === finite }
        if (at < 0) return null
        fun position(pada: dev.panini.vyakaranam.ast.Pada) = padas.indexOfFirst { it === pada }
        val nearest = candidates.minByOrNull { kotlin.math.abs(position(it.second) - at) } ?: return null
        val nearer = verb < 0 || kotlin.math.abs(position(nearest.second) - at) < kotlin.math.abs(position(nearest.second) - verb)
        return nearest.first.takeIf { candidates.size > 1 || nearer }
    }

    /** शास्त्रं पठितुं गच्छामि: the accusative written directly before tumun belongs to it. */
    fun infinitiveObjects(sentence: AkhyataVakya, frame: KriyaFrame): Map<Int, PurposeActionGovernor> {
        val nominals = frame.nominalRelations?.nominalPadas.orEmpty()
        return sentence.padas.withIndex().mapNotNull { (position, pada) ->
            val infinitive = pada as? AvyayaPada ?: return@mapNotNull null
            if ((infinitive.derivation as? AvyayaKridantaDerivation)?.krtAffix != KrtAffix.TUMUN) return@mapNotNull null
            val accusatives = nominals.indices.filter { nominals[it].supCandidates.isNotEmpty() && nominals[it].supCandidates.all { sup -> sup.vibhakti == Vibhakti.DVITIYA } }
            val index = ownedBy(sentence.padas, accusatives.map { it to nominals[it].pada }, infinitive, sentence.tinganta) ?: return@mapNotNull null
            index to PurposeActionGovernor.Written("tumun-$position", infinitive)
        }.toMap()
    }
}
