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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class Omega10 extends LinearOpMode {




	final static int ENCODER_CPR = 1120;
	final static double GEAR_RATIO = 1;
	final static int WHEEL_DIAMETER = 6;
	double DISTANCE = 12; //inches

	final static double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
	public double ROTATIONS = DISTANCE / CIRCUMFERENCE;
	public double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;

	/*
	retraction is geared at 1:2 for speed
	extension is not geared at 1:1
	 */

	DcMotor motorFrontRight;
	DcMotor motorBackRight;
	DcMotor motorFrontLeft;
	DcMotor motorBackLeft;

	DcMotor motorVarAngle;
	DcMotor motorE;
	DcMotor motorR1;
	DcMotor motorR2;






	/*
	 * Constructor
	 */
	public Omega10() {

	}

	@Override
	public void runOpMode() throws InterruptedException {

	}

	/*
         * Code to run when the op mode is initialized goes here
         *
         * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
         */
	public void reset_encoders()
	{
		motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motorE.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motorR1.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motorR2.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motorVarAngle.setMode(DcMotorController.RunMode.RESET_ENCODERS);
	}

	public void forward(double f)
	{
		do {
			motorFrontLeft.setPower(1);
			motorFrontRight.setPower(1);
			motorBackLeft.setPower(1);
			motorBackRight.setPower(1);
		}
		while (!have_encoders_reached(f*24));
		halt();
	}

	public void extend(double d){
		do{
			motorE.setPower(1);
		}while(!have_encoders_reached(d));
	}

	public void retract(double d){
		do{
			motorR1.setPower(-1);
			motorR2.setPower(-1);
		}while(!have_encoders_reached(d));
	}

	public void backward(double f)
	{
		do {
			motorFrontLeft.setPower(-1);
			motorFrontRight.setPower(-1);
			motorBackLeft.setPower(-1);
			motorBackRight.setPower(-1);
		}
		while (!have_encoders_reached(-f*24));
		halt();
	}

	public void halt()
	{
		motorFrontLeft.setPower(0);
		motorFrontRight.setPower(0);
		motorBackLeft.setPower(0);
		motorBackRight.setPower(0);
	}

	public void haltALL()
	{
		motorFrontLeft.setPower(0);
		motorFrontRight.setPower(0);
		motorBackLeft.setPower(0);
		motorBackRight.setPower(0);
		motorE.setPower(0);
		motorR1.setPower(0);
		motorR2.setPower(0);
		motorVarAngle.setPower(0);
	}

	public void turn_right(double f)
	{
		do {
			motorFrontLeft.setPower(1);
			motorFrontRight.setPower(1);
			motorBackLeft.setPower(-1);
			motorBackRight.setPower(-1);
		}while(!turnR_encoder_value(f));
		halt();
	}

	public void turn_left(double f)
	{
		do {
			motorFrontLeft.setPower(-1);
			motorFrontRight.setPower(-1);
			motorBackLeft.setPower(1);
			motorBackRight.setPower(1);
		}while(!have_encoders_reached(f));
		halt();
	}

	public boolean have_encoders_reached(double f){
		DISTANCE = f;
		if (motorBackLeft.getCurrentPosition() <= -COUNTS || motorBackRight.getCurrentPosition() >= COUNTS || motorFrontLeft.getCurrentPosition() <= -COUNTS || motorFrontRight.getCurrentPosition() >= COUNTS || motorE.getCurrentPosition() >= COUNTS){
			return true;
		}
		else{
			return false;
		}

	}
	public void revert_encoders(){
		motorFrontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorFrontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorBackLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorBackRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorE.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorR1.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorR2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorVarAngle.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
	}
	public boolean turnL_encoder_value(double f){
		DISTANCE = f;
		if (motorBackLeft.getCurrentPosition() >= COUNTS || motorBackRight.getCurrentPosition() >= COUNTS || motorFrontLeft.getCurrentPosition() >= COUNTS || motorFrontRight.getCurrentPosition() >= COUNTS){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean turnR_encoder_value(double f){
		DISTANCE = f;
		if (motorBackLeft.getCurrentPosition() >= COUNTS || motorBackRight.getCurrentPosition() <= -COUNTS || motorFrontLeft.getCurrentPosition() >= COUNTS || motorFrontRight.getCurrentPosition() <= -COUNTS){
			return true;
		}
		else{
			return false;
		}
	}

	public void Myinit() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */
		
		/*
		 * For the Omega System we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot at the front.
		 *   "motor_2" is on the left side of the bot at the front and reversed.
		 *   "motor_3" is on the right side of the bot at the back.
		 *   "motor_4" is on the left side of the bot at the back and reversed.
		 *   
		 *
		 */
		motorFrontLeft = hardwareMap.dcMotor.get("motor_1");
		motorFrontRight = hardwareMap.dcMotor.get("motor_2");
		motorBackLeft = hardwareMap.dcMotor.get("motor_3");
		motorBackRight = hardwareMap.dcMotor.get("motor_4");
		motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
		motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
		motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

		motorVarAngle = hardwareMap.dcMotor.get("motor_5");
		motorE = hardwareMap.dcMotor.get("motor_6");
		motorR1 = hardwareMap.dcMotor.get("motor_7");
		motorR2 = hardwareMap.dcMotor.get("motor_8");



	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */


	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */


}
