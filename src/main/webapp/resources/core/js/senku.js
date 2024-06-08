$(document).ready(function() {
    function actualizarTablero() {
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
            
            $('.casillero').click(function() {
                var x = $(this).data('x');
                var y = $(this).data('y');
                $.post("moverOSeleccionar/" + x + "/" + y, function(data) {
                    if (data.success) {
                        actualizarTablero();
                        console.log(data.message);
                    } else {
                        console.error("Error: " + data.message);
                    }
                });
            });
        });
    }

    actualizarTablero();
});
