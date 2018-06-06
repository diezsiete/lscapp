package com.diezsiete.lscapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.diezsiete.lscapp.db.entity.AchievementEntity;
import com.diezsiete.lscapp.db.entity.UserEntity;
import com.diezsiete.lscapp.repository.AchievementRepository;
import com.diezsiete.lscapp.repository.UserRepository;
import com.diezsiete.lscapp.util.AbsentLiveData;
import com.diezsiete.lscapp.vo.Resource;

import java.util.List;

import javax.inject.Inject;

public class UserViewModel extends ViewModel{

    private UserValidation userVali = new UserValidation();

    private final LiveData<UserEntity> user;
    private final MediatorLiveData<UserEntity> userMediator = new MediatorLiveData<>();

    private final MutableLiveData<UserValidation> userValidation = new MutableLiveData<>();

    private final UserRepository userRepository;

    private MutableLiveData<UserValidation> triggerAuthentication = new MutableLiveData<>();

    private final LiveData<Resource<UserEntity>> authenticatedUser;

    private MutableLiveData<List<AchievementEntity>> userAchievements = new MutableLiveData<>();
    private final LiveData<Resource<List<AchievementEntity>>> achievements;
    private MutableLiveData<Boolean> triggerAchievements = new MutableLiveData<>();

    @Inject
    UserViewModel(UserRepository userRepository, AchievementRepository achievementRepository) {
        this.userRepository = userRepository;

        user = userRepository.load();

        userMediator.addSource(user, userEntity -> {
            if(userEntity != null) {
                userValidation.setValue(userVali.setName(userEntity.name).setEmail(userEntity.email));
            }
            userMediator.setValue(userEntity);
        });

        authenticatedUser = Transformations.switchMap(triggerAuthentication, userValidation -> {
            if (userValidation == null) {
                return AbsentLiveData.create();
            }else {
                if(userValidation.passwordConfirm == null)
                    return userRepository.login(userValidation.email, userValidation.password);
                else
                    return userRepository.register(userValidation.email, userValidation.password, userValidation.passwordConfirm);
            }
        });

        achievements = Transformations.switchMap(triggerAchievements, trigger -> {
            if(trigger == null){
                return AbsentLiveData.create();
            }else{
                return achievementRepository.loadAll();
            }
        });
    }

    public LiveData<UserEntity> getUser() {
        //return user;
        return userMediator;
    }

    public LiveData<UserValidation> getUserValidation() {
        return userValidation;
    }

    public LiveData<Resource<List<AchievementEntity>>> getAchievements() {
        triggerAchievements.setValue(true);
        return achievements;
    }

    public boolean register(String email, String password, String passwordConfirm){
        boolean ok = false;
        if(setEmail(email) && setPassword(password) && setPasswordConfirm(passwordConfirm)){
            triggerAuthentication.setValue(userVali);
            ok = true;
        }
        return ok;
    }

    public boolean login(String email, String password) {
        boolean ok = false;
        if(setEmail(email) && setPassword(password)){
            triggerAuthentication.setValue(userVali);
            ok = true;
        }
        return ok;
    }

    public LiveData<Resource<UserEntity>> getAuthenticatedUser(){
        return authenticatedUser;
    }

    public void logout(){
        userRepository.delete();
    }

    public LiveData<Resource<UserEntity>> editUser(String name, String email, String password, String passwordConfirm) {
        boolean update = false;
        UserEntity userEntity = user.getValue();
        if(setName(name) && setEmail(email)){
            if(userEntity != null) {
                userEntity.name = name;
                userEntity.email = email;
                if (!TextUtils.isEmpty(password) || !TextUtils.isEmpty(passwordConfirm)) {
                    if (setPassword(password) && setPasswordConfirm(passwordConfirm)) {
                        userEntity.password = password;
                        userEntity.passwordConfirm = passwordConfirm;
                        update = true;
                    }
                } else
                    update = true;
            }
        }
        if(update) {
            if(TextUtils.isEmpty(password))
                return userRepository.updateProfileBasicInfo(userEntity.profileId, name, email);
            else
                return userRepository.updateProfile(userEntity.profileId, name, email, password, passwordConfirm);
        }else
            return AbsentLiveData.create();
    }


    public boolean setName(String name) {
        boolean ok = true;
        userVali.setName(name);
        String validation = "";
        if(TextUtils.isEmpty(name)){
            validation = "Este campo es obligatorio";
            ok = false;
        }
        if(!validation.isEmpty()){
            userValidation.setValue(userVali.setNameValidation(validation));
        }else
            userVali.setName(name);
        return ok;
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

    public static class UserValidation {
        public String name;
        public String email;
        public String password;
        public String passwordConfirm;
        public String nameValidation;
        public String emailValidation;
        public String passwordValidation;
        public String passwordConfirmValidation;

        public UserValidation setNameValidation(String nameValidation) {
            this.nameValidation = nameValidation;
            this.emailValidation = null;
            this.passwordValidation = null;
            this.passwordConfirmValidation = null;
            return this;
        }
        public UserValidation setEmailValidation(String emailValidation) {
            this.emailValidation = emailValidation;
            this.nameValidation = null;
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

        public UserValidation setName(String name) {
            this.name = name;
            this.nameValidation = null;
            return this;
        }
        public UserValidation setEmail(String email) {
            this.email = email;
            this.emailValidation = null;
            return this;
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
