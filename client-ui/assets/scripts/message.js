const host = "http://localhost:8080"
const params = new URLSearchParams(window.location.search)
let conversationId;
let messageInput = document.querySelector('#message');
let sendButton = document.querySelector('#send');
let stompClient = null;

if (params.has("conversationId")) {
    conversationId=params.get("conversationId")
}

function connect() {
    var socket = new SockJS(`${host}/ws`);
    stompClient = Stomp.over(socket);

    stompClient.connect({ "Authorization": `Bearer ${localStorage.getItem("userToken")}` }, onConnected, onError);
}

// Connect to WebSocket Server.
connect();

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            content: messageInput.value,
            conversationId: conversationId
        };
        stompClient.send(
            `/app/send/${conversationId}`,
            { "Authorization": `Bearer ${localStorage.getItem("userToken")}` },
            JSON.stringify(chatMessage)
        );
        messageInput.value = '';
    }
    event.preventDefault();
}


async function onConnected() {
    stompClient.subscribe(
        `/conversation/${conversationId}`,
        onMessageReceived,
        { "Authorization": `Bearer ${localStorage.getItem("userToken")}` }
    );
}

function onError() {
    console.log("Error");
}

function onMessageReceived(payload) {
    console.log("Subcribe");
    var message = JSON.parse(payload.body);
    console.log('Receive: ',message);
}

sendButton.addEventListener("click",sendMessage);