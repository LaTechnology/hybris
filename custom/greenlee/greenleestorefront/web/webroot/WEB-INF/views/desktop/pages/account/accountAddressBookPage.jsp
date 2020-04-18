<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>



<div class="col-md-10 account-body">
	<h1 class="book-heading hidden-xs hidden-sm">
		Address Book<i class="fa fa-angle-down hidden-md hidden-lg"></i>
	</h1>
	<div class="address-book">
		<ul class="address-items clearfix">
			<li class="add-address hidden-md hidden-lg"><c:url
					value="add-address" var="addAddress" /> <a href="${addAddress}"
				class="new-address__links">
					<div class="address-item">

						<div class="new-address">
							<p class="plus">&#43;</p>
							<span class="new-address__links"> <spring:theme
									code="text.account.addressBook.addAddress"
									text="Add new address" />
							</span>
						</div>

					</div>
			</a></li>
			<c:choose>
				<c:when test="${not empty addressData}">
					<%-- <c:forEach items="${addressData}" var="address">
						<c:if test="${address.defaultAddress}">
							<li>
								<div class="address-item shipping-address">
									<c:if
										test="${(address.isSapSourcedAddress or address.billingAddress) and not empty companyName}">
										<h5>${fn:escapeXml(companyName)}</h5>
                                            	</</c:if>
									<h5>${fn:escapeXml(address.title)}&nbsp;${fn:escapeXml(address.firstName)}&nbsp;${fn:escapeXml(address.lastName)}</h5>
									<address>
										${fn:escapeXml(address.line1)}<br />
										<c:if test="${not empty fn:escapeXml(address.line2)}">
                                                    ${fn:escapeXml(address.line2)}<br />
										</c:if>
										${fn:escapeXml(address.town)},&nbsp;${fn:escapeXml(address.region.name)}&nbsp;${fn:escapeXml(address.postalCode)}<br />
										${fn:escapeXml(address.country.name)}<br />
										${fn:escapeXml(address.phone)}
									</address>
									<ycommerce:testId code="addressBook_editAddress_button">
										<c:if
											test="${not address.isSapSourcedAddress and not address.billingAddress}">
											<a class="edit" href="edit-address/${address.id}"> <spring:theme
													code="text.edit" text="Edit" />
											</a>
										</c:if>
									</ycommerce:testId>

									gsm-36    <div class="change-address default-address">
                                                    <a href>
                                                        <i class="fa fa-truck"></i><spring:theme code="text.account.addressBook.defaultShipping" text="Default Shipping Address"/></a>
                                                </div>
									<div>
										<c:if
											test="${address.isSapSourcedAddress or address.billingAddress}">
											<spring:theme code="billing.address.to.change"
												text="contact Greenlee Customer Support" />
										</c:if>
									</div>
									<div class="change-address default-address">
										<c:if test="${BILLING eq 'true'}">
											<a href> <i class="fa fa-credit-card"></i>
											<spring:theme code="addressbook.billingAddress.header"
													text="Billing Address" /></a>
										</c:if>
										<c:if test="${SHIPPING eq 'false'}">
											<a href> <i class="fa fa-truck"></i>
											<spring:theme code="addressbook.shippingAddress.header"
													text="Shipping Address" /></a>
										</c:if>
										<c:if test="${PRIMARY eq 'PRIMARY'}">
	                                                 	 <a href>
	                                                 	 <i class="fa fa-truck"></i><spring:theme code="addressbook.primaryAddress.header" text="Primary Address"/></a>
	                                                 </c:if>
									</div>
								</div>
							</li>
						</c:if>
					</c:forEach> --%>
					<c:forEach items="${addressData}" var="address">
						<%-- <c:if test="${address.defaultAddress}"> --%>
						<li>
							<div class="address-item">
								<div>
									<c:if
										test="${(address.isSapSourcedAddress or address.billingAddress) and not empty companyName}">
										<h5>${fn:escapeXml(companyName)}</h5>
	                                </</c:if>
								</div>
								<h5>
									<c:choose>
										<c:when test="${not empty address.title}">
											${fn:escapeXml(address.title)}&nbsp;${fn:escapeXml(address.firstName)}&nbsp;${fn:escapeXml(address.lastName)}
											</c:when>
										<c:otherwise>
										${fn:escapeXml(address.firstName)}&nbsp;${fn:escapeXml(address.lastName)}</c:otherwise>
									</c:choose>
								</h5>
								
								<address>
									${fn:escapeXml(address.line1)}<br />
									<c:if test="${not empty fn:escapeXml(address.line2)}">
                                                    ${fn:escapeXml(address.line2)}<br />
									</c:if>
									${fn:escapeXml(address.town)},&nbsp;${fn:escapeXml(address.region.name)}&nbsp;${fn:escapeXml(address.postalCode)}<br />
									${fn:escapeXml(address.country.name)}<br />
									${fn:escapeXml(address.phone)}
								</address>
								<div class="address-links clearfix">
									<ul>
										<li class="first"><c:if
												test="${not address.isSapSourcedAddress and address.billingAddress and address.defaultBillingAddress}">
												<a href="remove-address/${address.id}"
													class="remove removeAddressButton">remove</a>&nbsp;
												</c:if></li>
										<li><ycommerce:testId
												code="addressBook_editAddress_button">
												<c:if
													test="${not address.isSapSourcedAddress and address.billingAddress and address.defaultBillingAddress}">
														&nbsp;<a class="edit" href="edit-address/${address.id}">
														<spring:theme code="text.edit" text="Edit" />
													</a>
												</c:if>
											</ycommerce:testId></li>
									</ul>
								</div>
								<c:if
									test="${not address.isSapSourcedAddress and address.billingAddress and address.defaultBillingAddress}">
									<div class="change-address default-address">
										<ycommerce:testId code="addressBook_isDefault_button">
											<a href="set-default-address/${address.id}"> <i
												class="fa fa-truck"></i> <spring:theme
													code="text.setDefault" text="Set as default" />
											</a>
										</ycommerce:testId>
									</div>
								</c:if>
								<c:set value="${address.primaryAddress}" var="isPrimary" />
								<div>
									<%-- <c:if
											test="${address.isSapSourcedAddress or address.billingAddress}"> --%>
									<c:if test="${isPrimary eq 'true'}">
										<spring:theme code="billing.address.to.change"
											text="contact Greenlee Customer Support" />
									</c:if>
								</div>
								<div class="change-address default-address">
									<%-- <c:if test="${isPrimary}">
										<!-- <a href>  <i class="fa fa-truck"></i> -->
										<spring:theme code="addressbook.primaryAddress.header"
											text="Primary Address" />&nbsp;
											<!-- </a> -->
									</c:if> --%>

									<c:choose>
										<c:when test="${not empty address.labelName}">
											<spring:theme code="addressbook.${address.labelName}.header"
												text="NA" />&nbsp;
											</c:when>
										<c:otherwise>
											NA</c:otherwise>
									</c:choose>

								</div>
							</div>
						</li>
						<%-- </c:if> --%>
					</c:forEach>
					<li class="add-address hidden-xs hidden-sm"><c:url
							value="add-address" var="addAddress" /> <a href="${addAddress}"
						class="new-address__links">
							<div class="address-item">
								<div class="new-address">
									<ycommerce:testId code="addressBook_addNewAddress_button">
										<p class="plus">&#43;</p>
										<span class="new-address__links"> <spring:theme
												code="text.account.addressBook.addAddress"
												text="Add new address" />
										</span>
									</ycommerce:testId>
								</div>
							</div>
					</a></li>
				</c:when>
				<c:otherwise>
					<li class="add-address hidden-xs hidden-sm"><c:url
							value="add-address" var="addAddress" /> <a href="${addAddress}"
						class="new-address__links">
							<div class="address-item">
								<div class="new-address">
									<ycommerce:testId code="addressBook_addNewAddress_button">
										<p class="plus">&#43;</p>
										<span class="new-address__links"> <spring:theme
												code="text.account.addressBook.addAddress"
												text="Add new address" />
										</span>
									</ycommerce:testId>
								</div>

							</div>
					</a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
</div>

