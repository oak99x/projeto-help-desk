CREATE DATABASE IF NOT EXISTS db_user;

USE db_user;

-- Criar a tabela tb_user
CREATE TABLE IF NOT EXISTS tb_user (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255)
);

-- Criar a tabela tb_role
CREATE TABLE IF NOT EXISTS tb_role (
    id BINARY(16) PRIMARY KEY,
    role_name VARCHAR(50)
);

-- Criar a tabela de junção tb_user_role
CREATE TABLE IF NOT EXISTS tb_user_role (
    user_id BINARY(16),
    role_id BINARY(16),
    FOREIGN KEY (user_id) REFERENCES tb_user(id),
    FOREIGN KEY (role_id) REFERENCES tb_role(id),
    PRIMARY KEY (user_id, role_id)
);

-- Usuarios normais

INSERT INTO tb_user (id, name, email, password) VALUES (UUID_TO_BIN('af020920-5202-41f4-b6ae-68f426c9f352'), 'Bob Rax', 'bob@gmail.com', '$2a$12$UZbKpZivMqJN8zklvbwAuOgCMsSnnhcMP7qZ4xaj65OzgL9P984ca');
INSERT INTO tb_user (id, name, email, password) VALUES (UUID_TO_BIN('d5c54427-5cc9-4759-9645-eb38d3e5f6e2'), 'Ana Russo', 'ana@gmail.com', '$2a$12$UZbKpZivMqJN8zklvbwAuOgCMsSnnhcMP7qZ4xaj65OzgL9P984ca');
INSERT INTO tb_user (id, name, email, password) VALUES (UUID_TO_BIN('7c0d5c43-8c5f-4e19-9f51-62f930175d79'), 'Carlos Chagas', 'carlos@gmail.com', '$2a$12$UZbKpZivMqJN8zklvbwAuOgCMsSnnhcMP7qZ4xaj65OzgL9P984ca');
INSERT INTO tb_user (id, name, email, password) VALUES (UUID_TO_BIN('025daa45-931d-4e6d-b4c3-971a2b83bb0b'), 'Mateus Freitas', 'mateus.freitas@zallpy.com', '$2a$12$UZbKpZivMqJN8zklvbwAuOgCMsSnnhcMP7qZ4xaj65OzgL9P984ca');

-- Inserir 2 administradores
INSERT INTO tb_user (id, name, email, password) VALUES (UUID_TO_BIN('b2e7878c-45a1-4f3d-8d3f-55e1889fa495'), 'Admin', 'admin@gmail.com', '$2a$12$7yp92O.HfczA8LILnU4HuevLPB22WC7sVAl07Fb2KJkYXjk36Iz/W');
INSERT INTO tb_user (id, name, email, password) VALUES (UUID_TO_BIN('4a9d8d5b-5f40-4b8b-9f0e-5080367e3068'), 'Mateus Carvalho', 'mateus.oak99@gmail.com', '$2a$12$Hm7.XRYYEXubpNH5VZwZLuABCnHFOm3pe19x3Q6zwnjJ/WqCD/j92');


-- Inserir Roles
INSERT INTO tb_role (id, role_name) VALUES (UUID_TO_BIN('b22e7b4e-4e57-44a5-8cbf-7fd1810d0e0a'), 'ROLE_USER');
INSERT INTO tb_role (id, role_name) VALUES (UUID_TO_BIN('c45c99b1-17e2-45b7-bb1c-0f903b8de369'), 'ROLE_ADMIN');


-- Usuarios normais
INSERT INTO tb_user_role (user_id, role_id) VALUES (UUID_TO_BIN('af020920-5202-41f4-b6ae-68f426c9f352'), UUID_TO_BIN('b22e7b4e-4e57-44a5-8cbf-7fd1810d0e0a'));
INSERT INTO tb_user_role (user_id, role_id) VALUES (UUID_TO_BIN('d5c54427-5cc9-4759-9645-eb38d3e5f6e2'), UUID_TO_BIN('b22e7b4e-4e57-44a5-8cbf-7fd1810d0e0a'));
INSERT INTO tb_user_role (user_id, role_id) VALUES (UUID_TO_BIN('7c0d5c43-8c5f-4e19-9f51-62f930175d79'), UUID_TO_BIN('b22e7b4e-4e57-44a5-8cbf-7fd1810d0e0a'));
INSERT INTO tb_user_role (user_id, role_id) VALUES (UUID_TO_BIN('025daa45-931d-4e6d-b4c3-971a2b83bb0b'), UUID_TO_BIN('b22e7b4e-4e57-44a5-8cbf-7fd1810d0e0a'));

-- Administradores
INSERT INTO tb_user_role (user_id, role_id) VALUES (UUID_TO_BIN('b2e7878c-45a1-4f3d-8d3f-55e1889fa495'), UUID_TO_BIN('c45c99b1-17e2-45b7-bb1c-0f903b8de369'));
INSERT INTO tb_user_role (user_id, role_id) VALUES (UUID_TO_BIN('4a9d8d5b-5f40-4b8b-9f0e-5080367e3068'), UUID_TO_BIN('c45c99b1-17e2-45b7-bb1c-0f903b8de369'));
