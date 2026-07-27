package dev.panini.unadipatha.adhyaya3

import dev.panini.dhatupatha.bhvadi.PumsDhatu
import dev.panini.unadipatha.UnadiChange
import dev.panini.unadipatha.UnadiState
import dev.panini.unadipatha.UnadiSutra

// 3.1: पुंसोऽसुन्
object PunsoAsunSutra : UnadiSutra(
    number = "3.1",
    text = "पुंसोऽसुन्",
    hindiExplanation = "पुम् धातु से असुन् प्रत्यय होता है।",
    suffix = "असुन्",
    roots = setOf(PumsDhatu())
) {
    override fun apply(context: UnadiState): UnadiChange {
        return UnadiChange(
            state = context.copy(
                suffix = suffix,
                surface = context.root + "अस्",
                itMarkers = emptySet(),
                stepTrace = context.stepTrace + "$number: Applied suffix $suffix (अस्) after root."
            ),
            explanation = "$number: Applied suffix $suffix (अस्) after root."
        )
    }
}
