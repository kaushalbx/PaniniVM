package dev.sanskrit.derivation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
class TingantaEngineTest { @Test fun `bhu present third singular derives bhavati`() { val r=TingantaEngine().derive(TingantaDerivationRequest("भू")); assertEquals("भवति",r.final.surface); assertTrue(r.applications.map{it.sutra}.containsAll(listOf("3.2.123","3.4.78","3.1.68","7.3.84","6.1.78"))) }
 @Test fun `bhu present third dual derives bhavatah`() { assertEquals("भवतः",TingantaEngine().derive(TingantaDerivationRequest("भू",Vacana.DVIVACANA)).final.surface) }
 @Test fun `bhu present third plural derives bhavanti`() { assertEquals("भवन्ति",TingantaEngine().derive(TingantaDerivationRequest("भू",Vacana.BAHUVACANA)).final.surface) } }
