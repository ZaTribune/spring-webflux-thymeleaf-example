function getContent(id,url) {
    $(id).load(url);
    let navbar=$("#navbarResponsive");
    if (navbar.hasClass('show')){
        $('#btnNavbar').click();
        navbar.area_expanded=false;
        navbar.addClass('collapsing');
        navbar.removeClass('show');
    }
}

function resizeTextarea (id) {
    let a = document.getElementById(id);
    a.style.height = 'auto';
    a.style.height = a.scrollHeight+'px';
}

function init() {
    let a = document.getElementsByTagName('textarea');
    for(let i=0,inb=a.length;i<inb;i++) {
        if(a[i].getAttribute('data-resizable')==='true')
            resizeTextarea(a[i].id);
    }
}


function getBase64(input, preview, file) {
    let reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function () {
        input.attr('value', reader.result);
        preview.attr('src', reader.result);
    };
    reader.onerror = function (error) {
        console.log('Error: ', error);
    };
}

function deleteTableRow(table) {
    $('#' + table[0].id + ' tbody tr td input:checkbox:checked').each(function (index, element) {
        this.closest("tr").remove();
    });
    //reassign names --> example if we deleted #4
    //ingredients[3].id: 26
    //ingredients[3].amount:
    //ingredients[3].unitMeasure.id: 3
    //ingredients[3].description:
    //ingredients[5].id: 13 .........

    $('.' + 'ingredient-id').each(function (index, element) {
        element.setAttribute("id", 'ingredients' + index + '.id');
        element.setAttribute("name", 'ingredients[' + index + '].id');
    })
    $('.' + 'ingredient-amount').each(function (index, element) {
        element.setAttribute("name", 'ingredients[' + index + '].amount');
        element.setAttribute("id", 'ingredients' + index + '.amount');
    })
    $('.' + 'ingredient-unitMeasure-id').each(function (index, element) {
        element.setAttribute("name", 'ingredients[' + index + '].unitMeasure.id');
        element.setAttribute("id", 'ingredients' + index + '.unitMeasure.id');
    })
    $('.' + 'ingredient-unitMeasure-description').each(function (index, element) {
        element.setAttribute("name", 'ingredients[' + index + '].unitMeasure.description');
        element.setAttribute("id", 'ingredients' + index + '.unitMeasure.description');
    })
    $('.' + 'ingredient-description').each(function (index, element) {
        element.setAttribute("name", 'ingredients[' + index + '].description');
        element.setAttribute("id", 'ingredients' + index + '.description');
    })
}

function insertCategory(parentId, item) {
    let invalid = false;
    let parent = document.getElementById(parentId);
    Array.from(parent.children).forEach((child, index) => {
        if (item.getAttribute("data-category-id") === child.getAttribute("data-category-id")) {
            invalid = true;
            return false;
        }
    });
    if (invalid === true)
        return;

    item.setAttribute('class', 'list-group-item d-flex justify-content-between p-1 align-items-center text-dark');
    item.setAttribute('onclick', '');
    //alert("parentID:"+parentId+"\nitemId:"+itemId+"\nitemText"+itemText);
    let btnDelete = document.createElement('button');
    item.appendChild(btnDelete);
    //must have a parent before setting outerHTML
    btnDelete.outerHTML = "<button class='btn btn-secondary' onclick='this.closest(\"a\").remove()'><i class='fa fa-trash p-1'></i></button>";
    parent.appendChild(item);
}

function insertIngredient() {
    let select = document.createElement("select");// Create with DOM
    for (let i = 0; i < unitMeasures.length; i++) {
        select.innerHTML += '<option value=' + unitMeasures[i]["id"] + '>' + unitMeasures[i]["description"] + '</option>';
    }

    let table = $('#ingredientsTable tbody');
    //the array counts from 0 But!!! there's a header
    //we need it to insert on the end
    let rowCount = $('#ingredientsTable tr').length - 1;
    select.setAttribute('class', "custom-select mr-sm-2")
    select.setAttribute('name', "ingredients[" + rowCount + "].unitMeasure.id")
    select.setAttribute('value', "ingredients[" + rowCount + "].unitMeasure.id")
    select.setAttribute('onchange', "selectUnitMeasure(this,this.parentElement.lastElementChild)")
    table.append('<tr>' +
        '<td class="p-0" style="vertical-align: middle"><input class="form-check-input me-1" type="checkbox">' +
        '<input type="hidden" name="ingredients[' + rowCount + '].id">' +
        '</td>' +
        '<td class="p-0"><input name="ingredients[' + rowCount + '].amount"></td>' +
        '<td class="p-0" style="vertical-align: middle">' +
        select.outerHTML +
        '<input type="hidden" class="ingredient-unitMeasure-description" ' +
        'name="ingredients[' + rowCount + '].unitMeasure.description">' +
        '</td>' +
        '<td class="p-0"><input name="ingredients[' + rowCount + '].description" ' +
        'type="text">' +
        '</td>' +
        '</tr>')
}

function selectUnitMeasure(select, unitMeasureDescriptionField) {
    unitMeasureDescriptionField.setAttribute('value', select.options[select.selectedIndex].text)
}

function closeAllLists() {
    $('#categoriesListContainer').empty()
}



function sendData(recipeId) {
    //let myFile = $('#image').prop('files');
    //console.log(typeof myFile);

    let selectedCategories = $('#selectedCategories');
    selectedCategories.children('a').each(function (index, element) {
        $('<input />').attr('type', 'hidden')
            .attr('name', 'categories[' + index + '].id')
            .attr('value', element.getAttribute('data-category-id'))
            .appendTo('#createRecipeForm');
        $('<input />').attr('type', 'hidden')
            .attr('name', 'categories[' + index + '].description')
            .attr('value', element.getAttribute('data-category-description'))
            .appendTo('#createRecipeForm');
    });
    $('.' + 'ingredient-unitMeasure-description').each(function (index, element) {
        element.setAttribute("value", unitMeasures[element.previousElementSibling.selectedIndex]["description"]);
    })
    $.ajax(
        {
            //don't add contentType: false,processData: false,
            type: "POST",
            data: $("#createRecipeForm").serialize(),
            cache: false,
            url: "/updateOrSaveRecipe",
            success: function (data) {
                $('#recipeTabContent').html(data);
            },
            error: function (data) {
                alert("Error - Data not saved");
            }
        });
}

function confirmDialog(modalType, okActionURL, laterFunction) {
    $('#modalOverlay').load('/modal/' + modalType, function () {
        let myModel = $('#myModal');
        myModel.on('hidden.bs.modal', function () {
            this.remove();
            // to remove the modal
        })
        myModel.modal('show');
        $('#btnOkDialog').click(function () {
            $.ajax(
                {
                    type: "GET",
                    data: null,//data to be sent
                    cache: false,
                    url: okActionURL,
                    success: function (data) {
                        //after
                        myModel.modal('hide');
                        laterFunction();
                    },
                    error: function () {
                        //error
                    }
                });
        })
    });

}



