package me.felixnaumann.fsh.Enums;

public enum CommandDescriptions {

    /**
     * String-Arrays should be structured like this:
     * command usage, command description, (argument, argument description)...
     */
    UNAME_DESC(new String[]{"uname [arguments]...", "print out system information", "-a", "print out all information", "-s", "print out kernel name", "-n", "print out host name", "-v", "print out os version", "-m", "print out machine architecture", "--help", "show this menu"}),
    DEBUG_DESC(new String[]{"debug [new status]", "enable or disable the debug mode", "true", "enable debug mode", "false", "disable debug mode"}),
    ;

    private String[] cmddesc;

    CommandDescriptions(String[] cmddesc) {
        this.cmddesc = cmddesc;
    }

    public String[] getDescription() {
        return this.cmddesc;
    }
}
