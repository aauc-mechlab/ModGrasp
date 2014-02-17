#include <EEPROM.h>
#define ID 3

void setup() {
  EEPROM.write(1023,ID);
}

void loop() {}
