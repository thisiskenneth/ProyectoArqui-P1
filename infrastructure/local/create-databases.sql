-- LogiFlow - creacion de bases para ejecucion local en PostgreSQL.
--
-- Cada microservicio con persistencia usa su propia base (database-per-service).
-- ms-seguimiento usa H2 en memoria (relay efimero) y graphql-gateway no tiene base.
--
-- Uso (PostgreSQL local, usuario postgres):
--   psql -U postgres -h localhost -p 5432 -f infrastructure/local/create-databases.sql
--
-- CREATE DATABASE no admite "IF NOT EXISTS"; se ignora el error si ya existe.

SELECT 'CREATE DATABASE flotadb'    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'flotadb')\gexec
SELECT 'CREATE DATABASE tallerdb'   WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'tallerdb')\gexec
SELECT 'CREATE DATABASE authdb'     WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'authdb')\gexec
SELECT 'CREATE DATABASE clientesdb' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'clientesdb')\gexec
SELECT 'CREATE DATABASE pedidosdb'  WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'pedidosdb')\gexec
SELECT 'CREATE DATABASE ruteodb'    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'ruteodb')\gexec
