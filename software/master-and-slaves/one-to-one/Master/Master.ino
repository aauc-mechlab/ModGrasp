#include <Wire.h>
#include <EasyTransferI2C.h>

//create object
EasyTransferI2C ET; 

struct SEND_DATA_STRUCTURE{

  int servoval;
  int servoval2;
};

//give a name to the group of data
SEND_DATA_STRUCTURE mydata;


void setup(){
  Wire.begin();
  Serial.begin(9600);
  //start the library, pass in the data details and the name of the serial port. Can be Serial, Serial1, Serial2, etc.
  ET.begin(details(mydata), &Wire);
}

void loop(){
  //this is how you access the variables. [name of the group].[variable name]
  mydata.servoval = analogRead(0); 
  //delay(5);
  mydata.servoval2 = analogRead(1);
  sendSerial();
  for(int i = 1;i<4;i++) {
    ET.sendData(i);
  }
  delay(15);
}

void sendSerial() {
  Serial.print(mydata.servoval);
  Serial.print(":::");
  Serial.println(mydata.servoval2);
}
