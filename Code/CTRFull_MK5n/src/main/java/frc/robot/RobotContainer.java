// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
// import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.SimElevator;
import frc.robot.subsystems.SimHand;
import frc.robot.subsystems.SimIntake;

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

  // public SubSystemSIM mSim = new SubSystemSIM();
  private SimIntake mSimIntake = new SimIntake(20, 1.5, 3);
  private SimElevator mSimElevator = new SimElevator(400, 1.5);
  private SimHand mSimHand = new SimHand(-180, 180, 1.5, 3);

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

    Cmdriver.a().onTrue(Commands.runOnce(() -> mSimIntake.setPosition(20, 3)));
    Cmdriver.b().onTrue(Commands.runOnce(() -> mSimIntake.setPosition(0, 3)));

    Cmdriver.a().onTrue(Commands.runOnce(() -> mSimIntake.setVelocity(1)));
    Cmdriver.b().onTrue(Commands.runOnce(() -> mSimIntake.setVelocity(0)));

    Cmdriver.povUp().onTrue(mSimElevator.CMDsetPosition(400, 3));
    Cmdriver.povDown().onTrue(mSimElevator.CMDsetPosition(0, 3));

    Cmdriver.povLeft().onTrue(Commands.runOnce(() -> mSimHand.setAngle(-45, 3)));
    Cmdriver.povRight().onTrue(Commands.runOnce(() -> mSimHand.setAngle(45, 3)));

    Cmdriver.y().onTrue(Commands.runOnce(() -> mSimHand.setVelocity(2)));
    Cmdriver.x().onTrue(Commands.runOnce(() -> mSimHand.setVelocity(-2)));
    Cmdriver.b().onTrue(Commands.runOnce(() -> mSimHand.setVelocity(0)));

    Cmdriver.back().onTrue(Commands.runOnce(() -> mSimIntake.setPosition(0, 3)));
    Cmdriver.back().onTrue(Commands.runOnce(() -> mSimIntake.setVelocity(0)));
    Cmdriver.back().onTrue(Commands.runOnce(() -> mSimElevator.setPosition(0, 3)));
    Cmdriver.back().onTrue(Commands.runOnce(() -> mSimHand.setAngle(0, 3)));
    Cmdriver.back().onTrue(Commands.runOnce(() -> mSimHand.setVelocity(0)));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }

  /**
   * Executa um path desejado.
   * @param pathName String do path que deseja executar.
   * @return movimentos e ações presentes no path.
   */
  private Command followPath(String pathName) {
      return Commands.defer(() -> {
          try {
              PathPlannerPath path = PathPlannerPath.fromPathFile(pathName);
              return AutoBuilder.followPath(path);
          } catch (Exception e) {
              DriverStation.reportError("path Error: " + pathName, e.getStackTrace());
              return Commands.none();
          }
      }, java.util.Set.of(driveBase));
  }

  /**
   * Ativa o comando alvo, quando a condição é verdadeira.
   * @param null
   */
  private void onCommandCondition(BooleanSupplier condition, Command command) {
      new Trigger(condition).onTrue(command);
  }

  /**
   * Matém um comando ativo, enquanto a condição é verdade.
   * @param null
   */
  private void whileCommandCondition(BooleanSupplier condition, Command command) {
      new Trigger(condition).whileTrue(command);
  }
}
