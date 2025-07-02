from confluent_kafka import Producer
from confluent_kafka.serialization import SerializationContext, MessageField
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroSerializer
import json
import os
from typing import Dict, Any


class ConfluentAvroSerializer:
    def __init__(self, schema_registry_url: str, schema_file_path: str):
        """
        Initialize Confluent Avro serializer with Schema Registry.
        
        Args:
            schema_registry_url: URL of the Schema Registry
            schema_file_path: Path to the .avsc schema file
        """
        self.schema_registry_client = SchemaRegistryClient({'url': schema_registry_url})
        
        # Load schema from file
        with open(schema_file_path, 'r') as schema_file:
            self.schema_str = schema_file.read()
        
        # Create Avro serializer
        self.avro_serializer = AvroSerializer(
            self.schema_registry_client,
            self.schema_str
        )
    
    def serialize(self, topic: str, data: Dict[str, Any]) -> bytes:
        """
        Serialize data using Confluent Avro serializer.
        
        Args:
            topic: Kafka topic name
            data: Data to serialize
            
        Returns:
            Serialized data as bytes
        """
        ctx = SerializationContext(topic, MessageField.VALUE)
        return self.avro_serializer(data, ctx)


def create_confluent_customer_serializer(schema_registry_url: str) -> ConfluentAvroSerializer:
    """Create Confluent customer event serializer."""
    schema_path = os.path.join(
        os.path.dirname(__file__), 
        'schemas', 
        'customer-created-event.avsc'
    )
    return ConfluentAvroSerializer(schema_registry_url, schema_path)
