function getBaseUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/reports/";
}


function search() {
    var $form = $("#vieworder-form");
    var json = toJson($form);
    json.startDate = json.startDate + 'T00:00:00+00:00';
    json.endDate = json.endDate + 'T23:59:00+00:00';
    if (((json.startDate == "") || (json.endDate == "")) && json.brand == "" && json.category == "") {
        $.notify("please provide a valid input", { autoHide: false });
    }
    var url = getBaseUrl() + "sales";
    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(json),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            console.log(response);
            writeFileData(response);
        },
        error: function (response) {
            $.notify(response['responseJSON']['message'], { autoHide: false });
        }
    });

}

function generateInventoryReport() {
    var url = getBaseUrl() + "inventory";
    $.ajax({
        url: url,
        type: 'GET',
        success: function (data) {
            console.log(data);
            writeFileData(data);
        },
        error: function () {
            alert("An error has occurred");
        }
    });

}

function generateBrandReport() {
    var url = $("meta[name=baseUrl]").attr("content") + "/api/brand";
    $.ajax({
        url: url,
        type: 'GET',
        success: function (data) {
            writeFileData(data);
        },
        error: function () {
            alert("An error has occurred");
        }
    });
}

function init() {
    $('#inventory-report').click(generateInventoryReport);
    $('#brand-report').click(generateBrandReport);
    $('#search-order').click(search);
    var date = new Date();
    document.getElementById('enddate').valueAsDate = date;
    date.setMonth(date.getMonth() - 1);
    document.getElementById('startdate').valueAsDate = date;
}

function toJson($form) {
    var serialized = $form.serializeArray();
    var s = '';
    var data = {};
    for (s in serialized) {
        data[serialized[s]['name']] = serialized[s]['value']
    }
    return data;
}

//write file functions 

function writeFileData(arr) {
    console.log(arr);
    var config = {
        quoteChar: '',
        escapeChar: '',
        delimiter: "\t"
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