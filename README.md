# Peach
event driven microservice

## delivery backoffice workflow
A Postman collection is available under the `doc` directory.
1. Request Delivery Order (`POST /delivery`)
2. Request Delivery Preparing (`PUT /delivery/{deliverId}/prepare`)
   - delivery state change event published
3. Request Delivery Packaging (`PUT /delivery/{deliverId}/package`)
   - delivery state change event published
4. Request Delivery Shipping (`PUT /delivery/{deliverId}/ship`)
   - delivery state change event published
5. Request Delivery Complete (temporary fix) (`PUT /delivery/{deliverId}/complete`)
   - delivery state change event published

## kafka local config
```
docker-compose -d up

docker-compose exec broker \                                                                                                                                  ✘ INT 1h 36m 23s
kafka-console-consumer --bootstrap-server localhost:29092 \
--topic delivery.status.change --from-beginning
```