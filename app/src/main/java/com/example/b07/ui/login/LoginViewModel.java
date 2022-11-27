package com.example.b07.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Intent;
import android.util.Log;
import android.util.Patterns;

import com.example.b07.data.LoginRepository;
import com.example.b07.data.Result;
import com.example.b07.data.model.LoggedInUser;
import com.example.b07.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> state = new MutableLiveData<>();
    private MutableLiveData<LoginResult> result = new MutableLiveData<>();
    private LoginRepository repo;

    LoginViewModel(LoginRepository loginRepository) {
        this.repo = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return state;
    }

    LiveData<LoginResult> getResult() {
        return result;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Log.d("Login", username + " " + password);
//        Result<LoggedInUser> result = repo.login(username, password);
//
//        if (result instanceof Result.Success) {
//            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
//            this.result.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
//        } else {
//            this.result.setValue(new LoginResult(R.string.login_failed));
//        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            state.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            state.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            state.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}