<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="cartData" required="true"
	type="de.hybris.platform.commercefacades.order.data.CartData"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/responsive/storepickup"%>

<div class="clearfix cart-heading hidden-xs hidden-sm">
	<div class="cart-item">
		<span class="heading">item</span>
	</div>
	<div class="cart-unit">
		<span class="heading">unit price</span>
	</div>
	<div class="cart-qty">
		<span class="heading">quantity</span>
	</div>
	<div class="cart-total">
		<span class="heading">total</span>
	</div>
</div>

<ul class="cart-list">
	<c:set var="oddOrEven" value="odd" />
	<c:forEach items="${cartData.entries}" var="entry">
		<c:url value="${entry.product.url}" var="productUrl" />
		<li class="product-item ${oddOrEven}"><c:if
				test="${entry.updateable}">
				<ycommerce:testId code="cart_product_removeProduct">
					<button class="remove-item remove-entry-button hidden-md hidden-lg"
						id="removeEntry_${entry.entryNumber}">
						<i class="gl gl-remove"></i>
					</button>
					<%--
		             <a href="#" class="btn  remove-item remove-entry-button" id="removeEntry_${entry.entryNumber}">
		                Remove
		            </a>
		             --%>
				</ycommerce:testId>
			</c:if>

			<div class="clearfix">
				<div class="cart-item">
					<div class="thumb">
						<a href="${productUrl}"><product:productPrimaryImage
								product="${entry.product}" format="thumbnail" /></a>
					</div>

					<div class="details">
						<ycommerce:testId code="cart_product_name">
							<a href="${productUrl}"><div class="name">${entry.product.name}</div></a>
						</ycommerce:testId>
						<div class="item-sku">${entry.product.code}</div>

						<c:if
							test="${ycommerce:doesPotentialPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
							<c:forEach items="${cartData.potentialProductPromotions}"
								var="promotion">
								<c:set var="displayed" value="false" />
								<c:forEach items="${promotion.consumedEntries}"
									var="consumedEntry">
									<c:if
										test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber && not empty promotion.description}">
										<c:set var="displayed" value="true" />

										<div class="promo">
											<ycommerce:testId code="cart_potentialPromotion_label">
		                                             ${promotion.description}
		                                         </ycommerce:testId>
										</div>
									</c:if>
								</c:forEach>
							</c:forEach>
						</c:if>
						<c:if
							test="${ycommerce:doesAppliedPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
							<c:forEach items="${cartData.appliedProductPromotions}"
								var="promotion">
								<c:set var="displayed" value="false" />
								<c:forEach items="${promotion.consumedEntries}"
									var="consumedEntry">
									<c:if
										test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
										<c:set var="displayed" value="true" />
										<div class="promo">
											<ycommerce:testId code="cart_appliedPromotion_label">
	                                            ${promotion.description}
	                                        </ycommerce:testId>
										</div>
									</c:if>
								</c:forEach>
							</c:forEach>
						</c:if>

						<c:set var="entryStock"
							value="${entry.product.stock.stockLevelStatus.code}" />

						<c:forEach items="${entry.product.baseOptions}" var="option">
							<c:if
								test="${not empty option.selected and option.selected.url eq entry.product.url}">
								<c:forEach items="${option.selected.variantOptionQualifiers}"
									var="selectedOption">
									<div>
										<strong>${selectedOption.name}:</strong> <span>${selectedOption.value}</span>
									</div>
									<c:set var="entryStock"
										value="${option.selected.stock.stockLevelStatus.code}" />
								</c:forEach>
							</c:if>
						</c:forEach>
					</div>
				</div>
				<div class="cart-qty qty-wrp hidden-lg hidden-md">
					<div class="qty-selector input-group js-qty-selector">
						<span class="input-group-btn">
							<button class="js-qty-selector-minus" type="button">-</button>
						</span> <input type="text" maxlength="3"
							class="form-control js-qty-selector-input" size="1" value="1"
							data-max="11" data-min="1" name="pdpAddtoCartInput"
							id="pdpAddtoCartInput"> <span
							class="input-group-btn plus-group">
							<button class="js-qty-selector-plus" type="button">+</button>
						</span>
					</div>
				</div>
				<div class="cart-unit">
					<div class="price">
						<format:price priceData="${entry.basePrice}"
							displayFreeForZero="true" />
					</div>
				</div>

				<div class="cart-qty hidden-xs hidden-sm">
					<div class="qty">
						<c:url value="/cart/update" var="cartUpdateFormAction" />
						<form:form id="updateCartForm${entry.entryNumber}"
							action="${cartUpdateFormAction}" method="post"
							commandName="updateQuantityForm${entry.entryNumber}">
							<input type="hidden" name="entryNumber"
								value="${entry.entryNumber}" />
							<input type="hidden" name="productCode"
								value="${entry.product.code}" />
							<input type="hidden" name="initialQuantity"
								value="${entry.quantity}" />
							<ycommerce:testId code="cart_product_quantity">
								<form:input cssClass="form-control update-entry-quantity-input"
									disabled="${not entry.updateable}" type="text" size="1"
									id="quantity_${entry.entryNumber}" path="quantity" />
							</ycommerce:testId>
						</form:form>
						<a class="remove-anchor remove-entry-button hidden-xs hidden-sm"
							id="dremoveEntry_${entry.entryNumber}" href="#">remove</a>
					</div>
				</div>
				<div class="cart-total">
					<div class="price">
						<ycommerce:testId code="cart_totalProductPrice_label">
							<format:price priceData="${entry.totalPrice}"
								displayFreeForZero="true" />
						</ycommerce:testId>
					</div>
				</div>

			</div></li>
		<c:choose>
			<c:when test="${oddOrEven == 'odd'}">
				<c:set var="oddOrEven" value="even" />
			</c:when>
			<c:otherwise>
				<c:set var="oddOrEven" value="odd" />
			</c:otherwise>
		</c:choose>
	</c:forEach>
</ul>
