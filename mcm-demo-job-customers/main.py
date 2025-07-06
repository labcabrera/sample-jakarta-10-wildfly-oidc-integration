import psycopg2
import os
import uuid
from confluent_kafka import Producer
from confluent_kafka.serialization import SerializationContext, MessageField, StringSerializer
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroSerializer

DB_CONFIG = {
    'dbname': os.getenv("APP_DB_SCHEMA", "mcmdemo"),
    'user': os.getenv("APP_DB_USERNAME", "user"),
    'password': os.getenv("APP_DB_PASSWORD", "changeit"),
    'host': os.getenv("APP_DB_HOST", "localhost"),
    'port': os.getenv("APP_DB_PORT", 5432)
}

KAFKA_BROKER = os.getenv("APP_KAFKA_BROKER", "192.168.49.2:31101")
KAFKA_TOPIC = os.getenv("APP_KAFKA_TOPIC_CUSTOMER_CREATED", "customers-created-topic")
SCHEMA_REGISTRY_URL = os.getenv("SCHEMA_REGISTRY_URL", "http://schema-registry.local")

KAFKA_SASL_USERNAME = os.getenv("APP_KAFKA_USERNAME", "user1")
KAFKA_SASL_PASSWORD = os.getenv("APP_KAFKA_PASSWORD", "gWXJezKXhm")
KAFKA_SECURITY_PROTOCOL = os.getenv("APP_KAFKA_SECURITY_PROTOCOL", "SASL_PLAINTEXT")
KAFKA_SASL_MECHANISM = os.getenv("APP_KAFKA_SASL_MECHANISM", "PLAIN")

conn = psycopg2.connect(**DB_CONFIG)
cursor = conn.cursor()

cursor.execute("SELECT id, email FROM customers WHERE status = 'PENDING_ACTIVATION'")
rows = cursor.fetchall()

producer_config = {
    'bootstrap.servers': KAFKA_BROKER,
    'security.protocol': KAFKA_SECURITY_PROTOCOL,
    'sasl.mechanism': KAFKA_SASL_MECHANISM,
    'sasl.username': KAFKA_SASL_USERNAME,
    'sasl.password': KAFKA_SASL_PASSWORD
}

schema_registry_conf = {'url': SCHEMA_REGISTRY_URL}
schema_registry_client = SchemaRegistryClient(schema_registry_conf)

with open('./schemas/customer-created-event.avsc', 'r') as f:
    value_schema_str = f.read()

string_serializer = StringSerializer('utf_8')  # Para la key
avro_serializer = AvroSerializer(schema_registry_client, value_schema_str)  # Para el value

producer = Producer(producer_config)

def delivery_report(err, msg):
    if err is not None:
        print(f'❌ Message delivery failed: {err}')
    else:
        print(f'✅ Message delivered to {msg.topic()} [{msg.partition()}] @ {msg.offset()}')

for row in rows:
    message_data = {
        'customerId': row[0],
        'email': row[1],
        'eventId': str(uuid.uuid4()),
        'eventVersion': '1.0'
    }

    headers = {
        'eventType': 'CustomerCreated',
        'eventVersion': '1.0',
        'source': 'mcm-demo-job-customers'
    }

    print(f"Sending message: {message_data}")

    try:
        serialized_key = string_serializer(
            row[0], 
            SerializationContext(KAFKA_TOPIC, MessageField.KEY)
        )
        serialized_value = avro_serializer(
            message_data, 
            SerializationContext(KAFKA_TOPIC, MessageField.VALUE)
        )
        producer.produce(
            topic=KAFKA_TOPIC,
            key=serialized_key,
            value=serialized_value,
            headers=headers,
            callback=delivery_report
        )
        print(f"✅ Message queued for sending")
    except Exception as e:
        print(f"❌ Error processing message: {e}")
        continue

producer.flush()

cursor.close()
conn.close()
print("✅ All messages sent successfully!")