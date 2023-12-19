package com.gregori.order.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.gregori.common.exception.NotFoundException;
import com.gregori.product.domain.Product;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.order.domain.Order;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;
import com.gregori.order.mapper.OrderMapper;
import com.gregori.order_detail.domain.OrderDetail;
import com.gregori.order_detail.dto.OrderDetailRequestDto;
import com.gregori.order_detail.mapper.OrderDetailMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.mapper.SellerMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceImplTest {

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

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private OrderService orderService;

	Member member;
	Seller seller;
	List<Product> products = new ArrayList<>();
	List<Long> orderIds = new ArrayList<>();
	List<Long> orderDetailIds = new ArrayList<>();

	@BeforeEach
	void beforeEach() {
		member = Member.builder()
			.email("a@a.a")
			.name("일호")
			.password(passwordEncoder.encode("aa11111!"))
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
	}

	@AfterEach
	void afterEach() {
		if (!orderDetailIds.isEmpty()) {
			orderDetailMapper.deleteByIds(orderDetailIds);
			orderDetailIds.clear();
		}
		if (!orderIds.isEmpty()) {
			orderMapper.deleteByIds(orderIds);
			orderIds.clear();
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
	@DisplayName("새로운 주문과 주문 상품을 DB에 저장하고 주문 정보를 반환한다.")
	void createOrder() {

		// given
		List<OrderDetailRequestDto> orderDetailRequest = List.of(new OrderDetailRequestDto(1L, products.get(0).getId()));
		OrderRequestDto orderRequestDto = new OrderRequestDto(member.getId(), "카드", 1000L, 12500L, orderDetailRequest);

		// when
		OrderResponseDto result = orderService.saveOrder(orderRequestDto);
		Order order = orderMapper.findById(result.getId()).orElseThrow(NotFoundException::new);
		List<OrderDetail> orderDetails = orderDetailMapper.findByOrderId(order.getId());

		orderIds.add(order.getId());
		orderDetailIds.add(orderDetails.get(0).getId());

		// then
		assertEquals(result.getId(), order.getId());
		assertEquals(result.getMemberId(), order.getMemberId());
		assertEquals(result.getPaymentMethod(), order.getPaymentMethod());
		assertEquals(result.getOrderDetails().size(), 1);
		assertEquals(orderDetails.size(), 1);
		assertEquals(result.getOrderDetails().get(0).getId(), orderDetails.get(0).getId());
		assertEquals(result.getOrderDetails().get(0).getOrderId(), orderDetails.get(0).getOrderId());
	}

	@Test
	@DisplayName("orderId로 DB에 저장된 주문과 주문 상품을 조회해서 반환한다.")
	void findOrderById() {

		// given
		Order order = Order.builder()
			.memberId(member.getId())
			.paymentMethod("카드")
			.paymentAmount(1000L)
			.deliveryCost(2500L)
			.build();
		orderMapper.insert(order);
		orderIds.add(order.getId());

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
		OrderResponseDto result = orderService.getOrder(order.getId());

		// then
		assertEquals(result.getId(), order.getId());
		assertEquals(result.getMemberId(), order.getMemberId());
		assertEquals(result.getPaymentMethod(), order.getPaymentMethod());
		assertEquals(result.getStatus(), order.getStatus());
		assertEquals(result.getOrderDetails().size(), 1);
		assertEquals(result.getOrderDetails().get(0).getId(), orderDetail.getId());
		assertEquals(result.getOrderDetails().get(0).getOrderId(), orderDetail.getOrderId());
	}
}
