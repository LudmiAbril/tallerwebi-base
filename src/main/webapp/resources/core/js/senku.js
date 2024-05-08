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
    });
});

// $('.tablero').on('click', '.casillero', function() {
//     var x = $(this).data('x');
//     var y = $(this).data('y');
//     $.post("seleccionarCasillero", { x: x, y: y }, function(data) {
//         if (data.success) {
            
//             $(this).addClass('ficha-seleccionada');
//         } else {
           
//             alert(data.message);
//         }
//     });
//});
