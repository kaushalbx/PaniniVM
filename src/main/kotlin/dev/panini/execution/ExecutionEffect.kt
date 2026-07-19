package dev.panini.execution

enum class ExecutionEffect {
    PURE,
    READ_MEMORY,
    WRITE_MEMORY,
    READ_RESOURCE,
    WRITE_RESOURCE,
    DELETE_RESOURCE,
    NETWORK,
    SEND_MESSAGE,
    EXECUTE_PROCESS,
}
