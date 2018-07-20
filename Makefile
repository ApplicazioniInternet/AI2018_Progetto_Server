default:
	docker-compose up
clean:
	docker volume prune --force
	docker image prune -a --force
	docker system prune --force
