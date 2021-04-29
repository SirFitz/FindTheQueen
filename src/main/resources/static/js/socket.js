const url = 'http://localhost:8080';
let stompClient;
let gameId;
let player;

function connectToSocket(gameId) {
    console.log("connecting to the game");
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            console.log(data);
            displayResponse(data);
        })
    })
}

function create_game() {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    if (!username || !password){
        alert("Please enter login information");
    } else {
        $.ajax({
            url: url + "/game/start",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "username": username,
                "password": password
            }),
            success: function (data) {
                console.log('RESP', data);
                gameId = data.gameId;

                reset();

                connectToSocket(gameId);
                let players = [data.player1, data.player2];
                player = players.find(p => p.username == username);

                if(!!data.player2){
                    alert("The game is in progress!");
                    initMove();
                }

                alert("Congrats the game has begun");

                gameOn = true;
            },
            error: function (error) {
                console.log('ERROR', error);
            }
        })
    }
}
