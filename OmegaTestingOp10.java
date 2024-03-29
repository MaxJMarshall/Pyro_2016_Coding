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
public class OmegaTestingOp10 extends OmegaTeleOp10{












	/**
	 * Constructor
	 */
	public OmegaTestingOp10(){

	}


	@Override
	public void init() {


	}

	public void start()
	{

	}


	@Override
	public void loop() {

        telemetry.addData("time",this.time);
        telemetry.addData("Run Mode", motorBackLeft.getMode());
        telemetry.addData("Back Left Motor", motorBackLeft.getCurrentPosition());
        telemetry.addData("Front Left Motor", motorFrontLeft.getCurrentPosition());
        telemetry.addData("Back Right Motor", motorBackRight.getCurrentPosition());
        telemetry.addData("Front Right Motor", motorFrontRight.getCurrentPosition());


		float throttleL = gamepad1.left_stick_y;
		float throttleR = gamepad1.right_stick_y;


		// clip the right/left values so that the values never exceed +/- 1
		throttleR = Range.clip(throttleR, -1, 1);
		throttleL = Range.clip(throttleL, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		throttleL = (float)scaleInput(throttleL);
		throttleR =  (float)scaleInput(throttleR);

		// write the values to the motors
		motorFrontRight.setPower(throttleR);
		motorFrontLeft.setPower(throttleL);
		motorBackRight.setPower(throttleR);
		motorBackLeft.setPower(throttleL);


	}

	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

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

	
	@Override
	public void stop() {

	}

}
