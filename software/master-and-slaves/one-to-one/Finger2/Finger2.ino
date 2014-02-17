#include <Wire.h>
#include <EasyTransferI2C.h>
#include <SoftwareServo.h>
#include <EEPROM.h>
SoftwareServo servo[3];
int current[4];
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
  for(int i = 0; i < 3;i++) {
    servo[i].attach(i+8);
    servo[i].setMaximumPulse(2400);
  }
  Wire.begin(id);
  //start the library, pass in the data details and the name of the serial port. Can be Serial, Serial1, Serial2, etc. 
  ET.begin(details(mydata), &Wire);
  //define handler function on receiving data
  Wire.onReceive(receive);

}

void loop() {
  for (int i = 0;i<3;i++) {
    current[i] = getCurrent(i);
  }
  int sumCurrent = current[0]+current[1]+current[2];
  int averageCurrent = sumCurrent/3;
 // Serial.println(averageCurrent);
  //check and see if a data packet has come in. 
  if(ET.receiveData()){
     mappedValue = map(mydata.servoval, 0,1023,30,110);
     Serial.println(mappedValue);
     for (int i = 0;i < 3;i++) {
       if(i==0) {
         servo[i].write(map(mappedValue,30,110,10,80));
       }
       if(i==1) {
         servo[i].write(map(mappedValue,30,110,60,80));
       }
       if(i==2) {
         servo[i].write(map(mappedValue,30,110,70,80));
       }
       if (averageCurrent>=maxLoad) {
         int value  = servo[i].read()+5;
         //Serial.println(value); 
         servo[i].write(value);     
      }
     }     
    }
  SoftwareServo::refresh();
}

void receive(int numBytes) {}

  const unsigned int NUM_SERVOS = 3;
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
