package dev.panini.execution.sutra

import dev.panini.execution.ExecutableUkti
import dev.panini.execution.VakyaPrayojana
import dev.panini.sutra.SutraRole
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraBlueprint
import dev.panini.sutra.runtime.SutraBlueprintGrantha
import dev.panini.sutra.runtime.SutraBlueprintGranthaValidator
import dev.panini.sutra.runtime.SutraGrantha
import dev.panini.sutra.runtime.SutraGranthaCompiler
import dev.panini.sutra.runtime.SutraGranthaLowering
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraProgram
import dev.panini.sutra.runtime.SutraRelation
import dev.panini.sutra.runtime.SutraSource

sealed interface ProgramGranthaCompilation {
    data class Success(
        val grantha: SutraGrantha<ProgramAvastha>,
    ) : ProgramGranthaCompilation

    data class Invalid(
        val diagnostics: List<ProgramBlueprintDiagnostic>,
    ) : ProgramGranthaCompilation
}

/**
 * Compatibility compiler used during the incremental migration. It preserves
 * the current binding model while normalizing each dhātu occurrence to one
 * runtime sūtra.
 */
object ExecutableUktiSutraCompiler {
    fun compile(ukti: ExecutableUkti): SutraProgram<ProgramAvastha> {
        return when (val lowering = SutraGranthaCompiler.lower(compileGrantha(ukti))) {
            is SutraGranthaLowering.Success -> lowering.program
            is SutraGranthaLowering.Invalid -> error(
                lowering.diagnostics.joinToString(separator = "\n") { it.message },
            )
        }
    }

    fun compileGrantha(ukti: ExecutableUkti): SutraGrantha<ProgramAvastha> =
        when (val compilation = compileGranthaResult(ukti)) {
            is ProgramGranthaCompilation.Success -> compilation.grantha
            is ProgramGranthaCompilation.Invalid -> error(
                compilation.diagnostics.joinToString(separator = "\n") { it.message },
            )
        }

    fun compileGranthaResult(ukti: ExecutableUkti): ProgramGranthaCompilation {
        val blueprintGrantha = compileBlueprintGrantha(ukti)
        val granthaValidation = SutraBlueprintGranthaValidator.validate(blueprintGrantha)
        if (!granthaValidation.isValid) {
            return ProgramGranthaCompilation.Invalid(
                granthaValidation.diagnostics.map {
                    ProgramBlueprintDiagnostic(
                        ProgramBlueprintDiagnosticCode.INVALID_GRANTHA,
                        it.message,
                    )
                },
            )
        }
        val context = ProgramBlueprintContext(
            speaker = ukti.speaker,
            listener = ukti.listener,
            text = ukti.text,
            prayojana = ukti.prayojana,
            polarity = ukti.polarity,
            lakara = ukti.lakara,
        )
        val diagnostics = mutableListOf<ProgramBlueprintDiagnostic>()
        val sutras = blueprintGrantha.sutras.mapNotNull { blueprint ->
            when (val compilation = ProgramBlueprintCompiler.compile(blueprint, context)) {
                is ProgramBlueprintCompilation.Success -> compilation.sutra
                is ProgramBlueprintCompilation.Invalid -> {
                    diagnostics += compilation.diagnostics
                    null
                }
            }
        }
        if (diagnostics.isNotEmpty()) return ProgramGranthaCompilation.Invalid(diagnostics)
        return ProgramGranthaCompilation.Success(
            SutraGrantha(
                id = blueprintGrantha.id,
                sutras = sutras,
                imports = blueprintGrantha.imports,
                adhikaras = blueprintGrantha.adhikaras,
                samjnas = blueprintGrantha.samjnas,
                exports = blueprintGrantha.exports,
            ),
        )
    }

    fun compileBlueprintGrantha(ukti: ExecutableUkti): SutraBlueprintGrantha {
        val dependenciesByTarget = ukti.dependencies.groupBy { it.after }
        val sutras = ukti.invocations.mapIndexed { index, invocation ->
            val id = SutraId(invocation.id)
            val prerequisites = dependenciesByTarget[invocation.id]
                .orEmpty()
                .mapTo(linkedSetOf()) { SutraId(it.before) }
            SutraBlueprint(
                id = id,
                source = SutraSource.Vakya(
                    uktiId = "ukti",
                    vakyaIndex = index,
                    text = ukti.text,
                ),
                role = when (ukti.prayojana) {
                    VakyaPrayojana.NISHEDHA -> SutraRole.Nishedha
                    else -> SutraRole.Vidhi
                },
                artha = SutraArtha(
                    kind = "kriya",
                    fields = buildMap {
                        put("dhatu", SutraArthaValue.Text(invocation.dhatu.sourceSurface))
                        put("upadesha", SutraArthaValue.Text(invocation.dhatu.upadesha))
                        invocation.selectedOperation?.let {
                            put("operation", SutraArthaValue.Symbol(it))
                        }
                        put(
                            "metadata",
                            SutraArthaValue.Record(
                                invocation.metadata.mapValues {
                                    SutraArthaValue.Text(it.value)
                                },
                            ),
                        )
                        put(
                            "karakas",
                            SutraArthaValue.Record(
                                invocation.bindings.mapKeys { it.key.name }
                                    .mapValues {
                                        ProgramSutraArthaCodec.encodeExpression(it.value)
                                },
                            ),
                        )
                        put(
                            "ambiguousKarakas",
                            SutraArthaValue.Sequence(
                                invocation.ambiguousBindings.map(
                                    ProgramSutraArthaCodec::encodeAmbiguousBinding,
                                ),
                            ),
                        )
                        put(
                            "karakaEvidence",
                            SutraArthaValue.Sequence(
                                invocation.karakaTrace.map(SutraArthaValue::Text),
                            ),
                        )
                        put(
                            "upasargas",
                            invocation.grammaticalFeatures.upasargas.toArthaSequence(),
                        )
                        put(
                            "sanadi",
                            invocation.grammaticalFeatures.sanadi.toArthaSequence(),
                        )
                        put(
                            "avyayas",
                            invocation.grammaticalFeatures.avyayas.toArthaSequence(),
                        )
                        invocation.grammaticalFeatures.lakara?.let {
                            put("lakara", SutraArthaValue.Symbol(it.name))
                        }
                        put(
                            "prerequisites",
                            SutraArthaValue.Sequence(
                                prerequisites.map(SutraArthaValue::SutraReference),
                            ),
                        )
                    },
                ),
                relations = prerequisites.mapTo(linkedSetOf()) { SutraRelation.DependsOn(it) },
            )
        }
        return SutraBlueprintGrantha(
            id = GranthaId("ukti"),
            sutras = sutras,
            exports = sutras.mapTo(linkedSetOf()) { it.id },
        )
    }

    private fun Set<String>.toArthaSequence(): SutraArthaValue.Sequence =
        map { SutraArthaValue.Symbol(it) }.let(SutraArthaValue::Sequence)
}
