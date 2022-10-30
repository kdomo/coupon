package com.domo.coupon.service;

import com.domo.coupon.domain.User;
import com.domo.coupon.dto.user.UserDto;
import com.domo.coupon.exception.DuplicateIdException;
import com.domo.coupon.repository.UserRepository;
import com.domo.coupon.utils.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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

    private void userIdDuplicateCheck(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if(optionalUser.isPresent()){
            throw new DuplicateIdException("이미 존재하는 사용자 입니다.");
        }
    }
}
