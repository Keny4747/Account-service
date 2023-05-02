package account.service;

import account.entity.NewPassword;
import account.entity.PasswordResponse;
import account.entity.User;
import account.exceptions.BreachedPasswordException;
import account.exceptions.UserExistException;
import account.exceptions.error.PasswordsNotDifferentException;
import account.repository.BreachedPasswordsRepository;
import account.repository.UserRepository;
import account.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final BreachedPasswordsRepository breachedPasswordsRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder, BreachedPasswordsRepository breachedPasswordsRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.breachedPasswordsRepository = breachedPasswordsRepository;
    }

    public ResponseEntity<User> registerAccount(User user) {
        if (userRepository.existsUserByEmailIgnoreCase(user.getEmail())) {
            throw new UserExistException();
        }
        if(breachedPasswordsRepository.getAllPasswords().contains(user.getPassword())){
            throw new BreachedPasswordException("The password is in the hacker's database!");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    public PasswordResponse changePassword(NewPassword newPassword, UserDetailsImpl userDetails){
        if(breachedPasswordsRepository.getAllPasswords().contains(newPassword.getNewPassword())){
            throw new BreachedPasswordException("The password is in the hacker's database!");
        }

        User user = userRepository.findByEmail(userDetails.getUsername());

        if(encoder.matches(newPassword.getNewPassword(),user.getPassword())){
            throw new PasswordsNotDifferentException("The passwords must be different!");
        }

        return new PasswordResponse(user.getEmail(),"The password has been updated successfully");
    }

}
