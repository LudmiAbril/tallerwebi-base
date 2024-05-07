$(document).ready(function() {
    // Obtener el tablero cuando se carga la página
    $.get("obtenerTablero", function(data) {
        var tableroHtml = '';
        for (var i = 0; i < data.tablero.casilleros.length; i++) {
            var fila = '<tr>'; // Comenzar una nueva fila
            for (var j = 0; j < data.tablero.casilleros[i].length; j++) {
                var color = data.tablero.casilleros[i][j].ocupado ? 'ficha' : 'vacio';
                fila += '<td class="' + color + '"></td>'; // Agregar una celda con la clase correspondiente
            }
            fila += '</tr>'; // Cerrar la fila
            tableroHtml += fila;
        }
        $('.tablero').html(tableroHtml); // Insertar las filas en la tabla
    });
});
