$(document).ready(function() {
    $.get("obtenerTablero", function(data) {
        var tableroHtml = '';
        for (var i = 0; i < data.tablero.casilleros.length; i++) {
            tableroHtml += '<div class="fila">';
            for (var j = 0; j < data.tablero.casilleros[i].length; j++) {
                var color = data.tablero.casilleros[i][j].ocupado ? 'ficha' : 'vacio';
                tableroHtml += '<div class="casillero ' + color + '" data-x="' + i + '" data-y="' + j + '"></div>'; 
            }
            tableroHtml += '</div>';
        }
        $('.tablero').html(tableroHtml);

        // Después de establecer el HTML del tablero, adjunta los manejadores de eventos
        $('.casillero').click(function() {
            var x = $(this).data('x');
            var y = $(this).data('y');
            $.post("marcarCasillero/" + x + "/" + y, function(data) {
                if (data.success) {
                    // Elimina la clase 'ficha-seleccionada' de todos los casilleros
                    $('.casillero').removeClass('ficha-seleccionada');
                    // Agrega la clase 'ficha-seleccionada' al casillero marcado
                    $(this).addClass('ficha-seleccionada');
                    console.log("Casillero marcado con éxito.");
                } else {
                    console.error("Error al marcar el casillero: " + data.message);
                }
            }.bind(this)); // Necesario para mantener el contexto de 'this' dentro de la función de retorno
        });
    });
});
