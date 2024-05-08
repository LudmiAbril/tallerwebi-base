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
                var numeroCasillero = data.carton.numeros[i][j];
                tablaHtml += "<td><button id='botonCasillero" + numeroCasillero + "' onclick='marcarCasillero(" + numeroCasillero + ")'>" + numeroCasillero + "</button></td>";
            }
            tablaHtml += "</tr>";
        }
        $(".carton").html(tablaHtml);
    });
    setInterval(() => {
        refrescarNumero();
    }, 7000);
});

function marcarCasillero(numeroCasillero) {
    $.get("obtenerNumeroActual", function(data) {
        numeroActual = data.numeroActual;
        if (numeroCasillero == numeroActual) {
            $("#botonCasillero" + numeroCasillero).css("background-color", "green");
        }
    });
}

function refrescarNumero(){
    $.get("obtenerNuevoNumero", function(data) {
        $("#numeroCantado").text(data.nuevoNumero);
    });
}

function bingo(){
    $.post("bingo", function(data){
        
    }
    );
}
