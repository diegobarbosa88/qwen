CREATE DATABASE controle_ponto;

USE controle_ponto;

CREATE TABLE funcionarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    senha VARCHAR(255),
    role ENUM('admin', 'funcionario') DEFAULT 'funcionario'
);

CREATE TABLE registros_ponto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    funcionario_id INT,
    data DATE,
    entrada TIME NULL,
    saida TIME NULL,
    pausa_inicio TIME NULL,
    pausa_fim TIME NULL,
    FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id)
);
