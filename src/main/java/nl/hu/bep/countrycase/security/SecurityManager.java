package nl.hu.bep.countrycase.security;

import java.util.ArrayList;
import java.util.List;

public class SecurityManager {

    private static List<MyUser> allUsers = new ArrayList<>();

    public static boolean registerUser(MyUser toAdd){
        if(!allUsers.contains(toAdd))  return allUsers.add(toAdd);
        return false;
    }

    public static MyUser getUserByName(String username) {
        return allUsers.stream().filter(user -> user.getName().equals(username)).findFirst().orElse(null);
    }

    public static String validateLogin(String username, String password){
        if(username==null || username.isBlank() || password == null || password.isBlank()) return null;
        var myUser = getUserByName(username);
        if(myUser == null) return null;
        return myUser.checkPassword(password) ? myUser.getRole() : null;
    }
}
