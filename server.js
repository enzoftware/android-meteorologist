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
        Request.get('http://api.football-data.org/v1/competitions/445/leagueTable' , function (error , response , body){
            if (error){
                throw error;
            }

            const data = JSON.parse(body);
            console.log(data);
            reply.view('index',{result : data});

        });
    }
});


server.route({
    method: 'GET',
    path: '/teams/{id}',
    handler: function (request, reply) {
        const teamID = encodeURIComponent(request.params.id);
        console.log(teamID);

        Request.get(`http://api.football-data.org/v1/teams/${teamID}`, function (error, response, body) {
            if (error) {
                throw error;
            }
 
            const result = JSON.parse(body);
            console.log(result);
 
            Request.get(`http://api.football-data.org/v1/teams/${teamID}/fixtures`, function (error, response, body) {
                if (error) {
                    throw error;
                }
 
                const fixtures = LodashTake(LodashFilter(JSON.parse(body).fixtures, function (match) {
                    return match.status === 'SCHEDULED';
                }), 5);
 
                reply.view('team', { result: result, fixtures: fixtures });
            });
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