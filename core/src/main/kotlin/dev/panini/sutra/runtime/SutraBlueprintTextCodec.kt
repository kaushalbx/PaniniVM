package dev.panini.sutra.runtime

import dev.panini.sutra.ParibhashaScope
import dev.panini.sutra.SutraGovernance
import dev.panini.sutra.SutraPriority
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraVisibility

enum class SutraBlueprintTextDiagnosticCode {
    INVALID_ARTHA_TEXT,
    INVALID_SCHEMA,
    UNSUPPORTED_ROLE,
}

data class SutraBlueprintTextDiagnostic(
    val code: SutraBlueprintTextDiagnosticCode,
    val message: String,
    val position: Int? = null,
)

sealed interface SutraBlueprintTextEncoding {
    data class Success(val text: String) : SutraBlueprintTextEncoding
    data class Invalid(val diagnostics: List<SutraBlueprintTextDiagnostic>) : SutraBlueprintTextEncoding
}

sealed interface SutraBlueprintTextDecoding {
    data class Success(val blueprint: SutraBlueprint) : SutraBlueprintTextDecoding
    data class Invalid(val diagnostics: List<SutraBlueprintTextDiagnostic>) : SutraBlueprintTextDecoding
}

/** Canonical portable source for one evaluator-free sūtra definition. */
object SutraBlueprintTextCodec {
    fun encode(blueprint: SutraBlueprint): SutraBlueprintTextEncoding {
        val value = when (val encoded = encodeValue(blueprint)) {
            is BlueprintValueEncoding.Success -> encoded.value
            is BlueprintValueEncoding.Invalid -> {
                return SutraBlueprintTextEncoding.Invalid(encoded.diagnostics)
            }
        }
        return SutraBlueprintTextEncoding.Success(SutraArthaTextCodec.encode(value))
    }

    internal fun encodeValue(blueprint: SutraBlueprint): BlueprintValueEncoding {
        val role = encodeRole(blueprint.role)
            ?: return BlueprintValueEncoding.Invalid(
                listOf(
                    SutraBlueprintTextDiagnostic(
                        SutraBlueprintTextDiagnosticCode.UNSUPPORTED_ROLE,
                        "Adhikāra role ${blueprint.id} contains a host-language eligibility predicate.",
                    ),
                ),
            )
        return BlueprintValueEncoding.Success(
            record(
                "id" to text(blueprint.id.value),
                "source" to encodeSource(blueprint.source),
                "role" to role,
                "artha" to record(
                    "kind" to text(blueprint.artha.kind),
                    "fields" to SutraArthaValue.Record(blueprint.artha.fields),
                ),
                "relations" to SutraArthaValue.Sequence(
                    blueprint.relations.map(::encodeRelation)
                        .sortedBy(SutraArthaTextCodec::encode),
                ),
                "governance" to record(
                    "optional" to SutraArthaValue.Truth(blueprint.governance.optional),
                    "priority" to symbol(blueprint.governance.priority.name),
                    "blocks" to SutraArthaValue.Sequence(
                        blueprint.governance.blocks.sorted().map(::text),
                    ),
                    "visibility" to symbol(blueprint.governance.visibility.name),
                ),
            ),
        )
    }

    fun decode(source: String): SutraBlueprintTextDecoding {
        val decoded = when (val artha = SutraArthaTextCodec.decode(source)) {
            is SutraArthaTextDecoding.Success -> artha.value
            is SutraArthaTextDecoding.Invalid -> {
                return SutraBlueprintTextDecoding.Invalid(
                    artha.diagnostics.map {
                        SutraBlueprintTextDiagnostic(
                            SutraBlueprintTextDiagnosticCode.INVALID_ARTHA_TEXT,
                            it.message,
                            it.position,
                        )
                    },
                )
            }
        }
        return decodeValue(decoded)
    }

    internal fun decodeValue(decoded: SutraArthaValue): SutraBlueprintTextDecoding {
        return try {
            val root = decoded.record("blueprint")
            val artha = root.required("artha").record("artha")
            val blueprint = SutraBlueprint(
                id = SutraId(root.text("id")),
                source = decodeSource(root.required("source")),
                role = decodeRole(root.required("role")),
                artha = SutraArtha(
                    kind = artha.text("kind"),
                    fields = artha.required("fields").record("artha.fields"),
                ),
                relations = root.sequence("relations").mapTo(linkedSetOf(), ::decodeRelation),
                governance = decodeGovernance(root.required("governance")),
            )
            val validation = SutraBlueprintValidator.validate(blueprint)
            if (validation.isNotEmpty()) {
                SutraBlueprintTextDecoding.Invalid(
                    validation.map {
                        SutraBlueprintTextDiagnostic(
                            SutraBlueprintTextDiagnosticCode.INVALID_SCHEMA,
                            it.message,
                        )
                    },
                )
            } else {
                SutraBlueprintTextDecoding.Success(blueprint)
            }
        } catch (invalid: SchemaException) {
            SutraBlueprintTextDecoding.Invalid(
                listOf(
                    SutraBlueprintTextDiagnostic(
                        invalid.code,
                        invalid.message ?: "Invalid blueprint schema.",
                    ),
                ),
            )
        } catch (invalid: IllegalArgumentException) {
            SutraBlueprintTextDecoding.Invalid(
                listOf(
                    SutraBlueprintTextDiagnostic(
                        SutraBlueprintTextDiagnosticCode.INVALID_SCHEMA,
                        invalid.message ?: "Invalid blueprint value.",
                    ),
                ),
            )
        }
    }

    private fun encodeSource(source: SutraSource): SutraArthaValue.Record = when (source) {
        is SutraSource.Ashtadhyayi -> record(
            "kind" to symbol("ashtadhyayi"),
            "number" to text(source.number),
            "text" to text(source.text),
        )
        is SutraSource.Vakya -> record(
            "kind" to symbol("vakya"),
            "uktiId" to text(source.uktiId),
            "vakyaIndex" to SutraArthaValue.Number(source.vakyaIndex.toLong()),
            "text" to text(source.text),
        )
        is SutraSource.Program -> record(
            "kind" to symbol("program"),
            "grantha" to text(source.grantha),
            "location" to text(source.location),
            "text" to text(source.text),
        )
    }

    private fun decodeSource(value: SutraArthaValue): SutraSource {
        val source = value.record("source")
        return when (source.symbol("kind")) {
            "ashtadhyayi" -> SutraSource.Ashtadhyayi(
                source.text("number"),
                source.text("text"),
            )
            "vakya" -> SutraSource.Vakya(
                source.text("uktiId"),
                source.number("vakyaIndex").toIntExact("source.vakyaIndex"),
                source.text("text"),
            )
            "program" -> SutraSource.Program(
                source.text("grantha"),
                source.text("location"),
                source.text("text"),
            )
            else -> schema("Unknown source kind '${source.symbol("kind")}'.")
        }
    }

    private fun encodeRole(role: SutraRole): SutraArthaValue? = when (role) {
        SutraRole.Samjna -> symbol("samjna")
        SutraRole.Vidhi -> symbol("vidhi")
        SutraRole.Nishedha -> symbol("nishedha")
        SutraRole.Niyama -> symbol("niyama")
        SutraRole.Atidesha -> symbol("atidesha")
        SutraRole.Anuvrtti -> symbol("anuvrtti")
        SutraRole.Apavada -> symbol("apavada")
        SutraRole.Vibhasha -> symbol("vibhasha")
        is SutraRole.Paribhasha -> record(
            "kind" to symbol("paribhasha"),
            "targetScope" to symbol(role.targetScope.name),
        )
        is SutraRole.Adhikara -> null
    }

    private fun decodeRole(value: SutraArthaValue): SutraRole = when (value) {
        is SutraArthaValue.Symbol -> when (value.name) {
            "samjna" -> SutraRole.Samjna
            "vidhi" -> SutraRole.Vidhi
            "nishedha" -> SutraRole.Nishedha
            "niyama" -> SutraRole.Niyama
            "atidesha" -> SutraRole.Atidesha
            "anuvrtti" -> SutraRole.Anuvrtti
            "apavada" -> SutraRole.Apavada
            "vibhasha" -> SutraRole.Vibhasha
            "adhikara" -> unsupported("Adhikāra source requires a host-language predicate.")
            else -> schema("Unknown sūtra role '${value.name}'.")
        }
        is SutraArthaValue.Record -> {
            if (value.fields.symbol("kind") != "paribhasha") {
                schema("Unknown structured sūtra role.")
            }
            SutraRole.Paribhasha(
                enumValue<ParibhashaScope>(
                    value.fields.symbol("targetScope"),
                    "paribhāṣā target scope",
                ),
            )
        }
        else -> schema("Sūtra role must be a symbol or record.")
    }

    private fun encodeRelation(relation: SutraRelation): SutraArthaValue.Record = when (relation) {
        is SutraRelation.DependsOn -> record(
            "kind" to symbol("dependsOn"),
            "prerequisite" to text(relation.prerequisite.value),
        )
        is SutraRelation.Blocks -> record(
            "kind" to symbol("blocks"),
            "target" to text(relation.target.value),
        )
        is SutraRelation.PhalaPravaha -> record(
            "kind" to symbol("phalaPravaha"),
            "source" to text(relation.source.value),
            "target" to text(relation.target.value),
        )
    }

    private fun decodeRelation(value: SutraArthaValue): SutraRelation {
        val relation = value.record("relation")
        return when (relation.symbol("kind")) {
            "dependsOn" -> SutraRelation.DependsOn(SutraId(relation.text("prerequisite")))
            "blocks" -> SutraRelation.Blocks(SutraId(relation.text("target")))
            "phalaPravaha" -> SutraRelation.PhalaPravaha(
                SutraId(relation.text("source")),
                SutraId(relation.text("target")),
            )
            else -> schema("Unknown sūtra relation '${relation.symbol("kind")}'.")
        }
    }

    private fun decodeGovernance(value: SutraArthaValue): SutraGovernance {
        val governance = value.record("governance")
        return SutraGovernance(
            optional = governance.truth("optional"),
            priority = enumValue(governance.symbol("priority"), "sūtra priority"),
            blocks = governance.sequence("blocks").mapTo(linkedSetOf()) {
                (it as? SutraArthaValue.Text)?.value
                    ?: schema("Governance block targets must be text.")
            },
            visibility = enumValue(governance.symbol("visibility"), "sūtra visibility"),
        )
    }

    private inline fun <reified E : Enum<E>> enumValue(name: String, label: String): E =
        enumValues<E>().firstOrNull { it.name == name }
            ?: schema("Unknown $label '$name'.")

    private fun SutraArthaValue.record(label: String): Map<String, SutraArthaValue> =
        (this as? SutraArthaValue.Record)?.fields ?: schema("$label must be a record.")

    private fun Map<String, SutraArthaValue>.required(name: String): SutraArthaValue =
        get(name) ?: schema("Missing blueprint field '$name'.")

    private fun Map<String, SutraArthaValue>.text(name: String): String =
        (required(name) as? SutraArthaValue.Text)?.value
            ?: schema("Blueprint field '$name' must be text.")

    private fun Map<String, SutraArthaValue>.symbol(name: String): String =
        (required(name) as? SutraArthaValue.Symbol)?.name
            ?: schema("Blueprint field '$name' must be a symbol.")

    private fun Map<String, SutraArthaValue>.number(name: String): Long =
        (required(name) as? SutraArthaValue.Number)?.value
            ?: schema("Blueprint field '$name' must be a number.")

    private fun Map<String, SutraArthaValue>.truth(name: String): Boolean =
        (required(name) as? SutraArthaValue.Truth)?.value
            ?: schema("Blueprint field '$name' must be truth-valued.")

    private fun Map<String, SutraArthaValue>.sequence(name: String): List<SutraArthaValue> =
        (required(name) as? SutraArthaValue.Sequence)?.values
            ?: schema("Blueprint field '$name' must be a sequence.")

    private fun Long.toIntExact(label: String): Int {
        if (this !in Int.MIN_VALUE..Int.MAX_VALUE) schema("$label is outside the integer range.")
        return toInt()
    }

    private fun record(
        vararg fields: Pair<String, SutraArthaValue>,
    ) = SutraArthaValue.Record(mapOf(*fields))

    private fun text(value: String) = SutraArthaValue.Text(value)
    private fun symbol(value: String) = SutraArthaValue.Symbol(value)

    private fun schema(message: String): Nothing =
        throw SchemaException(SutraBlueprintTextDiagnosticCode.INVALID_SCHEMA, message)

    private fun unsupported(message: String): Nothing =
        throw SchemaException(SutraBlueprintTextDiagnosticCode.UNSUPPORTED_ROLE, message)

    private class SchemaException(
        val code: SutraBlueprintTextDiagnosticCode,
        message: String,
    ) : IllegalArgumentException(message)

    internal sealed interface BlueprintValueEncoding {
        data class Success(val value: SutraArthaValue.Record) : BlueprintValueEncoding
        data class Invalid(
            val diagnostics: List<SutraBlueprintTextDiagnostic>,
        ) : BlueprintValueEncoding
    }
}
