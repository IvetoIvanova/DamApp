$(document).ready(function () {
    $('#city').on('input', function () {
        let query = $(this).val();
        if (query.length >= 1) {
            $.ajax({
                url: '/api/locations',
                data: { query: query },
                success: function (data) {
                    $('#citySuggestions').empty();
                    data.forEach(function (location) {
                        let suggestion = $('<a href="#" class="list-group-item list-group-item-action"></a>').text(location.name);
                        suggestion.on('click', function () {
                            $('#city').val(location.name);
                            $('#citySuggestions').empty();
                        });
                        $('#citySuggestions').append(suggestion);
                    });
                },
                error: function (xhr, status, error) {
                    console.error("Error fetching locations:", status, error);
                }
            });
        } else {
            $('#citySuggestions').empty();
        }
    });
});