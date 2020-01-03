/* Selectors */
let errorAlert = $('#error');
let artistInput = $('#artist');
let titleInput = $('#title');
let albumInput = $('#album');
let trackInput = $('#track');
let yearInput = $('#year');
let composerInput = $('#composer');
let genreInput = $('#genre');

/* Document ready */
jQuery(document).ready(function() {
    errorAlert.hide();
    getGenreList();
});

/* Load tag list */
function loadTags() {
    $.getJSON("/loadTags",function(result){
        artistInput.val(result.artist);
        titleInput.val(result.title);
        albumInput.val(result.album);
        trackInput.val(result.track);
        yearInput.val(result.year);
        composerInput.val(result.composer);
        genreInput.val(result.genre);

        $("#download").click(function () {
            download();
        });
    });
}

/* Load genre list */
function getGenreList() {
    $.ajax({
        url : '/genre',
        type : 'GET',
        success : function(result) {
            let lines = result.split("\n");
            for(let line = 0; line < lines.length; line++){
                let genreValue = lines[line].substr(0, lines[line].indexOf('-') - 1);
                let genreDesc = lines[line].substr(lines[line].indexOf('-') + 2, lines[line].length - 1);
                genreInput.append(new Option(genreDesc, genreValue));
            }
            loadTags();
        },
        error : function(err) {
            showError(err.responseJSON.message);
        }
    });
}

/* Display selected image */
function readImg(input) {
    errorAlert.hide();
    if (input.files && input.files[0]) {
        let reader = new FileReader();
        reader.onload = function (e) {
            $('img').attr('src', e.target.result);
        };
        reader.readAsDataURL(input.files[0]);
    }
}

/* Init download */
function download() {
    let formData = new FormData();
    formData.append('file', $('input[type=file]')[0].files[0]);
    formData.append('tags', getTags());
    $.ajax({
        url : '/update',
        data : formData,
        processData : false,
        contentType : false,
        type : 'POST',
        success : function() {
            window.location = "/download";
        },
        error : function(err) {
            showError(err.responseJSON.message);
        }
    });
}

/* Get updated tags */
function getTags() {
    let tags = {};
    tags.album = albumInput.val();
    tags.track = trackInput.val();
    tags.artist = artistInput.val();
    tags.title = titleInput.val();
    tags.composer = composerInput.val();
    tags.year = yearInput.val();
    tags.genre = genreInput.val();
    return JSON.stringify(tags);
}

/* Show error msg */
function showError(message) {
    errorAlert.html("");
    errorAlert.append(message);
    errorAlert.show();
}
