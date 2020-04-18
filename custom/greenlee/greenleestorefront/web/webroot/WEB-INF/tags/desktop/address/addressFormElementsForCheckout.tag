<%@ attribute name="regions" required="true" type="java.util.List"%>
<%@ attribute name="country" required="false" type="java.lang.String"%>
<%@ attribute name="tabIndex" required="false" type="java.lang.Integer"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>


		<div class="row">
			<div class="col-md-4">
		<formElement:formSelectBox idKey="address.title"
			labelKey="address.title" path="titleCode" mandatory="false"
			skipBlank="true" skipBlankMessageKey=""
			items="${titles}" selectedValue="${addressForm.titleCode}"
			selectCSSClass="custom-select" divCSSClass="title-select controls isBorder usertype-selector" />
			</div>
		</div>
		<div class="row">
			<div class="col-md-4">
				<formElement:formInputBox idKey="address.firstName"
					labelKey="address.firstName" path="firstName"
					inputCSS="form-control" mandatory="true" />
			</div>
			<div class="col-md-4">
				<formElement:formInputBox idKey="address.surname"
					labelKey="address.surname" path="lastName" inputCSS="form-control"
					mandatory="true" />
			</div>
		</div>
		<div class="row">
			<div class="col-md-4">						
				<div id="countrySelector" class="form-group ">
					<formElement:formSelectBox idKey="address.country"
							labelKey="address.country" path="countryIso" mandatory="true"
							skipBlank="false" skipBlankMessageKey="" items="${countries}"
							itemValue="${useShortRegionIso ? 'isocodeShort' : 'isocode'}"
							selectedValue="${addressForm.countryIso}"
							selectCSSClass="countrySelector form-control custom-select"
							divCSSClass=" controls isBorder usertype-selector"
							/>
						 <%-- <spring:theme  code="address.country.disclaimer"/>  --%>
					</div>
			</div>
			<div class="col-md-4">
				<formElement:formSelectBox idKey="address.region"
						labelKey="address.state" path="regionIso" mandatory="true"
						skipBlank="false" skipBlankMessageKey="form.select.empty"
						items="${regions}"
						itemValue="${useShortRegionIso ? 'isocodeShort' : 'isocode'}"
						selectedValue="${addressForm.regionIso}"
						selectCSSClass="countrySelector form-control custom-select"
						divCSSClass=" controls isBorder usertype-selector"
							/>
			</div>			
		</div>
		<div class="row">
			<div class="col-md-4">
			<formElement:formInputBox idKey="address.line1"
				labelKey="address.line1" path="line1" inputCSS="form-control"
				mandatory="true" />
			</div>
		</div>
		<div class="row">
			<div class="col-md-4">
			<formElement:formInputBox idKey="address.line2"
				labelKey="address.line2" path="line2" inputCSS="form-control"
				mandatory="false" />
			</div>
		</div>
		<div class="row">
			<div class="col-md-4">
				<formElement:formInputBox idKey="address.townCity"
					labelKey="address.townCity" path="townCity" inputCSS="form-control"
					mandatory="true" />
			</div>			
			<div class="col-md-4">
				<formElement:formInputBox idKey="address.postcode"
					labelKey="address.zipcode" path="postcode" inputCSS="form-control"
					mandatory="true" />
			</div>			
		</div>
		<div class="row">
			<div class="col-md-4">
				<formElement:formInputBox idKey="address.phone"
					labelKey="address.phone" path="phone" inputCSS="form-control"
					mandatory="true" />
			</div>
		</div>
	

