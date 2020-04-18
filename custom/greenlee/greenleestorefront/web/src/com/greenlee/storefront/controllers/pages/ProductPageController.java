/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.greenlee.storefront.controllers.pages;

import de.hybris.platform.acceleratorfacades.futurestock.FutureStockFacade;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.FutureStockForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.ReviewValidator;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.acceleratorstorefrontcommons.variants.VariantSortStrategy;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.FutureStockData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.Config;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.greenlee.core.comparators.GreenleeProductDocumentsGeneralComparator;
import com.greenlee.core.comparators.GreenleeProductDocumentsGroupComparator;
import com.greenlee.core.model.GreenleeProductModel;
import com.greenlee.facades.product.ShareProductFacade;
import com.greenlee.greenleefacades.greenleeproduct.data.GreenleeProductMediaGroupData;
import com.greenlee.storefront.controllers.ControllerConstants;
import com.greenlee.storefront.forms.GreenleeReviewForm;
import com.greenlee.storefront.forms.ShareProductForm;
import com.greenlee.storefront.forms.validation.GreenleeReviewValidator;


/**
 * Controller for product details page
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/**/p")
public class ProductPageController extends AbstractPageController
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ProductPageController.class);

	/**
	 * We use this suffix pattern because of an issue with Spring 3.1 where a Uri value is incorrectly extracted if it
	 * contains on or more '.' characters. Please see https://jira.springsource.org/browse/SPR-6164 for a discussion on
	 * the issue and future resolution.
	 */
	private static final String PRODUCT = "product";
	private static final String PRODUCT_CODE_PATH_VARIABLE_PATTERN = "/{productCode:.*}";
	private static final String REVIEWS_PATH_VARIABLE_PATTERN = "{numberOfReviews:.*}";

	private static final String FUTURE_STOCK_ENABLED = "storefront.products.futurestock.enabled";
	private static final String STOCK_SERVICE_UNAVAILABLE = "basket.page.viewFuture.unavailable";
	private static final String NOT_MULTISKU_ITEM_ERROR = "basket.page.viewFuture.not.multisku";
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String SPLIT_PATTERN = ",";

	@Resource(name = "productModelUrlResolver")
	private UrlResolver<ProductModel> productModelUrlResolver;

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "productBreadcrumbBuilder")
	private ProductBreadcrumbBuilder productBreadcrumbBuilder;

	@Resource(name = "cmsPageService")
	private CMSPageService cmsPageService;

	@Resource(name = "variantSortStrategy")
	private VariantSortStrategy variantSortStrategy;

	@Resource(name = "greenleeReviewValidator")
	private GreenleeReviewValidator reviewValidator;

	@Resource(name = "futureStockFacade")
	private FutureStockFacade futureStockFacade;

	@Resource(name = "shareProductFacade")
	private ShareProductFacade shareProductFacade;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String productDetail(@PathVariable("productCode") final String productCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException, UnsupportedEncodingException
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		final String redirection = checkRequestUrl(request, response, productModelUrlResolver.resolve(productModel));
		if (StringUtils.isNotEmpty(redirection))
		{
			return redirection;
		}

		updatePageTitle(productModel, model);

		final List<ProductOption> extraOptions = Arrays.asList(ProductOption.VARIANT_MATRIX_BASE, ProductOption.VARIANT_MATRIX_URL,
				ProductOption.VARIANT_MATRIX_MEDIA, ProductOption.EXTENDEDATTRIBUTES);
		populateProductDetailForDisplay(productModel, model, request, extraOptions);

		model.addAttribute(new GreenleeReviewForm());
		final List<ProductReferenceData> productReferences = productFacade.getProductReferencesForCode(productCode, Arrays
				.asList(ProductReferenceTypeEnum.SIMILAR, ProductReferenceTypeEnum.ACCESSORIES, ProductReferenceTypeEnum.FOLLOWUP),
				Arrays.asList(ProductOption.BASIC, ProductOption.PRICE), null);
		model.addAttribute("productReferences", productReferences);
		model.addAttribute("pageType", PageType.PRODUCT.name());
		model.addAttribute("futureStockEnabled", Boolean.valueOf(Config.getBoolean(FUTURE_STOCK_ENABLED, false)));

		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(productModel.getKeywords());
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(productModel.getDescription());
		setUpMetaData(model, metaKeywords, metaDescription);
		return getViewForPage(model);
	}

	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/orderForm", method = RequestMethod.GET)
	public String productOrderForm(@PathVariable("productCode") final String productCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		updatePageTitle(productModel, model);

		final List<ProductOption> extraOptions = Arrays.asList(ProductOption.VARIANT_MATRIX_BASE,
				ProductOption.VARIANT_MATRIX_PRICE, ProductOption.VARIANT_MATRIX_MEDIA, ProductOption.VARIANT_MATRIX_STOCK,
				ProductOption.URL);
		populateProductDetailForDisplay(productModel, model, request, extraOptions);

		if (!model.containsAttribute(WebConstants.MULTI_DIMENSIONAL_PRODUCT))
		{
			return REDIRECT_PREFIX + productModelUrlResolver.resolve(productModel);
		}

		return ControllerConstants.Views.Pages.Product.OrderForm;
	}

	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/zoomImages", method = RequestMethod.GET)
	public String showZoomImages(@PathVariable("productCode") final String productCode,
			@RequestParam(value = "galleryPosition", required = false) final String galleryPosition, final Model model)
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		final ProductData productData = productFacade.getProductForOptions(productModel,
				Arrays.asList(ProductOption.BASIC, ProductOption.EXTENDEDATTRIBUTES, ProductOption.VIDEOLINK, ProductOption.GALLERY));
		final List<Map<String, ImageData>> images = getGalleryImages(productData);
		populateProductData(productData, model);
		if (galleryPosition != null)
		{
			try
			{
				model.addAttribute("zoomImageUrl", images.get(Integer.parseInt(galleryPosition)).get("zoom").getUrl());
			}
			catch (final IndexOutOfBoundsException | NumberFormatException ioebe)
			{
				LOG.error(ioebe);
				model.addAttribute("zoomImageUrl", "");
			}
		}
		return ControllerConstants.Views.Fragments.Product.ZoomImagesPopup;
	}

	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/quickView", method = RequestMethod.GET)
	public String showQuickView(@PathVariable("productCode") final String productCode, final Model model,
			final HttpServletRequest request)
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		final ProductData productData = productFacade.getProductForOptions(productModel,
				Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
						ProductOption.CATEGORIES, ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.REVIEW,
						ProductOption.VARIANT_FULL, ProductOption.DELIVERY_MODE_AVAILABILITY));

		sortVariantOptionData(productData);
		populateProductData(productData, model);
		getRequestContextData(request).setProduct(productModel);

		return ControllerConstants.Views.Fragments.Product.QuickViewPopup;
	}

	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/review", method =
	{ RequestMethod.GET, RequestMethod.POST })
	public String postReview(@PathVariable final String productCode, final GreenleeReviewForm form, final BindingResult result,
			final Model model, final HttpServletRequest request, final RedirectAttributes redirectAttrs)
			throws CMSItemNotFoundException
	{
		getReviewValidator().validate(form, result);

		if (result.hasErrors())
		{
			return ControllerConstants.Views.Fragments.Product.ReviewsTab;
		}
		else
		{
			final ReviewData review = new ReviewData();
			review.setHeadline(XSSFilterUtil.filter(form.getHeadline()));
			review.setComment(XSSFilterUtil.filter(form.getComment()));
			review.setRating(form.getRating());
			review.setAlias(XSSFilterUtil.filter(form.getAlias()));
			productFacade.postReview(productCode, review);
			return ControllerConstants.Views.Fragments.Product.ReviewsValidationMessage;
		}
	}

	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/reviewhtml/"
			+ REVIEWS_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String reviewHtml(@PathVariable("productCode") final String productCode,
			@PathVariable("numberOfReviews") final String numberOfReviews, final Model model, final HttpServletRequest request)
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		final List<ReviewData> reviews;
		final ProductData productData = productFacade.getProductForOptions(productModel,
				Arrays.asList(ProductOption.BASIC, ProductOption.REVIEW));

		if ("all".equals(numberOfReviews))
		{
			reviews = productFacade.getReviews(productCode);
		}
		else
		{
			final int reviewCount = Math.min(Integer.parseInt(numberOfReviews),
					productData.getNumberOfReviews() == null ? 0 : productData.getNumberOfReviews().intValue());
			reviews = productFacade.getReviews(productCode, Integer.valueOf(reviewCount));
		}

		getRequestContextData(request).setProduct(productModel);
		model.addAttribute("reviews", reviews);
		model.addAttribute("reviewsTotal", productData.getNumberOfReviews());
		model.addAttribute(new GreenleeReviewForm());

		return ControllerConstants.Views.Fragments.Product.ReviewsTab;
	}

	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/writeReview", method = RequestMethod.GET)
	public String writeReview(@PathVariable final String productCode, final Model model) throws CMSItemNotFoundException
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		model.addAttribute(new GreenleeReviewForm());
		setUpReviewPage(model, productModel);
		return ControllerConstants.Views.Pages.Product.WriteReview;
	}

	protected void setUpReviewPage(final Model model, final ProductModel productModel) throws CMSItemNotFoundException
	{
		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(productModel.getKeywords());
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(productModel.getDescription());
		setUpMetaData(model, metaKeywords, metaDescription);
		storeCmsPageInModel(model, getPageForProduct(productModel));
		model.addAttribute(PRODUCT, productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC)));
		updatePageTitle(productModel, model);
	}

	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/writeReview", method = RequestMethod.POST)
	public String writeReview(@PathVariable final String productCode, final GreenleeReviewForm form, final BindingResult result,
			final Model model, final HttpServletRequest request, final RedirectAttributes redirectAttrs)
			throws CMSItemNotFoundException
	{
		getReviewValidator().validate(form, result);

		final ProductModel productModel = productService.getProductForCode(productCode);

		if (result.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "review.general.error");
			populateProductDetailForDisplay(productModel, model, request, Collections.emptyList());
			setUpReviewPage(model, productModel);
			return ControllerConstants.Views.Pages.Product.WriteReview;
		}

		final ReviewData review = new ReviewData();
		review.setHeadline(XSSFilterUtil.filter(form.getHeadline()));
		review.setComment(XSSFilterUtil.filter(form.getComment()));
		review.setRating(form.getRating());
		review.setAlias(XSSFilterUtil.filter(form.getAlias()));
		productFacade.postReview(productCode, review);
		GlobalMessages.addFlashMessage(redirectAttrs, GlobalMessages.CONF_MESSAGES_HOLDER, "review.confirmation.thank.you.title");

		return REDIRECT_PREFIX + productModelUrlResolver.resolve(productModel);
	}

	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/futureStock", method = RequestMethod.GET)
	public String productFutureStock(@PathVariable("productCode") final String productCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{
		final boolean futureStockEnabled = Config.getBoolean(FUTURE_STOCK_ENABLED, false);
		if (futureStockEnabled)
		{
			final List<FutureStockData> futureStockList = futureStockFacade.getFutureAvailability(productCode);
			if (futureStockList == null)
			{
				GlobalMessages.addErrorMessage(model, STOCK_SERVICE_UNAVAILABLE);
			}
			else if (futureStockList.isEmpty())
			{
				GlobalMessages.addInfoMessage(model, "product.product.details.future.nostock");
			}

			final ProductModel productModel = productService.getProductForCode(productCode);
			populateProductDetailForDisplay(productModel, model, request, Collections.emptyList());
			model.addAttribute("futureStocks", futureStockList);

			return ControllerConstants.Views.Fragments.Product.FutureStockPopup;
		}
		else
		{
			return ControllerConstants.Views.Pages.Error.ErrorNotFoundPage;
		}
	}

	@ResponseBody
	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/grid/skusFutureStock", method =
	{ RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	public final Map<String, Object> productSkusFutureStock(final FutureStockForm form, final Model model)
	{
		final String productCode = form.getProductCode();
		final List<String> skus = form.getSkus();
		final boolean futureStockEnabled = Config.getBoolean(FUTURE_STOCK_ENABLED, false);

		Map<String, Object> result = new HashMap<>();
		if (futureStockEnabled && CollectionUtils.isNotEmpty(skus) && StringUtils.isNotBlank(productCode))
		{
			final Map<String, List<FutureStockData>> futureStockData = futureStockFacade
					.getFutureAvailabilityForSelectedVariants(productCode, skus);

			if (futureStockData == null)
			{
				// future availability service is down, we show this to the user
				result = Maps.newHashMap();
				final String errorMessage = getMessageSource().getMessage(NOT_MULTISKU_ITEM_ERROR, null,
						getI18nService().getCurrentLocale());
				result.put(NOT_MULTISKU_ITEM_ERROR, errorMessage);
			}
			else
			{
				for (final Map.Entry<String, List<FutureStockData>> entry : futureStockData.entrySet())
				{
					result.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return result;
	}

	@ExceptionHandler(UnknownIdentifierException.class)
	public String handleUnknownIdentifierException(final UnknownIdentifierException exception, final HttpServletRequest request)
	{
		request.setAttribute("message", exception.getMessage());
		return FORWARD_PREFIX + "/404";
	}

	protected void updatePageTitle(final ProductModel productModel, final Model model)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveProductPageTitle(productModel));
	}

	protected void populateProductDetailForDisplay(final ProductModel productModel, final Model model,
			final HttpServletRequest request, final List<ProductOption> extraOptions) throws CMSItemNotFoundException
	{
		getRequestContextData(request).setProduct(productModel);

		final List<ProductOption> options = new ArrayList<>(Arrays.asList(ProductOption.VARIANT_FIRST_VARIANT, ProductOption.BASIC,
				ProductOption.URL, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
				ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION,
				ProductOption.VARIANT_FULL, ProductOption.STOCK, ProductOption.VOLUME_PRICES, ProductOption.PRICE_RANGE,
				ProductOption.DELIVERY_MODE_AVAILABILITY, ProductOption.VIDEOLINK, ProductOption.REQUESTTODEMO,
				ProductOption.PRODUCTFEATURETAB, ProductOption.SHOWADDTOCART, ProductOption.PRODUCTARTIFACTS,
				ProductOption.PRODUCTARTIFACTSGROUPS, ProductOption.SHOWPRICE));

		options.addAll(extraOptions);

		final ProductData productData = productFacade.getProductForOptions(productModel, options);

		sortVariantOptionData(productData);
		sortProductArtifactsData(productData);
		sortProductArtifactsGroupsData(productData);

		storeCmsPageInModel(model, getPageForProduct(productModel));
		populateProductData(productData, model);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, productBreadcrumbBuilder.getBreadcrumbs(productModel.getCode()));
		model.addAttribute("reviewsTotal", Long.valueOf(productData.getReviews().size()));

		if (CollectionUtils.isNotEmpty(productData.getVariantMatrix()))
		{
			model.addAttribute(WebConstants.MULTI_DIMENSIONAL_PRODUCT,
					Boolean.valueOf(CollectionUtils.isNotEmpty(productData.getVariantMatrix())));
		}
	}

	protected void populateProductData(final ProductData productData, final Model model)
	{
		model.addAttribute("galleryImages", getGalleryImages(productData));
		model.addAttribute(PRODUCT, productData);
	}

	protected void sortVariantOptionData(final ProductData productData)
	{
		if (CollectionUtils.isNotEmpty(productData.getBaseOptions()))
		{
			for (final BaseOptionData baseOptionData : productData.getBaseOptions())
			{
				if (CollectionUtils.isNotEmpty(baseOptionData.getOptions()))
				{
					Collections.sort(baseOptionData.getOptions(), variantSortStrategy);
				}
			}
		}

		if (CollectionUtils.isNotEmpty(productData.getVariantOptions()))
		{
			Collections.sort(productData.getVariantOptions(), variantSortStrategy);
		}
	}

	protected void sortProductArtifactsData(final ProductData productData)
	{
		if (CollectionUtils.isNotEmpty(productData.getProductArtifacts()))
		{
			Collections.sort(productData.getProductArtifacts(), new GreenleeProductDocumentsGeneralComparator());
		}
	}

	protected void sortProductArtifactsGroupsData(final ProductData productData)
	{
		if (CollectionUtils.isNotEmpty(productData.getProductArtifactsGroups()))
		{
			for (final GreenleeProductMediaGroupData groupData : productData.getProductArtifactsGroups())
			{
				if (CollectionUtils.isNotEmpty(groupData.getProductMedia()))
				{
					Collections.sort(groupData.getProductMedia(), new GreenleeProductDocumentsGroupComparator());
				}
			}
		}
	}

	protected List<Map<String, ImageData>> getGalleryImages(final ProductData productData)
	{
		final List<Map<String, ImageData>> galleryImages = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(productData.getImages()))
		{
			final List<ImageData> images = new ArrayList<>();
			for (final ImageData image : productData.getImages())
			{
				if (ImageDataType.GALLERY.equals(image.getImageType()))
				{
					images.add(image);
				}
			}
			Collections.sort(images, new Comparator<ImageData>()
			{
				@Override
				public int compare(final ImageData image1, final ImageData image2)
				{
					return image1.getGalleryIndex().compareTo(image2.getGalleryIndex());
				}
			});

			if (CollectionUtils.isNotEmpty(images))
			{
				int currentIndex = images.get(0).getGalleryIndex().intValue();
				Map<String, ImageData> formats = new HashMap<String, ImageData>();
				for (final ImageData image : images)
				{
					if (currentIndex != image.getGalleryIndex().intValue())
					{
						galleryImages.add(formats);
						formats = new HashMap<>();
						currentIndex = image.getGalleryIndex().intValue();
					}
					formats.put(image.getFormat(), image);
				}
				if (!formats.isEmpty())
				{
					galleryImages.add(formats);
				}
			}
		}
		return galleryImages;
	}

	protected ReviewValidator getReviewValidator()
	{
		return reviewValidator;
	}

	protected AbstractPageModel getPageForProduct(final ProductModel product) throws CMSItemNotFoundException
	{
		return cmsPageService.getPageForProduct(product);
	}

	/**
	 * Share product via email
	 *
	 * @return An object that contains toAddress, fromAddress messages or success message
	 * @throws UnsupportedEncodingException
	 * @throws CMSItemNotFoundException
	 */
	@ResponseBody
	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN
			+ "/send-to-friend", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ShareProductForm shareProduct(@PathVariable("productCode") final String productCode,
			@RequestParam("toAddress") final String toAddress, @RequestParam("fromAddress") final String fromAddress,
			@RequestParam("message") final String message) throws CMSItemNotFoundException, UnsupportedEncodingException
	{
		final List<String> recipients = Arrays.asList(toAddress.split(SPLIT_PATTERN));
		final ShareProductForm formResult = new ShareProductForm();
		if (recipients.isEmpty())
		{
			formResult.setToAddressMessage("Please check recipients' email addresses");
		}
		if (!fromAddress.trim().matches(EMAIL_PATTERN))
		{
			formResult.setFromAddressMessage("Please check your email address");
		}
		for (final String recipient : recipients)
		{
			if (!recipient.trim().matches(EMAIL_PATTERN))
			{
				formResult.setToAddressMessage("Please check recipients' email addresses");
			}
		}
		if (formResult.getFromAddressMessage() == null && formResult.getToAddressMessage() == null)
		{
			for (final String recipient : recipients)
			{
				final GreenleeProductModel greenleeProductModel = (GreenleeProductModel) productService
						.getProductForCode(productCode);
				shareProductFacade.shareProduct(greenleeProductModel, recipient.trim(), fromAddress, message);
			}
			formResult.setSuccessMessage("Your message has been sent");
		}
		return formResult;
	}

	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/shareProductPopup", method = RequestMethod.GET)
	public String getShareProductPopup(@PathVariable final String productCode, final Model model) throws CMSItemNotFoundException
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		final ProductData productData = productFacade.getProductForOptions(productModel,
				Arrays.asList(ProductOption.BASIC, ProductOption.URL, ProductOption.DESCRIPTION, ProductOption.IMAGES));
		model.addAttribute(PRODUCT, productData);
		return ControllerConstants.Views.Fragments.Product.ShareProductPopup;
	}

	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/reviewdatahtml", method = RequestMethod.GET)
	public String getReviewsPerPage(@PathVariable("productCode") final String productCode,
			@RequestParam(value = "page", required = false, defaultValue = "1") final int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "4") final int pageSize, final Model model,
			final HttpServletRequest request)
	{

		final ProductModel productModel = productService.getProductForCode(productCode);
		final ProductData productData = productFacade.getProductForOptions(productModel,
				Arrays.asList(ProductOption.BASIC, ProductOption.REVIEW));
		final List<ReviewData> reviewsList = new ArrayList<>(productFacade.getReviews(productCode));
		final Long totalreviews = Long.valueOf(reviewsList.size());

		int offset;
		if (pageNumber <= 1)
		{
			offset = 0;
		}
		else
		{
			offset = (pageNumber - 1) * (pageSize);
		}

		int maxLimit = offset + pageSize;

		if (reviewsList.size() < maxLimit)
		{

			maxLimit = reviewsList.size();
		}

		getRequestContextData(request).setProduct(productModel);
		model.addAttribute("reviews", reviewsList.isEmpty() ? reviewsList : new ArrayList(reviewsList.subList(offset, maxLimit)));
		model.addAttribute("totalCount", totalreviews);
		model.addAttribute("offset", Long.valueOf(offset));
		model.addAttribute("maxLimit", Long.valueOf(maxLimit));
		model.addAttribute(PRODUCT, productData);

		return ControllerConstants.Views.Fragments.Product.ReviewsPerPage;
	}

	@RequireHardLogIn
	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + "/openWriteReviewPopup", method = RequestMethod.GET)
	public String openWriteReviewPopup(@PathVariable final String productCode, final Model model) throws CMSItemNotFoundException
	{
		final ProductModel productModel = productService.getProductForCode(productCode);
		final ProductData productData = productFacade.getProductForOptions(productModel,
				Arrays.asList(ProductOption.BASIC, ProductOption.REVIEW));
		model.addAttribute(PRODUCT, productData);
		model.addAttribute(new GreenleeReviewForm());
		return ControllerConstants.Views.Fragments.Product.ReviewsTab;
	}
}
