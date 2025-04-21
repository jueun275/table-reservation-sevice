package com.example.tablereservation.endpoint.user;

import com.example.tablereservation.endpoint.user.dto.UserResponse;
import com.example.tablereservation.endpoint.user.dto.UserSignUpRequest;
import com.example.tablereservation.endpoint.user.service.UserService;
import com.example.tablereservation.global.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(@RequestBody UserSignUpRequest request) {
        Long userId =  userService.signUp(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userId);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getAllUsers(@LoginUser Long userId) {
        UserResponse response = userService.getUserInfo(userId);
        return ResponseEntity.ok(response);
    }
}
