package com.javid.service;

import com.javid.model.Bank;
import com.javid.model.Branch;
import com.javid.repository.BranchRepository;
import com.javid.repository.jdbc.BranchRepositoryImpl;

import java.util.List;

/**
 * @author javid
 * Created on 1/18/2022
 */
public class BranchService {

    private BranchRepository repository = new BranchRepositoryImpl();

    public Branch create(Branch branch) {
        branch.setId(repository.save(branch));
        return branch;
    }

    public List<Branch> findAll() {
        return repository.findAll();
    }

    public List<Branch> findAll(Bank bank) {
        return repository.findAll().stream()
                .filter(branch -> branch.getBank() != null)
                .filter(branch -> bank.getId().equals(branch.getBank().getId()))
                .toList();
    }

    public void update(Branch branch) {
        repository.update(branch);
    }

    public void delete(Branch branch) {
        if (!branch.isNew())
            repository.deleteById(branch.getId());
    }
}
