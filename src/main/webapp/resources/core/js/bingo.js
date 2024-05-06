$(document).ready(function () {
    // una vez que se realiza la peticion /obtenerDatosIniciales se ejecuta la funcion siguiente, que es la respuesta a esa peticion. Es decir, cuando se pide /obtenerDatosIniciales se responde de esa forma
    $.get("obtenerDatosIniciales", function (data) {
        // en el elemento HTML con la id numeroCantado guarda el numeroAleatorioCantado que llega por data
        $("#numeroCantado").text(data.numeroAleatorioCantado);
        // Generar la tabla con la matriz
        var tablaHtml = "";
        for (var i = 0; i < data.carton.numeros.length; i++) {
            tablaHtml += "<tr>";
            for (var j = 0; j < data.carton.numeros[i].length; j++) {
                numeroCasillero = JSON.stringify(data.carton.numeros[i][j]);
                tablaHtml += "<td><button id='botonCasillero" + numeroCasillero + "' onclick='marcarCasillero(" + JSON.stringify(numeroCasillero) + "," + data.numeroAleatorioCantado + ")'>" + numeroCasillero + "</button></td>";
            }
            tablaHtml += "</tr>";
        }
        $(".carton").html(tablaHtml);
    });

});

function marcarCasillero(numeroCasillero, numeroCantado) {
    // Si el número del casillero coincide con el número cantado, pintar el botón de color
    if (numeroCasillero == numeroCantado) {
        $("#botonCasillero" + numeroCasillero).css("background-color", "green");
    }

}


