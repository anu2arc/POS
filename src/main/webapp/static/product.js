function getBaseUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/product";
}

//BUTTON ACTIONS
function addProduct(event) {
    //Set the values to update
    var $form = $("#product-form");
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
            $.notify("Product added", "success");
            getProductList();
        },
        error: function (response) {
            console.log(json);
            $.notify(response['responseJSON']['message'], { autoHide: false });
        }
    });
}

function updateProduct(event) {
    var barcode = $("#product-edit-form input[name=barcode]").val();
    var url = getBaseUrl() + "/" + barcode;
    //Set the values to update
    var $form = $("#product-edit-form");
    var json = toJson($form);
    console.log(json);
    $.ajax({
        url: url,
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            $('#edit-product-modal').modal('toggle');
            getProductList(); //...
        },
        error: function (response) {
            $.notify(response['responseJSON']['message'], { autoHide: false });
        }
    });
    return false;
}


function getProductList() {
    var url = getBaseUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function (data) {
            console.log(data);
            displayProductList(data); //...
        },
        error: function (response) {
            $.notify("could not fetch product list", { autoHide: false });
        }
    });
}

//UI DISPLAY METHODS

function createtable(productdata, branddata) {
    var $tbody = $('#product-table').find('tbody');
    var button = 'type="button" class="btn btn-primary"';
    var buttonHtml = ' <button ' + button + ' onclick="displayEditProduct(' + productdata.id + ')">Edit</button>';
    var row = '<tr>' +
        '<td>' + productdata.barcode + '</td>' +
        '<td>' + productdata.name + '</td>' +
        '<td>' + branddata.brand + '</td>' +
        '<td>' + branddata.category + '</td>' +
        '<td>' + productdata.mrp + '</td>' +
        '<td>' + buttonHtml + '</td>' +
        '</tr>';
    $tbody.append(row);
}

function displayProductList(data) {
    var $tbody = $('#product-table').find('tbody');
    $tbody.empty();
    for (var i in data) {
        var e = data[i];
        getBrandCategory(e);
    }
}

function getBrandCategory(productdata) {
    var url = $("meta[name=baseUrl]").attr("content") + "/api/brand" + "/" + productdata.brandCategory;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (branddata) {
            createtable(productdata, branddata);
        },
        error: function (response) {
            $.notify(response['responseJSON']['message'], { autoHide: false });
        }
    });
}

function displayEditProduct(id) {
    var url = getBaseUrl() + "/" + id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (data) {
            displayProduct(data);
        },
        error: function (response) {
            $.notify(response['responseJSON']['message'], { autoHide: false });
        }
    });
}

function displayProduct(data) {
    console.log(data);
    $("#product-edit-form input[name=barcode]").val(data.barcode);
    $("#product-edit-form input[name=fbarcode]").val(data.barcode);
    $("#product-edit-form input[name=name]").val(data.name);
    $("#product-edit-form input[name=mrp]").val(data.mrp);
    $('#edit-product-modal').modal('toggle');
    //    $("#product-edit-form input[name=brand_category]").val(data.brand_category);
    setEdit(data.brandCategory);
}

function setEdit(id) {
    console.log(id);
    var url = $("meta[name=baseUrl]").attr("content") + "/api/brand/" + id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (branddata) {
            setEditOptions(branddata);
        },
        error: function (response) {
            $.notify(response['responseJSON']['message'], { autoHide: false });
        }
    });
}

function setEditOptions(branddata) {
    $("#product-edit-form input[name=brand]").val(branddata.brand);
    $("#product-edit-form input[name=fbrand]").val(branddata.brand);
    $("#product-edit-form input[name=category]").val(branddata.category);
    $("#product-edit-form input[name=fcategory]").val(branddata.category);
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
    $('#add-product').click(addProduct);
    $('#update-product').click(updateProduct);
    $('#refresh-data').click(getProductList);
    $('#upload-data').click(displayUploadData);
    $('#process-data').click(processData);
    $('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName)
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData() {
    var file = $('#productFile')[0].files[0];
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
            getProductList();
            updateUploadDialog();
        },
        error: function (response) {
            var json = { message: response.responseJSON.message };
            errorData.push(json);
            processCount = fileData.length;
            $.notify("error please download error report", { autoHide: false });
            console.log("error");
            updateUploadDialog();
        }
    });
}

function downloadErrors() {
    writeFileData(errorData);
}

function resetUploadDialog() {
    //Reset file name
    var $file = $('#productFile');
    $file.val('');
    $('#productFileName').html("Choose File");
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
    var $file = $('#productFile');
    var fileName = $file.val();
    $('#productFileName').html(fileName);
}

function displayUploadData() {
    resetUploadDialog();
    $('#upload-Product-modal').modal('toggle');
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
$(document).ready(getProductList);
// $(document).ready(setBrandCategory);