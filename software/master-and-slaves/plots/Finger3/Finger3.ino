#include <Wire.h>
#include <EEPROM.h>
#include <EasyTransferI2C_NL.h>
#include <SoftwareServo.h>
SoftwareServo servo[4];
#define maxLoad 100
//create object
EasyTransferI2C_NL ETsend, ETrec; 

struct ETrec_DATA_STRUCTURE{
  int servoval;
  int servoval2;
};

struct ETsend_DATA_STRUCTURE{
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
  //int loadf1[3];
  //int loadf2[2];
  //int loadf3[3];
};


//give a name to the group of data
ETrec_DATA_STRUCTURE recdata;
ETsend_DATA_STRUCTURE senddata;
int averageLoad;
int mappedValue;

void setup(){
  Serial.begin(9600);
 int id= EEPROM.read(1023);
  for(int i = 0; i < 4;i++) {
    servo[i].attach(i+9);
    servo[i].setMaximumPulse(2400);
  }
  Wire.begin(id);
  Wire.onRequest(request);
  //start the library, pass in the data details and the name of the serial port. Can be Serial, Serial1, Serial2, etc. 
  ETsend.begin(details(senddata), &Wire);
  ETrec.begin(details(recdata), &Wire);
  //define handler function on receiving data
  Wire.onReceive(receive);
}

void loop() {
  //check and see if a data packet has come in. 
  for (int i = 0;i<4;i++) {
    if(i == 0) {
      senddata.loadf31 = getLoad(i);
    }
    if(i == 1) {
       senddata.loadf32 = getLoad(i);
    }
    if(i == 2) {
       senddata.loadf33 = getLoad(i);
    }
    if(i == 3) {
       senddata.loadf34 = getLoad(i);
    }
  }
  byte sumLoad = senddata.loadf31+senddata.loadf32+senddata.loadf33+senddata.loadf34;
  averageLoad = sumLoad/4;
  //send the current back to the master
  ETsend.sendData();
  //check and see if a data packet has come in. 
  if(ETrec.receiveData()){
     mappedValue = map(recdata.servoval, 0,1023,30,110);
     //this is how you access the variables. [name of the group].
     for (int i = 0;i < 4;i++) {
       if(i==0) {
         servo[i].write(map(recdata.servoval2, 0, 1023, 90,15));
       }
       if(i==1) {
         servo[i].write(map(mappedValue,30,110,10,80));
       }
       if(i==2) {
         servo[i].write(map(mappedValue,30,110,115,95));
       }
       if(i==3) {
         servo[i].write(map(mappedValue,30,110,90,80));
       }
       if (averageLoad>=maxLoad) {
         int value  = servo[i].read()+5;
         servo[i].write(value);   
       }  
     }
   }
  SoftwareServo::refresh();
}

void receive(int numBytes) {}

  const unsigned int NUM_SERVOS = 4;
  const unsigned int BUFFER_SIZE = 16;
  int buffer[NUM_SERVOS][BUFFER_SIZE];
  int buffer_pos[NUM_SERVOS] = { 0 };
//Buffer for getting average values
int getLoad(int pin) {
  delay(1);
  buffer[pin][buffer_pos[pin]] = analogRead(pin);
  buffer_pos[pin] = (buffer_pos[pin] + 1) % BUFFER_SIZE;
  long sum = 0;
  for (unsigned int i = 0; i < BUFFER_SIZE; i++)
  sum += buffer[pin][i];
  return round(sum / BUFFER_SIZE);
}

void request() {
  ETsend.flagSlaveSend();    //if the master requests it, set the flag so that ETout.sendData() works properly in the loop().
  ETsend.sendData();          //An I2C SLAVE can only address the master, so no address is requested
}

