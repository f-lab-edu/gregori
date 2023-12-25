package com.gregori.order_detail.mapper;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gregori.category.domain.Category;
import com.gregori.category.mapper.CategoryMapper;
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

import static org.assertj.core.api.Assertions.assertThat;

@CustomMybatisTest
class OrderDetailMapperTest {

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private SellerMapper sellerMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderDetailMapper orderDetailMapper;

	Member member;
	Seller seller;
	Category category;
	Order order;
	List<Product> products = new CopyOnWriteArrayList<>();
	List<Long> orderDetailIds = new CopyOnWriteArrayList<>();

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

		category = new Category("name");
		categoryMapper.insert(category);

		Product product1 = Product.builder()
			.sellerId(seller.getId())
			.categoryId(category.getId())
			.name("name")
			.price(1L)
			.inventory(1L)
			.build();
		Product product2 = Product.builder()
			.sellerId(seller.getId())
			.categoryId(category.getId())
			.name("name")
			.price(2L)
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
		if (category != null) {
			categoryMapper.deleteById(category.getId());
			category = null;
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
	@DisplayName("새로운 주문 상세를 추가한다.")
	void should_insert() {

		// given
		OrderDetail orderDetail = OrderDetail.builder()
			.orderId(order.getId())
			.productId(products.get(0).getId())
			.productName(products.get(0).getName())
			.productPrice(products.get(0).getPrice())
			.productCount(1L)
			.build();

		// when
		orderDetailMapper.insert(orderDetail);
		orderDetailIds.add(orderDetail.getId());
		List<OrderDetail> result = orderDetailMapper.findByIds(List.of(orderDetail.getId()));

		// then
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get(0).getId()).isEqualTo(orderDetail.getId());
		assertThat(result.get(0).getProductCount()).isEqualTo(orderDetail.getProductCount());
	}

	@Test
	@DisplayName("id 목록으로 주문 상세를 삭제한다.")
	void should_deleteByIds() {

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
		assertThat(result.size()).isEqualTo(0);
	}

	@Test
	@DisplayName("orderId로 주문 상세를 조회한다.")
	void should_findByOrderId() {

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
		assertThat(result.get(0).getId()).isEqualTo(orderDetail1.getId());
		assertThat(result.get(0).getProductCount()).isEqualTo(orderDetail1.getProductCount());
		assertThat(result.get(1).getId()).isEqualTo(orderDetail2.getId());
		assertThat(result.get(1).getProductCount()).isEqualTo(orderDetail2.getProductCount());
	}

	@Test
	@DisplayName("productId로 주문 상세를 조회한다.")
	void should_findByProductId() {

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
		assertThat(result.get(0).getId()).isEqualTo(orderDetail1.getId());
		assertThat(result.get(0).getProductCount()).isEqualTo(orderDetail1.getProductCount());
		assertThat(result.get(1).getId()).isEqualTo(orderDetail2.getId());
		assertThat(result.get(1).getProductCount()).isEqualTo(orderDetail2.getProductCount());
	}
}
