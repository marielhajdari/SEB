package testing;

import manage.User;
import org.junit.jupiter.api.Test;
import org.junit.Assert;

public class UserLogisticsTest {
    @Test
    public void testRegister(){
        User userTest = new User();
        boolean didRegister = true;

        boolean res = userTest.registerUser("kot", "5678");

        Assert.assertEquals(didRegister, res);
    }

    @Test
    public void testLogin(){
        User testUser = new User();
        boolean isLogged = true;

        boolean res = testUser.loginUser("kot", "5678");

        Assert.assertEquals(isLogged,res);
    }

    @Test
    public void testAlreadyRegistered(){
        User userTest = new User();
        boolean canRegister = false;

        boolean res = userTest.registerUser("kot", "5678");

        Assert.assertEquals(canRegister,res);
    }

    @Test
    public void testLogoutUser(){
        User userTest = new User();
        boolean loggedOut = true;

        boolean res = userTest.logoutUser("kot", "5678");

        Assert.assertEquals(loggedOut, res);
    }

    @Test
    public void testLogoutAllUsers(){
        User userTest = new User();
        boolean allLoggedOut = true;

        boolean res = userTest.logoutALLUsers();

        Assert.assertEquals(allLoggedOut, res);
    }
    
}
