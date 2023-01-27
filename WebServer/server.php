<?php

require('vendor/autoload.php');

use PhpMqtt\Client\ConnectionSettings;
use PhpMqtt\Client\MqttClient;

$balise = 5;

$server = 'broker.emqx.io';
$port = 1883;
$clientId = 'mqttx_dfdd38db';

$clean_session = false;
$mqtt_version = MqttClient::MQTT_3_1_1;

$connectionSettings = (new ConnectionSettings)
    ->setKeepAliveInterval(60)
    ->setLastWillTopic('mainVerte/In')
    ->setLastWillMessage('client disconnect')
    ->setLastWillQualityOfService(1);


$mqtt = new MqttClient($server, $port, $clientId, $mqtt_version);

$mqtt->connect($connectionSettings, $clean_session);
printf("client connected\n");


$mqtt->subscribe('mainVerte/Out', function ($topic, $message) use ($balise) {

    printf("Received message on topic [%s]: %s\n", $topic, $message);

    $obj = json_decode($message);
    echo $obj;

    $temperature = (int)$obj->temperature;
    $humidite = (int)$obj->humidite;
    $luminosite = (int)$obj->luminosite;

    $data = array("idBalise" => $balise, "degreCelsius" => $temperature, "humiditeExt" => $humidite, "luminosite" => $luminosite, "longitude" => 1, "latitude" => 1);

    $datajson = json_encode($data);
    echo "balisejson";
    echo $datajson;

    $curl = curl_init();

    curl_setopt_array($curl, array(
        CURLOPT_URL => 'https://lamainverte1.herokuapp.com/balises-data',
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_ENCODING => '',
        CURLOPT_MAXREDIRS => 10,
        CURLOPT_TIMEOUT => 0,
        CURLOPT_FOLLOWLOCATION => true,
        CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
        CURLOPT_CUSTOMREQUEST => 'POST',
        CURLOPT_POSTFIELDS => $datajson,
        CURLOPT_HTTPHEADER => array(
            'x-access-token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InRlc3QiLCJwYXNzd29yZCI6InRlc3QiLCJpYXQiOjE2NzQ4MDUzMzcsImV4cCI6MTY3NzM5NzMzN30.VLOeOtb4uWoJRUoc4M9-xkgGe9GM5cWGSACWAzaWNdE',
            'Content-Type: application/json'
        ),
    ));

    $response = curl_exec($curl);

    curl_close($curl);
    echo $response;

}, 0);


$mqtt->loop(true);

