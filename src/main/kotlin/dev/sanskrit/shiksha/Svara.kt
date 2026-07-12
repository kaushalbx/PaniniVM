package dev.sanskrit.shiksha

enum class Svara(
    override val devanagari: String,
    val matra: String?,
    val sthana: Sthana,
    val abhyantaraPrayatna: AbhyantaraPrayatna = AbhyantaraPrayatna.VIVRTA
) : Varna {
    A("अ", null, Sthana.KANTHA),
    AA("आ", "ा", Sthana.KANTHA),
    I("इ", "ि", Sthana.TALU),
    II("ई", "ी", Sthana.TALU),
    U("उ", "ु", Sthana.OSTHA),
    UU("ऊ", "ू", Sthana.OSTHA),
    R("ऋ", "ृ", Sthana.MURDHA),
    RR("ॠ", "ॄ", Sthana.MURDHA),
    L("ऌ", "ॢ", Sthana.DANTA),
    LL("ॡ", "ॣ", Sthana.DANTA),
    E("ए", "े", Sthana.KANTHATALU),
    AI("ऐ", "ै", Sthana.KANTHATALU),
    O("ओ", "ो", Sthana.KANTHOSTHA),
    AU("औ", "ौ", Sthana.KANTHOSTHA);

    companion object {
        fun fromIndependent(value: Char): Svara? =
            entries.firstOrNull { it.devanagari == value.toString() }

        fun fromMatra(value: Char): Svara? =
            entries.firstOrNull { it.matra == value.toString() }
    }
}
