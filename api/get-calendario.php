<?php
header("Content-Type: application/json");
$conn = new mysqli("localhost", "root", "", "controle_ponto");

$funcionario_id = $_GET['funcionario_id'];
$sql = "SELECT DAY(data) AS dia, SUM(TIMEDIFF(saida, entrada)) AS horas FROM registros_ponto WHERE funcionario_id = $funcionario_id GROUP BY dia";
$result = $conn->query($sql);

$registros = [];
while ($row = $result->fetch_assoc()) {
    $registros[] = $row;
}
echo json_encode($registros);
?>
