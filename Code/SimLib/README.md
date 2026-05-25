<!-- LIBRARY -->
> [!WARNING]
> **LINHA DE RACIOCÍNIO E PRÉ-REQUISITOS**
>
> Os procedimentos descritos a seguir consideram que todas as etapas de validação apresentadas na seção `Comprovação do Funcionamento` já foram devidamente concluídas nos respectivos exemplos específicos das bibliotecas `YAGSL` ou `CTRE`.
>
> Dessa forma, antes de prosseguir, recomenda-se garantir que o drivetrain, a odometria e os controles básicos já estejam funcionando corretamente, evitando inconsistências durante as próximas etapas da simulação e integração dos subsistemas.
>
> Também estamos considerando que a pasta `Robot_MTModelA` ou `Robot_MTModelB` já foi corretamente adicionada ao AdvantageScope, conforme demonstrado na seção [`Adicionando ao AdvantageScope`](https://github.com/FRCMT-Repositories/MT_2026/tree/main/Code#adicionando-ao-advantage-scope).

<div align="justify">

<h2>PASSO 1: Fundamentação</h2>

Para confirmar que o modelo foi carregado corretamente e que a integração está funcionando como esperado, vamos substituir o modelo padrão (`KitBot`) pelo modelo personalizado selecionado anteriormente.

Não há problema caso o modelo apareça inicialmente “quebrado” ou desalinhado, como demonstrado na imagem abaixo. Isso é esperado em alguns momentos da configuração inicial e será corrigido nas próximas etapas.

Para alterar o modelo exibido no AdvantageScope, basta clicar no ícone indicado pela seta vermelha na imagem abaixo.

<table align="center">

<tr>

<td align="center" width="800">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/A_9_.png" width="800">
</td>

</tr>

</table>

Caso o modelo não esteja sendo exibido corretamente, é muito provável que a pasta do modelo não tenha sido adicionada ao AdvantageScope ou que tenha sido removida automaticamente pelo software em algum momento, conforme mencionado anteriormente.

Nessas situações, basta verificar novamente se a pasta `Robot_MTModelA` ou `Robot_MTModelB` continua presente no diretório utilizado pelo AdvantageScope e, se necessário, realizar a importação do modelo novamente.

<h3>Entendendo a Biblioteca</h3>

Dentro de cada classe da biblioteca existe um construtor responsável por receber os parâmetros iniciais de funcionamento do subsistema, como por exemplo valores de `kP`, posição inicial do mecanismo e leituras reais provenientes de encoders ou sensores físicos.

Cada subsistema também possui um método `periodic()`, responsável por executar continuamente a lógica de atualização e movimentação dos mecanismos simulados. Dentro dessas classes, existe ainda um controlador PID individual para cada componente. Esse PID não tem como objetivo representar o controle real do robô, mas sim suavizar e controlar a velocidade de atualização visual dos subsistemas durante a simulação, permitindo uma melhor visualização e compreensão do comportamento dos mecanismos.

Além disso, cada `periodic()` também é responsável por realizar o plot da posição 3D do componente no AdvantageScope. Todos os cálculos, transformações e ferramentas utilizadas serão explicados individualmente em cada exemplo de subsistema.

A biblioteca também possui alguns métodos auxiliares importantes:

- `map()`  
  Responsável por escalonar valores reais para os valores utilizados na simulação, permitindo relacionar corretamente o comportamento físico do robô com sua representação virtual.

- `config()`  
  Utilizado para configurar parâmetros internos do subsistema, como valores de PID (`kP`, `kD`, etc.) ou outras configurações desejadas.

- `getRawPosition()` / `getRawAngle()`  
  Responsáveis por retornar diretamente os valores de posição ou ângulo utilizados internamente pelo modelo 3D da simulação.

- `getRealPosition()` / `getRealAngle()`  
  Retornam os valores reais escalonados do mecanismo, considerando as relações e limites definidos no construtor da classe.

- `setPosition()` / `setAngle()`  
  Responsáveis por definir a posição ou ângulo do componente com base nos valores reais do mecanismo físico.

- `setVelocity()`  
  Responsável por definir a velocidade de rotação de mecanismos contínuos, como rodas de intake, coletores ou componentes que giram em torno do próprio eixo.

<h2>PASSO 2: Intake</h2>

<h3>Direto ao ponto</h3>

O arquivo `SimIntake.java` contém toda a estrutura e parametrização necessária para controlar os componentes `model_0` — responsável pela gaveta/articulação do intake — e `model_1` — correspondente ao coletor do intake.

Isso significa que, ao adicionar essa classe ao projeto e instanciar seu objeto dentro do `RobotContainer`, já será possível simular os acionamentos e movimentações do sistema de intake diretamente no AdvantageScope.

```java
private SimIntake mSimIntake = new SimIntake(20, 1.5, 3);
```

</div>

> [!TIP]
> **PARAMETRÔS**
>
> 20 = Valor "real" do encoder com meu sistema acionado no maximo, esse valor é varivel em função de cada robô.
>
> 1.5 = Valor do kP para o sistema de movimento do intake.
>
> 3 = Valor do kP para o sistema de coleta do intake.


<div align="justify">

Dentro do `RobotContainer`, mais especificamente no método `configureBindings()`, podemos definir os comandos responsáveis pelo acionamento e controle do intake, conforme demonstrado no exemplo abaixo:

```java
  private void configureBindings() {
    Cmdriver.a().onTrue(Commands.runOnce(() -> mSimIntake.setPosition(20, 3)));		// Move o intake para o máximo
    Cmdriver.a().onTrue(Commands.runOnce(() -> mSimIntake.setVelocity(1)));			// Aciona o coletor do intake na velocidade 1
    Cmdriver.b().onTrue(Commands.runOnce(() -> mSimIntake.setPosition(0, 3)));		// Move o intake para a posição minima
    Cmdriver.b().onTrue(Commands.runOnce(() -> mSimIntake.setVelocity(0)));			// Desliga o coletor do intake
    Cmdriver.back().onTrue(Commands.runOnce(() -> mSimIntake.setPosition(0, 3)));	// Move o intake para a posição minima
    Cmdriver.back().onTrue(Commands.runOnce(() -> mSimIntake.setVelocity(0)));		// Desliga o coletor do intake

...
```

Para visualizar o movimento dos mecanismos durante a simulação, é necessário associar os componentes ao modelo do robô dentro do AdvantageScope.

Para isso, abra o AdvantageScope e localize os plots relacionados ao intake no seguinte diretório:

`AdvantageKit` → `SubSystemSim` → `Intake`

Após localizar a pasta do intake, estarão disponíveis os plots `Coletor` e `Gaveta`, responsáveis pela simulação 3D dos mecanismos do sistema de intake.

Para validar o funcionamento do mecanismo, basta arrastar o plot `Gaveta` para dentro do modelo principal do robô, previamente inserido na etapa anterior. Após essa associação, o componente passará a acompanhar dinamicamente os movimentos enviados pela simulação.

<table align="center">

<tr>

<td align="center" width="800">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/IntakeComponent.gif" width="800">
</td>

</tr>

</table>

</div>

> [!IMPORTANT]
> **SEQUÊNCIA DE ASSOCIAÇÃO DOS COMPONENTES**
>
> É extremamente importante respeitar a ordem de inserção dos componentes conforme definida nos arquivos `model.glb` e `config.json`.
>
> Cada componente do modelo 3D possui um identificador específico (`model_0`, `model_1`, `model_2`, etc.), e esses identificadores precisam corresponder exatamente aos parâmetros configurados no `config.json`.
>
> Por exemplo, neste projeto:
>
> - `model_0` → corresponde à `Gaveta`
> - `model_1` → corresponde ao `Coletor`
>
> Da mesma forma, no arquivo `config.json`, o `model_0` possui os parâmetros 3D relacionados à gaveta, enquanto o `model_1` está associado ao coletor.
>
> Caso a ordem seja alterada incorretamente, os movimentos da simulação serão aplicados nos componentes errados, causando comportamentos visuais inconsistentes dentro do AdvantageScope.

<div align="justify">

<table align="center">

<tr>

<td align="center" width="800">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/IntakeCalc/IntakeFuncionando.gif" width="800">
</td>

</tr>

</table>

- Ao pressionar o botão A o intake deve avançar e acionar o rolete de coleta
- Ao pressionar o botão B o intake deve recolher e desligar o rolete de coleta

<h3>Por trás da classe</h3>

Agora vamos desmembrar o funcionamento interno da classe do intake, entendendo como métodos como `setPosition()` e `setVelocity()` operam, não apenas do ponto de vista lógico, mas também como essas informações são convertidas em movimentações reais dos componentes 3D dentro do AdvantageScope.

Conforme explicado anteriormente na fundamentação da biblioteca, a estrutura da classe é baseada em métodos padronizados, como:

- `set...`
- `get...`
- `config()`
- `periodic()`

Os métodos `set...` são responsáveis por definir estados e comportamentos dos mecanismos, enquanto os métodos `get...` retornam informações relacionadas às posições, ângulos e valores processados pela simulação.

Entretanto, é dentro do método `periodic()` que toda a lógica principal acontece. É nele que os cálculos de posicionamento, interpolação, suavização de movimento e atualização dos modelos 3D são efetivamente executados, permitindo que os componentes simulados acompanhem dinamicamente os valores enviados pelo código.

```java
    @Override
    public void periodic() {

        intakeMove();
        intakeVelocity();

        double Intake_CO = CurrentPosition * Math.sin(Math.toRadians(5.71));
        double Intake_CA = CurrentPosition * Math.cos(Math.toRadians(5.71));

        Logger.recordOutput("SubSystemSim/Intake/Velocity", CurrentVelocity);

        Logger.recordOutput("SubSystemSim/Intake/Gaveta", new Pose3d[] { new Pose3d(
            Intake_CA, 0.0, -Intake_CO,new Rotation3d(0.0, 0, 0))});

        Logger.recordOutput("SubSystemSim/Intake/Coletor", new Pose3d[] { new Pose3d(
            Intake_CA + 0.256619 , 0.0, -Intake_CO + 0.214588 , new Rotation3d(0, Rotation, 0))});
    }
```

```java
    private void intakeMove(){
        OutPosition = intakePID.calculate(CurrentPosition, SetPosition);
        CurrentPosition += OutPosition * 0.02;
        CurrentPosition = MathUtil.clamp(CurrentPosition, Intake_Zero, Intake_Foward);
    }
```

```java
    private void intakeVelocity(){
        double intakeVelocityOutput = intakeVelocity.calculate(CurrentVelocity, SetpointVelocity);
        CurrentVelocity += intakeVelocityOutput * 0.02;

        Rotation = Rotation + (0.1 * CurrentVelocity);
        if(Rotation > Math.PI || Rotation < -Math.PI){
            Rotation = 0;
        }
    }
```

As funções `intakeMove()` e `intakeVelocity()` são responsáveis principalmente pelo processamento e suavização dos movimentos simulados através de controladores PID.

A função `intakeMove()` realiza o cálculo do PID utilizando um feedback hipotético de posição, já que, dentro da simulação, não existe um sensor físico retornando a posição real atual do mecanismo. Dessa forma, o próprio sistema mantém internamente uma estimativa da posição simulada do componente.

Já a função `intakeVelocity()` é utilizada para controlar mecanismos com rotação contínua, como rodas de coleta. Nesse caso, o valor de rotação (`Rotation`) é reiniciado periodicamente para evitar o acúmulo infinito de ângulo ao longo do tempo.

Isso é necessário porque, após completar uma rotação completa de `360°`, o componente pode reiniciar naturalmente sua referência angular, evitando inconsistências numéricas e facilitando o controle visual da simulação.

Posto isso, vamos agora entender os cálculos responsáveis pelo posicionamento 3D do componente dentro da simulação.

De forma simplificada, o principal conceito matemático utilizado neste sistema é a trigonometria. No caso específico da gaveta do intake, o movimento executado pelo mecanismo se comporta de maneira semelhante ao deslocamento de uma hipotenusa.

Estamos considerando que os arquivos `.glb` foram exportados corretamente conforme a orientação descrita na seção [`Exportação do .glb`](https://github.com/FRCMT-Repositories/MT_2026/tree/main/CAD).

O primeiro ponto importante a observar é que o movimento da gaveta ocorre de forma diagonal. Dessa maneira, precisamos determinar o curso total do mecanismo para calcular corretamente o quanto o componente poderá se deslocar dentro da simulação.

Para isso, foi medida:
- a posição do sistema em repouso
- a posição do sistema totalmente avançado
- o ângulo do intake em relação ao chão

<table align="center">

<tr>

<td align="center" width="250">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/IntakeCalc/IntakeMedida2.png" width="250">
</td>

<td align="center" width="350">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/IntakeCalc/IntakeMedida1.png" width="350">
</td>

<td align="center" width="250">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/IntakeCalc/IntakeAngulo_.png" width="250">
</td>

</tr>

</table>

Em seguida, realizamos a diferença entre essas duas posições `386.7 - 176.7`, obtendo um curso total de `210 mm`.

Como o sistema de poses 3D utilizado pela programação trabalha em metros, convertemos esse valor para:

```java
private final static double Intake_Foward = 0.210;
```

Com o curso máximo definido e conhecendo o ângulo de inclinação do intake, agora já podemos aplicar os cálculos trigonométricos necessários para determinar os deslocamentos nos eixos `X` e `Y` do objeto 3D.

Quando o mecanismo está em repouso, ambos os deslocamentos possuem valor `0`. Entretanto, para simular corretamente o avanço diagonal da gaveta — semelhante ao comportamento de uma hipotenusa — precisamos calcular os valores correspondentes ao:
- Cateto Oposto (`CO`)
- Cateto Adjacente (`CA`)

Para isso, utilizamos as seguintes equações trigonométricas:

```java
        double Intake_CO = CurrentPosition * Math.sin(Math.toRadians(5.71));
        double Intake_CA = CurrentPosition * Math.cos(Math.toRadians(5.71));
```

Nesse caso:
- CurrentPosition representa a extensão atual do mecanismo
- 5.71° corresponde ao ângulo de inclinação do intake

Observe também que utilizamos Math.toRadians(). Isso é necessário porque as funções trigonométricas da biblioteca Java (Math.sin() e Math.cos()) trabalham utilizando ângulos em radianos, e não em graus.

Além disso, o próprio sistema de rotações 3D utilizado pelo Pose3d e Rotation3d dentro do WPILib também utiliza radianos como unidade padrão para representação angular.

Com os valores calculados, já podemos realizar o plot da posição do componente 3D dentro do AdvantageScope:

```java
        Logger.recordOutput("SubSystemSim/Intake/Gaveta", new Pose3d[] { new Pose3d(
            Intake_CA, 0.0, -Intake_CO, new Rotation3d(0.0, 0.0, 0.0))});
```

Os valores de Rotation estão zerados pois, já estão parametrizados no arquivo `config.json`.

Observe que o valor do Cateto Oposto está invertido `-Intake_CO`. Isso ocorre porque, ao avançar o mecanismo, a extremidade do intake tende a se aproximar do chão, fazendo com que o deslocamento no eixo vertical `Z` aconteça no sentido negativo dentro do sistema de coordenadas 3D utilizado pela simulação.

Com esse sistema funcionando corretamente, agora podemos aplicar o mesmo conceito ao coletor, porém com alguns pontos importantes de atenção.

Diferentemente da gaveta do intake — que foi exportada considerando como referência o centro do robô — mecanismos que realizam rotação em torno do próprio eixo precisam possuir seu ponto de origem (`offset`) diretamente associado ao eixo do próprio componente.

Ou seja:
- na gaveta, o deslocamento foi calculado em função do robô
- no coletor, a rotação precisa acontecer em função do eixo central do próprio mecanismo

Sabendo disso, precisamos determinar corretamente:
- a distância do eixo `Z`, considerando a posição inicial do componente em repouso (`posição 0`)
- e a distância entre o centro do robô e o eixo central do coletor, também com o mecanismo em sua posição inicial de repouso

Esses valores serão fundamentais para posicionar corretamente o modelo 3D e garantir que a rotação aconteça exatamente sobre o eixo físico esperado do mecanismo dentro da simulação.

<table align="center">

<tr>

<td align="center" width="400">
	<img src="https://github.com/FRCMT-Repositories/.github/blob/main/profile/IntakeCalc/IntakeMedidasColetor.png" width="400">
</td>

</tr>

</table>

Com esses valores definidos, agora já podemos criar o objeto 3D do coletor, aplicando apenas os offsets iniciais correspondentes às posições dos eixos `X` e `Z` do rolete.

Além disso, como o coletor está fisicamente acoplado à gaveta do intake, o componente também passará a acompanhar automaticamente todo o movimento executado pela gaveta durante sua extensão e retração.

Dessa forma, o mecanismo será corretamente posicionado dentro da simulação, respeitando tanto sua localização física no robô quanto o eixo real de rotação do componente.

```java
        Logger.recordOutput("SubSystemSim/Intake/Coletor", new Pose3d[] { new Pose3d(
            Intake_CA + 0.27168 , 0.0, -Intake_CO + 0.21308 , new Rotation3d(0.0, Rotation, 0.0))});
```
Nesse trecho:
- `Intake_CA` representa o deslocamento horizontal da gaveta
- `-Intake_CO` representa o deslocamento vertical da gaveta no sentido do chão
- `0.27168` é o offset inicial do coletor no eixo X
- `0.21308` é o offset inicial do coletor no eixo Z
- `Rotation` representa a rotação contínua do coletor em torno do próprio eixo

<h2>PASSO 3: Elevator</h2>

<h3>Direto ao ponto</h3>

O arquivo `SimElevator.java` contém toda a estrutura e parametrização necessária para controlar o componente `model_2` — responsável pela elevador.

Isso significa que, ao adicionar essa classe ao projeto e instanciar seu objeto dentro do `RobotContainer`, já será possível simular os acionamentos e movimentações do sistema do elevador diretamente no AdvantageScope.

```java
private SimElevator mSimElevator = new SimElevator(400, 1.5);
```

</div>

> [!TIP]
> **PARAMETRÔS**
>
> 400 = Valor "real" do encoder com meu sistema acionado no maximo, esse valor é varivel em função de cada robô.
>
> 1.5 = Valor do kP para o sistema de movimento do elevador.

<div align="justify">

Dentro do `RobotContainer`, mais especificamente no método `configureBindings()`, podemos definir os comandos responsáveis pelo acionamento e controle do elevador, conforme demonstrado no exemplo abaixo:

```java
  private void configureBindings() {
    Cmdriver.povUp().onTrue(mSimElevator.CMDsetPosition(400, 3));	// Move o elevador para a posição maxima
    Cmdriver.povDown().onTrue(mSimElevator.CMDsetPosition(0, 3));	// Move o elevador para a posição minima
    Cmdriver.back().onTrue(Commands.runOnce(() -> mSimElevator.setPosition(0, 3)));	// Move o elevador para a posição minima

...
```

Para visualizar o movimento dos mecanismos durante a simulação, é necessário associar os componentes ao modelo do robô dentro do AdvantageScope.

Para isso, abra o AdvantageScope e localize os plots relacionados ao intake no seguinte diretório:

`AdvantageKit` → `SubSystemSim` → `Intake`

Após localizar a pasta do intake, estarão disponíveis os plots `Coletor` e `Gaveta`, responsáveis pela simulação 3D dos mecanismos do sistema de intake.

Para validar o funcionamento do mecanismo, basta arrastar o plot `Gaveta` para dentro do modelo principal do robô, previamente inserido na etapa anterior. Após essa associação, o componente passará a acompanhar dinamicamente os movimentos enviados pela simulação.



</div>




