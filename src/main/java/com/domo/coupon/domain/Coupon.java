package com.domo.coupon.domain;

import com.domo.coupon.type.DiscountType;
import com.domo.coupon.type.Status;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "TBL_COUPON")
@EntityListeners(AuditingEntityListener.class)
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private String code;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private Long discountValue;

    @Enumerated(EnumType.STRING)
    private Status status;


}
