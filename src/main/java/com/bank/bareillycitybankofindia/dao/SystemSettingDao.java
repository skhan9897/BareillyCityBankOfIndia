package com.bank.bareillycitybankofindia.dao;

import com.bank.bareillycitybankofindia.model.SystemSetting;

import java.util.List;

public interface SystemSettingDao {
    List<SystemSetting> findAll();
    String getValue(String key, String defaultValue);
    void updateValue(String key, String value);
}
