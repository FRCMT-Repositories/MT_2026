package frc.robot;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TankSubsystem extends SubsystemBase {

    // -----------------------------
    // ODOMETRIA
    // -----------------------------

    private double leftDistanceMeters = 0.0;
    private double rightDistanceMeters = 0.0;

    private Rotation2d heading = new Rotation2d();

    private final DifferentialDriveOdometry odometry =
        new DifferentialDriveOdometry(
            heading,
            leftDistanceMeters,
            rightDistanceMeters,
            new Pose2d()
        );

    // -----------------------------
    // SIMULAÇÃO
    // -----------------------------

    private double leftVelocity = 0.0;
    private double rightVelocity = 0.0;

    private double previousTime = Timer.getFPGATimestamp();

    // Distância entre lado esquerdo e direito do robô
    private static final double TRACK_WIDTH = 0.60;

    // Velocidade máxima simulada
    private static final double MAX_SPEED = 3.0;

    public TankSubsystem() {}

    public void drive(double forward, double backward, double rotation) {

        forward = MathUtil.applyDeadband(forward, 0.08);
        backward = MathUtil.applyDeadband(backward, 0.08);
        rotation = MathUtil.applyDeadband(rotation, 0.08);

        double leftCommand = (forward + backward) + rotation;
        double rightCommand = (forward + backward) - rotation;

        leftCommand = MathUtil.clamp(leftCommand, -1.0, 1.0);
        rightCommand = MathUtil.clamp(rightCommand, -1.0, 1.0);

        leftVelocity = leftCommand * MAX_SPEED;
        rightVelocity = rightCommand * MAX_SPEED;
    }

    public void stop() {
        leftVelocity = 0.0;
        rightVelocity = 0.0;
    }

    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    public void resetPose(Pose2d pose) {
        leftDistanceMeters = 0.0;
        rightDistanceMeters = 0.0;

        heading = pose.getRotation();

        odometry.resetPosition(
            heading,
            leftDistanceMeters,
            rightDistanceMeters,
            pose
        );
    }

    @Override
    public void periodic() {

        double currentTime = Timer.getFPGATimestamp();
        double dt = currentTime - previousTime;
        previousTime = currentTime;

        leftDistanceMeters += leftVelocity * dt;
        rightDistanceMeters += rightVelocity * dt;

        double omega = (rightVelocity - leftVelocity)/ TRACK_WIDTH;

        heading = heading.plus(Rotation2d.fromRadians(omega * dt));

        Pose2d pose = odometry.update(
            heading,
            leftDistanceMeters,
            rightDistanceMeters
        );

        Logger.recordOutput("ODOMETRY", pose);
        Logger.recordOutput("Tank/LeftDistance",leftDistanceMeters);
        Logger.recordOutput("Tank/RightDistance",rightDistanceMeters);
        Logger.recordOutput("Tank/Heading",heading.getDegrees());
        Logger.recordOutput("Tank/LeftVelocity",leftVelocity);
        Logger.recordOutput("Tank/RightVelocity",rightVelocity);
    }
}