package dev.panini

import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val output = PrintStream(FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8)
    System.setOut(output)

    val exitCode = CliApplication(output = output).run(args)
    if (exitCode != 0) exitProcess(exitCode)
}
