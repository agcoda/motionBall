#include <Adafruit_MMA8451.h>
#include <Adafruit_Sensor.h>

//Have to use Serial1 for the pro micro, switch to //! if debugging with regular ard

Adafruit_MMA8451 mma = Adafruit_MMA8451();

double A[3] = {0.00,0.00,0.00};
//char BTRun = "0";
void setup() {
  //!Serial.begin(9600);
  Serial1.begin(9600);
  //start the mma and set range to 2g. 4g and 8g also available for higher prcision
  mma.begin();
  mma.setRange(MMA8451_RANGE_2_G);
}

void loop() {
    
  //!Serial.read();
  Serial1.read();

  //reads the raw data from mma
  mma.read();
//get a new sensor event
  sensors_event_t event;
  mma.getEvent(&event);
// take the three values and shove them into an array
  A[0] = event.acceleration.x;
  A[1] = event.acceleration.y;
  A[2] = event.acceleration.z;
  /*if(Serial1.available()){
  BTRun = Serial1.read();
  }*/

/* //!
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
    */

//   if(BTRun == "1"){
  Serial1.print("{");
  Serial1.print(A[0]);
  Serial1.print(",");
  Serial1.print(A[1]);
  Serial1.print(",");
  Serial1.print(A[2]);
  // }
   //else if(BTRun =="0"){
    Serial1.read();
  delay(200);
}
