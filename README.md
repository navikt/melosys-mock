# Melosys-mock

Mock-applikasjon for melosys sine avhengigheter, og samlerepo for instruksjoner for å kjøre opp melosys på lokal maskin. 
Leverer i hovedsak statisk filer


## Kafka

For å kjøre opp kafka med zookeeper kan følgende kommando brukes: 
```shell script
docker-compose -f docker-compose-kafka.yml up
```
Nye topics kan enkelt legges til i `docker-compose-kafka.yml`
