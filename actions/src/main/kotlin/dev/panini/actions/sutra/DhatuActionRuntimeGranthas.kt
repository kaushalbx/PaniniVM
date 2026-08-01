package dev.panini.actions.sutra

import dev.panini.actions.numeric.NumericDhatuActionGrantha
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.SutraBlueprintGrantha

/**
 * Discoverable registry for application Dhātu Actions carrying [kind = "dhatu-action"] in SutraArtha.
 */
@Deprecated("Use NumericDhatuActionGrantha, the authoritative migrated action grantha.")
object DhatuActionRuntimeGrantha {
    val id: GranthaId get() = NumericDhatuActionGrantha.blueprint.id
    val blueprint: SutraBlueprintGrantha get() = NumericDhatuActionGrantha.blueprint
}
