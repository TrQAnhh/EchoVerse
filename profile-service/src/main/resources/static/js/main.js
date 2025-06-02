let stompClient = null;
let senderId = null;
let receiverId = null;

const loginPage = document.querySelector("#login-page");
const chatPage = document.querySelector("#chat-page");
const loginForm = document.querySelector("#loginForm");
const messageForm = document.querySelector("#messageForm");
const messageInput = document.querySelector("#message");
const messageArea = document.querySelector("#messageArea");

loginForm.addEventListener("submit", connect);
messageForm.addEventListener("submit", sendMessage);

function connect(event) {
    event.preventDefault();
    senderId = document.querySelector("#senderId").value;
    receiverId = document.querySelector("#receiverId").value;

    const socket = new SockJS("/profile/ws?senderId=" + senderId);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        loginPage.classList.add("hidden");
        chatPage.classList.remove("hidden");

        const topic = `/topic/conversation.${Math.min(senderId, receiverId)}_${Math.max(senderId, receiverId)}`;
        console.log("✅ Subscribing to", topic);

        stompClient.subscribe(topic, onMessageReceived);
    });
}


function sendMessage(event) {
    event.preventDefault();
    const content = messageInput.value.trim();
    if (content && stompClient) {
        const msg = {
            senderId: Number(senderId),
            receiverId: Number(receiverId),
            content: content,
            messageType: "TEXT"
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(msg));
        messageInput.value = "";
    }
}


function onMessageReceived(payload) {
    const msg = JSON.parse(payload.body);
    displayMessage(msg, false);
}

function displayMessage(msg, isOwn) {
    const li = document.createElement("li");
    li.classList.add("chat-message");

    const content = document.createElement("p");

    if (isOwn) {
        li.classList.add("own");
        content.textContent = `You: ${msg.content}`;
    } else {
        li.classList.add("friend");
        content.textContent = `User ${msg.senderId}: ${msg.content}`;
    }

    li.appendChild(content);
    messageArea.appendChild(li);
    messageArea.scrollTop = messageArea.scrollHeight;
}

