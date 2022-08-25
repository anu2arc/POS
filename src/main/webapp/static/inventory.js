
function getBaseUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}
//BUTTON ACTIONS

function addInventory(event) {
	var $form = $("#inventory-form");
	var json = toJson($form);
	var barcode = document.getElementById('inputbarcode').value;
	if (barcode == null || barcode == "")
		$.notify("please enter a valid barcode", { autoHide: false });
	else
		addWithBarcode(barcode, json);
}

function addWithBarcode(barcode, json) {
	var url = $("meta[name=baseUrl]").attr("content") + "/api/product/barcode" + "/" + barcode;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (productData) {
			addInventoryData(productData.id, json)
		},
		error: function (response) {
			$.notify(response['responseJSON']['message'], { autoHide: false });
		}
	});
}

function addInventoryData(id, json) {
	//Set the values to update
	var url = getBaseUrl() + '/' + id;
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			$.notify("Inventory added", "success");
			getInventory();     //...
		},
		error: function (response) {
			$.notify(response['responseJSON']['message'], { autoHide: false });
		}
	});
	return false;
}

function updateInventory(event) {
	$('#edit-brand-modal').modal('toggle');
	var barcode = document.getElementById('barcode').value;
	var $form = $("#product-edit-form");
	var json = toJson($form);
	addWithBarcode(barcode, json);
}

function getInventory() {
	var url = getBaseUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayInventory(data);     //...
		},
		error: function (response) {
			$.notify(response['responseJSON']['message'], { autoHide: false });
		}
	});
}


//UI DISPLAY METHODS

function displayInventory(data) {
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();

	for (var i in data) {
		var e = data[i];
		getProductData(e);
	}
}

function getProductData(iData) {
	var url = $("meta[name=baseUrl]").attr("content") + "/api/product" + '/' + iData.id;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (pData) {
			displayInventoryData(pData, iData); //...
		},
		error: function (response) {
			$.notify(response['responseJSON']['message'], { autoHide: false });
		}
	});
}

function displayInventoryData(pData, iData) {
	var button = 'type="button" class="btn btn-primary"';
	var $tbody = $('#product-table').find('tbody');
	var buttonHtml = ' <button ' + button + ' onclick="displayEditInventory(\'' + pData.barcode + '\')">Edit</button>';
	var row = '<tr>'
		+ '<td>' + pData.barcode + '</td>'
		+ '<td>' + pData.name + '</td>'
		+ '<td>' + iData.quantity + '</td>'
		+ '<td>' + pData.mrp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
	$tbody.append(row);
}

function displayEditInventory(barcode) {
	$('#edit-product-modal').modal('toggle');
	$("#product-edit-form input[name=barcode]").val(barcode);
	$("#product-edit-form input[name=fbarcode]").val(barcode);
}

function fetchInventory(data) {
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-product-modal').modal('toggle');
}


//HELPER METHOD
function toJson($form) {
	var serialized = $form.serializeArray();
	var s = '';
	var data = {};
	for (s in serialized) {
		data[serialized[s]['name']] = serialized[s]['value']
	}
	var json = JSON.stringify(data);
	return json;
}


//INITIALIZATION CODE
function init() {
	$('#add-inventory').click(addInventory);
	$('#update-product').click(updateInventory);
	$('#refresh-data').click(getInventory);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
	$('#employeeFile').on('change', updateFileName)
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData() {
	var file = $('#employeeFile')[0].files[0];
	console.log(file);
	if (file == undefined) {
		document.getElementById("emptyline").innerHTML = "*Please select a file";
	}
	else {
		document.getElementById("emptyline").innerHTML = "";
		readFileData(file, readFileDataCallback);
	}
}

function readFileDataCallback(results) {
	fileData = results.data;
	uploadRows();
}

function uploadRows() {
	//Process next row

	var json = JSON.stringify(fileData);
	var url = getBaseUrl() + "/bulk-add";
	//Make ajax call
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			$.notify("Upload Successful", "success");
			getInventory();
			updateUploadDialog();
		},
		error: function (response) {
			var json = { message: response.responseJSON.message };
			errorData.push(json);
			processCount = fileData.length;
			$.notify("error please download error report", { autoHide: false });
			updateUploadDialog();
		}
	});
}

function downloadErrors() {
	console.log(errorData);
	writeFileData(errorData);
}

function resetUploadDialog() {
	//Reset file name
	var $file = $('#employeeFile');
	$file.val('');
	$('#employeeFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog() {
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	if (errorData.length > 0)
		document.getElementById('download-errors').disabled = false;
	else
		document.getElementById('download-errors').disabled = true;

}

function updateFileName() {
	var $file = $('#employeeFile');
	var fileName = $file.val();
	$('#employeeFileName').html(fileName);
}

function displayUploadData() {
	resetUploadDialog();
	$('#upload-employee-modal').modal('toggle');
}

function readFileData(file, callback) {
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function (results) {
			callback(results);
		}
	}
	Papa.parse(file, config);
}

function writeFileData(arr) {
	console.log(arr);
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\n"
	};

	var data = Papa.unparse(arr, config);
	var blob = new Blob([data], { type: 'text/tsv;charset=utf-8;' });
	var fileUrl = null;

	if (navigator.msSaveBlob) {
		fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
	} else {
		fileUrl = window.URL.createObjectURL(blob);
	}
	var tempLink = document.createElement('a');
	tempLink.href = fileUrl;
	tempLink.setAttribute('download', 'download.tsv');
	tempLink.click();
}

$(document).ready(init);
$(document).ready(getInventory);

