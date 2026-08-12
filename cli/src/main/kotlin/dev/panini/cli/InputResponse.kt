package dev.panini.cli

internal sealed interface InputResponse {
    data class Value(val text: String) : InputResponse
    data object Cancelled : InputResponse
    data object EndOfInput : InputResponse
}

internal class InteractiveInputTerminated(
    val response: InputResponse,
    val subject: String,
) : RuntimeException(null, null, false, false)
