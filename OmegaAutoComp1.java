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
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class OmegaAutoComp1 extends LinearOpMode {

	DcMotor motorFrontRight;
	DcMotor motorBackRight;
	DcMotor motorFrontLeft;
	DcMotor motorBackLeft;

	Servo climbers;

	final static int ENCODER_CPR = 1120;
	final static double GEAR_RATIO = 1;
	final static int WHEEL_DIAMETER = 6;
	double DISTANCE = 12; //inches

	final static double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
	public double ROTATIONS = DISTANCE / CIRCUMFERENCE;
	public double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
	/**
	 * Constructor
	 */
	public OmegaAutoComp1() {

	}

	/*
	 * Code to run when the op mode is initialized goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */

	public void myInit() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */
		
		/*
		 * For the Omega System we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot and reversed.
		 *   "motor_3" is on the right side of the bot.
		 *   "motor_4" is on the left side of the bot and reversed.
		 *
		 */
		motorFrontLeft = hardwareMap.dcMotor.get("motor_1");
		motorFrontRight = hardwareMap.dcMotor.get("motor_2");
		motorBackLeft = hardwareMap.dcMotor.get("motor_3");
		motorBackRight = hardwareMap.dcMotor.get("motor_4");
		climbers = hardwareMap.servo.get("servo_1");
		motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
		motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
		motorFrontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorFrontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorBackLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		motorBackRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
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
	public boolean have_encoders_reached(double f){
		DISTANCE = f;
		if (motorFrontLeft.getCurrentPosition() >= COUNTS || motorFrontRight.getCurrentPosition() >= COUNTS){
			return true;
		}
		else{
			return false;
		}

	}
	public void drop(){
		climbers.setPosition(1);
	}
	public void halt()
	{
		motorFrontLeft.setPower(0);
		motorFrontRight.setPower(0);
		motorBackLeft.setPower(0);
		motorBackRight.setPower(0);
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
	public void reset_encoders()
	{
		motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
	}
	public void revert_encoders()
	{
		motorFrontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorFrontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
	}
	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */

	public void runOpMode()throws InterruptedException {

		/*
		 * Gamepad 1 controls the drive motors via the left and right stick
		 *
		 * Gamepad 2 controls the arm via the
		 */

		// throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
		// 1 is full down
		// direction: right_stick_y ranges from -1 to 1, where -1 is full left
		// and 1 is full right
		waitForStart();
		reset_encoders();
		sleep(1000);
		revert_encoders();
		backward(5.32);
		drop();
		stop();




		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */



	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */

	
	/*
	 * This method scales the joystick input so for low joystick values, the 
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
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

}
