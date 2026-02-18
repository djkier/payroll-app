/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.app;

import com.motorph.payrollsystem.repository.UserRepository;
import com.motorph.payrollsystem.service.AuthService;

/**
 *
 * @author djjus
 */
public class AppContext {
    private final SessionManager sessionManager;
    private final AuthService authService;
    
    public AppContext() {
        this.sessionManager = new SessionManager();
        UserRepository finder = new UserRepository("/csv-files/user-accounts.csv");
        this.authService = new AuthService(finder);
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public AuthService getAuthService(){
        return authService;
    }
    
    
    
    
}
