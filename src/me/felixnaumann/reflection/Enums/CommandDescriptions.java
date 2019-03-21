package me.felixnaumann.reflection.Enums;

public enum CommandDescriptions {

    /**
     * String-Arrays should be structured like this:
     * command usage, command description, (argument, argument description)...
     */
    UNAME_DESC(new String[]{"uname [arguments]...", "print out system information", "-a", "print out all information", "-s", "print out kernel name", "-n", "print out host name", "-v", "print out os version", "-m", "print out machine architecture", "--help", "show this menu"}),
    ;

    private String[] cmddesc;

    CommandDescriptions(String[] cmddesc) {
        this.cmddesc = cmddesc;
    }

    public String[] getDescription() {
        return this.cmddesc;
    }
}
