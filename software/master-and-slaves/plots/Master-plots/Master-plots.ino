#include <Wire.h>
#include <EasyTransferI2C_NL.h>

//create object
EasyTransferI2C_NL ETsend, ETrec;

struct ETsend_DATA_STRUCTURE{
  int servoval;
  int servoval2;
};

struct ETrec_DATA_STRUCTURE{
  byte loadf11;
  byte loadf12;
  byte loadf13;
  byte loadf14;
  byte loadf21;
  byte loadf22;
  byte loadf23;
  byte loadf31;
  byte loadf32;
  byte loadf33;
  byte loadf34;
  
};
//give a name to the group of data
ETrec_DATA_STRUCTURE recdata;
ETsend_DATA_STRUCTURE senddata;
byte loadf1[3];
byte loadf2[2];
byte loadf3[3];
void setup(){
  Wire.begin();
  Serial.begin(9600);
  //start the library, pass in the data details and the name of the serial port. Can be Serial, Serial1, Serial2, etc.
  ETsend.begin(details(senddata), &Wire);
  ETrec.begin(details(recdata), &Wire);
}

void loop(){
  senddata.servoval = analogRead(0); 
  senddata.servoval2 = analogRead(1);
  //sendSerial();
  for(int i = 1;i<4;i++) {
    ETsend.sendData(i);
  }
  if (ETrec.receiveData(1));
    loadf1[0]=recdata.loadf11;
    loadf1[1]=recdata.loadf12;
    loadf1[2]=recdata.loadf13;
    loadf1[3]=recdata.loadf14;
    delay(5);
  if (ETrec.receiveData(2));
    loadf2[0]=recdata.loadf21;
    loadf2[1]=recdata.loadf22;
    loadf2[2]=recdata.loadf23;
    delay(5);
  if (ETrec.receiveData(3));
    loadf3[0]=recdata.loadf31;
    loadf3[1]=recdata.loadf32;
    loadf3[2]=recdata.loadf33;
    loadf3[3]=recdata.loadf34;
    delay(5);
  printLoad();
}

void sendSerial() {
  Serial.print(senddata.servoval);
  Serial.print(":::");
  Serial.println(senddata.servoval2);
}

void printLoad() {
  //Serial.print("  Finger2:");
  Serial.print(loadf2[0]);
  Serial.print("\t");
  Serial.print(loadf2[1]);
  Serial.print("\t");
  Serial.print(loadf2[2]);
  //Serial.print("  Finger1:");
  Serial.print("\t");
  Serial.print(loadf1[0]);
  Serial.print("\t");
  Serial.print(loadf1[1]);
  Serial.print("\t");
  Serial.print(loadf1[2]);
  Serial.print("\t");
  Serial.print(loadf1[3]);
  //Serial.print("  Finger3:");
  Serial.print("\t");
  Serial.print(loadf3[0]);
  Serial.print("\t");
  Serial.print(loadf3[3]);
  Serial.print("\t");
  Serial.print(loadf3[1]);
  Serial.print("\t");
  Serial.println(loadf3[2]);
  Serial.print("\n");
}
