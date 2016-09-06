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
public class OmegaAutoRegionals extends OpMode {
	//this program works but we don't have two 180 degree servos one for our arm var angle
	//and one for our climber drop so we will try to use a continuous servo for the climbers
	//see OmegaAutoContServo
	final static int encoderCPR = 1120;
	final static double gearRatio = 1;
	final static int wheelDiameter = 6;
	double distance = 12; //inches
	double pos = 0;
	int step = 0;

	final static double circumference = Math.PI * wheelDiameter;
	public double rotation = distance / circumference;
	public double counts = encoderCPR * rotation * gearRatio;

	DcMotor motorFrontRight;
	DcMotor motorBackRight;
	DcMotor motorFrontLeft;
	DcMotor motorBackLeft;

	Servo backDrive;
	Servo climbers;
	@Override
	public void init() {

		motorFrontLeft = hardwareMap.dcMotor.get("motorL1");
		motorFrontRight = hardwareMap.dcMotor.get("motorR1");
		motorBackLeft = hardwareMap.dcMotor.get("motorL2");
		motorBackRight = hardwareMap.dcMotor.get("motorR2");
		climbers = hardwareMap.servo.get("servo_1");
		backDrive = hardwareMap.servo.get("servo_4");

		motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
		motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

		motorFrontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorFrontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorBackLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		motorBackRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

		climbers.scaleRange(0,1);
	}
	public double getCounts(double dist){
		distance = dist;
		rotation = distance / circumference;
		counts = encoderCPR * rotation * gearRatio;
		return counts;
	}

	//changed from boolean to void for testing
	public void backward(int stp, double f){
		if(step == stp) {
			if (!backEncodersReached(f)) {
				backDrive.setPosition(0);
				motorFrontRight.setPower(-.25);
				motorFrontLeft.setPower(-.25);
				motorBackRight.setPower(-.25);
				motorBackLeft.setPower(-.25);
			} else {
				halt();
				step++;
			}
		}
	}
	public void moment(int stp, double time){
		if(step==stp){
			backDrive.setPosition(0);
			try {
				Thread.sleep((long)time*1000);
				step++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void halt(){
		motorFrontRight.setPower(0);
		motorFrontLeft.setPower(0);
		motorBackRight.setPower(0);
		motorBackLeft.setPower(0);
	}
	public void reset(){
		motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
	}
	public void revert()
	{
		motorFrontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorFrontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
	}

	public boolean backEncodersReached(double f){
		double cnt = getCounts(f);
		if(motorFrontLeft.getCurrentPosition()<= -cnt || motorFrontRight.getCurrentPosition() >= cnt) {
			return true;
		}
		else{
			return false;
		}
	}

	public OmegaAutoRegionals() {

	}



	@Override
	public void loop() {
		moment(0,10);
		backward(1, 55);// if 70 = 114 than x = 100

		telemetry.addData("Front Left Encoder", motorFrontLeft.getCurrentPosition());
		telemetry.addData("Front Left Motor Power", motorFrontLeft.getPower());
		telemetry.addData("Position",climbers.getPosition());
		telemetry.addData("Step",step);
	}


	@Override
	public void stop() {
	}
}
/*
 70		  x
----- = -----
 114	 100
 */