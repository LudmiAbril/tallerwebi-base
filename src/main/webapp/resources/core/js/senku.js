$(document).ready(function() {
    $.get("/obtenerTablero", function(tablero) {
        var tableroHtml = '';
        for (var i = 0; i < tablero.casilleros.length; i++) {
            var fila = '<div class="fila">';
            for (var j = 0; j < tablero.casilleros[i].length; j++) {
                var color = tablero.casilleros[i][j].ocupado ? 'ficha' : 'vacio';
                fila += '<div class="' + color + '"></div>';
            }
            fila += '</div>';
            tableroHtml += fila;
        }
        $('#tablero').html(tableroHtml);
    });
});
