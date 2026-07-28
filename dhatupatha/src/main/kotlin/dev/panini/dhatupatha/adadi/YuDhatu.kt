package dev.panini.dhatupatha.adadi

import dev.panini.actions.numeric.SanskritAdditionAction
import dev.panini.actions.numeric.SanskritSubtractionAction
import dev.panini.actions.collection.SanskritListMapAction
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
        SanskritAdditionAction.numericOp {
            triggeredBy(forbiddenUpasargas = setOf("वि"))
            returns(Samjna.SANKHYA, Samjna.SHABDA)
        },
        SanskritSubtractionAction.numericOp {
            triggeredBy(requiredUpasargas = setOf("वि"))
            returns(Samjna.SANKHYA, Samjna.SHABDA)
        },
        SanskritListMapAction.op {
            requires(Karaka.KARMAN) // list
            requires(Karaka.KARANA) // target action name
            returns(Samjna.GANA)
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
        SanskritSubtractionAction.numericOp {
            returns(Samjna.SANKHYA, Samjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("वर्जित", "विवर्जित", "वृङ्क्ते"),
)
