package dev.panini.actions.sutra

import dev.panini.actions.numeric.AdditionAction
import dev.panini.actions.numeric.MultiplicationAction
import dev.panini.actions.numeric.SubtractionAction
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.SamjnaDeclaration
import dev.panini.sutra.runtime.SutraBlueprintGrantha
import dev.panini.sutra.runtime.SutraGranthaRegistry

/**
 * Discoverable registry for application Dhātu Actions carrying [kind = "dhatu-action"] in SutraArtha.
 */
object DhatuActionRuntimeGrantha {
    val id: GranthaId = GranthaId("dhatu-actions")

    val blueprint: SutraBlueprintGrantha = SutraBlueprintGrantha(
        id = id,
        sutras = listOf(
            AdditionAction.blueprint,
            SubtractionAction.blueprint,
            MultiplicationAction.blueprint,
        ),
        samjnas = listOf(
            SamjnaDeclaration(
                name = "numeric-fold",
                description = "A checked fold over saṅkhyā operands.",
            ),
        ),
        exports = setOf(
            AdditionAction.blueprint.id,
            SubtractionAction.blueprint.id,
            MultiplicationAction.blueprint.id,
        ),
    )

    val registry: SutraGranthaRegistry = SutraGranthaRegistry(emptyList())
}
