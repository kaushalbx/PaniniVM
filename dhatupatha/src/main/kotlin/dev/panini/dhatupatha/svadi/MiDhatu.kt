package dev.panini.dhatupatha.svadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

class MiDhatu : Dhatu(
    id = "05.0004",
    krama = 4,
    upadesha = "डुमिञ्",
    sourceSurface = "मि",
    artha = "प्रक्षेपणे",
    arthaHindi = "फेंकना",
    arthaEnglish = "to cast, to throw, to scatter",
    gana = DhatuGana.SVADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA
)
