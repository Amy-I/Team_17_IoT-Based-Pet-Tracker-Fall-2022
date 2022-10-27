void setup() {
  // put your setup code here, to run once:
    Serial.begin(4800);
}

void loop() {
  // put your main code here, to run repeatedly:
  char message = "$GPRMC,120241.00,A,5323.2228,N,00635.1483,W,21.8,221.6,260206,,*1A";
  Serial.print(message[1]);
}
