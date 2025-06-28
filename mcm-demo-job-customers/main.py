import psycopg2
from kafka import KafkaProducer
import json
import os
# from dotenv import load_dotenv

# load_dotenv("config.env")

DB_CONFIG = {
    'dbname': os.getenv("PG_DB", "mcmdemo"),
    'user': os.getenv("PG_USER", "user"),
    'password': os.getenv("PG_PASS", "changeit"),
    'host': os.getenv("PG_HOST", "localhost"),
    'port': os.getenv("PG_PORT", 5432)
}

KAFKA_BROKER = 'TODO-read-environment'
KAFKA_TOPIC = 'TODO-read-environment'

conn = psycopg2.connect(**DB_CONFIG)
cursor = conn.cursor()

cursor.execute("SELECT id, firstname, lastName, email FROM customers WHERE status = 'pending-activation'")
rows = cursor.fetchall()

producer = KafkaProducer(
    bootstrap_servers=KAFKA_BROKER,
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

for row in rows:
    mensaje = {
        'id': row[0],
        'nombre': row[1],
        'email': row[3]
    }
    print(f"Enviando mensaje a Kafka: {mensaje}")
    producer.send(KAFKA_TOPIC, mensaje)

producer.flush()
cursor.close()
conn.close()
