#include <Adafruit_MMA8451.h>
#include <Adafruit_Sensor.h>


Adafruit_MMA8451 mma = Adafruit_MMA8451();

double A[3] = {0.00,0.00,0.00};
//char BTRun = "0";
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  //start the mma and set range to 2g. 4g and 8g also available for higher prcision
  mma.begin();
  mma.setRange(MMA8451_RANGE_2_G);
}

void loop() {
  
  
  Serial.read();

  //reads the raw data from mma
  mma.read();
//get a new sensor event
  sensors_event_t event;
  mma.getEvent(&event);
// take the three values and shove them into an array
  A[0] = event.acceleration.x;
  A[1] = event.acceleration.y;
  A[2] = event.acceleration.z;
  /*if(Serial.available()){
  BTRun = Serial.read();
  }*/

//   if(BTRun == "1"){
  Serial.print("{");
  Serial.print(A[0]);
  Serial.print(",");
  Serial.print(A[1]);
  Serial.print(",");
  Serial.print(A[2]);
  // }
   //else if(BTRun =="0"){
    Serial.read();
  delay(200);
}
