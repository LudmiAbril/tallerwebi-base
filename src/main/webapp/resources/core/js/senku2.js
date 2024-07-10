$(document).ready(function () {
    $('.carousel').carousel();
});

function abrirModalAyuda() {
    $('#modalAyuda').show();
}

function cerrarModalAyuda() {
    $('#modalAyuda').hide();
}

$('#closeCustomExitModal').click(function() {
    $('#customExitModal').hide();
});

$('#customCancelButton').click(function() {
    $('#customExitModal').hide();
});