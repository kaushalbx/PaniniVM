package dev.panini.dhatupatha.rudhadi

import dev.panini.actions.numeric.AdditionAction
import dev.panini.actions.numeric.ComparisonAction
import dev.panini.actions.numeric.SubtractionAction
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
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
        AdditionAction.numericOp {
            triggeredBy(forbiddenUpasargas = setOf("वि", "तुल्"))
            returns(Samjna.SANKHYA, Samjna.SHABDA)
        },
        SubtractionAction.numericOp {
            triggeredBy(requiredUpasargas = setOf("वि"))
            returns(Samjna.SANKHYA, Samjna.SHABDA)
        },
        ComparisonAction.GreaterThan.numericOp {
            triggeredBy(requiredUpasargas = setOf("तुल्"))
            returns(Samjna.SHABDA)
        },
    ),
    semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT),
)
