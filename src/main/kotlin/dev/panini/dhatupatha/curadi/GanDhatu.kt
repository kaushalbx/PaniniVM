package dev.panini.dhatupatha.curadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.core.Karaka
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Curādi dhātu गण संख्याने. */
class GanDhatu : Dhatu(
    id = "10.0391",
    krama = 391,
    upadesha = "गण",
    sourceSurface = "गण",
    artha = "सङ्ख्याने",
    arthaHindi = "गिनना, गुणा करना",
    arthaEnglish = "to count, to enumerate, to multiply",
    gana = DhatuGana.CURADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
) {

}
