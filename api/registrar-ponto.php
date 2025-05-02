<?php
header("Content-Type: application/json");
$conn = new mysqli("localhost", "root", "", "controle_ponto");

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = json_decode(file_get_contents('php://input'), true);
    $funcionario_id = $conn->real_escape_string($data['funcionario_id']);
    $tipo = $conn->real_escape_string($data['tipo']);
    $data_atual = date('Y-m-d');
    $hora_atual = date('H:i:s');

    $campo = '';
    switch ($tipo) {
        case 'entrada': $campo = 'entrada'; break;
        case 'saida': $campo = 'saida'; break;
        case 'pausa': $campo = 'pausa_inicio'; break;
        case 'retorno': $campo = 'pausa_fim'; break;
    }

    // Verifica se já existe um registro para hoje
    $check = $conn->query("SELECT * FROM registros_ponto WHERE funcionario_id = '$funcionario_id' AND data = '$data_atual'");
    
    if ($check->num_rows > 0) {
        $conn->query("UPDATE registros_ponto SET $campo = '$hora_atual' WHERE funcionario_id = '$funcionario_id' AND data = '$data_atual'");
    } else {
        $conn->query("INSERT INTO registros_ponto (funcionario_id, data, $campo) VALUES ('$funcionario_id', '$data_atual', '$hora_atual')");
    }

    echo json_encode(['status' => 'success']);
}
?>
