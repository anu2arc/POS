var orderList = [];
var orderListData = [];
function getEmployeeUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/inventory";
}

function addOrder() {
    var $form = $("#placeorder-form");
    var json = toJsonObject($form);
    if (json['barcode'] == "") {
        $.notify("please provide a barcode", { autoHide: false });
    }
    else if (json['quantity'] == null || json['quantity'] <= 0) {
        $.notify("please provide a valid quantity", { autoHide: false });
    }
    else if (json['sellingprice'] <= 0 || json['sellingprice'] > 1000000) {
        $.notify("please provide a valid selling price", { autoHide: false });
    }
    else {
        var url = $("meta[name=baseUrl]").attr("content") + "/api/product/barcode" + "/" + json['barcode'];
        $.ajax({
            url: url,
            type: 'GET',
            success: function (productData) {
                fetchQuantity(productData, json)
            },
            error: function () {
                $.notify("Invalid barcode", { autoHide: false });
            }
        });
    }
}



function fetchQuantity(productData, json) {
    var url = $("meta[name=baseUrl]").attr("content") + "/api/inventory/barcode" + "/" + json['barcode'];
    $.ajax({
        url: url,
        type: 'GET',
        success: function (inventoryData) {
            frontEndValidate(productData, inventoryData, json)
        },
        error: function () {
            $.notify("Could not fetch brand and category", { autoHide: false });
        }
    });
}

function frontEndValidate(productData, inventoryData, json) {
    if (inventoryData['quantity'] < json['quantity'])
        $.notify('Order Quantity exceeded max limit', { autoHide: false });
    else {
        productData = Object.assign(productData, inventoryData);
        productData = Object.assign(productData, json);
        var flag = true;
        for (let i = 0; i < orderList.length; i++) {
            if (orderList[i].barcode === productData.barcode) {
                $.notify("Product already exists", { autoHide: false });
                flag = false;
                break;
            }
        }
        if (flag) {
            orderList.push(productData);
            orderListData.push(json);
            refresh();
        }
    }
}

function rowDelete(link, index) {
    var row = link.parentNode.parentNode;
    var table = row.parentNode;
    table.removeChild(row);
    orderList.splice(index, 1);
    orderListData.splice(index, 1);
    refresh();
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

function toJsonObject($form) {
    var serialized = $form.serializeArray();
    var s = '';
    var data = {};
    for (s in serialized) {
        data[serialized[s]['name']] = serialized[s]['value']
    }
    return data;
}

function refresh() {
    var table = document.getElementById("order-table");
    var $tbody = $('#order-table').find('tbody');
    $tbody.empty();
    for (let i = 0; i < orderList.length; i++) {
        var data = orderList[i];
        var row = '<tr>'
            + '<td>' + data['barcode'] + '</td>'
            + '<td>' + data['name'] + '</td>'
            + '<td>' + data['quantity'] + '</td>'
            + '<td>' + data['sellingprice'] + '</td>'
            + '<td>' + '<button type="button" class="btn btn-primary" onclick="rowDelete(this,' + i + '); return false;">delete</button>' + '</td>'
            + '</tr>';
        $tbody.append(row);
    }
    if (orderList.length > 0) {
        var button = `<tr><td><button type="submit" onclick="placeorder(); return false;" class="btn btn-primary">Submit</button></td></tr>`
        $tbody.append(button);
    }
    printer();
}

function placeorder() {
    var url = $("meta[name=baseUrl]").attr("content") + "/api/order-item";
    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(orderListData),
        contentType: 'application/json',
        success: function (response) {
            $.notify(response, "success", { autoHide: false });
            orderList = [];
            orderListData = [];
            refresh();
        },
        error: function (response) {
            $.notify(response, { autoHide: false });
        }
    });
}

function printer() {
    console.log(orderList);
    console.log(orderListData);
    console.log(JSON.stringify(orderListData));
}

//INITIALIZATION CODE
function init() {
    $('#add-order').click(addOrder);
    $('#refresh-data').click(refresh);

}

$(document).ready(init);