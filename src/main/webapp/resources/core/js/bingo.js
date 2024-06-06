var intervaloRefresco;
//se utilizará para almacenar el intervalo de actualización del número cantado.
var numeroColorMap = {};
$(document).ready(function () {
    // una vez que se realiza la peticion /obtenerDatosIniciales se ejecuta la funcion siguiente, que es la respuesta a esa peticion. Es decir, cuando se pide /obtenerDatosIniciales se responde de esa forma
    $.get("obtenerDatosIniciales", function (data) {
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

        if (data.error) {
            alert(data.error)
        } else {
            tipoPartidaBingo = data.tipoPartidaBingo;
            if (tipoPartidaBingo === "LINEA") {
                console.log("msotrando el boton de linea")
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


function bingo() {
    $.post("bingo", function (data) {
        if (data.seHizoBingo) {
            abrirModal();
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
let game = null;
let player = null;

/**
 * Sends a message to the server using the STOMP client.
 * @param {Object} message - The message to be sent. Must contain at least a "type" field.
 */
const sendMessage = (message) => {
    stompClient.send(`/app/${message.type}`, {}, JSON.stringify(message));
}

/**
 * An object containing functions to handle each type of message received from the server.
 */
const messageHandlers = {
    "bingo.join": (message) => {
        updateGame(message);
    },
    "bingo.numberCalled": (message) => {
        updateGame(message);
    },
    "bingo.winner": (message) => {
        updateGame(message);
        showWinner(message.winner);
    }/*,
    "error": (message) => {
        toastr.error(message.content);
    }*/
}

/**
 * Handles a message received from the server.
 * @param {Object} message - The message received.
 */
const handleMessage = (message) => {
    if (messageHandlers[message.type]) {
        messageHandlers[message.type](message);
    }
}

/**
 * Connects the STOMP client to the server and subscribes to the "/topic/bingo" topic.
 */
const connect = () => {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/bingo', function (message) {
            handleMessage(JSON.parse(message.body));
        });
        loadGame();
    });
}

/**
 * Attempts to load a game by joining with the player's previously stored name, or prompts the player to enter their name if no name is stored.
 */
const loadGame = () => {
    const playerName = localStorage.getItem("playerName");
    if (playerName) {
        sendMessage({
            type: "bingo.join",
            player: playerName
        });
    } else {
        joinGame();
    }
}

/**
 * Starts the process of joining a game. Asks the player to enter their name and sends a message to the server requesting to join the game.
 */
const joinGame = () => {
    const playerName = prompt("Enter your name:");
    localStorage.setItem("playerName", playerName);
    sendMessage({
        type: "bingo.join",
        player: playerName
    });
}

/**
 * Updates the game state with the information received from the server.
 * @param {Object} message - The message received from the server.
 */
const updateGame = (message) => {
    game = message;
    updateBoard(message.board);
    document.getElementById("player1").innerHTML = game.player1;
    document.getElementById("player2").innerHTML = game.player2 || 'Waiting for player 2...';
    document.getElementById("numeroCantado").innerHTML = game.currentNumber;
    if (game.winner) {
        showWinner(game.winner);
    }
}

/**
 * Displays a success message with the name of the winning player.
 * @param {String} winner - The name of the winning player.
 */
const showWinner = (winner) => {
    toastr.success(`The winner is ${winner}!`);
    clearInterval(intervaloRefresco);
}

/**
 * Calls a number and sends it to the server.
 */
const callNumber = () => {
    $.get("obtenerNuevoNumero", function (data) {
        if (!data.limiteAlcanzado) {
            sendMessage({
                type: "bingo.numberCalled",
                number: data.nuevoNumero
            });
        } else {
            abrirModalDeLimiteAlcanzado();
        }
    });
}

/**
 * Marks a cell and sends it to the server.
 * @param {Number} numeroCasillero - The number of the cell to be marked.
 */
const marcarCasillero = (numeroCasillero) => {
    sendMessage({
        type: "bingo.markCell",
        number: numeroCasillero,
        player: player
    });
}

/**
 * Calls bingo and sends it to the server.
 */
const bingo = () => {
    sendMessage({
        type: "bingo.call",
        player: player
    });
}

/**
 * Get the initial game data and render the board.
 */
$(document).ready(function () {
    connect();
    $.get("obtenerDatosIniciales", function (data) {
        $("#numeroCantado").text(data.numeroAleatorioCantado);
        renderBoard(data.carton.numeros);
    });
    intervaloRefresco = setInterval(callNumber, 7000);
});

/**
 * Renders the board based on the numbers provided.
 * @param {Array} board - The board numbers.
 */
const renderBoard = (board) => {
    let tablaHtml = "";
    for (let i = 0; i < board.length; i++) {
        tablaHtml += "<tr>";
        for (let j = 0; j < board[i].length; j++) {
            let numeroCasillero = board[i][j];
            tablaHtml += `<td><button id='botonCasillero${numeroCasillero}' onclick='marcarCasillero(${numeroCasillero})'>${numeroCasillero}</button></td>`;
        }
        tablaHtml += "</tr>";
    }
    $(".carton").html(tablaHtml);
}

/**
 * Shows a modal when the limit is reached.
 */
const abrirModalDeLimiteAlcanzado = () => {
    document.getElementById("modalLimite").style.display = "block";
}

window.onload = function () {
    connect();
}
