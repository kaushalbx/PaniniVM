package dev.panini.unadipatha.adhyaya2

import dev.panini.dhatupatha.bhvadi.ShruDhatu
import dev.panini.dhatupatha.svadi.VrDhatu
import dev.panini.dhatupatha.kryadi.DrDhatu
import dev.panini.dhatupatha.adadi.PraDhatu
import dev.panini.unadipatha.UnadiChange
import dev.panini.unadipatha.UnadiState
import dev.panini.unadipatha.UnadiSutra

// 2.8: श्रुवृदृप्रावृभ्यः सिः
object ShruVrDrPraVrbhyahSihSutra : UnadiSutra(
    number = "2.8",
    text = "श्रुवृदृप्रावृभ्यः सिः",
    hindiExplanation = "श्रु, वृ, दृ, प्रा, वृ धातुओं से सिः प्रत्यय होता है।",
    suffix = "सिः",
    roots = setOf(ShruDhatu(), VrDhatu(), DrDhatu(), PraDhatu())
) {
    override fun apply(context: UnadiState): UnadiChange {
        val root = context.root + "त्"
        return UnadiChange(
            state = context.copy(
                root = root,
                suffix = suffix,
                surface = root,
                itMarkers = emptySet(),
                stepTrace = context.stepTrace + "$number: Applied suffix $suffix with elision and tut-āgama."
            ),
            explanation = "$number: Applied suffix $suffix with elision and tut-āgama."
        )
    }
}
