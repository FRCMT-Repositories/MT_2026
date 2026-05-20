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
- O **Model A** utiliza módulos **MK4i**
- O **Model B** utiliza módulos **MK5n**

A escolha entre os modelos afeta apenas a representação visual e estética do robô durante a simulação, não influenciando no funcionamento da programação, lógica de controle ou comportamento dos subsistemas.

<h3>Entendendo os arquivos dos Models A e B</h3>

<table align="center">
<tr>
	<td align="center" width="100"><b style="font-size:22px;">
		<b>config.json</b>
	</b></td>

	<td align="center" width="1000"><b style="font-size:22px;">
		Conjunto de informações do modelo, como por exemplo nome, posicionamento 3D do modelo principal e componentes (subsistemas como intake, elevador e braço), além do posicionamento de vistas, como câmeras acopladas ao robô.
	</b></td>
</tr>

<tr>
	<td align="center" width="100"><b style="font-size:22px;">
		<b>model.glb</b>
	</b></td>

	<td align="center" width="1000"><b style="font-size:22px;">
		Modelo CAD base do projeto, neste caso o chassi contendo todos os elementos fixos do robô.
	</b></td>
</tr>
</table>

</div>

Aqui será descrito, como fazer a simulação dos modelos de Robô A e B, para eventual validação dos subsistemas.