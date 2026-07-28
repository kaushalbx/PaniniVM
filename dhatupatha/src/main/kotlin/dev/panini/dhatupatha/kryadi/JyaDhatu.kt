package dev.panini.dhatupatha.kryadi

import dev.panini.actions.numeric.TrigonometryAction
import dev.panini.actions.numeric.CircumferenceAction
import dev.panini.actions.numeric.HypotenuseAction
import dev.panini.actions.numeric.AreaAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable dhātu ज्या वयोहानौ (Jya / Trigonometric & Geometric operations). */
class JyaDhatu : Dhatu(
    id = "09.0034",
    krama = 34,
    upadesha = "ज्या",
    sourceSurface = "ज्या",
    artha = "वयोहानौ",
    arthaHindi = "ज्यासाधनम्",
    arthaEnglish = "to calculate sine/trigonometry",
    gana = DhatuGana.KRYADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.AKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        TrigonometryAction.op {
            requires(Karaka.KARMAN); returns(Samjna.SANKHYA)
        },
    ),
    surfaceAliases = setOf("ज्या", "जीनाति", "जयति", "कोटिज्या", "स्पर्शज्या", "उत्क्रमज्या"),
)
