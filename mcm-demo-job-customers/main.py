import psycopg2
from kafka import KafkaProducer
import json
import os
# from dotenv import load_dotenv

# load_dotenv("config.env")

DB_CONFIG = {
    'dbname': os.getenv("APP_DB_SCHEMA", "mcmdemo"),
    'user': os.getenv("APP_DB_USERNAME", "user"),
    'password': os.getenv("APP_DB_PASSWORD", "changeit"),
    'host': os.getenv("APP_DB_HOST", "localhost"),
    'port': os.getenv("APP_DB_PORT", 5432)
}

KAFKA_BROKER = os.getenv("APP_KAFKA_BROKER", "192.168.49.2:31108")
KAFKA_TOPIC = os.getenv("APP_KAFKA_TOPIC_CUSTOMER_CREATED", "customers-created-topic")

KAFKA_SASL_USERNAME = os.getenv("APP_KAFKA_USERNAME", "user1")
KAFKA_SASL_PASSWORD = os.getenv("APP_KAFKA_PASSWORD", "879P1qgPkc")
KAFKA_SECURITY_PROTOCOL = os.getenv("APP_KAFKA_SECURITY_PROTOCOL", "SASL_PLAINTEXT")
KAFKA_SASL_MECHANISM = os.getenv("APP_KAFKA_SASL_MECHANISM", "PLAIN")

conn = psycopg2.connect(**DB_CONFIG)
cursor = conn.cursor()

cursor.execute("SELECT id, email FROM customers WHERE status = 'PENDING_ACTIVATION'")
rows = cursor.fetchall()

producer_config = {
    'bootstrap_servers': KAFKA_BROKER,
    'value_serializer': lambda v: json.dumps(v).encode('utf-8')
}

if KAFKA_SASL_USERNAME and KAFKA_SASL_PASSWORD:
    producer_config.update({
        'security_protocol': KAFKA_SECURITY_PROTOCOL,
        'sasl_mechanism': KAFKA_SASL_MECHANISM,
        'sasl_plain_username': KAFKA_SASL_USERNAME,
        'sasl_plain_password': KAFKA_SASL_PASSWORD
    })

producer = KafkaProducer(**producer_config)

for row in rows:
    mensaje = {
        'customerId': row[0],
        'email': row[1]
    }
    print(f"Sending message: {mensaje}")
    producer.send(KAFKA_TOPIC, mensaje)

producer.flush()
cursor.close()
conn.close()
