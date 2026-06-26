-- =============================================
-- DATA.SQL - Bookstore AI
-- Roles: ROLE_OWNER, ROLE_CUSTOMER
-- Password: Password@123 (BCrypt)
-- 30 clientes, 100 ventas (2021-2025)
-- =============================================

-- ROLES
INSERT INTO roles (id, name) VALUES (1, 'ROLE_OWNER') ON CONFLICT DO NOTHING;
INSERT INTO roles (id, name) VALUES (2, 'ROLE_CUSTOMER') ON CONFLICT DO NOTHING;

-- USUARIOS (1 owner + 30 customers)
INSERT INTO users (id, email, password, enabled, role_id) VALUES
(1, 'owner@bookstore.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 1),
(2, 'carlos@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(3, 'maria@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(4, 'ana@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(5, 'luis@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(6, 'sofia@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(7, 'pedro@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(8, 'lucia@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(9, 'diego@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(10, 'camila@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(11, 'jorge@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(12, 'valentina@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(13, 'miguel@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(14, 'elena@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(15, 'andres@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(16, 'rosa@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(17, 'fernando@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(18, 'carmen@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(19, 'raul@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(20, 'patricia@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(21, 'oscar@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(22, 'gabriela@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(23, 'ricardo@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(24, 'daniela@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(25, 'alejandro@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(26, 'natalia@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(27, 'santiago@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(28, 'paula@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(29, 'manuel@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(30, 'andrea@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2),
(31, 'ivan@email.com', '$2a$10$raX5e504731O8xTkRU9FcOSYxgJ53SzrcNIp/yfbw5in1CgmRJbSq', true, 2)
ON CONFLICT DO NOTHING;

-- CUSTOMERS (30)
INSERT INTO customers (id, first_name, last_name, dni, phone, user_id) VALUES
(1, 'Carlos', 'Mendoza', '10000001', '987654321', 2),
(2, 'Maria', 'Lopez', '10000002', '912345678', 3),
(3, 'Ana', 'Garcia', '10000003', '956789012', 4),
(4, 'Luis', 'Torres', '10000004', '934567890', 5),
(5, 'Sofia', 'Ramirez', '10000005', '945678901', 6),
(6, 'Pedro', 'Sanchez', '10000006', '923456789', 7),
(7, 'Lucia', 'Flores', '10000007', '967890123', 8),
(8, 'Diego', 'Castillo', '10000008', '978901234', 9),
(9, 'Camila', 'Vargas', '10000009', '989012345', 10),
(10, 'Jorge', 'Rojas', '10000010', '990123456', 11),
(11, 'Valentina', 'Diaz', '10000011', '901234567', 12),
(12, 'Miguel', 'Herrera', '10000012', '912345670', 13),
(13, 'Elena', 'Castro', '10000013', '923456701', 14),
(14, 'Andres', 'Ortiz', '10000014', '934567012', 15),
(15, 'Rosa', 'Paredes', '10000015', '945670123', 16),
(16, 'Fernando', 'Reyes', '10000016', '956701234', 17),
(17, 'Carmen', 'Gutierrez', '10000017', '967012345', 18),
(18, 'Raul', 'Medina', '10000018', '978123456', 19),
(19, 'Patricia', 'Silva', '10000019', '989234567', 20),
(20, 'Oscar', 'Morales', '10000020', '990345678', 21),
(21, 'Gabriela', 'Jimenez', '10000021', '901456789', 22),
(22, 'Ricardo', 'Perez', '10000022', '912567890', 23),
(23, 'Daniela', 'Romero', '10000023', '923678901', 24),
(24, 'Alejandro', 'Ruiz', '10000024', '934789012', 25),
(25, 'Natalia', 'Alvarez', '10000025', '945890123', 26),
(26, 'Santiago', 'Fernandez', '10000026', '956901234', 27),
(27, 'Paula', 'Gonzalez', '10000027', '968012345', 28),
(28, 'Manuel', 'Chavez', '10000028', '979123456', 29),
(29, 'Andrea', 'Quispe', '10000029', '980234567', 30),
(30, 'Ivan', 'Huaman', '10000030', '991345678', 31)
ON CONFLICT DO NOTHING;

-- AUTORES
INSERT INTO authors (id, first_name, last_name, nationality) VALUES
(1, 'Gabriel', 'Garcia Marquez', 'Colombiano'),
(2, 'Mario', 'Vargas Llosa', 'Peruano'),
(3, 'Isabel', 'Allende', 'Chilena'),
(4, 'Jorge', 'Luis Borges', 'Argentino'),
(5, 'Julio', 'Cortazar', 'Argentino'),
(6, 'Pablo', 'Neruda', 'Chileno'),
(7, 'Stephen', 'King', 'Estadounidense'),
(8, 'Juan', 'Rulfo', 'Mexicano')
ON CONFLICT DO NOTHING;

-- LIBROS
INSERT INTO books (id, title, genre, price, stock, min_stock, description, slug, author_id, image_url, file_url) VALUES
(1, 'Cien anos de soledad', 'Realismo magico', 55.00, 15, 5, 'Narra la historia de la familia Buendia a lo largo de siete generaciones en el pueblo ficticio de Macondo.', 'cien-anos-de-soledad-gabriel-garcia-marquez', 1, NULL, NULL),
(2, 'El amor en los tiempos del colera', 'Novela', 48.00, 10, 5, 'Historia de amor entre Fermina Daza y Florentino Ariza que se extiende por mas de cincuenta anos.', 'el-amor-en-los-tiempos-del-colera-gabriel-garcia-marquez', 1, NULL, NULL),
(3, 'La hojarasca', 'Novela', 35.00, 8, 5, 'Primera novela de Garcia Marquez ambientada en Macondo.', 'la-hojarasca-gabriel-garcia-marquez', 1, NULL, NULL),
(4, 'La ciudad y los perros', 'Novela', 45.00, 12, 5, 'Primera novela de Vargas Llosa ambientada en el Colegio Militar Leoncio Prado de Lima.', 'la-ciudad-y-los-perros-mario-vargas-llosa', 2, NULL, NULL),
(5, 'La casa verde', 'Novela', 42.00, 9, 5, 'Novela que entrelaza historias en la selva peruana y la ciudad de Piura.', 'la-casa-verde-mario-vargas-llosa', 2, NULL, NULL),
(6, 'Conversacion en la catedral', 'Novela', 52.00, 7, 5, 'Novela sobre la dictadura de Odria en Peru contada a traves de una conversacion.', 'conversacion-en-la-catedral-mario-vargas-llosa', 2, NULL, NULL),
(7, 'La casa de los espiritus', 'Realismo magico', 50.00, 11, 5, 'Saga familiar que abarca cuatro generaciones de la familia Trueba en Chile.', 'la-casa-de-los-espiritus-isabel-allende', 3, NULL, NULL),
(8, 'Eva Luna', 'Novela', 38.00, 14, 5, 'Historia de una joven huerfana con el don de contar historias.', 'eva-luna-isabel-allende', 3, NULL, NULL),
(9, 'El Aleph', 'Cuento', 34.00, 13, 5, 'Coleccion de cuentos que exploran temas filosoficos y metafisicos.', 'el-aleph-jorge-luis-borges', 4, NULL, NULL),
(10, 'Ficciones', 'Cuento', 36.00, 10, 5, 'Obra maestra de cuentos que mezcla filosofia, matematicas y literatura.', 'ficciones-jorge-luis-borges', 4, NULL, NULL),
(11, 'Rayuela', 'Novela', 47.00, 8, 5, 'Novela experimental que puede leerse de multiples maneras.', 'rayuela-julio-cortazar', 5, NULL, NULL),
(12, 'Misery', 'Terror', 52.00, 6, 5, 'Un escritor es secuestrado por su fan numero uno.', 'misery-stephen-king', 7, NULL, NULL),
(13, 'Veinte poemas de amor', 'Poesia', 28.00, 20, 5, 'Coleccion de poemas de amor que marcaron la poesia latinoamericana.', 'veinte-poemas-de-amor-pablo-neruda', 6, NULL, NULL),
(14, 'Pedro Paramo', 'Realismo magico', 32.00, 10, 5, 'Un hombre viaja al pueblo de Comala en busca de su padre Pedro Paramo.', 'pedro-paramo-juan-rulfo', 8, NULL, NULL),
(15, 'El llano en llamas', 'Cuento', 30.00, 12, 5, 'Coleccion de cuentos sobre la vida rural mexicana.', 'el-llano-en-llamas-juan-rulfo', 8, NULL, NULL)
ON CONFLICT DO NOTHING;

-- VENTAS (100 ventas distribuidas entre 2021 y marzo 2025)
INSERT INTO sales (id, customer_id, total, created_at) VALUES
-- 2021 (15 ventas)
(1, 1, 55.00, '2021-02-10 10:00:00'),
(2, 3, 48.00, '2021-03-15 14:00:00'),
(3, 5, 83.00, '2021-04-20 09:00:00'),
(4, 2, 50.00, '2021-05-12 11:30:00'),
(5, 7, 45.00, '2021-06-08 16:00:00'),
(6, 4, 97.00, '2021-07-14 10:15:00'),
(7, 8, 34.00, '2021-08-22 13:00:00'),
(8, 6, 52.00, '2021-09-03 15:30:00'),
(9, 10, 76.00, '2021-10-18 09:45:00'),
(10, 9, 42.00, '2021-11-05 12:00:00'),
(11, 1, 36.00, '2021-11-20 14:30:00'),
(12, 12, 47.00, '2021-12-01 10:00:00'),
(13, 3, 55.00, '2021-12-10 11:00:00'),
(14, 11, 30.00, '2021-12-15 16:00:00'),
(15, 14, 52.00, '2021-12-28 09:00:00'),
-- 2022 (20 ventas)
(16, 2, 55.00, '2022-01-10 10:00:00'),
(17, 5, 48.00, '2022-02-14 14:00:00'),
(18, 13, 35.00, '2022-02-28 09:30:00'),
(19, 7, 90.00, '2022-03-15 11:00:00'),
(20, 15, 42.00, '2022-04-01 16:00:00'),
(21, 4, 50.00, '2022-04-20 10:30:00'),
(22, 16, 34.00, '2022-05-10 13:00:00'),
(23, 8, 73.00, '2022-06-05 15:00:00'),
(24, 17, 55.00, '2022-06-18 09:00:00'),
(25, 1, 47.00, '2022-07-22 12:00:00'),
(26, 18, 52.00, '2022-08-08 10:30:00'),
(27, 6, 38.00, '2022-08-25 14:00:00'),
(28, 19, 84.00, '2022-09-12 11:00:00'),
(29, 20, 28.00, '2022-10-03 16:30:00'),
(30, 3, 97.00, '2022-10-18 09:00:00'),
(31, 21, 45.00, '2022-11-05 13:00:00'),
(32, 9, 36.00, '2022-11-20 15:00:00'),
(33, 22, 55.00, '2022-12-01 10:00:00'),
(34, 10, 48.00, '2022-12-15 14:00:00'),
(35, 23, 32.00, '2022-12-28 11:00:00'),
-- 2023 (25 ventas)
(36, 1, 103.00, '2023-01-08 10:00:00'),
(37, 24, 48.00, '2023-01-22 14:30:00'),
(38, 2, 50.00, '2023-02-05 09:00:00'),
(39, 25, 42.00, '2023-02-18 11:00:00'),
(40, 5, 55.00, '2023-03-10 16:00:00'),
(41, 26, 34.00, '2023-03-25 10:30:00'),
(42, 3, 47.00, '2023-04-08 13:00:00'),
(43, 27, 52.00, '2023-04-20 15:00:00'),
(44, 7, 38.00, '2023-05-05 09:00:00'),
(45, 28, 30.00, '2023-05-18 12:00:00'),
(46, 4, 93.00, '2023-06-02 10:30:00'),
(47, 29, 55.00, '2023-06-15 14:00:00'),
(48, 11, 45.00, '2023-07-01 11:00:00'),
(49, 30, 36.00, '2023-07-20 16:00:00'),
(50, 6, 84.00, '2023-08-05 09:30:00'),
(51, 12, 52.00, '2023-08-18 13:00:00'),
(52, 8, 48.00, '2023-09-01 15:00:00'),
(53, 13, 35.00, '2023-09-15 10:00:00'),
(54, 14, 97.00, '2023-10-08 14:00:00'),
(55, 9, 42.00, '2023-10-22 11:30:00'),
(56, 15, 28.00, '2023-11-05 16:00:00'),
(57, 16, 55.00, '2023-11-18 09:00:00'),
(58, 10, 50.00, '2023-12-01 13:00:00'),
(59, 17, 34.00, '2023-12-12 15:00:00'),
(60, 1, 47.00, '2023-12-28 10:00:00'),
-- 2024 (25 ventas)
(61, 18, 55.00, '2024-01-10 10:00:00'),
(62, 2, 83.00, '2024-01-25 14:00:00'),
(63, 19, 48.00, '2024-02-08 09:00:00'),
(64, 20, 52.00, '2024-02-22 11:30:00'),
(65, 3, 45.00, '2024-03-05 16:00:00'),
(66, 21, 36.00, '2024-03-18 10:30:00'),
(67, 22, 50.00, '2024-04-01 13:00:00'),
(68, 4, 97.00, '2024-04-15 15:00:00'),
(69, 23, 34.00, '2024-05-02 09:00:00'),
(70, 5, 42.00, '2024-05-18 12:00:00'),
(71, 24, 55.00, '2024-06-05 10:30:00'),
(72, 25, 38.00, '2024-06-20 14:00:00'),
(73, 6, 84.00, '2024-07-08 11:00:00'),
(74, 26, 30.00, '2024-07-22 16:00:00'),
(75, 7, 52.00, '2024-08-05 09:30:00'),
(76, 27, 47.00, '2024-08-18 13:00:00'),
(77, 28, 55.00, '2024-09-01 15:00:00'),
(78, 8, 48.00, '2024-09-15 10:00:00'),
(79, 29, 35.00, '2024-10-01 14:00:00'),
(80, 9, 93.00, '2024-10-18 11:00:00'),
(81, 30, 42.00, '2024-11-02 16:00:00'),
(82, 10, 55.00, '2024-11-15 09:00:00'),
(83, 1, 50.00, '2024-12-01 13:00:00'),
(84, 11, 34.00, '2024-12-12 15:00:00'),
(85, 12, 48.00, '2024-12-28 10:00:00'),
-- 2025 enero-marzo (15 ventas)
(86, 2, 55.00, '2025-01-05 10:00:00'),
(87, 13, 52.00, '2025-01-12 14:00:00'),
(88, 3, 45.00, '2025-01-20 09:30:00'),
(89, 14, 83.00, '2025-01-28 11:00:00'),
(90, 4, 36.00, '2025-02-05 16:00:00'),
(91, 15, 48.00, '2025-02-12 10:00:00'),
(92, 5, 97.00, '2025-02-18 13:00:00'),
(93, 16, 42.00, '2025-02-25 15:00:00'),
(94, 6, 55.00, '2025-03-01 09:00:00'),
(95, 17, 34.00, '2025-03-05 12:00:00'),
(96, 7, 50.00, '2025-03-08 10:30:00'),
(97, 18, 47.00, '2025-03-12 14:00:00'),
(98, 1, 84.00, '2025-03-15 11:00:00'),
(99, 8, 38.00, '2025-03-20 16:00:00'),
(100, 19, 52.00, '2025-03-25 09:00:00'),
-- 2025 abril-diciembre (20 ventas)
(101, 1, 55.00, '2025-04-10'),
(102, 20, 48.00, '2025-04-22'),
(103, 3, 83.00, '2025-05-08'),
(104, 21, 42.00, '2025-05-20'),
(105, 5, 50.00, '2025-06-05'),
(106, 22, 97.00, '2025-06-18'),
(107, 7, 34.00, '2025-07-10'),
(108, 23, 52.00, '2025-07-25'),
(109, 8, 45.00, '2025-08-08'),
(110, 24, 84.00, '2025-08-22'),
(111, 1, 47.00, '2025-09-05'),
(112, 25, 55.00, '2025-09-18'),
(113, 2, 36.00, '2025-10-08'),
(114, 26, 93.00, '2025-10-22'),
(115, 3, 48.00, '2025-11-05'),
(116, 27, 55.00, '2025-11-20'),
(117, 4, 42.00, '2025-12-05'),
(118, 28, 50.00, '2025-12-12'),
(119, 5, 84.00, '2025-12-18'),
(120, 29, 38.00, '2025-12-28'),
-- 2026 enero-marzo (10 ventas)
(121, 1, 55.00, '2026-01-10'),
(122, 6, 48.00, '2026-01-22'),
(123, 30, 83.00, '2026-02-05'),
(124, 2, 42.00, '2026-02-18'),
(125, 7, 97.00, '2026-03-01'),
(126, 8, 34.00, '2026-03-05'),
(127, 3, 55.00, '2026-03-10'),
(128, 9, 50.00, '2026-03-15'),
(129, 10, 84.00, '2026-03-20'),
(130, 1, 47.00, '2026-03-25'),
(131, 2, 55.00, '2026-04-05'),
(132, 11, 48.00, '2026-04-18'),
(133, 3, 83.00, '2026-05-08'),
(134, 12, 42.00, '2026-05-22'),
(135, 4, 50.00, '2026-06-10'),
(136, 13, 97.00, '2026-06-25'),
(137, 5, 34.00, '2026-07-08'),
(138, 14, 52.00, '2026-07-20'),
(139, 6, 45.00, '2026-08-05'),
(140, 15, 84.00, '2026-08-18'),
(141, 7, 47.00, '2026-09-02'),
(142, 16, 55.00, '2026-09-15'),
(143, 8, 36.00, '2026-10-05'),
(144, 17, 93.00, '2026-10-20'),
(145, 9, 48.00, '2026-11-08'),
(146, 18, 55.00, '2026-11-22'),
(147, 10, 42.00, '2026-12-05'),
(148, 19, 50.00, '2026-12-12'),
(149, 1, 84.00, '2026-12-18'),
(150, 20, 38.00, '2026-12-28')
ON CONFLICT DO NOTHING;

-- ITEMS DE VENTA (1-2 items por venta, libros variados)
INSERT INTO sale_items (sale_id, book_id, quantity, unit_price) VALUES
(1, 1, 1, 55.00), (2, 2, 1, 48.00), (3, 1, 1, 55.00), (3, 13, 1, 28.00),
(4, 7, 1, 50.00), (5, 4, 1, 45.00), (6, 1, 1, 55.00), (6, 5, 1, 42.00),
(7, 9, 1, 34.00), (8, 12, 1, 52.00), (9, 2, 1, 48.00), (9, 13, 1, 28.00),
(10, 5, 1, 42.00), (11, 10, 1, 36.00), (12, 11, 1, 47.00), (13, 1, 1, 55.00),
(14, 15, 1, 30.00), (15, 12, 1, 52.00), (16, 1, 1, 55.00), (17, 2, 1, 48.00),
(18, 3, 1, 35.00), (19, 7, 1, 50.00), (19, 8, 1, 38.00), (19, 14, 1, 2.00),
(20, 5, 1, 42.00), (21, 7, 1, 50.00), (22, 9, 1, 34.00), (23, 1, 1, 55.00),
(23, 8, 1, 18.00), (24, 1, 1, 55.00), (25, 11, 1, 47.00), (26, 12, 1, 52.00),
(27, 8, 1, 38.00), (28, 2, 1, 48.00), (28, 10, 1, 36.00), (29, 13, 1, 28.00),
(30, 1, 1, 55.00), (30, 5, 1, 42.00), (31, 4, 1, 45.00), (32, 10, 1, 36.00),
(33, 1, 1, 55.00), (34, 2, 1, 48.00), (35, 14, 1, 32.00), (36, 1, 1, 55.00),
(36, 2, 1, 48.00), (37, 2, 1, 48.00), (38, 7, 1, 50.00), (39, 5, 1, 42.00),
(40, 1, 1, 55.00), (41, 9, 1, 34.00), (42, 11, 1, 47.00), (43, 12, 1, 52.00),
(44, 8, 1, 38.00), (45, 15, 1, 30.00), (46, 1, 1, 55.00), (46, 8, 1, 38.00),
(47, 1, 1, 55.00), (48, 4, 1, 45.00), (49, 10, 1, 36.00), (50, 2, 1, 48.00),
(50, 10, 1, 36.00), (51, 12, 1, 52.00), (52, 2, 1, 48.00), (53, 3, 1, 35.00),
(54, 1, 1, 55.00), (54, 5, 1, 42.00), (55, 5, 1, 42.00), (56, 13, 1, 28.00),
(57, 1, 1, 55.00), (58, 7, 1, 50.00), (59, 9, 1, 34.00), (60, 11, 1, 47.00),
(61, 1, 1, 55.00), (62, 1, 1, 55.00), (62, 13, 1, 28.00), (63, 2, 1, 48.00),
(64, 12, 1, 52.00), (65, 4, 1, 45.00), (66, 10, 1, 36.00), (67, 7, 1, 50.00),
(68, 1, 1, 55.00), (68, 5, 1, 42.00), (69, 9, 1, 34.00), (70, 5, 1, 42.00),
(71, 1, 1, 55.00), (72, 8, 1, 38.00), (73, 2, 1, 48.00), (73, 10, 1, 36.00),
(74, 15, 1, 30.00), (75, 12, 1, 52.00), (76, 11, 1, 47.00), (77, 1, 1, 55.00),
(78, 2, 1, 48.00), (79, 3, 1, 35.00), (80, 1, 1, 55.00), (80, 8, 1, 38.00),
(81, 5, 1, 42.00), (82, 1, 1, 55.00), (83, 7, 1, 50.00), (84, 9, 1, 34.00),
(85, 2, 1, 48.00), (86, 1, 1, 55.00), (87, 12, 1, 52.00), (88, 4, 1, 45.00),
(89, 1, 1, 55.00), (89, 13, 1, 28.00), (90, 10, 1, 36.00), (91, 2, 1, 48.00),
(92, 1, 1, 55.00), (92, 5, 1, 42.00), (93, 5, 1, 42.00), (94, 1, 1, 55.00),
(95, 9, 1, 34.00), (96, 7, 1, 50.00), (97, 11, 1, 47.00), (98, 2, 1, 48.00),
(98, 10, 1, 36.00), (99, 8, 1, 38.00), (100, 12, 1, 52.00),
-- Ventas 101-130
(101, 1, 1, 55.00), (102, 2, 1, 48.00), (103, 1, 1, 55.00), (103, 13, 1, 28.00),
(104, 5, 1, 42.00), (105, 7, 1, 50.00), (106, 1, 1, 55.00), (106, 5, 1, 42.00),
(107, 9, 1, 34.00), (108, 12, 1, 52.00), (109, 4, 1, 45.00), (110, 2, 1, 48.00),
(110, 10, 1, 36.00), (111, 11, 1, 47.00), (112, 1, 1, 55.00), (113, 10, 1, 36.00),
(114, 1, 1, 55.00), (114, 8, 1, 38.00), (115, 2, 1, 48.00), (116, 1, 1, 55.00),
(117, 5, 1, 42.00), (118, 7, 1, 50.00), (119, 2, 1, 48.00), (119, 10, 1, 36.00),
(120, 8, 1, 38.00), (121, 1, 1, 55.00), (122, 2, 1, 48.00), (123, 1, 1, 55.00),
(123, 13, 1, 28.00), (124, 5, 1, 42.00), (125, 1, 1, 55.00), (125, 5, 1, 42.00),
(126, 9, 1, 34.00), (127, 1, 1, 55.00), (128, 7, 1, 50.00), (129, 2, 1, 48.00),
(129, 10, 1, 36.00), (130, 11, 1, 47.00),
-- Ventas 131-150
(131, 1, 1, 55.00), (132, 2, 1, 48.00), (133, 1, 1, 55.00), (133, 13, 1, 28.00),
(134, 5, 1, 42.00), (135, 7, 1, 50.00), (136, 1, 1, 55.00), (136, 5, 1, 42.00),
(137, 9, 1, 34.00), (138, 12, 1, 52.00), (139, 4, 1, 45.00), (140, 2, 1, 48.00),
(140, 10, 1, 36.00), (141, 11, 1, 47.00), (142, 1, 1, 55.00), (143, 10, 1, 36.00),
(144, 1, 1, 55.00), (144, 8, 1, 38.00), (145, 2, 1, 48.00), (146, 1, 1, 55.00),
(147, 5, 1, 42.00), (148, 7, 1, 50.00), (149, 2, 1, 48.00), (149, 10, 1, 36.00),
(150, 8, 1, 38.00)
ON CONFLICT DO NOTHING;

-- RESENAS (2-3 por libro, todos los libros cubiertos)
INSERT INTO reviews (id, user_id, book_id, rating, comment, created_at) VALUES
-- Libro 1: Cien anos de soledad
(1, 2, 1, 5, 'Una obra maestra de la literatura latinoamericana. Imprescindible.', '2022-03-10 10:00:00'),
(2, 3, 1, 4, 'Hermoso pero complejo. Necesitas concentracion para seguir a los Buendia.', '2022-05-15 14:00:00'),
(3, 5, 1, 5, 'Lo he leido tres veces y cada vez descubro algo nuevo. Genial.', '2023-02-20 09:00:00'),
-- Libro 2: El amor en los tiempos del colera
(4, 4, 2, 5, 'Una historia de amor que te hace creer en la paciencia y la persistencia.', '2022-08-20 09:00:00'),
(5, 6, 2, 4, 'Romantica y profunda. Garcia Marquez en su mejor momento.', '2023-06-12 14:00:00'),
-- Libro 3: La hojarasca
(6, 7, 3, 4, 'Corta pero intensa. Se nota el inicio del estilo de Macondo.', '2023-03-15 11:00:00'),
(7, 8, 3, 3, 'Buena pero no es su mejor obra. Vale la pena para fans de Garcia Marquez.', '2024-01-10 16:00:00'),
-- Libro 4: La ciudad y los perros
(8, 10, 4, 4, 'Vargas Llosa retrata la vida militar con una precision brutal.', '2024-04-08 09:30:00'),
(9, 9, 4, 5, 'Una novela que te golpea. Realismo puro y duro.', '2024-08-22 13:00:00'),
-- Libro 5: La casa verde
(10, 11, 5, 5, 'La casa verde es una novela que te atrapa desde la primera pagina.', '2024-07-20 12:00:00'),
(11, 4, 5, 4, 'Compleja pero fascinante. Vargas Llosa entrelaza historias con maestria.', '2023-09-05 10:00:00'),
-- Libro 6: Conversacion en la catedral
(12, 2, 6, 5, 'La mejor novela politica que he leido. Magistral.', '2023-11-18 14:00:00'),
(13, 12, 6, 4, 'Densa pero recompensa al lector paciente. Imprescindible.', '2024-05-10 11:00:00'),
-- Libro 7: La casa de los espiritus
(14, 5, 7, 4, 'Isabel Allende en su mejor momento. Una saga familiar inolvidable.', '2023-01-12 11:00:00'),
(15, 13, 7, 5, 'Magica y emotiva. Una historia de mujeres fuertes.', '2024-03-08 16:00:00'),
-- Libro 8: Eva Luna
(16, 3, 8, 5, 'Eva Luna es una heroina inolvidable. Allende escribe con magia.', '2025-01-18 11:00:00'),
(17, 14, 8, 4, 'Una novela que celebra el poder de las historias. Hermosa.', '2024-06-15 09:00:00'),
-- Libro 9: El Aleph
(18, 6, 9, 5, 'Borges es un genio. Cada cuento es un universo.', '2023-04-05 16:00:00'),
(19, 15, 9, 5, 'Literatura que desafia la mente. Cuentos perfectos.', '2024-09-12 13:00:00'),
-- Libro 10: Ficciones
(20, 16, 10, 5, 'Borges reinventa la literatura en cada pagina. Extraordinario.', '2023-08-20 10:00:00'),
(21, 8, 10, 4, 'Cuentos que te hacen pensar durante dias. Brillante.', '2024-02-14 14:00:00'),
-- Libro 11: Rayuela
(22, 7, 11, 4, 'Rayuela es unica. Te desafia como lector.', '2023-07-18 10:00:00'),
(23, 17, 11, 5, 'La novela mas innovadora que he leido. Cortazar era un visionario.', '2024-11-05 11:00:00'),
-- Libro 12: Misery
(24, 8, 12, 5, 'King en su mejor momento. Misery es puro terror psicologico.', '2023-10-22 13:00:00'),
(25, 18, 12, 4, 'Aterrador sin necesidad de lo sobrenatural. Genial.', '2024-12-08 16:00:00'),
(26, 3, 12, 5, 'No pude soltarlo hasta terminarlo. King es el maestro del suspenso.', '2025-02-10 09:00:00'),
-- Libro 13: Veinte poemas de amor
(27, 2, 13, 4, 'Neruda habla al corazon. Poemas que nunca envejecen.', '2024-10-05 14:00:00'),
(28, 19, 13, 5, 'Poesia pura. Cada verso es una caricia al alma.', '2025-01-22 10:00:00'),
-- Libro 14: Pedro Paramo
(29, 9, 14, 5, 'Pedro Paramo es poesia en prosa. Obra maestra mexicana.', '2024-01-15 15:00:00'),
(30, 20, 14, 4, 'Un viaje al mundo de los muertos. Rulfo es inigualable.', '2024-08-30 12:00:00'),
-- Libro 15: El llano en llamas
(31, 21, 15, 4, 'Cuentos que retratan la vida rural con una belleza cruda.', '2023-12-05 11:00:00'),
(32, 10, 15, 5, 'Rulfo escribe poco pero cada palabra pesa. Magnifico.', '2024-07-02 14:00:00')
ON CONFLICT DO NOTHING;

-- RESETEAR SECUENCIAS
SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles));
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('customers_id_seq', (SELECT MAX(id) FROM customers));
SELECT setval('authors_id_seq', (SELECT MAX(id) FROM authors));
SELECT setval('books_id_seq', (SELECT MAX(id) FROM books));
SELECT setval('sales_id_seq', (SELECT MAX(id) FROM sales));
SELECT setval('reviews_id_seq', (SELECT MAX(id) FROM reviews));
