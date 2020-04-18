<link rel="stylesheet" type="text/css" media="all" href="${commonResourcePath}/css/productCompare.css"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div class="greenlee-body">
            <div class="product-compare">
                <div class="simple-responsive-banner-component">
                    <img src="/image/compare/compare-banner.jpg" />
                </div>
                <div class="container">
                <div class="compare-heading">
                    <h1><spring:theme code="search.compareModels" text="COMPARE MODELS"/></h1>
                    <a href=""><spring:theme code="search.ViewAllModels" text="View all models"/></a>
                </div>
                
                    <div class="row product product-compare-list">
                        <div class="col-md-offset-3 col-xs-offset-6">
                        	<div class="compare-carousel">
                        		<c:forEach items="${products}" var="product">
	                            <div class="item">
	                                <div class="col-xs-12 col-md-4 prd">
	                                    <div class="compare-prd">
	                                        <a class="thumb" href="/p/ask306" title="AirScout 306 Wi-Fi Testing Kit ASK306">
	                                            <img src="image/home/range.png" alt="AirScout 306 Wi-Fi Testing Kit ASK306" />
	                                        </a>
	                                        <h3><a class="thumb" href="/p/ask306" title="AirScout 306 Wi-Fi Testing Kit ASK306">${product.name}</a></h3>
	                                        <span class="price"><product:productPricePanel product="${product}"/></span>
	                                        <p>${product.description}</p>
	                                        <form method="post" id="addToCartForm1" class="add_to_cart_form" action="${addToCartUrl}">
	                                            <input type="hidden" maxlength="3" size="1" id="qty" name="qty" class="qty js-qty-selector-input" value="1">
	                                            <input type="hidden" name="productCodePost" value="${product.code}" />
	                                            <button id="addToCartButton1" type="submit" class="btn btn-primary btn-block js-add-to-cart">
	                                                <i class="fa fa-shopping-cart"></i>Add to cart
	                                            </button>
	                                        </form>
	                                    </div>
	                                </div>
	                            </div>
	                            </c:forEach>
	                          </div>
                        </div>
                    </div>
                    <div class="row feature-compare-list">
                    	<div class="feature-compare-title">
                        	FEATURE NAME
                        </div>
                        
                        <div class="col-md-3 col-xs-6">
	                        <ul class="feature-list--title">
	                        <c:forEach items="${featureNames}" var="feature">
	                        	<li>${feature}</li>
	                        </c:forEach>
							</ul>
                        </div>
                        <div class="list-compare">
                        	<c:forEach items="${featureValues}" var="featureValue">
                        		
                        		
                        			<div class="item">
                        		<div class="col-md-3 col-xs-6">
                        			<ul>
                        			<c:forEach items="${featureValue.value}" var="feature">
                        			<li>${feature}</li>
									</c:forEach>
									</ul>
                        		</div>
                        	</div>		
                        	</c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>