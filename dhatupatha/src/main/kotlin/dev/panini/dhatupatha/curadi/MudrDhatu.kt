package dev.panini.dhatupatha.curadi

import dev.panini.actions.io.PrintAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/**
 * Executable Curādi dhātu मुद्रँ संसर्ग-संवरकयोः / मुद्रणाय.
 */
class MudrDhatu : Dhatu(
    id = "10.0510",
    krama = 510,
    upadesha = "मुद्रँ",
    sourceSurface = "मुद्र्",
    artha = "संसर्ग-संवरकयोः",
    arthaHindi = "छापना, मुद्रित करना, मुहर लगाना",
    arthaEnglish = "to stamp, to print, to seal",
    gana = DhatuGana.CURADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        PrintAction.op {
            requires(Karaka.KARMAN)
            returns(Samjna.SHABDA)
        }
    ),
)
