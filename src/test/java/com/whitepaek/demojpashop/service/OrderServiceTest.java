package com.whitepaek.demojpashop.service;

import com.whitepaek.demojpashop.domain.Address;
import com.whitepaek.demojpashop.domain.Member;
import com.whitepaek.demojpashop.domain.Order;
import com.whitepaek.demojpashop.domain.OrderStatus;
import com.whitepaek.demojpashop.domain.item.Book;
import com.whitepaek.demojpashop.domain.item.Item;
import com.whitepaek.demojpashop.exception.NotEnoughStockException;
import com.whitepaek.demojpashop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook("IntelliJ IDEA 프로젝트에 활용하기", 26000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문 시, 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 26000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄여야 한다.", 8, book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Item item = createBook("IntelliJ IDEA 프로젝트에 활용하기", 26000, 10);

        int orderCount = 11;

        // when
        orderService.order(member.getId(), item.getId(), orderCount);

        // then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Item item = createBook("IntelliJ IDEA 프로젝트에 활용하기", 26000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소 시, 상태는 CANCEL 이다.", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        book.setIsbn("9791165920043");
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("백승주");
        member.setAddress(new Address("서울", "영등포구", "123-123"));
        em.persist(member);
        return member;
    }

}