package dev.panini.actions.numeric

import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.SamjnaDeclaration
import dev.panini.sutra.runtime.SutraBlueprintGrantha

/** Canonical, inspectable library of migrated numeric dhātu actions. */
object NumericDhatuActionGrantha {
    val blueprint = SutraBlueprintGrantha(
        id = GranthaId("dhatu-actions.numeric"),
        sutras = listOf(
            AdditionAction.blueprint,
            SubtractionAction.blueprint,
            MultiplicationAction.blueprint,
            DivisionAction.blueprint,
            ModuloAction.blueprint,
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
            DivisionAction.blueprint.id,
            ModuloAction.blueprint.id,
        ),
    )
}
