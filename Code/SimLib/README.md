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
> 20 = Valor real do encoder com meu sistema acionado no maximo, esse valor é varivel em função de cada robô.
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



</div>

