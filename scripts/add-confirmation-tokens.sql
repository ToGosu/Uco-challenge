-- Script para agregar columnas de tokens de confirmación a la tabla usuario
-- Ejecutar este script después de actualizar el código

-- Agregar columnas para tokens de confirmación de email
ALTER TABLE usuario 
ADD COLUMN IF NOT EXISTS token_confirmacion_email VARCHAR(255),
ADD COLUMN IF NOT EXISTS token_email_expiracion TIMESTAMP;

-- Agregar columnas para tokens de confirmación de celular
ALTER TABLE usuario 
ADD COLUMN IF NOT EXISTS token_confirmacion_movil VARCHAR(255),
ADD COLUMN IF NOT EXISTS token_movil_expiracion TIMESTAMP;

-- Crear índices para mejorar el rendimiento de búsqueda por tokens
CREATE INDEX IF NOT EXISTS idx_usuario_email_token ON usuario(token_confirmacion_email);
CREATE INDEX IF NOT EXISTS idx_usuario_mobile_token ON usuario(token_confirmacion_movil);

-- Comentarios para documentación
COMMENT ON COLUMN usuario.token_confirmacion_email IS 'Token único para confirmar correo electrónico';
COMMENT ON COLUMN usuario.token_email_expiracion IS 'Fecha y hora de expiración del token de email';
COMMENT ON COLUMN usuario.token_confirmacion_movil IS 'Código de 6 dígitos para confirmar número de celular';
COMMENT ON COLUMN usuario.token_movil_expiracion IS 'Fecha y hora de expiración del token de celular';

