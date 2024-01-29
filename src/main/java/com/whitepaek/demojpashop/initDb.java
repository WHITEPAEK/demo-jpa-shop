package com.whitepaek.demojpashop;

import com.whitepaek.demojpashop.domain.*;
import com.whitepaek.demojpashop.domain.item.Book;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        public void dbInit1() {
            Member member = createMember("백승주", "서울", "디지털로", "12345");
            em.persist(member);

            Book book1 = createBook("IntelliJ IDEA 프로젝트에 활용하기", 23400, 100);
            em.persist(book1);

            Book book2 = createBook("자바 ORM 표준 JPA 프로그래밍", 43000, 300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 38700, 1);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("이수열", "수원", "경인대로", "54321");
            em.persist(member);

            Book book1 = createBook("Real MySQL 8.0 1권", 30000, 200);
            em.persist(book1);

            Book book2 = createBook("Real MySQL 8.0 2권", 32000, 200);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 27000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 28800, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private static Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private static Book createBook(String name, int price, int stockQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        private static Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

    }
}
