const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/spring/wschat'
});

stompClient.debug = function(str) {
    console.log(str)
 };
const numeroCantado;
stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/messages', (m) => {
        console.log("Mensaje recibido:", m.body); // Verifica que el mensaje recibido no sea undefined
        numeroCantado = JSON.parse(m.body);
        console.log("Número recibido:", numeroRecibido); // Verifica que el parseo es correcto


    });
    setInterval(() => {
            nuevoNumero();
        }, 7000);
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

stompClient.activate();


function nuevoNumero() {
    stompClient.publish({
        destination: "/app/chat",
        body: JSON.stringify({})
    });
}

var intervaloRefresco;
//se utilizará para almacenar el intervalo de actualización del número cantado.
var numeroColorMap = {};
$(document).ready(function () {
    // una vez que se realiza la peticion /obtenerDatosIniciales se ejecuta la funcion siguiente, que es la respuesta a esa peticion. Es decir, cuando se pide /obtenerDatosIniciales se responde de esa forma
    $.get("obtenerDatosInicialesMultijugador", function (data) {

        //$("#numeroCantado").text(numeroCantado);
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
        /*$(".numeroCantadoContenedor").addClass("w3-animate-top");*/
        $(".carton").addClass("w3-animate-bottom");

        if (data.error) {
            alert(data.error)
        } else {
            /*tipoPartidaBingo = data.tipoPartidaBingo;
            if (tipoPartidaBingo === "LINEA") {
                console.log("mostrando el boton de linea")
                document.getElementById("botonLinea").style.display = "block";
                document.getElementById("botonBingo").style.display = "none";
            } else if (tipoPartidaBingo === "BINGO") {
                console.log("mostrando el boton de bingo")
                document.getElementById("botonLinea").style.display = "none";
                document.getElementById("botonBingo").style.display = "block";
            }*/
            document.getElementById("botonLinea").style.display = "none";
            document.getElementById("botonBingo").style.display = "block";
        }

    });
});
/*
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
}*/
