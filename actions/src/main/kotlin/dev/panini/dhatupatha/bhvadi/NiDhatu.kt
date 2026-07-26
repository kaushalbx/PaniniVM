package dev.panini.dhatupatha.bhvadi

import dev.panini.actions.collection.SanskritListMoveAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu णिशिँ प्रापणे. */
class NiDhatu : Dhatu(
    id = "01.9902",
    krama = 9902,
    upadesha = "णिशिँ",
    sourceSurface = "नी",
    artha = "प्रापणे",
    arthaHindi = "ले जाना, पहुँचाना, मार्गदर्शन करना",
    arthaEnglish = "to lead, to guide, to bring, to carry forward",
    gana = DhatuGana.BHVADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        SanskritListMoveAction.op {
            requires(Karaka.KARMAN)
            returns(ExecutionSamjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("नयति", "नय", "नयनम्"),
)
