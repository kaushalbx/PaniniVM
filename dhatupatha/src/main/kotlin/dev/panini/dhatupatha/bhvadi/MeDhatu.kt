package dev.panini.dhatupatha.bhvadi

import dev.panini.actions.collection.ListExchangeAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.ExpressionShape
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva
import dev.panini.shiksha.Samjna

/** Executable Bhvādi dhātu मेङ् प्रणिदाने, including exchange of two list positions. */
class MeDhatu : Dhatu(
    id = "01.1116",
    krama = 1116,
    upadesha = "मेङ्",
    sourceSurface = "मे",
    artha = "प्रणिदाने",
    arthaHindi = "विनिमय करना, सुपुर्द करना",
    arthaEnglish = "to exchange, to hand over",
    gana = DhatuGana.BHVADI,
    pada = PadaType.ATMANEPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        ListExchangeAction.op {
            requires(Karaka.SAMBANDHA)
            requires(Karaka.ADHIKARANA, minimum = 2, maximum = 2,
                shape = ExpressionShape.COORDINATION)
            returns(Samjna.GANA)
        },
    ),
    surfaceAliases = setOf("मयते", "विनिमयः"),
)
