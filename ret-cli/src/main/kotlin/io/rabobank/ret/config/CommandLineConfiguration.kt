package io.rabobank.ret.config

import io.quarkus.picocli.runtime.PicocliCommandLineFactory
import io.rabobank.ret.RetConsole
import io.rabobank.ret.plugins.PluginLoader
import picocli.CommandLine
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces

@ApplicationScoped
class CommandLineConfiguration(val pluginLoader: PluginLoader) {

    @Produces
    fun customCommandLine(factory: PicocliCommandLineFactory, retConsole: RetConsole): CommandLine {
        val commandLine =  factory.create()
            .setExecutionExceptionHandler(ExceptionMessageHandler(retConsole))
            .setExecutionStrategy {
                CommandLine.RunLast().execute(it)
            }

        pluginLoader.getPluginCommands(commandLine).forEach {
            commandLine.addSubcommand(it.name, it.commandSpec)
        }

        return commandLine
    }
}
