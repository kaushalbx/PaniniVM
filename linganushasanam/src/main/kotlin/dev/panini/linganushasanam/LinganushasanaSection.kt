package dev.panini.linganushasanam

/**
 * The six canonical gender sections (अधिकार) of Pāṇini's Liṅgānuśāsanam.
 *
 * [VISESYANIGHNALINGA] and [SAMASALINGA] are retained for the older executable
 * subdivision used by this module; the complete canonical corpus uses the six
 * sections ending in [AVISISHTALINGA].
 */
enum class LinganushasanaSection(val devanagariName: String) {
    STRILINGA("अथ स्त्रीलिङ्गम्"),
    PUMLINGA("अथ पुंलिङ्गम्"),
    NAPUMSAKALINGA("अथ नपुंसकलिङ्गम्"),
    STRIPUMSALINGA("अथ स्त्रीपुंसलिङ्गम्"),
    PUMNAPUMSAKALINGA("अथ पुंनपुंसकलिङ्गम्"),
    AVISISHTALINGA("अथ अविशिष्टलिङ्गम्"),
    VISESYANIGHNALINGA("अथ विशेष्यनिघ्नलिङ्गम्"),
    SAMASALINGA("अथ समासलिङ्गम्"),
}
