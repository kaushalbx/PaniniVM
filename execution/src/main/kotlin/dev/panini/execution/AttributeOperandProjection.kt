package dev.panini.execution

import dev.panini.vyakaranam.ast.*

/** Projects grammatical field phrases into typed operands without reparsing source text. */
object AttributeOperandProjection {
    data class Operand(val name: String, val access: TaddhitaAttributeAccess)
    data class Projection(val invocation: Invocation, val operands: List<Operand>)

    fun project(invocation: Invocation, prefix: String = "__pvm_attribute_",
        knownOperationStems: Set<String> = emptySet()): Projection {
        var current = invocation
        val operands = mutableListOf<Operand>()
        while (true) {
            val reference = TaddhitaStructEngine.detectAttributeReference(current.vakya)
                ?: return Projection(current, operands)
            // A genitive receiver followed by an instrumental procedure names a
            // method call, not a field whose value should replace that procedure.
            if (reference.access.resultAffix.vibhakti == dev.panini.core.Vibhakti.TRTIYA &&
                (current.vakya.padas[reference.padaRange.last] as? SubantaPada)
                    ?.pratipadika?.prakriyaIdentity() in knownOperationStems) return Projection(current, operands)
            val name = "$prefix${operands.size}"
            operands += Operand(name, reference.access)
            val sup = reference.access.resultAffix.upadesha
            val replacement = SubantaPada("$name+$sup", MulaPratipadika(name, name), SupPratyaya(sup, sup))
            val padas = current.vakya.padas.toMutableList().apply {
                subList(reference.padaRange.first, reference.padaRange.last + 1).clear()
                add(reference.padaRange.first, replacement)
            }
            val vakya = requireNotNull(current.vakya as? AkhyataVakya) {
                "Embedded field operands require a finite verbal clause."
            }
            current = current.copy(vakya = vakya.copy(padas = padas))
        }
    }
}
