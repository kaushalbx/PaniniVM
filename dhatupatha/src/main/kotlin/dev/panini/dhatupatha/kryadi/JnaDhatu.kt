package dev.panini.dhatupatha.kryadi

import dev.panini.actions.control.IfAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Kryādi dhātu ज्ञा अवबोधने. */
class JnaDhatu : Dhatu(
    id = "09.0043",
    krama = 43,
    upadesha = "ज्ञा",
    sourceSurface = "ज्ञा",
    artha = "अवबोधने",
    arthaHindi = "जानना, निर्णय करना",
    arthaEnglish = "to know, to understand, to decide",
    gana = DhatuGana.KRYADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        IfAction.op {
            requires(Karaka.APADANA) // condition predicate name
            requires(Karaka.KARANA) // true action
            optional(Karaka.SAMPRADANA) // false action (else)
            returns(Samjna.SANKHYA, Samjna.SHABDA)
        }
    ),
    surfaceAliases = setOf("जानाति", "जानीते", "ज्ञा", "निर्णयः"),
)
