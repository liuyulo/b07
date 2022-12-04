package com.example.b07;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import androidx.fragment.app.Fragment;

import com.example.b07.user.Account;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestLoginPresenter {
    @Mock
    Account model;
    @Mock
    Fragment view;
    LoginPresenter p;

    @Before
    public void setup() {
        p = new LoginPresenter(view, model);
    }

    @Test
    public void encryptSHA256() {
        String sha = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";
        assertEquals(p.sha256("password"), sha);
    }

    @Test
    public void encryptNoAlg() {
        assertEquals(p.encrypt("password", "nope"), "");
    }

    @Test
    public void badUsername() {
        assertEquals(p.checkUsername(""), LoginPresenter.USERNAME_EMPTY);
        assertEquals(p.checkUsername(" startswith"), LoginPresenter.USERNAME_STARTS_WHITESPACE);
        assertEquals(p.checkUsername("endswith "), LoginPresenter.USERNAME_ENDS_WHITESPACE);
    }


    @Test
    public void goodUsername() {
        assertNull(p.checkUsername("15 points please"));
    }

    @Test
    public void badPassword() {
        assertEquals(p.checkPassword("n".repeat(LoginPresenter.N - 1)), LoginPresenter.PASSWORD_TOO_SHORT);
    }

    @Test
    public void goodPassword() {
        assertNull(p.checkPassword("n".repeat(LoginPresenter.N)));
    }
}