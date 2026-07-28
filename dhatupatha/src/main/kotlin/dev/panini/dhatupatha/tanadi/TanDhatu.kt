package dev.panini.dhatupatha.tanadi

import dev.panini.actions.collection.ListFlattenAction
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

/** Executable Tanādi root तनुँ विस्तारे. */
class TanDhatu : Dhatu(
    id = "08.0001",
    krama = 1,
    upadesha = "तनुँ",
    sourceSurface = "तन्",
    artha = "विस्तारे",
    arthaHindi = "फैलाना, बढ़ाना",
    arthaEnglish = "to spread, to stretch, to expand, to increase",
    gana = DhatuGana.TANADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        ListFlattenAction.op {
            requires(Karaka.KARMAN)
            returns(Samjna.GANA)
        }
    )
)
