all: up

start: DOCKER_OPTIONS=--detach
start: up

stop: down

bootstrap:
	@mkdir -p ./dub-data/

up: bootstrap
	@echo "> Starting docker evironment..."
	@export CURRENT_UID=$(shell stat -c "%u:%g" .); docker-compose up $(DOCKER_OPTIONS)

down:
	@echo "> Stopping docker evironment..."
	@export CURRENT_UID=$(shell stat -c "%u:%g" .); docker-compose down

restart:
	@echo "> Restarting docker evironment..."
	@export CURRENT_UID=$(shell stat -c "%u:%g" .); docker-compose restart

build:
	@echo "> Building docker evironment in parallel..."
	@docker-compose build --parallel

logging:
	@docker-compose logs -f

status:
	@docker-compose ps --all

clean:
	@echo "> Cleaning database..."
	@rm -rf ./db-data/ ./dub-data/
