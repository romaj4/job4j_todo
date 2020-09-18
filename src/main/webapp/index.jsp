<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>TODO list</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script>
        function taskTable(data) {
            data.forEach(item => {
                if (item.done === false)
                    $('#tbody').append(
                        $('<tr class="addRow">')
                            .append(
                                $("<td class=\"id\">").text(item.id),
                                $("<td>").text(item.description),
                                $("<td>").text(item.created),
                                $("<td>").html("<input type=\"checkbox\">"),
                                $('<td>').text(item.user.name)))
            })
        }

        function doneTaskTable(data) {
            data.forEach(item => {
                $('#tbody').append(
                    $('<tr class="addRow">')
                        .append(
                            $("<td class=\"id\">").text(item.id),
                            $("<td>").text(item.description),
                            $("<td>").text(item.created),
                            item.done === true ? $("<td>").html("<input type=\"checkbox\" checked>") : $("<td>").html("<input type=\"checkbox\">"),
                            $('<td>').text(item.user.name)))
            })
        }

        function addItem() {
            var check = ($('#showAllTasks').is(':checked'));
            $.ajax({
                type: 'GET',
                url: 'http://localhost:8080/todo/additem',
                data: 'desc=' + $('#desc').val(),
                dataType: 'text'
            }).done(function () {
                $(".addRow").remove();
                $('#showAllTasks').prop('checked', check);
                check ? loadDoneTaskTable() : loadTaskTable();
            }).fail(function (err) {
                alert(err);
            });
        }

        function loadTaskTable() {
            $.ajax({
                type: 'GET',
                url: 'http://localhost:8080/todo/items',
                dataType: 'json'
            }).done(function (data) {
                taskTable(data)
            });
        }

        function loadDoneTaskTable() {
            $.ajax({
                type: 'GET',
                url: 'http://localhost:8080/todo/items',
                dataType: 'json'
            }).done(function (data) {
                doneTaskTable(data)
            });
        }

        $(document).on('click', ':checkbox:not(#showAllTasks)', function () {
            $.ajax({
                type: 'GET',
                url: 'http://localhost:8080/todo/donetask',
                data: {
                    id: $(this).closest("tr").find('.id').html(),
                    done: this.checked
                }
            })
        })

        $(document).on('keypress', function (e) {
            if (e.which == 13) {
                $("#addButton").click();
            }
        });

        $(document).ready(function () {
            $("#showAllTasks").click(function () {
                $(".addRow").remove();
                $(this).is(":checked") ? loadDoneTaskTable() : loadTaskTable();
            });
        });
    </script>
</head>
<body>
<script>
    window.onload = loadTaskTable();
</script>
<div class="container">
    <div class="row">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href='<%=request.getContextPath()%>/reg.jsp'>Регистрация</a>
            </li>
            <%if (session.getAttribute("user") == null) {%>
            <li class="nav-item">
                <a class="nav-link" href='<%=request.getContextPath()%>/login.jsp'>Войти</a>
            </li>
            <% } else {%>
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/auth?log=0">${user.name} | Выйти</a>
            </li>
            <%}%>
        </ul>
    </div>
    <form id="myForm" onsubmit="return false;">
        <fieldset class="mt-3">
            <div class="form-group">
                <legend>Добавить новое задание</legend>
                <label for="desc">Описание</label>
                <input type="desc" class="form-control" id="desc" placeholder="Введите описание задачи" name="desc"
                       autocomplete="off">
            </div>
            <button type="reset" class="btn btn-primary" id="addButton" onclick="addItem()">Добавить</button>
        </fieldset>
        <fieldset class="mt-5">
            <legend>Задачи</legend>
            <div class="col-3 ml-auto">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="showCheck" id="showAllTasks">
                    <label class="form-check-label" for="showAllTasks"> Показать все </label>
                </div>
            </div>
            <table class="table table-bordered" id="itemTable">
                <thead>
                <tr>
                    <th width="5%">Id</th>
                    <th width="40%">Задание</th>
                    <th width="30%">Дата создания</th>
                    <th width="10%">Статус</th>
                    <th width="15%">Автор</th>
                </tr>
                </thead>
                <tbody id="tbody"></tbody>
            </table>
        </fieldset>
    </form>
</div>
</body>
</html>