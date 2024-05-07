 $(document).ready(function() {

        $.get("/comenzar-senku", function(data) {

            var tableroData = data.tablero;
            var tableroHtml = '';
            for (var i = 0; i < tableroData.length; i++) {
                var fila = '<div class="fila">';
                for (var j = 0; j < tableroData[i].length; j++) {
                    var color = tableroData[i][j].ocupado ? 'ficha' : 'vacio';
                    fila += '<div class="' + color + '"></div>';
                }
                fila += '</div>';
                tableroHtml += fila;
            }
            $('#tablero').html(tableroHtml);
        });
    });