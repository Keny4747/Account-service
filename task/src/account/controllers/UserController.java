package account.controllers;

import account.entity.NewPassword;
import account.entity.PasswordResponse;
import account.entity.User;
import account.security.UserDetailsImpl;
import account.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<User> registerAccount(@Valid @RequestBody User user) {
        return userService.registerAccount(user);
    }

    @PostMapping("/changepass")
    public PasswordResponse  changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody NewPassword newPassword) {

        return  userService.changePassword(newPassword, userDetails);
    }

    @GetMapping("/all")
    public List<User> getAll( ){
        return userService.getAllUser();
    }
}
