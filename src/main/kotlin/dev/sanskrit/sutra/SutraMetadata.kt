package dev.sanskrit.sutra

class SutraMetadata(
    val sutraNumber: String,
    val sutraText: String,
    val hindiVyakhya: String,
    val type: SutraType,
    val adhyaya: Int,
    val pada: Int,
    val vaikalpika: Boolean,
    val krama: Int,
    val avastha: SutraAvastha = SutraAvastha.KRIYAVAT,
) {
    override fun equals(other: Any?): Boolean =
        other is SutraMetadata && sutraNumber == other.sutraNumber

    override fun hashCode(): Int = sutraNumber.hashCode()

    override fun toString(): String = "$sutraNumber $sutraText"
}
