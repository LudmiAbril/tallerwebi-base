const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/spring/wschat'
});

stompClient.debug = function(str) {
    console.log(str)
 };

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/messages', (m) => {
        console.log("Mensaje recibido:", m.body); // Verifica que el mensaje recibido no sea undefined
        const numeroRecibido = JSON.parse(m.body);
        console.log("Número recibido:", numeroRecibido); // Verifica que el parseo es correcto

        const contenedorNumeroEntregado = document.getElementById("numeroCantado");
        contenedorNumeroEntregado.textContent = numeroRecibido;
    });
    setInterval(() => {
            sendMessage();
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


function sendMessage() {
    stompClient.publish({
        destination: "/app/chat",
        body: JSON.stringify({}) // Enviando un objeto vacío, puedes modificar esto según lo necesario
    });
}

