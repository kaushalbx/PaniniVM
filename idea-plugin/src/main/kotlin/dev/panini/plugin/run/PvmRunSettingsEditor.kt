package dev.panini.plugin.run

import com.intellij.openapi.options.SettingsEditor
import com.intellij.util.ui.FormBuilder
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

class PvmRunSettingsEditor : SettingsEditor<PvmRunConfiguration>() {
    private val myPanel: JPanel
    private val scriptPathField = JTextField()
    private val executionModeComboBox = JComboBox(arrayOf("Direct VM (In-Process)", "Compilation (Gradle Subprocess)"))

    init {
        myPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent("Script path:", scriptPathField)
            .addLabeledComponent("Execution mode:", executionModeComboBox)
            .panel
    }

    override fun resetEditorFrom(s: PvmRunConfiguration) {
        scriptPathField.text = s.scriptPath ?: ""
        executionModeComboBox.selectedIndex = if (s.runViaVm) 0 else 1
    }

    override fun applyEditorTo(s: PvmRunConfiguration) {
        s.scriptPath = scriptPathField.text
        s.runViaVm = (executionModeComboBox.selectedIndex == 0)
    }

    override fun createEditor(): JComponent = myPanel
}
