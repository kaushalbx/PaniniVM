package dev.panini.execution

object DispositionResolver {
    fun resolve(nirdesha: Nirdesha): ExecutionDisposition = when (nirdesha.prayojana) {
        VakyaPrayojana.AJNA -> if (nirdesha.polarity == Polarity.NEGATIVE) ExecutionDisposition.CONSTRAIN else ExecutionDisposition.EXECUTE
        VakyaPrayojana.PRARTHANA -> if (nirdesha.polarity == Polarity.NEGATIVE) ExecutionDisposition.CONSTRAIN else ExecutionDisposition.REQUEST_EXECUTION
        VakyaPrayojana.PRASHNA -> ExecutionDisposition.QUERY
        VakyaPrayojana.VIDHANA -> ExecutionDisposition.DECLARE
        VakyaPrayojana.NISHEDHA -> ExecutionDisposition.CONSTRAIN
        VakyaPrayojana.ASHAMSA -> ExecutionDisposition.DESIRE
        VakyaPrayojana.ANUMATI -> ExecutionDisposition.GRANT
        VakyaPrayojana.NIMANTRANA -> ExecutionDisposition.OFFER
    }
}
