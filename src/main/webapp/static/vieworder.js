function gatBaseUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/order";
}

function getFormattedDate(date) {
    var year = date.year;

    var month = (date.monthValue).toString();
    month = month.length > 1 ? month : '0' + month;

    var day = date.dayOfMonth.toString();
    day = day.length > 1 ? day : '0' + day;

    return day + '/' + month + '/' + year;
}

function setpage() {
    var url = gatBaseUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function (data) {
            displayOrderList(data);     //...
        },
        error: function (response) {
            $.notify(response['responseJSON']['message'], { autoHide: false });
        }
    });
}

function displayOrderList(data) {
    var $tbody = $('#order-table').find('tbody');
    $tbody.empty();
    var button = 'type="button" class="btn btn-primary"';
    for (let i = 0; i < data.length; i++) {
        date = getFormattedDate(data[i].time);
        var buttonHtml = '<button ' + button + 'onclick="viewOrder(' + data[i].id + ')">View</button>'
        buttonHtml += ' <button ' + button + ' onclick="invoice(' + data[i].id + ')">Invoice</button>'
        var row = '<tr>'
            + '<td>' + data[i].id + '</td>'
            + '<td>' + date + '</td>'
            + '<td>' + buttonHtml + '</td>'
            + '</tr>';
        $tbody.append(row);
    }
    var date = new Date();
    document.getElementById('enddate').valueAsDate = date;
    date.setMonth(date.getMonth() - 1);
    document.getElementById('startdate').valueAsDate = date;
}

function viewOrder(id) {
    var url = $("meta[name=baseUrl]").attr("content") + "/api/order/" + id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (data) {
            $('#edit-product-modal').modal('toggle');
            $('#viewtable').find('tbody').empty();
            console.log(data.time)

            var date = getFormattedDate(data.time);
            document.getElementById("vieworderid").value = data.id;
            document.getElementById("orderDate").value = date;
            setData(id);
        },
        error: function () {
            $.notify("Please provide a valid ID", { autoHide: false });
        }
    });
}

function setData(id) {
    var url = $("meta[name=baseUrl]").attr("content") + "/api/order-item/" + id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (data) {
            viewOrderDetails(data);
        },
        error: function (response) {
            $.notify(response['responseJSON']['message'], { autoHide: false });
        }
    });
}

function viewOrderDetails(oData) {
    for (let i = 0; i < oData.length; i++) {
        var url = $("meta[name=baseUrl]").attr("content") + "/api/product/" + oData[i]['productId'];
        $.ajax({
            url: url,
            type: 'GET',
            success: function (pData) {
                displaySelectedOrder(oData, pData, i); //...
            },
            error: function (response) {
                $.notify(response['responseJSON']['message'], { autoHide: false });
            }
        });
    }
}

function displaySelectedOrder(oData, pData, i) {
    var $table = $('#viewtable').find('tbody');
    var tablerow = `<tr>
        <td>${i + 1}</td>
        <td>${pData['name']}</td>
        <td>${oData[i]['quantity']}</td>
        <td>${oData[i]['sellingPrice']}</td>
        <td>${oData[i]['quantity'] * oData[i]['sellingPrice']}</td>
    </tr>`;
    $table.append(tablerow);
}

function search() {
    var id = document.getElementById("orderid").value;
    var startdate = document.getElementById("startdate").value;
    var enddate = document.getElementById("enddate").value;
    console.log(startdate);
    if ((id == "" || isNaN(id)) && (startdate == "" || enddate == ""))
        $.notify("please provice a valid input", { autoHide: false });
    else if (id != "" && !isNaN(id)) {
        viewOrder(id);
    }
    else if (startdate > enddate) {
        $.notify("Start Date cannot be greater than End Date", { autoHide: false });
    }
    else {
        var json = { "startDate": startdate + 'T00:00:00+00:00', "endDate": enddate + 'T23:59:00+00:00' };
        showForDateRange(json);
    }
}

function showForDateRange(json) {
    var url = $("meta[name=baseUrl]").attr("content") + "/api/order/range";
    console.log(JSON.stringify(json));
    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(json),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            displayOrderList(response);
        },
        error: function (response) {
            $.notify(response['responseJSON']['message'], { autoHide: false });
        }
    });
}

function download(url) {
    console.log("downloading...");
    const a = document.createElement('a')
    a.href = url
    a.setAttribute('download', 'invoice.pdf');
    // a.download = url.split('/').pop()
    // document.body.appendChild(a)
    a.click()
    // document.body.removeChild(a)
}

function invoice(id) {
    var url = $("meta[name=baseUrl]").attr("content") + "/api/order-item/invoice/" + id;
    $.ajax({
        url: url,
        xhr: function () {
            const xhr = new XMLHttpRequest();
            xhr.responseType = 'blob'
            return xhr;
        },
        type: 'GET',
        success: function (data) {
            var file = new Blob([data], { type: 'application/pdf' });
            var fileURL = URL.createObjectURL(file);
            setTimeout(() => {
                window.open(fileURL);
            })
        }
    });
}

//INITIALIZATION CODE
function init() {
    $('#refresh-data').click(setpage);
    $('#search-order').click(search);
}

$(document).ready(setpage);
$(document).ready(init);
