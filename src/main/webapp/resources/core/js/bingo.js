var intervaloRefresco;
//se utilizará para almacenar el intervalo de actualización del número cantado.
var numeroColorMap = {};
$(document).ready(function () {

            // Inicializar stompClient después de cargar la biblioteca

    // una vez que se realiza la peticion /obtenerDatosIniciales se ejecuta la funcion siguiente, que es la respuesta a esa peticion. Es decir, cuando se pide /obtenerDatosIniciales se responde de esa forma
    $.get("obtenerDatosIniciales", function (data) {

        $("#numeroCantado").text(data.numeroAleatorioCantado);
        //para construir la estructura de la tabla del cartón.
        var tablaHtml = "";
        //nUMEROS ESTA NULL? WTF!!
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

        if (data.error) {
            alert(data.error)
        } else {
            tipoPartidaBingo = data.tipoPartidaBingo;
            if (tipoPartidaBingo === "LINEA") {
                console.log("mostrando el boton de linea")
                document.getElementById("botonLinea").style.display = "block";
                document.getElementById("botonBingo").style.display = "none";
            } else if (tipoPartidaBingo === "BINGO") {
                console.log("mostrando el boton de bingo")
                document.getElementById("botonLinea").style.display = "none";
                document.getElementById("botonBingo").style.display = "block";
                // } else if (tipoPartidaBingo === "AMBAS") {
                //     console.log("mostrando ambos botones")
                //     document.getElementById("botonLinea").style.display = "block";
                //     document.getElementById("botonBingo").style.display = "block";
            }
        }

    });
    intervaloRefresco = setInterval(refrescarNumero, 7000);
});
function refrescarNumero() {
    obtenerLosNumerosEntregados();
    $(".numeroCantadoContenedor").removeClass("w3-animate-top");
    setTimeout(function () {
        $.get("obtenerNuevoNumero", function (data) {
            if (data.limiteAlcanzado) {
                abrirModalDeLimiteAlcanzado();
            } else {
                $("#numeroCantado").text(data.nuevoNumero);
                enviarNumeroAlServidor(data.nuevoNumero);
                $(".numeroCantadoContenedor").addClass("w3-animate-top");
            }
        });
    }, 100); // Espera 100 milisegundos antes de solicitar el nuevo número
}
function marcarCasillero(numeroCasillero) {
    $.get("obtenerNumeroActual", function (data) {
        numeroActual = data.numeroActual;
        casilleroEsIgualANumeroEntregado(numeroCasillero, function (result) {
            if (numeroCasillero == numeroActual || result) {
                console.log("marcado")
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
function enviarNumeroAlServidor(nuevoNumero) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/bingo-multijugador/nuevoNumero", {}, JSON.stringify({'nuevoNumero': nuevoNumero}));
    } else {
        console.error("stompClient no está conectado.");
    }
}

function bingo() {
    $.post("bingo", function (data) {
        if (data.seHizoBingo) {
            abrirModal();
            enviarMensajeGanador(data.ganador);
            clearInterval(intervaloRefresco); // Detener la actualización del número
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

function mostrarModalSeleccionTipoPartidaBingo(event) {
    event.preventDefault();
    document.getElementById("modalTipoPartida").style.display = "block";
}
function mostrarModalPartidaMultijugador(event){
    event.preventDefault();
    document.getElementById("iniciarPartidaModal").style.display = "block";
}


function linea() {
    $.get("linea", function (data) {
        if (data.seHizoLinea) {
            console.log("hiciste linea")
            abrirModal();
            clearInterval(intervaloRefresco);
            intervaloRefresco = null;
        } else if (!data.seHizoLinea) {
            console.log("no hiciste linea")
            var botonLinea = document.querySelector("#botonLinea");
            botonLinea.style.color = 'black';
            botonLinea.classList.add('animate__animated', 'animate__shakeX');
            botonLinea.style.backgroundColor = 'gray';
            setTimeout(function () {
                botonLinea.classList.remove('animate__animated', 'animate__shakeX');
                botonLinea.style.backgroundColor = '#8a2be2';
            }, 1000);
        }
    }
    );
}

function abrirModalDeLimiteAlcanzado() {
    document.getElementById("modalLimite").style.display = "block";
}

//ACA VA LO DE WEBSOCKETS
let stompClient = null;

function connect() {
    const socket = new SockJS('/bingo-multijugador');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/updates', function (message) {
            showGameUpdate(JSON.parse(message.body));
        });
    });
}

function obtenerNuevoNumero() {
    stompClient.send("/app/obtenerNuevoNumero", {}, {});
}
/*const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/spring/bingo-multijugador',
    reconnectDelay: 5000, // Intentar reconectar en 5 segundos si se pierde la conexión
    heartbeatIncoming: 4000, // Verificar cada 4 segundos si la conexión sigue activa
    heartbeatOutgoing: 4000
});
stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    //Greetings seguro lo debo de modificar, revisar despues
    stompClient.subscribe('/topic/greetings', (message) => {
        handleWebSocketMessage(JSON.parse(message.body));
    });
};
const sendMessage = (message) => {
    stompClient.send(`/app/${message.type}`, {}, JSON.stringify(message));
}
stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};*/
function handleWebSocketMessage(message) {
    if (message.type === "nuevoNumero") {
        showGreeting(message.nuevoNumero);
    } else if (message.type === "ganador") {
        showWinner(message.ganador);
    }
}
const messageHandlers = {
    "bingo.join": (message) => {
        updateGame(message);
    },
    "bingo.move": (message) => {
        updateGame(message);
    },
    "bingo.winner": (message) => {
        updateGame(message);
        showWinner(message.winner);
    },
    "error": (message) => {
        toastr.error(message.content);
    }
}

const handleMessage = (message) => {
    if (messageHandlers[message.type]) {
        messageHandlers[message.type](message);
    }
    if (message.type === 'bingo.winner') {
        showWinner(message.winner);
    }
}

const abrirModalLimiteAlcanzado = () => {
    document.getElementById("modalLimite").style.display = "block";
}
function showGameUpdate(update) {
    // Aquí se actualizará la interfaz de usuario con la información del juego recibida
    console.log(update);
}
window.onload = function() {
    connect();
};
function showGreeting(message) {
    $("#ultimoNumeroCantado").text(message);
}
function showWinner(winner) {
    alert("El ganador del juego es: " + winner);
}
document.addEventListener("DOMContentLoaded", function() {
    function actualizarJugador2() {
        $.ajax({
            url: '/obtenerEstadoPartida',
            method: 'GET',
            success: function(response) {
                let nombreJugador2 = response.nombreJugador2;
                if (nombreJugador2) {
                    $('#nombreJugador2').text(nombreJugador2);
                } else {
                    $('#nombreJugador2').text('esperando nuevos jugadores');
                }
            },
            error: function(error) {
                console.error("Error al obtener el estado de la partida", error);
            }
        });
    }
    actualizarJugador2();
    setInterval(actualizarJugador2, 50000);
});
socket.onmessage = function(event) {
    var data = JSON.parse(event.data);
    if (data.tipo === "conexion") {
        // Actualizar el valor del span con el nombre del jugador 2
        document.getElementById("nombreJugador2").innerText = data.jugador;
    }
};