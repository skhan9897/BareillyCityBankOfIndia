package com.bank.bareillycitybankofindia.dao;

import com.bank.bareillycitybankofindia.model.Branch;

import java.util.List;

public interface BranchDao {
    List<Branch> findAll();
    Branch findById(int id);
    int createBranch(Branch branch);
}
