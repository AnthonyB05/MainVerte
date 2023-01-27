<?php

require('vendor/autoload.php');

use \PhpMqtt\Client\MqttClient;
use \PhpMqtt\Client\ConnectionSettings;

$server   = 'broker.emqx.io';
$port     = 1883;
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

$mqtt->subscribe('mainVerte/Out', function ($topic, $message) {
    printf("Received message on topic [%s]: %s\n", $topic, $message);
}, 0);

for ($i = 0; $i< 10; $i++) {
    $payload = array(
        'protocol' => 'tcp',
        'date' => date('Y-m-d H:i:s'),
        'url' => 'https://github.com/emqx/MQTT-Client-Examples'
    );
    $mqtt->publish(
    // topic
        'mainVerte/Out',
        // payload
        json_encode($payload),
        // qos
        0,
        // retain
        true
    );
    printf("msg $i send\n");
    sleep(1);
}

$mqtt->loop(true);

