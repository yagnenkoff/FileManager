<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="css/stylesheets/select.css">
    <link rel="stylesheet" href="css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <title>Folders</title>
    <style type="text/css">
        td {
            -ms-user-select: none;
            -moz-user-select: none;
            -khtml-user-select: none;
            -webkit-user-select: none;
        }
    </style>
</head>
<body>
<h2 class="text-center">Folders List</h2>
<div class="container" style="border: 2px solid black; padding: 10px;">
    <div class="row justify-content-around">
        <div class="col-md-9 col-sm-12">
            <div class="input-group">
                <input type="button" id="btn-back" value="Назад">
                <input class="form-control" type="text" name="path" id="path" value="root">
                <input type="button" id="btn-change-directory" value="Перейти">
            </div>
            <br/>
            <table class="table table-bordered" style="margin: auto;">
                <tbody id="sometable">
                <c:forEach var="folder" items="${folders}">
                    <tr id="${folder.id}">
                        <td><img src="images/3d_bookmarks_folder_20537.png" style="margin-right: 5px;">${folder.name}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="col-md-3 col-sm-12">
            <input type="button" class="btn btn-secondary btn-lg btn-block" id="btn-create" value="Создать">
            <input type="button" class="btn btn-secondary btn-lg btn-block btn-disabled" disabled="disabled" id="btn-rename" value="Переименовать">
            <input type="button" class="btn btn-secondary btn-lg btn-block btn-disabled" disabled="disabled" id="btn-delete" value="Удалить">
            <input type="button" class="btn btn-secondary btn-lg btn-block btn-disabled" disabled="disabled" id="btn-replace" value="Перенести">
        </div>
        <script src="js/filemanage.js"></script>
    </div>
</div>
</body>
</html>