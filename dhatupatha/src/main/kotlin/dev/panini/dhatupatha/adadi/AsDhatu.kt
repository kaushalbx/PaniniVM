package dev.panini.dhatupatha.adadi

import dev.panini.actions.collection.ListContainsAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.ExpressionShape
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Adādi root असँ भुवि. */
class AsDhatu : Dhatu(
    id = "02.0060",
    krama = 60,
    upadesha = "असँ",
    sourceSurface = "अस्",
    artha = "भुवि",
    arthaHindi = "होना, रहना",
    arthaEnglish = "to be, to exist",
    gana = DhatuGana.ADADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        ListContainsAction.op {
            requires(Karaka.KARMAN)
            optional(Karaka.KARANA)
            returns(Samjna.SATYA)
        }
    )
)
