# final project - 상품 구매 사이트 1단계

<hr>

## [ 1단계 기능 구현 ]
* 판매자 서버에서 상품 리스트를 받아온 후, 원하는 상품을 구매한다.
* 구매한 상품을 구매 취소할 수 있다.

<hr>

## 1. 구매하기 
* 상품을 구매할 때, 클라이언트가 구매한만큼 재고가 감소되어야 한다.
* save와 update를 하나의 @Transactional 에 묶는다.
* save가 실패하면 수량 update가 일어나지 않아야하고, save가 성공하면 수량 update도 발생해야 하기 때문

### 1-1. OrderRepository 
```java
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

```

### 1-2. OrderService
```java
    @Transactional
    public void saveOrder(OrderRequest.SaveDTO requestDTO) {
        orderRepo.save(requestDTO);
        orderRepo.updateQty(requestDTO);

    }
```

### 1-3. OrderController
```java
    @PostMapping("/order-save")
    public String save(OrderRequest.SaveDTO requestDTO) {
        orderService.saveOrder(requestDTO);
        return "redirect:/order-list";

    }
```

<hr>

## 2. 구매 취소하기 
```

구매 취소가 되면, 기존 상품 재고가 + 되어야 하고, 
구매 목록 보기에서는 삭제하지 않고, 수정되어야 함.
* 삭제는 함부로 하지 않는다.

예를 들어 구매 테이블 Order에 status 같은 필드를 둔다.
status는 구매, 취소 두가지 상태를 가진다.
그리고 상품 구매가 취소되면, 구매목록에서 구매를 취소로 변경만 한다.

사용자가 구매 목록을 조회할 때, 취소된 주문도 보여주되, 
status 필드를 통해 구매가 취소되었음을 명시적으로 표시한다.

[ 왜 delete가 아니라 상태만 변경할까? ]
1. 구매 이력을 유지하여 사용자의 구매 활동을 추적 가능
2. 데이터를 삭제하는 것보다 상태를 변경하는 것이 데이터의 무결성을 유지하는 데 도움

```

### 2-1. OrderRepository
```java
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
```




