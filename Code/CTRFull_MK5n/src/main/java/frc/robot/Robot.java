// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends LoggedRobot {
  private Command m_autonomousCommand;
  private final RobotContainer m_robotContainer;

  private Field2d field = new Field2d();

  public static GenericEntry key1;
  public static GenericEntry key2;
  public static GenericEntry key3;

  public Robot() {
    m_robotContainer = new RobotContainer();

    key1 = Shuffleboard.getTab("CONFIG").add("key1", 0)
      .withWidget(BuiltInWidgets.kTextView).getEntry();

    key2 = Shuffleboard.getTab("CONFIG").add("key2", 0)
      .withWidget(BuiltInWidgets.kTextView).getEntry();

    key3 = Shuffleboard.getTab("CONFIG").add("key3", 0)
      .withWidget(BuiltInWidgets.kTextView).getEntry();

    Logger.recordMetadata("ProjectName", "MeuRobo");

    if (isReal()) {
        Logger.addDataReceiver(new WPILOGWriter("/home/lvuser/logs"));
        Logger.addDataReceiver(new NT4Publisher());
    } else {
        // SIMULAÇÃO
        Logger.addDataReceiver(new WPILOGWriter("logs"));
        Logger.addDataReceiver(new NT4Publisher());
    }

    Logger.start();
    Logger.recordOutput("ALOU", "alou");

    Pose2d robot = new Pose2d(2, 4, new Rotation2d(Math.PI));
    SmartDashboard.putData("FIELD", field);
    field.getObject("Robot").setPose(robot);
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      CommandScheduler.getInstance().schedule(m_autonomousCommand);
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}

  public static double[] getKeysDash(){
    return new double[] {
      key1.getDouble(0),
      key2.getDouble(0),
      key3.getDouble(0)};
  }
}
