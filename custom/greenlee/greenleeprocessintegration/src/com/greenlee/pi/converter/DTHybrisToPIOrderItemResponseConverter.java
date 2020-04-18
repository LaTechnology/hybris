/**
 *
 */
package com.greenlee.pi.converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.greenlee.core.util.CommonUtils;
import com.greenlee.orderhistory.data.PIItems;
import com.greenlee.orderhistory.data.PIOrderItem;
import com.greenlee.orderhistory.data.PIPartyAddress;
import com.greenlee.orderhistory.data.PITrackingItems;
import com.greenlee.orderhistory.data.TrackingSerialsItem;
import com.hybris.urn.xi.order_history_item.DTPIToHybrisOrderItemResponse.OrderDetails;
import com.hybris.urn.xi.order_history_item.DTPIToHybrisOrderItemResponse.OrderDetails.BillToPartyAddress;
import com.hybris.urn.xi.order_history_item.DTPIToHybrisOrderItemResponse.OrderDetails.OrderDetailLine;
import com.hybris.urn.xi.order_history_item.DTPIToHybrisOrderItemResponse.OrderDetails.OrderDetailLine.Items;
import com.hybris.urn.xi.order_history_item.DTPIToHybrisOrderItemResponse.OrderDetails.OrderDetailLine.Items.DlvItems;
import com.hybris.urn.xi.order_history_item.DTPIToHybrisOrderItemResponse.OrderDetails.OrderDetailLine.Items.TrackingNoDetails.TrackingItems;
import com.hybris.urn.xi.order_history_item.DTPIToHybrisOrderItemResponse.OrderDetails.ShiptoPartyAddress;
import com.hybris.urn.xi.order_history_item.DTPIToHybrisOrderItemResponse.OrderDetails.SoldToPartyAddress;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.impl.AbstractConverter;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;


/**
 * @author peter.asirvatham
 *
 */
public class DTHybrisToPIOrderItemResponseConverter extends AbstractConverter<OrderDetails, PIOrderItem>
{
	private static final Logger LOG = Logger.getLogger(DTHybrisToPIOrderItemResponseConverter.class);

	@Resource(name = "productConverter")
	private AbstractPopulatingConverter<ProductModel, ProductData> productConverter;

	@Resource(name = "productService")
	private ProductService productService;
	private static final String DISPLAYPATTERN = "EEEEE, MMMMM dd yyyy";

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.impl.AbstractConverter#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final OrderDetails source, final PIOrderItem target)
	{
		target.setName1(source.getName1());
		target.setSalesOrderNo(source.getSalesOrderNo());
		target.setDescription(source.getDescription());
		target.setStatus(source.getStatus());
		target.setShipToDetails(getShippingAddress(source.getShiptoPartyAddress()));
		target.getShipToDetails().setPartyNumber(source.getShipToPartyNumber());
		target.setSoldToDetails(getSoldToAddress(source.getSoldToPartyAddress()));
		target.getSoldToDetails().setPartyNumber(source.getSoldToPartyNumber());
		target.setBillToDetails(getBillToAddress(source.getBillToPartyAddress()));
		target.getBillToDetails().setPartyNumber(source.getBillToPartyNumber());
		target.setCustomerPurchaseOrderNo(source.getCustomerPurchaseOrderNo());
		target.setPaymentTerms(source.getPaymentTerms());
		target.setNetValueinDocumentCurrency(source.getNetValueInDocumentCurrency());
		target.setRecordCreatedDate(CommonUtils.orderDetailDateFormat(source.getRecordCreatedDate(), DISPLAYPATTERN));
		target.setDocumentRequestedDeliveryDate(source.getDocumentRequestedDeliveryDate());
		target.setOrderNote(source.getOrderNote());
		target.setShipInstLongText(source.getShipInstLongText());
		target.setShipToPONumber(source.getShipToPONumber());
		target.setOrderDetailLine(getPIItems(source.getOrderDetailLine()));
		target.setTax(source.getTax());
		target.setDuty(source.getDuty());
		target.setFreight(source.getFreight());
		target.setSubTotal(source.getSubTotal());
	}

	public PIPartyAddress getShippingAddress(final ShiptoPartyAddress shipAddress)
	{
		final PIPartyAddress shipTargetAddress = new PIPartyAddress();
		if (shipAddress != null)
		{
			shipTargetAddress.setName1(shipAddress.getName1());
			shipTargetAddress.setName2(shipAddress.getName2());
			shipTargetAddress.setHouseNumberAndStreet(shipAddress.getHouseNumberAndStreet());
			shipTargetAddress.setCity(shipAddress.getCity());
			shipTargetAddress.setRegion(shipAddress.getRegion());
			shipTargetAddress.setPostalCode(shipAddress.getPostalCode());
			shipTargetAddress.setCountryKey(shipAddress.getCountryKey());
		}
		LOG.error("PIPartyAddress >> shipAddress: " + "Name1 [" + shipAddress.getName1() + "] Name2[" + shipAddress.getName2()
				+ "] Street [" + shipAddress.getHouseNumberAndStreet() + "] City [" + shipAddress.getCity() + "] Region["
				+ shipAddress.getRegion() + "]Postal Code[" + shipAddress.getPostalCode() + "] Conuntry ["
				+ shipAddress.getCountryKey() + "]");
		return shipTargetAddress;
	}

	public PIPartyAddress getSoldToAddress(final SoldToPartyAddress soldToAddress)
	{
		final PIPartyAddress soldToTargetAddress = new PIPartyAddress();
		if (soldToAddress != null)
		{
			soldToTargetAddress.setName1(soldToAddress.getName1());
			soldToTargetAddress.setName2(soldToAddress.getName2());
			soldToTargetAddress.setHouseNumberAndStreet(soldToAddress.getHouseNumberAndStreet());
			soldToTargetAddress.setCity(soldToAddress.getCity());
			soldToTargetAddress.setRegion(soldToAddress.getRegion());
			soldToTargetAddress.setPostalCode(soldToAddress.getPostalCode());
			soldToTargetAddress.setCountryKey(soldToAddress.getCountryKey());
		}
		LOG.error("PIPartyAddress >> SoldToAddress: " + "Name1 [" + soldToAddress.getName1() + "] Name2["
				+ soldToAddress.getName2() + "] Street [" + soldToAddress.getHouseNumberAndStreet() + "] City ["
				+ soldToAddress.getCity() + "] Region[" + soldToAddress.getRegion() + "]Postal Code[" + soldToAddress.getPostalCode()
				+ "] Conuntry [" + soldToAddress.getCountryKey() + "]");
		return soldToTargetAddress;
	}

	public PIPartyAddress getBillToAddress(final BillToPartyAddress billToAddress)
	{
		final PIPartyAddress billToTargetAddress = new PIPartyAddress();
		if (billToAddress != null)
		{
			billToTargetAddress.setName1(billToAddress.getName1());
			billToTargetAddress.setName2(billToAddress.getName2());
			billToTargetAddress.setHouseNumberAndStreet(billToAddress.getHouseNumberAndStreet());
			billToTargetAddress.setCity(billToAddress.getCity());
			billToTargetAddress.setRegion(billToAddress.getRegion());
			billToTargetAddress.setPostalCode(billToAddress.getPostalCode());
			billToTargetAddress.setCountryKey(billToAddress.getCountryKey());
		}
		LOG.error("PIPartyAddress >> billToAddress: " + "Name1 [" + billToAddress.getName1() + "] Name2["
				+ billToAddress.getName2() + "] Street [" + billToAddress.getHouseNumberAndStreet() + "] City ["
				+ billToAddress.getCity() + "] Region[" + billToAddress.getRegion() + "]Postal Code[" + billToAddress.getPostalCode()
				+ "] Conuntry [" + billToAddress.getCountryKey() + "]");
		return billToTargetAddress;
	}

	/**
	 * @param orderLine
	 * @return List<PIItems>
	 */
	public List<PIItems> getPIItems(final OrderDetailLine orderLine)
	{
		final List<PIItems> itemList = new ArrayList<PIItems>();
		List<PITrackingItems> targetTrackingItemList = null;
		List<TrackingSerialsItem> trackingSerialsItems=null;
		if (orderLine != null)
		{
			for (final Items item : orderLine.getItems())
			{
				final PIItems targetItem = new PIItems();
				targetItem.setSalesOrderItemNo(item.getSalesOrderItemNo());
				targetItem.setMaterialNumber(item.getMaterialNumber());
					final ProductModel productModel = productService.getProductForCode(item.getMaterialNumber());
					if (productModel != null)
					{
						final ProductData productData = productConverter.convert(productModel);
						targetItem.setProduct(productData);
					}
					targetItem.setCatalogNumber(item.getCatalogNumber());
					targetItem.setUpcCode(item.getUPCCode());
					targetItem.setMaterialDescription(item.getMaterialDescription());
					targetItem.setHigherLevelItemInBOM(item.getHigherLevelItemInBOM());
					targetItem.setOrderQuantity(item.getOrderQuantity());
					targetItem.setShippedQuantity(item.getShippedQuantity());
					targetItem.setOpenQuantity(item.getOpenQuantity());
					targetItem.setUnitOfMeasure(item.getUnitOfMeasure());
					targetItem.setUnitrice(item.getUnitPrice());
					targetItem.setExtPrice(item.getExtPrice());
					targetItem.setScheduleLineDate(item.getScheduleLineDate());
					LOG.info("Tracking Items Size:" + item.getTrackingNoDetails().getTrackingItems().size());
					if (item.getTrackingNoDetails() != null)
					{
						targetTrackingItemList = new ArrayList<PITrackingItems>();
						for (final TrackingItems trackingItem : item.getTrackingNoDetails().getTrackingItems())
						{
							final PITrackingItems targetTrackingItems = new PITrackingItems();
							targetTrackingItems.setRoute(trackingItem.getRoute());
							targetTrackingItems.setCarrierURL(trackingItem.getCarrierURL());
							targetTrackingItems.setDelivery(trackingItem.getDelivery());
							targetTrackingItems.setTrackingNo(trackingItem.getTrackingNo());
							targetTrackingItemList.add(targetTrackingItems);
						}
						targetItem.setTrackingNoDetails(targetTrackingItemList);
					}
					targetItem.setStatus(item.getStatus());
					if(item.getDlvItems()!=null && !item.getDlvItems().isEmpty()){
						List<DlvItems> listDlvItems =item.getDlvItems();
						LOG.info("Delivery Items Size:"+item.getDlvItems().size());
						for (int itemIndex = 0; itemIndex < item.getDlvItems().size(); itemIndex++)
						{
							trackingSerialsItems = new ArrayList<TrackingSerialsItem>(listDlvItems.size());
							DlvItems dlvItems2 =listDlvItems.get(itemIndex);
							for (int serialIndex = 0;serialIndex< dlvItems2.getSerialNo().size(); serialIndex++)
							{
								LOG.info("item >> getMaterialNumber >> "+item.getMaterialNumber());
								TrackingSerialsItem serialsItem = new TrackingSerialsItem();
								serialsItem.setSerialNumber(dlvItems2.getSerialNo().get(serialIndex).toString());
								LOG.info("setSerialNumber : "+ serialIndex +"  :: " +dlvItems2.getSerialNo().get(serialIndex).toString());
								
								serialsItem.setDeliveryItemNumber(dlvItems2.getDlvItemNo());
								LOG.info("DeliveryItemNumber : "+ serialIndex +"  :: " + dlvItems2.getDlvItemNo());
								
								trackingSerialsItems.add(serialsItem);
								LOG.info("Total Delivery Items added "+trackingSerialsItems.size());
							}
						}
					}
					targetItem.setSerialNoDetails(trackingSerialsItems);
					itemList.add(targetItem);
			}
		}
		return itemList;
	}


}
