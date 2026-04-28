/* ==========================================
   Kitchen Master - App.js
   ========================================== */

// Cache for unit measures and categories
let cachedUnitMeasures = [];
let cachedCategories = [];

// Pagination state
let currentRecipePage = 0;
let currentRecipePageSize = 12;
let currentCategoryPage = 0;
let currentCategoryPageSize = 10;
let currentSearch = '';
let currentDifficulty = '';

     // Track if we're on the last page
let isLastRecipePage = false;
let isLastCategoryPage = false;

document.addEventListener('DOMContentLoaded', function() {
    _authCache = null; // Reset auth cache on page load
    handleRouting();
});

/**
 * Main routing handler
 */
function handleRouting() {
    const path = window.location.pathname;

    // Update navbar auth state
    updateNavbarAuth();

    if (path === '/' || path === '/index') {
        showHome();
    } else if (path === '/recipes') {
        showRecipes();
    } else if (path === '/categories') {
        showCategories();
    }
}

/**
 * Update navbar elements based on authentication status
 */
function updateNavbarAuth() {
    checkAuthentication(function(authenticated, data) {
        if (authenticated) {
            $('#navSignIn').hide();
            $('#navNewRecipe').show();
            $('#navLogout').show();
            if (data.username) {
                $('#navUsername').text(data.username);
                $('#navUserInfo').show();
            }
        } else {
            $('#navSignIn').show();
            $('#navNewRecipe').hide();
            $('#navLogout').hide();
            $('#navUserInfo').hide();
        }
    });
}

/**
 * Show home page
 */
function showHome() {
    loadFeaturedRecipes();
}

/**
 * Load featured recipes
 */
function loadFeaturedRecipes() {
    $.ajax({
        url: '/api/recipes/featured',
        method: 'GET',
        success: function(recipes) {
            if (recipes && recipes.length > 0) {
                let html = '';
                recipes.slice(0, 3).forEach(recipe => {
                    html += createRecipeCard(recipe);
                });
                $('#recipesContainer').html(html);
            }
        },
        error: function() {
            // Silently fail - featured section is optional
        }
    });
}

/**
 * Show recipes page
 */
function showRecipes() {
    // Change URL without reload
    window.history.pushState({}, '', '/recipes');

    $('#mainContent').html(`
         <div>
             <div class="d-flex justify-content-between align-items-center mb-4">
                 <h2><i class="fas fa-utensils"></i> Recipes</h2>
                 <button class="btn btn-success" id="addRecipeBtn" onclick="showRecipeForm()" style="display: none;">
                     <i class="fas fa-plus"></i> Add New Recipe
                 </button>
             </div>

             <div class="row g-3 mb-4">
                 <div class="col-md-8">
                     <input type="search" class="form-control" id="recipeSearch" placeholder="Search recipes...">
                 </div>
                 <div class="col-md-4">
                     <select class="form-select" id="recipeDifficultyFilter">
                         <option value="">All Difficulty Levels</option>
                         <option value="EASY">Easy</option>
                         <option value="MODERATE">Moderate</option>
                         <option value="HARD">Hard</option>
                     </select>
                 </div>
             </div>

             <div id="recipesGrid" class="row g-3">
                 <div class="col-12 text-center py-5">
                     <div class="spinner-border text-primary" role="status">
                         <span class="visually-hidden">Loading...</span>
                     </div>
                 </div>
             </div>

              <!-- Pagination Controls -->
              <nav aria-label="Recipe pagination" class="mt-4">
                  <ul class="pagination justify-content-center">
                      <li class="page-item" id="recipePrevBtn"><a class="page-link" href="#" onclick="previousRecipePage(); return false;">Previous</a></li>
                      <li class="page-item active"><span class="page-link" id="currentRecipePage">Page 1</span></li>
                      <li class="page-item" id="recipeNextBtn"><a class="page-link" href="#" onclick="nextRecipePage(); return false;">Next</a></li>
                  </ul>
              </nav>
         </div>
     `);

    // Check if authenticated and show/hide add button
    checkAuthentication(function(authenticated) {
        if (authenticated) {
            $('#addRecipeBtn').show();
        }
    });

    // Load all recipes
    loadAllRecipes();

    // Setup search and filter
    $('#recipeSearch').on('keyup', function() {
        filterAndLoadRecipes();
    });

    $('#recipeDifficultyFilter').on('change', function() {
        filterAndLoadRecipes();
    });

    // Prevent clicks on disabled pagination buttons
    $(document).on('click', '#recipePrevBtn.disabled, #recipeNextBtn.disabled', function(e) {
        e.preventDefault();
        e.stopPropagation();
        return false;
    });
}

/**
 * Load all recipes
 */
function loadAllRecipes() {
    console.log(`loadAllRecipes: page=${currentRecipePage}, pageSize=${currentRecipePageSize}`);
    $.ajax({
        url: '/api/recipes',
        method: 'GET',
        data: {
            page: currentRecipePage,
            size: currentRecipePageSize
        },
        success: function(recipes) {
            console.log(`API returned ${recipes ? recipes.length : 0} recipes`);
            displayRecipes(recipes);
            updateRecipePageIndicator();
        },
        error: function() {
            console.error('Error loading recipes');
            $('#recipesGrid').html(`
                <div class="col-12">
                    <div class="alert alert-danger" role="alert">
                        <i class="fas fa-exclamation-circle"></i> Error loading recipes
                    </div>
                </div>
            `);
        }
    });
}

/**
 * Filter and load recipes
 */
function filterAndLoadRecipes() {
    const search = $('#recipeSearch').val();
    const difficulty = $('#recipeDifficultyFilter').val();

    currentSearch = search;
    currentDifficulty = difficulty;
    currentRecipePage = 0; // Reset to first page when filtering

    $.ajax({
        url: '/api/recipes/search',
        method: 'GET',
        data: {
            search: search,
            difficulty: difficulty,
            page: currentRecipePage,
            size: currentRecipePageSize
        },
        success: function(recipes) {
            displayRecipes(recipes);
            updateRecipePageIndicator();
        }
    });
}

/**
 * Display recipes grid
 */
function displayRecipes(recipes) {
    // Track if this is the last page
    // It's the last page if:
    // 1. No recipes returned, OR
    // 2. Got fewer items than requested (pageSize)
    // ALSO: If we're on page 0 and got MORE than pageSize items, API isn't paginating
    // In that case, we're always on the last (and only) page
    const isApiNotPaginating = currentRecipePage === 0 && recipes && recipes.length > currentRecipePageSize;
    isLastRecipePage = !recipes || recipes.length < currentRecipePageSize || isApiNotPaginating;

    // Debug: log the state
    console.log(`Recipes: ${recipes ? recipes.length : 0}, PageSize: ${currentRecipePageSize}, IsLastPage: ${isLastRecipePage}, CurrentPage: ${currentRecipePage}, ApiNotPaginating: ${isApiNotPaginating}`);

    if (!recipes || recipes.length === 0) {
        $('#recipesGrid').html(`
            <div class="col-12">
                <div class="empty-state">
                    <i class="fas fa-search"></i>
                    <h5>No recipes found</h5>
                    <p>Try adjusting your search filters or browse our collection</p>
                </div>
            </div>
        `);
        updateRecipePageIndicator();
        return;
    }

    let html = '';
    recipes.forEach(recipe => {
        html += createRecipeCard(recipe);
    });
    $('#recipesGrid').html(html);

    // Attach click handlers
    $('.btn-view-recipe').on('click', function(e) {
        e.preventDefault();
        const recipeId = $(this).data('recipe-id');
        showRecipeDetail(recipeId);
    });
}

/**
 * Create recipe card HTML
 */
function createRecipeCard(recipe) {
    const imageSrc = recipe.image || '/images/camera.svg';
    const difficulty = recipe.difficulty || 'Easy';
    const difficultyClass = recipe.difficulty === 'EASY' ? 'success' :
                           recipe.difficulty === 'MODERATE' ? 'warning' : 'danger';

    return `
        <div class="col-md-6 col-lg-4 fade-in">
            <div class="card recipe-card">
                <img src="${imageSrc}" class="recipe-card-image" alt="${recipe.title}">
                <div class="card-body recipe-card-body">
                    <h5 class="recipe-card-title">${recipe.title}</h5>
                    <div class="recipe-card-meta">
                        <i class="fas fa-clock"></i> Prep: ${recipe.prepTime} min
                    </div>
                    <div class="recipe-card-meta">
                        <i class="fas fa-fire"></i> Cook: ${recipe.cookTime} min
                    </div>
                    <div class="recipe-card-meta">
                        <span class="badge bg-${difficultyClass}">${difficulty}</span>
                    </div>
                    <div class="recipe-card-footer">
                        <button class="btn btn-sm btn-view-recipe btn-primary" data-recipe-id="${recipe.id}">
                            <i class="fas fa-eye"></i> View
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `;
}

/**
 * Show recipe detail in modal
 */
function showRecipeDetail(recipeId) {
    $.ajax({
        url: `/api/recipes/${recipeId}`,
        method: 'GET',
        success: function(recipe) {
            displayRecipeDetailModal(recipe);
        },
        error: function() {
            alert('Error loading recipe details');
        }
    });
}

/**
 * Display recipe detail modal
 */
function displayRecipeDetailModal(recipe) {
    const imageSrc = recipe.image || '/images/camera.svg';
    const difficulty = recipe.difficulty || 'Easy';
    const difficultyClass = recipe.difficulty === 'EASY' ? 'success' : 
                           recipe.difficulty === 'MODERATE' ? 'warning' : 'danger';

    let ingredientsList = '<ul class="recipe-ingredients-list">';
    if (recipe.ingredients && recipe.ingredients.length > 0) {
        recipe.ingredients.forEach(ing => {
            ingredientsList += `<li>${ing.amount} ${ing.unitMeasure?.description || ''} ${ing.description}</li>`;
        });
    }
    ingredientsList += '</ul>';

    const categoriesHtml = recipe.categories && recipe.categories.length > 0
        ? '<div>' + recipe.categories.map(c => `<span class="badge bg-info">${c.description}</span>`).join(' ') + '</div>'
        : '<div class="text-muted">No categories</div>';

    const detailBody = `
        <img src="${imageSrc}" class="recipe-detail-image" alt="${recipe.title}">
        
        <div class="recipe-detail-info">
            <div class="recipe-detail-item">
                <i class="fas fa-hourglass-start"></i>
                <span><strong>Prep:</strong> ${recipe.prepTime} min</span>
            </div>
            <div class="recipe-detail-item">
                <i class="fas fa-fire"></i>
                <span><strong>Cook:</strong> ${recipe.cookTime} min</span>
            </div>
            <div class="recipe-detail-item">
                <i class="fas fa-signal"></i>
                <span><strong>Difficulty:</strong> <span class="badge bg-${difficultyClass}">${difficulty}</span></span>
            </div>
            <div class="recipe-detail-item">
                <i class="fas fa-users"></i>
                <span><strong>Servings:</strong> ${recipe.servings}</span>
            </div>
        </div>

        <div class="recipe-detail-section">
            <h6><i class="fas fa-tag"></i> Categories</h6>
            ${categoriesHtml}
        </div>

        <div class="recipe-detail-section">
            <h6><i class="fas fa-list"></i> Ingredients</h6>
            ${ingredientsList}
        </div>

        <div class="recipe-detail-section">
            <h6><i class="fas fa-directions"></i> Directions</h6>
            <p>${recipe.directions ? recipe.directions.replace(/\n/g, '<br>') : 'No directions provided'}</p>
        </div>

        ${recipe.notes && recipe.notes.description ? `
        <div class="recipe-detail-section">
            <h6><i class="fas fa-sticky-note"></i> Notes</h6>
            <p>${recipe.notes.description}</p>
        </div>
        ` : ''}
    `;

    let footerHtml = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>`;
    
    // Add edit/delete buttons if authenticated
    checkAuthentication(function(authenticated) {
        if (authenticated) {
            footerHtml += ` 
                <button type="button" class="btn btn-warning" onclick="editRecipe('${recipe.id}')">
                    <i class="fas fa-edit"></i> Edit
                </button>
                <button type="button" class="btn btn-danger" onclick="deleteRecipe('${recipe.id}')">
                    <i class="fas fa-trash"></i> Delete
                </button>
            `;
        }
        $('#recipeDetailFooter').html(footerHtml);
    });

    $('#recipeTitle').text(recipe.title);
    $('#recipeDetailBody').html(detailBody);
    
    const modal = new bootstrap.Modal(document.getElementById('recipeDetailModal'));
    modal.show();
}

/**
 * Show categories page
 */
function showCategories() {
    window.history.pushState({}, '', '/categories');

    $('#mainContent').html(`
        <div>
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="fas fa-list"></i> Categories</h2>
                <a href="/createCategory" class="btn btn-success" id="addCategoryBtn" style="display: none;">
                    <i class="fas fa-plus"></i> Add Category
                </a>
            </div>

            <div id="categoriesGrid" class="row g-3">
                <div class="col-12 text-center py-5">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                </div>
            </div>

             <!-- Pagination Controls -->
             <nav aria-label="Category pagination" class="mt-4">
                 <ul class="pagination justify-content-center">
                     <li class="page-item" id="categoryPrevBtn"><a class="page-link" href="#" onclick="previousCategoryPage(); return false;">Previous</a></li>
                     <li class="page-item active"><span class="page-link" id="currentCategoryPage">Page 1</span></li>
                     <li class="page-item" id="categoryNextBtn"><a class="page-link" href="#" onclick="nextCategoryPage(); return false;">Next</a></li>
                 </ul>
             </nav>
        </div>
    `);

    // Check if admin and show/hide add button
    checkAuthentication(function(authenticated) {
        // For now, hide - should check for ADMIN role
        // $('#addCategoryBtn').show();
    });

    loadAllCategories();

    // Prevent clicks on disabled pagination buttons
    $(document).on('click', '#categoryPrevBtn.disabled, #categoryNextBtn.disabled', function(e) {
        e.preventDefault();
        e.stopPropagation();
        return false;
    });
}

/**
 * Load all categories
 */
function loadAllCategories() {
    $.ajax({
        url: '/api/categories',
        method: 'GET',
        data: {
            page: currentCategoryPage,
            size: currentCategoryPageSize
        },
        success: function(categories) {
            displayCategories(categories);
            updateCategoryPageIndicator();
        },
        error: function() {
            $('#categoriesGrid').html(`
                <div class="col-12">
                    <div class="alert alert-danger" role="alert">
                        <i class="fas fa-exclamation-circle"></i> Error loading categories
                    </div>
                </div>
            `);
        }
    });
}

/**
 * Display categories grid
 */
function displayCategories(categories) {
    // Track if this is the last page
    // It's the last page if:
    // 1. No categories returned, OR
    // 2. Got fewer items than requested (pageSize)
    // ALSO: If we're on page 0 and got MORE than pageSize items, API isn't paginating
    // In that case, we're always on the last (and only) page
    const isApiNotPaginating = currentCategoryPage === 0 && categories && categories.length > currentCategoryPageSize;
    isLastCategoryPage = !categories || categories.length < currentCategoryPageSize || isApiNotPaginating;

    // Debug: log the state
    console.log(`Categories: ${categories ? categories.length : 0}, PageSize: ${currentCategoryPageSize}, IsLastPage: ${isLastCategoryPage}, CurrentPage: ${currentCategoryPage}, ApiNotPaginating: ${isApiNotPaginating}`);

    if (!categories || categories.length === 0) {
        $('#categoriesGrid').html(`
            <div class="col-12">
                <div class="empty-state">
                    <i class="fas fa-inbox"></i>
                    <h5>No categories available</h5>
                    <p>Check back soon!</p>
                </div>
            </div>
        `);
        updateCategoryPageIndicator();
        return;
    }

    let html = '';
    categories.forEach(category => {
        html += createCategoryCard(category);
    });
    $('#categoriesGrid').html(html);

    // Attach click handlers
    $('.btn-view-category').on('click', function(e) {
        e.preventDefault();
        const categoryId = $(this).data('category-id');
        showCategoryDetail(categoryId);
    });
}

/**
 * Create category card HTML
 */
function createCategoryCard(category) {
    const imageSrc = category.image ? `data:image/png;base64,${category.image}` : '/images/camera.svg';

    return `
        <div class="col-md-6 col-lg-4 fade-in">
            <div class="card category-card">
                <img src="${imageSrc}" class="category-card-image" alt="${category.description}">
                <div class="card-body category-card-body">
                    <h5 class="category-card-title">${category.description}</h5>
                    <p class="category-card-description">${category.info || 'No description'}</p>
                    <button class="btn btn-sm btn-view-category btn-primary mt-auto" data-category-id="${category.id}">
                        <i class="fas fa-eye"></i> View
                    </button>
                </div>
            </div>
        </div>
    `;
}

/**
 * Show category detail in modal
 */
function showCategoryDetail(categoryId) {
    $.ajax({
        url: `/api/categories/${categoryId}`,
        method: 'GET',
        success: function(category) {
            displayCategoryDetailModal(category);
        },
        error: function() {
            alert('Error loading category details');
        }
    });
}

/**
 * Display category detail modal
 */
function displayCategoryDetailModal(category) {
    const imageSrc = category.image ? `data:image/png;base64,${category.image}` : '/images/camera.svg';

    const detailBody = `
        <img src="${imageSrc}" class="recipe-detail-image" alt="${category.description}">
        <h6 class="mb-3"><i class="fas fa-info-circle"></i> About</h6>
        <p>${category.info || 'No description available'}</p>
    `;

    let footerHtml = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>`;

    // Add edit/delete buttons if admin
    checkAuthentication(function(authenticated) {
        // if (authenticated && isAdmin) {
        //     footerHtml += `
        //         <a href="/updateCategory/${category.id}" class="btn btn-warning">
        //             <i class="fas fa-edit"></i> Edit
        //         </a>
        //         <button type="button" class="btn btn-danger" onclick="deleteCategory('${category.id}')">
        //             <i class="fas fa-trash"></i> Delete
        //         </button>
        //     `;
        // }
        $('#categoryDetailFooter').html(footerHtml);
    });

    $('#categoryTitle').text(category.description);
    $('#categoryDetailBody').html(detailBody);

    const modal = new bootstrap.Modal(document.getElementById('categoryDetailModal'));
    modal.show();
}

/**
 * Delete recipe
 */
function deleteRecipe(recipeId) {
    if (!confirm('Are you sure you want to delete this recipe? This action cannot be undone.')) {
        return;
    }

    $.ajax({
        url: `/api/recipes/${recipeId}`,
        method: 'DELETE',
        success: function(response) {
            if (response.success) {
                bootstrap.Modal.getInstance(document.getElementById('recipeDetailModal')).hide();
                alert('Recipe deleted successfully');
                location.reload();
            } else {
                alert('Error: ' + response.message);
            }
        },
        error: function() {
            alert('Error deleting recipe');
        }
    });
}

/**
 * Check if user is authenticated via API
 */
let _authCache = null;

function checkAuthentication(callback) {
    if (_authCache !== null) {
        if (typeof callback === 'function') {
            callback(_authCache.authenticated, _authCache);
        }
        return _authCache.authenticated;
    }

    $.ajax({
        url: '/api/auth/status',
        method: 'GET',
        success: function(data) {
            _authCache = data;
            if (typeof callback === 'function') {
                callback(data.authenticated, data);
            }
        },
        error: function() {
            _authCache = { authenticated: false };
            if (typeof callback === 'function') {
                callback(false, _authCache);
            }
        }
    });

    return false;
}

/**
 * API endpoints - these would be created in the controller
 */

// GET /api/recipes - Get all recipes
// GET /api/recipes/featured - Get featured recipes (first 3)
// GET /api/recipes/{id} - Get specific recipe
// GET /api/recipes/filter?search=&difficulty= - Filter recipes
// POST /api/recipes - Create recipe
// PUT /api/recipes/{id} - Update recipe
// DELETE /api/recipes/{id} - Delete recipe

// GET /api/categories - Get all categories
// GET /api/categories/{id} - Get specific category
// POST /api/categories - Create category
// PUT /api/categories/{id} - Update category
// DELETE /api/categories/{id} - Delete category


// ==========================================
// Recipe Image Handling
// ==========================================

/**
 * Preview selected recipe image and convert to base64
 */
function previewRecipeImage(input) {
    if (input.files && input.files[0]) {
        const file = input.files[0];

        // Check file size (warn if > 2MB)
        if (file.size > 2 * 1024 * 1024) {
            if (!confirm('This image is larger than 2MB. Large images may slow down the app. Continue?')) {
                input.value = '';
                return;
            }
        }

        const reader = new FileReader();
        reader.onload = function(e) {
            const base64 = e.target.result; // "data:image/...;base64,..."
            $('#recipeImageBase64').val(base64);
            $('#recipeImagePreviewImg').attr('src', base64);
            $('#recipeImagePreview').show();
        };
        reader.readAsDataURL(file);
    }
}

/**
 * Clear selected recipe image
 */
function clearRecipeImage() {
    $('#recipeImageInput').val('');
    $('#recipeImageBase64').val('');
    $('#recipeImagePreviewImg').attr('src', '');
    $('#recipeImagePreview').hide();
}

// ==========================================
// Recipe Form (Add / Edit)
// ==========================================

/**
 * Show recipe form modal for creating or editing
 */
function showRecipeForm(recipeId) {
    // Load unit measures and categories first - load all for form (no pagination needed)
    Promise.all([
        $.ajax({ url: '/api/unit-measures', method: 'GET' }),
        $.ajax({ url: '/api/categories?page=0&size=100', method: 'GET' })  // Load up to 100 categories for form
    ]).then(function([unitMeasures, categories]) {
        cachedUnitMeasures = unitMeasures || [];
        cachedCategories = categories || [];

        // Reset form
        $('#recipeForm')[0].reset();
        $('#recipeId').val('');
        $('#ingredientsList').empty();
        $('#recipeFormTitleText').text('New Recipe');
        clearRecipeImage();

        // Build category checkboxes
        let catHtml = '';
        cachedCategories.forEach(cat => {
            catHtml += `
                <div class="form-check">
                    <input class="form-check-input category-checkbox" type="checkbox" 
                           value="${cat.id}" id="cat-${cat.id}">
                    <label class="form-check-label" for="cat-${cat.id}">${cat.description}</label>
                </div>
            `;
        });
        $('#categoriesCheckboxes').html(catHtml);

        // Add one empty ingredient row
        addIngredientRow();

        if (recipeId) {
            // Edit mode - load existing recipe
            loadRecipeForEdit(recipeId);
        } else {
            // Show modal
            new bootstrap.Modal(document.getElementById('recipeFormModal')).show();
        }
    }).catch(function() {
        alert('Error loading form data. Please try again.');
    });
}

/**
 * Load recipe data into form for editing
 */
function loadRecipeForEdit(recipeId) {
    $.ajax({
        url: `/api/recipes/${recipeId}`,
        method: 'GET',
        success: function(recipe) {
            $('#recipeFormTitleText').text('Edit Recipe');
            $('#recipeId').val(recipe.id);
            $('#recipeTitleInput').val(recipe.title);
            $('#recipePrepTime').val(recipe.prepTime);
            $('#recipeCookTime').val(recipe.cookTime);
            $('#recipeServings').val(recipe.servings);
            $('#recipeDifficulty').val(recipe.difficulty || 'EASY');
            $('#recipeSource').val(recipe.source || '');
            $('#recipeUrl').val(recipe.url || '');
            $('#recipeDirections').val(recipe.directions || '');
            $('#recipeNotes').val(recipe.notes ? recipe.notes.description : '');

            // Load existing image
            if (recipe.image) {
                $('#recipeImageBase64').val(recipe.image);
                $('#recipeImagePreviewImg').attr('src', recipe.image);
                $('#recipeImagePreview').show();
            } else {
                clearRecipeImage();
            }

            // Check categories
            if (recipe.categories) {
                recipe.categories.forEach(cat => {
                    $(`#cat-${cat.id}`).prop('checked', true);
                });
            }

            // Populate ingredients
            $('#ingredientsList').empty();
            if (recipe.ingredients && recipe.ingredients.length > 0) {
                recipe.ingredients.forEach(ing => {
                    addIngredientRow(ing);
                });
            } else {
                addIngredientRow();
            }

            // Close detail modal if open
            const detailModal = bootstrap.Modal.getInstance(document.getElementById('recipeDetailModal'));
            if (detailModal) detailModal.hide();

            new bootstrap.Modal(document.getElementById('recipeFormModal')).show();
        },
        error: function() {
            alert('Error loading recipe for editing');
        }
    });
}

/**
 * Edit recipe - called from detail modal
 */
function editRecipe(recipeId) {
    showRecipeForm(recipeId);
}

/**
 * Add ingredient row to the form
 */
function addIngredientRow(ingredient) {
    const idx = $('#ingredientsList .ingredient-row').length;

    let unitOptions = '';
    cachedUnitMeasures.forEach(um => {
        const selected = ingredient && ingredient.unitMeasure && ingredient.unitMeasure.id === um.id ? 'selected' : '';
        unitOptions += `<option value="${um.id}" ${selected}>${um.description}</option>`;
    });

    const html = `
        <div class="row g-2 mb-2 ingredient-row align-items-end">
            <div class="col-md-2">
                <input type="number" class="form-control ing-amount" placeholder="Amount" step="0.1" min="0"
                       value="${ingredient ? ingredient.amount : ''}">
            </div>
            <div class="col-md-3">
                <select class="form-select ing-unit">
                    <option value="">-- Unit --</option>
                    ${unitOptions}
                </select>
            </div>
            <div class="col-md-5">
                <input type="text" class="form-control ing-desc" placeholder="Description"
                       value="${ingredient ? (ingredient.description || '') : ''}">
            </div>
            <div class="col-md-2">
                <button type="button" class="btn btn-outline-danger btn-sm w-100" onclick="this.closest('.ingredient-row').remove()">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        </div>
    `;
    $('#ingredientsList').append(html);
}

/**
 * Submit recipe form
 */
function submitRecipeForm() {
    const form = document.getElementById('recipeForm');
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    // Build recipe object
    const recipe = {
        title: $('#recipeTitleInput').val(),
        prepTime: parseInt($('#recipePrepTime').val()),
        cookTime: parseInt($('#recipeCookTime').val()),
        servings: parseInt($('#recipeServings').val()) || null,
        difficulty: $('#recipeDifficulty').val(),
        source: $('#recipeSource').val() || null,
        url: $('#recipeUrl').val() || null,
        directions: $('#recipeDirections').val(),
        image: $('#recipeImageBase64').val() || null,
        notes: {
            description: $('#recipeNotes').val() || null
        },
        ingredients: [],
        categories: []
    };

    // Add ID if editing
    const recipeId = $('#recipeId').val();
    if (recipeId) {
        recipe.id = recipeId;
    }

    // Collect ingredients
    $('.ingredient-row').each(function() {
        const amount = $(this).find('.ing-amount').val();
        const unitId = $(this).find('.ing-unit').val();
        const desc = $(this).find('.ing-desc').val();
        if (desc && desc.trim()) {
            const ingredient = {
                description: desc.trim(),
                amount: amount ? parseFloat(amount) : 0
            };
            if (unitId) {
                ingredient.unitMeasure = { id: unitId };
            }
            recipe.ingredients.push(ingredient);
        }
    });

    // Collect selected categories
    $('.category-checkbox:checked').each(function() {
        recipe.categories.push({ id: $(this).val() });
    });

    // Submit
    $.ajax({
        url: '/api/recipes',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(recipe),
        success: function(savedRecipe) {
            // Close modal
            bootstrap.Modal.getInstance(document.getElementById('recipeFormModal')).hide();
            alert(recipeId ? 'Recipe updated successfully!' : 'Recipe created successfully!');
            // Refresh recipes list
            if (window.location.pathname === '/recipes') {
                loadAllRecipes();
            } else {
                window.location.href = '/recipes';
            }
        },
        error: function(xhr) {
            if (xhr.status === 401 || xhr.status === 403) {
                alert('You must be logged in to save recipes.');
            } else {
                alert('Error saving recipe. Please check your input and try again.');
            }
        }
    });
}

/**
 * Update recipe page indicator
 */
function updateRecipePageIndicator() {
    $('#currentRecipePage').text('Page ' + (currentRecipePage + 1));
    updateRecipePaginationButtons();
}

/**
 * Update recipe pagination button states
 */
function updateRecipePaginationButtons() {
    console.log(`updateRecipePaginationButtons: page=${currentRecipePage}, isLastPage=${isLastRecipePage}`);

    // Disable Previous button if on first page
    if (currentRecipePage === 0) {
        $('#recipePrevBtn').addClass('disabled');
    } else {
        $('#recipePrevBtn').removeClass('disabled');
    }

    // Disable Next button if on last page
    if (isLastRecipePage) {
        $('#recipeNextBtn').addClass('disabled');
        console.log('Next button DISABLED');
    } else {
        $('#recipeNextBtn').removeClass('disabled');
        console.log('Next button ENABLED');
    }
}

/**
 * Go to next recipe page
 */
function nextRecipePage() {
    // Triple-check: if button is disabled, don't proceed
    if ($('#recipeNextBtn').hasClass('disabled')) {
        console.log('Next button is disabled, aborting');
        return false;
    }
    // Prevent going to next page if we're already on the last page
    if (currentRecipePage < 0 || isLastRecipePage) {
        console.log(`Cannot go to next page: currentPage=${currentRecipePage}, isLastPage=${isLastRecipePage}`);
        return false;
    }
    console.log(`Going to next recipe page: ${currentRecipePage} -> ${currentRecipePage + 1}`);
    currentRecipePage++;
    if (currentSearch || currentDifficulty) {
        filterAndLoadRecipes();
    } else {
        loadAllRecipes();
    }
    return false;
}

/**
 * Go to previous recipe page
 */
function previousRecipePage() {
    // Triple-check: if button is disabled, don't proceed
    if ($('#recipePrevBtn').hasClass('disabled')) {
        console.log('Previous button is disabled, aborting');
        return false;
    }
    if (currentRecipePage <= 0) {
        console.log(`Cannot go to previous page: already at page 0`);
        return false;
    }
    console.log(`Going to previous recipe page: ${currentRecipePage} -> ${currentRecipePage - 1}`);
    currentRecipePage--;
    if (currentSearch || currentDifficulty) {
        filterAndLoadRecipes();
    } else {
        loadAllRecipes();
    }
    return false;
}

/**
 * Update category page indicator
 */
function updateCategoryPageIndicator() {
    $('#currentCategoryPage').text('Page ' + (currentCategoryPage + 1));
    updateCategoryPaginationButtons();
}

/**
 * Update category pagination button states
 */
function updateCategoryPaginationButtons() {
    // Disable Previous button if on first page
    if (currentCategoryPage === 0) {
        $('#categoryPrevBtn').addClass('disabled');
    } else {
        $('#categoryPrevBtn').removeClass('disabled');
    }

    // Disable Next button if on last page
    if (isLastCategoryPage) {
        $('#categoryNextBtn').addClass('disabled');
    } else {
        $('#categoryNextBtn').removeClass('disabled');
    }
}

/**
 * Go to next category page
 */
function nextCategoryPage() {
    // Double-check: if button is disabled, don't proceed
    if ($('#categoryNextBtn').hasClass('disabled')) {
        return false;
    }
    // Prevent going to next page if we're already on the last page
    if (currentCategoryPage < 0 || isLastCategoryPage) {
        return false;
    }
    currentCategoryPage++;
    loadAllCategories();
    return false;
}

/**
 * Go to previous category page
 */
function previousCategoryPage() {
    // Double-check: if button is disabled, don't proceed
    if ($('#categoryPrevBtn').hasClass('disabled')) {
        return false;
    }
    if (currentCategoryPage <= 0) {
        return false;
    }
    currentCategoryPage--;
    loadAllCategories();
    return false;
}
