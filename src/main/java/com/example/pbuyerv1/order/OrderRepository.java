package com.example.pbuyerv1.order;

import com.example.pbuyerv1.product.Product;
import com.example.pbuyerv1.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepository {
    private final EntityManager em;

    //주문 취소 쿼리문 join 쓰고싶어서 씀 (product_tb 수량 변경, order_tb 상태값 변경)
    public void findByIdAndUpdateStatus(OrderRequest.CancelDTO requestDTO) {
        String q = """
                update order_tb o 
                inner join product_tb p on o.product_id = p.id 
                set o.status = ?, p.qty = qty + ? where o.id = ?;
                """;

        Query query = em.createNativeQuery(q);
        query.setParameter(1, requestDTO.getStatus());
        query.setParameter(2, requestDTO.getBuyQty());
        query.setParameter(3, requestDTO.getOrderId());
        query.executeUpdate();

    }


    //TODO: 만약 똑같은 쿼리문을 UserRepository에서 사용한다고 하면 그걸 끌어와서 써야하는지?
    // 유저 조회
    public User findByUserId(Integer id) {
        String q = """
                select * from user_tb where id = ?
                """;
        Query query = em.createNativeQuery(q, User.class);
        query.setParameter(1, id);
        User user = (User) query.getSingleResult();
        return user;
    }

    //상품 조회
    public Product findByProductId(Integer id) {
        String q = """
                select * from product_tb where id = ?
                """;
        Query query = em.createNativeQuery(q, Product.class);
        query.setParameter(1, id);
        Product product = (Product) query.getSingleResult();
        System.out.println("여기서 지금 고정값 밖에 조회못함. 선택한 값으로 안들어오고 있음 : " + product);
        return product;
    }


    //구매하기 !!
    public void save(OrderRequest.SaveDTO requestDTO) {
        String q = """
                insert into order_tb (user_id, product_id, buy_qty, sum, status, payment, created_at) values (?, ?, ?, ?, ?, ?, now());
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, requestDTO.getUserId());
        query.setParameter(2, requestDTO.getProductId());
        query.setParameter(3, requestDTO.getBuyQty());
        query.setParameter(4, requestDTO.getSum());
        query.setParameter(5, requestDTO.getStatus());
        query.setParameter(6, requestDTO.getPayment());

        query.executeUpdate();
    }

    //상품을 구매하면 재고 차감
    public void updateQty(OrderRequest.SaveDTO requestDTO) {
        String q = """
                update product_tb set qty = qty - ? where id = ?
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, requestDTO.getBuyQty());
        query.setParameter(2, requestDTO.getProductId());
        query.executeUpdate();
    }

    //주문내역 폼 (order-detail-form) 조회용
    public OrderResponse.DetailDTO findUserProductByOrderId(Integer orderId) {
        String q = """
                select o.id, o.buy_qty, o.product_id, o.sum, o.payment, o.user_id, o.status, 
                u.name uName, u.address, u.phone, p.name pName, p.price 
                from order_tb o 
                inner join user_tb u on o.user_id = u.id 
                inner join product_tb p on o.product_id = p.id 
                where o.id = ?;
                """;

        Query query = em.createNativeQuery(q);
        query.setParameter(1, orderId);

        Object[] row = (Object[]) query.getSingleResult();
        Integer id = (Integer) row[0];
        Integer buyQty = (Integer) row[1];
        Integer productId = (Integer) row[2];
        Integer sum = (Integer) row[3];
        String payment = (String) row[4];
        Integer userId = (Integer) row[5];
        String status = (String) row[6];
        String uName = (String) row[7];
        String address = (String) row[8];
        String phone = (String) row[9];
        String pName = (String) row[10];
        Integer price = (Integer) row[11];

        OrderResponse.DetailDTO detailDTO = OrderResponse.DetailDTO.builder()
                .id(id)
                .buyQty(buyQty)
                .productId(productId)
                .sum(sum)
                .payment(payment)
                .userId(userId)
                .status(status)
                .uName(uName)
                .address(address)
                .phone(phone)
                .pName(pName)
                .price(price)
                .build();

        return detailDTO;

    }


    // TODO: 돌아가는지 테스트 좀 하고 써라! 까먹지마~!!
    //order-list 조회용
    public List<OrderResponse.ListDTO> findAllOrder() {
        String q = """
                select o.id, o.user_id, o.buy_qty, o.payment, o.sum, o.status, o.created_at, p.name 
                from order_tb o 
                inner join product_tb p on o.product_id = p.id 
                order by o.id desc;
                """;
        Query query = em.createNativeQuery(q);

        //Object 배열 타입으로 받아야함.
        List<Object[]> rows = query.getResultList();
        List<OrderResponse.ListDTO> orderList = new ArrayList<>();

        for (Object[] row : rows) {
            //listDTO
            Integer id = (Integer) row[0];
            Integer userId = (Integer) row[1];
            Integer buyQty = (Integer) row[2];
            String payment = (String) row[3];
            Integer sum = (Integer) row[4];
            String status = (String) row[5];
            LocalDate createdAt = ((Timestamp) row[6]).toLocalDateTime().toLocalDate();
            String name = (String) row[7];

            OrderResponse.ListDTO listDTO = OrderResponse.ListDTO.builder()
                    .id(id)
                    .userId(userId)
                    .buyQty(buyQty)
                    .payment(payment)
                    .sum(sum)
                    .status(status)
                    .createdAt(createdAt)
                    .name(name)
                    .build();

            orderList.add(listDTO);
        }

//        System.out.println("db값 확인용..." + orderList);

        return orderList;

    }
}
