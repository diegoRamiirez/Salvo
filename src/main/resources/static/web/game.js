var app = new Vue({
      el: '#app',
      data: {
          filas:[ "A", "B", "C" , "D", "E", "F" , "G", "H" , "I", "J"],
          columnas:[ "1" , "2" , "3" , "4" , "5", "6", "7", "8", "9","10"],
          gp: null,
          jugador: null,
          jugadorMail:null,
          contrincante: null,
          contrincanteMail:null,
          shipsLocationCell:[],
          salvoLocationCell:[],
          view: {},

      },
      methods:{
          findData:function () {
              app.gpID();
              $.get("/api/game_view/"+ app.gp, )
               .then(function(data){
                app.game = data;
                app.playerView();
                app.salvoLocations();
                app.shipsLocations();

              })},

          playerView:function (){
              for(var i=0;i<app.game.gamePlayers.length;i++){
                  if( app.game.gamePlayers[i].id == app.gp ){
                      app.jugadorMail = app.game.gamePlayers[i].player;
                    }
                  else if (app.game.gamePlayers.id !== app.gp){
                     app.contrincanteMail = app.game.gamePlayers[i].player;
                  }
               }
          },

         //   shipsLocations:function (){
                 //       app.game.ships.forEach(ship => {
                   //          ship.locations.forEach( x  => {
                     //           document.getElementById(x).classList.add("bg-secondary");
                            //        app.shipsLocationCell.push(x);
                               //                               })
                             //                           })

//funciona pero voy a crear varios for
                                //      },


             shipsLocations : function(){
                for (i=0; i < app.game.ships.length; i++){
                    for(k=0; k < app.game.ships[i].locations.length; k++){
                          var elements =  document.getElementById(app.game.ships[i].locations[k]);
                              elements.classList.add("bg-primary");
                        for(l=0; l < app.game.salvos.length; l++){
                           for(m=0; m < app.game.salvos[l].salvoLocations.length; m++){
                               if ((app.contrincanteMail.id == app.game.salvos[l].playerId) && (app.game.ships[i].locations[k] == app.game.salvos[l].salvoLocations[m])){
                               var elements2 = document.getElementById(app.game.salvos[l].salvoLocations[m]);
                                   elements2.innerHTML = "x" ;
                               }
                           }
                          }
                          }
}
  },
      /* locateShip : function(){
          for (i=0 ; i< app.view.ships.length ; i++){
              for (k=0; k <app.view.ships[i].locations.length; k++){
                  var element = document.getElementById (app.view.ships[i].locations[k]);
                  element.classList.add('location');

                  for( j = 0 ; j < app.view.salvoes.length; j++ ){
                      if(app.opponentMail.id == app.view.salvoes[j].playerId)
                          for(l = 0 ; l < app.view.salvoes[j].locations.length ; l++){
                              if(app.view.salvoes[j].locations[l] == app.view.ships[i].locations[k])
                              element.innerHTML = "x";
                          }
                  }
              }
          }
         },
*/


            salvoLocations : function(){
               for (i=0 ; i< app.game.salvos.length ; i++){
                   for (j=0; j < app.game.salvos[i].salvoLocations.length; j++){
                        if (app.jugadorMail.id == app.game.salvos[i].playerId){
                       var elements = document.getElementById (app.game.salvos[i].salvoLocations[j]+'s');
                       elements.classList.add("bg-danger")
                       elements.innerHTML = app.game.salvos[i].turno;}
                       // else {

                     //   var elements2 = document.getElementById (app.game.salvos[i].salvoLocations[j]+'s');
                         //                            elements2.classList.add("bg-secondary");
                                             //        elements2.innerHTML = app.game.salvos[i].turno;

                        }
                   }

              },



          gpID:function () {
            const urlParams = new URLSearchParams(window.location.search);
            app.gp = urlParams.get('Gp');
          },
          }
  })
app.findData();



