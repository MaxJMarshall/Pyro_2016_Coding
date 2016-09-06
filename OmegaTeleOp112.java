/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class OmegaTeleOp112 extends Omega112 {








	/*
	 * Constructor
	 */
	public OmegaTeleOp112() {

	}

	/*
	 * Code to run when the op mode is initialized goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */

	@Override
	public void init() {
		super.init();
	}

	/*
         * This method will be called repeatedly in a loop
         *
         * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
         */
	@Override
	public void loop() {

		/*
		 * Gamepad 1
		 * 
		 * Gamepad 1 controls the motors via the left and right stick, and it controls the
		 *
		 */

		// throttle: left_stick_y ranges from -1 to 1, where -1 is full forward, and
		// 1 is full backwards on the left side
		// direction: right_stick_y ranges from -1 to 1, where -1 is full forward, and
		// 1 is full backwards on the right side
		// extend: is positive or negative where it activates the extend and retract motors and moves the arm
		// forward if the button is pushed and sets the extend and retract motors
		// up: make the arm go up
		// down: makes the arm go down
		// rightZLArmO: puts the right arm to hit the climber zip lines out
		// rightZLArmI: pulls the right arm to hit the climber zip lines in
		// leftZLArmO: puts the  left arm to hit the climber zip lines out
		// leftZLArmI: pulls the  left arm to hit the climber zip lines in

		float throttleL = gamepad1.left_stick_y;
		float throttleR = gamepad1.right_stick_y;
		float extend = gamepad2.right_stick_y;
		boolean stayU = gamepad2.dpad_up;
		boolean stayD = gamepad2.dpad_down;
		boolean armU = gamepad2.y;
		boolean armD = gamepad2.a;

		boolean stayE = gamepad2.y;
		boolean stayR = gamepad2.a;


		// Clip the right/left values so that the values never exceed +/- 1
		throttleR = Range.clip(throttleR, -1, 1);
		throttleL = Range.clip(throttleL, -1, 1);

		extend = Range.clip(extend, -1, 1);

		// Scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		throttleL = (float) scaleInput(throttleL);
		throttleR = (float) scaleInput(throttleR);

		// Write the values to the motors
		motorFrontRight.setPower(throttleR);
		motorFrontLeft.setPower(throttleL);
		motorBackRight.setPower(throttleR);
		motorBackLeft.setPower(throttleL);

		motorE.setPower(extend);
		motorR1.setPower(-extend);

		motorE.setPower(-extend);
		motorR1.setPower(extend);
		// Sets the values for the encoders in relation to controller 2


		// Sets the arm variable motor to go up or down in relation to controller 2
		if(armU){
			motorArmUp.setPower(1);
			motorArmDown.setPower(-1);
		}
		if(armD){
			motorArmDown.setPower(1);
			motorArmUp.setPower(-1);
		}
		double y = 0;
		if(stayU){
			y = 0;
			y = y+.1;
			motorArmUp.setPower(y);
		}
		if(stayD){
			y = 0;
			y = y-.1;
			motorArmDown.setPower(y);
		}

		// Sets the arm extension and retraction in relation to controller 2
		double x = 0;
		if(stayR){
			x = 0;
			x = x+.1;
			motorR1.setPower(x);
		}
		if(stayE){
			x = 0;
			x = x+.1;
			motorE.setPower(x);
		}






		// Sets range limits for the extension and retraction of the arm
		do{
			motorE.setPower(-1);
		}while(motorE.getCurrentPosition() >= (1120)*7);

		do{
			motorR1.setPower(1);
		}while(motorR1.getCurrentPosition() <= 0);

		// Sets limits for the lift and lowering of the arm
		do{
			motorArmUp.setPower(-1);
		}while (motorArmUp.getCurrentPosition() >= 1120*7);

		do{
			motorArmUp.setPower(1);
		}while(motorArmUp.getCurrentPosition() <= 0);







		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
		telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", throttleL));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", throttleR));

	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}
	
	/*
	 * This method scales the joystick input so for low joystick values, the 
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.00, 0.05, 0.10, 0.15, 0.20, 0.25,
				0.30, 0.35, 0.40, 0.45, 0.50, 0.55, 0.65, 0.70, 0.75, 0.80, 0.95, 1.00};
		
		// get the corresponding index for the scaleInput array.
		int index = (int) (dVal * 16.0);
		if (index < 0) {
			index = -index;
		} else if (index > 16) {
			index = 16;
		}
		
		double dScale = 0.0;
		if (dVal < 0) {
			dScale = -scaleArray[index];
		} else {
			dScale = scaleArray[index];
		}
		
		return dScale;
	}

}
