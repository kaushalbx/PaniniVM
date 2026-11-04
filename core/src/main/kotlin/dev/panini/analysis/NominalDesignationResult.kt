package dev.panini.analysis

enum class NominalDesignation { AMANTRITA, SAMBUDDHI }

/** A saṃjñā for an already established nominal use; this does not assign a case. */
data class NominalDesignationResult(val designation: NominalDesignation, val evidence: KarakaEvidence)
