var intervaloRefresco;
//se utilizará para almacenar el intervalo de actualización del número cantado.
var numeroColorMap = {};
$(document).ready(function () {
    // una vez que se realiza la peticion /obtenerDatosIniciales se ejecuta la funcion siguiente, que es la respuesta a esa peticion. Es decir, cuando se pide /obtenerDatosIniciales se responde de esa forma
    $.get("obtenerDatosIniciales", function (data) {
        // en el elemento HTML con la id numeroCantado guarda el numeroAleatorioCantado que llega por data
        //Establece el número cantado en el elemento HTML con el id numeroCantado, utilizando el número aleatorio devueltpor el servidor.
        $("#numeroCantado").text(data.numeroAleatorioCantado);
        //para construir la estructura de la tabla del cartón.
        var tablaHtml = "";
        for (var i = 0; i < data.carton.numeros.length; i++) {
            tablaHtml += "<tr>";
            for (var j = 0; j < data.carton.numeros[i].length; j++) {
                //Obtiene el número de casillero en la posición (i, j) del cartón.
                var numeroCasillero = data.carton.numeros[i][j];
                tablaHtml += "<td><button id='botonCasillero" + numeroCasillero + "' onclick='marcarCasillero(" + numeroCasillero + ")'>" + numeroCasillero + "</button></td>";
            }
            tablaHtml += "</tr>";
        }
        $(".carton").html(tablaHtml);
        $(".numeroCantadoContenedor").addClass("w3-animate-top");
        $(".carton").addClass("w3-animate-bottom");
    });
    intervaloRefresco = setInterval(refrescarNumero, 7000);
});
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
function marcarCasillero(numeroCasillero) {
    $.get("obtenerNumeroActual", function (data) {
        numeroActual = data.numeroActual;
        casilleroEsIgualANumeroEntregado(numeroCasillero, function (result) {
            console.log("marcado")
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

// function obtenerLosNumerosEntregados() {
//     bolaAmarillo = "bolaAmarillo.png";
//     bolaCeleste = "bolaCeleste.png"
//     bolaNaranja = "bolaNaranja.png";
//     bolaRoja = "bolaRoja.png";
//     bolaVerde = "bolaVerde.png"
//     bolaVioleta = "bolaVioleta.png"
//     rutaDeLasImgDeLasBolas = "/spring/imgStatic/";

//     let bolas = [
//         bolaAmarillo,
//         bolaCeleste,
//         bolaNaranja,
//         bolaRoja,
//         bolaVerde,
//         bolaVioleta
//     ];

//     let currentBolaIndex = 0;
//     $.get("obtenerLosNumerosEntregados", function (data) {
//         var ultimosNumeros = Array.from(data.numerosEntregadosDeLaSesion);
//         ultimosNumeros.reverse();
//         var numerosParaMostrar = ultimosNumeros.slice(0, 5);
//         var numerosEntregadosDiv = $(".numerosEntregados");
//         numerosEntregadosDiv.empty();
//         numerosParaMostrar.forEach(function (numero) {
//             var parrafo = $("<p>").text(numero).attr("id", "numeroCantadoColeccion").addClass("numerosEntregadosContenedor");
//             var bola = bolas[currentBolaIndex];
//             var backgroundImageUrl = rutaDeLasImgDeLasBolas + bola;
//             parrafo.css('background-image', 'url(' + backgroundImageUrl + ')');
//             currentBolaIndex = (currentBolaIndex + 1) % bolas.length;
//             numerosEntregadosDiv.append(parrafo);
//         });
//     });
// }

function obtenerLosNumerosEntregados() {
    var bolaAmarillo = "bolaAmarillo.png";
    var bolaCeleste = "bolaCeleste.png";
    var bolaNaranja = "bolaNaranja.png";
    var bolaRoja = "bolaRoja.png";
    var bolaVerde = "bolaVerde.png";
    var bolaVioleta = "bolaVioleta.png";
    var rutaDeLasImgDeLasBolas = "/spring/imgStatic/";

    var bolas = [
        bolaAmarillo,
        bolaCeleste,
        bolaNaranja,
        bolaRoja,
        bolaVerde,
        bolaVioleta
    ];

    $.get("obtenerCincoUltimosNumerosEntregados", function (data) {
        var ultimosNumeros = data.ultimosNumerosEntregados;
        ultimosNumeros.reverse();
        var numerosEntregadosDiv = $(".numerosEntregados");
        numerosEntregadosDiv.empty();

        ultimosNumeros.forEach(function (numero) {
            if (!numeroColorMap[numero]) {
                numeroColorMap[numero] = bolas[Object.keys(numeroColorMap).length % bolas.length];
            }

            var parrafo = $("<p>").text(numero).attr("id", "numeroCantadoColeccion").addClass("numerosEntregadosContenedor");
            var backgroundImageUrl = rutaDeLasImgDeLasBolas + numeroColorMap[numero];
            parrafo.css('background-image', 'url(' + backgroundImageUrl + ')');
            numerosEntregadosDiv.append(parrafo);
        });
    });
}


function bingo() {
    $.post("bingo", function (data) {
        if (data.seHizoBingo) {
            console.log("hiciste bingo")
            abrirModal();
            clearInterval(intervaloRefresco); // Detener la actualización del número
            intervaloRefresco = null;
        } else if(!data.seHizoBingo) {
            console.log("no hicisite bingo")
            var botonBingo = document.querySelector("#botonBingo");
            botonBingo.style.color = 'black';
            botonBingo.classList.add('animate__animated', 'animate__shakeX');
            botonBingo.style.backgroundColor = 'gray';
            setTimeout(function () {
                botonBingo.classList.remove('animate__animated', 'animate__shakeX');
                botonBingo.style.backgroundColor = '#8a2be2';
            }, 1000);
        }

    }
    );
}

function abrirModal() {
    document.getElementById("modalBingo").style.display = "block";
    lanzarConfetti();
}

function lanzarConfetti() {
    var duration = 5 * 1000;
    var animationEnd = Date.now() + duration;
    var defaults = { startVelocity: 30, spread: 360, ticks: 60, zIndex: 0 };

    function randomInRange(min, max) {
        return Math.random() * (max - min) + min;
    }

    var interval = setInterval(function() {
        var timeLeft = animationEnd - Date.now();

        if (timeLeft <= 0) {
            return clearInterval(interval);
        }

        var particleCount = 50 * (timeLeft / duration);
        confetti(Object.assign({}, defaults, { 
            particleCount, 
            origin: { x: randomInRange(0.1, 0.3), y: Math.random() - 0.2 } 
        }));
        confetti(Object.assign({}, defaults, { 
            particleCount, 
            origin: { x: randomInRange(0.7, 0.9), y: Math.random() - 0.2 } 
        }));
    }, 250);
}

function mostrarModalSeleccionTipoPartidaBingo(){

}


