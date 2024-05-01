package com.example.pbuyerv1.order;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Table(name = "order_tb")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private Integer productId;
    private String payment;

    //구매 수량
    private Integer buyQty;

    //합계
    private Integer sum;

    //구매 상태
    private String status;  //주문완료, 취소완료

    @Transient
    private Integer indexNum;

    @CreationTimestamp
    private LocalDateTime createdAt;



}
