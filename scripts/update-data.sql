-- Script para actualizar/refrescar datos de ejemplo
-- Este script actualiza los datos existentes o los inserta si no existen
-- Ejecutar: .\scripts\update-data.ps1
-- O manualmente: docker exec -i postgres-db psql -U postgres -d ucochallenge < scripts/update-data.sql

-- Actualizar o insertar país (Colombia)
INSERT INTO pais (id, nombre) 
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Colombia')
ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre;

-- Actualizar o insertar departamento (Antioquia)
INSERT INTO departamento (id, nombre, pais) 
VALUES ('550e8400-e29b-41d4-a716-446655440001', 'Antioquia', '550e8400-e29b-41d4-a716-446655440000')
ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre, pais = EXCLUDED.pais;

-- Actualizar o insertar ciudades
INSERT INTO ciudad (id, nombre, departamento) VALUES
('550e8400-e29b-41d4-a716-446655440010', 'Medellin', '550e8400-e29b-41d4-a716-446655440001'),
('550e8400-e29b-41d4-a716-446655440011', 'Rionegro', '550e8400-e29b-41d4-a716-446655440001'),
('550e8400-e29b-41d4-a716-446655440012', 'El Retiro', '550e8400-e29b-41d4-a716-446655440001'),
('550e8400-e29b-41d4-a716-446655440013', 'Santuario', '550e8400-e29b-41d4-a716-446655440001'),
('550e8400-e29b-41d4-a716-446655440014', 'Guarne', '550e8400-e29b-41d4-a716-446655440001')
ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre, departamento = EXCLUDED.departamento;

-- Actualizar o insertar tipos de identificación
INSERT INTO tipo_identificacion (id, nombre) VALUES
('550e8400-e29b-41d4-a716-446655440021', 'Cedula de Extranjeria'),
('550e8400-e29b-41d4-a716-446655440022', 'Pasaporte'),
('550e8400-e29b-41d4-a716-446655440023', 'Tarjeta de Identidad'),
('550e8400-e29b-41d4-a716-446655440024', 'NIT')
ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre;


