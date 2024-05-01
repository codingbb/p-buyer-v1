package com.example.pbuyerv1.product;

import lombok.Data;

public class ProductResponse {

    //상품 목록 리스트 dto
    @Data
    public static class ListDTO {
        private Integer id;
        private String name;
        private Integer price;
        private String imgFileName;


        public ListDTO(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.imgFileName = product.getImgFileName();
        }
    }


    // 1. 상품 상세보기 화면을 위한 DTO
    @Data
    public static class DetailDTO {
        private Integer id;
        private String name;
        private Integer price;
        private Integer qty;
        private String imgFileName;

        public DetailDTO(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.qty = product.getQty();
            this.imgFileName = product.getImgFileName();
        }
    }

    // 3. 상품 목록보기 화면을 위한 DTO
    @Data
    public static class MainDTO {
        private Integer id;
        private String name;
        private Integer price;
        private String imgFileName;

        public MainDTO(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.imgFileName = product.getImgFileName();
        }

    }

    //내구매목록 dto
    @Data
    public static class BuyListDTO {
        private Integer id;
        private String name;
        private Integer price;
        private Integer qty;
        private Integer indexNumb;

        public BuyListDTO(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.qty = product.getQty();
            this.indexNumb = product.getIndexNumb();
        }

    }



}
