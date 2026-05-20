<h2>SIMULAÇÃO - PASSO A PASSO</h2> 
<table align="center">

<tr>

<td align="center" width="700">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/Vista1.png" width="250">
</td>

<td align="center" width="700">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/Simulation.gif" width="250">
</td>

<td align="center" width="700">
	<img src="https://raw.githubusercontent.com/FRCMT-Repositories/.github/main/profile/Vista2.png" width="250">
</td>

</tr>

<tr>

 <td align="center" width="700"><b style="font-size:22px;">VISTA 1</b></td>
 <td align="center" width="700"><b style="font-size:22px;">SIMULAÇÃO DOS SUBSISTEMAS</b></td>
 <td align="center" width="700"><b style="font-size:22px;">VISTA 2</b></td>

</tr>
</table>


<h2>PASSO 1: Definição do Model</h2>
<div align="justify">

Dentro da pasta **Advantage Scope** existem duas pastas principais:
- **[Robot_MTModelA](https://github.com/FRCMT-Repositories/MT_2026/tree/main/Code/Advantage%20Scope/Robot_MTModelA)**
- **[Robot_MTModelB](https://github.com/FRCMT-Repositories/MT_2026/tree/main/Code/Advantage%20Scope/Robot_MTModelB)**

Ambas as pastas contêm todos os componentes necessários para a simulação dos mecanismos do robô no AdvantageScope.

A principal diferença entre os modelos está no CAD utilizado para o chassi:
- O **Model A** utiliza módulos swerve **MK4i**
- O **Model B** utiliza módulos swerve **MK5n**

A escolha entre os modelos afeta apenas a representação visual e estética do robô durante a simulação, não influenciando no funcionamento da programação, lógica de controle ou comportamento dos subsistemas.

<h3>Entendendo os arquivos dos Models A e B</h3>
</div>

| Arquivo | Descrição |
|---|---|
| `config.json` | Conjunto de informações do modelo, como nome do robô, posicionamento 3D do modelo principal, componentes dos subsistemas como intake, elevador e braço, além do posicionamento de vistas, como câmeras acopladas ao robô. |
| `model.glb` | Modelo CAD base do projeto, contendo todos os elementos estruturais fixos do robô. |
| `model_0.glb` | Modelo CAD do 1° componente, para o nosso exemplo este é o Gaveta do intake. |
| `model_1.glb` | Modelo CAD do 2° componente, para o nosso exemplo este é o Coletor do intake. |
| `model_2.glb` | Modelo CAD do 3° componente, para o nosso exemplo este é o Elevador. |
| `model_3.glb` | Modelo CAD do 4° componente, para o nosso exemplo este é a articulação do braço. |
| `model_4.glb` | Modelo CAD do 5° componente, para o nosso exemplo este é a roda de coleta 1 do braço. |
| `model_5.glb` | Modelo CAD do 6° componente, para o nosso exemplo este é a roda de coleta 2 do braço. |

**[Aprenda a configurar o .json e os models](https://github.com/FRCMT-Repositories/MT_2026/tree/main/Code/Advantage%20Scope/Robot_MTModelA)**

<h3>Adicionando ao Advantage Scope</h3>
<div align="justify">

Após a seleção do modelo, tornasse necessario copiar a pasta do modelo escolhido para o diretório autoAssets do Advantage Scope, geralmente presente no seguinte diretório `C:\Users\user\AppData\Roaming\AdvantageScope\autoAssets`.

**ATENÇÃO:**

- Recomenda-se criar um atalho para esta pasta, pois ela será utilizada com frequência durante o desenvolvimento e validação da simulação dos subsistemas.

- Durante o uso do Advantage Scope, em algumas situações o software pode remover automaticamente a pasta do modelo previamente carregada, tornando necessário adicioná-la novamente. Por esse motivo, recomenda-se não mover a pasta original do modelo diretamente para o diretório do Advantage Scope. Em vez disso, crie uma cópia da pasta do modelo dentro do diretório utilizado pelo software, preservando assim os arquivos originais do projeto.

</div>

<h2>PASSO 2: Definição do módulo swerve </h2>
<div align="justify">

Nesta etapa, tornasse necessario escolher a biblioteca responsável por controlar o seu módulo serve, atualmente temos 2 exemplos funcionais:

</div>

| MÓDULO | BIBLIOTECA | TIPO DE MOTOR |
|---|---|
| [MK4i](https://github.com/FRCMT-Repositories/MT_2026/tree/main/Code/YAGSLFull_MK4i) | YAGSL | NEO e KRAKEN |
| [MK5n](https://github.com/FRCMT-Repositories/MT_2026/tree/main/Code/CTRFull_MK5n) | CTRe | KRAKEN |

<div align="justify">
**Observação:**

A escolha da biblioteca de desenvolvimento fica inteiramente a critério de cada equipe. Caso optem por utilizar soluções diferentes das apresentadas neste repositório, sintam-se totalmente à vontade para adaptá-las conforme as necessidades e preferências do projeto.

A única exigência para a utilização correta da simulação do robô é a disponibilização do plot da odometria do robô. Em ambos os exemplos apresentados anteriormente, cada biblioteca possui, em seu escopo principal, a publicação das informações de odometria necessárias para integração com o Advantage Scope.

Esses dados são fundamentais para permitir o posicionamento e movimentação correta do robô dentro do ambiente de simulação.

</div>

Aqui será descrito, como fazer a simulação dos modelos de Robô A e B, para eventual validação dos subsistemas.