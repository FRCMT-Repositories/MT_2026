// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;


public class RobotContainer {

  private final TankSubsystem tank = new TankSubsystem();

  private final CommandXboxController driver =new CommandXboxController(0);

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
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
