package dev.panini.execution

/** Opaque identity of one runtime local referent, independent of its current value. */
class LocalReferentIdentity internal constructor()

class LocalScopeIdentity internal constructor()

/** Grammatical evidence is separate from payload type and collection cardinality. */
data class LocalReferent(
    val identity: LocalReferentIdentity,
    val owner: LocalScopeIdentity,
    val declaration: dev.panini.vyakaranam.ast.SubantaPada?,
    val grammaticalNumbers: Set<dev.panini.core.Vacana>,
    val grammaticalGenders: Set<dev.panini.core.Linga>,
    val value: SanskritValue,
) {
    val gender: dev.panini.core.Linga? get() = grammaticalGenders.singleOrNull()
}

/** Call-owned parameter slots; named and positional references address the same slot. */
class ProcedureLocalBindings private constructor(
    private val slots: Map<String, Slot>,
    val scopeIdentity: LocalScopeIdentity,
    private val parent: ProcedureLocalBindings? = null,
    private val procedureBoundary: Boolean = false,
) {
    private class Slot(var value: SanskritValue, val owner: LocalScopeIdentity,
        val declaration: dev.panini.vyakaranam.ast.SubantaPada?,
        val identity: LocalReferentIdentity = LocalReferentIdentity()) {
        fun referent(): LocalReferent {
            val agreement = ReferentAgreement.fromDeclaration(declaration)
            return LocalReferent(identity, owner, declaration, agreement.numbers, agreement.genders, value)
        }
    }

    val values: Map<String, SanskritValue> = object : AbstractMap<String, SanskritValue>() {
        override fun get(key: String): SanskritValue? = slots[key]?.value
        override val entries: Set<Map.Entry<String, SanskritValue>>
            get() = slots.mapValues { it.value.value }.entries
    }
    val names: Set<String> get() = parent?.names.orEmpty() + slots.keys

    /** Uses the same nearest-binding rule as assignment; aliases share identity. */
    fun identity(name: String): LocalReferentIdentity? = slots[name]?.identity ?: parent?.identity(name)

    fun referent(name: String): LocalReferent? = slots[name]?.referent() ?: parent?.referent(name)

    /** Visible bindings, deduplicated by identity rather than alias spelling or value equality. */
    fun visibleReferents(): List<LocalReferent> = names.mapNotNull(::referent).distinctBy { it.identity }

    /** A procedure's pronouns cannot acquire antecedents from its dynamic caller. */
    fun anaphoricReferents(): List<LocalReferent> {
        val inherited = if (procedureBoundary) emptyList() else parent?.anaphoricReferents().orEmpty()
        val local = slots.values.map { it.referent() }.distinctBy { it.identity }
        val visibleIdentities = visibleReferents().mapTo(mutableSetOf()) { it.identity }
        return (local + inherited).distinctBy { it.identity }.filter { it.identity in visibleIdentities }
    }

    fun resolveReferent(agreement: ReferentAgreement): ReferentAgreementResolution<LocalReferentIdentity> =
        ReferentAgreementResolver.resolve(agreement, anaphoricReferents().map {
            AgreementCandidate(it.identity, ReferentAgreement(it.grammaticalNumbers, it.grammaticalGenders))
        })

    /** Invocation-owned aliases address existing visible cells, never copied values. */
    fun withReferentAliases(aliases: Map<String, LocalReferentIdentity>): ProcedureLocalBindings {
        val visible = visibleReferents().mapTo(mutableSetOf()) { it.identity }
        fun cell(identity: LocalReferentIdentity): Slot? =
            slots.values.firstOrNull { it.identity === identity } ?: parent?.findCell(identity)
        val additions = aliases.mapValues { (name, identity) ->
            require(name !in names) { "Referent alias collides with a visible binding: '$name'." }
            require(identity in visible) { "Referent alias requires a visible antecedent." }
            requireNotNull(cell(identity))
        }
        return ProcedureLocalBindings(slots + additions, scopeIdentity, parent, procedureBoundary)
    }

    private fun findCell(identity: LocalReferentIdentity): Slot? =
        slots.values.firstOrNull { it.identity === identity } ?: parent?.findCell(identity)

    fun accept(assignments: Map<String, SanskritValue>) {
        assignments.forEach { (name, value) ->
            val slot = slots[name]
            if (slot != null) slot.value = value
            else parent?.accept(mapOf(name to value))
        }
    }

    companion object {
        fun member(parent: ProcedureLocalBindings?, name: String, value: SanskritValue,
            declaration: dev.panini.vyakaranam.ast.SubantaPada? = null): ProcedureLocalBindings {
            val owner = LocalScopeIdentity()
            return ProcedureLocalBindings(mapOf(name to Slot(value, owner, declaration)), owner, parent)
        }

        fun create(parameters: List<PrakriyaParameter>, arguments: List<SanskritValue>,
            parent: ProcedureLocalBindings? = null,
            declarations: Map<String, dev.panini.vyakaranam.ast.SubantaPada> = emptyMap()): ProcedureLocalBindings {
            requireUniqueParameters(parameters)
            val slots = linkedMapOf<String, Slot>()
            val owner = LocalScopeIdentity()
            arguments.forEachIndexed { index, value ->
                val names = setOfNotNull(parameters.getOrNull(index)?.nameStem,
                    PrakriyaAstArgumentBinder.referenceKey(index))
                val slot = Slot(value, owner, parameters.getOrNull(index)?.nameStem?.let(declarations::get))
                names.forEach { name -> slots[name] = slot }
            }
            return ProcedureLocalBindings(slots, owner, parent, procedureBoundary = true)
        }
    }
}
