package com.domo.coupon.dto.user;


import lombok.*;

import javax.validation.constraints.NotBlank;

public class UpdateUser {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String userId;

        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;

        private String userId;

        private String password;
    }

}
