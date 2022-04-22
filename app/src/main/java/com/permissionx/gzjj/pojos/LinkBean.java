package com.permissionx.gzjj.pojos;


import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LinkBean {
    public List<ItemL> itemLS;
    public List<Item> itemS;

    public static class ItemL {
        private String title;
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class Item {
        private int discount;
        private String title;
        private String name;
        private String price;
        private String imageUrl;
        private int num = 0;
        private String content;
        private String  material;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setMaterial(String material) {
            this.material = material;
        }

        public String getMaterial() {
            return material;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        public void addNum(){
            this.num++;
        }

        public void subNum(){
            if(num > 0){
                num--;
            }else {
                num = 0;
            }
        }
    }
}
