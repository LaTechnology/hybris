$(document).ready(function ()
{
	localStorage.clear();
	$("#compareBtn").hide()
	$("#product1").hide();
	$("#product2").hide();
	$("#product3").hide();
	var productData=[];
	productsData= getProducts();
	
	if(productsData===null)
	{
		$("#products").empty();
	}
	else
	{
		showProducts(productsData);
	}
});

$("#remove1").click(function()
		{
			var code=$("#productData1").text();
			removeProduct(code);
		}
		);
$("#remove2").click(function()
		{
			var code=$("#productData2").text();
			removeProduct(code);
		}
		);

$("#remove3").click(function()
		{
			var code=$("#productData3").text();
			removeProduct(code);
		}
		);

$("#compareBtn").click(function()
{
	var compareUrl=$("#compareUrl").text();
	window.location.assign(compareUrl+"="+localStorage.getItem("products"));
	console.log(localStorage.getItem("products"));
}
);

function removeProduct(code)
{
	
	var products=[];
	products=getProducts();
	var productsList=[];
	for(i=0;i<products.length;i++)
	{ 
		if(products[i]==code)
		{
			//
		}
		else
		{
			productsList.push(products[i]);
		}
	}
	saveProducts(productsList);
	showProducts(productsList);
}
function showProducts(productsData)
{

	if(productsData.length>=0)
	{
		for(i=0;i<productsData.length;i++)
		{
			$("#productData"+(i+1)).text(productsData[i]);
			$("#product"+(i+1)).show();
		}
	}
	if(productsData.length>=2)
	{
		$("#compareBtn").show();
	}
}
function getProducts()
{
	var products=[];
	products= getProducts();
	 return products;
}


function addProducts(value)
{
	var products=[];
	products=getProducts();
	if(products===null)
	{
		var products=[];
		products.push(value);
		saveProducts(products)
		showProducts(products);
	}
	else
	{ 
		if(checkProduct(value))
		{
			document.getElementById('com-message').innerHTML = "<h4 style='color: red'>Selected product is already in list<h4>";
		}
		else
		{
			products.push(value);
			saveProducts(products);
			showProducts(products);
		}
	}
}

function checkProduct(code)
{
	var products=[];
	products=getProducts();
	for(i=0;i<products.length;i++)
	{
		if(products[i]===code)
		{
			return true;
		}
	}
	return false;
}

function saveProducts(products)
{
	localStorage.setItem("products",JSON.stringify(products));
}
function getProducts()
{
	return JSON.parse(localStorage.getItem("products"));
}

function addOrRemove(value)
{
	if($("#"+value).is(':checked')){
		addProducts(value);
   }
    else
    {
    	removeProduct(value);;
     }
}
