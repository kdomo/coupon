package com.domo.coupon.controller;

import com.domo.coupon.service.CouponService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"coupons"})
@RestController
@RequestMapping("/coupons")
@Log4j2
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;
}
