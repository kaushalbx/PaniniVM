package dev.panini.execution

/** Equality of semantic payloads, independent of rendering and annotation metadata. */
fun SanskritValue.semanticallyEquals(other: SanskritValue): Boolean = when (this) {
    is SanskritValue.Sankhya -> other is SanskritValue.Sankhya && value == other.value
    is SanskritValue.Shabda -> other is SanskritValue.Shabda && text == other.text
    is SanskritValue.Satya -> other is SanskritValue.Satya && boolean == other.boolean
    is SanskritValue.Range -> other is SanskritValue.Range &&
        minimum.semanticallyEquals(other.minimum) && maximum.semanticallyEquals(other.maximum)
    is SanskritValue.Rational -> other is SanskritValue.Rational && if (denominator == 0L || other.denominator == 0L) {
        numerator == other.numerator && denominator == other.denominator
    } else {
        numerator.toBigInteger() * other.denominator.toBigInteger() ==
            other.numerator.toBigInteger() * denominator.toBigInteger()
    }
    is SanskritValue.Suchi -> other is SanskritValue.Suchi && items.sameValues(other.items)
    is SanskritValue.Gana -> other is SanskritValue.Gana && elements.sameValues(other.elements)
    is SanskritValue.Rupa -> other is SanskritValue.Rupa && schema == other.schema &&
        fields.keys == other.fields.keys && fields.all { (name, value) -> value.semanticallyEquals(other.fields.getValue(name)) }
    SanskritValue.Lopa -> other === SanskritValue.Lopa
}

private fun List<SanskritValue>.sameValues(other: List<SanskritValue>): Boolean =
    size == other.size && indices.all { this[it].semanticallyEquals(other[it]) }
