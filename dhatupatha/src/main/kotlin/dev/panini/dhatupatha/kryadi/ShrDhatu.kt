package dev.panini.dhatupatha.kryadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

class ShrDhatu : Dhatu(
    id = "09.0021",
    krama = 21,
    upadesha = "शॄ",
    sourceSurface = "शृ",
    artha = "हिंसायाम्",
    arthaHindi = "हिंसा करना, मार डालना",
    arthaEnglish = "to kill, to destroy",
    gana = DhatuGana.KRYADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    surfaceAliases = setOf("शृ", "शॄ")
)
