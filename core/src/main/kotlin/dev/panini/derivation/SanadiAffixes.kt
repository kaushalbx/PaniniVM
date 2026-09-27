package dev.panini.derivation

import dev.panini.core.SanadiAffix

/** Canonical identities of sanādi affixes; surface forms are never used for classification. */
object SanadiAffixes {
    val upadeshas: Set<String> = SanadiAffix.entries.mapTo(mutableSetOf()) { it.upadesha }

    fun contains(upadesha: String): Boolean = SanadiAffix.fromUpadesha(upadesha) != null
}
