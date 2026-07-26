package dev.panini.dhatupatha.bhvadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu अयन गतौ (पलाय्). */
class PalayDhatu : Dhatu(
    id = "01.0983",
    krama = 983,
    upadesha = "पलाय्",
    sourceSurface = "पलाय्",
    artha = "गतौ",
    arthaHindi = "भाग जाना, पलायन करना",
    arthaEnglish = "to flee, to run away",
    gana = DhatuGana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.AKARMAKA,
    svara = Accent.ANUDATTA,
)
