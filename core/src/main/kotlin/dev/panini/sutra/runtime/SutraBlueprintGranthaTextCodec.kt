package dev.panini.sutra.runtime

enum class SutraBlueprintGranthaTextDiagnosticCode {
    INVALID_ARTHA_TEXT,
    INVALID_SCHEMA,
    INVALID_BLUEPRINT,
    INVALID_GRANTHA,
}

data class SutraBlueprintGranthaTextDiagnostic(
    val code: SutraBlueprintGranthaTextDiagnosticCode,
    val message: String,
    val position: Int? = null,
)

sealed interface SutraBlueprintGranthaTextEncoding {
    data class Success(val text: String) : SutraBlueprintGranthaTextEncoding
    data class Invalid(
        val diagnostics: List<SutraBlueprintGranthaTextDiagnostic>,
    ) : SutraBlueprintGranthaTextEncoding
}

sealed interface SutraBlueprintGranthaTextDecoding {
    data class Success(val grantha: SutraBlueprintGrantha) : SutraBlueprintGranthaTextDecoding
    data class Invalid(
        val diagnostics: List<SutraBlueprintGranthaTextDiagnostic>,
    ) : SutraBlueprintGranthaTextDecoding
}

/** Canonical source container for segmented evaluator-free sūtra software. */
object SutraBlueprintGranthaTextCodec {
    fun encode(grantha: SutraBlueprintGrantha): SutraBlueprintGranthaTextEncoding {
        val diagnostics = mutableListOf<SutraBlueprintGranthaTextDiagnostic>()
        val sutras = grantha.sutras.mapIndexedNotNull { index, blueprint ->
            when (val encoded = SutraBlueprintTextCodec.encode(blueprint)) {
                is SutraBlueprintTextEncoding.Success -> SutraArthaValue.Text(encoded.text)
                is SutraBlueprintTextEncoding.Invalid -> {
                    diagnostics += encoded.diagnostics.map {
                        SutraBlueprintGranthaTextDiagnostic(
                            SutraBlueprintGranthaTextDiagnosticCode.INVALID_BLUEPRINT,
                            "Sūtra $index (${blueprint.id}): ${it.message}",
                            it.position,
                        )
                    }
                    null
                }
            }
        }
        if (diagnostics.isNotEmpty()) {
            return SutraBlueprintGranthaTextEncoding.Invalid(diagnostics)
        }

        val value = record(
            "id" to text(grantha.id.value),
            "imports" to SutraArthaValue.Sequence(
                grantha.imports.sortedBy { it.alias }.map {
                    record(
                        "grantha" to text(it.grantha.value),
                        "alias" to text(it.alias),
                    )
                },
            ),
            "adhikaras" to SutraArthaValue.Sequence(
                grantha.adhikaras.sortedBy { it.sutraId.value }.map {
                    record(
                        "sutra" to text(it.sutraId.value),
                        "members" to SutraArthaValue.Sequence(
                            it.members.sortedBy(SutraId::value).map { member ->
                                text(member.value)
                            },
                        ),
                    )
                },
            ),
            "samjnas" to SutraArthaValue.Sequence(
                grantha.samjnas.sortedBy { it.name }.map {
                    record(
                        *buildList<Pair<String, SutraArthaValue>> {
                            add("name" to text(it.name))
                            it.description?.let { description ->
                                add("description" to text(description))
                            }
                        }.toTypedArray(),
                    )
                },
            ),
            "exports" to SutraArthaValue.Sequence(
                grantha.exports.sortedBy(SutraId::value).map { text(it.value) },
            ),
            "sutras" to SutraArthaValue.Sequence(sutras),
        )
        return SutraBlueprintGranthaTextEncoding.Success(SutraArthaTextCodec.encode(value))
    }

    fun decode(source: String): SutraBlueprintGranthaTextDecoding {
        val decoded = when (val artha = SutraArthaTextCodec.decode(source)) {
            is SutraArthaTextDecoding.Success -> artha.value
            is SutraArthaTextDecoding.Invalid -> {
                return SutraBlueprintGranthaTextDecoding.Invalid(
                    artha.diagnostics.map {
                        SutraBlueprintGranthaTextDiagnostic(
                            SutraBlueprintGranthaTextDiagnosticCode.INVALID_ARTHA_TEXT,
                            it.message,
                            it.position,
                        )
                    },
                )
            }
        }
        return try {
            val root = decoded.record("grantha")
            val diagnostics = mutableListOf<SutraBlueprintGranthaTextDiagnostic>()
            val sutras = root.sequence("sutras").mapIndexedNotNull { index, value ->
                val blueprintSource = (value as? SutraArthaValue.Text)?.value
                if (blueprintSource == null) {
                    diagnostics += invalidBlueprint(index, "Blueprint source must be text.")
                    null
                } else {
                    when (val blueprint = SutraBlueprintTextCodec.decode(blueprintSource)) {
                        is SutraBlueprintTextDecoding.Success -> blueprint.blueprint
                        is SutraBlueprintTextDecoding.Invalid -> {
                            diagnostics += blueprint.diagnostics.map {
                                invalidBlueprint(index, it.message, it.position)
                            }
                            null
                        }
                    }
                }
            }
            if (diagnostics.isNotEmpty()) {
                return SutraBlueprintGranthaTextDecoding.Invalid(diagnostics)
            }
            val grantha = SutraBlueprintGrantha(
                id = GranthaId(root.text("id")),
                sutras = sutras,
                imports = root.sequence("imports").map { value ->
                    val entry = value.record("import")
                    GranthaImport(
                        GranthaId(entry.text("grantha")),
                        entry.text("alias"),
                    )
                },
                adhikaras = root.sequence("adhikaras").map { value ->
                    val entry = value.record("adhikara")
                    AdhikaraDeclaration(
                        SutraId(entry.text("sutra")),
                        entry.sequence("members").mapTo(linkedSetOf()) {
                            SutraId(it.textValue("adhikara member"))
                        },
                    )
                },
                samjnas = root.sequence("samjnas").map { value ->
                    val entry = value.record("samjna")
                    SamjnaDeclaration(
                        entry.text("name"),
                        (entry["description"] as? SutraArthaValue.Text)?.value,
                    )
                },
                exports = root.sequence("exports").mapTo(linkedSetOf()) {
                    SutraId(it.textValue("export"))
                },
            )
            val validation = SutraBlueprintGranthaValidator.validate(grantha)
            if (validation.isValid) {
                SutraBlueprintGranthaTextDecoding.Success(grantha)
            } else {
                SutraBlueprintGranthaTextDecoding.Invalid(
                    validation.diagnostics.map {
                        SutraBlueprintGranthaTextDiagnostic(
                            SutraBlueprintGranthaTextDiagnosticCode.INVALID_GRANTHA,
                            it.message,
                        )
                    },
                )
            }
        } catch (invalid: IllegalArgumentException) {
            SutraBlueprintGranthaTextDecoding.Invalid(
                listOf(
                    SutraBlueprintGranthaTextDiagnostic(
                        SutraBlueprintGranthaTextDiagnosticCode.INVALID_SCHEMA,
                        invalid.message ?: "Invalid grantha schema.",
                    ),
                ),
            )
        }
    }

    private fun invalidBlueprint(
        index: Int,
        message: String,
        position: Int? = null,
    ) = SutraBlueprintGranthaTextDiagnostic(
        SutraBlueprintGranthaTextDiagnosticCode.INVALID_BLUEPRINT,
        "Sūtra $index: $message",
        position,
    )

    private fun SutraArthaValue.record(label: String): Map<String, SutraArthaValue> =
        (this as? SutraArthaValue.Record)?.fields
            ?: throw IllegalArgumentException("$label must be a record.")

    private fun SutraArthaValue.textValue(label: String): String =
        (this as? SutraArthaValue.Text)?.value
            ?: throw IllegalArgumentException("$label must be text.")

    private fun Map<String, SutraArthaValue>.text(name: String): String =
        get(name)?.textValue("Grantha field '$name'")
            ?: throw IllegalArgumentException("Missing grantha field '$name'.")

    private fun Map<String, SutraArthaValue>.sequence(name: String): List<SutraArthaValue> =
        (get(name) as? SutraArthaValue.Sequence)?.values
            ?: throw IllegalArgumentException("Grantha field '$name' must be a sequence.")

    private fun record(
        vararg fields: Pair<String, SutraArthaValue>,
    ) = SutraArthaValue.Record(mapOf(*fields))

    private fun text(value: String) = SutraArthaValue.Text(value)
}
