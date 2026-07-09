package dev.sanskrit.shiksha

enum class Ayogavaha(
    override val devanagari: String,
) : Varna {
    ANUSVARA("ं"),
    VISARGA("ः"),
    JIHVAMULIYA("ᳵ"),
    UPADHMANIYA("ᳶ");
}
