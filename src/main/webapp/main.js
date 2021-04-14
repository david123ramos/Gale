var ws; 

onload = function() {
    document.querySelector("#startBtn").onclick = start;
};
onunload = function(){
    exit();
}





function start() {
    ws = new WebSocket("ws://localhost:8080/Gale/royale"); 
    ws.onopen = function(e) {
        console.log(e);
    };
    

    ws.onmessage = function(e) {
        const eventInfo = JSON.parse(e.data);
        const event = eventInfo["eventName"];
        const data = eventInfo["data"];
    
        EventController[event](data);
    };
    showLoading();
}

function showLoading(){
    document.querySelector("#loading").classList.remove("d-none");
}

function hideLoading(){
    document.querySelector("#loading").classList.add("d-none");
}



/**
 * Desenha o board.
 * @param {Array} board 
 */
function printBoard(board, addListeners=false) {

    const table = document.querySelector("#board");
    table.innerHTML = "";

    
    board.forEach((row, i) => {
        const tr = document.createElement("tr");
        row.forEach((col, j) => {
            const td = document.createElement("td");
            td.setAttribute("data-posx", col.posx);
            td.setAttribute("data-posy", col.posy);
            td.classList.add(col.color || "empty");

            if(col.info && col.info.includes("path")) {
                td.classList.add("path");
                const div = document.createElement("div");
                div.classList.add("pathNum");
                div.textContent = col.info.split(".")[1];
                td.appendChild(div);
            }

            if(addListeners) {
                td.onclick = function() {
                    sendMove(i, j);
                };
            }

            tr.appendChild(td);
        });
        table.appendChild(tr);
    });
}

/**
 * controla a mudança de turno
 * @param {String} turn 
 * @param {Boolean} hasWinner 
 */
function changeTurn(turn,  hasWinner=false){
    const turnDict = {
        "yellow": "Amarelo",
        "blue": "Azul"
    };
    const classAlert = {
        "yellow" : "alert-warning",
        "blue" : "alert-primary",
    };

    const turnDiv = document.querySelector("#turn");
    const alert = document.querySelector("#alertPanel");

    alert.setAttribute("class", `alert ${classAlert[turn]}`);

    if(hasWinner){
        turnDiv.textContent = `O jogador ${turnDict[turn]} venceu!`;
        return;
    }
    turnDiv.textContent = `É a vez do jogador ${turnDict[turn]}`;
}

function declareWiner(arr){
    printBoard(arr[0]);
    changeTurn(arr[1], true);
    ws.close();
    const restart = document.querySelector("#startBtn");
    restart.textContent = "Restart";
    restart.onclick = function () {
        document.querySelector("#alertPanel").setAttribute("class", "");
        document.querySelector("#turn").textContent = "";
        start();
    
    };
    
}   

/**
 * abandona a partida
 */
function exit(){
    ws.send(JSON.stringify({"eventName": "onLeave", "data": "true"}));
}

function printWhoIAm(i){
    document.querySelector("#whoiam").textContent = i.toUpperCase();
}


//controla as mensagens vindas do backend
const EventController = {
    "onStart" : function(data){
        hideLoading();
        printBoard(data, true);

        const exitBtn = document.querySelector("#startBtn");
        exitBtn.textContent = "Leave";
        exitBtn.onclick = exit;


    },
    "onMoveUp" : function(data) {
        printBoard(data, true);
    },
    "onTurnChange": function(turn) {
        changeTurn(turn);
    },
    "onWin": function(arr) {
        declareWiner(arr);
    },

    "whoiam": function(i){
        printWhoIAm(i);
    }
};


function sendMove(x, y) {
    ws.send(JSON.stringify({"eventName":  "onMove", "data": [x, y]}));
}