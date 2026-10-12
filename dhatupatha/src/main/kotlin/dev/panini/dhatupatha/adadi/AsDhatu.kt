package dev.panini.dhatupatha.adadi

import dev.panini.actions.collection.ListContainsAction
import dev.panini.actions.comparison.CopularEqualityAction
import dev.panini.actions.comparison.CopularOrderAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.ExpressionShape
import dev.panini.execution.OperationValueType
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Adādi root असँ भुवि. */
class AsDhatu : Dhatu(
    id = "02.0060",
    krama = 60,
    upadesha = "असँ",
    sourceSurface = "अस्",
    artha = "भुवि",
    arthaHindi = "होना, रहना",
    arthaEnglish = "to be, to exist",
    gana = DhatuGana.ADADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        CopularEqualityAction.op {
            predicates(dev.panini.execution.CopularPredicate.EQUAL)
            requires(Karaka.KARTR, maximum = 1)
            requires(Karaka.KARANA, maximum = 1)
            requires(Karaka.KARMAN, maximum = 1, samjnas = arrayOf(Samjna.SHABDA))
            triggeredBy(allowedLakaras = setOf(Lakara.LAT))
            returns(Samjna.SATYA)
        },
        CopularOrderAction.op {
            predicates(dev.panini.execution.CopularPredicate.LESS_THAN, dev.panini.execution.CopularPredicate.GREATER_THAN)
            requires(Karaka.KARTR, maximum = 1, samjnas = arrayOf(Samjna.SANKHYA),
                valueType = OperationValueType.NUMBER)
            requires(Karaka.APADANA, maximum = 1, samjnas = arrayOf(Samjna.SANKHYA),
                valueType = OperationValueType.NUMBER)
            requires(Karaka.KARMAN, maximum = 1, samjnas = arrayOf(Samjna.SHABDA))
            triggeredBy(allowedLakaras = setOf(Lakara.LAT))
            returns(Samjna.SATYA)
        },
        ListContainsAction.op {
            requires(Karaka.KARTR, maximum = 1) // the entity whose presence is asserted
            requires(Karaka.ADHIKARANA, maximum = 1,
                valueType = OperationValueType.COLLECTION) // the collection in which it exists
            triggeredBy(allowedLakaras = setOf(Lakara.LAT))
            returns(Samjna.SATYA)
        },
        // Compatibility frame: prefer "वस्तु सूचीस्थम् अस्ति" expressed by
        // nominative वस्तु and locative सूची in new programs.
        ListContainsAction.op {
            requires(Karaka.KARMAN)
            optional(Karaka.KARANA)
            forbids(Karaka.KARTR, Karaka.APADANA, Karaka.ADHIKARANA)
            returns(Samjna.SATYA)
        }
    )
)
