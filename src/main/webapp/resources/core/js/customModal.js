

function initializeCustomModal() {
    $('#openCustomExitModal').click(function (e) {
        e.preventDefault();
        $('#customExitModal').show();
    });

    $('#closeCustomExitModal, #customCancelButton').click(function () {
        $('#customExitModal').hide();
    });
}

$(document).ready(function () {
    initializeCustomModal();
});
