package dev.panini.dhatupatha

import dev.panini.core.SanadiAffix
import dev.panini.core.Upasarga
import kotlin.test.*

class GrammaticalTriggerCatalogTest {
    @Test fun `executable dhatu prefix and sanadi constraints use grammatical identities`() {
        for (dhatu in DhatuPatha.all) for (operation in dhatu.operations) {
            val trigger = operation.trigger
            for (prefix in trigger.requiredUpasargas + trigger.forbiddenUpasargas) {
                assertNotNull(Upasarga.fromUpadesha(prefix), "${dhatu.id} ${operation.name}: $prefix")
            }
            for (affix in trigger.requiredSanadi + trigger.forbiddenSanadi) {
                assertNotNull(SanadiAffix.fromUpadesha(affix), "${dhatu.id} ${operation.name}: $affix")
            }
        }
    }
}
