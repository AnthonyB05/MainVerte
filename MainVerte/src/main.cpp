/**********************************************************************
   Contrôle d'un servomoteur au moyen d'une page Web.
   ESP32 et ESP8266
   https://electroniqueamateur.blogspot.com/2018/09/servomoteur-controle-par-wi-fi-esp8266.html
***********************************************************************/

// inclusion des bibliothèques utiles
#if defined ARDUINO_ARCH_ESP8266  // s'il s'agit d'un ESP8266
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#elif defined ARDUINO_ARCH_ESP32  // s'il s'agit d'un ESP32
#include "WiFi.h"
#include <WebServer.h>
#endif

#include <Servo.h>  // s'il s'agit d'un ESP32, il faut installer une version spéciale
                    // de la bibliothèque servo: 
                    // https://github.com/RoboticsBrno/ESP32-Arduino-Servo-Library


// le servomoteur est contrôlé par les GPIO 4 de l'ESP8266
#define pinServo 2

#if defined ARDUINO_ARCH_ESP8266  // s'il s'agit d'un ESP8266
ESP8266WebServer server(80);
#elif defined ARDUINO_ARCH_ESP32  // s'il s'agit d'un ESP32
WebServer server(80);
#endif

Servo monServo;  //création de l'objet servo

// La variable qui indique la position désirée pour le servomoteur (dépend du bouton coché sur la page web)
String positionSTR = "1";          // peut prendre les valeurs 1, 2 ou 3.

/* La fonction construitPage retourne un string qui contient toute notre page web  */

/**********************************************************************
 * DHT SENSOR
 ***********************************************************************/
#include <Arduino.h>
#include "DHT.h"

DHT dht(D3, DHT11);
Servo myservo;

/**********************************************************************
 * Lum SENSOR
 ***********************************************************************/

#define CS 15 // Assignment of the CS pin

#include <SPI.h>


byte intensity;

/**********************************************************************
 * MQTT
 ***********************************************************************/

#include "Arduino.h"
#include "ESP8266WiFi.h"
#include "PubSubClient.h"
#include "NTPClient.h"
#include "WiFiUdp.h"

const char* ssid = "SER@YNOV";//"AndroidAPe5e7";
const char* password = "SteGYdUR227NtpEXT";//"tsmk8952";
const char* mqtt_server = "broker.emqx.io";
const uint16_t mqtt_server_port = 1883;
const char* mqttUser = "";
const char* mqttPassword = "";
const char* mqttTopicIn = "mainVerte/In";
const char* mqttTopicOut = "mainVerte/Out";

WiFiClient wifiClient;
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP);
PubSubClient mqttClient(wifiClient);

unsigned long lastMsg = 0;
char msg[75];
int value = 0;
int motor = 5;

void connect() {
  while (!mqttClient.connected()) {
    Serial.print("Attempting MQTT connection...");
    String mqttClientId = "mqttx_e4c58dfb";
    if (mqttClient.connect(mqttClientId.c_str())) {
      Serial.println("connected");
      mqttClient.subscribe(mqttTopicIn);
    } else {
      Serial.print("failed, rc=");
      Serial.print(mqttClient.state());
      Serial.println(" will try again in 5 seconds");
      delay(5000);
    }
  }
}

void setup_wifi() {
  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  timeClient.begin();
  Serial.println("WiFi connected");
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived on topic: '");
  Serial.print(topic);
  Serial.print("' with payload: ");
  for (unsigned int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();
  String myCurrentTime = timeClient.getFormattedTime();
  mqttClient.publish(mqttTopicOut,("Time: " + myCurrentTime).c_str());
}



String construitPage() {

  String bouton1Str, bouton2Str;

  // pour que le bon bouton demeure coché:

  if (positionSTR == "1") {
    bouton1Str = "checked";
  }
  if (positionSTR == "2") {
    bouton2Str = "checked";
  }


  String page = "<html lang=fr-FR><head>";
  page += "<title>ESP8266 Servomoteur</title>";
  page += "<style> body { background-color: #fffff; font-family: Arial, Helvetica, Sans-Serif; Color: #000088; }</style>";
  page += "</head><body><h1>ESP8266 Servomoteur</h1>";
  page += "<form action='/' method='POST'>";
  page += "<h3>Position du servomoteur:</h3>";
  page += "<p><INPUT type='radio' name='position' value='1' " + bouton1Str + ">A";
  page += "<INPUT type='radio' name='position' value='2' " + bouton2Str + ">B</p>";

  page += "<INPUT type='submit' value='Appliquer'><br><br>";
  page += "</body></html>";
  return page;
}


/* Contrôle du servomoteur */
void gestionMoteur() {

  int laPosition;

  if (positionSTR == "1") {
    laPosition = 0;
  }

  if (positionSTR == "2") {
    laPosition = 199;
  }


  monServo.write(laPosition);
}
/*  La fonction gestionPage modifie les caractéristiques du moteur si le bouton
    Appliquer a été cliqué. */
void gestionPage() {
  if ( server.hasArg("position") ) {
    positionSTR = server.arg("position");

    Serial.print("Commande recue.  Position: ");
    Serial.println(positionSTR);

    gestionMoteur();
  }
  server.send ( 200, "text/html", construitPage() );

}


void initMoteur(){
  monServo.attach(pinServo);

  // initialisation de la communication WiFi
  WiFi.begin ( ssid, password );
  while ( WiFi.status() != WL_CONNECTED ) {
    delay ( 500 ); Serial.print ( "." );
  }
  Serial.println ( "" );
  Serial.print ( "Maintenant connecte a " );
  Serial.println ( ssid );
  Serial.print ( "Adresse IP: " );
  Serial.println ( WiFi.localIP() );

  // On indique le nom de la fonction qui gère l'interraction avec la page web
  server.on ( "/",  gestionPage );

  server.begin();
  Serial.println ( "Serveur HTTP en fonction" );
}

void initDHTSensor(){
  Serial.println(F("DHTxx test!"));
  dht.begin();

  myservo.attach(9);
}

void initLumSensor(){
  SPI.begin(); // initialization of SPI port
  SPI.setDataMode(SPI_MODE0); // configuration of SPI communication in mode 0
  SPI.setClockDivider(SPI_CLOCK_DIV16); // configuration of clock at 1MHz
  pinMode(CS, OUTPUT); //configure pin connected to chip select as output
}

void setup() {

  // pour affichage dans le moniteur série
  Serial.begin ( 115200 );
  setup_wifi();
  mqttClient.setServer(mqtt_server, mqtt_server_port);
  mqttClient.setCallback(callback);
  initMoteur();
  initDHTSensor();
  initLumSensor();

}

void DHTSensor(int intensity){
  delay(2000);

  // Reading temperature or humidity takes about 250 milliseconds!

  // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
  float h = dht.readHumidity();
  // Read temperature as Celsius (the default)
  float t = dht.readTemperature();
  // Read temperature as Fahrenheit (isFahrenheit = true)
  float f = dht.readTemperature(true);

  // Check if any reads failed and exit early (to try again).
  if (isnan(h) || isnan(t) || isnan(f)) {
    Serial.println(F("Failed to read from DHT sensor!"));
    return;
  }

  // Compute heat index in Fahrenheit (the default)
  float hif = dht.computeHeatIndex(f, h);
  // Compute heat index in Celsius (isFahreheit = false)
  float hic = dht.computeHeatIndex(t, h, false);

  Serial.print(F("Humidity: "));
  Serial.print(h);
  Serial.print(F("%  Temperature: "));
  Serial.print(t);
  Serial.print(F("°C "));
  Serial.print(f);
  Serial.print(F("°F  Heat index: "));
  Serial.print(hic);
  Serial.print(F("°C "));
  Serial.print(hif);
  Serial.println(F("°F"));

  snprintf(msg, 75,"{\"temperature\":\"%.2f\",\"humidite\":\"%.2f\",\"luminosite\":\"%d\"}", t, h, intensity);

  Serial.print("Publish message: ");
    Serial.println(msg);
    mqttClient.publish(mqttTopicOut, msg);
}

void MotorSensor(){
  server.handleClient();
}

int LumSensor(){
  intensity = 0;
  digitalWrite(CS, LOW); // activation of CS line
  intensity = SPI.transfer(0) << 3; // Aquisition of first 5 bits of data without leading zeros
  intensity |= (SPI.transfer(0) >> 4); //Aquisition of last 3 bits of data and appending
  digitalWrite(CS, HIGH);

  //display result
  Serial.print("Light intensity = ");
  Serial.println(intensity);
  delay(1000);
  return intensity;
}

void loop() {

  if (!mqttClient.connected()) {
    connect();
  }
  mqttClient.loop();
  timeClient.update();

 unsigned long now = millis();
 MotorSensor();

  if (now - lastMsg > 2000) {
    lastMsg = now;
    ++value;

    int intensity = LumSensor();
    DHTSensor(intensity);

    Serial.print(value);
    Serial.print("--");
    Serial.print(motor);
    Serial.print("/////");

    // if(value == motor){

    //   if(positionSTR.equals("1")){
    //     Serial.print("position 1: ");
    //     positionSTR = "2";
    //     gestionMoteur();
    //     motor = motor+10;
    //   }else{
    //     Serial.print("position 2: ");
    //     positionSTR = "1";
    //     gestionMoteur();
    //     motor = motor+5;
    //   }

    // }
  }
}