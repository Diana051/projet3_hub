#include "./pistacheLib/pistache/client.h"
#include "./pistacheLib/pistache/http.h"
#include "./pistacheLib/pistache/net.h"

using namespace Pistache;
using namespace Pistache::Http;


int main() {

     Http::Client client;
     client.init();
     client.options().keepAlive(true).maxConnectionsPerHost(120);

}