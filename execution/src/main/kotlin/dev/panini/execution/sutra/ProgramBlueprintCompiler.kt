package dev.panini.execution.sutra

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.execution.AmbiguousKarakaBinding
import dev.panini.execution.DhatuInvocation
import dev.panini.execution.ExecutableUkti
import dev.panini.execution.GrammaticalFeatures
import dev.panini.execution.Polarity
import dev.panini.execution.VakyaPrayojana
import dev.panini.sutra.runtime.RuntimeSutra
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraBlueprint
import dev.panini.sutra.runtime.SutraBlueprintValidator
import dev.panini.sutra.runtime.SutraNirnaya
import dev.panini.sutra.runtime.SutraRelation

data class ProgramBlueprintContext(
    val speaker: String,
    val listener: String,
    val text: String,
    val prayojana: VakyaPrayojana = VakyaPrayojana.AJNA,
    val polarity: Polarity = Polarity.POSITIVE,
    val lakara: Lakara? = null,
)

enum class ProgramBlueprintDiagnosticCode {
    INVALID_GRANTHA,
    INVALID_BLUEPRINT,
    UNSUPPORTED_ARTHA,
    MISSING_FIELD,
    INVALID_FIELD,
    UNKNOWN_DHATU,
    AMBIGUOUS_DHATU,
    PREREQUISITE_MISMATCH,
}

data class ProgramBlueprintDiagnostic(
    val code: ProgramBlueprintDiagnosticCode,
    val message: String,
)

sealed interface ProgramBlueprintCompilation {
    data class Success(
        val sutra: RuntimeSutra<ProgramAvastha>,
        val invocation: DhatuInvocation,
    ) : ProgramBlueprintCompilation

    data class Invalid(
        val diagnostics: List<ProgramBlueprintDiagnostic>,
    ) : ProgramBlueprintCompilation
}

object ProgramBlueprintCompiler {
    fun compile(
        blueprint: SutraBlueprint,
        context: ProgramBlueprintContext,
    ): ProgramBlueprintCompilation {
        val diagnostics = SutraBlueprintValidator.validate(blueprint).map {
            ProgramBlueprintDiagnostic(
                ProgramBlueprintDiagnosticCode.INVALID_BLUEPRINT,
                it.message,
            )
        }.toMutableList()
        if (blueprint.artha.kind != "kriya") {
            diagnostics += ProgramBlueprintDiagnostic(
                ProgramBlueprintDiagnosticCode.UNSUPPORTED_ARTHA,
                "Program compiler supports kriya blueprints, not '${blueprint.artha.kind}'.",
            )
            return ProgramBlueprintCompilation.Invalid(diagnostics)
        }

        val fields = blueprint.artha.fields
        val dhatuId = (fields["dhatuId"] as? SutraArthaValue.Symbol)?.name
        val dhatuSurface = (fields["dhatu"] as? SutraArthaValue.Text)?.value
        if (dhatuSurface == null) {
            diagnostics += ProgramBlueprintDiagnostic(
                ProgramBlueprintDiagnosticCode.MISSING_FIELD,
                "Kriyā blueprint ${blueprint.id} requires text field 'dhatu'.",
            )
        }
        val upadesha = (fields["upadesha"] as? SutraArthaValue.Text)?.value
        val candidates = upadesha
            ?.let(dev.panini.dhatupatha.DhatuPathaRegistration::resolve)
            .orEmpty()
            .filter { dhatu -> dhatuId == null || dhatu.id == dhatuId }
        when {
            upadesha == null -> diagnostics += ProgramBlueprintDiagnostic(
                ProgramBlueprintDiagnosticCode.MISSING_FIELD,
                "Kriyā blueprint ${blueprint.id} requires exact text field 'upadesha'.",
            )
            candidates.isEmpty() ->
                diagnostics += ProgramBlueprintDiagnostic(
                    ProgramBlueprintDiagnosticCode.UNKNOWN_DHATU,
                    "No executable dhātu matches id '$dhatuId' and exact upadeśa '$upadesha'.",
                )
            candidates.size > 1 -> diagnostics += ProgramBlueprintDiagnostic(
                ProgramBlueprintDiagnosticCode.AMBIGUOUS_DHATU,
                "More than one dhātu matches exact upadeśa '$upadesha'; include dhātu id.",
            )
        }

        val bindings = linkedMapOf<Karaka, dev.panini.execution.ExecutionExpression>()
        val karakaRecord = fields["karakas"] as? SutraArthaValue.Record
        if (karakaRecord == null) {
            diagnostics += ProgramBlueprintDiagnostic(
                ProgramBlueprintDiagnosticCode.MISSING_FIELD,
                "Kriyā blueprint ${blueprint.id} requires record field 'karakas'.",
            )
        } else {
            karakaRecord.fields.forEach { (name, value) ->
                val karaka = runCatching { Karaka.valueOf(name) }.getOrNull()
                if (karaka == null) {
                    diagnostics += ProgramBlueprintDiagnostic(
                        ProgramBlueprintDiagnosticCode.INVALID_FIELD,
                        "Unknown kāraka '$name' in blueprint ${blueprint.id}.",
                    )
                } else {
                    runCatching { ProgramSutraArthaCodec.decodeExpression(value) }
                        .onSuccess { bindings[karaka] = it }
                        .onFailure {
                            diagnostics += ProgramBlueprintDiagnostic(
                                ProgramBlueprintDiagnosticCode.INVALID_FIELD,
                                "Invalid expression for kāraka '$name': ${it.message}",
                            )
                        }
                }
            }
        }

        val relationPrerequisites = blueprint.relations
            .filterIsInstance<SutraRelation.DependsOn>()
            .mapTo(linkedSetOf()) { it.prerequisite }
        val arthaPrerequisites = (fields["prerequisites"] as? SutraArthaValue.Sequence)
            ?.values
            ?.mapNotNull { (it as? SutraArthaValue.SutraReference)?.id }
            ?.toSet()
        if (arthaPrerequisites != null && arthaPrerequisites != relationPrerequisites) {
            diagnostics += ProgramBlueprintDiagnostic(
                ProgramBlueprintDiagnosticCode.PREREQUISITE_MISMATCH,
                "Blueprint ${blueprint.id} has inconsistent prerequisite semantics and relations.",
            )
        }
        val blueprintLakaraName = (fields["lakara"] as? SutraArthaValue.Symbol)?.name
        val blueprintLakara = blueprintLakaraName?.let { name ->
            runCatching { Lakara.valueOf(name) }.getOrElse {
                diagnostics += ProgramBlueprintDiagnostic(
                    ProgramBlueprintDiagnosticCode.INVALID_FIELD,
                    "Unknown lakāra '$name' in blueprint ${blueprint.id}.",
                )
                null
            }
        }
        val metadata = fields.decodeTextRecord("metadata", diagnostics, blueprint)
        val ambiguousBindings = fields.decodeAmbiguousBindings(diagnostics, blueprint)
        val karakaTrace = fields.decodeTextSequence("karakaEvidence", diagnostics, blueprint)
        if (diagnostics.isNotEmpty()) return ProgramBlueprintCompilation.Invalid(diagnostics)

        val invocation = DhatuInvocation(
            id = blueprint.id.value,
            dhatu = candidates.single(),
            bindings = bindings,
            selectedOperation = (fields["operation"] as? SutraArthaValue.Symbol)?.name,
            metadata = metadata,
            grammaticalFeatures = GrammaticalFeatures(
                upasargas = fields.symbolSet("upasargas"),
                sanadi = fields.symbolSet("sanadi"),
                avyayas = fields.symbolSet("avyayas"),
                lakara = blueprintLakara,
            ),
            ambiguousBindings = ambiguousBindings,
            karakaTrace = karakaTrace,
        )
        val ukti = ExecutableUkti(
            speaker = context.speaker,
            listener = context.listener,
            text = context.text,
            prayojana = context.prayojana,
            polarity = context.polarity,
            lakara = context.lakara,
            invocations = listOf(invocation),
            dependencies = emptySet(),
        )
        return ProgramBlueprintCompilation.Success(
            sutra = RuntimeSutra(
                id = blueprint.id,
                source = blueprint.source,
                role = blueprint.role,
                artha = blueprint.artha,
                relations = blueprint.relations,
                governance = blueprint.governance,
                evaluator = { _, state ->
                    when {
                        state.halted -> SutraNirnaya.NotApplicable(
                            listOf("Program execution has been suspended or terminated."),
                        )
                        blueprint.id in state.completedSutras -> SutraNirnaya.NotApplicable(
                            listOf("The sūtra has already been applied."),
                        )
                        relationPrerequisites.any { it !in state.completedSutras } -> {
                            val missing = relationPrerequisites.first { it !in state.completedSutras }
                            SutraNirnaya.Blocked(
                                missing,
                                listOf("A prerequisite sūtra has not completed."),
                            )
                        }
                        else -> SutraNirnaya.Applicable(
                            listOf(InvokeDhatuEffect(invocation, ukti)),
                            listOf("The compiled kriyā blueprint is ready."),
                        )
                    }
                },
            ),
            invocation = invocation,
        )
    }

    private fun Map<String, SutraArthaValue>.symbolSet(
        field: String,
    ): Set<String> = (get(field) as? SutraArthaValue.Sequence)
        ?.values
        ?.mapNotNull { (it as? SutraArthaValue.Symbol)?.name }
        ?.toSet()
        .orEmpty()

    private fun Map<String, SutraArthaValue>.decodeTextRecord(
        field: String,
        diagnostics: MutableList<ProgramBlueprintDiagnostic>,
        blueprint: SutraBlueprint,
    ): Map<String, String> {
        val value = get(field) ?: return emptyMap()
        val record = value as? SutraArthaValue.Record
        if (record == null) {
            diagnostics += invalidField(blueprint, "'$field' must be a record of text values.")
            return emptyMap()
        }
        return buildMap {
            record.fields.forEach { (name, entry) ->
                val text = (entry as? SutraArthaValue.Text)?.value
                if (text == null) {
                    diagnostics += invalidField(
                        blueprint,
                        "Entry '$name' in '$field' must be text.",
                    )
                } else {
                    put(name, text)
                }
            }
        }
    }

    private fun Map<String, SutraArthaValue>.decodeTextSequence(
        field: String,
        diagnostics: MutableList<ProgramBlueprintDiagnostic>,
        blueprint: SutraBlueprint,
    ): List<String> {
        val value = get(field) ?: return emptyList()
        val sequence = value as? SutraArthaValue.Sequence
        if (sequence == null) {
            diagnostics += invalidField(blueprint, "'$field' must be a sequence of text values.")
            return emptyList()
        }
        return sequence.values.mapIndexedNotNull { index, entry ->
            val text = (entry as? SutraArthaValue.Text)?.value
            if (text == null) {
                diagnostics += invalidField(
                    blueprint,
                    "Entry $index in '$field' must be text.",
                )
            }
            text
        }
    }

    private fun Map<String, SutraArthaValue>.decodeAmbiguousBindings(
        diagnostics: MutableList<ProgramBlueprintDiagnostic>,
        blueprint: SutraBlueprint,
    ): List<AmbiguousKarakaBinding> {
        val value = get("ambiguousKarakas") ?: return emptyList()
        val sequence = value as? SutraArthaValue.Sequence
        if (sequence == null) {
            diagnostics += invalidField(
                blueprint,
                "'ambiguousKarakas' must be a sequence.",
            )
            return emptyList()
        }
        return sequence.values.mapIndexedNotNull { index, entry ->
            runCatching { ProgramSutraArthaCodec.decodeAmbiguousBinding(entry) }
                .onFailure {
                    diagnostics += invalidField(
                        blueprint,
                        "Invalid ambiguous kāraka at index $index: ${it.message}",
                    )
                }
                .getOrNull()
        }
    }

    private fun invalidField(
        blueprint: SutraBlueprint,
        message: String,
    ) = ProgramBlueprintDiagnostic(
        ProgramBlueprintDiagnosticCode.INVALID_FIELD,
        "Blueprint ${blueprint.id}: $message",
    )
}
