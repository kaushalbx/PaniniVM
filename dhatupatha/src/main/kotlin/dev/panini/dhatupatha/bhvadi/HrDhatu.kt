package dev.panini.dhatupatha.bhvadi

import dev.panini.actions.numeric.SanskritDivisionAction
import dev.panini.actions.collection.SanskritListPopAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.numericOp
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu हृञ् हरणे. */
class HrDhatu : Dhatu(
    id = "01.1046",
    krama = 1046,
    upadesha = "हृञ्",
    sourceSurface = "हृ",
    artha = "हरणे",
    arthaHindi = "ले जाना, हरण करना, चोरी करना",
    arthaEnglish = "to take away, to carry, to steal, to acquire, to divide, to pop item from list",
    gana = DhatuGana.BHVADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.DVIKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        SanskritDivisionAction.numericOp(),
        SanskritListPopAction.op {
            triggeredBy(requiredUpasargas = setOf("उद्"))
            requires(Karaka.KARMAN)
            returns(Samjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("हरति", "हरणम्", "उद्धरणम्"),
)
