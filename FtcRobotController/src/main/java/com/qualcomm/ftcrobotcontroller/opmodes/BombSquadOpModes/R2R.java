package com.qualcomm.ftcrobotcontroller.opmodes.BombSquadOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.DcMotor;


public class R2R extends LinearOpMode {

    DcMotor right;
    DcMotor left;
    double speed = 1;
    double targetHeading = 0.0;
    double gain = 0.1;
    double steeringError;
    double leftPower;
    double rightPower;
    int currentHeading = 0;
    double steeringAdjustment = 0;

    double a = 4.0;
    double c = 4.0;
    double d = 4.0;

    @Override
    public void runOpMode() throws InterruptedException {

        right = hardwareMap.dcMotor.get("m1");
        left = hardwareMap.dcMotor.get("m2");
        right.setDirection(DcMotor.Direction.REVERSE);
        GyroSensor sensorGyro;


        // write some device information (connection info, name and type)
        // to the log file.
        hardwareMap.logDevices();

        // get a reference to our GyroSensor object.
        sensorGyro = hardwareMap.gyroSensor.get("gyro");

        // calibrate the gyro.
        sensorGyro.calibrate();

        // wait for the start button to be pressed.
        waitForStart();

        // make sure the gyro is calibrated.
        while (sensorGyro.isCalibrating()) {
            Thread.sleep(50);
        }

        this.resetStartTime();


        while (opModeIsActive()) {

            // get the heading info.
            // the Modern Robotics' gyro sensor keeps
            // track of the current heading for the Z axis only.
            currentHeading = sensorGyro.getHeading();
            if (currentHeading > 180) {
                currentHeading -= 360;
            }

            steeringError = currentHeading - targetHeading;

            steeringAdjustment = steeringError * gain;

            rightPower = (speed + steeringAdjustment);
            leftPower = (speed - steeringAdjustment);

            if (rightPower < 0.0) {
                rightPower = 0.0;
            }
            if (leftPower < 0.0) {
                leftPower = 0.0;
            }
            if (rightPower > 1.0) {
                rightPower = 1.0;
            }
            if (leftPower > 1.0) {
                leftPower = 1.0;
            }

            /* if (this.getRuntime() > a){

                speed = 0.3;
                targetHeading -= 45;

                right.setPower(rightPower);
                left.setPower(leftPower);


            } else {

                speed = 1;
                right.setPower(rightPower);
                left.setPower(leftPower);
            } */


            telemetry.addData("1. h", String.format("%03d", currentHeading));
            telemetry.addData("2. th", String.format("%03d", targetHeading));

            Thread.sleep(10);
        }
        while (opModeIsActive()) {

            right.setPower(rightPower);
            left.setPower(leftPower);
            sleep(4000);

            targetHeading -= 90;
            right.setPower(rightPower);
            left.setPower(leftPower);
            sleep(5000);

            right.setPower(0);
            left.setPower(0);
            sleep(5000);

            break;

        }

    }
}