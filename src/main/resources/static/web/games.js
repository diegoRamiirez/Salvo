var app = new Vue({
      el: '#app',
      data: {
          games: [],
          scores: [],
          email:"",
          password:"",
          playerLog:null,
          scores:[],
      },
      methods:{
          findData:function () {
              $.get("/api/games", function(data){
                  app.games = data.games;
                  app.playerLog= data.playerLog;
                 app.scoresPlayers(app.games);
              });
          },
          scoresPlayers: function (games){
                           for (i = 0 ; i < games.length; i++){
                              var gamePlayers = games[i].gamePlayers;

                              for (j = 0 ; j< gamePlayers.length; j++){
                               var index =  app.scores.findIndex(scorePlayer => scorePlayer.player === gamePlayers[j].player.userName);
                               if ( index == -1 ){
                                   var scorePlayer = {
                                       player: gamePlayers[j].player.userName,
                                       nLoss: 0,
                                       nTies: 0,
                                       nWins: 0,
                                       nTotal: 0,
                                   };
                                if(gamePlayers[j].score != null){

                                   if (gamePlayers[j].score.score == 0.0) {scorePlayer.nLoss ++}
                                   else if (gamePlayers[j].score.score == 0.5) {scorePlayer.nTies ++}
                                   else if (gamePlayers[j].score.score == 1.0) {scorePlayer.nWins ++};


                                      scorePlayer.nTotal +=gamePlayers[j].score.score;

                                   app.scores.push(scorePlayer);
                                   }
                               }
                               else{
                                if(gamePlayers[j].score != null){
                                   if (gamePlayers[j].score.score == 0.0) {app.scores[index].nLoss ++}
                                   else if (gamePlayers[j].score.score == 0.5) {app.scores[index].nTies ++}
                                   else if (gamePlayers[j].score.score == 1.0) {app.scores[index].nWins ++};
                                      app.scores[index].nTotal +=gamePlayers[j].score.score;
                                      }
                               }
                              };
                           }
                  }
                },

         })
app.findData();



function login() {
    var user = app.email
    var pass = app.password
    if (user == "" || pass == "") {
        alert("Please fill all the fields");
    } else {
        $.post("/api/login", {
                userName: user,
                password: pass,
            }).done(function () {
                location.reload()
            })
            .fail(function (error) {
                alert("Pleaste, try again :(");
            })
    }
}



function logout() {
    $.post("/api/logout").done(function () {
        location.reload();
        alert("logged out succesfull")
    })
}

function signUp() {
    var user = app.email
    var pass = app.password
    if (user == "" || pass == "") {
        alert("Please fill all the fields");
    } else {
        $.post("/api/players", {
            userName: user,
            password: pass,
        },login())
    }
}

