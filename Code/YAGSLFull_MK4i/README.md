<h1>ENTENDENDO O CÓDIGO</h1>
<div align="justify">

⚠️ **ATENÇÃO**

> Estes passos servem apenas para nortear oque é desenvolvido para o funcionamento correto da simulação, considerando um novo template, ou seja, um código novo, caso queira utilizar o exemplo completo, os passos citados a seguir devem ser desconsiderados, visto que essas modificações já estão inclusas.

<h2>PASSO 1: AdvantageKit</h2>

Ao baixar o template desejado ou criar um novo projeto — seja ele baseado em Command Robot, Command Timed Skeleton ou qualquer outra estrutura — é altamente recomendável alterar a classe principal do robô para utilizar o `LoggedRobot`.
Essa alteração é fundamental para permitir a utilização do Logger do AdvantageKit, possibilitando o plot de sinais, variáveis e estados internos do robô durante sua execução.
Além de auxiliar diretamente no desenvolvimento e validação dos subsistemas, essa ferramenta também se torna extremamente importante para processos de debug e análise de desempenho. O AdvantageKit permite registrar informações em uma linha do tempo sincronizada, possibilitando revisões pós-teste e pós-partida, facilitando a identificação e correção de problemas encontrados durante o funcionamento do robô.

Para utilizar esta ferramenta, precisamos ter a biblioteca `AdvantageKit` instalada.

> A instalação do AdvatangeKit pode ser realizada na aba `WPILib Vendor Dependencies` presente no canto superior esquerdo do `WPILib VS Code`.

Esta etapa deve ser realizada como parte das boas práticas de desenvolvimento, sendo utilizada frequentemente para o envio de sinais, posições, estados e demais informações relevantes para dashboards e ferramentas de análise, como o AdvantageScope.
A utilização do Logger facilita significativamente o monitoramento e visualização do comportamento do robô durante testes e partidas, tornando o processo de desenvolvimento mais organizado, eficiente e intuitivo.

**Observação**

> O uso do Logger não é obrigatório para a validação da simulação. Para o funcionamento básico dos modelos e plots, é possível utilizar ferramentas como `SmartDashboard`, `Shuffleboard` ou qualquer outro sistema de publicação de dados.
> Entretanto, conforme mencionado anteriormente, o uso do AdvantageKit é altamente recomendado devido à praticidade, organização e aos recursos avançados que a ferramenta oferece, especialmente para debug, análise temporal de sinais e revisão pós-teste ou pós-partida.

<h2>PASSO 2: Looger e Odometria</h2>

Apartir de agora, entramos de fato em modificações no código, para comprovar o funcionamento.

<h3>Inicialização do Logger</h3>

No arquivo principal do projeto, `Robot.java`, altere a extensão da classe para `LoggedRobot`, permitindo que os logs e sinais do robô sejam corretamente enviados e registrados pelo AdvantageKit.
Além disso, também será necessário inicializar o sistema de logging dentro da classe `Robot`. Após realizar essa alteração, o próprio ambiente de desenvolvimento solicitará automaticamente os imports necessários para o funcionamento da biblioteca.

Ao final, a estrutura do código deverá ficar semelhante ao exemplo apresentado abaixo:

### Imports

```java
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;
```

### Extensão da classe

```java
public class Robot extends LoggedRobot {
```

### Inicialização do Logger

```java
public Robot() {
    m_robotContainer = new RobotContainer();

    Logger.recordMetadata("ProjectName", "MeuRobo");

    if (isReal()) {
        Logger.addDataReceiver(new WPILOGWriter("/home/lvuser/logs"));
        Logger.addDataReceiver(new NT4Publisher());
    } else {
        Logger.addDataReceiver(new WPILOGWriter("logs"));
        Logger.addDataReceiver(new NT4Publisher());
    }

    Logger.start();
}
```

**Explicação**

A classe principal do robô precisa herdar (`extends`) de `LoggedRobot` para que o AdvantageKit consiga interceptar e gerenciar automaticamente o sistema de logs do projeto. É essa extensão que adiciona ao robô toda a estrutura necessária para gravação, publicação e sincronização dos sinais utilizados pelo Logger.

Além disso, a inicialização utilizando `Logger.start()` é responsável por iniciar efetivamente o sistema de logging durante a execução do robô. Sem essa inicialização, mesmo que existam chamadas como `Logger.recordOutput()`, nenhuma informação será realmente enviada, gravada ou disponibilizada para ferramentas como o AdvantageScope.

### Envio da odometria

Por padrão, a biblioteca YAGSL já disponibiliza um método responsável por retornar a pose do robô dentro do arquivo `SwerveSubsystem.java`. Além disso, esse subsistema também possui um método `periodic()`, que é executado continuamente durante o funcionamento do robô.

Dessa forma, podemos aproveitar essa estrutura para realizar o envio (plot) das informações de odometria para o AdvantageScope.

```java
  @Override
  public void periodic() {
    swerveDrive.updateOdometry();

    if (visionDriveTest) {
        vision.updatePoseEstimation(swerveDrive);
    }

    Pose2d currentPose = this.getPose();
    Logger.recordOutput("ODOMETRY", currentPose);
    
  }
```

Note que, diferentemente do `SmartDashboard.put...`, o `Logger.recordOutput()` não precisa que informemos explicitamente o tipo de dado que será enviado. Isso acontece porque o Logger possui suporte automático para diferentes tipos de informações, sendo capaz de interpretar e registrar valores como números, booleanos, poses, arrays, estruturas geométricas e diversos outros objetos utilizados no WPILib.

Aproveitando que já estamos trabalhando nesta classe, também podemos definir uma posição inicial fixa para o robô durante a simulação, facilitando testes, validações e a visualização do comportamento dos subsistemas dentro do ambiente virtual.

```java
  @Override
  public void simulationPeriodic()
  {
    if(ctrInit){
        if(!isRedAlliance()) this.resetOdometry(new Pose2d(2, 4, new Rotation2d(Math.PI)));
        else this.resetOdometry(new Pose2d(13.5, 4, new Rotation2d(0)));
        ctrInit = false;
    }
  }
```

> A variável `ctrInit` consiste em um simples `private boolean ctrInit = true;` declarado globalmente, utilizado para garantir que determinadas instruções sejam executadas apenas no primeiro ciclo da simulação.
> Dessa forma, o preset inicial da odometria consegue seguir corretamente a lógica definida dentro do `simulationPeriodic()`, evitando conflitos entre a posição inicial configurada e as atualizações de movimento do robô durante a simulação.


<h2>PASSO 3: Movimentação</h2>

Garanta que os eixos do controle estejam corretamente associados aos movimentos do robô. Por padrão, a YAGSL já fornece algumas configurações de movimentação utilizando `SwerveInputStream`, normalmente localizadas no `RobotContainer`, contendo os principais bindings necessários para o controle do chassi.

Em alguns casos, determinados eixos do joystick podem apresentar movimentação invertida em relação ao comportamento esperado. Nessas situações, basta multiplicar o eixo correspondente por `-1`, invertendo sua direção de leitura, conforme demonstrado no exemplo abaixo:

```java
public class RobotContainer {
  final CommandXboxController Cmdriver = new CommandXboxController(0);

  public final SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve/MK4i"));

  private final SendableChooser<Command> autoChooser;

  SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> -Cmdriver.getLeftY(),
                                                                () -> -Cmdriver.getLeftX())
                                                            .withControllerRotationAxis(() -> (-Cmdriver.getRightX()) * 0.8)
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(0.8)
                                                            .allianceRelativeControl(true);
...
```

> [!WARNING]
> **⚠️ ATENÇÃO — ESTA CONFIGURAÇÃO NÃO AFETA A SIMULAÇÃO**
>
> O código disponibilizado foi originalmente testado e validado utilizando o robô **MIRAGE**, da equipe **FRC 9168**. Por esse motivo, os valores presentes no diretório `src/main/deploy/swerve/MK4i` estão diretamente relacionados à configuração física do chassi da equipe AGROBOT.
>
> Dessa forma, embora a simulação funcione corretamente, a utilização prática desse código em um robô real provavelmente não apresentará o comportamento esperado, a menos que os IDs CAN, relações mecânicas, offsets e valores de PID sejam idênticos aos utilizados no projeto original.

Para finalizar, também será necessário, dentro do método `configureBindings()`, criar o comando responsável por utilizar o `SwerveInputStream` citado anteriormente como sistema principal de controle do chassi.

Em seguida, definiremos esse comando como o comportamento padrão do drivetrain através do método `setDefaultCommand()`, garantindo que o robô permaneça constantemente recebendo os comandos de movimentação do controle durante sua execução.

Além disso, por questões de praticidade e boas práticas de desenvolvimento, também é recomendado associar um botão responsável por resetar a odometria do robô utilizando o método `zeroGyro()`, facilitando testes, reposicionamentos e validações durante a simulação.

```java
  private void configureBindings() {
    Command driveMode = drivebase.driveFieldOriented(driveAngularVelocity);

    drivebase.setDefaultCommand(driveMode);
    Cmdriver.start().onTrue((Commands.runOnce(drivebase::zeroGyro)));
...
```

<h2>PASSO 4: Validação</h2>
Agora que já configuramos a base do código, vamos iniciar a simulação.

<h3>4.1 - Inicialização</h3>

Clique no ícone da WPILib destacado em vermelho na imagem abaixo. Em seguida, pesquise por `simulate`, selecione a opção indicada pela seta verde — correspondente à mesma descrição apresentada — e, por fim, clique em `OK` para iniciar a simulação do projeto.

<table align="center">
<tr>
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/A_1.png" width="1000">
</tr>
</table>
Após essa etapa, o projeto será compilado automaticamente (`build`). Caso não haja erros durante a compilação, uma nova janela será exibida solicitando a utilização da Driver Station em conjunto com a simulação.

Habilite essa opção e, em seguida, clique em `OK` para continuar a execução da simulação do robô.

<table align="center">
<tr>
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/A_1_1.png" width="500">
</tr>
</table>

</div>



<b>
</b>













| Arquivo | Descrição |
|---|---|
| `config.json` | Conjunto de configurações responsáveis pela definição estrutural do modelo, incluindo nome do robô, posicionamento 3D do modelo principal, composição dos subsistemas (como intake, elevador e braço), além da configuração de elementos auxiliares, como câmeras e diferentes pontos de visualização utilizados na simulação. |
| `model.glb` | Modelo CAD base do projeto, contendo todos os elementos estruturais fixos do robô. |
| `model_0.glb` | Modelo CAD correspondente ao 1º componente do mecanismo. Neste exemplo, o componente representado é a gaveta do intake. |
| `model_1.glb` | Modelo CAD correspondente ao 2º componente do mecanismo. Neste exemplo, o componente representado é o coletor do intake. |
| `model_2.glb` | Modelo CAD correspondente ao 3º componente do mecanismo. Neste exemplo, o componente representado é o elevador. |
| `model_3.glb` | Modelo CAD correspondente ao 4º componente do mecanismo. Neste exemplo, o componente representado é o braço. |
| `model_4.glb` | Modelo CAD correspondente ao 5º componente do mecanismo. Neste exemplo, o componente representado é a 1ª roda de coleta do braço. |
| `model_5.glb` | Modelo CAD correspondente ao 6º componente do mecanismo. Neste exemplo, o componente representado é a 2ª roda de coleta do braço. |

**[Aprenda a configurar o .json e os models](https://github.com/FRCMT-Repositories/MT_2026/tree/main/Code/Advantage%20Scope/Robot_MTModelA)**

<h3>Adicionando ao Advantage Scope</h3>
<div align="justify">

Após a seleção do modelo, tornasse necessario copiar a pasta do modelo escolhido para o diretório autoAssets do Advantage Scope, geralmente presente no seguinte diretório `C:\Users\user\AppData\Roaming\AdvantageScope\autoAssets`.

**ATENÇÃO**

- Recomenda-se criar um atalho para esta pasta, pois ela será utilizada com frequência durante o desenvolvimento e validação da simulação dos subsistemas.

- Durante o uso do Advantage Scope, em algumas situações o software pode remover automaticamente a pasta do modelo previamente carregada, tornando necessário adicioná-la novamente. Por esse motivo, recomenda-se não mover a pasta original do modelo diretamente para o diretório do Advantage Scope. Em vez disso, crie uma cópia da pasta do modelo dentro do diretório utilizado pelo software, preservando assim os arquivos originais do projeto.

</div>

<h2>PASSO 2: Definição do módulo swerve </h2>
<div align="justify">

Nesta etapa, tornasse necessario escolher a biblioteca responsável por controlar o seu módulo swerve, atualmente temos 2 exemplos funcionais:

</div>

<div align="center">

| MÓDULO | BIBLIOTECA | TIPO DE MOTOR |
|:---:|:---:|:---:|
| [MK4i](https://github.com/FRCMT-Repositories/MT_2026/tree/main/Code/YAGSLFull_MK4i)| YAGSL | NEO e KRAKEN |
| [MK5n](https://github.com/FRCMT-Repositories/MT_2026/tree/main/Code/CTRFull_MK5n) | CTRe | KRAKEN |

</div>

<div align="justify">

De forma geral, a biblioteca YAGSL atende muito bem diferentes configurações de módulos swerve, oferecendo grande flexibilidade e facilidade de adaptação para diversos tipos de hardware.

Entretanto, quando o robô é composto majoritariamente por dispositivos CTRE, a biblioteca oficial da CTRE tende a apresentar uma estrutura mais limpa, organizada e otimizada, além de proporcionar melhor desempenho e integração nativa entre os componentes.

**Observação:**

A escolha da biblioteca de desenvolvimento fica inteiramente a critério de cada equipe. Caso optem por utilizar soluções diferentes das apresentadas neste repositório, sintam-se totalmente à vontade para adaptá-las conforme as necessidades e preferências do projeto.

A única exigência para a utilização correta da simulação do robô é a disponibilização do plot da odometria do robô. Em ambos os exemplos apresentados anteriormente, cada biblioteca possui, em seu escopo principal, a publicação das informações de odometria necessárias para integração com o Advantage Scope. Esses dados são fundamentais para permitir o posicionamento e movimentação correta do robô dentro do ambiente de simulação.

⚠️ **ATENÇÃO**

> É extremamente importante destacar que todas as bibliotecas disponibilizadas neste repositório foram testadas utilizando as versões referentes à temporada de 2026.
> Atualizações futuras de bibliotecas, como por exemplo a YAGSL, podem ocasionar incompatibilidades ou pequenas quebras no código original.
> Entretanto, na maioria dos casos, essas alterações podem ser corrigidas com pequenos ajustes e adaptações relacionadas às mudanças introduzidas pelas novas versões das bibliotecas.

</div>

<h2>PASSO 3: Definição do módulo swerve </h2>
<div align="justify">


</div>
