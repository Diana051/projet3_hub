#GET /donnees/usage/<annee>/<temps>/<station>
import requests
import json

#Split le string pour obtenir les valeurs demander
#url = ""
#res = urllib.urlopen(urllib)
#data = json.loads(res.read())

import pandas as pd
import matplotlib.pyplot as plt
import csv
"""
split = url.split("/")
year = split[2]   
time = split[3]
station = split[4]
"""


def donnee(annee, time, station):
    #annee = "TEST"
    #time = "parmois"
    #station = "6119"
    
    print("L'annee est: "+ annee)
    print("Le temps est:"+time)
    print("La station est:"+station)
    
    months = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"]
    weekDays = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
    hourDay = ["00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
               "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"]
    monthX= [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
    daysX = [1,2,3,4,5,6,7]
    Yaxis = list()
    Xaxis = list()
    
    if (time == "parmois"):
        print ("2) On regarde les valeurs parmois ")
        for month in months:
            compteur = 0;
            print("Mois:" + month)
            #with open('../Engin2/Data_TEST.csv') as f:
            with open('../Engin2/Data_'+ annee +'.csv') as f:
                reader = csv.DictReader(f)
                for row in reader:
                    if (row["start_month"] == month):
                        if (station == "toutes"):
                            compteur = compteur + 1
                        elif (row["start_station_code"] == station):
                            compteur = compteur + 1
            Yaxis.append(compteur)
        Xaxis.append('Janvier')
        Xaxis.append('Fevrier')
        Xaxis.append('Mars')
        Xaxis.append('Avril')
        Xaxis.append('Mai')
        Xaxis.append('Juin')
        Xaxis.append('Juillet')
        Xaxis.append('Aout')
        Xaxis.append('Septembre')
        Xaxis.append('Octobre')
        Xaxis.append('Novembre')
        Xaxis.append('Decembre')
    
    if (time == "parheure"):
        print ("2) On regarde les valeurs parheure ")
        for hour in hourDay:
            compteur = 0;
            print("Heure: " + hour)
            #with open('../Engin2/Data_TEST.csv') as f:
            with open('../Engin2/Data_'+ annee +'.csv') as f:
                reader = csv.DictReader(f)
                for row in reader:
                    if (row["start_hour"] == hour):
                        if (station == "toutes"):
                            compteur = compteur + 1
                        elif (row["start_station_code"] == station):
                            compteur = compteur + 1
            Yaxis.append(compteur)
            Xaxis.append(hour+" heure")
    
    if (time == "parjourdelasemaine"):
        print ("2) On regarde les valeurs parjourdelasemaine ")
        for day in weekDays:
            compteur = 0;
            print("Jour de la semaine: " + day)
            #with open('../Engin2/Data_TEST.csv') as f:
            with open('../Engin2/Data_'+ annee +'.csv') as f:
                reader = csv.DictReader(f)
                for row in reader:
                    if (row["day_of_week"] == day):
                        if (station == "toutes"):
                            compteur = compteur + 1
                        elif (row["start_station_code"] == station):
                            compteur = compteur + 1
            Yaxis.append(compteur)
        Xaxis.append("Lundi")
        Xaxis.append("Mardi")
        Xaxis.append("Mercredi")
        Xaxis.append("Jeudi")
        Xaxis.append("Vendredi")
        Xaxis.append("Samedi")
        Xaxis.append("Dimanche")
        
       
    print("3) Creation du graphe")
    #Creation du graphe resultant pour celui existant
    true_data = pd.DataFrame(data = {'temps': Xaxis, 'actual': Yaxis})
    
    # Plot all the data as lines
    plt.plot(true_data['temps'], true_data['actual'], 'b-', label  = 'utilisation', alpha = 1.0)
        
    # Formatting plot
    plt.legend(); plt.xticks(rotation = '60'); 
    
    # Lables and title    
    if(time == "parmois"):
        if(station == "toutes"):
            plt.xlabel('Mois'); plt.ylabel("Nombre d'emprunt"); plt.title("Utilisation par mois des stations"); 
        else:
            plt.xlabel('Mois'); plt.ylabel("Nombre d'emprunt"); plt.title("Utilisation par mois de la station "+station); 
            
    if(time == "parjourdelasemaine"):
        if(station == "toutes"):
            plt.xlabel('Jour de la semaine'); plt.ylabel("Nombre d'emprunt"); plt.title("Utilisation par jour de la semaine des stations"); 
        else:
            plt.xlabel('Jour de la semaine'); plt.ylabel("Nombre d'emprunt"); plt.title("Utilisation par jour de la semaine de la station "+station); 

    if(time == "parheure"):
         if(station == "toutes"):
            plt.xlabel('Heure'); plt.ylabel("Nombre d'emprunt"); plt.title("Utilisation par heure des stations"); 
         else:
            plt.xlabel('Heure'); plt.ylabel("Nombre d'emprunt"); plt.title("Utilisation par heure de la station "+station); 

    plt.savefig('engin2.png',  bbox_inches='tight')
    plt.clf()
    
    print("4) On convertit en base 64 notre image")
    import base64
    with open("engin2.png", "rb") as img_file:
        my_string = base64.b64encode(img_file.read())
    
    myJson = list()
    #affichage ="["
    if(time == "parmois"):
        for i in range(12):
            myJson.append([int(monthX[i]), int(Yaxis[i])])
            #affichage = affichage + ",["+str(monthX[i])+","+str(Yaxis[i])+"]"
        #affichage = affichage+"]"
    if(time == "parheure"):
        for i in range(24):
            myJson.append([int(hourDay[i]), int(Yaxis[i])])
    if(time == "parjourdelasemaine"):
        for i in range(7):
            myJson.append([int(daysX[i]), int(Yaxis[i])])
        
    #datas = "{'donnees' : " +affichage+", 'graphique': "+my_string.decode('utf-8')+"}"
    datas = json.dumps({"donnees" :  myJson, "graphique": my_string.decode('utf-8')})

    #print("Mes donnees sur json: " + myJson)
    print("5) Fin du programme")
    
    return datas


#donnee("TEST", "parjourdelasemaine", "toutes")


from flask import Flask, request

app = Flask(__name__)

@app.route("/")
def home():
    return "<h1>HELLO ENGIN 2<h1>"

@app.route("/envoi", methods=['POST'])
def home_test():
    return "<h1>You are in Engin2<h1>",200

@app.route("/donnees/usage/<annee>/<temps>/<station>", methods=['POST', 'GET'])
def search_station(annee, temps, station):
    if request.method == 'GET':
        annee = {annee}.pop()
        temps = {temps}.pop()
        station = {station}.pop()
        
        print ("1) On verifie la validite des requetes")
        if (annee != "2014" and annee != "2015" and annee != "2016" and annee != "2017"):
            print("L'annee est invalide")
            #return "Pas de donnee", 404
        
        if (temps != "parheure" and temps != "parjourdelasemaine" and temps != "parmois"):
            print("Le 'temps' est invalide")
            return "Mauvaise requete", 400
    
        exist = False
        with open('../Engin2/Data_TEST.csv') as f:
            reader = csv.DictReader(f)
            for row in reader:
                if(row["start_station_code"] == station):
                    exist = True
        if(exist == False and station != 'toutes'):
            print("La station est invalide")
            return "Pas de donnee", 404
        
        data = donnee(str(annee), str(temps), str(station))
        print (data)
        #Mettre l'erreur lorsque le return est 0
        return data, 200 
    else:
        return "Mauvaise requete", 400


if __name__ == "__main__":
    #app.run()
    app.run(port=5002)
