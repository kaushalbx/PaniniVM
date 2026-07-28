package dev.panini.dhatupatha.bhvadi

import dev.panini.actions.numeric.SanskritScaleAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu एधँ वृद्धौ. */
class EdhDhatu : Dhatu(
    id = "01.9905",
    krama = 9905,
    upadesha = "एधँ",
    sourceSurface = "एध्",
    artha = "वृद्धौ",
    arthaHindi = "बढ़ना, वृद्धि प्राप्त करना",
    arthaEnglish = "to grow, to expand, to scale",
    gana = DhatuGana.BHVADI,
    pada = PadaType.ATMANEPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        SanskritScaleAction.op {
            requires(Karaka.KARMAN)
            returns(Samjna.SANKHYA, Samjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("एधते", "एध", "वर्धनम्"),
)
