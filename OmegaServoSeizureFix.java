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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class OmegaServoSeizureFix extends OpMode {
	//we currently have problems with five servos plugged in and any one or up to
	//all of them plugged in so this is the test code to make sure that does not happen
	//test 1, five servos plugged in but only one initialized
	
	double pos = 0;
	Servo servo1;
	Servo servo3;
	@Override
	public void init() {
		servo1 = hardwareMap.servo.get("servo_1");
		servo3 = hardwareMap.servo.get("servo_3");

		servo1.scaleRange(0, 1);
	}
	
	
	public OmegaServoSeizureFix() {

	}



	@Override
	public void loop() {
		boolean zero = gamepad1.y;
		boolean quarter = gamepad1.b;
		boolean half = gamepad1.a;
		boolean threeFourths = gamepad1.x;
		boolean one = gamepad1.right_bumper;
		boolean stopRetract = gamepad1.dpad_right;
		boolean letRetract = gamepad1.dpad_left;
		if(zero){
			servo1.setPosition(0.0);
		}
		if(quarter){
			servo1.setPosition(0.25);
		}
		if(half){
			servo1.setPosition(0.5);
		}
		if(threeFourths){
			servo1.setPosition(0.75);
		}
		if(one){
			servo1.setPosition(1.0);
		}

		if(stopRetract){
			servo3.setPosition(1);
		}
		if(letRetract){
			servo3.setPosition(0);
		}
		if(!letRetract&&!stopRetract){
			servo3.setPosition(.5);
		}
		telemetry.addData("Position",servo1.getPosition());
		
	}


	/*@Override
	public void stop() {
	}*/
}
/*
 70		  x
----- = -----
 114	 100
 */