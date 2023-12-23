package com.gregori.order_detail.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gregori.common.CustomMybatisTest;
import com.gregori.product.domain.Product;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.order.domain.Order;
import com.gregori.order.mapper.OrderMapper;
import com.gregori.order_detail.domain.OrderDetail;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.mapper.SellerMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@CustomMybatisTest
class OrderDetailMapperTest {

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private SellerMapper sellerMapper;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderDetailMapper orderDetailMapper;

	Member member;
	Seller seller;
	Order order;
	List<Product> products = new ArrayList<>();
	List<Long> orderDetailIds = new ArrayList<>();

	@BeforeEach
	void beforeEach() {
		member = Member.builder()
			.email("a@a.a")
			.name("일호")
			.password("aa11111!")
			.build();
		memberMapper.insert(member);

		seller = Seller.builder()
			.memberId(member.getId())
			.businessNumber("111-11-11111")
			.businessName("일호 상점")
			.build();
		sellerMapper.insert(seller);

		Product product1 = Product.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();
		Product product2 = Product.builder()
			.sellerId(seller.getId())
			.name("아이템2")
			.price(200L)
			.inventory(2L)
			.build();

		productMapper.insert(product1);
		productMapper.insert(product2);
		products.add(product1);
		products.add(product2);

		order = Order.builder()
			.memberId(member.getId())
			.paymentMethod("카드")
			.paymentAmount(1000L)
			.deliveryCost(2500L)
			.build();
		orderMapper.insert(order);
	}

	@AfterEach
	void afterEach() {
		if (!orderDetailIds.isEmpty()) {
			orderDetailMapper.deleteByIds(orderDetailIds);
			orderDetailIds.clear();
		}
		if (order != null) {
			orderMapper.deleteByIds(List.of(order.getId()));
			order = null;
		}
		if (!products.isEmpty()) {
			productMapper.deleteByIds(products.stream().map(Product::getId).toList());
			products.clear();
		}
		if (seller != null) {
			sellerMapper.deleteByIds(List.of(seller.getId()));
			seller = null;
		}
		if(member != null) {
			memberMapper.deleteByIds(List.of(member.getId()));
			member = null;
		}
	}

	@Test
	@DisplayName("OrderDetails 테이블에 새로운 주문 상품을 삽입한다.")
	void should_insert_when_validOrderDetail() {

		// given
		OrderDetail orderDetail = OrderDetail.builder()
			.orderId(order.getId())
			.productId(products.get(0).getId())
			.productName(products.get(0).getName())
			.productPrice(products.get(0).getPrice())
			.productCount(2L)
			.build();

		// when
		orderDetailMapper.insert(orderDetail);
		orderDetailIds.add(orderDetail.getId());
		List<OrderDetail> result = orderDetailMapper.findByIds(List.of(orderDetail.getId()));

		// then
		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getId(), orderDetail.getId());
		assertEquals(result.get(0).getProductCount(), orderDetail.getProductCount());
	}

	@Test
	@DisplayName("DB에서 id가 일치하는 주문을 전부 삭제한다.")
	void should_delete_when_IdsMatch() {

		// given
		OrderDetail orderDetail = OrderDetail.builder()
			.orderId(order.getId())
			.productId(products.get(0).getId())
			.productName(products.get(0).getName())
			.productPrice(products.get(0).getPrice())
			.productCount(2L)
			.build();

		orderDetailMapper.insert(orderDetail);
		orderDetailIds.add(orderDetail.getId());

		// when
		orderDetailMapper.deleteByIds(List.of(orderDetail.getId()));
		List<OrderDetail> result = orderDetailMapper.findByIds(List.of(orderDetail.getId()));

		// then
		assertEquals(result.size(), 0);
	}

	@Test
	@DisplayName("DB에서 orderId가 일치하는 주문 상품을 조회한다.")
	void should_find_when_idMatch() {

		// given
		OrderDetail orderDetail1 = OrderDetail.builder()
			.orderId(order.getId())
			.productId(products.get(0).getId())
			.productName(products.get(0).getName())
			.productPrice(products.get(0).getPrice())
			.productCount(2L)
			.build();
		OrderDetail orderDetail2 = OrderDetail.builder()
			.orderId(order.getId())
			.productId(products.get(1).getId())
			.productName(products.get(1).getName())
			.productPrice(products.get(1).getPrice())
			.productCount(2L)
			.build();

		orderDetailMapper.insert(orderDetail1);
		orderDetailMapper.insert(orderDetail2);
		orderDetailIds.add(orderDetail1.getId());
		orderDetailIds.add(orderDetail2.getId());

		// when
		List<OrderDetail> result = orderDetailMapper.findByOrderId(order.getId());

		// then
		assertEquals(result.get(0).getId(), orderDetail1.getId());
		assertEquals(result.get(0).getProductCount(), orderDetail1.getProductCount());
		assertEquals(result.get(1).getId(), orderDetail2.getId());
		assertEquals(result.get(1).getProductCount(), orderDetail2.getProductCount());
	}

	@Test
	@DisplayName("DB에서 id가 일치하는 주문을 전부 조회한다.")
	void should_find_when_idsMatch() {

		// given
		OrderDetail orderDetail1 = OrderDetail.builder()
			.orderId(order.getId())
			.productId(products.get(0).getId())
			.productName(products.get(0).getName())
			.productPrice(products.get(0).getPrice())
			.productCount(2L)
			.build();
		OrderDetail orderDetail2 = OrderDetail.builder()
			.orderId(order.getId())
			.productId(products.get(1).getId())
			.productName(products.get(1).getName())
			.productPrice(products.get(1).getPrice())
			.productCount(2L)
			.build();

		orderDetailMapper.insert(orderDetail1);
		orderDetailMapper.insert(orderDetail2);
		orderDetailIds.add(orderDetail1.getId());
		orderDetailIds.add(orderDetail2.getId());

		// when
		List<OrderDetail> result = orderDetailMapper.findByIds(List.of(orderDetail1.getId(), orderDetail2.getId()));

		// then
		assertEquals(result.get(0).getId(), orderDetail1.getId());
		assertEquals(result.get(0).getProductCount(), orderDetail1.getProductCount());
		assertEquals(result.get(1).getId(), orderDetail2.getId());
		assertEquals(result.get(1).getProductCount(), orderDetail2.getProductCount());
	}
}
