<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${not empty product.classifications}">

	<table>
		<tbody>
			<c:forEach items="${product.classifications}" var="classification">
				<c:forEach items="${classification.features}" var="feature">
					<c:set value="false" var="noValue" />
					<c:forEach items="${feature.featureValues}" var="value">
						<c:if test="${value.value eq '-' }">
							<c:set value="true" var="noValue" />
						</c:if>
					</c:forEach>
					<c:if test="${noValue eq false}">
						<tr>
							<td class="text-right">${feature.name}</td>
							<td><c:forEach items="${feature.featureValues}" var="value"
									varStatus="status">
										${value.value}
										<c:choose>
										<c:when test="${feature.range}">
												${not status.last ? '-' : feature.featureUnit.symbol}
											</c:when>
										<c:otherwise>
												${feature.featureUnit.symbol}
												${not status.last ? '<br/>' : ''}
											</c:otherwise>
									</c:choose>
								</c:forEach></td>
						</tr>
					</c:if>
				</c:forEach>
			</c:forEach>
		</tbody>
	</table>

</c:if>

<%-- <div class="product-classifications">
	<c:if test="${not empty product.classifications}">
		<c:forEach items="${product.classifications}" var="classification">
			<div class="headline">${classification.name}</div>
				<table class="table">
					<tbody>
						<c:forEach items="${classification.features}" var="feature">
							<tr>
								<td class="attrib">${feature.name}</td>

								<td>
									<c:forEach items="${feature.featureValues}" var="value" varStatus="status">
										${value.value}
										<c:choose>
											<c:when test="${feature.range}">
												${not status.last ? '-' : feature.featureUnit.symbol}
											</c:when>
											<c:otherwise>
												${feature.featureUnit.symbol}
												${not status.last ? '<br/>' : ''}
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

		</c:forEach>
	</c:if>
</div> --%>