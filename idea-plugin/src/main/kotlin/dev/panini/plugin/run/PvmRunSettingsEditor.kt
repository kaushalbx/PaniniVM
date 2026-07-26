package dev.panini.plugin.run

import com.intellij.openapi.options.SettingsEditor
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

class PvmRunSettingsEditor : SettingsEditor<PvmRunConfiguration>() {
    private val myPanel: JPanel
    private val scriptPathField = JTextField()

    init {
        myPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent("Script path:", scriptPathField)
            .panel
    }

    override fun resetEditorFrom(s: PvmRunConfiguration) {
        scriptPathField.text = s.scriptPath ?: ""
    }

    override fun applyEditorTo(s: PvmRunConfiguration) {
        s.scriptPath = scriptPathField.text
    }

    override fun createEditor(): JComponent = myPanel
}
