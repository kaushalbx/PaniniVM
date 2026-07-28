package dev.panini.sutra.runtime

@JvmInline
value class GranthaId(val value: String) {
    init {
        require(value.isNotBlank()) { "A sūtra-grantha requires a non-blank identity." }
    }

    override fun toString(): String = value
}

data class GranthaImport(
    val grantha: GranthaId,
    val alias: String = grantha.value,
) {
    init {
        require(alias.isNotBlank()) { "A grantha import requires a non-blank alias." }
    }
}

data class AdhikaraDeclaration(
    val sutraId: SutraId,
    val members: Set<SutraId>,
)

data class SamjnaDeclaration(
    val name: String,
    val description: String? = null,
) {
    init {
        require(name.isNotBlank()) { "A saṃjñā declaration requires a name." }
    }
}

/**
 * Package-level representation of segmented PaniniVM software. A grantha owns
 * declarations and public boundaries; SutraProgram remains the executable IR.
 */
data class SutraGrantha<S : SutraAvastha>(
    val id: GranthaId,
    val sutras: List<RuntimeSutra<S>>,
    val imports: List<GranthaImport> = emptyList(),
    val adhikaras: List<AdhikaraDeclaration> = emptyList(),
    val samjnas: List<SamjnaDeclaration> = emptyList(),
    val exports: Set<SutraId> = emptySet(),
)

/**
 * Evaluator-free package representation. This is the source form used by
 * generated and persisted sūtra software before a domain compiler lowers it.
 */
data class SutraBlueprintGrantha(
    val id: GranthaId,
    val sutras: List<SutraBlueprint>,
    val imports: List<GranthaImport> = emptyList(),
    val adhikaras: List<AdhikaraDeclaration> = emptyList(),
    val samjnas: List<SamjnaDeclaration> = emptyList(),
    val exports: Set<SutraId> = emptySet(),
)

fun SutraGrantha<*>.toBlueprintGrantha(): SutraBlueprintGrantha =
    SutraBlueprintGrantha(
        id = id,
        sutras = sutras.map { it.toBlueprint() },
        imports = imports,
        adhikaras = adhikaras,
        samjnas = samjnas,
        exports = exports,
    )

enum class SutraGranthaDiagnosticCode {
    DUPLICATE_IMPORT_ALIAS,
    DUPLICATE_SAMJNA,
    MISSING_ADHIKARA_SUTRA,
    MISSING_ADHIKARA_MEMBER,
    MISSING_EXPORT,
    INVALID_SUTRA_PROGRAM,
}

data class SutraGranthaDiagnostic(
    val code: SutraGranthaDiagnosticCode,
    val message: String,
)

sealed interface SutraGranthaLowering<out S : SutraAvastha> {
    data class Success<S : SutraAvastha>(
        val program: SutraProgram<S>,
    ) : SutraGranthaLowering<S>

    data class Invalid(
        val diagnostics: List<SutraGranthaDiagnostic>,
    ) : SutraGranthaLowering<Nothing>
}

object SutraGranthaCompiler {
    fun <S : SutraAvastha> lower(
        grantha: SutraGrantha<S>,
    ): SutraGranthaLowering<S> {
        val diagnostics = mutableListOf<SutraGranthaDiagnostic>()
        grantha.imports.groupBy { it.alias }.filterValues { it.size > 1 }.keys.forEach { alias ->
            diagnostics += SutraGranthaDiagnostic(
                SutraGranthaDiagnosticCode.DUPLICATE_IMPORT_ALIAS,
                "Grantha ${grantha.id} declares import alias '$alias' more than once.",
            )
        }
        grantha.samjnas.groupBy { it.name }.filterValues { it.size > 1 }.keys.forEach { name ->
            diagnostics += SutraGranthaDiagnostic(
                SutraGranthaDiagnosticCode.DUPLICATE_SAMJNA,
                "Grantha ${grantha.id} declares saṃjñā '$name' more than once.",
            )
        }

        val sutraIds = grantha.sutras.mapTo(linkedSetOf()) { it.id }
        grantha.adhikaras.forEach { adhikara ->
            if (adhikara.sutraId !in sutraIds) {
                diagnostics += SutraGranthaDiagnostic(
                    SutraGranthaDiagnosticCode.MISSING_ADHIKARA_SUTRA,
                    "Adhikāra ${adhikara.sutraId} is not present in grantha ${grantha.id}.",
                )
            }
            adhikara.members.filter { it !in sutraIds }.forEach { missing ->
                diagnostics += SutraGranthaDiagnostic(
                    SutraGranthaDiagnosticCode.MISSING_ADHIKARA_MEMBER,
                    "Adhikāra ${adhikara.sutraId} contains missing sūtra $missing.",
                )
            }
        }
        grantha.exports.filter { it !in sutraIds }.forEach { missing ->
            diagnostics += SutraGranthaDiagnostic(
                SutraGranthaDiagnosticCode.MISSING_EXPORT,
                "Grantha ${grantha.id} exports missing sūtra $missing.",
            )
        }

        val program = SutraProgram(grantha.id.value, grantha.sutras)
        SutraProgramValidator.validate(program).diagnostics.forEach { diagnostic ->
            diagnostics += SutraGranthaDiagnostic(
                SutraGranthaDiagnosticCode.INVALID_SUTRA_PROGRAM,
                diagnostic.message,
            )
        }
        return if (diagnostics.isEmpty()) {
            SutraGranthaLowering.Success(program)
        } else {
            SutraGranthaLowering.Invalid(diagnostics)
        }
    }
}
