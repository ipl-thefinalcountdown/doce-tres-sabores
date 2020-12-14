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
	@export CURRENT_UID=$(shell stat -c "%u:%g" .); docker-compose restart $(DOCKER_TARGET)

restart-frontend: DOCKER_TARGET=frontend
restart-frontend: restart

restart-wildfly: DOCKER_TARGET=wildfly
restart-wildfly: restart

watch-frontend:
	@echo "Watching frontend webpack resources..."
	@sh -c "(cd frontend; npm run watch)"

build:
	@echo "> Building docker evironment in parallel..."
	@docker-compose build --parallel

deploy:
	@echo "> Deploy war to wildfly..."
	@mvn wildfly:deploy

logging:
	@docker-compose logs -f --tail=1000

status:
	@docker-compose ps --all

clean:
	@echo "> Cleaning database..."
	@rm -rf ./db-data/ ./dub-data/
