<?php
header("Content-Type: application/json");
$conn = new mysqli("localhost", "root", "", "controle_ponto");

$sql = "SELECT f.nome, SUM(TIMEDIFF(r.saida, r.entrada)) AS total_horas FROM funcionarios f LEFT JOIN registros_ponto r ON f.id = r.funcionario_id GROUP BY f.id";
$result = $conn->query($sql);

$funcionarios = [];
while ($row = $result->fetch_assoc()) {
    $funcionarios[] = $row;
}
echo json_encode($funcionarios);
?>
