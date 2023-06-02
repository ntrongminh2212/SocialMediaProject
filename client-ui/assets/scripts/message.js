const host = "http://localhost:8080"
const params = new URLSearchParams(window.location.search)
let conversationId;
let msgList =[];
let messageInput = document.querySelector('#message');
let sendButton = document.querySelector('#send');
let ulMsgBox = document.querySelector('#message-box');
let stompClient = null;

if (params.has("conversationId")) {
    conversationId=params.get("conversationId")
}

function connect() {
    var socket = new SockJS(`${host}/ws`);
    stompClient = Stomp.over(socket);

    stompClient.connect({ "Authorization": `Bearer ${sessionStorage.getItem("userToken")}` }, onConnected, onError);
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
            { "Authorization": `Bearer ${sessionStorage.getItem("userToken")}` },
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
        { "Authorization": `Bearer ${sessionStorage.getItem("userToken")}` }
    );

    msgList = await getConversationMsgsAPI(conversationId);
    renderMessage(msgList);
}

function onError(error) {
    alert(error)
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body).body;
    console.log('Receive: ',message);
    renderMessage([message])
}

function renderMessage(msgList) {
    console.log(msgList);
    ulMsgBox.insertAdjacentHTML(
        "beforeend",
        msgList.map((msg)=>{
            return ` <li msg-id="${msg.messageId}" class="msg">
                <span class="sender-nickname">${msg.nickname}:</span>
                <span class="send-time">${msg.sendTime}:</span>
                <span>${msg.content}</span>
            </li>`
        }).join(" ")
    )
}

sendButton.addEventListener("click",sendMessage);