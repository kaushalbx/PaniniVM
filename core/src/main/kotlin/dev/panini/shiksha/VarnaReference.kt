package dev.panini.shiksha

import dev.panini.core.ItMarker

/** A sound mentioned in grammatical notation, not a pronounced sequence of marker sounds. */
data class VarnaReference(
    val varna: Varna,
    val itMarkers: Set<ItMarker> = emptySet(),
    val tMarkerPosition: TMarkerPosition? = null,
    val use: VarnaReferenceUse = VarnaReferenceUse.DENOTATION,
)

/** 1.1.70 admits both a following t and a sound following t. */
enum class TMarkerPosition { BEFORE, AFTER }

/** Prescribed affixes, substitutions and augments are not savarṇa-denoting mentions (1.1.69). */
enum class VarnaReferenceUse { DENOTATION, PRESCRIPTION }
