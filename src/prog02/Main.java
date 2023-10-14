package prog02;

/**
 * A program to query and modify the phone directory stored in csc220.txt.
 *
 * @author vjm
 */
public class Main {

    /**
     * Processes user's commands on a phone directory.
     *
     * @param fn The file containing the phone directory.
     * @param ui The UserInterface object to use
     *           to talk to the user.
     * @param pd The PhoneDirectory object to use
     *           to process the phone directory.
     */
    public static void processCommands(String fn, UserInterface ui, PhoneDirectory pd) {
        pd.loadData(fn);
        boolean changed = false;

        String[] commands = {"Add/Change Entry", "Look Up Entry", "Remove Entry", "Save Directory", "Exit"};

        String name, number, oldNumber;

        while (true) {
            int c = ui.getCommand(commands);
            switch (c) {
                case -1:
                    ui.sendMessage("You shut down the program, restarting.  Use Exit to exit.");
                    break;
                case 0:
                    name = ui.getInfo("Please enter name: ");
                    if(name == null){
                        break;
                    }
                    if(name.length() == 0){
                        ui.sendMessage("Blank names are not allowed");
                        break;
                    }
                    number = ui.getInfo("Please enter your number: ");
                    if (number == null){
                        break;
                    }
                    changed = true;
                    oldNumber = pd.addOrChangeEntry(name, number);
                    if (oldNumber == null){
                    ui.sendMessage("You have successfully added " + name + " with the number " + number);
                    break;
                    } else {
                        ui.sendMessage("New number: " + number + '\n'+ "Old number: " + oldNumber);
                    break;
                    }
                case 1:
                    name = ui.getInfo("Enter the name ");
                    if(name == null){
                        break;
                    }
                    number = pd.lookupEntry(name);
                    if(name.length() == 0){
                        ui.sendMessage("Blank names are not allowed");
                        break;
                    }
                    if (number == null){
                        ui.sendMessage("No such person exists in the directory");
                        break;
                    }
                    changed = true;
                    ui.sendMessage(name + " has number " + number);
                    break;
                case 2:
                    name = ui.getInfo("Please enter name: ");
                    if(name == null){
                        break;
                    }
                    if(name.length() == 0){
                        ui.sendMessage("Blank names are not allowed");
                        break;
                    }
                    number = pd.removeEntry(name);
                    if(number == null){
                        ui.sendMessage("No such person exists in the directory");
                        break;
                    }
                    changed = true;
                    ui.sendMessage("You have removed " + name + ". They had the number " + number);
                    break;
                case 3:
                    pd.save();
                    changed = false;
                    break;
                case 4:
                    if (changed){
                        ui.sendMessage("You are going to be asked if you want to quit without saving.");
                        String[] options = {"Yes", "No"};
                        int d = ui.getCommand(options);
                        switch (d) {
                            case -1:
                                ui.sendMessage("You shut down the program, restarting.  Use Exit to exit.");
                                break;
                            case 0:
                                return;
                            case 1:
                                break;
                        }
                        break;
                    }
                    ui.sendMessage("Thank you");
                    return;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fn = "csc220.txt";
        //PhoneDirectory pd = new ArrayBasedPD();
        PhoneDirectory pd = new SortedPD();
        //UserInterface ui = new ConsoleUI();

        UserInterface ui = new GUI("Phone Directory");
        //UserInterface ui = new TestUI("Phone Directory");
        processCommands(fn, ui, pd);
    }
}
