<?php
header("Content-Type: application/json");
$conn = new mysqli("localhost", "root", "", "controle_ponto");

$funcionario_id = $_GET['funcionario_id'];
$data_atual = date('Y-m-d');

$sql = "SELECT * FROM registros_ponto WHERE funcionario_id = $funcionario_id AND data = '$data_atual'";
$result = $conn->query($sql);
echo json_encode($result->fetch_assoc());
?>
