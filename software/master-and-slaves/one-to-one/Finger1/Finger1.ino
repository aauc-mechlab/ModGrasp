#include <Wire.h>
#include <EEPROM.h>
#include <EasyTransferI2C.h>
#include <SoftwareServo.h>
SoftwareServo servo[4];
int current[5];
int mappedValue;
#define maxLoad 100
//create object
EasyTransferI2C ET; 

struct RECEIVE_DATA_STRUCTURE{
  int servoval;
  int servoval2;
};

//give a name to the group of data
RECEIVE_DATA_STRUCTURE mydata;


void setup(){
  Serial.begin(9600);
  int id= EEPROM.read(1023);
  for(int i = 0; i < 4;i++) {
    servo[i].attach(i+3);
    servo[i].setMaximumPulse(2400);
  }
  Wire.begin(id);
  //start the library, pass in the data details and the name of the serial port. Can be Serial, Serial1, Serial2, etc. 
  ET.begin(details(mydata), &Wire);
  //define handler function on receiving data
  Wire.onReceive(receive);
  
  
}

void loop() {
  for (int i = 0;i<4;i++) {
    current[i] = getCurrent(i);
  }
  int sumCurrent = current[0]+current[1]+current[2];
  int averageCurrent = sumCurrent/4;
  //check and see if a data packet has come in. 

  if(ET.receiveData()){
     mappedValue = map(mydata.servoval, 0,1023,30,110);
     //this is how you access the variables. [name of the group].[variable name]
     for (int i = 0;i < 4;i++) {
       if(i == 0) {
         int mappedValue2 = map(mydata.servoval2, 0, 1023, 175,90);
         servo[0].write(mappedValue2);
       }
       if(i==3) {
         servo[i].write(map(mappedValue,30,110,70,80));
       }
       if(i==2) {
         servo[i].write(map(mappedValue,30,110,60,80));
       }
       if(i==1) {
         servo[i].write(map(mappedValue,30,110,10,80));
       }
       if (averageCurrent>=maxLoad) {
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
int getCurrent(int pin) {
  delay(1);
  buffer[pin][buffer_pos[pin]] = analogRead(pin);
  buffer_pos[pin] = (buffer_pos[pin] + 1) % BUFFER_SIZE;
  long sum = 0;
  for (unsigned int i = 0; i < BUFFER_SIZE; i++)
  sum += buffer[pin][i];
  return round(sum / BUFFER_SIZE);
}
