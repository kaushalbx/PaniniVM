package dev.panini.dhatupatha.bhvadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

class RiDhatu : Dhatu(
    id = "01.1086",
    krama = 1086,
    upadesha = "ऋ",
    sourceSurface = "ऋ",
    artha = "गतौ प्रापणे च",
    arthaHindi = "जाना, प्राप्त करना",
    arthaEnglish = "to go, to reach",
    gana = DhatuGana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA
)
