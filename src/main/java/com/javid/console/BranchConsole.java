package com.javid.console;

import com.javid.model.Branch;
import com.javid.model.Employee;
import com.javid.service.BranchService;
import com.javid.util.Screen;

import java.util.List;

/**
 * @author javid
 * Created on 1/18/2022
 */
public class BranchConsole {

    private Branch branch;
    private final BranchService branchService;

    private static class Singleton {
        private static final BranchConsole INSTANCE = new BranchConsole();
    }

    public static BranchConsole getInstance() {
        return Singleton.INSTANCE;
    }

    private BranchConsole() {
        branchService = new BranchService();
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public void mainMenu() {
        while (true) {
            int choice = Screen.showMenu("", "", "Select from menu: ", "Invalid choice."
                    , "Back to main"
                    , "Select branch"
                    , "Create branch"
                    , "Update branch"
                    , "Delete branch");

            if (choice == 0)
                break;

            switch (choice) {
                case 1 -> selectBranch();
                case 2 -> createBranch();
                case 3 -> updateBranch();
                case 4 -> deleteBranch();
            }
        }
    }

    public void selectBranch() {
        Branch branch1 = selectBranch("Select from menu: ");
        if (!branch1.isNew()) {
            branch = branch1;
        }
    }

    public Branch selectBranch(String message) {
        List<Branch> branches = branchService.findAll();
        String[] arr = branches.stream()
                .map(Branch::toString)
                .toList()
                .toArray(new String[0]);

        int choice = Screen.showMenu("", "", message, "Invalid choice"
                , "Cancel"
                , arr);

        if (choice == 0) {
            return new Branch();
        }

        return branches.get(choice - 1);
    }

    private void createBranch() {
        Branch branch1 = new Branch()
                .setName(Screen.getString("Enter branch name: "));

        branch1 = branchService.create(branch1);
        if (!branch1.isNew()) {
            branch = branch1;
        }
    }

    private void updateBranch() {
        Branch branch1 = selectBranch("Select branch to update.");
        String name = Screen.getString("Enter - or new name: ");
        if (!"-".equals(name.trim())) {
            branch.setName(name);
        }

        Employee manager = EmployeeConsole.getInstance().selectEmployee("Select new manager or cancel: ");
        if (!manager.isNew() && manager.getId() > 0) {
            branch1.setManager(manager);
        }

        branchService.update(branch1);
    }

    private void deleteBranch() {
        Branch branch1 = selectBranch("Select branch to delete");
        if (branch1.isNew())
            return;
        branchService.delete(branch1);
    }
}
