package dev.panini.dhatupatha.bhvadi

import dev.panini.actions.collection.ListReverseAction
import dev.panini.actions.control.LoopAction
import dev.panini.actions.control.ForEachAction
import dev.panini.actions.control.WhileAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu वृताँ वर्तने. */
class VrtDhatu : Dhatu(
    id = "01.9910",
    krama = 9910,
    upadesha = "वृताँ",
    sourceSurface = "वृत्",
    artha = "वर्तने",
    arthaHindi = "वर्तना, घूमना, पुनरावृत्ति करना",
    arthaEnglish = "to turn, to exist, to repeat/loop",
    gana = DhatuGana.BHVADI,
    pada = PadaType.ATMANEPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        LoopAction.op {
            triggeredBy(forbiddenUpasargas = setOf("अनु"))
            requires(Karaka.KARMAN, 1, null, null, Samjna.SANKHYA) // loop count (must be a number)
            requires(Karaka.KARANA) // target action name
            optional(Karaka.SAMPRADANA, Karaka.APADANA) // initial state
            returns(Samjna.SHABDA)
        },
        ListReverseAction.op {
            triggeredBy(requiredUpasargas = setOf("प्रति"))
            requires(Karaka.KARMAN) // list
            returns(Samjna.GANA)
        },
        WhileAction.op {
            triggeredBy(forbiddenUpasargas = setOf("अनु"))
            requires(Karaka.KARMAN, 1, null, null, Samjna.SHABDA) // condition predicate (must be a name/word)
            requires(Karaka.KARANA) // body action
            requires(Karaka.SAMPRADANA) // initial state
            returns(Samjna.SANKHYA, Samjna.SHABDA)
        },
        ForEachAction.op {
            triggeredBy(requiredUpasargas = setOf("अनु"))
            requires(Karaka.KARMAN) // list
            requires(Karaka.KARANA) // body action
            optional(Karaka.SAMPRADANA) // initial state/accumulator
            returns(Samjna.GANA, Samjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("वर्तते", "वृत्", "अनुवृत्तिः", "पुनरावृत्तिः"),
)
