package dev.panini.dhatupatha.rudhadi

import dev.panini.actions.numeric.ComparisonAction
import dev.panini.actions.numeric.MinAction
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

/** Executable Rudhādi dhātu विदँ विचारणे. */
class VidDhatu : Dhatu(
    id = "07.0013",
    krama = 13,
    upadesha = "विदँ",
    sourceSurface = "विद्",
    artha = "विचारणे",
    arthaHindi = "मनन करना, विचार करना, तुलना करना",
    arthaEnglish = "to think, to analyze, to compare",
    gana = DhatuGana.RUDHADI,
    pada = PadaType.ATMANEPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        ComparisonAction.GreaterThan.op {
            requiresNumbers(shape = ExpressionShape.COORDINATION)
            triggeredBy(forbiddenAvyayas = setOf("न्यूनतया"), forbiddenUpasargas = setOf("नि"))
            returns(Samjna.SATYA)
        },
        ComparisonAction.LessThan.op {
            requiresNumbers(shape = ExpressionShape.COORDINATION)
            triggeredBy(requiredUpasargas = setOf("नि"))
            returns(Samjna.SATYA)
        },
        MinAction.op {
            requiresNumbers(shape = ExpressionShape.COORDINATION)
            bindsResultTo(Karaka.SAMPRADANA)
            triggeredBy(requiredAvyayas = setOf("न्यूनतया"))
            returns(Samjna.SANKHYA)
        },
    ),
)
