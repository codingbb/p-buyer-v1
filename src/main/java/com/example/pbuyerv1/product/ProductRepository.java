package com.example.pbuyerv1.product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final EntityManager em;

    //상품 상세보기
    public Product findById(Integer id) {
        String q = """
                select * from product_tb where id = ?
                """;
        Query query = em.createNativeQuery(q, Product.class);
        query.setParameter(1, id);
        Product result = (Product) query.getSingleResult();
        return result;
    }


    //상품 목록보기
    public List<Product> findAll() {
        String q = """
                select * from product_tb order by id desc 
                """;
        Query query = em.createNativeQuery(q, Product.class);
        List<Product> productList = query.getResultList();
        return productList;
    }


    //상품명 실시간 중복체크
    public Product findByName(String name) {
        try {
            String q = """
                    select * from product_tb where name = ?
                    """;
            Query query = em.createNativeQuery(q, Product.class);
            query.setParameter(1, name);
            Product product = (Product) query.getSingleResult();
            return product;

        } catch (NoResultException e) {
            return null;
        }
    }

    //상품명 실시간 중복체크
    public Product findByNameUpdate(String name, Integer id) {
        try {
            String q = """
                    select * from product_tb where name = ? and id != ?
                    """;
            Query query = em.createNativeQuery(q, Product.class);
            query.setParameter(1, name);
            query.setParameter(2, id);
            Product product = (Product) query.getSingleResult();
            return product;

        } catch (NoResultException e) {
            return null;
        }
    }



}
