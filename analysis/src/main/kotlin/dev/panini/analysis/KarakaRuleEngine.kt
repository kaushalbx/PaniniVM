package dev.panini.analysis

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya2.pada3.SamantriteSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.EkavacanamSambuddhihSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.KarmanaYamAbhipraitiSampradanamSutra
import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraType

/** Semantic kāraka assignment (1.4) followed by nominal-case validation (2.3). */
object KarakaRuleEngine {
    private val ordinaryMeanings = mapOf("1.4.24" to SemanticRelation.SOURCE,
        "1.4.32" to SemanticRelation.RECIPIENT, "1.4.42" to SemanticRelation.INSTRUMENT,
        "1.4.45" to SemanticRelation.LOCATION, "1.4.49" to SemanticRelation.DESIRED_OBJECT,
        "1.4.50" to SemanticRelation.INDIFFERENT_OBJECT,
        "1.4.51" to SemanticRelation.SECONDARY_OBJECT,
        "1.4.54" to SemanticRelation.INDEPENDENT_AGENT)

    val karakaRules: List<Sutra<KarakaRuleContext, KarakaRuleResult>> by lazy {
        @Suppress("UNCHECKED_CAST")
        Ashtadhyayi.registry.sutras
            .filter { sutra ->
                sutra !is DerivationSutra &&
                    sutra.chapter == 1 &&
                    sutra.pada == 4 &&
                    sutra.action == SutraAction.SAMJNA &&
                    sutra.adhikara.contains("1.4.23")
            }
            .sortedBy { it.krama } as List<Sutra<KarakaRuleContext, KarakaRuleResult>>
    }

    val vibhaktiRules: List<Sutra<VibhaktiRuleContext, VibhaktiRuleResult>> by lazy {
        @Suppress("UNCHECKED_CAST")
        Ashtadhyayi.registry.sutras
            .filter { sutra ->
                sutra !is DerivationSutra &&
                    sutra.type != SutraType.NISHEDHA &&
                    sutra.action == SutraAction.VIDHI &&
                    sutra.chapter == 2 &&
                    sutra.pada == 3
            }
            .sortedBy { it.krama } as List<Sutra<VibhaktiRuleContext, VibhaktiRuleResult>>
    }

    private val coexistentRules by lazy { karakaRules.filter { it is CoexistentKarakaRuleOutcome } }

    private fun enrich(context: KarakaRuleContext): KarakaRuleContext =
        VerbalConstructionEvidence.enrich(KrtGovernanceEvidence.enrich(context))

    fun resolve(context: KarakaRuleContext): KarakaResolution {
        val enriched = enrich(context)
        if (!enriched.participant.hasEstablishedMeaning(SemanticRelation.RECIPIENT) ||
            enriched.participant.intendedObject == IntendedObject.Implied) return resolvePrepared(enriched)
        // First resolve each same-action participant using local canonical
        // eligibility only. This phase never calls resolve(), so explicit or
        // cyclic recipient links cannot recurse through the engine.
        val readings = enriched.allParticipants.filter { it.id != enriched.participant.id }
            .groupBy { it.id }.mapNotNull { (id, occurrences) ->
                val other = occurrences.singleOrNull() ?: return@mapNotNull null
                id to resolvePrepared(enrich(enriched.copy(participant = other, candidates = emptySet(),
                    mediatingObjectResolutions = emptyMap())))
            }.toMap()
        val prepared = enriched.copy(mediatingObjectResolutions = readings)
        val result = resolvePrepared(prepared)
        if (!KarmanaYamAbhipraitiSampradanamSutra.hasRecipientIntent(prepared) ||
            KarmanaYamAbhipraitiSampradanamSutra.matches(prepared)) return result
        val relevant = when (val binding = enriched.participant.intendedObject) {
            is IntendedObject.Written -> readings.filterKeys { it == binding.participantId }
            null -> readings
            IntendedObject.Implied -> emptyMap()
        }
        val unresolved = relevant.filter { (id, resolution) ->
            val other = enriched.allParticipants.single { it.id == id }
            resolution.resolved == null && Karaka.KARMAN in resolution.candidates &&
                hasDesignationProof(enriched.copy(participant = other), resolution, Karaka.KARMAN)
        }.mapValues { it.value.candidates }
        return result.copy(unresolvedObjectMediators = unresolved)
    }

    /** Classify the retained canonical proof, not a morphology-only candidate. */
    private fun hasDesignationProof(context: KarakaRuleContext, resolution: KarakaResolution, role: Karaka): Boolean =
        (resolution.evidence + resolution.alternatives.flatMap { it.evidence }).any { proof ->
            if (!proof.applied) return@any false
            val rule = karakaRules.singleOrNull { it.number == proof.sutra } ?: return@any false
            (rule.apply(context) as? KarakaRuleResult.Assigned)?.karaka == role
        }

    private fun resolvePrepared(context: KarakaRuleContext): KarakaResolution {
        val applied = resolveSingle(context)
        if (applied.coexistingKarakas.size > 1) return resolveCoexistent(context, applied)
        val rule = applied.evidence.firstNotNullOfOrNull { proof ->
            karakaRules.singleOrNull { it.number == proof.sutra && it.optional && it is OptionalKarakaRuleOutcome }
        } ?: return applied
        val outcome = rule as OptionalKarakaRuleOutcome
        val appliedDesignation = rule.apply(context) as KarakaRuleResult.Assigned
        val declinedDesignation = outcome.declinedDesignation(context)
        val declined = resolveSingle(context, declinedDesignation)
        fun compatible(reading: KarakaResolution): Boolean =
            reading.evidence.lastOrNull()?.sutra?.startsWith("2.3.") == true ||
                reading.alternatives.any { it.matchesObservedMorphology && it.vibhakti != null }
        val applyFits = compatible(applied)
        val declineFits = compatible(declined)
        val chooseDecline = declineFits && !applyFits
        val primary = if (chooseDecline) declined else applied
        val counter = if (chooseDecline) applied else declined
        val counterDesignation = if (chooseDecline) appliedDesignation else declinedDesignation
        val counterFits = if (chooseDecline) applyFits else declineFits
        val generatedContext = context.copy(participant = context.participant.copy(possibleVibhaktis = Vibhakti.entries.toSet()),
            candidates = emptySet())
        val counterContext = if (counterFits) context else generatedContext
        val counterReading = if (counterFits) counter else resolveSingle(generatedContext, counterDesignation)
        val counterCase = counterReading.resolvedVibhakti ?: vibhaktiAssignment(counterContext,
            counterDesignation.karaka, counterContext.participant.possibleVibhaktis,
            designation = counterDesignation.evidence.sutra)?.second?.vibhakti
        fun decision(apply: Boolean) = appliedDesignation.evidence.copy(optional = true, applied = apply,
            semanticEvidence = context.participant.semanticEvidence,
            reason = if (apply) "Optional designation applied." else "Optional designation declined; the underlying designation is retained.")
        val counterDecision = decision(chooseDecline)
        val kind = if (chooseDecline) KarakaAlternativeKind.OPTIONAL_APPLY else KarakaAlternativeKind.OPTIONAL_DECLINE
        val alternatives = primary.alternatives + KarakaAlternative(counterDesignation.karaka, counterCase,
            counterReading.evidence, kind, counterFits && counterCase in context.participant.possibleVibhaktis,
            optionalDecision = counterDecision) + counterReading.alternatives.map { alternative ->
                alternative.copy(kind = kind, matchesObservedMorphology = counterFits && alternative.matchesObservedMorphology,
                    optionalDecision = counterDecision)
            }
        val unresolved = applyFits == declineFits
        return primary.copy(
            resolved = primary.resolved.takeUnless { unresolved },
            resolvedVibhakti = primary.resolvedVibhakti.takeUnless { unresolved },
            candidates = if (unresolved) setOf(appliedDesignation.karaka, declinedDesignation.karaka) else primary.candidates,
            alternatives = alternatives.distinct(), primaryOptionalDecision = decision(!chooseDecline),
            additionalDesignations = primary.additionalDesignations.takeUnless { unresolved }.orEmpty(),
        )
    }

    /** Simultaneous names remain established even when their case projection is unknown. */
    private fun resolveCoexistent(context: KarakaRuleContext, base: KarakaResolution): KarakaResolution {
        val rule = karakaRules.single { it.number == base.evidence.first().sutra }
        val designation = rule.apply(context) as KarakaRuleResult.Assigned
        val readings = designation.coexistingKarakas.associateWith { role ->
            resolveSingle(context, designation.copy(karaka = role))
        }
        fun compatible(reading: KarakaResolution) = reading.evidence.lastOrNull()?.sutra?.startsWith("2.3.") == true ||
            reading.alternatives.any { it.matchesObservedMorphology && it.vibhakti != null }
        val observed = readings.filterValues(::compatible)
        val primaryRole = observed.keys.singleOrNull() ?: designation.karaka
        val primary = readings.getValue(primaryRole)
        val generatedContext = context.copy(participant = context.participant.copy(possibleVibhaktis = Vibhakti.entries.toSet()),
            candidates = emptySet())
        val alternatives = readings.filterKeys { it != primaryRole }.flatMap { (role, reading) ->
            val fits = compatible(reading)
            val branchContext = if (fits) context else generatedContext
            val branch = if (fits) reading else resolveSingle(generatedContext, designation.copy(karaka = role))
            val case = branch.resolvedVibhakti ?: vibhaktiAssignment(branchContext, role,
                branchContext.participant.possibleVibhaktis, designation = designation.evidence.sutra)?.second?.vibhakti
            val sameCaseProjection = fits && primary.resolvedVibhakti != null && case == primary.resolvedVibhakti
            (if (sameCaseProjection) emptyList() else listOf(KarakaAlternative(role, case, branch.evidence,
                KarakaAlternativeKind.COEXISTENT_CASE,
                matchesObservedMorphology = fits && case in context.participant.possibleVibhaktis))) + branch.alternatives.map {
                it.copy(kind = KarakaAlternativeKind.COEXISTENT_CASE,
                    matchesObservedMorphology = fits && it.matchesObservedMorphology)
            }
        }
        val observedCases = observed.values.mapNotNull { it.resolvedVibhakti }.toSet()
        return primary.copy(resolvedVibhakti = primary.resolvedVibhakti.takeIf { observedCases.size == 1 },
            alternatives = (primary.alternatives + alternatives).distinct(), coexistingKarakas = designation.coexistingKarakas)
    }

    /** Resolve one designation; optional counter-branches are constructed separately. */
    private fun resolveSingle(context: KarakaRuleContext, forcedDesignation: KarakaRuleResult.Assigned? = null): KarakaResolution {
        val possibleVibhaktis = context.participant.possibleVibhaktis
        val rejectedRules = mutableListOf<RejectedKarakaRule>()
        val candidates = context.candidates.ifEmpty {
            KarakaInference.candidatesForCases(context.participant.possibleVibhaktis, context.prayoga, context.dhatu.sakarmaka)
        }
        val semanticContext = context.copy(candidates = candidates)
        val isAddress = SemanticRelation.DIRECT_ADDRESS in context.participant.semanticRelations
        val isActionMarking = SemanticRelation.ACTION_MARKING in context.participant.semanticRelations
        val isDistanceReading = SemanticRelation.DISTANCE_OR_PROXIMITY in context.participant.semanticRelations
        val isDirectionalReading = SemanticRelation.DIRECTIONAL_EXCLUSION in context.participant.semanticRelations
        val isPurposeAction = SemanticRelation.PURPOSE_ACTION in context.participant.semanticRelations
        val isBodyDeformity = SemanticRelation.BODY_DEFORMITY in context.participant.semanticRelations
        val isSufficiencyReading = SemanticRelation.SUFFICIENCY in context.participant.semanticRelations
        val isExplicitHetuReading = SemanticRelation.CAUSE_HETU in context.participant.semanticRelations &&
            SemanticRelation.EXPLICIT_HETU_USE in context.participant.semanticRelations
        val krtRoleGovernance = SemanticRelation.KRT_GOVERNED_PARTICIPANT in context.participant.semanticRelations &&
            context.participant.semanticRelations.any { it in setOf(SemanticRelation.INDEPENDENT_AGENT, SemanticRelation.DESIRED_OBJECT) }
        val isSesha = SemanticRelation.SESHA_RELATION in context.participant.semanticRelations &&
            context.participant.meaningEvidenceStrength(SemanticRelation.SESHA_RELATION) >=
                ordinaryMeanings.values.maxOf(context.participant::meaningEvidenceStrength) &&
            !krtRoleGovernance
        val matchingSemanticRules = if (forcedDesignation != null) listOf(karakaRules.single {
            it.number == forcedDesignation.evidence.sutra
        }) else karakaRules.filter { rule ->
            if (isAddress || isSesha || isActionMarking || isDistanceReading || isDirectionalReading || isPurposeAction || isBodyDeformity || isSufficiencyReading || isExplicitHetuReading || context.participant.governingAvyaya != null) return@filter false
            val prohibition = NishedhaRuleEngine.evaluateProhibition(ProhibitionContext(targetSutraNumber = rule.number))
            prohibition !is NishedhaRuleResult.Blocked && try { rule.matches(semanticContext) } catch (_: ClassCastException) { false }
        }
        val strengths = matchingSemanticRules.mapNotNull { ordinaryMeanings[it.number] }
            .map(context.participant::meaningEvidenceStrength).distinct()
        val ordinaryTie = matchingSemanticRules.size > 1 && matchingSemanticRules.all { it.number in ordinaryMeanings } &&
            strengths.size == 1 && strengths.single() > 0
        val compatibleOrdinaryRules = if (ordinaryTie) matchingSemanticRules.filter { rule ->
            val result = rule.apply(semanticContext) as? KarakaRuleResult.Assigned
            result != null && vibhaktiAssignment(context, result.karaka, possibleVibhaktis,
                designation = result.evidence.sutra) != null
        } else emptyList()
        fun roles(rules: List<Sutra<KarakaRuleContext, KarakaRuleResult>>) = rules.mapNotNull {
            (it.apply(semanticContext) as? KarakaRuleResult.Assigned)?.karaka
        }.toSet()
        val compatibleRoles = roles(compatibleOrdinaryRules)
        val unresolvedOrdinaryReadings = ordinaryTie && roles(compatibleOrdinaryRules.ifEmpty { matchingSemanticRules }).size > 1
        val semanticRule = compatibleOrdinaryRules.firstOrNull().takeIf { compatibleRoles.size == 1 }
            ?: matchingSemanticRules.firstOrNull()
        val semantic = forcedDesignation ?: (semanticRule?.apply(semanticContext) as? KarakaRuleResult.Assigned)
        // Morphology proposes candidates; only a 1.4 saṃjñā rule establishes a
        // kāraka. Sambandha is the deliberate exception: it is a non-kāraka
        // relation established through 2.3.50 ṣaṣṭhī śeṣe below.
        val semanticResolved = semantic?.karaka
        val directAssignment = if (semantic == null) vibhaktiAssignment(
            context,
            Karaka.ANIRDHARITA,
            possibleVibhaktis,
            eligibleRuleNumbers = directNonKarakaVibhaktiRules,
            rejectedRules = rejectedRules,
        ) else null
        val sambandhaHypothesis = semantic == null && directAssignment == null && candidates == setOf(Karaka.SAMBANDHA)
        val otherNonKarakaHypothesis = semantic == null && candidates == setOf(Karaka.ANIRDHARITA)
        val provisional = semanticResolved
            ?: Karaka.SAMBANDHA.takeIf { sambandhaHypothesis }
            ?: Karaka.ANIRDHARITA.takeIf { otherNonKarakaHypothesis }
        var resolvedVibhakti: Vibhakti? = null
        var directNonKarakaAssignment = false
        val assignedEvidence = buildList {
            semantic?.let { add(it.evidence.copy(
                optional = requireNotNull(semanticRule).optional,
                semanticEvidence = context.participant.semanticEvidence,
            )) }
            provisional?.let { karaka ->
                vibhaktiAssignment(context, karaka, possibleVibhaktis,
                    eligibleRuleNumbers = if (otherNonKarakaHypothesis) directNonKarakaVibhaktiRules else null,
                    rejectedRules = rejectedRules, designation = semantic?.evidence?.sutra)?.let { (rule, assignment) ->
                    resolvedVibhakti = assignment.vibhakti
                    add(assignment.evidence.copy(optional = rule.optional))
                }
            }
            // Some 2.3 rules establish a case relation directly rather than
            // realizing a kāraka-saṃjñā from 1.4 (for example 2.3.20
            // येनाङ्गविकारः). A specific construction takes priority over
            // the general ṣaṣṭhī śeṣe hypothesis.
            if (semantic == null && provisional == null) {
                directAssignment?.let { (rule, assignment) ->
                    resolvedVibhakti = assignment.vibhakti
                    directNonKarakaAssignment = true
                    add(assignment.evidence.copy(
                        optional = rule.optional,
                        semanticEvidence = context.participant.semanticEvidence,
                    ))
                }
            }
        }
        val caseRule = vibhaktiRules.firstOrNull { it.number == assignedEvidence.lastOrNull()?.sutra }
        val caseRole = resolvedVibhakti?.let { (caseRule as? CaseDependentKarakaOutcome)?.karakaForCase(it) }
        val evidence = listOfNotNull(caseRole?.takeUnless { it == semanticResolved }
            ?.let { caseRoleEvidence(context, it) }) + assignedEvidence
        val resolved = semanticResolved ?: caseRole
            ?: Karaka.SAMBODHANA.takeIf { evidence.lastOrNull()?.sutra == "2.3.47" }
            ?: Karaka.SAMBANDHA.takeIf { evidence.lastOrNull()?.sutra in setOf("2.3.50", "2.3.51", "2.3.52", "2.3.53", "2.3.54", "2.3.55", "2.3.56", "2.3.57", "2.3.58", "2.3.61", "2.3.64", "2.3.72", "2.3.73") }
            ?: Karaka.SAMBANDHA.takeIf { sambandhaHypothesis && resolvedVibhakti == Vibhakti.SASTHI }
            ?: Karaka.ANIRDHARITA.takeIf { otherNonKarakaHypothesis && resolvedVibhakti != null }
            ?: Karaka.ANIRDHARITA.takeIf { directNonKarakaAssignment }
        val alternatives = buildList {
            val assigningRule = vibhaktiRules.firstOrNull { it.number == evidence.lastOrNull()?.sutra }
            val caseOutcome = assigningRule as? OptionalVibhaktiRuleOutcome
            if (caseOutcome != null && resolved != null) {
                caseOutcome.permittedCases(VibhaktiRuleContext(resolved, possibleVibhaktis, participant = context.participant))
                    .filter { it != resolvedVibhakti }.forEach { alternativeCase ->
                    val dependentRole = (assigningRule as? CaseDependentKarakaOutcome)?.karakaForCase(alternativeCase)
                    val alternativeRole = dependentRole ?: resolved
                    val assignment = vibhaktiAssignment(context, alternativeRole, setOf(alternativeCase),
                        eligibleRuleNumbers = caseOutcome.alternativeRuleNumbers(
                            VibhaktiRuleContext(resolved, possibleVibhaktis, participant = context.participant))
                            ?: setOf(assigningRule.number), designation = if (alternativeRole == resolved)
                            semantic?.evidence?.sutra else caseRoleEvidence(context, alternativeRole)?.sutra) ?: return@forEach
                    val baseEvidence = if (dependentRole != null) listOfNotNull(caseRoleEvidence(context, alternativeRole))
                        else evidence.filter { it.sutra != assigningRule.number }
                    add(KarakaAlternative(
                        alternativeRole, alternativeCase,
                        baseEvidence + assignment.second.evidence.copy(optional = true),
                        KarakaAlternativeKind.OPTIONAL_VIBHAKTI,
                        matchesObservedMorphology = alternativeCase in possibleVibhaktis,
                    ))
                }
            }
            // Bahulam is construction-specific: preserve licensed observed readings,
            // without generating a genitive for every occurrence in a Vedic register.
            if (resolved != null) {
                vibhaktiRules.forEach { attestedRule ->
                    val attested = attestedRule as? AttestedVibhaktiRuleOutcome ?: return@forEach
                    val case = attested.attestedCase
                    if (case !in possibleVibhaktis || case == resolvedVibhakti ||
                        any { it.vibhakti == case && it.evidence.lastOrNull()?.sutra == attestedRule.number }) return@forEach
                    val role = (attestedRule as? CaseDependentKarakaOutcome)?.karakaForCase(case) ?: resolved
                    val assignment = vibhaktiAssignment(context, role, setOf(case),
                        eligibleRuleNumbers = setOf(attestedRule.number), designation = if (role == resolved)
                            semantic?.evidence?.sutra else caseRoleEvidence(context, role)?.sutra) ?: return@forEach
                    val proof = if (role == resolved) evidence.filter { it.sutra.startsWith("1.4.") }
                        else listOfNotNull(caseRoleEvidence(context, role))
                    add(KarakaAlternative(role, case, proof + assignment.second.evidence.copy(optional = true),
                        KarakaAlternativeKind.OPTIONAL_VIBHAKTI, matchesObservedMorphology = true))
                }
            }
            // Competing supplied readings of the ordinary roles are epistemic
            // alternatives, not grammatical optionality. Special saṃjñā rules
            // retain their own precedence and explicitly modeled branches.
            val selectedMeaning = ordinaryMeanings[semanticRule?.number]
            val selectedStrength = selectedMeaning?.let(context.participant::meaningEvidenceStrength) ?: 0
            if (selectedStrength > 0) {
                matchingSemanticRules.filter { it !== semanticRule }.filter { competingRule ->
                    ordinaryMeanings[competingRule.number]?.let {
                        context.participant.meaningEvidenceStrength(it) == selectedStrength
                    } == true
                }.forEach { competingRule ->
                    val competing = competingRule.apply(semanticContext) as? KarakaRuleResult.Assigned ?: return@forEach
                    val assignment = vibhaktiAssignment(context, competing.karaka, possibleVibhaktis,
                        designation = competing.evidence.sutra)
                    add(KarakaAlternative(competing.karaka, assignment?.second?.vibhakti,
                        listOf(competing.evidence.copy(semanticEvidence = context.participant.semanticEvidence)) +
                            listOfNotNull(assignment?.second?.evidence), KarakaAlternativeKind.COMPETING_ANALYSIS,
                        matchesObservedMorphology = assignment?.second?.vibhakti in possibleVibhaktis))
                }
            }
        }
        val compatibleCaseReading = alternatives.any {
            it.kind == KarakaAlternativeKind.OPTIONAL_VIBHAKTI && it.matchesObservedMorphology
        }
        val compatibleCaseRoleReading = alternatives.any {
            it.kind == KarakaAlternativeKind.OPTIONAL_VIBHAKTI && it.matchesObservedMorphology && it.karaka != resolved
        }
        val compatibleCompetingRole = alternatives.any {
            it.kind == KarakaAlternativeKind.COMPETING_ANALYSIS && it.matchesObservedMorphology && it.karaka != resolved
        }
        val conflictingCompetingCase = alternatives.any {
            it.kind == KarakaAlternativeKind.COMPETING_ANALYSIS && it.matchesObservedMorphology && it.vibhakti != resolvedVibhakti
        }
        val finalCase = resolvedVibhakti.takeUnless { compatibleCaseReading || conflictingCompetingCase }
        val designations = if (resolved == Karaka.SAMBODHANA && finalCase == Vibhakti.PRATHAMA) {
            val designationContext = VibhaktiRuleContext(resolved, setOf(finalCase), participant = context.participant)
            buildList {
                if (SamantriteSutra.matches(designationContext)) add(SamantriteSutra.apply(designationContext))
                if (EkavacanamSambuddhihSutra.matches(designationContext)) add(EkavacanamSambuddhihSutra.apply(designationContext))
            }
        } else emptyList()
        val roleCandidates = candidates + setOfNotNull(caseRole)
        val supportedOrdinaryRoles = if (unresolvedOrdinaryReadings)
            (compatibleOrdinaryRules.ifEmpty { matchingSemanticRules }).mapNotNull {
                (it.apply(semanticContext) as? KarakaRuleResult.Assigned)?.karaka
            }.toSet() else emptySet()
        val observedCandidates = if (unresolvedOrdinaryReadings) supportedOrdinaryRoles
            else if (compatibleCaseRoleReading || compatibleCompetingRole) roleCandidates + setOfNotNull(resolved) +
            alternatives.filter { it.matchesObservedMorphology }.map { it.karaka } else roleCandidates
        return KarakaResolution(observedCandidates, resolved.takeUnless { compatibleCaseRoleReading || compatibleCompetingRole || unresolvedOrdinaryReadings }, possibleVibhaktis,
            evidence, finalCase, alternatives, rejectedRules.distinct(), designations,
            semantic?.additionalDesignations.orEmpty().takeUnless {
                compatibleCaseRoleReading || compatibleCompetingRole || unresolvedOrdinaryReadings
            }.orEmpty(), coexistingKarakas = semantic?.coexistingKarakas.orEmpty())
    }

    /** Object case branches need their own 1.4 proof; a relational genitive supplies none. */
    private fun caseRoleEvidence(context: KarakaRuleContext, role: Karaka): KarakaEvidence? {
        if (role == Karaka.SAMBANDHA || role == Karaka.ANIRDHARITA) return null
        val participant = context.participant.copy(semanticRelations =
            context.participant.semanticRelations - SemanticRelation.SESHA_RELATION,
            semanticEvidence = context.participant.semanticEvidence.filterNot {
                it.relation == SemanticRelation.SESHA_RELATION }.toSet())
        val branch = context.copy(participant = participant, candidates = setOf(role))
        return karakaRules.firstNotNullOfOrNull { rule ->
            val blocked = NishedhaRuleEngine.evaluateProhibition(ProhibitionContext(targetSutraNumber = rule.number))
            if (blocked is NishedhaRuleResult.Blocked || !rule.matches(branch)) null
            else (rule.apply(branch) as? KarakaRuleResult.Assigned)?.takeIf { it.karaka == role }?.evidence
                ?.copy(optional = rule.optional, semanticEvidence = participant.semanticEvidence)
        }
    }

    private fun isAbhihita(context: KarakaRuleContext, karaka: Karaka, designation: String? = null): Boolean =
        isDirectlyAbhihita(context, karaka, designation) || coexistentRules.any { rule ->
            val names = (rule as CoexistentKarakaRuleOutcome).coexistingKarakas
            karaka in names && rule.matches(context) && names.any { isDirectlyAbhihita(context, it, designation) }
        }

    private fun isDirectlyAbhihita(context: KarakaRuleContext, karaka: Karaka, designation: String?): Boolean {
        if (context.governingKrt != null) return karaka in context.governingKrtExpressedRoles.orEmpty()
        if (PassiveObjectExpression.applies(context, karaka, designation))
            return PassiveObjectExpression.isExpressed(context, designation) == true
        return when (context.prayoga) {
        Prayoga.KARTARI -> karaka == Karaka.KARTR
        Prayoga.KARMANI -> karaka == Karaka.KARMAN
        Prayoga.CAUSATIVE -> karaka == Karaka.KARTR &&
            context.participant.meaningEvidenceStrength(SemanticRelation.PROMPTER_CAUSE) >= 2
        Prayoga.BHAVE, Prayoga.ANIRDHARITA -> false
        }
    }

    private fun vibhaktiAssignment(
        context: KarakaRuleContext,
        karaka: Karaka,
        morphologicalCandidates: Set<Vibhakti>,
        eligibleRuleNumbers: Set<String>? = null,
        rejectedRules: MutableList<RejectedKarakaRule>? = null,
        designation: String? = null,
    ): Pair<Sutra<VibhaktiRuleContext, VibhaktiRuleResult>, VibhaktiRuleResult.Assigned>? {
        if (PassiveObjectExpression.applies(context, karaka, designation) &&
            PassiveObjectExpression.isExpressed(context, designation) == null) return null
        if (context.governingKrt?.let { !KrtGovernanceEvidence.hasRecognizedMorphology(it) } == true) return null
        if (context.governingKrt != null && context.governingKrtExpressedRoles == null &&
            karaka !in setOf(Karaka.ANIRDHARITA, Karaka.SAMBANDHA, Karaka.SAMBODHANA)) return null
        val ruleKaraka = if (karaka == Karaka.SAMBANDHA) Karaka.ANIRDHARITA else karaka
        val vibhaktiContext = VibhaktiRuleContext(
            ruleKaraka,
            morphologicalCandidates,
            abhihita = isAbhihita(context, karaka, designation),
            participant = context.participant,
            dhatu = context.dhatu,
            verbNode = context.verbNode,
            allParticipants = context.allParticipants,
            baseDhatu = context.baseDhatu,
            register = context.register,
            governingKrt = context.governingKrt,
            governingKrtExpressedRoles = context.governingKrtExpressedRoles,
        )
        // A written wrong ending cannot remove an obligatory kṛt genitive
        // domain and thereby license the ordinary accusative/instrumental.
        // Probe the domain independently of the observed ending, respecting
        // canonical prohibitions and the optional kṛtya-agent exception.
        val genitiveContext = vibhaktiContext.copy(morphologicalCandidates = setOf(Vibhakti.SASTHI))
        val mandatoryKrtGenitive = SemanticRelation.KRT_GOVERNED_PARTICIPANT in
            context.participant.semanticRelations && vibhaktiRules.any { candidate ->
            candidate.number in setOf("2.3.65", "2.3.66", "2.3.67", "2.3.68") &&
                isVibhaktiEligible(candidate, genitiveContext) && candidate.matches(genitiveContext) &&
                NishedhaRuleEngine.evaluateProhibition(ProhibitionContext(
                    targetSutraNumber = candidate.number,
                    isKrtProhibitedForSasthi = context.participant.isKrtProhibitedForSasthi,
                    krtGenitiveProhibition = context.participant.krtGenitiveProhibition,
                    proposedVibhakti = Vibhakti.SASTHI,
                )) !is NishedhaRuleResult.Blocked
        }
        val rule = vibhaktiRules.firstOrNull { candidate ->
            if (mandatoryKrtGenitive && candidate.number in setOf("2.3.2", "2.3.18")) {
                false
            } else if (eligibleRuleNumbers != null && candidate.number !in eligibleRuleNumbers) {
                false
            } else {
                val matches = isVibhaktiEligible(candidate, vibhaktiContext) &&
                    try { candidate.matches(vibhaktiContext) } catch (_: ClassCastException) { false }
                if (!matches) return@firstOrNull false
                val caseDependent = candidate as? CaseDependentKarakaOutcome
                if (caseDependent != null) {
                    val proposed = candidate.apply(vibhaktiContext) as? VibhaktiRuleResult.Assigned
                        ?: return@firstOrNull false
                    val branchRole = caseDependent.karakaForCase(proposed.vibhakti)
                    val branchDesignation = if (branchRole == karaka) designation else caseRoleEvidence(context, branchRole)?.sutra
                    if (PassiveObjectExpression.applies(context, branchRole, branchDesignation) &&
                        PassiveObjectExpression.isExpressed(context, branchDesignation) == null) return@firstOrNull false
                    if (isAbhihita(context, branchRole, branchDesignation)) return@firstOrNull false
                    if (branchRole !in setOf(Karaka.SAMBANDHA, Karaka.ANIRDHARITA) &&
                        caseRoleEvidence(context, branchRole) == null) return@firstOrNull false
                }
                val prohibition = NishedhaRuleEngine.evaluateProhibition(
                    ProhibitionContext(
                        targetSutraNumber = candidate.number,
                        isKrtProhibitedForSasthi = context.participant.isKrtProhibitedForSasthi,
                        krtGenitiveProhibition = context.participant.krtGenitiveProhibition,
                        proposedVibhakti = if (candidate.number == "2.3.66")
                            (candidate.apply(vibhaktiContext) as? VibhaktiRuleResult.Assigned)?.vibhakti else null,
                    ),
                )
                if (prohibition is NishedhaRuleResult.Blocked) {
                    rejectedRules?.add(RejectedKarakaRule(candidate.number, prohibition.evidence.copy(
                        semanticEvidence = context.participant.semanticEvidence,
                    )))
                    false
                } else true
            }
        } ?: return null
        val assignment = rule.apply(vibhaktiContext) as? VibhaktiRuleResult.Assigned ?: return null
        return rule to assignment.copy(evidence = assignment.evidence.copy(
            semanticEvidence = assignment.evidence.semanticEvidence + context.participant.semanticEvidence,
        ))
    }

    /**
     * Direct non-kāraka realization is opt-in. Each entry must have an exact
     * regression proving that it does not turn morphology alone into a
     * semantic conclusion.
     */
    private val directNonKarakaVibhaktiRules = setOf(
        "2.3.3",
        "2.3.4",
        "2.3.5",
        "2.3.6",
        "2.3.7",
        "2.3.8",
        "2.3.9",
        "2.3.10",
        "2.3.11",
        "2.3.15",
        "2.3.16",
        "2.3.19",
        "2.3.20",
        "2.3.21",
        "2.3.23",
        "2.3.24",
        "2.3.25",
        "2.3.26",
        "2.3.27",
        "2.3.32",
        "2.3.29",
        "2.3.30",
        "2.3.31",
        "2.3.34",
        "2.3.35",
        "2.3.36",
        "2.3.37",
        "2.3.38",
        "2.3.39",
        "2.3.40",
        "2.3.41",
        "2.3.42",
        "2.3.43",
        "2.3.44",
        "2.3.46",
        "2.3.47",
        "2.3.50",
        "2.3.51",
        "2.3.52",
        "2.3.53",
        "2.3.54",
        "2.3.55",
        "2.3.56",
        "2.3.57",
        "2.3.58",
        "2.3.59",
        "2.3.60",
        "2.3.61",
        "2.3.62",
        "2.3.63",
        "2.3.64",
        "2.3.66",
        "2.3.72",
        "2.3.73",
    )

    private data class AdhikaraDomain(
        val number: String,
        val role: SutraRole.Adhikara,
    )

    private val activeAdhikaraDomains: List<AdhikaraDomain> by lazy {
        Ashtadhyayi.adhikaraSutras.mapNotNull { domain ->
            val role = domain.role as? SutraRole.Adhikara ?: return@mapNotNull null
            AdhikaraDomain(domain.number, role)
        }
    }

    /**
     * Applies only the adhikāras declared by the rule's canonical metadata.
     * A numeric range alone is insufficient because later rules such as 2.3.46
     * deliberately escape 2.3.1 and license an abhihita nominative.
     */
    private fun isVibhaktiEligible(
        rule: Sutra<*, *>,
        context: VibhaktiRuleContext,
    ): Boolean {
        return activeAdhikaraDomains.all { domain ->
            if (domain.number in rule.adhikara) {
                domain.role.isContextEligible(context)
            } else {
                true
            }
        }
    }
}
