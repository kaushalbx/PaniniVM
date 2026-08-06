package dev.panini.linganushasanam

/**
 * The 5 canonical sections (अधिकार/अध्याय) of Pāṇini's Liṅgānuśāsanam text.
 */
enum class LinganushasanaSection(val devanagariName: String) {
    STRILINGA("अथ स्त्रीलिङ्गम्"),
    PUMLINGA("अथ पुंलिङ्गम्"),
    NAPUMSAKALINGA("अथ नपुंसकलिङ्गम्"),
    VISESYANIGHNALINGA("अथ विशेष्यनिघ्नलिङ्गम्"),
    SAMASALINGA("अथ समासलिङ्गम्"),
}
