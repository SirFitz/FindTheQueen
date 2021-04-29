let gameOn = false;


function initMove(game) {
    $("#login_form").hide();
    $("#game_form").show();
}

function playerTurn(turn, value) {
    if (gameOn) {
        if ([1,2,3].includes(value)) {
            makeAMove(player, value);
        } else {
            alert('Choice must be 1,2 or 3');
        }
    }
}

function makeAMove(player, value) {
    let playerChoice = {
        Player: player,
        gameId: gameId
    }

    if(player.type == "dealer"){
        playerChoice.QueenPosition = value;
    } else {
        playerChoice.GuessPosition = value;
    }

    $.ajax({
        url: url + "/game/gameplay",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(playerChoice),
        success: function (data) {
            gameOn = false;
            displayResponse(data);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function displayResponse(data) {
    console.log('DISPLAY DATA', data);
    if(data.status == "complete"){
        dealerWins = data.winners.filter(x => x == 1);
        spotterWins = data.winners.filter(x => x == 2);
        if(dealerWins > spotterWins){
            if(player.type == "dealer"){
                alert('Victory');
            } else {
                alert('Defeat');
            }
        } else {
            if(player.type == "spotter"){
                alert('Victory');
            } else {
                alert('Defeat');
            }
        }
        return;
    }
    if(data.status == "started"){
        alert("we're waiting on player 2");
    }
    gameOn = true;
}

function reset() {
    player = null;
}



document.addEventListener('DOMContentLoaded', function(e){


    toastr.options = {
        "closeButton": false,
        "debug": false,
        "newestOnTop": false,
        "progressBar": true,
        "positionClass": "toast-top-right",
        "preventDuplicates": false,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "15000",
        //"extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }


});

document.addEventListener('submit', function(event){
    if(event.target.matches('#login_form')){
        event.preventDefault();
        create_game();
    }

    if(event.target.matches('#game_form')){
        event.preventDefault();
        playerTurn(player, document.querySelector("#choice").value);
    }
});
