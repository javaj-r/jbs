package com.javid;

import com.javid.console.*;
import com.javid.util.Screen;

/**
 * @author javid
 * Created on 1/18/2022
 */
public class Application {

    public static void main(String[] args) {
        mainMenu();
    }

    private static void mainMenu() {
        while (true) {
            try {

                int choice = Screen.showMenu("", "", "Select from menu: ", "Invalid choice."
                        , "Exit"
                        , "Branch"
                        , "Employee"
                        , "Customer"
                        , "Account"
                        , "Card"
                        , "Transaction"
                );

                if (choice == 0)
                    break;

                switch (choice) {
                    case 1 -> BranchConsole.getInstance().mainMenu();
                    case 2 -> EmployeeConsole.getInstance().mainMenu();
                    case 3 -> CustomerConsole.getInstance().mainMenu();
                    case 4 -> AccountConsole.getInstance().mainMenu();
                    case 5 -> CardConsole.getInstance().mainMenu();
                    case 6 -> TransactionConsole.getInstance().mainMenu();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isForUpdate(String element) {
        return !"-".equals(element.trim());
    }
}
