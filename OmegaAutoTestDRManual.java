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
import com.qualcomm.robotcore.hardware.Servo;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class OmegaAutoTestDRManual extends OpMode {
	Servo climbers;

	public void drop(){
		if(climbers.getPosition()<.9) {
			climbers.setPosition(1);
		}
		else{
			climbers.setDirection(Servo.Direction.REVERSE);
		}
	}
	public void resetClimbers(){
		if(climbers.getPosition()>1){
			climbers.setDirection(Servo.Direction.REVERSE);
		}
	}
	public OmegaAutoTestDRManual() {

	}


	@Override
	public void init() {
		climbers = hardwareMap.servo.get("servo_1");
	}


	@Override
	public void loop() {
		boolean zero = gamepad1.y;
		boolean quarter = gamepad1.b;
		boolean half = gamepad1.a;
		boolean threeFourths = gamepad1.x;
		boolean one = gamepad1.right_bumper;
		if(zero){
			climbers.setPosition(0.0);
		}
		if(quarter){
			climbers.setPosition(0.25);
		}
		if(half){
			climbers.setPosition(0.5);
		}
		if(threeFourths){
			climbers.setPosition(0.75);
		}
		if(one){
			climbers.setPosition(1.0);
		}
		telemetry.addData("Position",climbers.getPosition());
	}


	@Override
	public void stop() {

	}
}