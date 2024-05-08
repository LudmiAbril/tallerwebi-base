/*$(document).ready(function() {
    // Obtener el tablero cuando se carga la página
    $.get("obtenerTablero", function(data) {
        var tableroHtml = '';
        for (var i = 0; i < data.tablero.casilleros.length; i++) {
            for (var j = 0; j < data.tablero.casilleros[i].length; j++) {
                var color = data.tablero.casilleros[i][j].ocupado ? 'ficha' : 'vacio';
                tableroHtml += '<div class="' + color + '"></div>'; // Agregar un div con la clase correspondiente
            }
        }
        $('.tablero').html(tableroHtml); // Insertar los divs en el contenedor del tablero
    });
});*/

$(document).ready(function() {
    // Obtener el tablero cuando se carga la página
    $.get("obtenerTablero", function(data) {
        var tableroHtml = '';
        for (var i = 0; i < data.tablero.casilleros.length; i++) {
            tableroHtml += '<div class="fila">';
            for (var j = 0; j < data.tablero.casilleros[i].length; j++) {
                var color = data.tablero.casilleros[i][j].ocupado ? 'ficha' : 'vacio';
                tableroHtml += '<div class="' + color + '"></div>'; // Agregar un div con la clase correspondiente
            }
            tableroHtml += '</div>';
        }
        $('.tablero').html(tableroHtml); // Insertar los divs en el contenedor del tablero
    });
});

