// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import frc.robot.subsystems.SimElevator;
import frc.robot.subsystems.SimHand;
import frc.robot.subsystems.SimIntake;

public class RobotContainer {

  private final TankSubsystem tank = new TankSubsystem();

  private final CommandXboxController driver =new CommandXboxController(0);

  private SimIntake mSimIntake = new SimIntake(20, 1.5, 3);
  private SimElevator mSimElevator = new SimElevator(400, 1.5);
  private SimHand mSimHand = new SimHand(-180, 180, 1.5, 3);

  public RobotContainer() {
    configureBindings();

    tank.setDefaultCommand(tank.run(() -> {
      double forward = driver.getRawAxis(3);
      double backward = driver.getRawAxis(2);
      double rotation = driver.getRightX();
      tank.drive(forward, -backward, rotation);
    }));
  }

  private void configureBindings() {
    driver.start().onTrue(tank.runOnce(() -> tank.resetPose(new Pose2d(2.0, 4.0, new Rotation2d()))));

    driver.a().onTrue(Commands.runOnce(() -> mSimIntake.setPosition(20, 3)));
    driver.b().onTrue(Commands.runOnce(() -> mSimIntake.setPosition(0, 3)));

    driver.a().onTrue(Commands.runOnce(() -> mSimIntake.setVelocity(1)));
    driver.b().onTrue(Commands.runOnce(() -> mSimIntake.setVelocity(0)));

    driver.povUp().onTrue(mSimElevator.CMDsetPosition(400, 3));
    driver.povDown().onTrue(mSimElevator.CMDsetPosition(0, 3));

    driver.povLeft().onTrue(Commands.runOnce(() -> mSimHand.setAngle(-45, 3)));
    driver.povRight().onTrue(Commands.runOnce(() -> mSimHand.setAngle(45, 3)));

    driver.y().onTrue(Commands.runOnce(() -> mSimHand.setVelocity(2)));
    driver.x().onTrue(Commands.runOnce(() -> mSimHand.setVelocity(-2)));
    driver.b().onTrue(Commands.runOnce(() -> mSimHand.setVelocity(0)));

    driver.back().onTrue(Commands.runOnce(() -> mSimIntake.setPosition(0, 3)));
    driver.back().onTrue(Commands.runOnce(() -> mSimIntake.setVelocity(0)));
    driver.back().onTrue(Commands.runOnce(() -> mSimElevator.setPosition(0, 3)));
    driver.back().onTrue(Commands.runOnce(() -> mSimHand.setAngle(0, 3)));
    driver.back().onTrue(Commands.runOnce(() -> mSimHand.setVelocity(0)));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
