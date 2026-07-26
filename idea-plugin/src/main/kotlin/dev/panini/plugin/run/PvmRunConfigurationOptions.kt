package dev.panini.plugin.run

import com.intellij.execution.configurations.LocatableRunConfigurationOptions
import com.intellij.openapi.components.StoredProperty

class PvmRunConfigurationOptions : LocatableRunConfigurationOptions() {
    private val scriptPathProperty: StoredProperty<String?> = string("").provideDelegate(this, "scriptPath")
    var scriptPath: String?
        get() = scriptPathProperty.getValue(this)
        set(value) = scriptPathProperty.setValue(this, value)
}
