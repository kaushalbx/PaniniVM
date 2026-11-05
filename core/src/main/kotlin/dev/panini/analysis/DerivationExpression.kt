package dev.panini.analysis

import dev.panini.core.Karaka

/** The non-verbal expression sources named under 2.3.1, besides tiṅ and kṛt. */
enum class ExpressionSourceKind { TADDHITA, SAMASA }

/**
 * Supplied reading that a written taddhita or compound occurrence expresses these roles
 * of this participant, as in प्राप्तानन्दो देवदत्तः. The role is not extracted from the word.
 */
data class DerivationExpression(val sourceIndex: Int, val kind: ExpressionSourceKind, val roles: Set<Karaka>) {
    init { require(roles.isNotEmpty()) { "A derivation expression must name an expressed role." } }
}
