var intervaloRefresco;
//se utilizará para almacenar el intervalo de actualización del número cantado.
var numeroColorMap = {};
$(document).ready(function () {
    $.get("obtenerDatosInicialesBot", function (data) {
        $("#numeroCantado").text(data.numeroAleatorioCantado);
        var tablaHtml = "";
        for (var i = 0; i < data.cartonBot.numeros.length; i++) {
            tablaHtml += "<tr>";
            for (var j = 0; j < data.cartonBot.numeros[i].length; j++) {
                //Obtiene el número de casillero en la posición (i, j) del cartón.
                var numeroCasillero = data.cartonBot.numeros[i][j];
                tablaHtml += "<td><button id='botonCasillero" + numeroCasillero + "' onclick='marcarCasillero(" + numeroCasillero + ")'>" + numeroCasillero + "</button></td>";
            }
            tablaHtml += "</tr>";
        }
        $(".cartonBot").html(tablaHtml);
        $(".numeroCantadoContenedor").addClass("w3-animate-top");
        $(".cartonBot").addClass("w3-animate-bottom");
        $("#numerosRestantesParaCompletarLaTirada").text("Numeros restantes para completar la tirada: " + data.numerosRestantesParaCompletarLaTirada);
        console.log(numerosRestantesParaCompletarLaTirada)

        if (data.error) {
            alert(data.error)
        } else {
            tipoPartidaBingo = data.tipoPartidaBingo;
            if (tipoPartidaBingo === "LINEA") {
                document.getElementById("botonLinea").style.display = "block";
                document.getElementById("botonBingo").style.display = "none";
            } else if (tipoPartidaBingo === "BINGO") {
                document.getElementById("botonLinea").style.display = "none";
                document.getElementById("botonBingo").style.display = "block";
            }
        }

    });
    intervaloRefresco = setInterval(refrescarNumero, 7000);
});
function refrescarNumero() {
    obtenerLosNumerosEntregados();
    $(".numeroCantadoContenedor").removeClass("w3-animate-top");
    setTimeout(function () {
        $.get("obtenerNuevoNumeroBot", function (data) {
            console.log("realice la peticion")
            if (data.limiteAlcanzado) {
                abrirModalDeLimiteAlcanzado();
            } else {
                $("#numerosRestantesParaCompletarLaTirada").text("Numeros restantes para completar la tirada: " + data.numerosRestantesParaCompletarLaTirada);
                $("#numeroCantado").text(data.nuevoNumero);
                $(".numeroCantadoContenedor").addClass("w3-animate-top");
            }
        });
        console.log("no pude procesar la solicitud")
    }, 100);
}
// function marcarCasillero(numeroCasillero) {
//     $.get("obtenerNumeroActual", function (data) {
//         numeroActual = data.numeroActual;
//         casilleroEsIgualANumeroEntregado(numeroCasillero, function (result) {
//             if (numeroCasillero == numeroActual || result) {
//                 $.post("marcarCasillero/" + numeroCasillero, function () {
//                     $("#botonCasillero" + numeroCasillero).css("background-color", "purple");
//                 })
//             }
//         });
//     });
// }

function marcarCasillero(numeroCasillero) {
    $.get("obtenerNuevoNumeroBot", function (data) {
        if (data.seMarcoBot == true) {
            $("#botonCasillero" + numeroCasillero).css("background-color", "purple");
        }
        // numeroActual = data.numeroActual;
        // casilleroEsIgualANumeroEntregado(numeroCasillero, function (result) {
        //     if (numeroCasillero == numeroActual || result) {
        //         $.post("marcarCasillero/" + numeroCasillero, function () {
        //             $("#botonCasillero" + numeroCasillero).css("background-color", "purple");
        //         })
        //     }
        // });
        if (data.seHizoBingo) {
            abrirModal();
            clearInterval(intervaloRefresco);
            intervaloRefresco = null;
        } else if (!data.seHizoBingo) {
            var botonBingo = document.querySelector("#botonBingo");
            botonBingo.style.color = 'black';
            botonBingo.classList.add('animate__animated', 'animate__shakeX');
            botonBingo.style.backgroundColor = 'gray';
            setTimeout(function () {
                botonBingo.classList.remove('animate__animated', 'animate__shakeX');
                botonBingo.style.backgroundColor = '#8a2be2';
            }, 1000);
        }

        if (data.seHizoLinea) {
            abrirModal();
            clearInterval(intervaloRefresco);
            intervaloRefresco = null;
        } else if (!data.seHizoLinea) {
            var botonLinea = document.querySelector("#botonLinea");
            botonLinea.style.color = 'black';
            botonLinea.classList.add('animate__animated', 'animate__shakeX');
            botonLinea.style.backgroundColor = 'gray';
            setTimeout(function () {
                botonLinea.classList.remove('animate__animated', 'animate__shakeX');
                botonLinea.style.backgroundColor = '#8a2be2';
            }, 1000);
        }
    });
}


// function casilleroEsIgualANumeroEntregado(numeroCasillero, callback) {
//     $.get("obtenerLosNumerosEntregados", function (data) {
//         numerosEntregados = new Set(data.numerosEntregadosDeLaSesion);
//         if (numerosEntregados.has(numeroCasillero)) {
//             console.log("el casillero es igual a un numero entregado antes")
//             callback(true);
//         } else {
//             console.log("El casillero no es igual a un numero entregado antes")
//             callback(false);
//         }

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

    $.get("obtenerCincoUltimosNumerosEntregadosBot", function (data) {
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


// function bingo() {
//     $.post("bingo", function (data) {
//         if (data.seHizoBingo) {
//             abrirModal();
//             clearInterval(intervaloRefresco); // Detener la actualización del número
//             intervaloRefresco = null;
//         } else if (!data.seHizoBingo) {
//             var botonBingo = document.querySelector("#botonBingo");
//             botonBingo.style.color = 'black';
//             botonBingo.classList.add('animate__animated', 'animate__shakeX');
//             botonBingo.style.backgroundColor = 'gray';
//             setTimeout(function () {
//                 botonBingo.classList.remove('animate__animated', 'animate__shakeX');
//                 botonBingo.style.backgroundColor = '#8a2be2';
//             }, 1000);
//         }

//     }
//     );
// }

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

    var interval = setInterval(function () {
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

function mostrarModalSeleccionTipoPartidaBingoBot(event) {
    event.preventDefault();
    document.getElementById("modalTipoPartida").style.display = "block";
}

// function linea() {
//     $.get("linea", function (data) {
//         if (data.seHizoLinea) {
//             abrirModal();
//             clearInterval(intervaloRefresco);
//             intervaloRefresco = null;
//         } else if (!data.seHizoLinea) {
//             var botonLinea = document.querySelector("#botonLinea");
//             botonLinea.style.color = 'black';
//             botonLinea.classList.add('animate__animated', 'animate__shakeX');
//             botonLinea.style.backgroundColor = 'gray';
//             setTimeout(function () {
//                 botonLinea.classList.remove('animate__animated', 'animate__shakeX');
//                 botonLinea.style.backgroundColor = '#8a2be2';
//             }, 1000);
//         }
//     }
//     );
// }

function abrirModalDeLimiteAlcanzado() {
    document.getElementById("modalLimite").style.display = "block";
}
