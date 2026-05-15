// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.XboxController;
// import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.SubSystemSIM;

public class RobotContainer {
  public double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);
  private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

  // // o robo ira dirigir de acordo com o campo.
  private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
      .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1)
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

  final CommandXboxController Cmdriver = new CommandXboxController(0);
  final XboxController driver = new XboxController(0);
  
  // private SendableChooser<Command> autoChooser = new SendableChooser<>();
  private CommandSwerveDrivetrain driveBase = TunerConstants.createDrivetrain();

  public SubSystemSIM mSim = new SubSystemSIM();

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    driveBase.setDefaultCommand(driveBase.applyRequest(() -> { return drive
      .withVelocityX(-driver.getLeftY() * MaxSpeed * driver.getRightTriggerAxis())
      .withVelocityY(-driver.getLeftX() * MaxSpeed * driver.getRightTriggerAxis())
      .withRotationalRate(-driver.getRightX() * MaxSpeed);
    }));

    Cmdriver.start().onTrue(driveBase.runOnce(() -> driveBase.configAngleInit()));
    Cmdriver.rightBumper().whileTrue(driveBase.brakeX().onlyIf(()-> true));

    Cmdriver.a().onTrue(Commands.runOnce(() -> mSim.setIntakePosition(50, 3)));
    Cmdriver.b().onTrue(Commands.runOnce(() -> mSim.setIntakePosition(0, 3)));

    Cmdriver.a().onTrue(Commands.runOnce(() -> mSim.setIntakeVelocity(2)));
    Cmdriver.b().onTrue(Commands.runOnce(() -> mSim.setIntakeVelocity(0)));

    Cmdriver.povUp().onTrue(Commands.runOnce(() -> mSim.setElevatorPosition(1000, 3)));
    Cmdriver.povDown().onTrue(Commands.runOnce(() -> mSim.setElevatorPosition(0, 3)));

    Cmdriver.povLeft().onTrue(Commands.runOnce(() -> mSim.setHandPosition(90, 3)));
    Cmdriver.povRight().onTrue(Commands.runOnce(() -> mSim.setHandPosition(-90, 3)));

    Cmdriver.back().onTrue(Commands.runOnce(() -> mSim.setHandPosition(0, 3)));
    Cmdriver.back().onTrue(Commands.runOnce(() -> mSim.setElevatorPosition(0, 3)));
    Cmdriver.back().onTrue(Commands.runOnce(() -> mSim.setIntakePosition(0, 3)));
    Cmdriver.back().onTrue(Commands.runOnce(() -> mSim.setIntakeVelocity(0)));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
