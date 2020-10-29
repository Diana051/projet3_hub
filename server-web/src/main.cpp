#include <iostream>
#include "../pistacheLib/pistache/endpoint.h"
#include "../pistacheLib/pistache/http.h"
#include "../pistacheLib/pistache/router.h"
#include "../json-develop/nlohmann/json.hpp"


using namespace std;
using namespace Pistache;
using json = nlohmann::json;

namespace Generic {

}

class StatsEndpoint {
public:
    explicit StatsEndpoint(Address addr)
        : httpEndpoint(std::make_shared<Http::Endpoint>(addr))
    { }

    void init() {
        int thr = 2;
        auto opts = Http::Endpoint::options()
            .threads(static_cast<int>(thr));
        httpEndpoint->init(opts);
        setupRoutes();
    } 

    void start() {
        httpEndpoint->setHandler(router.handler());
        httpEndpoint->serve();
    }

private:
    void setupRoutes() {
        using namespace Rest;


        /*** APPLICATION PC ***/
        Routes::Post(router, "/server/usager/login", Routes::bind(&StatsEndpoint::login, this));
        //Routes::Put(router, "/server/usager/passWord", Routes::bind(&StatsEndpoint::changePassWord, this));
        Routes::Get(router, "/server/sondage", Routes::bind(&StatsEndpoint::getSurvey, this));

        /*** APPLICATION ANDROID ***/
        Routes::Put(router, "/server/sondage", Routes::bind(&StatsEndpoint::putSurvey, this));
        //Routes::Get(router, "/server/status", Routes::bind(&StatsEndpoint::getStatut, this));

        /*** TEST DE CONNEXION ***/
        Routes::Post(router, "/server/envoi", Routes::bind(&StatsEndpoint::respondPost, this));
        Routes::Get(router, "/server/", Routes::bind(&StatsEndpoint::respondGet, this));
        Routes::Get(router, "/server/sondageTest", Routes::bind(&StatsEndpoint::getSurveyTest, this));

    }



    void respondGet(const Rest::Request& request, Http::ResponseWriter response) {
        response.send(Http::Code::Ok, "Hello BUDDY welcome in the server of INF3395-EQUIPE-2");
    }


    void respondPost(const Rest::Request& request, Http::ResponseWriter response) {
        auto body = request.body();
        response.send(Http::Code::Ok, "Hello BUDDY we receive " + body);
    }

    void getSurveyTest(const Rest::Request& request, Http::ResponseWriter response) {
        response.send(Http::Code::Ok, surveys_array.dump());
    }


    bool authUser(const Rest::Request& request) {
        auto header = request.headers();
        auto ct = header.get<Http::Header::Authorization>();
         if(ct->getBasicPassword() == "salut") {
           return true;
        } else {
           return false ;
        }
    }


    void login(const Rest::Request& request, Http::ResponseWriter response) {
       auto header = request.headers();
        auto ct = header.get<Http::Header::Authorization>();
        if(authUser(request)) {
            response.send(Http::Code::Ok, "ok"); // 200
        } else {
            response.send(Http::Code::Forbidden, "utilisateur non autorisé"); // 403
        }
    }


    void putSurvey(const Rest::Request& request, Http::ResponseWriter response) {
        auto surveys = request.body();
        addSurveyToArray(surveys);
        response.send(Http::Code::Ok, "bien recu");
    }


    void getSurvey(const Rest::Request& request, Http::ResponseWriter response) {
    
        if(authUser(request)) {
           response.send(Http::Code::Ok, surveys_array.dump());
        } else {
            response.send(Http::Code::Forbidden, "utilisateur non autorisé"); // 403
        }
    }


    void addSurveyToArray(string newSurveys) {
        json j = json::parse(newSurveys);
        /*for(auto i = 0; i < surveys_array.size() ; i++) {
            if(j.["courriel"] == surveys_array.items[i].["courriel"]) {
                  surveys_array.erase(i);
                break;
            }
        }*/
        auto pos = surveys_array.insert(surveys_array.end(), j);
    }



    std::shared_ptr<Http::Endpoint> httpEndpoint;
    Rest::Router router;
    json surveys_array = json::array();
};

int main() {
    Port port(8081);

    Address addr(Ipv4::any(), port);

    cout << "serveur roule sur le port " << port << endl;

    StatsEndpoint stats(addr);

    stats.init();
    stats.start();
}






/*
    void changePassWord(const Rest::Request& request, Http::ResponseWriter response) {
        auto newPassWord = request.body();
        response.send(Http::Code::Ok, "ok"); // 200
        //response.send(Http::Code::Bad_Request, "mauvaise requête ( mauvais nouveau mot de passe ou erreur dans le JSON ) "); // 400
        //response.send(Http::Code::Unauthorized, "utilisateur non authentifié"); // 401
    }

    void getSurveyTest(const Rest::Request& request, Http::ResponseWriter response) {
    
           response.send(Http::Code::Ok, surveys_array.dump());
    }

     void getSurvey(const Rest::Request& request, Http::ResponseWriter response) {
    
        if(authUser(request)) {
            getSurveyArray().then(
                [=] (json returnValue) {response.send(Http::Code::Ok, returnValue.dump() );
            },
                [Http::ResponseWriter] (exception_ptr ptr) {
                  response.send(Http::Code::Bad_Request, "mauvaise requête ");
             });
           response.send(Http::Code::Ok, surveys_array.dump());
           .then(
               ,  [Http::ResponseWriter] (exception_ptr ptr) { response.send(Http::Code::Bad_Request, "mauvaise requête ");
             }
           )
        } else {
            response.send(Http::Code::Forbidden, "utilisateur non autorisé"); // 403
        }
    }

     void putSurvey(const Rest::Request& request, Http::ResponseWriter response) {
         auto surveys = request.body();
         json j = json::parse(surveys);
         for(auto i = 0; i < surveys_array.size() ; i++) {
              if(j.["courriel"] == surveys_array.items[i].["courriel"]) {
                  surveys_array.erase(i);
                break;
              }
            }

         auto pos = surveys_array.insert(surveys_array.end(), j);

         response.send(Http::Code::Ok, "well receive");

         // put in array
         addSurveyToArray(surveys).then(
                [Http::ResponseWriter] (json newSurveys) { response.send(Http::Code::Ok, "well receive");
            },
                [Http::ResponseWriter] (exception_ptr ptr) { response.send(Http::Code::Bad_Request, "mauvaise requête ");
            });
    }

     void getStatut(const Rest::Request& request, Http::ResponseWriter response) {
        checkStatut().then(
            [response, response.send()] (json message) { response.send(Http::Code::Ok, "engins are connected");
        },
            [Http::ResponseWriter] (json message) { response.send(Http::Code::Internal_Server_Error, message);

        });
         response.send(Http::Code::Ok, "engins are connected");
    }


    Async::Promise<json> checkStatut() {
        const json message =  { "message": "le serveur a perdu contact avec les engins de données" };
        return Async::Promise<json> message;
    }

    Async::Promise<json>addSurveyToArray(string newSurveys) {
        json j = json::parse(newSurveys);
        auto pos = surveys_array.insert(surveys_array.end(), j);
        return Async::Promise<json>(j);
    }

    Async::Promise<json> getSurveyArray() {
        return Async::Promise<json>(surveys_array) ;
    }
*/