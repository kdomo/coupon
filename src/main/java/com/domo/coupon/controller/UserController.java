package com.domo.coupon.controller;

import com.domo.coupon.dto.user.CreateUser;
import com.domo.coupon.dto.user.UpdateUser;
import com.domo.coupon.dto.user.UserDto;
import com.domo.coupon.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"users"})
@RestController
@RequestMapping("/users")
@Log4j2
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers() {
        return userService.getUser();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody @Valid CreateUser.Request request) {
        userService.createUser(
                request.getUserId(),
                request.getPassword()
        );
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UpdateUser.Response UpdateUser(@PathVariable Long id,
                                          @RequestBody UpdateUser.Request request) {
        UserDto userDto = userService.updateUser(id, request);
        return UpdateUser.Response.builder()
                .id(userDto.getId())
                .userId(userDto.getUserId())
                .password(userDto.getPassword())
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void DeleteUser(
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
    }
}
