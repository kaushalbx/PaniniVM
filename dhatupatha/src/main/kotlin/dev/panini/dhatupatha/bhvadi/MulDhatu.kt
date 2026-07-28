package dev.panini.dhatupatha.bhvadi

import dev.panini.actions.numeric.SquareRootAction
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu मूलँ प्रतिष्ठायाम् (वर्गमूले). */
class MulDhatu : Dhatu(
    id = "01.0607",
    krama = 607,
    upadesha = "मूलँ",
    sourceSurface = "मूल्",
    artha = "प्रतिष्ठायाम्",
    arthaHindi = "जड़ जमाना, वर्गमूल निकालना",
    arthaEnglish = "to root, to establish, to compute square root",
    gana = DhatuGana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        SquareRootAction.op {
            requiresNumbers()
            returns(Samjna.SANKHYA)
        },
    ),
    surfaceAliases = setOf("मूलति", "मूल्", "वर्गमूलम्"),
)
