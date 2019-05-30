-- User
CREATE USER matchmakers LOGIN PASSWORD 'matchmakers' NOCREATEDB NOCREATEROLE NOREPLICATION VALID UNTIL 'infinity';

-- Database
CREATE DATABASE matchmakers;
GRANT ALL PRIVILEGES ON DATABASE matchmakers TO matchmakers;

-- Schema
\connect matchmakers;
CREATE SCHEMA matchmakers AUTHORIZATION matchmakers;
