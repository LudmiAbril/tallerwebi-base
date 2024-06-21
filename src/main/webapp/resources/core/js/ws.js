
//ACA VA LO DE WEBSOCKETS
//let stompClientSalaEspera = null;
let stompClient = null;
let jugadores = [];

function connect() {
    const socket = new SockJS('/spring/bingo-multijugador');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/updates', function (message) {
            handleWebSocketMessage(JSON.parse(message.body));
        });
    });
}

function obtenerNuevoNumero() {
    stompClient.send("/spring/bingo-multijugador/obtenerNuevoNumero", {}, {});
}
function enviarNumeroAlServidor(nuevoNumero) {
    if (stompClient && stompClient.connected) {
        stompClient.send({
                    destination: "/app/nuevoNumero",
                    body: JSON.stringify({tipo: "nuevoNumero", nuevoNumero: nuevoNumero})
                });
    } else {
        console.error("stompClient no está conectado.");
    }
}
function handleWebSocketMessage(message) {
    switch (message.tipo) {
        case "nuevoNumero":
            actualizarNumero(message.nuevoNumero);
            break;
        case "ganador":
            mostrarGanador(message.ganador);
            break;
        case "join":
            actualizarJugadoresEnSala(message.jugadores);
            break;
        default:
            console.error("Tipo de mensaje desconocido:", message);
    }
}
function handleSalaEsperaMessage(message) {
    if (message.type === "join") {
        actualizarJugadoresEnSala(message.jugadores);
    } else if (message.type === "start") {
        iniciarPartida(message);
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
function actualizarJugadoresEnSala(jugadoresEnSala) {
    jugadores = jugadoresEnSala;
    if (jugadores.length > 0) {
        $("#jugador1").text(jugadores[0].nombre);
    }
    if (jugadores.length > 1) {
        $("#jugador2").text(jugadores[1].nombre);
    }
    else {
            $("#nombreJugador2").text('esperando nuevos jugadores');
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
window.onload = function() {
    connect();
};
function showGreeting(message) {
    $("#ultimoNumeroCantado").text(message);
}
function showWinner(winner) {
    alert("El ganador del juego es: " + winner);
}/*
const stompClientSalaEspera = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/spring/sala-espera',
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
});
function connectSalaEspera() {
    stompClientSalaEspera.onConnect = (frame) => {
        console.log('Connected to sala de espera: ' + frame);
        stompClientSalaEspera.subscribe('/topic/sala-espera', (message) => {
            handleSalaEsperaMessage(JSON.parse(message.body));
        });
        stompClientSalaEspera.publish({
            destination: '/app/sala-espera/join',
            body: JSON.stringify({ 'message': 'Jugador se ha unido' }),
        });
    };

    stompClientSalaEspera.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
    };

    stompClientSalaEspera.activate();
}
function disconnectSalaEspera() {
    if (stompClientSalaEspera) {
        stompClientSalaEspera.deactivate();
    }
}
const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/spring/bingo-multijugador'
});

stompClient.debug = function(str) {
    console.log(str)
 };

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/messages', (m) => {
        console.log(JSON.parse(m.body).content);
        const messagesContainer = document.getElementById("chat-messages");
        const newMessage = document.createElement("p")
        newMessage.textContent = JSON.parse(m.body).content;
        messagesContainer.appendChild(newMessage);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

stompClient.activate();

// Take the value in the ‘message-input’ text field and send it to the server with empty headers.
function sendMessage(){

    let input = document.getElementById("message");
    let message = input.value;

    stompClient.publish({
        destination: "/app/chat",
        body: JSON.stringify({message: message})
    });
}
window.onload = function () {
    connectSalaEspera();

    // Si necesitas conectar al bingo multijugador al cargar la página,
    // puedes llamar a connectBingo() aquí también.
};*/