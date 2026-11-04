package dev.panini.analysis

import dev.panini.core.NominalCategory
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.lexicon.PratipadikaEntry

/** Infers dynamic semantic relations for a nominal participant based on head noun semantics and morphological features. */
object ParticipantRelationInferrer {

    /** Ordinary case support used to keep verbal valency participant-specific. */
    private val ordinaryCases = mapOf(
        SemanticRelation.INDEPENDENT_AGENT to setOf(Vibhakti.PRATHAMA, Vibhakti.TRTIYA),
        SemanticRelation.DESIRED_OBJECT to setOf(Vibhakti.DVITIYA),
        SemanticRelation.INSTRUMENT to setOf(Vibhakti.TRTIYA),
        SemanticRelation.RECIPIENT to setOf(Vibhakti.CHATURTHI),
        SemanticRelation.SOURCE to setOf(Vibhakti.PANCHAMI),
        SemanticRelation.LOCATION to setOf(Vibhakti.SAPTAMI),
        SemanticRelation.PROMPTER_CAUSE to setOf(Vibhakti.PRATHAMA),
        SemanticRelation.CAUSEE_AGENT to setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA),
        SemanticRelation.MOTION_GOAL to setOf(Vibhakti.DVITIYA),
        SemanticRelation.SESHA_RELATION to setOf(Vibhakti.SASTHI),
    )

    /**
     * Distributes verbal valency instead of copying the complete dhatu profile
     * to every nominal. A syncretic participant receives a relation only when
     * it is the sole case-compatible participant or belongs to the sole
     * explicitly coordinated, morphologically homogeneous position.
     */
    fun distributeDhatuRelations(
        possibleVibhaktis: List<Set<Vibhakti>>,
        dhatuRelations: Set<SemanticRelation>,
        coordinatedParticipants: List<Set<Int>> = emptyList(),
        alternativeInstrumentCases: Set<Vibhakti> = emptySet(),
    ): List<Set<SemanticRelation>> {
        require(coordinatedParticipants.flatten().all { it in possibleVibhaktis.indices })
        require(coordinatedParticipants.flatten().distinct().size == coordinatedParticipants.sumOf { it.size })
        val assigned = List(possibleVibhaktis.size) { mutableSetOf<SemanticRelation>() }
        dhatuRelations.forEach { relation ->
            val expected = ordinaryCases[relation]?.let {
                if (relation == SemanticRelation.INSTRUMENT) it + alternativeInstrumentCases else it
            }
            val eligible = possibleVibhaktis.indices.filter { index ->
                expected == null || possibleVibhaktis[index].any(expected::contains)
            }
            // Explicit coordination can occupy one valency position. Equal
            // morphology is required: mixed-case members need independent
            // analysis, and adjacency alone never creates a shared reading.
            val soleCoordinatedPosition = coordinatedParticipants.any { group ->
                group.isNotEmpty() && group == eligible.toSet() &&
                    group.map { possibleVibhaktis[it] }.distinct().size == 1
            }
            eligible.forEach { index ->
                if (possibleVibhaktis[index].size == 1 || eligible.size == 1 || soleCoordinatedPosition) assigned[index] += relation
            }
        }
        return assigned.map { it.toSet() }
    }

    fun infer(
        lexicalEntry: PratipadikaEntry?,
        possibleVibhaktis: Set<Vibhakti>,
        dhatuRelations: Set<SemanticRelation>,
        prayoga: Prayoga? = null,
        sakarmaka: Boolean = true,
        baseAkarmaka: Boolean = false,
    ): Set<SemanticRelation> = inferEvidence(
        lexicalEntry, possibleVibhaktis, dhatuRelations, prayoga, sakarmaka, baseAkarmaka,
    ).mapTo(mutableSetOf()) { it.relation }

    fun inferEvidence(
        lexicalEntry: PratipadikaEntry?,
        possibleVibhaktis: Set<Vibhakti>,
        dhatuRelations: Set<SemanticRelation>,
        prayoga: Prayoga? = null,
        sakarmaka: Boolean = true,
        baseAkarmaka: Boolean = false,
    ): Set<SemanticRelationEvidence> = buildSet {
        fun record(relation: SemanticRelation, source: SemanticEvidenceSource, detail: String) {
            add(SemanticRelationEvidence(relation, source, detail))
        }
        lexicalEntry?.semanticRelations.orEmpty().forEach {
            record(it, SemanticEvidenceSource.LEXICAL_ENTRY, "Prātipadika lexical semantics.")
        }
        dhatuRelations.forEach {
            record(it, SemanticEvidenceSource.DHATU_VALENCY, "Participant-specific dhātu valency.")
        }
        val categories = lexicalEntry?.categories.orEmpty()

        // Voice and unambiguous morphology provide construction hypotheses;
        // the 1.4 sūtras, not this inference, establish the final kāraka-saṃjñā.
        when (prayoga) {
            Prayoga.KARTARI -> {
                if (possibleVibhaktis == setOf(Vibhakti.PRATHAMA)) record(SemanticRelation.INDEPENDENT_AGENT,
                    SemanticEvidenceSource.VOICE_CONSTRUCTION, "Kartari construction with an unambiguous prathamā participant.")
                if (sakarmaka && possibleVibhaktis == setOf(Vibhakti.DVITIYA)) record(SemanticRelation.DESIRED_OBJECT,
                    SemanticEvidenceSource.VOICE_CONSTRUCTION, "Sakarmaka kartari construction with unambiguous dvitīyā.")
            }
            Prayoga.KARMANI -> {
                if (sakarmaka && possibleVibhaktis == setOf(Vibhakti.PRATHAMA)) record(SemanticRelation.DESIRED_OBJECT,
                    SemanticEvidenceSource.VOICE_CONSTRUCTION, "Karmani construction: prathamā is the expressed object hypothesis.")
                if (possibleVibhaktis == setOf(Vibhakti.TRTIYA)) record(SemanticRelation.INDEPENDENT_AGENT,
                    SemanticEvidenceSource.VOICE_CONSTRUCTION, "Karmani construction: tṛtīyā is the unexpressed agent hypothesis.")
            }
            Prayoga.BHAVE ->
                if (possibleVibhaktis == setOf(Vibhakti.TRTIYA)) record(SemanticRelation.INDEPENDENT_AGENT,
                    SemanticEvidenceSource.VOICE_CONSTRUCTION, "Bhāve construction with an anabhihita tṛtīyā agent.")
            Prayoga.CAUSATIVE -> {
                if (possibleVibhaktis == setOf(Vibhakti.PRATHAMA)) record(SemanticRelation.PROMPTER_CAUSE,
                    SemanticEvidenceSource.VOICE_CONSTRUCTION, "Ṇic construction with prathamā prayojaka.")
                if (sakarmaka && possibleVibhaktis == setOf(Vibhakti.DVITIYA)) record(SemanticRelation.DESIRED_OBJECT,
                    SemanticEvidenceSource.VOICE_CONSTRUCTION, "Ṇic sakarmaka construction with dvitīyā object hypothesis.")
                val causeeEligibleDhatu = baseAkarmaka || dhatuRelations.any {
                    it in setOf(
                        SemanticRelation.MOTION,
                        SemanticRelation.COGNITION,
                        SemanticRelation.CONSUMPTION,
                        SemanticRelation.WORD_AS_OBJECT,
                    )
                }
                if (causeeEligibleDhatu && NominalCategory.HUMAN in categories &&
                    possibleVibhaktis.singleOrNull() in setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA)
                ) record(SemanticRelation.CAUSEE_AGENT, SemanticEvidenceSource.VOICE_CONSTRUCTION,
                    "Human prayojya licensed by the base-root causative class.")
            }
            Prayoga.ANIRDHARITA, null -> Unit
        }

        if (NominalCategory.PLACE_LOCATION in categories || possibleVibhaktis == setOf(Vibhakti.SAPTAMI)) {
            record(SemanticRelation.LOCATION,
                if (NominalCategory.PLACE_LOCATION in categories) SemanticEvidenceSource.NOMINAL_CATEGORY
                else SemanticEvidenceSource.UNAMBIGUOUS_MORPHOLOGY,
                "Place category or unambiguous saptamī locus hypothesis.")
        }

        if (NominalCategory.INSTRUMENT_TOOL in categories || possibleVibhaktis == setOf(Vibhakti.TRTIYA)) {
            record(SemanticRelation.INSTRUMENT,
                if (NominalCategory.INSTRUMENT_TOOL in categories) SemanticEvidenceSource.NOMINAL_CATEGORY
                else SemanticEvidenceSource.UNAMBIGUOUS_MORPHOLOGY,
                "Instrument category or unambiguous tṛtīyā instrument hypothesis.")
        }

        if (possibleVibhaktis == setOf(Vibhakti.CHATURTHI)) record(SemanticRelation.RECIPIENT,
            SemanticEvidenceSource.UNAMBIGUOUS_MORPHOLOGY, "Unambiguous caturthī recipient hypothesis.")
        if (possibleVibhaktis == setOf(Vibhakti.PANCHAMI)) record(SemanticRelation.SOURCE,
            SemanticEvidenceSource.UNAMBIGUOUS_MORPHOLOGY, "Unambiguous pañcamī source hypothesis.")

        if (NominalCategory.HUMAN in categories) {
            if (possibleVibhaktis == setOf(Vibhakti.CHATURTHI)) {
                record(SemanticRelation.RECIPIENT, SemanticEvidenceSource.NOMINAL_CATEGORY,
                    "Human nominal category supports recipient semantics.")
            }
            if (possibleVibhaktis == setOf(Vibhakti.PRATHAMA) ||
                possibleVibhaktis == setOf(Vibhakti.TRTIYA)
            ) {
                record(SemanticRelation.INDEPENDENT_AGENT, SemanticEvidenceSource.NOMINAL_CATEGORY,
                    "Human nominal category supports agent semantics.")
            }
        }

        if (NominalCategory.OBJECT_ENTITY in categories || possibleVibhaktis == setOf(Vibhakti.DVITIYA)) {
            record(SemanticRelation.DESIRED_OBJECT,
                if (NominalCategory.OBJECT_ENTITY in categories) SemanticEvidenceSource.NOMINAL_CATEGORY
                else SemanticEvidenceSource.UNAMBIGUOUS_MORPHOLOGY,
                "Object category or unambiguous dvitīyā object hypothesis.")
        }
    }
}
