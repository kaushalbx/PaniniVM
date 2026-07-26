package dev.panini.dhatupatha.rudhadi

import dev.panini.actions.numeric.SanskritAdditionAction
import dev.panini.actions.numeric.SanskritComparisonAction
import dev.panini.actions.numeric.SanskritSubtractionAction
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.numericOp
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva
import dev.panini.analysis.SemanticRelation

/** Canonical Rudhādi entry युजिँर् योगे. */
open class YujirDhatu : Dhatu(
    id = "07.0007",
    krama = 7,
    upadesha = "युजिँर्",
    sourceSurface = "युज्",
    artha = "योगे",
    arthaHindi = "जुड़ना, मिलाप करना, एकत्र करना",
    arthaEnglish = "to bind, restrain, join, unite, apply, or combine",
    gana = DhatuGana.RUDHADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        SanskritAdditionAction.numericOp {
            triggeredBy(forbiddenUpasargas = setOf("वि", "तुल्"))
            returns(ExecutionSamjna.SANKHYA, ExecutionSamjna.SHABDA)
        },
        SanskritSubtractionAction.numericOp {
            triggeredBy(requiredUpasargas = setOf("वि"))
            returns(ExecutionSamjna.SANKHYA, ExecutionSamjna.SHABDA)
        },
        SanskritComparisonAction.GreaterThan.numericOp {
            triggeredBy(requiredUpasargas = setOf("तुल्"))
            returns(ExecutionSamjna.SHABDA)
        },
    ),
    semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT),
)
