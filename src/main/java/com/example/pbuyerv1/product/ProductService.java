package com.example.pbuyerv1.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepo;

    //상품 목록보기
    public List<ProductResponse.ListDTO> productList() {
        List<Product> productList = productRepo.findAll();

        return productList.stream().map(product ->
                new ProductResponse.ListDTO(product)).collect(Collectors.toList());
    }


    //상품 상세보기
    public ProductResponse.DetailDTO productDetail(Integer id) {
        Product product = productRepo.findById(id);
        return new ProductResponse.DetailDTO(product);
    }


    //상품 메인 목록 보기
    public List<ProductResponse.MainDTO> main() {
        List<Product> productList = productRepo.findAll();

        //엔티티 받아온걸 dto로 변경
        return productList.stream().map(product ->
                new ProductResponse.MainDTO(product)).collect(Collectors.toList());
    }


    //상품명 실시간 중복체크
//    public Product findByName(String name) {
//        Product product = productRepo.findByName(name);
//        return product;
//    }

    //상품명 실시간 중복체크(업데이트)
//    public Product findByNameUpdate(String name, Integer id) {
//        Product product = productRepo.findByNameUpdate(name, id);
//        return product;
//    }


}
