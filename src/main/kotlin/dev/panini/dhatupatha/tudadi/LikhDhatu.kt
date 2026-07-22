package dev.panini.dhatupatha.tudadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Tudādi dhātu लिखँ अक्षरविन्यासे. */
class LikhDhatu : Dhatu(
    id = "06.0090",
    krama = 90,
    upadesha = "लिखँ",
    sourceSurface = "लिख्",
    artha = "अक्षरविन्यासे",
    arthaHindi = "लिखना, चित्र बनाना",
    arthaEnglish = "to write, to draw",
    gana = DhatuGana.TUDADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
)
