package dev.panini.dhatupatha.bhvadi

import dev.panini.actions.resource.ResourceReleaseAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu पाँ पाने. */
class PaaDhatu : Dhatu(
    id = "01.9903",
    krama = 9903,
    upadesha = "पाँ",
    sourceSurface = "पा",
    artha = "पाने",
    arthaHindi = "पीना, उपभोग करना",
    arthaEnglish = "to drink, to consume, to absorb",
    gana = DhatuGana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        ResourceReleaseAction.op {
            requires(Karaka.KARMAN)
            returns(Samjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("पिबति", "पिब", "पानम्"),
)
