package fundsControl;

import fundsControl.models.User;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.doReturn;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Test
public class UserSpec {

    User user;

    @BeforeMethod
    public void setUp() {
        user = new User();
    }


    public void whenGivenUserNameThenUserHasName () {
        String name = "Mariusz";
        user.setName(name);
        assertEquals(name, user.getName());
    }

}
