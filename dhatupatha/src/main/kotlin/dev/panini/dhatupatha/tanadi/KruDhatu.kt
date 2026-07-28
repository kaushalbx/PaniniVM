package dev.panini.dhatupatha.tanadi

import dev.panini.actions.linguistic.SanskritSandhiAction
import dev.panini.actions.linguistic.SanskritSubantaDerivationAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.ExpressionShape
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Tanādi dhātu डुकृञ् करणे. */
class KruDhatu : Dhatu(
    id = "08.0010",
    krama = 10,
    upadesha = "डुकृञ्",
    sourceSurface = "कृ",
    artha = "करणे",
    arthaHindi = "करना, रचना करना, रूपनिष्पत्ति करना",
    arthaEnglish = "to do, to act, to make, to transform, to join sandhi",
    gana = DhatuGana.TANADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        SanskritSandhiAction.op {
            requires(Karaka.KARMAN, minimum = 2, shape = ExpressionShape.COORDINATION)
            triggeredBy(requiredAvyayas = setOf("इति")); returns(Samjna.SHABDA)
        },
        SanskritSubantaDerivationAction.op {
            requires(Karaka.KARMAN); returns(Samjna.SHABDA)
        },
    ),
)
