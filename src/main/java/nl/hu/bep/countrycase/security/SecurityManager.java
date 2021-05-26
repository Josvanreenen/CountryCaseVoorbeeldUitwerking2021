package nl.hu.bep.countrycase.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SecurityManager implements Serializable {

    private static SecurityManager securityManager;

    public static SecurityManager getInstance(){
        if(securityManager==null) securityManager = new SecurityManager();
        return securityManager;
    }

    private SecurityManager(){}

    public boolean deserialize(SecurityManager securityManager){
        if(securityManager==null) return false;
        this.securityManager = securityManager;
        return true;
    }

    private List<MyUser> allUsers = new ArrayList<>();

    public boolean registerUser(MyUser toAdd){
        if(!this.allUsers.contains(toAdd))  return this.allUsers.add(toAdd);
        return false;
    }

    public MyUser getUserByName(String username) {
        return this.allUsers.stream().filter(user -> user.getName().equals(username)).findFirst().orElse(null);
    }

    public String validateLogin(String username, String password){
        if(username==null || username.isBlank() || password == null || password.isBlank()) return null;
        var myUser = this.getUserByName(username);
        if(myUser == null) return null;
        return myUser.checkPassword(password) ? myUser.getRole() : null;
    }
}
