package dev.panini.derivation.sutra

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.ContextualSamjnaAssignmentArtha
import dev.panini.sutra.ContextualProhibitionArtha
import dev.panini.sutra.InterpretivePrincipleArtha
import dev.panini.sutra.ProhibitionTarget
import dev.panini.sutra.SamjnaAssignmentTarget
import dev.panini.sutra.SamjnaDefinitionArtha
import dev.panini.sutra.SamjnaSetDefinitionArtha
import dev.panini.sutra.SamjniSetDefinitionArtha
import dev.panini.sutra.runtime.RuntimeSutra
import dev.panini.sutra.runtime.SutraBlueprint
import dev.panini.sutra.runtime.SutraNirnaya

/** Lowers evaluator-free grammatical meanings into derivation runtime rules. */
object DerivationBlueprintCompiler {
    fun compile(blueprint: SutraBlueprint): RuntimeSutra<DerivationAvastha> =
        when (blueprint.artha.kind) {
            ContextualProhibitionArtha.KIND ->
                compileContextualProhibition(blueprint)
            ContextualSamjnaAssignmentArtha.KIND ->
                compileContextualSamjnaAssignment(blueprint)
            InterpretivePrincipleArtha.KIND -> compileInterpretivePrinciple(blueprint)
            SamjnaDefinitionArtha.KIND -> compileSamjnaDefinition(blueprint)
            SamjnaSetDefinitionArtha.KIND -> compileSamjnaSetDefinition(blueprint)
            SamjniSetDefinitionArtha.KIND -> compileSamjniSetDefinition(blueprint)
            else -> error(
                "Unsupported derivation blueprint meaning '${blueprint.artha.kind}' for ${blueprint.id}.",
            )
        }

    private fun compileContextualProhibition(
        blueprint: SutraBlueprint,
    ): RuntimeSutra<DerivationAvastha> {
        val artha = ContextualProhibitionArtha.fromSutraArtha(blueprint.artha)
        require(artha.target == ProhibitionTarget.VIBHAKTI_FINAL_TUSMA) {
            "Unsupported prohibition target '${artha.target}' for ${blueprint.id}."
        }

        return RuntimeSutra(
            id = blueprint.id,
            source = blueprint.source,
            role = blueprint.role,
            artha = blueprint.artha,
            evaluator = { _, state ->
                val derivation = state.derivation
                val matching = derivation.terms.any { term ->
                    derivation.samjnas.any {
                        it.targetId == term.id && it.samjna == dev.panini.shiksha.Samjna.PRATYAYA
                    } && term.surface.hasTusmaEnding()
                }
                if (!matching) {
                    SutraNirnaya.NotApplicable(
                        listOf("No vibhakti has a final tu-s-ma sound."),
                    )
                } else {
                    SutraNirnaya.Applicable(
                        effects = listOf(
                            ApplyDerivationChange(
                                sutraId = blueprint.id,
                                change = DerivationChange(
                                    state = derivation.blockSutra(
                                        artha.prohibitedSutra.value,
                                        blueprint.id.value,
                                    ),
                                    explanation =
                                        "${blueprint.id} prohibits ${artha.prohibitedSutra} for the matching vibhakti.",
                                ),
                            ),
                        ),
                        reasons = listOf(
                            "A vibhakti-final tu-s-ma sound triggers the prohibition.",
                        ),
                    )
                }
            },
            relations = blueprint.relations,
            governance = blueprint.governance,
        )
    }

    private fun compileContextualSamjnaAssignment(
        blueprint: SutraBlueprint,
    ): RuntimeSutra<DerivationAvastha> {
        val artha = ContextualSamjnaAssignmentArtha.fromSutraArtha(blueprint.artha)

        return RuntimeSutra(
            id = blueprint.id,
            source = blueprint.source,
            role = blueprint.role,
            artha = blueprint.artha,
            evaluator = { _, state ->
                val derivation = state.derivation
                val applicable =
                    when (artha.target) {
                        SamjnaAssignmentTarget.UPADESHA_NASALIZED_VOWEL ->
                            derivation.stage == DerivationStage.PRATYAYA_SELECTED &&
                                derivation.terms.any {
                                    it.surface.endsWith("ँ") && ItMarker.U !in it.itMarkers
                                }
                        SamjnaAssignmentTarget.UPADESHA_FINAL_CONSONANT ->
                            derivation.stage == DerivationStage.PRATYAYA_SELECTED &&
                                derivation.terms.any { term ->
                                    term.kind != TermKind.PRATIPADIKA &&
                                        term.id !in RESOLVED_AGAMAS &&
                                        term.surface.endsWith("्") &&
                                        term.surface.length >= 2 &&
                                        Varnamala.isConsonant(
                                            term.surface[term.surface.length - 2],
                                        ) &&
                                        term.itMarkers.isEmpty()
                                }
                        SamjnaAssignmentTarget.DHATU_UPADESHA_INITIAL_NI_TU_DU ->
                            derivation.stage == DerivationStage.INITIAL &&
                                derivation.terms.any { term ->
                                    term.kind == TermKind.DHATU &&
                                        term.initialNiTuDuMarker()
                                            ?.let { it !in term.itMarkers } == true
                                }
                        SamjnaAssignmentTarget.PRATYAYA_INITIAL_SSA ->
                            derivation.stage == DerivationStage.PRATYAYA_SELECTED &&
                                derivation.terms.any { term ->
                                    term.kind == TermKind.PRATYAYA &&
                                        term.surface.startsWith("ष") &&
                                        ItMarker.SH !in term.itMarkers
                                }
                        SamjnaAssignmentTarget.PRATYAYA_INITIAL_CU_TTU ->
                            derivation.stage == DerivationStage.PRATYAYA_SELECTED &&
                                derivation.terms.any { term ->
                                    term.kind == TermKind.PRATYAYA &&
                                        term.initialCuTtuMarker()
                                            ?.let { it !in term.itMarkers } == true
                                }
                        }
                if (!applicable) {
                    SutraNirnaya.NotApplicable(
                        listOf("No material matching ${artha.target} requires designation."),
                    )
                } else {
                    val changed = derivation.copy(
                        terms = derivation.terms.map { term ->
                            when (artha.target) {
                                SamjnaAssignmentTarget.UPADESHA_NASALIZED_VOWEL ->
                                    if (term.surface.endsWith("ँ")) {
                                        term.copy(itMarkers = term.itMarkers + ItMarker.U)
                                    } else {
                                        term
                                    }
                                SamjnaAssignmentTarget.UPADESHA_FINAL_CONSONANT -> {
                                    val isTarget =
                                        term.kind != TermKind.PRATIPADIKA &&
                                            term.id !in RESOLVED_AGAMAS &&
                                            term.surface.endsWith("्") &&
                                            term.surface.length >= 2 &&
                                            Varnamala.isConsonant(
                                                term.surface[term.surface.length - 2],
                                            ) &&
                                            term.itMarkers.isEmpty()
                                    if (isTarget) {
                                        term.copy(itMarkers = term.itMarkers + ItMarker.KIT)
                                    } else {
                                        term
                                    }
                                }
                                SamjnaAssignmentTarget.DHATU_UPADESHA_INITIAL_NI_TU_DU -> {
                                    val marker = if (term.kind == TermKind.DHATU) {
                                        term.initialNiTuDuMarker()
                                    } else {
                                        null
                                    }
                                    if (marker != null) {
                                        term.copy(itMarkers = term.itMarkers + marker)
                                    } else {
                                        term
                                    }
                                }
                                SamjnaAssignmentTarget.PRATYAYA_INITIAL_SSA ->
                                    if (
                                        term.kind == TermKind.PRATYAYA &&
                                        term.surface.startsWith("ष")
                                    ) {
                                        term.copy(itMarkers = term.itMarkers + ItMarker.SH)
                                    } else {
                                        term
                                    }
                                SamjnaAssignmentTarget.PRATYAYA_INITIAL_CU_TTU -> {
                                    val marker = if (term.kind == TermKind.PRATYAYA) {
                                        term.initialCuTtuMarker()
                                    } else {
                                        null
                                    }
                                    if (marker != null) {
                                        term.copy(itMarkers = term.itMarkers + marker)
                                    } else {
                                        term
                                    }
                                }
                            }
                        },
                    )
                    SutraNirnaya.Applicable(
                        effects = listOf(
                            ApplyDerivationChange(
                                sutraId = blueprint.id,
                                change = DerivationChange(
                                    state = changed,
                                    explanation =
                                        "${blueprint.id} assigns ${artha.samjna} to the matching upadeśa sound.",
                                ),
                            ),
                        ),
                        reasons = listOf(
                            "A nasalized vowel in upadeśa requires the ${artha.samjna} designation.",
                        ),
                    )
                }
            },
            relations = blueprint.relations,
            governance = blueprint.governance,
        )
    }

    private val RESOLVED_AGAMAS = setOf("siyut", "yasut", "vuk", "nic")

    private fun dev.panini.derivation.DerivationTerm.initialNiTuDuMarker(): ItMarker? =
        when {
            surface.startsWith("ञि") -> ItMarker.KIT
            surface.startsWith("टु") -> ItMarker.T
            surface.startsWith("डु") -> ItMarker.KIT
            else -> null
        }

    private fun dev.panini.derivation.DerivationTerm.initialCuTtuMarker(): ItMarker? =
        when (surface.firstOrNull()) {
            in CU_INITIALS -> ItMarker.J
            in TTU_INITIALS -> ItMarker.T
            else -> null
        }

    private val CU_INITIALS = setOf('च', 'छ', 'ज', 'झ', 'ञ')
    private val TTU_INITIALS = setOf('ट', 'ठ', 'ड', 'ढ', 'ण')

    private fun String.hasTusmaEnding(): Boolean =
        TUSMA_ENDINGS.any(::endsWith)

    private val TUSMA_ENDINGS = setOf(
        "त्", "थ्", "द्", "ध्", "न्", "स्", "म्",
        "त", "थ", "द", "ध", "न", "स", "म",
    )

    private fun compileInterpretivePrinciple(
        blueprint: SutraBlueprint,
    ): RuntimeSutra<DerivationAvastha> {
        val artha = InterpretivePrincipleArtha.fromSutraArtha(blueprint.artha)
        val definition = InterpretivePrincipleDefinition(
            principle = artha.principle,
            definingSutra = blueprint.id,
        )

        return RuntimeSutra(
            id = blueprint.id,
            source = blueprint.source,
            role = blueprint.role,
            artha = blueprint.artha,
            evaluator = { _, state ->
                if (definition in state.interpretivePrinciples) {
                    SutraNirnaya.NotApplicable(
                        listOf("The interpretive principle is already registered."),
                    )
                } else {
                    SutraNirnaya.Applicable(
                        effects = listOf(DefineInterpretivePrinciple(definition)),
                        reasons = listOf("The paribhāṣā establishes an interpretation principle."),
                    )
                }
            },
            relations = blueprint.relations,
            governance = blueprint.governance,
        )
    }

    private fun compileSamjnaDefinition(
        blueprint: SutraBlueprint,
    ): RuntimeSutra<DerivationAvastha> {
        val artha = SamjnaDefinitionArtha.fromSutraArtha(blueprint.artha)
        val definition = SamjnaDefinition(
            samjni = artha.samjni,
            samjna = artha.samjna,
            definingSutra = blueprint.id,
        )

        return RuntimeSutra(
            id = blueprint.id,
            source = blueprint.source,
            role = blueprint.role,
            artha = blueprint.artha,
            evaluator = { _, state ->
                if (definition in state.samjnaDefinitions) {
                    SutraNirnaya.NotApplicable(
                        listOf("The saṃjñā definition is already registered."),
                    )
                } else {
                    SutraNirnaya.Applicable(
                        effects = listOf(DefineSamjna(definition)),
                        reasons = listOf("The interpretive saṃjñā sūtra defines a technical term."),
                    )
                }
            },
            relations = blueprint.relations,
            governance = blueprint.governance,
        )
    }

    private fun compileSamjnaSetDefinition(
        blueprint: SutraBlueprint,
    ): RuntimeSutra<DerivationAvastha> {
        val artha = SamjnaSetDefinitionArtha.fromSutraArtha(blueprint.artha)
        val definitions = artha.samjnas.mapTo(linkedSetOf()) { samjna ->
            SamjnaDefinition(
                samjni = artha.samjni,
                samjna = samjna,
                definingSutra = blueprint.id,
            )
        }

        return RuntimeSutra(
            id = blueprint.id,
            source = blueprint.source,
            role = blueprint.role,
            artha = blueprint.artha,
            evaluator = { _, state ->
                val missing = definitions - state.samjnaDefinitions
                if (missing.isEmpty()) {
                    SutraNirnaya.NotApplicable(
                        listOf("The saṃjñā-set definition is already registered."),
                    )
                } else {
                    SutraNirnaya.Applicable(
                        effects = missing.map { DefineSamjna(it) },
                        reasons = listOf(
                            "The interpretive saṃjñā sūtra defines a set of technical terms.",
                        ),
                    )
                }
            },
            relations = blueprint.relations,
            governance = blueprint.governance,
        )
    }

    private fun compileSamjniSetDefinition(
        blueprint: SutraBlueprint,
    ): RuntimeSutra<DerivationAvastha> {
        val artha = SamjniSetDefinitionArtha.fromSutraArtha(blueprint.artha)
        val definitions = artha.samjnis.mapTo(linkedSetOf()) { samjni ->
            SamjnaDefinition(
                samjni = samjni,
                samjna = artha.samjna,
                definingSutra = blueprint.id,
            )
        }

        return RuntimeSutra(
            id = blueprint.id,
            source = blueprint.source,
            role = blueprint.role,
            artha = blueprint.artha,
            evaluator = { _, state ->
                val missing = definitions - state.samjnaDefinitions
                if (missing.isEmpty()) {
                    SutraNirnaya.NotApplicable(
                        listOf("The saṃjñin-set definition is already registered."),
                    )
                } else {
                    SutraNirnaya.Applicable(
                        effects = missing.map { DefineSamjna(it) },
                        reasons = listOf(
                            "The interpretive saṃjñā sūtra assigns one technical term to a set of concepts.",
                        ),
                    )
                }
            },
            relations = blueprint.relations,
            governance = blueprint.governance,
        )
    }
}
