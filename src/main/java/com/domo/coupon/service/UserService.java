package com.domo.coupon.service;

import com.domo.coupon.domain.User;
import com.domo.coupon.dto.user.UpdateUser;
import com.domo.coupon.dto.user.UserDto;
import com.domo.coupon.exception.DuplicateIdException;
import com.domo.coupon.exception.user.UserMisMatchException;
import com.domo.coupon.exception.user.UserNotFoundException;
import com.domo.coupon.repository.UserRepository;
import com.domo.coupon.utils.SHA256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final SHA256 sha256;

    @Transactional
    public UserDto createUser(String userId, String password) {
        userIdDuplicateCheck(userId);
        return UserDto.fromEntity(userRepository.save(
                User.builder()
                        .userId(userId)
                        .password(sha256.encrypt(password))
                        .build()
        ));
    }

    @Transactional
    public UserDto updateUser(Long id, UpdateUser.Request request) {
        log.info(sha256.encrypt(request.getPassword()));
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setUserId(request.getUserId());
        user.setPassword(sha256.encrypt(request.getPassword()));

        return UserDto.fromEntity(user);
    }

    private void userIdDuplicateCheck(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if(optionalUser.isPresent()){
            throw new DuplicateIdException("이미 존재하는 사용자 입니다.");
        }
    }

}
