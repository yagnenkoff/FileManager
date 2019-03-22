//clean local storage
localStorage.removeItem('idFolder');
localStorage.removeItem('folderName');
localStorage.removeItem('parentFolder');

//default id dir
localStorage.setItem('idThisFolder', 1);
//ajax pool
let ajax;
//path this dir
let path = "root";

//double click
$('body').on('dblclick', 'tr', function(){
    let idFolder = $(this).closest('tr').attr('id');
    let nameFolder = null;
    $('img').attr('src', 'images/3d_bookmarks_folder_20537.png');
    $('.selected').each(function() {
        nameFolder = $(this).find('td').text();
    });
    var image = $(this).find('td').children[0];
    $(this).find('td').children().remove(image);
    $(this).find('td').prepend('<img src="images/126.gif" style="margin-right: 5px;">');
    ajax = $.ajax({
        url : 'open',
        type: 'POST',
        data : {
            id : idFolder
        },
        success : function(responseJson) {
            $('#sometable').empty();
            $.each(responseJson, function(index, folder) {
                $('#sometable').append( '<tr id="'+ folder.id +'"><td><img src="images/3d_bookmarks_folder_20537.png" style="margin-right: 5px;">' + folder.name + '</td></tr>' );
            });
            path+='/' + nameFolder;
            $('#path').val(path);
            localStorage.setItem('idThisFolder', idFolder);
            $('.btn-disabled').prop('disabled', true);
            if(localStorage.getItem('idFolder')!=null){
                $('#btn-replace').prop('disabled', false);
            }
        },
        beforeSend: function() {
            if (ajax) {
                ajax.abort();
            }
        }
    })
});

//button create click
$('body').on('click', '#btn-create', function(){
    let nameFolder = prompt('Введите название папки',"");
    $.ajax({
        url : 'create',
        type: 'POST',
        data : {
            name : nameFolder,
            parentId: localStorage.getItem('idThisFolder')
        },
        success:function(folderId){
            $('#sometable').append( '<tr id="'+ folderId +'"><td><img src="images/3d_bookmarks_folder_20537.png" style="margin-right: 5px;">' + nameFolder + '</td></tr>' );
        },
        error: function() {
            alert("Некорректный ввод имени");
        },
    });
});

//button rename click
$('body').on('click', '#btn-rename', function(){
    $('img').attr('src', 'images/3d_bookmarks_folder_20537.png');
    if (ajax) {
        ajax.abort();
    }
    let id = $('.selected').attr('id');
    let nameFolder = null;
    $('.selected').each(function() {
        nameFolder = $(this).find('td').text();
    });
    let newNameFolder = prompt('Введите новое название папки',nameFolder);
    if (newNameFolder!=null) {
        $.ajax({
            url: 'edit',
            type: 'PUT',
            data: {
                id: id,
                name: newNameFolder
            },
            success: function () {
                $('.selected').each(function () {
                    $(this).find('td').text(newNameFolder).prepend('<img src="images/3d_bookmarks_folder_20537.png" style="margin-right: 5px;">');
                });
                $('.btn-disabled').prop('disabled', true);
                if (localStorage.getItem('idFolder') != null) {
                    $('#btn-replace').prop('disabled', false);
                }
                $('tr').removeClass();
                },
            error: function () {
                alert("Некорректный ввод имени");
                },
            });
        }
});

//button delete click
$('body').on('click', '#btn-delete', function(){
    if (ajax) {
        ajax.abort();
    }
    $('img').attr('src', 'images/3d_bookmarks_folder_20537.png');
    let id = $('.selected').attr('id');
    $.ajax({
        url : 'delete',
        type: 'DELETE',
        data : {
            id : id
        },
        success:function(){
            document.getElementById(id).remove();
            $('.btn-disabled').prop('disabled', true);
            if(localStorage.getItem('idFolder')!=null){
                $('#btn-replace').prop('disabled', false);
            }
        },
    });
});

//button go to click
$('body').on('click', '#btn-change-directory', function(){
    $.ajax({
        url : 'path',
        type: 'GET',
        data : {
            path : $('#path').val()
        },
        success : function(responseJson) {
            // localStorage.setItem('idThisFolder', responseJson[0].parentId);
            $('#sometable').empty();
            $.each(responseJson, function(index, folder) {
                $('#sometable').append( '<tr id="'+ folder.id +'"><td><img src="images/3d_bookmarks_folder_20537.png" style="margin-right: 5px;">' + folder.name + '</td></tr>' );
            });
            $('.btn-disabled').prop('disabled', true);
            if(localStorage.getItem('idFolder')!=null){
                $('#btn-replace').prop('disabled', false);
            }
            path = $('#path').val();
        },
        error: function() {
            alert("Вы указали неправильный путь к папке");
        },
    });
});

//table click
$('body').on('click','tr', function () {
    $('tr').removeClass();
    $(this).addClass('selected');
    $('.btn-disabled').prop('disabled', false);
});

//button back click
$('body').on('click', '#btn-back', function() {
    if (path.indexOf('/') > -1) {
        let i = path.length - 1;
        while (path.charAt(i) != '/') {
            path = path.substring(0, path.length - 1);
            i--;
        }
        path = path.substring(0, path.length - 1);

        $('#path').val(path);
        $('#sometable').empty();
        $.ajax({
            url: 'path',
            type: 'GET',
            data: {
                path: path
            },
            success: function (responseJson) {
                localStorage.setItem('idThisFolder', responseJson[0].parentId);

                $.each(responseJson, function (index, folder) {
                    $('#sometable').append('<tr id="' + folder.id + '"><td><img src="images/3d_bookmarks_folder_20537.png" style="margin-right: 5px;">' + folder.name + '</td></tr>');
                });
                $('.btn-disabled').prop('disabled', true);
                if (localStorage.getItem('idFolder') != null) {
                    $('#btn-replace').prop('disabled', false);
                }
            }
        });
    } else {
        alert("Это корневая папка");
    }
});

//button relocate click
$('body').on('click', '#btn-replace', function() {
    if(localStorage.getItem('idFolder')!=null){
        if(localStorage.getItem('idThisFolder').localeCompare(localStorage.getItem('parentFolder'))!=0) {
            $.ajax({
                url: 'relocate',
                type: 'PUT',
                data: {
                    id: localStorage.getItem('idFolder'),
                    parentId: localStorage.getItem('idThisFolder')
                },
                success: function (name) {
                    $('#sometable').append('<tr id="' + localStorage.getItem('idFolder') + '"><td><img src="images/3d_bookmarks_folder_20537.png" style="margin-right: 5px;">' + name + '</td></tr>');
                    localStorage.removeItem('idFolder');
                    localStorage.removeItem('folderName');
                    localStorage.removeItem('parentFolder');
                    $('#btn-replace').val("Перенести");
                    $('#btn-replace').prop('disabled', true);
                },
                error: function() {
                    alert("Неправильный путь");
                },
            });
        } else{
            localStorage.removeItem('idFolder');
            localStorage.removeItem('folderName');
            localStorage.removeItem('parentFolder');
            $('#btn-replace').val("Перенести");
        }
    }
    else {
        let id = $('.selected').attr('id');
        localStorage.setItem('idFolder', id);
        let nameFolder = null;
        $('.selected').each(function() {
            nameFolder = $(this).find('td').text();
        });
        localStorage.setItem('folderName', nameFolder);
        localStorage.setItem('parentFolder', localStorage.getItem('idThisFolder'));
        $('#btn-replace').val("Вставить");
    }
});