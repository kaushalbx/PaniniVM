package dev.sanskrit

import dev.sanskrit.derivation.*
import kotlin.test.Test

class ScratchTest {
    @Test
    fun testDerivationTrace() {
        val request = SubantaDerivationRequest("राम", Vibhakti.TRTIYA, Vacana.DVIVACANA)
        var current = request.initialState()
        val engine = DerivationEngine()
        
        println("=== STARTING STEP-BY-STEP TRACE FOR RAMABHYAM ===")
        val visited = mutableSetOf(current)
        for (step in 1..40) {
            val selectMethod = DerivationEngine::class.java.getDeclaredMethod("select", DerivationState::class.java, Set::class.java)
            selectMethod.isAccessible = true
            val selection = selectMethod.invoke(engine, current, emptySet<String>())
            
            val selectedField = selection.javaClass.getDeclaredField("selected")
            selectedField.isAccessible = true
            val candidate = selectedField.get(selection)
            if (candidate == null) {
                println("Step $step: No candidate selected! Derivation finished. Final surface: ${current.surface} stage: ${current.stage}")
                break
            }
            
            val sutraField = candidate.javaClass.getDeclaredField("sutra")
            sutraField.isAccessible = true
            val sutra = sutraField.get(candidate) as DerivationSutra
            
            val changeField = candidate.javaClass.getDeclaredField("change")
            changeField.isAccessible = true
            val change = changeField.get(candidate) as DerivationChange
            
            println("Step $step: Selected sutra ${sutra.sutra} -> ${change.state.terms.map { it.surface }} stage: ${change.state.stage}")
            
            if (change.state in visited) {
                println("  CYCLE DETECTED when transitioning to: ${change.state.terms.map { it.surface }}")
                break
            }
            visited.add(change.state)
            current = change.state
        }
    }
}
