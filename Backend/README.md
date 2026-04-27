# CSC8019 Coffee Shop Project

## Overview
Multi-station coffee ordering system with a Vue frontend and Spring Boot backend sharing one database.

## Repository Structure
- Backend/: Java Spring Boot API, database access, and tests
- Frontend/: Vue 3 + Vite client application
- workflows/: automation scripts and workflow assets

## SQL Setup
Use scripts in Backend/sql scripts/:
- coffee_shop.sql: creates tables
- getallorders.sql: reads all orders
- getOrderById.sql: reads order by ID
- inprogress_orders.sql: procedure for in-progress orders
- Ready_orders_view.sql: view for ready orders

## Backend Flow
Frontend -> DTO -> Controller -> Service -> Repository -> Database

## Development Workflow
1. Branch from develop for feature work.
2. Keep main protected and merge via pull requests.
3. Use clear commit messages.
