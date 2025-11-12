-- Script para eliminar todos los usuarios de la base de datos
-- Este script elimina todos los registros de la tabla usuario
-- Ejecutar: .\scripts\delete-all-users.ps1
-- O manualmente: docker exec -i postgres-db psql -U postgres -d ucochallenge < scripts/delete-all-users.sql

-- Mostrar la cantidad de usuarios antes de eliminar
SELECT 'Usuarios antes de eliminar:' as informacion, COUNT(*) as cantidad FROM usuario;

-- Eliminar todos los usuarios
DELETE FROM usuario;

-- Mostrar la cantidad de usuarios después de eliminar
SELECT 'Usuarios después de eliminar:' as informacion, COUNT(*) as cantidad FROM usuario;

-- Verificar que la tabla está vacía
SELECT 'Verificación final:' as informacion, COUNT(*) as cantidad FROM usuario;

