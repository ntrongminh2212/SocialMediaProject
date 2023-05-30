const host = "http://localhost:9003"
const params = new URLSearchParams(window.location.search)
let conversationId;
let messageInput = document.querySelector('#message');
let sendButton = document.querySelector('#send');
let stompClient = null;

if (params.has("conversationId")) {
    conversationId=params.get("conversationId")
}

function connect() {
    var socket = new SockJS(`${host}/message/ws`);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
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
        stompClient.send(`${host}/send/${conversationId}`, {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onConnected() {
    console.log("Connected!");
    // Subscribe to the Public Topic
    stompClient.subscribe(`${host}/message/conversation/${conversationId}`, onMessageReceived);
}

function onError() {
    console.log("Error");
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    console.log(message);
}

sendButton.addEventListener("click",sendMessage);