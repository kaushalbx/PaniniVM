package dev.panini.dhatupatha.rudhadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Canonical Rudhādi entry युजिँर् योगे; executable meanings live in execution registry. */
class YujirDhatu : Dhatu(
    id = "07.0007",
    krama = 7,
    upadesha = "युजिँर्",
    sourceSurface = "युज्",
    artha = "योगे",
    arthaHindi = "जुड़ना, मिलाप करना, एकत्र करना",
    arthaEnglish = "to bind, restrain, join, unite, apply, or combine",
    gana = DhatuGana.RUDHADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
)
