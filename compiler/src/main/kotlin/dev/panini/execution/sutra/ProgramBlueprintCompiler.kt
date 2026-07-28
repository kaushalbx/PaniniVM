package dev.panini.execution.sutra

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.dhatupatha.DhatuPatha
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
        val dhatuSurface = (fields["dhatu"] as? SutraArthaValue.Text)?.value
        if (dhatuSurface == null) {
            diagnostics += ProgramBlueprintDiagnostic(
                ProgramBlueprintDiagnosticCode.MISSING_FIELD,
                "Kriyā blueprint ${blueprint.id} requires text field 'dhatu'.",
            )
        }
        val upadesha = (fields["upadesha"] as? SutraArthaValue.Text)?.value
        val candidates = dhatuSurface?.let { surface ->
            DhatuPatha.all.filter {
                it.sourceSurface == surface && (upadesha == null || it.upadesha == upadesha)
            }
        }.orEmpty()
        when {
            dhatuSurface != null && candidates.isEmpty() -> diagnostics += ProgramBlueprintDiagnostic(
                ProgramBlueprintDiagnosticCode.UNKNOWN_DHATU,
                "No executable dhātu matches surface '$dhatuSurface' and upadeśa '$upadesha'.",
            )
            candidates.size > 1 -> diagnostics += ProgramBlueprintDiagnostic(
                ProgramBlueprintDiagnosticCode.AMBIGUOUS_DHATU,
                "More than one dhātu matches surface '$dhatuSurface'; include exact upadeśa.",
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
        if (diagnostics.isNotEmpty()) return ProgramBlueprintCompilation.Invalid(diagnostics)

        val invocation = DhatuInvocation(
            id = blueprint.id.value,
            dhatu = candidates.single(),
            bindings = bindings,
            selectedOperation = (fields["operation"] as? SutraArthaValue.Symbol)?.name,
            grammaticalFeatures = GrammaticalFeatures(
                upasargas = fields.symbolSet("upasargas"),
                sanadi = fields.symbolSet("sanadi"),
                avyayas = fields.symbolSet("avyayas"),
                lakara = blueprintLakara,
            ),
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
            RuntimeSutra(
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
        )
    }

    private fun Map<String, SutraArthaValue>.symbolSet(
        field: String,
    ): Set<String> = (get(field) as? SutraArthaValue.Sequence)
        ?.values
        ?.mapNotNull { (it as? SutraArthaValue.Symbol)?.name }
        ?.toSet()
        .orEmpty()
}
