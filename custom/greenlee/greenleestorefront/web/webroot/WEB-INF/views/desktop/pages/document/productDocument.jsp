<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>

<template:page pageTitle="${pageTitle}">
<main data-currency-iso-code="USD">
  
  <div class="greenlee-body">
  <div class="container">
                <div class="row">
                    <div class="col-md-12 cart-switch">
                        <cms:pageSlot position="ASMComponentSlot" var="feature">
                            <cms:component component="${feature}"/>
                        </cms:pageSlot>
                    </div>
                </div>
            </div>
  	<div class="container">
   		<div class="row"> 
   			<div class="col-md-12 cart-switch"> 
  				<cms:pageSlot position="BreadCrumbSlot" var="feature"> 
  					<cms:component component="${feature}" /> 
  				</cms:pageSlot> 
  			</div>
  		 </div> 
  		 <div class="row">
				<div class="col-md-12 cart-switch">
					<cms:pageSlot position="TopHeaderSlot" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
				</div>
			</div>
  	</div>
    <div class="doc-search-container search-result">
      <div class="container">
        <h1><spring:theme code="productDocument.searchHeader" text="Search Manuals and Documents"/></h1>
        
		<div class="row">
			<div class="col-md-8 col-md-offset-2">
				<div class="search-here">
					<p><spring:theme code="productDocument.searchDescription" text="Enter your device model number to download owner's manuals, use and care manuals, installation information and other related documents."/></p>
					<div class="control-group">
					  <div class="controls">
					  	<form action="productDocument" method="get">
						<input type="text" name="text" placeholder='"<spring:theme code="productDocument.searchPlaceholder" text="Eg: AirScout 310 or ASK310"/>"'>
						<button type="submit" class="">
						<c:if test="${isSearchPage}">
						<i class="fa fa-search"></i>
						</c:if>
						</button>
						<c:if test="${isSearchPage}">
						<c:url var="documentLandingpage" value="/productDocument"></c:url>
						<label class="over-label"><spring:theme code="productDocument.resultsFor" text="Results For "/>
						<span> "${searchText}"</span><a href="javascript:void(0);"><i class="fa fa-close"></i></a></label>
						</c:if>
						</form>
					  </div>
					</div>
				</div>
				<c:if test = "${not empty searchPageData.results}">
				<div class="search-output">
					<h2><spring:theme code="productDocument.allResults" text="All results "/>(<c:out value="${noOfResults}"/>)</h2>
					<div class="header">
						<div class="row">
							<div class="col-xs-8 col-md-6 name"><spring:theme code="productDocument.manuals" text="Manuals & Documents"/></div>
							<div class="col-md-3 hidden-xs hidden-sm type"><spring:theme code="productDocument.fileType" text="File type"/></div>
							<div class="col-xs-4 col-md-3 size"><spring:theme code="productDocument.fileSize" text="File Size"/></div>
						</div>
					</div>
					
  			<c:forEach items="${searchPageData.results}" var="document">
					<div class="row">
						<div class="col-xs-8 col-md-6 name"><a href="${document.URL}"><i class="fa fa-file-pdf-o fa-m"></i>${document.documentName}</a></div>
						<div class="col-md-3 hidden-xs hidden-sm type">${document.documentType}</div>
						<div class="col-xs-4 col-md-3 size">${document.documentSize}</div>
					</div>
					</c:forEach>
				</div>
				</c:if>
			</div>
		</div>
		<div class="hidden-xs hidden-sm did-you-find">
			<h3><spring:theme code="productDocument.lookingFor" text="Did you find what you were looking for?"/></h3>
			<p><spring:theme code="productDocument.check" text="If not, check to make sure you entered your model number correctly,"/> </br><spring:theme code="productDocument.email" text="or email cscommunications@greenlee.textron.com or call 1-800-642-2155"/></p>
		</div>
	  </div>
    </div>
    </div>
  </main>
</template:page>