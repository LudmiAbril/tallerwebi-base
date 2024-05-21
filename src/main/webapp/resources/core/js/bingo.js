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
                    $("#botonCasillero" + numeroCasillero).css("background-color", "purple");
                })
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
    obtenerLosNumerosEntregados();
    $(".numeroCantadoContenedor").removeClass("w3-animate-top");
    setTimeout(function () {
        $.get("obtenerNuevoNumero", function (data) {
            $("#numeroCantado").text(data.nuevoNumero);
            $(".numeroCantadoContenedor").addClass("w3-animate-top");
        });
    }, 100); // Espera 100 milisegundos antes de solicitar el nuevo número
}


function obtenerLosNumerosEntregados() {
    bolaAmarillo = "bolaAmarillo.png";
    bolaCeleste = "bolaCeleste.png"
    bolaNaranja = "bolaNaranja.png";
    bolaRoja = "bolaRoja.png";
    bolaVerde = "bolaVerde.png"
    bolaVioleta = "bolaVioleta.png"
    rutaDeLasImgDeLasBolas = "/spring/imgStatic/";

    let bolas = [
        bolaAmarillo,
        bolaCeleste,
        bolaNaranja,
        bolaRoja,
        bolaVerde,
        bolaVioleta
    ];

    let currentBolaIndex = 0;
    $.get("obtenerLosNumerosEntregados", function (data) {
        var ultimosNumeros = Array.from(data.numerosEntregadosDeLaSesion);
        // ultimo = ultimosNumeros.lastIndexOf();
        // console.log(ultimo);
        ultimosNumeros.reverse();
        var numerosParaMostrar = ultimosNumeros.slice(0, 5);
        // console.log("Números a mostrar:", numerosParaMostrar);
        var numerosEntregadosDiv = $(".numerosEntregados");
        numerosEntregadosDiv.empty();
        numerosParaMostrar.forEach(function (numero) {
            var parrafo = $("<p>").text(numero).attr("id", "numeroCantadoColeccion").addClass("numerosEntregadosContenedor");

            // Agregar la imagen de fondo
            var bola = bolas[currentBolaIndex];
            var backgroundImageUrl = rutaDeLasImgDeLasBolas + bola;
            parrafo.css('background-image', 'url(' + backgroundImageUrl + ')');

            // Incrementar el índice de la bola y reiniciar si alcanza el final del array
            currentBolaIndex = (currentBolaIndex + 1) % bolas.length;

            numerosEntregadosDiv.append(parrafo);
        });
    });
}

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
    console.log("apretado")
    var botonBingo = document.querySelector("#botonBingo");
    botonBingo.style.color='black';
    botonBingo.classList.add('animate__animated', 'animate__shakeX');
    botonBingo.style.backgroundColor = 'gray';
    setTimeout(function () {
        botonBingo.classList.remove('animate__animated', 'animate__shakeX');
        botonBingo.style.backgroundColor = '#8a2be2';
    }, 1000);
}


