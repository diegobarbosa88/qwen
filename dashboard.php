<?php
session_start();
if (!isset($_SESSION['funcionario_id'])) {
    header("Location: index.html");
    exit();
}

$funcionario_id = $_SESSION['funcionario_id'];
$conn = new mysqli("localhost", "root", "", "controle_ponto");
$sql = "SELECT * FROM registros_ponto WHERE funcionario_id = $funcionario_id AND data = CURDATE()";
$result = $conn->query($sql);
$registro = $result->fetch_assoc();
?>
<!-- Conteúdo do dashboard aqui -->
