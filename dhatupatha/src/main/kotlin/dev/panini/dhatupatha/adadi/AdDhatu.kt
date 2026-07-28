package dev.panini.dhatupatha.adadi

import dev.panini.actions.resource.SanskritConsumeAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Adādi dhātu अदँ भक्षणे. */
class AdDhatu : Dhatu(
    id = "02.9901",
    krama = 9901,
    upadesha = "अदँ",
    sourceSurface = "अद्",
    artha = "भक्षणे",
    arthaHindi = "खाना, भक्षण करना, उपभोग करना",
    arthaEnglish = "to eat, to consume, to devour",
    gana = DhatuGana.ADADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        SanskritConsumeAction.op {
            requires(Karaka.KARMAN)
            returns(Samjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("अत्ति", "अद्", "भक्षणम्"),
)
