import requests
import json
#GET LE IPADRR ET LE PORT POUR LE GET
#res = requests.get("http://192.168.235.71:8000/envoi")
#res.json()

#OR
#Split le string pour obtenir les valeurs demander
#url = ""
#res = urllib.urlopen(urllib)
#data = json.loads(res.read())
import pandas as pd
import csv

"""
split = url.split("/")
code = split[2]
"""
#GET /station/<code>
def station_code(code):
    
    print("1) Debut de la recherche pour la position")
    with open('../Engin1/Stations_2017.csv') as f:
        reader = csv.DictReader(f)
        print("2) On cherche ligne par ligne")
        for row in reader:
            if (row["code"] == code):
                name = row["name"]
                latitude = row["latitude"]
                longitude = row["longitude"]
                
    datas = json.dumps({"nom": str(name), "latitude": float(latitude), "longitude": float(longitude)})
    print("3) La position trouvee est :")
    print(datas)
    return datas

#station_code("7060")
#station_code("6119")


#POST /station/recherche
def name_station(name):
    #name = "Laurier"
    stationFound = list()
    
    print("1) Debut de la recherche de la station")
    with open('../Engin1/Stations_2017.csv') as f:
        print("2) On cherche ligne par ligne")
        reader = csv.DictReader(f)
        for row in reader:
            if (name in row["name"]):
                nameStation = row["name"]
                codeStation = row["code"]
                stationFound.append({"code": codeStation, "name": nameStation})
    
    datas = json.dumps({"stations": stationFound})
    print("3) Les stations trouvees sont :")
    print(datas)
    return datas


from flask import Flask, request

app = Flask(__name__)

@app.route("/")
def home():
    return "<h1>HELLO ENGIN 1<h1>"

@app.route("/envoi", methods=['POST'])
def home_test():
    return "<h1>You are in Engin1<h1>",200

@app.route("/station/<code>", methods=['POST', 'GET'])
def search_code_station(code):
    if request.method == 'GET':
        code = {code}.pop()
        
        exist = False
        with open('../Engin1/Stations_2017.csv') as f:
            reader = csv.DictReader(f)
            for row in reader:
                if(row["code"] == code):
                    exist = True
        if(exist == False):
            print("La station est invalide")
            return "Pas de donnee", 404
        
        data = station_code(str(code))
        print (data)
        return data, 200 
    else:
        return "Mauvaise requete", 400

@app.route("/station/recherche", methods=['POST', 'GET'])
def search_station_by_name():
    if request.method == 'GET':
        return "Mauvaise requete", 400
    else:
        val = request.get_json()
        pairs = val.items()
        for key, value in pairs:
            print (value)
        datas = name_station(value)
        return datas, 200

if __name__ == "__main__":
   app.run()
   #app.run(port=5001)