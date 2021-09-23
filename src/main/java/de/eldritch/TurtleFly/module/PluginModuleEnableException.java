package de.eldritch.TurtleFly.module;

public class PluginModuleEnableException extends Exception {
    public PluginModuleEnableException(String message, Throwable cause) {
        super(message + " Module will be ignored.", cause);
    }
}
