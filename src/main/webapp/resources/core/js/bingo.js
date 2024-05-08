var intervaloRefresco;
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
    intervaloRefresco = setInterval(refrescarNumero, 7000);
});

function marcarCasillero(numeroCasillero) {
    $.get("obtenerNumeroActual", function (data) {
        numeroActual = data.numeroActual;
        casilleroEsIgualANumeroEntregado(numeroCasillero, function (result) {
            if (numeroCasillero == numeroActual || result) {
                $.post("marcarCasillero/" + numeroCasillero, function () {
                    $("#botonCasillero" + numeroCasillero).css("background-color", "green");
                })
            }
        })
    });
}

function casilleroEsIgualANumeroEntregado(numeroCasillero, callback) {
    $.get("obtenerLosNumerosEntregados", function (data) {
        numerosEntregados = new Set(data.numerosEntregadosDeLaSesion);
        if (numerosEntregados.has(numeroCasillero)) {
            console.log("el casillero es igual a un numero entregado antes")
            callback(true);
        } else {
            console.log("El casillero no es igual a un numero entregado antes")
            callback(false);
        }

    });
}

// function marcarCasillero(numeroCasillero) {
//     $.get("obtenerNumeroActual", function (data) {
//         numeroActual = data.numeroActual;
//         if (numeroCasillero == numeroActual || casilleroEsIgualANumeroEntregado(numeroCasillero)) {
//             $.post("marcarCasillero/" + numeroCasillero, function () {
//                 $("#botonCasillero" + numeroCasillero).css("background-color", "green");
//             });
//         }
//     });
// }

function refrescarNumero() {
    $.get("obtenerNuevoNumero", function (data) {
        $("#numeroCantado").text(data.nuevoNumero);
    });
}

function bingo() {
    $.post("bingo", function (data) {
        if (data.seHizoBingo) {
            abrirModal();
            clearInterval(intervaloRefresco); // Detener la actualización del número
            intervaloRefresco = null;
            // console.log("hiciste bingo");
        } else {
            // console.log("no hiciste bingo");
        }
        // console.log(data.seHizoBingo);
    }
    );
}

function abrirModal() {
    document.getElementById("modalBingo").style.display = "block";
}

