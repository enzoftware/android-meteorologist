const Hapi = require('hapi');
const Vision = require('vision');
const Request = require('request');
const Handlebars = require('handlebars');
const LodashFilter = require('lodash.filter');
const LodashTake = require('lodash.take');


const server = new Hapi.Server();

server.connection({
    host : 'localhost',
    port : 3000
});

server.register(Vision,(err)=>{
    server.views({
        engines : {
            html : Handlebars
        },
        relativeTo : __dirname,
        path : './views',
    });
});

server.route({
    method : 'GET' ,
    path : '/' ,
    handler : function (request, reply){
        Request.get('http://api.football-data.org/v1/competitions/456/leagueTable' , function (error , response , body){
            if (error){
                throw error;
            }

            const data = JSON.parse(body);
            reply.view('index',{result : data});

        });
    }
});

Handlebars.registerHelper('teamID',function (teamURL){
    return teamURL.slice(38);
});


server.start((err) =>{
    if(err){
        throw err;
    }

    console.log('Server running at :' +  server.info.uri );
});