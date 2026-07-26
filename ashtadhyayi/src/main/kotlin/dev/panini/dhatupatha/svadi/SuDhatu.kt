package dev.panini.dhatupatha.svadi

import dev.panini.actions.linguistic.SanskritSummarizeAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Svādi dhātu सु अभिषवे. */
class SuDhatu : Dhatu(
    id = "05.9901",
    krama = 9901,
    upadesha = "सु",
    sourceSurface = "सु",
    artha = "अभिषवे",
    arthaHindi = "अर्क निकालना, मथना, संक्षेप करना",
    arthaEnglish = "to extract, to distill, to summarize",
    gana = DhatuGana.SVADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        SanskritSummarizeAction.op {
            requires(Karaka.KARMAN)
            returns(ExecutionSamjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("सुनोति", "सु", "अभिषवः"),
)
