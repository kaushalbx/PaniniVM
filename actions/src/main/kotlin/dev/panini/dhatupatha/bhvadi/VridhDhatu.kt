package dev.panini.dhatupatha.bhvadi

import dev.panini.actions.numeric.SanskritExponentiationAction
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.numericOp
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu वृधुँ वृद्धौ. */
class VridhDhatu : Dhatu(
    id = "01.0863",
    krama = 863,
    upadesha = "वृधुँ",
    sourceSurface = "वृध्",
    artha = "वृद्धौ",
    arthaHindi = "बढ़ना, वृद्धि करना, घात करना",
    arthaEnglish = "to grow, to increase, to elevate, to raise to power",
    gana = DhatuGana.BHVADI,
    pada = PadaType.ATMANEPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.AKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        SanskritExponentiationAction.numericOp(),
    ),
)
