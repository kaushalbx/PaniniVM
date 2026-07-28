package dev.panini.execution.sutra

import dev.panini.core.Karaka
import dev.panini.execution.AmbiguousKarakaBinding
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.SanskritValue
import dev.panini.shiksha.Samjna
import dev.panini.sutra.runtime.SutraArthaValue

object ProgramSutraArthaCodec {
    fun encodeAmbiguousBinding(binding: AmbiguousKarakaBinding): SutraArthaValue.Record =
        SutraArthaValue.Record(
            mapOf(
                "expression" to encodeExpression(binding.expression),
                "candidates" to SutraArthaValue.Sequence(
                    binding.candidates.map { SutraArthaValue.Symbol(it.name) },
                ),
            ),
        )

    fun decodeAmbiguousBinding(value: SutraArthaValue): AmbiguousKarakaBinding {
        val fields = (value as? SutraArthaValue.Record)?.fields
            ?: throw IllegalArgumentException("An ambiguous kāraka binding must be a semantic record.")
        val candidates = fields.sequence("candidates").mapTo(linkedSetOf()) {
            val name = (it as? SutraArthaValue.Symbol)?.name
                ?: throw IllegalArgumentException("An ambiguous kāraka candidate must be a symbol.")
            runCatching { Karaka.valueOf(name) }.getOrElse {
                throw IllegalArgumentException("Unknown kāraka candidate '$name'.")
            }
        }
        return AmbiguousKarakaBinding(
            expression = decodeExpression(
                fields["expression"]
                    ?: throw IllegalArgumentException("Ambiguous binding requires field 'expression'."),
            ),
            candidates = candidates,
        )
    }

    fun encodeExpression(expression: ExecutionExpression): SutraArthaValue.Record = when (expression) {
        is ExecutionExpression.Pada -> SutraArthaValue.Record(
            buildMap {
                put("expressionType", SutraArthaValue.Symbol("pada"))
                put("prakriti", SutraArthaValue.Text(expression.prakriti))
                put(
                    "samjnas",
                    SutraArthaValue.Sequence(expression.samjnas.map(::encodeSamjna)),
                )
                expression.value?.let { put("value", encodeValue(it)) }
            },
        )
        is ExecutionExpression.Coordination -> SutraArthaValue.Record(
            mapOf(
                "expressionType" to SutraArthaValue.Symbol("coordination"),
                "members" to SutraArthaValue.Sequence(
                    expression.members.map(::encodeExpression),
                ),
            ),
        )
        is ExecutionExpression.Reference -> SutraArthaValue.Record(
            mapOf(
                "expressionType" to SutraArthaValue.Symbol("reference"),
                "name" to SutraArthaValue.Symbol(expression.name),
            ),
        )
    }

    fun decodeExpression(value: SutraArthaValue): ExecutionExpression {
        val fields = (value as? SutraArthaValue.Record)?.fields
            ?: throw IllegalArgumentException("An execution expression must be a semantic record.")
        return when (fields.symbol("expressionType")) {
            "pada" -> ExecutionExpression.Pada(
                prakriti = fields.text("prakriti"),
                samjnas = fields.sequence("samjnas").mapTo(linkedSetOf(), ::decodeSamjna),
                value = fields["value"]?.let(::decodeValue),
            )
            "coordination" -> ExecutionExpression.Coordination(
                fields.sequence("members").map(::decodeExpression),
            )
            "reference" -> ExecutionExpression.Reference(fields.symbol("name"))
            else -> throw IllegalArgumentException("Unknown execution expression type.")
        }
    }

    fun encodeValue(value: SanskritValue): SutraArthaValue.Record = when (value) {
        is SanskritValue.Sankhya -> record(
            "sankhya",
            "number" to SutraArthaValue.Number(value.value),
            "word" to SutraArthaValue.Text(value.word),
        )
        is SanskritValue.Rational -> record(
            "rational",
            "numerator" to SutraArthaValue.Number(value.numerator),
            "denominator" to SutraArthaValue.Number(value.denominator),
            "word" to SutraArthaValue.Text(value.word),
        )
        is SanskritValue.Shabda -> record(
            "shabda",
            "text" to SutraArthaValue.Text(value.text),
            "samjnas" to SutraArthaValue.Sequence(value.samjnas.map(::encodeSamjna)),
        )
        is SanskritValue.Gana -> record(
            "gana",
            "elements" to SutraArthaValue.Sequence(value.elements.map(::encodeValue)),
        )
        is SanskritValue.Suchi -> record(
            "suchi",
            "items" to SutraArthaValue.Sequence(value.items.map(::encodeValue)),
        )
        is SanskritValue.Satya -> record(
            "satya",
            "boolean" to SutraArthaValue.Truth(value.boolean),
        )
    }

    fun decodeValue(value: SutraArthaValue): SanskritValue {
        val fields = (value as? SutraArthaValue.Record)?.fields
            ?: throw IllegalArgumentException("A Sanskrit value must be a semantic record.")
        return when (fields.symbol("valueType")) {
            "sankhya" -> SanskritValue.Sankhya(
                fields.number("number"),
                fields.text("word"),
            )
            "rational" -> SanskritValue.Rational(
                fields.number("numerator"),
                fields.number("denominator"),
                fields.text("word"),
            )
            "shabda" -> SanskritValue.Shabda(
                fields.text("text"),
                fields.sequence("samjnas").mapTo(linkedSetOf(), ::decodeSamjna),
            )
            "gana" -> SanskritValue.Gana(
                fields.sequence("elements").map(::decodeValue),
            )
            "suchi" -> SanskritValue.Suchi(
                fields.sequence("items").map(::decodeValue),
            )
            "satya" -> SanskritValue.Satya(fields.truth("boolean"))
            else -> throw IllegalArgumentException("Unknown Sanskrit semantic value type.")
        }
    }

    private fun encodeSamjna(samjna: Samjna): SutraArthaValue =
        when (samjna) {
            is Samjna.Rudhi -> SutraArthaValue.Record(
                mapOf("rudhi" to SutraArthaValue.Text(samjna.word)),
            )
            is Enum<*> -> SutraArthaValue.Symbol(samjna.name)
            else -> error("Unsupported saṃjñā implementation: ${samjna::class.qualifiedName}")
        }

    private fun decodeSamjna(value: SutraArthaValue): Samjna = when (value) {
        is SutraArthaValue.Symbol -> Samjna.valueOf(value.name)
        is SutraArthaValue.Record -> Samjna.Rudhi(value.fields.text("rudhi"))
        else -> throw IllegalArgumentException("Invalid saṃjñā semantic value.")
    }

    private fun record(
        type: String,
        vararg fields: Pair<String, SutraArthaValue>,
    ): SutraArthaValue.Record = SutraArthaValue.Record(
        mapOf("valueType" to SutraArthaValue.Symbol(type), *fields),
    )

    private fun Map<String, SutraArthaValue>.text(name: String): String =
        (get(name) as? SutraArthaValue.Text)?.value
            ?: throw IllegalArgumentException("Semantic field '$name' must be text.")

    private fun Map<String, SutraArthaValue>.symbol(name: String): String =
        (get(name) as? SutraArthaValue.Symbol)?.name
            ?: throw IllegalArgumentException("Semantic field '$name' must be a symbol.")

    private fun Map<String, SutraArthaValue>.number(name: String): Long =
        (get(name) as? SutraArthaValue.Number)?.value
            ?: throw IllegalArgumentException("Semantic field '$name' must be a number.")

    private fun Map<String, SutraArthaValue>.truth(name: String): Boolean =
        (get(name) as? SutraArthaValue.Truth)?.value
            ?: throw IllegalArgumentException("Semantic field '$name' must be truth-valued.")

    private fun Map<String, SutraArthaValue>.sequence(name: String): List<SutraArthaValue> =
        (get(name) as? SutraArthaValue.Sequence)?.values
            ?: throw IllegalArgumentException("Semantic field '$name' must be a sequence.")
}
