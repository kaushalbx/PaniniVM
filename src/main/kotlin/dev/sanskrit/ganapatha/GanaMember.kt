package dev.sanskrit.ganapatha

data class GanaMember(
    val text: String,
    val hindiArtha: String = "",
    val englishArtha: String = "",
) {
    val normalized: String = GanaNormalizer.normalize(text)
}
