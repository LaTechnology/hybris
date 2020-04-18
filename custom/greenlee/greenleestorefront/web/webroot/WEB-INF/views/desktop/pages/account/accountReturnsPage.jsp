<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<div class="col-md-10">
	<div class="order-details-container return">
		<h1>
			<spring:theme code="account.orders.returns.title" text="Request A Return"/>
		</h1>

		<form action="${salesforce_form_action}" method="POST">
			<input type="hidden" name="orgid" value="${salesforce_form_orgid}">
				<input type=hidden name="retURL" value="${salesforce_form_returl}">
					<input type=hidden name="recordType" id="recordType" value="${salesforce_form_recordtype}">

						<!--  ----------------------------------------------------------------------  -->
						<!--  NOTE: These fields are optional debugging elements. Please uncomment    -->
						<!--  these lines if you wish to test in debug mode.                          -->
						<!--  <input type="hidden" name="debug" value=1>                              -->
						<!--  <input type="hidden" name="debugEmail"                                  -->
						<!--  value="lalario@greenlee.textron.com">                                   -->
						<!--  ----------------------------------------------------------------------  -->

						<div class="row">
							<div class="col-md-4 saleforceform">

								<div class="form-group">
									<label for="First Name:"><spring:theme code="account.orders.returns.firstname" text="First Name"/></label>
									<input id="First_Name__c" maxlength="25" name="First_Name__c" size="25" type="text" class="form-control"/>
								</div>
								<div class="form-group">
									<label for="Last Name:<"><spring:theme code="account.orders.returns.lastname" text="Last Name"/></label>
									<input id="Last_Name__c" maxlength="35" name="Last_Name__c" size="35" type="text" class="form-control"/>
								</div>
								<div class="form-group">
									<label for="email"><spring:theme code="account.orders.returns.email" text="Email"/></label>
									<input id="email" maxlength="80" name="email" size="20" type="text" class="form-control"></div>
									<div class="form-group">
										<label for="phone"><spring:theme code="account.orders.returns.phone" text="Phone"/></label><input id="phone" maxlength="40" name="phone" size="20" type="text" class="form-control"/>
									</div>
									<div class="form-group">
										<label for="company"><spring:theme code="account.orders.returns.company" text="Company"/></label><input id="Company_Name__c" maxlength="30" name="Company_Name__c" size="20" type="text" class="form-control"/>
									</div>
									<div class="form-group">
										<label><spring:theme code="account.orders.returns.ordernumber" text="Order Number"/></label><input id="Order_Number__c" maxlength="10" name="Order_Number__c" size="20" type="text" class="form-control"/>
									</div>
									<div class="form-group">
										<label><spring:theme code="account.orders.requestrepair.internal.number" text="Greenlee Internal Part Number"/></label><input id="Enter_5M_Number__c" maxlength="25" name="Enter_5M_Number__c" size="20" type="text" class="form-control number-group"/>
									</div>
									<div class="form-group">
										<label><spring:theme code="account.orders.returns.entercatalognumber" text="Enter Catalog Number"/></label><input id="Enter_Catalog_Number__c" maxlength="25" name="Enter_Catalog_Number__c" size="20" type="text" class="form-control number-group"/>
									</div>
									<div class="form-group">
										<label><spring:theme code="account.orders.returns.quantity" text="Quantity"/></label><input id="Quantity__c" name="Quantity__c" size="20" type="text" class="form-control"/>
									</div>
									<div class="form-group">

										<label><spring:theme code="account.orders.returns.reasonforreturn" text="Reason For Return"/></label>
										<div class="controls isBorder">
											<select id="Reason_for_Return__c" name="Reason_for_Return__c" title="Reason for Return" class="custom-select">
												<option value="">--None--</option>
												<option value="Carrier Damage">Carrier Damage</option>
												<option value="Carrier Lost">Carrier Lost</option>
												<option value="Consignment Stock Return">Consignment Stock Return</option>
												<option value="Open Box - Label &amp; Material Mismatched">Open Box - Label &amp; Material Mismatched</option>
												<option value="Other">Other</option>
												<option value="Product Defective">Product Defective</option>
												<option value="Short Shipment">Short Shipment</option>
												<option value="Stock Adjustment">Stock Adjustment</option>
												<option value="Wrong Item Ordered / Wanted">Wrong Item Ordered / Wanted</option>
											</select>

										</div>
										<div class="form-group">
											<label for="description"><spring:theme code="account.orders.returns.description" text="Description"/></label>
											<textarea name="description"></textarea>
										</div>
									</div>
								</div>
									<div class="col-md-12 saleforceform">
										<div class="form-group">
											<div class="checkbox checkbox-group">

												<input id="Privacy_Policy_Consent__c" name="Privacy_Policy_Consent__c" type="checkbox" value="1"/>
												<c:url var="privacyPolicyURL" value="/policies-and-terms"></c:url>
												<label for="Privacy_Policy_Consent__c">I acknowledge that I have read and expressly consent to the collection and usage of this information as described in the
													<a href="${privacyPolicyURL}">Privacy Policy.</a>
												</label>
											</div>
										</div>
										<div class="form-group">
											<div class="checkbox checkbox-group">

												<input id="Sales_and_Marketing_Info_request__c" name="Sales_and_Marketing_Info_request__c" type="checkbox" value="1"/>
												<label for="Sales_and_Marketing_Info_request__c">I consent to receiving additional sales, marketing and other information about the products and services of Greenlee and its affiliates. You may unsubscribe at any time.
												</label>
											</div>
										</div>
									</div>
									<div class="col-md-4 saleforceform">
										<div class="btn-sec">
											<button class="btn-white">Submit</button>
										</div>

								</div>
							</div>
						</form>

					</div>
				</div>
