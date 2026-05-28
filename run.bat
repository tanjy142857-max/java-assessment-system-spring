@echo off
title Assessment Feedback System (Spring Boot)
cd /d "%~dp0"
echo.
echo ============================================
echo   Assessment Feedback System - Spring Boot
echo ============================================
echo.
echo Starting server... Please wait.
echo Once started, open: http://localhost:8080
echo Default login: admin / admin123
echo.
echo Press Ctrl+C to stop the server.
echo ============================================
echo.
call mvn spring-boot:run
pause
