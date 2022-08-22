function getBaseUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addBrandCategory(event) {
	//Set the values to update
	var $form = $("#brand-form");
	var json = toJson($form);
	var url = getBaseUrl();

	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			$.notify(response, "success");
			getBrandList();     //...
		},
		error: function (response) {
			$.notify(response['responseJSON']['description'], { autoHide: false });
		}
	});

	return false;
}

function updateBrandCategory(event) {

	document.getElementById("emptyline").innerHTML = "a";
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBaseUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);
	$.ajax({
		url: url,
		type: 'PUT',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			$('#edit-brand-modal').modal('toggle');
			$.notify("Updated", "success");
			getBrandList();     //...
		},
		error: function (response) {
			$.notify(response['responseJSON']['description'], { autoHide: false });
		}
	});
	return false;
}


function getBrandList() {
	var url = getBaseUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayBrandList(data);     //...
		},
		error: function (response) {
			$.notify(response['responseJSON']['description'], { autoHide: false });
		}
	});
}

//UI DISPLAY METHODS

function displayBrandList(data) {
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	var count = 1;
	var button = 'type="button" class="btn btn-primary"';
	for (var i in data) {
		var e = data[i];
		var buttonHtml = ' <button ' + button + ' onclick="displayEditBrand(' + e.id + ')">Edit</button>';
		var row = '<tr>'
			+ '<td>' + count + '</td>'
			+ '<td>' + e.brand + '</td>'
			+ '<td>' + e.category + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
		$tbody.append(row);
		count += 1;
	}
}

function displayEditBrand(id) {
	var url = getBaseUrl() + "/" + id;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			displayBrand(data);     //...
		},
		error: function (response) {
			$.notify(response['responseJSON']['description'], { autoHide: false });
		}
	});
}

function displayBrand(data) {
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
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
	$('#add-brand').click(addBrandCategory);
	$('#update-brand').click(updateBrandCategory);
	$('#refresh-data').click(getBrandList);
	$('#upload-brand').click(displayUploadData);
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
	var json = JSON.stringify(fileData);
	var url = getBaseUrl() + "/bulk-add";
	console.log(json);
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			$.notify("Upload Successful", "success");
			getBrandList();
			updateUploadDialog();
		},
		error: function (response) {
			var json = { description: response.responseJSON.description };
			errorData.push(json);
			processCount = fileData.length;
			$.notify("Error please download error report", { autoHide: false });
			updateUploadDialog();
		}
	});
}

function downloadErrors() {
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
$(document).ready(getBrandList);
