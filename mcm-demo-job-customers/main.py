import psycopg2
from kafka import KafkaProducer
import json
import os
import time
import uuid
from src.avro_utils import create_customer_serializer
# from dotenv import load_dotenv

# load_dotenv("config.env")

DB_CONFIG = {
    'dbname': os.getenv("APP_DB_SCHEMA", "mcmdemo"),
    'user': os.getenv("APP_DB_USERNAME", "user"),
    'password': os.getenv("APP_DB_PASSWORD", "changeit"),
    'host': os.getenv("APP_DB_HOST", "localhost"),
    'port': os.getenv("APP_DB_PORT", 5432)
}

KAFKA_BROKER = os.getenv("APP_KAFKA_BROKER", "192.168.49.2:31101")
KAFKA_TOPIC = os.getenv("APP_KAFKA_TOPIC_CUSTOMER_CREATED", "customers-created-topic")

KAFKA_SASL_USERNAME = os.getenv("APP_KAFKA_USERNAME", "user1")
KAFKA_SASL_PASSWORD = os.getenv("APP_KAFKA_PASSWORD", "gWXJezKXhm")
KAFKA_SECURITY_PROTOCOL = os.getenv("APP_KAFKA_SECURITY_PROTOCOL", "SASL_PLAINTEXT")
KAFKA_SASL_MECHANISM = os.getenv("APP_KAFKA_SASL_MECHANISM", "PLAIN")

# Initialize Avro serializer
avro_serializer = create_customer_serializer()

conn = psycopg2.connect(**DB_CONFIG)
cursor = conn.cursor()

cursor.execute("SELECT id, email FROM customers WHERE status = 'PENDING_ACTIVATION'")
rows = cursor.fetchall()

producer_config = {
    'bootstrap_servers': KAFKA_BROKER,
    'value_serializer': lambda v: v if isinstance(v, bytes) else json.dumps(v).encode('utf-8')
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
    # Create the message data
    message_data = {
        'customerId': row[0],
        'email': row[1],
        'eventId': str(uuid.uuid4()),
        'eventVersion': '1.0'
    }
    headers = [
        ('eventType', b'CustomerCreated'),
        ('eventVersion', b'1.0'),
        ('source', b'mcm-demo-job-customers'),
        ('schemaId', b'customers-created-topic-value'),
        ('schemaVersion', b'1.0')
    ]

    print(f"Sending message: {message_data}")

    # Serialize using Avro
    serialized_message = avro_serializer.serialize(message_data)

    print(f"Serialized message: {serialized_message}")
    
    producer.send(
        KAFKA_TOPIC,
        # key=str(uuid.uuid4()),
        value=serialized_message,
        headers=headers
    )

producer.flush()
cursor.close()
conn.close()
