package dev.panini.dhatupatha.divadi

import dev.panini.actions.numeric.SanskritRandomChoiceAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Divādi dhātu दिवूँ क्रीडायाम्. */
class DivDhatu : Dhatu(
    id = "04.9901",
    krama = 9901,
    upadesha = "दिवूँ",
    sourceSurface = "दिव्",
    artha = "क्रीडाविजिगीषाव्यवहारद्युतिस्तुतिमोदमदस्वप्नकान्तिगतिषु",
    arthaHindi = "खेलना, क्रीडा करना, यादृच्छिक निष्पादन करना",
    arthaEnglish = "to play, to gamble, to compute random choice",
    gana = DhatuGana.DIVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        SanskritRandomChoiceAction.op {
            requires(Karaka.KARMAN)
            returns(Samjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("दीव्यति", "दीव्", "क्रीडा"),
)
