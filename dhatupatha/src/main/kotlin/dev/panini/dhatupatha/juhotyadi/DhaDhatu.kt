package dev.panini.dhatupatha.juhotyadi

import dev.panini.actions.linguistic.SandhiAction
import dev.panini.actions.numeric.AdditionAction
import dev.panini.actions.numeric.CircumferenceAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.ExpressionShape
import dev.panini.execution.numericOp
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva
import dev.panini.analysis.SemanticRelation

/** Executable Juhotyādi dhātu डुधाञ् धारणपोषणयोः / सन्धाने. */
open class DhaDhatu : Dhatu(
    id = "03.0011",
    krama = 11,
    upadesha = "डुधाञ्",
    sourceSurface = "धा",
    artha = "धारणपोषणयोः",
    arthaHindi = "धारण करना, पहनना, पालन करना, सन्धि करना",
    arthaEnglish = "to wear, bear, support, nourish, protect, or join in sandhi",
    gana = DhatuGana.JUHOTYADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        SandhiAction.op {
            requires(Karaka.KARMAN, shape = ExpressionShape.COORDINATION)
            triggeredBy(requiredUpasargas = setOf("सम्"))
        },
        CircumferenceAction.op {
            requires(Karaka.KARMAN); returns(Samjna.SANKHYA)
            triggeredBy(requiredUpasargas = setOf("परि"))
        },
        AdditionAction.numericOp {
            triggeredBy(requiredUpasargas = setOf("स"))
            returns(Samjna.SANKHYA, Samjna.SHABDA)
        },
    ),
    semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT),
    surfaceAliases = setOf("सन्धा", "सन्दधाति", "दधाति", "दध"),
)
