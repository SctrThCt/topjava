const userAjaxUrl = "admin/users/";
// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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

function enable(checkbox,id)
{
    var enabled = checkbox.is(":checked");
    $.ajax({
        url: userAjaxUrl+id,
        type:"POST",
        data:"enabled="+enabled
    }).done(function(){
        checkbox.closest("tr").attr("data-user-enabled", enabled);
        successNoty(enabled ? "common.enabled":"common.disabled");
    }).fail(function ()
    {
        $(checkbox).prop("checked", !enabled);
    });
}
