package com.domo.coupon.service;

import com.domo.coupon.domain.User;
import com.domo.coupon.dto.user.UpdateUser;
import com.domo.coupon.dto.user.UserDto;
import com.domo.coupon.exception.DuplicateIdException;
import com.domo.coupon.exception.user.UserNotFoundException;
import com.domo.coupon.repository.UserRepository;
import com.domo.coupon.utils.SHA256;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private SHA256 sha256;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 - 성공")
    void successCreateUser() {
        //given
        User user = User.builder()
                .id(14L)
                .userId("domo")
                .password("1234")
                .build();
        given(userRepository.save(any()))
                .willReturn(user);

        //when
        UserDto userDto = userService.createUser("222", "111");

        //then
        assertEquals(14L, userDto.getId());
        assertEquals("domo", userDto.getUserId());
        assertEquals("1234", userDto.getPassword());
    }

    @Test
    @DisplayName("회원가입 - 실패")
    void faileCreateUser() {
        //given
        given(userRepository.findByUserId(anyString()))
                .willReturn(Optional.of(User.builder().build()));
        //when
        DuplicateIdException duplicateIdException =
                assertThrows(DuplicateIdException.class,
                        () -> userService.createUser("domo22", "123123"));

        //then
        assertEquals(
                duplicateIdException.getMessage(),
                "이미 존재하는 사용자 입니다."
        );
    }

    @Test
    @DisplayName("회원 수정 - 성공")
    void successUpdateUser() {
        //given
        User user = User.builder()
                .id(14L)
                .userId("domo")
                .password("1234")
                .build();
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(sha256.encrypt(any()))
                .willReturn("12345");

        UpdateUser.Request request = UpdateUser.Request.builder()
                .userId("domo1")
                .password("12345")
                .build();

        //when
        UserDto userDto = userService.updateUser(
                14L, request);
        //then
        assertEquals("domo1", userDto.getUserId());
        assertEquals("12345", userDto.getPassword());
    }

    @Test
    @DisplayName("회원 수정 - 실패")
    void failUpdateUser() {
        //given
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        UpdateUser.Request request = UpdateUser.Request.builder()
                .userId("domo1")
                .password("12345")
                .build();
        //when
        UserNotFoundException userNotFoundException =
                assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(1L, request));

        //then
        assertEquals(userNotFoundException.getMessage(), "user not found : 1");
    }

    @Test
    @DisplayName("회원 삭제 - 성공")
    void successDeleteUser() {
        //given
        User user = User.builder()
                .id(14L)
                .userId("domo")
                .password("1234")
                .build();
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        //when
        userService.deleteUser(14L);

        //then
        verify(userRepository,times(1)).delete(captor.capture());
    }

    @Test
    @DisplayName("회원 삭제 - 실패")
    void failDeleteUser() {
        //given
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        UserNotFoundException userNotFoundException =
                assertThrows(UserNotFoundException.class,
                        () -> userService.deleteUser(1L));

        //then
        assertEquals(userNotFoundException.getMessage(), "user not found : 1");
    }

    @Test
    void successGetUsers() {
        //given
        List<User> users = new ArrayList<>();
        users.add(User.builder().id(1L).userId("domo").password("1234").build());
        users.add(User.builder().id(2L).userId("lupi").password("4321").build());
        given(userRepository.findAll())
                .willReturn(users);
        //when
        List<UserDto> userDtos = userService.getUser();

        //then
        assertEquals(userDtos.size(),2);
        assertEquals(userDtos.get(0).getUserId(),"domo");
        assertEquals(userDtos.get(0).getPassword(),"1234");
        assertEquals(userDtos.get(1).getUserId(),"lupi");
        assertEquals(userDtos.get(1).getPassword(),"4321");

    }
}