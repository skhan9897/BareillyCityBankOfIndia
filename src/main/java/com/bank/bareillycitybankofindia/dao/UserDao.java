package com.bank.bareillycitybankofindia.dao;

import com.bank.bareillycitybankofindia.model.Role;
import com.bank.bareillycitybankofindia.model.User;

import java.util.List;

public interface UserDao {
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(long id);
    long createUser(User user, int roleId);
    void updatePassword(long userId, String newPasswordHash);
    void updateStatus(long userId, String status);
    void recordFailedLogin(String username);
    void resetFailedLogin(long userId);
    void lockUser(long userId, int durationMinutes);
    List<Role> getUserRoles(long userId);
}
