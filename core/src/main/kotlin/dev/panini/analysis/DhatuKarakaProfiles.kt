package dev.panini.analysis

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha

enum class SemanticRelation {
    RECIPIENT,
    /** Participant against whom anger/hostility is directed, inherited by 1.4.37–38. */
    ANGER_TARGET,
    /** Experiencer pleased by another participant's action in the ruc construction (1.4.33). */
    PLEASED_EXPERIENCER,
    /** Īpsitamātra: object of longing under 1.4.36, distinct from īpsitatama karman. */
    LONGED_FOR_OBJECT,
    /** Uttamarṇa: creditor, rather than a generic recipient, in 1.4.35. */
    CREDITOR,
    /** Person intended to be informed by the act, under 1.4.34. */
    INTENDED_TO_BE_INFORMED,
    /** Agent of the preceding prompting action, inherited by 1.4.40–41. */
    PRIOR_ACTION_AGENT,
    /** Selected most effective means in this reading; a tool/category/case alone is a hypothesis. */
    INSTRUMENT,
    /** Asattvavacana: a property/degree rather than a substance, independently selected. */
    NON_SUBSTANCE_MEANING,
    /** Paryāpti/sāmarthya reading, required for alam and its supported synonyms (2.3.16). */
    SUFFICIENCY,
    /** Payment/means in fixed-term hiring (parikraya), not outright purchase. */
    FIXED_TERM_HIRING_PAYMENT,
    SOURCE,
    /** Avadhi relative to the separation event; need not be physically motionless (1.4.24). */
    SEPARATION_REFERENCE,
    /** Bhayahetu: feared danger, including that from which protection is sought (1.4.25). */
    FEAR_CAUSE,
    /** Asoḍha: that which cannot be endured in the para-ji construction (1.4.26). */
    UNBEARABLE_OBJECT,
    /** Observer from whom the agent intends to conceal themself (1.4.28). */
    CONCEALMENT_OBSERVER,
    /** Antardhi: self-concealment/interposition event, independently supplied for 1.4.28. */
    SELF_CONCEALMENT,
    /** Object desired by the restrained participant, from which they are warded off (1.4.27). */
    PREVENTED_DESIRED_OBJECT,
    /** Place at which a participant first becomes manifest, not any location (1.4.31). */
    FIRST_MANIFESTATION_SOURCE,
    /** Upādāna cause of the entity coming into being (1.4.30). */
    BIRTH_MATERIAL_CAUSE,
    /** Sahakārin cause under Kāśikā/Nyāsa's broader interpretation of 1.4.30. */
    BIRTH_ASSISTING_CAUSE,
    /** Teacher/source in niyamapūrvaka instruction (upayoga), required by 1.4.29. */
    FORMAL_INSTRUCTION_SOURCE,
    LOCATION,
    /** Explicitly established locative usage in abhiniviś's vyavasthita-vibhāṣā domain (1.4.47). */
    ABHINIVESH_LOCATIVE_USAGE,
    /** Vas construed as abstaining from food, excluded by the vārttika on 1.4.48. */
    FASTING_SENSE,
    DESIRED_OBJECT,
    INDEPENDENT_AGENT,
    PROMPTER_CAUSE,
    /** Agent of the non-causative action, construed under ṇic as prayojya. */
    CAUSEE_AGENT,
    /** Agent of an already causative action, not the aṇi-kartṛ required by 1.4.52–53. */
    PRIOR_CAUSATIVE_AGENT,
    /** Abhi-vad construed as salutation, not generic speech, in the vārttika on 1.4.53. */
    ABHIVAD_SALUTATION_SENSE,
    /** Bhakṣ construed in the injurious sense licensed by the 1.4.52 exception. */
    BHAKS_INJURIOUS_SENSE,
    /** Non-injurious eating sense of bhakṣ, excluded from 1.4.52 causee-karman. */
    BHAKS_NONINJURIOUS_SENSE,
    /** Explicit niyantṛ-kartṛka vah construction, licensing 1.4.52's causee-karman. */
    VAH_NIYANTR_KARTRKA,
    /** Explicit aniyantṛ-kartṛka vah construction, covered by the prohibition. */
    VAH_ANIYANTR_KARTRKA,
    /** Anīpsita, connected like the desired object (1.4.50); an occurrence reading. */
    INDIFFERENT_OBJECT,
    /** Gauṇa-karman/akathita participant of a dvikarmaka construction. */
    SECONDARY_OBJECT,
    /** Independently established action sense admitting an akathita argument; generic speech or two accusatives do not establish this valency. */
    DVIKARMAKA_ACTION_SENSE,
    /** Independently established meaning-class membership for the -arthānām rules 1.4.25, 27, 33 and 37; generic categories do not establish these. */
    FEAR_PROTECTION_ACTION_SENSE,
    VARANA_ACTION_SENSE,
    RUCI_ACTION_SENSE,
    ANGER_ACTION_SENSE,
    /** Two-object construction where primary versus akathita status is unresolved. */
    AMBIGUOUS_DVIKARMAKA_OBJECT,
    ACCOMPANIMENT,
    /** Identifying-mark relation already incorporated in a compound (excluded by 2.3.21). */
    MARK_INCORPORATED_IN_COMPOUND,
    /** Apradhāna participant whose relation is conveyed through accompaniment (2.3.19). */
    NON_PRINCIPAL_ACCOMPANIMENT,
    BODY_DEFORMITY,
    CAUSE_HETU,
    GROUP_SELECTION,
    /** This participant is the reference group from which a distinction is made (2.3.41–42). */
    NIRDHARANA_REFERENCE,
    /** The compared groups are distinct in the expressed basis, rather than member and containing group. */
    SEPARATE_COMPARISON_GROUPS,
    CHARACTERISTIC_MARK,
    EXCLUSION_VINA,
    /** Explicit syntactic connection to pṛthag, vinā, or nānā, not mere word presence. */
    PRTHAG_VINA_NANA_LINK,
    OWNERSHIP_SWAMIN,
    DISREGARD_ANADARA,
    DIRECTIONAL_EXCLUSION,
    ACTION_MARKING,
    MOTION_GOAL,
    /** Bodily/physical movement, excluding merely mental movement, under 2.3.12. */
    PHYSICAL_MOTION,
    /** Destination construed other than as the traversed path, under 2.3.12. */
    NON_PATH_DESTINATION,
    /** Object in the sam-jñā construction licensed by 2.3.22. */
    SAM_JNA_OBJECT,
    /** Inanimate object of man construed in disregard, under 2.3.17. */
    MAN_INANIMATE_DISREGARD_OBJECT,
    PRESENT_PARTICIPLE_AGENT,
    ENGROSSED_ATTACHMENT,
    /** Respect/praise sense in the sādhu/nipuṇa construction, rather than factual aptitude. */
    ARCHA_RESPECT,
    /** Established participant-specific karmapravacanīya links, not mere nearby words or verbal prefixes. */
    PARI_KARMAPRAVACANIYA_LINK,
    ANU_KARMAPRAVACANIYA_LINK,
    MEMORY_OR_RULING_OBJECT,
    /** Jñā construed in pravṛtti/mithyājñāna rather than ordinary knowledge (2.3.51). */
    JNA_NON_KNOWLEDGE_SENSE,
    REMEMBERING_SENSE,
    DAY_DANA_GATI_RAKSHANA_SENSE,
    ISH_AISHVARYA_SENSE,
    BETWEEN_OR_WITHOUT,
    /** Direct dependent of nipāta antarā/antareṇa, not a nominal homonym or unrelated possessor. */
    ANTARA_NIPATA_LINK,
    KARMAPRAVACANIYA_GOVERNANCE,
    ENAPA_SUFFIX,
    EXCLUSION_LIMIT,
    /** Independently established ap/āṅ/pari karmapravacanīya link; sense is recorded separately. */
    AP_ANG_PARI_KARMAPRAVACANIYA_LINK,
    REPRESENTATIVE_EXCHANGE,
    /** The original/main reference (yasmāt), not the substitute or returned item (2.3.11). */
    REPRESENTATIVE_EXCHANGE_REFERENCE,
    /** Independently established karmapravacanīya prati linkage. */
    PRATI_KARMAPRAVACANIYA_LINK,
    IMPLIED_PURPOSE_OBJECT,
    /** Explicit participant binding to a separate written or omitted purpose action. */
    PURPOSE_ACTION_PARTICIPANT,
    PURPOSE_ACTION,
    /** Selected bhāva reading of this verbal noun, distinct from agent/object denotation. */
    ACTION_NOUN_DENOTATION,
    NON_FEMININE_QUALITY_CAUSE,
    EXPLICIT_HETU_USE,
    PRONOMINAL_HETU,
    SPATIAL_DIRECTION,
    DISTANCE_OR_PROXIMITY,
    COMPARATIVE_DISTINCTION,
    EQUAL_COMPARISON,
    BENEDICTION_WELLBEING,
    INTERVENING_DURATION_DISTANCE,
    /** Interval between kāraka powers/relations, which may belong to one entity in different actions (2.3.7). */
    KARAKA_POWER_INTERVAL,
    GAMBLING_INSTRUMENT,
    ASTROLOGICAL_TIME,
    /** Nominal stem meaning/gender/measure/number alone, established independently of nominative morphology. */
    BARE_NOMINAL_MEANING,
    /** Relation construed as śeṣa (e.g. possession, part/whole, parent/child), independent of case spelling. */
    SESHA_RELATION,
    /** Addressing the referent, not assigning it a verbal kāraka. */
    DIRECT_ADDRESS,
    /** Independently identified nakṣatra expression, excluding other lup-derived names. */
    NAKSATRA_EXPRESSION,
    DISEASE_PAIN_OBJECT,
    BLESSING_HOPE_OBJECT,
    INJURY_VIOLENCE_OBJECT,
    LOCATION_PARTICIPLE_RELATION,
    INQUIRY_DESTINY_TARGET,
    INDETERMINATE_QUANTITY,
    TRANSFORMATION_ENDOWMENT,
    /** Already existent substrate receiving another quality in pratiyatna (2.3.53). */
    EXISTING_SUBSTRATE,
    /** Event/state nominal (bhāva), independently established rather than inferred from agent case. */
    BHAVA_EVENT,
    TRANSACTION_GAMBLING_OBJECT,
    HATRED_PARTICIPLE_OBJECT,
    /** Object of hu in a Vedic (chandas) construction, for 2.3.3. */
    VEDIC_HU_OBJECT,
    /** Time or distance whose completion yields the stated result, for 2.3.6. */
    COMPLETION_LIMIT,
    /** Time/distance construed in uninterrupted full extent (atyantasaṃyoga), 2.3.5–6. */
    CONTINUOUS_TIME_DISTANCE_EXTENT,
    /** Reference relative to which superiority or lordship is asserted, for 2.3.9. */
    SUPERIORITY_REFERENCE,
    /** Selected reference in the sva/svāmin sovereignty relation under adhi (2.3.9). */
    SOVEREIGNTY_REFERENCE,
    UPA_SUPERIORITY_KARMAPRAVACANIYA_LINK,
    ADHI_SOVEREIGNTY_KARMAPRAVACANIYA_LINK,
    /** Debt construed as a non-agent cause, for 2.3.24; not ordinary debt ownership. */
    NON_AGENT_DEBT_REFERENCE,
    /** Gambling/transaction object of div in Brāhmaṇa usage, for 2.3.60. */
    BRAHMANA_DIV_OBJECT,
    /** Oblation object in preṣya/brūhi with a deity recipient, for 2.3.61. */
    DEITY_OBLATION_OBJECT,
    /** Independently identified deity; a deity name alone does not establish recipient role. */
    DEITY_REFERENT,
    /** Established prasthita state of the oblation under the prohibition accompanying 2.3.61. */
    PRASTHITA_OBLATION,
    /** Vedic expression carrying dative meaning but realized by ṣaṣṭhī, for 2.3.62. */
    VEDIC_DATIVE_MEANING,
    /** Instrument of yaj in the Vedic optional construction of 2.3.63. */
    VEDIC_YAJ_INSTRUMENT,
    /** Temporal locus governed by an explicit frequency expression, for 2.3.64. */
    FREQUENCY_TIME_LOCUS,
    /** Temporal reference independently distinguished from a spatial locus or instrument. */
    TIME_REFERENCE,
    /** Object selected for ṣaṣṭhī when a kṛt form admits both agent and object, for 2.3.66. */
    KRT_DUAL_ELIGIBLE_OBJECT,
    /** Competing agent excluded from ṣaṣṭhī by 2.3.66 in its ordinary domain. */
    KRT_DUAL_ELIGIBLE_AGENT,
    /** Participant governed by a kṛt nominal, required by 2.3.65. */
    KRT_GOVERNED_PARTICIPANT,
    /** Agent governed specifically by a kṛtya (gerundive) form, for 2.3.71. */
    KRTYA_GOVERNED_AGENT,
    // Action categories & relational tags
    EXISTENCE,
    MOTION,
    SPEECH,
    /** Action whose object is verbal text (śabdakarman), distinct from speech in general. */
    WORD_AS_OBJECT,
    COGNITION,
    CONSUMPTION,
    CREATION,
    DESTRUCTION,
    GIVING,
    RECEIVING,
    HEARING_PERCEPTION,
    SEEING_PERCEPTION,
    FEAR_EMOTION,
    ANGER_EMOTION,
    SYNONYM_RELATION,
    ANTONYM_RELATION,
    CAUSATIVE_STEM,
    DESIDERATIVE_STEM,
    INTENSIVE_STEM,
}

data class DhatuKarakaProfile(
    val surfaces: Set<String>,
    val relations: Set<SemanticRelation>,
)

/** Semantic valency facts consumed by kāraka-saṃjñā rules. */
object DhatuKarakaProfiles {
    private val profiles = listOf(
        DhatuKarakaProfile(setOf("दा", "ददाति", "यच्छति", "देहि"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("लिख्", "लिख"), setOf(SemanticRelation.INSTRUMENT)),
        DhatuKarakaProfile(setOf("पलाय्", "पलाय"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("भू", "प्रभू"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("शी", "स्था", "तिष्ठति", "तिष्ठ", "आस्", "अधिशी", "अधिस्था", "अध्यास्"), setOf(SemanticRelation.LOCATION)),
        DhatuKarakaProfile(setOf("जन्", "जन", "जाय"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("क्रुध्", "क्रुध", "द्रुह", "ईर्ष्या", "असूया", "अभिक्रुध", "अभिद्रुह"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("भी", "बिभेति", "बिभ्यति", "बिभ्यात्", "भीति", "बिभ", "त्रा", "त्राय"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("रुच", "रोच"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("वस", "वसति", "वसत", "उपवस", "अनुवस", "अधिवस", "आवस"), setOf(SemanticRelation.LOCATION)),
        DhatuKarakaProfile(setOf("पराजि", "पराजय"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("अधी", "पठ"), setOf(SemanticRelation.SOURCE, SemanticRelation.DESIRED_OBJECT)),
        DhatuKarakaProfile(setOf("स्पृह", "स्पृहय"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("अभिनिविश"), setOf(SemanticRelation.LOCATION)),
        DhatuKarakaProfile(setOf("वारय", "वार"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("निली", "तिरोभू"), setOf(SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("धारय"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("श्लाघ", "ह्नु", "शप"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("दिव", "दीव्"), setOf(SemanticRelation.INSTRUMENT)),
        DhatuKarakaProfile(setOf("परिक्री", "क्री"), setOf(SemanticRelation.INSTRUMENT)),
        DhatuKarakaProfile(setOf("प्रतिश्रु", "आश्रु"), setOf(SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("ग्रह्", "गृह्णाति", "गृह्ण", "अनुगृ", "प्रतिगृ"), setOf(SemanticRelation.RECIPIENT, SemanticRelation.DESIRED_OBJECT)),
        // Additional Dhātus
        DhatuKarakaProfile(setOf("कृ", "करोति", "करो", "कुर्व", "कारय", "अकृ"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.INSTRUMENT)),
        DhatuKarakaProfile(setOf("गम्", "गच्छति", "गच्छ", "आगम्", "आगच्छ", "अनुगम्", "अनुगच्छ", "उपागम्", "उपागच्छ"), setOf(SemanticRelation.MOTION_GOAL, SemanticRelation.DESIRED_OBJECT, SemanticRelation.LOCATION)),
        DhatuKarakaProfile(setOf("पा", "पिबति", "पिब", "पाति"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("दृश्", "पश्यति", "पश्य", "द्रक्ष्य"), setOf(SemanticRelation.DESIRED_OBJECT)),
        DhatuKarakaProfile(setOf("लभ्", "लभते", "लभ", "लप्स्य"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("ज्ञा", "जानाति", "जाना", "ज्ञाय"), setOf(SemanticRelation.DESIRED_OBJECT)),
        DhatuKarakaProfile(setOf("श्रु", "शृणोति", "शृणु", "श्रोष्य"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("वच्", "वक्ति", "वक्", "ब्रू", "कथ", "कथय", "भाष्"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.RECIPIENT)),
        DhatuKarakaProfile(setOf("हृ", "हरति", "हर", "हरय"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SOURCE)),
        DhatuKarakaProfile(setOf("गण", "गणयति", "गणय"), setOf(SemanticRelation.DESIRED_OBJECT)),
        DhatuKarakaProfile(setOf("युज्", "योजयति", "युञ्ज", "योजय"), setOf(SemanticRelation.DESIRED_OBJECT)),
        DhatuKarakaProfile(setOf("इष्", "इच्छति", "इच्छ"), setOf(SemanticRelation.DESIRED_OBJECT)),
        DhatuKarakaProfile(setOf("नी", "नयति", "नय"), setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.MOTION_GOAL)),
    )

    fun forDhatu(dhatu: Dhatu): DhatuKarakaProfile? {
        val entrySpellings = (setOf(dhatu.sourceSurface, dhatu.upadesha) + dhatu.surfaceAliases)
            .map { it.trimEnd('्', 'ँ') }.toSet()
        val lexical = if (dhatu.semanticRelations.isNotEmpty()) dhatu.semanticRelations else
            profiles.firstOrNull { profile -> profile.surfaces.any { it.trimEnd('्', 'ँ') in entrySpellings } }
                ?.relations.orEmpty()
        // These are declared lexical positions, independent of operation
        // selection, runtime payloads or whether a candidate operation fits.
        val positions = dhatu.operations.flatMap { operation ->
            operation.signature.requirements.map { it.karaka } + operation.signature.optionalKarakas +
                listOfNotNull(operation.resultBindingKaraka)
        }.toSet()
        val declared = buildSet {
            if (dev.panini.core.Karaka.SAMPRADANA in positions) add(SemanticRelation.RECIPIENT)
            if (dev.panini.core.Karaka.KARANA in positions) add(SemanticRelation.INSTRUMENT)
            if (dev.panini.core.Karaka.SAMBANDHA in positions) add(SemanticRelation.SESHA_RELATION)
        }
        val relations = lexical + declared
        return if (relations.isEmpty()) null else DhatuKarakaProfile(
            setOf(dhatu.sourceSurface, dhatu.upadesha) + dhatu.surfaceAliases, relations)
    }

    fun forSurface(surface: String): DhatuKarakaProfile? {
        val normSurface = surface.trimEnd('्', 'ँ')
        val dhatuEntries = DhatuPatha.all.filter { dhatu ->
            val normUp = dhatu.upadesha.trimEnd('्', 'ँ')
            val normSrc = dhatu.sourceSurface.trimEnd('्', 'ँ')
            surface == dhatu.upadesha || surface == dhatu.sourceSurface || surface == dhatu.derivationalSurface ||
                normSurface == normUp || normSurface == normSrc ||
                dhatu.surfaceAliases.contains(surface) ||
                dhatu.surfaceAliases.any { entry ->
                    val normEntry = entry.trimEnd('्', 'ँ')
                    surface == entry || normSurface == normEntry || surface.startsWith(entry) || entry.startsWith(surface)
                }
        }
        val dhatuRelations = dhatuEntries.flatMap { it.semanticRelations }.toSet()
        val dhatuAliases = dhatuEntries.flatMap { setOf(it.sourceSurface, it.upadesha) + it.surfaceAliases }.toSet()

        val fallback = profiles.firstOrNull { profile ->
            profile.surfaces.any { entry ->
                val normEntry = entry.trimEnd('्', 'ँ')
                surface == entry || normSurface == normEntry || surface.startsWith(entry) || entry.startsWith(surface)
            }
        }

        val combinedRelations = fallback?.relations.orEmpty() + dhatuRelations
        val combinedSurfaces = (fallback?.surfaces.orEmpty() + dhatuAliases).ifEmpty { setOf(surface) }

        if (combinedRelations.isEmpty()) return null
        return DhatuKarakaProfile(
            surfaces = combinedSurfaces,
            relations = combinedRelations,
        )
    }
}
