package dev.panini.dhatupatha.svadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

class AshDhatu : Dhatu(
    id = "05.0020",
    krama = 20,
    upadesha = "अशूँ",
    sourceSurface = "अश्",
    artha = "व्याप्तौ सङ्घाते च",
    arthaHindi = "फैलना, राशि करना, ढेर करना",
    arthaEnglish = "to pervade,to heap,to pile up",
    gana = DhatuGana.SVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA
)
