package dev.panini.dhatupatha.bhvadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.core.Karaka
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/**
 * 01.0601 स्मृँ (आध्याने) - Context persistence and state memory retrieval.
 */
class SmrDhatu : Dhatu(
    id = "01.0601",
    krama = 601,
    upadesha = "स्मृँ",
    sourceSurface = "स्मृ",
    artha = "आध्याने",
    arthaHindi = "याद करना, स्मरण रखना",
    arthaEnglish = "to remember, to keep in mind, to retain context",
    gana = DhatuGana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
) {

}
