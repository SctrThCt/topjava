const mealAjaxUrl = "profile/meals/";
// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable:filterByDateTime()
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function filterByDateTime()
{
    let filterForm = $('#filter');
    $.ajax({
        type:"GET",
        url:ctx.ajaxUrl+"filter",
        data: filterForm.serialize()

    }).done(function (data) {
        drawTableByData(data);
    });
}

function clearFilter()
{
    $("#filter")[0].reset();
    $.get(mealAjaxUrl,drawTableByData)
}