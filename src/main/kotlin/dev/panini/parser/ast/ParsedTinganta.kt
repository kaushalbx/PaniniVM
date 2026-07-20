package dev.panini.parser.ast

data class ParsedTinganta(
    /**
     * Null only for the grammar's plain IDENTIFIER alternative.
     */
    val dhatu: String?,

    val sanadiPratyayas: List<String> = emptyList(),

    val vikarana: String? = null,

    /**
     * Null only for the grammar's plain IDENTIFIER alternative.
     */
    val lakara: String?,

    val tingPratyaya: String? = null,

    /**
     * Populated when `tingantaPada : IDENTIFIER`.
     */
    val unresolvedIdentifier: String? = null,
) {
    init {
        val segmented = dhatu != null

        if (segmented) {
            require(!lakara.isNullOrBlank()) {
                "A segmented tiṅanta requires a lakāra."
            }

            require(unresolvedIdentifier == null) {
                "A segmented tiṅanta cannot also have an unresolved identifier."
            }
        } else {
            require(!unresolvedIdentifier.isNullOrBlank()) {
                "An unsegmented tiṅanta requires an identifier."
            }

            require(lakara == null) {
                "An unsegmented tiṅanta cannot have a separately parsed lakāra."
            }

            require(sanadiPratyayas.isEmpty()) {
                "An unsegmented tiṅanta cannot contain parsed sanādi suffixes."
            }

            require(vikarana == null) {
                "An unsegmented tiṅanta cannot contain a parsed vikaraṇa."
            }

            require(tingPratyaya == null) {
                "An unsegmented tiṅanta cannot contain a parsed tiṅ suffix."
            }
        }
    }

    val segmentedText: String
        get() {
            unresolvedIdentifier?.let {
                return it
            }

            return buildList {
                dhatu?.let(::add)
                addAll(sanadiPratyayas)
                vikarana?.let(::add)
                lakara?.let(::add)
                tingPratyaya?.let(::add)
            }.joinToString(" + ")
        }
}
