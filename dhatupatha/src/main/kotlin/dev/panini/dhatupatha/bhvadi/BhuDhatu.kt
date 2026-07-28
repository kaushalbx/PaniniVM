package dev.panini.dhatupatha.bhvadi

import dev.panini.actions.state.SanskritStateInstantiateAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu भूँ सत्तायाम्. */
class BhuDhatu : Dhatu(
    id = "01.9904",
    krama = 9904,
    upadesha = "भूँ",
    sourceSurface = "भू",
    artha = "सत्तायाम्",
    arthaHindi = "होना, उत्पन्न होना, सत्ता प्राप्त करना",
    arthaEnglish = "to be, to exist, to become, to happen, to instantiate",
    gana = DhatuGana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        SanskritStateInstantiateAction.op {
            requires(Karaka.KARMAN)
            returns(Samjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("भवति", "भव", "भवनम्", "सत्ता"),
)
