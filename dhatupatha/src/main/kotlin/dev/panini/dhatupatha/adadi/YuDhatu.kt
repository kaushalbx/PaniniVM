package dev.panini.dhatupatha.adadi

import dev.panini.actions.numeric.AdditionAction
import dev.panini.actions.numeric.SubtractionAction
import dev.panini.actions.collection.ListMapAction
import dev.panini.actions.collection.ListFilterAction
import dev.panini.actions.numeric.IsEvenAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.numericOp
import dev.panini.execution.op
import dev.panini.execution.ExpressionShape
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Adādi dhātu यु मिश्रणेऽमिश्रणे च (Addition & Subtraction / Yu, Yuta). */
open class YuDhatu : Dhatu(
    id = "02.0027",
    krama = 27,
    upadesha = "यु",
    sourceSurface = "यु",
    artha = "मिश्रणेऽमिश्रणे च",
    arthaHindi = "मिश्रित करना, मिलाना, युत करना, सूचीसंयोजनम्",
    arthaEnglish = "to mix, to join, to group, to add, to map/iterate over list",
    gana = DhatuGana.ADADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        AdditionAction.numericOp {
            triggeredBy(forbiddenUpasargas = setOf("वि", "सम्"))
            returns(Samjna.SANKHYA, Samjna.SHABDA)
        },
        SubtractionAction.numericOp {
            triggeredBy(requiredUpasargas = setOf("वि"))
            returns(Samjna.SANKHYA, Samjna.SHABDA)
        },
        ListMapAction.op {
            triggeredBy(requiredUpasargas = setOf("सम्"))
            requires(Karaka.KARMAN) // list
            requires(Karaka.KARANA) // target action name
            returns(Samjna.GANA)
        },
        IsEvenAction.op {
            requires(Karaka.KARMAN, shape = ExpressionShape.LITERAL)
            returns(Samjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("युत", "युयोति", "युते", "मिश्रणम्", "संयोजनम्"),
)

/** Executable Adādi dhātu वृजीँ वर्जने (Subtraction / Vrj, Varjita). */
open class VrjDhatu : Dhatu(
    id = "02.0022",
    krama = 22,
    upadesha = "वृजीँ",
    sourceSurface = "वृज्",
    artha = "वर्जने",
    arthaHindi = "छोड़ना, वर्जित करना, घटाना",
    arthaEnglish = "to avoid, to abandon, to leave, to subtract",
    gana = DhatuGana.ADADI,
    pada = PadaType.ATMANEPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        SubtractionAction.numericOp {
            triggeredBy(forbiddenUpasargas = setOf("वि"))
            returns(Samjna.SANKHYA, Samjna.SHABDA)
        },
        ListFilterAction.op {
            triggeredBy(requiredUpasargas = setOf("वि"))
            requires(Karaka.KARMAN) // list
            requires(Karaka.KARANA) // target predicate operation name
            returns(Samjna.GANA)
        },
    ),
    surfaceAliases = setOf("वर्जित", "विवर्जित", "वृङ्क्ते", "शोधनम्", "वर्जनम्"),
)
