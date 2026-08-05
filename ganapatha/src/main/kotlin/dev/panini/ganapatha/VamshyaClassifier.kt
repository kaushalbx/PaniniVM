package dev.panini.ganapatha

import dev.panini.shiksha.Samjna

/**
 * Classifier for Vaṁśya (lineage/sage dynasty) stems used in Samāsa sūtras (2.1.19),
 * leveraging actual Pāṇinian Gaṇapāṭha lists (Gargādi, Bidādi, Upakādi, Śārṅgaravādi, etc.).
 */
object VamshyaClassifier {
    fun isVamshya(stem: String, samjnas: Set<Samjna> = emptySet()): Boolean =
        GanaPatha.isVamshya(stem, samjnas)
}
