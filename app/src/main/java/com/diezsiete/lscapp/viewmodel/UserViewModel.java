package com.diezsiete.lscapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.diezsiete.lscapp.db.entity.User;
import com.diezsiete.lscapp.repository.UserRepository;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.vo.Authentication;
import com.diezsiete.lscapp.vo.Resource;

import javax.inject.Inject;

public class UserViewModel extends ViewModel{

    private UserValidation userVali = new UserValidation();

    private final LiveData<User> user;

    private final MutableLiveData<UserValidation> userValidation = new MutableLiveData<>();

    private final UserRepository userRepository;

    private MutableLiveData<UserValidation> triggerAuthentication = new MutableLiveData<>();

    private final LiveData<Resource<User>> authenticatedUser;

    @Inject
    UserViewModel(UserRepository userRepository) {
        user = userRepository.load();
        this.userRepository = userRepository;


        authenticatedUser = Transformations.switchMap(triggerAuthentication, userValidation -> {
            if (userValidation == null) {
                return AbsentLiveData.create();
            }else {
                //if(!userValidation.passwordConfirm.isEmpty())
                return userRepository.register(userValidation.email, userValidation.password, userValidation.passwordConfirm);
                //else
                    //return userRepository.login(userValidation.email, userValidation.password);
            }
        });

    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<UserValidation> getUserValidation() {
        return userValidation;
    }

    public boolean setEmail(String email) {
        boolean ok = true;
        userVali.setEmail(email);
        String validation = "";
        if (TextUtils.isEmpty(email)) {
            validation = "Este campo es obligatorio";
            ok = false;
        }
        else if (!email.contains("@")) {
            validation = "Este correo es incorrecto";
            ok = false;
        }
        if(!validation.isEmpty()){
            userValidation.setValue(userVali.setEmailValidation(validation));
        }else
            userVali.setEmail(email);
        return ok;
    }

    public boolean setPassword(String password) {
        userVali.setPassword(password);
        boolean ok = !TextUtils.isEmpty(password);
        if(!ok){
            userValidation.setValue(userVali.setPasswordValidation("Este campo es obligatorio"));
        }else
            userVali.setPassword(password);
        return ok;
    }

    public boolean setPasswordConfirm(String passwordConfirm) {
        userVali.setPasswordConfirm(passwordConfirm);
        boolean ok = !TextUtils.isEmpty(passwordConfirm);
        if(!ok){
            userValidation.setValue(userVali.setPasswordConfirmValidation("Este campo es obligatorio"));
        }else if(!passwordConfirm.equals(userVali.password)) {
            userVali.setPasswordValidation("Contraseñas no concuerdan");
            userVali.password = null;
            userVali.passwordConfirm = null;
            userValidation.setValue(userVali);
            ok = false;
        }else
            userVali.setPasswordConfirm(passwordConfirm);
        return ok;
    }

    public boolean register(String email, String password, String passwordConfirm){
        boolean ok = false;
        if(setEmail(email) && setPassword(password) && setPasswordConfirm(passwordConfirm)){
            triggerAuthentication.setValue(userVali);
            ok = true;
        }
        return ok;
    }

    public LiveData<Resource<User>> getAuthenticatedUser(){
        return authenticatedUser;
    }

    public void logout(){
        userRepository.delete();
    }


    public static class UserValidation {
        public String email;
        public String password;
        public String passwordConfirm;
        public String emailValidation;
        public String passwordValidation;
        public String passwordConfirmValidation;

        public UserValidation setEmailValidation(String emailValidation) {
            this.emailValidation = emailValidation;
            this.passwordValidation = null;
            this.passwordConfirmValidation = null;
            return this;
        }

        public UserValidation setPasswordValidation(String passwordValidation) {
            this.passwordValidation = passwordValidation;
            this.passwordConfirmValidation = null;
            this.emailValidation = null;
            return this;
        }

        public UserValidation setPasswordConfirmValidation(String passwordConfirmValidation) {
            this.passwordConfirmValidation = passwordConfirmValidation;
            this.emailValidation = null;
            this.passwordConfirm = null;
            return this;
        }

        public String getEmailValidation() {
            return emailValidation;
        }

        public String getPasswordValidation() {
            return passwordValidation;
        }

        public String getPasswordConfirmValidation() {
            return passwordConfirmValidation;
        }

        public void setEmail(String email) {
            this.email = email;
            this.emailValidation = null;
        }

        public void setPassword(String password) {
            this.password = password;
            this.passwordValidation = null;
        }

        public void setPasswordConfirm(String passwordConfirm) {
            this.passwordConfirm = passwordConfirm;
            this.passwordConfirmValidation = null;
        }
    }
}
