<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<c:url value="${product.url}" var="productUrl"/>

<div class="pdp-popup js-email-popup sendEmailToFriendPopup">
    <div class="email-popup-content">
        <div class="control-group">
            <div class="share-form">
                <input type="hidden" id="productCodeForShare" value="${product.code}"/>
                <input type="hidden" id="CSRFToken" value="${CSRFToken}"/>
                <div class="row">
                    <div class="form-group col-md-6">
                        <label class="control-label">To *</label>
                        <input name="toAddress" id="toAddress" type="text" class="form-control " />
                        <span id="share-form-toAddress" class="error"></span>
                    </div>
                    <div class="form-group col-md-6 separate">
                        <p>Separate multiple address with commas</p>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-6">
                        <label class="control-label">Your email address *</label>
                        <input name="fromAddress" id="fromAddress" type="text" class="form-control "/>
                        <span id="share-form-fromAddress" class="error"></span>
                    </div>
                    <div class="form-group col-md-12">
                        <label class="control-label">Message</label>
                        <textarea name="message" id="message"></textarea>
                    </div>
                </div>
            </div>
            <span id="share-form-message"></span>

            <div class="row prd-popup">
                <div class="col-md-4">
                    <div class="text-center">
                        <a href="${productUrl }"><product:productPrimaryImage product="${product}" format="product"/></a>
                    </div>
                </div>
                <div class="col-md-8 prd-popup-info">
                    <h3>
                        <a href="${productUrl }">${product.name }</a>
                    </h3>
                    <p>${product.description }</p>
                </div>
            </div>
            <div class="form-group">
                <button class="sendProductAsEmailButton btn btn-white btn-popup" value="Share">Share</button>
            </div>
        </div>
    </div>
</div>
