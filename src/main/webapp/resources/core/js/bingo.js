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
        $(".numeroCantadoContenedor").addClass("w3-animate-top");
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
                    obtenerLosNumerosEntregados(); // Llamar aquí a la función
                });
            } else {
                // obtenerLosNumerosEntregados()
            }
        });
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

function refrescarNumero() {
    $(".numeroCantadoContenedor").removeClass("w3-animate-top");
    setTimeout(function () {
        $.get("obtenerNuevoNumero", function (data) {
            $("#numeroCantado").text(data.nuevoNumero);
            $(".numeroCantadoContenedor").addClass("w3-animate-top");
            obtenerLosNumerosEntregados();
        });
    }, 100); // Espera 100 milisegundos antes de solicitar el nuevo número
}

function obtenerLosNumerosEntregados() {
    $.get("obtenerLosNumerosEntregados", function (data) {
        var ultimosNumeros = data.numerosEntregadosDeLaSesion.slice(-5);
        ultimosNumeros.reverse(); // Invertir el orden de los números entregados
        var numerosEntregadosDiv = $(".numerosEntregados");
        numerosEntregadosDiv.empty();
        ultimosNumeros.forEach(function (numero) {
            var parrafo = $("<p>").text(numero).attr("id", "numeroCantado").addClass("numerosEntregadosContenedor");
            numerosEntregadosDiv.append(parrafo);
        });
    });
}


/*function obtenerLosNumerosEntregados() {
    $.get("obtenerLosNumerosEntregados", function (data) {
        // tengo que ir recorriendo cada item de los numeros y ponerlos en una etiqueta html
        numerosEntregadosDiv = $(".numerosEntregados");
        numerosEntregadosDiv.empty();
        data.numerosEntregadosDeLaSesion.forEach(function (numero) {
            parrafo = $("<p>").text(numero).attr("id", "numeroEntregado").addClass("numerosEntregadosContenedor");
            numerosEntregadosDiv.append(parrafo);
        })
    })
}*/
function bingo() {
    $.post("bingo", function (data) {
        if (data.seHizoBingo) {
            abrirModal();
            clearInterval(intervaloRefresco); // Detener la actualización del número
            intervaloRefresco = null;
        } else {
            sacudirBotonDeBingo();
        }

    }
    );
}

function abrirModal() {
    document.getElementById("modalBingo").style.display = "block";
}

function sacudirBotonDeBingo() {
    var botonBingo = document.getElementById("botonBingo");
    botonBingo.classList.add('animate__animated', 'animate__shakeX');
    botonBingo.style.backgroundColor = 'gray';
    setTimeout(function () {
        botonBingo.classList.remove('animate__animated', 'animate__shakeX');
        botonBingo.style.backgroundColor = '#8a2be2';
    }, 1000);
}


