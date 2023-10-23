document.getElementById('connect').addEventListener('click', connect);
document.getElementById('disconnect').addEventListener('click', disconnect);
document.getElementById('send').addEventListener('click', sendMessage);

let stompClient = null;
const id = generateRandomString(10);
const ids = ["1", "2", "3", "4"];
const user = ids[Math.floor(Math.random() * ids.length)];

console.log("id : ", id);
console.log("user: ", user);

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('send').disabled = !connected;
}

function connect() {
    const socket = new SockJS(`http://localhost:8181/ws?userID=${user}`);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe(`/user/${user}/branch`, (messageOutput) => {
            console.log(JSON.parse(messageOutput.body));
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function generateRandomString(length) {
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let result = '';
    for (let i = 0; i < length; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length);
        result += characters.charAt(randomIndex);
    }
    return result;
}

function sendMessage() {
    const body = {
        id: id,
        msg: 'test',
        token: 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7bmFtZT1UZXN0IHVzZXIsIHVzZXJJRD0yMmI4YzdhNi01OWUyLTQ3ZjEtYjM3Yy00M2JkNjA1OWU1ZWUsIGVtYWlsPXRlc3QxMjNAdGVzdC5jb219IiwiaWF0IjoxNjk3NzE5Mzc4LCJleHAiOjE2OTc3MjI5Nzh9.ddv6JdGnPfSct10_CPpL6aYdw1wJk7QHeSfNMAMPR6c'
    }
    console.log(body);
    stompClient.send("/app/createBranch", {}, JSON.stringify(body));
}

function showMessageOutput(messageOutput) {
    console.log(messageOutput)
    const response = document.getElementById('messages');
    const p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(`id > ${messageOutput.id} >>> msg > ${messageOutput.message} >>> code > ${messageOutput.code}`));
    response.appendChild(p);
}
