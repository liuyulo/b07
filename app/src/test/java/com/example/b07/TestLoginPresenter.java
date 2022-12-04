package com.example.b07;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestLoginPresenter {
    @Test
    public void encryptSHA256() {
        assertEquals(LoginPresenter.sha256("password"), "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
    }

    @Test
    public void encryptNoAlg() {
        assertEquals(LoginPresenter.encrypt("password", "nope"), "");
    }

    @Test
    public void badUsername() {
        assertEquals(LoginPresenter.checkUsername(""), LoginPresenter.USERNAME_EMPTY);
        assertEquals(LoginPresenter.checkUsername(" startswith"), LoginPresenter.USERNAME_STARTS_WHITESPACE);
        assertEquals(LoginPresenter.checkPassword("endswith "), LoginPresenter.USERNAME_ENDS_WHITESPACE);
    }


    @Test
    public void goodUsername() {
        assertEquals(LoginPresenter.checkUsername("15 points please"), "");
    }

    @Test
    public void badPassword() {
        assertEquals(LoginPresenter.checkPassword("n".repeat(LoginPresenter.N - 1)), LoginPresenter.PASSWORD_TOO_SHORT);
    }

    @Test
    public void goodPassword() {
        assertEquals(LoginPresenter.checkPassword("owo"), "");
    }

    @Test
    public void test(){
        MainActivity main = Mockito.mock(MainActivity.class);
        return;
    }
}